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
<<<<<<< HEAD
                "Matthew Wethington",
                "Yoseph Wordofa",
                "Jonathan Guidry",
=======
<<<<<<< HEAD
                "Name 1",
                "Yoseph Wordofa",
                "Name 3",
=======
                "Matthew Wethington",
                "Yoseph Wordofa1",
                "Jonathan Guidry",
>>>>>>> eca91e385bed19a860d67b3857237323d4243bbf
>>>>>>> 2537e3d38fc67025030fe8b62a1bf9c24a6d061c
                "Name 4",
                "Name 5"};

        System.out.println(String.join(separator, names));
    }
}
