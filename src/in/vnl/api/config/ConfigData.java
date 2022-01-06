package in.vnl.api.config;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;

public class ConfigData {
	
	private String id;
	private ArrayList< HashMap<String,String>> data;
	private String duration;
	private String tech;
	private String plmn;
	private String check;
	private String antennaId;
	private String antennaName;

	public ConfigData() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ConfigData(String id, ArrayList< HashMap<String,String>> data, String duration, String tech,String plmn) {
		super();
		this.id = id;
		this.data = data;
		this.duration = duration;
		this.tech = tech;
		this.plmn = plmn;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public ArrayList< HashMap<String,String>> getData() {
		return data;
	}
	public void setData(ArrayList< HashMap<String,String>> data) {
		this.data = data;
	}
	public String getDuration() {
		return duration;
	}
	public void setDuration(String duration) {
		this.duration = duration;
	}
	public String getTech() {
		return tech;
	}
	public void setTech(String tech) {
		this.tech = tech;
	}
	public String getPlmn() {
		return plmn;
	}
	public void setPlmn(String plmn) {
		this.plmn = plmn;
	}
	public String getCheck() {
		return check;
	}
	public void setCheck(String check) {
		this.check = check;
	}
	
	public String getAntennaId() {
		return antennaId;
	}

	public void setAntennaId(String antennaId) {
		this.antennaId = antennaId;
	}
	
	public String getAntennaName() {
		return antennaName;
	}

	public void setAntennaName(String antennaName) {
		this.antennaName = antennaName;
	}
}
