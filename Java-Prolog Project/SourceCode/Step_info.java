public class Step_info implements Comparable<Step_info>{
	private double d,e;
	private long id;
	private String Path;
	private boolean Lit, Toll;
	
	Step_info(double distance, long ide, double evaluation, String s, boolean light, boolean tl){
		d = distance;
		e = evaluation;
		id = ide;
		Path = s;
		Lit = light;
		Toll = tl;
	}
	
	public double get_eval(){
		return d+e;
	}
	
	public double get_e(){
		return e;
	}
	
	public long get_id(){
		return id;
	}
	
	public double get_d(){
		return d;
	}
	
	public String get_Path(){
		return new String(Path);
	}
	
	public boolean get_lit(){
		return Lit;
	}
	
	public boolean get_toll(){
		return Toll;
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
