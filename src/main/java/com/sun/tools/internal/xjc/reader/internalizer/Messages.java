/*    */ package com.sun.tools.internal.xjc.reader.internalizer;
/*    */ 
/*    */ import java.text.MessageFormat;
/*    */ import java.util.ResourceBundle;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ class Messages
/*    */ {
/*    */   static final String ERR_INCORRECT_SCHEMA_REFERENCE = "Internalizer.IncorrectSchemaReference";
/*    */   static final String ERR_XPATH_EVAL = "Internalizer.XPathEvaluationError";
/*    */   static final String NO_XPATH_EVAL_TO_NO_TARGET = "Internalizer.XPathEvaluatesToNoTarget";
/*    */   static final String NO_XPATH_EVAL_TOO_MANY_TARGETS = "Internalizer.XPathEvaulatesToTooManyTargets";
/*    */   static final String NO_XPATH_EVAL_TO_NON_ELEMENT = "Internalizer.XPathEvaluatesToNonElement";
/*    */   static final String XPATH_EVAL_TO_NON_SCHEMA_ELEMENT = "Internalizer.XPathEvaluatesToNonSchemaElement";
/*    */   static final String SCD_NOT_ENABLED = "SCD_NOT_ENABLED";
/*    */   static final String ERR_SCD_EVAL = "ERR_SCD_EVAL";
/*    */   static final String ERR_SCD_EVALUATED_EMPTY = "ERR_SCD_EVALUATED_EMPTY";
/*    */   static final String ERR_SCD_MATCHED_MULTIPLE_NODES = "ERR_SCD_MATCHED_MULTIPLE_NODES";
/*    */   static final String ERR_SCD_MATCHED_MULTIPLE_NODES_FIRST = "ERR_SCD_MATCHED_MULTIPLE_NODES_FIRST";
/*    */   static final String ERR_SCD_MATCHED_MULTIPLE_NODES_SECOND = "ERR_SCD_MATCHED_MULTIPLE_NODES_SECOND";
/*    */   static final String CONTEXT_NODE_IS_NOT_ELEMENT = "Internalizer.ContextNodeIsNotElement";
/*    */   static final String ERR_INCORRECT_VERSION = "Internalizer.IncorrectVersion";
/*    */   static final String ERR_VERSION_NOT_FOUND = "Internalizer.VersionNotPresent";
/*    */   static final String TWO_VERSION_ATTRIBUTES = "Internalizer.TwoVersionAttributes";
/*    */   static final String ORPHANED_CUSTOMIZATION = "Internalizer.OrphanedCustomization";
/*    */   static final String ERR_UNABLE_TO_PARSE = "AbstractReferenceFinderImpl.UnableToParse";
/*    */   static final String ERR_FILENAME_IS_NOT_URI = "ERR_FILENAME_IS_NOT_URI";
/*    */   static final String ERR_GENERAL_SCHEMA_CORRECTNESS_ERROR = "ERR_GENERAL_SCHEMA_CORRECTNESS_ERROR";
/*    */   static final String DOMFOREST_INPUTSOURCE_IOEXCEPTION = "DOMFOREST_INPUTSOURCE_IOEXCEPTION";
/*    */   
/*    */   static String format(String property, Object... args) {
/* 38 */     String text = ResourceBundle.getBundle(Messages.class.getPackage().getName() + ".MessageBundle").getString(property);
/* 39 */     return MessageFormat.format(text, args);
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\xjc\reader\internalizer\Messages.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */