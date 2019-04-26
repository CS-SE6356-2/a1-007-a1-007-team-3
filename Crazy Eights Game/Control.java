/*
 * CS 3354 (Software Engineering) Semester Project
 * Spring 2019
 * Team 3
 * Dr. Marcus
 * Our semester project is the card game Crazy Eights implemented in Java.
 */
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import java.util.*;
import java.awt.event.*;

/**
 * This class is the game engine. It takes care of all game logic.
 *
 * @author Matthew Wethington, Martin Boerwinkle, Jonathan Guidry, Yoseph Wordofa
 *
 */
public class Control
{
    private static Scanner in;
    private static DiscardPile discardpile;
    private static Deck gamedeck;
    public static Player players[];
    private static int numplayers;
    
    public static void main(String args[])
    {
        in = new Scanner(System.in);
        int done = 0;//Flag to tell us when to terminate the program

        //Here comes the initial selection
        System.out.println("What Type Of Interface Would You Like To Use To Play The Game?");
        System.out.println("1. Text Based");
        System.out.println("2. Graphical");
        System.out.println("Specify Selection By Entering In 1 Or 2.");
        int startresponse = 0;//Graphical mode flag
        boolean validresponse = false;
        int playermode = 0;//Player mode flag
        
        JFrame frame = new JFrame("JOptionPane showMessageDialog example");

        while (!validresponse)//Get the input for the interface type we want to run the game with
        {
            try
            {
                startresponse = in.nextInt();
                if (startresponse > 2 || startresponse < 1)
                    System.out.println("Invalid Response. Please Try Again.");
                else
                    validresponse = true;
            }
            catch (Exception e)
            {
                System.out.println(e);
                in.nextLine();//Consume the remaining invalid tokens to prevent an infinite error loop
            }
        }
        
        validresponse = false;//Reset this variable
        System.out.println("Would You Like To Play Singleplayer Against AI Or Multiplayer Against Other Human Players?");
        System.out.println("1. Singleplayer Against AI");
        System.out.println("2. Multiplayer Against Human Players");
        System.out.println("Specify Selection By Entering In 1 Or 2.");
        while (!validresponse)//Get the input for the player mode (whether we want to play singleplayer or multiplayer)
        {
            try
            {
                playermode = in.nextInt();
                if (playermode > 2 || playermode < 1)
                    System.out.println("Invalid Response. Please Try Again.");
                else
                    validresponse = true;
            }
            catch (Exception e)
            {
                System.out.println(e);
                in.nextLine();//Consume the remaining invalid tokens to prevent an infinite error loop
            }
        }

        switch (startresponse)//Start the game in the specified mode
        {
            //Initialize in text based mode
            case 1:
                switch (playermode)
                {
                    case 1://Singleplayer with text based interface
                        while (done == 0)
                        {
                            prepareNewGame();
                            
                            //get randome player from the selected amount of players so the user can always be random
                            Random rand = new Random();
                            int n = rand.nextInt(numplayers);
                            JOptionPane.showMessageDialog(frame,"****** Singleplayer Game Has Been Launched! *****");
                            JOptionPane.showMessageDialog(frame,"****** You are player: #"+(n+1)+ " *****");
                            
                            UserInterface UI = new UserInterface(gamedeck, discardpile, in);
                            
                            //The game cycles through each players turn until someone wins
                            int playerIdx = 0;
                            int winner = -1;
                            while (winner == -1)
                            {
                                //This if statment is to control the user. Comment it out if you want AI vs AI.
                                if (playerIdx == n)
                                {
                                    System.out.println("Launching UI For Player " + (playerIdx + 1) + "!");//Player number is their index + 1
                                    UI.SetCurrentUserPlayer(players[playerIdx]);//Player n gets to go
                                    if(players[playerIdx].GetCardQuantity() == 0)
                                    {
                                        winner = playerIdx;
                                        continue;//Skip over all other statements in the loop since this player won
                                    }
                                    playerIdx = (playerIdx+1)%numplayers;
                                }
                        
                                System.out.println("Launching UI For Player " + (playerIdx + 1) + "!");//Player number is their index + 1
                                UI.SetCurrentAIPlayer(players[playerIdx]);//Player 1 gets to start the game
                                if (players[playerIdx].GetCardQuantity() == 0)
                                    winner = playerIdx;
                                playerIdx = (playerIdx + 1) % numplayers;
                            }
                            
                            if(winner != n)
                            {
                                System.out.println("****************You Lost!*******************");               
                                System.out.println("Winner is player "+ (winner + 1));
                                done = checkDone();
                            }
                            else
                            {
                                System.out.println("****************YOU WON!*******************");
                                done = checkDone();
                            }
                        }
                        break;
                    case 2://Multiplayer with text based interface
                        while (done == 0)
                        {
                            prepareNewGame();
                                               
                            JOptionPane.showMessageDialog(frame,"****** Multiplayer Game Has Been Launched! *****");
                            
                            UserInterface UI = new UserInterface(gamedeck, discardpile, in);
                            
                            //The game cycles through each players turn until someone wins
                            int playerIdx = 0;
                            int winner = -1;
                            while (winner == -1)
                            {
                                System.out.println("Launching UI For Player " + (playerIdx + 1) + "!");//Player number is their index + 1
                                UI.SetCurrentUserPlayer(players[playerIdx]);//Player 1 gets to start the game
                                if (players[playerIdx].GetCardQuantity() == 0)
                                {
                                    winner = playerIdx;
                                    continue;//Skip over all other statements in the loop since this player won
                                }
                                playerIdx = (playerIdx + 1) % numplayers;
                            }
                                      
                            System.out.println("Winner is player "+ (winner + 1));
                            done = checkDone();
                        }
                        break;
                }
                
                System.out.println("Thanks For Playing!");
                break;
            
            //Initialize in GUI mode
            case 2:
                switch (playermode)
                {
                    case 1://Singleplayer with graphical interface
                        while (done == 0)
                        {    
                            prepareNewGame();
                            System.out.println("Game Will Open In A New Window.");
                            GUI game = new GUI(gamedeck, discardpile);
                            game.setVisible(true);

                            //left this in because i'm not sure if removing it will break anything
                            for (int i = 0; i < 7; i++)
                            {
                                game.addCard(80 + i*80 + i*10, 400, 80, 120);
                                game.cards.get(i).setText("Card " + (i+1));
                                final int temp = i;//Solves stupid local variables referenced from an inner class must be final or effectively final error
                                game.cards.get(i).addActionListener(new ActionListener(){public void actionPerformed(ActionEvent e){game.removeCard(temp);}});
                            }

                            //The game cycles through each players turn until someone wins
                            //***Because there is nothing that makes the program stop and wait for the user, right now it will loop forever
                            int playerIdx = 0;
                            int winner = -1;    
                            while (winner == -1)
                            {
                                game.setCurrentPlayer(playerIdx);//Player 1 gets to start the game
                                if (players[playerIdx].GetCardQuantity() == 0)
                                {
                                    winner = playerIdx;
                                    continue;//Skip over all other statements in the loop since this player won
                                }
                                playerIdx = (playerIdx + 1) % numplayers;
                            }
                            System.out.println("Winner Is Player " + (winner + 1));
                            done = checkDone();
                        }
                        break;
                    case 2://Multiplayer with graphical interface
                        //Nothing here yet. Graphical interface and networking need to be finished before we can do anything here.
                        break;
                }
                
                System.out.println("Thanks For Playing!");
                break;
                
            //Should not get here. If we do, something went terribly wrong.
            default:
                System.out.println("Critical Error. Terminating Program.");
                break;
        }
        //The program keeps getting stuck here after players wish to stop playing, and I don't know why since there aren't any further statements here.
        //As such, I'm forcing the program to terminate sucessfully. Boom! Problem solved! Take that you stupid hanging program!
        System.exit(0);
    }
    
    //Sets up everything required to start a new game
    public static void prepareNewGame()
    {
        //Get the number of players playing the game
        while (numplayers < 2)
        {
            System.out.println("How Many Players Will Be Playing Crazy Eights For This Session?");
            numplayers = in.nextInt();
            in.nextLine();//Set the read pointer to the next line and throw away the rest of the input that wasn't a number.

            if (numplayers <= 1)
                System.out.println("Invalid Number Of Players. Number Of Players Must Be In Range [2, 7].");
            else if (numplayers > 7)
                System.out.println("Too Many Players. Maximum Allowed Is 7.");
        }
        //Construct the list of players.
        players = new Player[numplayers];
        for (int i = 0; i < numplayers; i++)
            players[i] = new Player();

        prepareDeck();
        discardpile.InsertCard(gamedeck.GetTopCard()); //Prime the discard pile
        gamedeck.Deal(players);

        System.out.println("Starting Game.");
    }
    
    //Creates a new deck and shuffles it
    public static void prepareDeck()
    {
        System.out.println("Constructing Deck And Discard Pile.");
        //Declare and initialize the deck and discard pile
        discardpile = new DiscardPile();
        gamedeck = new Deck(discardpile);
        //Populate the deck with 52 cards, the standard deck size.
        for (Rank r : Rank.values())
            for (Suit s : Suit.values())
                gamedeck.InsertCard(new Card(s, r));
        gamedeck.Shuffle();      
    }
    
    //Prompts the user if they would like to start another game
    public static int checkDone()
    {
        in.nextLine();//Clear the input buffer of any remaining crap before we get the user's repsonse
        System.out.println("Want To Play Another Game Session? (Respond Using Y For Yes Or N For No)");
        while(true) //probably a better way of doing this, but it shouldnt get stuck
        {
            String response = in.nextLine();//Make response not case sensitive
            switch (response)
            {
                case "N":
                    return 1;
                case "n":
                    return 1;
                case "Y":
                    return 0;
                case "y":
                    return 0;
                default:
                    System.out.println("Invalid Response. Please Try Again. (Respond Using Y For Yes Or N For No)");
            }
        }
    }
}
