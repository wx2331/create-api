/*    */ package com.sun.tools.internal.ws.processor.model.java;
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
/*    */ public class JavaArrayType
/*    */   extends JavaType
/*    */ {
/*    */   private String elementName;
/*    */   private JavaType elementType;
/*    */   private String soapArrayHolderName;
/*    */   
/*    */   public JavaArrayType() {}
/*    */   
/*    */   public JavaArrayType(String name) {
/* 38 */     super(name, true, "null");
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public JavaArrayType(String name, String elementName, JavaType elementType) {
/* 44 */     super(name, true, "null");
/* 45 */     this.elementName = elementName;
/* 46 */     this.elementType = elementType;
/*    */   }
/*    */   
/*    */   public String getElementName() {
/* 50 */     return this.elementName;
/*    */   }
/*    */   
/*    */   public void setElementName(String name) {
/* 54 */     this.elementName = name;
/*    */   }
/*    */   
/*    */   public JavaType getElementType() {
/* 58 */     return this.elementType;
/*    */   }
/*    */   
/*    */   public void setElementType(JavaType type) {
/* 62 */     this.elementType = type;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getSOAPArrayHolderName() {
/* 67 */     return this.soapArrayHolderName;
/*    */   }
/*    */   
/*    */   public void setSOAPArrayHolderName(String holderName) {
/* 71 */     this.soapArrayHolderName = holderName;
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\ws\processor\model\java\JavaArrayType.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */