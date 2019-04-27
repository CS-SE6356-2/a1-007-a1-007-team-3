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
            System.out.println("Failed to write to Client");
        }
    }

    public void displayView() throws Exception
    {
        //System.out.println("You Have "+currentplayer.GetCardQuantity()+" Cards!");
        ArrayList<Card> currentHand = currentplayer.GetContents();
        int i = 1;
        currentplayer.outToClient.writeBytes(currentHand.size()+"\n");
        for(Card c : currentHand)
        {
            currentplayer.outToClient.writeBytes(c.GetRank()+"\n"+c.GetSuit()+"\n");
        /*    System.out.println("Card " + i + ":");
            System.out.println("Rank: "+c.GetRank());
            System.out.println("Suit: "+c.GetSuit());*/
            i++;
        }
        //System.out.println("Discard Pile Has "+discardpile.size()+" Cards!");
        //System.out.println("You Must Play A "+discardpile.GetRequiredRank()+" Or A "+discardpile.GetRequiredSuit()+"Or An EIGHT!");
        currentplayer.outToClient.writeBytes(discardpile.GetRequiredRank()+"\n"+discardpile.GetRequiredSuit()+"\n");
        System.out.println("Which Card Do You Want To Play? (Specify Using Card #. Type -1 For Draw)");
        try
        {
            int request = Integer.valueOf(currentplayer.inFromClient.readLine());
            if(request != -1)
            {
                currentplayer.PlayCard(currentHand.get(request), discardpile);
            }else{
                currentplayer.PullFromDeck(gamedeck);
            }
        }catch(Exception e)
        {
            System.out.println("failed to play card "+e);
        }
    }
}
