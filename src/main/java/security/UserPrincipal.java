package security;

import entities.User;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UserPrincipal implements Principal {

  private String userId;
  private String username;

  private List<String> roles = new ArrayList<>();

  /* Create a UserPrincipal, given the Entity class User*/
  public UserPrincipal(User user) {
    this.userId = String.valueOf(user.getId());
    this.username = user.getUsername();
    this.roles = user.getRolesAsStringList();
  }

  public UserPrincipal( String userId, String username, String[] roles) {
    super();
    this.userId = userId;
    this.username = username;
    this.roles = Arrays.asList(roles);
  }

  @Override
  public String getName() {
    return userId;
  }

  public boolean isUserInRole(String role) {
    return this.roles.contains(role);
  }


}