package Services.config;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class Config {
    public static Properties getProperties(){
        Properties properties = new Properties();
        try{
            properties.load(Config.class.getResourceAsStream("/config.properties"));
            return properties;
        }catch(IOException ex){
            throw new RuntimeException("Cannot load config properties");
        }
    }
}
