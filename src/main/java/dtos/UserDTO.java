package dtos;

import entities.User;

import java.util.List;

public class UserDTO {
    private Integer id;
    private String username;

    private String password;
    private Integer age;

    private List<String> roles;



    public UserDTO(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.password = user.getPassword();
        this.age = user.getAge();

    }

    public UserDTO(Integer id, String username, Integer age, List<String> roles) {
        this.id = id;
        this.username = username;
        this.age = age;
        this.roles = roles;
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

    public void setId(Integer id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public void setAge(Integer age) {
        this.age = age;
    }



}
