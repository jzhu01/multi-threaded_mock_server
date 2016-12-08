import java.util.*;
import assignment4.MockProcess;
import java.io.*;
/**
*
* @author: Jen Zhu
* @due date: 10/24/16
* CPU Scheduler mimics a CPU Scheduler and reads in a text file containing the time quantum
*  context switch time, several processes and their corresponding arrival times, burst times,
*  and priority
*/

public class CPUScheduler {
  public CPUScheduler(){
    // nothing?
  }

  public static MockProcess createProcess(String line) {
    String[] original_nums = line.split("\\s",0);
    int arriveTime = Integer.parseInt(original_nums[1]);
    int burstTime = Integer.parseInt(original_nums[2]);
    int priority = Integer.parseInt(original_nums[3]);
    MockProcess p = new MockProcess(arriveTime, burstTime, priority);
    return p;
  }

  public static void main(String[] args) {
    // declare variables to store values in
    int timeQuantum;
    int contextSwitchTime;
    ArrayList<MockProcess> allProcesses = new ArrayList<MockProcess>();
    // read the file
    if (args.length == 0){ // if there are no args passed in,
      System.out.println("Error: No file specified. Please try again.");
      System.exit(0);
    } else if (args.length > 1) {
      System.out.println("Error: Multiple files specified. Please try again.");
      System.exit(0);
    }
    try {
      Scanner sc = new Scanner(new File(args[0]));
      timeQuantum = sc.nextInt();
      contextSwitchTime = sc.nextInt();
      while(sc.hasNextLine()) {
        String line = sc.nextLine();
        MockProcess p = createProcess(line);
        allProcesses.add(p);
      }
    } catch (IOException e){
      e.printStackTrace();
    }


  }

}
