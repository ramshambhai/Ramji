package in.vnl.api.config;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;

import org.json.JSONArray;
import org.json.JSONObject;

public class Cell implements Comparable<Cell>,Cloneable,Serializable{

	private String PLMN;
	private String MCC;
	private String MNC;
	private String LAC;
	private String CELL;
	private String band;
	private int arfcn;
	private int rssi;
	private int uarfcn;
	private int psc;
	private int earfcn;
	private int pci;
	private String tac;
	private String bsic ;
	private String lat;
	private String lon;
	private JSONObject Neighbour;
	private ArrayList<Cell> genratedNeighbours;
	private String opr;
	
	public Object clone()throws CloneNotSupportedException{  
		return super.clone();  
		}
	public Cell() {};
	public Cell(String pLMN, String mCC, String mNC, String lAC, String cELL, String band, int arfcn, int rssi,
			String lat, String lon, JSONObject neighbour,int uarfcn,int psc,String tac,int earfcn,int pci) {
		super();
		PLMN = pLMN;
		MCC = mCC;
		MNC = mNC;
		LAC = lAC;
		CELL = cELL;
		this.band = band;
		this.arfcn = arfcn;
		this.rssi = rssi;
		this.lat = lat;
		this.lon = lon;
		Neighbour = neighbour;
		this.uarfcn = uarfcn;
		this.psc = psc;
		this.tac = tac;
		this.earfcn = earfcn;
		this.pci = pci;
	}

	public String getPLMN() {
		return PLMN;
	}

	public void setPLMN(String pLMN) {
		PLMN = pLMN;
	}

	public String getMCC() {
		return MCC;
	}

	public void setMCC(String mCC) {
		MCC = mCC;
	}

	public String getMNC() {
		return MNC;
	}

	public void setMNC(String mNC) {
		MNC = mNC;
	}

	public String getLAC() {
		return LAC;
	}

	public void setLAC(String lAC) {
		LAC = lAC;
	}

	public String getCELL() {
		return CELL;
	}

	public void setCELL(String cELL) {
		CELL = cELL;
	}

	public String getBand() {
		return band;
	}

	public void setBand(String band) {
		this.band = band;
	}

	public int getArfcn() {
		return arfcn;
	}

	public void setArfcn(int arfcn) {
		this.arfcn = arfcn;
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
	
	public String getTac() {
		return tac;
	}
	public void setTac(String tac) {
		this.tac = tac;
	}
	
	public int getRssi() {
		return rssi;
	}

	public void setRssi(int rssi) {
		this.rssi = rssi;
	}

	public String getLat() {
		return lat;
	}

	public void setLat(String lat) {
		this.lat = lat;
	}

	public String getLon() {
		return lon;
	}

	public void setLon(String lon) {
		this.lon = lon;
	}
	
	public String getBsic() {
		return bsic;
	}

	public void setBsic(String bsic) {
		this.bsic = bsic;
	}

	public JSONObject getNeighbour() {
		return Neighbour;
	}

	public void setNeighbour(JSONObject neighbour) {
		Neighbour = neighbour;
	}
	
	
	public ArrayList<Cell> getGenratedNeighbours() {
		return genratedNeighbours;
	}

	public void setGenratedNeighbours(ArrayList<Cell> neighbour) {
		genratedNeighbours = neighbour;
	}
	
	public String getOpr() {
		return opr;
	}

	public void setOpr(String opr) {
		this.opr = opr;
	}
	
    public static Comparator<Cell> rssiAscending = new Comparator<Cell>() {

        @Override
        public int compare(Cell c1, Cell c2) {
            return c1.getRssi() - c2.getRssi();
        }
    };
    
    public static Comparator<Cell> rssiDescending = new Comparator<Cell>() {

        @Override
        public int compare(Cell c1, Cell c2) {
            return c2.getRssi() - c1.getRssi();
        }
    };
    
    
    public static Comparator<Cell> arfcnAscending = new Comparator<Cell>() {

        @Override
        public int compare(Cell c1, Cell c2) {
            return c1.getArfcn() - c2.getArfcn();
        }
    };
    
    public static Comparator<Cell> arfcnDescending = new Comparator<Cell>() {

        @Override
        public int compare(Cell c1, Cell c2) {
            return c2.getArfcn() - c1.getArfcn();
        }
    };
    
    public static Comparator<Cell> uarfcnAscending = new Comparator<Cell>() {

        @Override
        public int compare(Cell c1, Cell c2) {
            return c1.getUarfcn() - c2.getUarfcn();
        }
    };
    
    public static Comparator<Cell> uarfcnDescending = new Comparator<Cell>() {

        @Override
        public int compare(Cell c1, Cell c2) {
            return c2.getUarfcn() - c1.getUarfcn();
        }
    };
    
    public static Comparator<Cell> earfcnAscending = new Comparator<Cell>() {

        @Override
        public int compare(Cell c1, Cell c2) {
            return c1.getEarfcn() - c2.getEarfcn();
        }
    };
    
    public static Comparator<Cell> earfcnDescending = new Comparator<Cell>() {

        @Override
        public int compare(Cell c1, Cell c2) {
            return c2.getEarfcn() - c1.getEarfcn();
        }
    };

	@Override
	public int compareTo(Cell arg0) {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public String toString() {
		return "Cell [PLMN=" + PLMN + ", MCC=" + MCC + ", MNC=" + MNC + ", LAC=" + LAC + ", CELL=" + CELL + ", band="
				+ band + ", arfcn=" + arfcn + ", rssi=" + rssi + ", uarfcn=" + uarfcn + ", psc=" + psc + ", earfcn="
				+ earfcn + ", pci=" + pci + ", tac=" + tac + ", bsic=" + bsic +"]";
	}
	
	
	
	
}
