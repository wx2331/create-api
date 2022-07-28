/*     */ package com.sun.tools.internal.xjc.reader.xmlschema.bindinfo;
/*     */
/*     */ import com.sun.codemodel.internal.ClassType;
/*     */ import com.sun.codemodel.internal.JClassAlreadyExistsException;
/*     */ import com.sun.codemodel.internal.JCodeModel;
/*     */ import com.sun.codemodel.internal.JDefinedClass;
/*     */ import com.sun.tools.internal.xjc.ErrorReceiver;
/*     */ import com.sun.tools.internal.xjc.generator.bean.ImplStructureStrategy;
/*     */ import com.sun.tools.internal.xjc.model.Model;
/*     */ import com.sun.tools.internal.xjc.reader.Ring;
/*     */ import com.sun.tools.internal.xjc.reader.xmlschema.SimpleTypeBuilder;
/*     */ import com.sun.tools.internal.xjc.util.ReadOnlyAdapter;
/*     */ import com.sun.xml.internal.bind.api.impl.NameConverter;
/*     */ import com.sun.xml.internal.xsom.XSComponent;
/*     */ import com.sun.xml.internal.xsom.XSDeclaration;
/*     */ import com.sun.xml.internal.xsom.XSSchemaSet;
/*     */ import com.sun.xml.internal.xsom.XSSimpleType;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import javax.xml.bind.annotation.XmlAttribute;
/*     */ import javax.xml.bind.annotation.XmlElement;
/*     */ import javax.xml.bind.annotation.XmlEnumValue;
/*     */ import javax.xml.bind.annotation.XmlRootElement;
/*     */ import javax.xml.bind.annotation.XmlTransient;
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
/*     */ @XmlRootElement(name = "globalBindings")
/*     */ public final class BIGlobalBinding
/*     */   extends AbstractDeclarationImpl
/*     */ {
/*     */   @XmlTransient
/*  85 */   public NameConverter nameConverter = NameConverter.standard;
/*     */
/*     */
/*     */
/*     */   @XmlAttribute
/*     */   void setUnderscoreBinding(UnderscoreBinding ub) {
/*  91 */     this.nameConverter = ub.nc;
/*     */   }
/*     */
/*     */   UnderscoreBinding getUnderscoreBinding() {
/*  95 */     throw new IllegalStateException();
/*     */   }
/*     */
/*     */   public JDefinedClass getSuperClass() {
/*  99 */     if (this.superClass == null) return null;
/* 100 */     return this.superClass.getClazz(ClassType.CLASS);
/*     */   }
/*     */
/*     */   public JDefinedClass getSuperInterface() {
/* 104 */     if (this.superInterface == null) return null;
/* 105 */     return this.superInterface.getClazz(ClassType.INTERFACE);
/*     */   }
/*     */
/*     */   public BIProperty getDefaultProperty() {
/* 109 */     return this.defaultProperty;
/*     */   }
/*     */
/*     */   public boolean isJavaNamingConventionEnabled() {
/* 113 */     return this.isJavaNamingConventionEnabled;
/*     */   }
/*     */
/*     */   public BISerializable getSerializable() {
/* 117 */     return this.serializable;
/*     */   }
/*     */
/*     */   public boolean isGenerateElementClass() {
/* 121 */     return this.generateElementClass;
/*     */   }
/*     */
/*     */   public boolean isGenerateMixedExtensions() {
/* 125 */     return this.generateMixedExtensions;
/*     */   }
/*     */
/*     */   public boolean isChoiceContentPropertyEnabled() {
/* 129 */     return this.choiceContentProperty;
/*     */   }
/*     */
/*     */   public int getDefaultEnumMemberSizeCap() {
/* 133 */     return this.defaultEnumMemberSizeCap;
/*     */   }
/*     */
/*     */   public boolean isSimpleMode() {
/* 137 */     return (this.simpleMode != null);
/*     */   }
/*     */
/*     */   public boolean isRestrictionFreshType() {
/* 141 */     return (this.treatRestrictionLikeNewType != null);
/*     */   }
/*     */
/*     */   public EnumMemberMode getEnumMemberMode() {
/* 145 */     return this.generateEnumMemberName;
/*     */   }
/*     */
/*     */   public boolean isSimpleTypeSubstitution() {
/* 149 */     return this.simpleTypeSubstitution;
/*     */   }
/*     */
/*     */   public ImplStructureStrategy getCodeGenerationStrategy() {
/* 153 */     return this.codeGenerationStrategy;
/*     */   }
/*     */
/*     */   public LocalScoping getFlattenClasses() {
/* 157 */     return this.flattenClasses;
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */   public void errorCheck() {
/* 164 */     ErrorReceiver er = (ErrorReceiver)Ring.get(ErrorReceiver.class);
/* 165 */     for (QName n : this.enumBaseTypes) {
/* 166 */       XSSchemaSet xs = (XSSchemaSet)Ring.get(XSSchemaSet.class);
/* 167 */       XSSimpleType st = xs.getSimpleType(n.getNamespaceURI(), n.getLocalPart());
/* 168 */       if (st == null) {
/* 169 */         er.error(this.loc, Messages.ERR_UNDEFINED_SIMPLE_TYPE.format(new Object[] { n }));
/*     */
/*     */         continue;
/*     */       }
/* 173 */       if (!SimpleTypeBuilder.canBeMappedToTypeSafeEnum(st)) {
/* 174 */         er.error(this.loc, Messages.ERR_CANNOT_BE_BOUND_TO_SIMPLETYPE.format(new Object[] { n }));
/*     */       }
/*     */     }
/*     */   }
/*     */
/*     */   private enum UnderscoreBinding
/*     */   {
/* 181 */     WORD_SEPARATOR((String)NameConverter.standard),
/*     */
/* 183 */     CHAR_IN_WORD((String)NameConverter.jaxrpcCompatible);
/*     */
/*     */     final NameConverter nc;
/*     */
/*     */
/*     */     UnderscoreBinding(NameConverter nc) {
/* 189 */       this.nc = nc;
/*     */     }
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   @XmlAttribute(name = "enableJavaNamingConventions")
/*     */   boolean isJavaNamingConventionEnabled = true;
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   @XmlAttribute(name = "mapSimpleTypeDef")
/*     */   boolean simpleTypeSubstitution = false;
/*     */
/*     */
/*     */
/*     */
/*     */   @XmlTransient
/*     */   private BIProperty defaultProperty;
/*     */
/*     */
/*     */
/*     */
/*     */   @XmlAttribute
/*     */   private boolean fixedAttributeAsConstantProperty = false;
/*     */
/*     */
/*     */
/*     */
/*     */   @XmlAttribute
/* 223 */   private CollectionTypeAttribute collectionType = new CollectionTypeAttribute();
/*     */
/*     */   @XmlAttribute
/*     */   void setGenerateIsSetMethod(boolean b) {
/* 227 */     this.optionalProperty = b ? OptionalPropertyMode.ISSET : OptionalPropertyMode.WRAPPER;
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   @XmlAttribute(name = "typesafeEnumMemberName")
/* 235 */   EnumMemberMode generateEnumMemberName = EnumMemberMode.SKIP;
/*     */
/*     */
/*     */
/*     */
/*     */   @XmlAttribute(name = "generateValueClass")
/* 241 */   ImplStructureStrategy codeGenerationStrategy = ImplStructureStrategy.BEAN_ONLY;
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   @XmlAttribute(name = "typesafeEnumBase")
/*     */   private Set<QName> enumBaseTypes;
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   @XmlElement
/* 257 */   private BISerializable serializable = null;
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   @XmlElement(namespace = "http://java.sun.com/xml/ns/jaxb/xjc")
/* 264 */   ClassNameBean superClass = null;
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   @XmlElement(namespace = "http://java.sun.com/xml/ns/jaxb/xjc")
/* 271 */   ClassNameBean superInterface = null;
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   @XmlElement(name = "simple", namespace = "http://java.sun.com/xml/ns/jaxb/xjc")
/* 278 */   String simpleMode = null;
/*     */
/*     */
/*     */
/*     */
/*     */   @XmlElement(name = "treatRestrictionLikeNewType", namespace = "http://java.sun.com/xml/ns/jaxb/xjc")
/* 284 */   String treatRestrictionLikeNewType = null;
/*     */
/*     */
/*     */   @XmlAttribute
/*     */   boolean generateElementClass = false;
/*     */
/*     */
/*     */   @XmlAttribute
/*     */   boolean generateMixedExtensions = false;
/*     */
/*     */
/*     */   @XmlElement(namespace = "http://java.sun.com/xml/ns/jaxb/xjc")
/* 296 */   Boolean generateElementProperty = null;
/*     */
/*     */
/*     */   @XmlAttribute(name = "generateElementProperty")
/*     */   private void setGenerateElementPropertyStd(boolean value) {
/* 301 */     this.generateElementProperty = Boolean.valueOf(value);
/*     */   }
/*     */
/*     */   @XmlAttribute
/*     */   boolean choiceContentProperty = false;
/*     */   @XmlAttribute
/* 307 */   OptionalPropertyMode optionalProperty = OptionalPropertyMode.WRAPPER;
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   @XmlAttribute(name = "typesafeEnumMaxMembers")
/* 315 */   int defaultEnumMemberSizeCap = 256;
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   @XmlAttribute(name = "localScoping")
/* 324 */   LocalScoping flattenClasses = LocalScoping.NESTED;
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   @XmlTransient
/* 332 */   private final Map<QName, BIConversion> globalConversions = new HashMap<>();
/*     */
/*     */
/*     */
/*     */   @XmlElement(name = "javaType")
/*     */   private void setGlobalConversions(GlobalStandardConversion[] convs) {
/* 338 */     for (GlobalStandardConversion u : convs) {
/* 339 */       this.globalConversions.put(u.xmlType, u);
/*     */     }
/*     */   }
/*     */
/*     */   @XmlElement(name = "javaType", namespace = "http://java.sun.com/xml/ns/jaxb/xjc")
/*     */   private void setGlobalConversions2(GlobalVendorConversion[] convs) {
/* 345 */     for (GlobalVendorConversion u : convs) {
/* 346 */       this.globalConversions.put(u.xmlType, u);
/*     */     }
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   @XmlElement(namespace = "http://java.sun.com/xml/ns/jaxb/xjc")
/* 355 */   String noMarshaller = null;
/*     */   @XmlElement(namespace = "http://java.sun.com/xml/ns/jaxb/xjc")
/* 357 */   String noUnmarshaller = null;
/*     */   @XmlElement(namespace = "http://java.sun.com/xml/ns/jaxb/xjc")
/* 359 */   String noValidator = null;
/*     */   @XmlElement(namespace = "http://java.sun.com/xml/ns/jaxb/xjc")
/* 361 */   String noValidatingUnmarshaller = null;
/*     */   @XmlElement(namespace = "http://java.sun.com/xml/ns/jaxb/xjc")
/* 363 */   TypeSubstitutionElement typeSubstitution = null;
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   @XmlElement(name = "serializable", namespace = "http://java.sun.com/xml/ns/jaxb/xjc")
/*     */   void setXjcSerializable(BISerializable s) {
/* 372 */     this.serializable = s;
/*     */   }
/*     */
/*     */
/*     */   private static final class TypeSubstitutionElement
/*     */   {
/*     */     @XmlAttribute
/*     */     String type;
/*     */   }
/*     */
/*     */   public void onSetOwner() {
/* 383 */     super.onSetOwner();
/*     */
/* 385 */     NameConverter nc = ((Model)Ring.get(Model.class)).options.getNameConverter();
/* 386 */     if (nc != null) {
/* 387 */       this.nameConverter = nc;
/*     */     }
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   public void setParent(BindInfo parent) {
/* 397 */     super.setParent(parent);
/*     */
/* 399 */     if (this.enumBaseTypes == null) {
/* 400 */       this.enumBaseTypes = Collections.singleton(new QName("http://www.w3.org/2001/XMLSchema", "string"));
/*     */     }
/* 402 */     this
/* 403 */       .defaultProperty = new BIProperty(getLocation(), null, null, null, this.collectionType, Boolean.valueOf(this.fixedAttributeAsConstantProperty), this.optionalProperty, this.generateElementProperty);
/* 404 */     this.defaultProperty.setParent(parent);
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   public void dispatchGlobalConversions(XSSchemaSet schema) {
/* 412 */     for (Map.Entry<QName, BIConversion> e : this.globalConversions.entrySet()) {
/*     */
/* 414 */       QName name = e.getKey();
/* 415 */       BIConversion conv = e.getValue();
/*     */
/* 417 */       XSSimpleType st = schema.getSimpleType(name.getNamespaceURI(), name.getLocalPart());
/* 418 */       if (st == null) {
/* 419 */         ((ErrorReceiver)Ring.get(ErrorReceiver.class)).error(
/* 420 */             getLocation(), Messages.ERR_UNDEFINED_SIMPLE_TYPE
/* 421 */             .format(new Object[] { name }));
/*     */
/*     */         continue;
/*     */       }
/*     */
/* 426 */       getBuilder().getOrCreateBindInfo((XSComponent)st).addDecl(conv);
/*     */     }
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   public boolean canBeMappedToTypeSafeEnum(QName typeName) {
/* 438 */     return this.enumBaseTypes.contains(typeName);
/*     */   }
/*     */
/*     */   public boolean canBeMappedToTypeSafeEnum(String nsUri, String localName) {
/* 442 */     return canBeMappedToTypeSafeEnum(new QName(nsUri, localName));
/*     */   }
/*     */
/*     */   public boolean canBeMappedToTypeSafeEnum(XSDeclaration decl) {
/* 446 */     return canBeMappedToTypeSafeEnum(decl.getTargetNamespace(), decl.getName());
/*     */   }
/*     */
/*     */   public QName getName() {
/* 450 */     return NAME;
/* 451 */   } public static final QName NAME = new QName("http://java.sun.com/xml/ns/jaxb", "globalBindings");
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   static final class ClassNameBean
/*     */   {
/*     */     @XmlAttribute(required = true)
/*     */     String name;
/*     */
/*     */
/*     */
/*     */
/*     */     @XmlTransient
/*     */     JDefinedClass clazz;
/*     */
/*     */
/*     */
/*     */
/*     */     JDefinedClass getClazz(ClassType t) {
/* 472 */       if (this.clazz != null) return this.clazz;
/*     */       try {
/* 474 */         JCodeModel codeModel = (JCodeModel)Ring.get(JCodeModel.class);
/* 475 */         this.clazz = codeModel._class(this.name, t);
/* 476 */         this.clazz.hide();
/* 477 */         return this.clazz;
/* 478 */       } catch (JClassAlreadyExistsException e) {
/* 479 */         return e.getExistingClass();
/*     */       }
/*     */     }
/*     */   }
/*     */
/*     */   static final class ClassNameAdapter extends ReadOnlyAdapter<ClassNameBean, String> {
/*     */     public String unmarshal(ClassNameBean bean) throws Exception {
/* 486 */       return bean.name;
/*     */     }
/*     */   }
/*     */
/*     */
/*     */   static final class GlobalStandardConversion
/*     */     extends BIConversion.User
/*     */   {
/*     */     @XmlAttribute
/*     */     QName xmlType;
/*     */
/*     */
/*     */     public boolean equals(Object obj) {
/* 499 */       if (obj instanceof GlobalStandardConversion) {
/* 500 */         return ((GlobalStandardConversion)obj).xmlType.equals(this.xmlType);
/*     */       }
/*     */
/* 503 */       return false;
/*     */     }
/*     */
/*     */
/*     */     public int hashCode() {
/* 508 */       int hash = 7;
/* 509 */       hash = 73 * hash + ((this.xmlType != null) ? this.xmlType.hashCode() : 0);
/* 510 */       return hash;
/*     */     }
/*     */   }
/*     */
/*     */
/*     */   static final class GlobalVendorConversion
/*     */     extends BIConversion.UserAdapter
/*     */   {
/*     */     @XmlAttribute
/*     */     QName xmlType;
/*     */
/*     */
/*     */     public boolean equals(Object obj) {
/* 523 */       if (obj instanceof GlobalVendorConversion) {
/* 524 */         return ((GlobalVendorConversion)obj).xmlType.equals(this.xmlType);
/*     */       }
/*     */
/* 527 */       return false;
/*     */     }
/*     */
/*     */
/*     */     public int hashCode() {
/* 532 */       int hash = 7;
/* 533 */       hash = 73 * hash + ((this.xmlType != null) ? this.xmlType.hashCode() : 0);
/* 534 */       return hash;
/*     */     }
/*     */   }
/*     */
/*     */
/*     */   public boolean isEqual(BIGlobalBinding b) {
/* 540 */     boolean equal = (this.isJavaNamingConventionEnabled == b.isJavaNamingConventionEnabled && this.simpleTypeSubstitution == b.simpleTypeSubstitution && this.fixedAttributeAsConstantProperty == b.fixedAttributeAsConstantProperty && this.generateEnumMemberName == b.generateEnumMemberName && this.codeGenerationStrategy == b.codeGenerationStrategy && this.serializable == b.serializable && this.superClass == b.superClass && this.superInterface == b.superInterface && this.generateElementClass == b.generateElementClass && this.generateMixedExtensions == b.generateMixedExtensions && this.generateElementProperty == b.generateElementProperty && this.choiceContentProperty == b.choiceContentProperty && this.optionalProperty == b.optionalProperty && this.defaultEnumMemberSizeCap == b.defaultEnumMemberSizeCap && this.flattenClasses == b.flattenClasses);
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/* 557 */     if (!equal) return false;
/*     */
/* 559 */     return (isEqual(this.nameConverter, b.nameConverter) &&
/* 560 */       isEqual(this.noMarshaller, b.noMarshaller) &&
/* 561 */       isEqual(this.noUnmarshaller, b.noUnmarshaller) &&
/* 562 */       isEqual(this.noValidator, b.noValidator) &&
/* 563 */       isEqual(this.noValidatingUnmarshaller, b.noValidatingUnmarshaller) &&
/* 564 */       isEqual(this.typeSubstitution, b.typeSubstitution) &&
/* 565 */       isEqual(this.simpleMode, b.simpleMode) &&
/* 566 */       isEqual(this.enumBaseTypes, b.enumBaseTypes) &&
/* 567 */       isEqual(this.treatRestrictionLikeNewType, b.treatRestrictionLikeNewType) &&
/* 568 */       isEqual(this.globalConversions, b.globalConversions));
/*     */   }
/*     */
/*     */   private boolean isEqual(Object a, Object b) {
/* 572 */     if (a != null) {
/* 573 */       return a.equals(b);
/*     */     }
/* 575 */     return (b == null);
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\xjc\reader\xmlschema\bindinfo\BIGlobalBinding.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
