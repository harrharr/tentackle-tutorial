/*
 * Tracker generated by tentackle-project-archetype.
 */

/**
 * FX GUI module.
 */
open module com.example.tracker.gui {
  exports com.example.tracker.gui;
  exports com.example.tracker.gui.main;

  requires com.example.tracker.pdo;

  requires org.tentackle.fx.rdc;

  requires java.desktop;
  requires java.prefs;

  provides org.tentackle.common.ModuleHook with com.example.tracker.gui.service.Hook;
}
