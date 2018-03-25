import java.util.*;
import java.lang.Math;
import java.io.*;

public class RoadsList {

	private HashMap<Integer/*id*/,RoadPoints> IDs;
	private Double cust_x,cust_y;
	int cust_id;

	private void MakeRoadPoints(Vector<Row> File){
		int i;
		int length = File.size();
		RoadPoints tempPoints;
		
		IDs = new HashMap<Integer/*id*/,RoadPoints>();
		
		tempPoints= new RoadPoints(new String (File.get(0).Name));
		tempPoints.addPoint(File.get(0).x, File.get(0).y,0);
		
		for(i=1;i<length;i++){
			if(File.get(i).id == File.get(i-1).id){
				tempPoints.addPoint(File.get(i).x, File.get(i).y,i);
			}
			else {
				IDs.put(File.get(i-1).id, tempPoints);
				tempPoints = new RoadPoints(new String (File.get(i).Name));
				tempPoints.addPoint(File.get(i).x, File.get(i).y,i);
			}	
		}
		IDs.put(File.get(length-1).id, tempPoints);
	}

	@SuppressWarnings("resource")
	RoadsList(String filename,Double Cx, Double Cy){
		
		Vector<Row> File = new Vector<Row>();
		int i,j,length;
		
		try{
			FileReader fileReader= new FileReader(filename);
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			bufferedReader = new BufferedReader(new FileReader(filename));

			Row temp;
			String s;
			s = bufferedReader.readLine();
			while ((s = bufferedReader.readLine()) != null) {
				temp = new Row();
				String[] parts = s.split(",");
				temp.x = Double.parseDouble(parts[0]);
				temp.y = Double.parseDouble(parts[1]);
				temp.id = Integer.parseInt(parts[2]);
				if(parts.length==3){
					temp.Name="";
				}
				else{
					temp.Name = parts[3];
				}
				
				File.addElement(temp);
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
	
		MakeRoadPoints(File);
		
		Collections.sort(File);
		length=File.size();
		
		Double tempaki,min = 100000000.0;
		
		for(i=0;i<length;i++){
			
			tempaki = (Cx-File.get(i).x)*(Cx-File.get(i).x) + (Cy-File.get(i).y)*(Cy-File.get(i).y);
			
			if(tempaki<min){
				cust_x=File.get(i).x;
				cust_y=File.get(i).y;
				cust_id = File.get(i).id;
				min = tempaki;
			}
			
			Vector<Integer> temp = new Vector<Integer>();
			
			for(j=i-1;j>=0;j--){
				if(Math.abs(File.get(j).y-File.get(i).y)>0.000000001 || Math.abs(File.get(j).x-File.get(i).x)>0.000000001){
					break;
				}
				else{
					temp.addElement(File.get(j).id);
				}
			}
			for(j=i+1;j<length;j++){
				if(Math.abs(File.get(j).y-File.get(i).y)>0.000000001 || Math.abs(File.get(j).x-File.get(i).x)>0.000000001){
					break;
				}
				else{
					temp.addElement(File.get(j).id);
				}
			}
			if (temp.size()>0){
				IDs.get(File.get(i).id).addinIntersections(File.get(i).x, File.get(i).y, temp);
			}
		}	
	}

	public Vector<Integer> getIntersection(Double xx, Double yy, int idd){
		return IDs.get(idd).getIntersection(xx,yy);
	}

	public Double getcx(){
		return cust_x;
	}

	public Double getcy(){
		return cust_y;
	}

	public int getcid() {
		return cust_id;
	}
	
	public RoadPoints get_RoadPoints(int id){
		return IDs.get(id);
	}
	
}
