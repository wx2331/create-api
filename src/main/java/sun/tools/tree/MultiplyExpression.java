/*    */ package sun.tools.tree;
/*    */ 
/*    */ import sun.tools.asm.Assembler;
/*    */ import sun.tools.java.Environment;
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
/*    */ public class MultiplyExpression
/*    */   extends BinaryArithmeticExpression
/*    */ {
/*    */   public MultiplyExpression(long paramLong, Expression paramExpression1, Expression paramExpression2) {
/* 42 */     super(33, paramLong, paramExpression1, paramExpression2);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   Expression eval(int paramInt1, int paramInt2) {
/* 49 */     return new IntExpression(this.where, paramInt1 * paramInt2);
/*    */   }
/*    */   Expression eval(long paramLong1, long paramLong2) {
/* 52 */     return new LongExpression(this.where, paramLong1 * paramLong2);
/*    */   }
/*    */   Expression eval(float paramFloat1, float paramFloat2) {
/* 55 */     return new FloatExpression(this.where, paramFloat1 * paramFloat2);
/*    */   }
/*    */   Expression eval(double paramDouble1, double paramDouble2) {
/* 58 */     return new DoubleExpression(this.where, paramDouble1 * paramDouble2);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   Expression simplify() {
/* 65 */     if (this.left.equals(1)) {
/* 66 */       return this.right;
/*    */     }
/* 68 */     if (this.right.equals(1)) {
/* 69 */       return this.left;
/*    */     }
/* 71 */     return this;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   void codeOperation(Environment paramEnvironment, Context paramContext, Assembler paramAssembler) {
/* 78 */     paramAssembler.add(this.where, 104 + this.type.getTypeCodeOffset());
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\sun\tools\tree\MultiplyExpression.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */