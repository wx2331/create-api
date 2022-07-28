/*    */ package com.sun.tools.internal.xjc.reader.xmlschema;
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
/*    */ public class Messages
/*    */ {
/*    */   static final String WARN_NO_GLOBAL_ELEMENT = "BGMBuilder.NoGlobalElement";
/*    */   public static final String WARN_UNUSED_EXPECTED_CONTENT_TYPES = "UnusedCustomizationChecker.WarnUnusedExpectedContentTypes";
/*    */   static final String ERR_MULTIPLE_SCHEMA_BINDINGS = "BGMBuilder.MultipleSchemaBindings";
/*    */   static final String ERR_MULTIPLE_SCHEMA_BINDINGS_LOCATION = "BGMBuilder.MultipleSchemaBindings.Location";
/*    */   static final String JAVADOC_HEADING = "ClassSelector.JavadocHeading";
/*    */   static final String ERR_RESERVED_CLASS_NAME = "ClassSelector.ReservedClassName";
/*    */   static final String ERR_CLASS_NAME_IS_REQUIRED = "ClassSelector.ClassNameIsRequired";
/*    */   static final String ERR_INCORRECT_CLASS_NAME = "ClassSelector.IncorrectClassName";
/*    */   static final String ERR_INCORRECT_PACKAGE_NAME = "ClassSelector.IncorrectPackageName";
/*    */   static final String ERR_CANNOT_BE_TYPE_SAFE_ENUM = "ConversionFinder.CannotBeTypeSafeEnum";
/*    */   static final String ERR_CANNOT_BE_TYPE_SAFE_ENUM_LOCATION = "ConversionFinder.CannotBeTypeSafeEnum.Location";
/*    */   static final String ERR_NO_ENUM_NAME_AVAILABLE = "ConversionFinder.NoEnumNameAvailable";
/*    */   static final String ERR_NO_ENUM_FACET = "ConversionFinder.NoEnumFacet";
/*    */   static final String ERR_ILLEGAL_EXPECTED_MIME_TYPE = "ERR_ILLEGAL_EXPECTED_MIME_TYPE";
/*    */   static final String ERR_DATATYPE_ERROR = "DatatypeBuilder.DatatypeError";
/*    */   static final String ERR_UNABLE_TO_GENERATE_NAME_FROM_MODELGROUP = "DefaultParticleBinder.UnableToGenerateNameFromModelGroup";
/*    */   static final String ERR_INCORRECT_FIXED_VALUE = "FieldBuilder.IncorrectFixedValue";
/*    */   static final String ERR_INCORRECT_DEFAULT_VALUE = "FieldBuilder.IncorrectDefaultValue";
/*    */   static final String ERR_CONFLICT_BETWEEN_USERTYPE_AND_ACTUALTYPE_ATTUSE = "FieldBuilder.ConflictBetweenUserTypeAndActualType.AttUse";
/*    */   static final String ERR_CONFLICT_BETWEEN_USERTYPE_AND_ACTUALTYPE_ATTUSE_SOURCE = "FieldBuilder.ConflictBetweenUserTypeAndActualType.AttUse.Source";
/*    */   
/*    */   public static String format(String property, Object... args) {
/* 38 */     String text = ResourceBundle.getBundle(Messages.class.getPackage().getName() + ".MessageBundle").getString(property);
/* 39 */     return MessageFormat.format(text, args);
/*    */   }
/*    */   
/*    */   static final String ERR_UNNESTED_JAVATYPE_CUSTOMIZATION_ON_SIMPLETYPE = "SimpleTypeBuilder.UnnestedJavaTypeCustomization";
/*    */   static final String JAVADOC_NIL_PROPERTY = "FieldBuilder.Javadoc.NilProperty";
/*    */   static final String JAVADOC_LINE_UNKNOWN = "ClassSelector.JavadocLineUnknown";
/*    */   static final String JAVADOC_VALUEOBJECT_PROPERTY = "FieldBuilder.Javadoc.ValueObject";
/*    */   static final String MSG_COLLISION_INFO = "CollisionInfo.CollisionInfo";
/*    */   static final String MSG_UNKNOWN_FILE = "CollisionInfo.UnknownFile";
/*    */   static final String MSG_LINE_X_OF_Y = "CollisionInfo.LineXOfY";
/*    */   static final String MSG_FALLBACK_JAVADOC = "DefaultParticleBinder.FallbackJavadoc";
/*    */   static final String ERR_ENUM_MEMBER_NAME_COLLISION = "ERR_ENUM_MEMBER_NAME_COLLISION";
/*    */   static final String ERR_ENUM_MEMBER_NAME_COLLISION_RELATED = "ERR_ENUM_MEMBER_NAME_COLLISION_RELATED";
/*    */   static final String ERR_CANNOT_GENERATE_ENUM_NAME = "ERR_CANNOT_GENERATE_ENUM_NAME";
/*    */   static final String WARN_ENUM_MEMBER_SIZE_CAP = "WARN_ENUM_MEMBER_SIZE_CAP";
/*    */   public static final String ERR_UNACKNOWLEDGED_CUSTOMIZATION = "UnusedCustomizationChecker.UnacknolwedgedCustomization";
/*    */   public static final String ERR_UNACKNOWLEDGED_CUSTOMIZATION_LOCATION = "UnusedCustomizationChecker.UnacknolwedgedCustomization.Relevant";
/*    */   public static final String ERR_MULTIPLE_GLOBAL_BINDINGS = "ERR_MULTIPLE_GLOBAL_BINDINGS";
/*    */   public static final String ERR_MULTIPLE_GLOBAL_BINDINGS_OTHER = "ERR_MULTIPLE_GLOBAL_BINDINGS_OTHER";
/*    */   public static final String ERR_REFERENCE_TO_NONEXPORTED_CLASS = "ERR_REFERENCE_TO_NONEXPORTED_CLASS";
/*    */   public static final String ERR_REFERENCE_TO_NONEXPORTED_CLASS_MAP_FALSE = "ERR_REFERENCE_TO_NONEXPORTED_CLASS_MAP_FALSE";
/*    */   public static final String ERR_REFERENCE_TO_NONEXPORTED_CLASS_REFERER = "ERR_REFERENCE_TO_NONEXPORTED_CLASS_REFERER";
/*    */   static final String WARN_DEFAULT_VALUE_PRIMITIVE_TYPE = "WARN_DEFAULT_VALUE_PRIMITIVE_TYPE";
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\xjc\reader\xmlschema\Messages.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */