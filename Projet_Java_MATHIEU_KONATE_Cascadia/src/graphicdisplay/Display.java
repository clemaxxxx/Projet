package graphicdisplay;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Objects;

import com.github.forax.zen.ApplicationContext;

import board.Board;
import board.FaunaToken;
import board.Tile;
import game.Game;
import model.Angle;
import model.Animal;
import model.Landscape;
import model.Pos;
import player.ManagementPlayers;
import player.Player;

/**
 * The Display class is responsible for rendering graphical output.
 * It provides functionality to display visual elements on the screen.
 */
public class Display {


  /**
   * Default constructor for the graphical display.
   *
   */
  public Display() {
  }
  
  
  /**
   * Clears the graphical rendering area.
   *
   * @param graphics The `Graphics2D` object used for rendering.
   * @param width The width of the rendering area.
   * @param height The height of the rendering area.
   */
  public void erase(Graphics2D graphics,float width,float height) {
    Objects.requireNonNull(graphics);
    graphics.clearRect(0, 0, (int)width, (int)height);
  }
  
  
  
  /**
   * Chooses a color corresponding to the given landscape type.
   *
   * @param landscape The type of landscape.
   * @return A `Color` object representing the landscape.
   */
  private  Color chooseLandscapeColor(Landscape landscape) {
    return switch (landscape) {
      case Landscape.MONTAGNE -> new Color(101,67,33);  
      case Landscape.FORET -> new Color(34,139,34);  
      case Landscape.PRAIRIE -> Color.GREEN;             
      case Landscape.MARAIS -> new Color(85,107,47);             
      case Landscape.RIVIERE -> new Color(135,206,235);              
      default -> Color.LIGHT_GRAY;         
    };
  }
  
  

  /**
   * Draws a divided hexagon with two landscape colors.
   *
   * @param graphics The graphics context.
   * @param centerx X-coordinate of the hexagon center.
   * @param centery Y-coordinate of the hexagon center.
   * @param radius The radius of the hexagon.
   * @param color1 The first color.
   * @param color2 The second color.
   * @param landscape1right Indicates if the first landscape is on the right side.
   */
  private void drawHexagonDivide(Graphics2D graphics, float centerx, float centery, float radius, Color color1, Color color2, boolean landscape1right) {
    var angle = Math.PI /3; 
    var x_points = new int[6];
    var y_points = new int[6];
    for (int i = 0; i < 6; i++) {
      x_points[i] = (int) (centerx + radius * Math.cos(i *angle +Math.PI/6));
      y_points[i] = (int) (centery + radius * Math.sin(i * angle + Math.PI/6));
    }
    graphics.setColor(landscape1right ? color2 : color1); 
    graphics.fillPolygon(new int[]{x_points[0], x_points[5], x_points[4], x_points[3]},
            new int[]{y_points[0], y_points[5], y_points[4], y_points[3]}, 4);
    graphics.setColor(landscape1right ? color1 : color2); 
    graphics.fillPolygon(new int[]{x_points[0], x_points[1], x_points[2], x_points[3]},
            new int[]{y_points[0], y_points[1], y_points[2], y_points[3]}, 4);
    graphics.setColor(Color.BLACK);
    graphics.drawPolygon(x_points, y_points, 6);
  }
  

  /**
   * Draws a hexagonal tile at a specified position.
   *
   * @param graphics The `Graphics2D` object for rendering.
   * @param tile The tile to draw.
   * @param startx X-coordinate of the board's starting position.
   * @param starty Y-coordinate of the board's starting position.
   * @param radius The radius of the hexagon.
   * @param i The row index of the tile.
   * @param j The column index of the tile.
   */
  private void drawHexagonTile(Graphics2D graphics, Tile tile, float startx, float starty, float radius, int i, int j) {
    var espacementx = radius * 1.5f;
    var espacementy = (float)(Math.sqrt(3)*radius);
    var xoffset = startx + j * espacementx +(i%2==0?0:espacementx/2);      
    var yoffset = starty+i * espacementy;
    var landscape = new ArrayList<>(tile.getLandscape().entrySet());
    var landscape1 = landscape.get(0);
    var landscape2 = landscape.size() > 1 ? landscape.get(1) : null;
    var color1 = chooseLandscapeColor(landscape1.getKey());
    var color2 = landscape2 != null ? chooseLandscapeColor(landscape2.getKey()) : Color.WHITE;
    var paysagedroite = landscape1.getValue() != null && tile.getAngle1().equals(Angle.E);
    drawHexagonDivide(graphics, xoffset, yoffset, radius, color1, color2, paysagedroite);      
    var text = tile.toStringForTile();
    Rectangle2D textBounds = graphics.getFontMetrics().getStringBounds(text, graphics);
    var textx = xoffset - (float) textBounds.getWidth() / 2;
    var texty = yoffset + (float) textBounds.getHeight() / 4;
    graphics.setColor(Color.BLACK); 
    graphics.drawString(text, textx, texty);
}


  
  /**
   * Displays the hexagonal game board.
   *
   * @param graphics The `Graphics2D` object for rendering.
   * @param env The game board.
   * @param startx X-coordinate for the top-left corner of the board.
   * @param starty Y-coordinate for the top-left corner of the board.
   * @param radius The radius of each hexagon.
   */
  private void displayHexagonalPlatform(Graphics2D graphics, Board env, float startx, float starty, float radius) {
    graphics.setFont(new Font("Arial", Font.BOLD, 12));
    graphics.setColor(Color.BLACK);
    for(int i = 0; i < env.getSize(); i++) {
        for(int j = 0; j < env.getSize(); j++) {
            var tile = env.getEnv().get(new Pos(i, j));
            if(tile != null) {
              drawHexagonTile(graphics,tile,startx,starty,radius,i,j);
            }
        }
    }
  }
  


  
  
  /**
   * Renders the grid for the current player's environment.
   *
   * @param graphics The graphics context used for rendering.
   * @param player The current player.
   * @param size_tab The size of the grid.
   * @param espacement The spacing between tiles.
   * @param startx The X-coordinate for the starting point of the grid.
   * @param starty The Y-coordinate for the starting point of the grid.
   */
  private void grid(Graphics2D graphics,Player player, int size_tab, float espacement, float startx, float starty) {
    graphics.setColor(Color.BLACK);
    graphics.setFont(new Font("Arial", Font.PLAIN, 10));
    for(int i=0; i<player.getEnv().getSize(); i++) {
      for(int j=0; j< player.getEnv().getSize(); j++) {
        var tile = player.getEnv().getEnv().get(new Pos(i,j));
        if(tile != null) {  
          var rectx = startx + j * espacement;
          var recty = starty + i * espacement;
          graphics.setColor(chooseLandscapeColor(tile.getLandscape1()));
          graphics.fill(new Rectangle2D.Float(rectx,recty,espacement,espacement));
          graphics.setColor(Color.BLACK);
          graphics.draw(new Rectangle2D.Float(rectx,recty,espacement,espacement));
          graphics.drawString(tile.toStringForTile(),rectx + espacement/6,recty + espacement/2);
        }
      }
    }
  }
  
  /**
   * Displays options for tiles and fauna tokens at the bottom of the graphical interface.
   *
   * @param graphics The graphics context used for rendering.
   * @param startx The X-coordinate for the starting point of the options.
   * @param starty The Y-coordinate for the starting point of the options.
   * @param espacement The spacing between options.
   * @param tab1 The array of tiles available for selection.
   * @param tab2 The array of fauna tokens available for selection.
   */
  private static void displayOptions(Graphics2D graphics, float startx, float starty, float espacement, Tile[] tab1,FaunaToken[] tab2) {
    graphics.setColor(Color.BLACK);
    graphics.setFont(new Font("Arial", Font.PLAIN, 16));
    var width_rectangle = espacement * 1.5f; 
    var height_rectangle = 80; 
    for(int i=0; i<4; i++) {
      var optionx = startx+i*width_rectangle; 
      var optiony = starty-100; 
      graphics.setColor(Color.LIGHT_GRAY);
      graphics.fill(new Rectangle2D.Float(optionx,optiony,width_rectangle,height_rectangle)); 
      graphics.setColor(Color.BLACK);
      graphics.draw(new Rectangle2D.Float(optionx, optiony, width_rectangle, height_rectangle)); 
      graphics.drawString(tab1[i].toString(),optionx +width_rectangle /6,optiony + 25); 
      graphics.drawString(tab2[i].toString(),optionx + width_rectangle /6,optiony +50); 
    }
  } 
  
  
  
  /**
   * Displays the variant selection options graphically.
   *
   * @param graphics The graphics context used for rendering.
   * @param startx The X-coordinate for the starting point of the options.
   * @param starty The Y-coordinate for the starting point of the options.
   */
  public void showVariant(Graphics2D graphics, float startx, float starty) {
    Objects.requireNonNull(graphics);
    graphics.setColor(Color.GRAY);
    graphics.fill(new Rectangle2D.Float(startx,starty,200,80));
    graphics.fill(new Rectangle2D.Float(startx+250,starty,200, 80));
    graphics.fill(new Rectangle2D.Float(startx+500,starty,200,80));
    graphics.setColor(Color.BLACK);
    graphics.setFont(new Font("Arial", Font.PLAIN, 16));
    graphics.drawString("Variante Famille",startx+200/4,starty + 80/2);
    graphics.drawString("Variante Intermédiaire",startx+250+200/4,starty+ 80/2);
    graphics.drawString("Carte Decompte",startx+250+200+500/4,starty+80/2);
  }
  
  
  /**
   * Displays animal cards for selection.
   *
   * @param graphics The `Graphics2D` object used for rendering.
   * @param screenWidth The width of the screen.
   * @param starty The Y-coordinate where the cards will be displayed.
   */
  public void displayAnimalsCard(Graphics2D graphics, float screenWidth, float starty) {
    Objects.requireNonNull(graphics);
    var cardWidth = 200;
    var cardHeight = 100; 
    var spacing = 20;
    String[] animals = {"Ours", "Saumon", "Aigle", "Renard", "Wapiti"};
    Color[] colors = {Color.GRAY, Color.BLUE, Color.GREEN, Color.ORANGE, Color.RED};
    var startx = (screenWidth - (animals.length * cardWidth + (animals.length - 1) * spacing)) / 2;
    graphics.setFont(new Font("Arial", Font.BOLD, 16));
    for(int i = 0; i < animals.length; i++) {
      var cardx = startx + i*(cardWidth+spacing);
      graphics.setColor(colors[i]);
      graphics.fill(new Rectangle2D.Float(cardx,starty,cardWidth,cardHeight));
      graphics.setColor(Color.BLACK);
      graphics.draw(new Rectangle2D.Float(cardx,starty,cardWidth,cardHeight));
      graphics.drawString(animals[i], 
          cardx + (cardWidth - (float) graphics.getFontMetrics().getStringBounds(animals[i], graphics).getWidth()) / 2, 
          starty + (cardHeight + (float) graphics.getFontMetrics().getHeight()) / 2);
    }
  }

  
  
  /**
   * Displays the hexagonal board and the available options for the current game state.
   *
   * @param context The application context used for rendering.
   * @param player The current player.
   * @param game The current game state.
   * @param radius The radius of the hexagons.
   * @param startx The starting X-coordinate of the board.
   * @param starty The starting Y-coordinate of the board.
   * @param optionx The X-coordinate for the options.
   * @param optiony The Y-coordinate for the options.
   * @param width The width of the rendering area.
   * @param height The height of the rendering area.
   */
  public void displayPOHexagonal(ApplicationContext context, Player player, Game game, float radius, float startx, float starty, float optionx, float optiony, float width, float height) {
    Objects.requireNonNull(context);
    Objects.requireNonNull(player);
    Objects.requireNonNull(game);
    context.renderFrame(graphics -> {
      erase(graphics, width, height);
      displayOptions(graphics,optionx,height-100,100,game.getChoiceTile(),game.getChoiceToken());
      displayHexagonalPlatform(graphics,player.getEnv(),startx,starty,radius);
    });
  }
  
  
  /**
   * Displays the graphical user interface for square tiles and available options.
   *
   * @param context The application context used for rendering.
   * @param player The current player.
   * @param game The game instance.
   * @param size The size of the player's board.
   * @param espacement The spacing between tiles.
   * @param startx The X-coordinate for the starting point of the grid.
   * @param starty The Y-coordinate for the starting point of the grid.
   * @param optionx The X-coordinate for the starting point of the options.
   * @param optiony The Y-coordinate for the starting point of the options.
   * @param width The width of the rendering area.
   * @param height The height of the rendering area.
   */
  public void displayPOCarre(ApplicationContext context, Player player, Game game, int size, float espacement, float startx, float starty, float optionx, float optiony,float width,float height) {
    Objects.requireNonNull(context);
    Objects.requireNonNull(player);
    Objects.requireNonNull(game);
    context.renderFrame(graphics -> {
      erase(graphics,width,height);
      displayOptions(graphics,optionx,optiony,100,game.getChoiceTile(),game.getChoiceToken());
      grid(graphics,player,size,espacement,startx,starty);
    });
  }
  
  /**
   * Displays the scoring cards for a selected animal.
   *
   * @param graphics The `Graphics2D` object used for rendering.
   * @param animal The selected animal.
   * @param screenWidth The width of the screen.
   * @param starty The Y-coordinate where the cards will be displayed.
   */
  public void displayCards(Graphics2D graphics, Animal animal, float screenWidth, float starty) {
    Objects.requireNonNull(graphics);
    Objects.requireNonNull(animal);
    var cardwidth = 150; 
    var cardheight = 100; 
    var spacing = 20;
    Color color = switch (animal) {
      case OURS -> Color.GRAY; case SAUMON -> Color.BLUE; case AIGLE -> Color.GREEN;
      case RENARD -> Color.ORANGE; case WAPITI -> Color.RED;
      default -> throw new IllegalArgumentException("Animal inconnu : " + animal);
    };
    var startx = (screenWidth-(4*cardwidth +3*spacing))/2;
    graphics.setFont(new Font("Arial", Font.BOLD, 16));
    for(int i = 0; i < 4; i++) {
      var cardx = startx + i * (cardwidth + spacing);
      graphics.setColor(color);
      graphics.fill(new Rectangle2D.Float(cardx, starty,cardwidth,cardheight));
      graphics.setColor(Color.BLACK);
      graphics.draw(new Rectangle2D.Float(cardx, starty, cardwidth, cardheight));
      graphics.drawString("Carte " +(i + 1),cardx +(cardwidth-(float)graphics.getFontMetrics().stringWidth("Carte "+(i + 1))) / 2, starty + (cardheight + (float) graphics.getFontMetrics().getHeight()) / 2);
    }
  }

  /**
   * Displays the winner of the game and scores of all players graphically.
   *
   * @param context The application context used for rendering.
   * @param players The management object for all players in the game.
   */
  public void displayWinnerGraphic(ApplicationContext context, ManagementPlayers players) {
    Objects.requireNonNull(context);
    Objects.requireNonNull(players);
    var res = determineWinner(players);
    context.renderFrame(graphics -> {
      var screeninfo = context.getScreenInfo();
      int width = screeninfo.width();
      int height = screeninfo.height();
      graphics.setColor(Color.WHITE);
      graphics.fillRect(0, 0, width, height);
      graphics.setColor(Color.BLACK);
      graphics.setFont(new Font("Arial", Font.BOLD, 24));
      for(int i =0; i<players.getNbPlayer();i++) {
        var j = players.getPlayerI(i);
        var score = "Score de " + j.getNom() + " : " + j.getScore();
        graphics.drawString(score, width / 4, height / 2 - (i*50));
      }
      graphics.setColor(res.contains("gagnant") ? Color.GREEN : Color.ORANGE);
      graphics.drawString(res, width / 4, height / 2 + 50);
    });
  }
  
  
  

  /**
 * Determines the winner of the game.
 *
 * @param players The management object for all players in the game.
 * @return A string indicating the winner.
 */
  private static String determineWinner(ManagementPlayers players) {
    var j = players.winner();
    var res = "Le gagnant est : " + j.getNom();
    return res;
  }



}