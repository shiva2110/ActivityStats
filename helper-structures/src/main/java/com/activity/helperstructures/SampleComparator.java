package com.activity.helperstructures;
import java.util.Comparator;


public class SampleComparator implements Comparator {

	public int compare(Object object1, Object object2) {
		Integer a = ((TestObj)object1).value;
		Integer b = ((TestObj)object2).value;
		
		return a.compareTo(b);
	}

}
