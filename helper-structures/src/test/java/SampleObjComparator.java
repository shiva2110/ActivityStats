import java.util.Comparator;


public class SampleObjComparator implements Comparator<SampleObj>{

	public int compare(SampleObj o1, SampleObj o2) {
		if( (o1).value < (o2).value ){
			return -1;
		} else if( (o1).value > (o2).value ){
			return 1;
		} else {
			return 0;
		}
	}

}
