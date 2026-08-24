/*
 * Tracker JPro web client.
 */

package com.example.tracker.jpro;

import com.example.tracker.jpro.session.UserScope;
import com.example.tracker.pdo.td.Message;
import com.jpro.webapi.server.Request;
import com.jpro.webapi.server.Response;
import com.jpro.webapi.server.ServerAPI;

import org.tentackle.common.LocaleProvider;
import org.tentackle.log.Logger;
import org.tentackle.pdo.Pdo;
import org.tentackle.session.Session;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

/**
 * REST endpoints served alongside the UI.
 * <p>
 * This is the dispatcher hook that was impossible while JPro ran on Play. Since JPro moved to
 * Helidon, {@link ServerAPI#addRequestHandler} lets an application handle plain HTTP requests, and
 * the recipe from <a href="https://tentackle.org/web/">tentackle.org/web</a> applies verbatim:
 * take the session for the web session key, make it current, work, clear it and put it back.
 * Handlers run on a Helidon worker thread, one per request, so unlike the UI they get a natural
 * per-request boundary.
 * <p>
 * The UI never passes through here: it runs over a WebSocket, dispatched onto the shared JavaFX
 * application thread, which is why {@link com.example.tracker.jpro.session.FxSessionBinder} exists.
 * <p>
 * (For full Helidon routing — path parameters, streaming, filters — an
 * {@code io.helidon.webserver.spi.ServerFeatureProvider} service would be picked up by JPro's
 * server as well, but it is not part of JPro's public API.)
 */
public final class TrackerRequestHandler {

  private static final Logger LOGGER = Logger.get();

  /** prefix of all endpoints handled here. */
  private static final String PREFIX = "/api/";

  /** header carrying the JPro instance id, i.e. the web session key. */
  private static final String INSTANCE_HEADER = "x-jpro-instance";

  /** query parameter carrying the JPro instance id. */
  private static final String INSTANCE_PARAMETER = "instanceID";


  /**
   * Registers the request handler.
   * <p>
   * Does nothing if the application does not run inside a JPro server.
   */
  public static void register() {
    ServerAPI serverAPI = ServerAPI.getServerAPI();
    if (serverAPI == null) {
      LOGGER.warning("no JPro server API available -> REST endpoints disabled");
    }
    else {
      serverAPI.addRequestHandler(TrackerRequestHandler::handle);
      LOGGER.info("REST endpoints registered below {0}", PREFIX);
    }
  }


  private static Response handle(Request request) {
    String path = request.getPath();
    if (path == null || !path.startsWith(PREFIX)) {
      return Response.empty();    // not ours: let JPro handle it
    }

    String instanceID = instanceIdOf(request);
    if (UserScope.get(instanceID) == null) {
      return text("not logged in", 401);
    }

    TrackerWebApplication application = TrackerJProApplication.getWebApplication();
    Session session = application.getSession(instanceID);
    if (session == null) {
      return text("no session", 401);
    }

    try {
      session.makeCurrent();
      LocaleProvider.getInstance().setCurrentLocale(session.getSessionInfo().getLocale());
      return switch (path.substring(PREFIX.length())) {
        case "messages/count" -> json("{\"count\":" + Pdo.create(Message.class,
                Pdo.createDomainContext()).selectAll().size() + "}");
        default -> text("no such endpoint: " + path, 404);
      };
    }
    catch (RuntimeException rx) {
      LOGGER.severe("request failed: " + path, rx);
      return text(String.valueOf(rx.getMessage()), 500);
    }
    finally {
      LocaleProvider.getInstance().setCurrentLocale(null);
      session.clearCurrent();
      application.putSession(session);
    }
  }

  /**
   * Determines the web session key of a request.
   * <p>
   * {@code jpro.addInstanceID = true} makes the client send its instance id along; the exact
   * transport is JPro's business, so any candidate that resolves to a live scope is accepted.
   *
   * @param request the request
   * @return the instance id, null if none could be determined
   */
  private static String instanceIdOf(Request request) {
    Map<String, List<String>> headers = request.getHeaders();
    if (headers != null) {
      for (Map.Entry<String, List<String>> entry : headers.entrySet()) {
        if (INSTANCE_HEADER.equalsIgnoreCase(entry.getKey()) && !entry.getValue().isEmpty()) {
          return entry.getValue().getFirst();
        }
      }
    }
    String query = URI.create(request.getURI()).getQuery();
    if (query != null) {
      for (String parameter : query.split("&")) {
        int ndx = parameter.indexOf('=');
        if (ndx > 0 && INSTANCE_PARAMETER.equals(parameter.substring(0, ndx))) {
          return parameter.substring(ndx + 1);
        }
      }
    }
    return null;
  }

  private static Response json(String body) {
    return Response.of(body.getBytes(StandardCharsets.UTF_8), "application/json", 200);
  }

  private static Response text(String body, int statusCode) {
    return Response.of(body.getBytes(StandardCharsets.UTF_8), "text/plain", statusCode);
  }


  private TrackerRequestHandler() {}

}
