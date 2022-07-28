/*    */ package com.sun.tools.hat.internal.util;
/*    */ 
/*    */ import java.util.Enumeration;
/*    */ import java.util.NoSuchElementException;
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
/*    */ public class CompositeEnumeration
/*    */   implements Enumeration
/*    */ {
/*    */   Enumeration e1;
/*    */   Enumeration e2;
/*    */   
/*    */   public CompositeEnumeration(Enumeration paramEnumeration1, Enumeration paramEnumeration2) {
/* 43 */     this.e1 = paramEnumeration1;
/* 44 */     this.e2 = paramEnumeration2;
/*    */   }
/*    */   
/*    */   public boolean hasMoreElements() {
/* 48 */     return (this.e1.hasMoreElements() || this.e2.hasMoreElements());
/*    */   }
/*    */   
/*    */   public Object nextElement() {
/* 52 */     if (this.e1.hasMoreElements()) {
/* 53 */       return this.e1.nextElement();
/*    */     }
/*    */     
/* 56 */     if (this.e2.hasMoreElements()) {
/* 57 */       return this.e2.nextElement();
/*    */     }
/*    */     
/* 60 */     throw new NoSuchElementException();
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\hat\interna\\util\CompositeEnumeration.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */