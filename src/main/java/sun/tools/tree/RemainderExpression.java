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
/*    */ public class RemainderExpression
/*    */   extends DivRemExpression
/*    */ {
/*    */   public RemainderExpression(long paramLong, Expression paramExpression1, Expression paramExpression2) {
/* 42 */     super(32, paramLong, paramExpression1, paramExpression2);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   Expression eval(int paramInt1, int paramInt2) {
/* 49 */     return new IntExpression(this.where, paramInt1 % paramInt2);
/*    */   }
/*    */   Expression eval(long paramLong1, long paramLong2) {
/* 52 */     return new LongExpression(this.where, paramLong1 % paramLong2);
/*    */   }
/*    */   Expression eval(float paramFloat1, float paramFloat2) {
/* 55 */     return new FloatExpression(this.where, paramFloat1 % paramFloat2);
/*    */   }
/*    */   Expression eval(double paramDouble1, double paramDouble2) {
/* 58 */     return new DoubleExpression(this.where, paramDouble1 % paramDouble2);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   void codeOperation(Environment paramEnvironment, Context paramContext, Assembler paramAssembler) {
/* 65 */     paramAssembler.add(this.where, 112 + this.type.getTypeCodeOffset());
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\sun\tools\tree\RemainderExpression.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */