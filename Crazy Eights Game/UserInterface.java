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
    
        //this method is to triger the AI display only opposed to the method bellow    
    public void SetCurrentAIPlayer(Player player)
    {
        currentplayer = player;
        displayAI();
    }
      //this method is to triger the user display only opposed to the method above

    public void SetCurrentUserPlayer(Player player)
    {
        currentplayer = player;
        displayUser();
    }

    public void displayUser()
    {
        System.out.println("You Have "+currentplayer.GetCardQuantity()+" Cards!");
        ArrayList<Card> currentHand = currentplayer.GetContents();
        int i = 1;
        for(Card c : currentHand)
        {
            System.out.println("Card " + i + ":");
            System.out.println("Rank: "+c.GetRank());
            System.out.println("Suit: "+c.GetSuit());
            i++;
        }
        System.out.println("Discard Pile Has "+discardpile.size()+" Cards!");
        if (discardpile.GetRequiredRank() == Rank.EIGHT)//No need to print out "You Must Play A Eight Or A (Some Rank) Or An Eight!". That's weird.
            System.out.println("You Must Play A "+discardpile.GetRequiredRank()+" Or A "+discardpile.GetRequiredSuit()+"!");
        else
            System.out.println("You Must Play A "+discardpile.GetRequiredRank()+" Or A "+discardpile.GetRequiredSuit()+" Or An EIGHT!");
        System.out.println("Which Card Do You Want To Play? (Specify Using Card #. Type -1 For Draw)");
        boolean done = false;
        while(!done)
        {
            try
            {
                int reqCard = in.nextInt();
                if(reqCard == -1)
                {
                    currentplayer.PullFromDeck(gamedeck);
                    done = true;
                    continue;
                }
                currentplayer.PlayCard(currentHand.get(reqCard-1), discardpile);
                done = true;
            }catch(Exception e)
            {
                System.out.println(e);
                in.nextLine();//Consume the remaining invalid tokens to prevent an infinite error loop
            }
        }
    }
    
    public void displayAI()
    {
        System.out.println("^This is AI^");
        System.out.println("AI has "+currentplayer.GetCardQuantity()+" Cards!");
        
        ArrayList<Card> currentHand = currentplayer.GetContents();
        //no need to show AIs hand, that's cheating! shame on u! lol
        //for(Card c : currentHand)
        //{
          //  System.out.println("Rank: "+c.GetRank()+" Suit: "+c.GetSuit());
        //} 
        System.out.println("Discard Pile Has "+discardpile.size()+" Cards!");
        System.out.println("Deck has "+gamedeck.GetContents().size()+" cards!");
        if (discardpile.GetRequiredRank() == Rank.EIGHT)
            System.out.println("AI Must Play A "+discardpile.GetRequiredRank()+" Or A "+discardpile.GetRequiredSuit()+"!");
        else
            System.out.println("AI Must Play A "+discardpile.GetRequiredRank()+" Or A "+discardpile.GetRequiredSuit()+" Or An EIGHT!");  
        boolean done = false;
        while(!done)
        {
            try
            {
                for(int i=0; i < currentplayer.GetCardQuantity(); i++) //go through all cards in hand
                    {
                        Card c = currentHand.get(i);//get card values
                     if(currentplayer.PlayAICard(currentHand.get(i), discardpile) == false)//if card doesnt match = false
                        { 
                            if(i+1 >= currentplayer.GetCardQuantity())//if all cards don't match = pull
                           {
                            System.out.println("The AI has pulled a card from deck!");
                            currentplayer.PullFromDeck(gamedeck);
                            done = true;
                                break;
                            }                             
                           continue; //keep going till its "true" or no match have been found!                           
                            }                     
                     else if(currentplayer.PlayAICard(currentHand.get(i), discardpile) == true)//if card match = true
                        {   
                            System.out.println("The AI Played -> Rank: "+c.GetRank()+" Suit: "+c.GetSuit());
                            currentplayer.PlayCard(currentHand.get(i), discardpile);//plays the first "true" matching cards
                            done = true;
                                break;
                         }                                          
                    } 
            }catch(Exception e)
            {
                System.out.println(e);
            }
        }
    }
}
