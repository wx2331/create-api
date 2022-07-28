/*    */ package com.sun.tools.javadoc;
/*    */ 
/*    */ import com.sun.javadoc.AnnotationTypeElementDoc;
/*    */ import com.sun.javadoc.AnnotationValue;
/*    */ import com.sun.source.util.TreePath;
/*    */ import com.sun.tools.javac.code.Symbol;
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
/*    */ public class AnnotationTypeElementDocImpl
/*    */   extends MethodDocImpl
/*    */   implements AnnotationTypeElementDoc
/*    */ {
/*    */   public AnnotationTypeElementDocImpl(DocEnv paramDocEnv, Symbol.MethodSymbol paramMethodSymbol) {
/* 49 */     super(paramDocEnv, paramMethodSymbol);
/*    */   }
/*    */   
/*    */   public AnnotationTypeElementDocImpl(DocEnv paramDocEnv, Symbol.MethodSymbol paramMethodSymbol, TreePath paramTreePath) {
/* 53 */     super(paramDocEnv, paramMethodSymbol, paramTreePath);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean isAnnotationTypeElement() {
/* 61 */     return !isMethod();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean isMethod() {
/* 70 */     return this.env.legacyDoclet;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean isAbstract() {
/* 78 */     return false;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public AnnotationValue defaultValue() {
/* 86 */     return (this.sym.defaultValue == null) ? null : new AnnotationValueImpl(this.env, this.sym.defaultValue);
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\javadoc\AnnotationTypeElementDocImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */