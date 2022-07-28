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
/*    */ 
/*    */ public class DivideExpression
/*    */   extends DivRemExpression
/*    */ {
/*    */   public DivideExpression(long paramLong, Expression paramExpression1, Expression paramExpression2) {
/* 43 */     super(31, paramLong, paramExpression1, paramExpression2);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   Expression eval(int paramInt1, int paramInt2) {
/* 50 */     return new IntExpression(this.where, paramInt1 / paramInt2);
/*    */   }
/*    */   Expression eval(long paramLong1, long paramLong2) {
/* 53 */     return new LongExpression(this.where, paramLong1 / paramLong2);
/*    */   }
/*    */   Expression eval(float paramFloat1, float paramFloat2) {
/* 56 */     return new FloatExpression(this.where, paramFloat1 / paramFloat2);
/*    */   }
/*    */   Expression eval(double paramDouble1, double paramDouble2) {
/* 59 */     return new DoubleExpression(this.where, paramDouble1 / paramDouble2);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   Expression simplify() {
/* 75 */     if (this.right.equals(1)) {
/* 76 */       return this.left;
/*    */     }
/* 78 */     return this;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   void codeOperation(Environment paramEnvironment, Context paramContext, Assembler paramAssembler) {
/* 85 */     paramAssembler.add(this.where, 108 + this.type.getTypeCodeOffset());
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\sun\tools\tree\DivideExpression.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */