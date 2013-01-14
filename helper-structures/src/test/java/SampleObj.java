
public class SampleObj {
	public String name;
	public int value;
	
	public SampleObj(String name, int value){
		this.name = name;
		this.value = value;
	}
	
	@Override
	public boolean equals(Object obj){
		if(obj==null || !this.getClass().equals(obj.getClass())) {
			return false;
		}
		
		return this.name.equals(
				((SampleObj)obj).name);
	}
	
	
	public String toString(){
		return name + "(" + value + ")";
	}
}
