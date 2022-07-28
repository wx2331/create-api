/*    */ package com.sun.tools.internal.ws.processor.modeler.wsdl;
/*    */ 
/*    */ import com.sun.tools.internal.ws.resources.WscompileMessages;
/*    */ import com.sun.tools.internal.ws.wscompile.ErrorReceiver;
/*    */ import java.io.OutputStream;
/*    */ import java.io.PrintStream;
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
/*    */ public class ConsoleErrorReporter
/*    */   extends ErrorReceiver
/*    */ {
/*    */   private boolean hasError;
/*    */   private PrintStream output;
/*    */   private boolean debug;
/*    */   
/*    */   public ConsoleErrorReporter(PrintStream stream) {
/* 43 */     this.output = stream;
/*    */   }
/*    */   
/*    */   public ConsoleErrorReporter(OutputStream outputStream) {
/* 47 */     this.output = new PrintStream(outputStream);
/*    */   }
/*    */   
/*    */   public boolean hasError() {
/* 51 */     return this.hasError;
/*    */   }
/*    */   
/*    */   public void error(SAXParseException e) {
/* 55 */     if (this.debug)
/* 56 */       e.printStackTrace(); 
/* 57 */     this.hasError = true;
/* 58 */     if (e.getSystemId() == null && e.getPublicId() == null && e.getCause() instanceof java.net.UnknownHostException) {
/* 59 */       print(WscompileMessages.WSIMPORT_ERROR_MESSAGE(e.toString()), e);
/*    */     } else {
/* 61 */       print(WscompileMessages.WSIMPORT_ERROR_MESSAGE(e.getMessage()), e);
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void fatalError(SAXParseException e) {
/* 68 */     if (this.debug)
/* 69 */       e.printStackTrace(); 
/* 70 */     this.hasError = true;
/* 71 */     print(WscompileMessages.WSIMPORT_ERROR_MESSAGE(e.getMessage()), e);
/*    */   }
/*    */   
/*    */   public void warning(SAXParseException e) {
/* 75 */     print(WscompileMessages.WSIMPORT_WARNING_MESSAGE(e.getMessage()), e);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void info(SAXParseException e) {
/* 83 */     print(WscompileMessages.WSIMPORT_INFO_MESSAGE(e.getMessage()), e);
/*    */   }
/*    */   
/*    */   public void debug(SAXParseException e) {
/* 87 */     print(WscompileMessages.WSIMPORT_DEBUG_MESSAGE(e.getMessage()), e);
/*    */   }
/*    */ 
/*    */   
/*    */   private void print(String message, SAXParseException e) {
/* 92 */     this.output.println(message);
/* 93 */     this.output.println(getLocationString(e));
/* 94 */     this.output.println();
/*    */   }
/*    */   
/*    */   public void enableDebugging() {
/* 98 */     this.debug = true;
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\ws\processor\modeler\wsdl\ConsoleErrorReporter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */