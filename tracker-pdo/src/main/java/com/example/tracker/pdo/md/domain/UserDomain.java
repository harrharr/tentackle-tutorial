/*
 * Tracker generated by tentackle-project-archetype.
 */

package com.example.tracker.pdo.md.domain;

import com.example.tracker.pdo.md.User;

/**
 * Domain interface for User.
 */
public interface UserDomain extends OrgUnitDomain<User> {

  /**
   * Creates the password hash stored in the database.
   *
   * @param password is the clear-text password, null or empty if none
   * @return the password hash, null if input was null or empty
   */
  String hash(char[] password);

}
