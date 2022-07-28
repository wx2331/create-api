/*     */ package com.sun.tools.internal.xjc.reader.dtd;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
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
/*     */ final class ModelGroup
/*     */   extends Term
/*     */ {
/*     */   Kind kind;
/*     */   
/*     */   enum Kind
/*     */   {
/*  39 */     CHOICE, SEQUENCE;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  44 */   private final List<Term> terms = new ArrayList<>();
/*     */   void normalize(List<Block> r, boolean optional) {
/*     */     Block b;
/*  47 */     switch (this.kind) {
/*     */       case SEQUENCE:
/*  49 */         for (Term t : this.terms)
/*  50 */           t.normalize(r, optional); 
/*     */         return;
/*     */       case CHOICE:
/*  53 */         b = new Block((isOptional() || optional), isRepeated());
/*  54 */         addAllElements(b);
/*  55 */         r.add(b);
/*     */         return;
/*     */     } 
/*     */   }
/*     */   
/*     */   void addAllElements(Block b) {
/*  61 */     for (Term t : this.terms)
/*  62 */       t.addAllElements(b); 
/*     */   }
/*     */   
/*     */   boolean isOptional() {
/*  66 */     switch (this.kind) {
/*     */       case SEQUENCE:
/*  68 */         for (Term t : this.terms) {
/*  69 */           if (!t.isOptional())
/*  70 */             return false; 
/*  71 */         }  return true;
/*     */       case CHOICE:
/*  73 */         for (Term t : this.terms) {
/*  74 */           if (t.isOptional())
/*  75 */             return true; 
/*  76 */         }  return false;
/*     */     } 
/*  78 */     throw new IllegalArgumentException();
/*     */   }
/*     */ 
/*     */   
/*     */   boolean isRepeated() {
/*  83 */     switch (this.kind) {
/*     */       case SEQUENCE:
/*  85 */         return true;
/*     */       case CHOICE:
/*  87 */         for (Term t : this.terms) {
/*  88 */           if (t.isRepeated())
/*  89 */             return true; 
/*  90 */         }  return false;
/*     */     } 
/*  92 */     throw new IllegalArgumentException();
/*     */   }
/*     */ 
/*     */   
/*     */   void setKind(short connectorType) {
/*     */     Kind k;
/*  98 */     switch (connectorType) {
/*     */       case 1:
/* 100 */         k = Kind.SEQUENCE;
/*     */         break;
/*     */       case 0:
/* 103 */         k = Kind.CHOICE;
/*     */         break;
/*     */       default:
/* 106 */         throw new IllegalArgumentException();
/*     */     } 
/*     */     
/* 109 */     assert this.kind == null || k == this.kind;
/* 110 */     this.kind = k;
/*     */   }
/*     */   
/*     */   void addTerm(Term t) {
/* 114 */     if (t instanceof ModelGroup) {
/* 115 */       ModelGroup mg = (ModelGroup)t;
/* 116 */       if (mg.kind == this.kind) {
/* 117 */         this.terms.addAll(mg.terms);
/*     */         return;
/*     */       } 
/*     */     } 
/* 121 */     this.terms.add(t);
/*     */   }
/*     */ 
/*     */   
/*     */   Term wrapUp() {
/* 126 */     switch (this.terms.size()) {
/*     */       case 0:
/* 128 */         return EMPTY;
/*     */       case 1:
/* 130 */         assert this.kind == null;
/* 131 */         return this.terms.get(0);
/*     */     } 
/* 133 */     assert this.kind != null;
/* 134 */     return this;
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\xjc\reader\dtd\ModelGroup.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */