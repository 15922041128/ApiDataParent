package org.pbccrc.api.base.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesUtil {

	static Properties prop = null;
	
	static {
		prop = new Properties();   
        InputStream in = RedisClient.class.getResourceAsStream("system.properties");
        try {
			prop.load(in);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static String getStringByKey(String key) {
		return prop.getProperty(key);
	}
}
