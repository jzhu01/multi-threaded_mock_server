//Now consider both clients are trying to read the same file and you can allow only 1
//access at a time. Provide a synchronization for such a case
// A critical section is a section of code that is executed by multiple threads and where the sequence of execution for the 
//threads makes a difference in the result of the concurrent execution of the critical section.
//To prevent race conditions from occurring you must make sure that the critical section is executed as an atomic instruction. That means that once a single thread is executing it, 
//no other threads can execute it until the first thread has left the critical section.


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

/**
 *
 * @author sadievrenseker
 */

public class server implements Runnable
    {
    
    public static void main(String args[]){
        new server(1234);
    }

  private void s(String s2) { //an alias to avoid typing so much!
    System.out.println(s2);
  }

  public void run(){
    //System.out.println("running thread");
    //http_handler(input, output);
  }

    private int port;
    ArrayList<SharedFile> files = new ArrayList<SharedFile>();

 public server(int listen_port) {
    port = listen_port;
    ServerSocket serversocket = null; 
    try {
      
      s("Trying to bind to localhost on port " + Integer.toString(port) + "...");
      
      serversocket = new ServerSocket(port);
    }
    catch (Exception e) { //catch any errors and print errors to gui
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
           // Thread thread = new Thread( new server());
            //thread.start();
            Thread thread = new Thread(){
              public void run(){
                System.out.println("Thread Running");
                http_handler(input, output); //this is where the thread should handle
              }
            };
            thread.start();
            

      }
      catch (Exception e) { 
        s("\nError:" + e.getMessage());
      }

    } 
  }
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
      System.out.println("random port number : "+newPortNumber);
      
     
      
      

      output.writeUTF("newportnumber:"+newPortNumber);
      output.flush();
      output.close();
      System.out.println("random port sent to client"+newPortNumber);
      
       ServerSocket serversocket2 = new ServerSocket(newPortNumber);
      DataOutputStream output2 =null;
       try {
        Socket connectionsocket2 = serversocket2.accept();
        InetAddress client = connectionsocket2.getInetAddress();
        s(client.getHostName() + " connected to server.\n");
        input2=
            new BufferedReader(new InputStreamReader(connectionsocket2.
            getInputStream()));
        output2 =
            new DataOutputStream(connectionsocket2.getOutputStream());
        //http_handler(input, output);
      }catch(Exception e){
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
      path = tmp2.substring(start+2, end); //fill in the path
      System.out.println("path: " + path);
      SharedFile needed = null;

      System.out.println(files.size());

      if(files.size() > 0){
      for(int i = 0; i < files.size(); i++){
          if(files.get(i).getPathName().equals(path)){
            needed = files.get(i);

            break;
          }
          else if(i == files.size()-1){
            needed = new SharedFile(new File(path));
            files.add(needed);
          }

        }
    }
    else{
      needed = new SharedFile(new File(path));
      files.add(needed);
        }
      needed.process(this, output2);

        //outpu.writeUTF("newportnumber:");

          

    }
    catch (Exception e) {
        e.printStackTrace();
        
    
      
      
    }

  }

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

  public void accessFile(File path, DataOutputStream output2){
    try{
      output2.writeBytes(construct_http_header(200, 5));

      BufferedReader br = new BufferedReader(new FileReader(path));
      s("openning file"+path);
           String line="";
           while((line=br.readLine())!=null){
               output2.writeUTF(line);
               s("line: "+line);
           }
      output2.writeUTF("requested file name :"+path);
      output2.writeUTF("hello world");
      Thread.sleep(10000);
      output2.close(); 
      br.close();
      s("closing file " + path.getName());
    }
        catch (Exception e) {
        e.printStackTrace();
      
    }

  }

} 
