package com.activity.trends;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.store.Directory;

import com.activity.helperstructures.MaxPriorityQ;
import com.activity.helperstructures.MinPriorityQ;
import com.activity.index.Activity;
import com.activity.index.FieldNames;
import com.activity.index.Indexer;
import com.activity.index.MMapIndexer.IndexResetter;
import com.activity.search.SearchResult;

public class Trends {

		MaxPriorityQ<Activity> popularList = new MaxPriorityQ<Activity>(new ActivityTrendsComparator<Activity>());
		MinPriorityQ<Activity> oddList = new MinPriorityQ<Activity>(new ActivityTrendsComparator<Activity>());
		private Long resetInterval;
		
		public class TrendsResetter implements Runnable {
			private Long resetInterval;		
			private Indexer indexer;
			public void setResetInterval(Long resetInterval) {
				this.resetInterval = resetInterval;
			}
			public void setIndexer(Indexer indexer) {
				this.indexer = indexer;
			}
			
			public void run() {
				while(true){
					try {
						Thread.sleep(resetInterval);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					try {
						reset(indexer);
					} catch (CorruptIndexException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			
		}
		
		public void setResetInterval(long resetInterval, Indexer indexer){
			if(this.resetInterval==null){
				this.resetInterval = resetInterval;
				TrendsResetter trendsResetter = new TrendsResetter();
				trendsResetter.setResetInterval(resetInterval);
				trendsResetter.setIndexer(indexer);
				
				Thread resetThread = new Thread(trendsResetter); 
				resetThread.start();
			}
			
		}
		
		public void add(Activity activity){
			popularList.insert(activity);	
			oddList.insert(activity);
		}
		
		public List<Activity> getMostPopular() {
			List<Activity> filterList = new ArrayList<Activity>();
			List<Activity> topK = popularList.getTopK(5);
			
			for(Activity obj: topK){
				if(obj.hits>0){
					filterList.add(obj);
				}
			}
			
			return filterList;
		}
		
		public List<Activity> getMostOdd() {
			List<Activity> filterList = new ArrayList<Activity>();
			List<Activity> topK = oddList.getTopK(5);
			
			for(Activity obj: topK){
				if(obj.hits>0){
					filterList.add(obj);
				}
			}
			
			return filterList;
		}
		
		/**
		 * reset(), resets the min-heap list from scratch reading from index.
		 * @throws IOException 
		 * @throws CorruptIndexException 
		 */
		public void reset(Indexer indexer) throws CorruptIndexException, IOException {
			popularList = new MaxPriorityQ<Activity>(new ActivityTrendsComparator<Activity>());
			oddList = new MinPriorityQ<Activity>(new ActivityTrendsComparator<Activity>());
			List<Directory> indexDirs = indexer.getAllIndexDirs();
			
			for(Directory dir: indexDirs){
				IndexReader reader = IndexReader.open(dir);
				for(int i=0; i<reader.maxDoc(); i++){
					if(reader.isDeleted(i)){
						continue;
					}
					
					Document doc = reader.document(i);
					Activity activity = new Activity();
					activity.processedTokens = new ArrayList<String>();
					String[] arr = doc.get(FieldNames.PROCESSED_TOKEN_FIELD).split(" ");					
					for(String str : arr) {
						activity.processedTokens.add(str);
					}
					activity.sentence = doc.get(FieldNames.ACTUAL_TOKEN_FIELD);
					
					SearchResult searchResult = indexer.search(activity);
					activity.hits = searchResult.getTotalHits();
					
					if(activity.hits>0){
						popularList.insert(activity);
						oddList.insert(activity);
					}
					
				}
			}			
		}
		
		
}
