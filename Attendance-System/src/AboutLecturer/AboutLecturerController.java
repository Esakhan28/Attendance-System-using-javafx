package AboutLecturer;

import Classes.Teacher;
import Login.LoginModel;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;

public class AboutLecturerController implements Initializable {

    @FXML
    private ImageView avatar;
    @FXML
    private Label gender;

    @FXML
    private Label name;

    @FXML
    private Label ID;

    @FXML
    private Label email;

    @FXML
    private Label subjects;

    @FXML
    private Label exp;

    @FXML
    private Label number;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Teacher teacher = LoginModel.getLogged(); // get logged in teacher from login model class

        // set the about info to that
        name.setText(teacher.getName());
        ID.setText(Integer.toString(teacher.getID()));
        email.setText(teacher.getEmail());
        gender.setText(teacher.getGender());
        subjects.setText("");
        Map<String,  String[]> subj = teacher.getSubjects();
        for (String name: subj.keySet()){
            subjects.setText(subjects.getText() + name + ", ");
        }
        exp.setText(teacher.getXP());
        number.setText(String.valueOf(teacher.getPhone()));

        Image male = new Image("resources/male.png");
        Image female = new Image("resources/female.png");

        if (teacher.getGender().startsWith("F") || teacher.getGender().startsWith("f")) {
            avatar.setImage((female));
        } else avatar.setImage(male);

    }


}
