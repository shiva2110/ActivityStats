package com.activity.index;

import java.util.List;

import org.apache.lucene.store.Directory;

import com.activity.search.SearchResult;

public interface Indexer {
	public void index(Activity activity);
	public SearchResult search(Activity activity);
	public List<Directory> getAllIndexDirs();
}
