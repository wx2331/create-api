/*    */ package com.sun.tools.internal.xjc;
/*    */ 
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
/*    */   private PrintStream output;
/*    */   private boolean hadError = false;
/*    */   
/*    */   public ConsoleErrorReporter(PrintStream out) {
/* 49 */     this.output = out;
/*    */   }
/*    */   public ConsoleErrorReporter(OutputStream out) {
/* 52 */     this(new PrintStream(out));
/*    */   } public ConsoleErrorReporter() {
/* 54 */     this(System.out);
/*    */   }
/*    */   public void warning(SAXParseException e) {
/* 57 */     print("Driver.WarningMessage", e);
/*    */   }
/*    */   
/*    */   public void error(SAXParseException e) {
/* 61 */     this.hadError = true;
/* 62 */     print("Driver.ErrorMessage", e);
/*    */   }
/*    */   
/*    */   public void fatalError(SAXParseException e) {
/* 66 */     this.hadError = true;
/* 67 */     print("Driver.ErrorMessage", e);
/*    */   }
/*    */   
/*    */   public void info(SAXParseException e) {
/* 71 */     print("Driver.InfoMessage", e);
/*    */   }
/*    */   
/*    */   public boolean hadError() {
/* 75 */     return this.hadError;
/*    */   }
/*    */   
/*    */   private void print(String resource, SAXParseException e) {
/* 79 */     this.output.println(Messages.format(resource, new Object[] { e.getMessage() }));
/* 80 */     this.output.println(getLocationString(e));
/* 81 */     this.output.println();
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\xjc\ConsoleErrorReporter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */