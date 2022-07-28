/*    */ package com.sun.tools.internal.ws.processor.model;
/*    */ 
/*    */ import com.sun.istack.internal.localization.Localizable;
/*    */ import com.sun.tools.internal.ws.processor.ProcessorException;
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
/*    */ public class ModelException
/*    */   extends ProcessorException
/*    */ {
/*    */   public ModelException(String key, Object... args) {
/* 42 */     super(key, args);
/*    */   }
/*    */   
/*    */   public ModelException(Throwable throwable) {
/* 46 */     super(throwable);
/*    */   }
/*    */   
/*    */   public ModelException(Localizable arg) {
/* 50 */     super("model.nestedModelError", new Object[] { arg });
/*    */   }
/*    */   
/*    */   public String getDefaultResourceBundleName() {
/* 54 */     return "com.sun.tools.internal.ws.resources.model";
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\ws\processor\model\ModelException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */