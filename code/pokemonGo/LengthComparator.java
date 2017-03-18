package pokemonGo;
/*
	The comparator for priority queue used in MST approximation
	created by Yichuan Wang
 */
import java.util.Comparator;

public class LengthComparator implements Comparator<Edge>{
	@Override
	public int compare(Edge x, Edge y){
		
		if (x.getWeight() < y.getWeight()){
			return -1;
	    }
	    if (x.getWeight() > y.getWeight()){
	        return 1;
	    }
	    return 0;
	}
}

