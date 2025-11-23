package game;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Objects;

import board.Board;
import board.FaunaToken;
import board.Tile;
import card.Card;
import card.CardAigle;
import card.CardOurs;
import card.CardRenard;
import card.CardSaumon;
import card.CardVariante;
import card.CardWapiti;
import card.ManagementCard;
import model.Angle;
import model.Animal;
import model.Landscape;
import model.Pos;
import player.ManagementPlayers;
import player.Player;
/**
 * The Save class handles saving and loading the game's state,
 * including the round, players, cards, and board type.
 */
public class Save {
  private int round;
  private ManagementPlayers players;
  private ManagementCard cards;
  private boolean istilesquare;
  
  /**
   * Constructor for the Save class.
   *
   * @param round The current game round.
   * @param cards The card manager for the game.
   * @param manageplayer The player manager for the game.
   * @param istilesquare The board type (true for square, false for hexagonal).
   */
  public Save(int round,ManagementCard cards,ManagementPlayers manageplayer,boolean istilesquare) {
    Objects.requireNonNull(cards);
    Objects.requireNonNull(manageplayer);
    this.round = round;
    players = manageplayer;
    this.cards = cards;
    this.istilesquare = istilesquare;
  }
  
  /**
   * Saves the round and board type to the file.
   *
   * @param writer A BufferedWriter object to write to the file.
   * @throws IOException If an I/O error occurs.
   */
  private void saveRoundTile(BufferedWriter writer) throws IOException {
    writer.write("Tour: "+round);
    writer.newLine();
    writer.write("Tuile: "+istilesquare);
    writer.newLine();
  }
  /**
   * Saves the card information to the file.
   *
   * @param writer A BufferedWriter object to write to the file.
   * @throws IOException If an I/O error occurs.
   */
  private void saveCards(BufferedWriter writer) throws IOException {
    writer.write("Cartes:");
    writer.newLine();
    writer.write(cards.toString());
  }
  /**
   * Saves the players' information to the file.
   *
   * @param writer A BufferedWriter object to write to the file.
   * @throws IOException If an I/O error occurs.
   */
  private void savePlayers(BufferedWriter writer) throws IOException {
    writer.write("Joueurs:");
    writer.newLine();
    writer.write(players.toString());
  }

  /**
   * Saves the game's state to a specified file.
   *
   * @param file The path of the file to save the game state.
   */ 
  private void SaveInFile(String file) {
    var path = Path.of(file);
    try(var writer = Files.newBufferedWriter(path)) {
      saveRoundTile(writer);
      saveCards(writer);
      savePlayers(writer);
    }catch (IOException e) {
      System.err.println(e.getMessage());
    }
  }
  /**
   * Prompts the user to save the game and quit.
   * @param interaction The buffer.
   * @return true if the user chooses to save, otherwise false.
   * @throws IOException If an I/O error occurs.
   */
  public boolean save(Interaction interaction) throws IOException {
    Objects.requireNonNull(interaction);
    var choice = interaction.askQuestion("Voulez-vous sauvegarder et quitter (y/n) : ",new String[] { "y", "n" });
    if(choice.equalsIgnoreCase("y")) {
      SaveInFile("sauvegarde.txt");
      return true;
    }
    return false;
}

  /**
   * Prompts the user to load a previous save.
   * 
   * @param interaction The buffer.
   * @return true if a save is loaded successfully, otherwise false.
   * @throws IOException If an I/O error occurs.
   */
  public boolean loadSave(Interaction interaction) throws IOException {
    Objects.requireNonNull(interaction);
    var choice = interaction.askQuestion("Voulez-vous charger une sauvegarde (y/n) : ",new String[] { "y", "n" });
    if(choice.equalsIgnoreCase("y")) {
      load("sauvegarde.txt");
      return true;
    }
    return false;
}

  
  /**
   * Loads the game round from the save file.
   *
   * @param reader A BufferedReader object to read the save file.
   * @throws IOException If an I/O error occurs.
   */
  
  private void loadRound(BufferedReader reader) throws IOException {
    var ligne = reader.readLine();
    if(ligne.startsWith("Tour:")) {
        round = Integer.parseInt(ligne.split(":")[1].trim());
    }else{
      round = 0;
    }
  }
  /**
   * Loads the board type (square or hexagonal) from the save file.
   *
   * @param reader A BufferedReader object to read the save file.
   * @throws IOException If an I/O error occurs.
   */
  private void loadTilesType(BufferedReader reader) throws IOException {
    var ligne = reader.readLine();
    if (ligne.startsWith("Tuile:")) {
        istilesquare = Boolean.parseBoolean(ligne.split(":")[1].trim());
    }else {
      istilesquare = false;
    }
  }
  /**
   * Returns the appropriate card object based on the card type and choice number.
   *
   * @param cardType The type of the card (e.g., "ours", "aigle").
   * @param choice   The choice number for the card.
   * @return The corresponding card object.
   */
  private Card TypeCard(String carte,int choix) {
    if(carte.equals("ours")) {
      return new CardOurs(choix,istilesquare);
    }else if(carte.equals("aigle")) {
      return new CardAigle(choix,istilesquare);
    }else if(carte.equals("renard")) {
      return new CardRenard(choix,istilesquare);
    }else if(carte.equals("saumon")) {
      return new CardSaumon(choix,istilesquare);
    }else if(carte.equals("wapiti")) {
      return new CardWapiti(choix,istilesquare);
    }else if(carte.equals("variante")) {
      return new CardVariante(choix,istilesquare);
    }else {
      return null;
    }
  }
  /**
   * Loads the card data from the save file.
   *
   * @param reader A BufferedReader object to read the save file.
   * @throws IOException If an I/O error occurs.
   */
  private void loadCards(BufferedReader reader) throws IOException {
    String ligne;
    while((ligne = reader.readLine())!= null && !ligne.equals("Joueurs:")) {
      var array = ligne.split(",");
      if(array[0].equals("Carte")) {
        var choix = Integer.parseInt(array[1]);
        var typecard = array[2];
        cards.addCard(TypeCard(typecard,choix));
      }
    }
  }
  
  /**
   * Loads the landscape and angle data from a given array.
   *
   * @param array The array containing landscape and angle data.
   * @return A LinkedHashMap of Landscape as keys and Angle as values.
   */
  private LinkedHashMap<Landscape, Angle> loadLandscapes(String[] array){
    int i =3;
    var landscapes = new LinkedHashMap<Landscape, Angle>();
    while(!array[i].equals("animaux")) {
      landscapes.put(RecupLandscapes(array[i]),RecupAngle(array[i+1]));
      i+=2;
    }
    return landscapes;
  }

  /**
   * Retrieves a Landscape object based on a string representation.
   *
   * @param paysage The string representation of a landscape.
   * @return The corresponding Landscape object.
   */
  private Landscape RecupLandscapes(String paysage) {
    if(paysage.equals("MO")) {
      return Landscape.MONTAGNE;
    }else if(paysage.equals("F")) {
      return Landscape.FORET;
    }else if(paysage.equals("P")) {
      return Landscape.PRAIRIE;
    }else if(paysage.equals("MA")) {
      return Landscape.MARAIS;
    }else if(paysage.equals("R")) {
      return Landscape.RIVIERE;
    }
    return null;
  }
  /**
   * Retrieves an Angle object based on a string representation.
   *
   * @param angle The string representation of an angle.
   * @return The corresponding Angle object.
   */
  private Angle RecupAngle(String angle) {
    if(angle.equals("E")) {
      return Angle.E;
    }else if(angle.equals("SE")) {
      return Angle.SE;
    }else if(angle.equals("NE")) {
      return Angle.NE;
    }else if(angle.equals("O")) {
      return Angle.O;
    }else if(angle.equals("SO")) {
      return Angle.SO;
    }else if(angle.equals("NO")) {
      return Angle.NO;
    }else {
      return Angle.NULL;
    }
  }
  /**
   * Retrieves an Animal object based on a string representation.
   *
   * @param animal The string representation of an animal.
   * @return The corresponding Animal object.
   */
  private Animal RecupAnimals(String animal) {
    if(animal.equals("O")) {
      return Animal.OURS;
    }else if(animal.equals("S")) {
      return Animal.SAUMON;
    }else if(animal.equals("A")) {
      return Animal.AIGLE;
    }else if(animal.equals("R")) {
      return Animal.RENARD;
    }else if(animal.equals("W")) {
      return Animal.WAPITI;
    }
    return null;
  }
  /**
   * Loads animal data from a given array.
   *
   * @param array The array containing animal data.
   * @return A list of Animal objects.
   */
  private ArrayList<Animal> loadAnimal(String[] array){
    var animals = new ArrayList<Animal>(); 
    int i = 3;
    while(!array[i].equals("animaux")) {
      i++;
    }
    i++;
    while(!array[i].equals("jeton")) {
      animals.add(RecupAnimals(array[i]));
      i++;
    }
    return animals;
  }
  /**
   * Loads a FaunaToken object from a given array.
   *
   * @param array The array containing token data.
   * @return The FaunaToken object or null if no token exists.
   */
  private FaunaToken loadToken(String[] array) {
    int i = 3;
    while(!array[i].equals("jeton")) {
      i++;
    }
    i++;
    if(!array[i].equals("null")) {
      return new FaunaToken(RecupAnimals(array[i]));
    }
    return null;
  }
  

  /**
   * Loads the game board data from a BufferedReader.
   *
   * @param reader The BufferedReader to read board data from.
   * @return A HashMap of positions and their corresponding tiles.
   * @throws IOException If an I/O error occurs.
   */
  private HashMap<Pos, Tile> loadBoard(BufferedReader reader) throws IOException {
    String ligne;
    var plateau = new HashMap<Pos, Tile>();
    boolean inPlateau = false;
    while((ligne = reader.readLine()) != null) {
      if(ligne.startsWith("Plateau:")) {
        inPlateau = true;
        continue;
      }
      if(ligne.startsWith("nom:")) { 
        reader.reset();
        break;
      }
      if(inPlateau && !ligne.isEmpty()) {
        processBoardLine(ligne, plateau);  
      }
      reader.mark(1000);
    }
    return plateau;
  }
  /**
   * Processes a single line of board data and updates the board map.
   *
   * @param line  The line containing board data.
   * @param board The board map to update.
   */
  private void processBoardLine(String ligne, HashMap<Pos, Tile> board) {
    var array = ligne.split(",");
    if(!array[2].equals("null")) {  
      var x = Integer.parseInt(array[0]);
      var y = Integer.parseInt(array[1]);
      var landscapes = loadLandscapes(array);
      var animals = loadAnimal(array);
      var tuile = new Tile(landscapes, animals);
      var jeton = loadToken(array);
      if(jeton != null) {
        tuile.placeFaunaToken(jeton);
      }
      board.put(new Pos(x, y), tuile);
    }
  }

  /**
   * Loads players' data from a BufferedReader.
   *
   * @param reader The BufferedReader to read player data from.
   * @throws IOException If an I/O error occurs.
   */
  private void loadPlayers(BufferedReader reader) throws IOException {
    String ligne;
    while((ligne = reader.readLine()) != null ) {
      if(ligne.startsWith("nom:")) {
        var name = ligne.split(":")[1].trim();
        ligne = reader.readLine();
        var size = Integer.parseInt(ligne.split(":")[1].trim());
        var player = new Player(name,size);
        var board = loadBoard(reader);
        var env = new Board(size);
        env.setPlateau(board);
        player.SetEnv(env);
        players.add(player);
      }
    }
  }
  
  private int load(String file) {
    var path = Path.of(file);
    int tour = 0;
    try(var reader = Files.newBufferedReader(path)) {
      loadRound(reader);
      loadTilesType(reader);
      loadCards(reader);
      loadPlayers(reader);
    }catch (IOException e) {
      System.err.println(e.getMessage());
    }
    return tour;
  }

  /**
   * Returns a string representation of the Save object.
   *
   * @return A string describing the Save object.
   */
  @Override
  public String toString() {
    return "Save [tour=" + round + ", joueurs=" + players + ", cartes=" + cards + ", istilesquare=" + istilesquare+ "]";
  }
}
