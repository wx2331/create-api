/*    */ package com.sun.tools.internal.ws.wsdl.document.jaxws;
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
/*    */ public class Exception
/*    */ {
/*    */   private CustomName className;
/*    */   
/*    */   public Exception() {}
/*    */   
/*    */   public Exception(CustomName name) {
/* 39 */     this.className = name;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public CustomName getClassName() {
/* 47 */     return this.className;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void setClassName(CustomName className) {
/* 53 */     this.className = className;
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\ws\wsdl\document\jaxws\Exception.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */