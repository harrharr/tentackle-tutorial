/*
 * Tracker generated by tentackle-project-archetype.
 */

package com.example.tracker.common;

import org.tentackle.common.Service;
import org.tentackle.session.SessionInfo;
import org.tentackle.session.SessionInfoFactory;

import java.util.Properties;

/**
 * Application specific session info factory.
 */
@Service(SessionInfoFactory.class)
public class TrackerSessionInfoFactory implements SessionInfoFactory {

  @Override
  public SessionInfo create(String username, char[] password, String propertiesName) {
    return new TrackerSessionInfo(username, password, propertiesName);
  }

  @Override
  public SessionInfo create(String propertiesName) {
    return new TrackerSessionInfo(propertiesName);
  }

  @Override
  public SessionInfo create(Properties properties) {
    return new TrackerSessionInfo(properties);
  }

  @Override
  public SessionInfo create() {
    return new TrackerSessionInfo();
  }

}
