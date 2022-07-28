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
/*    */ public class PreDecExpression
/*    */   extends IncDecExpression
/*    */ {
/*    */   public PreDecExpression(long paramLong, Expression paramExpression) {
/* 42 */     super(40, paramLong, paramExpression);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void codeValue(Environment paramEnvironment, Context paramContext, Assembler paramAssembler) {
/* 49 */     codeIncDec(paramEnvironment, paramContext, paramAssembler, false, true, true);
/*    */   }
/*    */   public void code(Environment paramEnvironment, Context paramContext, Assembler paramAssembler) {
/* 52 */     codeIncDec(paramEnvironment, paramContext, paramAssembler, false, true, false);
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\sun\tools\tree\PreDecExpression.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */