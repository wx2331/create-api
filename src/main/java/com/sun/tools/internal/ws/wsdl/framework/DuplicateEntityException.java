/*    */ package com.sun.tools.internal.ws.wsdl.framework;
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
/*    */ public class DuplicateEntityException
/*    */   extends ValidationException
/*    */ {
/*    */   public DuplicateEntityException(GloballyKnown entity) {
/* 36 */     super("entity.duplicateWithType", new Object[] { entity
/*    */           
/* 38 */           .getElementName().getLocalPart(), entity
/* 39 */           .getName() });
/*    */   }
/*    */   
/*    */   public DuplicateEntityException(Identifiable entity) {
/* 43 */     super("entity.duplicateWithType", new Object[] { entity
/*    */           
/* 45 */           .getElementName().getLocalPart(), entity
/* 46 */           .getID() });
/*    */   }
/*    */   
/*    */   public DuplicateEntityException(Entity entity, String name) {
/* 50 */     super("entity.duplicateWithType", new Object[] { entity
/*    */           
/* 52 */           .getElementName().getLocalPart(), name });
/*    */   }
/*    */   
/*    */   public String getDefaultResourceBundleName() {
/* 56 */     return "com.sun.tools.internal.ws.resources.wsdl";
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\ws\wsdl\framework\DuplicateEntityException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */