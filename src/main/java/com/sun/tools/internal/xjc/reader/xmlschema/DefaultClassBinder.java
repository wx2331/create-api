/*     */ package com.sun.tools.internal.xjc.reader.xmlschema;
/*     */ 
/*     */ import com.sun.codemodel.internal.JJavaName;
/*     */ import com.sun.codemodel.internal.JPackage;
/*     */ import com.sun.istack.internal.NotNull;
/*     */ import com.sun.istack.internal.Nullable;
/*     */ import com.sun.tools.internal.xjc.ErrorReceiver;
/*     */ import com.sun.tools.internal.xjc.model.CClass;
/*     */ import com.sun.tools.internal.xjc.model.CClassInfo;
/*     */ import com.sun.tools.internal.xjc.model.CClassInfoParent;
/*     */ import com.sun.tools.internal.xjc.model.CClassRef;
/*     */ import com.sun.tools.internal.xjc.model.CCustomizations;
/*     */ import com.sun.tools.internal.xjc.model.CElement;
/*     */ import com.sun.tools.internal.xjc.model.CElementInfo;
/*     */ import com.sun.tools.internal.xjc.model.Model;
/*     */ import com.sun.tools.internal.xjc.reader.Ring;
/*     */ import com.sun.tools.internal.xjc.reader.xmlschema.bindinfo.BIClass;
/*     */ import com.sun.tools.internal.xjc.reader.xmlschema.bindinfo.BIGlobalBinding;
/*     */ import com.sun.tools.internal.xjc.reader.xmlschema.bindinfo.BISchemaBinding;
/*     */ import com.sun.tools.internal.xjc.reader.xmlschema.bindinfo.BIXSubstitutable;
/*     */ import com.sun.tools.internal.xjc.reader.xmlschema.bindinfo.BindInfo;
/*     */ import com.sun.tools.internal.xjc.reader.xmlschema.ct.ComplexTypeBindingMode;
/*     */ import com.sun.tools.internal.xjc.reader.xmlschema.ct.ComplexTypeFieldBuilder;
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
/*     */ import com.sun.xml.internal.xsom.XSSchemaSet;
/*     */ import com.sun.xml.internal.xsom.XSSimpleType;
/*     */ import com.sun.xml.internal.xsom.XSType;
/*     */ import com.sun.xml.internal.xsom.XSWildcard;
/*     */ import com.sun.xml.internal.xsom.XSXPath;
/*     */ import java.util.Set;
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
/*     */ final class DefaultClassBinder
/*     */   implements ClassBinder
/*     */ {
/*  83 */   private final SimpleTypeBuilder stb = (SimpleTypeBuilder)Ring.get(SimpleTypeBuilder.class);
/*  84 */   private final Model model = (Model)Ring.get(Model.class);
/*     */   
/*  86 */   protected final BGMBuilder builder = (BGMBuilder)Ring.get(BGMBuilder.class);
/*  87 */   protected final ClassSelector selector = (ClassSelector)Ring.get(ClassSelector.class);
/*     */   
/*  89 */   protected final XSSchemaSet schemas = (XSSchemaSet)Ring.get(XSSchemaSet.class);
/*     */   
/*     */   public CElement attGroupDecl(XSAttGroupDecl decl) {
/*  92 */     return allow((XSComponent)decl, decl.getName());
/*     */   }
/*     */   
/*     */   public CElement attributeDecl(XSAttributeDecl decl) {
/*  96 */     return allow((XSComponent)decl, decl.getName());
/*     */   }
/*     */   
/*     */   public CElement modelGroup(XSModelGroup mgroup) {
/* 100 */     return (CElement)never();
/*     */   }
/*     */   
/*     */   public CElement modelGroupDecl(XSModelGroupDecl decl) {
/* 104 */     return (CElement)never();
/*     */   }
/*     */   public CElement complexType(XSComplexType type) {
/*     */     String className;
/*     */     CClassInfoParent scope;
/* 109 */     CElement ci = allow((XSComponent)type, type.getName());
/* 110 */     if (ci != null) return ci;
/*     */ 
/*     */ 
/*     */     
/* 114 */     BindInfo bi = this.builder.getBindInfo((XSComponent)type);
/*     */     
/* 116 */     if (type.isGlobal()) {
/* 117 */       QName tagName = null;
/* 118 */       String str = deriveName(type);
/* 119 */       Locator loc = type.getLocator();
/*     */       
/* 121 */       if (getGlobalBinding().isSimpleMode()) {
/*     */         
/* 123 */         XSElementDecl referer = getSoleElementReferer((XSType)type);
/* 124 */         if (referer != null && isCollapsable(referer)) {
/*     */ 
/*     */ 
/*     */           
/* 128 */           tagName = BGMBuilder.getName((XSDeclaration)referer);
/* 129 */           str = deriveName((XSDeclaration)referer);
/* 130 */           loc = referer.getLocator();
/*     */         } 
/*     */       } 
/*     */ 
/*     */ 
/*     */       
/* 136 */       JPackage pkg = this.selector.getPackage(type.getTargetNamespace());
/*     */       
/* 138 */       return (CElement)new CClassInfo(this.model, pkg, str, loc, getTypeName(type), tagName, (XSComponent)type, bi.toCustomizationList());
/*     */     } 
/* 140 */     XSElementDecl element = type.getScope();
/*     */     
/* 142 */     if (element.isGlobal() && isCollapsable(element)) {
/* 143 */       if (this.builder.getBindInfo((XSComponent)element).get(BIClass.class) != null)
/*     */       {
/*     */         
/* 146 */         return null;
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 153 */       return (CElement)new CClassInfo(this.model, this.selector.getClassScope(), 
/* 154 */           deriveName((XSDeclaration)element), element.getLocator(), null, 
/* 155 */           BGMBuilder.getName((XSDeclaration)element), (XSComponent)element, bi.toCustomizationList());
/*     */     } 
/*     */ 
/*     */     
/* 159 */     CElement parentType = this.selector.isBound(element, (XSComponent)type);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 165 */     if (parentType != null && parentType instanceof CElementInfo && ((CElementInfo)parentType)
/*     */       
/* 167 */       .hasClass()) {
/*     */       
/* 169 */       CElementInfo cElementInfo = (CElementInfo)parentType;
/* 170 */       className = "Type";
/*     */     }
/*     */     else {
/*     */       
/* 174 */       className = this.builder.getNameConverter().toClassName(element.getName());
/*     */ 
/*     */       
/* 177 */       BISchemaBinding sb = (BISchemaBinding)this.builder.getBindInfo((XSComponent)type.getOwnerSchema()).get(BISchemaBinding.class);
/* 178 */       if (sb != null) className = sb.mangleAnonymousTypeClassName(className); 
/* 179 */       scope = this.selector.getClassScope();
/*     */     } 
/*     */     
/* 182 */     return (CElement)new CClassInfo(this.model, scope, className, type.getLocator(), null, null, (XSComponent)type, bi.toCustomizationList());
/*     */   }
/*     */ 
/*     */   
/*     */   private QName getTypeName(XSComplexType type) {
/* 187 */     if (type.getRedefinedBy() != null) {
/* 188 */       return null;
/*     */     }
/* 190 */     return BGMBuilder.getName((XSDeclaration)type);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean isCollapsable(XSElementDecl decl) {
/* 198 */     XSType type = decl.getType();
/*     */     
/* 200 */     if (!type.isComplexType()) {
/* 201 */       return false;
/*     */     }
/* 203 */     if (decl.getSubstitutables().size() > 1 || decl.getSubstAffiliation() != null)
/*     */     {
/* 205 */       return false;
/*     */     }
/* 207 */     if (decl.isNillable())
/*     */     {
/* 209 */       return false;
/*     */     }
/* 211 */     BIXSubstitutable bixSubstitutable = (BIXSubstitutable)this.builder.getBindInfo((XSComponent)decl).get(BIXSubstitutable.class);
/* 212 */     if (bixSubstitutable != null) {
/*     */ 
/*     */       
/* 215 */       bixSubstitutable.markAsAcknowledged();
/* 216 */       return false;
/*     */     } 
/*     */     
/* 219 */     if (getGlobalBinding().isSimpleMode() && decl.isGlobal()) {
/*     */ 
/*     */       
/* 222 */       XSElementDecl referer = getSoleElementReferer(decl.getType());
/* 223 */       if (referer != null) {
/* 224 */         assert referer == decl;
/* 225 */         return true;
/*     */       } 
/*     */     } 
/*     */     
/* 229 */     if (!type.isLocal() || !type.isComplexType()) {
/* 230 */       return false;
/*     */     }
/* 232 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private XSElementDecl getSoleElementReferer(@NotNull XSType t) {
/* 240 */     Set<XSComponent> referer = this.builder.getReferer(t);
/*     */     
/* 242 */     XSElementDecl sole = null;
/* 243 */     for (XSComponent r : referer) {
/* 244 */       if (r instanceof XSElementDecl) {
/* 245 */         XSElementDecl x = (XSElementDecl)r;
/* 246 */         if (!x.isGlobal()) {
/*     */           continue;
/*     */         }
/*     */         
/* 250 */         if (sole == null) { sole = x; continue; }
/* 251 */          return null;
/*     */       } 
/*     */ 
/*     */       
/* 255 */       return null;
/*     */     } 
/*     */ 
/*     */     
/* 259 */     return sole;
/*     */   }
/*     */   public CElement elementDecl(XSElementDecl decl) {
/*     */     CElementInfo cElementInfo;
/* 263 */     CElement r = allow((XSComponent)decl, decl.getName());
/*     */     
/* 265 */     if (r == null) {
/* 266 */       QName tagName = BGMBuilder.getName((XSDeclaration)decl);
/* 267 */       CCustomizations custs = this.builder.getBindInfo((XSComponent)decl).toCustomizationList();
/*     */       
/* 269 */       if (decl.isGlobal()) {
/* 270 */         if (isCollapsable(decl))
/*     */         {
/*     */           
/* 273 */           return (CElement)this.selector.bindToType(decl.getType().asComplexType(), (XSComponent)decl, true);
/*     */         }
/* 275 */         String className = null;
/* 276 */         if (getGlobalBinding().isGenerateElementClass()) {
/* 277 */           className = deriveName((XSDeclaration)decl);
/*     */         }
/*     */ 
/*     */         
/* 281 */         CElementInfo cei = new CElementInfo(this.model, tagName, this.selector.getClassScope(), className, custs, decl.getLocator());
/* 282 */         this.selector.boundElements.put(decl, cei);
/*     */         
/* 284 */         this.stb.refererStack.push(decl);
/* 285 */         cei.initContentType(this.selector.bindToType(decl.getType(), (XSComponent)decl), decl, decl.getDefaultValue());
/* 286 */         this.stb.refererStack.pop();
/* 287 */         cElementInfo = cei;
/*     */       } 
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 293 */     XSElementDecl top = decl.getSubstAffiliation();
/* 294 */     if (top != null) {
/* 295 */       CElement topci = this.selector.bindToType(top, (XSComponent)decl);
/*     */       
/* 297 */       if (cElementInfo instanceof CClassInfo && topci instanceof CClassInfo)
/* 298 */         ((CClassInfo)cElementInfo).setBaseClass((CClass)topci); 
/* 299 */       if (cElementInfo instanceof CElementInfo && topci instanceof CElementInfo) {
/* 300 */         cElementInfo.setSubstitutionHead((CElementInfo)topci);
/*     */       }
/*     */     } 
/* 303 */     return (CElement)cElementInfo;
/*     */   }
/*     */   public CClassInfo empty(XSContentType ct) {
/* 306 */     return null;
/*     */   }
/*     */   public CClassInfo identityConstraint(XSIdentityConstraint xsIdentityConstraint) {
/* 309 */     return never();
/*     */   }
/*     */   
/*     */   public CClassInfo xpath(XSXPath xsxPath) {
/* 313 */     return never();
/*     */   }
/*     */   
/*     */   public CClassInfo attributeUse(XSAttributeUse use) {
/* 317 */     return never();
/*     */   }
/*     */   
/*     */   public CElement simpleType(XSSimpleType type) {
/* 321 */     CElement c = allow((XSComponent)type, type.getName());
/* 322 */     if (c != null) return c;
/*     */     
/* 324 */     if (getGlobalBinding().isSimpleTypeSubstitution() && type.isGlobal()) {
/* 325 */       return (CElement)new CClassInfo(this.model, this.selector.getClassScope(), 
/* 326 */           deriveName((XSDeclaration)type), type.getLocator(), BGMBuilder.getName((XSDeclaration)type), null, (XSComponent)type, null);
/*     */     }
/*     */     
/* 329 */     return (CElement)never();
/*     */   }
/*     */   
/*     */   public CClassInfo particle(XSParticle particle) {
/* 333 */     return never();
/*     */   }
/*     */   
/*     */   public CClassInfo wildcard(XSWildcard wc) {
/* 337 */     return never();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public CClassInfo annotation(XSAnnotation annon) {
/*     */     assert false;
/* 344 */     return null;
/*     */   }
/*     */   
/*     */   public CClassInfo notation(XSNotation not) {
/*     */     assert false;
/* 349 */     return null;
/*     */   }
/*     */   
/*     */   public CClassInfo facet(XSFacet decl) {
/*     */     assert false;
/* 354 */     return null;
/*     */   }
/*     */   public CClassInfo schema(XSSchema schema) {
/*     */     assert false;
/* 358 */     return null;
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
/*     */   private CClassInfo never() {
/* 389 */     return null;
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
/*     */   private CElement allow(XSComponent component, String defaultBaseName) {
/* 406 */     BIClass decl = null;
/*     */     
/* 408 */     if (component instanceof XSComplexType) {
/* 409 */       XSType complexType = (XSType)component;
/*     */       
/* 411 */       BIClass lastFoundRecursiveBiClass = null;
/*     */       
/* 413 */       if (complexType.getName() != null) {
/* 414 */         while (!this.schemas.getAnyType().equals(complexType)) {
/* 415 */           BindInfo bindInfo1 = this.builder.getBindInfo((XSComponent)complexType);
/* 416 */           BIClass biClass = (BIClass)bindInfo1.get(BIClass.class);
/*     */           
/* 418 */           if (biClass != null && "true".equals(biClass.getRecursive())) {
/* 419 */             lastFoundRecursiveBiClass = biClass;
/*     */           }
/* 421 */           complexType = complexType.getBaseType();
/*     */         } 
/*     */       }
/*     */ 
/*     */       
/* 426 */       decl = lastFoundRecursiveBiClass;
/*     */     } 
/*     */ 
/*     */     
/* 430 */     BindInfo bindInfo = this.builder.getBindInfo(component);
/* 431 */     if (decl == null) {
/* 432 */       decl = (BIClass)bindInfo.get(BIClass.class);
/* 433 */       if (decl == null) return null;
/*     */     
/*     */     } 
/* 436 */     decl.markAsAcknowledged();
/*     */ 
/*     */     
/* 439 */     String ref = decl.getExistingClassRef();
/* 440 */     if (ref != null) {
/* 441 */       if (!JJavaName.isFullyQualifiedClassName(ref)) {
/* 442 */         ((ErrorReceiver)Ring.get(ErrorReceiver.class)).error(decl.getLocation(), 
/* 443 */             Messages.format("ClassSelector.IncorrectClassName", new Object[] { ref }));
/*     */       } else {
/*     */         
/* 446 */         if (component instanceof XSComplexType)
/*     */         {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 455 */           ((ComplexTypeFieldBuilder)Ring.get(ComplexTypeFieldBuilder.class)).recordBindingMode((XSComplexType)component, ComplexTypeBindingMode.NORMAL);
/*     */         }
/*     */ 
/*     */         
/* 459 */         return (CElement)new CClassRef(this.model, component, decl, bindInfo.toCustomizationList());
/*     */       } 
/*     */     }
/*     */     
/* 463 */     String clsName = decl.getClassName();
/* 464 */     if (clsName == null) {
/*     */ 
/*     */       
/* 467 */       if (defaultBaseName == null) {
/* 468 */         ((ErrorReceiver)Ring.get(ErrorReceiver.class)).error(decl.getLocation(), 
/* 469 */             Messages.format("ClassSelector.ClassNameIsRequired", new Object[0]));
/*     */ 
/*     */         
/* 472 */         defaultBaseName = "undefined" + component.hashCode();
/*     */       } 
/* 474 */       clsName = this.builder.deriveName(defaultBaseName, component);
/*     */     }
/* 476 */     else if (!JJavaName.isJavaIdentifier(clsName)) {
/*     */       
/* 478 */       ((ErrorReceiver)Ring.get(ErrorReceiver.class)).error(decl.getLocation(), 
/* 479 */           Messages.format("ClassSelector.IncorrectClassName", new Object[] { clsName }));
/*     */       
/* 481 */       clsName = "Undefined" + component.hashCode();
/*     */     } 
/*     */ 
/*     */     
/* 485 */     QName typeName = null;
/* 486 */     QName elementName = null;
/*     */     
/* 488 */     if (component instanceof XSType) {
/* 489 */       XSType t = (XSType)component;
/* 490 */       typeName = BGMBuilder.getName((XSDeclaration)t);
/*     */     } 
/*     */     
/* 493 */     if (component instanceof XSElementDecl) {
/* 494 */       XSElementDecl e = (XSElementDecl)component;
/* 495 */       elementName = BGMBuilder.getName((XSDeclaration)e);
/*     */     } 
/*     */     
/* 498 */     if (component instanceof XSElementDecl && !isCollapsable((XSElementDecl)component)) {
/* 499 */       XSElementDecl e = (XSElementDecl)component;
/*     */ 
/*     */ 
/*     */       
/* 503 */       CElementInfo cei = new CElementInfo(this.model, elementName, this.selector.getClassScope(), clsName, bindInfo.toCustomizationList(), decl.getLocation());
/* 504 */       this.selector.boundElements.put(e, cei);
/*     */       
/* 506 */       this.stb.refererStack.push(component);
/* 507 */       cei.initContentType(this.selector
/* 508 */           .bindToType(e.getType(), (XSComponent)e), e, e
/* 509 */           .getDefaultValue());
/* 510 */       this.stb.refererStack.pop();
/* 511 */       return (CElement)cei;
/*     */     } 
/*     */ 
/*     */     
/* 515 */     CClassInfo bt = new CClassInfo(this.model, this.selector.getClassScope(), clsName, decl.getLocation(), typeName, elementName, component, bindInfo.toCustomizationList());
/*     */ 
/*     */     
/* 518 */     if (decl.getJavadoc() != null) {
/* 519 */       bt.javadoc = decl.getJavadoc() + "\n\n";
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 525 */     String implClass = decl.getUserSpecifiedImplClass();
/* 526 */     if (implClass != null) {
/* 527 */       bt.setUserSpecifiedImplClass(implClass);
/*     */     }
/* 529 */     return (CElement)bt;
/*     */   }
/*     */ 
/*     */   
/*     */   private BIGlobalBinding getGlobalBinding() {
/* 534 */     return this.builder.getGlobalBinding();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private String deriveName(XSDeclaration comp) {
/* 542 */     return this.builder.deriveName(comp.getName(), (XSComponent)comp);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private String deriveName(XSComplexType comp) {
/* 551 */     String seed = this.builder.deriveName(comp.getName(), (XSComponent)comp);
/* 552 */     int cnt = comp.getRedefinedCount();
/* 553 */     for (; cnt > 0; cnt--)
/* 554 */       seed = "Original" + seed; 
/* 555 */     return seed;
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\xjc\reader\xmlschema\DefaultClassBinder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */