import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.*;

public class GPS {
	
	private static String nodes=new String("nodes.csv"), taxis = new String("taxis.csv"), client = new String("client.csv");

	private static Double customer_x,customer_y,MinDistance;
	private static Vector<Double> taxi_x, taxi_y;
	private static Vector<Integer> taxi_id;
	private static int customer_id,Size,Steps,MaxSize;
	private static Vector<Astar> Astars;
	
	public static void main(String[] args) {
		
		long startTime = System.nanoTime();
		
		int i,count=0;
		
		if(args.length>0){
			Size = Integer.parseInt(args[0]);
		}
		else {
			Size = 10000;
		}

		read_client(client); // method that saves client's data

		taxi_x = new Vector<Double>();
		taxi_y = new Vector<Double>();
		taxi_id = new Vector<Integer>();

		read_taxis(taxis); // method that saves taxis' data
		
		RoadsList HMap = new RoadsList(nodes,customer_x,customer_y); // Data structure in which the Map is read and saved

		customer_x = HMap.getcx();
		customer_y = HMap.getcy();
		customer_id = HMap.getcid();
		
		Astars = new Vector<Astar>();
		for(i=0;i<taxi_id.size();i++){
			// Pass into Astars the data needed to run the A* algorithm
			Astars.addElement(new Astar(customer_x,customer_y,customer_id,taxi_x.get(i),taxi_y.get(i),Size,HMap));
		}
		
		System.out.println("Time for parsing input was: " + ((System.nanoTime()-startTime)/1000000000.0) + " seconds");
		
		long midTime = System.nanoTime();

		for(i=0;i<taxi_id.size();i++){
			Astars.get(i).start(); // Run A*
		}
		
		for(i=0;i<taxi_id.size();i++){
			try {
				Astars.get(i).join();
			} 
			catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		System.out.println("\nTime for completing " + taxi_id.size() +" Astars was: " + ((System.nanoTime()-midTime)/1000000000.0) + " seconds\n");
		
		HMap=null;
		MinDistance=100000.0;
		MaxSize = 0;
		int minpos=0;
		Steps=0;
		
		for(i=0;i<Astars.size();i++){
			Steps += Astars.get(i).get_steps();
			
			if(Astars.get(i).get_Distance()<MinDistance){
				MinDistance = Astars.get(i).get_Distance();
				minpos = i;
			}
			if(Astars.get(i).get_Distance()>1000.0){
				count++;
			}
			
			if(Astars.get(i).get_maxsize()>MaxSize){
				MaxSize = Astars.get(i).get_maxsize();
			}
		}
		
		if(count>0){
			System.out.println(count + " out of the " + taxi_id.size() + " taxis didn't find a route" );
		}
		else {
			System.out.println("All " + Astars.size()+" taxis found a route");
		}
		
		if(count<Astars.size()){
			System.out.println("\tMinimum distance was " + myround(MinDistance) + " km");
			System.out.println("\tTotal steps of all A* was " + Steps);
			System.out.println("\tMax allowed Size was " + Size + ", one instance of A* reached size " + MaxSize);
		}
		
		try {
			PrintWriter writer = new PrintWriter("output.kml", "UTF-8");
			initial(writer);
			for(i=0;i<Astars.size();i++){
				if(Astars.get(i).get_Distance()>1000.0) 
					continue;
				
				String color;
				if(i==minpos){
					color = new String("green");
				}
				else{
					color = new String("red");
				}
				writer.println("<Placemark>");
				writer.println("<name>id="+taxi_id.get(i)+"</name>");
				writer.println("<styleUrl>#"+color+"</styleUrl>");
				writer.println("<LineString>");
				writer.println("<altitudeMode>relative</altitudeMode>");
				writer.println("<coordinates>");
				String[] parts = Astars.get(i).get_Route().split(" ");
				for(String w:parts){  
					writer.println(w + ",0");  
				}
				writer.println("</coordinates>");
				writer.println("</LineString>");
				writer.println("</Placemark>");				
			}
			terminal(writer);
			writer.close();
			
		}
		catch (FileNotFoundException | UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		System.out.println("\nTotal time was: " + ((System.nanoTime()-startTime)/1000000000.0) + " seconds");

	}

	private static double myround(double d) {
		d = 100*d;
		return ((double)Math.round(d))/100;
	}

	@SuppressWarnings("resource")
	private static void read_client(String argg){

		try{
			FileReader fileReader= new FileReader(argg);
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			bufferedReader = new BufferedReader(new FileReader(argg));

			String s;
			s = bufferedReader.readLine();
			s = bufferedReader.readLine();

			String[] parts = s.split(",");
			customer_x = Double.parseDouble(parts[0]);
			customer_y = Double.parseDouble(parts[1]);

			if (bufferedReader != null)
				bufferedReader.close();

			if (fileReader != null)
				fileReader.close();
		} catch(FileNotFoundException ex) {
			System.out.println("Unable to open file '" + argg + "'");                
		}
		catch(IOException ex) {
			ex.printStackTrace();
		}	
	}

	@SuppressWarnings("resource")
	private static void read_taxis(String argg){

		String s=new String("nothing");
		try{
			FileReader fileReader= new FileReader(argg);
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			bufferedReader = new BufferedReader(new FileReader(argg));

			
			s = bufferedReader.readLine();
			while ((s = bufferedReader.readLine()) != null && s.length()>4) {

				String[] parts = s.split(",");
				
				taxi_x.addElement( Double.parseDouble(parts[0]));
				taxi_y.addElement( Double.parseDouble(parts[1]));
				taxi_id.addElement( Integer.parseInt(parts[2]));
				
			}
			if (bufferedReader != null)
				bufferedReader.close();

			if (fileReader != null)
				fileReader.close();
		} catch(FileNotFoundException ex) {
			System.out.println("Unable to open file '" + argg + "'");                
		}
		catch(IOException ex) {
			ex.printStackTrace();
		}	
	}

	private static void initial(PrintWriter writer){
		writer.println("<?xml version=" + '"' + "1.0" + '"' +" encoding=" +'"' + "UTF-8" + '"' + "?>");
		writer.println("<kml xmlns=" + '"' + "http://earth.google.com/kml/2.1" + '"' +">");
		writer.println("<Document>");
		writer.println("<name>Taxi Routes</name>");
		writer.println("<Style id=" +'"' + "green" + '"' + ">");
		writer.println("<LineStyle>");
		writer.println("<color>ff009900</color>");
		writer.println("<width>4</width>");
		writer.println("</LineStyle>");
		writer.println("</Style>");
		writer.println("<Style id=" +'"' + "red" + '"' + ">");
		writer.println("<LineStyle>");
		writer.println("<color>ff0000ff</color>");
		writer.println("<width>4</width>");
		writer.println("</LineStyle>");
		writer.println("</Style>");		
	}
	
	private static void terminal(PrintWriter writer) {
		writer.println("</Document>");
		writer.println("</kml>");
	}
}
