package main.java.app;

import java.util.ArrayList;

public class WeekTenkiInfo {

	private String prefectures = "“Œ‹";
	private String area = "“Œ‹’n•û";
	private ArrayList<String> dateArray = new ArrayList<String>();
	private ArrayList<String> weatherArray = new ArrayList<String>();
	private ArrayList<String> maxTemperatureArray = new ArrayList<String>();
	private ArrayList<String> minTemperatureArray = new ArrayList<String>();
	

	@Override
	public String toString() {
		return "y" + prefectures + "(" + area + ")";
	}

	public void setPrefectures(String prefectures) {
		this.prefectures = prefectures;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public void setDate(String date) {
		//this.date = date;
		this.dateArray.add(area);
	}

	public void setWeather(String weather) {
		//this.weather = weather;
		this.weatherArray.add(area);
	}
	
	public void setMaxTemperature(String weather) {
		//this.weather = weather;
		this.maxTemperatureArray.add(area);
	}
	
	public void setMinTemperature(String weather) {
		//this.weather = weather;
		this.minTemperatureArray.add(area);
	}



}
