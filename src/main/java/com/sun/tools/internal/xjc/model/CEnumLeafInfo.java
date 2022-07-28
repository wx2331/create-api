/*     */ package com.sun.tools.internal.xjc.model;
/*     */ 
/*     */ import com.sun.codemodel.internal.JClass;
/*     */ import com.sun.codemodel.internal.JExpression;
/*     */ import com.sun.codemodel.internal.JType;
/*     */ import com.sun.tools.internal.xjc.model.nav.NClass;
/*     */ import com.sun.tools.internal.xjc.model.nav.NType;
/*     */ import com.sun.tools.internal.xjc.outline.Aspect;
/*     */ import com.sun.tools.internal.xjc.outline.Outline;
/*     */ import com.sun.xml.internal.bind.v2.model.annotation.Locatable;
/*     */ import com.sun.xml.internal.bind.v2.model.core.Element;
/*     */ import com.sun.xml.internal.bind.v2.model.core.EnumLeafInfo;
/*     */ import com.sun.xml.internal.bind.v2.model.core.ID;
/*     */ import com.sun.xml.internal.bind.v2.model.core.NonElement;
/*     */ import com.sun.xml.internal.bind.v2.runtime.Location;
/*     */ import com.sun.xml.internal.xsom.XSComponent;
/*     */ import com.sun.xml.internal.xsom.XmlString;
/*     */ import java.util.Collection;
/*     */ import javax.activation.MimeType;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class CEnumLeafInfo
/*     */   implements EnumLeafInfo<NType, NClass>, NClass, CNonElement
/*     */ {
/*     */   public final Model model;
/*     */   public final CClassInfoParent parent;
/*     */   public final String shortName;
/*     */   private final QName typeName;
/*     */   private final XSComponent source;
/*     */   public final CNonElement base;
/*     */   public final Collection<CEnumConstant> members;
/*     */   private final CCustomizations customizations;
/*     */   private final Locator sourceLocator;
/*     */   public String javadoc;
/*     */   
/*     */   public CEnumLeafInfo(Model model, QName typeName, CClassInfoParent container, String shortName, CNonElement base, Collection<CEnumConstant> _members, XSComponent source, CCustomizations customizations, Locator _sourceLocator) {
/* 119 */     this.model = model;
/* 120 */     this.parent = container;
/* 121 */     this.shortName = model.allocator.assignClassName(this.parent, shortName);
/* 122 */     this.base = base;
/* 123 */     this.members = _members;
/* 124 */     this.source = source;
/* 125 */     if (customizations == null)
/* 126 */       customizations = CCustomizations.EMPTY; 
/* 127 */     this.customizations = customizations;
/* 128 */     this.sourceLocator = _sourceLocator;
/* 129 */     this.typeName = typeName;
/*     */     
/* 131 */     for (CEnumConstant mem : this.members) {
/* 132 */       mem.setParent(this);
/*     */     }
/* 134 */     model.add(this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Locator getLocator() {
/* 145 */     return this.sourceLocator;
/*     */   }
/*     */   
/*     */   public QName getTypeName() {
/* 149 */     return this.typeName;
/*     */   }
/*     */   
/*     */   public NType getType() {
/* 153 */     return (NType)this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean canBeReferencedByIDREF() {
/* 161 */     return false;
/*     */   }
/*     */   
/*     */   public boolean isElement() {
/* 165 */     return false;
/*     */   }
/*     */   
/*     */   public QName getElementName() {
/* 169 */     return null;
/*     */   }
/*     */   
/*     */   public Element<NType, NClass> asElement() {
/* 173 */     return null;
/*     */   }
/*     */   
/*     */   public NClass getClazz() {
/* 177 */     return this;
/*     */   }
/*     */   
/*     */   public XSComponent getSchemaComponent() {
/* 181 */     return this.source;
/*     */   }
/*     */   
/*     */   public JClass toType(Outline o, Aspect aspect) {
/* 185 */     return (JClass)(o.getEnum(this)).clazz;
/*     */   }
/*     */   
/*     */   public boolean isAbstract() {
/* 189 */     return false;
/*     */   }
/*     */   
/*     */   public boolean isBoxedType() {
/* 193 */     return false;
/*     */   }
/*     */   
/*     */   public String fullName() {
/* 197 */     return this.parent.fullName() + '.' + this.shortName;
/*     */   }
/*     */   
/*     */   public boolean isPrimitive() {
/* 201 */     return false;
/*     */   }
/*     */   
/*     */   public boolean isSimpleType() {
/* 205 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean needsValueField() {
/* 217 */     for (CEnumConstant cec : this.members) {
/* 218 */       if (!cec.getName().equals(cec.getLexicalValue()))
/* 219 */         return true; 
/*     */     } 
/* 221 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public JExpression createConstant(Outline outline, XmlString literal) {
/* 227 */     JClass type = toType(outline, Aspect.EXPOSED);
/* 228 */     for (CEnumConstant mem : this.members) {
/* 229 */       if (mem.getLexicalValue().equals(literal.value))
/* 230 */         return (JExpression)type.staticRef(mem.getName()); 
/*     */     } 
/* 232 */     return null;
/*     */   }
/*     */   
/*     */   @Deprecated
/*     */   public boolean isCollection() {
/* 237 */     return false;
/*     */   }
/*     */   
/*     */   @Deprecated
/*     */   public CAdapter getAdapterUse() {
/* 242 */     return null;
/*     */   }
/*     */   
/*     */   @Deprecated
/*     */   public CNonElement getInfo() {
/* 247 */     return this;
/*     */   }
/*     */   
/*     */   public ID idUse() {
/* 251 */     return ID.NONE;
/*     */   }
/*     */   
/*     */   public MimeType getExpectedMimeType() {
/* 255 */     return null;
/*     */   }
/*     */   
/*     */   public Collection<CEnumConstant> getConstants() {
/* 259 */     return this.members;
/*     */   }
/*     */   
/*     */   public NonElement<NType, NClass> getBaseType() {
/* 263 */     return this.base;
/*     */   }
/*     */   
/*     */   public CCustomizations getCustomizations() {
/* 267 */     return this.customizations;
/*     */   }
/*     */   
/*     */   public Locatable getUpstream() {
/* 271 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */   public Location getLocation() {
/* 275 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\xjc\model\CEnumLeafInfo.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */