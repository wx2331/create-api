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
/*    */ public class ErrorPattern
/*    */   extends Pattern
/*    */ {
/*    */   ErrorPattern() {
/* 53 */     super(false, 0, 3);
/*    */   }
/*    */   boolean samePattern(Pattern other) {
/* 56 */     return other instanceof ErrorPattern;
/*    */   }
/*    */   public void accept(PatternVisitor visitor) {
/* 59 */     visitor.visitError();
/*    */   }
/*    */   public Object apply(PatternFunction f) {
/* 62 */     return f.caseError(this);
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\xml\internal\rngom\binary\ErrorPattern.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */