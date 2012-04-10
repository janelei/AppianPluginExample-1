package com.appiancorp.pluginexample;

import java.util.ResourceBundle;

import org.apache.log4j.Logger;

import com.appiancorp.suiteapi.common.exceptions.InvalidStateException;

public class AppianMadLibUtil {

  private static final Logger LOG = Logger.getLogger(AppianMadLibUtil.class);

  private static final String CONFIG_BUNDLE_FILE = "com.appiancorp.pluginexample.madlib";
  private static final String CONFIG_PROP_MADLIB_TEXT = "madlib.text";
  private static ResourceBundle BUNDLE;

  static {
    LOG.info("Loading MadLib configuration");
    try {
      BUNDLE = ResourceBundle.getBundle(CONFIG_BUNDLE_FILE);
      LOG.info("Configuration loaded successfully");
    } catch (Exception e) {
      LOG.error("Unable to load madlib configuration from bundle file " + CONFIG_BUNDLE_FILE);
    }
  }

  /**
   * Inserts the MadLib answers into the text template
   * 
   * @param lib MadLib answers
   * @return the generated MadLib text from the template
   */
  protected static String createMadLibText(AppianMadLib lib) throws InvalidStateException{
    if (null == BUNDLE){
      throw new InvalidStateException("MadLibs config not loaded. Check logs for the problem.");
    }
    String templateText = BUNDLE.getString(CONFIG_PROP_MADLIB_TEXT);    
    if (LOG.isDebugEnabled()){
      LOG.debug("Generating madlib using template text "+templateText);
    }
    return String.format(templateText, getValuesFromMadLib(lib));    
  }

  //
  
  /**
   * Utility method to convert the MadLib answers into a text array for insertion
   * into the text template  
   * 
   * Ideally this would be placed inside the AppianMadLib class but we've moved it here so 
   * the AppianMadLib class can be auto-generated without losing any custom code
   *  
   * @param lib MadLib answers
   * @return array of answers (Strings)
   */
  private static Object[] getValuesFromMadLib(AppianMadLib lib) {
    String values[] = {lib.getAdjective1(), lib.getNoun1(), lib.getAdjective2(), lib.getNoun2(),
      lib.getAdjective3(), lib.getNoun3()};
    return values;
  }
}
