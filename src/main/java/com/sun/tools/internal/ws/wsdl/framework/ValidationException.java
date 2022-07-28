/*    */ package com.sun.tools.internal.ws.wsdl.framework;
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
/*    */ 
/*    */ 
/*    */ public class ValidationException
/*    */   extends JAXWSExceptionBase
/*    */ {
/*    */   public ValidationException(String key, Object... args) {
/* 38 */     super(key, args);
/*    */   }
/*    */   
/*    */   public ValidationException(Throwable throwable) {
/* 42 */     super(throwable);
/*    */   }
/*    */   
/*    */   public String getDefaultResourceBundleName() {
/* 46 */     return "com.sun.tools.internal.ws.resources.wsdl";
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\ws\wsdl\framework\ValidationException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */