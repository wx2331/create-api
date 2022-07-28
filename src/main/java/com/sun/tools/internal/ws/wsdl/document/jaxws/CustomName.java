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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class CustomName
/*    */ {
/*    */   private String javaDoc;
/*    */   private String name;
/*    */   
/*    */   public CustomName() {}
/*    */   
/*    */   public CustomName(String name, String javaDoc) {
/* 48 */     this.name = name;
/* 49 */     this.javaDoc = javaDoc;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getJavaDoc() {
/* 56 */     return this.javaDoc;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void setJavaDoc(String javaDoc) {
/* 62 */     this.javaDoc = javaDoc;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public String getName() {
/* 68 */     return this.name;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void setName(String name) {
/* 74 */     this.name = name;
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\ws\wsdl\document\jaxws\CustomName.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */