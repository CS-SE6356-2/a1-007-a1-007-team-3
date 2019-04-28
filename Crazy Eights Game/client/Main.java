import java.util.*;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
public class Main
{
    private static DiscardPile discardpile;
    private static Deck gamedeck;
    public static Player players[];
    private static int numplayers;
    
    public static void main(String args[])
    {
        Scanner in = new Scanner(System.in);
        int done = 0;//Flag to tell us when to terminate the program
        UserInterface.in = in;
        Network n;
        
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
        
        switch (startresponse)
        {
            case 1://Start in text based graphics mode
                switch(playermode)
                {
                    case 1://Initialize local singleplayer
                        while (done == 0)
                        {
                            prepareNewGame(in);
                            
                            //get random player from the selected amount of players so the user can always be random
                            Random rand = new Random();
                            int p = rand.nextInt(numplayers);
                            JOptionPane.showMessageDialog(frame,"****** Singleplayer Game Has Been Launched! *****");
                            JOptionPane.showMessageDialog(frame,"****** You are player: #"+(p+1)+ " *****");
                            
                            UserInterface UI = new UserInterface(gamedeck, discardpile);
                            
                            //The game cycles through each players turn until someone wins
                            int playerIdx = 0;
                            int winner = -1;
                            while (winner == -1)
                            {
                                //This if statment is to control the user. Comment it out if you want AI vs AI.
                                if (playerIdx == p)
                                {
                                    System.out.println("Launching UI For Player " + (playerIdx + 1) + "!");//Player number is their index + 1
                                    UI.SetCurrentUserPlayer(players[playerIdx]);//Player p gets to go
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
                            
                            if(winner != p)
                            {
                                System.out.println("****************You Lost!*******************");               
                                System.out.println("Winner is player "+ (winner + 1));
                                done = checkDone(in);
                            }
                            else
                            {
                                System.out.println("****************YOU WON!*******************");
                                done = checkDone(in);
                            }
                        }
                        break;
                    
                    case 2://Initialize networked multiplayer
                        in.nextLine();//Clear the input buffer of any crap before entering in the IP address
                        n = new Network(in);
                        JOptionPane.showMessageDialog(frame,"****** Connection Successful! Multiplayer Game Has Been Launched! *****");
                        System.out.println("Waiting On Other Players...");
                        n.processServerData();
                        break;
                    
                    default://Should not get here. If we do, something went terribly wrong.
                        System.out.println("Critical Error. Terminating Program Now.");
                        break;
                }
                break;
            
            case 2://Start in GUI mode
                switch(playermode)
                {   
                    case 1://Initialize local singleplayer
                        while (done == 0)
                        {
                            prepareNewGame(in);
                            
                            //get random player from the selected amount of players so the user can always be random
                            Random rand = new Random();
                            int p = rand.nextInt(numplayers);
                            JOptionPane.showMessageDialog(frame,"****** Singleplayer Game Has Been Launched! *****");
                            JOptionPane.showMessageDialog(frame,"****** You are player: #"+(p+1)+ " *****");
                            
                            GUI gamedisplay = new GUI(gamedeck, discardpile, numplayers);
                            
                            //The game cycles through each players turn until someone wins
                            int playerIdx = 0;
                            int winner = -1;
                            while (winner == -1)
                            {
                                //This if statment is to control the user. Comment it out if you want AI vs AI.
                                if (playerIdx == p)
                                {
                                    //System.out.println("Launching UI For Player " + (playerIdx + 1) + "!");//Player number is their index + 1
                                    gamedisplay.setCurrentUserPlayer(players[playerIdx], playerIdx);//Player p gets to go
                                    if(players[playerIdx].GetCardQuantity() == 0)
                                    {
                                        winner = playerIdx;
                                        continue;//Skip over all other statements in the loop since this player won
                                    }
                                    playerIdx = (playerIdx+1)%numplayers;
                                }
                        
                                gamedisplay.setCurrentAIPlayer(players[playerIdx]);//Player 1 gets to start the game
                                if (players[playerIdx].GetCardQuantity() == 0)
                                    winner = playerIdx;
                                playerIdx = (playerIdx + 1) % numplayers;
                            }
                            
                            if(winner != p)
                            {
                                System.out.println("****************You Lost!*******************");               
                                System.out.println("Winner is player "+ (winner + 1));
                                done = checkDone(in);
                            }
                            else
                            {
                                System.out.println("****************YOU WON!*******************");
                                done = checkDone(in);
                            }
                        }
                        break;
                    
                    case 2://Initialize networked multiplayer
                        in.nextLine();//Clear the input buffer of any crap before entering in the IP address
                        n = new Network(in);
                        //commented this out to avoid the error, will fix when fitting GUI to Network
                        //n.processServerDataGraphical(gamedisplay);
                        break;
                    
                    default://Should not get here. If we do, something went terribly wrong.
                        System.out.println("Critical Error. Terminating Program Now.");
                        break;
                }
                break;
            
            default://Should not get here. If we do, something went terribly wrong.
                System.out.println("Critical Error. Terminating Program Now.");
                break;
        }
        System.out.println("Thanks For Playing!");
        //The program keeps getting stuck here after players wish to stop playing, and I don't know why since there aren't any further statements here.
        //As such, I'm forcing the program to terminate sucessfully. Boom! Problem solved! Take that you stupid hanging program!
        System.exit(0);
    }
    
    //Sets up everything required to start a new game
    public static void prepareNewGame(Scanner in)
    {
        //Get the number of players playing the game
        while (numplayers < 2 || numplayers > 7)
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
    public static int checkDone(Scanner in)
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
