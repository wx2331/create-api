/*    */ package sun.tools.tree;
/*    */ 
/*    */ import sun.tools.asm.Assembler;
/*    */ import sun.tools.java.Environment;
/*    */ import sun.tools.java.Type;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class BitNotExpression
/*    */   extends UnaryExpression
/*    */ {
/*    */   public BitNotExpression(long paramLong, Expression paramExpression) {
/* 42 */     super(38, paramLong, paramExpression.type, paramExpression);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   void selectType(Environment paramEnvironment, Context paramContext, int paramInt) {
/* 49 */     if ((paramInt & 0x20) != 0) {
/* 50 */       this.type = Type.tLong;
/*    */     } else {
/* 52 */       this.type = Type.tInt;
/*    */     } 
/* 54 */     this.right = convert(paramEnvironment, paramContext, this.type, this.right);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   Expression eval(int paramInt) {
/* 61 */     return new IntExpression(this.where, paramInt ^ 0xFFFFFFFF);
/*    */   }
/*    */   Expression eval(long paramLong) {
/* 64 */     return new LongExpression(this.where, paramLong ^ 0xFFFFFFFFFFFFFFFFL);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   Expression simplify() {
/* 71 */     if (this.right.op == 38) {
/* 72 */       return ((BitNotExpression)this.right).right;
/*    */     }
/* 74 */     return this;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void codeValue(Environment paramEnvironment, Context paramContext, Assembler paramAssembler) {
/* 81 */     this.right.codeValue(paramEnvironment, paramContext, paramAssembler);
/* 82 */     if (this.type.isType(4)) {
/* 83 */       paramAssembler.add(this.where, 18, new Integer(-1));
/* 84 */       paramAssembler.add(this.where, 130);
/*    */     } else {
/* 86 */       paramAssembler.add(this.where, 20, new Long(-1L));
/* 87 */       paramAssembler.add(this.where, 131);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\sun\tools\tree\BitNotExpression.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */