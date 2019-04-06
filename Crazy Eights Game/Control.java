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
 * @author Matthew Wethington, Martin Boerwinkle, Jonathan Guidry, Yoseph Wordofa
 */
public class Control
{
    //Enumerations for suit types
    final static int Hearts = 1;
    final static int Clubs = 2;
    final static int Spades = 3;
    final static int Diamonds = 4;
    
    public static void main(String args[])
    {
        Scanner in = new Scanner(System.in);
        //Get the number of players playing the game
        int numplayers = 0;
        while(numplayers < 2)
        {
            System.out.println("How Many Players Will Be Playing Crazy Eights For This Session?");
            numplayers = in.nextInt();
            in.nextLine();//Set the read pointer to the next line and throw away the rest of the input that wasn't a number.
            
            if(numplayers < 1)
                System.out.println("Invalid Number Of Players. Number Of Players Must Be In Range [2, 7].");
            else if(numplayers > 7)
                System.out.println("Too Many Players. Maximum Allowed Is 7.");
        }
        
        //Construct the list of players.
        Player players[] = new Player[numplayers];
        for(int i = 0; i < numplayers; i++)
        {
            players[i] = new Player();
        }
        
        System.out.println("Constructing Deck And Discard Pile.");
        //Declare and initialize the deck and discard pile
        DiscardPile discardpile = new DiscardPile();
        Deck gamedeck = new Deck();
        //Populate the deck with 52 cards, the standard deck size.
        for(int i = 1; i <= 13; i++)
        {
            gamedeck.InsertCard(new Card(Hearts, i));
        }
        for(int i = 1; i <= 13; i++)
        {
            gamedeck.InsertCard(new Card(Clubs, i));
        }
        for(int i = 1; i <= 13; i++)
        {
            gamedeck.InsertCard(new Card(Spades, i));
        }
        for(int i = 1; i <= 13; i++)
        {
            gamedeck.InsertCard(new Card(Diamonds, i));
        }
        
        System.out.println("Starting Game.");
        //Start the game here (wait for the user interface to take shape)
        UserInterface UI = new UserInterface();
        UI.SetCurrentPlayer(players[0]);//Player 1 gets to start the game
        while(true)//The game goes on until someone wins
        {
            //Further to-do game logic here
            break;//Dummy statement to prevent infinite loop for this incomplete build
        }
        System.out.println("Thanks For Playing!");
    }
}
