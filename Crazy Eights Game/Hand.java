/*
 * CS 3354 (Software Engineering) Semester Project
 * Spring 2019
 * Team 3
 * Dr. Marcus
 * Our semester project is the card game Crazy Eights implemented in Java.
 */

import java.util.*;

/**
 * This class holds cards held by a player.
 * @author Matthew Wethington, Martin Boerwinkle, Jonathan Guidry, Yoseph Wordofa
 */
public class Hand
{
    private int numcards;
    private int score;
    private ArrayList<Card> cards;
    
    public Hand()
    {
        numcards = 0;
        score = 0;
        cards = new ArrayList<Card>();
    }
    
    public void PullFromDeck(Deck deck)
    {
        cards.add(deck.GetTopCard());
    }
    
    public void PlayCard(Card card)
    {
        //Do stuff here.
    }
    
    public void GetContents()
    {
        //This will be working with the UserInterface class.
        //We'll do stuff here later.
    }
    
    public int GetScore()
    {
        return score;
    }
    
    public int GetQuantity()
    {
        return numcards;
    }
}
