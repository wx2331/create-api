/*     */ package com.sun.tools.internal.ws.wscompile;
/*     */ 
/*     */ import com.sun.istack.internal.Nullable;
/*     */ import com.sun.istack.internal.SAXParseException2;
/*     */ import com.sun.tools.internal.ws.resources.ModelMessages;
/*     */ import com.sun.tools.internal.xjc.api.ErrorListener;
/*     */ import org.xml.sax.ErrorHandler;
/*     */ import org.xml.sax.Locator;
/*     */ import org.xml.sax.SAXParseException;
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
/*     */ public abstract class ErrorReceiver
/*     */   implements ErrorHandler, ErrorListener
/*     */ {
/*     */   public final void error(Locator loc, String msg) {
/*  70 */     error(new SAXParseException2(msg, loc));
/*     */   }
/*     */   
/*     */   public final void error(Locator loc, String msg, Exception e) {
/*  74 */     error(new SAXParseException2(msg, loc, e));
/*     */   }
/*     */   
/*     */   public final void error(String msg, Exception e) {
/*  78 */     error(new SAXParseException2(msg, null, e));
/*     */   }
/*     */   
/*     */   public void error(Exception e) {
/*  82 */     error(e.getMessage(), e);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void warning(@Nullable Locator loc, String msg) {
/*  89 */     warning(new SAXParseException(msg, loc));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract void error(SAXParseException paramSAXParseException) throws AbortException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract void fatalError(SAXParseException paramSAXParseException) throws AbortException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract void warning(SAXParseException paramSAXParseException) throws AbortException;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void pollAbort() throws AbortException {}
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract void info(SAXParseException paramSAXParseException);
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void debug(String msg) {
/* 124 */     info(new SAXParseException(msg, null));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract void debug(SAXParseException paramSAXParseException);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final String getLocationString(SAXParseException e) {
/* 143 */     if (e.getLineNumber() != -1 || e.getSystemId() != null) {
/* 144 */       int line = e.getLineNumber();
/* 145 */       return ModelMessages.CONSOLE_ERROR_REPORTER_LINE_X_OF_Y((line == -1) ? "?" : Integer.toString(line), 
/* 146 */           getShortName(e.getSystemId()));
/*     */     } 
/* 148 */     return "";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private String getShortName(String url) {
/* 154 */     if (url == null)
/* 155 */       return ModelMessages.CONSOLE_ERROR_REPORTER_UNKNOWN_LOCATION(); 
/* 156 */     return url;
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\ws\wscompile\ErrorReceiver.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */