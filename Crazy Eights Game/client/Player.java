/*
 * CS 3354 (Software Engineering) Semester Project
 * Spring 2019
 * Team 3
 * Dr. Marcus
 * Our semester project is the card game Crazy Eights implemented in Java.
 */

import java.util.*;

/**
 * This class is for constructing players, one of the most fundamental units of any game.
 * The player will hold its cards. No need for an external data structure to do this as that will add performance overhead.
 * @author Matthew Wethington, Martin Boerwinkle, Jonathan Guidry, Yoseph Wordofa
 */
public class Player
{
    private int numcards;
    private int score;
    private ArrayList<Card> cards;
    public static int didsomething;
    //These variables are used by Yoseph's AI functions
    private int faults = -1;
    private int if_only_Eight = 0;    
    private int any_But_Eight = 1;
    private int any = 2;
    private int not_Eight = 3;
    private int ok = 4;
    
    public Player()
    {
        numcards = 0;
        score = 0;
        cards = new ArrayList<Card>();
    }
    
    public void PullFromDeck(Deck deck)
    {
        cards.add(deck.GetTopCard());
        numcards = cards.size();
    }
    
    public void PlayCard(Card card, DiscardPile target) throws Exception
    {
        if(card.GetRank() == target.GetRequiredRank() || card.GetSuit() == target.GetRequiredSuit() || card.GetRank() == Rank.EIGHT)
        {
            cards.remove(card);
            numcards = cards.size();
            target.InsertCard(card);
        }
        else if(!cards.contains(card))
        {
            throw new Exception("Card Not In Hand!");
        }
        else
        {
            throw new Exception("Unplayable Card");
        }
    }
    
    int PlayAICard(Card card, DiscardPile target)
    {
        if(card.GetRank() == Rank.EIGHT)
            {return if_only_Eight;}//true only when eight is in hand. make it skip and do the bottom condition
        
        if(card.GetRank() != Rank.EIGHT)
            {return not_Eight;} 
            
            return ok; 
    } 
    
    int PlayAICard2(Card card, DiscardPile target)
    {       
        if (card.GetRank() == target.GetRequiredRank() || card.GetSuit() == target.GetRequiredSuit())
           {return any;}//any valid card would do
           
        if(card.GetRank() != target.GetRequiredRank() && card.GetSuit() != target.GetRequiredSuit() && card.GetRank() != Rank.EIGHT)
            return faults; //false-1 non valid pull
            
            return ok; 
    }
    
    int PlayAICard3(Card card, DiscardPile target)
    {       
        if((card.GetRank() == target.GetRequiredRank() && card.GetRank() != Rank.EIGHT) && card.GetSuit() == target.GetRequiredSuit())
            return any_But_Eight;//play non-eight hand
        if((card.GetRank() != target.GetRequiredRank() && card.GetRank() != Rank.EIGHT) && card.GetSuit() == target.GetRequiredSuit())
            return any_But_Eight;//play non-eight hand 
        if(card.GetRank() == target.GetRequiredRank() && card.GetRank() != Rank.EIGHT && card.GetSuit() != target.GetRequiredSuit())
            return any_But_Eight;//play non-eight hand
            
            return ok; 
    }
    
    public ArrayList<Card> GetContents()
    {
        return new ArrayList<Card>(cards);
    }
    
    public int GetScore()
    {
        return score;
    }
    
    public int GetCardQuantity()
    {
        return numcards;
    }
}
