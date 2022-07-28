/*    */ package com.sun.xml.internal.rngom.parse.host;
/*    */ 
/*    */ import com.sun.xml.internal.rngom.ast.builder.Annotations;
/*    */ import com.sun.xml.internal.rngom.ast.builder.BuildException;
/*    */ import com.sun.xml.internal.rngom.ast.builder.CommentList;
/*    */ import com.sun.xml.internal.rngom.ast.om.Location;
/*    */ import com.sun.xml.internal.rngom.ast.om.ParsedElementAnnotation;
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
/*    */ class AnnotationsHost
/*    */   extends Base
/*    */   implements Annotations
/*    */ {
/*    */   final Annotations lhs;
/*    */   final Annotations rhs;
/*    */   
/*    */   AnnotationsHost(Annotations lhs, Annotations rhs) {
/* 64 */     this.lhs = lhs;
/* 65 */     this.rhs = rhs;
/*    */   }
/*    */ 
/*    */   
/*    */   public void addAttribute(String ns, String localName, String prefix, String value, Location _loc) throws BuildException {
/* 70 */     LocationHost loc = cast(_loc);
/* 71 */     this.lhs.addAttribute(ns, localName, prefix, value, loc.lhs);
/* 72 */     this.rhs.addAttribute(ns, localName, prefix, value, loc.rhs);
/*    */   }
/*    */   
/*    */   public void addComment(CommentList _comments) throws BuildException {
/* 76 */     CommentListHost comments = (CommentListHost)_comments;
/* 77 */     this.lhs.addComment((comments == null) ? null : comments.lhs);
/* 78 */     this.rhs.addComment((comments == null) ? null : comments.rhs);
/*    */   }
/*    */   
/*    */   public void addElement(ParsedElementAnnotation _ea) throws BuildException {
/* 82 */     ParsedElementAnnotationHost ea = (ParsedElementAnnotationHost)_ea;
/* 83 */     this.lhs.addElement(ea.lhs);
/* 84 */     this.rhs.addElement(ea.rhs);
/*    */   }
/*    */   
/*    */   public void addLeadingComment(CommentList _comments) throws BuildException {
/* 88 */     CommentListHost comments = (CommentListHost)_comments;
/* 89 */     this.lhs.addLeadingComment(comments.lhs);
/* 90 */     this.rhs.addLeadingComment(comments.rhs);
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\xml\internal\rngom\parse\host\AnnotationsHost.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */