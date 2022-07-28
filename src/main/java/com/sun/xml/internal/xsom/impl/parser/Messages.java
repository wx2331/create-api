/*    */ package com.sun.xml.internal.xsom.impl.parser;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Messages
/*    */ {
/*    */   public static final String ERR_UNDEFINED_SIMPLETYPE = "UndefinedSimpleType";
/*    */   public static final String ERR_UNDEFINED_COMPLEXTYPE = "UndefinedCompplexType";
/*    */   public static final String ERR_UNDEFINED_TYPE = "UndefinedType";
/*    */   public static final String ERR_UNDEFINED_ELEMENT = "UndefinedElement";
/*    */   public static final String ERR_UNDEFINED_MODELGROUP = "UndefinedModelGroup";
/*    */   public static final String ERR_UNDEFINED_ATTRIBUTE = "UndefinedAttribute";
/*    */   public static final String ERR_UNDEFINED_ATTRIBUTEGROUP = "UndefinedAttributeGroup";
/*    */   public static final String ERR_UNDEFINED_IDENTITY_CONSTRAINT = "UndefinedIdentityConstraint";
/*    */   public static final String ERR_UNDEFINED_PREFIX = "UndefinedPrefix";
/*    */   public static final String ERR_DOUBLE_DEFINITION = "DoubleDefinition";
/*    */   public static final String ERR_DOUBLE_DEFINITION_ORIGINAL = "DoubleDefinition.Original";
/*    */   public static final String ERR_MISSING_SCHEMALOCATION = "MissingSchemaLocation";
/*    */   public static final String ERR_ENTITY_RESOLUTION_FAILURE = "EntityResolutionFailure";
/*    */   public static final String ERR_SIMPLE_CONTENT_EXPECTED = "SimpleContentExpected";
/*    */   public static final String JAXP_UNSUPPORTED_PROPERTY = "JAXPUnsupportedProperty";
/*    */   public static final String JAXP_SUPPORTED_PROPERTY = "JAXPSupportedProperty";
/*    */   
/*    */   public static String format(String property, Object... args) {
/* 39 */     String text = ResourceBundle.getBundle(Messages.class.getName()).getString(property);
/* 40 */     return MessageFormat.format(text, args);
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\xml\internal\xsom\impl\parser\Messages.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */