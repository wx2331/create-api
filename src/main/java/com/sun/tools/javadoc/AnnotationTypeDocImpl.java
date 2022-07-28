/*     */ package com.sun.tools.javadoc;
/*     */ 
/*     */ import com.sun.javadoc.AnnotationTypeDoc;
/*     */ import com.sun.javadoc.AnnotationTypeElementDoc;
/*     */ import com.sun.javadoc.MethodDoc;
/*     */ import com.sun.source.util.TreePath;
/*     */ import com.sun.tools.javac.code.Scope;
/*     */ import com.sun.tools.javac.code.Symbol;
/*     */ import com.sun.tools.javac.util.List;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class AnnotationTypeDocImpl
/*     */   extends ClassDocImpl
/*     */   implements AnnotationTypeDoc
/*     */ {
/*     */   public AnnotationTypeDocImpl(DocEnv paramDocEnv, Symbol.ClassSymbol paramClassSymbol) {
/*  52 */     this(paramDocEnv, paramClassSymbol, (TreePath)null);
/*     */   }
/*     */   
/*     */   public AnnotationTypeDocImpl(DocEnv paramDocEnv, Symbol.ClassSymbol paramClassSymbol, TreePath paramTreePath) {
/*  56 */     super(paramDocEnv, paramClassSymbol, paramTreePath);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isAnnotationType() {
/*  64 */     return !isInterface();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isInterface() {
/*  73 */     return this.env.legacyDoclet;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MethodDoc[] methods(boolean paramBoolean) {
/*  82 */     return this.env.legacyDoclet ? (MethodDoc[])
/*  83 */       elements() : new MethodDoc[0];
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AnnotationTypeElementDoc[] elements() {
/*  93 */     List list = List.nil();
/*  94 */     for (Scope.Entry entry = (this.tsym.members()).elems; entry != null; entry = entry.sibling) {
/*  95 */       if (entry.sym != null && entry.sym.kind == 16) {
/*  96 */         Symbol.MethodSymbol methodSymbol = (Symbol.MethodSymbol)entry.sym;
/*  97 */         list = list.prepend(this.env.getAnnotationTypeElementDoc(methodSymbol));
/*     */       } 
/*     */     } 
/* 100 */     return (AnnotationTypeElementDoc[])list
/* 101 */       .toArray((Object[])new AnnotationTypeElementDoc[list.length()]);
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\javadoc\AnnotationTypeDocImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */