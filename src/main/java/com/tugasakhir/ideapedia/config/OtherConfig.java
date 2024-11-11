package com.tugasakhir.ideapedia.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:otherconfig.properties")
public class OtherConfig {

    private static String enablePrint;
    private static String enableAntiRobot;

    public static String getEnableAntiRobot() {
        return enableAntiRobot;
    }

    @Value("${enable.anti.robot}")
    private void setEnableAntiRobot(String enableAntiRobot) {
        OtherConfig.enableAntiRobot = enableAntiRobot;
    }

    public static String getEnablePrint() {
        return enablePrint;
    }

    @Value("${enable.print}")
    private void setEnablePrint(String enablePrint) {
        this.enablePrint = enablePrint;
    }
}
