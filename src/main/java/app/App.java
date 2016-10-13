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
		
		// プロパティファイル読み込む
		System.out.println("アカウント名：" + propReader.getProp(PropSearchKeys.accountName));
		System.out.println("URL：" + propReader.getProp(PropSearchKeys.wheatherInfoPath));
		Twitter twitter = new TwitterFactory().getInstance();
		
        //天気APIの東京データのURLを取得
        URL url = new URL(propReader.getProp(PropSearchKeys.wheatherInfoPath));
        String str = getSourceText(url);
        
        //HTMlファイルを読み込む
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
        
		//S3への接続設定
		S3Reader s3reader = new S3Reader(
				propReader.getProp(PropAwsKeys.accessKey),
				propReader.getProp(PropAwsKeys.secretKey)
				);
		s3reader.dispBucketList();
		
		//S3の画像をランダムに取得
		BufferedImage img = ImageIO.read(
				s3reader.getS3ObjectRandam(
						propReader.getProp(PropAwsKeys.bucketName),
						propReader.getProp(PropAwsKeys.pngImgFolderPath)
						)
				);
        
		/* 画像の上に半透明のぼやけた文字列を描画する */
        //Java2D
		Graphics2D gr = img.createGraphics();

        /* 文字列描画用のBufferedImageを作成 */
        BufferedImage img2 = new BufferedImage(
            img.getWidth(), img.getHeight(),
            BufferedImage.TYPE_INT_ARGB_PRE
        );
        
        //文字描画用の変数
        int imgWidth = img.getWidth();
        int imgHeight = img.getHeight();
        
        int fontSize = 45;
        int padding = 13;
        int startWidth = 20;//文字の開始位置(左から)
        int startHeight = 0;//文字の開始位置(上から)
        int diffSizeX = 2;//何pxずらすか
        int diffSizeY = 2;//何pxずらすか
        System.out.println(imgHeight);
        System.out.println(imgWidth);
        Graphics2D gr2 = img2.createGraphics();
        
        //文字の周囲4辺を黒地で囲う
        for(int i = 0;i < 4; i++){
        	int tmpDiffSizeX = diffSizeX;
        	int tmpDiffSizeY = diffSizeY;
        	if(i == 2 || i == 3){
        		tmpDiffSizeX = -1 * diffSizeX;
        		
        	}
        	if(i == 1 || i == 3){
        		tmpDiffSizeY = -1 * diffSizeY;
        	}

        	
            gr2.setColor(new Color(0x00, 0x00, 0x00, 0xf0));//黒地
            gr2.setFont(new Font(Font.DIALOG, Font.BOLD, fontSize));
            gr2.drawString("■" + info.getPrefectures() + info.getDate() + "の天気", startWidth + tmpDiffSizeX, startHeight + (padding + fontSize) * 1 + tmpDiffSizeY);
            gr2.drawString(info.getWeatherDetail(), startWidth + tmpDiffSizeX, startHeight + (padding + fontSize) * 2 + tmpDiffSizeY);
            gr2.drawString("■気温", startWidth + tmpDiffSizeX, imgHeight - (padding + fontSize) * 8 + fontSize + tmpDiffSizeY);
            gr2.drawString("　高：" + info.getMaxTemperature() + "℃", startWidth + tmpDiffSizeX, imgHeight - (padding + fontSize) * 7 + fontSize + tmpDiffSizeY);
            gr2.drawString("　低：" + info.getMinTemperature() + "℃", startWidth + tmpDiffSizeX, imgHeight - (padding + fontSize) * 6 + fontSize + tmpDiffSizeY);
            gr2.drawString("■降水確率", startWidth + tmpDiffSizeX, imgHeight - (padding + fontSize) * 5 + fontSize + tmpDiffSizeY);
            gr2.drawString("　00~：" + info.getRainfallchance00() + "%", startWidth + tmpDiffSizeX, imgHeight - (padding + fontSize) * 4 + fontSize + tmpDiffSizeY);
            gr2.drawString("　06~：" + info.getRainfallchance06() + "%", startWidth + tmpDiffSizeX, imgHeight - (padding + fontSize) * 3 + fontSize + tmpDiffSizeY);
            gr2.drawString("　12~：" + info.getRainfallchance12() + "%", startWidth + tmpDiffSizeX, imgHeight - (padding + fontSize) * 2 + fontSize + tmpDiffSizeY);
            gr2.drawString("　18~：" + info.getRainfallchance18() + "%", startWidth + tmpDiffSizeX, imgHeight - (padding + fontSize) * 1 + fontSize + tmpDiffSizeY);               	
        }
        
        //上から白地で文字表示
        gr2.setColor(new Color(0xff, 0xff, 0xff, 0xf0));//白地
        gr2.setFont(new Font(Font.DIALOG, Font.BOLD, fontSize));
        gr2.drawString("■" + info.getPrefectures() + info.getDate() + "の天気", startWidth, startHeight + (padding + fontSize) * 1);
        gr2.drawString(info.getWeatherDetail(), startWidth, startHeight + (padding + fontSize) * 2);
        gr2.drawString("■気温", startWidth, imgHeight - (padding + fontSize) * 8 + fontSize );
        gr2.drawString("　高：" + info.getMaxTemperature() + "℃", startWidth, imgHeight - (padding + fontSize) * 7 + fontSize);
        gr2.drawString("　低：" + info.getMinTemperature() + "℃", startWidth, imgHeight - (padding + fontSize) * 6 + fontSize);
        gr2.drawString("■降水確率", startWidth, imgHeight - (padding + fontSize) * 5 + fontSize);
        gr2.drawString("　00~：" + info.getRainfallchance00() + "%", startWidth, imgHeight - (padding + fontSize) * 4 + fontSize);
        gr2.drawString("　06~：" + info.getRainfallchance06() + "%", startWidth, imgHeight - (padding + fontSize) * 3 + fontSize);
        gr2.drawString("　12~：" + info.getRainfallchance12() + "%", startWidth, imgHeight - (padding + fontSize) * 2 + fontSize);
        gr2.drawString("　18~：" + info.getRainfallchance18() + "%", startWidth, imgHeight - (padding + fontSize) * 1 + fontSize);
       
        gr2.dispose();

        //文字
        gr.drawImage(img2, 0, 0, null);
        gr.dispose();

        ImageIO.write(img, "png", new File("output.png"));
        
        
		//png画像を出力
		FileSystem fs = FileSystems.getDefault();
        Path path = fs.getPath("output.png");
        File file = path.toFile();
                
        //画像付きツイート
        twitter.updateStatus(new StatusUpdate(info.getDate() + "の" + info.getPrefectures() + "の天気だよ" + info.getInfomation() ).media(file));
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




