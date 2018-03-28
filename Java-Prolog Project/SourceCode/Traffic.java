import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class Traffic {

	@SuppressWarnings("resource")
	public Traffic(String filename, Lines L){
		
		int line_id;
		String info[],parts[],traffic,temp[];		
		String s=new String();
		
		try{
			FileReader fileReader= new FileReader(filename);
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			bufferedReader = new BufferedReader(new FileReader(filename));

			s = bufferedReader.readLine();
			
			
			FileWriter fw;
			BufferedWriter bw;
			PrintWriter out;

			fw = new FileWriter("info.pl", true);
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
				
				if(parts.length < 3)
					continue;

				line_id = Integer.parseInt(parts[0]);
				
				if(!L.is_road(line_id)) continue;
								
				info = parts[2].split("\\|");
				
				for(int i=0;i<info.length;i++){
					parts = info[i].split("=");
					traffic = new String(parts[1]);
					parts = parts[0].split("-");
					temp = parts[0].split(":");
					parts[0] = temp[0];					
					temp = parts[1].split(":");
					parts[1] = temp[0];
					
					out.println("traffic("+line_id+","+parts[0]+","+parts[1]+",'"+traffic+"').");
				}

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
	
}
