package player;


import java.util.Objects;

import board.FaunaToken;
import model.Animal;
import model.Pos;
/**
 * The `Succes` class evaluates achievements for a player based on their score and game state.
 */
public class Succes {
  
  private final Player player;
  private int nb_succes;
  /**
   * Constructs a `Succes` object for a specific player.
   *
   * @param j the player whose achievements will be evaluated
   */
  public Succes(Player j) {
    Objects.requireNonNull(j);
    player = j;
    nb_succes = 0;
  }
  /**
   * Checks if the player's score meets predefined milestones and increments
   * the achievement count if any milestones are surpassed.
   */
  private void SuccesScore() {
    int[] scores = {80, 85, 90, 95, 100, 105, 110}; 
    for(var ele : scores) {
      if(player.getScore() > ele) {
        System.out.println("succes : Marquer " + ele + " pts ou + realise par :" + player.getNom());
        nb_succes++;
      }
    }
  }
  /**
   * Searches the player's environment to determine if a specific fauna token
   * is present.
   *
   * @param animal the type of animal to search for
   * @return `true` if the specified fauna token is found, otherwise `false`
   */
  private boolean SearchToken(Animal animal) {
    var jetonfaune = new FaunaToken(animal);
    var env = player.getEnv();
    for(int i = 0; i < env.getSize(); i++) {
      for(int j = 0; j < env.getSize(); j++) {
        var tuile = env.getEnv().get(new Pos(i, j));
        if(tuile != null && jetonfaune.equals(tuile.getfaunatoken())) {
          return true;
        }
      }
    }
    return false;
  }
  
  /**
   * Checks if the player does not possess specific fauna tokens and increments
   * the achievement count for each missing token.
   */
  private void SuccesFaunaToken() {
    Animal[] animaux = {Animal.OURS,Animal.AIGLE,Animal.RENARD,Animal.SAUMON,Animal.WAPITI};
    for(var ele : animaux) {
      if(!SearchToken(ele)) {
        System.out.println("succes : Ne pas avoir  " + ele + "realise par :" + player.getNom());
        nb_succes++;
      }
    }
  }
  /**
   * Evaluates and displays all achievements for the player, including both
   * fauna token and score achievements. Also displays the total number of achievements.
   */
  public void Succesplayer() {
    SuccesFaunaToken();
    SuccesScore();
    System.out.println("le nombre de succes realise est de  :" + nb_succes + "par le player :"+ player.getNom());
  }

}
