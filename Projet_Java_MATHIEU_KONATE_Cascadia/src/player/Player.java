package player;



import java.io.IOException;
import java.util.Objects;

import board.Board;
/**
 * Represents a player in the game, including their name, score, and game board.
 */
public class Player {
  private final String nom;
  private int score;
  private Board environnement;
  /**
   * Constructs a player with the given name and board size.
   *
   * @param nom    the name of the player
   * @param taille the size of the player's game board
   * @throws IOException if an error occurs while initializing the board
   * @throws NullPointerException if taille is null
   */
  public Player(String nom,int taille) throws IOException {
    Objects.requireNonNull(nom);
    this.nom = nom;
    score = 0;
    environnement = new Board(taille);  
  }
  /**
   * Gets the player's current score.
   *
   * @return the player's score
   */
  public int getScore() {
    return score;
  }
  /**
   * Gets the player's game board.
   *
   * @return the player's game board
   */
  public Board getEnv() {
    return environnement;
  }
  /**
   * Gets the player's name.
   *
   * @return the player's name
   */
  public String getNom()  {
    return nom;
  }
  
  /**
   * Adds points to the player's score.
   *
   * @param nb the number of points to add
   */
  public void add(int nb) {
	    score += nb;
  }
  /**
   * Sets the player's game board to a new environment.
   *
   * @param env the new game board
   */
  public void SetEnv(Board env) {
    Objects.requireNonNull(env);
    this.environnement = env;
  }
  
  /**
   * Returns a string representation of the player, including their name,
   * game board size, and the current state of the board.
   *
   * @return a string representation of the player
   */
  @Override
  public String toString() {
    var sb = new StringBuilder();
    sb.append("nom: ").append(nom).append("\n");
    sb.append("taille: ").append(environnement.getSize()).append("\n");
    sb.append("Plateau:\n");
    for(var ele : environnement.getEnv().entrySet()) {
      var value = ele.getValue() !=null ? ele.getValue().displaySave() : "null";
      sb.append("").append(ele.getKey()).append(",").append(value).append("\n");
      
    }
    return sb.toString();
  }

}
