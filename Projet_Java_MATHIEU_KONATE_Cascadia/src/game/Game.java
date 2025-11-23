package game;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Objects;

import board.FaunaToken;
import board.Tile;
import card.ManagementCard;
import graphicdisplay.ManageGraphic;
import model.Angle;
import model.Animal;
import model.Landscape;
import player.ManagementPlayers;
import player.Player;

/**
 * The `Game` class manages the core game logic, including tile and fauna token management,
 * game initialization, and player interactions.
 */
public class Game {
  private ArrayList<Tile> alltiles;
  private ArrayList<Tile> parttiles;
  private Tile[] choicetiles;
  private ArrayList<FaunaToken> allfaunatoken;
  private FaunaToken[] choicefaunatoken;
  private boolean istilesquare;
  
  /**
   * Constructor that initializes the game and determines the type of tiles.
   * 
   * @throws IOException if an I/O error occurs during initialization.
   */
  public Game() throws IOException {
    alltiles = new ArrayList<>();
    parttiles = new ArrayList<>();
    choicetiles = new Tile[4];
    allfaunatoken = new ArrayList<>();
    choicefaunatoken = new FaunaToken[4];
    istilesquare = choiceTypeTile();
  }
  
  /**
   * Gets the current available fauna tokens for players to choose.
   *
   * @return An array of `FaunaToken` objects representing the available tokens.
   */
  public FaunaToken[] getChoiceToken() {
    return choicefaunatoken;
  }
  
  /**
   * Gets the current available tiles for players to choose.
   *
   * @return An array of `Tile` objects representing the available tiles.
   */
  public Tile[] getChoiceTile() {
	    return choicetiles;
	}
  
  /**
   * Gets the complete list of all tiles in the game.
   *
   * @return An `ArrayList` of `Tile` objects.
   */
  public ArrayList<Tile> getAllTiles() {
    return alltiles;  
  }
  
  /**
   * Checks whether the game is using square tiles.
   *
   * @return `true` if the tiles are square, `false` otherwise.
   */
  public boolean getTypeTile() {
    return istilesquare;
  }
  
  /**
   * Converts a text representation of a hexagonal tile into a `Tile` object.
   *
   * @param txt The text representation of the tile in the format "landscape1,angle1,landscape2,angle2,animal1,animal2,animal3".
   * @return A `Tile` object representing the tile.
   */
  private Tile fromText(String txt)  {
    var array = txt.split(",");
    var landscape = new LinkedHashMap<Landscape,Angle>();
    var animals = new ArrayList<Animal>();
    for(int i = 0; i< 4;i+=2) {
      if(!array[i].equals("null")) {
        if(array[i+1].equals("null")) {
          landscape.put(Landscape.valueOf(array[i].toUpperCase()), Angle.NULL);
        }else {
          landscape.put(Landscape.valueOf(array[i].toUpperCase()), Angle.valueOf(array[i+1].toUpperCase()));
        }
      }
    }
    for(int i=4; i < 7;i++){
      if(!array[i].equals("null")) {
        animals.add(Animal.valueOf(array[i].toUpperCase()));
      }
    }
    return new Tile(landscape,animals);
  }
  
  /**
   * Converts a text representation of a square tile into a `Tile` object.
   *
   * @param txt The text representation of the tile in the format "landscape,animal1,animal2".
   * @return A `Tile` object representing the tile.
   */
  private Tile fromTextCarre(String txt)  {
    var array = txt.split(",");
    var landscape = new LinkedHashMap<Landscape,Angle>();
    var animals = new ArrayList<Animal>();
    landscape.put(Landscape.valueOf(array[0].toUpperCase()), Angle.NULL);
    animals.add(Animal.valueOf(array[1].toUpperCase()));
    animals.add(Animal.valueOf(array[2].toUpperCase()));
    return new Tile(landscape,animals);
  }
  
  /**
   * Loads tile data from a file into the game.
   *
   * @param path The path to the tile data file.
   * @throws IOException if an I/O error occurs.
   */
  public void load(Path path) throws IOException {
    Objects.requireNonNull(path);
    try(var reader = Files.newBufferedReader(path)) {
      String line;
      Tile tile;
      while((line = reader.readLine()) != null)  {
        if(istilesquare) {
          tile = fromTextCarre(line);
        }else {
          tile = fromText(line);
        }
        alltiles.add(tile);
      }
    }
  }
  
  /**
   * Initializes the list of all fauna tokens with 20 tokens for each animal type.
   */
  private void initializeFaunaToken() {
    Animal[] animals = {Animal.OURS,Animal.AIGLE,Animal.RENARD,Animal.SAUMON,Animal.WAPITI};
    for(var animal : animals)  {
      for(int i = 0 ; i<20; i++)  {
        allfaunatoken.add(new FaunaToken(animal));	      
      }	
    }
  }
  /**
   * Fills empty slots in the `choicetiles` array with tiles from the `parttiles` list.
   */
  private void fillChoiceTile() {
    for(int i = 0 ; i < choicetiles.length;i++) {
      if(choicetiles[i] == null && !parttiles.isEmpty())  {
        choicetiles[i] = parttiles.remove(0);  
      }
    }
  }
  /**
   * Fills empty slots in the `choicefaunatoken` array with fauna tokens from the `allfaunatoken` list.
   */
  private void fillChoiceFaunaToken() {
    for(int i =0; i < choicefaunatoken.length;i++) {
  	    if(choicefaunatoken[i] == null && !allfaunatoken.isEmpty())  {
  	      choicefaunatoken[i] = allfaunatoken.remove(0);
  	    }
	    }
  }
  
  /**
   * Fills the arrays of tiles and fauna tokens with new options.
   */
  public void fill() {
    fillChoiceFaunaToken();
    fillChoiceTile();
  }
  
  /**
   * Removes a tile and token choice after they have been used.
   *
   * @param choice The index of the choice to remove.
   */
  public void removeChoice(int choice) {
    choicetiles[choice] = null;
    choicefaunatoken[choice] = null;
  }
  
  /**
   * Initializes the game with the necessary tiles and tokens.
   *
   * @param nbjoueur The number of players.
   * @throws IOException if an error occurs during initialization.
   */
  public void initializeGame(int nbjoueur) throws IOException  {
    if(istilesquare) {
      load(Path.of("tuiles.txt"));
    }else {
      load(Path.of("tuiles_hexagone.txt"));
    }
    initializeFaunaToken();
    Collections.shuffle(alltiles);
    Collections.shuffle(allfaunatoken);
    for(int i = 0; i< 20*nbjoueur+3; i++)  {
      parttiles.add(alltiles.get(i));
    }
    fill();
  }
  
  /**
   * Counts the occurrences of each type of fauna token in the current choices.
   *
   * @return A map with each animal type as a key and its count as a value.
   */
  private HashMap<Animal,Integer> checkChoiceFaunaToken()  {
    var verif = new HashMap<Animal, Integer>();
    for(var ele : choicefaunatoken)  {
      if(ele != null)  {
       verif.put(ele.token(), verif.getOrDefault(ele.token(), 0) +1);	  
      }
    }
    return verif;
  }
  /**
   * Checks if any fauna token type appears exactly four times.
   *
   * @param countMap A map of animal types to their counts.
   * @return True if any animal type appears four times, false otherwise.
   */
  private boolean checkChoiceFaunaToken2(HashMap<Animal,Integer> check)  {
    for(var ele : check.entrySet())  {
  	    var value = ele.getValue();
  	    if(value == 4)  {
  	      return true;
  	    }
    }
    return false; 
  }
  /**
   * Prompts the player to replace three identical fauna tokens with new ones.
   *
   * @param animal The animal type that appears three times.
   * @throws IOException If an I/O error occurs during input.
   */
  private void requestPlayerTokenFauna3Identical(Animal animal, Interaction interaction) throws IOException   {
    var choice = interaction.askQuestion("Il y a 3 tokens identiques, voulez vous les repiocher (y\n) :",new String[] {"y", "n"});
    if("y".equals(choice))  {
      for(int i = 0; i<4; i++)  {
        if(choicefaunatoken[i].token().equals(animal)){
          allfaunatoken.add(choicefaunatoken[i]);
          choicefaunatoken[i] = null;
        }
      } 
      Collections.shuffle(allfaunatoken);
      fillChoiceFaunaToken();
    }
  }
  /**
   * Checks and adjusts the current fauna token choices if there are duplicates.
   *
   * @throws IOException If an I/O error occurs during input.
   */
  private void changeChoiceFaunaToken(Interaction interaction) throws IOException  {
    Objects.requireNonNull(interaction);
    var verif = checkChoiceFaunaToken();
    for(var ele : verif.entrySet())  {
      var value = ele.getValue();
      if(value == 4) {
        for(int i = 0; i< 4; i++)  {
          allfaunatoken.add(choicefaunatoken[i]);
          choicefaunatoken[i] = null;
        }
        Collections.shuffle(allfaunatoken);
        fillChoiceFaunaToken();
      }
      if(value == 3) {
        requestPlayerTokenFauna3Identical(ele.getKey(),interaction);
      }
    }
    if(checkChoiceFaunaToken2(checkChoiceFaunaToken())) {
      changeChoiceFaunaToken(interaction);	
    }
  }
  
  /**
   * Handles a game session in terminal mode.
   *
   * @param players The `ManagementPlayers` instance managing the players.
   * @param cards The `ManagementCard` instance managing the cards.
   * @param interaction.
   * @throws IOException if an error occurs during gameplay.
   */
  private void gameTerminal(ManagementPlayers players,ManagementCard cards, Interaction interaction) throws IOException {
    System.out.println("Debut de la partie \n");
    cards.initializeCards(istilesquare,interaction);
    for(int j =0; j<20;j++) {
      	for(int i = 0; i<players.getNbPlayer();i++)  {
    	    System.out.println("Tour" + (j+1));
    	    var joueur = players.getPlayerI(i);
    	    performTour(joueur,interaction);   
    	    joueur.getEnv().EnlargeEnv();
      	}
      var save = new Save(j,cards,players,istilesquare);
      if(save.save(interaction)) {
        break;
      }
    }
    	for(int i = 0; i<players.getNbPlayer();i++)  {
      cards.counterScore(players.getPlayerI(i));
    	}
    players.BonusMajorite(istilesquare);
    System.out.print("le gagnant est : " + players.winner());
  }
  
  
  
  
  /**
   * Handles a game session in graphic mode.
   *
   * @param joueurs The `ManagementPlayers` instance managing the players.
   * @param cartes The `ManagementCard` instance managing the cards.
   * @param game.
   * @throws IOException if an error occurs during gameplay.
   */
  private void gameGraphic(ManagementPlayers players,ManagementCard cards,Game game) {
    var manage_graphic = new ManageGraphic();
    manage_graphic.gameGraphic(players, cards, game);
  }
  
 
  /**
   * start the game.
   *
   * @param players The `ManagementPlayers` instance managing the players.
   * @param cards The `ManagementCard` instance managing the cards.
   * @param game.
   * @param interaction.
   * @param choice.
   * @throws IOException if an error occurs during gameplay.
   */
  public void startGame(ManagementPlayers players,ManagementCard cards,Game game,Interaction interaction,int choice) throws IOException {
    if(choice==2) {
      gameGraphic(players,cards,game);
    }else {
      gameTerminal(players,cards,interaction);
    }
    
  }
  
  /**
   * Prompts the user to choose the type of tiles for the game (square or hexagonal).
   *
   * @return `true` if the user selects square tiles ("C"), `false` if hexagonal tiles ("H").
   * @throws IOException if an I/O error occurs during user input.
   */
  private boolean choiceTypeTile() throws IOException {
    var interaction = new Interaction();
    var choice = interaction.askQuestion("Veuillez choisir le type de tile (C pour carre; H pour hexagonal): ",new String[] {"C", "H"});
  	  if(choice.equals("C")) {
      return true;
  	  }else{
  	    return false;
  	  }
  }
 
  /**
   * Determines the game mode based on the tile type.
   * @param interaction .
   * @return `1` for terminal mode, `2` for graphical mode.
   * @throws IOException if an error occurs during mode selection.
   */
  public int choiceGame(Interaction interaction) throws IOException {
    Objects.requireNonNull(interaction);
    if(!istilesquare) {
      return 2;
    }
    var choice = interaction.askQuestion("Veuillez choisir le jeu, (1 pour terminal; 2 pour graphique): \n",new String[] { "1", "2" });
  	  if(choice.equals("1")) {
      return 1;
  	  }
    	return 2;
  }
  
  
  /**
   * Handles the placement of a tile or fauna token at user-specified coordinates.
   *
   * @param player The player placing the tile or token.
   * @param choiceIndex The index of the chosen tile or token.
   * @param isTile True if placing a tile, false if placing a fauna token.
   * @throws IOException if an I/O error occurs during input.
   */
  private void choicePositionTileFaunaToken(Player j, int nb, boolean tile) throws IOException {
    var interaction = new Interaction();
    	var array = interaction.getCoordinates();
    	boolean f;
    	if(tile) {
    	  f = j.getEnv().addTile(choicetiles[nb-1], array[0], array[1],istilesquare);
    	}else {
    	  f = j.getEnv().placeFaunaToken(choicefaunatoken[nb-1], array[0], array[1]);
    	}
    	while (!f) {
    	  array = interaction.getCoordinates();
    	  if(tile) {
    	    f = j.getEnv().addTile(choicetiles[nb-1], array[0], array[1],istilesquare);
    	  }else {
    		f = j.getEnv().placeFaunaToken(choicefaunatoken[nb-1], array[0], array[1]);
    	  }
    	}
  }
  /**
   * Handles a single player's turn.
   *
   * @param j The current player.
   * @throws IOException if an error occurs during the player's turn.
   */
  private void performTour(Player j,Interaction interaction) throws IOException {
    System.out.println("C'est au tour du joueur " + j.getNom());
    changeChoiceFaunaToken(interaction);
  	  System.out.println("Voici ci-dessous les 4 choice de tokens faunes et tiles");
  	  for(int i = 0; i < 4; i++) {
  	    System.out.println("choice numero "+ (i+1) + " " + choicetiles[i] + " token : " + choicefaunatoken[i]);
  	  }
    	System.out.println(j.getEnv());
    	var num_option = choiceOption(interaction);
    	System.out.println(j.getEnv());
    choicePositionTileFaunaToken(j, num_option,true);
  	  System.out.println(j.getEnv());
  	  choicePositionTileFaunaToken(j, num_option,false);
  	  System.out.println(j.getEnv());
  	  choicetiles[num_option-1] = null;
  	  choicefaunatoken[num_option-1] = null;
  	  fill();
  }
  
  
  
  /**
   * Prompts the user to choose an option from a predefined range.
   *
   * @return The chosen option as an integer (between 1 and 4 inclusive).
   * @throws IOException if an I/O error occurs during user input.
   */
  private int choiceOption(Interaction interaction) throws IOException{
    var choice = interaction.askQuestion("Veuillez choisir une des option ci dessus :",new String[] {"1", "2","3","4"});
    	int nb = Integer.parseInt(choice);
  	  return nb;
  }

}
