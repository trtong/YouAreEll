package shellutilities;

import java.util.Scanner;

public class Console {

    Scanner in;

    public Console(Scanner in) {
        this.in = in;
    }

    public Console() {
        in = new Scanner(System.in);
    }

    public static void print(String s) {
        System.out.println(s);
    }
}