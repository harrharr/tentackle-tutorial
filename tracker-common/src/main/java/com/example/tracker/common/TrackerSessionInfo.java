/*
 * Tracker generated by tentackle-project-archetype.
 */

package com.example.tracker.common;

import org.tentackle.common.EncryptedProperties;
import org.tentackle.session.DefaultSessionInfo;

import java.io.Serial;
import java.util.Locale;

/**
 * Application specific session info.
 */
public class TrackerSessionInfo extends DefaultSessionInfo {

  @Serial
  private static final long serialVersionUID = 1L;


  /**
   * Create session info from a username, password and
   * property file holding the connection parameters.
   *
   * @param username is the name of the user, null if {@code System.getProperty("user.name")}
   * @param password is the password, null if none
   * @param propertiesName name of the session properties, null if {@code "backend"}
   */
  public TrackerSessionInfo(String username, char[] password, String propertiesName) {
    super(username, password, propertiesName);
  }

  /**
   * Create session info from a property file holding the connection parameters.
   *
   * @param propertiesName name of the session properties, null if {@code "backend"}
   */
  public TrackerSessionInfo(String propertiesName) {
    super(propertiesName);
  }

  /**
   * Create session from a properties object.
   *
   * @param properties the properties
   */
  public TrackerSessionInfo(EncryptedProperties properties)  {
    super(properties);
  }

  /**
   * Create session info from the default properties {@code "backend"}.
   */
  public TrackerSessionInfo() {
    super();
  }

  @Override
  public void setUserName(String userName) {
    super.setUserName(userName == null ? null : userName.toUpperCase(Locale.ROOT));    // usernames are uppercase only
  }
}
