/*     */ package com.sun.tools.internal.xjc.generator.bean;
/*     */ 
/*     */ import com.sun.codemodel.internal.ClassType;
/*     */ import com.sun.codemodel.internal.JAnnotatable;
/*     */ import com.sun.codemodel.internal.JAssignmentTarget;
/*     */ import com.sun.codemodel.internal.JClass;
/*     */ import com.sun.codemodel.internal.JClassAlreadyExistsException;
/*     */ import com.sun.codemodel.internal.JClassContainer;
/*     */ import com.sun.codemodel.internal.JCodeModel;
/*     */ import com.sun.codemodel.internal.JDefinedClass;
/*     */ import com.sun.codemodel.internal.JEnumConstant;
/*     */ import com.sun.codemodel.internal.JExpr;
/*     */ import com.sun.codemodel.internal.JExpression;
/*     */ import com.sun.codemodel.internal.JFieldVar;
/*     */ import com.sun.codemodel.internal.JForEach;
/*     */ import com.sun.codemodel.internal.JInvocation;
/*     */ import com.sun.codemodel.internal.JJavaName;
/*     */ import com.sun.codemodel.internal.JMethod;
/*     */ import com.sun.codemodel.internal.JPackage;
/*     */ import com.sun.codemodel.internal.JResourceFile;
/*     */ import com.sun.codemodel.internal.JType;
/*     */ import com.sun.codemodel.internal.JVar;
/*     */ import com.sun.codemodel.internal.fmt.JStaticJavaFile;
/*     */ import com.sun.istack.internal.NotNull;
/*     */ import com.sun.tools.internal.xjc.AbortException;
/*     */ import com.sun.tools.internal.xjc.ErrorReceiver;
/*     */ import com.sun.tools.internal.xjc.api.SpecVersion;
/*     */ import com.sun.tools.internal.xjc.generator.annotation.spec.XmlAnyAttributeWriter;
/*     */ import com.sun.tools.internal.xjc.generator.annotation.spec.XmlEnumValueWriter;
/*     */ import com.sun.tools.internal.xjc.generator.annotation.spec.XmlEnumWriter;
/*     */ import com.sun.tools.internal.xjc.generator.annotation.spec.XmlJavaTypeAdapterWriter;
/*     */ import com.sun.tools.internal.xjc.generator.annotation.spec.XmlMimeTypeWriter;
/*     */ import com.sun.tools.internal.xjc.generator.annotation.spec.XmlRootElementWriter;
/*     */ import com.sun.tools.internal.xjc.generator.annotation.spec.XmlSeeAlsoWriter;
/*     */ import com.sun.tools.internal.xjc.generator.annotation.spec.XmlTypeWriter;
/*     */ import com.sun.tools.internal.xjc.generator.bean.field.FieldRenderer;
/*     */ import com.sun.tools.internal.xjc.model.CAdapter;
/*     */ import com.sun.tools.internal.xjc.model.CClassInfo;
/*     */ import com.sun.tools.internal.xjc.model.CClassInfoParent;
/*     */ import com.sun.tools.internal.xjc.model.CClassRef;
/*     */ import com.sun.tools.internal.xjc.model.CElementInfo;
/*     */ import com.sun.tools.internal.xjc.model.CEnumConstant;
/*     */ import com.sun.tools.internal.xjc.model.CEnumLeafInfo;
/*     */ import com.sun.tools.internal.xjc.model.CPropertyInfo;
/*     */ import com.sun.tools.internal.xjc.model.CReferencePropertyInfo;
/*     */ import com.sun.tools.internal.xjc.model.CTypeRef;
/*     */ import com.sun.tools.internal.xjc.model.Model;
/*     */ import com.sun.tools.internal.xjc.model.nav.NClass;
/*     */ import com.sun.tools.internal.xjc.model.nav.NType;
/*     */ import com.sun.tools.internal.xjc.outline.Aspect;
/*     */ import com.sun.tools.internal.xjc.outline.ClassOutline;
/*     */ import com.sun.tools.internal.xjc.outline.ElementOutline;
/*     */ import com.sun.tools.internal.xjc.outline.EnumConstantOutline;
/*     */ import com.sun.tools.internal.xjc.outline.EnumOutline;
/*     */ import com.sun.tools.internal.xjc.outline.FieldOutline;
/*     */ import com.sun.tools.internal.xjc.outline.Outline;
/*     */ import com.sun.tools.internal.xjc.outline.PackageOutline;
/*     */ import com.sun.tools.internal.xjc.util.CodeModelClassFactory;
/*     */ import com.sun.xml.internal.bind.v2.model.core.ID;
/*     */ import com.sun.xml.internal.bind.v2.runtime.SwaRefAdapterMarker;
/*     */ import com.sun.xml.internal.xsom.XmlString;
/*     */ import java.io.Serializable;
/*     */ import java.net.URL;
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.TreeSet;
/*     */ import javax.xml.bind.JAXBContext;
/*     */ import javax.xml.bind.JAXBException;
/*     */ import javax.xml.bind.annotation.XmlAttachmentRef;
/*     */ import javax.xml.bind.annotation.XmlID;
/*     */ import javax.xml.bind.annotation.XmlIDREF;
/*     */ import javax.xml.namespace.QName;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class BeanGenerator
/*     */   implements Outline
/*     */ {
/*     */   private final CodeModelClassFactory codeModelClassFactory;
/*     */   private final ErrorReceiver errorReceiver;
/* 116 */   private final Map<JPackage, PackageOutlineImpl> packageContexts = new LinkedHashMap<>();
/*     */   
/* 118 */   private final Map<CClassInfo, ClassOutlineImpl> classes = new LinkedHashMap<>();
/*     */   
/* 120 */   private final Map<CEnumLeafInfo, EnumOutline> enums = new LinkedHashMap<>();
/*     */ 
/*     */ 
/*     */   
/* 124 */   private final Map<Class, JClass> generatedRuntime = (Map)new LinkedHashMap<>();
/*     */ 
/*     */   
/*     */   private final Model model;
/*     */   
/*     */   private final JCodeModel codeModel;
/*     */   
/* 131 */   private final Map<CPropertyInfo, FieldOutline> fields = new LinkedHashMap<>();
/*     */ 
/*     */ 
/*     */   
/* 135 */   final Map<CElementInfo, ElementOutlineImpl> elements = new LinkedHashMap<>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final CClassInfoParent.Visitor<JClassContainer> exposedContainerBuilder;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final CClassInfoParent.Visitor<JClassContainer> implContainerBuilder;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Outline generate(Model model, ErrorReceiver _errorReceiver) {
/*     */     try {
/* 154 */       return new BeanGenerator(model, _errorReceiver);
/* 155 */     } catch (AbortException e) {
/* 156 */       return null;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void generateClassList() {
/*     */     try {
/*     */       StringBuilder buf;
/* 273 */       JDefinedClass jc = this.codeModel.rootPackage()._class("JAXBDebug");
/* 274 */       JMethod m = jc.method(17, JAXBContext.class, "createContext");
/* 275 */       JVar $classLoader = m.param(ClassLoader.class, "classLoader");
/* 276 */       m._throws(JAXBException.class);
/* 277 */       JInvocation inv = this.codeModel.ref(JAXBContext.class).staticInvoke("newInstance");
/* 278 */       m.body()._return((JExpression)inv);
/*     */       
/* 280 */       switch (this.model.strategy) {
/*     */         case ID:
/* 282 */           buf = new StringBuilder();
/* 283 */           for (PackageOutlineImpl po : this.packageContexts.values()) {
/* 284 */             if (buf.length() > 0) {
/* 285 */               buf.append(':');
/*     */             }
/* 287 */             buf.append(po._package().name());
/*     */           } 
/* 289 */           inv.arg(buf.toString()).arg((JExpression)$classLoader);
/*     */           return;
/*     */         
/*     */         case IDREF:
/* 293 */           for (ClassOutlineImpl cc : getClasses()) {
/* 294 */             inv.arg(cc.implRef.dotclass());
/*     */           }
/* 296 */           for (PackageOutlineImpl po : this.packageContexts.values()) {
/* 297 */             inv.arg(po.objectFactory().dotclass());
/*     */           }
/*     */           return;
/*     */       } 
/* 301 */       throw new IllegalStateException();
/*     */     }
/* 303 */     catch (JClassAlreadyExistsException e) {
/* 304 */       e.printStackTrace();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Model getModel() {
/* 311 */     return this.model;
/*     */   }
/*     */   
/*     */   public JCodeModel getCodeModel() {
/* 315 */     return this.codeModel;
/*     */   }
/*     */   
/*     */   public JClassContainer getContainer(CClassInfoParent parent, Aspect aspect) {
/*     */     CClassInfoParent.Visitor<JClassContainer> v;
/* 320 */     switch (aspect) {
/*     */       case ID:
/* 322 */         v = this.exposedContainerBuilder;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 331 */         return (JClassContainer)parent.accept(v);case IDREF: v = this.implContainerBuilder; return (JClassContainer)parent.accept(v);
/*     */     } 
/*     */     assert false;
/*     */     throw new IllegalStateException(); } public final JType resolve(CTypeRef ref, Aspect a) {
/* 335 */     return ((NType)ref.getTarget().getType()).toType(this, a);
/*     */   }
/* 337 */   private BeanGenerator(Model _model, ErrorReceiver _errorReceiver) { this.exposedContainerBuilder = new CClassInfoParent.Visitor<JClassContainer>()
/*     */       {
/*     */         public JClassContainer onBean(CClassInfo bean)
/*     */         {
/* 341 */           return (JClassContainer)(BeanGenerator.this.getClazz(bean)).ref;
/*     */         }
/*     */ 
/*     */         
/*     */         public JClassContainer onElement(CElementInfo element) {
/* 346 */           return (JClassContainer)(BeanGenerator.this.getElement(element)).implClass;
/*     */         }
/*     */         
/*     */         public JClassContainer onPackage(JPackage pkg) {
/* 350 */           return (JClassContainer)BeanGenerator.this.model.strategy.getPackage(pkg, Aspect.EXPOSED);
/*     */         }
/*     */       };
/* 353 */     this.implContainerBuilder = new CClassInfoParent.Visitor<JClassContainer>()
/*     */       {
/*     */         public JClassContainer onBean(CClassInfo bean)
/*     */         {
/* 357 */           return (JClassContainer)(BeanGenerator.this.getClazz(bean)).implClass;
/*     */         }
/*     */         
/*     */         public JClassContainer onElement(CElementInfo element) {
/* 361 */           return (JClassContainer)(BeanGenerator.this.getElement(element)).implClass;
/*     */         }
/*     */         
/*     */         public JClassContainer onPackage(JPackage pkg) {
/* 365 */           return (JClassContainer)BeanGenerator.this.model.strategy.getPackage(pkg, Aspect.IMPLEMENTATION); } }; this.model = _model; this.codeModel = this.model.codeModel; this.errorReceiver = _errorReceiver; this.codeModelClassFactory = new CodeModelClassFactory(this.errorReceiver); for (CEnumLeafInfo p : this.model.enums().values())
/*     */       this.enums.put(p, generateEnumDef(p));  JPackage[] packages = getUsedPackages(Aspect.EXPOSED); for (JPackage pkg : packages)
/*     */       getPackageContext(pkg);  for (CClassInfo bean : this.model.beans().values())
/*     */       getClazz(bean);  for (PackageOutlineImpl p : this.packageContexts.values())
/*     */       p.calcDefaultValues();  JClass OBJECT = this.codeModel.ref(Object.class); for (ClassOutlineImpl cc : getClasses()) { CClassInfo superClass = cc.target.getBaseClass(); if (superClass != null) { this.model.strategy._extends(cc, getClazz(superClass)); } else { CClassRef refSuperClass = cc.target.getRefBaseClass(); if (refSuperClass != null) { cc.implClass._extends(refSuperClass.toType(this, Aspect.EXPOSED)); }
/*     */         else { if (this.model.rootClass != null && cc.implClass._extends().equals(OBJECT))
/*     */             cc.implClass._extends(this.model.rootClass);  if (this.model.rootInterface != null)
/*     */             cc.ref._implements(this.model.rootInterface);  }
/*     */          }
/*     */        if (this.model.serializable) { cc.implClass._implements(Serializable.class); if (this.model.serialVersionUID != null)
/*     */           cc.implClass.field(28, (JType)this.codeModel.LONG, "serialVersionUID", JExpr.lit(this.model.serialVersionUID.longValue()));  }
/*     */        CClassInfoParent base = cc.target.parent(); if (base != null && base instanceof CClassInfo) { String pkg = base.getOwnerPackage().name(); String shortName = base.fullName().substring(base.fullName().indexOf(pkg) + pkg.length() + 1); if (cc.target.shortName.equals(shortName))
/*     */           getErrorReceiver().error(cc.target.getLocator(), Messages.ERR_KEYNAME_COLLISION.format(new Object[] { shortName }));  }
/*     */        }
/*     */      for (ClassOutlineImpl co : getClasses())
/*     */       generateClassBody(co);  for (EnumOutline eo : this.enums.values())
/*     */       generateEnumBody(eo);  for (CElementInfo ei : this.model.getAllElements())
/*     */       getPackageContext(ei._package()).objectFactoryGenerator().populate(ei);  if (this.model.options.debugMode)
/* 383 */       generateClassList();  } public final JPackage[] getUsedPackages(Aspect aspect) { Set<JPackage> s = new TreeSet<>();
/*     */     
/* 385 */     for (CClassInfo bean : this.model.beans().values()) {
/* 386 */       JClassContainer cont = getContainer(bean.parent(), aspect);
/* 387 */       if (cont.isPackage()) {
/* 388 */         s.add((JPackage)cont);
/*     */       }
/*     */     } 
/*     */     
/* 392 */     for (CElementInfo e : this.model.getElementMappings(null).values())
/*     */     {
/*     */ 
/*     */ 
/*     */       
/* 397 */       s.add(e._package());
/*     */     }
/*     */     
/* 400 */     return s.<JPackage>toArray(new JPackage[s.size()]); }
/*     */ 
/*     */   
/*     */   public ErrorReceiver getErrorReceiver() {
/* 404 */     return this.errorReceiver;
/*     */   }
/*     */   
/*     */   public CodeModelClassFactory getClassFactory() {
/* 408 */     return this.codeModelClassFactory;
/*     */   }
/*     */   
/*     */   public PackageOutlineImpl getPackageContext(JPackage p) {
/* 412 */     PackageOutlineImpl r = this.packageContexts.get(p);
/* 413 */     if (r == null) {
/* 414 */       r = new PackageOutlineImpl(this, this.model, p);
/* 415 */       this.packageContexts.put(p, r);
/*     */     } 
/* 417 */     return r;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private ClassOutlineImpl generateClassDef(CClassInfo bean) {
/*     */     JDefinedClass jDefinedClass;
/* 425 */     ImplStructureStrategy.Result r = this.model.strategy.createClasses(this, bean);
/*     */ 
/*     */     
/* 428 */     if (bean.getUserSpecifiedImplClass() != null) {
/*     */       JDefinedClass usr;
/*     */       
/*     */       try {
/* 432 */         usr = this.codeModel._class(bean.getUserSpecifiedImplClass());
/*     */         
/* 434 */         usr.hide();
/* 435 */       } catch (JClassAlreadyExistsException e) {
/*     */         
/* 437 */         usr = e.getExistingClass();
/*     */       } 
/* 439 */       usr._extends((JClass)r.implementation);
/* 440 */       jDefinedClass = usr;
/*     */     } else {
/* 442 */       jDefinedClass = r.implementation;
/*     */     } 
/*     */     
/* 445 */     return new ClassOutlineImpl(this, bean, r.exposed, r.implementation, (JClass)jDefinedClass);
/*     */   }
/*     */ 
/*     */   
/*     */   public Collection<ClassOutlineImpl> getClasses() {
/* 450 */     assert this.model.beans().size() == this.classes.size();
/* 451 */     return this.classes.values();
/*     */   }
/*     */   
/*     */   public ClassOutlineImpl getClazz(CClassInfo bean) {
/* 455 */     ClassOutlineImpl r = this.classes.get(bean);
/* 456 */     if (r == null) {
/* 457 */       this.classes.put(bean, r = generateClassDef(bean));
/*     */     }
/* 459 */     return r;
/*     */   }
/*     */   
/*     */   public ElementOutlineImpl getElement(CElementInfo ei) {
/* 463 */     ElementOutlineImpl def = this.elements.get(ei);
/* 464 */     if (def == null && ei.hasClass())
/*     */     {
/* 466 */       def = new ElementOutlineImpl(this, ei);
/*     */     }
/* 468 */     return def;
/*     */   }
/*     */   
/*     */   public EnumOutline getEnum(CEnumLeafInfo eli) {
/* 472 */     return this.enums.get(eli);
/*     */   }
/*     */   
/*     */   public Collection<EnumOutline> getEnums() {
/* 476 */     return this.enums.values();
/*     */   }
/*     */   
/*     */   public Iterable<? extends PackageOutline> getAllPackageContexts() {
/* 480 */     return this.packageContexts.values();
/*     */   }
/*     */   
/*     */   public FieldOutline getField(CPropertyInfo prop) {
/* 484 */     return this.fields.get(prop);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void generateClassBody(ClassOutlineImpl cc) {
/* 492 */     CClassInfo target = cc.target;
/*     */ 
/*     */     
/* 495 */     String mostUsedNamespaceURI = cc._package().getMostUsedNamespaceURI();
/*     */ 
/*     */ 
/*     */     
/* 499 */     XmlTypeWriter xtw = (XmlTypeWriter)cc.implClass.annotate2(XmlTypeWriter.class);
/* 500 */     writeTypeName(cc.target.getTypeName(), xtw, mostUsedNamespaceURI);
/*     */     
/* 502 */     if (this.model.options.target.isLaterThan(SpecVersion.V2_1)) {
/*     */       
/* 504 */       Iterator<CClassInfo> subclasses = cc.target.listSubclasses();
/* 505 */       if (subclasses.hasNext()) {
/* 506 */         XmlSeeAlsoWriter saw = (XmlSeeAlsoWriter)cc.implClass.annotate2(XmlSeeAlsoWriter.class);
/* 507 */         while (subclasses.hasNext()) {
/* 508 */           CClassInfo s = subclasses.next();
/* 509 */           saw.value((JType)(getClazz(s)).implRef);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 514 */     if (target.isElement()) {
/* 515 */       String namespaceURI = target.getElementName().getNamespaceURI();
/* 516 */       String localPart = target.getElementName().getLocalPart();
/*     */ 
/*     */ 
/*     */       
/* 520 */       XmlRootElementWriter xrew = (XmlRootElementWriter)cc.implClass.annotate2(XmlRootElementWriter.class);
/* 521 */       xrew.name(localPart);
/* 522 */       if (!namespaceURI.equals(mostUsedNamespaceURI))
/*     */       {
/* 524 */         xrew.namespace(namespaceURI);
/*     */       }
/*     */     } 
/*     */     
/* 528 */     if (target.isOrdered()) {
/* 529 */       for (CPropertyInfo p : target.getProperties()) {
/* 530 */         if (!(p instanceof com.sun.tools.internal.xjc.model.CAttributePropertyInfo) && (
/* 531 */           !(p instanceof CReferencePropertyInfo) || 
/* 532 */           !((CReferencePropertyInfo)p).isDummy())) {
/* 533 */           xtw.propOrder(p.getName(false));
/*     */         }
/*     */       }
/*     */     
/*     */     } else {
/*     */       
/* 539 */       xtw.getAnnotationUse().paramArray("propOrder");
/*     */     } 
/*     */     
/* 542 */     for (CPropertyInfo prop : target.getProperties()) {
/* 543 */       generateFieldDecl(cc, prop);
/*     */     }
/*     */     
/* 546 */     if (target.declaresAttributeWildcard()) {
/* 547 */       generateAttributeWildcard(cc);
/*     */     }
/*     */ 
/*     */     
/* 551 */     cc.ref.javadoc().append(target.javadoc);
/*     */     
/* 553 */     cc._package().objectFactoryGenerator().populate(cc);
/*     */   }
/*     */   
/*     */   private void writeTypeName(QName typeName, XmlTypeWriter xtw, String mostUsedNamespaceURI) {
/* 557 */     if (typeName == null) {
/* 558 */       xtw.name("");
/*     */     } else {
/* 560 */       xtw.name(typeName.getLocalPart());
/* 561 */       String typeNameURI = typeName.getNamespaceURI();
/* 562 */       if (!typeNameURI.equals(mostUsedNamespaceURI))
/*     */       {
/* 564 */         xtw.namespace(typeNameURI);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void generateAttributeWildcard(ClassOutlineImpl cc) {
/* 573 */     String FIELD_NAME = "otherAttributes";
/* 574 */     String METHOD_SEED = this.model.getNameConverter().toClassName(FIELD_NAME);
/*     */     
/* 576 */     JClass mapType = this.codeModel.ref(Map.class).narrow(new Class[] { QName.class, String.class });
/* 577 */     JClass mapImpl = this.codeModel.ref(HashMap.class).narrow(new Class[] { QName.class, String.class });
/*     */ 
/*     */ 
/*     */     
/* 581 */     JFieldVar $ref = cc.implClass.field(4, (JType)mapType, FIELD_NAME, 
/* 582 */         (JExpression)JExpr._new(mapImpl));
/* 583 */     $ref.annotate2(XmlAnyAttributeWriter.class);
/*     */     
/* 585 */     MethodWriter writer = cc.createMethodWriter();
/*     */     
/* 587 */     JMethod $get = writer.declareMethod((JType)mapType, "get" + METHOD_SEED);
/* 588 */     $get.javadoc().append("Gets a map that contains attributes that aren't bound to any typed property on this class.\n\n<p>\nthe map is keyed by the name of the attribute and \nthe value is the string value of the attribute.\n\nthe map returned by this method is live, and you can add new attribute\nby updating the map directly. Because of this design, there's no setter.\n");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 596 */     $get.javadoc().addReturn().append("always non-null");
/*     */     
/* 598 */     $get.body()._return((JExpression)$ref);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private EnumOutline generateEnumDef(CEnumLeafInfo e) {
/* 608 */     JDefinedClass type = getClassFactory().createClass(
/* 609 */         getContainer(e.parent, Aspect.EXPOSED), e.shortName, e.getLocator(), ClassType.ENUM);
/* 610 */     type.javadoc().append(e.javadoc);
/*     */     
/* 612 */     return new EnumOutline(e, type)
/*     */       {
/*     */         
/*     */         @NotNull
/*     */         public Outline parent()
/*     */         {
/* 618 */           return BeanGenerator.this;
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   private void generateEnumBody(EnumOutline eo) {
/* 624 */     JDefinedClass type = eo.clazz;
/* 625 */     CEnumLeafInfo e = eo.target;
/*     */     
/* 627 */     XmlTypeWriter xtw = (XmlTypeWriter)type.annotate2(XmlTypeWriter.class);
/* 628 */     writeTypeName(e.getTypeName(), xtw, eo
/* 629 */         ._package().getMostUsedNamespaceURI());
/*     */     
/* 631 */     JCodeModel cModel = this.model.codeModel;
/*     */ 
/*     */     
/* 634 */     JType baseExposedType = e.base.toType(this, Aspect.EXPOSED).unboxify();
/* 635 */     JType baseImplType = e.base.toType(this, Aspect.IMPLEMENTATION).unboxify();
/*     */ 
/*     */     
/* 638 */     XmlEnumWriter xew = (XmlEnumWriter)type.annotate2(XmlEnumWriter.class);
/* 639 */     xew.value(baseExposedType);
/*     */ 
/*     */     
/* 642 */     boolean needsValue = e.needsValueField();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 648 */     Set<String> enumFieldNames = new HashSet<>();
/*     */     
/* 650 */     for (CEnumConstant mem : e.members) {
/* 651 */       String constName = mem.getName();
/*     */       
/* 653 */       if (!JJavaName.isJavaIdentifier(constName))
/*     */       {
/* 655 */         getErrorReceiver().error(e.getLocator(), Messages.ERR_UNUSABLE_NAME
/* 656 */             .format(new Object[] { mem.getLexicalValue(), constName }));
/*     */       }
/*     */       
/* 659 */       if (!enumFieldNames.add(constName)) {
/* 660 */         getErrorReceiver().error(e.getLocator(), Messages.ERR_NAME_COLLISION.format(new Object[] { constName }));
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 666 */       JEnumConstant constRef = type.enumConstant(constName);
/* 667 */       if (needsValue) {
/* 668 */         constRef.arg(e.base.createConstant(this, new XmlString(mem.getLexicalValue())));
/*     */       }
/*     */       
/* 671 */       if (!mem.getLexicalValue().equals(constName)) {
/* 672 */         ((XmlEnumValueWriter)constRef.annotate2(XmlEnumValueWriter.class)).value(mem.getLexicalValue());
/*     */       }
/*     */ 
/*     */       
/* 676 */       if (mem.javadoc != null) {
/* 677 */         constRef.javadoc().append(mem.javadoc);
/*     */       }
/*     */       
/* 680 */       eo.constants.add(new EnumConstantOutline(mem, constRef)
/*     */           {
/*     */           
/*     */           });
/*     */     } 
/* 685 */     if (needsValue) {
/*     */       JInvocation jInvocation1, jInvocation2;
/*     */       
/* 688 */       JFieldVar $value = type.field(12, baseExposedType, "value");
/*     */ 
/*     */ 
/*     */       
/* 692 */       type.method(1, baseExposedType, "value").body()._return((JExpression)$value);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 699 */       JMethod m = type.constructor(0);
/* 700 */       m.body().assign((JAssignmentTarget)$value, (JExpression)m.param(baseImplType, "v"));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 712 */       m = type.method(17, (JType)type, "fromValue");
/* 713 */       JVar $v = m.param(baseExposedType, "v");
/* 714 */       JForEach fe = m.body().forEach((JType)type, "c", (JExpression)type.staticInvoke("values"));
/*     */       
/* 716 */       if (baseExposedType.isPrimitive()) {
/* 717 */         JExpression eq = fe.var().ref((JVar)$value).eq((JExpression)$v);
/*     */       } else {
/* 719 */         jInvocation1 = fe.var().ref((JVar)$value).invoke("equals").arg((JExpression)$v);
/*     */       } 
/*     */       
/* 722 */       fe.body()._if((JExpression)jInvocation1)._then()._return((JExpression)fe.var());
/*     */       
/* 724 */       JInvocation ex = JExpr._new(cModel.ref(IllegalArgumentException.class));
/*     */ 
/*     */       
/* 727 */       if (baseExposedType.isPrimitive()) {
/* 728 */         jInvocation2 = cModel.ref(String.class).staticInvoke("valueOf").arg((JExpression)$v);
/* 729 */       } else if (baseExposedType == cModel.ref(String.class)) {
/* 730 */         JVar jVar = $v;
/*     */       } else {
/* 732 */         jInvocation2 = $v.invoke("toString");
/*     */       } 
/* 734 */       m.body()._throw((JExpression)ex.arg((JExpression)jInvocation2));
/*     */     
/*     */     }
/*     */     else {
/*     */       
/* 739 */       type.method(1, String.class, "value").body()._return((JExpression)JExpr.invoke("name"));
/*     */ 
/*     */ 
/*     */       
/* 743 */       JMethod m = type.method(17, (JType)type, "fromValue");
/* 744 */       m.body()._return((JExpression)JExpr.invoke("valueOf").arg((JExpression)m.param(String.class, "v")));
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
/*     */   private FieldOutline generateFieldDecl(ClassOutlineImpl cc, CPropertyInfo prop) {
/* 756 */     FieldRenderer fr = prop.realization;
/* 757 */     if (fr == null)
/*     */     {
/* 759 */       fr = this.model.options.getFieldRendererFactory().getDefault();
/*     */     }
/*     */     
/* 762 */     FieldOutline field = fr.generate(cc, prop);
/* 763 */     this.fields.put(prop, field);
/*     */     
/* 765 */     return field;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void generateAdapterIfNecessary(CPropertyInfo prop, JAnnotatable field) {
/* 774 */     CAdapter adapter = prop.getAdapter();
/* 775 */     if (adapter != null) {
/* 776 */       if (adapter.getAdapterIfKnown() == SwaRefAdapterMarker.class) {
/* 777 */         field.annotate(XmlAttachmentRef.class);
/*     */       }
/*     */       else {
/*     */         
/* 781 */         XmlJavaTypeAdapterWriter xjtw = (XmlJavaTypeAdapterWriter)field.annotate2(XmlJavaTypeAdapterWriter.class);
/* 782 */         xjtw.value((JType)((NClass)adapter.adapterType).toType(this, Aspect.EXPOSED));
/*     */       } 
/*     */     }
/*     */     
/* 786 */     switch (prop.id()) {
/*     */       case ID:
/* 788 */         field.annotate(XmlID.class);
/*     */         break;
/*     */       case IDREF:
/* 791 */         field.annotate(XmlIDREF.class);
/*     */         break;
/*     */     } 
/*     */     
/* 795 */     if (prop.getExpectedMimeType() != null) {
/* 796 */       ((XmlMimeTypeWriter)field.annotate2(XmlMimeTypeWriter.class)).value(prop.getExpectedMimeType().toString());
/*     */     }
/*     */   }
/*     */   
/*     */   public final JClass addRuntime(Class clazz) {
/* 801 */     JClass g = this.generatedRuntime.get(clazz);
/* 802 */     if (g == null) {
/*     */       
/* 804 */       JPackage implPkg = getUsedPackages(Aspect.IMPLEMENTATION)[0].subPackage("runtime");
/* 805 */       g = generateStaticClass(clazz, implPkg);
/* 806 */       this.generatedRuntime.put(clazz, g);
/*     */     } 
/* 808 */     return g;
/*     */   }
/*     */   
/*     */   public JClass generateStaticClass(Class src, JPackage out) {
/* 812 */     String shortName = getShortName(src.getName());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 818 */     URL res = src.getResource(shortName + ".java");
/* 819 */     if (res == null) {
/* 820 */       res = src.getResource(shortName + ".java_");
/*     */     }
/* 822 */     if (res == null) {
/* 823 */       throw new InternalError("Unable to load source code of " + src.getName() + " as a resource");
/*     */     }
/*     */     
/* 826 */     JStaticJavaFile sjf = new JStaticJavaFile(out, shortName, res, null);
/* 827 */     out.addResourceFile((JResourceFile)sjf);
/* 828 */     return sjf.getJClass();
/*     */   }
/*     */   
/*     */   private String getShortName(String name) {
/* 832 */     return name.substring(name.lastIndexOf('.') + 1);
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\xjc\generator\bean\BeanGenerator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */