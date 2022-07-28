/*     */ package com.sun.tools.internal.xjc.reader.xmlschema.bindinfo;
/*     */ 
/*     */ import com.sun.codemodel.internal.JJavaName;
/*     */ import com.sun.codemodel.internal.JType;
/*     */ import com.sun.tools.internal.xjc.ErrorReceiver;
/*     */ import com.sun.tools.internal.xjc.generator.bean.field.FieldRenderer;
/*     */ import com.sun.tools.internal.xjc.generator.bean.field.FieldRendererFactory;
/*     */ import com.sun.tools.internal.xjc.generator.bean.field.IsSetFieldRenderer;
/*     */ import com.sun.tools.internal.xjc.model.CAttributePropertyInfo;
/*     */ import com.sun.tools.internal.xjc.model.CCustomizations;
/*     */ import com.sun.tools.internal.xjc.model.CElementPropertyInfo;
/*     */ import com.sun.tools.internal.xjc.model.CPropertyInfo;
/*     */ import com.sun.tools.internal.xjc.model.CReferencePropertyInfo;
/*     */ import com.sun.tools.internal.xjc.model.CValuePropertyInfo;
/*     */ import com.sun.tools.internal.xjc.model.TypeUse;
/*     */ import com.sun.tools.internal.xjc.reader.RawTypeSet;
/*     */ import com.sun.tools.internal.xjc.reader.Ring;
/*     */ import com.sun.tools.internal.xjc.reader.TypeUtil;
/*     */ import com.sun.tools.internal.xjc.reader.xmlschema.BGMBuilder;
/*     */ import com.sun.xml.internal.bind.api.impl.NameConverter;
/*     */ import com.sun.xml.internal.xsom.XSAnnotation;
/*     */ import com.sun.xml.internal.xsom.XSAttGroupDecl;
/*     */ import com.sun.xml.internal.xsom.XSAttributeDecl;
/*     */ import com.sun.xml.internal.xsom.XSAttributeUse;
/*     */ import com.sun.xml.internal.xsom.XSComplexType;
/*     */ import com.sun.xml.internal.xsom.XSComponent;
/*     */ import com.sun.xml.internal.xsom.XSContentType;
/*     */ import com.sun.xml.internal.xsom.XSDeclaration;
/*     */ import com.sun.xml.internal.xsom.XSElementDecl;
/*     */ import com.sun.xml.internal.xsom.XSFacet;
/*     */ import com.sun.xml.internal.xsom.XSIdentityConstraint;
/*     */ import com.sun.xml.internal.xsom.XSModelGroup;
/*     */ import com.sun.xml.internal.xsom.XSModelGroupDecl;
/*     */ import com.sun.xml.internal.xsom.XSNotation;
/*     */ import com.sun.xml.internal.xsom.XSParticle;
/*     */ import com.sun.xml.internal.xsom.XSSchema;
/*     */ import com.sun.xml.internal.xsom.XSSimpleType;
/*     */ import com.sun.xml.internal.xsom.XSWildcard;
/*     */ import com.sun.xml.internal.xsom.XSXPath;
/*     */ import com.sun.xml.internal.xsom.util.XSFinder;
/*     */ import com.sun.xml.internal.xsom.visitor.XSFunction;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import javax.xml.bind.annotation.XmlAttribute;
/*     */ import javax.xml.bind.annotation.XmlElement;
/*     */ import javax.xml.bind.annotation.XmlElementRef;
/*     */ import javax.xml.bind.annotation.XmlRootElement;
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
/*     */ @XmlRootElement(name = "property")
/*     */ public final class BIProperty
/*     */   extends AbstractDeclarationImpl
/*     */ {
/*     */   @XmlAttribute
/* 106 */   private String name = null;
/*     */ 
/*     */   
/*     */   @XmlElement
/* 110 */   private String javadoc = null;
/*     */ 
/*     */   
/*     */   @XmlElement
/* 114 */   private BaseTypeBean baseType = null; @XmlAttribute
/*     */   private boolean generateFailFastSetterMethod = false;
/*     */   @XmlAttribute
/*     */   private CollectionTypeAttribute collectionType;
/*     */   @XmlAttribute
/*     */   private OptionalPropertyMode optionalProperty;
/*     */   @XmlAttribute
/*     */   private Boolean generateElementProperty;
/*     */   @XmlAttribute(name = "fixedAttributeAsConstantProperty")
/*     */   private Boolean isConstantProperty;
/*     */   private final XSFinder hasFixedValue;
/*     */   
/* 126 */   public BIProperty(Locator loc, String _propName, String _javadoc, BaseTypeBean _baseType, CollectionTypeAttribute collectionType, Boolean isConst, OptionalPropertyMode optionalProperty, Boolean genElemProp) { super(loc);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 213 */     this.collectionType = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 226 */     this.optionalProperty = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 241 */     this.generateElementProperty = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 596 */     this.hasFixedValue = new XSFinder() {
/*     */         public Boolean attributeDecl(XSAttributeDecl decl) {
/* 598 */           return Boolean.valueOf((decl.getFixedValue() != null));
/*     */         }
/*     */         
/*     */         public Boolean attributeUse(XSAttributeUse use) {
/* 602 */           return Boolean.valueOf((use.getFixedValue() != null));
/*     */         }
/*     */ 
/*     */         
/*     */         public Boolean schema(XSSchema s)
/*     */         {
/* 608 */           return Boolean.valueOf(true); } }; this.name = _propName; this.javadoc = _javadoc; this.baseType = _baseType; this.collectionType = collectionType; this.isConstantProperty = isConst; this.optionalProperty = optionalProperty; this.generateElementProperty = genElemProp; } public Collection<BIDeclaration> getChildren() { BIConversion conv = getConv(); if (conv == null) return super.getChildren();  return Collections.singleton(conv); } public void setParent(BindInfo parent) { super.setParent(parent); if (this.baseType != null && this.baseType.conv != null) this.baseType.conv.setParent(parent);  } public String getPropertyName(boolean forConstant) { if (this.name != null) { BIGlobalBinding gb = getBuilder().getGlobalBinding(); NameConverter nc = (getBuilder()).model.getNameConverter(); if (gb.isJavaNamingConventionEnabled() && !forConstant) return nc.toPropertyName(this.name);  return this.name; }  BIProperty next = getDefault(); if (next != null) return next.getPropertyName(forConstant);  return null; } public String getJavadoc() { return this.javadoc; } public JType getBaseType() { if (this.baseType != null && this.baseType.name != null) return TypeUtil.getType(getCodeModel(), this.baseType.name, (ErrorReceiver)Ring.get(ErrorReceiver.class), getLocation());  BIProperty next = getDefault(); if (next != null) return next.getBaseType();  return null; } protected BIProperty() { this.collectionType = null; this.optionalProperty = null; this.generateElementProperty = null; this.hasFixedValue = new XSFinder() { public Boolean attributeDecl(XSAttributeDecl decl) { return Boolean.valueOf((decl.getFixedValue() != null)); } public Boolean attributeUse(XSAttributeUse use) { return Boolean.valueOf((use.getFixedValue() != null)); } public Boolean schema(XSSchema s) { return Boolean.valueOf(true); } }; }
/*     */   CollectionTypeAttribute getCollectionType() { if (this.collectionType != null) return this.collectionType;  return getDefault().getCollectionType(); }
/*     */   @XmlAttribute void setGenerateIsSetMethod(boolean b) { this.optionalProperty = b ? OptionalPropertyMode.ISSET : OptionalPropertyMode.WRAPPER; }
/*     */   public OptionalPropertyMode getOptionalPropertyMode() { if (this.optionalProperty != null) return this.optionalProperty;  return getDefault().getOptionalPropertyMode(); }
/*     */   private Boolean generateElementProperty() { if (this.generateElementProperty != null) return this.generateElementProperty;  BIProperty next = getDefault(); if (next != null) return next.generateElementProperty();  return null; }
/*     */   public boolean isConstantProperty() { if (this.isConstantProperty != null) return this.isConstantProperty.booleanValue();  BIProperty next = getDefault(); if (next != null) return next.isConstantProperty();  throw new AssertionError(); }
/*     */   public CValuePropertyInfo createValueProperty(String defaultName, boolean forConstant, XSComponent source, TypeUse tu, QName typeName) { markAsAcknowledged(); constantPropertyErrorCheck(); String name = getPropertyName(forConstant); if (name == null) { name = defaultName; if (tu.isCollection() && getBuilder().getGlobalBinding().isSimpleMode())
/*     */         name = JJavaName.getPluralForm(name);  }  CValuePropertyInfo prop = wrapUp(new CValuePropertyInfo(name, source, getCustomizations(source), source.getLocator(), tu, typeName), source); BIInlineBinaryData.handle(source, (CPropertyInfo)prop); return prop; }
/*     */   public CAttributePropertyInfo createAttributeProperty(XSAttributeUse use, TypeUse tu) { boolean forConstant = (getCustomization((XSComponent)use).isConstantProperty() && use.getFixedValue() != null); String name = getPropertyName(forConstant); if (name == null) { NameConverter conv = getBuilder().getNameConverter(); if (forConstant) { name = conv.toConstantName(use.getDecl().getName()); } else { name = conv.toPropertyName(use.getDecl().getName()); }  if (tu.isCollection() && getBuilder().getGlobalBinding().isSimpleMode())
/*     */         name = JJavaName.getPluralForm(name);  }  markAsAcknowledged(); constantPropertyErrorCheck(); return wrapUp(new CAttributePropertyInfo(name, (XSComponent)use, getCustomizations(use), use.getLocator(), BGMBuilder.getName((XSDeclaration)use.getDecl()), tu, BGMBuilder.getName((XSDeclaration)use.getDecl().getType()), use.isRequired()), (XSComponent)use); }
/*     */   public CElementPropertyInfo createElementProperty(String defaultName, boolean forConstant, XSParticle source, RawTypeSet types) { if (!types.refs.isEmpty())
/*     */       markAsAcknowledged();  constantPropertyErrorCheck(); String name = getPropertyName(forConstant); if (name == null)
/*     */       name = defaultName;  CElementPropertyInfo prop = wrapUp(new CElementPropertyInfo(name, types.getCollectionMode(), types.id(), types.getExpectedMimeType(), (XSComponent)source, getCustomizations(source), source.getLocator(), types.isRequired()), (XSComponent)source); types.addTo(prop); BIInlineBinaryData.handle((XSComponent)source.getTerm(), (CPropertyInfo)prop); return prop; }
/* 621 */   public CReferencePropertyInfo createDummyExtendedMixedReferenceProperty(String defaultName, XSComponent source, RawTypeSet types) { return createReferenceProperty(defaultName, false, source, types, true, true, false, true); } protected BIProperty getDefault() { if (getOwner() == null) return null; 
/* 622 */     BIProperty next = getDefault(getBuilder(), getOwner());
/* 623 */     if (next == this) return null; 
/* 624 */     return next; }
/*     */   public CReferencePropertyInfo createContentExtendedMixedReferenceProperty(String defaultName, XSComponent source, RawTypeSet types) { return createReferenceProperty(defaultName, false, source, types, true, false, true, true); }
/*     */   public CReferencePropertyInfo createReferenceProperty(String defaultName, boolean forConstant, XSComponent source, RawTypeSet types, boolean isMixed, boolean dummy, boolean content, boolean isMixedExtended) { if (types == null) { content = true; } else if (!types.refs.isEmpty()) { markAsAcknowledged(); }  constantPropertyErrorCheck(); String name = getPropertyName(forConstant); if (name == null) name = defaultName;  CReferencePropertyInfo prop = wrapUp(new CReferencePropertyInfo(name, (types == null) ? true : ((types.getCollectionMode().isRepeated() || isMixed)), (types == null) ? false : types.isRequired(), isMixed, source, getCustomizations(source), source.getLocator(), dummy, content, isMixedExtended), source); if (types != null) types.addTo(prop);  BIInlineBinaryData.handle(source, (CPropertyInfo)prop); return prop; }
/*     */   public CPropertyInfo createElementOrReferenceProperty(String defaultName, boolean forConstant, XSParticle source, RawTypeSet types) { boolean generateRef; Boolean b; switch (types.canBeTypeRefs) { case PRIMITIVE: case WRAPPER: b = generateElementProperty(); if (b == null) { boolean bool = (types.canBeTypeRefs == RawTypeSet.Mode.CAN_BE_TYPEREF); break; }  generateRef = b.booleanValue(); break;case ISSET: generateRef = true; break;default: throw new AssertionError(); }  if (generateRef) return (CPropertyInfo)createReferenceProperty(defaultName, forConstant, (XSComponent)source, types, false, false, false, false);  return (CPropertyInfo)createElementProperty(defaultName, forConstant, source, types); }
/* 628 */   private <T extends CPropertyInfo> T wrapUp(T prop, XSComponent source) { FieldRenderer r; IsSetFieldRenderer isSetFieldRenderer; ((CPropertyInfo)prop).javadoc = concat(this.javadoc, getBuilder().getBindInfo(source).getDocumentation()); if (((CPropertyInfo)prop).javadoc == null) ((CPropertyInfo)prop).javadoc = "";  OptionalPropertyMode opm = getOptionalPropertyMode(); if (prop.isCollection()) { CollectionTypeAttribute ct = getCollectionType(); r = ct.get((getBuilder()).model); } else { FieldRendererFactory frf = (getBuilder()).fieldRendererFactory; if (prop.isOptionalPrimitive()) { switch (opm) { case PRIMITIVE: r = frf.getRequiredUnboxed(); break;case WRAPPER: r = frf.getSingle(); break;case ISSET: r = frf.getSinglePrimitiveAccess(); break;default: throw new Error(); }  } else { r = frf.getDefault(); }  }  if (opm == OptionalPropertyMode.ISSET) isSetFieldRenderer = new IsSetFieldRenderer(r, (prop.isOptionalPrimitive() || prop.isCollection()), true);  ((CPropertyInfo)prop).realization = (FieldRenderer)isSetFieldRenderer; JType bt = getBaseType(); if (bt != null) ((CPropertyInfo)prop).baseType = bt;  return prop; } private CCustomizations getCustomizations(XSComponent src) { return getBuilder().getBindInfo(src).toCustomizationList(); } private CCustomizations getCustomizations(XSComponent... src) { CCustomizations c = null; for (XSComponent s : src) { CCustomizations r = getCustomizations(s); if (c == null) { c = r; } else { c = CCustomizations.merge(c, r); }  }  return c; } private CCustomizations getCustomizations(XSAttributeUse src) { if (src.getDecl().isLocal()) return getCustomizations(new XSComponent[] { (XSComponent)src, (XSComponent)src.getDecl() });  return getCustomizations((XSComponent)src); } private CCustomizations getCustomizations(XSParticle src) { if (src.getTerm().isElementDecl()) { XSElementDecl xed = src.getTerm().asElementDecl(); if (xed.isGlobal()) return getCustomizations((XSComponent)src);  }  return getCustomizations(new XSComponent[] { (XSComponent)src, (XSComponent)src.getTerm() }); } public void markAsAcknowledged() { if (isAcknowledged()) return;  super.markAsAcknowledged(); BIProperty def = getDefault(); if (def != null) def.markAsAcknowledged();  } private void constantPropertyErrorCheck() { if (this.isConstantProperty != null && getOwner() != null) if (!this.hasFixedValue.find(getOwner())) { ((ErrorReceiver)Ring.get(ErrorReceiver.class)).error(getLocation(), Messages.ERR_ILLEGAL_FIXEDATTR.format(new Object[0])); this.isConstantProperty = null; }   } private static BIProperty getDefault(BGMBuilder builder, XSComponent c) { while (c != null) {
/* 629 */       c = (XSComponent)c.apply(defaultCustomizationFinder);
/* 630 */       if (c != null) {
/* 631 */         BIProperty prop = builder.getBindInfo(c).<BIProperty>get(BIProperty.class);
/* 632 */         if (prop != null) return prop;
/*     */       
/*     */       } 
/*     */     } 
/*     */     
/* 637 */     return builder.getGlobalBinding().getDefaultProperty(); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static BIProperty getCustomization(XSComponent c) {
/* 671 */     BGMBuilder builder = (BGMBuilder)Ring.get(BGMBuilder.class);
/*     */ 
/*     */     
/* 674 */     if (c != null) {
/* 675 */       BIProperty prop = builder.getBindInfo(c).<BIProperty>get(BIProperty.class);
/* 676 */       if (prop != null) return prop;
/*     */     
/*     */     } 
/*     */     
/* 680 */     return getDefault(builder, c);
/*     */   }
/*     */   
/* 683 */   private static final XSFunction<XSComponent> defaultCustomizationFinder = new XSFunction<XSComponent>()
/*     */     {
/*     */       public XSComponent attributeUse(XSAttributeUse use) {
/* 686 */         return (XSComponent)use.getDecl();
/*     */       }
/*     */       
/*     */       public XSComponent particle(XSParticle particle) {
/* 690 */         return (XSComponent)particle.getTerm();
/*     */       }
/*     */ 
/*     */       
/*     */       public XSComponent schema(XSSchema schema) {
/* 695 */         return null;
/*     */       }
/*     */       
/*     */       public XSComponent attributeDecl(XSAttributeDecl decl) {
/* 699 */         return (XSComponent)decl.getOwnerSchema();
/* 700 */       } public XSComponent wildcard(XSWildcard wc) { return (XSComponent)wc.getOwnerSchema(); }
/* 701 */       public XSComponent modelGroupDecl(XSModelGroupDecl decl) { return (XSComponent)decl.getOwnerSchema(); }
/* 702 */       public XSComponent modelGroup(XSModelGroup group) { return (XSComponent)group.getOwnerSchema(); }
/* 703 */       public XSComponent elementDecl(XSElementDecl decl) { return (XSComponent)decl.getOwnerSchema(); }
/* 704 */       public XSComponent complexType(XSComplexType type) { return (XSComponent)type.getOwnerSchema(); } public XSComponent simpleType(XSSimpleType st) {
/* 705 */         return (XSComponent)st.getOwnerSchema();
/*     */       }
/*     */       
/* 708 */       public XSComponent attGroupDecl(XSAttGroupDecl decl) { throw new IllegalStateException(); }
/* 709 */       public XSComponent empty(XSContentType empty) { throw new IllegalStateException(); }
/* 710 */       public XSComponent annotation(XSAnnotation xsAnnotation) { throw new IllegalStateException(); }
/* 711 */       public XSComponent facet(XSFacet xsFacet) { throw new IllegalStateException(); }
/* 712 */       public XSComponent notation(XSNotation xsNotation) { throw new IllegalStateException(); }
/* 713 */       public XSComponent identityConstraint(XSIdentityConstraint x) { throw new IllegalStateException(); } public XSComponent xpath(XSXPath xsxPath) {
/* 714 */         throw new IllegalStateException();
/*     */       }
/*     */     };
/*     */   
/*     */   private static String concat(String s1, String s2) {
/* 719 */     if (s1 == null) return s2; 
/* 720 */     if (s2 == null) return s1; 
/* 721 */     return s1 + "\n\n" + s2;
/*     */   }
/*     */   public QName getName() {
/* 724 */     return NAME;
/*     */   }
/*     */   
/* 727 */   public static final QName NAME = new QName("http://java.sun.com/xml/ns/jaxb", "property");
/*     */ 
/*     */   
/*     */   public BIConversion getConv() {
/* 731 */     if (this.baseType != null) {
/* 732 */       return this.baseType.conv;
/*     */     }
/* 734 */     return null;
/*     */   }
/*     */   
/*     */   private static final class BaseTypeBean {
/*     */     @XmlElementRef
/*     */     BIConversion conv;
/*     */     @XmlAttribute
/*     */     String name;
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\xjc\reader\xmlschema\bindinfo\BIProperty.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */