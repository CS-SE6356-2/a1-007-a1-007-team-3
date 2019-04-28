import java.io.*;
import java.net.*;
import java.util.*;
public class Network
{
    Socket clientSocket;
    DataOutputStream outToServer;
    BufferedReader inFromServer;
    
    public Network(String IPStr)
    {
        constructorHelper(IPStr);
    }
    
    public Network(Scanner in)
    {
        System.out.println("IP Of Server To Join: ");
        constructorHelper(in);
    }
    
    private void constructorHelper(String IPStr)
    {
        for (int i = 0; i < 5; i++)//Try to connect to the server 5 times. If we still fail after 5 times, terminate the program.
        {
            try
            {
                clientSocket = new Socket(IPStr, 6677);
                outToServer = new DataOutputStream(clientSocket.getOutputStream());
                inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            }catch(Exception e){
                System.out.println("Server Connection Failed.");
            }
        }
        System.exit(0);//Close the program
    }
    
    private void constructorHelper(Scanner in)
    {
        boolean NotConnected = true;
        
        while (NotConnected)
        {
            try
            {
                clientSocket = new Socket(in.nextLine(), 6677);
                outToServer = new DataOutputStream(clientSocket.getOutputStream());
                inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                NotConnected = false;//Should only get to this statement if the connection succeeded.
            }catch(Exception e){
                System.out.println("Server Connection Failed.");
            }
        }
    }

    public void processServerData()
    {
        Rank reqRank = Rank.ACE;
        Suit reqSuit = Suit.SPADE;
        
        while (true)//The program will continue executing until the user exits
        {
            ArrayList<Card> cards = new ArrayList<Card>();
            try
            {
                int num = Integer.parseInt(inFromServer.readLine());
                
                if (num <= 0)//Do 1 check instead of 3 to check for a special signal from the server
                    switch (num)
                    {
                        case 0://The playwer won if it gets this signal
                            System.out.println("You Won!");
                            System.out.println("Waiting On Server...");
                            continue;//Skip the other statements in this iteration. The player won, so no need to read in any more card data until the next game.
                        case -1://The player lost if it gets this signal
                            int winner = Integer.parseInt(inFromServer.readLine());//The server will announce the winner to losing players
                            System.out.println("Player " + winner + " Won The Game!");
                            System.out.println("Waiting On Server...");
                            continue;
                        case -2://The server signaled a shutdown
                            //The server signals clients to terminate if the server itself is told to terminate.
                            //This is to avoid weird bugs when the server closes but the clients are left running.
                            System.out.println("Server Initiated Shutdown.");
                            System.out.println("Thanks For Playing!");
                            System.exit(0);//Terminate the program
                    }
            
                for(int idx = 0; idx < num; idx++)
                {
                    String rank = inFromServer.readLine();
                    String suit = inFromServer.readLine();
                    cards.add(new Card(Suit.valueOf(suit), Rank.valueOf(rank)));
                }
                reqRank = Rank.valueOf(inFromServer.readLine());
                reqSuit = Suit.valueOf(inFromServer.readLine());
            }catch(Exception e){
                System.out.println("Server Read Failed.");
            }
            UserInterface.displayNetwork(cards, reqRank, reqSuit);
            try
            {
                outToServer.writeBytes(UserInterface.respond(cards, reqRank, reqSuit)+"\n");
            }catch(Exception e){
                System.out.println("Failed To Respond To Server");
            }
            System.out.println("Waiting On Other Players...");
        }
    }
    
    public void processServerDataGraphical(GUI display)
    {
        ArrayList<Card> cards = new ArrayList<Card>();
        Rank reqRank = Rank.ACE;
        Suit reqSuit = Suit.SPADE;
        
        while (true)//The program will continue executing until the user exits
        {
            try
            {
                int num = Integer.parseInt(inFromServer.readLine());
            
                if (num == 0)//If the size of that player's deck is 0, the server tells them they won
                {
                    System.out.println("You Won!");
                    continue;//Skip the other statements in this iteration. The player won, so no need to read in any more card data until the next game.
                }
                else if (num == -1)
                {
                    int winner = Integer.parseInt(inFromServer.readLine());//The server will announce the winner to losing players
                    System.out.println("Player " + winner + " Won The Game!");
                    continue;
                }
                else if (num == -2)//The server signaled a shutdown
                {
                    //The server signals clients to terminate if the server itself is told to terminate.
                    //This is to avoid weird bugs when the server closes but the clients are left running.
                    System.out.println("Server Initialized Shutdown.");
                    System.out.println("Thanks For Playing!");
                    System.exit(0);//Terminate the program
                }
            
                for(int idx = 0; idx < num; idx++)
                {
                    String rank = inFromServer.readLine();
                    String suit = inFromServer.readLine();
                    cards.add(new Card(Suit.valueOf(suit), Rank.valueOf(rank)));
                }
                reqRank = Rank.valueOf(inFromServer.readLine());
                reqSuit = Suit.valueOf(inFromServer.readLine());
            }catch(Exception e){
                System.out.println("Server Read Failed.");
            }
            display.updateGUI(cards, reqRank, reqSuit);
            try
            {
                outToServer.writeBytes(UserInterface.respond(cards, reqRank, reqSuit)+"\n");
            }catch(Exception e){
                System.out.println("Failed To Respond To Server");
            }
            System.out.println("Waiting On Other Players...");
        }
    }
}
