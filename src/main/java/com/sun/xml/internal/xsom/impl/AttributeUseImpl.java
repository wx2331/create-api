/*    */ package com.sun.xml.internal.xsom.impl;
/*    */ 
/*    */ import com.sun.xml.internal.xsom.XSAttributeDecl;
/*    */ import com.sun.xml.internal.xsom.XSAttributeUse;
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
/*    */ public class AttributeUseImpl
/*    */   extends ComponentImpl
/*    */   implements XSAttributeUse
/*    */ {
/*    */   private final Ref.Attribute att;
/*    */   private final XmlString defaultValue;
/*    */   private final XmlString fixedValue;
/*    */   private final boolean required;
/*    */   
/*    */   public AttributeUseImpl(SchemaDocumentImpl owner, AnnotationImpl ann, Locator loc, ForeignAttributesImpl fa, Ref.Attribute _decl, XmlString def, XmlString fixed, boolean req) {
/* 41 */     super(owner, ann, loc, fa);
/*    */     
/* 43 */     this.att = _decl;
/* 44 */     this.defaultValue = def;
/* 45 */     this.fixedValue = fixed;
/* 46 */     this.required = req;
/*    */   }
/*    */   
/*    */   public XSAttributeDecl getDecl() {
/* 50 */     return this.att.getAttribute();
/*    */   }
/*    */   
/*    */   public XmlString getDefaultValue() {
/* 54 */     if (this.defaultValue != null) return this.defaultValue; 
/* 55 */     return getDecl().getDefaultValue();
/*    */   }
/*    */ 
/*    */   
/*    */   public XmlString getFixedValue() {
/* 60 */     if (this.fixedValue != null) return this.fixedValue; 
/* 61 */     return getDecl().getFixedValue();
/*    */   }
/*    */   
/*    */   public boolean isRequired() {
/* 65 */     return this.required;
/*    */   }
/*    */   public Object apply(XSFunction f) {
/* 68 */     return f.attributeUse(this);
/*    */   }
/*    */   public void visit(XSVisitor v) {
/* 71 */     v.attributeUse(this);
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\xml\internal\xsom\impl\AttributeUseImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */