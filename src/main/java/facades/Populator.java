/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facades;

import javax.persistence.EntityManagerFactory;

import entities.User;
import errorhandling.IllegalAgeException;
import errorhandling.InvalidPasswordException;
import errorhandling.InvalidUsernameException;
import utils.EMF_Creator;

/**
 *
 * @author tha
 */
public class Populator {
    public static void populate() throws InvalidPasswordException, InvalidUsernameException, IllegalAgeException {
        EntityManagerFactory emf = EMF_Creator.createEntityManagerFactory();
        UserFacade userFacade = UserFacade.getUserFacade(emf);
        userFacade.createUser(new User("owais","1234",26));
        userFacade.createUser(new User("daniel","1234",26));
        userFacade.createUser(new User("andreas","1234",23));
        userFacade.createUser(new User("thomas","1234",36));
    }
    
    public static void main(String[] args) throws InvalidPasswordException, InvalidUsernameException, IllegalAgeException {
        populate();
    }
}
