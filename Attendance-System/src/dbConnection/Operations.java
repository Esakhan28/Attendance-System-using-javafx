package dbConnection;

import Classes.Student;
import Classes.Teacher;
import Login.LoginModel;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.scene.control.TableView;

import java.sql.*;
import java.util.*;

public class Operations {
    private static Connection conns = Connect.getConnect();

    public static void FilterData(JFXTextField searchFiled, ObservableList<Student> students, TableView<Student> list_table) {
        FilteredList<Student> filteredList = new FilteredList<>(students, p -> true);
        searchFiled.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredList.setPredicate(student -> {
                if (newValue == null || newValue.isEmpty()) return true;
                String word = newValue.toLowerCase();
                if (student.getID() != 0) { // to ignore empty fields (otherwise its a NullPointer ma dude)
                    if (String.valueOf(student.getID()).toLowerCase().contains(word)) return true;
                }
                if (student.getName() != null) { // to ignore empty fields (otherwise its a NullPointer ma dude)
                    if (student.getName().toLowerCase().contains(word)) return true;
                }
                if (student.getAbsences() != null) { // to ignore empty fields (otherwise its a NullPointer ma dude)
                    if (student.getAbsences().toLowerCase().contains(word)) return true;
                }
                if (student.getBar_status() != null) { // to ignore empty fields (otherwise its a NullPointer ma dude)
                    if (student.getBar_status().toLowerCase().contains(word)) return true;
                }
                if (student.getEmail() != null) { // to ignore empty fields (otherwise its a NullPointer ma dude)
                    return student.getEmail().toLowerCase().contains(word);
                }
                return false;
            });
            SortedList<Student> sortedList = new SortedList<>(filteredList);
            sortedList.comparatorProperty().bind(list_table.comparatorProperty());
            list_table.setItems(sortedList);
        });
    }

    private static void FilterClass(TableView<Student> list_table, ObservableList<Student> students,
                                    JFXComboBox<String> subjs, String diff) throws SQLException {
        Teacher logged = LoginModel.getLogged();
        list_table.setItems(students); // surpass the error when the observable list being used is the filtered one
        list_table.getItems().clear(); // clear table content before adding them again
        String selectedItem;
        if (!diff.equals("")) selectedItem = diff + "," + subjs.getSelectionModel().getSelectedItem();
        else selectedItem = subjs.getSelectionModel().getSelectedItem();
        checkConn(); // check connection

        String st = "select * from '" + logged.getID() + "' where subjects like " + "'%" + selectedItem + "%'";
        ResultSet rs = Objects.requireNonNull(conns).createStatement().executeQuery(st);
        while (rs.next()) {
            // store each row in a student object
            students.add(new Student(rs.getInt("ID"), rs.getString("name"),
                    rs.getString("gender"), rs.getString("email"), rs.getString("absences"),
                    rs.getString("bar"), parseSubjs(rs), rs.getBoolean("present"), rs.getString("excuse")));

        }
    }

    public static void LoadData(TableView<Student> list_table, ObservableList<Student> students) {
        list_table.getItems().clear(); // clear table content before adding them again
        try {
            checkConn(); // check connection
            Teacher logged = LoginModel.getLogged();
            String query = "select * from '" + logged.getID() + "'";
            ResultSet rs = Objects.requireNonNull(conns).prepareStatement(query).executeQuery();
            while (rs.next()) {
                // store each row in a student object
                students.add(new Student(rs.getInt("ID"), rs.getString("name"),
                        rs.getString("gender"), rs.getString("email"), rs.getString("absences"),
                        rs.getString("bar"), parseSubjs(rs), rs.getBoolean("present"), rs.getString("excuse")));
            }
            rs.close(); // close statement
            conns.close(); // close connection for now
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static Map<String, String[]> parseSubjs(ResultSet resultSet) throws SQLException {
        String[] subjs = resultSet.getString("subjects").split("( )");
        String[][] sections = new String[subjs.length][];
        Map<String, String[]> subjects = new HashMap<>();
        for (int i = 0; i < subjs.length; i++) {
            subjs[i] = subjs[i].replace("(", "");
            subjs[i] = subjs[i].replace(")", "");
            sections[i] = subjs[i].split(",");
        }
        for (int i = 0; i < sections.length; i++) {
            String[] finale = Arrays.copyOfRange(sections[i], 1, sections[i].length);
            subjects.put(subjs[i].split(",")[0], finale);
        }
        return subjects;
    }

    public static void DropFilter(JFXComboBox source, JFXComboBox<String> section, JFXComboBox<String> subjs,
                                  TableView<Student> list_table, ObservableList<Student> students, Teacher logged) throws SQLException {
        if (source.getId().equals("subjs")) {
            section.getItems().clear();
            section.getSelectionModel().clearSelection();
            if (subjs.getSelectionModel().getSelectedItem().contains("All")) {
                LoadData(list_table, students); // load regular view
                section.setDisable(true);
                return; // terminate the method.
            }
            section.setDisable(false);
            Map<String, String[]> secs = logged.getSubjects();
            String[] sec = secs.get(subjs.getSelectionModel().getSelectedItem());
            for (String s : sec) {
                section.getItems().add(s);
            }
            FilterClass(list_table, students, subjs, "");
        } else if (source.getId().equals("section")){
            FilterClass(list_table, students, section, subjs.getSelectionModel().getSelectedItem());
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
