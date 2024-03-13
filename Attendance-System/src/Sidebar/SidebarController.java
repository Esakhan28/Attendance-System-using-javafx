package Sidebar;

import Classes.Teacher;
import Login.LoginModel;
import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class SidebarController implements Initializable {

    @FXML
    JFXButton logout;
    /* clicking a section button will just change the center of the pane
     not actually the whole window
      (this allows us to make the sidebar permanent and doesn't move)
      */
    @FXML
    private BorderPane homePane;
    @FXML
    private Label UserName;
    @FXML
    private HBox dashboard;
    @FXML
    private HBox attend;
    @FXML
    private HBox list;
    @FXML
    private HBox aboutLec;
    @FXML
    private HBox about;
    @FXML
    private HBox settings;

    private List<HBox> buttons = new ArrayList<>(); // needed for later

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        buttons.add(dashboard);
        buttons.add(attend);
        buttons.add(list);
        buttons.add(aboutLec);
        buttons.add(about);
        buttons.add(settings);

        Teacher teacher = LoginModel.getLogged(); // get logged in teacher from login model class
        logout.setFocusTraversable(false);
        dashboard.setStyle("-fx-background-color: #2980B9");
        // set the dashboard as default view when user logs in
        try {
            loadPane("/Dashboard/Dashboard");
        } catch (IOException e) {
            e.printStackTrace();
        }
        String name = teacher.getName().split(" ")[0]; // get only the first word of the name
        // set prefix based on gender.
        if (teacher.getGender().equals("Male")) {
            UserName.setText("MR. " + name.toUpperCase());
        } else {
            UserName.setText("MS. " + name.toUpperCase());
        }
    }

    @FXML
    void changeView(MouseEvent event) throws IOException {
        HBox source = (HBox) event.getSource(); // get the source reference
        // so it wont have the coming hover like effects
        source.setOnMouseEntered(null);
        source.setOnMouseExited(null);
        source.setStyle("-fx-background-color: #2980B9");
        // clear the other Hboxes and style them again
        for (HBox item : buttons) {
            if (item != source) {
                item.setStyle("-fx-background-color: #303952");
                item.setOnMouseEntered(e -> item.setStyle("-fx-background-color: #2980B9"));
                item.setOnMouseExited(e -> item.setStyle("-fx-background-color: #303952"));
            }
        }
        if (source == dashboard) {
            loadPane("/Dashboard/Dashboard");
        } else if (source == attend) {
            loadPane("/Attendance/Attendance");
        } else if (source == list) {
            loadPane("/List/List");
        } else if (source == aboutLec) {
            loadPane("/AboutLecturer/AboutLecturer");
        } else if (source == about) {
            loadPane("/AboutApp/AboutApp");
        } else if(source == settings){
            loadPane("/Settings/Settings");
        }
    }


    // function to set the borderPane center to the passed fxml file
    private void loadPane(String UI) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource(UI + ".fxml"));
        homePane.setCenter(root);
    }


    @FXML
    void logoutNow(ActionEvent event) throws IOException {
        // an alert box to confirm user action
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Logout");
        alert.setHeaderText("You really want to logout?");
        ButtonType Yes = new ButtonType("Yes");
        ButtonType No = new ButtonType("No");
        alert.getButtonTypes().setAll(Yes, No);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == Yes) {
            // back to the main scene if user selected to logout
            Parent Logout = FXMLLoader.load(getClass().getResource("/Login/Login.fxml"));
            Scene Login = new Scene(Logout);
            //This line gets the Stage information
            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow(); // use the same stage again
            window.setTitle("Login"); // set title
            window.setScene(Login); // load login scene
            window.show(); // show window
        }

    }
}
