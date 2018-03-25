
public class Row implements Comparable<Row>{
	public Double x,y;
	public int id;
	public String Name;
	
	Row(){
		Name = new String();
	}

	@Override
	public int compareTo(Row o) {
		if(x>o.x){
			return 1;
		}
		else if(x<o.x){
			return -1;
		}
		else if(y>o.y){
			return 1;
		}
		else if(y<o.y){
			return -1;
		}
		else {
			return 0;
		}
	}
}
