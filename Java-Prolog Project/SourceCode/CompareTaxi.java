import java.util.Comparator;

public class CompareTaxi implements Comparator<Taxi>{

	private boolean time;

	public CompareTaxi(boolean b){
		time = b;
	}

	@Override
	public int compare(Taxi o1, Taxi o2) {

		if(time){
			return (int) (100*(o1.get_Time()-o2.get_Time()));
		}
		else{
			if(o1.get_rating()>o2.get_rating()){
				return -1;
			}
			else {
				return 1;
			}
		}
	}

}
