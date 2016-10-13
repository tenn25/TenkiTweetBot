package main.java.reader;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectSummary;

public class S3Reader {
	String accessKey;
	String secretKey;
	String bucketName;
	String folderName;
	AmazonS3Client s3Client;
	InputStream content;
	
	public S3Reader(String accessKey,String secretKey){
		this.accessKey = accessKey;
		this.secretKey = secretKey;
		
		// プロパティファイル読み込んで接続情報を取得
		s3Client = new AmazonS3Client(
	    		new BasicAWSCredentials(
	    				this.accessKey, 
	    				this.secretKey
		    		  )
	    		);
	}

 
    //デバッグ用メソッド
	//バケット一覧を表示
	public void dispBucketList() {  
		
	    // バケット情報を取得  
	    final List<Bucket> bucketList = s3Client.listBuckets();  
	    
	    for (final Bucket bucket : bucketList) {  
	        System.out.println("バケット名：" + bucket.getName());  
	    } 
	} 
	
	// S3のオブジェクトをランダムで1つ返す
	public InputStream getS3ObjectRandam(String bucketName,String folderName) {
		 List<String> fileList = new ArrayList<>();
		
	    // 以下のようなおまじないを書くことでエラーが出なくなるそう  
	    //System.setProperty("org.xml.sax.driver", "org.xmlpull.v1.sax2.Driver");  
	  
	    // 指定したバケット内のファイル情報を取得  
	    final ObjectListing objectListing = s3Client.listObjects(bucketName);  
	    for (final S3ObjectSummary summary : objectListing.getObjectSummaries()) {  
	        // ファイル名を表示
	        System.out.println("ファイル名：" + summary.getKey()); 
	       
	        //指定したフォルダにあるファイルをリストに格納
	        if(summary.getKey().startsWith(folderName)){
	        	fileList.add(summary.getKey());
	        }
	    }
	    
	    // リストの並びをシャッフルします。
	    Collections.shuffle(fileList);
	    
	    // オブジェクトを取得
	    S3Object object = s3Client.getObject(bucketName, fileList.get(0));
	    
	    System.out.println("選択されたファイル名：" + fileList.get(0)); 
	    //ImputStream型で返す
	    return object.getObjectContent();
	}
	
	public InputStream getImageStream() {
		return content;
	}
	
}
