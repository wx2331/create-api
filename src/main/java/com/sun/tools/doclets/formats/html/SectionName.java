/*    */ package com.sun.tools.doclets.formats.html;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public enum SectionName
/*    */ {
/* 40 */   ANNOTATION_TYPE_ELEMENT_DETAIL("annotation.type.element.detail"),
/* 41 */   ANNOTATION_TYPE_FIELD_DETAIL("annotation.type.field.detail"),
/* 42 */   ANNOTATION_TYPE_FIELD_SUMMARY("annotation.type.field.summary"),
/* 43 */   ANNOTATION_TYPE_OPTIONAL_ELEMENT_SUMMARY("annotation.type.optional.element.summary"),
/* 44 */   ANNOTATION_TYPE_REQUIRED_ELEMENT_SUMMARY("annotation.type.required.element.summary"),
/* 45 */   CONSTRUCTOR_DETAIL("constructor.detail"),
/* 46 */   CONSTRUCTOR_SUMMARY("constructor.summary"),
/* 47 */   ENUM_CONSTANT_DETAIL("enum.constant.detail"),
/* 48 */   ENUM_CONSTANTS_INHERITANCE("enum.constants.inherited.from.class."),
/* 49 */   ENUM_CONSTANT_SUMMARY("enum.constant.summary"),
/* 50 */   FIELD_DETAIL("field.detail"),
/* 51 */   FIELDS_INHERITANCE("fields.inherited.from.class."),
/* 52 */   FIELD_SUMMARY("field.summary"),
/* 53 */   METHOD_DETAIL("method.detail"),
/* 54 */   METHODS_INHERITANCE("methods.inherited.from.class."),
/* 55 */   METHOD_SUMMARY("method.summary"),
/* 56 */   NAVBAR_BOTTOM("navbar.bottom"),
/* 57 */   NAVBAR_BOTTOM_FIRSTROW("navbar.bottom.firstrow"),
/* 58 */   NAVBAR_TOP("navbar.top"),
/* 59 */   NAVBAR_TOP_FIRSTROW("navbar.top.firstrow"),
/* 60 */   NESTED_CLASSES_INHERITANCE("nested.classes.inherited.from.class."),
/* 61 */   NESTED_CLASS_SUMMARY("nested.class.summary"),
/* 62 */   OVERVIEW_DESCRIPTION("overview.description"),
/* 63 */   PACKAGE_DESCRIPTION("package.description"),
/* 64 */   PROPERTY_DETAIL("property.detail"),
/* 65 */   PROPERTIES_INHERITANCE("properties.inherited.from.class."),
/* 66 */   PROPERTY_SUMMARY("property.summary"),
/* 67 */   SKIP_NAVBAR_BOTTOM("skip.navbar.bottom"),
/* 68 */   SKIP_NAVBAR_TOP("skip.navbar.top"),
/* 69 */   UNNAMED_PACKAGE_ANCHOR("unnamed.package");
/*    */   
/*    */   private final String value;
/*    */   
/*    */   SectionName(String paramString1) {
/* 74 */     this.value = paramString1;
/*    */   }
/*    */   
/*    */   public String getName() {
/* 78 */     return this.value;
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\doclets\formats\html\SectionName.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */