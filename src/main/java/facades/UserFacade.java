package facades;

import entities.User;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import errorhandling.IllegalAgeException;
import errorhandling.InvalidUsernameException;
import errorhandling.MissingDataException;
import security.errorhandling.AuthenticationException;

/**
 * @author lam@cphbusiness.dk
 */
public class UserFacade {

    private static EntityManagerFactory emf;
    private static UserFacade instance;

    public static final int MINIMUM_AGE = 13;
    public static final int MAXIMUM_AGE = 120;

    public static final int MINIMUM_USERNAME_LENGTH = 3;
    public static final int MAXIMUM_USERNAME_LENGTH = 20;

    private UserFacade() {
    }

    /**
     *
     * @param _emf
     * @return the instance of this facade.
     */
    public static UserFacade getUserFacade(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new UserFacade();
        }
        return instance;
    }

    public User getVeryfiedUser(String username, String password) throws AuthenticationException {
        EntityManager em = emf.createEntityManager();
        User user;
        try {
            user = em.find(User.class, username);
            if (user == null || !user.verifyPassword(password)) {
                throw new AuthenticationException("Invalid user name or password");
            }
        } finally {
            em.close();
        }
        return user;
    }


    public User createUser(User user) throws IllegalAgeException, InvalidUsernameException {
        EntityManager em = emf.createEntityManager();

        if(user.getAge() < MINIMUM_AGE || user.getAge() > MAXIMUM_AGE) {
            throw new IllegalAgeException(user.getAge());
        }

        if(user.getUsername() == null || user.getUsername().equals("")) {
            throw new InvalidUsernameException("Username cannot be null or an empty string");
        }

        if(user.getUsername().length() < 3 || user.getUsername().length() > 20) {
            throw new InvalidUsernameException("Username length should be between 3 and 20 characters");
        }

        try {
            em.getTransaction().begin();
            em.persist(user);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
        return user;
    }
}
