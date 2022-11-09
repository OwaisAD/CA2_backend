package facades;

import entities.Movie;

import javax.persistence.*;

public class MovieFacade {

    private static EntityManagerFactory emf;

    private static MovieFacade facade;


    private MovieFacade() {
    }


    public static MovieFacade getFacade(EntityManagerFactory _emf) {
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

    public Movie getMovieByTitleAndYear(String title, Integer year) {
        EntityManager em = emf.createEntityManager();
        Movie movie;
        try {
            TypedQuery<Movie> query = em.createQuery("SELECT m FROM Movie m WHERE m.title =:title " +
                    "AND m.year =:year", Movie.class);
            query.setParameter("title", title);
            query.setParameter("year", year);
            movie = query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        } finally {
            em.close();
        }
        return movie;
    }

    public Movie getMovieById(int id) {
        EntityManager em = emf.createEntityManager();
        Movie movie;
        try {
            em.getTransaction().begin();
            movie = em.find(Movie.class, id);
            em.getTransaction().commit();
        } catch (NoResultException e) {
            return null;
        } finally {
            em.close();
        }
        return movie;
    }
}

