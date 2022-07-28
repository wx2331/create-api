/*     */ package com.sun.tools.javac.comp;
/*     */ 
/*     */ import com.sun.tools.javac.tree.JCTree;
/*     */ import java.util.Iterator;
/*     */ import java.util.NoSuchElementException;
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
/*     */ public class Env<A>
/*     */   implements Iterable<Env<A>>
/*     */ {
/*     */   public Env<A> next;
/*     */   public Env<A> outer;
/*     */   public JCTree tree;
/*     */   public JCTree.JCCompilationUnit toplevel;
/*     */   public JCTree.JCClassDecl enclClass;
/*     */   public JCTree.JCMethodDecl enclMethod;
/*     */   public A info;
/*     */   public boolean baseClause = false;
/*     */   
/*     */   public Env(JCTree paramJCTree, A paramA) {
/*  82 */     this.next = null;
/*  83 */     this.outer = null;
/*  84 */     this.tree = paramJCTree;
/*  85 */     this.toplevel = null;
/*  86 */     this.enclClass = null;
/*  87 */     this.enclMethod = null;
/*  88 */     this.info = paramA;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Env<A> dup(JCTree paramJCTree, A paramA) {
/*  95 */     return dupto(new Env(paramJCTree, paramA));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Env<A> dupto(Env<A> paramEnv) {
/* 102 */     paramEnv.next = this;
/* 103 */     paramEnv.outer = this.outer;
/* 104 */     paramEnv.toplevel = this.toplevel;
/* 105 */     paramEnv.enclClass = this.enclClass;
/* 106 */     paramEnv.enclMethod = this.enclMethod;
/* 107 */     return paramEnv;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Env<A> dup(JCTree paramJCTree) {
/* 114 */     return dup(paramJCTree, this.info);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Env<A> enclosing(JCTree.Tag paramTag) {
/* 120 */     Env<A> env = this;
/* 121 */     for (; env != null && !env.tree.hasTag(paramTag); env = env.next);
/* 122 */     return env;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 127 */     StringBuilder stringBuilder = new StringBuilder();
/* 128 */     stringBuilder.append("Env[").append(this.info);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 133 */     if (this.outer != null)
/* 134 */       stringBuilder.append(",outer=").append(this.outer); 
/* 135 */     stringBuilder.append("]");
/* 136 */     return stringBuilder.toString();
/*     */   }
/*     */   
/*     */   public Iterator<Env<A>> iterator() {
/* 140 */     return new Iterator<Env<A>>() {
/* 141 */         Env<A> next = Env.this;
/*     */         public boolean hasNext() {
/* 143 */           return (this.next.outer != null);
/*     */         }
/*     */         public Env<A> next() {
/* 146 */           if (hasNext()) {
/* 147 */             Env<A> env = this.next;
/* 148 */             this.next = env.outer;
/* 149 */             return env;
/*     */           } 
/* 151 */           throw new NoSuchElementException();
/*     */         }
/*     */         
/*     */         public void remove() {
/* 155 */           throw new UnsupportedOperationException();
/*     */         }
/*     */       };
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\javac\comp\Env.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */