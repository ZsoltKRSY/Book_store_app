package view.model;

import javafx.beans.property.*;
import javafx.collections.ObservableList;
import model.Role;

import java.util.List;

public class UserDTO {
    private LongProperty id;

    public LongProperty idProperty() {
        if (id == null)
            id = new SimpleLongProperty(this, "id");

        return id;
    }

    public void setId(Long id) {
        idProperty().set(id);
    }

    public Long getId() {
        return idProperty().get();
    }

    private StringProperty username;

    public StringProperty usernameProperty() {
        if (username == null)
            username = new SimpleStringProperty(this, "username");

        return username;
    }

    public void setUsername(String username) {
        usernameProperty().set(username);
    }

    public String getUsername() {
        return usernameProperty().get();
    }

    private ListProperty<Role> roles;

    public ListProperty<Role> rolesProperty(){
        if(roles == null)
            roles = new SimpleListProperty<>(this, "roles");

        return roles;
    }

    public void setRoles(ObservableList<Role> roles){
        rolesProperty().set(roles);
    }

    public List<Role> getRoles(){
        return rolesProperty().get();
    }
}
