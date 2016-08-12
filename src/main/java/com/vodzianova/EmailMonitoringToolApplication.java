package com.vodzianova;

import com.vodzianova.ui.AbstractJavaFxApplicationSupport;
import com.vodzianova.ui.ConfigurationControllers;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Lazy;

import static org.apache.log4j.Logger.getLogger;

/**
 * @author Elena Vodzianova 11/08/2016
 */
@Lazy
@SpringBootApplication
public class EmailMonitoringToolApplication extends AbstractJavaFxApplicationSupport {

    private static Logger logger = getLogger(EmailMonitoringToolApplication.class);

    @Value("${app.ui.title: Please hire me as Middle Java Developer :)}")//
    private String windowTitle;

    @Qualifier("mainView")
    @Autowired
    private ConfigurationControllers.View view;

    public static void main(String[] args) {
        logger.info("Launching Email Monitoring Tool...");
        launchApp(EmailMonitoringToolApplication.class, args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        logger.info("Stage configuration has been started...");
        stage.setTitle(windowTitle);
        stage.setScene(new Scene(view.getView()));
        stage.setResizable(true);
        stage.centerOnScreen();
        stage.show();
    }
}
