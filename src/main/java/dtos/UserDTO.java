package dtos;

import entities.User;

public class UserDTO {
    private int id;
    private String username;

    private String password;
    private int age;



    public UserDTO(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.password = user.getPassword();
        this.age = user.getAge();
    }

    public UserDTO(int id, String username, int age) {
        this.id = id;
        this.username = username;
        this.age = age;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public int getAge() {
        return age;
    }
}
