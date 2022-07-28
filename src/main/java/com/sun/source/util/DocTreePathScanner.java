/*    */ package com.sun.source.util;
/*    */ 
/*    */ import com.sun.source.doctree.DocTree;
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
/*    */ @Exported
/*    */ public class DocTreePathScanner<R, P>
/*    */   extends DocTreeScanner<R, P>
/*    */ {
/*    */   private DocTreePath path;
/*    */   
/*    */   public R scan(DocTreePath paramDocTreePath, P paramP) {
/* 45 */     this.path = paramDocTreePath;
/*    */     try {
/* 47 */       return (R)paramDocTreePath.getLeaf().accept(this, paramP);
/*    */     } finally {
/* 49 */       this.path = null;
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public R scan(DocTree paramDocTree, P paramP) {
/* 59 */     if (paramDocTree == null) {
/* 60 */       return null;
/*    */     }
/* 62 */     DocTreePath docTreePath = this.path;
/* 63 */     this.path = new DocTreePath(this.path, paramDocTree);
/*    */     try {
/* 65 */       return (R)paramDocTree.accept(this, paramP);
/*    */     } finally {
/* 67 */       this.path = docTreePath;
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public DocTreePath getCurrentPath() {
/* 76 */     return this.path;
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\sourc\\util\DocTreePathScanner.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */