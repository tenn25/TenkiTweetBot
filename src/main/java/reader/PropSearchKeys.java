package reader;
import java.io.File;
import java.util.Properties;

/** searchSetting.properties �ŗ��p����L�[ */
public enum PropSearchKeys implements PropKey {
	accountName,
	wheatherInfoPath,
	wheatherDataPath,
	imageSourcePath,
	;
	public static final String PATH = "searchSetting.properties";

	@Override
	public Properties getProperties() {
		return PropertyReader.getProperties(new File(PATH));
	}
	
}