/*
 * Tracker JPro web client.
 */

package com.example.tracker.jpro;

import com.example.tracker.common.Version;
import com.example.tracker.pdo.md.User;

import org.tentackle.pdo.DomainContext;
import org.tentackle.pdo.Pdo;
import org.tentackle.session.Session;
import org.tentackle.session.SessionInfo;
import org.tentackle.session.auth.AuthenticationException;
import org.tentackle.web.app.WebApplication;

/**
 * The multi-user web application hosting the FX UI.
 * <p>
 * One instance per JVM, serving as many users as there are open browser sessions — exactly like a
 * traditional web server. The web session key is the JPro instance id.
 * <p>
 * The application itself logs in with the <em>technical</em> user from {@code backend.properties}.
 * Because that session is remote, {@code finishStartup()} creates a
 * {@link org.tentackle.session.MultiUserSessionPool}, from which every browser user then gets their
 * own upstream session via {@link #getSession(String)}.
 */
@SuppressWarnings("unchecked")    // due to getUser() below
public class TrackerWebApplication extends WebApplication<String> {

  /**
   * Creates the web application.
   */
  public TrackerWebApplication() {
    super("TrackerWebServer", Version.RELEASE);
  }

  @Override
  public User getUser(DomainContext context, long userId) {
    return Pdo.create(User.class, context).selectCached(userId);
  }

  @Override
  public SessionInfo createSessionInfo(String username, char[] password, String sessionPropertiesBaseName) {
    SessionInfo info = super.createSessionInfo(username, password, sessionPropertiesBaseName);
    info.setClientVersion(Version.RELEASE);
    info.setSessionName("WEB#0");   // MultiUserDbPool derives its own counter from "WEB"
    return info;
  }

  @Override
  public Session getSession(String sessionKey) {
    Session session = super.getSession(sessionKey);
    if (session.getSessionInfo().getUserId() == 0) {
      if (session.isRemote()) {
        session.getSessionInfo().setUserId(session.getRemoteSession().getClientSessionInfo().getUserId());
        session.getSessionInfo().setUserClassId(session.getRemoteSession().getClientSessionInfo().getUserClassId());
      }
      else {
        throw new AuthenticationException("cannot determine userId and userClassId");
      }
    }
    return session;
  }

}
