package com.activity.helperstructures;

public class TestObj {
	public String name ;
	public Integer value;
	
	@Override
	public boolean equals(Object obj) {
		return this.name.equals(((TestObj)obj).name);
	}
	
	@Override
	public int hashCode() {
		return this.name.hashCode();
	}
	
	public TestObj(String name, int value){
		this.name = name;
		this.value = value;
	}
	
	public String toString() {
		return this.name + "(" + this.value + ")";
	}
}
