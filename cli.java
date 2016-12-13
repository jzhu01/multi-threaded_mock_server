import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.Date;
import java.util.Random;
/*
 * Operating Systems Final Project
 * @author Sadi Evren Seker, Jen Zhu, Sabrina Sayasith
 * Due Date: 12/15/16
 *
 */

/** Class for Command Line Interface */
class cli extends Thread{
    server adminThread;
    public cli(server s1){
      this.adminThread = s1;
    }
    public void run(){
        System.out.println("Welcome to our command line interface:");
        while(true){
            Scanner s = new Scanner(System.in);
            System.out.print(">>>");
            String commandLine = s.nextLine();

            StringTokenizer st = new StringTokenizer(commandLine);

                String command = st.nextToken();
                System.out.println("command: "+command);

                // starting up the server
                if(command.equals("start")){
                    // create a new server thread to act as admin
                    adminThread.start();
                    System.out.println("Is this working?");
                    // code to start the server
                } // closing out of the if
                if(command.equals("shutdown")){
                  // code to close the server and all running threads
                  System.out.println("Preparing to shutdown...");
                }
                //
        }
    }
    public static void main(String[] args){
      server firstServer = new server(1234);
      cli c1 = new cli(firstServer);
      c1.run();
    }
}
