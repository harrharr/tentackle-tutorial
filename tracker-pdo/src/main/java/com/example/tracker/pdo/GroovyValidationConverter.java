/*
 * Tracker generated by tentackle-project-archetype.
 */

package com.example.tracker.pdo;

import org.tentackle.script.ScriptConverter;
import org.tentackle.script.ScriptingLanguage;
import org.tentackle.validate.validator.ValidationScriptConverter;

/**
 * Adds imports to groovy scripts in validators.
 */
@ValidationScriptConverter("Groovy")
public class GroovyValidationConverter implements ScriptConverter {

  @Override
  public String convert(String code, ScriptingLanguage language) {
    return
            "import com.example.tracker.pdo.*;\n" +
            code;
  }

}
