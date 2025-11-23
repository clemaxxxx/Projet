package card;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

import game.Interaction;
import model.Animal;
import player.Player;

/**
 * The `ManagementCard` class is responsible for managing a collection of cards
 * and their associated operations such as adding cards, calculating scores,
 * and initializing cards for different game modes.
 */
public class ManagementCard {
  private final ArrayList<Card> cards = new ArrayList<>();
  
  /**
   * Default constructor for a ManagementCard object.
   */
  public ManagementCard() {
  }
  
  /**
   * Adds a card to the list of managed cards.
   *
   * @param card The card to add. Must not be null.
   * @throws NullPointerException If the card is null.
   */
  public void addCard(Card card) {
    Objects.requireNonNull(card);
    cards.add(card);
  }
  
  /**
   * Calculates the score for all the cards associated with a player.
   *
   * @param player The player whose score will be calculated. Must not be null.
   * @throws NullPointerException If the player is null.
   */
  public void counterScore(Player player) {
    Objects.requireNonNull(player);
    for (var card : cards) {
      System.out.println("Calcul du score pour la carte : " + card.toString());
      card.counterScore(player);
    }
  }
  
  /**
   * Returns a string representation of all the cards managed by this class.
   *
   * @return A string representation of the cards.
   */
  @Override
  public String toString() {
    var sb = new StringBuilder();
    for (var carte : cards) {
      sb.append(carte).append("\n");
    }
    return sb.toString();
  }
  
  
  /**
   * Prompts the user to choose a card variant or scoring card via terminal input.
   *
   * @return The user's choice as an integer (1, 2, or 3).
   * @throws IOException If an I/O error occurs during input reading.
   */
  private int choiceCard(Interaction interaction) throws IOException {
    var choice = interaction.askQuestion("Quelle variante voulez-vous choisir famille/intermediaire ou Carte Decompte(1,2,3) \n",new String[] { "1", "2","3" });
    return Integer.parseInt(choice);
  }
  
  /**
   * Prompts the user to choose a scoring card for a specific animal via terminal input.
   *
   * @param animal The animal associated with the scoring card.
   * @param interaction.
   * @return The user's choice as an integer (1, 2, 3, or 4).
   * @throws IOException If an I/O error occurs during input reading.
   */
  private int choiceCardDecompte(Animal animal,Interaction interaction) throws IOException {
    var choice = interaction.askQuestion("Quelle carte decompte voulez vous choisir pour " + animal + " (1,2,3,4):",new String[] {"1", "2","3","4"});
    return Integer.parseInt(choice);
  }
  
  /**
   * Adds scoring cards for all animals via terminal input.
   *
   * @param istilesquare Whether the tiles are square or hexagonal.
   * @throws IOException If an I/O error occurs during input reading.
   */
  private void addCardDecompteTerminal(boolean istilesquare,Interaction interaction) throws IOException {
    int choice = choiceCardDecompte(Animal.OURS,interaction);
    cards.add(new CardOurs(choice, istilesquare));
    choice = choiceCardDecompte(Animal.AIGLE,interaction);
    cards.add(new CardAigle(choice, istilesquare));
    choice = choiceCardDecompte(Animal.RENARD,interaction);
    cards.add(new CardRenard(choice, istilesquare));
    choice = choiceCardDecompte(Animal.SAUMON,interaction);
    cards.add(new CardSaumon(choice, istilesquare));
    choice = choiceCardDecompte(Animal.WAPITI,interaction);
    cards.add(new CardWapiti(choice, istilesquare));
  }
  
  /**
   * Adds a scoring card for a specific animal in a graphical interface.
   *
   * @param animal       The animal associated with the scoring card.
   * @param istilesquare Whether the tiles are square or hexagonal.
   * @param choice       The user's choice of scoring card.
   */
  private void addCardDecompteGraphic(Animal animal, boolean istilesquare, int choice) {
    switch (animal) {
      case OURS -> cards.add(new CardOurs(choice, istilesquare));
      case SAUMON -> cards.add(new CardSaumon(choice, istilesquare));
      case AIGLE -> cards.add(new CardAigle(choice, istilesquare));
      case RENARD -> cards.add(new CardRenard(choice, istilesquare));
      case WAPITI -> cards.add(new CardWapiti(choice, istilesquare));
    }
  }
  
  /**
   * Initializes cards based on the chosen variant or scoring card via terminal input.
   *
   * @param istilesquare Whether the tiles are square or hexagonal.
   * @param interaction.
   * @throws IOException If an I/O error occurs during input reading.
   */
  public void initializeCards(boolean istilesquare,Interaction interaction) throws IOException {
    Objects.requireNonNull(interaction);
    int choice = choiceCard(interaction);
    if (choice == 1) {
      cards.add(new CardVariante(1, istilesquare));
    } else if (choice == 2) {
      cards.add(new CardVariante(2, istilesquare));
    } else {
      addCardDecompteTerminal(istilesquare,interaction);
    }
  }
  
  /**
   * Initializes scoring cards for a specific animal in a graphical interface.
   *
   * @param animal       The animal associated with the scoring card.
   * @param istilesquare Whether the tiles are square or hexagonal.
   * @param choice       The user's choice of scoring card.
   */
  public void initializeCardsDecompteGraphic(Animal animal, boolean istilesquare, int choice) {
    Objects.requireNonNull(animal);
    addCardDecompteGraphic(animal, istilesquare, choice);
  }
  
  /**
   * Initializes variant cards in a graphical interface.
   *
   * @param istilesquare Whether the tiles are square or hexagonal.
   * @param choice       The user's choice of variant card (0 or 1).
   */
  public void initializeCardsVariantGraphic(boolean istilesquare, int choice) {
    if (choice == 0) {
      cards.add(new CardVariante(1, istilesquare));
    } else if (choice == 1) {
      cards.add(new CardVariante(2, istilesquare));
    }
  }
}