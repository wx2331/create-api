/*    */ package com.sun.tools.internal.ws.processor.modeler.wsdl;
/*    */ 
/*    */ import javax.xml.namespace.QName;
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
/*    */ class AccessorElement
/*    */ {
/*    */   private QName type;
/*    */   private String name;
/*    */   
/*    */   public AccessorElement(String name, QName type) {
/* 47 */     this.type = type;
/* 48 */     this.name = name;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public QName getType() {
/* 54 */     return this.type;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void setType(QName type) {
/* 60 */     this.type = type;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public String getName() {
/* 66 */     return this.name;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void setName(String name) {
/* 72 */     this.name = name;
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\ws\processor\modeler\wsdl\AccessorElement.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */