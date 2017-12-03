package com.splider.utils;

import java.io.IOException;
import java.util.Properties;

public class PropertiesMgr {

    private static Properties props;

    public Properties getProps(){
        if(props==null){
            props=new Properties();
            try {
                props.load(this.getClass().getResourceAsStream("/default-config.properties"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return props;
    }
}

