package card;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import board.Board;
import board.Tile;
import model.Angle;
import model.Landscape;
import model.Pos;
import player.Player;

/**
 * This class calculates the score for a player's landscapes.
 * It supports scoring for both square and hexagonal tiles.
 * @param istuilecarre indicates whether the tile is square or not
 */
public record CartdScoreLandscape(boolean istuilecarre){
  
  /**
   * Adds or updates the count of a specific landscape type in the score map.
   * 
   * @param nb_habitat  Map storing the landscape type and its highest count.
   * @param landscape   The type of landscape to update.
   * @param nb          The count of contiguous tiles of the given landscape type.
   */
  private void addNbHabitat(HashMap<Landscape,Integer> nb_habitat,Landscape landscape,int nb)  {
    if(!nb_habitat.containsKey(landscape))  {
      nb_habitat.put(landscape, nb);
    } else if(nb > nb_habitat.get(landscape)) {
      nb_habitat.put(landscape, nb);
    }
  }
  
  /**
   * Counts the number of contiguous tiles with the same landscape type.
   * 
   * @param env         The game board.
   * @param i           The x-coordinate of the current tile.
   * @param j           The y-coordinate of the current tile.
   * @param v           Map to track visited positions.
   * @param landscape   The type of landscape to count.
   * @return The number of contiguous tiles with the same landscape type.
   */
  private int nbTileAnimalsAdjacent(Board env,int i,int j,HashMap<Pos, Boolean> v,Landscape landscape) {
    int nb = 0;
    if(i < 0 || j < 0 || i >= env.getSize() || j >= env.getSize())  {
      return 0;
    }
    if(v.containsKey(new Pos(i,j)) || env.getEnv().get(new Pos(i,j)) == null) {
      return 0; 
    }
    var t = env.getEnv().get(new Pos(i,j));
    if(t.getLandscape1() == null || !t.getLandscape1().equals(landscape)) {
      
      return 0;
    }
    v.put(new Pos(i,j),true);
    nb++;
    nb += nbTileAnimalsAdjacent(env,i+1,j,v,landscape);
    nb += nbTileAnimalsAdjacent(env,i-1,j,v,landscape);
    nb += nbTileAnimalsAdjacent(env,i,j-1,v,landscape);
    nb += nbTileAnimalsAdjacent(env,i,j+1,v,landscape);
    return nb; 
  }
  
  /**
   * Adds the score for the player based on the landscape counts.
   * 
   * @param player      The player whose score will be updated.
   * @param nb_habitat  Map storing the landscape type and its highest count.
   */
  private void addScore(Player player,HashMap<Landscape,Integer> nb_habitat) {
    for(var ele : nb_habitat.entrySet())  {
  	    player.add(ele.getValue());
  	    System.out.println(player.getNom() + ":" + "Le nombre le plus grand de " + ele.getKey() + " adjacent est de :" + ele.getValue());
  	  }
  }
  
  /**
   * Calculates the score for square tiles by identifying the largest groups of contiguous landscapes.
   * 
   * @param player The player whose score is calculated.
   * @return A map containing landscape types and their largest group sizes.
   */
  private HashMap<Landscape,Integer> scoreTileSquare(Player player) {
    int nb;
	    var visit = new HashMap<Pos, Boolean>();
	    var nb_habitat = new HashMap<Landscape,Integer>();
    	for(int i = 0; i < player.getEnv().getSize(); i++) {
  	    for(int j = 0; j< player.getEnv().getSize(); j++) {
  	      var t =player.getEnv().getEnv().get(new Pos(i,j));
  	      if(t != null && t.getLandscape1() != null && !visit.containsKey(new Pos(i,j))) {
  	        nb = nbTileAnimalsAdjacent(player.getEnv(),i,j,visit,t.getLandscape1());
  	        addNbHabitat(nb_habitat,t.getLandscape1(), nb);
  	      }
  	    }
  	  }
    addScore(player,nb_habitat);
    return nb_habitat;
  }  
  
  /**
   * Counts the number of contiguous hexagonal tiles with a specific landscape type.
   * 
   * @param tilepast   The previous tile in the chain.
   * @param env        The game board.
   * @param i          The x-coordinate of the current tile.
   * @param j          The y-coordinate of the current tile.
   * @param v          Map to track visited positions.
   * @param p          The landscape type to count.
   * @param stop       Number of steps in the current chain.
   * @return The number of contiguous tiles with the specified landscape type.
   */
  private int nbLandscapeH(Tile tilepast,Board env,int i,int j,HashMap<	Pos, Boolean> v,Landscape p,int stop) {
    int nb = 0;
    if(i < 0 || j < 0 || i >= env.getSize() || j >= env.getSize())  {
      return 0;
    }
    if(v.containsKey(new Pos(i,j)) || env.getEnv().get(new Pos(i,j)) == null) {
      return 0; 
    }
    var t = env.getEnv().get(new Pos(i,j));
    if(t.getLandscape2()!=null) {
      if(!t.getLandscape1().equals(p) && !t.getLandscape2().equals(p)) {
        return 0;
      }
    }
    if(stop>0) {
      if(!isLandscapeConnected(tilepast,t,p)) {
        return 0;
      }
    }
    v.put(new Pos(i,j),true);
    nb++;
    tilepast = t;
    var list  = callByAngle(tilepast,p,i,j);
    for(var ele : list) {
      nb +=nbLandscapeH(tilepast,env,ele.x(),ele.y(),v,p,stop++);
    }
    return nb; 
  }
  
  /**
   * Determines the positions of adjacent tiles based on the angle of the current tile.
   * 
   * @param t       The current tile.
   * @param p       The landscape type.
   * @param i       The x-coordinate of the current tile.
   * @param j       The y-coordinate of the current tile.
   * @return A list of positions representing adjacent tiles.
   */
  private ArrayList<Pos> callByAngle(	Tile t,Landscape p,int i, int j) {
    var list = new ArrayList<Pos>();
    var angle = recupAngleLandscape(t,p);
    if(angle.equals(Angle.NULL)) {
      posAngleNull(list,i,j);
    }else if(angle.equals(Angle.O)) {
      posAngleO(list,i,j);
    }else if(angle.equals(Angle.NO)) {
      posAngleNO(list,i,j);
    }else if(angle.equals(Angle.SO)) {
      posAngleSO(list,i,j);
    }else if(angle.equals(Angle.E)) {
      posAngleE(list,i,j);
    }else if(angle.equals(Angle.NE)) {
      posAngleNE(list,i,j);
    }else if(angle.equals(Angle.SE)) {
      posAngleSE(list,i,j);
    }
    return list; 
  }
  
  /**
   * Adds positions of tiles adjacent to the current tile when the angle is West (O).
   *
   * @param list The list to store the adjacent positions.
   * @param i    The x-coordinate of the current tile.
   * @param j    The y-coordinate of the current tile.
   */
  private void posAngleO(ArrayList<Pos> list,int i, int j) {
    list.add(new Pos(i,j-2));
    list.add(new Pos(i-1,j-1));
    list.add(new Pos(i+1,j-1));
  }
  
  /**
   * Adds positions of tiles adjacent to the current tile when the angle is North-West (NO).
   *
   * @param list The list to store the adjacent positions.
   * @param i    The x-coordinate of the current tile.
   * @param j    The y-coordinate of the current tile.
   */
  private void posAngleNO(ArrayList<Pos> list,int i, int j) {
    list.add(new Pos(i,j-2));
    list.add(new Pos(i-1,j-1));
    list.add(new Pos(i-1,j+1));
  }
  
  /**
   * Adds positions of tiles adjacent to the current tile when the angle is South-West (SO).
   *
   * @param list The list to store the adjacent positions.
   * @param i    The x-coordinate of the current tile.
   * @param j    The y-coordinate of the current tile.
   */
  private void posAngleSO(ArrayList<Pos> list,int i, int j) {
    list.add(new Pos(i,j-2));
    list.add(new Pos(i+1,j-1));
    list.add(new Pos(i+1,j+1));
  }
  
  /**
   * Adds positions of tiles adjacent to the current tile when the angle is East (E).
   *
   * @param list The list to store the adjacent positions.
   * @param i    The x-coordinate of the current tile.
   * @param j    The y-coordinate of the current tile.
   */
  private void posAngleE(ArrayList<Pos> list,int i, int j) {
    list.add(new Pos(i,j+2));
    list.add(new Pos(i-1,j+1));
    list.add(new Pos(i+1,j+1));
  }
  
  /**
   * Adds positions of tiles adjacent to the current tile when the angle is North-East (NE).
   *
   * @param list The list to store the adjacent positions.
   * @param i    The x-coordinate of the current tile.
   * @param j    The y-coordinate of the current tile.
   */
  private void posAngleNE(ArrayList<Pos> list,int i, int j) {
    list.add(new Pos(i,j+2));
    list.add(new Pos(i-1,j+1));
    list.add(new Pos(i-1,j-1));
  }
  


  /**
   * Adds positions of tiles adjacent to the current tile when the angle is South-East (SE).
   *
   * @param list The list to store the adjacent positions.
   * @param i    The x-coordinate of the current tile.
   * @param j    The y-coordinate of the current tile.
   */
  private void posAngleSE(ArrayList<Pos> list,int i, int j) {
    list.add(new Pos(i,j+2));
    list.add(new Pos(i+1,j+1));
    list.add(new Pos(i+1,j-1));
  }
  
  /**
   * Adds positions of tiles adjacent to the current tile when the angle is NULL (all directions).
   *
   * @param list The list to store the adjacent positions.
   * @param i    The x-coordinate of the current tile.
   * @param j    The y-coordinate of the current tile.
   */
  private void posAngleNull(ArrayList<Pos> list,int i, int j) {
    list.add(new Pos(i,j+2));
    list.add(new Pos(i,j-2));
    list.add(new Pos(i-1,j+1));
    list.add(new Pos(i-1,j-1));
    list.add(new Pos(i+1,j+1));
    list.add(new Pos(i+1,j-1));
  }
  
  /**
   * Retrieves the angle of a specific landscape on a tile.
   * 
   * @param t          The tile to check.
   * @param landscape  The landscape type to retrieve.
   * @return The angle associated with the specified landscape on the tile.
   */
  private Angle recupAngleLandscape(Tile t, Landscape landscape) {
    if(t.getLandscape1().equals(landscape)) {
      return t.getAngle1();
    }else {
      return t.getAngle2();
    }
  }
  
  /**
   * Checks if two tiles are connected based on their landscape angles.
   * 
   * @param tpast The previous tile.
   * @param t2    The current tile.
   * @param l     The landscape type being checked.
   * @return True if the tiles are connected, false otherwise.
   */
  private boolean isLandscapeConnected(Tile tpast, Tile t2,Landscape l) {
    var angle1 = recupAngleLandscape(tpast,l);
    var angle2 = recupAngleLandscape(t2,l);
    if(angle1.equals(Angle.NULL) || angle2.equals(Angle.NULL)) {
      return true; 
    }
    if(angle1.equals(Angle.O)) {
      return angle2.equals(Angle.E) || angle2.equals(Angle.SE) || angle2.equals(Angle.NE);
    }else if(angle1.equals(Angle.NO)) {
      return angle2.equals(Angle.E) || angle2.equals(Angle.SE) || angle2.equals(Angle.SO);
    }else if(angle1.equals(Angle.SO)) {
      return angle2.equals(Angle.E) || angle2.equals(Angle.NE) || angle2.equals(Angle.NO);
    }else if(angle1.equals(Angle.E)) {
      return angle2.equals(Angle.O) || angle2.equals(Angle.SO) || angle2.equals(Angle.NO);
    }else if(angle1.equals(Angle.NE)) {
      return angle2.equals(Angle.O) || angle2.equals(Angle.SO) || angle2.equals(Angle.SE);
    }else if(angle1.equals(Angle.SE)) {
      return angle2.equals(Angle.O) || angle2.equals(Angle.NE) || angle2.equals(Angle.NO);
    }else {
      return false;
  }
}
  
  /**
   * Calculates the score for hexagonal tiles.
   * 
   * @param player The player whose score is calculated.
   * @return A map containing landscape types and their largest group sizes.
   */
  private HashMap<Landscape,Integer> scoreTileH(Player player) {
    int nb;
	    var nb_habitat = new HashMap<Landscape,Integer>();
    	for(int i = 0; i < player.getEnv().getSize(); i++) {
  	    for(int j = 0; j< player.getEnv().getSize(); j++) {
  	      var visite = new HashMap<Pos, Boolean>();
  	      var t =player.getEnv().getEnv().get(new Pos(i,j));
  	      if(t != null && t.getLandscape1() != null && !visite.containsKey(new Pos(i,j))) {			
  	        nb = nbLandscapeH(t,player.getEnv(),i,j,visite,t.getLandscape1(),0);
  	        addNbHabitat(nb_habitat,t.getLandscape1(), nb);
  	        if(t.getLandscape2()!=null) {
  	          visite = new HashMap<Pos, Boolean>();
  	          nb = nbLandscapeH(t,player.getEnv(),i,j,visite,t.getLandscape2(),0);
  	          addNbHabitat(nb_habitat,t.getLandscape2(), nb);
  	        }
  	      }
  	    }
  	  }
    addScore(player,nb_habitat);
    return nb_habitat;
  }
  
  /**
   * Calculates the player's score based on the type of tiles (square or hexagonal).
   * 
   * @param player The player whose score is calculated.
   * @return A map containing landscape types and their largest group sizes.
   */
  public Map<Landscape,Integer> CompterScore(Player player) {
    Objects.requireNonNull(player);
    if(istuilecarre) {
      return scoreTileSquare(player);
    }else {
      return scoreTileH(player);
    }
  }
  
}
