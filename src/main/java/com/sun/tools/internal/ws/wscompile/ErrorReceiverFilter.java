/*    */ package com.sun.tools.internal.ws.wscompile;
/*    */ 
/*    */ import com.sun.tools.internal.xjc.api.ErrorListener;
/*    */ import org.xml.sax.SAXParseException;
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
/*    */ public class ErrorReceiverFilter
/*    */   extends ErrorReceiver
/*    */ {
/*    */   private ErrorListener core;
/*    */   
/*    */   public ErrorReceiverFilter() {}
/*    */   
/*    */   public ErrorReceiverFilter(ErrorListener h) {
/* 44 */     setErrorReceiver(h);
/*    */   }
/*    */ 
/*    */   
/*    */   public void setErrorReceiver(ErrorListener handler) {
/* 49 */     this.core = handler;
/*    */   }
/*    */   
/*    */   public final boolean hadError() {
/* 53 */     return this.hadError;
/*    */   }
/*    */ 
/*    */   
/*    */   private boolean hadError = false;
/*    */   
/*    */   public void reset() {
/* 60 */     this.hadError = false;
/*    */   }
/*    */   
/*    */   public void info(SAXParseException exception) {
/* 64 */     if (this.core != null) this.core.info(exception);
/*    */   
/*    */   }
/*    */ 
/*    */   
/*    */   public void debug(SAXParseException exception) {}
/*    */   
/*    */   public void warning(SAXParseException exception) {
/* 72 */     if (this.core != null) this.core.warning(exception); 
/*    */   }
/*    */   
/*    */   public void error(SAXParseException exception) {
/* 76 */     this.hadError = true;
/* 77 */     if (this.core != null) this.core.error(exception); 
/*    */   }
/*    */   
/*    */   public void fatalError(SAXParseException exception) {
/* 81 */     this.hadError = true;
/* 82 */     if (this.core != null) this.core.fatalError(exception); 
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\ws\wscompile\ErrorReceiverFilter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */