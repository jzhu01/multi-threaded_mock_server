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
    server server;
    Thread adminThread;
    public cli(server s1){
      this.server = s1;
      this.adminThread = null;
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
                if (command.equals("start")){
                    server = new server(1234);
                    this.adminThread = (new Thread(new server(1234)));
                    adminThread.start();
                    //System.out.println("Is this working?");
                } // closing out of the if
                if(command.equals("shutdown")){
                  // code to close the server and all running threads
                  System.out.println("Preparing to shutdown...");
                  server.closeThreads();
                  adminThread.interrupt();
                }
        }
    }
    public static void main(String[] args){
      server s1 = new server(1234);
      cli c1 = new cli(s1);
      c1.run();
    }
}
