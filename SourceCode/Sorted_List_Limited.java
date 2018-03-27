import java.util.*;

public class Sorted_List_Limited{

	private Vector<Step_info> The;
	private int size;

	Sorted_List_Limited(int siz){
		size = siz;
		The = new Vector<Step_info>();
	}

	public boolean push(Step_info step){

		if(The.size()==0){
			The.addElement(step);
			return true;
		}
		int curr,left,right;
		left = 0;
		right = The.size()-1;

		curr = (left+right)/2;
		while(left<=right){
			if(step.get_eval()>The.get(curr).get_eval()){
				left = curr+1;
				curr = (left+right)/2;
			}
			else {
				right = curr-1;
				curr = (left+right)/2;
			}
		}
		if(step.get_eval()>The.get(curr).get_eval()){
			if(curr+1==size){
				return false;
			}
			The.add(curr+1,step);
		}
		else {
			if(curr==size){
				return false;
			}
			The.add(curr,step);
		}

		while(The.size()>size){
			The.removeElementAt(size);
		}
		return true;
	}

	public Step_info top(){
		return The.get(0);
	}

	public Step_info pop(){
		return The.remove(0);
	}
	
	public boolean empty(){
		return (The.size()==0);
	}

	public int size(){
		return The.size();
	}
}
