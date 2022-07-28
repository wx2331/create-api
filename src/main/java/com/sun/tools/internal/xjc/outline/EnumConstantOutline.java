/*    */ package com.sun.tools.internal.xjc.outline;
/*    */ 
/*    */ import com.sun.codemodel.internal.JEnumConstant;
/*    */ import com.sun.tools.internal.xjc.model.CEnumConstant;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class EnumConstantOutline
/*    */ {
/*    */   public final CEnumConstant target;
/*    */   public final JEnumConstant constRef;
/*    */   
/*    */   protected EnumConstantOutline(CEnumConstant target, JEnumConstant constRef) {
/* 55 */     this.target = target;
/* 56 */     this.constRef = constRef;
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\xjc\outline\EnumConstantOutline.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */