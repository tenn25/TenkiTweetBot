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
		
		// �v���p�e�B�t�@�C���ǂݍ���Őڑ������擾
		s3Client = new AmazonS3Client(
	    		new BasicAWSCredentials(
	    				this.accessKey, 
	    				this.secretKey
		    		  )
	    		);
	}

 
    //�f�o�b�O�p���\�b�h
	//�o�P�b�g�ꗗ��\��
	public void dispBucketList() {  
		
	    // �o�P�b�g�����擾  
	    final List<Bucket> bucketList = s3Client.listBuckets();  
	    
	    for (final Bucket bucket : bucketList) {  
	        System.out.println("�o�P�b�g���F" + bucket.getName());  
	    } 
	} 
	
	// S3�̃I�u�W�F�N�g�������_����1�Ԃ�
	public InputStream getS3ObjectRandam(String bucketName,String folderName) {
		 List<String> fileList = new ArrayList<>();
		
	    // �ȉ��̂悤�Ȃ��܂��Ȃ����������ƂŃG���[���o�Ȃ��Ȃ邻��  
	    //System.setProperty("org.xml.sax.driver", "org.xmlpull.v1.sax2.Driver");  
	  
	    // �w�肵���o�P�b�g���̃t�@�C�������擾  
	    final ObjectListing objectListing = s3Client.listObjects(bucketName);  
	    for (final S3ObjectSummary summary : objectListing.getObjectSummaries()) {  
	        // �t�@�C������\��
	        System.out.println("�t�@�C�����F" + summary.getKey()); 
	       
	        //�w�肵���t�H���_�ɂ���t�@�C�������X�g�Ɋi�[
	        if(summary.getKey().startsWith(folderName)){
	        	fileList.add(summary.getKey());
	        }
	    }
	    
	    // ���X�g�̕��т��V���b�t�����܂��B
	    Collections.shuffle(fileList);
	    
	    // �I�u�W�F�N�g���擾
	    S3Object object = s3Client.getObject(bucketName, fileList.get(0));
	    
	    System.out.println("�I�����ꂽ�t�@�C�����F" + fileList.get(0)); 
	    //ImputStream�^�ŕԂ�
	    return object.getObjectContent();
	}
	
	public InputStream getImageStream() {
		return content;
	}
	
}
