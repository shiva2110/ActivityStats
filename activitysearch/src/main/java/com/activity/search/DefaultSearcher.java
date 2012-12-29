package com.activity.search;

import java.io.IOException;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.Filter;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.NumericRangeFilter;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.util.Version;

import com.activity.index.Activity;
import com.activity.index.FieldNames;

public class DefaultSearcher implements Searcher{
	


	/**
	 * Performs a boolean search on only latest indices.
	 * 
	 * @param activity
	 * @param indexDirs
	 * @param timeBefore - Specifies in seconds, how far in time before the current system time that this method is allowed to search the indices.
	 * @return
	 */
	public SearchResult booleanLatestSearch(Activity activity,
			List<Directory> indexDirs, long timeBefore) {
		
		SearchResult searchResult = new SearchResult();
		
		StringBuffer sb = new StringBuffer();
		
		for(int i=0; i<activity.processedTokens.size()-1; i++) {
			sb.append(activity.processedTokens.get(i)).append(" AND ");
		}
		
		sb.append(activity.processedTokens.get(activity.processedTokens.size()-1));
			
		for(Directory dir : indexDirs) {
			try {
				IndexSearcher searcher = new IndexSearcher(IndexReader.open(dir));
				QueryParser queryParser = new QueryParser(Version.LUCENE_36, FieldNames.PROCESSED_TOKEN_FIELD, new StandardAnalyzer(Version.LUCENE_36, new HashSet<String>()));
				Query q = queryParser.parse(sb.toString());				
		
				Date date = new Date();
				Long curTime = date.getTime()/1000;
				Long minRange = curTime - timeBefore;
				Filter filter = NumericRangeFilter.newLongRange(FieldNames.INDEX_TIME, minRange, null, true, false);
				TopDocs docs = searcher.search(q, filter, Integer.MAX_VALUE);
				searchResult.setTotalHits(searchResult.getTotalHits() + docs.totalHits);
				searcher.close();
			} catch (CorruptIndexException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return searchResult;
	}

	public SearchResult vectorModelSearch(Activity activity,
			List<Directory> indexDirs) {
		// TODO Auto-generated method stub
		return null;
	}

	public SearchResult booleanSearch(Activity activity,
			List<Directory> indexDirs) {
		// TODO Auto-generated method stub
		return null;
	}

}
