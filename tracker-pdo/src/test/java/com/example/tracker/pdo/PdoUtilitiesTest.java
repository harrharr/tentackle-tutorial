/*
 * Tracker generated by tentackle-project-archetype.
 */

package com.example.tracker.pdo;

import org.testng.Assert;
import org.testng.annotations.Test;

import org.tentackle.pdo.PdoUtilities;

import java.util.MissingResourceException;

/**
 * Tests the pdo utilities.<br>
 * This is also a template for applications that have replaced PdoUtilities
 * to map the names via a resource bundle for &#64;Singular and &#64;Plural.
 */
public class PdoUtilitiesTest {

  @Test
  public void testNames() {
    for (String pdoName: PdoUtilities.getInstance().getClassNames()) {
      try {
        Class<?> clazz = Class.forName(pdoName);
        String name = PdoUtilities.getInstance().getSingular(clazz);
        Assert.assertNotNull(name, "no @Singular for " + clazz.getName());
        name = PdoUtilities.getInstance().getPlural(clazz);
        Assert.assertNotNull(name, "no @Plural for " + clazz.getName());
      }
      catch (MissingResourceException mx) {
        Assert.fail("in PDO class " + pdoName + ": " + mx.getMessage());
      }
      catch (ClassNotFoundException cx) {
        Assert.fail("no such PDO class: " + pdoName);
      }
    }
  }

}
