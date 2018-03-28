public class Step_info implements Comparable<Step_info>{
	private double d,x,y,e;
	private int id;
	private String Path;
	
	Step_info(double distance, double posx, double posy, int ide,double evaluation,String s){
		d = distance;
		x = posx;
		y = posy;
		e = evaluation;
		id = ide;
		Path = s;
	}
	
	public double get_eval(){
		return d+0.8*e;
	}
	
	public double get_e(){
		return e;
	}
	
	public int get_id(){
		return id;
	}
	
	public double get_x(){
		return x;
	}
	
	public double get_y(){
		return y;
	}

	public double get_d(){
		return d;
	}
	
	public String get_Path(){
		return new String(Path);
	}

	@Override
	public int compareTo(Step_info o) {
		if(this.get_eval()<o.get_eval()){
			return 1;
		}
		else {
			return -1;
		}
	}
}
