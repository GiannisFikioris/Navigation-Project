//package search;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;


public class NewGPS {

	private static final String client_file = 	new String("client.csv");
	private static final String taxis_file =	new String("taxis.csv");
	private static final String lines_file = 	new String("lines.csv");
	private static final String nodes_file = 	new String("nodes.csv");
	private static final String traffic_file = 	new String("traffic.csv");
	private static final int 		metop =		100;
	private static final int 			k =		6;


	public static void main(String[] args){

		long starttime = System.nanoTime();

		Client client = new Client(client_file);
		Taxis taxis = new Taxis(taxis_file);
		Lines lines = new Lines(lines_file);
		Nodes nodes = new Nodes(nodes_file,lines,taxis,client);
		@SuppressWarnings("unused")
		Traffic traffic = new Traffic(traffic_file,lines);


		Prolog prolog = new Prolog("info.pl"); 

		taxis.Astars(client,prolog,nodes, metop); 

		client.Astar(prolog, nodes,metop);

		taxis.sort_results();
		taxis.sort_ratings(k);
		client.results();

		try {
			PrintWriter writer = new PrintWriter("output.kml", "UTF-8");
			initial(writer);

			taxis.writekml(writer);

			client.writekml(writer);

			terminal(writer);

			writer.close();

		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		long steps,maxsize;
		steps = taxis.get_steps()+client.get_steps();
		maxsize = max(taxis.get_maxsize(),client.get_maxsize());

		System.out.println("****** Max size was "+maxsize+", allowed size was "+metop+" ******");
		System.out.println("****** Astar comlpeted in "+steps+" steps ******");
		System.out.println("****** "+(System.nanoTime()-starttime)/1000000/1000.0 + " secs ******");
	}

	private static void initial(PrintWriter writer){
		writer.println("<?xml version=" + '"' + "1.0" + '"' +" encoding=" +'"' + "UTF-8" + '"' + "?>");
		writer.println("<kml xmlns=" + '"' + "http://earth.google.com/kml/2.1" + '"' +">");
		writer.println("<Document>");
		writer.println("<name>Taxi Routes</name>");
		writer.println("<Style id=" +'"' + "green" + '"' + ">");
		writer.println("<LineStyle>");
		writer.println("<color>ff009900</color>");
		writer.println("<width>6</width>");
		writer.println("</LineStyle>");
		writer.println("</Style>");

		writer.println("<Style id=" +'"' + "red1" + '"' + ">");
		writer.println("<LineStyle>");
		writer.println("<color>501400FF</color>");
		writer.println("<width>5</width>");
		writer.println("</LineStyle>");
		writer.println("</Style>");

		writer.println("<Style id=" +'"' + "red2" + '"' + ">");
		writer.println("<LineStyle>");
		writer.println("<color>501400E6</color>");
		writer.println("<width>4</width>");
		writer.println("</LineStyle>");
		writer.println("</Style>");		

		writer.println("<Style id=" +'"' + "red3" + '"' + ">");
		writer.println("<LineStyle>");
		writer.println("<color>501400C8</color>");
		writer.println("<width>3</width>");
		writer.println("</LineStyle>");
		writer.println("</Style>");	

		writer.println("<Style id=" +'"' + "red4" + '"' + ">");
		writer.println("<LineStyle>");
		writer.println("<color>501400AA</color>");
		writer.println("<width>2</width>");
		writer.println("</LineStyle>");
		writer.println("</Style>");	

		writer.println("<Style id=" +'"' + "red5" + '"' + ">");
		writer.println("<LineStyle>");
		writer.println("<color>5014008C</color>");
		writer.println("<width>2</width>");
		writer.println("</LineStyle>");
		writer.println("</Style>");	

		writer.println("<Style id=" +'"' + "red6" + '"' + ">");
		writer.println("<LineStyle>");
		writer.println("<color>5014006E</color>");
		writer.println("<width>2</width>");
		writer.println("</LineStyle>");
		writer.println("</Style>");	

		writer.println("<Style id=" +'"' + "red7" + '"' + ">");
		writer.println("<LineStyle>");
		writer.println("<color>50140050</color>");
		writer.println("<width>2</width>");
		writer.println("</LineStyle>");
		writer.println("</Style>");	

		writer.println("<Style id=" +'"' + "red8" + '"' + ">");
		writer.println("<LineStyle>");
		writer.println("<color>50140032</color>");
		writer.println("<width>2</width>");
		writer.println("</LineStyle>");
		writer.println("</Style>");	

		writer.println("<Style id=" +'"' + "red9" + '"' + ">");
		writer.println("<LineStyle>");
		writer.println("<color>5014000A</color>");
		writer.println("<width>2</width>");
		writer.println("</LineStyle>");
		writer.println("</Style>");	

		writer.println("<Style id=" +'"' + "blue" + '"' + ">");
		writer.println("<LineStyle>");
		writer.println("<color>50F07800</color>");
		writer.println("<width>4</width>");
		writer.println("</LineStyle>");
		writer.println("</Style>");	
	}

	private static void terminal(PrintWriter writer) {
		writer.println("</Document>");
		writer.println("</kml>");
	}

	private static long max(int a, int b) {
		if(a>b) return a;
		else return b;
	}
}
