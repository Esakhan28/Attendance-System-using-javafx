package Classes;

import javafx.scene.control.CheckBox;

import java.util.Map;

public class Student extends Person {
    private String absences; // number of times student was absent
    private String excuse;
    private CheckBox present = new CheckBox();
    private String bar_status;

    // a constructor for the students list page
    public Student(int ID, String name, String gender, String mail, String absences, String bar_status,
                   Map<String, String[]> subjects, Boolean present, String excuse) {
        this.name = name;
        this.ID = ID;
        this.gender = gender;
        this.email = mail;
        this.absences = absences;
        this.bar_status = bar_status;
        this.subjects = subjects;
        this.present.setSelected(present);
        this.excuse = excuse;
    }

    public String getAbsences() {
        return absences;
    }

    public void setAbsences(String absences) {
        this.absences = absences;
    }

    public String getExcuse() {
        return excuse;
    }

    public void setExcuse(String excuse) {
        this.excuse = excuse;
    }

    public CheckBox getPresent() {
        return present;
    }

    public void setPresent(Boolean present) {
        this.present.setSelected(present);
    }

    public String getBar_status() {
        return bar_status;
    }

    public void setBar_status(String bar_status) {
        this.bar_status = bar_status;
    }

    @Override
    public Map<String, String[]> getSubjects() {
        return subjects;
    }
}
