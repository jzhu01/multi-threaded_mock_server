import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.Date;
import java.util.Random;
import java.io.IOException;

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
    Boolean running;
    public cli(){
      //this.server = s1;
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
                    //server = new server(1234);
                    this.server = new server(1234);
                    this.adminThread = (new Thread(this.server));
                    adminThread.start();
                    //System.out.println("Is this working?");
                } // closing out of the if
                if(command.equals("shutdown")){
                  // code to close the server and all running threads
                  System.out.println("Preparing to shutdown...");
<<<<<<< HEAD
                  this.server.closeThreads();
                  this.server.stopRunning();
                  try{
                   server.getServerSocket().close();
                  }
                  catch(Exception e){
                    System.out.println("Problem closing server socket");
                  }
=======
                  server.closeThreads(adminThread);
                  try {
                    server.serversocket.close();
                  } catch (IOException e){
                    // handle error
                    System.out.println("Socket successfully closed.");
                  }
                  System.out.println("Finished closing everything.");
>>>>>>> a489f037ed9dd2a4c5859854c746cadfd55a6d8a
                  //adminThread.interrupt();
                }
                if(command.equals("ls")){
                  System.out.println("Some Thread Info:");
                  server.listThreads();
                }
                if (command.equals("quit")){
                  System.out.println("Exiting the command line interface.");
                  System.out.println("Goodbye!");
                  System.exit(0);
                }
        }
    }
    public static void main(String[] args){
      server s1 = new server(1234);
      cli c1 = new cli();
      c1.run();
    }
}
