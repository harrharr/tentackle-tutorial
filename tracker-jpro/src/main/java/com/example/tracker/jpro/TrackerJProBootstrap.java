/*
 * Tracker JPro web client.
 */

package com.example.tracker.jpro;

import com.example.tracker.jpro.session.FxSessionBinder;

import org.tentackle.app.Application;
import org.tentackle.fx.FxFactory;
import org.tentackle.fx.FxUtilities;
import org.tentackle.fx.ThemeUtilities;
import org.tentackle.fx.rdc.RdcUtilities;
import org.tentackle.log.Logger;

/**
 * Per-JVM bootstrap, configured as {@code jpro.onFXStartup}.
 * <p>
 * The JPro counterpart of a {@code ServletContextListener}: runs once, before any browser connects.
 */
public final class TrackerJProBootstrap {

  private static final Logger LOGGER = Logger.get();

  /**
   * Starts the web application of this JVM.
   *
   * @param args the command line arguments (unused)
   */
  public static void main(String[] args) {
    ensureStarted();
  }

  /**
   * Starts the web application unless it is already running.
   */
  public static synchronized void ensureStarted() {
    if (Application.getInstance() == null) {
      new TrackerWebApplication().start(new String[0]);    // register + initialize + technical login
      applyTheme();
      FxSessionBinder.install();
      TrackerRequestHandler.register();
      logServices();
      LOGGER.info("tracker web application started");
    }
  }


  /**
   * Logs the FX services in effect.
   * <p>
   * Every one of them is overridden for the web tier, and each override only takes effect if it
   * wins the {@code @Service} lookup against the implementation Tentackle itself provides. Getting
   * that wrong is silent, so it is logged once at startup.
   */
  private static void logServices() {
    LOGGER.info("FxFactory={0}, FxUtilities={1}, RdcUtilities={2}, ThemeUtilities={3}",
                FxFactory.getInstance().getClass().getName(),
                FxUtilities.getInstance().getClass().getName(),
                RdcUtilities.getInstance().getClass().getName(),
                ThemeUtilities.getInstance().getClass().getName());
  }

  /**
   * Installs the user agent stylesheet.
   * <p>
   * {@code DesktopApplication.applyProperties} does this for the desktop client; a
   * {@link org.tentackle.web.app.WebApplication} knows nothing about JavaFX and does not.
   * Without it JavaFX stays on Modena, the AtlantaFX variables the Tentackle stylesheets look up
   * ({@code -color-header-bg}, {@code -color-cell-border}, ...) are undefined, and every rule using
   * one fails at conversion time with {@code String cannot be cast to Paint}.
   * <p>
   * The theme is a per-JVM setting and therefore the same for every user of this server, which is
   * why dark mode is not derived from {@code WebAPI.isDarkMode()} at login: that would flip the
   * theme for everybody.
   */
  private static void applyTheme() {
    ThemeUtilities themeUtilities = ThemeUtilities.getInstance();
    themeUtilities.setTheme(false, themeUtilities.getThemeName());   // light mode, platform default theme
    LOGGER.info("theme {0} applied", themeUtilities.getThemeName());
  }


  private TrackerJProBootstrap() {}

}
