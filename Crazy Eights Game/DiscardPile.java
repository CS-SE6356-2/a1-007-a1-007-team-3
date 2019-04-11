/*
 * CS 3354 (Software Engineering) Semester Project
 * Spring 2019
 * Team 3
 * Dr. Marcus
 * Our semester project is the card game Crazy Eights implemented in Java.
 */

import java.util.*;

/**
 * This class holds discarded cards.
 * @author Matthew Wethington, Martin Boerwinkle, Jonathan Guidry, Yoseph Wordofa
 */
public class DiscardPile
{
    Stack<Card> cards;
    Suit requiredSuit;
    Rank requiredRank;

    public int size()
    {
        return cards.size();
    }

    public Rank GetRequiredRank()
    {
        return requiredRank;
    }

    public Suit GetRequiredSuit()
    {
        return requiredSuit;
    }
    
    public DiscardPile()
    {
        cards = new Stack<Card>();
    }
    
    public void InsertCard(Card card)//Insert a card onto the top of the deck
    {
        cards.push(card);
        requiredRank = card.GetRank();
        requiredSuit = card.GetSuit();
    }
    
    /*public Card GetTopCard()//Get the top card from the deck
    {
        return cards.pop();
    }*///We should never remove the top card from the discard pile
    
    public void TransferToDeck(Deck deck)//Transfer all but one of the discard pile back to the deck
    {
        while(cards.size() > 1)
        {
            deck.InsertCard(cards.pop());
        }
    }
}
