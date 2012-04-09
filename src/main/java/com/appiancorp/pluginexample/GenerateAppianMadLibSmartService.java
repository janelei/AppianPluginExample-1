package com.appiancorp.pluginexample;

import java.io.PrintStream;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.appian.types.pluginexample.AppianMadLib;
import com.appiancorp.suiteapi.common.Name;
import com.appiancorp.suiteapi.common.exceptions.PrivilegeException;
import com.appiancorp.suiteapi.content.Content;
import com.appiancorp.suiteapi.content.ContentOutputStream;
import com.appiancorp.suiteapi.content.ContentService;
import com.appiancorp.suiteapi.knowledge.Document;
import com.appiancorp.suiteapi.knowledge.DocumentDataType;
import com.appiancorp.suiteapi.knowledge.FolderDataType;
import com.appiancorp.suiteapi.personalization.UserProfile;
import com.appiancorp.suiteapi.personalization.UserProfileService;
import com.appiancorp.suiteapi.process.exceptions.SmartServiceException;
import com.appiancorp.suiteapi.process.framework.AppianSmartService;
import com.appiancorp.suiteapi.process.framework.Input;
import com.appiancorp.suiteapi.process.framework.MessageContainer;
import com.appiancorp.suiteapi.process.framework.Required;
import com.appiancorp.suiteapi.process.framework.SmartServiceContext;
import com.appiancorp.suiteapi.process.palette.PaletteInfo;

@PaletteInfo(paletteCategory = "Integration Services", palette = "Connectivity Services")
public class GenerateAppianMadLibSmartService extends AppianSmartService {

  private static final Logger LOG = Logger.getLogger(GenerateAppianMadLibSmartService.class);
  private static final String INPUT_NAME_MADLIB = "madlib";
  private static final String INPUT_NAME_FOLDER = "saveIn";
  private static final String OUTPUT_NAME_NEWDOC = "madlibDoc";
  private static final String DOC_EXTENSION = ".txt";
  private final SmartServiceContext smartServiceCtx;
  private ContentService cs;
  private UserProfileService us;
  private AppianMadLib madlib;
  private Long saveIn;
  private Long madlibDoc;

  @Override
  public void run() throws SmartServiceException {
    String text = AppianMadLibUtil.createMadLibText(madlib);    
    UserProfile author = us.getUser(smartServiceCtx.getUsername());
    String username = author.getUsername();
    String userFullName = author.getFirstName()+" "+author.getLastName(); 
    String docName = username +"_"+madlib.getTitle();
    Document newDoc = new Document(saveIn, docName, DOC_EXTENSION);
    try {
      ContentOutputStream madlibOS = cs.upload(newDoc, Content.UNIQUE_NONE);   
      madlibDoc = madlibOS.getContentId();
      PrintStream ps = new PrintStream(madlibOS);      
      ps.println(text);
      ps.println("By "+userFullName);
      ps.println("Created "+new Date());
      ps.close();      
    } catch (PrivilegeException e) {
      LOG.error("User "+smartServiceCtx.getUsername()+" did not have permission to write to folder id="+saveIn, e);
      throw createException(e, "error.exception.permission", smartServiceCtx.getUsername(), saveIn);
    } catch (Exception e) {
      LOG.error("Unexpected error creating madlib doc in folder id="+saveIn, e);
      throw createException(e, "error.exception.unknown", smartServiceCtx.getUsername(), saveIn);
    }
    
  }
  public GenerateAppianMadLibSmartService(SmartServiceContext smartServiceCtx, ContentService cs, UserProfileService us) {
    super();
    this.smartServiceCtx = smartServiceCtx;
    this.cs = cs;
    this.us = us;
  }

  public void onSave(MessageContainer messages) {
  }

  public void validate(MessageContainer messages) {
    if (StringUtils.isBlank(madlib.getTitle())){
      messages.addError(INPUT_NAME_MADLIB, "error.validation.title.missing");

    }
    if (StringUtils.isBlank(madlib.getNoun1()) || StringUtils.isBlank(madlib.getNoun2()) ||
      StringUtils.isBlank(madlib.getNoun3())) {
      messages.addError(INPUT_NAME_MADLIB, "error.validation.noun.missing");
    }

    if (StringUtils.isBlank(madlib.getAdjective1()) || StringUtils.isBlank(madlib.getAdjective2()) ||
      StringUtils.isBlank(madlib.getAdjective3())) {
      messages.addError(INPUT_NAME_MADLIB, "error.validation.adjective.missing");
    }
  }

  private SmartServiceException createException(Throwable t, String key, Object... args) {
    return new SmartServiceException.Builder(getClass(), t).userMessage(key, args).build();
  }

  @Input(required = Required.ALWAYS)
  @Name(INPUT_NAME_MADLIB)
  public void setMadlib(AppianMadLib val) {
    this.madlib = val;
  }

  @Input(required = Required.ALWAYS)
  @Name(INPUT_NAME_FOLDER)
  @FolderDataType
  public void setSaveIn(Long val) {
    this.saveIn = val;
  }

  @Name(OUTPUT_NAME_NEWDOC)
  @DocumentDataType
  public Long getMadlibDoc() {
    return madlibDoc;
  }

}
