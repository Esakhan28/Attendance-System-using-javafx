package Attendance;

import Classes.Student;
import Classes.Teacher;
import Login.LoginModel;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import dbConnection.Connect;
import dbConnection.Operations;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;

import javafx.event.ActionEvent;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Map;
import java.util.Objects;
import java.util.ResourceBundle;

import static dbConnection.Operations.*;

public class AttendanceController implements Initializable {

    @FXML
    private TableView<Student> list_table;

    @FXML
    private JFXDatePicker datePicker;

    @FXML
    private TableColumn<Student, Integer> id_col;

    @FXML
    private TableColumn<Student, String> name_col;

    @FXML
    private TableColumn<Student, Boolean> present_col;

    @FXML
    private TableColumn<Student, String> excuse_col;

    @FXML
    private JFXComboBox<String> subjs;

    @FXML
    private JFXComboBox<String> section;

    @FXML
    private JFXTextField searchFiled;

    // get logged in teacher
    private Teacher logged = LoginModel.getLogged();

    // an observable list of students
    private ObservableList<Student> students = FXCollections.observableArrayList();

    // get connection
    private static Connection conns = Connect.getConnect();

    private Calendar cal = Calendar.getInstance();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        Operations.LoadData(list_table, students); // load table on class start
        // define what each column is going to hold (based on student class)
        id_col.setCellValueFactory(new PropertyValueFactory<>("ID"));
        name_col.setCellValueFactory(new PropertyValueFactory<>("name"));
        present_col.setCellValueFactory(new PropertyValueFactory<>("present"));
        excuse_col.setCellValueFactory(new PropertyValueFactory<>("excuse"));

        list_table.setItems(students); // set table items as the Students observable list
        list_table.setEditable(true); // enable table editing
        excuse_col.setCellFactory(TextFieldTableCell.forTableColumn()); // enable column editing
        list_table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY); // resize column based on whole table(window) size

        for (Student stud : students) {
            stud.getPresent().setOnAction(e -> updateAtten(stud));
        }

        subjs.getItems().add("All");
        Map<String, String[]> subs = logged.getSubjects();
        for (String name : subs.keySet()) {
            subjs.getItems().add(name);
        }
        subjs.getSelectionModel().selectFirst();

    }

    private static void updateAtten(Student stud) {
        Teacher logged  = LoginModel.getLogged();
        String query = "update '" + logged.getID() + "' set present = ? where id = ?"; // sql query
        try {
            checkConn(); // check connection
            PreparedStatement psts = conns.prepareStatement(query);
            psts.setBoolean(1, stud.getPresent().isSelected());
            psts.setString(2, String.valueOf(stud.getID())); // pass the id of the column that has to be edited
            conns.isClosed();
            psts.execute(); // execute query
            psts.close();
            conns.close(); // close connection for now
            System.out.println("done");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    // this method can edit any selected row (if you have enabled editing for it)
    public void UpdateExcuse(TableColumn.CellEditEvent edditedcell) {
        Student selected = list_table.getSelectionModel().getSelectedItem(); // get the student being edited right now
        String query = "update '" + logged.getID() + "' set " + edditedcell.getTableColumn().getText().toLowerCase()
                + " = ? where id = ?"; // sql query
        try {
            checkConn(); // check connection
            PreparedStatement pst = Objects.requireNonNull(conns).prepareStatement(query);
            pst.setString(1, edditedcell.getNewValue().toString()); // pass the inputted value to be updated in that row
            pst.setString(2, String.valueOf(selected.getID())); // pass the id of the column that has to be edited
            pst.execute(); // execute query
            pst.close();
            conns.close(); // close connection for now
            LoadData(list_table, students); // reload tables after updating field
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @FXML
    void Filter() {
        FilterData(searchFiled, students, list_table);
    }

    @FXML
    void setClass(ActionEvent event) throws SQLException {
        DropFilter((JFXComboBox) event.getSource(), section, subjs, list_table, students, logged);
        for (Student stud : students) {
            stud.getPresent().setOnAction(e -> updateAtten(stud));
        }
    }

    private static void checkConn() {
        // if connection is closed get it again
        try {
            if (conns.isClosed()) conns = Connect.getConnect();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
