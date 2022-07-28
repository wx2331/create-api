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
/*    */ 
/*    */ 
/*    */ public abstract class LazyBlockReference
/*    */   implements BlockReference
/*    */ {
/* 38 */   private JBlock block = null;
/*    */ 
/*    */ 
/*    */   
/*    */   protected abstract JBlock create();
/*    */ 
/*    */ 
/*    */   
/*    */   public JBlock get(boolean create) {
/* 47 */     if (!create) return this.block; 
/* 48 */     if (this.block == null)
/* 49 */       this.block = create(); 
/* 50 */     return this.block;
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\xjc\generato\\util\LazyBlockReference.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */