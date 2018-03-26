import java.util.*;

public class Intersection {

	private Vector<Integer> IDs;

	Intersection (){
		IDs = new Vector<Integer>();
	}
	
	public void change(Vector<Integer> id){
		IDs = id;
	}
	
	public int getID(int i){
		if(i>=IDs.size()) 
			return -1;
		
		return IDs.get(i);
	}
	
	public Vector<Integer> getIntersection(){
		return new Vector<Integer>(IDs);
	}
	
}
