/*
 * CS 3354 (Software Engineering) Semester Project
 * Spring 2019
 * Team 3
 * Dr. Marcus
 * Our semester project is the card game Crazy Eights implemented in Java.
 */

import java.util.*;

/**
 * This class holds cards.
 * @author Matthew Wethington, Martin Boerwinkle, Jonathan Guidry, Yoseph Wordofa
 */
public class Deck
{
    Stack<Card> cards;
    
    public Deck()
    {
        cards = new Stack<Card>();
    }
    
    public void Shuffle()
    {
        System.out.println("Preshuffle size " + cards.size());
        for(int iter = 0; iter < 100; iter++)
        {
            Stack<Card> A = new Stack<Card>();
            Stack<Card> B = new Stack<Card>();
            while(cards.size() > 0)
            {
                if(Math.random() < 0.5)
                {
                    A.push(cards.pop());
                }else
                {
                    B.push(cards.pop());
                }
            }
            cards.addAll(A);
            cards.addAll(B);
        }
        System.out.println("Postshuffle size " + cards.size());
    }
    
    public void InsertCard(Card card)//Insert a card onto the top of the deck
    {
        cards.push(card);
    }
    
    public Card GetTopCard()//Get the top card from the deck
    {
        return cards.pop();
    }
    
    public ArrayList<Card> GetContents()//Return the contents of the deck in list format
    {
        Stack<Card> temp = (Stack<Card>)cards.clone();
        ArrayList<Card> returnthis = new ArrayList<Card>();
        
       while(!temp.empty())//Get the cards from the deck, destroying the copy in the process rather than the original.
        {
            returnthis.add(temp.pop());
        }
        
        return returnthis;
    }
    
    public void Deal(Player playerlist[])//Deal the cards to players
    {
        int cardQuantity = (playerlist.length > 2) ? 5 : 7;
        for(Player p : playerlist)
        {
            for(int x = 0; x < cardQuantity; x++)
            {
                p.PullFromDeck(this);
            }
        }
    }
}
