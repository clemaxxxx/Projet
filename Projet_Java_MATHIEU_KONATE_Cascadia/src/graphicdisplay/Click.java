package graphicdisplay;

import java.util.Objects;

import com.github.forax.zen.ApplicationContext;
import com.github.forax.zen.PointerEvent;

import board.Board;
import board.FaunaToken;
import board.Tile;
import game.Game;
import model.Animal;
import model.Pos;
import player.Player;


/**
 * The Click class encapsulates logic for handling click events in the game.
 */
public class Click {
  

  /**
   * Constructor for ClickManager.
   *
   */
  public Click() {

  }
  
  
  
  /**
   * Calculates the Euclidean distance between two points.
   *
   * @param x1 X-coordinate of the first point.
   * @param y1 Y-coordinate of the first point.
   * @param x2 X-coordinate of the second point.
   * @param y2 Y-coordinate of the second point.
   * @return The distance between the two points.
   */
  private double distance(float x1, float y1, float x2, float y2) {   
    return Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
  }
  
  /**
   * Handles clicks to select a scoring card.
   *
   * @param context The application context.
   * @param startx The X-coordinate of the first card.
   * @param starty The Y-coordinate of the cards.
   * @return The index of the selected card (1-based), or `-1` if no selection is made.
   */
  public int clickCard(ApplicationContext context,float startx, float starty) {
    Objects.requireNonNull(context);
    var width_card = 150; 
    var height_card = 100; 
    var event = context.pollOrWaitEvent(10);
    if (event instanceof PointerEvent pointerEvent) {
      var location = pointerEvent.location();
      var x = location.x();
      var y = location.y();
      for (int i = 0; i < 4; i++) {
        float cardx = startx + i * ( width_card + 20);
        if (x >= cardx && x <= cardx +  width_card && y >= starty && y <= starty + height_card) {
          System.out.println("Carte de décompte sélectionnée : Carte " + (i + 1));
          return i + 1; 
        }
      }
    }
    return -1;
  }
  
  
  /**
   * Handles clicks to select an animal card.
   *
   * @param context The application context.
   * @param startx The X-coordinate of the first card.
   * @param starty The Y-coordinate of the cards.
   * @return The selected animal, or `null` if no selection is made.
   */
  public Animal clickAnimals(ApplicationContext context,float startx, float starty) {
    Objects.requireNonNull(context);
    var width_Card = 200; 
    var height_Card = 100;
    Animal[] animaux = {Animal.OURS,Animal.SAUMON,Animal.AIGLE,Animal.RENARD,Animal.WAPITI};
    var event = context.pollOrWaitEvent(10);
    if (event instanceof PointerEvent pointerEvent) {
      var location = pointerEvent.location();
        var x = location.x();
        var y = location.y();
        for(int i = 0; i < animaux.length; i++) {
          float cardX = startx + i * (width_Card + 20);
          if(x >= cardX && x <= cardX + width_Card && y >= starty && y <= starty + height_Card) {
            System.out.println("Animal sélectionné : " + animaux[i]);
            return animaux[i];
          }
        }
    }
    return null;
  }
  
  
  
  /**
   * Handles clicks for selecting a variant option.
   * 
   * @param context The application context for handling events.
   * @param startx The X-coordinate for the starting point of the options.
   * @param starty The Y-coordinate for the starting point of the options.
   * @return The index of the selected variant.
   */
  public int clickVariant(ApplicationContext context, float startx, float starty) {
    Objects.requireNonNull(context);
    var event = context.pollOrWaitEvent(10);  
    if (event instanceof PointerEvent pointerEvent) {
      var location = pointerEvent.location();
      var x = location.x(); var y = location.y();
      if (x >= startx && x <= startx + 200 && y >= starty && y <= starty + 80) {
        System.out.println("Variante Famille sélectionnée.");
        return 0;
      }
      if (x >= startx + 250 && x <= startx + 250 + 200 && y >= starty && y <= starty + 80) {
        System.out.println("Variante Intermédiaire sélectionnée.");
        return 1;
      }
      if (x >= startx + 500 && x <= startx + 500 + 200 && y >= starty && y <= starty + 80) {
        System.out.println("Carte Decompte sélectionnée.");
        return 2;
      }
    }
    return -1;
  }
  
  
  
  /**
   * Handles board clicks for placing tiles or fauna tokens.
   * 
   * @param context The application context for handling events.
   * @param player The current player.
   * @param startx The X-coordinate for the starting point of the grid.
   * @param starty The Y-coordinate for the starting point of the grid.
   * @param espacement The spacing between tiles.
   * @param tiletoplace The tile to place.
   * @param token The fauna token to place.
   * @param Tile True if placing a tile, false if placing a fauna token.
   * @return True if the placement was successful, false otherwise.
   */
  public boolean clickBoard(ApplicationContext context,Player player, float startx, float starty, float espacement, Tile tiletoplace,FaunaToken token, boolean Tile) {
    Objects.requireNonNull(context);
    Objects.requireNonNull(player);
    Objects.requireNonNull(tiletoplace);
    Objects.requireNonNull(token);
    var event = context.pollOrWaitEvent(10);
    if(event == null) {
      return false;
    }
    if(event instanceof PointerEvent pointerEvent) {
      var location = pointerEvent.location();
      float grid_width = player.getEnv().getSize() * espacement;
      float grid_height = player.getEnv().getSize() * espacement;
      if(location.x() >= startx && location.x() <= startx + grid_width && location.y() >= starty && location.y() <= starty + grid_height) {
        var j = (int)((location.x()-startx) /espacement);  
        var i = (int)((location.y()-starty) /espacement); 
        if(Tile) {
         return player.getEnv().addTile(tiletoplace, i, j,true);
        }
        return player.getEnv().placeFaunaToken(token, i, j);
      }
    }
    return false;
  }
  
  
  
  /**
   * Handles option selection via clicks.
   *
   * @param context The application context for handling events.
   * @param startx The X-coordinate for the starting point of the options.
   * @param starty The Y-coordinate for the starting point of the options.
   * @param espacement The spacing between options.
   * @param tab1 The array of tiles available for selection.
   * @return The index of the selected option, or -1 if no selection is made.
   */
  public int clickOptions(ApplicationContext context,float startx, float starty, float espacement, Tile[] tab1) {
    Objects.requireNonNull(context);
    Objects.requireNonNull(tab1);
    var event = context.pollOrWaitEvent(10);  
    if(event instanceof PointerEvent pointerEvent) {
      var location = pointerEvent.location();
      var x = location.x();
      var y = location.y();
      for(int i=0; i<tab1.length; i++) {
        var optionx = startx +i*(espacement*1.5);
        var optiony = starty-espacement;
        var width_rectangle = espacement*1.5;
        var height_rectangle = espacement;
        if(x >= optionx && x <= optionx +  width_rectangle && y >= optiony && y <= optiony +  height_rectangle) {
          System.out.println("Option cliquée, indice : " + i);
          System.out.println("Option choisie : " + tab1[i]);
          return i;
        }
      }
    }
    return -1;
  }
  
  
  
  /**
   * Verifies if an item (tile or token) can be placed on the hexagonal board at a specific position.
   *
   * @param env The game board environment.
   * @param x The X-coordinate of the click.
   * @param y The Y-coordinate of the click.
   * @param startx The starting X-coordinate of the board.
   * @param starty The starting Y-coordinate of the board.
   * @param radius The radius of the hexagons.
   * @param item The item to be placed (tile or fauna token).
   * @param isTuile True if the item is a tile, false if it is a fauna token.
   * @return True if the item was successfully placed, false otherwise.
   */
  public boolean checkAndPlaceTokenOrTile(Board env, float x, float y, float startx, float starty, float radius, Object item, boolean isTuile) {
    Objects.requireNonNull(env);
    Objects.requireNonNull(item);
    var espacementx = radius * 1.5f;
    var espacementy = (float) (Math.sqrt(3) * radius);
    for (int i = 0; i < env.getSize(); i++) {
        for (int j = 0; j < env.getSize(); j++) {
            float xoffset = startx + j * espacementx + (i%2==0 ? 0:espacementx/2);
            float yoffset = starty + i * espacementy;
            if (distance(x, y, xoffset, yoffset) <= radius) {
                if (isTuile && item instanceof Tile tuile) {
                    return env.addTile(tuile, i, j, false); 
                }
                if (!isTuile && item instanceof FaunaToken jetonFaune) {
                    return env.placeFaunaToken(jetonFaune, i, j);
                }
            }
        }
    }
    return false;
  }
  
  
  /**
   * Handles hexagonal tile rotations based on user clicks.
   *
   * @param context The application context for event handling.
   * @param player The current player.
   * @param game The current game state.
   * @param startx The starting X-coordinate of the board.
   * @param starty The starting Y-coordinate of the board.
   * @param radius The radius of the hexagons.
   * @param width The width of the rendering area.
   * @param height The height of the rendering area.
   */
  public void handleHexagonalRotation( ApplicationContext context,Player player, Game game, float startx, float starty, float radius, float width, float height) {
    Objects.requireNonNull(context);
    Objects.requireNonNull(player);
    Objects.requireNonNull(game);
    var event = context.pollOrWaitEvent(10);
    if(event instanceof PointerEvent pointerEvent) {
      var location = pointerEvent.location();
      var x = location.x(); 
      var y = location.y();
      for(int i = 0; i < player.getEnv().getSize(); i++) {
        for(int j = 0; j < player.getEnv().getSize(); j++) {
          var xoffset = startx + j *radius *1.5f +(i % 2 == 0 ? 0 : radius * 0.75f);
          var yoffset = starty + i * radius * (float) Math.sqrt(3);
          if(distance(x, y, xoffset, yoffset) <= radius) {
            if(player.getEnv().getEnv().get(new Pos(i, j)) != null) {
              player.getEnv().getEnv().get(new Pos(i, j)).hexagonalRotation();
              System.out.println("Tuile en position (" + i + ", " + j + ") tournée.");
            }
          }
        }
      }
    }
  }
  
  

}
