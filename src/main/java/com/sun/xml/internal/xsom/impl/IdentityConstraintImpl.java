/*     */ package com.sun.xml.internal.xsom.impl;
/*     */ 
/*     */ import com.sun.xml.internal.xsom.XSElementDecl;
/*     */ import com.sun.xml.internal.xsom.XSIdentityConstraint;
/*     */ import com.sun.xml.internal.xsom.XSXPath;
/*     */ import com.sun.xml.internal.xsom.impl.parser.SchemaDocumentImpl;
/*     */ import com.sun.xml.internal.xsom.visitor.XSFunction;
/*     */ import com.sun.xml.internal.xsom.visitor.XSVisitor;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import org.xml.sax.Locator;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class IdentityConstraintImpl
/*     */   extends ComponentImpl
/*     */   implements XSIdentityConstraint, Ref.IdentityConstraint
/*     */ {
/*     */   private XSElementDecl parent;
/*     */   private final short category;
/*     */   private final String name;
/*     */   private final XSXPath selector;
/*     */   private final List<XSXPath> fields;
/*     */   private final Ref.IdentityConstraint refer;
/*     */   
/*     */   public IdentityConstraintImpl(SchemaDocumentImpl _owner, AnnotationImpl _annon, Locator _loc, ForeignAttributesImpl fa, short category, String name, XPathImpl selector, List<XPathImpl> fields, Ref.IdentityConstraint refer) {
/*  57 */     super(_owner, _annon, _loc, fa);
/*  58 */     this.category = category;
/*  59 */     this.name = name;
/*  60 */     this.selector = selector;
/*  61 */     selector.setParent(this);
/*  62 */     this.fields = Collections.unmodifiableList((List)fields);
/*  63 */     for (XPathImpl xp : fields)
/*  64 */       xp.setParent(this); 
/*  65 */     this.refer = refer;
/*     */   }
/*     */ 
/*     */   
/*     */   public void visit(XSVisitor visitor) {
/*  70 */     visitor.identityConstraint(this);
/*     */   }
/*     */   
/*     */   public <T> T apply(XSFunction<T> function) {
/*  74 */     return (T)function.identityConstraint(this);
/*     */   }
/*     */   
/*     */   public void setParent(ElementDecl parent) {
/*  78 */     this.parent = parent;
/*  79 */     parent.getOwnerSchema().addIdentityConstraint(this);
/*     */   }
/*     */   
/*     */   public XSElementDecl getParent() {
/*  83 */     return this.parent;
/*     */   }
/*     */   
/*     */   public String getName() {
/*  87 */     return this.name;
/*     */   }
/*     */   
/*     */   public String getTargetNamespace() {
/*  91 */     return getParent().getTargetNamespace();
/*     */   }
/*     */   
/*     */   public short getCategory() {
/*  95 */     return this.category;
/*     */   }
/*     */   
/*     */   public XSXPath getSelector() {
/*  99 */     return this.selector;
/*     */   }
/*     */   
/*     */   public List<XSXPath> getFields() {
/* 103 */     return this.fields;
/*     */   }
/*     */   
/*     */   public XSIdentityConstraint getReferencedKey() {
/* 107 */     if (this.category == 1) {
/* 108 */       return this.refer.get();
/*     */     }
/* 110 */     throw new IllegalStateException("not a keyref");
/*     */   }
/*     */   
/*     */   public XSIdentityConstraint get() {
/* 114 */     return this;
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\xml\internal\xsom\impl\IdentityConstraintImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */