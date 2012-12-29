package com.activity.index;

import java.util.ArrayList;
import java.util.List;

public class Activity {
	public String sentence;
	public List<String> processedTokens;
	public int hits;
	
	@Override
	public boolean equals(Object obj){
		
		if(obj==null || (obj.getClass()!=this.getClass())) {
			return false;
		}
		
		return sentence.equals(((Activity)obj).sentence);
	}
	
	@Override
	public int hashCode(){
		return sentence.hashCode();
	}

	public String getSentence() {
		return sentence;
	}

	public int getHits() {
		return hits;
	}
	
	

}
