/*    */ package com.sun.source.tree;
/*    */ 
/*    */ import java.util.List;
/*    */ import javax.lang.model.element.Name;
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
/*    */ @Exported
/*    */ public interface MemberReferenceTree
/*    */   extends ExpressionTree
/*    */ {
/*    */   ReferenceMode getMode();
/*    */   
/*    */   ExpressionTree getQualifierExpression();
/*    */   
/*    */   Name getName();
/*    */   
/*    */   List<? extends ExpressionTree> getTypeArguments();
/*    */   
/*    */   @Exported
/*    */   public enum ReferenceMode
/*    */   {
/* 52 */     INVOKE,
/*    */     
/* 54 */     NEW;
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\source\tree\MemberReferenceTree.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */