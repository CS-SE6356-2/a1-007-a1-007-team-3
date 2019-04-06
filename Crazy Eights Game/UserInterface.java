/*
 * CS 3354 (Software Engineering) Semester Project
 * Spring 2019
 * Team 3
 * Dr. Marcus
 * Our semester project is the card game Crazy Eights implemented in Java.
 */

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
    }
}
