package card;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import model.Animal;
import model.Pos;
import player.Player;


/**
 * Represents the "Bear" card in the game. 
 * This card calculates the player's score based on specific rules related to adjacent bear tokens on the board.
 * 
 * @param choice Determines the scoring rules variant for the card (1, 2, 3, or 4).
 * @param istilesquare Indicates if the board uses square tiles (true) or hexagonal tiles (false).
 */
public record CardOurs(int choice,boolean istilesquare) implements Card{
  /**
   * Checks if the number of adjacent bear tokens matches the scoring criteria based on the card variant.
   * 
   * @param list The list of positions containing adjacent bear tokens.
   * @return True if the number of adjacent bear tokens satisfies the rules for the current card variant, false otherwise.
   */
  private boolean checkNbAdjacentOurs(ArrayList<Pos> list) {
    switch(choice) {
      case 1 -> {return list.size() == 2;}
      case 2 -> {return list.size() == 3;}
      case 3 -> {return list.size() <= 3;}
      case 4 -> {return list.size() > 1 && list.size() <= 4;}
      default -> {return true;}  
    }
  }
  
  /**
   * Distributes points to the player based on the number of adjacent bear tokens and the card variant.
   * 
   * @param nb_ours A map containing the number of connected bears.
   * @param player The player whose score will be updated.
   */
  private void addPoint(HashMap<Integer,Integer> nb_ours,Player player) {
    if(choice == 1) {
      card1(nb_ours,player);
    }else if(choice == 2) {
      card2(nb_ours,player);
    }else if(choice == 3) {
      card3(nb_ours,player);
    }else if(choice == 4) {
      card4(nb_ours,player);
    }
    
  }
  
  /**
   * Scoring rules for card variant 1.
   * 
   * <p>
   * Awards points based on specific thresholds of adjacent bear groups.
   * </p>
   * 
   * @param nb_ours A map containing the number of connected bears.
   * @param player The player whose score will be updated.
   */
  private void card1(HashMap<Integer, Integer> nb_ours, Player player) {
    nb_ours.forEach((key, value) -> {
      if(key==2) {
        int scoreToAdd = switch (value) {
          case 1 -> 4;
          case 2 -> 11;
          case 3 -> 19;
          default -> 0; 
        };
        player.add(scoreToAdd); 
        if(value >= 4) {
          player.add(27);
        }
      }
    });
  }
  
  /**
   * Scoring rules for card variant 2.
   * 
   * <p>
   * Awards 10 points for every group of three adjacent bears.
   * </p>
   * 
   * @param nb_ours A map containing the number of connected bears.
   * @param player The player whose score will be updated.
   */
  private void card2(HashMap<Integer,Integer> nb_ours,Player player) {
    for(var ele : nb_ours.entrySet()) {
      if(ele.getKey()==3) {
        for(int i = 0 ; i<ele.getValue();i++) {
          player.add(10);
        }
      }
    }
  }
  
  /**
   * Scoring rules for card variant 3.
   * 
   * <p>
   * Awards points based on group sizes of 1, 2, or 3 bears and adds a bonus for having all three group sizes.
   * </p>
   * 
   * @param nb_ours A map containing the number of connected bears.
   * @param player The player whose score will be updated.
   */
  private void card3(HashMap<Integer,Integer> nb_ours,Player player) {
    int bonus = 0;
    var point = Map.of(0,0,1,2,2,5,3,8);
    for(var ele : nb_ours.entrySet()) {
      var key = ele.getKey();
      var value = ele.getValue();
      if(point.containsKey(key) && value >0 && value < 4) {
        bonus++;
        player.add(value*point.get(key));
      }
    }
    if(bonus==3) {
      player.add(3);
    }
  }
  
  /**
   * Scoring rules for card variant 4.
   * 
   * <p>
   * Awards points based on group sizes of 2, 3, or 4 bears.
   * </p>
   * 
   * @param nb_ours A map containing the number of connected bears.
   * @param player The player whose score will be updated.
   */
  private void card4(HashMap<Integer,Integer> nb_ours,Player player) {
    var point = Map.of(0,0,1,0,2,5,3,8,4,13);
    for(var ele : nb_ours.entrySet()) {
      var key = ele.getKey();
      var value = ele.getValue();
      if(point.containsKey(key) && value >0 && value < 5) {
        player.add(value*point.get(key));
      }
    }
  }
  
  /**
   * Calculates and updates the player's score based on the bear tokens on the board.
   * 
   * <p>
   * The method searches the board for connected groups of bear tokens and calculates the score 
   * based on the card's rules and the current board configuration.
   * </p>
   * 
   * @param player The player whose score will be updated.
   * @throws NullPointerException if the player is null.
   */
  @Override
  public void counterScore(Player player) {
    Objects.requireNonNull(player);
    var nb_ours = new HashMap<Integer,Integer>();
    var visit = new HashMap<Pos, Boolean>();
    for(int i = 0; i < player.getEnv().getSize(); i++) {
      for(int j = 0; j < player.getEnv().getSize(); j++) {
        var list = new ArrayList<Pos>();
        var tile = player.getEnv().getEnv().get(new Pos(i, j));
        if(tile != null && tile.getfaunatoken() != null && tile.getfaunatoken().token().equals(Animal.OURS) && !visit.containsKey(new Pos(i,j))) {  
          nbAnimalHC(list,player.getEnv(),i,j,visit,tile.getfaunatoken().token(),istilesquare);
          if(checkNbAdjacentOurs(list)) {
            nb_ours.put(list.size(), nb_ours.getOrDefault(list.size(), 0)+1);
          }
        }
      }
    }
    addPoint(nb_ours,player);
  }

  /**
   * Returns a string representation of the card.
   * 
   * @return A string representation of the card.
   */
  @Override
  public String toString() {
    return switch (choice) {
      case 1 -> "Carte,1,ours";
      case 2 -> "Carte,2,ours";
      case 3 -> "Carte,3,ours";
      case 4 -> "Carte,4,ours";
      default -> null; 
    };
}


}
