import java.util.*;

public class Astar extends Thread{

	private Sorted_List_Limited lista;
	private double customer_x,customer_y,taxi_x,taxi_y,Distance;
	private int customer_id,steps,maxsize;
	private String Route;
	private RoadsList Thing;

	Astar(double cx,double cy,int cid,double tx,double ty,int size,RoadsList Hmap){
		lista = new Sorted_List_Limited(size);
		customer_x = cx;
		customer_y = cy;
		customer_id = cid;
		taxi_x = tx;
		taxi_y = ty;
		Thing = Hmap;
	}
	
	public double haversine(double lon1, double lat1, double lon2, double lat2) {
		double R = 6372.8; // In kilometers
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        lat1 = Math.toRadians(lat1);
        lat2 = Math.toRadians(lat2);
 
        double a = Math.pow(Math.sin(dLat / 2),2) + Math.pow(Math.sin(dLon / 2),2) * Math.cos(lat1) * Math.cos(lat2);
        double c = 2 * Math.asin(Math.sqrt(a));
        return R * c;
    }
	
	public void run(){
		HashMap<Integer,Double> Distances = new HashMap<Integer,Double>();
		Step_info temp,temp2;
		boolean flag;
		double dist,dist2;
		int i,j,key;
		String s;
		RoadPoints tempRoadPoints;
		Vector<Integer> tempv;
		Vector<Point> tempPoint;
		s = new String(Double.toString(customer_x) + "," + Double.toString(customer_y));
		
		dist2 = haversine(customer_x, customer_y, taxi_x, taxi_y);
		temp = new Step_info(0.0,customer_x,customer_y,customer_id,dist2,s);
		lista.push(temp);
		steps=0;
		maxsize=0;
		
		while(!lista.empty() && lista.top().get_e()>0.08){
			temp = lista.pop();
			tempRoadPoints = Thing.get_RoadPoints(temp.get_id());
			key = tempRoadPoints.get_key(temp.get_x(), temp.get_y());

			if(!Distances.containsKey(key)){
				Distances.put(key,temp.get_d());
			}
			else{
				if(Distances.get(key)<temp.get_d()){
					continue;
				}
				else{
					Distances.put(key,temp.get_d());
				}
			}
			steps++;
			tempv = tempRoadPoints.getIntersection(temp.get_x(),temp.get_y());

			tempPoint = tempRoadPoints.get_neighbours(temp.get_x(), temp.get_y());
			
			for(i=0;i<tempPoint.size();i++){
				
				dist = temp.get_d() + haversine(temp.get_x(),temp.get_y(),tempPoint.get(i).x,tempPoint.get(i).y);
				
				key = tempRoadPoints.get_key(tempPoint.get(i).x, tempPoint.get(i).y);

				if(Distances.containsKey(key) && Distances.get(key)<dist+0.0001){
					continue;
				}

				dist2 = haversine(taxi_x, taxi_y, tempPoint.get(i).x, tempPoint.get(i).y);
				s = temp.get_Path() + " " + Double.toString(tempPoint.get(i).x) + "," + Double.toString(tempPoint.get(i).y);

				temp2 = new Step_info(dist,tempPoint.get(i).x,tempPoint.get(i).y,temp.get_id(),dist2,s);
				flag = lista.push(temp2);
				if(flag){
					Distances.put(key,dist);
				}

			}

			for(j=0;j<tempv.size();j++){
				tempRoadPoints = Thing.get_RoadPoints(tempv.get(j));
				tempPoint = tempRoadPoints.get_neighbours(temp.get_x(), temp.get_y());
				
				for(i=0;i<tempPoint.size();i++){
				
					dist = temp.get_d() + haversine(temp.get_x(),temp.get_y(),tempPoint.get(i).x,tempPoint.get(i).y);
					
					key = tempRoadPoints.get_key(tempPoint.get(i).x, tempPoint.get(i).y);

					if(Distances.containsKey(key) && Distances.get(key)<dist+0.0001){
						continue;
					}

					dist2 = haversine(taxi_x, taxi_y, tempPoint.get(i).x, tempPoint.get(i).y);
					s = temp.get_Path() + " " + Double.toString(tempPoint.get(i).x) + "," + Double.toString(tempPoint.get(i).y);

					temp2 = new Step_info(dist,tempPoint.get(i).x,tempPoint.get(i).y,tempv.get(j),dist2,s);
					flag = lista.push(temp2);
					if(flag){
						Distances.put(key,dist);
					}
				}
			}
			if(maxsize<lista.size()){
				maxsize=lista.size();
			}
			
		}
		
		if(!lista.empty()){
			Route = new String(lista.top().get_Path());
			Distance = lista.top().get_d();
		}
		else{
			Route = new String("");
			Distance=100000.0;
		}
		Thing = null;
	}

	public double get_Distance(){
		return Distance;
	}

	public String get_Route(){
		return Route;
	}

	public int get_steps(){
		return steps;
	}

	public int get_maxsize(){
		return maxsize;
	}
}