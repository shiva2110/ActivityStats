package com.activity.search;

import java.util.List;

import org.apache.lucene.store.Directory;

import com.activity.index.Activity;

public interface Searcher {
	
	public SearchResult booleanSearch(Activity activity, List<Directory> indexDirs);
	public SearchResult vectorModelSearch(Activity activity, List<Directory> indexDirs);
	public SearchResult booleanLatestSearch(Activity activity,	List<Directory> indexDirs, long timeBefore);
}
