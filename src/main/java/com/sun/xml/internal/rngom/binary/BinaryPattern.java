/*     */ package com.sun.xml.internal.rngom.binary;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import org.xml.sax.SAXException;
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
/*     */ public abstract class BinaryPattern
/*     */   extends Pattern
/*     */ {
/*     */   protected final Pattern p1;
/*     */   protected final Pattern p2;
/*     */   
/*     */   BinaryPattern(boolean nullable, int hc, Pattern p1, Pattern p2) {
/*  59 */     super(nullable, Math.max(p1.getContentType(), p2.getContentType()), hc);
/*  60 */     this.p1 = p1;
/*  61 */     this.p2 = p2;
/*     */   }
/*     */   
/*     */   void checkRecursion(int depth) throws SAXException {
/*  65 */     this.p1.checkRecursion(depth);
/*  66 */     this.p2.checkRecursion(depth);
/*     */   }
/*     */ 
/*     */   
/*     */   void checkRestrictions(int context, DuplicateAttributeDetector dad, Alphabet alpha) throws RestrictionViolationException {
/*  71 */     this.p1.checkRestrictions(context, dad, alpha);
/*  72 */     this.p2.checkRestrictions(context, dad, alpha);
/*     */   }
/*     */   
/*     */   boolean samePattern(Pattern other) {
/*  76 */     if (getClass() != other.getClass())
/*  77 */       return false; 
/*  78 */     BinaryPattern b = (BinaryPattern)other;
/*  79 */     return (this.p1 == b.p1 && this.p2 == b.p2);
/*     */   }
/*     */   
/*     */   public final Pattern getOperand1() {
/*  83 */     return this.p1;
/*     */   }
/*     */   
/*     */   public final Pattern getOperand2() {
/*  87 */     return this.p2;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void fillChildren(Collection col) {
/*  99 */     fillChildren(getClass(), this.p1, col);
/* 100 */     fillChildren(getClass(), this.p2, col);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final Pattern[] getChildren() {
/* 107 */     List lst = new ArrayList();
/* 108 */     fillChildren(lst);
/* 109 */     return (Pattern[])lst.toArray((Object[])new Pattern[lst.size()]);
/*     */   }
/*     */   
/*     */   private void fillChildren(Class<?> c, Pattern p, Collection<Pattern> col) {
/* 113 */     if (p.getClass() == c) {
/* 114 */       BinaryPattern bp = (BinaryPattern)p;
/* 115 */       bp.fillChildren(c, bp.p1, col);
/* 116 */       bp.fillChildren(c, bp.p2, col);
/*     */     } else {
/* 118 */       col.add(p);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\xml\internal\rngom\binary\BinaryPattern.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */