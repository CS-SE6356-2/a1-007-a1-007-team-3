/*
 * CS 3354 (Software Engineering) Semester Project
 * Spring 2019
 * Team 3
 * Dr. Marcus
 * Our semester project is the card game Crazy Eights implemented in Java.
 */

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
        
        //Initialize the players (wait for the user interface to take shape)
        
        
        //Further to-do game logic here
    }
}
