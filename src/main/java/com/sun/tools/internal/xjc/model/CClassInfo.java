/*     */ package com.sun.tools.internal.xjc.model;
/*     */
/*     */ import com.sun.codemodel.internal.JClass;
/*     */ import com.sun.codemodel.internal.JCodeModel;
/*     */ import com.sun.codemodel.internal.JExpression;
/*     */ import com.sun.codemodel.internal.JPackage;
/*     */ import com.sun.codemodel.internal.JType;
/*     */ import com.sun.istack.internal.Nullable;
/*     */ import com.sun.tools.internal.xjc.Language;
/*     */ import com.sun.tools.internal.xjc.model.nav.NClass;
/*     */ import com.sun.tools.internal.xjc.model.nav.NType;
/*     */ import com.sun.tools.internal.xjc.outline.Aspect;
/*     */ import com.sun.tools.internal.xjc.outline.Outline;
/*     */ import com.sun.tools.internal.xjc.reader.Ring;
/*     */ import com.sun.tools.internal.xjc.reader.xmlschema.BGMBuilder;
/*     */ import com.sun.tools.internal.xjc.reader.xmlschema.bindinfo.BIFactoryMethod;
/*     */ import com.sun.xml.internal.bind.v2.model.core.ClassInfo;
/*     */ import com.sun.xml.internal.bind.v2.model.core.Element;
/*     */ import com.sun.xml.internal.bind.v2.model.core.PropertyInfo;
/*     */ import com.sun.xml.internal.xsom.XSComponent;
/*     */ import com.sun.xml.internal.xsom.XmlString;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import javax.activation.MimeType;
/*     */ import javax.xml.bind.annotation.XmlElement;
/*     */ import javax.xml.bind.annotation.XmlID;
/*     */ import javax.xml.bind.annotation.XmlIDREF;
/*     */ import javax.xml.namespace.QName;
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
/*     */ public final class CClassInfo
/*     */   extends AbstractCElement
/*     */   implements ClassInfo<NType, NClass>, CClassInfoParent, CClass, NClass
/*     */ {
/*     */   @XmlIDREF
/*     */   private CClass baseClass;
/*     */   private CClassInfo firstSubclass;
/*  84 */   private CClassInfo nextSibling = null;
/*     */
/*     */
/*     */
/*     */   private final QName typeName;
/*     */
/*     */
/*     */
/*     */   @Nullable
/*     */   private String squeezedName;
/*     */
/*     */
/*     */   @Nullable
/*     */   private final QName elementName;
/*     */
/*     */
/*     */   private boolean isOrdered = true;
/*     */
/*     */
/* 103 */   private final List<CPropertyInfo> properties = new ArrayList<>();
/*     */
/*     */
/*     */
/*     */
/*     */   public String javadoc;
/*     */
/*     */
/*     */
/*     */
/*     */   @XmlIDREF
/*     */   private final CClassInfoParent parent;
/*     */
/*     */
/*     */
/*     */
/*     */   public final String shortName;
/*     */
/*     */
/*     */
/*     */   @Nullable
/*     */   private String implClass;
/*     */
/*     */
/*     */
/*     */   public final Model model;
/*     */
/*     */
/*     */
/*     */   private boolean hasAttributeWildcard;
/*     */
/*     */
/*     */
/*     */
/*     */   public CClassInfo(Model model, JPackage pkg, String shortName, Locator location, QName typeName, QName elementName, XSComponent source, CCustomizations customizations) {
/* 138 */     this(model, model.getPackage(pkg), shortName, location, typeName, elementName, source, customizations);
/*     */   }
/*     */
/*     */   public CClassInfo(Model model, CClassInfoParent p, String shortName, Locator location, QName typeName, QName elementName, XSComponent source, CCustomizations customizations) {
/* 142 */     super(model, source, location, customizations);
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
/* 449 */     this._implements = null;
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/* 459 */     this.constructors = new ArrayList<>(1); this.model = model; this.parent = p; this.shortName = model.allocator.assignClassName(this.parent, shortName); this.typeName = typeName; this.elementName = elementName; Language schemaLanguage = model.options.getSchemaLanguage(); if (schemaLanguage != null && (schemaLanguage.equals(Language.XMLSCHEMA) || schemaLanguage.equals(Language.WSDL))) { BIFactoryMethod factoryMethod = (BIFactoryMethod)((BGMBuilder)Ring.get(BGMBuilder.class)).getBindInfo(source).get(BIFactoryMethod.class); if (factoryMethod != null) { factoryMethod.markAsAcknowledged(); this.squeezedName = factoryMethod.name; }  }  model.add(this); } public CClassInfo(Model model, JCodeModel cm, String fullName, Locator location, QName typeName, QName elementName, XSComponent source, CCustomizations customizations) { super(model, source, location, customizations); this._implements = null; this.constructors = new ArrayList<>(1); this.model = model; int idx = fullName.indexOf('.'); if (idx < 0) { this.parent = model.getPackage(cm.rootPackage()); this.shortName = model.allocator.assignClassName(this.parent, fullName); } else { this.parent = model.getPackage(cm._package(fullName.substring(0, idx))); this.shortName = model.allocator.assignClassName(this.parent, fullName.substring(idx + 1)); }  this.typeName = typeName; this.elementName = elementName; model.add(this); } public boolean hasAttributeWildcard() { return this.hasAttributeWildcard; } public void hasAttributeWildcard(boolean hasAttributeWildcard) { this.hasAttributeWildcard = hasAttributeWildcard; } public boolean hasSubClasses() { return (this.firstSubclass != null); } public boolean declaresAttributeWildcard() { return (this.hasAttributeWildcard && !inheritsAttributeWildcard()); } public boolean inheritsAttributeWildcard() { if (getRefBaseClass() != null) { CClassRef cref = (CClassRef)this.baseClass; if (cref.getSchemaComponent().getForeignAttributes().size() > 0) return true;  } else { for (CClassInfo c = getBaseClass(); c != null; c = c.getBaseClass()) { if (c.hasAttributeWildcard) return true;  }  }  return false; } public NClass getClazz() { return this; } public CClassInfo getScope() { return null; } @XmlID public String getName() { return fullName(); } @XmlElement public String getSqueezedName() { if (this.squeezedName != null) return this.squeezedName;  return calcSqueezedName.onBean(this); } private static final Visitor<String> calcSqueezedName = new Visitor<String>() { public String onBean(CClassInfo bean) { return (String)bean.parent.accept(this) + bean.shortName; } public String onElement(CElementInfo element) { return (String)element.parent.<String>accept(this) + element.shortName(); } public String onPackage(JPackage pkg) { return ""; } }
/*     */   ; private Set<JClass> _implements; private final List<Constructor> constructors; public List<CPropertyInfo> getProperties() { return this.properties; } public boolean hasValueProperty() { throw new UnsupportedOperationException(); } public CPropertyInfo getProperty(String name) { for (CPropertyInfo p : this.properties) { if (p.getName(false).equals(name)) return p;  }  return null; } public boolean hasProperties() { return !getProperties().isEmpty(); }
/*     */   public boolean isElement() { return (this.elementName != null); }
/*     */   @Deprecated public CNonElement getInfo() { return this; }
/* 463 */   public void addConstructor(String... fieldNames) { this.constructors.add(new Constructor(fieldNames)); }
/*     */   public Element<NType, NClass> asElement() { if (isElement()) return this;  return null; }
/*     */   public boolean isOrdered() { return this.isOrdered; }
/*     */   public boolean isFinal() { return false; }
/*     */   public void setOrdered(boolean value) { this.isOrdered = value; }
/* 468 */   public QName getElementName() { return this.elementName; } public QName getTypeName() { return this.typeName; } public boolean isSimpleType() { throw new UnsupportedOperationException(); } public String fullName() { String r = this.parent.fullName(); if (r.length() == 0) return this.shortName;  return r + '.' + this.shortName; } public CClassInfoParent parent() { return this.parent; } public Collection<? extends Constructor> getConstructors() { return this.constructors; }
/*     */   public void setUserSpecifiedImplClass(String implClass) { assert this.implClass == null; assert implClass != null; this.implClass = implClass; }
/*     */   public String getUserSpecifiedImplClass() { return this.implClass; } public void addProperty(CPropertyInfo prop) { if (prop.ref().isEmpty()) return;  prop.setParent(this); this.properties.add(prop); } public void setBaseClass(CClass base) { assert this.baseClass == null; assert base != null; this.baseClass = base; assert this.nextSibling == null; if (base instanceof CClassInfo) { CClassInfo realBase = (CClassInfo)base; this.nextSibling = realBase.firstSubclass; realBase.firstSubclass = this; }  } public CClassInfo getBaseClass() { if (this.baseClass instanceof CClassInfo) return (CClassInfo)this.baseClass;  return null; } public CClassRef getRefBaseClass() { if (this.baseClass instanceof CClassRef) return (CClassRef)this.baseClass;  return null; } public Iterator<CClassInfo> listSubclasses() { return new Iterator<CClassInfo>() {
/*     */         CClassInfo cur = CClassInfo.this.firstSubclass; public boolean hasNext() { return (this.cur != null); } public CClassInfo next() { CClassInfo r = this.cur; this.cur = this.cur.nextSibling; return r; } public void remove() { throw new UnsupportedOperationException(); }
/* 472 */       }; } public CClassInfo getSubstitutionHead() { CClassInfo c = getBaseClass(); while (c != null && !c.isElement()) c = c.getBaseClass();  return c; } public void _implements(JClass c) { if (this._implements == null) this._implements = new HashSet<>();  this._implements.add(c); } public final <T> T accept(Visitor<T> visitor) { return visitor.onBean(this); }
/*     */
/*     */
/*     */   public JPackage getOwnerPackage() {
/* 476 */     return this.parent.getOwnerPackage();
/*     */   }
/*     */
/*     */   public final NClass getType() {
/* 480 */     return this;
/*     */   }
/*     */
/*     */   public final JClass toType(Outline o, Aspect aspect) {
/* 484 */     switch (aspect) {
/*     */       case IMPLEMENTATION:
/* 486 */         return (o.getClazz(this)).implRef;
/*     */       case EXPOSED:
/* 488 */         return (JClass)(o.getClazz(this)).ref;
/*     */     }
/* 490 */     throw new IllegalStateException();
/*     */   }
/*     */
/*     */
/*     */   public boolean isBoxedType() {
/* 495 */     return false;
/*     */   }
/*     */
/*     */   public String toString() {
/* 499 */     return fullName();
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\xjc\model\CClassInfo.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
