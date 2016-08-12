package com.vodzianova.ui;

import com.vodzianova.EmailMonitoringToolApplication;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import org.apache.log4j.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.io.InputStream;

import static org.apache.log4j.Logger.getLogger;

/**
 * @author Elena Vodzianova 11/08/2016
 */
@Configuration
public class ConfigurationControllers {

    private static Logger logger = getLogger(ConfigurationControllers.class);

    @Bean(name = "mainView")
    public View getMainView() throws IOException {
        logger.debug("mainView is loaded.");
        return loadView("fxml/emailMonitoringTool.fxml");
    }

    /**
     * This method adds MainController to Spring context
     */
    @Bean
    public MainController getMainController() throws IOException {
        logger.debug("Controller is added to Spring context.");
        return (MainController) getMainView().getController();
    }

    /**
     * Initialization of FXML loader.
     */
    private View loadView(String url) throws IOException {
        InputStream fxmlStream = null;
        try {
            fxmlStream = getClass().getClassLoader().getResourceAsStream(url);
            FXMLLoader loader = new FXMLLoader();
            loader.load(fxmlStream);
            logger.debug("FXML loader is initialized.");
            return new View(loader.getRoot(), loader.getController());
        } finally {
            if (fxmlStream != null) {
                fxmlStream.close();
            }
        }
    }

    /**
     * Class wrapper: controller is used as bean,
     * View is used in {@link EmailMonitoringToolApplication}.
     */
    public class View {
        private Parent view;
        private Object controller;

        View(Parent view, Object controller) {
            this.view = view;
            this.controller = controller;
        }

        public Parent getView() {
            return view;
        }

        Object getController() {
            return controller;
        }
    }

}
