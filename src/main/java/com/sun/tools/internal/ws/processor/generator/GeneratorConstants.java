/*    */ package com.sun.tools.internal.ws.processor.generator;
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
/*    */ public enum GeneratorConstants
/*    */ {
/* 33 */   DOTC("."),
/*    */   
/* 35 */   SIG_INNERCLASS("$"),
/*    */   
/* 37 */   JAVA_SRC_SUFFIX(".java"),
/*    */   
/* 39 */   QNAME_SUFFIX("_QNAME"),
/*    */   
/* 41 */   GET("get"),
/*    */   
/* 43 */   IS("is"),
/*    */   
/* 45 */   RESPONSE("Response"),
/*    */   
/* 47 */   FAULT_CLASS_MEMBER_NAME("faultInfo");
/*    */   
/*    */   private String value;
/*    */   
/*    */   GeneratorConstants(String value) {
/* 52 */     this.value = value;
/*    */   }
/*    */   
/*    */   public String getValue() {
/* 56 */     return this.value;
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\ws\processor\generator\GeneratorConstants.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */