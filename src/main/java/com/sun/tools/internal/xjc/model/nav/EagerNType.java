/*    */ package com.sun.tools.internal.xjc.model.nav;
/*    */ 
/*    */ import com.sun.codemodel.internal.JType;
/*    */ import com.sun.tools.internal.xjc.outline.Aspect;
/*    */ import com.sun.tools.internal.xjc.outline.Outline;
/*    */ import java.lang.reflect.Type;
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
/*    */ class EagerNType
/*    */   implements NType
/*    */ {
/*    */   final Type t;
/*    */   
/*    */   public EagerNType(Type type) {
/* 41 */     this.t = type;
/* 42 */     assert this.t != null;
/*    */   }
/*    */   
/*    */   public JType toType(Outline o, Aspect aspect) {
/*    */     try {
/* 47 */       return o.getCodeModel().parseType(this.t.toString());
/* 48 */     } catch (ClassNotFoundException e) {
/* 49 */       throw new NoClassDefFoundError(e.getMessage());
/*    */     } 
/*    */   }
/*    */   
/*    */   public boolean equals(Object o) {
/* 54 */     if (this == o) return true; 
/* 55 */     if (!(o instanceof EagerNType)) return false;
/*    */     
/* 57 */     EagerNType eagerNType = (EagerNType)o;
/*    */     
/* 59 */     return this.t.equals(eagerNType.t);
/*    */   }
/*    */   
/*    */   public boolean isBoxedType() {
/* 63 */     return false;
/*    */   }
/*    */   
/*    */   public int hashCode() {
/* 67 */     return this.t.hashCode();
/*    */   }
/*    */   
/*    */   public String fullName() {
/* 71 */     return Utils.REFLECTION_NAVIGATOR.getTypeName(this.t);
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\xjc\model\nav\EagerNType.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */