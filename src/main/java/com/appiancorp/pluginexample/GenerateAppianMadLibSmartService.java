package com.appiancorp.pluginexample;

import java.io.PrintWriter;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

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
import com.appiancorp.suiteapi.process.palette.ConnectivityServices;

@ConnectivityServices
public class GenerateAppianMadLibSmartService extends AppianSmartService {

  private static final Logger LOG = Logger.getLogger(GenerateAppianMadLibSmartService.class);

  private static final String INPUT_NAME_MADLIB = "madlib";
  private static final String INPUT_NAME_FOLDER = "saveIn";
  private static final String OUTPUT_NAME_NEWDOC = "madlibDoc";
  private static final String DOC_EXTENSION = "txt";

  private final SmartServiceContext smartServiceCtx;
  private ContentService cs;
  private UserProfileService us;
  private AppianMadLib madlib;
  private Long saveIn;
  private Long madlibDoc;

  @Override
  public void run() throws SmartServiceException {
    // All the important info about the user context and process context is included in the smartServiceCtx
    UserProfile author = us.getUser(smartServiceCtx.getUsername());
    String username = author.getUsername();

    // Create a filename based on the user's name and MadLib title.
    String userFullName = author.getFirstName() + " " + author.getLastName();
    String docName = username + " " + madlib.getTitle();

    if (LOG.isDebugEnabled()) {
      LOG.debug(String.format("Creating new document in folder %d with name %s and ext %s", saveIn, docName,
        DOC_EXTENSION));
    }
    // Instantiate a new Document object with the necessary metadata
    Document newDoc = new Document(saveIn, docName, DOC_EXTENSION);
    try {
      // Creates a new document in the Appian engine. At this point the document is empty.
      // Content.UNIQUE_NONE specifies that we don't need to worry about document name uniqueness
      ContentOutputStream madlibOS = cs.upload(newDoc, Content.UNIQUE_NONE);

      // Save the id of the new document as our smartservice output
      madlibDoc = madlibOS.getContentId();

      // Write the madlib text and some footer information to the new document file on disk
      PrintWriter pw = new PrintWriter(madlibOS);
      pw.println(AppianMadLibUtil.createMadLibText(madlib));
      pw.println("By " + userFullName);
      pw.println("Created " + new Date());
      pw.close();

    } catch (PrivilegeException e) {
      LOG.error(
        String.format("User %s did not have permission to write to folder [id=%d]", username, saveIn), e);
      throw createException(e, "error.exception.permission", smartServiceCtx.getUsername(), saveIn);
    } catch (Exception e) {
      LOG.error(
        String.format("Unexpected error creating new doc in folder [id=%d] as user %s", saveIn, username), e);
      throw createException(e, "error.exception.unknown", smartServiceCtx.getUsername(), saveIn);
    }
  }

  public GenerateAppianMadLibSmartService(SmartServiceContext smartServiceCtx, ContentService cs,
    UserProfileService us) {
    super();
    this.smartServiceCtx = smartServiceCtx;
    this.cs = cs;
    this.us = us;
  }

  @Override
  public void onSave(MessageContainer messages) {
    // nothing special we need to do here
  }

  /**
   * Executes before run(). Validates for any input problems before trying to do any actual work.
   * If the node is attended, the errors will be shown to the user.
   * If unattended, the errors will be sent as an alert to the Designer.
   * 
   * @param messages
   *          The container of validation messages that will be sent back to the system
   */
  @Override
  public void validate(MessageContainer messages) {
    // Verify that they user entered a title
    if (StringUtils.isBlank(madlib.getTitle())) {
      messages.addError(INPUT_NAME_MADLIB, "error.validation.title.missing");
    }
    // Verify that they user entered all the nouns
    if (StringUtils.isBlank(madlib.getNoun1()) || StringUtils.isBlank(madlib.getNoun2()) ||
      StringUtils.isBlank(madlib.getNoun3())) {
      messages.addError(INPUT_NAME_MADLIB, "error.validation.noun.missing");
    }
    // Verify that they user entered all the adjectives
    if (StringUtils.isBlank(madlib.getAdjective1()) || StringUtils.isBlank(madlib.getAdjective2()) ||
      StringUtils.isBlank(madlib.getAdjective3())) {
      messages.addError(INPUT_NAME_MADLIB, "error.validation.adjective.missing");
    }
  }

  /**
   * Utility method for creating a SmartServiceException that can be passed back to the Appian framework
   * 
   * @param t
   *          Throwable object
   * @param key
   *          The key in the smart service i18n bundle that indicates the error message to display to the user
   * @param args
   *          The arguments that will be inserted into the error message text
   * @return a SmartServiceExtension
   */
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
