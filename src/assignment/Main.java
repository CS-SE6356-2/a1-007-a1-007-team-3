package assignment;

public class Main {

    public static void main(String[] args) {
        new NamePrinter().printNames();
    }
}

class NamePrinter {
    /**
     * Prints the names of all group members separated by a specified separator token.
     */
    public void printNames() {
        String separator = ",";

        String[] names = {
                "Matthew Wethington",
                "Yoseph Wordofa",
                "Jonathan Guidry",
                "Name 4",
                "Name 5"};

        System.out.println(String.join(separator, names));
    }
}
