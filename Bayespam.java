import java.io.*;
import java.util.*;

public class Bayespam {
	
	   // This defines the two types of messages we have.
    static enum MessageType
    {
        NORMAL, SPAM
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
        double conditionalprob_spam = 0;
        double conditionalprob_regular = 0;
        // Increase one of the counters by one
        public void incrementCounter(MessageType type) {
            if (type == MessageType.NORMAL) {
                ++counter_regular;
            } else {
                ++counter_spam;
            }
           
        }
        
        public void calculateProbability(float nWordsRegular, float nWordsSpam){
                  
        	      if (counter_regular == 0 && counter_spam > 0){
        	    	  conditionalprob_regular = -1 * Math.log(1/ (nWordsRegular + nWordsSpam));
        	      }else {
        	      conditionalprob_regular = -1 * Math.log(counter_regular / nWordsRegular);
        	      }
        	      
        	      if (counter_spam == 0 && counter_regular > 0){
        	    	  conditionalprob_spam = -1 * Math.log(1 / (nWordsRegular + nWordsSpam));
        	      }else {
                  conditionalprob_spam = -1 * Math.log(counter_spam /nWordsSpam);
        	      }
              }    
  
        
        
    }

    ///TODO give message as input
    ///Calculates the probability of a message being a regular email
    public static double probabilityRegular()
            throws IOException{
        File[] messages = new File[0];
        Multiple_Counter counter = new Multiple_Counter();

        ///TODO Calculate actual alpha value
        double alpha = 0.01;
        ///Initialize as alpha and prior prob
        double logPregularmsg = alpha + priorProbability(MessageType.NORMAL);;

        for (int i = 0; i < messages.length; ++i)
        {
            FileInputStream i_s = new FileInputStream( messages[1] ); ///always the same msg
            BufferedReader in = new BufferedReader(new InputStreamReader(i_s));
            String line;
            String word;

            while ((line = in.readLine()) != null)                      // read a line
            {
                StringTokenizer st = new StringTokenizer(line);         // parse it into words

                while (st.hasMoreTokens())                  // while there are still words left..
                {

                    final String next = st.nextToken();

                    ///probability is the sum of alpha + prior probability of regular message + sum of probability of word being regular
                    ///So we now add the probability of each word at every step
                    counter = vocab.get(next);
                    double pWord = counter.conditionalprob_regular;
                    logPregularmsg += pWord;
                }
            }
            in.close();
        }
        return logPregularmsg;
    }

    ///TODO give message as input
    ///Calculates the probability of a message being a regular email
    public static double probabilitySpam()
            throws IOException{
        File[] messages = new File[0];
        Multiple_Counter counter = new Multiple_Counter();

        ///TODO Calculate actual alpha value
        double alpha = 0.01;
        ///Initialize as alpha and prior prob
        double logPspammsg = alpha + priorProbability(MessageType.SPAM);;

        for (int i = 0; i < messages.length; ++i)
        {
            FileInputStream i_s = new FileInputStream( messages[1] ); ///always the same msg
            BufferedReader in = new BufferedReader(new InputStreamReader(i_s));
            String line;
            String word;

            while ((line = in.readLine()) != null)                      // read a line
            {
                StringTokenizer st = new StringTokenizer(line);         // parse it into words

                while (st.hasMoreTokens())                  // while there are still words left..
                {

                    final String next = st.nextToken();

                    ///probability is the sum of alpha + prior probability of regular message + sum of probability of word being regular
                    ///So we now add the probability of each word at every step
                    counter = vocab.get(next);
                    double pWord = counter.conditionalprob_spam;
                    logPspammsg += pWord;
                }
            }
            in.close();
        }
        return logPspammsg;
    }

    
    // Listings of the two subdirectories (regular/ and spam/)
    private static File[] listing_regular = new File[0];
    private static File[] listing_spam = new File[0];

    /// Listings of the two subdirectories (regular/ and spam/) , in the TEST directory
    private static File[] listing_regularTest = new File[0];
    private static File[] listing_spamTest = new File[0];

    
   ///calculates prior probability.
    public static double priorProbability(MessageType type){ 
     double i; 
     float total = listing_regular.length + listing_spam.length;
    if(type == MessageType.NORMAL) {
     i = -1 * Math.log((listing_regular.length) / total);  
     }else{
     
     i = -1 * Math.log((listing_spam.length) / total); 
     }
        
      return i; 
    }
    
    
    // A hash table for the vocabulary (word searching is very fast in a hash table)
    private static Hashtable <String, Multiple_Counter> vocab = new Hashtable <String, Multiple_Counter> ();

    
    // Add a word to the vocabulary and increments the total counter of 
    private static void addWord(String word, MessageType type)
    {
        Multiple_Counter counter = new Multiple_Counter();
        ///Check if word is too short, less than 4 chars.
        if(word.length() < 4){ //TODO Slips through single letter words and words that were reformatted.
            return;
        }
        word = formatter(word);

        if ( vocab.containsKey(word) ){                  // if word exists already in the vocabulary..
            counter = vocab.get(word);                  // get the counter from the hashtable
        }
        counter.incrementCounter(type);
        // increase the counter appropriately and calculate the conditional probability 
        
        
        
        vocab.put(word, counter);
        
        // put the word with its counter into the hashtable
    }


    // List the regular and spam messages
    ///Maakt van de 2 folders variabelen met alle berichten van elke catagorie
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

    ///Maakt van de 2 folders variabelen met alle berichten van elke catagorie
    private static void listDirs2(File dir_location)
    {
        // List all files in the directory passed
        File[] dir_listing = dir_location.listFiles();

        // Check that there are 2 subdirectories
        if ( dir_listing.length != 2 )
        {
            System.out.println( "- Error: specified directory does not contain two subdirectories.\n" );
            Runtime.getRuntime().exit(0);
        }

        listing_regularTest = dir_listing[0].listFiles();
        listing_spamTest    = dir_listing[1].listFiles();
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
                    " in spam: " + counter.counter_spam + " ||| conditional probability regular: " + counter.conditionalprob_regular + " conditional probability spam: " + counter.conditionalprob_spam);
        }
    }
    
    private static void calculateConditionalProbabilities(float nWordsRegular, float nWordsSpam)
    {
        Multiple_Counter counter = new Multiple_Counter();

        for (Enumeration<String> e = vocab.keys() ; e.hasMoreElements() ;)
        {   
            String word;
            
            word = e.nextElement();
            counter  = vocab.get(word);
            counter.calculateProbability(nWordsRegular, nWordsSpam);

        }
    }

    private static float countWords(int x, float nWords){
            Multiple_Counter counter = new Multiple_Counter();

            for (Enumeration<String> e = vocab.keys() ; e.hasMoreElements() ;)
            {   
                String word;
                
                word = e.nextElement();
                counter  = vocab.get(word);
                if (x == 0){
                nWords = nWords + counter.counter_regular;
                }else{
                nWords = nWords + counter.counter_spam;
                }
                
                
            }
    return nWords;    
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

                    addWord(next, type);// add them to the vocabulary

                }

            }

            in.close();
        }
            }



    public static void main(String[] args)

    throws IOException {

        double prior_spam = 0;
        double prior_regular = 0;

        // Location of the directory (the path) taken from the cmd line (first arg)
        File dir_location = new File(args[0]);

        ///Look into the second argument, the second path with folders
        File dir_locationTest = new File(args[1]);


        // Check if the cmd line arg is a directory
        if (!dir_location.isDirectory()) {
            System.out.println("- Error: cmd line arg not a directory.\n");
            Runtime.getRuntime().exit(0);
        }

        // Initialize the regular and spam lists
        listDirs(dir_location);
        ///Initialize the TEST directory
        listDirs2(dir_locationTest);

        // Read the e-mail messages
        readMessages(MessageType.NORMAL);
        readMessages(MessageType.SPAM);


        prior_spam = priorProbability(MessageType.SPAM);
        prior_regular = priorProbability(MessageType.NORMAL);
        float nMessagesRegular = listing_regular.length;
        float nMessagesSpam = listing_spam.length;
        float nWordsSpam = 0;
        float nWordsRegular = 0;
        nWordsRegular = countWords(0, nWordsRegular);
        nWordsSpam = countWords(1, nWordsSpam);
        calculateConditionalProbabilities(nWordsRegular, nWordsSpam);

        // Print out the hash table
        printVocab();


        System.out.println("total Regular: " + nMessagesRegular);
        System.out.println("total Spam : " + nMessagesSpam);
        System.out.println(" a priori spam : " + prior_spam);
        System.out.println(" a priori regular : " + prior_regular);
        System.out.println(" a nWordsSpam: " + nWordsSpam);
        System.out.println(" a nWordsRegular : " + nWordsRegular);

        System.out.println("probabilityRegular of message 1 : " + probabilityRegular());
        System.out.println("probabilitySpam of message 1 : " + probabilitySpam());
        if (probabilityRegular() > probabilitySpam()) {
            System.out.println("Message is not spam");
        } else
            System.out.println("Message is spam");


        // Now all students must continue from here:

        // 6) Bayes rule must be applied on new messages, followed by argmax classification
        // 7) Errors must be computed on the test set (FAR = false accept rate (misses), FRR = false reject rate (false alarms))
        // 8) Improve the code and the performance (speed, accuracy)
        //
        // Use the same steps to create a class BigramBayespam which implements a classifier using a vocabulary consisting of bigrams
    }
}
