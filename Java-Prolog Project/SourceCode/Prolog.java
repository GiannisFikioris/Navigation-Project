import java.util.Vector;

import com.ugos.jiprolog.engine.JIPEngine;
import com.ugos.jiprolog.engine.JIPQuery;
import com.ugos.jiprolog.engine.JIPTerm;
import com.ugos.jiprolog.engine.JIPTermParser;

public class Prolog {
	
	JIPEngine jip;

	public Prolog(String filename){
		
		jip = new JIPEngine();
		jip.consultFile("pro.pl");
		jip.consultFile(filename);
		jip.consultFile("nextinfo.pl");
	}
	

	public Vector<Answer> getnext(long nodeid, int hour){

		JIPTermParser parser = jip.getTermParser();
		String s,id,speed,light,tl,tr;
		Vector<Answer> V = new Vector<Answer>();
		
		s = new String("canMoveFromTo("+nodeid+","+hour+",Nextid,Maxspeed,Lit,Toll,Traffic).");

		JIPQuery jipQuery; 
		JIPTerm term;

		jipQuery = jip.openSynchronousQuery(parser.parseTerm(s));
		
		term = jipQuery.nextSolution();
		while (term != null) {
			
			id = term.getVariablesTable().get("Nextid").toString();
			speed = term.getVariablesTable().get("Maxspeed").toString();
			light = term.getVariablesTable().get("Lit").toString();
			tl = term.getVariablesTable().get("Toll").toString();
			tr = term.getVariablesTable().get("Traffic").toString();
			
			//System.out.println("id " + id +" speed " + speed +" light " + light +" tl " + tl);
			
			V.addElement(new Answer(id, speed, light, tl, tr));
			
			term = jipQuery.nextSolution();
		}
		
		return V;
	}

}
