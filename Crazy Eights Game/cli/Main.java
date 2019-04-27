import java.util.*;
public class Main
{
    public static void main(String args[])
    {
        Scanner in = new Scanner(System.in);
        UserInterface.in = in;
        Network n;
        if(args.length > 0)
        {
            n = new Network(args[0]);
        }else
        {
            n = new Network(in);
        }
        while(n.processNext());
    }
}
