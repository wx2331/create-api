/*    */ package com.sun.tools.internal.ws.wsdl.framework;
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
/*    */ public class NoSuchEntityException
/*    */   extends ValidationException
/*    */ {
/*    */   public NoSuchEntityException(QName name) {
/* 38 */     super("entity.notFoundByQName", new Object[] { name
/*    */           
/* 40 */           .getLocalPart(), name.getNamespaceURI() });
/*    */   }
/*    */   
/*    */   public NoSuchEntityException(String id) {
/* 44 */     super("entity.notFoundByID", new Object[] { id });
/*    */   }
/*    */   
/*    */   public String getDefaultResourceBundleName() {
/* 48 */     return "com.sun.tools.internal.ws.resources.wsdl";
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\ws\wsdl\framework\NoSuchEntityException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */