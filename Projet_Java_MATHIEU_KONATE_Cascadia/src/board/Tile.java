package board;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

import model.Angle;
import model.Animal;
import model.Landscape;

/**
 * Represents a tile on the game board. A tile contains landscapes, animals, 
 * and optionally a fauna token.
 */
public class Tile {

  private LinkedHashMap<Landscape, Angle> landscapes; // Landscapes and their angles
  private final ArrayList<Animal> animals;           // List of animals on the tile
  private FaunaToken faunatoken;                     // Fauna token placed on the tile

  /**
   * Creates a tile with specified landscapes and animals.
   * 
   * @param landscapes the landscapes on this tile and their angles
   * @param animals the animals associated with this tile
   * @throws NullPointerException if landscapes or animals are null
   */
  public Tile(LinkedHashMap<Landscape, Angle> landscapes, ArrayList<Animal> animals) {
    Objects.requireNonNull(landscapes);
    Objects.requireNonNull(animals);
    this.landscapes = landscapes;
    this.animals = animals;
    faunatoken = null;
  }

  /**
   * Rotates the tile's landscapes clockwise by updating their angles.
   */
  public void hexagonalRotation() {
    var newAngles = new LinkedHashMap<Landscape, Angle>();
    for(var element : landscapes.entrySet()) {
        var landscape = element.getKey();
        var angle = element.getValue();
        newAngles.put(landscape, angle != null ? angle.next() : Angle.NULL);
    }
    landscapes = newAngles;
  }

  /**
   * Gets all landscapes and their angles on the tile.
   * 
   * @return a map of landscapes and their associated angles
   */
  public Map<Landscape, Angle> getLandscape() {
    return landscapes;
  }

  /**
   * Gets the first landscape on the tile.
   * 
   * @return the first landscape, or null if no landscapes exist
   */
  public Landscape getLandscape1() {
    return (Landscape) landscapes.keySet().toArray()[0];
  }

  /**
   * Gets the second landscape on the tile.
   * 
   * @return the second landscape, or null if only one landscape exists
   */
  public Landscape getLandscape2() {
    return landscapes.size() < 2 ? null : (Landscape) landscapes.keySet().toArray()[1];
  }

  /**
   * Gets the angle of the first landscape.
   * 
   * @return the angle of the first landscape
   */
  public Angle getAngle1() {
    return (Angle) landscapes.values().toArray()[0];
  }

  /**
   * Gets the angle of the second landscape.
   * 
   * @return the angle of the second landscape, or Angle.NULL if it does not exist
   */
  public Angle getAngle2() {
    return landscapes.size() < 2 ? Angle.NULL : (Angle) landscapes.values().toArray()[1];
  }

  /**
   * Gets the fauna token placed on the tile.
   * 
   * @return the fauna token, or null if none is placed
   */
  public FaunaToken getfaunatoken() {
    return faunatoken;
  }

  /**
   * Gets the animal at a specific index.
   * 
   * @param i the index of the animal
   * @return the animal at the specified index, or null if out of bounds
   */
  public Animal getAnimalIndex(int i) {
    return (i < 0 || i >= animals.size()) ? null : animals.get(i);
  }

  /**
   * Gets all animals on the tile.
   * 
   * @return a list of animals
   */
  public ArrayList<Animal> getAnimal() {
    return animals;
  }

  /**
   * Checks if a fauna token can be placed on this tile.
   * 
   * @param token the fauna token to place
   * @return true if the token can be placed, false otherwise
   * @throws NullPointerException if the token is null
   */
  public boolean PossiblePlaceFT(FaunaToken token) {
    Objects.requireNonNull(token);
    return token.token().equals(getAnimalIndex(0)) ||
           token.token().equals(getAnimalIndex(1)) ||
           token.token().equals(getAnimalIndex(2));
  }

  /**
   * Places a fauna token on the tile if possible.
   * 
   * @param token the fauna token to place
   * @return true if the token was successfully placed, false otherwise
   * @throws NullPointerException if the token is null
   */
  public boolean placeFaunaToken(FaunaToken token) {
    Objects.requireNonNull(token);
    if (PossiblePlaceFT(token)) {
      faunatoken = token;
      return true;
    }
    return false;
  }

  /**
   * Prepares a string representation of the tile for saving purposes.
   * 
   * @return a string with landscapes, animals, and fauna token
   */
  public String displaySave() {
    var landscapesStr = new StringBuilder();
    for (var entry : landscapes.entrySet()) {
      landscapesStr.append(entry.getKey().toString()).append(",").append(entry.getValue().toString()).append(",");
    }
    var animalsStr = new StringBuilder();
    for (Animal animal : animals) {
      animalsStr.append(animal.toString()).append(",");
    }
    var tokenStr = faunatoken != null ? faunatoken.toString() : "null";
    return String.format("paysage,%sanimaux,%sjeton,%s", landscapesStr, animalsStr, tokenStr);
  }

  /**
   * Returns a string representation of the tile.
   * 
   * @return a string with landscapes, animals, and fauna token
   */
  @Override
  public String toString() {
    var landscapesstr = landscapes.entrySet().stream()
     .map(entry -> entry.getKey().toString() + (entry.getValue() != Angle.NULL ? ":" + entry.getValue().toString() : ""))
     .reduce((a, b) -> a + "," + b)
     .orElse("");
    var animalsstr = animals.stream().map(Animal::toString).reduce((a, b) -> a + "," + b).orElse("");
    return String.format("(%s),(%s)", landscapesstr, animalsstr);
  }
  
  /**
   * Returns a string representation of the tile.
   * 
   * @return a string with landscapes, animals, and fauna token
   */
  public String toStringForTile() {
    var anglesstr = landscapes.values().stream().filter(angle -> angle != Angle.NULL).map(Angle::toString).reduce((a, b) -> a + "," + b).orElse("");
    var animalsstr = animals.stream().map(Animal::toString).reduce((a, b) -> a + "," + b).orElse("");              
    var tokenstr = faunatoken != null ? faunatoken.toString() : "";
    var angle_part = anglesstr.isEmpty() ? "" :String.format("(%s)",anglesstr);
    var animals_part = animalsstr.isEmpty() ? "" :String.format("(%s)",animalsstr);
    var token_part = tokenstr.isEmpty() ? "" :String.format("(%s)",tokenstr);
    return String.join(" ", angle_part, animals_part, token_part);
  }

}