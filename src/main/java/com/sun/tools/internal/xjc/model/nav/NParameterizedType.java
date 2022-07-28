/*    */ package com.sun.tools.internal.xjc.model.nav;
/*    */ 
/*    */ import com.sun.codemodel.internal.JClass;
/*    */ import com.sun.codemodel.internal.JType;
/*    */ import com.sun.tools.internal.xjc.outline.Aspect;
/*    */ import com.sun.tools.internal.xjc.outline.Outline;
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
/*    */ final class NParameterizedType
/*    */   implements NClass
/*    */ {
/*    */   final NClass rawType;
/*    */   final NType[] args;
/*    */   
/*    */   NParameterizedType(NClass rawType, NType[] args) {
/* 43 */     this.rawType = rawType;
/* 44 */     this.args = args;
/* 45 */     assert args.length > 0;
/*    */   }
/*    */   
/*    */   public JClass toType(Outline o, Aspect aspect) {
/* 49 */     JClass r = this.rawType.toType(o, aspect);
/*    */     
/* 51 */     for (NType arg : this.args) {
/* 52 */       r = r.narrow(arg.toType(o, aspect).boxify());
/*    */     }
/* 54 */     return r;
/*    */   }
/*    */   
/*    */   public boolean isAbstract() {
/* 58 */     return this.rawType.isAbstract();
/*    */   }
/*    */   
/*    */   public boolean isBoxedType() {
/* 62 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public String fullName() {
/* 67 */     StringBuilder buf = new StringBuilder();
/* 68 */     buf.append(this.rawType.fullName());
/* 69 */     buf.append('<');
/* 70 */     for (int i = 0; i < this.args.length; i++) {
/* 71 */       if (i != 0)
/* 72 */         buf.append(','); 
/* 73 */       buf.append(this.args[i].fullName());
/*    */     } 
/* 75 */     buf.append('>');
/* 76 */     return buf.toString();
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\xjc\model\nav\NParameterizedType.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */