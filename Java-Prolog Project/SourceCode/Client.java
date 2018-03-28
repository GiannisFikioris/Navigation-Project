import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Vector;

public class Client {

	private double X, Y, X_dest, Y_dest, divergence,divergence_dest, Time;
	private String language[], Route;
	private int persons, luggage, time, steps, maxsize;
	private Vector<String> languages = new Vector<String>();
	private long temp_id,temp_id_dest;
	private boolean Lit, Toll;

	@SuppressWarnings("resource")
	public Client(String filename){

		divergence	= 100000000000.0;
		divergence_dest	= 100000000000.0;
		try{
			FileReader fileReader= new FileReader(filename);
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			bufferedReader = new BufferedReader(new FileReader(filename));

			String s;
			s = bufferedReader.readLine();
			s = bufferedReader.readLine();

			String[] p,parts = s.split(",");

			X 		= Double.parseDouble(parts[0]);
			Y 		= Double.parseDouble(parts[1]);
			X_dest  = Double.parseDouble(parts[2]);
			Y_dest  = Double.parseDouble(parts[3]);
			p = parts[4].split(":"); ///??
			time	= Integer.parseInt(p[0]+p[1]); //??
			persons = Integer.parseInt(parts[5]);
			language= parts[6].split("\\|");
			for(int i=0;i<language.length; i++){languages.addElement(new String(language[i]));}
			luggage = Integer.parseInt(parts[7]);

			if (bufferedReader != null)
				bufferedReader.close();

			if (fileReader != null)
				fileReader.close();
		} catch(FileNotFoundException ex) {
			System.out.println("Unable to open file '" + filename + "'");                
		}
		catch(IOException ex) {
			ex.printStackTrace();
		}
	}

	public void Astar(Prolog prolog, Nodes nodes, int metop){
		
		Sorted_List_Limited list = new Sorted_List_Limited(metop);
		Node node1, node2;
		HashMap<Long,Double> Times = new HashMap<Long,Double>();
		Step_info temp1,temp2;
		boolean flag, lit=false, toll=true;
		double dist,dist2,dest_x,dest_y;
		int i;
		long key;
		String s;
		Vector<Answer> nextnodes;
		
		node2 = nodes.get_node_at(temp_id_dest);
		dest_x=node2.X;
		dest_y=node2.Y;
		
		s = new String(Double.toString(X) + "," + Double.toString(Y));
		node1 = nodes.get_node_at(temp_id);

		dist2 = haversine(dest_x, dest_y, node1.X, node1.Y)/131.0;
		temp1 = new Step_info(0.0, temp_id, dist2, s, true, false);
		list.push(temp1);
		steps=0;
		maxsize=0;

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

			nextnodes = prolog.getnext(temp1.get_id(),time);
			
			for(i=0;i<nextnodes.size();i++){

				key = nextnodes.get(i).nextid;

				node2 = nodes.get_node_at(key);

				dist = temp1.get_d() + haversine(node1.X,node1.Y,node2.X,node2.Y) / nextnodes.get(i).speed();/////////////////////

				if(Times.containsKey(key) && Times.get(key)<dist+0.0000001){
					continue;
				}

				dist2 = haversine(dest_x, dest_y, node2.X, node2.Y) / 131.0;

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
			Route = new String();
			Time=100000000.0;
		}

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

	public int get_persons(){
		return persons;
	}

	public int get_luggage(){
		return luggage;
	}

	public double get_X(){
		return X;
	}

	public double get_Y(){
		return Y;
	}

	public Vector<String> get_languages(){
		return languages;
	}

	public int get_time(){
		return time;
	}

	public void update(long node_id, double x, double y) {
		double temp = (X-x)*(X-x) + (Y-y)*(Y-y);

		if(temp<divergence){
			divergence = temp;
			temp_id = node_id;
		}

		temp = (X_dest-x)*(X_dest-x) + (Y_dest-y)*(Y_dest-y);

		if(temp<divergence_dest){
			divergence_dest = temp;
			temp_id_dest = node_id;
		}
	}

	public long get_beg() {
	
		return temp_id;
	}

	public void writekml(PrintWriter writer) {
		
		writer.println("<Placemark>");
		writer.println("<name>"+"Client"+"</name>");
		writer.println("<styleUrl>#"+"blue"+"</styleUrl>");
		writer.println("<LineString>");
		writer.println("<altitudeMode>relative</altitudeMode>");
		writer.println("<coordinates>");
		String[] parts = Route.split(" ");
		for(String w:parts){  
			writer.println(w + ",0");  
		}
		writer.println("</coordinates>");
		writer.println("</LineString>");
		writer.println("</Placemark>");	
		
	}

	public void results() {

		System.out.println("Client will reach his destination in " + Time*3600 + " seconds.");
		if(Lit){
			System.out.println("\t~Road will be lit.");
		}
		else {
			System.out.println("\t~Road will not be lit.");
		}
		if(Toll){
			System.out.println("\t~You will have to pay tolls.");
		}
		else {
			System.out.println("\t~Paying tolls will not be necessary.");
		}
		System.out.println();
	}

	public int get_steps() {
		return steps;
	}

	public int get_maxsize() {
		return maxsize;
	}


}
