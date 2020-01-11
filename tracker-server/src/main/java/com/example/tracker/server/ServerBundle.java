/*
 * Tracker generated by tentackle-project-archetype.
 */

package com.example.tracker.server;

import org.tentackle.common.Bundle;
import org.tentackle.common.BundleFactory;
import org.tentackle.common.LocaleProvider;

import java.util.ResourceBundle;

/**
 * Bundle for translations of the server module.
 */
@Bundle
public class ServerBundle {

  /**
   * Gets the bundle.
   *
   * @return the resource bundle
   */
  public static ResourceBundle getBundle() {
    return BundleFactory.getBundle(ServerBundle.class.getName(), LocaleProvider.getInstance().getLocale());
  }

  /**
   * Gets a string for the given key.
   *
   * @param key the key
   * @return the string from the bundle
   */
  public static String getString(String key) {
    return getBundle().getString(key);
  }

  private ServerBundle() {
  }

}
