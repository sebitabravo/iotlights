package modelo;

import com.google.firebase.firestore.Exclude;

public class Light {
    private String name;
    private boolean isOn;

    // Constructor vacío
    public Light() {}

    public Light(String name, boolean isOn) {
        this.name = name;
        this.isOn = isOn;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isOn() {
        return isOn;
    }

    public void setOn(boolean on) {
        isOn = on;
    }

    public String getId() {
        return "";
    }
}
