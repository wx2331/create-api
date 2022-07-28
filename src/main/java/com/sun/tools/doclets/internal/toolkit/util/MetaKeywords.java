/*     */ package com.sun.tools.doclets.internal.toolkit.util;
/*     */ 
/*     */ import com.sun.javadoc.ClassDoc;
/*     */ import com.sun.javadoc.MemberDoc;
/*     */ import com.sun.javadoc.PackageDoc;
/*     */ import com.sun.tools.doclets.internal.toolkit.Configuration;
/*     */ import com.sun.tools.javac.jvm.Profile;
/*     */ import java.util.ArrayList;
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
/*     */ public class MetaKeywords
/*     */ {
/*     */   private final Configuration configuration;
/*     */   
/*     */   public MetaKeywords(Configuration paramConfiguration) {
/*  58 */     this.configuration = paramConfiguration;
/*     */   }
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
/*     */   public String[] getMetaKeywords(ClassDoc paramClassDoc) {
/*  74 */     ArrayList<String> arrayList = new ArrayList();
/*     */ 
/*     */     
/*  77 */     if (this.configuration.keywords) {
/*  78 */       arrayList.addAll(getClassKeyword(paramClassDoc));
/*  79 */       arrayList.addAll(getMemberKeywords((MemberDoc[])paramClassDoc.fields()));
/*  80 */       arrayList.addAll(getMemberKeywords((MemberDoc[])paramClassDoc.methods()));
/*     */     } 
/*  82 */     return arrayList.<String>toArray(new String[0]);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected ArrayList<String> getClassKeyword(ClassDoc paramClassDoc) {
/*  90 */     String str = paramClassDoc.isInterface() ? "interface" : "class";
/*  91 */     ArrayList<String> arrayList = new ArrayList(1);
/*  92 */     arrayList.add(paramClassDoc.qualifiedName() + " " + str);
/*  93 */     return arrayList;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String[] getMetaKeywords(PackageDoc paramPackageDoc) {
/* 100 */     if (this.configuration.keywords) {
/* 101 */       String str = Util.getPackageName(paramPackageDoc);
/* 102 */       return new String[] { str + " package" };
/*     */     } 
/* 104 */     return new String[0];
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String[] getMetaKeywords(Profile paramProfile) {
/* 114 */     if (this.configuration.keywords) {
/* 115 */       String str = paramProfile.name;
/* 116 */       return new String[] { str + " profile" };
/*     */     } 
/* 118 */     return new String[0];
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String[] getOverviewMetaKeywords(String paramString1, String paramString2) {
/* 126 */     if (this.configuration.keywords) {
/* 127 */       String str = this.configuration.getText(paramString1);
/* 128 */       String[] arrayOfString = { str };
/* 129 */       if (paramString2.length() > 0) {
/* 130 */         arrayOfString[0] = arrayOfString[0] + ", " + paramString2;
/*     */       }
/* 132 */       return arrayOfString;
/*     */     } 
/* 134 */     return new String[0];
/*     */   }
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
/*     */   protected ArrayList<String> getMemberKeywords(MemberDoc[] paramArrayOfMemberDoc) {
/* 148 */     ArrayList<String> arrayList = new ArrayList();
/*     */     
/* 150 */     for (byte b = 0; b < paramArrayOfMemberDoc.length; b++) {
/*     */       
/* 152 */       String str = paramArrayOfMemberDoc[b].name() + (paramArrayOfMemberDoc[b].isMethod() ? "()" : "");
/* 153 */       if (!arrayList.contains(str)) {
/* 154 */         arrayList.add(str);
/*     */       }
/*     */     } 
/* 157 */     return arrayList;
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\doclets\internal\toolki\\util\MetaKeywords.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */