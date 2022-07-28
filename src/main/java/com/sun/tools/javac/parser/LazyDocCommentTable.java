/*    */ package com.sun.tools.javac.parser;
/*    */ 
/*    */ import com.sun.tools.javac.tree.DCTree;
/*    */ import com.sun.tools.javac.tree.DocCommentTable;
/*    */ import com.sun.tools.javac.tree.JCTree;
/*    */ import com.sun.tools.javac.util.DiagnosticSource;
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
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
/*    */ public class LazyDocCommentTable
/*    */   implements DocCommentTable
/*    */ {
/*    */   ParserFactory fac;
/*    */   DiagnosticSource diagSource;
/*    */   Map<JCTree, Entry> table;
/*    */   
/*    */   private static class Entry
/*    */   {
/*    */     final Tokens.Comment comment;
/*    */     DCTree.DCDocComment tree;
/*    */     
/*    */     Entry(Tokens.Comment param1Comment) {
/* 51 */       this.comment = param1Comment;
/*    */     }
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   LazyDocCommentTable(ParserFactory paramParserFactory) {
/* 60 */     this.fac = paramParserFactory;
/* 61 */     this.diagSource = paramParserFactory.log.currentSource();
/* 62 */     this.table = new HashMap<>();
/*    */   }
/*    */   
/*    */   public boolean hasComment(JCTree paramJCTree) {
/* 66 */     return this.table.containsKey(paramJCTree);
/*    */   }
/*    */   
/*    */   public Tokens.Comment getComment(JCTree paramJCTree) {
/* 70 */     Entry entry = this.table.get(paramJCTree);
/* 71 */     return (entry == null) ? null : entry.comment;
/*    */   }
/*    */   
/*    */   public String getCommentText(JCTree paramJCTree) {
/* 75 */     Tokens.Comment comment = getComment(paramJCTree);
/* 76 */     return (comment == null) ? null : comment.getText();
/*    */   }
/*    */   
/*    */   public DCTree.DCDocComment getCommentTree(JCTree paramJCTree) {
/* 80 */     Entry entry = this.table.get(paramJCTree);
/* 81 */     if (entry == null)
/* 82 */       return null; 
/* 83 */     if (entry.tree == null)
/* 84 */       entry.tree = (new DocCommentParser(this.fac, this.diagSource, entry.comment)).parse(); 
/* 85 */     return entry.tree;
/*    */   }
/*    */   
/*    */   public void putComment(JCTree paramJCTree, Tokens.Comment paramComment) {
/* 89 */     this.table.put(paramJCTree, new Entry(paramComment));
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\javac\parser\LazyDocCommentTable.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */