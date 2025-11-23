package model;

/**
 * Represents the different types of animals available in the game.
 * Each animal is associated with a single-letter abbreviation.
 */
public enum Animal {
  /**
   * Represents a bear.
   */
  OURS,

  /**
   * Represents a salmon.
   */
  SAUMON,

  /**
   * Represents an eagle).
   */
  AIGLE,

  /**
   * Represents a fox.
   */
  RENARD,

  /**
   * Represents a wapiti.
   */
  WAPITI;

  /**
   * Provides a single-letter abbreviation for the animal.
   *
   * @return A string representing the abbreviation of the animal:
   *         "O" for OURS, "S" for SAUMON, "A" for AIGLE, "R" for RENARD, and "W" for WAPITI.
   */
  @Override
  public String toString() {
    return switch (this) {
      case OURS -> "O";
      case SAUMON -> "S";
      case AIGLE -> "A";
      case RENARD -> "R";
      case WAPITI -> "W";
    };
  }
}