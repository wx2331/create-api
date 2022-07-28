/*     */ package com.sun.xml.internal.rngom.binary;
/*     */ 
/*     */ import com.sun.xml.internal.rngom.binary.visitor.PatternFunction;
/*     */ import com.sun.xml.internal.rngom.binary.visitor.PatternVisitor;
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
/*     */ public class OneOrMorePattern
/*     */   extends Pattern
/*     */ {
/*     */   Pattern p;
/*     */   
/*     */   OneOrMorePattern(Pattern p) {
/*  56 */     super(p.isNullable(), p
/*  57 */         .getContentType(), 
/*  58 */         combineHashCode(19, p.hashCode()));
/*  59 */     this.p = p;
/*     */   }
/*     */ 
/*     */   
/*     */   Pattern expand(SchemaPatternBuilder b) {
/*  64 */     Pattern ep = this.p.expand(b);
/*  65 */     if (ep != this.p) {
/*  66 */       return b.makeOneOrMore(ep);
/*     */     }
/*  68 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   void checkRecursion(int depth) throws SAXException {
/*  73 */     this.p.checkRecursion(depth);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   void checkRestrictions(int context, DuplicateAttributeDetector dad, Alphabet alpha) throws RestrictionViolationException {
/*  79 */     switch (context) {
/*     */       case 0:
/*  81 */         throw new RestrictionViolationException("start_contains_one_or_more");
/*     */       case 7:
/*  83 */         throw new RestrictionViolationException("data_except_contains_one_or_more");
/*     */     } 
/*     */     
/*  86 */     this.p.checkRestrictions((context == 1) ? 2 : context, dad, alpha);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  91 */     if (context != 6 && 
/*  92 */       !contentTypeGroupable(this.p.getContentType(), this.p.getContentType()))
/*  93 */       throw new RestrictionViolationException("one_or_more_string"); 
/*     */   }
/*     */   
/*     */   boolean samePattern(Pattern other) {
/*  97 */     return (other instanceof OneOrMorePattern && this.p == ((OneOrMorePattern)other).p);
/*     */   }
/*     */ 
/*     */   
/*     */   public void accept(PatternVisitor visitor) {
/* 102 */     visitor.visitOneOrMore(this.p);
/*     */   }
/*     */   
/*     */   public Object apply(PatternFunction f) {
/* 106 */     return f.caseOneOrMore(this);
/*     */   }
/*     */   
/*     */   Pattern getOperand() {
/* 110 */     return this.p;
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\xml\internal\rngom\binary\OneOrMorePattern.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */