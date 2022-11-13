package facades;

import entities.User;

import javax.persistence.*;
import javax.ws.rs.BadRequestException;

import errorhandling.IllegalAgeException;
import errorhandling.InvalidUsernameException;
import errorhandling.MissingDataException;
import errorhandling.UsernameAlreadyExists;
import org.eclipse.persistence.exceptions.DatabaseException;
import security.errorhandling.AuthenticationException;

import java.sql.SQLIntegrityConstraintViolationException;

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
    public static UserFacade getFacade(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new UserFacade();
        }
        return instance;
    }

    public User getVerifiedUser(String username, String password) throws AuthenticationException {
        EntityManager em = emf.createEntityManager();
        User user;
        try {
            TypedQuery<User> query = em.createQuery("SELECT u FROM User u WHERE u.username =:username ",User.class);
            query.setParameter("username",username);
            try {
                user = query.getSingleResult();
            } catch (NoResultException e) {
                throw new AuthenticationException("Invalid user name or password");
            }

            if (!user.verifyPassword(password)) {
                throw new AuthenticationException("Invalid user name or password");
            }
        } finally {
            em.close();
        }
        return user;
    }

    public User createUser(User user) throws IllegalAgeException, InvalidUsernameException, MissingDataException {
        EntityManager em = emf.createEntityManager();

        String errorMsg = "";

        if(user.getUsername() == null || user.getUsername().equals("")) {
            throw new InvalidUsernameException("Username cannot be null or an empty string.");
        }

        if(user.getUsername().length() < MINIMUM_USERNAME_LENGTH || user.getUsername().length() > MAXIMUM_USERNAME_LENGTH) {
            errorMsg += "Username length should be between " + MINIMUM_USERNAME_LENGTH + " and " +
                    + MAXIMUM_USERNAME_LENGTH+ " characters. ";
        }

        if(user.getAge() < MINIMUM_AGE || user.getAge() > MAXIMUM_AGE) {
            errorMsg += "Age should be between 13 and 120";
        }

        if(!errorMsg.equals("")) {
            throw new MissingDataException(errorMsg);
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

    public void updateUser(User user) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(user);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    public User getUserById(int id) {
        EntityManager em = emf.createEntityManager();
        User user = em.find(User.class,id);
        em.close();
        if (user == null) {
            throw new EntityNotFoundException("User with id: "+id+" does not exist in database");
        }
        return user;
    }

    public boolean checkIfUserAlreadyHasMovie(int userId, int movieId) {
        EntityManager em = emf.createEntityManager();
        try {
            Query query = em.createQuery("SELECT count(um) FROM UserMovie um WHERE um.movie.id=:movieid AND um.user.id=:userid");
            query.setParameter("movieid", movieId);
            query.setParameter("userid", userId);
            long sameMovieCount = (long) query.getSingleResult();
            if(sameMovieCount > 0) {
                return true;
            }
            return false;
        } finally {
            em.close();
        }
    }

    public void checkIfUserAlreadyExist(String username) throws UsernameAlreadyExists {
        EntityManager em = emf.createEntityManager();

        try {
            Query query = em.createQuery("SELECT count(u) FROM User u WHERE u.username=:username");
            query.setParameter("username", username);
            long userCount = (long) query.getSingleResult();
            if(userCount > 0) {
                throw new UsernameAlreadyExists();
            }
        } finally {
            em.close();
        }
    }
}
