/*    */ package com.sun.source.tree;
/*    */ 
/*    */ import java.util.List;
/*    */ import jdk.Exported;
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
/*    */ @Exported
/*    */ public interface LambdaExpressionTree
/*    */   extends ExpressionTree
/*    */ {
/*    */   List<? extends VariableTree> getParameters();
/*    */   
/*    */   Tree getBody();
/*    */   
/*    */   BodyKind getBodyKind();
/*    */   
/*    */   @Exported
/*    */   public enum BodyKind
/*    */   {
/* 50 */     EXPRESSION,
/*    */     
/* 52 */     STATEMENT;
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\source\tree\LambdaExpressionTree.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */