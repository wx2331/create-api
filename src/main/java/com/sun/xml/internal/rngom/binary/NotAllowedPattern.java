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
/*    */ public class NotAllowedPattern
/*    */   extends Pattern
/*    */ {
/*    */   NotAllowedPattern() {
/* 53 */     super(false, 0, 7);
/*    */   }
/*    */   
/*    */   boolean isNotAllowed() {
/* 57 */     return true;
/*    */   }
/*    */   
/*    */   boolean samePattern(Pattern other) {
/* 61 */     return (other.getClass() == getClass());
/*    */   }
/*    */   public void accept(PatternVisitor visitor) {
/* 64 */     visitor.visitNotAllowed();
/*    */   }
/*    */   public Object apply(PatternFunction f) {
/* 67 */     return f.caseNotAllowed(this);
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\xml\internal\rngom\binary\NotAllowedPattern.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */