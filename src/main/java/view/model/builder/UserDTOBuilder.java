package view.model.builder;

import javafx.collections.ObservableList;
import model.Role;
import view.model.UserDTO;

public class UserDTOBuilder {
    private final UserDTO userDTO;

    public UserDTOBuilder(){
        userDTO = new UserDTO();
    }

    public UserDTOBuilder setId(Long id){
        userDTO.setId(id);
        return this;
    }

    public UserDTOBuilder setUsername(String username){
        userDTO.setUsername(username);
        return this;
    }

    public UserDTOBuilder setRoles(ObservableList<Role> roles){
        userDTO.setRoles(roles);
        return this;
    }

    public UserDTO build(){
        return userDTO;
    }
}
