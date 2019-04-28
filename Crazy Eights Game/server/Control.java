/*
 * CS 3354 (Software Engineering) Semester Project
 * Spring 2019
 * Team 3
 * Dr. Marcus
 * Our semester project is the card game Crazy Eights implemented in Java.
 */

import java.util.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;

/**
 * This class is the game engine. It takes care of all game logic.
 *
 * @author Matthew Wethington, Martin Boerwinkle, Jonathan Guidry, Yoseph Wordofa
 *
 */
public class Control
{
    private static Scanner in;
    private static DiscardPile discardpile;
    private static Deck gamedeck;
    public static Player players[];
    private static int numplayers;
    public static int datasendfails;
    
    public static void main(String args[]) throws Exception
    {
        in = new Scanner(System.in);
        datasendfails = 0;
        int done = 0;
        while (done == 0)
        {
            prepareNewGame();
            UserInterface UI = new UserInterface(gamedeck, discardpile, in);
            
            //The game cycles through each players turn until someone wins
            int playerIdx = 0;
            int winner = -1;
            while (winner == -1)
            {
                System.out.println("Player " + (playerIdx + 1) + "'s Turn!");//Player number is their index + 1
                UI.SetCurrentPlayer(players[playerIdx]);//Player 1 gets to start the game
                if (players[playerIdx].GetCardQuantity() == 0)
                {
                    winner = playerIdx;
                    continue;//Skip over remaining statements in the loop
                }
                playerIdx = (playerIdx + 1) % numplayers;
            }
            System.out.println("Winner Is Player " + (winner + 1));
            //Now announce to the clients who won
            for (int i = 0; i < numplayers; i++)
            {
                if (i == winner)//If we've reached the winning player, tell them they won
                    players[winner].outToClient.writeBytes("0\n");//Tell the player they won
                else//Tell all other players they lost and who won the game
                {
                    players[i].outToClient.writeBytes("-1\n");//Tell the losing players they lost
                    players[i].outToClient.writeBytes((winner+1)+"\n");//Tell the losing players who won
                }
            }
            done = checkDone();
        }
        
        for (int i = 0; i < numplayers; i++)//Signal program shutdown to all connected clients
        {
            players[i].outToClient.writeBytes("-2\n");//Tell the client to terminate
        }
        
        System.out.println("Thanks For Playing!");
    }
    
    //Sets up everything required to start a new game
    public static void prepareNewGame()
    {
        //Get the number of players playing the game
        while (numplayers < 2 || numplayers > 7)
        {
            System.out.println("How Many Players Will Be Playing Crazy Eights For This Session?");
            numplayers = in.nextInt();
            in.nextLine();//Set the read pointer to the next line and throw away the rest of the input that wasn't a number.

            if (numplayers <= 1)
                System.out.println("Invalid Number Of Players. Number Of Players Must Be In Range [2, 7].");
            else if (numplayers > 7)
                System.out.println("Too Many Players. Maximum Allowed Is 7.");
        }
        //Construct the list of players.
        try
        {
            System.out.println("Waiting For Players To Join.");
            ServerSocket welcomeSocket = new ServerSocket(6677);
            players = new Player[numplayers];
            for (int i = 0; i < numplayers; i++)
            {
                players[i] = new Player(welcomeSocket.accept());
                System.out.println("Player " + (i+1) + " Has Joined!");
            }
        }catch(Exception e){
            System.out.println("Player connection failed");
        }
        prepareDeck();
        discardpile.InsertCard(gamedeck.GetTopCard()); //Prime the discard pile
        gamedeck.Deal(players);

        System.out.println("Starting Game.");
    }
    
    //Creates a new deck and shuffles it
    public static void prepareDeck()
    {
        System.out.println("Constructing Deck And Discard Pile.");
        //Declare and initialize the deck and discard pile
        discardpile = new DiscardPile();
        gamedeck = new Deck(discardpile);
        //Populate the deck with 52 cards, the standard deck size.
        for (Rank r : Rank.values())
            for (Suit s : Suit.values())
                gamedeck.InsertCard(new Card(s, r));
        gamedeck.Shuffle();      
    }
    
    //Prompts the user if they would like to start another game
    public static int checkDone()
    {
        System.out.println("Want To Play Another Game Session? (Respond Using Y For Yes Or N For No)");
        while(true) //probably a better way of doing this, but it shouldnt get stuck
        {
            String response = in.nextLine();//Make response not case sensitive
            switch (response)
            {
                case "N":
                    return 1;
                case "n":
                    return 1;
                case "Y":
                    return 0;
                case "y":
                    return 0;
                default:
                    System.out.println("Invalid Response. Please Try Again. (Respond Using Y For Yes Or N For No)");
            }
        }
    }
}
