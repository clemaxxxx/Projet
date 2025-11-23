package game;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Objects;

/**
 * The Interaction class centralizes all user interactions in the system.
 */
public class Interaction {

  private BufferedReader reader;

  /**
   * Constructs an Interaction object and initializes the reader 
   * to read input from the console (System.in).
   */
  public Interaction(){
    reader = new BufferedReader(new InputStreamReader(System.in));
  }

    /**
     * Prompts the user with a question and validates the input.
     * 
     * @param question The question to ask the user.
     * @param validanswers Array of valid answers.
     * @return The user's valid choice as a String.
     * @throws IOException if an I/O error occurs.
     */
  public String askQuestion(String question, String[] validanswers) throws IOException {
    Objects.requireNonNull(question);
    Objects.requireNonNull(validanswers);
    while(true){
      System.out.println(question);
      var choice = reader.readLine().trim();
      for(String validAnswer : validanswers) {
        if(choice.equalsIgnoreCase(validAnswer)) {
          return choice;
         }
       }
       System.out.println("Choix Incorrecte.");
    }
  }

    /**
     * Asks the user to input coordinates in the format "x,y".
     * 
     * @return An array of two integers representing the coordinates.
     * @throws IOException if an I/O error occurs.
     */
    /**
     * Prompts the user to enter coordinates in the format "i,j".
     *
     * @return An array of two integers representing the coordinates.
     * @throws IOException if an I/O error occurs during input.
     */
  public int[] getCoordinates() throws IOException {
  	  var coordonnees = new int[2]; 
  	  while(true){
    	  try{
    	    System.out.println("Veuillez entrer les coordonnees(i,j) :");
    	    var choice = reader.readLine();
    	    var array = choice.split(",");
    	    if(array.length != 2) {
    	      throw new IllegalArgumentException("Erreur veuillez entrer deux entiers separe par une virgule");
    	    }
    	    coordonnees[0] = Integer.parseInt(array[0]);
    	    coordonnees[1] = Integer.parseInt(array[1]);
    	    return coordonnees; 
    	  }catch(NumberFormatException e) {
    	    System.out.println("Erreur : veuillez entrer uniquement des entiers pour i et j.");
    	  }catch(IllegalArgumentException e) {
    	    System.out.println("Erreur veuillez entrer deux entier i et j");
    	  }
    }
  }
    
    
    
    
    /**
     * Requests a player's name from the console.
     *
     * @return the name of the player
     * @throws IOException if an input error occurs
     */
  public String requestName() throws IOException{
    System.out.println("Nom du joueur :");
    	var choix = reader.readLine();
    	return choix;
  }
    
    /**
     * Closes the reader to free resources.
     */
  public void close() {
    try {
      reader.close();
    }catch (IOException e) {
      System.err.println("Error closing the reader: " + e.getMessage());
    }
  }
}