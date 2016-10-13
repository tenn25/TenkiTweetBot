package main.java.reader;
import java.io.File;
import java.io.FileReader;
import java.util.Properties;


/** �v���p�e�B�ǂݍ��݃N���X */
public class PropertyReader {

  /**
   * �v���p�e�B�t�@�C������l��ǂݍ���
   * @param key PropKey
   * @return �l
   */
  public String getProp(PropKey key) {
    Properties prop;
      prop = key.getProperties(); // �ǂ̃v���p�e�B�t�@�C����ǂݍ��߂Ηǂ����̓L�[���m���Ă���
    return prop.getProperty(key.name());
  }
  
  /** �v���p�e�B�t�@�C���̓ǂݍ���(I/O) */
  public static Properties getProperties(File propFile) {
    Properties result = new Properties();
    try {
      result.load(new FileReader(propFile));
    } catch (Exception e) {
      e.printStackTrace();
    }
    return result;
  }
  
}





