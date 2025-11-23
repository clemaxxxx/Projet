package main;

import java.io.IOException;

import card.ManagementCard;
import game.Game;
import game.Interaction;
import game.Save;
import player.ManagementPlayers;

/**
 * The `Main` class serves as the entry point for the game application.
 * It initializes the game, manages players and cards, and allows for
 * either graphical or terminal-based gameplay.
 */
public class Main { 
  /**
   * The main method to launch the game application.
   *
   * @param args Command-line arguments (not used in this application).
   * @throws IOException If an I/O error occurs during save loading or game initialization.
   */
  public static void main(String[] args) throws IOException {
    var cards = new ManagementCard();
    var players = new ManagementPlayers();
    var save = new Save(0,cards,players,true);
    var interaction = new Interaction();
    if(!save.loadSave(interaction)) {
      var game = new Game();
      players.initializePlayers(interaction);;
      game.initializeGame(players.getNbPlayer());
      var choix = game.choiceGame(interaction);
      game.startGame(players,cards,game,interaction,choix);
  	  }else {
     var game = new Game();
     game.initializeGame(players.getNbPlayer());
     var choix = game.choiceGame(interaction);
     game.startGame(players,cards,game,interaction,choix);
  	  }
   }

}