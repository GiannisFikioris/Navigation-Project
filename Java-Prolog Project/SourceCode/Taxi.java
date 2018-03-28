import java.util.Comparator;
import java.util.HashMap;
import java.util.Vector;

public class Taxi{

	private double X,Y,divergence,Time;
	private int id,capacity[],steps,maxsize;
	private boolean available, long_distance, cmn_language, Lit, Toll;
	private String languages[],type,Route, taxi_state; 
	private float rating;
	private long temp_id;

	public Taxi(String s){

		String[] parts = s.split(",");

		X 		= Double.parseDouble(parts[0]);
		Y 		= Double.parseDouble(parts[1]);
		id 		= Integer.parseInt(parts[2]);
		divergence	= 100000000000.0;

		if (parts[3].equals("yes")){
			available = true;
		}
		else {
			available = false;
		}

		capacity = new int[2];
		String[] parts2 = parts[4].split("-");
		capacity[0]= Integer.parseInt(parts2[0]);
		capacity[1]= Integer.parseInt(parts2[1]);
		languages= parts[5].split("\\|");
		rating	 = Float.parseFloat(parts[6]);

		if (parts[7].equals("yes")){
			long_distance = true;
		}
		else {
			long_distance = false;
		}

		String[] temp = parts[8].split(" ");
		type = new String(temp[0]);
	}

	private double haversine(double lon1, double lat1, double lon2, double lat2) {
		double R = 6372.8; // In kilometers
		double dLat = Math.toRadians(lat2 - lat1);
		double dLon = Math.toRadians(lon2 - lon1);
		lat1 = Math.toRadians(lat1);
		lat2 = Math.toRadians(lat2);

		double a = Math.pow(Math.sin(dLat / 2),2) + Math.pow(Math.sin(dLon / 2),2) * Math.cos(lat1) * Math.cos(lat2);
		double c = 2 * Math.asin(Math.sqrt(a));
		return R * c;
	}

	public void Astar(Client c, Prolog prolog, Nodes nodes, int metop){

		Sorted_List_Limited list = new Sorted_List_Limited(metop);
		Node node1, node2;
		HashMap<Long,Double> Times = new HashMap<Long,Double>();
		Step_info temp1,temp2;
		boolean flag, lit=false, toll=true;
		double dist,dist2,tempd,customer_x,customer_y;
		int i,j;
		long key;
		String s;
		Vector<Answer> nextnodes;

		steps=0;
		maxsize=0;
		
		if(c.get_persons()<capacity[0] || c.get_persons()>capacity[1]) { 
			taxi_state = new String("Taxi Id:"+id+" is not available due to capacity limit"); 
			Time=100000000.0; return;
		}
		if(c.get_luggage()>0 && type.equals("subcompact")) {
			taxi_state = new String("Taxi Id:"+id+" is not capable to carry your luggage");
			Time=100000000.0; return;
		} 
		if(!available) {
			taxi_state = new String("Taxi Id:"+id+" is not available for transportation right now");
			Time=100000000.0; return;
		}

		cmn_language = false;
		Vector<String> temp = c.get_languages();
		for(i=0;i<temp.size();i++){
			for(j=0;j<languages.length;j++){
				if (temp.get(i).equals(languages[j])){cmn_language = true; break; }
			}
			if(cmn_language) break;
		}
		///////////////////////////////////////////////////

		s = new String(Double.toString(X) + "," + Double.toString(Y));
		node1 = nodes.get_node_at(temp_id);
		node2 = nodes.get_node_at(c.get_beg());
		customer_x = node2.X;
		customer_y = node2.Y;

		tempd = haversine(customer_x, customer_y, node1.X, node1.Y);
		
		//long distanceffffffffffffff
		if(tempd>50 && long_distance) {
			taxi_state = new String("Taxi Id:"+id+" is not available for long distance transportation");
			Time=100000000.0; return;
		}
		
		dist2 = tempd/131.0;
		temp1 = new Step_info(0.0, temp_id, dist2, s, true, false);
		list.push(temp1);
		
		while(!list.empty() && list.top().get_e()*3600>5){
			temp1 = list.pop();
			key = temp1.get_id();

			if(!Times.containsKey(key)){
				Times.put(key,temp1.get_d());
			}
			else{
				if(Times.get(key)<temp1.get_d()){
					continue;
				}
				else{
					Times.put(key,temp1.get_d());
				}
			}
			steps++;
			node1 = nodes.get_node_at(temp1.get_id());

			nextnodes = prolog.getnext(temp1.get_id(),c.get_time());

			for(i=0;i<nextnodes.size();i++){

				key = nextnodes.get(i).nextid;

				node2 = nodes.get_node_at(key);

				dist = temp1.get_d() + haversine(node1.X,node1.Y,node2.X,node2.Y) / nextnodes.get(i).speed();

				if(Times.containsKey(key) && Times.get(key)<dist+0.0000001){
					continue;
				}

				dist2 = haversine(customer_x, customer_y, node2.X, node2.Y) / 131.0;

				s = temp1.get_Path() + " " + Double.toString(node2.X) + "," + Double.toString(node2.Y);

				lit = nextnodes.get(i).lit & temp1.get_lit(); 

				toll = nextnodes.get(i).toll | temp1.get_toll(); 

				temp2 = new Step_info(dist, key, dist2, s, lit, toll);
				flag = list.push(temp2);
				if(flag){
					Times.put(key,dist);
				}
			}

			if(maxsize<list.size()){
				maxsize=list.size();
			}
		}


		if(!list.empty()){
			Route = new String(list.top().get_Path());
			Time = list.top().get_d();
			Lit = lit;
			Toll = toll;
		}
		else{
			Route = new String("");
			Time=100000000.0;
		}
	}

	public void update(long node_id, double x, double y) {

		double temp = (X-x)*(X-x) + (Y-y)*(Y-y);

		if(temp<divergence){
			divergence = temp;
			temp_id = node_id;
		}
	}

	public double get_Time() {
		return Time;
	}

	public String get_Route() {
		return Route;
	}

	public boolean get_Lit() {
		return Lit;
	}

	public boolean get_Toll() {
		return Toll;
	}

	public float get_rating() {
		return rating;
	}

	public int get_id() {
		return id;
	}

	public boolean get_cmn() {
		return cmn_language;
	}

	public String get_languages() {
		String s = new String(languages[0]);
		for(int i =1;i<languages.length;i++){
			s = s + "," + languages[i];
		}
		return s;
	}

	public int get_steps() {
		return steps;
	}

	public int get_maxsize() {
		return maxsize;
	}

}
