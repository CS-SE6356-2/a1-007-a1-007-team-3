/*
 * CS 3354 (Software Engineering) Semester Project
 * Spring 2019
 * Team 3
 * Dr. Marcus
 * Our semester project is the card game Crazy Eights implemented in Java.
 */

/**
 * This class is for constructing players, one of the most fundamental units of any game.
 * @author Matthew Wethington, Martin Boerwinkle, Jonathan Guidry, Yoseph Wordofa
 */
public class Player
{
    private Hand playerhand;
    
    public Player(Hand hand)
    {
        playerhand = hand;
    }
    
    public int GetCardQuantity()
    {
        return playerhand.GetQuantity();
    }
}
