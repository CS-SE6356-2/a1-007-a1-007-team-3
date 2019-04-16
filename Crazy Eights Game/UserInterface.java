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
    private DiscardPile discardpile;
    private Deck gamedeck;
    private Scanner in;
    
    public UserInterface(Deck gamedeck, DiscardPile discardpile, Scanner in)
    {
        this.gamedeck = gamedeck;
        this.discardpile = discardpile;
        this.in = in;
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
        System.out.println("Discard Pile has "+discardpile.size()+" cards!");
        System.out.println("You must play a "+discardpile.GetRequiredRank()+" or a "+discardpile.GetRequiredSuit()+"!");
        System.out.println("Which card do you want to play? (-1 for draw)");
        boolean done = false;
        while(!done)
        {
            try
            {
                int reqCard = in.nextInt();
                in.nextLine();
                if(reqCard == -1)
                {
                    currentplayer.PullFromDeck(gamedeck);
                    done = true;
                    continue;
                }
                currentplayer.PlayCard(currentHand.get(reqCard), discardpile);
                done = true;
            }catch(Exception e)
            {
                System.out.println("Invalid Input. Please Try Again.");
                in.nextLine();//Consume the remaining invalid tokens to prevent an infinite error loop
            }
        }
    }
}
