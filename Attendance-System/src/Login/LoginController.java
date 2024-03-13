package Login;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.validation.NumberValidator;
import com.jfoenix.validation.RequiredFieldValidator;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    // create an object of login class
    private LoginModel model = new LoginModel();

    @FXML
    private JFXTextField id;

    @FXML
    private JFXPasswordField pass;

    @FXML
    private Label wrongData;

    @FXML
    private Button logBtn;


    // handle login button click event
    @FXML
    private void dataCheck() throws IOException, SQLException {
        // validation
        if (model.isCorrect(id.getText(), pass.getText())) {
            // switch to the home scene
            System.out.println("Welcome back fam!!"); // some background action.
//            Operations.loadPref(); // load settings
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("../Sidebar/Sidebar.fxml"));
            Parent homeParent = loader.load();
            Scene home = new Scene(homeParent);
            //This line gets the Stage information
            Stage window = (Stage) logBtn.getScene().getWindow();
            window.setTitle("Attendance System");
            window.setScene(home);
            window.show();
        } else {
            // this else statement doesn't work anymore because of the setLogged function in LoginModel class
            System.out.println("Nah Not today ma dude");
            // display the wrong information text in red if data doesn't match available one.
            id.focusedProperty().addListener((observableValue, oldValue, newValue) -> {
                if (!newValue) {
                    id.validate();
                }
            });
            // set the wrong data label to visible
            wrongData.setStyle("-fx-font-weight: bold");
            wrongData.setVisible(true);
        }
    }

    @FXML
    public void HandleEnter(KeyEvent event) throws IOException, SQLException {
        // make sure the button is enabled first before doing the action
        if (!logBtn.isDisabled()) if (event.getCode().toString().equals("ENTER")) dataCheck();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // disable login button if either text fields is empty (boolean binding)
        BooleanBinding booleanBind = Bindings.and(id.textProperty().isEmpty(), pass.textProperty().isEmpty());
        logBtn.disableProperty().bind(booleanBind);

        // validators to make sure id field is only numbers and not empty and password filed is not empty.
        NumberValidator numberValidator = new NumberValidator();
        RequiredFieldValidator fieldValidate = new RequiredFieldValidator();
        RequiredFieldValidator passValidate = new RequiredFieldValidator();
        id.getValidators().add(fieldValidate);
        id.getValidators().add(numberValidator);
        pass.getValidators().add(passValidate);
        numberValidator.setMessage("Please enter a number 1-9");
        fieldValidate.setMessage(numberValidator.getMessage());
        passValidate.setMessage("Please enter your password");
        id.focusedProperty().addListener((observableValue, oldValue, newValue) -> {
            wrongData.setVisible(false); // hide the wrong information text when the user focuses on one of the text fields again
            if (!newValue) {
                id.validate();
            }
        });
        pass.focusedProperty().addListener((observableValue, oldValue, newValue) -> {
            wrongData.setVisible(false); // hide the wrong information text when the user focuses on one of the text fields again
            if (!newValue) {
                pass.validate();
            }
        });
    }


}
