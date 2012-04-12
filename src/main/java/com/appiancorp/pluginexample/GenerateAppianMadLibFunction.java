package com.appiancorp.pluginexample;

import org.apache.log4j.Logger;

import com.appiancorp.services.ServiceContext;
import com.appiancorp.suiteapi.common.exceptions.AppianException;
import com.appiancorp.suiteapi.expression.annotations.Function;
import com.appiancorp.suiteapi.expression.annotations.HiddenCategory;
import com.appiancorp.suiteapi.expression.annotations.Parameter;

//@AppianScriptingFunctionsCategory
//This is how to deprecate a function:
@HiddenCategory
public class GenerateAppianMadLibFunction {

  private static final Logger LOG = Logger.getLogger(GenerateAppianMadLibFunction.class);

  @Function
  public String GenerateAppianMadLibText(ServiceContext sc, @Parameter AppianMadLib madlib)
    throws AppianException {
    if (LOG.isDebugEnabled()) {
      LOG.debug("Generating AppianMadLib for " + sc.getIdentity().getIdentity());
    }
    return AppianMadLibUtil.createMadLibText(madlib);
  }
}
