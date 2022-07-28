/*    */ package com.sun.codemodel.internal;
/*    */ 
/*    */ import java.util.Collections;
/*    */ import java.util.Iterator;
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
/*    */ final class JDirectClass
/*    */   extends JClass
/*    */ {
/*    */   private final String fullName;
/*    */   
/*    */   public JDirectClass(JCodeModel _owner, String fullName) {
/* 43 */     super(_owner);
/* 44 */     this.fullName = fullName;
/*    */   }
/*    */   
/*    */   public String name() {
/* 48 */     int i = this.fullName.lastIndexOf('.');
/* 49 */     if (i >= 0) return this.fullName.substring(i + 1); 
/* 50 */     return this.fullName;
/*    */   }
/*    */   
/*    */   public String fullName() {
/* 54 */     return this.fullName;
/*    */   }
/*    */   
/*    */   public JPackage _package() {
/* 58 */     int i = this.fullName.lastIndexOf('.');
/* 59 */     if (i >= 0) return owner()._package(this.fullName.substring(0, i)); 
/* 60 */     return owner().rootPackage();
/*    */   }
/*    */   
/*    */   public JClass _extends() {
/* 64 */     return owner().ref(Object.class);
/*    */   }
/*    */   
/*    */   public Iterator<JClass> _implements() {
/* 68 */     return Collections.<JClass>emptyList().iterator();
/*    */   }
/*    */   
/*    */   public boolean isInterface() {
/* 72 */     return false;
/*    */   }
/*    */   
/*    */   public boolean isAbstract() {
/* 76 */     return false;
/*    */   }
/*    */   
/*    */   protected JClass substituteParams(JTypeVar[] variables, List<JClass> bindings) {
/* 80 */     return this;
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\codemodel\internal\JDirectClass.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */