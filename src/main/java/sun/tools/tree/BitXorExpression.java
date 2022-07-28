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
/*    */ public class BitXorExpression
/*    */   extends BinaryBitExpression
/*    */ {
/*    */   public BitXorExpression(long paramLong, Expression paramExpression1, Expression paramExpression2) {
/* 42 */     super(17, paramLong, paramExpression1, paramExpression2);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   Expression eval(boolean paramBoolean1, boolean paramBoolean2) {
/* 49 */     return new BooleanExpression(this.where, paramBoolean1 ^ paramBoolean2);
/*    */   }
/*    */   Expression eval(int paramInt1, int paramInt2) {
/* 52 */     return new IntExpression(this.where, paramInt1 ^ paramInt2);
/*    */   }
/*    */   Expression eval(long paramLong1, long paramLong2) {
/* 55 */     return new LongExpression(this.where, paramLong1 ^ paramLong2);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   Expression simplify() {
/* 63 */     if (this.left.equals(true)) {
/* 64 */       return new NotExpression(this.where, this.right);
/*    */     }
/* 66 */     if (this.right.equals(true)) {
/* 67 */       return new NotExpression(this.where, this.left);
/*    */     }
/* 69 */     if (this.left.equals(false) || this.left.equals(0)) {
/* 70 */       return this.right;
/*    */     }
/* 72 */     if (this.right.equals(false) || this.right.equals(0)) {
/* 73 */       return this.left;
/*    */     }
/* 75 */     return this;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   void codeOperation(Environment paramEnvironment, Context paramContext, Assembler paramAssembler) {
/* 82 */     paramAssembler.add(this.where, 130 + this.type.getTypeCodeOffset());
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\sun\tools\tree\BitXorExpression.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */