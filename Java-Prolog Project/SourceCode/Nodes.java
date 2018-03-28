import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

public class Nodes {
	
	private HashMap<Long,Node> m;

	@SuppressWarnings("resource")
	public Nodes (String filename,Lines L, Taxis taxis,Client client){
		
		String s=new String();
		long node_id,prevnode=-1;
		int line_id,prevline=-1;
		double x,y;
		m = new HashMap<Long,Node>();
		try{
			FileReader fileReader= new FileReader(filename);
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			bufferedReader = new BufferedReader(new FileReader(filename));

			String parts[];
			s = bufferedReader.readLine();
			
			FileWriter fw,fw2;
			BufferedWriter bw,bw2;
			PrintWriter out,out2;

			fw = new FileWriter("info.pl", true);
			bw = new BufferedWriter(fw);
			out = new PrintWriter(bw);
			
			fw2 = new FileWriter("nextinfo.pl", false);
			bw2 = new BufferedWriter(fw2);
			out2 = new PrintWriter(bw2);

			while ((s = bufferedReader.readLine()) != null) {			

				parts = s.split(""+'"');

				if (parts.length==1){

				}
				else {
					s = new String(parts[0] + parts[parts.length-1]);
				}

				parts = s.split(",");
				
				node_id = Long.parseLong(parts[3]);
				x = Double.parseDouble(parts[0]);
				y = Double.parseDouble(parts[1]);
				line_id = Integer.parseInt(parts[2]);
				
				if(!L.is_road(line_id)) continue;
				
				if(prevline==line_id){
					out2.println("next("+line_id+","+prevnode+","+node_id+").");
				}
				else{
					prevline = line_id;
				}
				
				m.put(node_id, new Node(x,y));
				
				taxis.update(node_id,x,y);
				client.update(node_id,x,y);
				
				//out.println("node("+line_id+","+node_id+").");
				out.println("node("+node_id+","+line_id+").");
				prevnode=node_id;

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
			if (out2!=null){
				out2.close();
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

	public Node get_node_at(long key){
		return m.get(key);
	}

}
