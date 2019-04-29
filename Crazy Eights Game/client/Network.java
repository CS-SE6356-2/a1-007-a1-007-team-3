import java.io.*;
import java.net.*;
import java.util.*;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
public class Network
{
    Socket clientSocket;
    DataOutputStream outToServer;
    BufferedReader inFromServer;
    JFrame messages = new JFrame("JOptionPane showMessageDialog example");
    
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

    public void processServerData()throws Exception
    {
        Rank reqRank = Rank.ACE;
        Suit reqSuit = Suit.SPADE;
        int whatever = Integer.parseInt(inFromServer.readLine());//Be very apathetic and completely disregard the first number the server sends you
        
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
    
    public void processServerDataGraphical()throws Exception
    {
        Rank reqRank = Rank.ACE;
        Suit reqSuit = Suit.SPADE;
        int numplayers = Integer.parseInt(inFromServer.readLine());//The first number the server sends clients is the number of players there will be
        GUI gui = new GUI(numplayers);
        JOptionPane.showMessageDialog(messages,"Waiting On Other Players...");
        
        while (true)//The program will continue executing until the user exits
        {
            ArrayList<Card> cards = new ArrayList<Card>();
            try
            {
                int num = Integer.parseInt(inFromServer.readLine());
            
                if (num == 0)//If the size of that player's deck is 0, the server tells them they won
                {
                    JOptionPane.showMessageDialog(messages,"You Won!");
                    continue;//Skip the other statements in this iteration. The player won, so no need to read in any more card data until the next game.
                }
                else if (num == -1)
                {
                    int winner = Integer.parseInt(inFromServer.readLine());//The server will announce the winner to losing players
                    JOptionPane.showMessageDialog(messages,"Player " + winner + " Won The Game!");
                    continue;
                }
                else if (num == -2)//The server signaled a shutdown
                {
                    //The server signals clients to terminate if the server itself is told to terminate.
                    //This is to avoid weird bugs when the server closes but the clients are left running.
                    JOptionPane.showMessageDialog(messages,"Server Initialized Shutdown.");
                    JOptionPane.showMessageDialog(messages,"Thanks For Playing!");
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
            gui.displayNetwork(cards, reqRank, reqSuit);
            try
            {
                outToServer.writeBytes(gui.respond()+"\n");
            }catch(Exception e){
                System.out.println("Failed To Respond To Server");
            }
            JOptionPane.showMessageDialog(messages,"Waiting On Other Players...");
        }
    }
}
