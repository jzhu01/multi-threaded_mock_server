/**
 * Operating Systems Final Project
 * @author Sadi Evren Seker, Jennifer Zhu, Sabrina Sayasith
 * Due: 12/15/16
 */

import java.io.*;
import java.net.*;
import java.util.Scanner;
public class Requester{
    Socket requestSocket;
    ObjectOutputStream out;
    ObjectInputStream in;
    String message;
    Requester(){}
    void run() {
      //1. creating a socket to connect to the server
      try{
        requestSocket = new Socket("localhost", 1234);
        System.out.println("Connected to localhost in port 1234");

        PrintWriter out = new PrintWriter(requestSocket.getOutputStream(), true);
        BufferedReader in = new BufferedReader(
        new InputStreamReader(requestSocket.getInputStream()));
        Scanner stdIn = new Scanner(System.in);

        String fromUser ="knockknock";
        System.out.println("connected to server, expecting new port number");
        //out.println(fromUser);
        String line ="some predefined msg";
        line =in.readLine();
        System.out.println(line);


        int newportnumber = Integer.parseInt(line.substring(line.indexOf(":")+2));


        requestSocket = new Socket("localhost", newportnumber);
        System.out.println("Connected to localhost in port"+newportnumber);

        out = new PrintWriter(requestSocket.getOutputStream(), true);
        in = new BufferedReader(
        new InputStreamReader(requestSocket.getInputStream()));

        System.out.println("Please type in your file name.");
        System.out.print(">>>");
        fromUser = stdIn.nextLine();
        fromUser = "GET /" + fromUser + " HTTP/1.1";

        //fromUser = "GET /try2.html HTTP/1.1";
        if (fromUser != null) {
            System.out.println("Client: " + fromUser);
            out.println(fromUser);
        }

        while((line =in.readLine())!=null){
            System.out.println(line);
        }
        // System.out.println("sending :GET /try.html HTTP/1.0 ");
        /*  sendMessage("GET /try.html HTTP/1.1");
           message = (String)in.readObject();
          System.out.println("server>" + message);
          message = "bye";
          sendMessage(message);*/
        requestSocket.close();
      } catch(Exception e){
          System.err.println("data received in unknown format");
          e.printStackTrace();
      }
    }

    void sendMessage(String msg) {
        try {
            out.writeObject(msg);
            out.flush();
            System.out.println("client>" + msg);
        } catch(IOException ioException){
            ioException.printStackTrace();
        }
    }

    public static void main(String args[]) {
        Requester client = new Requester();
        client.run();
    }
}
