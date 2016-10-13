package main.java.reader;

import java.io.IOException;
import java.util.Calendar;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import main.java.app.TenkiInfo;

public class XmlReader {
	public void domRead(String file,TenkiInfo info) throws SAXException, IOException, ParserConfigurationException {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder documentBuilder = factory.newDocumentBuilder();
		Document document = documentBuilder.parse(file);

		Element root = document.getDocumentElement();

		//ルート要素のノード名を取得する
		System.out.println("ノード名：" +root.getNodeName());

		//ルート要素の属性を取得する
		System.out.println("ルート要素の属性：" + root.getAttribute("pref"));

		System.out.println("ルート要素の属性：" + root.getAttribute("pref"));
		System.out.println("属性ノードを取得：" + root.getAttributeNode("pref"));
		System.out.println("子孫要素全てを含む NodeList：" + root.getElementsByTagName("pref"));
		//ルート要素の子ノードを取得する
		NodeList rootChildren = root.getChildNodes();//★

		System.out.println("子要素の数：" + rootChildren.getLength());
		
		
		
		//今日の日付を取得
		Calendar cal1 = Calendar.getInstance();  //(1)オブジェクトの生成

	    int year = cal1.get(Calendar.YEAR);        //(2)現在の年を取得
	    int month = cal1.get(Calendar.MONTH) + 1;  //(3)現在の月を取得
	    int day = cal1.get(Calendar.DATE);         //(4)現在の日を取得
	    String month2;
	    if(month < 10){
	    	month2 = "0" + month;
	    }else{
	    	month2 = "" + month;
	    }
	    
	    String day2;
	    if(day < 10){
	    	day2 = "0" + day;
	    }else{
	    	day2 = "" + day;
	    }

	    String today;
	    today = year + "/" + month2 + "/" + day2;
	    
	    System.out.println("今日の日付:" + today);
		System.out.println("------------------");

		for(int i=0; i < rootChildren.getLength(); i++) {
			Node node = rootChildren.item(i);
			//System.out.println(node.getNodeName());
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				Element element = (Element)node;
				System.out.println(element.getNodeName());
				if (element.getNodeName().equals("pref")) {
					System.out.println("都道府県：" + element.getAttribute("id"));
					info.setPrefectures(element.getAttribute("id"));//地域情報セット
					
					NodeList prefChildren = node.getChildNodes();//★
					for(int j=0; j < prefChildren.getLength(); j++) {
						Node node2 = prefChildren.item(j);
						//System.out.println(node2.getNodeName());
						if (node2.getNodeType() == Node.ELEMENT_NODE) {
							Element element2 = (Element)node2;
							System.out.println(element2.getAttribute("id"));
							if (element2.getNodeName().equals("area") && element2.getAttribute("id").equals("東京地方")) {//東京地方
								System.out.println("地域：" + element2.getAttribute("id"));
								info.setArea(element2.getAttribute("id"));//地域情報セット
								//NodeList personChildren = node.getChildNodes();
								
								NodeList areaChildren = node2.getChildNodes();//★
								for(int k=0; k < areaChildren.getLength(); k++) {
									Node node3 = areaChildren.item(k);
									if (node3.getNodeType() == Node.ELEMENT_NODE) {
										Element element3 = (Element)node3;
										

									    System.out.println("element3.getAttribute(date):"+ element3.getAttribute("date"));
									    System.out.println("element3.getNodeName():" + element3.getNodeName());
									    
										if (element3.getNodeName().equals("info") && element3.getAttribute("date").equals(today)) {
											System.out.println(element3.getAttribute("date"));
											info.setDate(month + "/" + day);//日付情報セット
											NodeList todayChildren = node3.getChildNodes();//★
											for(int l=0; l < todayChildren.getLength(); l++) {
												Node node4 = todayChildren.item(l);
												
												if (node4.getNodeType() == Node.ELEMENT_NODE) {
													Element element4 = (Element)node4;
													//System.out.println("element4.getAttribute(range):"+ element4.getAttribute("weather"));
												    System.out.println("element4.getNodeName():" + element4.getNodeName());
													if (node4.getNodeName().equals("weather")) {
														System.out.println("天気：" + element4.getTextContent());
														info.setWeather(element4.getTextContent());//天気情報セット
													}else if (node4.getNodeName().equals("weather_detail")) {
														System.out.println("詳細：" + element4.getTextContent());
														info.setWeatherDetail(element4.getTextContent());//詳細情報セット
													}else if(node4.getNodeName().equals("temperature")){
														NodeList temperatureChildren = node4.getChildNodes();//★
														for(int m=0; m < temperatureChildren.getLength(); m++) {
															Node node5 = temperatureChildren.item(m);
															if (node5.getNodeType() == Node.ELEMENT_NODE) {
																Element element5 = (Element)node5;
																if(element5.getAttribute("centigrade").equals("max")){
																	System.out.println("最高気温：" + element5.getTextContent());
																	info.setMaxTemperature(element5.getTextContent());//最高気温セット
																}else if(element5.getAttribute("centigrade").equals("min")){
																	System.out.println("最低気温：" + element5.getTextContent());
																	info.setMinTemperature(element5.getTextContent());//最高気温セット
																}
															}
														}
													}else if(node4.getNodeName().equals("rainfallchance")){
														NodeList rainChildren = node4.getChildNodes();//★
														for(int m=0; m < rainChildren.getLength(); m++) {
															Node node5 = rainChildren.item(m);
															if (node5.getNodeType() == Node.ELEMENT_NODE) {
																Element element5 = (Element)node5;
																if(element5.getAttribute("hour").equals("00-06")){
																	System.out.println("00-06：" + element5.getTextContent());
																	info.setRainfallchance00(element5.getTextContent());//降水確率セット
																}else if(element5.getAttribute("hour").equals("06-12")){
																	System.out.println("06-12：" + element5.getTextContent());
																	info.setRainfallchance06(element5.getTextContent());//降水確率セット
																}else if(element5.getAttribute("hour").equals("12-18")){
																	System.out.println("12-18：" + element5.getTextContent());
																	info.setRainfallchance12(element5.getTextContent());//降水確率セット
																}else if(element5.getAttribute("hour").equals("18-24")){
																	System.out.println("18-24：" + element5.getTextContent());
																	info.setRainfallchance18(element5.getTextContent());//降水確率セット
																}
															}
														}
														
													}
												}
											}
											
											
										}
									}
								}
							}
						}
					}
					
					


					System.out.println("------------------");
				}
			}


		}


	}
}