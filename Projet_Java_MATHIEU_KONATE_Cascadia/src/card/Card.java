package card;

import java.util.ArrayList;
import java.util.HashMap;

import board.Board;
import model.Animal;
import model.Pos;
import player.Player;

/**
 * Represents a card in the game. Cards have specific rules for scoring 
 * based on the board and the player's actions.
 * 
 * <p>
 * This interface is sealed and can only be implemented by:
 * <ul>
 * <li>{@link CardVariante}</li>
 * <li>{@link CardOurs}</li>
 * <li>{@link CardWapiti}</li>
 * <li>{@link CardRenard}</li>
 * <li>{@link CardSaumon}</li>
 * <li>{@link CardAigle}</li>
 * </ul>
 */
public sealed interface Card permits CardVariante, CardOurs, CardWapiti, CardRenard, CardSaumon, CardAigle {

  /**
   * Calculates and updates the score of the specified player according 
   * to the rules of the card.
   * 
   * @param player the player whose score will be updated
   */
  void counterScore(Player player);

  /**
   * Counts the number of connected animals of a specific type on the board.
   * 
   * <p>
   * This method recursively checks neighboring tiles to find connected animals 
   * of the same type. It works for both square and hexagonal board types.
   * </p>
   * 
   * @param list a list to store the positions of connected animals
   * @param env the game board to search
   * @param i the x-coordinate of the current tile
   * @param j the y-coordinate of the current tile
   * @param v a map to track visited positions
   * @param type the type of animal to count
   * @param istilesquare true if the board is square, false if hexagonal
   * @return the number of connected animals of the specified type
   */
  default int nbAnimalHC(ArrayList<Pos> list, Board env, int i, int j, HashMap<Pos, Boolean> v, Animal type, boolean istilesquare) {
    var nb = 0;
    if (i < 0 || j < 0 || i >= env.getSize() || j >= env.getSize()) {
      return 0; // Out of bounds
    }
    if (v.containsKey(new Pos(i, j)) || env.getEnv().get(new Pos(i, j)) == null) {
      return 0; // Already visited or no tile at the position
    }
    var tile = env.getEnv().get(new Pos(i, j));
    if (tile.getfaunatoken() == null || !tile.getfaunatoken().token().equals(type)) {
      return 0; // No fauna token or token does not match the type
    }
    v.put(new Pos(i, j), true); // Mark the position as visited
    list.add(new Pos(i, j));    // Add the position to the list
    int[][] position = istilesquare 
        ? new int[][]{{-1, 0}, {1, 0}, {0, -1}, {0, 1}} // Square board neighbors
        : new int[][]{{0, 2}, {0, -2}, {-1, 1}, {-1, -1}, {1, 1}, {1, -1}}; // Hexagonal board neighbors
    nb++;
    for (var dir : position) {
      nb += nbAnimalHC(list, env, i + dir[0], j + dir[1], v, type, istilesquare);
    }
    return nb;
  }
  
  
}