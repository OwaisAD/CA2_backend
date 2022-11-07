package dtos;

import entities.User;

public class UserDTO {
    private int id;
    private String username;
    private int age;

    public UserDTO(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.age = user.getAge();
    }



}
