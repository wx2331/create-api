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
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class JNullType
/*    */   extends JClass
/*    */ {
/*    */   JNullType(JCodeModel _owner) {
/* 44 */     super(_owner);
/*    */   }
/*    */   
/* 47 */   public String name() { return "null"; } public String fullName() {
/* 48 */     return "null";
/*    */   } public JPackage _package() {
/* 50 */     return owner()._package("");
/*    */   } public JClass _extends() {
/* 52 */     return null;
/*    */   }
/*    */   public Iterator<JClass> _implements() {
/* 55 */     return Collections.<JClass>emptyList().iterator();
/*    */   }
/*    */   
/* 58 */   public boolean isInterface() { return false; } public boolean isAbstract() {
/* 59 */     return false;
/*    */   }
/*    */   protected JClass substituteParams(JTypeVar[] variables, List<JClass> bindings) {
/* 62 */     return this;
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\codemodel\internal\JNullType.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */