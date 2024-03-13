package AboutApp;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Hyperlink;

import java.awt.*;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ResourceBundle;

public class AboutAppController implements Initializable {
    @FXML
    private Hyperlink jfx;

    @FXML
    private Hyperlink suan;

    @FXML
    private Hyperlink fawesome;

    @FXML
    private Hyperlink cfx;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        final String[] URLs = {"https://github.com/Blacksuan19", "https://github.com/jfoenixadmin/JFoenix",
                "https://bitbucket.org/Jerady/fontawesomefx", "https://github.com/controlsfx/controlsfx"};

        // disable the stupid focus hint
        suan.setFocusTraversable(false);
        jfx.setFocusTraversable(false);
        fawesome.setFocusTraversable(false);
        cfx.setFocusTraversable(false);

        suan.setOnAction(e -> LoadURL(URLs[0]));
        jfx.setOnAction(e -> LoadURL(URLs[1]));
        fawesome.setOnAction(e -> LoadURL(URLs[2]));
        cfx.setOnAction(e -> LoadURL(URLs[3]));
    }

    private void LoadURL(String URL) {
        // use a different thread so it wont freeze the GUI
        new Thread(() -> {
            try {
                Desktop.getDesktop().browse(new URL(URL).toURI());
            } catch (IOException | URISyntaxException e1) {
                e1.printStackTrace();
            }
        }).start();

    }
}
