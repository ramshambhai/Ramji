package in.vnl.api.common;

import java.util.Date;

public class HummerCue {
	double frequency;
	double bandwidth;
	String sector;
	Date date;
	long validity;
   
public HummerCue(double frequency, double bandwidth, String sector, Date date, long validity) {
	this.frequency = frequency;
	this.bandwidth = bandwidth;
	this.sector = sector;
	this.date = date;
	this.validity = validity;
}
public double getFrequency() {
	return frequency;
}
public void setFrequency(double frequency) {
	this.frequency = frequency;
}
public double getBandwidth() {
	return bandwidth;
}
public void setBandwidth(double bandwidth) {
	this.bandwidth = bandwidth;
}
public String getSector() {
	return sector;
}
public void setSector(String sector) {
	this.sector = sector;
}
public Date getDate() {
	return date;
}
public void setDate(Date date) {
	this.date = date;
}
public long getValidity() {
	return validity;
}
public void setValidity(long validity) {
	this.validity = validity;
}
@Override
	public String toString() {
		return "HummerCue [frequency=" + frequency + ", bandwidth=" + bandwidth + ", sector=" + sector + ", date="
				+ date + ", validity=" + validity + "]";
	}

}
