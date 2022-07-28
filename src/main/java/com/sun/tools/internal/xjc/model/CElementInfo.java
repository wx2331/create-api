/*     */ package com.sun.tools.internal.xjc.model;
/*     */
/*     */ import com.sun.codemodel.internal.JExpression;
/*     */ import com.sun.codemodel.internal.JPackage;
/*     */ import com.sun.codemodel.internal.JType;
/*     */ import com.sun.istack.internal.Nullable;
/*     */ import com.sun.tools.internal.xjc.model.nav.NClass;
/*     */ import com.sun.tools.internal.xjc.model.nav.NType;
/*     */ import com.sun.tools.internal.xjc.model.nav.NavigatorImpl;
/*     */ import com.sun.tools.internal.xjc.outline.Aspect;
/*     */ import com.sun.tools.internal.xjc.outline.Outline;
/*     */ import com.sun.tools.internal.xjc.reader.Ring;
/*     */ import com.sun.tools.internal.xjc.reader.xmlschema.BGMBuilder;
/*     */ import com.sun.tools.internal.xjc.reader.xmlschema.bindinfo.BIFactoryMethod;
/*     */ import com.sun.tools.internal.xjc.reader.xmlschema.bindinfo.BIInlineBinaryData;
/*     */ import com.sun.xml.internal.bind.v2.model.core.ClassInfo;
/*     */ import com.sun.xml.internal.bind.v2.model.core.Element;
/*     */ import com.sun.xml.internal.bind.v2.model.core.ElementInfo;
/*     */ import com.sun.xml.internal.bind.v2.model.core.ElementPropertyInfo;
/*     */ import com.sun.xml.internal.bind.v2.model.core.NonElement;
/*     */ import com.sun.xml.internal.xsom.XSComponent;
/*     */ import com.sun.xml.internal.xsom.XSElementDecl;
/*     */ import com.sun.xml.internal.xsom.XmlString;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import javax.activation.MimeType;
/*     */ import javax.xml.bind.JAXBElement;
/*     */ import javax.xml.bind.annotation.XmlElement;
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
/*     */ public final class CElementInfo
/*     */   extends AbstractCElement
/*     */   implements ElementInfo<NType, NClass>, NType, CClassInfoParent
/*     */ {
/*     */   private final QName tagName;
/*     */   private NType type;
/*     */   private String className;
/*     */   public final CClassInfoParent parent;
/*     */   private CElementInfo substitutionHead;
/*     */   private Set<CElementInfo> substitutionMembers;
/*     */   private final Model model;
/*     */   private CElementPropertyInfo property;
/*     */   @Nullable
/*     */   private String squeezedName;
/*     */
/*     */   public CElementInfo(Model model, QName tagName, CClassInfoParent parent, TypeUse contentType, XmlString defaultValue, XSElementDecl source, CCustomizations customizations, Locator location) {
/* 120 */     super(model, (XSComponent)source, location, customizations);
/* 121 */     this.tagName = tagName;
/* 122 */     this.model = model;
/* 123 */     this.parent = parent;
/* 124 */     if (contentType != null) {
/* 125 */       initContentType(contentType, source, defaultValue);
/*     */     }
/* 127 */     model.add(this);
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
/*     */
/*     */
/*     */
/*     */
/*     */   public CElementInfo(Model model, QName tagName, CClassInfoParent parent, String className, CCustomizations customizations, Locator location) {
/* 143 */     this(model, tagName, parent, null, null, null, customizations, location);
/* 144 */     this.className = className;
/*     */   }
/*     */
/*     */   public void initContentType(TypeUse contentType, @Nullable XSElementDecl source, XmlString defaultValue) {
/* 148 */     assert this.property == null;
/*     */
/* 150 */     this
/*     */
/*     */
/*     */
/* 154 */       .property = new CElementPropertyInfo("Value", contentType.isCollection() ? CElementPropertyInfo.CollectionMode.REPEATED_VALUE : CElementPropertyInfo.CollectionMode.NOT_REPEATED, contentType.idUse(), contentType.getExpectedMimeType(), (XSComponent)source, null, getLocator(), true);
/* 155 */     this.property.setAdapter(contentType.getAdapterUse());
/* 156 */     BIInlineBinaryData.handle((XSComponent)source, this.property);
/* 157 */     this.property.getTypes().add(new CTypeRef(contentType.getInfo(), this.tagName, CTypeRef.getSimpleTypeName(source), true, defaultValue));
/* 158 */     this.type = NavigatorImpl.createParameterizedType(NavigatorImpl.theInstance
/* 159 */         .ref(JAXBElement.class), new NType[] {
/* 160 */           getContentInMemoryType()
/*     */         });
/* 162 */     BIFactoryMethod factoryMethod = (BIFactoryMethod)((BGMBuilder)Ring.get(BGMBuilder.class)).getBindInfo((XSComponent)source).get(BIFactoryMethod.class);
/* 163 */     if (factoryMethod != null) {
/* 164 */       factoryMethod.markAsAcknowledged();
/* 165 */       this.squeezedName = factoryMethod.name;
/*     */     }
/*     */   }
/*     */
/*     */
/*     */   public final String getDefaultValue() {
/* 171 */     return ((CTypeRef)getProperty().getTypes().get(0)).getDefaultValue();
/*     */   }
/*     */
/*     */   public final JPackage _package() {
/* 175 */     return this.parent.getOwnerPackage();
/*     */   }
/*     */
/*     */   public CNonElement getContentType() {
/* 179 */     return getProperty().ref().get(0);
/*     */   }
/*     */
/*     */   public NType getContentInMemoryType() {
/* 183 */     if (getProperty().getAdapter() == null) {
/* 184 */       NType itemType = getContentType().getType();
/* 185 */       if (!this.property.isCollection()) {
/* 186 */         return itemType;
/*     */       }
/* 188 */       return NavigatorImpl.createParameterizedType(List.class, new NType[] { itemType });
/*     */     }
/* 190 */     return (getProperty().getAdapter()).customType;
/*     */   }
/*     */
/*     */
/*     */   public CElementPropertyInfo getProperty() {
/* 195 */     return this.property;
/*     */   }
/*     */
/*     */   public CClassInfo getScope() {
/* 199 */     if (this.parent instanceof CClassInfo)
/* 200 */       return (CClassInfo)this.parent;
/* 201 */     return null;
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */   public NType getType() {
/* 208 */     return this;
/*     */   }
/*     */
/*     */   public QName getElementName() {
/* 212 */     return this.tagName;
/*     */   }
/*     */
/*     */   public JType toType(Outline o, Aspect aspect) {
/* 216 */     if (this.className == null) {
/* 217 */       return this.type.toType(o, aspect);
/*     */     }
/* 219 */     return (JType)(o.getElement(this)).implClass;
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   @XmlElement
/*     */   public String getSqueezedName() {
/* 229 */     if (this.squeezedName != null) return this.squeezedName;
/*     */
/* 231 */     StringBuilder b = new StringBuilder();
/* 232 */     CClassInfo s = getScope();
/* 233 */     if (s != null)
/* 234 */       b.append(s.getSqueezedName());
/* 235 */     if (this.className != null) {
/* 236 */       b.append(this.className);
/*     */     } else {
/* 238 */       b.append(this.model.getNameConverter().toClassName(this.tagName.getLocalPart()));
/* 239 */     }  return b.toString();
/*     */   }
/*     */
/*     */   public CElementInfo getSubstitutionHead() {
/* 243 */     return this.substitutionHead;
/*     */   }
/*     */
/*     */   public Collection<CElementInfo> getSubstitutionMembers() {
/* 247 */     if (this.substitutionMembers == null) {
/* 248 */       return Collections.emptyList();
/*     */     }
/* 250 */     return this.substitutionMembers;
/*     */   }
/*     */
/*     */
/*     */   public void setSubstitutionHead(CElementInfo substitutionHead) {
/* 255 */     assert this.substitutionHead == null;
/* 256 */     assert substitutionHead != null;
/* 257 */     this.substitutionHead = substitutionHead;
/*     */
/* 259 */     if (substitutionHead.substitutionMembers == null)
/* 260 */       substitutionHead.substitutionMembers = new HashSet<>();
/* 261 */     substitutionHead.substitutionMembers.add(this);
/*     */   }
/*     */
/*     */   public boolean isBoxedType() {
/* 265 */     return false;
/*     */   }
/*     */
/*     */   public String fullName() {
/* 269 */     if (this.className == null) {
/* 270 */       return this.type.fullName();
/*     */     }
/* 272 */     String r = this.parent.fullName();
/* 273 */     if (r.length() == 0) return this.className;
/* 274 */     return r + '.' + this.className;
/*     */   }
/*     */
/*     */
/*     */   public <T> T accept(Visitor<T> visitor) {
/* 279 */     return visitor.onElement(this);
/*     */   }
/*     */
/*     */   public JPackage getOwnerPackage() {
/* 283 */     return this.parent.getOwnerPackage();
/*     */   }
/*     */
/*     */   public String shortName() {
/* 287 */     return this.className;
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   public boolean hasClass() {
/* 295 */     return (this.className != null);
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\xjc\model\CElementInfo.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
