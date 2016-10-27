package assignment4;

public class MockProcess {
  /** int val for last time the process was run */
  int lastRun;
  /** int val for total time spent waiting */
  int waitTime;
  /** int val for response time */
  int responseTime;
  /** int val for time completed */
  int timeCompleted;
  int arrivalTime;
  int burstTime;
  int priority;

  /**
  * Constructor for MockProcess data structure
  * @params arrivalTime, burstTime, priority are all passed in from text file
  *
  */
  public MockProcess(int arrivalTime, int burstTime, int priority){
    this.arrivalTime = arrivalTime;
    this.burstTime = burstTime;
    this.priority = priority;
  }

  /**
  * Method to set last time run when process queued or completed
  * @param timeVal is the time to set response time to
  */
  public void setLastRun(int timeVal) {
    lastRun = timeVal;
  }

  /**
  * Method to update total wait time when process begins
  * @param currentTime is the time to subtract from
  * @param lastTimeRun is the time to subtract
  */
  public void setWaitTime(int currentTime, int lastTimeRun) {
    waitTime = currentTime - lastTimeRun;
  }

  /**
  * Method to set response time when process is first loaded into CPU
  * @param timeVal is the time to set response time to
  */
  public void setResponseTime(int timeVal){
    responseTime = timeVal;
  }

  /**
  * Method to set completed time, when it is done
  * @param timeVal is the time to set response time to
  */
  public void setCompletedTime(int timeVal) {
    timeCompleted = timeVal;
  }

}
