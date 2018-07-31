package courseprojectno4;

/**
 * CourseProjectNo4: CardPrefixes.java
 * Valid bank card prefixes
 *      - 4 - for Visa cards
 *      - 5 - for Master cards
 *      - 6 - for Discover cards
 *      - 37 - for American Express cards
 */
public enum CardPrefixes {
    VISA(4),
    MASTER(5),
    DISCOVER(6),
    AMERICAN_EXPRESS_FIRST(3),
    AMERICAN_EXPRESS_SECOND(7);
    
    /**
     * Card prefix
     */
    private final int prefix;
    
    /**
     * Constructor
     * @param prefix 
     */
    private CardPrefixes(int prefix){
        this.prefix = prefix;
    }
    
    /**
     * Get the card prefix
     * 
     * @return prefix 
     */
    public int getPrefix(){
        return prefix;
    }
}
