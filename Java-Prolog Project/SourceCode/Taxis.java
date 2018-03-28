import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Collections;
import java.util.Vector;

public class Taxis {

	Vector<Taxi> taxis;
	String colors[]={"green","red1","red2","red3","red4","red5","red6","red7","red8","red9","red10","red11"};

	@SuppressWarnings("resource")
	public Taxis(String filename){

		try{
			FileReader fileReader= new FileReader(filename);
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			bufferedReader = new BufferedReader(new FileReader(filename));

			String s;
			s = bufferedReader.readLine();
			taxis = new Vector<Taxi>();

			while ((s = bufferedReader.readLine()) != null) {			

				taxis.addElement(new Taxi(s));

			}
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

	public void Astars(Client c, Prolog prolog, Nodes nodes, int metop){
		for(int i=0; i<taxis.size(); i++){
			taxis.elementAt(i).Astar(c, prolog, nodes, metop);
		}
	}

	public void update(long node_id, double x, double y) {
		for(int i=0; i<taxis.size(); i++){
			taxis.elementAt(i).update(node_id, x, y);
		}
	}

	public void writekml(PrintWriter writer) {
		
		int j=0;

		for(int i=0; i<taxis.size(); i++){
			
			if(taxis.elementAt(i).get_Time()>100000){
				continue;
			}
			
			writer.println("<Placemark>");
			writer.println("<name>id="+taxis.elementAt(i).get_id()+"</name>");
			writer.println("<styleUrl>#"+colors[j]+"</styleUrl>");
			writer.println("<LineString>");
			writer.println("<altitudeMode>relative</altitudeMode>");
			writer.println("<coordinates>");
			String[] parts = taxis.elementAt(i).get_Route().split(" ");
			for(String w:parts){  
				writer.println(w + ",0");  
			}
			writer.println("</coordinates>");
			writer.println("</LineString>");
			writer.println("</Placemark>");	
			
			j++;
			if(j==colors.length)j--;
			
		}

	}

	public void sort_results() {

		CompareTaxi comp = new CompareTaxi(true);

		Collections.sort(taxis, comp);

		System.out.println("Taxis sorted by time:");

		for(int i=0; i<taxis.size(); i++){
			
			if(taxis.elementAt(i).get_Time()>10000) break;

			System.out.println("\t~Taxi No. "+taxis.elementAt(i).get_id()+" arrives in "+(3600*taxis.elementAt(i).get_Time()));

		}
		
		System.out.println();

	}

	public void sort_ratings(int kk) {
		
		int k;
		if(kk>taxis.size()){
			k = taxis.size();
		}
		else {
			k=kk;
		}

		CompareTaxi comp = new CompareTaxi(false);
		Taxi temp[] = new Taxi[taxis.size()];
		taxis.copyInto(temp);

		Arrays.sort(temp, 0, k, comp);

		System.out.println("Taxis sorted by rating:");

		for(int i=0; i<k; i++){

			System.out.print("\t~Taxi No. "+temp[i].get_id()+" has rating of "+temp[i].get_rating()+" ");
			if(temp[i].get_cmn()){
				System.out.println("and speaks the same language");
			}
			else {
				System.out.println("but speaks " + temp[i].get_languages());
			}

		}
		System.out.println();

	}

	public long get_steps() {
		long steps=0;
		for(int i=0;i<taxis.size();i++){
			
			steps+=taxis.elementAt(i).get_steps();
			
		}
		return steps;
	}

	public int get_maxsize() {
		int maxsize=0;
		for(int i=0;i<taxis.size();i++){
			
			maxsize = max(maxsize,taxis.elementAt(i).get_maxsize());
			
		}
		return maxsize;
	}

	private int max(int a, int b) {
		if(a>b) return a;
		else return b;
	}

}
