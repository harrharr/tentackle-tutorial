/*
 * Tracker JPro web client.
 */

package com.example.tracker.jpro.service;

import org.tentackle.common.ModuleHook;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Hook for the JPro web client module.
 */
public class Hook implements ModuleHook {

  @Override
  public ResourceBundle getBundle(String baseName, Locale locale) {
    return ResourceBundle.getBundle(baseName, locale);
  }

}
