public class Answer {

	public long nextid;
	private int maxspeed;
	public boolean lit,toll;
	public double traffic;
	
	public Answer(String id, String speed, String light, String tl, String tr){
		
		String parts[];
		nextid = (long) Double.parseDouble(id);
		
		if (speed.equals("''")){
			maxspeed = 0;
		}
		else {
			parts = speed.split("'");
			
			maxspeed = Integer.parseInt(parts[1]);
		}
		
		if (light.contains("yes")){
			lit = true;
		}
		else {
			lit = false;
		}
		
		if (tl.contains("yes")){
			toll = true;
		}
		else {
			toll = false;
		}
		
		if (tr.contains("low")){
			traffic = 1.0;
		}
		else if (tr.contains("medium")){
			traffic = 0.7;
		}
		else if (tr.contains("high")){
			traffic = 0.5;
		}
		else {
			System.out.println("Error");
		}
	}
	
	public double speed(){
		return maxspeed*traffic;
	}
}
