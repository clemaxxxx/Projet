package graphicdisplay;

import java.awt.Color;
import java.io.IOException;
import java.util.Objects;

import com.github.forax.zen.Application;
import com.github.forax.zen.ApplicationContext;
import com.github.forax.zen.PointerEvent;

import board.Board;
import card.ManagementCard;
import game.Game;
import model.Animal;
import player.ManagementPlayers;
import player.Player;



/**
 * The ManageGraphic class coordinates the graphical display and interactions with click for the game
 */
public class ManageGraphic {
  
  private final Display display;
  private final Click click;
  
  
  /**
   * Constructor for GameGraphicManager.
   *
   */
  public ManageGraphic() {
    display = new Display();
    click = new Click();
  }
  
  
  /**
   * Manages click events on the hexagonal board to place tiles or fauna tokens.
   *
   * @param context The application context used for handling events.
   * @param env The game board environment.
   * @param startx The starting X-coordinate of the board.
   * @param starty The starting Y-coordinate of the board.
   * @param radius The radius of the hexagons.
   * @param item The item to be placed (tile or fauna token).
   * @param isTuile True if the item is a tile, false if it is a fauna token.
   * @return True if the item was successfully placed, false otherwise.
   */
  private boolean manageClicHexagone(ApplicationContext context, Board env, float startx, float starty, float radius, Object item, boolean isTuile) {
    var event = context.pollOrWaitEvent(10);
    if(event instanceof PointerEvent pointerEvent) {
      var location = pointerEvent.location();
      return click.checkAndPlaceTokenOrTile(env, location.x(), location.y(), startx, starty, radius, item, isTuile);
    }
    return false;
  }
  
  
  
  
  /**
   * Handles the placement of a tile and fauna token on the hexagonal board based on user clicks.
   *
   * @param context The application context for rendering and event handling.
   * @param player The current player.
   * @param game The current game state.
   * @param startx The starting X-coordinate of the board.
   * @param starty The starting Y-coordinate of the board.
   * @param radius The radius of the hexagons.
   * @param width The width of the rendering area.
   * @param height The height of the rendering area.
   * @param choice The index of the chosen tile and fauna token.
   * @return True when placement is complete, false otherwise.
   */
  private boolean handleHexagonalPlacement(ApplicationContext context, Player player, Game game, float startx, float starty, float radius, float width, float height, int choice) {
    while (!manageClicHexagone(context, player.getEnv(), startx, starty, radius, game.getChoiceTile()[choice], true)) {
      display.displayPOHexagonal(context, player, game, radius, startx, starty, width / 2f - 200, height - 150, width, height);
    }
    if (player.getEnv().possiblePlaceFTInEnv(game.getChoiceToken()[choice])) {
      while (!manageClicHexagone(context, player.getEnv(), startx, starty, radius, game.getChoiceToken()[choice], false)) {
        display.displayPOHexagonal(context, player, game, radius, startx, starty, width / 2f - 200, height - 150, width, height);
      }
    }
    game.removeChoice(choice);
    game.fill();
    return true;
  }
  
  
  /**
   * Handles a full turn for hexagonal tile placement and rotation.
   *
   * @param context The application context for rendering and event handling.
   * @param width The width of the rendering area.
   * @param height The height of the rendering area.
   * @param game The current game state.
   * @param player The current player.
   * @param startx The starting X-coordinate of the board.
   * @param starty The starting Y-coordinate of the board.
   * @param radius The radius of the hexagons.
   */
  private void clickHexagonal(ApplicationContext context, float width, float height, Game game, Player player, float startx, float starty, float radius) {
    boolean actionTerminee = false;
    while (!actionTerminee) {
      var choice = click.clickOptions(context,width / 2f - 200, height - 150, 100, game.getChoiceTile());

      if(choice != -1) {
        actionTerminee = handleHexagonalPlacement(context, player, game, startx, starty, radius, width, height, choice);
        display.displayPOHexagonal(context, player, game, radius, startx, starty, width / 2f - 200, height - 150, width, height);
      } else {
        click.handleHexagonalRotation(context,player, game, startx, starty, radius, width, height);
        display.displayPOHexagonal(context, player, game, radius, startx, starty, width / 2f - 200, height - 150, width, height);
      }

    }
  }
  
  /**
   * Manages a player's turn on the hexagonal board.
   *
   * @param context The application context for rendering and event handling.
   * @param player The current player.
   * @param game The current game state.
   * @param radius The radius of the hexagons.
   * @param startx The starting X-coordinate of the board.
   * @param starty The starting Y-coordinate of the board.
   * @param width The width of the rendering area.
   * @param height The height of the rendering area.
   * @throws IOException If an error occurs during the process.
   */
  private void hexagonalTour(ApplicationContext context, Player player, Game game, float radius, float startx, float starty, float width, float height) throws IOException {
    var optionx = 50; 
    var optiony = 50;
    display.displayPOHexagonal(context, player, game, radius, startx, starty, optionx, optiony, width, height);
    clickHexagonal(context, width, height, game, player, startx, starty, radius);
  }
  
  
  /**
   * Manages click events for placing tiles and fauna tokens on the board.
   *
   * @param context The application context for handling events.
   * @param width The width of the rendering area.
   * @param height The height of the rendering area.
   * @param game The game instance.
   * @param player The current player.
   * @param startx The X-coordinate for the starting point of the grid.
   * @param starty The Y-coordinate for the starting point of the grid.
   * @param espacement The spacing between tiles.
   * @param size The size of the player's board.
   * @param optionx The X-coordinate for the starting point of the options.
   * @param optiony The Y-coordinate for the starting point of the options.
   */
  private  void Click(ApplicationContext context, float width,float height,Game game, Player player,float startx,float starty,float espacement,int size,float optionx,float optiony) {
    var choice = -1;
    while(choice ==-1) {
      choice = click.clickOptions(context,optionx, optiony, 100,game.getChoiceTile());
    }
    while(!click.clickBoard(context,player, startx,starty,espacement,game.getChoiceTile()[choice],game.getChoiceToken()[choice],true)) {
      display.displayPOCarre(context, player, game, size, espacement, startx, starty, optionx, optiony,width,height);
    }
    if(player.getEnv().possiblePlaceFTInEnv(game.getChoiceToken()[choice])) {
      while(!click.clickBoard(context,player, startx, starty,espacement,game.getChoiceTile()[choice],game.getChoiceToken()[choice],false)) {
        display.displayPOCarre(context, player, game, size, espacement, startx, starty, optionx, optiony,width,height);
      }
    }
    display.displayPOCarre(context, player, game, size, espacement, startx, starty, optionx, optiony,width,height);
    game.removeChoice(choice);
    game.fill();	
  }
  
  /**
   * Manages the process of selecting an animal and its corresponding scoring card.
   *
   * @param context The `ApplicationContext` used for rendering and event handling.
   * @param screenWidth The width of the screen.
   * @param screenHeight The height of the screen.
   * @param cards The `ManagementCard` instance for managing selected cards.
   * @param isTileCarre Whether the game uses square tiles.
   */
  private  void fluxGame(ApplicationContext context, float screenWidth, float screenHeight, ManagementCard cards, boolean isTileCarre) {
    Animal[] animalSelected = {null}; 
    int[] cardSelected = {-1};
    while (animalSelected[0] == null) {
      context.renderFrame(graphics -> {
        display.erase(graphics, screenWidth, screenHeight);
        display.displayAnimalsCard(graphics, screenWidth, screenHeight/3);
      });
      animalSelected[0] = click.clickAnimals(context,(screenWidth-(200*5+4*20))/2, screenHeight/3);
    }
    while (cardSelected[0] == -1) {
      context.renderFrame(graphics -> {
        display.erase(graphics, screenWidth, screenHeight);
        display.displayCards(graphics, animalSelected[0], screenWidth,(2*screenHeight)/3);
      });
      cardSelected[0] = click.clickCard(context,(screenWidth-(150*4 +3*20))/2,(2*screenHeight)/3);
    }
    context.renderFrame(graphics -> display.erase(graphics,screenWidth,screenHeight));
    cards.initializeCardsDecompteGraphic(animalSelected[0],isTileCarre,cardSelected[0]);
    System.out.println("Carte de décompte " + cardSelected[0] + " choisie pour " + animalSelected[0]);
  }
  
  
  
  /**
   * Displays and handles the choice of game variant.
   *
   * @param context The `ApplicationContext` used for rendering and event handling.
   * @param width The width of the screen.
   * @param height The height of the screen.
   * @return The index of the chosen variant.
   */
  private  int manageChoiceVariant(ApplicationContext context, float width, float height) {
    var choice_variant = -1;
    while (choice_variant == -1) {
      context.renderFrame(graphics -> {
          display.erase(graphics, width, height);
          display.showVariant(graphics, width / 2f - 350, 50);
      });
      choice_variant = click.clickVariant(context,width / 2f - 350, 50);
    }
    System.out.println("Variante sélectionnée : " + choice_variant);
    return choice_variant;
  }

  /**
  * Manages a graphical game turn for square tiles.
  *
  * @param context The application context used for rendering and event handling.
  * @param player The current player.
  * @param game The game instance.
  * @param espacement The spacing between tiles.
  * @param startx The X-coordinate for the starting point of the grid.
  * @param starty The Y-coordinate for the starting point of the grid.
  * @param size The size of the player's board.
  * @param width The width of the rendering area.
  * @param height The height of the rendering area.
  * @throws IOException If an input/output error occurs during the turn.
  */
  private void tourGraphic(ApplicationContext context,Player player, Game game,float espacement,float startx,float starty,int size,float width,float height) throws IOException {   
    var optionx = (width-15*espacement)/2; 
    var optiony = (height- 15*espacement)/2;
    display.displayPOCarre(context, player, game, size, espacement, startx, starty, optionx, optiony,width,height);
    Click(context,width,height,game,player,startx,starty,espacement,size,optionx,optiony);
  }
  
  

  /**
   * Initializes the game based on the chosen variant.
   *
   * @param context The `ApplicationContext` used for rendering and event handling.
   * @param startx The X-coordinate for the starting position.
   * @param starty The Y-coordinate for the starting position.
   * @param width The width of the rendering area.
   * @param height The height of the rendering area.
   * @param cards The `ManagementCard` instance managing all cards.
   * @param game The game instance.
   */
  private void startPart(ApplicationContext context,float startx,float starty,int width,int height,ManagementCard cards,Game game) {
    int choicecard = manageChoiceVariant(context,width,height);
    if(choicecard==0 || choicecard==1) {
      cards.initializeCardsVariantGraphic(game.getTypeTile(), choicecard);
    }else {
      for(int i =0; i<5;i++) {
        fluxGame(context, width, height, cards, game.getTypeTile());
      }
    }
  }
  
  /**
   * Handles a complete turn for all players using square tiles.
   *
   * @param context The `ApplicationContext` used for rendering and event handling.
   * @param players The `ManagementPlayers` instance managing all players.
   * @param game The game instance.
   * @param espacement The spacing between tiles.
   * @param startx The X-coordinate for the grid's starting position.
   * @param starty The Y-coordinate for the grid's starting position.
   * @param size The size of the grid.
   * @param width The width of the rendering area.
   * @param height The height of the rendering area.
   * @throws IOException If an error occurs during a player's turn.
   */
  private void performTurnPlayersSquare(ApplicationContext context,ManagementPlayers players, Game game,float espacement,float startx,float starty,int size,float width,float height) throws IOException {
    for(int i=0; i<players.getNbPlayer();i++) {
      var player = players.getPlayerI(i);
      tourGraphic(context,player,game,espacement,startx,starty,size,width,height);
      player.getEnv().EnlargeEnv();
    }
  }
  
  /**
   * Handles a complete turn for all players using hexagonal tiles.
   *
   * @param context The `ApplicationContext` used for rendering and event handling.
   * @param players The `ManagementPlayers` instance managing all players.
   * @param game The game instance.
   * @param espacement The spacing between tiles.
   * @param startx The X-coordinate for the grid's starting position.
   * @param starty The Y-coordinate for the grid's starting position.
   * @param size The size of the grid.
   * @param width The width of the rendering area.
   * @param height The height of the rendering area.
   * @throws IOException If an error occurs during a player's turn.
   */
  private void performTurnPlayersHexagonal(ApplicationContext context,ManagementPlayers players, Game game,float espacement,float startx,float starty,int size,float width,float height) throws IOException {
    for(int i=0; i<players.getNbPlayer();i++) {
      var player = players.getPlayerI(i);
      hexagonalTour(context,player,game,espacement,startx,starty,width,height);
      player.getEnv().EnlargeEnv();
    }
  }

  
  /**
   * Manages the sequence of player turns for a game session.
   *
   * @param context The `ApplicationContext` used for rendering and event handling.
   * @param players The `ManagementPlayers` instance managing all players.
   * @param game The game instance.
   * @param espacement The spacing between tiles.
   * @param startx The X-coordinate for the grid's starting position.
   * @param starty The Y-coordinate for the grid's starting position.
   * @param size The size of the grid.
   * @param width The width of the rendering area.
   * @param height The height of the rendering area.
   */
  private void performTour(ApplicationContext context,ManagementPlayers players, Game game,float espacement,float startx,float starty,int size,float width,float height) {
    for(int i = 0 ; i<20; i++) {
      try {
        if(game.getTypeTile()) {
          performTurnPlayersSquare(context,players,game,espacement,startx,starty,size,width,height);
        }else {
          performTurnPlayersHexagonal(context,players,game,espacement,startx,starty,size,width,height);
        }
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }


  /**
   * Manages the graphical interface for the game.
   *
   * @param players The `ManagementPlayers` instance managing all players.
   * @param cards The `ManagementCard` instance managing all cards.
   * @param game The game instance.
   */
  public void gameGraphic(ManagementPlayers players,ManagementCard cards, Game game) {
    Objects.requireNonNull(players);
    Objects.requireNonNull(cards);
    Objects.requireNonNull(game);
    Application.run(Color.WHITE, context -> {
      var screenInfo = context.getScreenInfo();
      var width = screenInfo.width();
      var height = screenInfo.height();
      var size = players.getPlayerI(0).getEnv().getSize();
      var espacement = 40; 
      var startx = (width - size * espacement) /2; 
      var starty = (height -size * espacement) /2; 
      startPart(context,startx,starty,width,height,cards,game);
      performTour(context,players,game,espacement,startx,starty,size,width,height);
      for(int i = 0; i<players.getNbPlayer();i++)  {
        cards.counterScore(players.getPlayerI(i));
      }
      players.BonusMajorite(game.getTypeTile());
      display.displayWinnerGraphic(context,players);      
   });
  }

}
