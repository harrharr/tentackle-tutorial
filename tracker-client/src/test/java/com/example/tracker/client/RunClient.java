/*
 * Tracker generated by tentackle-project-archetype.
 */

package com.example.tracker.client;

import org.testng.annotations.Test;

public class RunClient {

  @Test
  public void runClient() {
    run(new String[] {"--statistics"});
  }

  @Test
  public void runClientDE() {
    run(new String[] {"--statistics", "--locale=de_DE"});
  }


  private synchronized void run(String[] args) {
    @SuppressWarnings("unchecked")
    TrackerFxClient client = new TrackerFxClient() {

      @Override
      protected boolean isSystemExitNecessaryToStop() {
        return false;   // no System.exit() by default within the test container
      }

      @Override
      public void stop(int exitValue, Throwable exitThrowable) {
        super.stop(exitValue, exitThrowable);
        if (exitValue != 0) {
          System.exit(exitValue);   // system exit only if error
        }
      }
    };
    client.start(args);
  }

}