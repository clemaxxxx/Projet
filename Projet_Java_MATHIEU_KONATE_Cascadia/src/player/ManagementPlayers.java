package player;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

import card.CartdScoreLandscape;
import game.Interaction;
import model.Landscape;
/**
 * Manages the players in the game, including adding players, determining the winner,
 * and awarding bonus points based on majority landscapes.
 */
public class ManagementPlayers {
  
  private final ArrayList<Player> players;

  /**
   * Initializes an empty list of players.
   */
  public ManagementPlayers() {
    players = new ArrayList<Player>();
  }
  /**
   * Adds a player to the game.
   *
   * @param j the player to add
   * @throws NullPointerException if the player is null
   */
  public void add(Player j) {
    Objects.requireNonNull(j);
    players.add(j);
  }
  /**
   * Gets the number of players in the game.
   *
   * @return the number of players
   */
  public int getNbPlayer() {
    return players.size();
  }
  /**
   * Retrieves a player by their index.
   *
   * @param i the index of the player
   * @return the player at the specified index
   */
  public Player getPlayerI(int i) {
    return players.get(i);
  }

  
  private void addBonusPoint(ArrayList<Player> joueur_gagnant) {
    if(joueur_gagnant.size() == 1) {
      var gagnant = joueur_gagnant.get(0);
      gagnant.add(2);
    }else if(joueur_gagnant.size() > 1) {
      for(var gagnant : joueur_gagnant) {
        gagnant.add(1);
      }
    }
  }
  /**
   * Adds bonus points to players with majority control of specific landscapes.
   *
   * @param istuilecarre true if square tiles are used, false otherwise
   */
  public void BonusMajorite(boolean istuilecarre) {
    Landscape[] landscapes = {Landscape.MONTAGNE,Landscape.FORET,Landscape.PRAIRIE, Landscape.MARAIS,Landscape.RIVIERE};
    var score = new CartdScoreLandscape(istuilecarre);
    for(var player : players) {
      int max_score = 0;
      var players_winner = new ArrayList<Player>();
      var nb_habitat = score.CompterScore(player);
      for(var landscape : landscapes) {
        int score_landscape = nb_habitat.getOrDefault(landscape, 0);
        if(score_landscape > max_score) {
          max_score = score_landscape;
          players_winner.clear();
          players_winner.add(player);
        }else if (score_landscape == max_score) {
          players_winner.add(player);
        }
      }
      addBonusPoint(players_winner);
    }
  }
  /**
   * Determines the winner of the game based on the highest score.
   *
   * @return the player with the highest score
   */
  public Player winner() {
    var j = players.get(0);
    for(int i=1; i<players.size();i++) {
      var player = players.get(i);
      if(player.getScore() > j.getScore()) {
        j = player;
      }
    }
    return j;
  }
  
  /**
   * Prompts the user to input the number of players.
   *
   * @return the number of players (between 1 and 4)
   * @throws IOException if an input error occurs
   */
  private int ChoiceNbPlayer(Interaction interaction) throws IOException {
    var choice = interaction.askQuestion("Veuillez choisir le nb de joueur(1-4) : \n",new String[] { "1", "2","3","4"});
  	  switch (choice) {
    case "1":
      return 1;
    case "2":
      return 2;
    case "3":
      return 3;
    case "4":
      return 4;
    }
  	  return 0;
  }
  
  /**
   * Initializes the players based on the given number.
   *
   * @param nb_player the number of players
   * @throws IOException if an input error occurs
   */
  private void InitializeNbPlayer(int nb_player,Interaction interaction) throws IOException {
    for(int i =0; i<nb_player;i++) {
      var name = interaction.requestName();
      players.add(new Player(name,5));
    }
  }
 
  /**
   * Initializes the players for the game, including prompting for the number of players and their names.
   * @param interaction the buffer.
   * @throws IOException if an input error occurs
   */
  public void initializePlayers(Interaction interaction) throws IOException {
    Objects.requireNonNull(interaction);
    int nbjoueur = ChoiceNbPlayer(interaction);
    InitializeNbPlayer(nbjoueur,interaction);
  }
  
  /**
   * Returns a string representation of all players and their details.
   *
   * @return a string containing all players
   */
  @Override
  public String toString() {
   var sb = new StringBuilder();
   for(var joueur : players) {
     sb.append(joueur).append("\n");
   }
   return sb.toString();
  }

}
