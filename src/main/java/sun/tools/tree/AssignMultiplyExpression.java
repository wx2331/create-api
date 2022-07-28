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
/*    */ public class AssignMultiplyExpression
/*    */   extends AssignOpExpression
/*    */ {
/*    */   public AssignMultiplyExpression(long paramLong, Expression paramExpression1, Expression paramExpression2) {
/* 42 */     super(2, paramLong, paramExpression1, paramExpression2);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   void codeOperation(Environment paramEnvironment, Context paramContext, Assembler paramAssembler) {
/* 49 */     paramAssembler.add(this.where, 104 + this.itype.getTypeCodeOffset());
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\sun\tools\tree\AssignMultiplyExpression.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */