/*    */ package com.sun.xml.internal.rngom.digested;
/*    */ 
/*    */ import com.sun.xml.internal.rngom.ast.builder.Annotations;
/*    */ import com.sun.xml.internal.rngom.ast.builder.BuildException;
/*    */ import com.sun.xml.internal.rngom.ast.builder.CommentList;
/*    */ import com.sun.xml.internal.rngom.ast.om.Location;
/*    */ import com.sun.xml.internal.rngom.ast.om.ParsedElementAnnotation;
/*    */ import com.sun.xml.internal.rngom.ast.util.LocatorImpl;
/*    */ import javax.xml.namespace.QName;
/*    */ import org.xml.sax.Locator;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ class Annotation
/*    */   implements Annotations<ElementWrapper, LocatorImpl, CommentListImpl>
/*    */ {
/* 59 */   private final DAnnotation a = new DAnnotation();
/*    */   
/*    */   public void addAttribute(String ns, String localName, String prefix, String value, LocatorImpl loc) throws BuildException {
/* 62 */     this.a.attributes.put(new QName(ns, localName, prefix), new DAnnotation.Attribute(ns, localName, prefix, value, (Locator)loc));
/*    */   }
/*    */ 
/*    */   
/*    */   public void addElement(ElementWrapper ea) throws BuildException {
/* 67 */     this.a.contents.add(ea.element);
/*    */   }
/*    */ 
/*    */   
/*    */   public void addComment(CommentListImpl comments) throws BuildException {}
/*    */ 
/*    */   
/*    */   public void addLeadingComment(CommentListImpl comments) throws BuildException {}
/*    */   
/*    */   DAnnotation getResult() {
/* 77 */     return this.a;
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\xml\internal\rngom\digested\Annotation.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */