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
/*    */ public class GroupPattern
/*    */   extends BinaryPattern
/*    */ {
/*    */   GroupPattern(Pattern p1, Pattern p2) {
/* 52 */     super((p1.isNullable() && p2.isNullable()), 
/* 53 */         combineHashCode(13, p1.hashCode(), p2.hashCode()), p1, p2);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   Pattern expand(SchemaPatternBuilder b) {
/* 60 */     Pattern ep1 = this.p1.expand(b);
/* 61 */     Pattern ep2 = this.p2.expand(b);
/* 62 */     if (ep1 != this.p1 || ep2 != this.p2) {
/* 63 */       return b.makeGroup(ep1, ep2);
/*    */     }
/* 65 */     return this;
/*    */   }
/*    */ 
/*    */   
/*    */   void checkRestrictions(int context, DuplicateAttributeDetector dad, Alphabet alpha) throws RestrictionViolationException {
/* 70 */     switch (context) {
/*    */       case 0:
/* 72 */         throw new RestrictionViolationException("start_contains_group");
/*    */       case 7:
/* 74 */         throw new RestrictionViolationException("data_except_contains_group");
/*    */     } 
/* 76 */     super.checkRestrictions((context == 2) ? 3 : context, dad, alpha);
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 81 */     if (context != 6 && 
/* 82 */       !contentTypeGroupable(this.p1.getContentType(), this.p2.getContentType()))
/* 83 */       throw new RestrictionViolationException("group_string"); 
/*    */   }
/*    */   
/*    */   public void accept(PatternVisitor visitor) {
/* 87 */     visitor.visitGroup(this.p1, this.p2);
/*    */   }
/*    */   public Object apply(PatternFunction f) {
/* 90 */     return f.caseGroup(this);
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\xml\internal\rngom\binary\GroupPattern.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */