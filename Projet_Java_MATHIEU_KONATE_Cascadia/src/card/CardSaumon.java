package card;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import model.Animal;
import model.Pos;
import player.Player;

/**
 * Represents a scoring card for the animal "Saumon" (Salmon).
 * <p>
 * Each card has a scoring variant determined by the <code>choice</code> parameter,
 * and can work with either square or hexagonal tiles.
 * </p>
 * 
 * @param choice The variant of the card (1 to 4), which determines the scoring rules.
 * @param istilesquare <code>true</code> if the game uses square tiles; <code>false</code> if hexagonal tiles are used.
 */
public record CardSaumon(int choice,boolean istilesquare) implements Card{
  
  /**
   * Adds points to the player's score based on the connected groups of salmon.
   * 
   * @param nb_saumon A map where the keys represent the size of connected salmon groups,
   *                  and the values represent the number of groups of that size.
   * @param player The player whose score will be updated.
   */
  private void addPoint(HashMap<Integer,Integer> nb_saumon,Player player) {
    if(choice == 1) {
      card123(nb_saumon,player,7);
    }else if(choice == 2) {
      card123(nb_saumon,player,5);
    }else if(choice == 3) {
      card123(nb_saumon,player,5);
    }else if(choice == 4) {
      card123(nb_saumon,player,5);
    }
    
  }
  
  /**
   * Returns a map of point values for each card variant.
   * 
   * @return A map where the keys are group sizes and the values are points scored for groups of that size.
   */
  private Map<Integer,Integer> point() {
    return switch(choice) {
    case 1 -> Map.of(0,0,1,2,2,5,3,8,4,12,5,16,6,20,7,25);
    case 2 -> Map.of(0,0,1,2,2,4,3,9,4,11,5,17);
    case 3 -> Map.of(0,0,1,0,2,0,3,10,4,12,5,15);
    case 4 -> Map.of(0,0,1,2,2,5,3,8,4,12,5,16,6,20,7,25);
    default -> null;
    };
  }
  
  /**
   * Assigns points to the player based on the salmon groups for card variants 1, 2, and 3.
   * 
   * @param nb_saumon A map where the keys represent the size of salmon groups, and the values represent their counts.
   * @param player The player whose score will be updated.
   * @param valmax The maximum group size for scoring.
   */
  private void card123(HashMap<Integer, Integer> nb_saumon, Player player, int valmax) {
    var point = point();
    for(var ele: nb_saumon.entrySet()) {
      for(int i =0 ; i<ele.getValue();i++) {
        if(ele.getKey() > valmax) {
          player.add(point.get(valmax));
        }
        player.add(point.get(ele.getKey()));
      }
    }
  }
  
  /**
   * Calculates and updates the player's score based on the connected groups of salmon tokens.
   * 
   * @param player The player whose score will be updated.
   */
  @Override
  public void counterScore(Player player) {
    Objects.requireNonNull(player);
    var nb_saumon = new HashMap<Integer,Integer>();
    var visit = new HashMap<Pos, Boolean>();
    for(int i = 0; i < player.getEnv().getSize(); i++) {
      for(int j = 0; j < player.getEnv().getSize(); j++) {
        var list = new ArrayList<Pos>();
        var tile = player.getEnv().getEnv().get(new Pos(i, j));
        if(tile != null && tile.getfaunatoken() != null && tile.getfaunatoken().token().equals(Animal.SAUMON) && !visit.containsKey(new Pos(i,j))) {  
          nbAnimalHC(list,player.getEnv(),i,j,visit,tile.getfaunatoken().token(),istilesquare);
          nb_saumon.put(list.size(), nb_saumon.getOrDefault(list.size(), 0)+1);
        }
      }
    }
    addPoint(nb_saumon,player);
  }


  /**
   * Returns a string representation of the card.
   * @return A string representation of the card.
   */
  @Override
  public String toString() {
    return switch (choice) {
      case 1 -> "Carte,1,saumon";
      case 2 -> "Carte,2,saumon";
      case 3 -> "Carte,3,saumon";
      case 4 -> "Carte,4,saumon";
      default -> null; 
    };
  }

}