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
    public static Scanner in;
    public static void displayView(ArrayList<Card> cards, Rank reqRank, Suit reqSuit)
    {
        System.out.println("Required rank: "+reqRank);
        System.out.println("Required suit: "+reqSuit);
        for(int idx = 0; idx < cards.size(); idx++){
            System.out.println(idx+" "+cards.get(idx).GetRank()+" "+cards.get(idx).GetSuit());
        }
    }
    public static int respond(ArrayList<Card> cards, Rank reqRank, Suit reqSuit)
    {
        boolean good = false;
        int ret = 0;
        while(!good)
        {
            System.out.print("Play which card: ");
            ret = in.nextInt();
            if(ret < -1 || ret >= cards.size())
            {
                System.out.println("Card not valid");
                continue;
            }
            if(ret == -1)
            {
                good = true;
                break;
            }
            Card c = cards.get(ret);
            if(c.GetRank() == Rank.EIGHT || c.GetRank() == reqRank || c.GetSuit() == reqSuit)
            {
                good = true;
            }else
            {
                System.out.println("Card not valid");
            }
        }
        return ret;
    }
}
