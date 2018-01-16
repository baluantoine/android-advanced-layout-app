package sm.fr.advancedlayoutapp.model;

/**
 * Created by Formation on 16/01/2018.
 */

public class User {
    private String userName = "anonymous";

    public User() {
    }

    public User(String userName) {
        this.userName = userName;
    }

    public String getUserName() {
        return userName;
    }

    public User setUserName(String userName) {
        this.userName = userName;
        return this;
    }
}
