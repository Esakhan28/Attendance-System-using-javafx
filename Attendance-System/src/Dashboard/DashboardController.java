package Dashboard;

import Classes.Teacher;
import Login.LoginModel;
import dbConnection.Connect;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;
import java.util.ResourceBundle;

public class DashboardController implements Initializable {

    @FXML
    private Label Welcome;

    @FXML
    private Label stud_num;

    @FXML
    private Label classes_num;

    @FXML
    private Label abs_num;

    @FXML
    private Label atten_percent;

    @FXML
    private Label barred_num;

    // get logged in teacher
    private Teacher logged = LoginModel.getLogged();
    private Connection conn = Connect.getConnect(); // get connection to database

    @Override

    public void initialize(URL url, ResourceBundle resourceBundle) {
        // this function right here is why i
        // with my fully functioning brain love java now
        // that doesn't make it less of a meme anyway
        Welcome.setText("Welcome Back!"); // set the date label text
        Welcome.setStyle("-fx-font-weight: Bold");
        stud_num.setText(String.valueOf(getStudentsNum())); //set number of students label
        classes_num.setText(String.valueOf(getClassesNum())); // set number of classes label
        abs_num.setText(String.valueOf(getAbsentStudentsNum())); // set number of absent students in last class label
        // calculate percentage for students attendance
        double percent = 100 - (((double) getAbsentStudentsNum() / (double) getStudentsNum()) * 100);
        atten_percent.setText(Math.round(percent) + "%"); // set attendance percentage label
        barred_num.setText(String.valueOf(getBarredStudentsNum())); //  set number of barred students label

    }

    // get total number of students
    private int getStudentsNum() {
        checkConn(); // check if connection is available or not
        ResultSet rs = null;
        try {
            rs = Objects.requireNonNull(conn).createStatement()
                    .executeQuery(" select count(*) from '" + logged.getID() + "'"); // sql statement
            return rs.getInt(1); // get the statement output
        } catch (SQLException e) {
            e.printStackTrace();
        } finally { // finally block runs regarding if the statement was successful, we already returned something or whatever
            try {
                Objects.requireNonNull(rs).close();
                conn.close(); // make sure we close the connection
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return 0; //return 0 in case the try-catch block failed
    }

    // get number of barred students
    private int getBarredStudentsNum() {
        checkConn(); // check connection
        ResultSet rs = null;
        try {
            rs = Objects.requireNonNull(conn).createStatement()
                    .executeQuery(" select count(*) from '" + logged.getID() + "' where bar = 'barred' "); // sql statement
            return rs.getInt(1); // get the statement output
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                Objects.requireNonNull(rs).close();
                conn.close(); // make sure we close the connection
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return 0; //return 0 in case the try-catch block failed
    }

    // get number of absent students since last class
    private int getAbsentStudentsNum() {
        checkConn(); // check connection
        ResultSet rs = null;
        try {
            rs = Objects.requireNonNull(conn).createStatement().
                    executeQuery(" select count(*) from '" + logged.getID() + "' where present = 0"); // sql statement
            return rs.getInt(1); // get the statement output
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                rs.close();
                conn.close(); // make sure we close the connection
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return 0; //return 0 in case the try-catch block failed
    }

    // get number of classes.
    private int getClassesNum() {
        String subs; // temp for holding the value from sql
        checkConn();
        try {
            ResultSet rs = Objects.requireNonNull(conn).createStatement()
                    .executeQuery(" select * from Teachers where id = " + logged.getID()); // sql statement
            subs = rs.getString("subjects");
            // close query
            rs.close(); // close statement
            conn.close(); // close connection
            String[] subsArr = subs.split(" "); // split subjects (they're separated by space in database)
            return subsArr.length; // return the length of the splitted array ( yup i just made up the word splitted)
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0; //return 0 in case the try-catch block failed
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
