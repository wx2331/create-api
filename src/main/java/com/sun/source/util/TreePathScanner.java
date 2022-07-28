/*    */ package com.sun.source.util;
/*    */ 
/*    */ import com.sun.source.tree.Tree;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @Exported
/*    */ public class TreePathScanner<R, P>
/*    */   extends TreeScanner<R, P>
/*    */ {
/*    */   private TreePath path;
/*    */   
/*    */   public R scan(TreePath paramTreePath, P paramP) {
/* 48 */     this.path = paramTreePath;
/*    */     try {
/* 50 */       return (R)paramTreePath.getLeaf().accept(this, paramP);
/*    */     } finally {
/* 52 */       this.path = null;
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public R scan(Tree paramTree, P paramP) {
/* 62 */     if (paramTree == null) {
/* 63 */       return null;
/*    */     }
/* 65 */     TreePath treePath = this.path;
/* 66 */     this.path = new TreePath(this.path, paramTree);
/*    */     try {
/* 68 */       return (R)paramTree.accept(this, paramP);
/*    */     } finally {
/* 70 */       this.path = treePath;
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public TreePath getCurrentPath() {
/* 79 */     return this.path;
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\sourc\\util\TreePathScanner.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */