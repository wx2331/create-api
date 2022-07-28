/*    */ package com.sun.tools.internal.xjc;
/*    */ 
/*    */ import java.text.MessageFormat;
/*    */ import java.util.ResourceBundle;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Messages
/*    */ {
/*    */   static final String UNKNOWN_LOCATION = "ConsoleErrorReporter.UnknownLocation";
/*    */   static final String LINE_X_OF_Y = "ConsoleErrorReporter.LineXOfY";
/*    */   static final String UNKNOWN_FILE = "ConsoleErrorReporter.UnknownFile";
/*    */   static final String DRIVER_PUBLIC_USAGE = "Driver.Public.Usage";
/*    */   static final String DRIVER_PRIVATE_USAGE = "Driver.Private.Usage";
/*    */   static final String ADDON_USAGE = "Driver.AddonUsage";
/*    */   static final String EXPERIMENTAL_LANGUAGE_WARNING = "Driver.ExperimentalLanguageWarning";
/*    */   static final String NON_EXISTENT_DIR = "Driver.NonExistentDir";
/*    */   static final String MISSING_MODE_OPERAND = "Driver.MissingModeOperand";
/*    */   static final String MISSING_PROXY = "Driver.MISSING_PROXY";
/*    */   static final String MISSING_PROXYFILE = "Driver.MISSING_PROXYFILE";
/*    */   static final String NO_SUCH_FILE = "Driver.NO_SUCH_FILE";
/*    */   static final String ILLEGAL_PROXY = "Driver.ILLEGAL_PROXY";
/*    */   static final String ILLEGAL_TARGET_VERSION = "Driver.ILLEGAL_TARGET_VERSION";
/*    */   static final String MISSING_OPERAND = "Driver.MissingOperand";
/*    */   static final String MISSING_PROXYHOST = "Driver.MissingProxyHost";
/*    */   static final String MISSING_PROXYPORT = "Driver.MissingProxyPort";
/*    */   static final String STACK_OVERFLOW = "Driver.StackOverflow";
/*    */   static final String UNRECOGNIZED_MODE = "Driver.UnrecognizedMode";
/*    */   static final String UNRECOGNIZED_PARAMETER = "Driver.UnrecognizedParameter";
/*    */   static final String UNSUPPORTED_ENCODING = "Driver.UnsupportedEncoding";
/*    */   static final String MISSING_GRAMMAR = "Driver.MissingGrammar";
/*    */   static final String PARSING_SCHEMA = "Driver.ParsingSchema";
/*    */   static final String PARSE_FAILED = "Driver.ParseFailed";
/*    */   
/*    */   public static String format(String property, Object... args) {
/* 38 */     String text = ResourceBundle.getBundle(Messages.class.getPackage().getName() + ".MessageBundle").getString(property);
/* 39 */     return MessageFormat.format(text, args);
/*    */   }
/*    */   
/*    */   static final String COMPILING_SCHEMA = "Driver.CompilingSchema";
/*    */   static final String FAILED_TO_GENERATE_CODE = "Driver.FailedToGenerateCode";
/*    */   static final String FILE_PROLOG_COMMENT = "Driver.FilePrologComment";
/*    */   static final String DATE_FORMAT = "Driver.DateFormat";
/*    */   static final String TIME_FORMAT = "Driver.TimeFormat";
/*    */   static final String AT = "Driver.At";
/*    */   static final String VERSION = "Driver.Version";
/*    */   static final String FULLVERSION = "Driver.FullVersion";
/*    */   static final String BUILD_ID = "Driver.BuildID";
/*    */   static final String ERROR_MSG = "Driver.ErrorMessage";
/*    */   static final String WARNING_MSG = "Driver.WarningMessage";
/*    */   static final String INFO_MSG = "Driver.InfoMessage";
/*    */   static final String ERR_NOT_A_BINDING_FILE = "Driver.NotABindingFile";
/*    */   static final String ERR_TOO_MANY_SCHEMA = "ModelLoader.TooManySchema";
/*    */   static final String ERR_BINDING_FILE_NOT_SUPPORTED_FOR_RNC = "ModelLoader.BindingFileNotSupportedForRNC";
/*    */   static final String DEFAULT_VERSION = "Driver.DefaultVersion";
/*    */   static final String DEFAULT_PACKAGE_WARNING = "Driver.DefaultPackageWarning";
/*    */   static final String NOT_A_VALID_FILENAME = "Driver.NotAValidFileName";
/*    */   static final String FAILED_TO_PARSE = "Driver.FailedToParse";
/*    */   static final String NOT_A_FILE_NOR_URL = "Driver.NotAFileNorURL";
/*    */   static final String FIELD_RENDERER_CONFLICT = "FIELD_RENDERER_CONFLICT";
/*    */   static final String NAME_CONVERTER_CONFLICT = "NAME_CONVERTER_CONFLICT";
/*    */   static final String FAILED_TO_LOAD = "FAILED_TO_LOAD";
/*    */   static final String PLUGIN_LOAD_FAILURE = "PLUGIN_LOAD_FAILURE";
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\xjc\Messages.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */