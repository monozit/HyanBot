import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesLoader {
	public static Properties load(String filePath) throws IOException{
		Properties properties = new Properties();
		String propertiesPass = filePath;
		InputStream istream = new FileInputStream(propertiesPass);
        properties.load(istream);
        
        return properties;
	}
}
