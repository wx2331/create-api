/*    */ package com.sun.xml.internal.xsom.impl;
/*    */ 
/*    */ import com.sun.xml.internal.xsom.XSIdentityConstraint;
/*    */ import com.sun.xml.internal.xsom.XSXPath;
/*    */ import com.sun.xml.internal.xsom.XmlString;
/*    */ import com.sun.xml.internal.xsom.impl.parser.SchemaDocumentImpl;
/*    */ import com.sun.xml.internal.xsom.visitor.XSFunction;
/*    */ import com.sun.xml.internal.xsom.visitor.XSVisitor;
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
/*    */ public class XPathImpl
/*    */   extends ComponentImpl
/*    */   implements XSXPath
/*    */ {
/*    */   private XSIdentityConstraint parent;
/*    */   private final XmlString xpath;
/*    */   
/*    */   public XPathImpl(SchemaDocumentImpl _owner, AnnotationImpl _annon, Locator _loc, ForeignAttributesImpl fa, XmlString xpath) {
/* 44 */     super(_owner, _annon, _loc, fa);
/* 45 */     this.xpath = xpath;
/*    */   }
/*    */   
/*    */   public void setParent(XSIdentityConstraint parent) {
/* 49 */     this.parent = parent;
/*    */   }
/*    */   
/*    */   public XSIdentityConstraint getParent() {
/* 53 */     return this.parent;
/*    */   }
/*    */   
/*    */   public XmlString getXPath() {
/* 57 */     return this.xpath;
/*    */   }
/*    */   
/*    */   public void visit(XSVisitor visitor) {
/* 61 */     visitor.xpath(this);
/*    */   }
/*    */   
/*    */   public <T> T apply(XSFunction<T> function) {
/* 65 */     return (T)function.xpath(this);
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\xml\internal\xsom\impl\XPathImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */