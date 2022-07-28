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
/*    */ public class BitAndExpression
/*    */   extends BinaryBitExpression
/*    */ {
/*    */   public BitAndExpression(long paramLong, Expression paramExpression1, Expression paramExpression2) {
/* 42 */     super(18, paramLong, paramExpression1, paramExpression2);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   Expression eval(boolean paramBoolean1, boolean paramBoolean2) {
/* 49 */     return new BooleanExpression(this.where, paramBoolean1 & paramBoolean2);
/*    */   }
/*    */   Expression eval(int paramInt1, int paramInt2) {
/* 52 */     return new IntExpression(this.where, paramInt1 & paramInt2);
/*    */   }
/*    */   Expression eval(long paramLong1, long paramLong2) {
/* 55 */     return new LongExpression(this.where, paramLong1 & paramLong2);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   Expression simplify() {
/* 62 */     if (this.left.equals(true))
/* 63 */       return this.right; 
/* 64 */     if (this.right.equals(true))
/* 65 */       return this.left; 
/* 66 */     if (this.left.equals(false) || this.left.equals(0))
/* 67 */       return (new CommaExpression(this.where, this.right, this.left)).simplify(); 
/* 68 */     if (this.right.equals(false) || this.right.equals(0))
/* 69 */       return (new CommaExpression(this.where, this.left, this.right)).simplify(); 
/* 70 */     return this;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   void codeOperation(Environment paramEnvironment, Context paramContext, Assembler paramAssembler) {
/* 77 */     paramAssembler.add(this.where, 126 + this.type.getTypeCodeOffset());
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\sun\tools\tree\BitAndExpression.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */