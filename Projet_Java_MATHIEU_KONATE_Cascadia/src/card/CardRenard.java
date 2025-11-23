package card;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import board.Board;
import board.Tile;
import model.Animal;
import model.Pos;
import player.Player;

/**
 * Represents the "Fox" card in the game.
 * This card calculates the player's score based on adjacent animals or specific configurations of fox tokens.
 * 
 * @param choice Determines the scoring rules variant for the card (1, 2, 3, or 4).
 * @param istilesquare Indicates if the board uses square tiles (true) or hexagonal tiles (false).
 */
public record CardRenard(int choice,boolean istilesquare) implements Card {
  

  
  /**
   * Adds points to the player's score for card variant 1 based on adjacent animals.
   * 
   * @param list A map of adjacent animals and their counts.
   * @param nb_renard A map of counts for scoring.
   */
  private void card1(HashMap<Animal,Integer> list,HashMap<Integer,Integer> nb_renard) {
    for(int i=0;i<list.size();i++) {
      nb_renard.put(1, nb_renard.getOrDefault(1, 0) + 1);
    }
  }  
  /**
   * Adds points to the player's score for card variant 2 based on adjacent animals with a count of 2.
   * 
   * @param list A map of adjacent animals and their counts.
   * @param nb_renard A map of counts for scoring.
   */
  private void card2(HashMap<Animal,Integer> list,HashMap<Integer,Integer> nb_renard) {
    for(var ele: list.entrySet()) {
      var value = ele.getValue();
      if(value==2) {
        nb_renard.put(2, nb_renard.getOrDefault(1, 0)+1);
      }
    }
  }
  
  /**
   * Finds the highest count of adjacent animals for card variant 3.
   * 
   * @param list A map of adjacent animals and their counts.
   * @param nb_renard A map of counts for scoring.
   */
  private void card3(HashMap<Animal,Integer> list,HashMap<Integer,Integer> nb_renard) {
    int nb=0;
    for(var ele: list.entrySet()) {
      var value = ele.getValue();
      if(value > nb) {
        nb = value;
      }
    }
    nb_renard.put(3, nb);
  }
  
  /**
   * Adds points to the player's score for card variant 4 based on adjacent animals with a count of 2.
   * 
   * @param list A map of adjacent animals and their counts.
   * @param nb_renard A map of counts for scoring.
   */
  private void card4(HashMap<Animal,Integer> list,HashMap<Integer,Integer> nb_renard) {
    for(var ele: list.entrySet()) {
      var value = ele.getValue();
      if(value ==2) {
        nb_renard.put(4, nb_renard.getOrDefault(1, 0)+1);
      }
    }
  }
  

  private void addPointCard1(Player player,HashMap<Integer,Integer> nb_renard,HashMap<Animal,Integer> list) {
    card1(list,nb_renard);
    var point = point();
    for(var ele:nb_renard.entrySet()) {
      var value = ele.getValue();
      if(ele.getKey()==1&& value >0 && value <6) {
        player.add(point.get(value));
      }
    }
  }
  
  private void addPointCard2(Player player,HashMap<Integer,Integer> nb_renard,HashMap<Animal,Integer> list) {
    card2(list,nb_renard);
    var point = point();
    for(var ele:nb_renard.entrySet()) {
      var value = ele.getValue();
      if(ele.getKey()==2&& value >0 && value <4) {
        player.add(point.get(value));
      }
    }
  }
  
  private void addPointCard3(Player player,HashMap<Integer,Integer> nb_renard,HashMap<Animal,Integer> list) {
    card3(list,nb_renard);
    var point = point();
    for(var ele:nb_renard.entrySet()) {
      var value = ele.getValue();
      if(ele.getKey()==3&& value >0 && value <7) {
        player.add(point.get(value));
      }
    }
  }
  
  private void addPointCard4(Player player,HashMap<Integer,Integer> nb_renard,HashMap<Animal,Integer> list) {
    card4(list,nb_renard);
    var point = point();
    for(var ele:nb_renard.entrySet()) {
      var value = ele.getValue();
      if(ele.getKey()==4 && value >0 && value <5) {
        player.add(point.get(value));
      }
    }
  }
  
  /**
   * Adds points to the player's score based on the card variant.
   * 
   * @param player The player whose score is being updated.
   * @param nb_renard A map of counts for scoring.
   * @param list A map of adjacent animals and their counts.
   */
  private void addPoint(Player player,HashMap<Integer,Integer> nb_renard,HashMap<Animal,Integer> list) {
    switch(choice) {
    case 1 -> addPointCard1(player,nb_renard,list);
    case 2 -> addPointCard2(player,nb_renard,list);
    case 3 -> addPointCard3(player,nb_renard,list);
    case 4 -> addPointCard4(player,nb_renard,list);
    }
  }
  
  /**
   * Returns the scoring rules for the current card variant.
   * 
   * @return A map of scores based on the number of adjacent animals.
   */
  private Map<Integer,Integer> point() {
    return switch(choice) {
    case 1 -> Map.of(0,0,1,1,2,2,3,3,4,4,5,5);
    case 2 -> Map.of(0,0,1,3,2,5,3,7);
    case 3 -> Map.of(0,0,1,1,2,2,3,3,4,4,5,5,6,6);
    case 4 -> Map.of(0,0,1,5,2,7,3,9,4,1);
    default -> null;
    };
  }
  
  
  /**
   * Recursively counts animals adjacent to a specific position on the board.
   * 
   * @param list A map of animals and their counts adjacent to the starting position.
   * @param env The game board.
   * @param i The x-coordinate of the starting position.
   * @param j The y-coordinate of the starting position.
   * @param v A map to track visited positions.
   * @param type The type of animal being considered (e.g., Fox).
   * @param stop A counter for the depth of the recursion.
   */
  private void animalsAdjacent(HashMap<Animal,Integer> list,Board env,int i,int j,HashMap<	Pos, Boolean> v,Animal type,int stop) {
    if(i < 0 || j < 0 || i >= env.getSize() || j >= env.getSize() || stop==2)  {
      return;
    }
    if(choice!=4) {
      if((v.containsKey(new Pos(i,j))  || env.getEnv().get(new Pos(i,j)) == null)) {
        return; 
      }
    }else {
      if((v.containsKey(new Pos(i,j)) && stop!=0) || env.getEnv().get(new Pos(i,j)) == null){
        return;
      }
    }
    var tile = env.getEnv().get(new Pos(i, j));
    if(tile.getfaunatoken() == null || (tile.getfaunatoken().token().equals(type) && stop != 0 && choice!=1)) {
        return;
    }
    updateAnimalList(tile,list,type,stop);
    v.put(new Pos(i,j),true);
    int[][] position = istilesquare ? new int[][]{{-1, 0}, {1, 0}, {0, -1}, {0, 1}} : new int[][]{{0, 2}, {0, -2}, {-1, 1}, {-1, -1}, {1, 1}, {1, -1}}; 
    for(var ele: position) {
      animalsAdjacent(list,env,ele[0]+i,j+ele[1],v,type,stop+1);
    }
  }
  
  
  /**
   * Updates the count of a specific type of animal in the provided list if conditions are met.
   *
   * @param tile The tile being evaluated, containing the fauna token.
   * @param list A map of animals and their current counts to be updated.
   * @param type The type of animal being considered.
   * @param stop The depth of recursion; only updates the list if {@code stop != 0}.
   */
  private void updateAnimalList(Tile tile, HashMap<Animal, Integer> list, Animal type, int stop) {
    if(stop!=0) {
      list.put(tile.getfaunatoken().token(),list.getOrDefault(tile.getfaunatoken().token(), 0)+1);
    }
  }
  
  
  /**
   * Calculates and updates the player's score based on the fox tokens on the board.
   * 
   * @param player The player whose score is being updated.
   * @throws NullPointerException if the player is null.
   */
  @Override
  public void counterScore(Player player) {
    Objects.requireNonNull(player);
    var renard = Animal.RENARD;
    var visit = new HashMap<Pos, Boolean>();
    for(int i = 0; i < player.getEnv().getSize(); i++) {
      for(int j = 0; j < player.getEnv().getSize(); j++) {
        var list = new HashMap<Animal,Integer>();
        var nb_renard = new HashMap<Integer,Integer>();
        var pos = new ArrayList<Pos>();
        var tile = player.getEnv().getEnv().get(new Pos(i, j));
        if(tile != null && tile.getfaunatoken() != null && tile.getfaunatoken().token().equals(renard) && !visit.containsKey(new Pos(i,j)) && choice != 4) {  
          animalsAdjacent(list,player.getEnv(),i,j,visit,tile.getfaunatoken().token(),0);
        }else if(tile != null && tile.getfaunatoken() != null && tile.getfaunatoken().token().equals(renard) && !visit.containsKey(new Pos(i,j)) && choice == 4) {
          nbAnimalHC(pos,player.getEnv(),i,j,visit,renard,istilesquare); 
          if(pos.size()==2) {
            animalsAdjacent(list,player.getEnv(),pos.get(0).x(),pos.get(0).y(),visit,tile.getfaunatoken().token(),0);
            animalsAdjacent(list,player.getEnv(),pos.get(1).x(),pos.get(1).y(),visit,tile.getfaunatoken().token(),0);
          }
        }
        addPoint(player,nb_renard,list);
      }
    }
  }

  /**
   * Returns a string representation of the fox card.
   * 
   * @return A string describing the card variant and its association with the fox animal,
   *         or <code>null</code> if the choice is invalid.
   */
  @Override
  public String toString() {
    return switch (choice) {
      case 1 -> "Carte,1,renard";
      case 2 -> "Carte,2,renard";
      case 3 -> "Carte,3,renard";
      case 4 -> "Carte,4,renard";
      default -> null; 
    };
  }
}
