/*    */ package com.sun.xml.internal.rngom.binary;
/*    */ 
/*    */ import com.sun.xml.internal.rngom.binary.visitor.PatternFunction;
/*    */ import com.sun.xml.internal.rngom.binary.visitor.PatternVisitor;
/*    */ import org.relaxng.datatype.Datatype;
/*    */ import org.xml.sax.Locator;
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
/*    */ public class DataExceptPattern
/*    */   extends DataPattern
/*    */ {
/*    */   private Pattern except;
/*    */   private Locator loc;
/*    */   
/*    */   DataExceptPattern(Datatype dt, Pattern except, Locator loc) {
/* 58 */     super(dt);
/* 59 */     this.except = except;
/* 60 */     this.loc = loc;
/*    */   }
/*    */   
/*    */   boolean samePattern(Pattern other) {
/* 64 */     if (!super.samePattern(other))
/* 65 */       return false; 
/* 66 */     return this.except.samePattern(((DataExceptPattern)other).except);
/*    */   }
/*    */ 
/*    */   
/*    */   public void accept(PatternVisitor visitor) {
/* 71 */     visitor.visitDataExcept(getDatatype(), this.except);
/*    */   }
/*    */ 
/*    */   
/*    */   public Object apply(PatternFunction f) {
/* 76 */     return f.caseDataExcept(this);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   void checkRestrictions(int context, DuplicateAttributeDetector dad, Alphabet alpha) throws RestrictionViolationException {
/* 82 */     super.checkRestrictions(context, dad, alpha);
/*    */     try {
/* 84 */       this.except.checkRestrictions(7, null, null);
/*    */     }
/* 86 */     catch (RestrictionViolationException e) {
/* 87 */       e.maybeSetLocator(this.loc);
/* 88 */       throw e;
/*    */     } 
/*    */   }
/*    */   
/*    */   Pattern getExcept() {
/* 93 */     return this.except;
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\xml\internal\rngom\binary\DataExceptPattern.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */