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
    
    public DiscardPile()
    {
        cards = new Stack<Card>();
    }
    
    public void InsertCard(Card card)//Insert a card onto the top of the deck
    {
        cards.push(card);
    }
    
    public Card GetTopCard()//Get the top card from the deck
    {
        return cards.pop();
    }
    
    public void TransferToDeck(Deck deck)//Transfer the entire discard pile back to the specified deck
    {
        while(!cards.empty())
        {
            deck.InsertCard(cards.pop());
        }
    }
}
