/*    */ package com.sun.xml.internal.rngom.parse.host;
/*    */ 
/*    */ import com.sun.xml.internal.rngom.ast.builder.BuildException;
/*    */ import com.sun.xml.internal.rngom.ast.builder.CommentList;
/*    */ import com.sun.xml.internal.rngom.ast.om.Location;
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
/*    */ class CommentListHost
/*    */   extends Base
/*    */   implements CommentList
/*    */ {
/*    */   final CommentList lhs;
/*    */   final CommentList rhs;
/*    */   
/*    */   CommentListHost(CommentList lhs, CommentList rhs) {
/* 63 */     this.lhs = lhs;
/* 64 */     this.rhs = rhs;
/*    */   }
/*    */   
/*    */   public void addComment(String value, Location _loc) throws BuildException {
/* 68 */     LocationHost loc = cast(_loc);
/* 69 */     if (this.lhs != null)
/* 70 */       this.lhs.addComment(value, loc.lhs); 
/* 71 */     if (this.rhs != null)
/* 72 */       this.rhs.addComment(value, loc.rhs); 
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\xml\internal\rngom\parse\host\CommentListHost.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */