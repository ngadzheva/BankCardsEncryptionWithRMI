package courseprojectno4;

/**
 * CourseProjectNo4: UserType.java
 * Represents user types and their rights
 *      - administrator - has all rights (to create new users, to encrypt and
 *          decrypt bank cards)
 *      - user - has rights only to encrypt and decrypt bank cards
 *      - guest - doesn't have any rights
 */
public enum UserType {
    ADMIN(1),
    USER(0),
    GUEST(-1);
    
    /**
     * Represent the user's level:
     *  - 1 - for administrator
     *  - 0 - for users
     *  - -1 - for guests
     */
    private final int userLevel;
    
    /**
     * Constructor
     * @param level 
     */
    private UserType(int level){
        userLevel = level;
    }
    
    /**
     * Get the user level
     * 
     * @return userLevel
     */
    public int getUserLevel(){
        return userLevel;
    }

    /**
     * Override method toString for UserType
     * @return type
     */
    @Override
    public String toString() {
        String type = "";
        
        switch(this){
            case ADMIN: type = "admin";
                        break;
            case USER: type = "user";
                        break;
            case GUEST: type =  "guest";
                        break;
        }
        
        return type;
    }
    
    
}
