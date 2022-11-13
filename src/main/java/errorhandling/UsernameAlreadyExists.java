package errorhandling;

import facades.UserFacade;

public class UsernameAlreadyExists extends Exception {
    public UsernameAlreadyExists(String message) {
        super(message);
    }

    public UsernameAlreadyExists() {
        super("Username already exists..");
    }
}
