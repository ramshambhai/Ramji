package in.vnl.api.config;

import java.util.Comparator;

public class NeighbourEarfcn implements Comparable<NeighbourEarfcn>{
	
	private int earfcn;
	private int pci;
	private int count;
	
	public NeighbourEarfcn() 
	{
		
	}
	
	public NeighbourEarfcn(int earfcn,int pci,int count) 
	{
		this.earfcn = earfcn;
		this.pci = pci;
		this.count = count;
	}

	public int getEarfcn() {
		return earfcn;
	}

	public void setEarfcn(int earfcn) {
		this.earfcn = earfcn;
	}

	public int getPci() {
		return pci;
	}

	public void setPci(int pci) {
		this.pci = pci;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}
	
	
	@Override
    public int compareTo(NeighbourEarfcn na) 
	{
        return (this.count - na.count);
    }
	
    public static Comparator<NeighbourEarfcn> ascending = new Comparator<NeighbourEarfcn>() {

        @Override
        public int compare(NeighbourEarfcn c1, NeighbourEarfcn c2) {
            return c1.getCount() - c2.getCount();
        }
    };
    
    public static Comparator<NeighbourEarfcn> descending = new Comparator<NeighbourEarfcn>() {

        @Override
        public int compare(NeighbourEarfcn c1, NeighbourEarfcn c2) {
            return c2.getCount() - c1.getCount();
        }
    };
}
