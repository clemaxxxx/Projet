package board;

import model.Animal;

/**
 * Represents a fauna token associated with a specific animal.
 * @param token the animal associated with this token
 */
public record FaunaToken(Animal token) {

  /**
   * Returns the string representation of the fauna token.
   * 
   * @return the name of the animal associated with this token
   */
  @Override
  public String toString() {
    return token.toString();  
  }
}