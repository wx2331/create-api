/*     */ package com.sun.tools.doclets.formats.html.markup;
/*     */ 
/*     */ import com.sun.tools.doclets.internal.toolkit.Content;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class HtmlConstants
/*     */ {
/*  45 */   public static final Content START_OF_TOP_NAVBAR = new Comment("========= START OF TOP NAVBAR =======");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  51 */   public static final Content START_OF_BOTTOM_NAVBAR = new Comment("======= START OF BOTTOM NAVBAR ======");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  57 */   public static final Content END_OF_TOP_NAVBAR = new Comment("========= END OF TOP NAVBAR =========");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  63 */   public static final Content END_OF_BOTTOM_NAVBAR = new Comment("======== END OF BOTTOM NAVBAR =======");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  69 */   public static final Content START_OF_CLASS_DATA = new Comment("======== START OF CLASS DATA ========");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  75 */   public static final Content END_OF_CLASS_DATA = new Comment("========= END OF CLASS DATA =========");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  81 */   public static final Content START_OF_NESTED_CLASS_SUMMARY = new Comment("======== NESTED CLASS SUMMARY ========");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  87 */   public static final Content START_OF_ANNOTATION_TYPE_OPTIONAL_MEMBER_SUMMARY = new Comment("=========== ANNOTATION TYPE OPTIONAL MEMBER SUMMARY ===========");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  93 */   public static final Content START_OF_ANNOTATION_TYPE_REQUIRED_MEMBER_SUMMARY = new Comment("=========== ANNOTATION TYPE REQUIRED MEMBER SUMMARY ===========");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  99 */   public static final Content START_OF_ANNOTATION_TYPE_FIELD_SUMMARY = new Comment("=========== ANNOTATION TYPE FIELD SUMMARY ===========");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 105 */   public static final Content START_OF_CONSTRUCTOR_SUMMARY = new Comment("======== CONSTRUCTOR SUMMARY ========");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 111 */   public static final Content START_OF_ENUM_CONSTANT_SUMMARY = new Comment("=========== ENUM CONSTANT SUMMARY ===========");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 117 */   public static final Content START_OF_FIELD_SUMMARY = new Comment("=========== FIELD SUMMARY ===========");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 123 */   public static final Content START_OF_PROPERTY_SUMMARY = new Comment("=========== PROPERTY SUMMARY ===========");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 129 */   public static final Content START_OF_METHOD_SUMMARY = new Comment("========== METHOD SUMMARY ===========");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 135 */   public static final Content START_OF_ANNOTATION_TYPE_DETAILS = new Comment("============ ANNOTATION TYPE MEMBER DETAIL ===========");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 141 */   public static final Content START_OF_ANNOTATION_TYPE_FIELD_DETAILS = new Comment("============ ANNOTATION TYPE FIELD DETAIL ===========");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 147 */   public static final Content START_OF_METHOD_DETAILS = new Comment("============ METHOD DETAIL ==========");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 153 */   public static final Content START_OF_FIELD_DETAILS = new Comment("============ FIELD DETAIL ===========");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 159 */   public static final Content START_OF_PROPERTY_DETAILS = new Comment("============ PROPERTY DETAIL ===========");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 165 */   public static final Content START_OF_CONSTRUCTOR_DETAILS = new Comment("========= CONSTRUCTOR DETAIL ========");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 171 */   public static final Content START_OF_ENUM_CONSTANT_DETAILS = new Comment("============ ENUM CONSTANT DETAIL ===========");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 177 */   public static final HtmlTag TITLE_HEADING = HtmlTag.H1;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 182 */   public static final HtmlTag CLASS_PAGE_HEADING = HtmlTag.H2;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 187 */   public static final HtmlTag CONTENT_HEADING = HtmlTag.H2;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 192 */   public static final HtmlTag PACKAGE_HEADING = HtmlTag.H2;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 197 */   public static final HtmlTag PROFILE_HEADING = HtmlTag.H2;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 202 */   public static final HtmlTag SUMMARY_HEADING = HtmlTag.H3;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 207 */   public static final HtmlTag INHERITED_SUMMARY_HEADING = HtmlTag.H3;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 212 */   public static final HtmlTag DETAILS_HEADING = HtmlTag.H3;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 217 */   public static final HtmlTag SERIALIZED_MEMBER_HEADING = HtmlTag.H3;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 222 */   public static final HtmlTag MEMBER_HEADING = HtmlTag.H4;
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\doclets\formats\html\markup\HtmlConstants.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */