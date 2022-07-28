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
/*    */ 
/*    */ public abstract class BinaryBitExpression
/*    */   extends BinaryExpression
/*    */ {
/*    */   public BinaryBitExpression(int paramInt, long paramLong, Expression paramExpression1, Expression paramExpression2) {
/* 43 */     super(paramInt, paramLong, paramExpression1.type, paramExpression1, paramExpression2);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   void selectType(Environment paramEnvironment, Context paramContext, int paramInt) {
/* 50 */     if ((paramInt & 0x1) != 0) {
/* 51 */       this.type = Type.tBoolean;
/* 52 */     } else if ((paramInt & 0x20) != 0) {
/* 53 */       this.type = Type.tLong;
/*    */     } else {
/* 55 */       this.type = Type.tInt;
/*    */     } 
/* 57 */     this.left = convert(paramEnvironment, paramContext, this.type, this.left);
/* 58 */     this.right = convert(paramEnvironment, paramContext, this.type, this.right);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void codeValue(Environment paramEnvironment, Context paramContext, Assembler paramAssembler) {
/* 65 */     this.left.codeValue(paramEnvironment, paramContext, paramAssembler);
/* 66 */     this.right.codeValue(paramEnvironment, paramContext, paramAssembler);
/* 67 */     codeOperation(paramEnvironment, paramContext, paramAssembler);
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\sun\tools\tree\BinaryBitExpression.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */