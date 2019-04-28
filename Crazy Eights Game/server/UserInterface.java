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
        try
        {
            displayView();
        }
        catch(Exception e)
        {
            System.out.println("Failed To Write To Client");
        }
    }

    public void displayView() throws Exception
    {
        ArrayList<Card> currentHand = currentplayer.GetContents();
        //Try to tell the client their player information
        try
        {
            currentplayer.outToClient.writeBytes(currentHand.size()+"\n");
            for(Card c : currentHand)
                currentplayer.outToClient.writeBytes(c.GetRank()+"\n"+c.GetSuit()+"\n");
        
            currentplayer.outToClient.writeBytes(discardpile.GetRequiredRank()+"\n"+discardpile.GetRequiredSuit()+"\n");
            Control.datasendfails = 0;//We either recovered the connection or are already good
        }catch (Exception e)
        {
            System.out.println("Failed To Send Data To Client.");
            Control.datasendfails += 1;
            
            if (Control.datasendfails == Control.players.length)//If the server wasn't able to communicate with any of the players' clients
            {
                System.out.println("Failed To Communicate With All Clients. Assuming They All Left.");
                System.out.println("Terminating Server.");
                System.exit(0);
            }
        }
        //Get requests from the client
        try
        {
            int request = Integer.valueOf(currentplayer.inFromClient.readLine());
            if(request != -1)
            {
                Card temp = currentHand.get(request);
                currentplayer.PlayCard(temp, discardpile);
                System.out.println("This Player Played A " + temp.GetRank() + " Of " + temp.GetSuit() + "S");
            }else{
                currentplayer.PullFromDeck(gamedeck);
                System.out.println("This Player Drew From The Deck.");
            }
        }catch(Exception e)
        {
            System.out.println("Failed To Play Card "+e);
        }
    }
}
