import java.io.*;
import java.net.*;
import java.util.*;
public class Network{
    Socket clientSocket;
    DataOutputStream outToServer;
    BufferedReader inFromServer;
    public Network(String IPStr)
    {
        constructorHelper(IPStr);
    }
    public Network(Scanner in)
    {
        System.out.println("IP: ");
        constructorHelper(in.nextLine());
    }
    private void constructorHelper(String IPStr)
    {
        try
        {
            clientSocket = new Socket(IPStr, 6677);
            outToServer = new DataOutputStream(clientSocket.getOutputStream());
            inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        }catch(Exception e){
            System.out.println("Server connection failed");
        }
    }

    public boolean processNext()
    {
        ArrayList<Card> cards = new ArrayList<Card>();
        Rank reqRank = Rank.ACE;
        Suit reqSuit = Suit.SPADE;
        try
        {
            int num = Integer.parseInt(inFromServer.readLine());
            for(int idx = 0; idx < num; idx++)
            {
                String rank = inFromServer.readLine();
                String suit = inFromServer.readLine();
                cards.add(new Card(Suit.valueOf(suit), Rank.valueOf(rank)));
            }
            reqRank = Rank.valueOf(inFromServer.readLine());
            reqSuit = Suit.valueOf(inFromServer.readLine());
        }catch(Exception e){
            System.out.println("Server read failed");
        }
        UserInterface.displayView(cards, reqRank, reqSuit);
        try
        {
        outToServer.writeBytes(UserInterface.respond(cards, reqRank, reqSuit)+"\n");
        }catch(Exception e){
            System.out.println("Failed to respond to server");
        }
        return true;
    }
}
