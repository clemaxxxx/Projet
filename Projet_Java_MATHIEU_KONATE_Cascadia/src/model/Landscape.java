package model;

/**
 * Represents the different types of landscapes in the game.
 * Each landscape type has a corresponding abbreviation for easy identification.
 */
public enum Landscape {
  /**
   * Mountain landscape.
   */
  MONTAGNE,

  /**
   * Forest landscape.
   */
  FORET,

  /**
   * Prairie landscape.
   */
  PRAIRIE,

  /**
   * Marsh landscape.
   */
  MARAIS,

  /**
   * River landscape.
   */
  RIVIERE,

  /**
   * Null value, representing the absence of a landscape.
   */
  NULL;

  /**
   * Provides a string representation of the landscape type.
   *
   * @return A string representing the abbreviation of the landscape:
   *         "MO" for MONTAGNE, "F" for FORET, "P" for PRAIRIE,
   *         "MA" for MARAIS, "R" for RIVIERE, and an empty string for NULL.
   */
  @Override
  public String toString() {
    return switch (this) {
      case MONTAGNE -> "MO";
      case FORET -> "F";
      case PRAIRIE -> "P";
      case MARAIS -> "MA";
      case RIVIERE -> "R";
      case NULL -> "";
      };
   }
  
}

