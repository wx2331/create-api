/*    */ package com.sun.xml.internal.rngom.binary;
/*    */ 
/*    */ import javax.xml.namespace.QName;
/*    */ import org.xml.sax.Locator;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ final class RestrictionViolationException
/*    */   extends Exception
/*    */ {
/*    */   private String messageId;
/*    */   private Locator loc;
/*    */   private QName name;
/*    */   
/*    */   RestrictionViolationException(String messageId) {
/* 58 */     this.messageId = messageId;
/*    */   }
/*    */   
/*    */   RestrictionViolationException(String messageId, QName name) {
/* 62 */     this.messageId = messageId;
/* 63 */     this.name = name;
/*    */   }
/*    */   
/*    */   String getMessageId() {
/* 67 */     return this.messageId;
/*    */   }
/*    */   
/*    */   Locator getLocator() {
/* 71 */     return this.loc;
/*    */   }
/*    */   
/*    */   void maybeSetLocator(Locator loc) {
/* 75 */     if (this.loc == null)
/* 76 */       this.loc = loc; 
/*    */   }
/*    */   
/*    */   QName getName() {
/* 80 */     return this.name;
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\xml\internal\rngom\binary\RestrictionViolationException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */