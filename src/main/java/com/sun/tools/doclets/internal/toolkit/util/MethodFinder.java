/*    */ package com.sun.tools.doclets.internal.toolkit.util;
/*    */ 
/*    */ import com.sun.javadoc.ClassDoc;
/*    */ import com.sun.javadoc.MethodDoc;
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
/*    */ public abstract class MethodFinder
/*    */ {
/*    */   abstract boolean isCorrectMethod(MethodDoc paramMethodDoc);
/*    */   
/*    */   public MethodDoc search(ClassDoc paramClassDoc, MethodDoc paramMethodDoc) {
/* 46 */     MethodDoc methodDoc = searchInterfaces(paramClassDoc, paramMethodDoc);
/* 47 */     if (methodDoc != null) {
/* 48 */       return methodDoc;
/*    */     }
/* 50 */     ClassDoc classDoc = paramClassDoc.superclass();
/* 51 */     if (classDoc != null) {
/* 52 */       methodDoc = Util.findMethod(classDoc, paramMethodDoc);
/* 53 */       if (methodDoc != null && 
/* 54 */         isCorrectMethod(methodDoc)) {
/* 55 */         return methodDoc;
/*    */       }
/*    */       
/* 58 */       return search(classDoc, paramMethodDoc);
/*    */     } 
/* 60 */     return null;
/*    */   }
/*    */   
/*    */   public MethodDoc searchInterfaces(ClassDoc paramClassDoc, MethodDoc paramMethodDoc) {
/* 64 */     MethodDoc[] arrayOfMethodDoc = (new ImplementedMethods(paramMethodDoc, null)).build();
/* 65 */     for (byte b = 0; b < arrayOfMethodDoc.length; b++) {
/* 66 */       if (isCorrectMethod(arrayOfMethodDoc[b])) {
/* 67 */         return arrayOfMethodDoc[b];
/*    */       }
/*    */     } 
/* 70 */     return null;
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\doclets\internal\toolki\\util\MethodFinder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */