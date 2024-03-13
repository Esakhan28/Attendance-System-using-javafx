package List;

import Classes.Student;
import Classes.Teacher;
import Login.LoginModel;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import dbConnection.Connect;
import javafx.beans.binding.BooleanBinding;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;

import static dbConnection.Operations.*;

public class ListController implements Initializable {

    @FXML
    private TableView<Student> list_table;

    @FXML
    private TableColumn<Student, Integer> id_col;

    @FXML
    private TableColumn<Student, String> name_col;

    @FXML
    private TableColumn<Student, String> past_col;

    @FXML
    private TableColumn<Student, String> mail_col;

    @FXML
    private TableColumn<Student, String> bar_col;

    @FXML
    private JFXTextField id_field;

    @FXML
    private JFXTextField name_field;

    @FXML
    private JFXTextField abs_field;

    @FXML
    private JFXTextField bar_field;

    @FXML
    private JFXTextField subCol;

    @FXML
    private JFXTextField mail_field;

    @FXML
    private JFXButton addBtn;

    @FXML
    private JFXButton delBtn;

    @FXML
    private JFXComboBox<String> subjs;


    @FXML
    private JFXComboBox<String> section;

    @FXML
    private JFXTextField searchFiled;

    // an observable list of students
    private ObservableList<Student> students = FXCollections.observableArrayList();

    // get logged in teacher
    private Teacher logged = LoginModel.getLogged();
    // get connection
    private Connection conns = Connect.getConnect();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        LoadData(list_table, students); // load table on class start
        // define what each column is going to hold (based on student class)
        id_col.setCellValueFactory(new PropertyValueFactory<>("ID"));
        name_col.setCellValueFactory(new PropertyValueFactory<>("name"));
        mail_col.setCellValueFactory(new PropertyValueFactory<>("email"));
        past_col.setCellValueFactory(new PropertyValueFactory<>("absences"));
        bar_col.setCellValueFactory(new PropertyValueFactory<>("bar_status"));


        list_table.setItems(students); // add students observable list to table

        // disable add button unless all fields are filled (binding)
        BooleanBinding isData = id_field.textProperty().isEmpty()
                .and(name_field.textProperty().isEmpty())
                .and(abs_field.textProperty().isEmpty())
                .and(mail_field.textProperty().isEmpty()
                        .and(subCol.textProperty().isEmpty()));
        addBtn.disableProperty().bind(isData);

        list_table.setEditable(true); // enable editing
        name_col.setCellFactory(TextFieldTableCell.forTableColumn()); // enable column editing
        mail_col.setCellFactory(TextFieldTableCell.forTableColumn()); // enable column editing
        past_col.setCellFactory(TextFieldTableCell.forTableColumn()); // enable column editing
        bar_col.setCellFactory(TextFieldTableCell.forTableColumn()); // enable column editing
        list_table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        //initialize the drop down menu
        subjs.getItems().add("All");
        Map<String, String[]> subs = logged.getSubjects();
        for (String name: subs.keySet()){
            subjs.getItems().add(name);
        }
        subjs.getSelectionModel().selectFirst();
    }

    @FXML
    void addRow() { // add new student
        checkConn();
        String query = "insert into '" + logged.getID() + "' (ID, name, email, absences, bar, subjects) values(?, ?, ?, ?, ?, ?)";

        try {
            PreparedStatement pst = Objects.requireNonNull(conns).prepareStatement(query);
            // set the question marks in statement to these values
            pst.setString(1, id_field.getText());
            pst.setString(2, name_field.getText());
            pst.setString(3, mail_field.getText());
            pst.setString(4, abs_field.getText());
            pst.setString(5, bar_field.getText());
            pst.setString(6, "'(" + subCol.getText() + ")");
            pst.execute();
            pst.close(); // close statement
            conns.close(); // close connection
            LoadData(list_table, students); // reload table after adding new student
        } catch (SQLException e) {
            e.printStackTrace();
        }
        // clear the text fields after finishing
        id_field.clear();
        name_field.clear();
        abs_field.clear();
        mail_field.clear();
        bar_field.clear();
        subCol.clear();
        // alert the user after successfully adding student
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Add new student");
        alert.setHeaderText("Student added successfully!");
        alert.show();
    }

    @FXML
    void deleteRow() { // delete a student
        checkConn();
        Student stud = list_table.getSelectionModel().getSelectedItem(); // get selected item
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Record");
        alert.setHeaderText("Are you sure You want to delete record with ID: " + stud.getID() + "?");
        ButtonType Yes = new ButtonType("Yes");
        ButtonType No = new ButtonType("No");
        alert.getButtonTypes().setAll(Yes, No);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == Yes) {
            String query = String.format("delete from '" + logged.getID() + "' where id=%d", stud.getID());
            try {

                PreparedStatement pst = Objects.requireNonNull(conns).prepareStatement(query);
                pst.execute();
                conns.close(); // close connection for now
                if (!subjs.getSelectionModel().getSelectedItem().equals("All")) LoadData(list_table, students); // just reload that view
                else LoadData(list_table, students); // reload tables after deleting row
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void updateCol(TableColumn.CellEditEvent edditedcell) {
        Student selected = list_table.getSelectionModel().getSelectedItem(); // get the student being edited right now
        String query = "update '" + logged.getID() + "' set " +
                edditedcell.getTableColumn().getText().toLowerCase().split(" ")[0] + " = ? where id = ?";
        try {
            checkConn(); // check connection
            PreparedStatement pst = Objects.requireNonNull(conns).prepareStatement(query); // sql statement
            pst.setString(1, edditedcell.getNewValue().toString()); // pass the inputted value to be updated in that row
            pst.setString(2, String.valueOf(selected.getID())); // pass the id of the column that has to be edited
            pst.execute(); // execute statement
            conns.close(); // close connection for now
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
    }

    private void checkConn() {
        // if connection is closed get it again
        try {
            if (conns.isClosed()) conns = Connect.getConnect();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
