/*
 * Tracker generated by tentackle-project-archetype.
 */

package com.example.tracker.common;

import org.tentackle.bind.Bindable;
import org.tentackle.common.Service;
import org.tentackle.common.ServiceFactory;
import org.tentackle.prefs.CompositePreferences;
import org.tentackle.prefs.PersistedPreferencesFactory;


interface TrackerPreferencesHolder {
  TrackerPreferences INSTANCE = ServiceFactory.createService(TrackerPreferences.class);
}

/**
 * Preferences for Tracker.
 */
@Service(TrackerPreferences.class)    // defaults to self
public class TrackerPreferences extends CompositePreferences {


  /** online help prefix. */
  public static final String HELP_URL = "helpURL";



  public TrackerPreferences() {
    super(PersistedPreferencesFactory.getInstance().isSystemOnly());
  }

  /**
   * The singleton.
   *
   * @return the singleton
   */
  public static TrackerPreferences getInstance() {
    return TrackerPreferencesHolder.INSTANCE;
  }



  @Bindable
  public void setHelpUrl(String val) {
    setString(HELP_URL, val);
  }

  @Bindable
  public String getHelpUrl() {
    return getString(HELP_URL);
  }

}
