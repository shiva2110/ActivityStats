package com.activity.index;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.NumericField;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.search.NumericRangeQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.MMapDirectory;
import org.apache.lucene.util.Version;

import com.activity.search.DefaultSearcher;
import com.activity.search.SearchResult;
import com.activity.search.Searcher;
import com.activity.tokenize.Tokenizer;

public class MMapIndexer implements Indexer{

	private static IndexWriter MMapIndexWriter;
	private static final String FSdir = "/projects/activity/data/index/";		
	private static final Long timeBefore = new Long(3600);
	private Long resetInterval;
	
	public class IndexResetter implements Runnable {
		private Long resetInterval;		
		private Long timeBefore;
		public void setResetInterval(Long resetInterval) {
			this.resetInterval = resetInterval;
		}
		public void setTimeBefore(long timeBefore){
			this.timeBefore = timeBefore;
		}
		
		public void run() {
			while(true){
				try {
					Thread.sleep(resetInterval);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				purgeOutDatedIndice(timeBefore);
			}
		}
		
	}
	
	public void setResetInterval(long resetInterval){
		if(this.resetInterval==null){
			this.resetInterval = resetInterval;
			IndexResetter indexResetter = new IndexResetter();
			indexResetter.setResetInterval(resetInterval);
			indexResetter.setTimeBefore(timeBefore);
			
			Thread resetThread = new Thread(indexResetter); 
			resetThread.start();
		}
		
	}
	
	private void purgeOutDatedIndice(long timeBefore) {
		Date date = new Date();
		Long curTime = date.getTime()/1000;
		Long minRange = curTime - timeBefore;
		Query q = NumericRangeQuery.newLongRange(FieldNames.INDEX_TIME, minRange, null, true, false);
		
		try {
			getMMapIndexWriter().deleteDocuments(q);
			getMMapIndexWriter().commit();
		} catch (CorruptIndexException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	private Directory getMMapDir() {
		return MMapIndexer.getMMapIndexWriter().getDirectory();
	}
	
	@SuppressWarnings("deprecation")
	private static synchronized IndexWriter getMMapIndexWriter() {

		try {
			if (MMapIndexWriter == null) {
				MMapIndexWriter = new IndexWriter(
						new MMapDirectory(new File(FSdir)),
						new StandardAnalyzer(Version.LUCENE_36,
								new HashSet<String>()),
						IndexWriter.MaxFieldLength.UNLIMITED);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return MMapIndexWriter;
	}
	public void index(Activity activity) {
		//build doc
		Document doc = new Document();
		StringBuffer sb = new StringBuffer();
		for(String term : activity.processedTokens) {
			sb.append(term).append(" ");
		}
		
		doc.add(new Field(FieldNames.PROCESSED_TOKEN_FIELD, sb.toString(), Field.Store.YES, Field.Index.ANALYZED_NO_NORMS));
		doc.add(new Field(FieldNames.ACTUAL_TOKEN_FIELD, activity.sentence, Field.Store.YES, Field.Index.NO));
		
		//Current index time stored in seconds
		Date date = new Date();
		NumericField dateField = new NumericField(FieldNames.INDEX_TIME);
		dateField.setLongValue(date.getTime()/1000);
		doc.add(dateField);
		
		try {
			MMapIndexer.getMMapIndexWriter().addDocument(doc);
			MMapIndexer.getMMapIndexWriter().commit();
		} catch (CorruptIndexException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	public SearchResult search(Activity activity) {
		List<Directory> indexDirs = new ArrayList<Directory>();
		indexDirs.add(getMMapDir());
		
		Searcher searcher = new DefaultSearcher();
		return searcher.booleanLatestSearch(activity, indexDirs, timeBefore);
	}
	
	public List<Directory> getAllIndexDirs(){
		List<Directory> indexDirs = new ArrayList<Directory>();
		indexDirs.add(getMMapDir());
		return indexDirs;
	}

}
