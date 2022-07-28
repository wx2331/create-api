/*     */ package com.sun.xml.internal.xsom.impl.parser;
/*     */ 
/*     */ import com.sun.xml.internal.xsom.XSAttGroupDecl;
/*     */ import com.sun.xml.internal.xsom.XSAttributeDecl;
/*     */ import com.sun.xml.internal.xsom.XSComplexType;
/*     */ import com.sun.xml.internal.xsom.XSDeclaration;
/*     */ import com.sun.xml.internal.xsom.XSElementDecl;
/*     */ import com.sun.xml.internal.xsom.XSIdentityConstraint;
/*     */ import com.sun.xml.internal.xsom.XSModelGroupDecl;
/*     */ import com.sun.xml.internal.xsom.XSSchemaSet;
/*     */ import com.sun.xml.internal.xsom.XSSimpleType;
/*     */ import com.sun.xml.internal.xsom.XSTerm;
/*     */ import com.sun.xml.internal.xsom.XSType;
/*     */ import com.sun.xml.internal.xsom.impl.Ref;
/*     */ import com.sun.xml.internal.xsom.impl.SchemaImpl;
/*     */ import com.sun.xml.internal.xsom.impl.UName;
/*     */ import org.xml.sax.Locator;
/*     */ import org.xml.sax.SAXException;
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
/*     */ public abstract class DelayedRef
/*     */   implements Patch
/*     */ {
/*     */   protected final XSSchemaSet schema;
/*     */   private PatcherManager manager;
/*     */   private UName name;
/*     */   private Locator source;
/*     */   private Object ref;
/*     */   
/*     */   DelayedRef(PatcherManager _manager, Locator _source, SchemaImpl _schema, UName _name) {
/*  86 */     this.ref = null; this.schema = (XSSchemaSet)_schema.getRoot(); this.manager = _manager; this.name = _name; this.source = _source; if (this.name == null) throw new InternalError();  this.manager.addPatcher(this);
/*     */   } public void run() throws SAXException { if (this.ref == null)
/*  88 */       resolve();  this.manager = null; this.name = null; this.source = null; } protected final Object _get() { if (this.ref == null) throw new InternalError("unresolved reference"); 
/*  89 */     return this.ref; }
/*     */    protected abstract Object resolveReference(UName paramUName);
/*     */   protected abstract String getErrorProperty();
/*     */   private void resolve() throws SAXException {
/*  93 */     this.ref = resolveReference(this.name);
/*  94 */     if (this.ref == null) {
/*  95 */       this.manager.reportError(
/*  96 */           Messages.format(getErrorProperty(), new Object[] { this.name.getQualifiedName() }), this.source);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void redefine(XSDeclaration d) {
/* 105 */     if (!d.getTargetNamespace().equals(this.name.getNamespaceURI()) || 
/* 106 */       !d.getName().equals(this.name.getName())) {
/*     */       return;
/*     */     }
/* 109 */     this.ref = d;
/* 110 */     this.manager = null;
/* 111 */     this.name = null;
/* 112 */     this.source = null;
/*     */   }
/*     */   
/*     */   public static class Type
/*     */     extends DelayedRef implements Ref.Type {
/*     */     public Type(PatcherManager manager, Locator loc, SchemaImpl schema, UName name) {
/* 118 */       super(manager, loc, schema, name);
/*     */     }
/*     */     protected Object resolveReference(UName name) {
/* 121 */       Object o = this.schema.getSimpleType(name
/* 122 */           .getNamespaceURI(), name.getName());
/* 123 */       if (o != null) return o;
/*     */       
/* 125 */       return this.schema.getComplexType(name
/* 126 */           .getNamespaceURI(), name
/* 127 */           .getName());
/*     */     }
/*     */     protected String getErrorProperty() {
/* 130 */       return "UndefinedType";
/*     */     }
/*     */     public XSType getType() {
/* 133 */       return (XSType)_get();
/*     */     } }
/*     */   
/*     */   public static class SimpleType extends DelayedRef implements Ref.SimpleType {
/*     */     public SimpleType(PatcherManager manager, Locator loc, SchemaImpl schema, UName name) {
/* 138 */       super(manager, loc, schema, name);
/*     */     } public XSSimpleType getType() {
/* 140 */       return (XSSimpleType)_get();
/*     */     }
/*     */     protected Object resolveReference(UName name) {
/* 143 */       return this.schema.getSimpleType(name
/* 144 */           .getNamespaceURI(), name
/* 145 */           .getName());
/*     */     }
/*     */     
/*     */     protected String getErrorProperty() {
/* 149 */       return "UndefinedSimpleType";
/*     */     }
/*     */   }
/*     */   
/*     */   public static class ComplexType extends DelayedRef implements Ref.ComplexType {
/*     */     public ComplexType(PatcherManager manager, Locator loc, SchemaImpl schema, UName name) {
/* 155 */       super(manager, loc, schema, name);
/*     */     }
/*     */     protected Object resolveReference(UName name) {
/* 158 */       return this.schema.getComplexType(name
/* 159 */           .getNamespaceURI(), name
/* 160 */           .getName());
/*     */     }
/*     */     
/*     */     protected String getErrorProperty() {
/* 164 */       return "UndefinedCompplexType";
/*     */     }
/*     */     public XSComplexType getType() {
/* 167 */       return (XSComplexType)_get();
/*     */     } }
/*     */   
/*     */   public static class Element extends DelayedRef implements Ref.Element {
/*     */     public Element(PatcherManager manager, Locator loc, SchemaImpl schema, UName name) {
/* 172 */       super(manager, loc, schema, name);
/*     */     }
/*     */     protected Object resolveReference(UName name) {
/* 175 */       return this.schema.getElementDecl(name
/* 176 */           .getNamespaceURI(), name
/* 177 */           .getName());
/*     */     }
/*     */     
/*     */     protected String getErrorProperty() {
/* 181 */       return "UndefinedElement";
/*     */     }
/*     */     
/* 184 */     public XSElementDecl get() { return (XSElementDecl)_get(); } public XSTerm getTerm() {
/* 185 */       return (XSTerm)get();
/*     */     }
/*     */   }
/*     */   
/*     */   public static class ModelGroup extends DelayedRef implements Ref.Term { public ModelGroup(PatcherManager manager, Locator loc, SchemaImpl schema, UName name) {
/* 190 */       super(manager, loc, schema, name);
/*     */     }
/*     */     protected Object resolveReference(UName name) {
/* 193 */       return this.schema.getModelGroupDecl(name
/* 194 */           .getNamespaceURI(), name
/* 195 */           .getName());
/*     */     }
/*     */     
/*     */     protected String getErrorProperty() {
/* 199 */       return "UndefinedModelGroup";
/*     */     }
/*     */     
/* 202 */     public XSModelGroupDecl get() { return (XSModelGroupDecl)_get(); } public XSTerm getTerm() {
/* 203 */       return (XSTerm)get();
/*     */     } }
/*     */   
/*     */   public static class AttGroup extends DelayedRef implements Ref.AttGroup {
/*     */     public AttGroup(PatcherManager manager, Locator loc, SchemaImpl schema, UName name) {
/* 208 */       super(manager, loc, schema, name);
/*     */     }
/*     */     protected Object resolveReference(UName name) {
/* 211 */       return this.schema.getAttGroupDecl(name
/* 212 */           .getNamespaceURI(), name
/* 213 */           .getName());
/*     */     }
/*     */     
/*     */     protected String getErrorProperty() {
/* 217 */       return "UndefinedAttributeGroup";
/*     */     }
/*     */     public XSAttGroupDecl get() {
/* 220 */       return (XSAttGroupDecl)_get();
/*     */     }
/*     */   }
/*     */   
/*     */   public static class Attribute extends DelayedRef implements Ref.Attribute { public Attribute(PatcherManager manager, Locator loc, SchemaImpl schema, UName name) {
/* 225 */       super(manager, loc, schema, name);
/*     */     }
/*     */     protected Object resolveReference(UName name) {
/* 228 */       return this.schema.getAttributeDecl(name
/* 229 */           .getNamespaceURI(), name
/* 230 */           .getName());
/*     */     }
/*     */     
/*     */     protected String getErrorProperty() {
/* 234 */       return "UndefinedAttribute";
/*     */     }
/*     */     public XSAttributeDecl getAttribute() {
/* 237 */       return (XSAttributeDecl)_get();
/*     */     } }
/*     */   
/*     */   public static class IdentityConstraint extends DelayedRef implements Ref.IdentityConstraint {
/*     */     public IdentityConstraint(PatcherManager manager, Locator loc, SchemaImpl schema, UName name) {
/* 242 */       super(manager, loc, schema, name);
/*     */     }
/*     */     protected Object resolveReference(UName name) {
/* 245 */       return this.schema.getIdentityConstraint(name
/* 246 */           .getNamespaceURI(), name
/* 247 */           .getName());
/*     */     }
/*     */     
/*     */     protected String getErrorProperty() {
/* 251 */       return "UndefinedIdentityConstraint";
/*     */     }
/*     */     public XSIdentityConstraint get() {
/* 254 */       return (XSIdentityConstraint)_get();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\xml\internal\xsom\impl\parser\DelayedRef.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */