/*
 * CS 3354 (Software Engineering) Semester Project
 * Spring 2019
 * Team 3
 * Dr. Marcus
 * Our semester project is the card game Crazy Eights implemented in Java.
 */
import javax.swing.UIManager.LookAndFeelInfo;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import javax.swing.border.Border;
import javax.swing.*;
import java.util.*;
/**
 * This class takes care of all player interactions with the game through a GUI.
 * @author Matthew Wethington, Martin Boerwinkle, Jonathan Guidry, Yoseph Wordofa
 */
public class GUI extends JFrame {

    private DiscardPile discardpile;
    private Deck gamedeck;
    private Player currentplayer;
    private ArrayList<Card> hand;
    private int[] opponents;    //stores the quantity of cards in each opponents hand
    
    private JButton button1;
    private JTextField textfield1;
    JPanel contentPane;
    public ArrayList<JButton> cards;

    public GUI(Deck gamedeck, DiscardPile discardpile)
    {
        this.gamedeck = gamedeck;
        this.discardpile = discardpile;
        
        contentPane = new JPanel(null);
        cards = new ArrayList<JButton>();
        initGUI();
    }
    
    //Constructor 
    public void initGUI(){

        //initGUI() TODO list (mostly everything)
        //build the display
        //player cards area
        //opponent display
        //deck zone (wont be updated ever, basically)
        //discard pile display
        //some sort of text box
        
        this.setTitle("Crazy Eights");
        this.setSize(800,600);

        //pane with null layout
        contentPane.setPreferredSize(new Dimension(800,600));
        contentPane.setBackground(new Color(255,255,255));


        button1 = new JButton();
        button1.setBounds(270,560,90,35);
        button1.setBackground(new Color(214,217,223));
        button1.setForeground(new Color(0,0,0));
        button1.setEnabled(true);
        button1.setFont(new Font("sansserif",0,12));
        button1.setText("Enter");
        button1.setVisible(true);

        textfield1 = new JTextField();
        textfield1.setBounds(5,560,266,37);
        textfield1.setBackground(new Color(255,255,255));
        textfield1.setForeground(new Color(0,0,0));
        textfield1.setEnabled(true);
        textfield1.setFont(new Font("sansserif",0,12));
        textfield1.setText("Input");
        textfield1.setVisible(true);

        //adding components to contentPane panel
        contentPane.add(button1);
        contentPane.add(textfield1);

        //adding panel to JFrame and seting of window position and close operation
        this.add(contentPane);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.pack();
        this.setVisible(true);
    }
    
    public void setCurrentPlayer(int index)
    {
        currentplayer = Control.players[index];
        getOpponentCount(index);
        updateGUI();
    }
    
    public void updateGUI()
    {
        //updateGUI() TODO list
        //display the top card of the discard pile to the user
        //display information from opponents[] somehow
        //actually make it update the information visually
        //USER INTERACTION that moves the game forward

        //From the contents of the current players hand, get the coordinates of each card from the spritesheet
        int x; int y;
        hand = currentplayer.GetContents();
        for (Card i : hand)
        {
            //ordinal() gets the index of enumerators
            x = i.suit.ordinal();
            y = i.rank.ordinal();
            //Test purposes only, we want to display this information to the user graphically
            System.out.println("The " + i.GetRank() + " of " + i.GetSuit() + " is located in position (" + x + "," + y + ") of the spritesheet.");
            //Using those coordinates, we can get the real position of the card on the spritesheet by multiplying x and y by the dimensions of the actual card
            //After that, using those numbers we'll get a subimage of the larger spritesheet, and then make a new image of a single card using that section
            //Finally, we display the image to the user
        }
        
    }
    
    //Counts the number of cards left in each opponents hand to display to currently active player
    public void getOpponentCount(int index)
    {
        int j = 0;
        opponents = new int[Control.players.length - 1];
        for (int i = 0; i < Control.players.length; i++)
        {
            //gets the information from each player EXCEPT the currently active player
            if (i != index)
            {
                opponents[j] = Control.players[i].GetCardQuantity();
                //Test purposes only, we want to display this information to the user graphically in updateGUI() using info in opponents[]
                System.out.println("Player " + (i+1) + " has " + opponents[j] + " cards in their hand.");
                j++;
            }
        }
    }

    public void addCard(int x, int y, int width, int height)//Adds a card to the screen
    {
        JButton temp = new JButton();
        temp.setBounds(x, y, width, height);
        temp.setBackground(new Color(214,217,223));
        temp.setForeground(new Color(0,0,0));
        temp.setEnabled(true);
        temp.setFont(new Font("sansserif",0,12));
        temp.setVisible(true);
        cards.add(temp);
        contentPane.add(cards.get(cards.size()-1));//Add the most recently added ArrayList element to the contentPane
        this.add(contentPane);
    }
    
    public void removeCard(int cardindex)//Removes a card from the screen
    {
        cards.get(cardindex).setEnabled(false);
        cards.get(cardindex).setVisible(false);
        contentPane.remove(cards.get(cardindex));
        this.add(contentPane);
        //cards.remove(cardindex);//No need to remove cards anymore. Just overwrite the data if the card isn't visible.
    }
}