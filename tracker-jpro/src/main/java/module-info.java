/*
 * Tracker JPro web client.
 */

/**
 * Multi-user web client module.
 * <p>
 * Runs the Tracker FX UI server-side and streams it to the browser via JPro. One JVM hosts many
 * users at once, so a few rules apply that a desktop client does not have to care about:
 * <ol>
 *   <li>The JavaFX application thread is shared. Every code path reaching PDOs must run inside
 *       {@link com.example.tracker.jpro.session.FxSessionBinder}, which is what makes the calling
 *       user's session and locale thread-local. A missing binding fails with
 *       {@code PersistenceException("no thread-local session")} — that is the intended failure
 *       mode, never work around it with the application's technical session.</li>
 *   <li>Never capture {@code Session.getSession()} in a field. Capture the
 *       {@link com.example.tracker.jpro.session.UserScope} and re-bind instead, so a session can
 *       never outlive its unit of work.</li>
 *   <li>Background work needs its own session for the same user, see
 *       {@link com.example.tracker.jpro.TrackerJProRdcUtilities}.</li>
 *   <li>PDO caches are shared JVM-wide. Cached PDOs carry no session by design and resolve the
 *       thread-local one on demand, which is precisely why rule 1 matters.</li>
 *   <li>Preferences are system-only ({@code AbstractServerApplication} forces
 *       {@code PersistedPreferencesFactory.setSystemOnly(true)}) and the theme is a JVM-wide
 *       setting, so neither is per user here.</li>
 *   <li>Static mutable state in controllers is shared across users.</li>
 * </ol>
 */
open module com.example.tracker.jpro {
  exports com.example.tracker.jpro;
  exports com.example.tracker.jpro.session;

  requires transitive com.example.tracker.gui;
  requires com.example.tracker.persist;
  requires com.example.tracker.domain;
  requires transitive org.tentackle.web;
  requires jpro.webapi;

  provides org.tentackle.common.ModuleHook with com.example.tracker.jpro.service.Hook;
}
