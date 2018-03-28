import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

public class Lines {

	private HashMap<Integer, Boolean> m;

	@SuppressWarnings("resource")
	public Lines (String filename){
		
		String s=new String();
		try{
			FileReader fileReader= new FileReader(filename);
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			bufferedReader = new BufferedReader(new FileReader(filename));

			String parts[],highway,oneway,lit,lanes,maxspeed,railway,boundary,access,natural,barrier,waterway,toll;
			s = bufferedReader.readLine();
			int id;

			m = new HashMap<Integer, Boolean>();
			FileWriter fw;
			BufferedWriter bw;
			PrintWriter out;


			fw = new FileWriter("info.pl", false);
			bw = new BufferedWriter(fw);
			out = new PrintWriter(bw);

			while ((s = bufferedReader.readLine()) != null) {			

				parts = s.split(""+'"');

				if (parts.length==1){
				}
				else {
					s = new String(parts[0] + parts[parts.length-1]);
				}

				parts = s.split(",");

				if (parts.length>=2)highway = new String(parts[1]); else highway = new String("");
				if (parts.length>=8)railway = new String(parts[7]); else railway = new String("");
				if (parts.length>=9)boundary = new String(parts[8]); else boundary = new String("");
				if (parts.length>=10)access = new String(parts[9]); else access = new String("");
				if (parts.length>=11)natural = new String(parts[10]); else natural = new String("");
				if (parts.length>=12)barrier = new String(parts[11]); else barrier = new String("");
				if (parts.length>=16)waterway = new String(parts[15]); else waterway = new String("");
				
				if(highway.equals("elevator") || highway.equals("footway") || highway.equals("proposed") || highway.equals("bridleway") || highway.equals("steps") ||  highway.equals("construction") || highway.equals("track") || highway.equals("path") || highway.equals("cycleway") || highway.equals("pedestrian") || highway.equals("")){
					continue;
				}
				//highway.equals("unclassified") ||
				if (!railway.equals("") || !boundary.equals("") || !access.equals("") || !natural.equals("") || !barrier.equals("") || !waterway.equals("")){
					continue;
				}

				id = Integer.parseInt(parts[0]);
				m.put(id, true);

				if (parts.length>=4)oneway = new String(parts[3]); else oneway = new String("");
				if (parts.length>=5)lit = new String(parts[4]); else lit = new String("");
				if (parts.length>=6)lanes = new String(parts[5]); else lanes = new String("");
				if (parts.length>=7 && parts[6].length()>0)
					maxspeed = new String(parts[6]); 
				else{
					if(highway.contains("motorway")){
						maxspeed = new String("100");
					} else if (highway.contains("primary")){
						maxspeed = new String("100");
					}else if (highway.contains("secondary")){
						maxspeed = new String("80");
					}else if (highway.contains("tertiary")){
						maxspeed = new String("80");
					}else if (highway.contains("residential")){
						maxspeed = new String("40");
					}else if (highway.contains("service")){
						maxspeed = new String("20");
					}else if (highway.contains("trunk")){
						maxspeed = new String("100");
					}else if (highway.contains("living_street")){
						maxspeed = new String("40");
					}else if (highway.equals("unclassified")){
						maxspeed = new String("40");
					}else if (highway.equals("")){
						maxspeed = new String("40");
					}else {
						maxspeed = new String("40");
						System.out.println(highway);
					}
				}
				if (parts.length>=18)toll = new String(parts[17]); else toll = new String("");
				
				out.println("line("+id+",'"+oneway+"','"+lit+"','"+lanes+"','"+maxspeed+"','"+toll+"').");

			}
			if (bufferedReader != null){
				bufferedReader.close();
			}
			if (fileReader != null){
				fileReader.close();
			}
			if (out!=null){
				out.close();
			}
		} catch(FileNotFoundException ex) {
			System.out.println("Unable to open file '" + filename + "'");                
		}
		catch(IOException ex) {
			ex.printStackTrace();
		}
		catch(ArrayIndexOutOfBoundsException pig){
			System.out.println(s);
			pig.printStackTrace();
		}
		
	}

	public boolean is_road(int i){
		return m.containsKey(i);
	}

}
