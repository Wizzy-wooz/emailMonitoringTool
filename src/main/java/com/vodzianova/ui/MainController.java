package com.vodzianova.ui;

import com.vodzianova.domain.Profile;
import com.vodzianova.service.ProfileService;
import com.vodzianova.utils.EmailValidator;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.DirectoryChooser;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.io.File;
import java.util.List;

import static org.apache.log4j.Logger.getLogger;

/**
 * @author Elena Vodzianova 11/08/2016
 */
@SuppressWarnings("SpringJavaAutowiringInspection")
public class MainController {

    //    TODO: test coverage with JUnit5

    private static Logger logger = getLogger(MainController.class);

    @Autowired
    private ProfileService profileService;

    @FXML
    private TableView<Profile> table;
    @FXML
    private TextField txtName;
    @FXML
    private TextField txtEmail;
    @FXML
    private TextField txtPassword;
    @FXML
    private TextField txtPort;
    @FXML
    private TextField txtDirectory;


    // Variables
    private ObservableList<Profile> data;

    /**
     * Controller Initialization by JavaFX.
     */
    @FXML
    public void initialize() {
        logger.debug("MainController is initialized...");
    }

    /**
     * At this stage all injections are completed
     */
    @SuppressWarnings("unchecked")
    @PostConstruct
    public void init() {
        logger.debug("MainController init method started...");
        List<Profile> profiles = profileService.findAll();
        data = FXCollections.observableArrayList(profiles);

        // Table columns
        TableColumn<Profile, String> nameColumn = new TableColumn<>("Имя");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Profile, String> emailColumn = new TableColumn<>("E-mail");
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));

        TableColumn<Profile, String> passwordColumn = new TableColumn<>("Порт");
        passwordColumn.setCellValueFactory(new PropertyValueFactory<>("port"));

        TableColumn<Profile, String> portColumn = new TableColumn<>("Пароль");
        portColumn.setCellValueFactory(new PropertyValueFactory<>("password"));

        TableColumn<Profile, String> directoryColumn = new TableColumn<>("Путь");
        directoryColumn.setCellValueFactory(new PropertyValueFactory<>("directory"));

        table.getColumns().setAll(nameColumn, emailColumn, passwordColumn, portColumn, directoryColumn);

        // Table data
        table.setItems(data);
        logger.debug("Profile table data is filled.");
    }

    /**
     * Method called by clicking on button "Добавить".
     */
    @FXML
    public void addProfile() {
        //    TODO: extract and make decent validation!
        logger.debug("Adding new profile...");

        if (checkThatAllValuesAreProvidedByUser()) {
            logger.error("Profile info was not completely provided!");
            return;
        }

        EmailValidator emailValidator = new EmailValidator();
        if (!emailValidator.validate(txtEmail.getText())) {
            logger.error("Invalid email!");
            return;
        }

        Profile profile = new Profile(txtName.getText(),
                txtEmail.getText(), txtPassword.getText(),
                txtPort.getText(), txtDirectory.getText());

        logger.debug("Saving new profile...");
        profileService.save(profile);
        data.add(profile);
        cleanInputFields();
    }

    /**
     * Method called by clicking on button "+".
     */
    @FXML
    public void addDirectory() {
        logger.debug("Adding new profile attachment directory.");
        DirectoryChooser directoryChooser = new DirectoryChooser();
        File selectedDirectory = directoryChooser.showDialog(null);

        if (selectedDirectory != null) {
            txtDirectory.setText(selectedDirectory.getAbsolutePath());
            logger.info("Adding new profile attachment directory was successful.");
        }
    }

    private void cleanInputFields() {
        logger.debug("Start cleaning of input fields");
        txtName.setText("");
        txtPassword.setText("");
        txtPort.setText("");
        txtEmail.setText("");
        txtDirectory.setText("");
    }

    private boolean checkThatAllValuesAreProvidedByUser() {
        return StringUtils.isEmpty(txtName.getText()) ||
                StringUtils.isEmpty(txtEmail.getText()) ||
                StringUtils.isEmpty(txtPassword.getText()) ||
                StringUtils.isEmpty(txtPort.getText()) ||
                StringUtils.isEmpty(txtDirectory.getText());
    }
}
