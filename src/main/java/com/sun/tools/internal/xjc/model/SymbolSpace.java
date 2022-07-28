/*    */ package com.sun.tools.internal.xjc.model;
/*    */ 
/*    */ import com.sun.codemodel.internal.JCodeModel;
/*    */ import com.sun.codemodel.internal.JType;
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
/*    */ public class SymbolSpace
/*    */ {
/*    */   private JType type;
/*    */   private final JCodeModel codeModel;
/*    */   
/*    */   public SymbolSpace(JCodeModel _codeModel) {
/* 55 */     this.codeModel = _codeModel;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public JType getType() {
/* 67 */     if (this.type == null) return (JType)this.codeModel.ref(Object.class); 
/* 68 */     return this.type;
/*    */   }
/*    */   
/*    */   public void setType(JType _type) {
/* 72 */     if (this.type == null)
/* 73 */       this.type = _type; 
/*    */   }
/*    */   
/*    */   public String toString() {
/* 77 */     if (this.type == null) return "undetermined"; 
/* 78 */     return this.type.name();
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\xjc\model\SymbolSpace.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */