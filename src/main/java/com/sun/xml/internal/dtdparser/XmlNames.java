/*     */ package com.sun.xml.internal.dtdparser;
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
/*     */ public class XmlNames
/*     */ {
/*     */   public static boolean isName(String value) {
/*  48 */     if (value == null) {
/*  49 */       return false;
/*     */     }
/*  51 */     char c = value.charAt(0);
/*  52 */     if (!XmlChars.isLetter(c) && c != '_' && c != ':')
/*  53 */       return false; 
/*  54 */     for (int i = 1; i < value.length(); i++) {
/*  55 */       if (!XmlChars.isNameChar(value.charAt(i)))
/*  56 */         return false; 
/*  57 */     }  return true;
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
/*     */   public static boolean isUnqualifiedName(String value) {
/*  69 */     if (value == null || value.length() == 0) {
/*  70 */       return false;
/*     */     }
/*  72 */     char c = value.charAt(0);
/*  73 */     if (!XmlChars.isLetter(c) && c != '_')
/*  74 */       return false; 
/*  75 */     for (int i = 1; i < value.length(); i++) {
/*  76 */       if (!XmlChars.isNCNameChar(value.charAt(i)))
/*  77 */         return false; 
/*  78 */     }  return true;
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
/*     */   public static boolean isQualifiedName(String value) {
/*  93 */     if (value == null) {
/*  94 */       return false;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 100 */     int first = value.indexOf(':');
/*     */ 
/*     */     
/* 103 */     if (first <= 0) {
/* 104 */       return isUnqualifiedName(value);
/*     */     }
/*     */ 
/*     */     
/* 108 */     int last = value.lastIndexOf(':');
/* 109 */     if (last != first) {
/* 110 */       return false;
/*     */     }
/* 112 */     return (isUnqualifiedName(value.substring(0, first)) && 
/* 113 */       isUnqualifiedName(value.substring(first + 1)));
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
/*     */   public static boolean isNmtoken(String token) {
/* 126 */     int length = token.length();
/*     */     
/* 128 */     for (int i = 0; i < length; i++) {
/* 129 */       if (!XmlChars.isNameChar(token.charAt(i)))
/* 130 */         return false; 
/* 131 */     }  return true;
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
/*     */   public static boolean isNCNmtoken(String token) {
/* 145 */     return (isNmtoken(token) && token.indexOf(':') < 0);
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\xml\internal\dtdparser\XmlNames.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */