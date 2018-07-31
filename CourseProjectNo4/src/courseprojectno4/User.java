package courseprojectno4;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

/**
 * CourseProjectNo4: User.java
 * Represents an information for a user
 */
public class User {
    /**
     * User data:
     *  - user name
     *  - password
     *  - user level - represents the user permissions in the system
     */
    private String userName;
    private String password;
    private UserType userLevel;

    /**
     * General purpose constructor
     * @param userName
     * @param password
     * @param userLevel 
     */
    public User(String userName, String password, UserType userLevel) {
        setUserName(userName);
        setPassword(password);
        setUserLevel(userLevel);
    }
    
    public User(){
        this("","",UserType.GUEST);
    }
    
    /**
     * Copy constructor
     * @param user
     */
    public User(User user) {
        this(new String(user.userName), new String(user.password), user.userLevel);
    }

    /**
     * Get the value of the user level
     * 
     * @return the value of the user level
     */
    public final UserType getUserLevel() {
        UserType temp = userLevel;
        
        return temp;
    }

    /**
     * Set the value of the user level
     * 
     * @param userLevel 
     */
    private final void setUserLevel(UserType userLevel) {
        this.userLevel = userLevel != null ? userLevel : UserType.GUEST;
    }

    /**
     * Get the value of password
     *
     * @return the value of password
     */
    public final String getPassword() {
        return new String(password);
    }

    /**
     * Set the value of password
     *
     * @param password new value of password
     */
    private final void setPassword(String password) {
        this.password = password != null ? new String(password) : new String("");
    }


    /**
     * Get the value of userName
     *
     * @return the value of userName
     */
    public final String getUserName() {
        return new String(userName);
    }

    /**
     * Set the value of userName
     *
     * @param userName new value of userName
     */
    private final void setUserName(String userName) {
        this.userName = userName != null ? new String(userName) : new String(userName);
    }

    /**
     * Override method equals for class User
     * 
     * @param obj
     * @return true if the two User objects are equal, else return false
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final User other = (User) obj;
        if (!Objects.equals(this.userName, other.userName)) {
            return false;
        }
        if (!Objects.equals(this.password, other.password)) {
            return false;
        }
        if (this.userLevel != other.userLevel) {
            return false;
        }
        
        return true;
    }

    /**
     * Override method toString for class User
     * @return userInfo
     */
    @Override
    public String toString() {
        String userInfo = String.format("User: %s %nPassword: %s %nPermissions: %s"
                + "%nBank cards: %s%n", userName, password, userLevel.toString());
        
        return userInfo;
    }

    
}
