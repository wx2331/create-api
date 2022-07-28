/*    */ package com.sun.tools.internal.xjc.generator.util;
/*    */ 
/*    */ import com.sun.codemodel.internal.JBlock;
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
/*    */ public class ExistingBlockReference
/*    */   implements BlockReference
/*    */ {
/*    */   private final JBlock block;
/*    */   
/*    */   public ExistingBlockReference(JBlock _block) {
/* 39 */     this.block = _block;
/*    */   }
/*    */   
/*    */   public JBlock get(boolean create) {
/* 43 */     return this.block;
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\xjc\generato\\util\ExistingBlockReference.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */