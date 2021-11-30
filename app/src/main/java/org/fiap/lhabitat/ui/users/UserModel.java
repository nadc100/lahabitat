package org.fiap.lhabitat.ui.users;

import java.util.HashMap;

public class UserModel {
    public String id;
    public String username;
    public String email;
    public String password;
    public HashMap<String, Boolean> property;

    public UserModel() {
    }

    public UserModel(String id, String username, String email, String password, HashMap<String, Boolean> property) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.property = new HashMap<>();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public HashMap<String, Boolean> getProperty() {
        return property;
    }

    public void setProperty(HashMap<String, Boolean> property) {
        this.property = property;
    }
}
