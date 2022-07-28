/*    */ package com.sun.xml.internal.rngom.binary;
/*    */ 
/*    */ import com.sun.xml.internal.rngom.binary.visitor.PatternFunction;
/*    */ import com.sun.xml.internal.rngom.binary.visitor.PatternVisitor;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ChoicePattern
/*    */   extends BinaryPattern
/*    */ {
/*    */   ChoicePattern(Pattern p1, Pattern p2) {
/* 53 */     super((p1.isNullable() || p2.isNullable()), 
/* 54 */         combineHashCode(11, p1.hashCode(), p2.hashCode()), p1, p2);
/*    */   }
/*    */ 
/*    */   
/*    */   Pattern expand(SchemaPatternBuilder b) {
/* 59 */     Pattern ep1 = this.p1.expand(b);
/* 60 */     Pattern ep2 = this.p2.expand(b);
/* 61 */     if (ep1 != this.p1 || ep2 != this.p2) {
/* 62 */       return b.makeChoice(ep1, ep2);
/*    */     }
/* 64 */     return this;
/*    */   }
/*    */   
/*    */   boolean containsChoice(Pattern p) {
/* 68 */     return (this.p1.containsChoice(p) || this.p2.containsChoice(p));
/*    */   }
/*    */   
/*    */   public void accept(PatternVisitor visitor) {
/* 72 */     visitor.visitChoice(this.p1, this.p2);
/*    */   }
/*    */   
/*    */   public Object apply(PatternFunction f) {
/* 76 */     return f.caseChoice(this);
/*    */   }
/*    */ 
/*    */   
/*    */   void checkRestrictions(int context, DuplicateAttributeDetector dad, Alphabet alpha) throws RestrictionViolationException {
/* 81 */     if (dad != null)
/* 82 */       dad.startChoice(); 
/* 83 */     this.p1.checkRestrictions(context, dad, alpha);
/* 84 */     if (dad != null)
/* 85 */       dad.alternative(); 
/* 86 */     this.p2.checkRestrictions(context, dad, alpha);
/* 87 */     if (dad != null)
/* 88 */       dad.endChoice(); 
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\xml\internal\rngom\binary\ChoicePattern.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */