package com.activity.trends;
import java.util.Comparator;

import com.activity.index.Activity;

public class ActivityTrendsComparator<T> implements Comparator<T>{

	public enum SortOrder {
		ASC, DESC;
	}
	
	private SortOrder sortOrder = SortOrder.ASC;
	
	
	public void setSortOrder(SortOrder sortOrder) {
		this.sortOrder = sortOrder;
	}


	public int compare(T arg0, T arg1) {
		Activity object1 = (Activity)arg0;
		Activity object2 = (Activity)arg1;
		if(sortOrder.equals(SortOrder.ASC)){
			return Integer.compare(object1.hits, object2.hits);		
		} else{
			return Integer.compare(object2.hits,object1.hits);		
		}

	}

}
