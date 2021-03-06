
package com.activity.helperstructures;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class MinPriorityQ<T> {
	List<T> heapList = new ArrayList<T>();
	Comparator<T> comparator; // comparator to compare objects in heap.

	public MinPriorityQ(Comparator<T> comparator) {
		this.comparator = comparator;
	}

	public int getLeft(int pos) {
		return (pos * 2) + 1;
	}

	public int getRight(int pos) {
		return (pos * 2) + 2;
	}

	public int getParent(int pos) {
		return (int) Math.ceil((pos / 2.0) - 1);
	}

	public synchronized void insert(T object) {

		int index;
		
		if((index=findIndex(object))!=-1){
			if(comparator.compare(object, heapList.get(index))>0) {
				heapList.set(index, object);
				heapify(index);
			} else {
				heapList.set(index, object);
				heapifyUp(index);
			}			
		} else {
			heapList.add(object);
			heapifyUp(heapList.size()-1);
		}
	}

	public synchronized List<T> getTopK(int K) {
		List<T> topKList = new ArrayList<T>();
		while(K>0){
			try {
				topKList.add(extractMIN());
			} catch(Exception e){
				break;
			}
			
			K--;
		}
		
		for(T obj: topKList){
			insert(obj);
		}
		return topKList;
	}
	
	private T extractMIN() {
		T obj = heapList.get(0);
		heapList.set(0, heapList.get(heapList.size()-1));
		heapList.remove(heapList.size()-1);
		heapify(0);
		
		return obj;
	}
	
	private void heapifyUp(int curPos){
		if(curPos>=heapList.size() || curPos<=0){
			return;
		}
		
		try {
			int parent = getParent(curPos);
			if (comparator.compare(heapList.get(curPos), heapList.get(parent)) <=0){
				T temp = heapList.get(parent);
				heapList.set(parent, heapList.get(curPos));
				heapList.set(curPos, temp);
				heapifyUp(parent);
			}
		} catch(RuntimeException e){
			System.out.println("cur pos:" + curPos);
			System.out.println("heap size:" + heapList.size());
			throw e;
		}
	
	}

	private void heapify(int curPos) {

		int min = curPos;

		try {
			if (curPos > getParent(heapList.size() - 1)) {
				return;
			}

			T left = heapList.get(getLeft(curPos));
			if (comparator.compare(left, heapList.get(min)) <= 0) {
				min = getLeft(curPos);
			}

			if(!(getRight(curPos)>=heapList.size())){
				T right = heapList.get(getRight(curPos));
				if (comparator.compare(right, heapList.get(min)) <= 0) {
					min = getRight(curPos);
				}
			}	

			T temp = heapList.get(min);
			heapList.set(min, heapList.get(curPos));
			heapList.set(curPos, temp);

			if (min != curPos) {
				heapify(min);
			}
		} catch(RuntimeException e){
			System.out.println("cur pos: " + curPos);
			System.out.println("size: " + heapList.size());
			throw e;
		}
	
	}

	public synchronized void delete(T node) {
		int index = findIndex(node);
		if (index == -1) {
			return;
		}

		heapList.set(index, heapList.get(heapList.size() - 1));
		heapList.remove(heapList.size() - 1);
		if(comparator.compare(heapList.get(index), node)<0) {
			heapifyUp(index);
		} else {
			heapify(index);
		}		
	}

	private int findIndex(T node) {
		for (int i = 0; i < heapList.size(); i++) {
			if (heapList.get(i).equals(node)) {
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
		if (levelNodes.size() == 0) {
			return;
		}

		List<Integer> nextLevel = new ArrayList<Integer>();
		StringBuffer sb = new StringBuffer();
		for (int index : levelNodes) {

			sb.append(heapList.get(index).toString() + "-->");
			int left = getLeft(index);
			if (left < heapList.size()) {
				nextLevel.add(left);
			}

			int right = getRight(index);
			if (right < heapList.size()) {
				nextLevel.add(right);
			}
		}

		System.out.println(sb.toString());
		print(nextLevel);
	}

}
