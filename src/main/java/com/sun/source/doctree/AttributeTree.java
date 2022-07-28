/*    */ package com.sun.source.doctree;
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
/*    */ @Exported
/*    */ public interface AttributeTree
/*    */   extends DocTree
/*    */ {
/*    */   Name getName();
/*    */   
/*    */   ValueKind getValueKind();
/*    */   
/*    */   List<? extends DocTree> getValue();
/*    */   
/*    */   @Exported
/*    */   public enum ValueKind
/*    */   {
/* 39 */     EMPTY, UNQUOTED, SINGLE, DOUBLE;
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\source\doctree\AttributeTree.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */