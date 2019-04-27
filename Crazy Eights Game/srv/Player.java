/*
 * CS 3354 (Software Engineering) Semester Project
 * Spring 2019
 * Team 3
 * Dr. Marcus
 * Our semester project is the card game Crazy Eights implemented in Java.
 */

import java.util.*;
import java.io.*;
import java.net.*;

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
    Socket connectionSocket;
    BufferedReader inFromClient;
    DataOutputStream outToClient;
    
    public Player(Socket in) throws Exception
    {
        connectionSocket = in;
        inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
        outToClient = new DataOutputStream(connectionSocket.getOutputStream());
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
