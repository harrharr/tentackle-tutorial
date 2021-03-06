/*
 * Tracker generated by tentackle-project-archetype.
 */

/**
 * Persistent Domain Object (PDO) module.
 */
module com.example.tracker.pdo {
  exports com.example.tracker.pdo;
  exports com.example.tracker.pdo.md;
  exports com.example.tracker.pdo.md.domain;
  exports com.example.tracker.pdo.md.persist;
  exports com.example.tracker.pdo.security;
  exports com.example.tracker.pdo.td;
  exports com.example.tracker.pdo.td.domain;
  exports com.example.tracker.pdo.td.persist;

  requires transitive com.example.tracker.common;

  provides org.tentackle.common.ModuleHook with com.example.tracker.pdo.service.Hook;
}
