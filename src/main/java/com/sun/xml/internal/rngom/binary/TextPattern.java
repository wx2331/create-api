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
/*    */ public class TextPattern
/*    */   extends Pattern
/*    */ {
/*    */   TextPattern() {
/* 53 */     super(true, 2, 1);
/*    */   }
/*    */   
/*    */   boolean samePattern(Pattern other) {
/* 57 */     return other instanceof TextPattern;
/*    */   }
/*    */   
/*    */   public void accept(PatternVisitor visitor) {
/* 61 */     visitor.visitText();
/*    */   }
/*    */   
/*    */   public Object apply(PatternFunction f) {
/* 65 */     return f.caseText(this);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   void checkRestrictions(int context, DuplicateAttributeDetector dad, Alphabet alpha) throws RestrictionViolationException {
/* 71 */     switch (context) {
/*    */       case 7:
/* 73 */         throw new RestrictionViolationException("data_except_contains_text");
/*    */       case 0:
/* 75 */         throw new RestrictionViolationException("start_contains_text");
/*    */       case 6:
/* 77 */         throw new RestrictionViolationException("list_contains_text");
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\xml\internal\rngom\binary\TextPattern.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */