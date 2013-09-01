package classics.kmp;

/**
 * An implementation of the KMP (Knuth-Morris-Pratt) exact string matching algorithm.
 * 
 * The algorithm consists of 2 main parts:
 * 
 * 1. The prefix function
 * 2. The KMP matcher
 * 
 * The prefix function is essential to the cost-saving nature of the algorithm,
 * as it preprocesses the supplied pattern and stores information to help the matcher
 * take shortcuts and "skip over" characters.
 * 
 * 
 * @author Zac Chenaille
 */
public class KMPAlgorithm {
    
    private String pattern;
    private String text;
    
    public KMPAlgorithm(String text, String pattern) {
        this.pattern = pattern;
        this.text = text;
    }
    
    public void run() {
        int textLength = text.length();
        int patternLength = pattern.length();
        
        //Generate the prefix array
        int[] prefixArray = generatePrefixArray();
        
        char[] textChars = text.toCharArray();
        char[] patternChars = pattern.toCharArray();
        
        int patternIndex = -1;
        
        //Go through each character in the supplied text and attempt to match against the pattern
        for (int textIndex = 0; textIndex < textLength; textIndex++) {
            while (patternIndex > -1 && patternChars[patternIndex + 1] != textChars[textIndex]) {
                /*
                * If we had a match but the current character is a mismatch, check it against any other patterns
                * that may have been found within the supplied pattern
                */
                patternIndex = prefixArray[patternIndex];
            }
            
            if (patternChars[patternIndex + 1] == textChars[textIndex]) {
                //We found a match! increment the patternIndex to continue through the pattern
                patternIndex++;
            }
            
            //Have we matched the entire pattern?
            if ((patternIndex + 1) == patternLength) {
                //An exact string match was found! Print the index in the supplied text at which the pattern begins
                System.out.println("Pattern occurs starting at index " + ((textIndex + 1) - patternLength));
                
                //This is where we save computations. If there was an internal pattern with
                /*
                 * This is where we save computation. If there was an internal pattern within the supplied
                 * pattern, we don't need to start over from the beginning of the pattern as we continue
                 * to match against the text.
                 */
                patternIndex = prefixArray[patternIndex];
            }
        }
    }
    
    /**
     * Generates a prefix array for the supplied pattern. Essentially, the prefix array will represent
     * patterns that exist within the pattern.
     * 
     * @return A prefix array containing matched indexes for each character in the pattern
     */
    private int[] generatePrefixArray() {
        int patternLength = pattern.length();
        
        char[] patternChars = pattern.toCharArray();
        int[] prefixArray = new int[patternLength];
        
        /*
         * As we go through the pattern string, the matchIndex will be incremented as
         * patterns within the pattern are found. For example:
         * 
         * In the pattern "dogdogdog" the intial pattern "dog" begins again at index 2. Here, at
         * the start of the second "dog" the matchIndex will track which initial pattern character the 
         * other internal pattern characters match.
         * 
         * d o g d o g d o g <----- pattern
         * -----------------
         * x x x 0 1 2 3 4 5 <----- matchIndexes (prefix array)
         *   ^   ^   ^
         *   ^   ^   And so on
         *   ^   ^
         *   ^   Matches the character at index 0
         * No matches
         * 
         */
        int matchIndex = -1;
        
        //The first character can't possibly match anything (because there is nothing before it)
        prefixArray[0] = -1;
        
        //Go through each character in the pattern and attempt to find any internal patterns
        for (int patternIndex = 1; patternIndex < pattern.length(); patternIndex++) {
            /*
             * If we had a match but the current character is a mismatch, check it against any other patterns
             * that may have been found up to this point
             */
            while (matchIndex > -1 && patternChars[matchIndex + 1] != patternChars[patternIndex]) {
                matchIndex = prefixArray[matchIndex];
            }
            
            /*
             * If the current char matches a pattern, increment matchIndex to track which
             * previous index this char matches
             */
            if (patternChars[matchIndex + 1] == patternChars[patternIndex]) {
                matchIndex++;
            }
            //Put the current match index for this char into the prefix array
            prefixArray[patternIndex] = matchIndex;
        }
        return prefixArray;
    }
    
    
    public static void main(String[] args) {
        KMPAlgorithm kmp = new KMPAlgorithm("parallel", "rall");
        kmp.run();
    }
    
}
