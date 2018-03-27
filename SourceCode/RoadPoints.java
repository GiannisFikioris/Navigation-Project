import java.util.*;

public class RoadPoints {

	String RoadName;
	private Vector<Double> x,y;
	private Vector<Intersection> CrossRoad;
	private Vector<Integer> key;

	RoadPoints(String s){
		x = new Vector<Double>();
		y = new Vector<Double>();
		key = new Vector<Integer>();
		RoadName = new String(s);
		CrossRoad = new Vector<Intersection>();
	}

	public void addPoint(Double xx, Double yy, int index){
		x.addElement(xx);
		y.addElement(yy);
		key.addElement(index);
		CrossRoad.addElement(new Intersection());
	}

	public void addinIntersections(Double xx, Double yy, Vector<Integer> Ids){
		int i=0;
		int size=x.size();
		while(Math.abs(y.get(i)-yy)>0.000000001 || Math.abs(x.get(i)-xx)>0.000000001){
			i++;
			if (i==size) {
				System.out.println("Error");
				return;
			}
		}
		CrossRoad.elementAt(i).change(Ids);
	}

	public String getName(){
		return new String(RoadName);
	}

	public int get_key(Double xx, Double yy){
		int i=0;
		int size=x.size();
		while(Math.abs(y.get(i)-yy)>0.000000001 || Math.abs(x.get(i)-xx)>0.000000001){			
			i++;
			if (i==size) {
				System.out.println("Error ");
				return -1;
			}
		}
		return key.get(i);
	}

	public Vector<Integer> getIntersection(Double xx, Double yy){
		int i=0;
		int size=x.size();
		while(Math.abs(y.get(i)-yy)>0.000000001 || Math.abs(x.get(i)-xx)>0.000000001){			
			i++;
			if (i==size) {
				System.out.println("Error ");
				return null;
			}
		}

		return CrossRoad.get(i).getIntersection();
	}

	public Vector<Point> get_neighbours(double xx, double yy){
		int i=0;
		int size=x.size();
		Vector<Point> v = new Vector<Point>();
		while(Math.abs(y.get(i)-yy)>0.000000001 || Math.abs(x.get(i)-xx)>0.000000001){
			i++;
			if (i==size) {
				System.out.println("Error");
				break;
			}
		}
		
		if(i>0){
			v.addElement(new Point(x.get(i-1),y.get(i-1)));
		}
		if(i<size-1){
			v.addElement(new Point(x.get(i+1),y.get(i+1)));
		}
		return v;
	}
}
