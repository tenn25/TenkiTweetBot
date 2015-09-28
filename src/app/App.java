package app;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.URL;

import reader.PropertyFileReader;
import reader.XmlReader;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.User;

public class App {
	public static void main(String[] args) throws Exception {

		String propertyFilePath = "searchSetting";
		// �v���p�e�B�t�@�C���ǂݍ���
		PropertyFileReader property = new PropertyFileReader(propertyFilePath);
		System.out.println("�A�J�E���g���F" + property.getAccountName());

		Twitter twitter = new TwitterFactory().getInstance();
		User user = twitter.verifyCredentials();
		
		//���[�U���擾
        System.out.println("�Ȃ܂��@�@�@�F" + user.getName());
        System.out.println("�Ђ傤�����@�F" + user.getScreenName());
        System.err.println("�ӂ���[���@�F" + user.getFriendsCount());
        System.out.println("�ӂ����[���F" + user.getFollowersCount());
		
        //�V�CAPI�̓����f�[�^��URL���擾
        URL url = new URL(property.getWheatherInfoPath());
        String str = getSourceText(url);

        
        //HTML���t�@�C���ۑ�
        System.out.println(System.getProperty("file.encoding"));
        
        try{
        	  String filepath = property.getWheatherDataPath();
        	  File file = new File(filepath);
        	  PrintWriter pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file),"UTF-8")));
        	  pw.write(str);
        	  pw.close();
        	  
    	}catch(IOException e){
    	  System.out.println(e);
    	}
        
        
        TenkiInfo info = new TenkiInfo();
        
		XmlReader reader = new XmlReader();
		reader.domRead("tokyotenki.xml",info);
        
		
		//���[�Ƃ��Ă݂�
        twitter.updateStatus(info.toString());
        System.out.println(info.toString());
	}
	
	
	public static String getSourceText(URL url) throws IOException {
		InputStream in = url.openStream();
		StringBuilder sb = new StringBuilder();
		
		
		//String propertyFilePath = "searchSetting";
		//PropertyFileReader property = new PropertyFileReader(propertyFilePath);
		  //String filepath = property.getWheatherDataPath();
		  //File file = new File(filepath);
		  //BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file),"UTF-8"));
		  
  	  
		try {
		BufferedReader bf = new BufferedReader(new InputStreamReader(in, "UTF-8"));
		String s;
		while ((s=bf.readLine())!=null) {
		sb.append(s);
		sb.append("\n");
		//System.out.println(s);
		//bw.write(s);
		//bw.newLine();
		}
		} finally {
		in.close();
		}
		String str = sb.toString();
		System.out.println(str);
		return str;
		}
}