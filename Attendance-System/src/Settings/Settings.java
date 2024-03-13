package Settings;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import dbConnection.Connect;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Objects;
import java.util.ResourceBundle;

public class Settings implements Initializable {
    @FXML
    public JFXDatePicker sem_start = new JFXDatePicker();

    @FXML
    public JFXDatePicker sem_end = new JFXDatePicker();

    @FXML
    public JFXTextField sem_year = new JFXTextField();

    @FXML
    public JFXTextField sem_number = new JFXTextField();
    @FXML
    private JFXButton save;
    @FXML
    private Label saved;

    private Connection conn = Connect.getConnect();

    @FXML
    void savePref() throws SQLException {
        String query = "update Settings set sem_start = '" + sem_start.getValue() + "',  sem_end = '" + sem_end.getValue() +
                "', sem_year = " + sem_year.getText() + ", sem_num = " + sem_number.getText() + " where sem_count = 1";
        checkConn();
        PreparedStatement pst = Objects.requireNonNull(conn).prepareStatement(query);
        pst.execute();
        pst.close();
        conn.close();
        saved.setVisible(true);
    }

    private void loadPref() throws SQLException {
        checkConn();
        ResultSet rs = conn.createStatement().executeQuery("select * from Settings");
        while (rs.next()) {
            sem_start.setValue(LocalDate.parse(rs.getString("sem_start")));
            sem_end.setValue(LocalDate.parse(rs.getString("sem_end")));
            sem_year.setText(rs.getString("sem_year"));
            sem_number.setText(rs.getString("sem_num"));
        }
        rs.close();
        conn.close();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        save.setFocusTraversable(false);
        try {
            loadPref();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    private void checkConn() {
        // if connection is closed get it again
        try {
            if (conn.isClosed()) conn = Connect.getConnect();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
