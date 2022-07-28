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
/*    */ 
/*    */ public class AfterPattern
/*    */   extends BinaryPattern
/*    */ {
/*    */   AfterPattern(Pattern p1, Pattern p2) {
/* 54 */     super(false, 
/* 55 */         combineHashCode(41, p1.hashCode(), p2.hashCode()), p1, p2);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   boolean isNotAllowed() {
/* 61 */     return this.p1.isNotAllowed();
/*    */   }
/*    */   
/*    */   public Object apply(PatternFunction f) {
/* 65 */     return f.caseAfter(this);
/*    */   }
/*    */   public void accept(PatternVisitor visitor) {
/* 68 */     visitor.visitAfter(this.p1, this.p2);
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\xml\internal\rngom\binary\AfterPattern.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */