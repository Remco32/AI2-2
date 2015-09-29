import java.io.*;
import java.util.*;

//TODO spaties verwijderen
//TODO numerals


public class Bayespam
{
    // This defines the two types of messages we have.
    static enum MessageType
    {
        NORMAL, SPAM
    }

    ///Counts amount of messages of the input.
    public static void priorProbability(){
        System.out.print(listing_regular);
    }

    ///Check for words with more than 3 letters.
    public static boolean moreThanThreeLetters(String word){
        boolean bool;
        if(word.length() > 3){
            bool = true;
        }
        else{
            bool = false;
        }
        return bool;
        }

    ///Removes punctuation, numerals, all chars to lowercase
    public static String formatter(String word){
        ///Remove any non-alphabet character
        return word.replaceAll("[^a-zA-Z ]", "").toLowerCase();
    }

    // This a class with two counters (for regular and for spam)
    static class Multiple_Counter
    {
        int counter_spam    = 0;
        int counter_regular = 0;

        // Increase one of the counters by one
        public void incrementCounter(MessageType type) {
            if (type == MessageType.NORMAL) {
                ++counter_regular;
            } else {
                ++counter_spam;
            }
        }
    }

    // Listings of the two subdirectories (regular/ and spam/)
    private static File[] listing_regular = new File[0];
    private static File[] listing_spam = new File[0];

    // A hash table for the vocabulary (word searching is very fast in a hash table)
    private static Hashtable <String, Multiple_Counter> vocab = new Hashtable <String, Multiple_Counter> ();

    
    // Add a word to the vocabulary
    private static void addWord(String word, MessageType type)
    {
        Multiple_Counter counter = new Multiple_Counter();
        if(word.length() < 4){ //TODO check
            return;
        }
        word = formatter(word);

        if ( vocab.containsKey(word) ){                  // if word exists already in the vocabulary..
            counter = vocab.get(word);                  // get the counter from the hashtable
        }
        counter.incrementCounter(type);                 // increase the counter appropriately

        vocab.put(word, counter);                       // put the word with its counter into the hashtable
    }


    // List the regular and spam messages
    private static void listDirs(File dir_location)
    {
        // List all files in the directory passed
        File[] dir_listing = dir_location.listFiles();

        // Check that there are 2 subdirectories
        if ( dir_listing.length != 2 )
        {
            System.out.println( "- Error: specified directory does not contain two subdirectories.\n" );
            Runtime.getRuntime().exit(0);
        }

        listing_regular = dir_listing[0].listFiles();
        listing_spam    = dir_listing[1].listFiles();
    }

    
    // Print the current content of the vocabulary
    private static void printVocab()
    {
        Multiple_Counter counter = new Multiple_Counter();

        for (Enumeration<String> e = vocab.keys() ; e.hasMoreElements() ;)
        {   
            String word;
            
            word = e.nextElement();
            counter  = vocab.get(word);
            
            System.out.println(word + " | in regular: " + counter.counter_regular +
                    " in spam: " + counter.counter_spam);
        }
    }


    // Read the words from messages and add them to your vocabulary. The boolean type determines whether the messages are regular or not  
    private static void readMessages(MessageType type)
    throws IOException
    {
        File[] messages = new File[0];

        if (type == MessageType.NORMAL){
            messages = listing_regular;
        } else {
            messages = listing_spam;
        }
        
        for (int i = 0; i < messages.length; ++i)
        {
            FileInputStream i_s = new FileInputStream( messages[i] );
            BufferedReader in = new BufferedReader(new InputStreamReader(i_s));
            String line;
            String word;
            
            while ((line = in.readLine()) != null)                      // read a line
            {
                StringTokenizer st = new StringTokenizer(line);         // parse it into words
        
                while (st.hasMoreTokens())                  // while there are still words left..
                {

                    final String next = st.nextToken();
                    //st.nextToken() =

                    if(moreThanThreeLetters(next)) { /// More than 3 letters? //TODO still doesn't work
                    addWord(next, type);// add them to the vocabulary
                }
                }
            }

            in.close();
        }
    }


   
    public static void main(String[] args)
    throws IOException
    {
        // Location of the directory (the path) taken from the cmd line (first arg)
        File dir_location = new File( args[0] );
        
        // Check if the cmd line arg is a directory
        if ( !dir_location.isDirectory() )
        {
            System.out.println( "- Error: cmd line arg not a directory.\n" );
            Runtime.getRuntime().exit(0);
        }

        // Initialize the regular and spam lists
        listDirs(dir_location);

        // Read the e-mail messages
        readMessages(MessageType.NORMAL);
        readMessages(MessageType.SPAM);

        ///
        ///priorProbability();

        // Print out the hash table
        printVocab();
        
        // Now all students must continue from here:
        //
        // 1) A priori class probabilities must be computed from the number of regular and spam messages /// Aantal messages tellen van de inputs
        // 2) The vocabulary must be clean: punctuation and digits must be removed, case insensitive
        // 3) Conditional probabilities must be computed for every word
        // 4) A priori probabilities must be computed for every word
        // 5) Zero probabilities must be replaced by a small estimated value
        // 6) Bayes rule must be applied on new messages, followed by argmax classification
        // 7) Errors must be computed on the test set (FAR = false accept rate (misses), FRR = false reject rate (false alarms))
        // 8) Improve the code and the performance (speed, accuracy)
        //
        // Use the same steps to create a class BigramBayespam which implements a classifier using a vocabulary consisting of bigrams
    }
}