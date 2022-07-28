/*    */ package com.sun.xml.internal.xsom.impl.util;
/*    */ 
/*    */ import org.xml.sax.ErrorHandler;
/*    */ import org.xml.sax.SAXException;
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
/*    */ public class DraconianErrorHandler
/*    */   implements ErrorHandler
/*    */ {
/*    */   public void error(SAXParseException e) throws SAXException {
/* 37 */     throw e;
/*    */   }
/*    */   public void fatalError(SAXParseException e) throws SAXException {
/* 40 */     throw e;
/*    */   }
/*    */   
/*    */   public void warning(SAXParseException e) {}
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\xml\internal\xsom\imp\\util\DraconianErrorHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */