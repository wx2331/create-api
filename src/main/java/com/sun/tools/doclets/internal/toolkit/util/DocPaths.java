/*     */ package com.sun.tools.doclets.internal.toolkit.util;
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
/*     */ public class DocPaths
/*     */ {
/*  41 */   public static final DocPath ALLCLASSES_FRAME = DocPath.create("allclasses-frame.html");
/*     */ 
/*     */   
/*  44 */   public static final DocPath ALLCLASSES_NOFRAME = DocPath.create("allclasses-noframe.html");
/*     */ 
/*     */   
/*  47 */   public static final DocPath CLASS_USE = DocPath.create("class-use");
/*     */ 
/*     */   
/*  50 */   public static final DocPath CONSTANT_VALUES = DocPath.create("constant-values.html");
/*     */ 
/*     */   
/*  53 */   public static final DocPath DEPRECATED_LIST = DocPath.create("deprecated-list.html");
/*     */ 
/*     */   
/*  56 */   public static final DocPath DOC_FILES = DocPath.create("doc-files");
/*     */ 
/*     */   
/*  59 */   public static final DocPath HELP_DOC = DocPath.create("help-doc.html");
/*     */ 
/*     */   
/*  62 */   public static final DocPath INDEX = DocPath.create("index.html");
/*     */ 
/*     */   
/*  65 */   public static final DocPath INDEX_ALL = DocPath.create("index-all.html");
/*     */ 
/*     */   
/*  68 */   public static final DocPath INDEX_FILES = DocPath.create("index-files");
/*     */ 
/*     */   
/*     */   public static final DocPath indexN(int paramInt) {
/*  72 */     return DocPath.create("index-" + paramInt + ".html");
/*     */   }
/*     */ 
/*     */   
/*  76 */   public static final DocPath JAVASCRIPT = DocPath.create("script.js");
/*     */ 
/*     */   
/*  79 */   public static final DocPath OVERVIEW_FRAME = DocPath.create("overview-frame.html");
/*     */ 
/*     */   
/*  82 */   public static final DocPath OVERVIEW_SUMMARY = DocPath.create("overview-summary.html");
/*     */ 
/*     */   
/*  85 */   public static final DocPath OVERVIEW_TREE = DocPath.create("overview-tree.html");
/*     */ 
/*     */   
/*  88 */   public static final DocPath PACKAGE_FRAME = DocPath.create("package-frame.html");
/*     */ 
/*     */   
/*     */   public static final DocPath profileFrame(String paramString) {
/*  92 */     return DocPath.create(paramString + "-frame.html");
/*     */   }
/*     */ 
/*     */   
/*     */   public static final DocPath profilePackageFrame(String paramString) {
/*  97 */     return DocPath.create(paramString + "-package-frame.html");
/*     */   }
/*     */ 
/*     */   
/*     */   public static final DocPath profilePackageSummary(String paramString) {
/* 102 */     return DocPath.create(paramString + "-package-summary.html");
/*     */   }
/*     */ 
/*     */   
/*     */   public static final DocPath profileSummary(String paramString) {
/* 107 */     return DocPath.create(paramString + "-summary.html");
/*     */   }
/*     */ 
/*     */   
/* 111 */   public static final DocPath PACKAGE_LIST = DocPath.create("package-list");
/*     */ 
/*     */   
/* 114 */   public static final DocPath PACKAGE_SUMMARY = DocPath.create("package-summary.html");
/*     */ 
/*     */   
/* 117 */   public static final DocPath PACKAGE_TREE = DocPath.create("package-tree.html");
/*     */ 
/*     */   
/* 120 */   public static final DocPath PACKAGE_USE = DocPath.create("package-use.html");
/*     */ 
/*     */   
/* 123 */   public static final DocPath PROFILE_OVERVIEW_FRAME = DocPath.create("profile-overview-frame.html");
/*     */ 
/*     */   
/* 126 */   public static final DocPath RESOURCES = DocPath.create("resources");
/*     */ 
/*     */   
/* 129 */   public static final DocPath SERIALIZED_FORM = DocPath.create("serialized-form.html");
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 134 */   public static final DocPath SOURCE_OUTPUT = DocPath.create("src-html");
/*     */ 
/*     */   
/* 137 */   public static final DocPath STYLESHEET = DocPath.create("stylesheet.css");
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\doclets\internal\toolki\\util\DocPaths.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */