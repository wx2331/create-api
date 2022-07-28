/*     */ package com.sun.xml.internal.rngom.binary;
/*     */ 
/*     */ import com.sun.xml.internal.rngom.binary.visitor.PatternFunction;
/*     */ import com.sun.xml.internal.rngom.binary.visitor.PatternVisitor;
/*     */ import org.xml.sax.Locator;
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
/*     */ public class ListPattern
/*     */   extends Pattern
/*     */ {
/*     */   Pattern p;
/*     */   Locator locator;
/*     */   
/*     */   ListPattern(Pattern p, Locator locator) {
/*  58 */     super(false, 3, 
/*     */         
/*  60 */         combineHashCode(37, p.hashCode()));
/*  61 */     this.p = p;
/*  62 */     this.locator = locator;
/*     */   }
/*     */ 
/*     */   
/*     */   Pattern expand(SchemaPatternBuilder b) {
/*  67 */     Pattern ep = this.p.expand(b);
/*  68 */     if (ep != this.p) {
/*  69 */       return b.makeList(ep, this.locator);
/*     */     }
/*  71 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   void checkRecursion(int depth) throws SAXException {
/*  76 */     this.p.checkRecursion(depth);
/*     */   }
/*     */   
/*     */   boolean samePattern(Pattern other) {
/*  80 */     return (other instanceof ListPattern && this.p == ((ListPattern)other).p);
/*     */   }
/*     */ 
/*     */   
/*     */   public void accept(PatternVisitor visitor) {
/*  85 */     visitor.visitList(this.p);
/*     */   }
/*     */   
/*     */   public Object apply(PatternFunction f) {
/*  89 */     return f.caseList(this);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   void checkRestrictions(int context, DuplicateAttributeDetector dad, Alphabet alpha) throws RestrictionViolationException {
/*  95 */     switch (context) {
/*     */       case 7:
/*  97 */         throw new RestrictionViolationException("data_except_contains_list");
/*     */       case 0:
/*  99 */         throw new RestrictionViolationException("start_contains_list");
/*     */       case 6:
/* 101 */         throw new RestrictionViolationException("list_contains_list");
/*     */     } 
/*     */     try {
/* 104 */       this.p.checkRestrictions(6, dad, null);
/*     */     }
/* 106 */     catch (RestrictionViolationException e) {
/* 107 */       e.maybeSetLocator(this.locator);
/* 108 */       throw e;
/*     */     } 
/*     */   }
/*     */   
/*     */   Pattern getOperand() {
/* 113 */     return this.p;
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\xml\internal\rngom\binary\ListPattern.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */