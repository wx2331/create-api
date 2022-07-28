/*    */ package com.sun.tools.internal.ws.processor.modeler.annotation;
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
/*    */ public class FaultInfo
/*    */ {
/*    */   public String beanName;
/*    */   public TypeMoniker beanTypeMoniker;
/*    */   public boolean isWsdlException;
/*    */   public QName elementName;
/*    */   
/*    */   public FaultInfo() {}
/*    */   
/*    */   public FaultInfo(String beanName) {
/* 44 */     this.beanName = beanName;
/*    */   }
/*    */   public FaultInfo(String beanName, boolean isWsdlException) {
/* 47 */     this.beanName = beanName;
/* 48 */     this.isWsdlException = isWsdlException;
/*    */   }
/*    */   public FaultInfo(TypeMoniker typeMoniker, boolean isWsdlException) {
/* 51 */     this.beanTypeMoniker = typeMoniker;
/* 52 */     this.isWsdlException = isWsdlException;
/*    */   }
/*    */   
/*    */   public void setIsWsdlException(boolean isWsdlException) {
/* 56 */     this.isWsdlException = isWsdlException;
/*    */   }
/*    */   
/*    */   public boolean isWsdlException() {
/* 60 */     return this.isWsdlException;
/*    */   }
/*    */   
/*    */   public void setBeanName(String beanName) {
/* 64 */     this.beanName = beanName;
/*    */   }
/*    */   
/*    */   public String getBeanName() {
/* 68 */     return this.beanName;
/*    */   }
/*    */   
/*    */   public void setElementName(QName elementName) {
/* 72 */     this.elementName = elementName;
/*    */   }
/*    */   
/*    */   public QName getElementName() {
/* 76 */     return this.elementName;
/*    */   }
/*    */   public void setBeanTypeMoniker(TypeMoniker typeMoniker) {
/* 79 */     this.beanTypeMoniker = typeMoniker;
/*    */   }
/*    */   public TypeMoniker getBeanTypeMoniker() {
/* 82 */     return this.beanTypeMoniker;
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\ws\processor\modeler\annotation\FaultInfo.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */