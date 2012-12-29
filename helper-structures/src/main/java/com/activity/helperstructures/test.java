package com.activity.helperstructures;

public class test {
	public static void main(String[] args){
		MaxPriorityQ<TestObj> minQ = new MaxPriorityQ<TestObj>(new SampleComparator());
		minQ.insert(new TestObj("A", 15));
		minQ.insert(new TestObj("B", 25));
		minQ.insert(new TestObj("C", 18));
		minQ.insert(new TestObj("D", 10));
		minQ.insert(new TestObj("E", 8));
		minQ.insert(new TestObj("F", 23));
		minQ.insert(new TestObj("G", 32));
		minQ.insert(new TestObj("H", 14));
		minQ.insert(new TestObj("I", 11));
		minQ.insert(new TestObj("J", 20));
		minQ.insert(new TestObj("K", 30));
		
		minQ.print();
		
		minQ.insert(new TestObj("C", 26));
		System.out.println("after increase");
		minQ.print();
		
		minQ.insert(new TestObj("H", 2));
		System.out.println("after decrease");
		minQ.print();
	}
}
