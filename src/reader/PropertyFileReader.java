package reader;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

public class PropertyFileReader {

	private ResourceBundle bundle = null;

	private String accountName;
	private String wheatherInfoPath;
	private String wheatherDataPath;
	private String imageSourcePath;

	public PropertyFileReader(String propertyFilePass) {
		try {
			bundle = ResourceBundle.getBundle(propertyFilePass,
					UTF8_ENCODING_CONTROL);

			accountName = bundle.getString("accountName");
			
			accountName = bundle.getString("accountName");
			wheatherInfoPath = bundle.getString("wheatherInfoPath");
			wheatherDataPath = bundle.getString("wheatherDataPath");
			
			//imageSourcePath = bundle.getString("imageSourcePath");
			

			
		} catch (MissingResourceException e) {
			e.printStackTrace();
		}

	}

	public String getAccountName() {
		return accountName;
	}
	
	public String getWheatherInfoPath() {
		return wheatherInfoPath;
	}
	
	public String getWheatherDataPath() {
		return wheatherDataPath;
	}

	public String getImageSourcePath() {
		return imageSourcePath;
	}
	

	private static ResourceBundle.Control UTF8_ENCODING_CONTROL = new ResourceBundle.Control() {
		/**
		 * UTF-8 エンコー�?ィングのプロパティファイルから ResourceBundle オブジェクトを生�?�します�??
		 * <p>
		 * 参�?? : <a href=
		 * "http://jgloss.sourceforge.net/jgloss-core/jacoco/jgloss.util/UTF8ResourceBundleControl.java.html"
		 * > http://jgloss.sourceforge.net/jgloss-core/jacoco/jgloss.util/
		 * UTF8ResourceBundleControl.java.html </a>
		 * </p>
		 *
		 * @throws IllegalAccessException
		 * @throws InstantiationException
		 * @throws IOException
		 */
		@Override
		public ResourceBundle newBundle(String baseName, Locale locale,
				String format, ClassLoader loader, boolean reload)
				throws IllegalAccessException, InstantiationException,
				IOException {
			String bundleName = toBundleName(baseName, locale);
			String resourceName = toResourceName(bundleName, "properties");

			try (InputStream is = loader.getResourceAsStream(resourceName);
					InputStreamReader isr = new InputStreamReader(is, "UTF-8");
					BufferedReader reader = new BufferedReader(isr)) {
				return new PropertyResourceBundle(reader);
			}
		}
	};
}
