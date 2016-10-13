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

		//���[�g�v�f�̃m�[�h�����擾����
		System.out.println("�m�[�h���F" +root.getNodeName());

		//���[�g�v�f�̑������擾����
		System.out.println("���[�g�v�f�̑����F" + root.getAttribute("pref"));

		System.out.println("���[�g�v�f�̑����F" + root.getAttribute("pref"));
		System.out.println("�����m�[�h���擾�F" + root.getAttributeNode("pref"));
		System.out.println("�q���v�f�S�Ă��܂� NodeList�F" + root.getElementsByTagName("pref"));
		//���[�g�v�f�̎q�m�[�h���擾����
		NodeList rootChildren = root.getChildNodes();//��

		System.out.println("�q�v�f�̐��F" + rootChildren.getLength());
		
		
		
		//�����̓��t���擾
		Calendar cal1 = Calendar.getInstance();  //(1)�I�u�W�F�N�g�̐���

	    int year = cal1.get(Calendar.YEAR);        //(2)���݂̔N���擾
	    int month = cal1.get(Calendar.MONTH) + 1;  //(3)���݂̌����擾
	    int day = cal1.get(Calendar.DATE);         //(4)���݂̓����擾
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
	    
	    System.out.println("�����̓��t:" + today);
		System.out.println("------------------");

		for(int i=0; i < rootChildren.getLength(); i++) {
			Node node = rootChildren.item(i);
			//System.out.println(node.getNodeName());
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				Element element = (Element)node;
				System.out.println(element.getNodeName());
				if (element.getNodeName().equals("pref")) {
					System.out.println("�s���{���F" + element.getAttribute("id"));
					info.setPrefectures(element.getAttribute("id"));//�n����Z�b�g
					
					NodeList prefChildren = node.getChildNodes();//��
					for(int j=0; j < prefChildren.getLength(); j++) {
						Node node2 = prefChildren.item(j);
						//System.out.println(node2.getNodeName());
						if (node2.getNodeType() == Node.ELEMENT_NODE) {
							Element element2 = (Element)node2;
							System.out.println(element2.getAttribute("id"));
							if (element2.getNodeName().equals("area") && element2.getAttribute("id").equals("�����n��")) {//�����n��
								System.out.println("�n��F" + element2.getAttribute("id"));
								info.setArea(element2.getAttribute("id"));//�n����Z�b�g
								//NodeList personChildren = node.getChildNodes();
								
								NodeList areaChildren = node2.getChildNodes();//��
								for(int k=0; k < areaChildren.getLength(); k++) {
									Node node3 = areaChildren.item(k);
									if (node3.getNodeType() == Node.ELEMENT_NODE) {
										Element element3 = (Element)node3;
										

									    System.out.println("element3.getAttribute(date):"+ element3.getAttribute("date"));
									    System.out.println("element3.getNodeName():" + element3.getNodeName());
									    
										if (element3.getNodeName().equals("info") && element3.getAttribute("date").equals(today)) {
											System.out.println(element3.getAttribute("date"));
											info.setDate(month + "/" + day);//���t���Z�b�g
											NodeList todayChildren = node3.getChildNodes();//��
											for(int l=0; l < todayChildren.getLength(); l++) {
												Node node4 = todayChildren.item(l);
												
												if (node4.getNodeType() == Node.ELEMENT_NODE) {
													Element element4 = (Element)node4;
													//System.out.println("element4.getAttribute(range):"+ element4.getAttribute("weather"));
												    System.out.println("element4.getNodeName():" + element4.getNodeName());
													if (node4.getNodeName().equals("weather")) {
														System.out.println("�V�C�F" + element4.getTextContent());
														info.setWeather(element4.getTextContent());//�V�C���Z�b�g
													}else if (node4.getNodeName().equals("weather_detail")) {
														System.out.println("�ڍׁF" + element4.getTextContent());
														info.setWeatherDetail(element4.getTextContent());//�ڍ׏��Z�b�g
													}else if(node4.getNodeName().equals("temperature")){
														NodeList temperatureChildren = node4.getChildNodes();//��
														for(int m=0; m < temperatureChildren.getLength(); m++) {
															Node node5 = temperatureChildren.item(m);
															if (node5.getNodeType() == Node.ELEMENT_NODE) {
																Element element5 = (Element)node5;
																if(element5.getAttribute("centigrade").equals("max")){
																	System.out.println("�ō��C���F" + element5.getTextContent());
																	info.setMaxTemperature(element5.getTextContent());//�ō��C���Z�b�g
																}else if(element5.getAttribute("centigrade").equals("min")){
																	System.out.println("�Œ�C���F" + element5.getTextContent());
																	info.setMinTemperature(element5.getTextContent());//�ō��C���Z�b�g
																}
															}
														}
													}else if(node4.getNodeName().equals("rainfallchance")){
														NodeList rainChildren = node4.getChildNodes();//��
														for(int m=0; m < rainChildren.getLength(); m++) {
															Node node5 = rainChildren.item(m);
															if (node5.getNodeType() == Node.ELEMENT_NODE) {
																Element element5 = (Element)node5;
																if(element5.getAttribute("hour").equals("00-06")){
																	System.out.println("00-06�F" + element5.getTextContent());
																	info.setRainfallchance00(element5.getTextContent());//�~���m���Z�b�g
																}else if(element5.getAttribute("hour").equals("06-12")){
																	System.out.println("06-12�F" + element5.getTextContent());
																	info.setRainfallchance06(element5.getTextContent());//�~���m���Z�b�g
																}else if(element5.getAttribute("hour").equals("12-18")){
																	System.out.println("12-18�F" + element5.getTextContent());
																	info.setRainfallchance12(element5.getTextContent());//�~���m���Z�b�g
																}else if(element5.getAttribute("hour").equals("18-24")){
																	System.out.println("18-24�F" + element5.getTextContent());
																	info.setRainfallchance18(element5.getTextContent());//�~���m���Z�b�g
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