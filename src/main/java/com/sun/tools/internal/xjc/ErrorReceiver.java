/*     */ package com.sun.tools.internal.xjc;
/*     */ 
/*     */ import com.sun.istack.internal.SAXParseException2;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class ErrorReceiver
/*     */   implements ErrorHandler, ErrorListener
/*     */ {
/*     */   public final void error(Locator loc, String msg) {
/*  71 */     error(new SAXParseException2(msg, loc));
/*     */   }
/*     */   
/*     */   public final void error(Locator loc, String msg, Exception e) {
/*  75 */     error(new SAXParseException2(msg, loc, e));
/*     */   }
/*     */   
/*     */   public final void error(String msg, Exception e) {
/*  79 */     error(new SAXParseException2(msg, null, e));
/*     */   }
/*     */   
/*     */   public void error(Exception e) {
/*  83 */     error(e.getMessage(), e);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void warning(Locator loc, String msg) {
/*  91 */     warning(new SAXParseException(msg, loc));
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
/* 125 */     info(new SAXParseException(msg, null));
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
/*     */   
/*     */   protected final String getLocationString(SAXParseException e) {
/* 142 */     if (e.getLineNumber() != -1 || e.getSystemId() != null) {
/* 143 */       int line = e.getLineNumber();
/* 144 */       return Messages.format("ConsoleErrorReporter.LineXOfY", new Object[] { (line == -1) ? "?" : 
/* 145 */             Integer.toString(line), 
/* 146 */             getShortName(e.getSystemId()) });
/*     */     } 
/* 148 */     return Messages.format("ConsoleErrorReporter.UnknownLocation", new Object[0]);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private String getShortName(String url) {
/* 154 */     if (url == null) {
/* 155 */       return Messages.format("ConsoleErrorReporter.UnknownFile", new Object[0]);
/*     */     }
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
/* 170 */     return url;
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\xjc\ErrorReceiver.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */