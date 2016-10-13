package main.java.app;

public class TenkiInfo {

	private String prefectures = "東京";
	private String area = "東京地方";
	private String date = "2015\01\01";
	private String weather = "晴れ";
	private String weatherDetail = "晴れ　時々 曇り";
	private String maxTemperature = "00";
	private String minTemperature = "00";
	private String rainfallchance00 = "50";
	private String rainfallchance06 = "50";
	private String rainfallchance12 = "50";
	private String rainfallchance18 = "50";
	
	@Override
	public String toString() {
		return "■" + prefectures + "(" + area + ")" + date + "の天気\n" + weatherDetail + "\n■気温\n最高：" + maxTemperature + "℃\n最低：" + minTemperature + "℃\n■降水確率\n00~：" + rainfallchance00 + "%\n06~：" + rainfallchance06 + "%\n12~：" + rainfallchance12 + "%\n18~：" + rainfallchance18 + "%";
	}
	
	public String getInfomation(){
		if(Integer.parseInt(rainfallchance12) >80
				||Integer.parseInt(rainfallchance18) >= 80){
					return "\n天気悪い☔️☔️☔ ️️️️️️";
		}
		if(Integer.parseInt(rainfallchance12) >= 60
				||Integer.parseInt(rainfallchance18) >= 60){
					return "\n今日は雨降るよ☔☔ ️️️";
		}
		else if(Integer.parseInt(rainfallchance12) >= 40
			||Integer.parseInt(rainfallchance18) >= 40){
				return "\n午後雨降るかも？☔ ️";
		}
		else if(Integer.parseInt(rainfallchance06) >= 40){
				return "\nあんま天気良くない☁️ ️";
		}
		else if(Integer.parseInt(rainfallchance12) >= 20
				||Integer.parseInt(rainfallchance18) >= 20){
					return "\n今日は雨降らないと思う⛅　️";
		}
		else{
			return "\n今日はめっちゃ晴れ☀️　️";
		}
	}

	public String getPrefectures() {
		return prefectures;
	}
	public void setPrefectures(String prefectures) {
		this.prefectures = prefectures;
	}
	public String getArea() {
		return area;
	}
	public void setArea(String area) {
		this.area = area;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getWeather() {
		return weather;
	}
	public void setWeather(String weather) {
		this.weather = weather;
	}
	public String getWeatherDetail() {
		return weatherDetail;
	}
	public void setWeatherDetail(String weatherDetail) {
		this.weatherDetail = weatherDetail;
	}

	public String getMaxTemperature() {
		return maxTemperature;
	}

	public void setMaxTemperature(String maxTemperature) {
		this.maxTemperature = maxTemperature;
	}

	public String getMinTemperature() {
		return minTemperature;
	}

	public void setMinTemperature(String minTemperature) {
		this.minTemperature = minTemperature;
	}

	public String getRainfallchance00() {
		return rainfallchance00;
	}

	public void setRainfallchance00(String rainfallchance00) {
		this.rainfallchance00 = rainfallchance00;
	}

	public String getRainfallchance06() {
		return rainfallchance06;
	}

	public void setRainfallchance06(String rainfallchance06) {
		this.rainfallchance06 = rainfallchance06;
	}

	public String getRainfallchance12() {
		return rainfallchance12;
	}

	public void setRainfallchance12(String rainfallchance12) {
		this.rainfallchance12 = rainfallchance12;
	}

	public String getRainfallchance18() {
		return rainfallchance18;
	}

	public void setRainfallchance18(String rainfallchance18) {
		this.rainfallchance18 = rainfallchance18;
	}



}
