package in.vnl.api.config;

import java.util.Comparator;

public class NeighbourUarfcn implements Comparable<NeighbourUarfcn>{
	
	private int uarfcn;
	private int psc;
	private int count;
	
	public NeighbourUarfcn() 
	{
		
	}
	
	public NeighbourUarfcn(int uarfcn,int psc,int count) 
	{
		this.uarfcn = uarfcn;
		this.psc = psc;
		this.count = count;
	}

	public int getUarfcn() {
		return uarfcn;
	}

	public void setUarfcn(int uarfcn) {
		this.uarfcn = uarfcn;
	}

	public int getPsc() {
		return psc;
	}

	public void setPsc(int psc) {
		this.psc = psc;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}
	
	
	@Override
    public int compareTo(NeighbourUarfcn na) 
	{
        return (this.count - na.count);
    }
	
    public static Comparator<NeighbourUarfcn> ascending = new Comparator<NeighbourUarfcn>() {

        @Override
        public int compare(NeighbourUarfcn c1, NeighbourUarfcn c2) {
            return c1.getCount() - c2.getCount();
        }
    };
    
    public static Comparator<NeighbourUarfcn> descending = new Comparator<NeighbourUarfcn>() {

        @Override
        public int compare(NeighbourUarfcn c1, NeighbourUarfcn c2) {
            return c2.getCount() - c1.getCount();
        }
    };
	
	

}
