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
    
    public void TransferToDeck(Deck deck)//Transfer all but the last card played back to the deck
    {
        Card lastcardplayed = cards.pop();//The last card played is the top card on the discard pile
        for (Card c : cards)
        {
            deck.InsertCard(cards.pop());
        }
        cards.push(lastcardplayed);
    }
}
