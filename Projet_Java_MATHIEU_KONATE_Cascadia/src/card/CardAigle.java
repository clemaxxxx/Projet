package card;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import board.Board;
import model.Animal;
import model.Pos;
import player.Player;

/**
 * Represents the "Eagle" card in the game. 
 * This card calculates the player's score based on specific rules related to eagles on the board.
 * 
 * @param choice Determines the scoring rules variant for the card (1, 2, 3, or 4).
 * @param istilesquare Indicates if the board uses square tiles (true) or hexagonal tiles (false).
 */
public record CardAigle(int choice, boolean istilesquare) implements Card {

  /**
   * Checks if there are at least two eagle tokens in the same row as any position in the given list.
   * 
   * @param env The game board.
   * @param list The list of positions to check.
   * @return True if there is a line of sight with at least two eagle tokens, false otherwise.
   */
  private boolean checkLineOfSight(Board env, ArrayList<Pos> list) {
    for (var ele : list) {
      var i = ele.x();
      int tmp = 0;
      for (int j = 0; j < env.getSize(); j++) {
        var tile = env.getEnv().get(new Pos(i, j));
        if (tile != null && tile.getfaunatoken() != null && tile.getfaunatoken().token().equals(Animal.AIGLE)) {
          tmp++;
        }
      }
      if (tmp >= 2) {
        return true;
      }
    }
    return false;
  }

  /**
   * Provides the scoring rules for each card variant.
   * 
   * @return A map containing the number of eagles and their corresponding points, based on the card variant.
   */
  private Map<Integer, Integer> point() {
    return switch (choice) {
      case 1 -> Map.of(0,0,1, 2, 2, 5, 3, 8, 4, 11, 5, 14, 6, 18, 7, 22, 8, 26);
      case 2 -> Map.of(0,0,1, 0, 2, 5, 3, 9, 4, 12, 5, 16, 6, 20, 7, 24, 8, 28);
      case 3 -> Map.of(0,0,1, 2, 2, 5, 3, 8, 4, 11, 5, 14, 6, 18, 7, 22, 8, 26);
      case 4 -> Map.of(0,0,1, 0, 2, 5, 3, 9, 4, 12, 5, 16, 6, 20, 7, 24, 8, 28);
      default -> null;
    };
  }

  /**
   * Adds points to the player's score based on the number of eagle tokens and the card variant rules.
   * 
   * @param nb_aigle A map containing the number of connected eagles.
   * @param player The player whose score will be updated.
   */
  private void addPoint(HashMap<Integer, Integer> nb_aigle, Player player) {
    if (choice >= 1 && choice <= 4) {
      Carte12(nb_aigle, player);
    }
  }

  /**
   * Updates the player's score based on specific rules for card variants 1 and 2.
   * 
   * @param nb_aigle A map containing the number of connected eagles.
   * @param player The player whose score will be updated.
   */
  private void Carte12(HashMap<Integer, Integer> nb_aigle, Player player) {
    var point = point();
    for (var ele : nb_aigle.entrySet()) {
      if (ele.getKey() == 1) {
        var value = ele.getValue() / 2;
        if (value > 8) {
          player.add(point.get(8));
        } else {
          player.add(point.get(value));
        }
      }
    }
  }

  /**
   * Calculates and updates the player's score based on the eagle tokens on the board.
   * 
   * <p>
   * The method searches the board for connected groups of eagle tokens and calculates the score 
   * based on the card's rules and the current board configuration.
   * </p>
   * 
   * @param player The player whose score will be updated.
   * @throws NullPointerException if the player is null.
   */
  @Override
  public void counterScore(Player player) {
    Objects.requireNonNull(player);
    var nb_aigle = new HashMap<Integer, Integer>();
    var visit = new HashMap<Pos, Boolean>();
    for (int i = 0; i < player.getEnv().getSize(); i++) {
      for (int j = 0; j < player.getEnv().getSize(); j++) {
        var list = new ArrayList<Pos>();
        var tuile = player.getEnv().getEnv().get(new Pos(i, j));
        if (tuile != null && tuile.getfaunatoken() != null && tuile.getfaunatoken().token().equals(Animal.AIGLE) && !visit.containsKey(new Pos(i, j))) {
          nbAnimalHC(list, player.getEnv(), i, j, visit, tuile.getfaunatoken().token(), istilesquare);
          if (list.size() == 1 && choice != 2) {
            nb_aigle.put(list.size(), nb_aigle.getOrDefault(list.size(), 0) + 1);
          } else if (list.size() == 1 && checkLineOfSight(player.getEnv(), list)) {
            nb_aigle.put(list.size(), nb_aigle.getOrDefault(list.size(), 0) + 1);
          }
        }
      }
    }
    addPoint(nb_aigle, player);
  }

  /**
   * Returns a string representation of the card.
   * @return A string representation of the card.
   */
  @Override
  public String toString() {
    return switch (choice) {
      case 1 -> "Carte,1,aigle";
      case 2 -> "Carte,2,aigle";
      case 3 -> "Carte,3,aigle";
      case 4 -> "Carte,4,aigle";
      default -> null;
    };
  }
  
  
}