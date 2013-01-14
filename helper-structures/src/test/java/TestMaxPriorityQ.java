import org.junit.BeforeClass;
import org.junit.Test;

import com.activity.helperstructures.MaxPriorityQ;


public class TestMaxPriorityQ {

	@Test
	public void testInsert() {
		MaxPriorityQ<SampleObj> Q = new MaxPriorityQ<SampleObj>(new SampleObjComparator());
		System.out.println("Inserting A(10)");
		Q.insert(new SampleObj("A", 10));
		Q.print();
		System.out.println();
		
		System.out.println("Inserting B(12) & C(19)");
		Q.insert(new SampleObj("B", 12));
		Q.insert(new SampleObj("C", 19));
		Q.print();
		System.out.println();
		
		System.out.println("Increasing A with value 24");
		Q.insert(new SampleObj("A", 24));
		Q.print();
		System.out.println();
		
		System.out.println("Inserting D(24), E(26), F(13), G(10)");
		Q.insert(new SampleObj("D", 24));
		Q.insert(new SampleObj("E", 26));
		Q.insert(new SampleObj("F", 13));
		Q.insert(new SampleObj("G", 10));
		Q.print();
		System.out.println();
		
		System.out.println("Decreasing value of E to 2");
		Q.insert(new SampleObj("E", 2));
		Q.print();
		System.out.println();
		
		System.out.println("Decreasing value of F to 11");
		Q.insert(new SampleObj("F", 11));
		Q.print();
		System.out.println();
	}
}
