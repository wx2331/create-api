/*    */ package com.sun.codemodel.internal;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
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
/*    */ abstract class JGenerifiableImpl
/*    */   implements JGenerifiable, JDeclaration
/*    */ {
/* 40 */   private List<JTypeVar> typeVariables = null;
/*    */   
/*    */   protected abstract JCodeModel owner();
/*    */   
/*    */   public void declare(JFormatter f) {
/* 45 */     if (this.typeVariables != null) {
/* 46 */       f.p('<');
/* 47 */       for (int i = 0; i < this.typeVariables.size(); i++) {
/* 48 */         if (i != 0) f.p(','); 
/* 49 */         f.d(this.typeVariables.get(i));
/*    */       } 
/* 51 */       f.p('>');
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public JTypeVar generify(String name) {
/* 57 */     JTypeVar v = new JTypeVar(owner(), name);
/* 58 */     if (this.typeVariables == null)
/* 59 */       this.typeVariables = new ArrayList<>(3); 
/* 60 */     this.typeVariables.add(v);
/* 61 */     return v;
/*    */   }
/*    */   
/*    */   public JTypeVar generify(String name, Class<?> bound) {
/* 65 */     return generify(name, owner().ref(bound));
/*    */   }
/*    */   
/*    */   public JTypeVar generify(String name, JClass bound) {
/* 69 */     return generify(name).bound(bound);
/*    */   }
/*    */   
/*    */   public JTypeVar[] typeParams() {
/* 73 */     if (this.typeVariables == null) {
/* 74 */       return JTypeVar.EMPTY_ARRAY;
/*    */     }
/* 76 */     return this.typeVariables.<JTypeVar>toArray(new JTypeVar[this.typeVariables.size()]);
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\codemodel\internal\JGenerifiableImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */