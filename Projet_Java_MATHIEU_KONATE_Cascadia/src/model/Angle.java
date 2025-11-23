package model;

/**
 * Represents the six possible angles (E, SE, SO, O, NO, NE) and a NULL value
 * used in the game to define orientations or directions.
 */
public enum Angle {

  /**
   * East direction.
   */
  E,

  /**
   * South-East direction.
   */
  SE,

  /**
   * South-West direction.
   */
  SO,
  
  /**
   * West direction.
   */
  
  O,

  /**
   * North-West direction.
   */
  NO,

  /**
   * North-East direction.
   */
  NE,

  /**
   * Null value, representing no direction.
   */
  NULL;
 
  /**
   * Gets the next angle in a clockwise rotation.
   *
   * @return The next angle in clockwise order. If the current angle is NULL, returns NULL.
     */
   public Angle next() {
     return switch (this) {
       case E -> SE;
       case SE -> SO;
       case SO -> O;
       case O -> NO;
       case NO -> NE;
       case NE -> E;
       case NULL -> NULL;
     };
   }
   /**
    * Provides a string representation of the angle.
    *
    * @return The name of the angle.
    */
   @Override
   public String toString() {
     return this.name();
   }
}
