/*     */ package com.sun.tools.javac.comp;
/*     */ 
/*     */ import com.sun.tools.javac.code.Lint;
/*     */ import com.sun.tools.javac.code.Scope;
/*     */ import com.sun.tools.javac.code.Symbol;
/*     */ import com.sun.tools.javac.code.Type;
/*     */ import com.sun.tools.javac.tree.JCTree;
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
/*     */ public class AttrContext
/*     */ {
/*  44 */   Scope scope = null;
/*     */ 
/*     */ 
/*     */   
/*  48 */   int staticLevel = 0;
/*     */ 
/*     */ 
/*     */   
/*     */   boolean isSelfCall = false;
/*     */ 
/*     */ 
/*     */   
/*     */   boolean selectSuper = false;
/*     */ 
/*     */ 
/*     */   
/*     */   boolean isSerializable = false;
/*     */ 
/*     */ 
/*     */   
/*  64 */   Resolve.MethodResolutionPhase pendingResolutionPhase = null;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   Lint lint;
/*     */ 
/*     */ 
/*     */   
/*  73 */   Symbol enclVar = null;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  78 */   Attr.ResultInfo returnResult = null;
/*     */ 
/*     */ 
/*     */   
/*  82 */   Type defaultSuperCallSite = null;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   JCTree preferredTreeForDiagnostics;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   AttrContext dup(Scope paramScope) {
/*  94 */     AttrContext attrContext = new AttrContext();
/*  95 */     attrContext.scope = paramScope;
/*  96 */     attrContext.staticLevel = this.staticLevel;
/*  97 */     attrContext.isSelfCall = this.isSelfCall;
/*  98 */     attrContext.selectSuper = this.selectSuper;
/*  99 */     attrContext.pendingResolutionPhase = this.pendingResolutionPhase;
/* 100 */     attrContext.lint = this.lint;
/* 101 */     attrContext.enclVar = this.enclVar;
/* 102 */     attrContext.returnResult = this.returnResult;
/* 103 */     attrContext.defaultSuperCallSite = this.defaultSuperCallSite;
/* 104 */     attrContext.isSerializable = this.isSerializable;
/* 105 */     attrContext.preferredTreeForDiagnostics = this.preferredTreeForDiagnostics;
/* 106 */     return attrContext;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   AttrContext dup() {
/* 112 */     return dup(this.scope);
/*     */   }
/*     */   
/*     */   public Iterable<Symbol> getLocalElements() {
/* 116 */     if (this.scope == null)
/* 117 */       return (Iterable<Symbol>)List.nil(); 
/* 118 */     return this.scope.getElements();
/*     */   }
/*     */   
/*     */   boolean lastResolveVarargs() {
/* 122 */     return (this.pendingResolutionPhase != null && this.pendingResolutionPhase
/* 123 */       .isVarargsRequired());
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 128 */     return "AttrContext[" + this.scope.toString() + "]";
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\javac\comp\AttrContext.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */