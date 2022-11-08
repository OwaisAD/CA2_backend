package facades;

import entities.Movie;
import errorhandling.IllegalAgeException;
import errorhandling.InvalidUsernameException;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

public class MovieFacade {

    private static EntityManagerFactory emf;

    private static MovieFacade facade;


    private MovieFacade() {
    }


    public static MovieFacade getMovieFacade(EntityManagerFactory _emf) {
        if (facade == null) {
            emf = _emf;

            facade = new MovieFacade();
        }
        return facade;
    }


    public Movie createMovie(Movie movie) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(movie);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
        return movie;
    }

    public void removeMovie(Movie movie) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            if(!em.contains(movie)){
                movie = em.merge(movie);
            }
            em.remove(movie);
            em.getTransaction().commit();
        } finally {
            em.close();
        }

    }
}

