/*
 * CS 3354 (Software Engineering) Semester Project
 * Spring 2019
 * Team 3
 * Dr. Marcus
 * Our semester project is the card game Crazy Eights implemented in Java.
 */
import java.awt.*;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.FileInputStream;
import javax.imageio.ImageIO;
import javax.swing.JPanel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;
import java.util.*;
/**
 * This class takes care of all player interactions with the game through a GUI.
 * @author Matthew Wethington, Martin Boerwinkle, Jonathan Guidry, Yoseph Wordofa
 */
public class GUI extends JFrame {

    private DiscardPile discardPile;
    private Deck gameDeck;
    private int numPlayers;
    private Player currentPlayer;
    public ArrayList<JLabel> cards, opponents;
    
    private BufferedImage sourceDeck;
    private BufferedImage faceDown;
    private String imgName = "deck.png";   //file name of whole deck image
    private double dimX = 167.61538, dimY = 243.2; //Dimensions of a single card from source
    private int scaleX = 80, scaleY = 120; //Dimensions of a single card after being scaled
    private int backX = 2, backY = 4;   //position of the face down card on deck sheet
    private JTextField output;
    public JPanel contentPane;
    private JLabel drawPile, discard, card;
    private JLayeredPane handSpace;
    

    public GUI(Deck gameDeck, DiscardPile discardPile, int numPlayers)
    {
        this.gameDeck = gameDeck;
        this.discardPile = discardPile;
        this.numPlayers = numPlayers;
        contentPane = new JPanel(null);
        cards = new ArrayList<>();
        
        try {
            //Source image needs to be in root directory in order to be found
            sourceDeck = ImageIO.read(new FileInputStream(imgName));
        } catch (IOException ex){
            System.err.println("Caught IOException: " +  ex.getMessage());
        }
        
        //overridable method calls?
        initGUI();
        initTable();
    }
        
    //Constructor 
    public void initGUI(){

        //Creates the display window
        this.setTitle("Crazy Eights");
        this.setSize(800,600);
        contentPane.setPreferredSize(new Dimension(800,600));
        contentPane.setBackground(new Color(0,101,61));
        
        //Creates the text output box
        output = new JTextField();
        output.setBounds(5,560,512,36);
        output.setBackground(new Color(255,255,255));
        output.setEnabled(true);
        output.setEditable(false);
        output.setFont(new Font("sansserif",0,12));
        output.setText("Welcome to Crazy Eights!");
        
        //initialize deck, discard pile, and layered panel where the hand is contained
        //user needs to be able to click on deck, but the deck label needs to only be created
        BufferedImage deck = sourceDeck;
        faceDown = cropImage(deck, backX, backY);
        drawPile = new JLabel(new ImageIcon(faceDown));
        drawPile.setBounds(260, 240, scaleX, scaleY);
        discard = new JLabel(new ImageIcon(faceDown));
        discard.setBounds(460, 240, scaleX, scaleY);
        handSpace = new JLayeredPane();
        handSpace.setBounds(54, 430, 692, 120);
        
        //adding components to contentPane panel
        contentPane.add(output);
        contentPane.add(drawPile);
        contentPane.add(discard);
        contentPane.add(handSpace);
        
        //adding panel to JFrame and setting of window position and close operation
        this.add(contentPane);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.pack();
        this.setVisible(true);
        this.setResizable(false);
    }
        
    public void setCurrentUserPlayer(Player p, int index)
    {
        currentPlayer = p;
        ArrayList<Card> currentHand = currentPlayer.GetContents();
        updateOpponentHandCount(index);
        
        //Clears the previous hand off the screen and resets the cards array list
        for (JLabel i : cards)
            handSpace.remove(i);
        this.repaint();
        cards.clear();
        cards.trimToSize();
        
        //From the contents of the current players hand, build the new hand, calling addCard() to print it
        int posX, posY, j = 0, handSize = currentPlayer.GetCardQuantity();
        for (Card i : currentHand)
        {
            //ordinal() gets the index of enumerators
            posX = i.GetRank().ordinal();
            posY = i.GetSuit().ordinal();
            addCard(posX, posY, handSize, j);
            j++;
        }
        
        output.setText("It is now your turn...");
        waitThreeSeconds();
        updateDiscard(true);
        
        int i = 0;
        boolean done = false;
        while (!done){
            
            /*//placeholder to stop the game, remove this when actual interaction is implemented
            if (i == 0){
                System.out.println("program is stopped");
                i++;
            }*/
            
            /*
            try
            {
                //int reqCard = in.nextInt();
                if(reqCard == -1) //if click draw pile
                {
                    currentPlayer.PullFromDeck(gameDeck);
                    done = true;
                    continue;
                }
                //e;se if click card in hand
                currentPlayer.PlayCard(currentHand.get(reqCard-1), discardPile);
                done = true;
            }
            catch(Exception e){
                System.out.println(e);
                in.nextLine();//Consume the remaining invalid tokens to prevent an infinite error loop
            }*/
            
            
        }
    }
    
    //Adds a card to the screen
    public void addCard(int posX, int posY, int handSize, int index)
    {
        //The amount of distance between cards is dependant on the number of cards in the hand
        int x;
        if (handSize <= 7)
            x = 96;
        else if (handSize <= 13)
            x = 48;
        else if (handSize <= 26)
            x = 24;
        else
            x = 12;
        
        //create the card label, then add it to the cards array list
        BufferedImage deck = sourceDeck;
        card = new JLabel(new ImageIcon(cropImage(deck, posX, posY)));
        card.setBounds((x * index), 0, scaleX, scaleY);
        
        //adds mouse listener that returns the index of the card
        
        
        //add to a layered pane so that the cards layer on top of each other
        cards.add(card);
        handSpace.add(cards.get(index), -index);
        cards.get(index).setVisible(true);
    }
    
    public void setCurrentAIPlayer (Player p)
    {
        currentPlayer = p;
        ArrayList<Card> currentHand = currentPlayer.GetContents();
        boolean done = false;
        while(!done)
        {
            try
            {
                for(int i=0; i < currentPlayer.GetCardQuantity(); i++) //go through all cards in hand
                {
                    Card c = currentHand.get(i);//get card values                        
                    if(currentPlayer.PlayAICard(currentHand.get(i), discardPile) == 0)//if rank match eight only skip that and test other cards condition
                    { 
                        int card_with_eight = 0; 
                        card_with_eight = card_with_eight + i;//save card with eight   
                        for(int j=0; j < currentPlayer.GetCardQuantity(); j++)
                        {
                            Card c2 = currentHand.get(j);
                            if(currentPlayer.PlayAICard3(currentHand.get(j), discardPile) != 1)
                            {                              
                                if (j+1 >= currentPlayer.GetCardQuantity())
                                {                            
                                    output.setText("The AI Played -> Rank: "+c.GetRank()+" Suit: "+c.GetSuit());
                                    currentPlayer.PlayCard(currentHand.get(card_with_eight), discardPile);//as last resort, play the eighth card
                                    updateDiscard(false);
                                    waitThreeSeconds();
                                    done = true;
                                    break;
                                }
                                continue;
                            }
                            if(currentPlayer.PlayAICard3(currentHand.get(j), discardPile) == 1)//play (any) card that isnt eight
                            {                                   
                                output.setText("The AI Played -> Rank: "+c2.GetRank()+" Suit: "+c2.GetSuit());
                                currentPlayer.PlayCard(currentHand.get(j), discardPile);//plays the first "true" matching cards
                                updateDiscard(false);
                                waitThreeSeconds();
                                done = true;
                                break;
                            }                            
                        }
                        done = true;
                        break;
                    }
                    if(currentPlayer.PlayAICard(currentHand.get(i), discardPile) == 3)//if rank isn't eight, any card will do 
                    {
                        if(currentPlayer.PlayAICard2(currentHand.get(i), discardPile) == -1)//if card doesnt match = false/-1
                        { 
                            if(i+1 >= currentPlayer.GetCardQuantity())//if all cards don't match = pull
                            {
                                output.setText("The AI has pulled a card from deck!");
                                currentPlayer.PullFromDeck(gameDeck);
                                waitThreeSeconds();
                                done = true;
                                break;
                            }                             
                        continue; //keep going till its "valid" or no match have been found! 
                        }
                        if(currentPlayer.PlayAICard2(currentHand.get(i), discardPile) == 2)//play the card since its not eight
                        {   
                            output.setText("The AI Played -> Rank: "+c.GetRank()+" Suit: "+c.GetSuit());
                            currentPlayer.PlayCard(currentHand.get(i), discardPile);
                            updateDiscard(false);
                            waitThreeSeconds();
                            done = true;
                            break;
                        }
                    }                        
                }
            }
            catch(Exception e){
                System.out.println(e);
            }
        }
    }
    
    public void updateDiscard(boolean isHuman)
    {
        BufferedImage deck = sourceDeck;
        int disX = discardPile.GetRequiredRank().ordinal();
        int disY = discardPile.GetRequiredSuit().ordinal();
        discard.setIcon(new ImageIcon(cropImage(deck, disX, disY)));
        
        //if the current player is human, display information from the text box
        if(isHuman)
        {
            if (discardPile.GetRequiredRank() == Rank.EIGHT)
                output.setText("You must play a(n) "+ discardPile.GetRequiredRank()+ " or a "+ discardPile.GetRequiredSuit()+ "!");
            else
                output.setText("You must play a(n) "+ discardPile.GetRequiredRank()+ " or a "+ discardPile.GetRequiredSuit()+ " or an EIGHT!");
        }
        discard.setVisible(true);
    }
    
    //Counts the number of cards left in each opponents hand to display to currently active player
    public void updateOpponentHandCount(int index)
    {
        int j = 0; //counter for going through opponents<>
        for (int i = 0; i < numPlayers; i++)
        {
            if (i != index){
                opponents.get(j).setText(Main.players[i].GetCardQuantity() + " Card(s)");
                j++;
            }
        }
    }
    
    //Depending on the number of players in the game, sets up different table layouts
    public void initTable()
    {
        opponents = new ArrayList<>();
        BufferedImage pointSouth = rotateImage(faceDown, Math.toRadians(180));
        BufferedImage pointEast = rotateImage(faceDown, Math.toRadians(90));
        BufferedImage pointWest = rotateImage(faceDown, Math.toRadians(270));
        switch (numPlayers)
        {
            case 2:
                JLabel faceDown20 = new JLabel(new ImageIcon(pointSouth));
                faceDown20.setBounds(360, 20, scaleX, scaleY + 20);
                faceDown20.setForeground(Color.white);
                faceDown20.setText("P#: ");
                faceDown20.setHorizontalTextPosition(JLabel.CENTER);
                faceDown20.setVerticalTextPosition(JLabel.BOTTOM);
                contentPane.add(faceDown20);
                opponents.add(faceDown20);
                
                break;
            case 3:
                JLabel faceDown30 = new JLabel(new ImageIcon(pointEast));
                faceDown30.setBounds(20, 200, scaleY, scaleX + 20);
                faceDown30.setForeground(Color.white);
                faceDown30.setText("P#: ");
                faceDown30.setHorizontalTextPosition(JLabel.CENTER);
                faceDown30.setVerticalTextPosition(JLabel.BOTTOM);
                contentPane.add(faceDown30);
                opponents.add(faceDown30);
                
                JLabel faceDown31 = new JLabel(new ImageIcon(pointWest));
                faceDown31.setBounds(660, 200, scaleY, scaleX + 20);
                faceDown31.setForeground(Color.white);
                faceDown31.setText("P#: ");
                faceDown31.setHorizontalTextPosition(JLabel.CENTER);
                faceDown31.setVerticalTextPosition(JLabel.BOTTOM);
                contentPane.add(faceDown31);
                opponents.add(faceDown31);
                
                break;
            case 4:
                JLabel faceDown40 = new JLabel(new ImageIcon(pointEast));
                faceDown40.setBounds(20, 200, scaleY, scaleX + 20);
                faceDown40.setForeground(Color.white);
                faceDown40.setText("P#: ");
                faceDown40.setHorizontalTextPosition(JLabel.CENTER);
                faceDown40.setVerticalTextPosition(JLabel.BOTTOM);
                contentPane.add(faceDown40);
                opponents.add(faceDown40);
                
                JLabel faceDown41 = new JLabel(new ImageIcon(pointSouth));
                faceDown41.setBounds(360, 20, scaleX, scaleY + 20);
                faceDown41.setForeground(Color.white);
                faceDown41.setText("P#: ");
                faceDown41.setHorizontalTextPosition(JLabel.CENTER);
                faceDown41.setVerticalTextPosition(JLabel.BOTTOM);
                contentPane.add(faceDown41);
                opponents.add(faceDown41);
                
                JLabel faceDown42 = new JLabel(new ImageIcon(pointWest));
                faceDown42.setBounds(660, 200, scaleY, scaleX + 20);
                faceDown42.setForeground(Color.white);
                faceDown42.setText("P#: ");
                faceDown42.setHorizontalTextPosition(JLabel.CENTER);
                faceDown42.setVerticalTextPosition(JLabel.BOTTOM);
                contentPane.add(faceDown42);
                opponents.add(faceDown42);
                
                break;
            case 5:
                JLabel faceDown50 = new JLabel(new ImageIcon(pointEast));
                faceDown50.setBounds(20, 200, scaleY, scaleX + 20);
                faceDown50.setForeground(Color.white);
                faceDown50.setText("P#: ");
                faceDown50.setHorizontalTextPosition(JLabel.CENTER);
                faceDown50.setVerticalTextPosition(JLabel.BOTTOM);
                contentPane.add(faceDown50);
                opponents.add(faceDown50);
                
                JLabel faceDown51 = new JLabel(new ImageIcon(pointSouth));
                faceDown51.setBounds(260, 20, scaleX, scaleY + 20);
                faceDown51.setForeground(Color.white);
                faceDown51.setText("P#: ");
                faceDown51.setHorizontalTextPosition(JLabel.CENTER);
                faceDown51.setVerticalTextPosition(JLabel.BOTTOM);
                contentPane.add(faceDown51);
                opponents.add(faceDown51);
                
                JLabel faceDown52 = new JLabel(new ImageIcon(pointSouth));
                faceDown52.setBounds(460, 20, scaleX, scaleY + 20);
                faceDown52.setForeground(Color.white);
                faceDown52.setText("P#: ");
                faceDown52.setHorizontalTextPosition(JLabel.CENTER);
                faceDown52.setVerticalTextPosition(JLabel.BOTTOM);
                contentPane.add(faceDown52);
                opponents.add(faceDown52);
                
                JLabel faceDown53 = new JLabel(new ImageIcon(pointWest));
                faceDown53.setBounds(660, 200, scaleY, scaleX + 20);
                faceDown53.setForeground(Color.white);
                faceDown53.setText("P#: ");
                faceDown53.setHorizontalTextPosition(JLabel.CENTER);
                faceDown53.setVerticalTextPosition(JLabel.BOTTOM);
                contentPane.add(faceDown53);
                opponents.add(faceDown53);
                
                break;
            case 6:
                JLabel faceDown60 = new JLabel(new ImageIcon(pointEast));
                faceDown60.setBounds(20, 260, scaleY, scaleX + 20);
                faceDown60.setForeground(Color.white);
                faceDown60.setText("P#: ");
                faceDown60.setHorizontalTextPosition(JLabel.CENTER);
                faceDown60.setVerticalTextPosition(JLabel.BOTTOM);
                contentPane.add(faceDown60);
                opponents.add(faceDown60);
                
                JLabel faceDown61 = new JLabel(new ImageIcon(pointEast));
                faceDown61.setBounds(20, 110, scaleY, scaleX + 20);
                faceDown61.setForeground(Color.white);
                faceDown61.setText("P#: ");
                faceDown61.setHorizontalTextPosition(JLabel.CENTER);
                faceDown61.setVerticalTextPosition(JLabel.BOTTOM);
                contentPane.add(faceDown61);
                opponents.add(faceDown61);
                
                JLabel faceDown62 = new JLabel(new ImageIcon(pointSouth));
                faceDown62.setBounds(260, 20, scaleX, scaleY + 20);
                faceDown62.setForeground(Color.white);
                faceDown62.setText("P#: ");
                faceDown62.setHorizontalTextPosition(JLabel.CENTER);
                faceDown62.setVerticalTextPosition(JLabel.BOTTOM);
                contentPane.add(faceDown62);
                opponents.add(faceDown62);
                
                JLabel faceDown63 = new JLabel(new ImageIcon(pointSouth));
                faceDown63.setBounds(460, 20, scaleX, scaleY + 20);
                faceDown63.setForeground(Color.white);
                faceDown63.setText("P#: ");
                faceDown63.setHorizontalTextPosition(JLabel.CENTER);
                faceDown63.setVerticalTextPosition(JLabel.BOTTOM);
                contentPane.add(faceDown63);
                opponents.add(faceDown63);
                
                JLabel faceDown64 = new JLabel(new ImageIcon(pointWest));
                faceDown64.setBounds(660, 200, scaleY, scaleX + 20);
                faceDown64.setForeground(Color.white);
                faceDown64.setText("P#: ");
                faceDown64.setHorizontalTextPosition(JLabel.CENTER);
                faceDown64.setVerticalTextPosition(JLabel.BOTTOM);
                contentPane.add(faceDown64);
                opponents.add(faceDown64);
                
                break;
            case 7:
                JLabel faceDown70 = new JLabel(new ImageIcon(pointEast));
                faceDown70.setBounds(20, 260, scaleY, scaleX + 20);
                faceDown70.setForeground(Color.white);
                faceDown70.setText("P#: ");
                faceDown70.setHorizontalTextPosition(JLabel.CENTER);
                faceDown70.setVerticalTextPosition(JLabel.BOTTOM);
                contentPane.add(faceDown70);
                opponents.add(faceDown70);
                
                JLabel faceDown71 = new JLabel(new ImageIcon(pointEast));
                faceDown71.setBounds(20, 110, scaleY, scaleX + 20);
                faceDown71.setForeground(Color.white);
                faceDown71.setText("P#: ");
                faceDown71.setHorizontalTextPosition(JLabel.CENTER);
                faceDown71.setVerticalTextPosition(JLabel.BOTTOM);
                contentPane.add(faceDown71);
                opponents.add(faceDown71);
                
                JLabel faceDown72 = new JLabel(new ImageIcon(pointSouth));
                faceDown72.setBounds(260, 20, scaleX, scaleY + 20);
                faceDown72.setForeground(Color.white);
                faceDown72.setText("P#: ");
                faceDown72.setHorizontalTextPosition(JLabel.CENTER);
                faceDown72.setVerticalTextPosition(JLabel.BOTTOM);
                contentPane.add(faceDown72);
                opponents.add(faceDown72);
                
                JLabel faceDown73 = new JLabel(new ImageIcon(pointSouth));
                faceDown73.setBounds(460, 20, scaleX, scaleY + 20);
                faceDown73.setForeground(Color.white);
                faceDown73.setText("P#: ");
                faceDown73.setHorizontalTextPosition(JLabel.CENTER);
                faceDown73.setVerticalTextPosition(JLabel.BOTTOM);
                contentPane.add(faceDown73);
                opponents.add(faceDown73);
                
                JLabel faceDown74 = new JLabel(new ImageIcon(pointWest));
                faceDown74.setBounds(660, 110, scaleY, scaleX + 20);
                faceDown74.setForeground(Color.white);
                faceDown74.setText("P#: ");
                faceDown74.setHorizontalTextPosition(JLabel.CENTER);
                faceDown74.setVerticalTextPosition(JLabel.BOTTOM);
                contentPane.add(faceDown74);
                opponents.add(faceDown74);
                
                JLabel faceDown75 = new JLabel(new ImageIcon(pointWest));
                faceDown75.setBounds(660, 260, scaleY, scaleX + 20);
                faceDown75.setForeground(Color.white);
                faceDown75.setText("P#: ");
                faceDown75.setHorizontalTextPosition(JLabel.CENTER);
                faceDown75.setVerticalTextPosition(JLabel.BOTTOM);
                contentPane.add(faceDown75);
                opponents.add(faceDown75);
                
                break;
            default:
                System.out.println("Something went wrong. Terminating program");
                System.exit(0);
        }
    }
      
    //pauses the game for a couple seconds so that user can see AI make a move
    private void waitThreeSeconds()
    {
        try {
            //pauses for 3000 milliseconds
            Thread.sleep(3000);
        }
        catch(InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
    }
    
    private BufferedImage cropImage(BufferedImage img, int posX, int posY)
    {
        int realX = (int) Math.round(posX * dimX), realY = (int) Math.round(posY * dimY);
        BufferedImage sample = img.getSubimage(realX, realY, (int) Math.round(dimX), (int) Math.round(dimY)); 
        BufferedImage cropped = new BufferedImage(sample.getWidth(), sample.getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics g = cropped.createGraphics();
        g.drawImage(sample, 0, 0, null);
        return scaleImage(cropped, scaleX, scaleY);
    }
    
    private BufferedImage scaleImage (BufferedImage img, int width, int height)
    {
        BufferedImage scaled = new BufferedImage(width, height, BufferedImage.TRANSLUCENT);
        Graphics2D g2d = (Graphics2D) scaled.createGraphics();
        g2d.addRenderingHints(new RenderingHints(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY));
        g2d.drawImage(img, 0, 0, width, height, null);
        g2d.dispose();
        return scaled;
    }
    
    public BufferedImage rotateImage (BufferedImage image, double angle) 
    {
        double sin = Math.abs(Math.sin(angle)), cos = Math.abs(Math.cos(angle));
        int w = image.getWidth(), h = image.getHeight();
        int neww = (int)Math.floor(w*cos+h*sin), newh = (int) Math.floor(h * cos + w * sin);
        GraphicsConfiguration gc = getDefaultConfiguration();
        BufferedImage result = gc.createCompatibleImage(neww, newh, Transparency.TRANSLUCENT);
        Graphics2D g = result.createGraphics();
        g.translate((neww - w) / 2, (newh - h) / 2);
        g.rotate(angle, w / 2, h / 2);
        g.drawRenderedImage(image, null);
        g.dispose();
        return result;
    }

    private GraphicsConfiguration getDefaultConfiguration() 
    {
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice gd = ge.getDefaultScreenDevice();
        return gd.getDefaultConfiguration();
    }
    
    public int checkIfDone()
    {
        //Checks while in the user interface if they would like to play again with the same settings
        //if not, setup returns to the console in Control
        
        return 0;
    }
}