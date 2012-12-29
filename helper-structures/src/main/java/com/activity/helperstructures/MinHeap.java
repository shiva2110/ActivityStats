package com.activity.helperstructures;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


/**
 * This class provides maintains a MinHeap.
 * It provides access to insert(), delete() and getTopK() methods and this class is synchronized.
 * @author shiva2110
 *
 * @param <T>
 */
public class MinHeap<T> {
	final List<T> heapList = new ArrayList<T>();
	final int maxLimit; //max K to maintain in the heap.
	final Comparator<T> comparator; //comparator to compare objects in heap.

	
	public MinHeap(Comparator<T> comparator, int maxLimit) {
		this.comparator = comparator;
		this.maxLimit = maxLimit;
	}

	private int getLeft(int pos) {
		return (pos*2)+1;
	}
	
	private int getRight(int pos) {
		return (pos*2)+2;
	}
	
	private int getParent(int pos) {
		return (int)Math.ceil((pos/2)-1);		
	}
	
	public synchronized void insert(T object) {

		if(heapList.size()<maxLimit) {			
			int index;
			if((index=findIndex(object))!=-1){
				heapList.set(index, object);
			} else {
				heapList.add(object);
			}
						
			if(heapList.size()==maxLimit) {
				int curPos = getParent(heapList.size()-1);
				while(curPos>=0){
					heapify(curPos);
					curPos--;
				}
			}			
			return;
		}
		
		if(comparator.compare(object, heapList.get(0))>0) {			
			int index;
			if((index=findIndex(object))!=-1 &&
					comparator.compare(object, heapList.get(index))>0){
				heapList.set(index, object);
				heapify(index);
			} else {
				heapList.set(0, object);
				heapify(0);
			}			
		}
	}
	
	public synchronized List<T> getTopK() {
		List<T> topKList = new ArrayList<T>();
		topKList.addAll(heapList);
		return topKList;
	}
	
	/**
	 * heapify down to maintain the heap property
	 * @param curPos
	 */
	private void heapify(int curPos) {
		
			int min = curPos;
			
			if(curPos > getParent(heapList.size()-1)) {
				return;
			}
			
			T left = heapList.get(getLeft(curPos));
			T right = heapList.get(getRight(curPos));
			
			if(comparator.compare(left, heapList.get(min))<=0){
				min = getLeft(curPos);
			}
			
			if(comparator.compare(right, heapList.get(min))<=0){
				min = getRight(curPos);
			}
			
			T temp = heapList.get(min);
			heapList.set(min, heapList.get(curPos));
			heapList.set(curPos, temp);
			
			if(min!=curPos){
				heapify(min);
			}			
	}
	
	public synchronized void delete(T node){
		int index = findIndex(node);
		if(index==-1) {
			return;
		}
		
		heapList.set(index, heapList.get(heapList.size()-1));
		heapList.remove(heapList.size()-1);
		heapify(index);
	}
	
	//can be optimized with hashtable
	private int findIndex(T node){
		for(int i=0; i<heapList.size(); i++) {
			if(heapList.get(i).equals(node)){
				return i;
			}
		}
		return -1;
	}
	
	public void print() {
		List<Integer> levelNodes = new ArrayList<Integer>();
		levelNodes.add(0);
		print(levelNodes);
	}
	
	private void print(List<Integer> levelNodes) {
		if(levelNodes.size()==0){
			return;
		}
		
		List<Integer> nextLevel = new ArrayList<Integer>();
		StringBuffer sb = new StringBuffer();
 		for(int index: levelNodes) {
 			
 			sb.append(heapList.get(index).toString() + "-->");
 			int left = getLeft(index);
 			if(left<heapList.size()) {
 				nextLevel.add(left);
 			}
 			
 			int right = getRight(index);
 			if(right<heapList.size()) {
 				nextLevel.add(right);
 			}
 		}
 		
 		System.out.println(sb.toString());
 		print(nextLevel);
	}
}
