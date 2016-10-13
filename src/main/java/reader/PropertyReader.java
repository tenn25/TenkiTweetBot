package main.java.reader;
import java.io.File;
import java.io.FileReader;
import java.util.Properties;


/** プロパティ読み込みクラス */
public class PropertyReader {

  /**
   * プロパティファイルから値を読み込む
   * @param key PropKey
   * @return 値
   */
  public String getProp(PropKey key) {
    Properties prop;
      prop = key.getProperties(); // どのプロパティファイルを読み込めば良いかはキーが知っている
    return prop.getProperty(key.name());
  }
  
  /** プロパティファイルの読み込み(I/O) */
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





