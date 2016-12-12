 import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.io.InputStream;
import java.io.FileInputStream;
import java.io.IOException;

 public class SharedFile
 {
      private final File path;
      private final Object lock = new Object();

      public SharedFile(File path) {
         this.path = path;
      }

      public void process(server serv, DataOutputStream output2) throws IOException {
         synchronized(lock) {
            try(InputStream fileStream = new FileInputStream(path)) {
               serv.accessFile(path, output2);
            }
         }
      }

      public String getPathName(){
         return this.path.getName(); //method to get path and check it 
      }
 }