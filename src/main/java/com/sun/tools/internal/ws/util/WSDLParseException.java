/*    */ package com.sun.tools.internal.ws.util;
/*    */ 
/*    */ import com.sun.xml.internal.ws.util.exception.JAXWSExceptionBase;
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
/*    */ public class WSDLParseException
/*    */   extends JAXWSExceptionBase
/*    */ {
/*    */   public WSDLParseException(String key, Object... args) {
/* 36 */     super(key, args);
/*    */   }
/*    */   
/*    */   public WSDLParseException(Throwable throwable) {
/* 40 */     super(throwable);
/*    */   }
/*    */   
/*    */   public String getDefaultResourceBundleName() {
/* 44 */     return "com.sun.tools.internal.ws.resources.util";
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\w\\util\WSDLParseException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */