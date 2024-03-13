package Classes;

import java.util.Map;

public class Teacher extends Person {
    private String Pass; // login password
    private String XP; // experience (dunno why i called it XP)

    public Teacher(String name, String pass, int ID, String gender, String email, Map<String, String[]> subjects, String XP, long phone) {
        this.name = name;
        Pass = pass;
        this.ID = ID;
        this.gender = gender;
        this.email = email;
        this.XP = XP;
        this.subjects = subjects;
        this.phone = phone;
    }

    public String getXP() {
        return XP;
    }

}
