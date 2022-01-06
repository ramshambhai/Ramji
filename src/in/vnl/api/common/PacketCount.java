package in.vnl.api.common;

class PacketCount{
	private int count2g;
	private int initIndex2g;
	private int count3g;
	private int initIndex3g;
	private int count4g;
	private int initIndex4g;
	
	public int getCount2g() {
		return count2g;
	}
	public void setCount2g(int count2g) {
		this.count2g = count2g;
	}
	public int getInitIndex2g() {
		return initIndex2g;
	}
	public void setInitIndex2g(int initIndex2g) {
		this.initIndex2g = initIndex2g;
	}
	public int getCount3g() {
		return count3g;
	}
	public void setCount3g(int count3g) {
		this.count3g = count3g;
	}
	public int getInitIndex3g() {
		return initIndex3g;
	}
	public void setInitIndex3g(int initIndex3g) {
		this.initIndex3g = initIndex3g;
	}
	
	public int getCount4g() {
		return count4g;
	}
	public void setCount4g(int count4g) {
		this.count4g = count4g;
	}
	public int getInitIndex4g() {
		return initIndex4g;
	}
	public void setInitIndex4g(int initIndex4g) {
		this.initIndex4g = initIndex4g;
	}
	public PacketCount(int count2g, int initIndex2g, int count3g, int initIndex3g, int count4g, int initIndex4g) {
		super();
		this.count2g = count2g;
		this.initIndex2g = initIndex2g;
		this.count3g = count3g;
		this.initIndex3g = initIndex3g;
		this.count4g = count4g;
		this.initIndex4g = initIndex4g;
	}
}