package app;


import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
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
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.HashMap;

import javax.imageio.ImageIO;

import reader.PropertyFileReader;
import reader.XmlReader;
import twitter4j.StatusUpdate;
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
        

		
        //Java2D
        /* �摜�̏�ɔ������̂ڂ₯���������`�悷�� */

        BufferedImage img = ImageIO.read(new File("/Users/ryosuke/Desktop/workspace/Cf7MZggUIAAV-2k.jpg"));
        Graphics2D gr = img.createGraphics();

        /* ������`��p��BufferedImage���쐬 */
        BufferedImage img2 = new BufferedImage(
            img.getWidth(), img.getHeight(),
            BufferedImage.TYPE_INT_ARGB_PRE
        );
        Graphics2D gr2 = img2.createGraphics();
        /* �������`�� */
        gr2.setColor(new Color(0x00, 0x00, 0x00, 0xf0));
        gr2.setFont(new Font(Font.DIALOG, Font.BOLD, 20));
        gr2.drawString("�������s6/14�̓V�C", 5, 20);
        gr2.drawString("�쓌�̕��@������@���ɂ��@��@�J", 5, 60);
        gr2.drawString("���C��", 5, 100);
        gr2.drawString("�ō��F27��", 5, 140);
        gr2.drawString("�Œ�F18��", 5, 180);
        gr2.drawString("���~���m��", 5, 220);
        gr2.drawString("00~�F0%", 5, 260);
        gr2.drawString("06~�F10%", 5, 300);
        gr2.drawString("12~�F20%", 5, 340);
        gr2.drawString("18~�F30%", 5, 380);
        
        gr2.setColor(new Color(0xff, 0xff, 0xff, 0xf0));
        gr2.setFont(new Font(Font.DIALOG, Font.BOLD, 20));
        gr2.drawString("�������s6/14�̓V�C", 7, 21);
        gr2.drawString("�쓌�̕��@������@���ɂ��@��@�J", 7, 61);
        gr2.drawString("���C��", 7, 101);
        gr2.drawString("�ō��F27��", 7, 141);
        gr2.drawString("�Œ�F18��", 7, 181);
        gr2.drawString("���~���m��", 7, 221);
        gr2.drawString("00~�F0%", 7, 261);
        gr2.drawString("06~�F10%", 7, 301);
        gr2.drawString("12~�F20%", 7, 341);
        gr2.drawString("18~�F30%", 7, 381);
        
        
        gr2.dispose();

        //����
        gr.drawImage(img2, 0, 0, null);
        gr.dispose();

        ImageIO.write(img, "jpg", new File("test.jpg"));
        
        
		//�摜�t���̃c�C�[�g������B
		FileSystem fs = FileSystems.getDefault();
        Path path = fs.getPath("test.jpg");
        File file = path.toFile();
        
        
        
        //�摜�t���c�C�[�g
        //twitter.updateStatus(new StatusUpdate("�����t���摜�c�C�[�g�Ă��ƁI").media(file));
        
		//���[�Ƃ��Ă݂�
		//twitter.updateStatus("�Ă��ƁB");
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