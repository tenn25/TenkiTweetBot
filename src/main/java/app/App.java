package main.java.app;


import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
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

import javax.imageio.ImageIO;

import main.java.reader.PropAwsKeys;
import main.java.reader.PropSearchKeys;
import main.java.reader.PropertyReader;
import main.java.reader.S3Reader;
import main.java.reader.XmlReader;
import twitter4j.StatusUpdate;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;

public class App {
	public static void main(String[] args) throws Exception {

		PropertyReader propReader = new PropertyReader();
		String accountName = propReader.getProp(PropSearchKeys.accountName);
		System.out.println(accountName);
		
		// �v���p�e�B�t�@�C���ǂݍ���
		System.out.println("�A�J�E���g���F" + propReader.getProp(PropSearchKeys.accountName));
		System.out.println("URL�F" + propReader.getProp(PropSearchKeys.wheatherInfoPath));
		Twitter twitter = new TwitterFactory().getInstance();
		
        //�V�CAPI�̓����f�[�^��URL���擾
        URL url = new URL(propReader.getProp(PropSearchKeys.wheatherInfoPath));
        String str = getSourceText(url);
        
        //HTMl�t�@�C����ǂݍ���
        try{
        	  String filepath = propReader.getProp(PropSearchKeys.wheatherDataPath);
        	  File file = new File(filepath);
        	  PrintWriter pw = new PrintWriter(
        			  new BufferedWriter(
        					  new OutputStreamWriter(
        							  new FileOutputStream(file),"UTF-8")
        					  )
        			  );
        	  pw.write(str);
        	  pw.close();
        
    	}catch(IOException e){
    	  System.out.println(e);
    	}
        
        
        TenkiInfo info = new TenkiInfo();
        
		XmlReader reader = new XmlReader();
		reader.domRead(propReader.getProp(PropSearchKeys.wheatherDataPath),info);
        
		//S3�ւ̐ڑ��ݒ�
		S3Reader s3reader = new S3Reader(
				propReader.getProp(PropAwsKeys.accessKey),
				propReader.getProp(PropAwsKeys.secretKey)
				);
		s3reader.dispBucketList();
		
		//S3�̉摜�������_���Ɏ擾
		BufferedImage img = ImageIO.read(
				s3reader.getS3ObjectRandam(
						propReader.getProp(PropAwsKeys.bucketName),
						propReader.getProp(PropAwsKeys.pngImgFolderPath)
						)
				);
        
		/* �摜�̏�ɔ������̂ڂ₯���������`�悷�� */
        //Java2D
		Graphics2D gr = img.createGraphics();

        /* ������`��p��BufferedImage���쐬 */
        BufferedImage img2 = new BufferedImage(
            img.getWidth(), img.getHeight(),
            BufferedImage.TYPE_INT_ARGB_PRE
        );
        
        //�����`��p�̕ϐ�
        int imgWidth = img.getWidth();
        int imgHeight = img.getHeight();
        
        int fontSize = 45;
        int padding = 13;
        int startWidth = 20;//�����̊J�n�ʒu(������)
        int startHeight = 0;//�����̊J�n�ʒu(�ォ��)
        int diffSizeX = 2;//��px���炷��
        int diffSizeY = 2;//��px���炷��
        System.out.println(imgHeight);
        System.out.println(imgWidth);
        Graphics2D gr2 = img2.createGraphics();
        
        //�����̎���4�ӂ����n�ň͂�
        for(int i = 0;i < 4; i++){
        	int tmpDiffSizeX = diffSizeX;
        	int tmpDiffSizeY = diffSizeY;
        	if(i == 2 || i == 3){
        		tmpDiffSizeX = -1 * diffSizeX;
        		
        	}
        	if(i == 1 || i == 3){
        		tmpDiffSizeY = -1 * diffSizeY;
        	}

        	
            gr2.setColor(new Color(0x00, 0x00, 0x00, 0xf0));//���n
            gr2.setFont(new Font(Font.DIALOG, Font.BOLD, fontSize));
            gr2.drawString("��" + info.getPrefectures() + info.getDate() + "�̓V�C", startWidth + tmpDiffSizeX, startHeight + (padding + fontSize) * 1 + tmpDiffSizeY);
            gr2.drawString(info.getWeatherDetail(), startWidth + tmpDiffSizeX, startHeight + (padding + fontSize) * 2 + tmpDiffSizeY);
            gr2.drawString("���C��", startWidth + tmpDiffSizeX, imgHeight - (padding + fontSize) * 8 + fontSize + tmpDiffSizeY);
            gr2.drawString("�@���F" + info.getMaxTemperature() + "��", startWidth + tmpDiffSizeX, imgHeight - (padding + fontSize) * 7 + fontSize + tmpDiffSizeY);
            gr2.drawString("�@��F" + info.getMinTemperature() + "��", startWidth + tmpDiffSizeX, imgHeight - (padding + fontSize) * 6 + fontSize + tmpDiffSizeY);
            gr2.drawString("���~���m��", startWidth + tmpDiffSizeX, imgHeight - (padding + fontSize) * 5 + fontSize + tmpDiffSizeY);
            gr2.drawString("�@00~�F" + info.getRainfallchance00() + "%", startWidth + tmpDiffSizeX, imgHeight - (padding + fontSize) * 4 + fontSize + tmpDiffSizeY);
            gr2.drawString("�@06~�F" + info.getRainfallchance06() + "%", startWidth + tmpDiffSizeX, imgHeight - (padding + fontSize) * 3 + fontSize + tmpDiffSizeY);
            gr2.drawString("�@12~�F" + info.getRainfallchance12() + "%", startWidth + tmpDiffSizeX, imgHeight - (padding + fontSize) * 2 + fontSize + tmpDiffSizeY);
            gr2.drawString("�@18~�F" + info.getRainfallchance18() + "%", startWidth + tmpDiffSizeX, imgHeight - (padding + fontSize) * 1 + fontSize + tmpDiffSizeY);               	
        }
        
        //�ォ�甒�n�ŕ����\��
        gr2.setColor(new Color(0xff, 0xff, 0xff, 0xf0));//���n
        gr2.setFont(new Font(Font.DIALOG, Font.BOLD, fontSize));
        gr2.drawString("��" + info.getPrefectures() + info.getDate() + "�̓V�C", startWidth, startHeight + (padding + fontSize) * 1);
        gr2.drawString(info.getWeatherDetail(), startWidth, startHeight + (padding + fontSize) * 2);
        gr2.drawString("���C��", startWidth, imgHeight - (padding + fontSize) * 8 + fontSize );
        gr2.drawString("�@���F" + info.getMaxTemperature() + "��", startWidth, imgHeight - (padding + fontSize) * 7 + fontSize);
        gr2.drawString("�@��F" + info.getMinTemperature() + "��", startWidth, imgHeight - (padding + fontSize) * 6 + fontSize);
        gr2.drawString("���~���m��", startWidth, imgHeight - (padding + fontSize) * 5 + fontSize);
        gr2.drawString("�@00~�F" + info.getRainfallchance00() + "%", startWidth, imgHeight - (padding + fontSize) * 4 + fontSize);
        gr2.drawString("�@06~�F" + info.getRainfallchance06() + "%", startWidth, imgHeight - (padding + fontSize) * 3 + fontSize);
        gr2.drawString("�@12~�F" + info.getRainfallchance12() + "%", startWidth, imgHeight - (padding + fontSize) * 2 + fontSize);
        gr2.drawString("�@18~�F" + info.getRainfallchance18() + "%", startWidth, imgHeight - (padding + fontSize) * 1 + fontSize);
       
        gr2.dispose();

        //����
        gr.drawImage(img2, 0, 0, null);
        gr.dispose();

        ImageIO.write(img, "png", new File("output.png"));
        
        
		//png�摜���o��
		FileSystem fs = FileSystems.getDefault();
        Path path = fs.getPath("output.png");
        File file = path.toFile();
                
        //�摜�t���c�C�[�g
        twitter.updateStatus(new StatusUpdate(info.getDate() + "��" + info.getPrefectures() + "�̓V�C����" + info.getInfomation() ).media(file));
		//twitter.updateStatus(info.toString());
        //System.out.println(info.toString());
	}
	

	
	public static String getSourceText(URL url) throws IOException {
		InputStream in = url.openStream();
		StringBuilder sb = new StringBuilder();
		
		

		  
  	  
		try {
		BufferedReader bf = new BufferedReader(new InputStreamReader(in, "UTF-8"));
		String s;
		while ((s=bf.readLine())!=null) {
		sb.append(s);
		sb.append("\n");
		}
		} finally {
		in.close();
		}
		String str = sb.toString();
		System.out.println(str);
		return str;
	}
	
}




