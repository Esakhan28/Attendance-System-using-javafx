package Classes;

import java.util.HashMap;
import java.util.Map;

// i dont think this needs any comments
public class Person {
    String name;
    int ID;
    String gender;
    String email;
    long phone;
    Map<String, String[]> subjects = new HashMap<>();

    Person() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public long getPhone() {
        return phone;
    }

    public void setPhone(long phone) {
        this.phone = phone;
    }

    public Map<String, String[]> getSubjects() {
        return subjects;
    }
}
