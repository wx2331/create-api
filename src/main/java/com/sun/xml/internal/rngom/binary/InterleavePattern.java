/*     */ package com.sun.xml.internal.rngom.binary;
/*     */ 
/*     */ import com.sun.xml.internal.rngom.binary.visitor.PatternFunction;
/*     */ import com.sun.xml.internal.rngom.binary.visitor.PatternVisitor;
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
/*     */ public class InterleavePattern
/*     */   extends BinaryPattern
/*     */ {
/*     */   InterleavePattern(Pattern p1, Pattern p2) {
/*  53 */     super((p1.isNullable() && p2.isNullable()), 
/*  54 */         combineHashCode(17, p1.hashCode(), p2.hashCode()), p1, p2);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   Pattern expand(SchemaPatternBuilder b) {
/*  60 */     Pattern ep1 = this.p1.expand(b);
/*  61 */     Pattern ep2 = this.p2.expand(b);
/*  62 */     if (ep1 != this.p1 || ep2 != this.p2) {
/*  63 */       return b.makeInterleave(ep1, ep2);
/*     */     }
/*  65 */     return this;
/*     */   }
/*     */   
/*     */   void checkRestrictions(int context, DuplicateAttributeDetector dad, Alphabet alpha) throws RestrictionViolationException {
/*     */     Alphabet a1;
/*  70 */     switch (context) {
/*     */       case 0:
/*  72 */         throw new RestrictionViolationException("start_contains_interleave");
/*     */       case 7:
/*  74 */         throw new RestrictionViolationException("data_except_contains_interleave");
/*     */       case 6:
/*  76 */         throw new RestrictionViolationException("list_contains_interleave");
/*     */     } 
/*  78 */     if (context == 2) {
/*  79 */       context = 4;
/*     */     }
/*  81 */     if (alpha != null && alpha.isEmpty()) {
/*  82 */       a1 = alpha;
/*     */     } else {
/*  84 */       a1 = new Alphabet();
/*  85 */     }  this.p1.checkRestrictions(context, dad, a1);
/*  86 */     if (a1.isEmpty()) {
/*  87 */       this.p2.checkRestrictions(context, dad, a1);
/*     */     } else {
/*  89 */       Alphabet a2 = new Alphabet();
/*  90 */       this.p2.checkRestrictions(context, dad, a2);
/*  91 */       a1.checkOverlap(a2);
/*  92 */       if (alpha != null) {
/*  93 */         if (alpha != a1)
/*  94 */           alpha.addAlphabet(a1); 
/*  95 */         alpha.addAlphabet(a2);
/*     */       } 
/*     */     } 
/*  98 */     if (context != 6 && 
/*  99 */       !contentTypeGroupable(this.p1.getContentType(), this.p2.getContentType()))
/* 100 */       throw new RestrictionViolationException("interleave_string"); 
/* 101 */     if (this.p1.getContentType() == 2 && this.p2
/* 102 */       .getContentType() == 2)
/* 103 */       throw new RestrictionViolationException("interleave_text_overlap"); 
/*     */   }
/*     */   
/*     */   public void accept(PatternVisitor visitor) {
/* 107 */     visitor.visitInterleave(this.p1, this.p2);
/*     */   }
/*     */   public Object apply(PatternFunction f) {
/* 110 */     return f.caseInterleave(this);
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\xml\internal\rngom\binary\InterleavePattern.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */