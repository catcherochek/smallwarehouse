package model.providers;

import java.util.ArrayList;
import java.util.Iterator;

public class toolProv {
	public static ArrayList<String> deleteDulicates(ArrayList<String> in){
		ArrayList out = new ArrayList();
		

		Iterator<String> iterator = (Iterator)in.iterator();

		        while (iterator.hasNext())
		        {
		            String o = iterator.next();
		            Iterator<String> iterator2 = (Iterator)out.iterator();
		            boolean ok = true;
		            while (iterator2.hasNext())
		            {
		            	String s = iterator2.next();
		            	if (s.equals(o)) {
		            		ok = false;
		            	}
		            }
		            if (ok) {out.add(o);}
		            
		        }
		return out;
	} 
}
