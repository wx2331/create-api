/*     */ package com.sun.tools.internal.ws.resources;
/*     */ 
/*     */ import com.sun.istack.internal.localization.Localizable;
/*     */ import com.sun.istack.internal.localization.LocalizableMessageFactory;
/*     */ import com.sun.istack.internal.localization.Localizer;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class ModelMessages
/*     */ {
/*  39 */   private static final LocalizableMessageFactory messageFactory = new LocalizableMessageFactory("com.sun.tools.internal.ws.resources.model");
/*  40 */   private static final Localizer localizer = new Localizer();
/*     */   
/*     */   public static Localizable localizableMODEL_NESTED_MODEL_ERROR(Object arg0) {
/*  43 */     return messageFactory.getMessage("model.nestedModelError", new Object[] { arg0 });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String MODEL_NESTED_MODEL_ERROR(Object arg0) {
/*  51 */     return localizer.localize(localizableMODEL_NESTED_MODEL_ERROR(arg0));
/*     */   }
/*     */   
/*     */   public static Localizable localizableMODEL_EXCEPTION_NOTUNIQUE(Object arg0, Object arg1) {
/*  55 */     return messageFactory.getMessage("model.exception.notunique", new Object[] { arg0, arg1 });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String MODEL_EXCEPTION_NOTUNIQUE(Object arg0, Object arg1) {
/*  63 */     return localizer.localize(localizableMODEL_EXCEPTION_NOTUNIQUE(arg0, arg1));
/*     */   }
/*     */   
/*     */   public static Localizable localizableMODEL_SCHEMA_INVALID_WILDCARD_ALL_COMPOSITOR(Object arg0) {
/*  67 */     return messageFactory.getMessage("model.schema.invalidWildcard.allCompositor", new Object[] { arg0 });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String MODEL_SCHEMA_INVALID_WILDCARD_ALL_COMPOSITOR(Object arg0) {
/*  75 */     return localizer.localize(localizableMODEL_SCHEMA_INVALID_WILDCARD_ALL_COMPOSITOR(arg0));
/*     */   }
/*     */   
/*     */   public static Localizable localizableMODEL_SCHEMA_SIMPLE_TYPE_WITH_FACETS(Object arg0) {
/*  79 */     return messageFactory.getMessage("model.schema.simpleTypeWithFacets", new Object[] { arg0 });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String MODEL_SCHEMA_SIMPLE_TYPE_WITH_FACETS(Object arg0) {
/*  87 */     return localizer.localize(localizableMODEL_SCHEMA_SIMPLE_TYPE_WITH_FACETS(arg0));
/*     */   }
/*     */   
/*     */   public static Localizable localizableMODEL_SCHEMA_UNION_NOT_SUPPORTED(Object arg0) {
/*  91 */     return messageFactory.getMessage("model.schema.unionNotSupported", new Object[] { arg0 });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String MODEL_SCHEMA_UNION_NOT_SUPPORTED(Object arg0) {
/*  99 */     return localizer.localize(localizableMODEL_SCHEMA_UNION_NOT_SUPPORTED(arg0));
/*     */   }
/*     */   
/*     */   public static Localizable localizableMODEL_COMPLEX_TYPE_SIMPLE_CONTENT_RESERVED_NAME(Object arg0) {
/* 103 */     return messageFactory.getMessage("model.complexType.simpleContent.reservedName", new Object[] { arg0 });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String MODEL_COMPLEX_TYPE_SIMPLE_CONTENT_RESERVED_NAME(Object arg0) {
/* 111 */     return localizer.localize(localizableMODEL_COMPLEX_TYPE_SIMPLE_CONTENT_RESERVED_NAME(arg0));
/*     */   }
/*     */   
/*     */   public static Localizable localizableMODEL_IMPORTER_INVALID_ID(Object arg0, Object arg1) {
/* 115 */     return messageFactory.getMessage("model.importer.invalidId", new Object[] { arg0, arg1 });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String MODEL_IMPORTER_INVALID_ID(Object arg0, Object arg1) {
/* 123 */     return localizer.localize(localizableMODEL_IMPORTER_INVALID_ID(arg0, arg1));
/*     */   }
/*     */   
/*     */   public static Localizable localizableMODEL_SCHEMA_JAXB_EXCEPTION_MESSAGE(Object arg0) {
/* 127 */     return messageFactory.getMessage("model.schema.jaxbException.message", new Object[] { arg0 });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String MODEL_SCHEMA_JAXB_EXCEPTION_MESSAGE(Object arg0) {
/* 135 */     return localizer.localize(localizableMODEL_SCHEMA_JAXB_EXCEPTION_MESSAGE(arg0));
/*     */   }
/*     */   
/*     */   public static Localizable localizableMODEL_INVALID_MESSAGE_TYPE(Object arg0) {
/* 139 */     return messageFactory.getMessage("model.invalid.message.type", new Object[] { arg0 });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String MODEL_INVALID_MESSAGE_TYPE(Object arg0) {
/* 147 */     return localizer.localize(localizableMODEL_INVALID_MESSAGE_TYPE(arg0));
/*     */   }
/*     */   
/*     */   public static Localizable localizableCONSOLE_ERROR_REPORTER_UNKNOWN_LOCATION() {
/* 151 */     return messageFactory.getMessage("ConsoleErrorReporter.UnknownLocation", new Object[0]);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String CONSOLE_ERROR_REPORTER_UNKNOWN_LOCATION() {
/* 159 */     return localizer.localize(localizableCONSOLE_ERROR_REPORTER_UNKNOWN_LOCATION());
/*     */   }
/*     */   
/*     */   public static Localizable localizableMODEL_EXPORTER_UNSUPPORTED_CLASS(Object arg0) {
/* 163 */     return messageFactory.getMessage("model.exporter.unsupportedClass", new Object[] { arg0 });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String MODEL_EXPORTER_UNSUPPORTED_CLASS(Object arg0) {
/* 171 */     return localizer.localize(localizableMODEL_EXPORTER_UNSUPPORTED_CLASS(arg0));
/*     */   }
/*     */   
/*     */   public static Localizable localizableMODEL_SCHEMA_ELEMENT_NOT_FOUND(Object arg0) {
/* 175 */     return messageFactory.getMessage("model.schema.elementNotFound", new Object[] { arg0 });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String MODEL_SCHEMA_ELEMENT_NOT_FOUND(Object arg0) {
/* 183 */     return localizer.localize(localizableMODEL_SCHEMA_ELEMENT_NOT_FOUND(arg0));
/*     */   }
/*     */   
/*     */   public static Localizable localizableMODEL_UNIQUENESS_JAVASTRUCTURETYPE(Object arg0, Object arg1) {
/* 187 */     return messageFactory.getMessage("model.uniqueness.javastructuretype", new Object[] { arg0, arg1 });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String MODEL_UNIQUENESS_JAVASTRUCTURETYPE(Object arg0, Object arg1) {
/* 195 */     return localizer.localize(localizableMODEL_UNIQUENESS_JAVASTRUCTURETYPE(arg0, arg1));
/*     */   }
/*     */   
/*     */   public static Localizable localizableMODEL_SAXPARSER_EXCEPTION(Object arg0, Object arg1) {
/* 199 */     return messageFactory.getMessage("model.saxparser.exception", new Object[] { arg0, arg1 });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String MODEL_SAXPARSER_EXCEPTION(Object arg0, Object arg1) {
/* 208 */     return localizer.localize(localizableMODEL_SAXPARSER_EXCEPTION(arg0, arg1));
/*     */   }
/*     */   
/*     */   public static Localizable localizableMODEL_SCHEMA_UNSUPPORTED_TYPE(Object arg0, Object arg1, Object arg2) {
/* 212 */     return messageFactory.getMessage("model.schema.unsupportedType", new Object[] { arg0, arg1, arg2 });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String MODEL_SCHEMA_UNSUPPORTED_TYPE(Object arg0, Object arg1, Object arg2) {
/* 220 */     return localizer.localize(localizableMODEL_SCHEMA_UNSUPPORTED_TYPE(arg0, arg1, arg2));
/*     */   }
/*     */   
/*     */   public static Localizable localizableMODEL_SCHEMA_LIST_NOT_SUPPORTED(Object arg0) {
/* 224 */     return messageFactory.getMessage("model.schema.listNotSupported", new Object[] { arg0 });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String MODEL_SCHEMA_LIST_NOT_SUPPORTED(Object arg0) {
/* 232 */     return localizer.localize(localizableMODEL_SCHEMA_LIST_NOT_SUPPORTED(arg0));
/*     */   }
/*     */   
/*     */   public static Localizable localizableMODEL_ARRAYWRAPPER_NO_PARENT() {
/* 236 */     return messageFactory.getMessage("model.arraywrapper.no.parent", new Object[0]);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String MODEL_ARRAYWRAPPER_NO_PARENT() {
/* 244 */     return localizer.localize(localizableMODEL_ARRAYWRAPPER_NO_PARENT());
/*     */   }
/*     */   
/*     */   public static Localizable localizableMODEL_PARENT_TYPE_ALREADY_SET(Object arg0, Object arg1, Object arg2) {
/* 248 */     return messageFactory.getMessage("model.parent.type.already.set", new Object[] { arg0, arg1, arg2 });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String MODEL_PARENT_TYPE_ALREADY_SET(Object arg0, Object arg1, Object arg2) {
/* 256 */     return localizer.localize(localizableMODEL_PARENT_TYPE_ALREADY_SET(arg0, arg1, arg2));
/*     */   }
/*     */   
/*     */   public static Localizable localizableMODEL_ARRAYWRAPPER_NO_SUBTYPES() {
/* 260 */     return messageFactory.getMessage("model.arraywrapper.no.subtypes", new Object[0]);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String MODEL_ARRAYWRAPPER_NO_SUBTYPES() {
/* 268 */     return localizer.localize(localizableMODEL_ARRAYWRAPPER_NO_SUBTYPES());
/*     */   }
/*     */   
/*     */   public static Localizable localizableMODEL_IMPORTER_INVALID_MINOR_MINOR_OR_PATCH_VERSION(Object arg0, Object arg1, Object arg2) {
/* 272 */     return messageFactory.getMessage("model.importer.invalidMinorMinorOrPatchVersion", new Object[] { arg0, arg1, arg2 });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String MODEL_IMPORTER_INVALID_MINOR_MINOR_OR_PATCH_VERSION(Object arg0, Object arg1, Object arg2) {
/* 280 */     return localizer.localize(localizableMODEL_IMPORTER_INVALID_MINOR_MINOR_OR_PATCH_VERSION(arg0, arg1, arg2));
/*     */   }
/*     */   
/*     */   public static Localizable localizableMODEL_SCHEMA_CIRCULARITY(Object arg0) {
/* 284 */     return messageFactory.getMessage("model.schema.circularity", new Object[] { arg0 });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String MODEL_SCHEMA_CIRCULARITY(Object arg0) {
/* 292 */     return localizer.localize(localizableMODEL_SCHEMA_CIRCULARITY(arg0));
/*     */   }
/*     */   
/*     */   public static Localizable localizableMODEL_ARRAYWRAPPER_MEMBER_ALREADY_SET() {
/* 296 */     return messageFactory.getMessage("model.arraywrapper.member.already.set", new Object[0]);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String MODEL_ARRAYWRAPPER_MEMBER_ALREADY_SET() {
/* 304 */     return localizer.localize(localizableMODEL_ARRAYWRAPPER_MEMBER_ALREADY_SET());
/*     */   }
/*     */   
/*     */   public static Localizable localizableMODEL_IMPORTER_INVALID_CLASS(Object arg0, Object arg1) {
/* 308 */     return messageFactory.getMessage("model.importer.invalidClass", new Object[] { arg0, arg1 });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String MODEL_IMPORTER_INVALID_CLASS(Object arg0, Object arg1) {
/* 316 */     return localizer.localize(localizableMODEL_IMPORTER_INVALID_CLASS(arg0, arg1));
/*     */   }
/*     */   
/*     */   public static Localizable localizableMODEL_IMPORTER_INVALID_VERSION(Object arg0, Object arg1) {
/* 320 */     return messageFactory.getMessage("model.importer.invalidVersion", new Object[] { arg0, arg1 });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String MODEL_IMPORTER_INVALID_VERSION(Object arg0, Object arg1) {
/* 328 */     return localizer.localize(localizableMODEL_IMPORTER_INVALID_VERSION(arg0, arg1));
/*     */   }
/*     */   
/*     */   public static Localizable localizableCONSOLE_ERROR_REPORTER_LINE_X_OF_Y(Object arg0, Object arg1) {
/* 332 */     return messageFactory.getMessage("ConsoleErrorReporter.LineXOfY", new Object[] { arg0, arg1 });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String CONSOLE_ERROR_REPORTER_LINE_X_OF_Y(Object arg0, Object arg1) {
/* 340 */     return localizer.localize(localizableCONSOLE_ERROR_REPORTER_LINE_X_OF_Y(arg0, arg1));
/*     */   }
/*     */   
/*     */   public static Localizable localizableMODEL_PARAMETER_NOTUNIQUE(Object arg0, Object arg1) {
/* 344 */     return messageFactory.getMessage("model.parameter.notunique", new Object[] { arg0, arg1 });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String MODEL_PARAMETER_NOTUNIQUE(Object arg0, Object arg1) {
/* 354 */     return localizer.localize(localizableMODEL_PARAMETER_NOTUNIQUE(arg0, arg1));
/*     */   }
/*     */   
/*     */   public static Localizable localizableMODEL_SCHEMA_INVALID_SIMPLE_TYPE_INVALID_ITEM_TYPE(Object arg0, Object arg1) {
/* 358 */     return messageFactory.getMessage("model.schema.invalidSimpleType.invalidItemType", new Object[] { arg0, arg1 });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String MODEL_SCHEMA_INVALID_SIMPLE_TYPE_INVALID_ITEM_TYPE(Object arg0, Object arg1) {
/* 366 */     return localizer.localize(localizableMODEL_SCHEMA_INVALID_SIMPLE_TYPE_INVALID_ITEM_TYPE(arg0, arg1));
/*     */   }
/*     */   
/*     */   public static Localizable localizableMODEL_UNIQUENESS() {
/* 370 */     return messageFactory.getMessage("model.uniqueness", new Object[0]);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String MODEL_UNIQUENESS() {
/* 378 */     return localizer.localize(localizableMODEL_UNIQUENESS());
/*     */   }
/*     */   
/*     */   public static Localizable localizable_002F_002F_REPLACEMENT() {
/* 382 */     return messageFactory.getMessage("//replacement", new Object[0]);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String _002F_002F_REPLACEMENT() {
/* 390 */     return localizer.localize(localizable_002F_002F_REPLACEMENT());
/*     */   }
/*     */   
/*     */   public static Localizable localizableMODEL_SCHEMA_INVALID_SIMPLE_TYPE_NO_ITEM_LITERAL_TYPE(Object arg0, Object arg1) {
/* 394 */     return messageFactory.getMessage("model.schema.invalidSimpleType.noItemLiteralType", new Object[] { arg0, arg1 });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String MODEL_SCHEMA_INVALID_SIMPLE_TYPE_NO_ITEM_LITERAL_TYPE(Object arg0, Object arg1) {
/* 402 */     return localizer.localize(localizableMODEL_SCHEMA_INVALID_SIMPLE_TYPE_NO_ITEM_LITERAL_TYPE(arg0, arg1));
/*     */   }
/*     */   
/*     */   public static Localizable localizableMODEL_ARRAYWRAPPER_ONLY_ONE_MEMBER() {
/* 406 */     return messageFactory.getMessage("model.arraywrapper.only.one.member", new Object[0]);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String MODEL_ARRAYWRAPPER_ONLY_ONE_MEMBER() {
/* 414 */     return localizer.localize(localizableMODEL_ARRAYWRAPPER_ONLY_ONE_MEMBER());
/*     */   }
/*     */   
/*     */   public static Localizable localizableMODEL_PARAMETER_NOTUNIQUE_WRAPPER(Object arg0, Object arg1) {
/* 418 */     return messageFactory.getMessage("model.parameter.notunique.wrapper", new Object[] { arg0, arg1 });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String MODEL_PARAMETER_NOTUNIQUE_WRAPPER(Object arg0, Object arg1) {
/* 429 */     return localizer.localize(localizableMODEL_PARAMETER_NOTUNIQUE_WRAPPER(arg0, arg1));
/*     */   }
/*     */   
/*     */   public static Localizable localizableMODEL_SCHEMA_NOT_IMPLEMENTED(Object arg0) {
/* 433 */     return messageFactory.getMessage("model.schema.notImplemented", new Object[] { arg0 });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String MODEL_SCHEMA_NOT_IMPLEMENTED(Object arg0) {
/* 441 */     return localizer.localize(localizableMODEL_SCHEMA_NOT_IMPLEMENTED(arg0));
/*     */   }
/*     */   
/*     */   public static Localizable localizableMODEL_SCHEMA_INVALID_LITERAL_IN_ENUMERATION_ANONYMOUS(Object arg0) {
/* 445 */     return messageFactory.getMessage("model.schema.invalidLiteralInEnumeration.anonymous", new Object[] { arg0 });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String MODEL_SCHEMA_INVALID_LITERAL_IN_ENUMERATION_ANONYMOUS(Object arg0) {
/* 453 */     return localizer.localize(localizableMODEL_SCHEMA_INVALID_LITERAL_IN_ENUMERATION_ANONYMOUS(arg0));
/*     */   }
/*     */   
/*     */   public static Localizable localizableMODEL_PART_NOT_UNIQUE(Object arg0, Object arg1) {
/* 457 */     return messageFactory.getMessage("model.part.notUnique", new Object[] { arg0, arg1 });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String MODEL_PART_NOT_UNIQUE(Object arg0, Object arg1) {
/* 465 */     return localizer.localize(localizableMODEL_PART_NOT_UNIQUE(arg0, arg1));
/*     */   }
/*     */   
/*     */   public static Localizable localizableMODEL_ARRAYWRAPPER_NO_CONTENT_MEMBER() {
/* 469 */     return messageFactory.getMessage("model.arraywrapper.no.content.member", new Object[0]);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String MODEL_ARRAYWRAPPER_NO_CONTENT_MEMBER() {
/* 477 */     return localizer.localize(localizableMODEL_ARRAYWRAPPER_NO_CONTENT_MEMBER());
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\ws\resources\ModelMessages.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */