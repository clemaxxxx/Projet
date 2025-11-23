package card;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import model.Pos;
import player.Player;

/**
 * Represents a special scoring card for a game variant.
 * <p>
 * This card calculates scores based on connected groups of fauna tokens, with specific scoring rules depending on the variant type.
 * </p>
 * 
 * @param choice The variant type:
 *               <ul>
 *               <li><code>1</code> for the "family" variant</li>
 *               <li><code>0</code> for the "intermediate" variant</li>
 *               </ul>
 * @param istilesquare <code>true</code> if the game uses square tiles; <code>false</code> if hexagonal tiles are used.
 */
public record CardVariante(int choice,boolean istilesquare) implements Card {
  /**
   * Calculates the points for a group of connected fauna tokens based on the card variant.
   * 
   * @param nb The size of the connected group.
   * @return The points scored for the group.
   */
  private int nbPointVariante(int nb) {
    if(choice == 1) {
      if(nb == 1)  {
        return 2;
      }else if(nb == 2)  {
        	return  5;
      }else if(nb >=3)  {
        return 9;  
      }
    }else {
      if(nb == 2)  {
        return 5;
      }else if(nb == 3)  {
        return 8;
      }else if(nb >=4)  {
        return 12;  
      }
    }
    return 0;
  }
  
  /**
   * Calculates and updates the player's score based on the groups of connected fauna tokens.
   * <p>
   * The score is calculated by determining the size of each group and applying the point rules for the current variant.
   * </p>
   * 
   * @param player The player whose score will be updated.
   * @throws NullPointerException if the <code>player</code> is <code>null</code>.
   */
  @Override
  public void counterScore(Player player) {
    Objects.requireNonNull(player);
    int total_p = 0;
    int nb;
    var visit = new HashMap<Pos, Boolean>();
    for(int i = 0; i < player.getEnv().getSize(); i++) {
      for(int j = 0; j< player.getEnv().getSize(); j++) {
        var t = player.getEnv().getEnv().get(new Pos(i,j));
        var list = new ArrayList<Pos>();
        if(t!=null && t.getfaunatoken() != null && !visit.containsKey(new Pos(i,j)) ) {
          nb = nbAnimalHC(list,player.getEnv(),i,j,visit,t.getfaunatoken().token(),istilesquare);
          total_p += nbPointVariante(nb);
        }
        
      }
    }
    player.add(total_p);
  }
  
  /**
   * Returns a string representation of the card.
   * @return A string representation of the card.
   */
  @Override
  public String toString() {
    if(choice == 1) {
      return "Carte,1,variante";
    }else {
      return "Carte,0,variante";
    }
  }
  

}
