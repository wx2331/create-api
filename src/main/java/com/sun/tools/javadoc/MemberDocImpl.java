/*    */ package com.sun.tools.javadoc;
/*    */ 
/*    */ import com.sun.javadoc.MemberDoc;
/*    */ import com.sun.source.util.TreePath;
/*    */ import com.sun.tools.javac.code.Symbol;
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
/*    */ public abstract class MemberDocImpl
/*    */   extends ProgramElementDocImpl
/*    */   implements MemberDoc
/*    */ {
/*    */   public MemberDocImpl(DocEnv paramDocEnv, Symbol paramSymbol, TreePath paramTreePath) {
/* 60 */     super(paramDocEnv, paramSymbol, paramTreePath);
/*    */   }
/*    */   
/*    */   public abstract boolean isSynthetic();
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\javadoc\MemberDocImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */