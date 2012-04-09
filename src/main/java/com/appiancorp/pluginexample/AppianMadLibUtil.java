package com.appiancorp.pluginexample;

import java.util.Formatter;
import java.util.Locale;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;

import com.appian.types.pluginexample.AppianMadLib;

public class AppianMadLibUtil {

  private static final Logger LOG = Logger.getLogger(AppianMadLibUtil.class);

  private static final String CONFIG_BUNDLE_FILE = "com.appiancorp.pluginexample.madlib";
  private static final String MADLIB_TEXT_KEY = "madlib.text";
  private static ResourceBundle BUNDLE;

  static {
    LOG.info("Initializing configuration");
    try {
      BUNDLE = ResourceBundle.getBundle(CONFIG_BUNDLE_FILE);
      LOG.info("Initialized configuration");
    } catch (Exception e) {
      LOG.fatal("Unable to load madlib configuration from file specified by bundle key " + CONFIG_BUNDLE_FILE);
    }
  }

  protected static String createMadLibText(AppianMadLib lib) {
    String formatText = BUNDLE.getString(MADLIB_TEXT_KEY);
    StringBuilder sb = new StringBuilder();
    // Send all output to the Appendable object sb
    Formatter formatter = new Formatter(sb, Locale.US);
    formatter.format(formatText, getValuesFromMadLib(lib));
    return sb.toString();
  }

  // Ideally this would be placed inside the AppianMadLib class
  // but we've moved it here so the AppianMadLib class can be auto-generated
  // without losing any custom code/
  private static Object[] getValuesFromMadLib(AppianMadLib lib) {
    String values[] = {lib.getAdjective1(), lib.getNoun1(), lib.getAdjective2(), lib.getNoun2(),
      lib.getAdjective3(), lib.getNoun3()};
    return values;
  }
}
