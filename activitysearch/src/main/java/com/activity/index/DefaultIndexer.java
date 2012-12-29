package com.activity.index;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;

import com.activity.search.DefaultSearcher;
import com.activity.search.SearchResult;
import com.activity.search.Searcher;

public class DefaultIndexer implements Indexer {

	private static IndexWriter RAMIndexWriter;
	private static RAMDirectory RAMdir = new RAMDirectory();

	private static IndexWriter FSIndexWriter;
	private static final String FSdir = "/projects/activity/data/index/";

	private static final long RAMIndexBufferMax = 16000000; // 16MB
	private static final long RAMIndexDocsMax = 4; //4 docs
	
	

	public static Directory getRAMdir() {
		return getRAMIndexWriter().getDirectory();
	}
	
	

	public static Directory getFsdir() {
		return getFSIndexWriter().getDirectory();
	}



	@SuppressWarnings("deprecation")
	private static synchronized IndexWriter getRAMIndexWriter() {

		try {
			if (RAMIndexWriter == null) {
				RAMIndexWriter = new IndexWriter(RAMdir, new StandardAnalyzer(
						Version.LUCENE_36, new HashSet<String>()),
						IndexWriter.MaxFieldLength.UNLIMITED);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return RAMIndexWriter;
	}

	@SuppressWarnings("deprecation")
	private static synchronized IndexWriter getFSIndexWriter() {

		try {
			if (FSIndexWriter == null) {
				FSIndexWriter = new IndexWriter(
						FSDirectory.open(new File(FSdir)),
						new StandardAnalyzer(Version.LUCENE_36,
								new HashSet<String>()),
						IndexWriter.MaxFieldLength.UNLIMITED);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return FSIndexWriter;
	}

	/**
	 * Processes activity sentence and adds to an in-memory based index. The in-memory based index is accessible for search.
	 * The in-memory index is flushed to file-system based index after the buffer reaches a MAX size.
	 */
	public void index(Activity activity) {
			
			//build doc
		Document doc = new Document();
		StringBuffer sb = new StringBuffer();
		for(String term : activity.processedTokens) {
			sb.append(term).append(" ");
		}
		
		doc.add(new Field("activity", sb.toString(), Field.Store.YES, Field.Index.ANALYZED_NO_NORMS));
		DefaultIndexer.copyRAMIndextoFS();		
		try {
			
			DefaultIndexer.getRAMIndexWriter().addDocument(doc);
			RAMIndexWriter.commit();
		} catch (CorruptIndexException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static synchronized void copyRAMIndextoFS() {
		try {
			if (RAMdir.sizeInBytes() >= RAMIndexBufferMax) {
				getFSIndexWriter().addIndexes(new Directory[] { RAMdir });
				getFSIndexWriter().commit();
				
				getRAMIndexWriter().close();
				RAMIndexWriter = null;
			}
		} catch (IOException e) {

		}
	}
	
	public SearchResult search(Activity activity) {
		
		List<Directory> indexDirs = new ArrayList<Directory>();
		indexDirs.add(getRAMdir());
		indexDirs.add(getFsdir());
		
		Searcher searcher = new DefaultSearcher();
		return searcher.booleanSearch(activity, indexDirs);
	}



	public List<Directory> getAllIndexDirs() {
		// TODO Auto-generated method stub
		return null;
	}

}
