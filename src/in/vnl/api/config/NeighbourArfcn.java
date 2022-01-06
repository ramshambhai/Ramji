package in.vnl.api.config;

import java.util.Comparator;

public class NeighbourArfcn implements Comparable<NeighbourArfcn>{
	
	public int arfcn;
	public int count;
	
	public NeighbourArfcn() 
	{
		
	}
	
	public NeighbourArfcn(int arfcn, int count) {
		super();
		this.arfcn = arfcn;
		this.count = count;
	}
	
	public int getArfcn() {
		return arfcn;
	}
	public void setArfcn(int arfcn) {
		this.arfcn = arfcn;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	
	@Override
    public int compareTo(NeighbourArfcn na) {
		
        return (this.count - na.count);
    }
	
    public static Comparator<NeighbourArfcn> ascending = new Comparator<NeighbourArfcn>() {

        @Override
        public int compare(NeighbourArfcn c1, NeighbourArfcn c2) {
            return c1.getCount() - c2.getCount();
        }
    };
    
    public static Comparator<NeighbourArfcn> descending = new Comparator<NeighbourArfcn>() {

        @Override
        public int compare(NeighbourArfcn c1, NeighbourArfcn c2) {
            return c2.getCount() - c1.getCount();
        }
    };
}
