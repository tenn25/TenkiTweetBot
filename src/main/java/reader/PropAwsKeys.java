package reader;
import java.io.File;
import java.util.Properties;

/** aws.properties ‚Å—˜—p‚·‚éƒL[ */
public enum PropAwsKeys implements PropKey {
	accessKey,
	secretKey,
	bucketName,
	pngImgFolderPath,
	;
	public static final String PATH = "aws.properties";

	@Override
	public Properties getProperties() {
		return PropertyReader.getProperties(new File(PATH));
	}
}