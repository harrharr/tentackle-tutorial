/*
 * Tracker generated by tentackle-project-archetype.
 */

/**
 * Desktop FX client module.
 */
module com.example.tracker.client {
  exports com.example.tracker.client;

  requires transitive com.example.tracker.gui;
  requires com.example.tracker.persist;
  requires com.example.tracker.domain;
  requires transitive org.tentackle.fx.rdc.update;

  provides org.tentackle.common.ModuleHook with com.example.tracker.client.service.Hook;
}
