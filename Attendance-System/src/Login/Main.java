package Login;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.util.Optional;

public class Main extends Application {

    public static void main(String[] args) {
        // the most empty function in the whole project
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/Login/Login.fxml"));
        primaryStage.setTitle("Login");
        primaryStage.getIcons().add(new Image("resources/window.png")); // set window icon
        primaryStage.setScene(new Scene(root, 800, 600));
//        primaryStage.setResizable(false); // make the window unresizable (easier to design and no need to worry about that anymore)
        primaryStage.show();
        // ask user to confirm if they tried to close the program
        primaryStage.setOnCloseRequest(we -> {
            we.consume(); // consume the close event the program wont close anyway even if you said no
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Exit");
            alert.setHeaderText("Are you sure You want to exit?");
            ButtonType Yes = new ButtonType("Yes");
            ButtonType No = new ButtonType("No");
            alert.getButtonTypes().setAll(Yes, No);
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == Yes) {
                // exit the app using platforms preferred way.
                Platform.exit();
            }
        });
    }
}
