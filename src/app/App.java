package app;


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

import reader.PropertyFileReader;
import reader.XmlReader;
import twitter4j.StatusUpdate;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.User;

public class App {
	public static void main(String[] args) throws Exception {

		String propertyFilePath = "searchSetting";
		// プロパティファイル読み込む
		PropertyFileReader property = new PropertyFileReader(propertyFilePath);
		System.out.println("アカウント名：" + property.getAccountName());

		Twitter twitter = new TwitterFactory().getInstance();
		User user = twitter.verifyCredentials();
		
		//ユーザ情報取得
        System.out.println("なまえ　　　：" + user.getName());
        System.out.println("ひょうじ名　：" + user.getScreenName());
        System.err.println("ふぉろー数　：" + user.getFriendsCount());
        System.out.println("ふぉろわー数：" + user.getFollowersCount());
		
        //天気APIの東京データのURLを取得
        URL url = new URL(property.getWheatherInfoPath());
        String str = getSourceText(url);

        
        //HTMLをファイル保存
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
        
		//指定のフォルダからランダムに画像を取得
		String SearchPath = "/Users/ryosuke/Desktop/workspace/Apps/TwitterBot/TenkiTweetBot/chara/";
	    File dir = new File(SearchPath);
		File[] files = dir.listFiles();
	    
	    
		int a = (int)(Math.random() * files.length);
	    String imgPath = files[a].getName();
	    		
	    StringBuffer buf = new StringBuffer();
	    buf.append(SearchPath);
	    buf.append(imgPath);
	    String inputImgPath = buf.toString();
	    
	    
	    System.out.println(files.length);
	    System.out.println(a);
	    System.out.println(inputImgPath);
        //Java2D
        /* 画像の上に半透明のぼやけた文字列を描画する */

        BufferedImage img = ImageIO.read(new File(inputImgPath));
        Graphics2D gr = img.createGraphics();

        /* 文字列描画用のBufferedImageを作成 */
        BufferedImage img2 = new BufferedImage(
            img.getWidth(), img.getHeight(),
            BufferedImage.TYPE_INT_ARGB_PRE
        );
        
        int imgWidth = img.getWidth();
        int imgHeight = img.getHeight();
        
        int fontSize = 45;
        int padding = 13;
        int startWidth = 20;//文字の開始位置(左から)
        int startHeight = 0;//文字の開始位置(上から)
        int diffSize1 = -2;//何pxずらすか
        int diffSize2 = 2;//何pxずらすか
        System.out.println(imgHeight);
        System.out.println(imgWidth);
        Graphics2D gr2 = img2.createGraphics();
        /* 文字列を描画 */
        
        //左上
        gr2.setColor(new Color(0x00, 0x00, 0x00, 0xf0));//黒地
        gr2.setFont(new Font(Font.DIALOG, Font.BOLD, fontSize));
        gr2.drawString("■" + info.getPrefectures() + info.getDate() + "の天気", startWidth + diffSize1, startHeight + (padding + fontSize) * 1 + diffSize1);
        gr2.drawString(info.getWeatherDetail(), startWidth + diffSize1, startHeight + (padding + fontSize) * 2 + diffSize1);
        gr2.drawString("■気温", startWidth + diffSize1, imgHeight - (padding + fontSize) * 8 + fontSize + diffSize1);
        gr2.drawString("最高：" + info.getMaxTemperature() + "℃", startWidth + diffSize1, imgHeight - (padding + fontSize) * 7 + fontSize + diffSize1);
        gr2.drawString("最低：" + info.getMinTemperature() + "℃", startWidth + diffSize1, imgHeight - (padding + fontSize) * 6 + fontSize + diffSize1);
        gr2.drawString("■降水確率", startWidth + diffSize1, imgHeight - (padding + fontSize) * 5 + fontSize + diffSize1);
        gr2.drawString("00~：" + info.getRainfallchance00() + "%", startWidth + diffSize1, imgHeight - (padding + fontSize) * 4 + fontSize + diffSize1);
        gr2.drawString("06~：" + info.getRainfallchance06() + "%", startWidth + diffSize1, imgHeight - (padding + fontSize) * 3 + fontSize + diffSize1);
        gr2.drawString("12~：" + info.getRainfallchance12() + "%", startWidth + diffSize1, imgHeight - (padding + fontSize) * 2 + fontSize + diffSize1);
        gr2.drawString("18~：" + info.getRainfallchance18() + "%", startWidth + diffSize1, imgHeight - (padding + fontSize) * 1 + fontSize + diffSize1);
        
        //右上
        gr2.setColor(new Color(0x00, 0x00, 0x00, 0xf0));//黒地
        gr2.setFont(new Font(Font.DIALOG, Font.BOLD, fontSize));
        gr2.drawString("■" + info.getPrefectures() + info.getDate() + "の天気", startWidth + diffSize2, startHeight + (padding + fontSize) * 1 + diffSize1);
        gr2.drawString(info.getWeatherDetail(), startWidth + diffSize2, startHeight + (padding + fontSize) * 2+ diffSize1);
        gr2.drawString("■気温", startWidth + diffSize2, imgHeight - (padding + fontSize) * 8 + fontSize + diffSize1);
        gr2.drawString("最高：" + info.getMaxTemperature() + "℃", startWidth + diffSize2, imgHeight - (padding + fontSize) * 7 + fontSize + diffSize1);
        gr2.drawString("最低：" + info.getMinTemperature() + "℃", startWidth + diffSize2, imgHeight - (padding + fontSize) * 6 + fontSize + diffSize1);
        gr2.drawString("■降水確率", startWidth + diffSize2, imgHeight - (padding + fontSize) * 5 + fontSize + diffSize1);
        gr2.drawString("00~：" + info.getRainfallchance00() + "%", startWidth + diffSize2, imgHeight - (padding + fontSize) * 4 + fontSize + diffSize1);
        gr2.drawString("06~：" + info.getRainfallchance06() + "%", startWidth + diffSize2, imgHeight - (padding + fontSize) * 3 + fontSize + diffSize1);
        gr2.drawString("12~：" + info.getRainfallchance12() + "%", startWidth + diffSize2, imgHeight - (padding + fontSize) * 2 + fontSize + diffSize1);
        gr2.drawString("18~：" + info.getRainfallchance18() + "%", startWidth + diffSize2, imgHeight - (padding + fontSize) * 1 + fontSize + diffSize1);
       
        //左下
        gr2.setColor(new Color(0x00, 0x00, 0x00, 0xf0));//黒地
        gr2.setFont(new Font(Font.DIALOG, Font.BOLD, fontSize));
        gr2.drawString("■" + info.getPrefectures() + info.getDate() + "の天気", startWidth + diffSize1, startHeight + (padding + fontSize) * 1 + diffSize2);
        gr2.drawString(info.getWeatherDetail(), startWidth + diffSize1, startHeight + (padding + fontSize) * 2 + diffSize2);
        gr2.drawString("■気温", startWidth + diffSize1, imgHeight - (padding + fontSize) * 8 + fontSize + diffSize2);
        gr2.drawString("最高：" + info.getMaxTemperature() + "℃", startWidth + diffSize1, imgHeight - (padding + fontSize) * 7 + fontSize + diffSize2);
        gr2.drawString("最低：" + info.getMinTemperature() + "℃", startWidth + diffSize1, imgHeight - (padding + fontSize) * 6 + fontSize + diffSize2);
        gr2.drawString("■降水確率", startWidth + diffSize1, imgHeight - (padding + fontSize) * 5 + fontSize + diffSize2);
        gr2.drawString("00~：" + info.getRainfallchance00() + "%", startWidth + diffSize1, imgHeight - (padding + fontSize) * 4 + fontSize + diffSize2);
        gr2.drawString("06~：" + info.getRainfallchance06() + "%", startWidth + diffSize1, imgHeight - (padding + fontSize) * 3 + fontSize + diffSize2);
        gr2.drawString("12~：" + info.getRainfallchance12() + "%", startWidth + diffSize1, imgHeight - (padding + fontSize) * 2 + fontSize + diffSize2);
        gr2.drawString("18~：" + info.getRainfallchance18() + "%", startWidth + diffSize1, imgHeight - (padding + fontSize) * 1 + fontSize + diffSize2);
  
        //右下
        gr2.setColor(new Color(0x00, 0x00, 0x00, 0xf0));//黒地
        gr2.setFont(new Font(Font.DIALOG, Font.BOLD, fontSize));
        gr2.drawString("■" + info.getPrefectures() + info.getDate() + "の天気", startWidth + diffSize2, startHeight + (padding + fontSize) * 1 + diffSize2);
        gr2.drawString(info.getWeatherDetail(), startWidth + diffSize2, startHeight + (padding + fontSize) * 2 + diffSize2);
        gr2.drawString("■気温", startWidth + diffSize2, imgHeight - (padding + fontSize) * 8 + fontSize + diffSize2);
        gr2.drawString("最高：" + info.getMaxTemperature() + "℃", startWidth + diffSize2, imgHeight - (padding + fontSize) * 7 + fontSize + diffSize2);
        gr2.drawString("最低：" + info.getMinTemperature() + "℃", startWidth + diffSize2, imgHeight - (padding + fontSize) * 6 + fontSize + diffSize2);
        gr2.drawString("■降水確率", startWidth + diffSize2, imgHeight - (padding + fontSize) * 5 + fontSize + diffSize2);
        gr2.drawString("00~：" + info.getRainfallchance00() + "%", startWidth + diffSize2, imgHeight - (padding + fontSize) * 4 + fontSize + diffSize2);
        gr2.drawString("06~：" + info.getRainfallchance06() + "%", startWidth + diffSize2, imgHeight - (padding + fontSize) * 3 + fontSize + diffSize2);
        gr2.drawString("12~：" + info.getRainfallchance12() + "%", startWidth + diffSize2, imgHeight - (padding + fontSize) * 2 + fontSize + diffSize2);
        gr2.drawString("18~：" + info.getRainfallchance18() + "%", startWidth + diffSize2, imgHeight - (padding + fontSize) * 1 + fontSize + diffSize2);
      
        
        
        gr2.setColor(new Color(0xff, 0xff, 0xff, 0xf0));//白地
        gr2.setFont(new Font(Font.DIALOG, Font.BOLD, fontSize));
        gr2.drawString("■" + info.getPrefectures() + info.getDate() + "の天気", startWidth, startHeight + (padding + fontSize) * 1);
        gr2.drawString(info.getWeatherDetail(), startWidth, startHeight + (padding + fontSize) * 2);
        gr2.drawString("■気温", startWidth, imgHeight - (padding + fontSize) * 8 + fontSize );
        gr2.drawString("最高：" + info.getMaxTemperature() + "℃", startWidth, imgHeight - (padding + fontSize) * 7 + fontSize);
        gr2.drawString("最低：" + info.getMinTemperature() + "℃", startWidth, imgHeight - (padding + fontSize) * 6 + fontSize);
        gr2.drawString("■降水確率", startWidth, imgHeight - (padding + fontSize) * 5 + fontSize);
        gr2.drawString("00~：" + info.getRainfallchance00() + "%", startWidth, imgHeight - (padding + fontSize) * 4 + fontSize);
        gr2.drawString("06~：" + info.getRainfallchance06() + "%", startWidth, imgHeight - (padding + fontSize) * 3 + fontSize);
        gr2.drawString("12~：" + info.getRainfallchance12() + "%", startWidth, imgHeight - (padding + fontSize) * 2 + fontSize);
        gr2.drawString("18~：" + info.getRainfallchance18() + "%", startWidth, imgHeight - (padding + fontSize) * 1 + fontSize);
        
        
         
        
        gr2.dispose();

        //文字
        gr.drawImage(img2, 0, 0, null);
        gr.dispose();

        ImageIO.write(img, "png", new File("test2.png"));
        
        
		//画像付きのツイートをする。
		FileSystem fs = FileSystems.getDefault();
        Path path = fs.getPath("test2.png");
        File file = path.toFile();
        
        
        
        //画像付きツイート
        twitter.updateStatus(new StatusUpdate(info.getDate() + "の" + info.getPrefectures() + "の天気だよ" ).media(file));
        
		//ついーとしてみる
		//twitter.updateStatus("てすと。");
		//twitter.updateStatus(info.toString());
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