
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;
import java.lang.Runnable;
import java.util.concurrent.Semaphore;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;

/**
 * Operating Systems Final Project
 * @author Sadi Evren Seker, Sabrina Sayasith, Jen Zhu
 * Due: 12/15/16
 */

public class server implements Runnable {
    private int port;
    ArrayList<SharedFile> files = new ArrayList<SharedFile>();
    List<Thread> serverThreads = Collections.synchronizedList(new ArrayList<Thread>());
    //List<Thread> serverThreads = new ArrayList<Thread>();

    public static void main(String args[]){
        server s1 = new server(1234);
        s1.run();
    }

    private void s(String s2) { //an alias to avoid typing so much!
      System.out.println(s2);
    }

    public void run(){

      ServerSocket serversocket = null;
      try {
        s("Trying to bind to localhost on port " + Integer.toString(this.port) + "...");
        serversocket = new ServerSocket(this.port);
      } catch (Exception e) { //catch any errors and print errors to gui
        s("\nFatal Error:" + e.getMessage());
        return;
      }
      while (true) {
        s("\nReady, Waiting for requests...\n");
        try {
          Socket connectionsocket = serversocket.accept();
          InetAddress client = connectionsocket.getInetAddress();
          s(client.getHostName() + " connected to server.\n");
          BufferedReader input =
              new BufferedReader(new InputStreamReader(connectionsocket.
              getInputStream()));
          DataOutputStream output =
              new DataOutputStream(connectionsocket.getOutputStream());
              Thread thread = new Thread(){
                public void run(){
                  System.out.println("Thread Running");
                  http_handler(input, output); //this is where the thread should handle
                }
              };
              thread.start();
              synchronized(serverThreads){
                serverThreads.add(thread);  // add to list of server threads for deletion later on in case of shutdown
              System.out.println("Number of threads: "+serverThreads.size());
            }
          } catch (Exception e) {
          s("\nError:" + e.getMessage());
          }
        }
      }

    public server(int listen_port) {
      this.port = listen_port;
    }

    /** Method to handle HTTP requests */
    private void http_handler(BufferedReader input, DataOutputStream output) {
      int method = 0; //1 get, 2 head, 0 not supported
      String http = new String(); //a bunch of strings to hold
      String path = new String(); //the various things, what http v, what path,
      String file = new String(); //what file
      String user_agent = new String(); //what user_agent
      BufferedReader input2 = null;
      try {
        Random r= new Random();
        int newPortNumber = r.nextInt()%10000 + 40000;
        System.out.println("random port number: "+newPortNumber);
        output.writeUTF("newportnumber: "+newPortNumber);
        output.flush();
        output.close();
        System.out.println("random port sent to client: "+newPortNumber);
        ServerSocket serversocket2 = new ServerSocket(newPortNumber);
        DataOutputStream output2 =null;
        try {
          Socket connectionsocket2 = serversocket2.accept();
          InetAddress client = connectionsocket2.getInetAddress();
          s(client.getHostName() + " connected to server.\n");
          input2 = new BufferedReader(new InputStreamReader(connectionsocket2.getInputStream()));
          output2 = new DataOutputStream(connectionsocket2.getOutputStream());
        //http_handler(input, output);
        } catch(Exception e){
            e.printStackTrace();
        }

        //This is the two types of request we can handle
        //GET /index.html HTTP/1.0
        //HEAD /index.html HTTP/1.0
        String tmp = input2.readLine(); //read from the stream
        System.out.println("read: "+tmp);
        String tmp2 = new String(tmp);
        tmp.toUpperCase(); //convert it to uppercase
        if (tmp.startsWith("GET")) { //compare it is it GET
          method = 1;
          System.out.println("method1");
        } //if we set it to method 1
        if (tmp.startsWith("HEAD")) { //same here is it HEAD
          method = 2;
        } //set method to 2
        int start = 0;
        int end = 0;
        for (int a = 0; a < tmp2.length(); a++) {
          if (tmp2.charAt(a) == ' ' && start != 0) {
            end = a;
            break;
          }
          if (tmp2.charAt(a) == ' ' && start == 0) {
            start = a;
          }
        }

        // code to synchronize the file access
        path = tmp2.substring(start+2, end); //fill in the path
        System.out.println("path: " + path);
        SharedFile needed = null;

        System.out.println(files.size());

        if(files.size() > 0){                              // Case 1: If there are files in the SharedFile arrayList
          for(int i = 0; i < files.size(); i++){           // loop through file using i
              if(files.get(i).getPathName().equals(path)){ // if the path is equal to the path requested
                needed = files.get(i);                     // set needed (SharedFile) to be the file i

                break;                                     // exit the for loop
              } else if(i == files.size()-1){              // Case 1.5: Reach the end of the files arrayList and the requested file was not found (not added yet)
                needed = new SharedFile(new File(path));   // create a new SharedFile
                files.add(needed);                         // add the new SharedFile to our list of all the files
              }
            }
        } else {                                           // Case 2: Ther are no files in the SharedFile arrayList
          needed = new SharedFile(new File(path));         // create a new SharedFile
          files.add(needed);                               // add the new SharedFile to our list of all the files
        }
        needed.process(this, output2);                     // call the process function from SharedFile;
        //outpu.writeUTF("newportnumber:");
      } catch (Exception e) {
          e.printStackTrace();
      }
  }

  /** Method to mimic reply HTTP response */
  private String construct_http_header(int return_code, int file_type) {
    String s = "HTTP/1.0 ";
    switch (return_code) {
      case 200:
        s = s + "200 OK";
        break;
      case 400:
        s = s + "400 Bad Request";
        break;
      case 403:
        s = s + "403 Forbidden";
        break;
      case 404:
        s = s + "404 Not Found";
        break;
      case 500:
        s = s + "500 Internal Server Error";
        break;
      case 501:
        s = s + "501 Not Implemented";
        break;
    }

    s = s + "\r\n";
    s = s + "Connection: close\r\n";
    s = s + "Server: SmithOperatingSystemsCourse v0\r\n"; //server name

    switch (file_type) {
      case 0:
        break;
      case 1:
        s = s + "Content-Type: image/jpeg\r\n";
        break;
      case 2:
        s = s + "Content-Type: image/gif\r\n";
      case 3:
        s = s + "Content-Type: application/x-zip-compressed\r\n";
      default:
        s = s + "Content-Type: text/html\r\n";
        break;
    }
    s = s + "\r\n";
    return s;
  }

  /** Method to synchronize file access */
  public void accessFile(File path, DataOutputStream output2){
    try {
      output2.writeBytes(construct_http_header(200, 5));            // write a mock HTTP header

      BufferedReader br = new BufferedReader(new FileReader(path)); // new bufferedReader to read the file named path
      s("openning file "+path);                                     // print out now opening file
      String line="";
      while((line=br.readLine())!=null){                            // print out each line
        output2.writeUTF(line);
        s("line: "+line);
      }
      output2.writeUTF("\nrequested file name :"+path);
      //output2.writeUTF("hello world");
      Thread.sleep(10000);                                          // had the thread sleep to test synchronization
      output2.close();
      br.close();
      s("closing file " + path.getName());
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
  /** Method to close out of all active server threads */
  public void closeThreads(){
    //try {
    synchronized(serverThreads){
        for (Thread t: serverThreads){
          t.interrupt();
        }
      }
      Thread.currentThread().interrupt();
      return;
    //} catch (InterruptedException e){
      // handle error here
    //}
  }

  /** Method to list all active server threads */
  public void listThreads(){
    System.out.println("Got into the listThreads method! ");
    // for (Thread t: serverThreads) {
    //   System.out.println("Thread looped!");
    //   System.out.println("Thread: "+t.getName());
    // }
    synchronized(serverThreads){
      System.out.println("Number of threads: "+serverThreads.size());
      if (serverThreads.size() > 0){
        for (int i = 0; i < serverThreads.size(); i++){
          System.out.println(serverThreads.get(i));
        }
      }
    }
  }

  // Problems:
  // Server threads are not being added to the arrayList
  // --> when trying to loop through to print them out, unable to
  // --> when trying to end the threads by looping, unable to
  // May need to provide some sort of handling mechanism for interrupts

}
