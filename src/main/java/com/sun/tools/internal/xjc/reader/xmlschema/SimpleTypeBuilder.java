/*     */ package com.sun.tools.internal.xjc.reader.xmlschema;
/*     */ 
/*     */ import com.sun.codemodel.internal.JJavaName;
/*     */ import com.sun.codemodel.internal.util.JavadocEscapeWriter;
/*     */ import com.sun.tools.internal.xjc.ErrorReceiver;
/*     */ import com.sun.tools.internal.xjc.model.CBuiltinLeafInfo;
/*     */ import com.sun.tools.internal.xjc.model.CClassInfoParent;
/*     */ import com.sun.tools.internal.xjc.model.CClassRef;
/*     */ import com.sun.tools.internal.xjc.model.CEnumConstant;
/*     */ import com.sun.tools.internal.xjc.model.CEnumLeafInfo;
/*     */ import com.sun.tools.internal.xjc.model.CNonElement;
/*     */ import com.sun.tools.internal.xjc.model.Model;
/*     */ import com.sun.tools.internal.xjc.model.TypeUse;
/*     */ import com.sun.tools.internal.xjc.model.TypeUseFactory;
/*     */ import com.sun.tools.internal.xjc.reader.Ring;
/*     */ import com.sun.tools.internal.xjc.reader.xmlschema.bindinfo.BIConversion;
/*     */ import com.sun.tools.internal.xjc.reader.xmlschema.bindinfo.BIDeclaration;
/*     */ import com.sun.tools.internal.xjc.reader.xmlschema.bindinfo.BIEnum;
/*     */ import com.sun.tools.internal.xjc.reader.xmlschema.bindinfo.BIEnumMember;
/*     */ import com.sun.tools.internal.xjc.reader.xmlschema.bindinfo.BIProperty;
/*     */ import com.sun.tools.internal.xjc.reader.xmlschema.bindinfo.BindInfo;
/*     */ import com.sun.tools.internal.xjc.reader.xmlschema.bindinfo.EnumMemberMode;
/*     */ import com.sun.tools.internal.xjc.util.MimeTypeRange;
/*     */ import com.sun.xml.internal.bind.v2.runtime.SwaRefAdapterMarker;
/*     */ import com.sun.xml.internal.xsom.XSAttributeDecl;
/*     */ import com.sun.xml.internal.xsom.XSComplexType;
/*     */ import com.sun.xml.internal.xsom.XSComponent;
/*     */ import com.sun.xml.internal.xsom.XSDeclaration;
/*     */ import com.sun.xml.internal.xsom.XSElementDecl;
/*     */ import com.sun.xml.internal.xsom.XSFacet;
/*     */ import com.sun.xml.internal.xsom.XSListSimpleType;
/*     */ import com.sun.xml.internal.xsom.XSRestrictionSimpleType;
/*     */ import com.sun.xml.internal.xsom.XSSimpleType;
/*     */ import com.sun.xml.internal.xsom.XSUnionSimpleType;
/*     */ import com.sun.xml.internal.xsom.XSVariety;
/*     */ import com.sun.xml.internal.xsom.impl.util.SchemaWriter;
/*     */ import com.sun.xml.internal.xsom.visitor.XSSimpleTypeFunction;
/*     */ import com.sun.xml.internal.xsom.visitor.XSVisitor;
/*     */ import java.io.StringWriter;
/*     */ import java.io.Writer;
/*     */ import java.math.BigInteger;
/*     */ import java.text.ParseException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.Stack;
/*     */ import javax.activation.MimeTypeParseException;
/*     */ import javax.xml.bind.DatatypeConverter;
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
/*     */ public final class SimpleTypeBuilder
/*     */   extends BindingComponent
/*     */ {
/* 114 */   protected final BGMBuilder builder = (BGMBuilder)Ring.get(BGMBuilder.class);
/*     */   
/* 116 */   private final Model model = (Model)Ring.get(Model.class);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 127 */   public final Stack<XSComponent> refererStack = new Stack<>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 134 */   private final Set<XSComponent> acknowledgedXmimeContentTypes = new HashSet<>();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private XSSimpleType initiatingType;
/*     */ 
/*     */ 
/*     */   
/* 143 */   public static final Map<String, TypeUse> builtinConversions = new HashMap<>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TypeUse build(XSSimpleType type) {
/* 154 */     XSSimpleType oldi = this.initiatingType;
/* 155 */     this.initiatingType = type;
/*     */     
/* 157 */     TypeUse e = checkRefererCustomization(type);
/* 158 */     if (e == null) {
/* 159 */       e = compose(type);
/*     */     }
/* 161 */     this.initiatingType = oldi;
/*     */     
/* 163 */     return e;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TypeUse buildDef(XSSimpleType type) {
/* 172 */     XSSimpleType oldi = this.initiatingType;
/* 173 */     this.initiatingType = type;
/*     */     
/* 175 */     TypeUse e = (TypeUse)type.apply(this.composer);
/*     */     
/* 177 */     this.initiatingType = oldi;
/*     */     
/* 179 */     return e;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private BIConversion getRefererCustomization() {
/* 188 */     BindInfo info = this.builder.getBindInfo(getReferer());
/* 189 */     BIProperty prop = (BIProperty)info.get(BIProperty.class);
/* 190 */     if (prop == null) return null; 
/* 191 */     return prop.getConv();
/*     */   }
/*     */   
/*     */   public XSComponent getReferer() {
/* 195 */     return this.refererStack.peek();
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
/*     */   private TypeUse checkRefererCustomization(XSSimpleType type) {
/* 211 */     XSComponent top = getReferer();
/*     */     
/* 213 */     if (top instanceof XSElementDecl) {
/*     */       
/* 215 */       XSElementDecl eref = (XSElementDecl)top;
/* 216 */       assert eref.getType() == type;
/*     */ 
/*     */ 
/*     */       
/* 220 */       BindInfo info = this.builder.getBindInfo(top);
/* 221 */       BIConversion bIConversion = (BIConversion)info.get(BIConversion.class);
/* 222 */       if (bIConversion != null) {
/* 223 */         bIConversion.markAsAcknowledged();
/*     */         
/* 225 */         return bIConversion.getTypeUse(type);
/*     */       } 
/* 227 */       detectJavaTypeCustomization();
/*     */     }
/* 229 */     else if (top instanceof XSAttributeDecl) {
/* 230 */       XSAttributeDecl aref = (XSAttributeDecl)top;
/* 231 */       assert aref.getType() == type;
/* 232 */       detectJavaTypeCustomization();
/*     */     }
/* 234 */     else if (top instanceof XSComplexType) {
/* 235 */       XSComplexType tref = (XSComplexType)top;
/* 236 */       assert tref.getBaseType() == type || tref.getContentType() == type;
/* 237 */       detectJavaTypeCustomization();
/*     */     }
/* 239 */     else if (top != type) {
/*     */       assert false;
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 248 */     BIConversion conv = getRefererCustomization();
/* 249 */     if (conv != null) {
/* 250 */       conv.markAsAcknowledged();
/*     */       
/* 252 */       return conv.getTypeUse(type);
/*     */     } 
/*     */     
/* 255 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void detectJavaTypeCustomization() {
/* 266 */     BindInfo info = this.builder.getBindInfo(getReferer());
/* 267 */     BIConversion conv = (BIConversion)info.get(BIConversion.class);
/*     */     
/* 269 */     if (conv != null) {
/*     */       
/* 271 */       conv.markAsAcknowledged();
/*     */ 
/*     */       
/* 274 */       getErrorReporter().error(conv.getLocation(), "SimpleTypeBuilder.UnnestedJavaTypeCustomization", new Object[0]);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   TypeUse compose(XSSimpleType t) {
/* 283 */     TypeUse e = find(t);
/* 284 */     if (e != null) return e; 
/* 285 */     return (TypeUse)t.apply(this.composer);
/*     */   }
/*     */   
/* 288 */   public final XSSimpleTypeFunction<TypeUse> composer = new XSSimpleTypeFunction<TypeUse>()
/*     */     {
/*     */ 
/*     */       
/*     */       public TypeUse listSimpleType(XSListSimpleType type)
/*     */       {
/* 294 */         XSSimpleType itemType = type.getItemType();
/* 295 */         SimpleTypeBuilder.this.refererStack.push(itemType);
/* 296 */         TypeUse tu = TypeUseFactory.makeCollection(SimpleTypeBuilder.this.build(type.getItemType()));
/* 297 */         SimpleTypeBuilder.this.refererStack.pop();
/* 298 */         return tu;
/*     */       }
/*     */       public TypeUse unionSimpleType(XSUnionSimpleType type) {
/*     */         TypeUse typeUse;
/* 302 */         boolean isCollection = false;
/* 303 */         for (int i = 0; i < type.getMemberSize(); i++) {
/* 304 */           if (type.getMember(i).getVariety() == XSVariety.LIST || type.getMember(i).getVariety() == XSVariety.UNION) {
/* 305 */             isCollection = true;
/*     */             break;
/*     */           } 
/*     */         } 
/* 309 */         CBuiltinLeafInfo cBuiltinLeafInfo = CBuiltinLeafInfo.STRING;
/* 310 */         if (isCollection)
/* 311 */           typeUse = TypeUseFactory.makeCollection((TypeUse)cBuiltinLeafInfo); 
/* 312 */         return typeUse;
/*     */       }
/*     */ 
/*     */       
/*     */       public TypeUse restrictionSimpleType(XSRestrictionSimpleType type) {
/* 317 */         return SimpleTypeBuilder.this.compose(type.getSimpleBaseType());
/*     */       }
/*     */     };
/*     */ 
/*     */ 
/*     */   
/*     */   private static Set<XSRestrictionSimpleType> reportedEnumMemberSizeWarnings;
/*     */ 
/*     */   
/*     */   private static final Set<String> builtinTypeSafeEnumCapableTypes;
/*     */ 
/*     */ 
/*     */   
/*     */   private TypeUse find(XSSimpleType type) {
/* 331 */     boolean noAutoEnum = false;
/*     */ 
/*     */     
/* 334 */     BindInfo info = this.builder.getBindInfo((XSComponent)type);
/* 335 */     BIConversion conv = (BIConversion)info.get(BIConversion.class);
/*     */     
/* 337 */     if (conv != null) {
/*     */       
/* 339 */       conv.markAsAcknowledged();
/* 340 */       return conv.getTypeUse(type);
/*     */     } 
/*     */ 
/*     */     
/* 344 */     BIEnum en = (BIEnum)info.get(BIEnum.class);
/* 345 */     if (en != null) {
/* 346 */       en.markAsAcknowledged();
/*     */       
/* 348 */       if (!en.isMapped()) {
/* 349 */         noAutoEnum = true;
/*     */       }
/*     */       else {
/*     */         
/* 353 */         if (!canBeMappedToTypeSafeEnum(type)) {
/* 354 */           getErrorReporter().error(en.getLocation(), "ConversionFinder.CannotBeTypeSafeEnum", new Object[0]);
/*     */           
/* 356 */           getErrorReporter().error(type.getLocator(), "ConversionFinder.CannotBeTypeSafeEnum.Location", new Object[0]);
/*     */ 
/*     */           
/* 359 */           return null;
/*     */         } 
/*     */ 
/*     */         
/* 363 */         if (en.ref != null) {
/* 364 */           if (!JJavaName.isFullyQualifiedClassName(en.ref)) {
/* 365 */             ((ErrorReceiver)Ring.get(ErrorReceiver.class)).error(en.getLocation(), 
/* 366 */                 Messages.format("ClassSelector.IncorrectClassName", new Object[] { en.ref }));
/*     */             
/* 368 */             return null;
/*     */           } 
/*     */           
/* 371 */           return (TypeUse)new CClassRef(this.model, (XSComponent)type, en, info.toCustomizationList());
/*     */         } 
/*     */ 
/*     */ 
/*     */         
/* 376 */         return bindToTypeSafeEnum((XSRestrictionSimpleType)type, en.className, en.javadoc, en.members, 
/*     */             
/* 378 */             getEnumMemberMode().getModeWithEnum(), en
/* 379 */             .getLocation());
/*     */       } 
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 385 */     if (type.getTargetNamespace().equals("http://www.w3.org/2001/XMLSchema")) {
/* 386 */       String name = type.getName();
/* 387 */       if (name != null) {
/* 388 */         TypeUse r = lookupBuiltin(name);
/* 389 */         if (r != null) {
/* 390 */           return r;
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 395 */     if (type.getTargetNamespace().equals("http://ws-i.org/profiles/basic/1.1/xsd")) {
/* 396 */       String name = type.getName();
/* 397 */       if (name != null && name.equals("swaRef")) {
/* 398 */         return CBuiltinLeafInfo.STRING.makeAdapted(SwaRefAdapterMarker.class, false);
/*     */       }
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 404 */     if (type.isRestriction() && !noAutoEnum) {
/* 405 */       XSRestrictionSimpleType rst = type.asRestriction();
/* 406 */       if (shouldBeMappedToTypeSafeEnumByDefault(rst)) {
/* 407 */         TypeUse r = bindToTypeSafeEnum(rst, (String)null, (String)null, Collections.emptyMap(), 
/* 408 */             getEnumMemberMode(), (Locator)null);
/* 409 */         if (r != null) {
/* 410 */           return r;
/*     */         }
/*     */       } 
/*     */     } 
/* 414 */     return (TypeUse)getClassSelector()._bindToClass((XSComponent)type, null, false);
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
/*     */   private boolean shouldBeMappedToTypeSafeEnumByDefault(XSRestrictionSimpleType type) {
/* 426 */     if (type.isLocal()) return false;
/*     */ 
/*     */     
/* 429 */     if (type.getRedefinedBy() != null) return false;
/*     */     
/* 431 */     List<XSFacet> facets = type.getDeclaredFacets("enumeration");
/* 432 */     if (facets.isEmpty())
/*     */     {
/*     */       
/* 435 */       return false;
/*     */     }
/* 437 */     if (facets.size() > this.builder.getGlobalBinding().getDefaultEnumMemberSizeCap()) {
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 442 */       if (reportedEnumMemberSizeWarnings == null) {
/* 443 */         reportedEnumMemberSizeWarnings = new HashSet<>();
/*     */       }
/* 445 */       if (!reportedEnumMemberSizeWarnings.contains(type)) {
/* 446 */         getErrorReporter().warning(type.getLocator(), "WARN_ENUM_MEMBER_SIZE_CAP", new Object[] { type
/* 447 */               .getName(), Integer.valueOf(facets.size()), Integer.valueOf(this.builder.getGlobalBinding().getDefaultEnumMemberSizeCap()) });
/*     */         
/* 449 */         reportedEnumMemberSizeWarnings.add(type);
/*     */       } 
/*     */       
/* 452 */       return false;
/*     */     } 
/*     */     
/* 455 */     if (!canBeMappedToTypeSafeEnum((XSSimpleType)type))
/*     */     {
/* 457 */       return false;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 463 */     for (XSRestrictionSimpleType xSRestrictionSimpleType = type; xSRestrictionSimpleType != null; xSSimpleType = xSRestrictionSimpleType.getSimpleBaseType()) {
/* 464 */       XSSimpleType xSSimpleType; if (xSRestrictionSimpleType.isGlobal() && this.builder.getGlobalBinding().canBeMappedToTypeSafeEnum((XSDeclaration)xSRestrictionSimpleType))
/* 465 */         return true; 
/*     */     } 
/* 467 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static {
/* 474 */     Set<String> s = new HashSet<>();
/*     */ 
/*     */     
/* 477 */     String[] typeNames = { "string", "boolean", "float", "decimal", "double", "anyURI" };
/*     */ 
/*     */     
/* 480 */     s.addAll(Arrays.asList(typeNames));
/*     */     
/* 482 */     builtinTypeSafeEnumCapableTypes = Collections.unmodifiableSet(s);
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
/*     */   public static boolean canBeMappedToTypeSafeEnum(XSSimpleType type) {
/*     */     do {
/* 497 */       if ("http://www.w3.org/2001/XMLSchema".equals(type.getTargetNamespace())) {
/*     */         
/* 499 */         String localName = type.getName();
/* 500 */         if (localName != null) {
/* 501 */           if (localName.equals("anySimpleType"))
/* 502 */             return false; 
/* 503 */           if (localName.equals("ID") || localName.equals("IDREF")) {
/* 504 */             return false;
/*     */           }
/*     */           
/* 507 */           if (builtinTypeSafeEnumCapableTypes.contains(localName)) {
/* 508 */             return true;
/*     */           }
/*     */         } 
/*     */       } 
/* 512 */       type = type.getSimpleBaseType();
/* 513 */     } while (type != null);
/*     */     
/* 515 */     return false;
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
/*     */   private TypeUse bindToTypeSafeEnum(XSRestrictionSimpleType type, String className, String javadoc, Map<String, BIEnumMember> members, EnumMemberMode mode, Locator loc) {
/*     */     CClassInfoParent scope;
/* 543 */     if (loc == null) {
/* 544 */       loc = type.getLocator();
/*     */     }
/* 546 */     if (className == null) {
/*     */ 
/*     */       
/* 549 */       if (!type.isGlobal()) {
/* 550 */         getErrorReporter().error(loc, "ConversionFinder.NoEnumNameAvailable", new Object[0]);
/*     */         
/* 552 */         return (TypeUse)CBuiltinLeafInfo.STRING;
/*     */       } 
/* 554 */       className = type.getName();
/*     */     } 
/*     */ 
/*     */     
/* 558 */     className = this.builder.deriveName(className, (XSComponent)type);
/*     */ 
/*     */     
/* 561 */     StringWriter out = new StringWriter();
/* 562 */     SchemaWriter sw = new SchemaWriter((Writer)new JavadocEscapeWriter(out));
/* 563 */     type.visit((XSVisitor)sw);
/*     */     
/* 565 */     if (javadoc != null) { javadoc = javadoc + "\n\n"; }
/* 566 */     else { javadoc = ""; }
/*     */ 
/*     */     
/* 569 */     javadoc = javadoc + Messages.format("ClassSelector.JavadocHeading", new Object[] { type.getName() }) + "\n<p>\n<pre>\n" + out.getBuffer() + "</pre>";
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 574 */     this.refererStack.push(type.getSimpleBaseType());
/* 575 */     TypeUse use = build(type.getSimpleBaseType());
/* 576 */     this.refererStack.pop();
/*     */     
/* 578 */     if (use.isCollection()) {
/* 579 */       return null;
/*     */     }
/* 581 */     CNonElement baseDt = use.getInfo();
/*     */     
/* 583 */     if (baseDt instanceof com.sun.tools.internal.xjc.model.CClassInfo) {
/* 584 */       return null;
/*     */     }
/*     */     
/* 587 */     XSFacet[] errorRef = new XSFacet[1];
/* 588 */     List<CEnumConstant> memberList = buildCEnumConstants(type, false, members, errorRef);
/* 589 */     if (memberList == null || checkMemberNameCollision(memberList) != null) {
/* 590 */       switch (mode) {
/*     */         
/*     */         case SKIP:
/* 593 */           return null;
/*     */         
/*     */         case ERROR:
/* 596 */           if (memberList == null) {
/* 597 */             getErrorReporter().error(errorRef[0].getLocator(), "ERR_CANNOT_GENERATE_ENUM_NAME", new Object[] { errorRef[0]
/*     */                   
/* 599 */                   .getValue() });
/*     */           } else {
/* 601 */             CEnumConstant[] collision = checkMemberNameCollision(memberList);
/* 602 */             getErrorReporter().error(collision[0].getLocator(), "ERR_ENUM_MEMBER_NAME_COLLISION", new Object[] { collision[0]
/*     */                   
/* 604 */                   .getName() });
/* 605 */             getErrorReporter().error(collision[1].getLocator(), "ERR_ENUM_MEMBER_NAME_COLLISION_RELATED", new Object[0]);
/*     */           } 
/*     */           
/* 608 */           return null;
/*     */         
/*     */         case GENERATE:
/* 611 */           memberList = buildCEnumConstants(type, true, members, (XSFacet[])null);
/*     */           break;
/*     */       } 
/*     */     }
/* 615 */     if (memberList.isEmpty()) {
/* 616 */       getErrorReporter().error(loc, "ConversionFinder.NoEnumFacet", new Object[0]);
/* 617 */       return null;
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 622 */     if (type.isGlobal()) {
/* 623 */       CClassInfoParent.Package package_ = new CClassInfoParent.Package(getClassSelector().getPackage(type.getTargetNamespace()));
/*     */     } else {
/* 625 */       scope = getClassSelector().getClassScope();
/*     */     } 
/*     */     
/* 628 */     CEnumLeafInfo xducer = new CEnumLeafInfo(this.model, BGMBuilder.getName((XSDeclaration)type), scope, className, baseDt, memberList, (XSComponent)type, this.builder.getBindInfo((XSComponent)type).toCustomizationList(), loc);
/* 629 */     xducer.javadoc = javadoc;
/*     */     
/* 631 */     BIConversion.Static static_ = new BIConversion.Static(type.getLocator(), (TypeUse)xducer);
/* 632 */     static_.markAsAcknowledged();
/*     */ 
/*     */ 
/*     */     
/* 636 */     this.builder.getOrCreateBindInfo((XSComponent)type).addDecl((BIDeclaration)static_);
/*     */     
/* 638 */     return static_.getTypeUse((XSSimpleType)type);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private List<CEnumConstant> buildCEnumConstants(XSRestrictionSimpleType type, boolean needsToGenerateMemberName, Map<String, BIEnumMember> members, XSFacet[] errorRef) {
/* 649 */     List<CEnumConstant> memberList = new ArrayList<>();
/* 650 */     int idx = 1;
/* 651 */     Set<String> enums = new HashSet<>();
/*     */     
/* 653 */     for (XSFacet facet : type.getDeclaredFacets("enumeration")) {
/* 654 */       String name = null;
/* 655 */       String mdoc = this.builder.getBindInfo((XSComponent)facet).getDocumentation();
/*     */       
/* 657 */       if (!enums.add((facet.getValue()).value)) {
/*     */         continue;
/*     */       }
/* 660 */       if (needsToGenerateMemberName) {
/*     */ 
/*     */         
/* 663 */         name = "VALUE_" + idx++;
/*     */       } else {
/* 665 */         String facetValue = (facet.getValue()).value;
/* 666 */         BIEnumMember mem = members.get(facetValue);
/* 667 */         if (mem == null)
/*     */         {
/* 669 */           mem = (BIEnumMember)this.builder.getBindInfo((XSComponent)facet).get(BIEnumMember.class);
/*     */         }
/* 671 */         if (mem != null) {
/* 672 */           name = mem.name;
/* 673 */           if (mdoc == null) {
/* 674 */             mdoc = mem.javadoc;
/*     */           }
/*     */         } 
/*     */         
/* 678 */         if (name == null) {
/* 679 */           StringBuilder sb = new StringBuilder();
/* 680 */           for (int i = 0; i < facetValue.length(); i++) {
/* 681 */             char ch = facetValue.charAt(i);
/* 682 */             if (Character.isJavaIdentifierPart(ch)) {
/* 683 */               sb.append(ch);
/*     */             } else {
/* 685 */               sb.append('_');
/*     */             } 
/* 687 */           }  name = this.model.getNameConverter().toConstantName(sb.toString());
/*     */         } 
/*     */       } 
/*     */       
/* 691 */       if (!JJavaName.isJavaIdentifier(name)) {
/* 692 */         if (errorRef != null) errorRef[0] = facet; 
/* 693 */         return null;
/*     */       } 
/*     */       
/* 696 */       memberList.add(new CEnumConstant(name, mdoc, (facet.getValue()).value, (XSComponent)facet, this.builder.getBindInfo((XSComponent)facet).toCustomizationList(), facet.getLocator()));
/*     */     } 
/* 698 */     return memberList;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private CEnumConstant[] checkMemberNameCollision(List<CEnumConstant> memberList) {
/* 709 */     Map<String, CEnumConstant> names = new HashMap<>();
/* 710 */     for (CEnumConstant c : memberList) {
/* 711 */       CEnumConstant old = names.put(c.getName(), c);
/* 712 */       if (old != null)
/*     */       {
/* 714 */         return new CEnumConstant[] { old, c }; } 
/*     */     } 
/* 716 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private EnumMemberMode getEnumMemberMode() {
/* 722 */     return this.builder.getGlobalBinding().getEnumMemberMode();
/*     */   }
/*     */   
/*     */   private TypeUse lookupBuiltin(String typeLocalName) {
/* 726 */     if (typeLocalName.equals("integer") || typeLocalName.equals("long")) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 740 */       BigInteger xe = readFacet("maxExclusive", -1);
/* 741 */       BigInteger xi = readFacet("maxInclusive", 0);
/* 742 */       BigInteger max = min(xe, xi);
/*     */       
/* 744 */       if (max != null) {
/* 745 */         BigInteger ne = readFacet("minExclusive", 1);
/* 746 */         BigInteger ni = readFacet("minInclusive", 0);
/* 747 */         BigInteger min = max(ne, ni);
/*     */         
/* 749 */         if (min != null)
/* 750 */           if (min.compareTo(INT_MIN) >= 0 && max.compareTo(INT_MAX) <= 0) {
/* 751 */             typeLocalName = "int";
/*     */           }
/* 753 */           else if (min.compareTo(LONG_MIN) >= 0 && max.compareTo(LONG_MAX) <= 0) {
/* 754 */             typeLocalName = "long";
/*     */           }  
/*     */       } 
/*     */     } else {
/* 758 */       if (typeLocalName.equals("boolean") && isRestrictedTo0And1())
/*     */       {
/* 760 */         return CBuiltinLeafInfo.BOOLEAN_ZERO_OR_ONE;
/*     */       }
/* 762 */       if (typeLocalName.equals("base64Binary")) {
/* 763 */         return lookupBinaryTypeBinding();
/*     */       }
/* 765 */       if (typeLocalName.equals("anySimpleType")) {
/* 766 */         if (getReferer() instanceof XSAttributeDecl || getReferer() instanceof XSSimpleType) {
/* 767 */           return (TypeUse)CBuiltinLeafInfo.STRING;
/*     */         }
/* 769 */         return (TypeUse)CBuiltinLeafInfo.ANYTYPE;
/*     */       } 
/* 771 */     }  return builtinConversions.get(typeLocalName);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private TypeUse lookupBinaryTypeBinding() {
/* 780 */     XSComponent referer = getReferer();
/* 781 */     String emt = referer.getForeignAttribute("http://www.w3.org/2005/05/xmlmime", "expectedContentTypes");
/* 782 */     if (emt != null) {
/* 783 */       this.acknowledgedXmimeContentTypes.add(referer);
/*     */       
/*     */       try {
/* 786 */         List<MimeTypeRange> types = MimeTypeRange.parseRanges(emt);
/* 787 */         MimeTypeRange mt = MimeTypeRange.merge(types);
/*     */ 
/*     */         
/* 790 */         if (mt.majorType.equalsIgnoreCase("image")) {
/* 791 */           return CBuiltinLeafInfo.IMAGE.makeMimeTyped(mt.toMimeType());
/*     */         }
/* 793 */         if ((mt.majorType.equalsIgnoreCase("application") || mt.majorType.equalsIgnoreCase("text")) && 
/* 794 */           isXml(mt.subType)) {
/* 795 */           return CBuiltinLeafInfo.XML_SOURCE.makeMimeTyped(mt.toMimeType());
/*     */         }
/* 797 */         if (mt.majorType.equalsIgnoreCase("text") && mt.subType.equalsIgnoreCase("plain")) {
/* 798 */           return CBuiltinLeafInfo.STRING.makeMimeTyped(mt.toMimeType());
/*     */         }
/*     */         
/* 801 */         return CBuiltinLeafInfo.DATA_HANDLER.makeMimeTyped(mt.toMimeType());
/* 802 */       } catch (ParseException e) {
/* 803 */         getErrorReporter().error(referer.getLocator(), 
/* 804 */             Messages.format("ERR_ILLEGAL_EXPECTED_MIME_TYPE", new Object[] { emt, e.getMessage() }), new Object[0]);
/*     */       }
/* 806 */       catch (MimeTypeParseException e) {
/* 807 */         getErrorReporter().error(referer.getLocator(), 
/* 808 */             Messages.format("ERR_ILLEGAL_EXPECTED_MIME_TYPE", new Object[] { emt, e.getMessage() }), new Object[0]);
/*     */       } 
/*     */     } 
/*     */     
/* 812 */     return (TypeUse)CBuiltinLeafInfo.BASE64_BYTE_ARRAY;
/*     */   }
/*     */   
/*     */   public boolean isAcknowledgedXmimeContentTypes(XSComponent c) {
/* 816 */     return this.acknowledgedXmimeContentTypes.contains(c);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean isXml(String subType) {
/* 823 */     return (subType.equals("xml") || subType.endsWith("+xml"));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean isRestrictedTo0And1() {
/* 832 */     XSFacet pattern = this.initiatingType.getFacet("pattern");
/* 833 */     if (pattern != null) {
/* 834 */       String v = (pattern.getValue()).value;
/* 835 */       if (v.equals("0|1") || v.equals("1|0") || v.equals("\\d"))
/* 836 */         return true; 
/*     */     } 
/* 838 */     XSFacet enumf = this.initiatingType.getFacet("enumeration");
/* 839 */     if (enumf != null) {
/* 840 */       String v = (enumf.getValue()).value;
/* 841 */       if (v.equals("0") || v.equals("1"))
/* 842 */         return true; 
/*     */     } 
/* 844 */     return false;
/*     */   }
/*     */   
/*     */   private BigInteger readFacet(String facetName, int offset) {
/* 848 */     XSFacet me = this.initiatingType.getFacet(facetName);
/* 849 */     if (me == null)
/* 850 */       return null; 
/* 851 */     BigInteger bi = DatatypeConverter.parseInteger((me.getValue()).value);
/* 852 */     if (offset != 0)
/* 853 */       bi = bi.add(BigInteger.valueOf(offset)); 
/* 854 */     return bi;
/*     */   }
/*     */   
/*     */   private BigInteger min(BigInteger a, BigInteger b) {
/* 858 */     if (a == null) return b; 
/* 859 */     if (b == null) return a; 
/* 860 */     return a.min(b);
/*     */   }
/*     */   
/*     */   private BigInteger max(BigInteger a, BigInteger b) {
/* 864 */     if (a == null) return b; 
/* 865 */     if (b == null) return a; 
/* 866 */     return a.max(b);
/*     */   }
/*     */   
/* 869 */   private static final BigInteger LONG_MIN = BigInteger.valueOf(Long.MIN_VALUE);
/* 870 */   private static final BigInteger LONG_MAX = BigInteger.valueOf(Long.MAX_VALUE);
/* 871 */   private static final BigInteger INT_MIN = BigInteger.valueOf(-2147483648L);
/* 872 */   private static final BigInteger INT_MAX = BigInteger.valueOf(2147483647L);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static {
/* 879 */     Map<String, TypeUse> m = builtinConversions;
/*     */ 
/*     */     
/* 882 */     m.put("string", CBuiltinLeafInfo.STRING);
/* 883 */     m.put("anyURI", CBuiltinLeafInfo.STRING);
/* 884 */     m.put("boolean", CBuiltinLeafInfo.BOOLEAN);
/*     */ 
/*     */     
/* 887 */     m.put("hexBinary", CBuiltinLeafInfo.HEXBIN_BYTE_ARRAY);
/* 888 */     m.put("float", CBuiltinLeafInfo.FLOAT);
/* 889 */     m.put("decimal", CBuiltinLeafInfo.BIG_DECIMAL);
/* 890 */     m.put("integer", CBuiltinLeafInfo.BIG_INTEGER);
/* 891 */     m.put("long", CBuiltinLeafInfo.LONG);
/* 892 */     m.put("unsignedInt", CBuiltinLeafInfo.LONG);
/* 893 */     m.put("int", CBuiltinLeafInfo.INT);
/* 894 */     m.put("unsignedShort", CBuiltinLeafInfo.INT);
/* 895 */     m.put("short", CBuiltinLeafInfo.SHORT);
/* 896 */     m.put("unsignedByte", CBuiltinLeafInfo.SHORT);
/* 897 */     m.put("byte", CBuiltinLeafInfo.BYTE);
/* 898 */     m.put("double", CBuiltinLeafInfo.DOUBLE);
/* 899 */     m.put("QName", CBuiltinLeafInfo.QNAME);
/* 900 */     m.put("NOTATION", CBuiltinLeafInfo.QNAME);
/* 901 */     m.put("dateTime", CBuiltinLeafInfo.CALENDAR);
/* 902 */     m.put("date", CBuiltinLeafInfo.CALENDAR);
/* 903 */     m.put("time", CBuiltinLeafInfo.CALENDAR);
/* 904 */     m.put("gYearMonth", CBuiltinLeafInfo.CALENDAR);
/* 905 */     m.put("gYear", CBuiltinLeafInfo.CALENDAR);
/* 906 */     m.put("gMonthDay", CBuiltinLeafInfo.CALENDAR);
/* 907 */     m.put("gDay", CBuiltinLeafInfo.CALENDAR);
/* 908 */     m.put("gMonth", CBuiltinLeafInfo.CALENDAR);
/* 909 */     m.put("duration", CBuiltinLeafInfo.DURATION);
/* 910 */     m.put("token", CBuiltinLeafInfo.TOKEN);
/* 911 */     m.put("normalizedString", CBuiltinLeafInfo.NORMALIZED_STRING);
/* 912 */     m.put("ID", CBuiltinLeafInfo.ID);
/* 913 */     m.put("IDREF", CBuiltinLeafInfo.IDREF);
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\xjc\reader\xmlschema\SimpleTypeBuilder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */