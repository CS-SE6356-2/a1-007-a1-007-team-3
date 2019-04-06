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
    private int suit;
    private int rank;
    
    public Card(int suit, int rank)
    {
        this.suit = suit;
        this.rank = rank;
    }
    
    public int GetSuit()
    {
        return suit;
    }
    
    public int GetRank()
    {
        return rank;
    }
}
