/*     */ package com.sun.tools.doclets.internal.toolkit.util;
/*     */ 
/*     */ import com.sun.javadoc.AnnotationTypeDoc;
/*     */ import com.sun.javadoc.ClassDoc;
/*     */ import com.sun.javadoc.Doc;
/*     */ import com.sun.javadoc.MemberDoc;
/*     */ import com.sun.javadoc.PackageDoc;
/*     */ import com.sun.tools.doclets.internal.toolkit.Configuration;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DeprecatedAPIListBuilder
/*     */ {
/*     */   public static final int NUM_TYPES = 12;
/*     */   public static final int PACKAGE = 0;
/*     */   public static final int INTERFACE = 1;
/*     */   public static final int CLASS = 2;
/*     */   public static final int ENUM = 3;
/*     */   public static final int EXCEPTION = 4;
/*     */   public static final int ERROR = 5;
/*     */   public static final int ANNOTATION_TYPE = 6;
/*     */   public static final int FIELD = 7;
/*     */   public static final int METHOD = 8;
/*     */   public static final int CONSTRUCTOR = 9;
/*     */   public static final int ENUM_CONSTANT = 10;
/*     */   public static final int ANNOTATION_TYPE_MEMBER = 11;
/*  72 */   private List<List<Doc>> deprecatedLists = new ArrayList<>(); public DeprecatedAPIListBuilder(Configuration paramConfiguration) {
/*  73 */     for (byte b = 0; b < 12; b++) {
/*  74 */       this.deprecatedLists.add(b, new ArrayList<>());
/*     */     }
/*  76 */     buildDeprecatedAPIInfo(paramConfiguration);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void buildDeprecatedAPIInfo(Configuration paramConfiguration) {
/*  87 */     PackageDoc[] arrayOfPackageDoc = paramConfiguration.packages;
/*     */     
/*  89 */     for (byte b1 = 0; b1 < arrayOfPackageDoc.length; b1++) {
/*  90 */       PackageDoc packageDoc = arrayOfPackageDoc[b1];
/*  91 */       if (Util.isDeprecated((Doc)packageDoc)) {
/*  92 */         getList(0).add(packageDoc);
/*     */       }
/*     */     } 
/*  95 */     ClassDoc[] arrayOfClassDoc = paramConfiguration.root.classes();
/*  96 */     for (byte b2 = 0; b2 < arrayOfClassDoc.length; b2++) {
/*  97 */       ClassDoc classDoc = arrayOfClassDoc[b2];
/*  98 */       if (Util.isDeprecated((Doc)classDoc)) {
/*  99 */         if (classDoc.isOrdinaryClass()) {
/* 100 */           getList(2).add(classDoc);
/* 101 */         } else if (classDoc.isInterface()) {
/* 102 */           getList(1).add(classDoc);
/* 103 */         } else if (classDoc.isException()) {
/* 104 */           getList(4).add(classDoc);
/* 105 */         } else if (classDoc.isEnum()) {
/* 106 */           getList(3).add(classDoc);
/* 107 */         } else if (classDoc.isError()) {
/* 108 */           getList(5).add(classDoc);
/* 109 */         } else if (classDoc.isAnnotationType()) {
/* 110 */           getList(6).add(classDoc);
/*     */         } 
/*     */       }
/* 113 */       composeDeprecatedList(getList(7), (MemberDoc[])classDoc.fields());
/* 114 */       composeDeprecatedList(getList(8), (MemberDoc[])classDoc.methods());
/* 115 */       composeDeprecatedList(getList(9), (MemberDoc[])classDoc.constructors());
/* 116 */       if (classDoc.isEnum()) {
/* 117 */         composeDeprecatedList(getList(10), (MemberDoc[])classDoc.enumConstants());
/*     */       }
/* 119 */       if (classDoc.isAnnotationType()) {
/* 120 */         composeDeprecatedList(getList(11), (MemberDoc[])((AnnotationTypeDoc)classDoc)
/* 121 */             .elements());
/*     */       }
/*     */     } 
/* 124 */     sortDeprecatedLists();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void composeDeprecatedList(List<Doc> paramList, MemberDoc[] paramArrayOfMemberDoc) {
/* 134 */     for (byte b = 0; b < paramArrayOfMemberDoc.length; b++) {
/* 135 */       if (Util.isDeprecated((Doc)paramArrayOfMemberDoc[b])) {
/* 136 */         paramList.add(paramArrayOfMemberDoc[b]);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void sortDeprecatedLists() {
/* 146 */     for (byte b = 0; b < 12; b++) {
/* 147 */       Collections.sort(getList(b));
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<Doc> getList(int paramInt) {
/* 157 */     return this.deprecatedLists.get(paramInt);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hasDocumentation(int paramInt) {
/* 166 */     return (((List)this.deprecatedLists.get(paramInt)).size() > 0);
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\doclets\internal\toolki\\util\DeprecatedAPIListBuilder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */