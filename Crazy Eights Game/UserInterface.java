/*
 * CS 3354 (Software Engineering) Semester Project
 * Spring 2019
 * Team 3
 * Dr. Marcus
 * Our semester project is the card game Crazy Eights implemented in Java.
 */

import java.util.*;

/**
 * This class takes care of all player interactions with the game.
 * @author Matthew Wethington, Martin Boerwinkle, Jonathan Guidry, Yoseph Wordofa
 */
public class UserInterface
{
    private Player currentplayer;
    
    public UserInterface()
    {
        
    }
    
    public void SetCurrentPlayer(Player player)
    {
        currentplayer = player;
        displayView();
    }

    public void displayView()
    {
        System.out.println("You have "+currentplayer.GetCardQuantity()+" cards!");
        ArrayList<Card> currentHand = currentplayer.GetContents();
        for(Card c : currentHand)
        {
            System.out.println("Rank: "+c.GetRank()+" Suit: "+c.GetSuit());
        }
    }
}
