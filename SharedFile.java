import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.io.InputStream;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * Operating Systems Final Project
 * @author Sabrina Sayasith
 * Due: 12/15/16
 */


 public class SharedFile
 {
      private final File path;
      private final Object lock = new Object();

      public SharedFile(File path) {                                  // declare a instance variable called path for SharedFile
         this.path = path;
      }

      public void process(server serv, DataOutputStream output2) throws IOException {
         synchronized(lock) {
            try(InputStream fileStream = new FileInputStream(path)) { // try to create a new file input stream to read the file path
               serv.accessFile(path, output2);                        // call accessFile from server, pass in the path and output stream
            }
         }
      }

      public String getPathName(){
         return this.path.getName();                                  //method to get path and check it
      }
 }
