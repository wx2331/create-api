/*    */ package com.sun.xml.internal.xsom.impl;
/*    */ 
/*    */ import com.sun.xml.internal.xsom.XSAttGroupDecl;
/*    */ import com.sun.xml.internal.xsom.XSAttributeUse;
/*    */ import com.sun.xml.internal.xsom.XSWildcard;
/*    */ import com.sun.xml.internal.xsom.impl.parser.DelayedRef;
/*    */ import com.sun.xml.internal.xsom.impl.parser.SchemaDocumentImpl;
/*    */ import com.sun.xml.internal.xsom.visitor.XSFunction;
/*    */ import com.sun.xml.internal.xsom.visitor.XSVisitor;
/*    */ import java.util.Collection;
/*    */ import java.util.Iterator;
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
/*    */ public class AttGroupDeclImpl
/*    */   extends AttributesHolder
/*    */   implements XSAttGroupDecl
/*    */ {
/*    */   private WildcardImpl wildcard;
/*    */   
/*    */   public AttGroupDeclImpl(SchemaDocumentImpl _parent, AnnotationImpl _annon, Locator _loc, ForeignAttributesImpl _fa, String _name, WildcardImpl _wildcard) {
/* 44 */     this(_parent, _annon, _loc, _fa, _name);
/* 45 */     setWildcard(_wildcard);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public AttGroupDeclImpl(SchemaDocumentImpl _parent, AnnotationImpl _annon, Locator _loc, ForeignAttributesImpl _fa, String _name) {
/* 51 */     super(_parent, _annon, _loc, _fa, _name, false);
/*    */   }
/*    */ 
/*    */   
/*    */   public void setWildcard(WildcardImpl wc) {
/* 56 */     this.wildcard = wc; } public XSWildcard getAttributeWildcard() {
/* 57 */     return this.wildcard;
/*    */   }
/*    */   public XSAttributeUse getAttributeUse(String nsURI, String localName) {
/* 60 */     UName name = new UName(nsURI, localName);
/* 61 */     XSAttributeUse o = null;
/*    */     
/* 63 */     Iterator<XSAttGroupDecl> itr = iterateAttGroups();
/* 64 */     while (itr.hasNext() && o == null) {
/* 65 */       o = ((XSAttGroupDecl)itr.next()).getAttributeUse(nsURI, localName);
/*    */     }
/* 67 */     if (o == null) o = this.attributes.get(name);
/*    */     
/* 69 */     return o;
/*    */   }
/*    */   
/*    */   public void redefine(AttGroupDeclImpl ag) {
/* 73 */     for (Iterator<Ref.AttGroup> itr = this.attGroups.iterator(); itr.hasNext(); ) {
/* 74 */       DelayedRef.AttGroup r = (DelayedRef.AttGroup)itr.next();
/* 75 */       r.redefine(ag);
/*    */     } 
/*    */   }
/*    */   
/*    */   public void visit(XSVisitor visitor) {
/* 80 */     visitor.attGroupDecl(this);
/*    */   }
/*    */   public Object apply(XSFunction function) {
/* 83 */     return function.attGroupDecl(this);
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\xml\internal\xsom\impl\AttGroupDeclImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */