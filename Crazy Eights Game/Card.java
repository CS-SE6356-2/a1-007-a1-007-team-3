/*
 * CS 3354 (Software Engineering) Semester Project
 * Spring 2019
 * Team 3
 * Dr. Marcus
 * Our semester project is the card game Crazy Eights implemented in Java.
 */

/**
 * This class is the card class, the most fundamental unit of any card game.
 * @author Matthew Wethington, Martin Boerwinkle, Jonathan Guidry, Yoseph Wordofa
 */
public class Card
{
    Suit suit;
    Rank rank;
    
    public Card(Suit suit, Rank rank)
    {
        this.suit = suit;
        this.rank = rank;
    }
    
    public Suit GetSuit()
    {
        return suit;
    }
    
    public Rank GetRank()
    {
        return rank;
    }
}
