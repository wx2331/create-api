/*    */ package com.sun.tools.internal.ws.processor.modeler.annotation;
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
/*    */ public enum WebServiceConstants
/*    */ {
/* 33 */   SERVICE("Service"),
/*    */   
/* 35 */   JAXWS_PACKAGE_PD("jaxws."),
/*    */   
/* 37 */   PD_JAXWS_PACKAGE_PD(".jaxws."),
/*    */   
/* 39 */   BEAN("Bean"),
/*    */   
/* 41 */   FAULT_INFO("faultInfo"),
/*    */   
/* 43 */   RESPONSE("Response");
/*    */   
/*    */   private String value;
/*    */   
/*    */   WebServiceConstants(String value) {
/* 48 */     this.value = value;
/*    */   }
/*    */   
/*    */   public String getValue() {
/* 52 */     return this.value;
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\ws\processor\modeler\annotation\WebServiceConstants.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */