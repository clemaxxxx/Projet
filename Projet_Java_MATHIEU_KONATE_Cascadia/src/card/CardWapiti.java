package card;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;

import board.Board;
import model.Animal;
import model.Pos;
import player.Player;

/**
 * Represents a scoring card for the "Wapiti" animal in the game.
 * <p>
 * This card calculates points based on specific patterns of connected wapiti tokens.
 * The scoring rules depend on the card's choice type.
 * </p>
 * 
 * @param choice       Determines the scoring logic for the card:
 *                     <ul>
 *                     <li><code>1</code>: Line scoring</li>
 *                     <li><code>2</code>: Shape scoring (triangle, diamond, etc.)</li>
 *                     <li><code>3</code>: General size-based scoring</li>
 *                     <li><code>4</code>: Circle scoring</li>
 *                     </ul>
 * @param istilesquare <code>true</code> if the game uses square tiles; <code>false</code> if hexagonal tiles are used.
 */
public record CardWapiti(int choice,boolean istilesquare) implements Card {
  
  /**
   * Scoring logic for choice 1: checks line patterns.
   * 
   * @param env        The game board.
   * @param list       The list of positions forming a group.
   * @param nb_wapiti  The hashmap storing group sizes and their counts.
   * @param wapiti     The animal type (Wapiti).
   */
  private void Card1(Board env, ArrayList<Pos> list,HashMap<Integer,Integer> nb_wapiti, Animal wapiti) {
    if(list.size()==1) {
      nb_wapiti.put(list.size(),nb_wapiti.getOrDefault(list.size(), 0)+1);
    }else if(list.size()>=2) {
      if(checkLine(env,list,wapiti)) {
        nb_wapiti.put(list.size(),nb_wapiti.getOrDefault(list.size(), 0)+1);
      }
    }
  }
  
  /**
   * Checks if a group forms a straight line pattern.
   * 
   * @param env    The game board.
   * @param list   The list of positions forming a group.
   * @param wapiti The animal type (Wapiti).
   * @return <code>true</code> if the group forms a straight line; <code>false</code> otherwise.
   */
  private boolean checkLine(Board env,ArrayList<Pos> list,Animal wapiti) {
    var pos = list.getFirst();
    Pos[] positions = {pos,new Pos(pos.x(),pos.y()+2),new Pos(pos.x(),pos.y()+4),new Pos(pos.x(),pos.y()+6)};
    var position = new HashSet<Pos>();
    for(var ele : positions) {
      position.add(ele);
    }
    for(var p : list) {
      if(!position.contains(p)) {
          return false; 
      }
    }
    return true;
  }
    
  /**
   * Scoring logic for choice 2: checks shapes like triangles and diamonds.
   * 
   * @param env        The game board.
   * @param list       The list of positions forming a group.
   * @param nb_wapiti  The hashmap storing group sizes and their counts.
   * @param wapiti     The animal type (Wapiti).
   */
  private void Card2(Board env, ArrayList<Pos> list,HashMap<Integer,Integer> nb_wapiti, Animal wapiti) {
    var pos = list.getFirst();
    if(list.size()==1)  {
      nb_wapiti.put(list.size(),nb_wapiti.getOrDefault(list.size(), 0)+1);
    }else if(list.size()==2)  {
      if(checkLine(env,list,wapiti)) {
        nb_wapiti.put(list.size(),nb_wapiti.getOrDefault(list.size(), 0)+1);
      }
    }else if(list.size()==3)  {
      if(checkTriangle(env,pos,wapiti)) {
        nb_wapiti.put(list.size(),nb_wapiti.getOrDefault(list.size(), 0)+1);
      }
    }else if(list.size()==4)  {
      if(checkLosange(env,pos,wapiti)) {
        nb_wapiti.put(list.size(),nb_wapiti.getOrDefault(list.size(), 0)+1);
      }
    }
  }
  
  /**
   * Checks if a group forms a triangle pattern.
   * 
   * @param env    The game board.
   * @param pos    The starting position of the group.
   * @param wapiti The animal type (Wapiti).
   * @return <code>true</code> if the group forms a triangle; <code>false</code> otherwise.
   */
  private boolean checkTriangle(Board env,Pos pos, Animal wapiti) {
    var tuile1 = env.getEnv().get(new Pos(pos.x()+1,pos.y()-1));
    var tuile2 = env.getEnv().get(new Pos(pos.x()+1,pos.y()+1));
    if(tuile1!=null && tuile2!=null) {
      if(tuile1.getfaunatoken().token().equals(wapiti) && tuile2.getfaunatoken().token().equals(wapiti)) {
        return true;
      }
    }
    return false;
  }
  
  /**
   * Checks if a group forms a diamond pattern.
   * 
   * @param env    The game board.
   * @param pos    The starting position of the group.
   * @param wapiti The animal type (Wapiti).
   * @return <code>true</code> if the group forms a diamond; <code>false</code> otherwise.
   */
  private boolean checkLosange(Board env,Pos pos, Animal wapiti) {
    var tuile1 = env.getEnv().get(new Pos(pos.x()+1,pos.y()-1));
    var tuile2 = env.getEnv().get(new Pos(pos.x()+1,pos.y()+1));
    var tuile3 = env.getEnv().get(new Pos(pos.x()+2,pos.y()));
    if(tuile1!=null && tuile2!=null && tuile3!=null) {
      if(tuile1.getfaunatoken().token().equals(wapiti) && tuile2.getfaunatoken().token().equals(wapiti) && tuile3.getfaunatoken().token().equals(wapiti)) {
        return true;
      }
    }
    return false;
  }
 
  /**
   * Adds a Wapiti group to the scoring system for card choice 3.
   * 
   * @param list       The list of positions forming a group of Wapiti tokens.
   * @param nb_wapiti  A map storing group sizes and their counts.
   */
  private void Card3(ArrayList<Pos> list,HashMap<Integer,Integer> nb_wapiti) {
    nb_wapiti.put(list.size(),nb_wapiti.getOrDefault(list.size(), 0)+1);
  }
  
  /**
   * Adds a Wapiti group to the scoring system for card choice 4.
   * Checks if the group forms a circle pattern.
   * 
   * @param env        The game board.
   * @param list       The list of positions forming a group of Wapiti tokens.
   * @param nb_wapiti  A map storing group sizes and their counts.
   * @param wapiti     The animal type (Wapiti).
   */
  private void Card4(Board env, ArrayList<Pos> list,HashMap<Integer,Integer> nb_wapiti, Animal wapiti) {
    if(list.size()==1)  {
      nb_wapiti.put(list.size(),nb_wapiti.getOrDefault(list.size(),0)+1);
    }else if(list.size()>=2)  {
      if(checkCircle(env,list)) {
        nb_wapiti.put(list.size(),nb_wapiti.getOrDefault(list.size(),0)+1);
      }
    }
  }
  
  /**
   * Checks if the given list of positions forms a circle pattern.
   * 
   * @param env   The game board.
   * @param list  The list of positions to check.
   * @return <code>true</code> if the group forms a circle; <code>false</code> otherwise.
   */
 private boolean checkCircle(Board env, ArrayList<Pos> list) {
   var pos = list.getFirst();
   Pos[] positions = {new Pos(pos.x(),pos.y()),new Pos(pos.x(),pos.y()+2),new Pos(pos.x()+1,pos.y()-1),
       					               new Pos(pos.x()+1,pos.y()+3),new Pos(pos.x()+2,pos.y()),new Pos(pos.x()+2,pos.y()+2)};
   var position = new HashSet<Pos>();
   for(var ele : positions) {
     position.add(ele);
   }
   for(var p : list) {
     if(!position.contains(p)) {
         return false; 
     }
   }
   return true;
 }
 
 
 /**
  * Processes a Wapiti group based on the card choice.
  * 
  * @param env        The game board.
  * @param nbwapiti   A map storing group sizes and their counts.
  * @param pos        The list of positions forming a group of Wapiti tokens.
  * @param wapiti     The animal type (Wapiti).
  */
 private void choiceCard(Board env,HashMap<Integer,Integer> nbwapiti,ArrayList<Pos> pos,Animal wapiti) {
   if(choice==1) {
     Card1(env,pos,nbwapiti,wapiti);
   }else if(choice==2) {
     Card2(env,pos,nbwapiti,wapiti);
   }else if(choice==3) {
     Card3(pos,nbwapiti);
   }else if(choice==4) {
     Card4(env,pos,nbwapiti,wapiti);
   }
   
 }
 
 /**
  * Adds points to the player's score based on the group sizes and their associated points.
  * 
  * @param nb_wapiti  A map storing group sizes and their counts.
  * @param player     The player whose score is updated.
  */
  private void addPoint(HashMap<Integer,Integer> nb_wapiti,Player player) {
   var point = point();
   for(var ele : nb_wapiti.entrySet()) {
     var key = ele.getKey();
     var value = ele.getValue();
     if(point.containsKey(key)) {
       player.add(value*point.get(key));
     }
    }
  }
  
  /**
   * Returns a map of group sizes and their associated points based on the card choice.
   * 
   * @return A map containing group sizes and points.
   */
  private Map<Integer,Integer> point() {
    return switch(choice) {
    case 1 -> Map.of(0,0,1,2,2,5,3,9,4,13);
    case 2 -> Map.of(0,0,1,2,2,5,3,9,4,13);
    case 3 -> Map.of(0,0,1,2,2,4,3,7,4,10,5,14,6,18,7,23);
    case 4 -> Map.of(0,0,1,2,2,5,3,8,4,12,5,16,6,21);
    default -> null;
    };
  }
 
  /**
   * Calculates the player's score based on the card's rules.
   * 
   * @param player The player whose score is calculated.
   */
  @Override
  public void counterScore(Player player) {
    Objects.requireNonNull(player);
    var nb_wapiti = new HashMap<Integer,Integer>();
    var visit = new HashMap<Pos, Boolean>();
    var wapiti = Animal.WAPITI;
    for(int i = 0; i < player.getEnv().getSize(); i++) {
      for(int j = 0; j < player.getEnv().getSize(); j++) {
        var list = new ArrayList<Pos>();
        var tile = player.getEnv().getEnv().get(new Pos(i, j));
        if(tile != null && tile.getfaunatoken() != null && tile.getfaunatoken().token().equals(wapiti) && !visit.containsKey(new Pos(i,j))) {  
          nbAnimalHC(list,player.getEnv(),i,j,visit,tile.getfaunatoken().token(),istilesquare);
          choiceCard(player.getEnv(),nb_wapiti,list,wapiti);
        }
      }
    }
    addPoint(nb_wapiti,player);
  }

  /**
   * Returns the string representation of the card.
   * 
   * @return The string format of the card with its choice and type.
   */
  @Override
  public String toString(){
    return switch (choice) {
      case 1 -> "Carte,1,wapiti";
      case 2 -> "Carte,2,wapiti";
      case 3 -> "Carte,3,wapiti";
      case 4 -> "Carte,4,wapiti";
      default -> null; 
    };
  }


}
