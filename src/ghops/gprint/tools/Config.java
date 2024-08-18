package ghops.gprint.tools;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Config {

    Properties prop = null;

    public Config() {

        try {
            InputStream is = new FileInputStream("config.properties");
            this.prop = new Properties();
            this.prop.load(is);
            //is.close();
        } catch (IOException ex) {
            System.err.println("HATA:" + ex);
            this.prop = null;
        }
    }

    public List<StatusType> getStatusTypes() {
        List<StatusType> types = new ArrayList<>();
        Properties prop = null;
        try {
            InputStream is = getClass().getResourceAsStream("statustypes.properties");
            System.out.println("ghops.gprint.tools.statustypes.StatusTypes.getStatus()");
            prop = new Properties();
            prop.load(is);
            System.out.println();

            for (Object key : prop.keySet()) {
                StatusType st = new StatusType(Integer.parseInt(prop.getProperty(key.toString())));
                types.add(st);
            }

        } catch (IOException ex) {

        }

        return types;

    }
//C:\Users\dijital\Documents\NetBeansProjects\GPrint

    public String getImageseUrl() {
        String url = System.getProperty("user.dir") + "\\" + this.prop.getProperty("imageFolder") + "\\";
        //System.out.println(url);
        String os = System.getProperty("os.name");

        if (os.equals("Linux")) {
            return url.replace("\\", "/");
        }
        return url;

    }

    public String getImageType() {
        if (this.prop == null) {
            return null;
        }
        return this.prop.getProperty("imageType");
    }

    public String getDefaultImageFolder() {
        if (this.prop == null) {
            return null;
        }
        return this.prop.getProperty("defaultImageFolder");
    }

    public void setDefaultImageFolder(String path) {
        path = path.replace("\\", "/");
        this.prop.setProperty("defaultImageFolder", path);
        OutputStream os;
        try {
            os = new FileOutputStream("config.properties");
            this.prop.store(os, "");
        } catch (FileNotFoundException ex) {
            System.err.println(ex);
        } catch (IOException ex) {
            System.err.println(ex);
        }

    }

    public int getImageWidth() {
        if (this.prop == null) {
            return 200;
        }
        return Integer.parseInt(this.prop.getProperty("imageWidth"));
    }

    public int getImageHeight() {
        if (this.prop == null) {
            return 200;
        }
        return Integer.parseInt(this.prop.getProperty("imageHeight"));
    }

    public Properties getProperties() {
        return this.prop;
    }

}
