import id.IdController;
import message.MessageController;
import message.SendController;
import shellutilities.Console;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SimpleShell {


    public static void main(String[] args) throws java.io.IOException {
        String commandLine;
        BufferedReader console = new BufferedReader
                (new InputStreamReader(System.in));

        ProcessBuilder pb = new ProcessBuilder();
        List<String> history = new ArrayList<String>();
        int index = 0;
        //we break out with <ctrl c>
        while (true) {
            //read what the user enters
            Console.print("cmd? ");
            commandLine = console.readLine();

            String[] commands = commandLine.split(" ");

            //if the user entered a return, just loop again
            if (commandLine.equals(""))
                continue;

            if (commandLine.equals("exit")) {
                Console.print("bye!");
                break;
            }

            List<String> list = new ArrayList<>(Arrays.asList(commands));

            history.addAll(list);
            try {
                //display history of shell with index
                if (list.get(list.size() - 1).equals("history")) {
                    for (String s : history)
                        Console.print((index++) + " " + s);

                    continue;
                }

                // Specific Commands.

                if (commands[0].equals("ids")) {
                    IdController idc = new IdController(commands);
                    idc.idController();
                    continue;
                }

                // messages - all messages
                if (commands[0].equals("messages")) {
                    MessageController mc = new MessageController(commands);
                    mc.messageController();
                    continue;
                }

                if (commands[0].equals("send")) {
                    SendController sc = new SendController(commands);
                    sc.sendController();
                    continue;
                }

                //!! command returns the last command in history
                if (list.get(list.size() - 1).equals("!!")) {
//                if (commands[0].equals("!!")) {
                    try {
                        pb.command(history.get(history.size() - 2));
                    } catch (ArrayIndexOutOfBoundsException a) {
                        Console.print("Empty History... try again.");
                        continue;
                    }

                }//!<integer value i> command
                else if (list.get(list.size() - 1).charAt(0) == '!') {
                    int b = Character.getNumericValue(list.get(list.size() - 1).charAt(1));
                    if (b <= history.size())//check if integer entered isn't bigger than history size
                        pb.command(history.get(b));
                } else {
                    pb.command(list);
                }

                // wait, wait, what curiousness is this?
                Process process = pb.start();

                //obtain the input stream
                InputStream is = process.getInputStream();
                InputStreamReader isr = new InputStreamReader(is);
                BufferedReader br = new BufferedReader(isr);

                //read output of the process
                String line;
                while ((line = br.readLine()) != null)
                    Console.print(line);
                br.close();
            }
            catch (IOException e) {
                Console.print("Input Error, Please try again!");
            }
        }
    }
}