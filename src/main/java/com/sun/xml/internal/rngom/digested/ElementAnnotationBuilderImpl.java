/*    */ package com.sun.xml.internal.rngom.digested;
/*    */ 
/*    */ import com.sun.xml.internal.rngom.ast.builder.BuildException;
/*    */ import com.sun.xml.internal.rngom.ast.builder.CommentList;
/*    */ import com.sun.xml.internal.rngom.ast.builder.ElementAnnotationBuilder;
/*    */ import com.sun.xml.internal.rngom.ast.om.Location;
/*    */ import com.sun.xml.internal.rngom.ast.om.ParsedElementAnnotation;
/*    */ import org.w3c.dom.Element;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ class ElementAnnotationBuilderImpl
/*    */   implements ElementAnnotationBuilder
/*    */ {
/*    */   private final Element e;
/*    */   
/*    */   public ElementAnnotationBuilderImpl(Element e) {
/* 63 */     this.e = e;
/*    */   }
/*    */ 
/*    */   
/*    */   public void addText(String value, Location loc, CommentList comments) throws BuildException {
/* 68 */     this.e.appendChild(this.e.getOwnerDocument().createTextNode(value));
/*    */   }
/*    */   
/*    */   public ParsedElementAnnotation makeElementAnnotation() throws BuildException {
/* 72 */     return new ElementWrapper(this.e);
/*    */   }
/*    */   
/*    */   public void addAttribute(String ns, String localName, String prefix, String value, Location loc) throws BuildException {
/* 76 */     this.e.setAttributeNS(ns, localName, value);
/*    */   }
/*    */   
/*    */   public void addElement(ParsedElementAnnotation ea) throws BuildException {
/* 80 */     this.e.appendChild(((ElementWrapper)ea).element);
/*    */   }
/*    */   
/*    */   public void addComment(CommentList comments) throws BuildException {}
/*    */   
/*    */   public void addLeadingComment(CommentList comments) throws BuildException {}
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\xml\internal\rngom\digested\ElementAnnotationBuilderImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */