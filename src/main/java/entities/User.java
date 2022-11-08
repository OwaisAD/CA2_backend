package entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import errorhandling.InvalidPasswordException;
import org.mindrot.jbcrypt.BCrypt;

@Entity
@Table(name = "users")
public class User implements Serializable, entities.Entity{

  public static final int MINIMUM_PASSWORD_LENGTH = 4;

  private static final long serialVersionUID = 1L;
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false)
  private Integer id;

  @NotNull
  @Column(name = "username", length = 25, unique = true)
  private String username;
  @Basic(optional = false)
  @NotNull
  @Size(min = 1, max = 255)
  @Column(name = "password")
  private String password;

  @NotNull
  @Column(name = "age")
  private Integer age;

  @ManyToMany
  @JoinTable(name = "user_roles", joinColumns = {
          @JoinColumn(name = "user_id", referencedColumnName = "id")}, inverseJoinColumns = {
          @JoinColumn(name = "role_id", referencedColumnName = "id")})
  private List<Role> roleList = new ArrayList<>();



  @ManyToMany(cascade = CascadeType.PERSIST)
  @JoinTable(name = "user_movie",
          joinColumns = @JoinColumn(name = "movie_id"),
          inverseJoinColumns = @JoinColumn(name = "user_id"))
  private List<Movie> movies = new ArrayList<>();

  public List<String> getRolesAsStrings() {
    if (roleList.isEmpty()) {
      return null;
    }
    List<String> rolesAsStrings = new ArrayList<>();
    roleList.forEach((role) -> {
      rolesAsStrings.add(role.getRoleName());
    });
    return rolesAsStrings;
  }

  public User() {}

  //TODO Change when password is hashed
  public boolean verifyPassword(String pw){
    return BCrypt.checkpw(pw, password);
    //return(pw.equals(userPass));
  }

  private String validateAndHashPassword(String password) throws InvalidPasswordException {
      //check if empty or null?

      if(password.length() < MINIMUM_PASSWORD_LENGTH) {
        throw new InvalidPasswordException("Password is too short");
      }
      return BCrypt.hashpw(password, BCrypt.gensalt());
  }

  public User(String username, String password) throws InvalidPasswordException {
    this.username = username;
    this.password = validateAndHashPassword(password);
  }

  public User(String username, String password, int age) throws InvalidPasswordException {
    this.username = username;
    this.password = validateAndHashPassword(password);
    this.age = age;
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String userName) {
    this.username = userName;
  }

  public String getPassword() {
    return this.password;
  }

  public List<Role> getRoleList() {
    return roleList;
  }

  public void setRoleList(List<Role> roleList) {
    this.roleList = roleList;
  }

  public void addRole(Role userRole) {
    roleList.add(userRole);
  }

  public Integer getAge() {
    return age;
  }

  public void setAge(Integer age) {
    this.age = age;
  }
}