package board;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

import model.Angle;
import model.Animal;
import model.Landscape;
import model.Pos;

/**
 * Represents the game board where tiles and fauna tokens are placed.
 */
public class Board {
 
  private HashMap<Pos,Tile> env;
  private int size;
  
  /**
   * Creates a new board with a specified size and initializes it.
   * 
   * @param size the size of the board
   */
  public Board(int size) {
    this.size = size;
    env = new HashMap<>();
    initializeEnv();
    addHabitatIdeal();
  }
  
  /**
   * Gets the size of the board.
   * 
   * @return the size of the board
   */
  public int getSize() {
    return size;
  }
  
  /**
   * Gets the map of positions and tiles on the board.
   * 
   * @return a map representing the environment of the board
   */
  public Map<Pos,Tile> getEnv() {
    return env;
  }
  
  /**
   * Sets a new environment for the board.
   * 
   * @param env the new environment to set
   */
  public void setPlateau(HashMap<Pos,Tile> env) {
    Objects.requireNonNull(env);
    this.env = env; 
  }
  
  /**
   * Initializes the board by creating empty positions.
   */
  private void initializeEnv() {
    for(int i =0;i<size;i++) {
      for(int j=0;j<size;j++) {
        env.put(new Pos(i,j), null);
      }
    }
  }

  /**
   * Adds predefined ideal habitats to the center of the board.
   */
  private void addHabitatIdeal() {
    var mid = size/2;
    var landscapes1 = new LinkedHashMap<Landscape, Angle>();
    landscapes1.put(Landscape.MONTAGNE, Angle.NULL); 
    var animaux1 = new ArrayList<Animal>();
    animaux1.add(Animal.OURS);
    env.put(new Pos(mid-1, mid-1),new Tile(landscapes1, animaux1));
    var landscapes2 = new LinkedHashMap<Landscape, Angle>();
    landscapes2.put(Landscape.MARAIS, Angle.NULL);
    var animaux2 = new ArrayList<Animal>();
    animaux2.add(Animal.AIGLE);
    animaux2.add(Animal.WAPITI);
    animaux2.add(Animal.RENARD);
    env.put(new Pos(mid - 1, mid), new Tile(landscapes2, animaux2));
    var landscapes3 =  new LinkedHashMap<Landscape, Angle>();
    landscapes3.put(Landscape.RIVIERE, Angle.NULL); 
    var animaux3 = new ArrayList<Animal>();
    animaux3.add(Animal.SAUMON);
    animaux3.add(Animal.OURS);
    env.put(new Pos(mid, mid - 1), new Tile(landscapes3, animaux3));
  }


  /**
   * Checks if a position is contiguous to another tile on the board.
   * 
   * @param x the x-coordinate
   * @param y the y-coordinate
   * @param istilesquare true if the game uses square tiles, false for hexagonal tiles
   * @return true if the position is contiguous, false otherwise
   */
  private boolean isContiguous(int x, int y, boolean istilesquare) {
    if(istilesquare) {
      var poshigh = new Pos(x-1,y);
      var posdown = new Pos(x+1,y);
      var posleft = new Pos(x,y-1);
      var posright = new Pos(x,y+1);
      return(env.containsKey(poshigh) && env.get(poshigh) != null) || (env.containsKey(posdown) && env.get(posdown) != null) ||
            (env.containsKey(posright) && env.get(posright) != null) || (env.containsKey(posleft) && env.get(posleft) != null);
    }else{
      var pos_e = new Pos(x, y + 2);
      var pos_o = new Pos(x, y - 2);
      var pos_ne = new Pos(x - 1, y + 1);
      var pos_no = new Pos(x - 1, y - 1);
      var pos_se = new Pos(x + 1, y + 1);
      var pos_so = new Pos(x + 1, y - 1);
      return(env.containsKey(pos_e) && env.get(pos_e) != null) || (env.containsKey(pos_o) && env.get(pos_o) != null) ||
            (env.containsKey(pos_ne) && env.get(pos_ne) != null) || (env.containsKey(pos_no) && env.get(pos_no) != null) ||
            (env.containsKey(pos_se) && env.get(pos_se) != null) || (env.containsKey(pos_so) && env.get(pos_se) != null);
    }
  }
    
  /**
   * Places a fauna token on a specific position of the board.
   * 
   * @param token the fauna token to place
   * @param x the x-coordinate
   * @param y the y-coordinate
   * @return true if the token was successfully placed, false otherwise
   */
  public boolean placeFaunaToken(FaunaToken token, int x, int y) {
    Objects.requireNonNull(token);
    if(x < 0 || y < 0 || x >= size || y>= size) {
      System.out.println("cette position est en dehors du plateau\n");
      return false;
    }            
    var tile = env.get(new Pos(x,y));
    if(tile == null) {
      System.out.println("il n'y a pas de tuile a cette emplacement\n");
      return false;
    }
    if(tile.getfaunatoken() != null) {
      System.out.println("la tuile contient déja un jeton faune\n");
      return false;
    }    
    if(tile.placeFaunaToken(token)) {
      System.out.println("Le jeton " + token.token() + " a ete place\n");
      return true;
    }else{
      System.out.println("Le jeton " + token.token() + " n'est pas compatible avec cette tuile\n");
      return false;
    }
  }
  
  /**
   * Adds a tile to the board at a specific position.
   * 
   * @param tile the tile to add
   * @param x the x-coordinate
   * @param y the y-coordinate
   * @param istilesquare true if the game uses square tiles, false for hexagonal tiles
   * @return true if the tile was successfully added, false otherwise
   */
  public boolean addTile(Tile tile, int x, int y, boolean istilesquare) {
    Objects.requireNonNull(tile);
    if(x < 0 || y < 0 || x >= size || y>= size) {
      System.out.println("cette position est en dehors du plateau \n");
      return false;
    }
    if(env.get(new Pos(x,y)) != null)  {
      System.out.println("Une tuile est déja present sur cet emplacement\n");
      return false;
    } 
    if(!isContiguous(x, y,istilesquare)) {
      System.out.println("La tuile doit être placer à côté d'une autre tuile\n");
      return false;
    }
    env.put(new Pos(x,y), tile);
    System.out.println("La tuile a ete place\n");
    return true;
  }
  
  /**
   * Checks if a fauna token can be placed on any tile in the board.
   * 
   * @param jeton the fauna token to check
   * @return true if the token can be placed, false otherwise
   */
  public boolean possiblePlaceFTInEnv(FaunaToken jeton)  {
    Objects.requireNonNull(jeton);
    for(var entry: env.entrySet()) {
      var value = entry.getValue();
      if(value != null) {
        if(value.PossiblePlaceFT(jeton))  {
          return true;
        } 
      }
    }
    return false;
  }
  

  private boolean checkEnlargeEnv() {
    for(var key: env.keySet()){
      if( (key.y() == 1 || key.x() == 1 || key.x() == size-2 || key.y() == size-2) && env.get(key) != null) {
        return true;
      }
    }
    return false;
  }
  

  private void recenterEnv() {
    var envtmp = new HashMap<Pos,Tile>();
    for(var entry : env.entrySet()) {
      var pos = entry.getKey();
      var tuile = entry.getValue();
      if(tuile != null) {
        var newpos = new Pos(pos.x()+1,pos.y()+1);
        envtmp.put(newpos, tuile);
        env.put(pos, null);
      }
    }
    env.putAll(envtmp);
  }
  
  /**
   * Enlarges the board by increasing its size and recenters all tiles.
   */
  public void EnlargeEnv() {
    if(checkEnlargeEnv()) {
      size++;
      for(int i = 0; i<size;i++) {
        for(int j=0;j<size;j++) {
          if(!env.containsKey(new Pos(i,j))) {
            env.put(new Pos(i,j), null);  
          }
        }
      }
      recenterEnv();
    }
  }
  
  /**
   * Returns a string representation of the board.
   * 
   * @return the string representation of the board
   */
  @Override
  public String toString() {
    var sb = new StringBuilder("Plateau de jeu :\n");
    for(int i = 0; i < size; i++) {
      for(int j = 0; j < size; j++) {
        var tile = env.get(new Pos(i,j));
        if(tile != null) {
          var pos = tile.toString() +"(" + i+","+j + ")";
          sb.append(String.format("[%-20s]",pos));
        }else{
          sb.append(String.format("[%-20s]", " "));
        }
      }
      sb.append("\n");
    }
    return sb.toString();
  }  

  
  

}
