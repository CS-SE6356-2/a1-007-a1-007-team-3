/*
 * CS 3354 (Software Engineering) Semester Project
 * Spring 2019
 * Team 3
 * Dr. Marcus
 * Our semester project is the card game Crazy Eights implemented in Java.
 */

import java.util.*;

/**
 * This class is the game engine. It takes care of all game logic.
 *
 * @author Matthew Wethington, Martin Boerwinkle, Jonathan Guidry, Yoseph Wordofa
 */
public class Control {

    public static void main(String args[])
    {
        Scanner in = new Scanner(System.in);
        int done = 0;
        while (done == 0)
        {
            //Get the number of players playing the game
            int numplayers = 0;
            while (numplayers < 2)
            {
                System.out.println("How Many Players Will Be Playing Crazy Eights For This Session?");
                numplayers = in.nextInt();
                in.nextLine();//Set the read pointer to the next line and throw away the rest of the input that wasn't a number.

                if (numplayers < 1)
                    System.out.println("Invalid Number Of Players. Number Of Players Must Be In Range [2, 7].");
                else if (numplayers > 7)
                    System.out.println("Too Many Players. Maximum Allowed Is 7.");
            }

            //Construct the list of players.
            Player players[] = new Player[numplayers];
            for (int i = 0; i < numplayers; i++)
                players[i] = new Player();

            System.out.println("Constructing Deck And Discard Pile.");
            //Declare and initialize the deck and discard pile
            DiscardPile discardpile = new DiscardPile();
            Deck gamedeck = new Deck(discardpile);
            //Populate the deck with 52 cards, the standard deck size.
            for (Rank r : Rank.values())
                for (Suit s : Suit.values())
                    gamedeck.InsertCard(new Card(s, r));
            
            gamedeck.Shuffle();
            discardpile.InsertCard(gamedeck.GetTopCard()); //Prime the discard pile
            gamedeck.Deal(players);
            System.out.println("Starting Game.");
            //Start the game here (wait for the user interface to take shape)
            UserInterface UI = new UserInterface(gamedeck, discardpile, in);
            int playerIdx = 0;
            int winner = -1;
            while (winner == -1)//The game goes on until someone wins
            {
                System.out.println("Launching UI For Player " + (playerIdx+1) + "!");//Player number is their index + 1
                UI.SetCurrentPlayer(players[playerIdx]);//Player 1 gets to start the game
                if (players[playerIdx].GetCardQuantity() == 0)
                    winner = playerIdx;
                playerIdx = (playerIdx + 1) % numplayers;
            }
            System.out.println("Winner Is Player " + (winner+1));
            System.out.println("Want To Play Another Game Session? (Respond Using Y For Yes Or N For No)");
            int responsegotten = 0;
            
            while (responsegotten == 0)
            {
                String response = in.nextLine();//Make response not case sensitive
                switch (response)
                {
                    case "N":
                        done = 1;
                        responsegotten = 1;
                        break;
                    case "n":
                        done = 1;
                        responsegotten = 1;
                        break;
                    case "Y":
                        responsegotten = 1;
                        break;
                    case "y":
                        responsegotten = 1;
                        break;
                    default: System.out.println("Invalid Response. Please Try Again.");
                }
            }
        }
        System.out.println("Thanks For Playing!");
    }
}
