/*     */ package com.sun.tools.internal.xjc.reader.xmlschema;
/*     */ 
/*     */ import com.sun.tools.internal.xjc.model.CAdapter;
/*     */ import com.sun.tools.internal.xjc.model.CClass;
/*     */ import com.sun.tools.internal.xjc.model.CClassInfo;
/*     */ import com.sun.tools.internal.xjc.model.CClassInfoParent;
/*     */ import com.sun.tools.internal.xjc.model.CCustomizations;
/*     */ import com.sun.tools.internal.xjc.model.CElement;
/*     */ import com.sun.tools.internal.xjc.model.CElementInfo;
/*     */ import com.sun.tools.internal.xjc.model.CElementPropertyInfo;
/*     */ import com.sun.tools.internal.xjc.model.CNonElement;
/*     */ import com.sun.tools.internal.xjc.model.CReferencePropertyInfo;
/*     */ import com.sun.tools.internal.xjc.model.CTypeRef;
/*     */ import com.sun.tools.internal.xjc.model.Model;
/*     */ import com.sun.tools.internal.xjc.model.Multiplicity;
/*     */ import com.sun.tools.internal.xjc.model.TypeUse;
/*     */ import com.sun.tools.internal.xjc.reader.RawTypeSet;
/*     */ import com.sun.tools.internal.xjc.reader.Ring;
/*     */ import com.sun.tools.internal.xjc.reader.xmlschema.bindinfo.BIDom;
/*     */ import com.sun.tools.internal.xjc.reader.xmlschema.bindinfo.BIGlobalBinding;
/*     */ import com.sun.tools.internal.xjc.reader.xmlschema.bindinfo.BIXSubstitutable;
/*     */ import com.sun.xml.internal.bind.v2.model.core.ID;
/*     */ import com.sun.xml.internal.bind.v2.model.core.WildcardMode;
/*     */ import com.sun.xml.internal.xsom.XSComponent;
/*     */ import com.sun.xml.internal.xsom.XSDeclaration;
/*     */ import com.sun.xml.internal.xsom.XSElementDecl;
/*     */ import com.sun.xml.internal.xsom.XSModelGroup;
/*     */ import com.sun.xml.internal.xsom.XSModelGroupDecl;
/*     */ import com.sun.xml.internal.xsom.XSParticle;
/*     */ import com.sun.xml.internal.xsom.XSWildcard;
/*     */ import com.sun.xml.internal.xsom.visitor.XSTermVisitor;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.Set;
/*     */ import javax.activation.MimeType;
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
/*     */ public class RawTypeSetBuilder
/*     */   implements XSTermVisitor
/*     */ {
/*     */   public static RawTypeSet build(XSParticle p, boolean optional) {
/*  73 */     RawTypeSetBuilder rtsb = new RawTypeSetBuilder();
/*  74 */     rtsb.particle(p);
/*  75 */     Multiplicity mul = MultiplicityCounter.theInstance.particle(p);
/*     */     
/*  77 */     if (optional) {
/*  78 */       mul = mul.makeOptional();
/*     */     }
/*  80 */     return new RawTypeSet(rtsb.refs, mul);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  88 */   private final Set<QName> elementNames = new LinkedHashSet<>();
/*     */   
/*  90 */   private final Set<RawTypeSet.Ref> refs = new LinkedHashSet<>();
/*     */   
/*  92 */   protected final BGMBuilder builder = (BGMBuilder)Ring.get(BGMBuilder.class);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Set<RawTypeSet.Ref> getRefs() {
/* 101 */     return this.refs;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void particle(XSParticle p) {
/* 109 */     BIDom dom = this.builder.getLocalDomCustomization(p);
/* 110 */     if (dom != null) {
/* 111 */       dom.markAsAcknowledged();
/* 112 */       this.refs.add(new WildcardRef(WildcardMode.SKIP));
/*     */     } else {
/* 114 */       p.getTerm().visit(this);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void wildcard(XSWildcard wc) {
/* 119 */     this.refs.add(new WildcardRef(wc));
/*     */   }
/*     */   
/*     */   public void modelGroupDecl(XSModelGroupDecl decl) {
/* 123 */     modelGroup(decl.getModelGroup());
/*     */   }
/*     */   
/*     */   public void modelGroup(XSModelGroup group) {
/* 127 */     for (XSParticle p : group.getChildren()) {
/* 128 */       particle(p);
/*     */     }
/*     */   }
/*     */   
/*     */   public void elementDecl(XSElementDecl decl) {
/* 133 */     QName n = BGMBuilder.getName((XSDeclaration)decl);
/* 134 */     if (this.elementNames.add(n)) {
/* 135 */       CElement elementBean = ((ClassSelector)Ring.get(ClassSelector.class)).bindToType(decl, (XSComponent)null);
/* 136 */       if (elementBean == null) {
/* 137 */         this.refs.add(new XmlTypeRef(decl));
/*     */       
/*     */       }
/* 140 */       else if (elementBean instanceof CClass) {
/* 141 */         this.refs.add(new CClassRef(decl, (CClass)elementBean));
/*     */       } else {
/* 143 */         this.refs.add(new CElementInfoRef(decl, (CElementInfo)elementBean));
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static final class WildcardRef
/*     */     extends RawTypeSet.Ref
/*     */   {
/*     */     private final WildcardMode mode;
/*     */     
/*     */     WildcardRef(XSWildcard wildcard) {
/* 155 */       this.mode = getMode(wildcard);
/*     */     }
/*     */     WildcardRef(WildcardMode mode) {
/* 158 */       this.mode = mode;
/*     */     }
/*     */     
/*     */     private static WildcardMode getMode(XSWildcard wildcard) {
/* 162 */       switch (wildcard.getMode()) {
/*     */         case 1:
/* 164 */           return WildcardMode.LAX;
/*     */         case 2:
/* 166 */           return WildcardMode.STRICT;
/*     */         case 3:
/* 168 */           return WildcardMode.SKIP;
/*     */       } 
/* 170 */       throw new IllegalStateException();
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     protected CTypeRef toTypeRef(CElementPropertyInfo ep) {
/* 176 */       throw new IllegalStateException();
/*     */     }
/*     */     
/*     */     protected void toElementRef(CReferencePropertyInfo prop) {
/* 180 */       prop.setWildcard(this.mode);
/*     */     }
/*     */     
/*     */     protected RawTypeSet.Mode canBeType(RawTypeSet parent) {
/* 184 */       return RawTypeSet.Mode.MUST_BE_REFERENCE;
/*     */     }
/*     */     
/*     */     protected boolean isListOfValues() {
/* 188 */       return false;
/*     */     }
/*     */     
/*     */     protected ID id() {
/* 192 */       return ID.NONE;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public static final class CClassRef
/*     */     extends RawTypeSet.Ref
/*     */   {
/*     */     public final CClass target;
/*     */     public final XSElementDecl decl;
/*     */     
/*     */     CClassRef(XSElementDecl decl, CClass target) {
/* 204 */       this.decl = decl;
/* 205 */       this.target = target;
/*     */     }
/*     */     
/*     */     protected CTypeRef toTypeRef(CElementPropertyInfo ep) {
/* 209 */       return new CTypeRef((CNonElement)this.target, this.decl);
/*     */     }
/*     */     
/*     */     protected void toElementRef(CReferencePropertyInfo prop) {
/* 213 */       prop.getElements().add(this.target);
/*     */     }
/*     */ 
/*     */     
/*     */     protected RawTypeSet.Mode canBeType(RawTypeSet parent) {
/* 218 */       if (this.decl.getSubstitutables().size() > 1) {
/* 219 */         return RawTypeSet.Mode.MUST_BE_REFERENCE;
/*     */       }
/* 221 */       return RawTypeSet.Mode.SHOULD_BE_TYPEREF;
/*     */     }
/*     */     
/*     */     protected boolean isListOfValues() {
/* 225 */       return false;
/*     */     }
/*     */     
/*     */     protected ID id() {
/* 229 */       return ID.NONE;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public final class CElementInfoRef
/*     */     extends RawTypeSet.Ref
/*     */   {
/*     */     public final CElementInfo target;
/*     */     public final XSElementDecl decl;
/*     */     
/*     */     CElementInfoRef(XSElementDecl decl, CElementInfo target) {
/* 241 */       this.decl = decl;
/* 242 */       this.target = target;
/*     */     }
/*     */     
/*     */     protected CTypeRef toTypeRef(CElementPropertyInfo ep) {
/* 246 */       assert !this.target.isCollection();
/* 247 */       CAdapter a = this.target.getProperty().getAdapter();
/* 248 */       if (a != null && ep != null) ep.setAdapter(a);
/*     */       
/* 250 */       return new CTypeRef(this.target.getContentType(), this.decl);
/*     */     }
/*     */     
/*     */     protected void toElementRef(CReferencePropertyInfo prop) {
/* 254 */       prop.getElements().add(this.target);
/*     */     }
/*     */ 
/*     */     
/*     */     protected RawTypeSet.Mode canBeType(RawTypeSet parent) {
/* 259 */       if (this.decl.getSubstitutables().size() > 1) {
/* 260 */         return RawTypeSet.Mode.MUST_BE_REFERENCE;
/*     */       }
/* 262 */       BIXSubstitutable subst = (BIXSubstitutable)RawTypeSetBuilder.this.builder.getBindInfo((XSComponent)this.decl).get(BIXSubstitutable.class);
/* 263 */       if (subst != null) {
/* 264 */         subst.markAsAcknowledged();
/* 265 */         return RawTypeSet.Mode.MUST_BE_REFERENCE;
/*     */       } 
/*     */ 
/*     */       
/* 269 */       CElementPropertyInfo p = this.target.getProperty();
/*     */ 
/*     */ 
/*     */       
/* 273 */       if ((parent.refs.size() > 1 || !parent.mul.isAtMostOnce()) && p.id() != ID.NONE)
/* 274 */         return RawTypeSet.Mode.MUST_BE_REFERENCE; 
/* 275 */       if (parent.refs.size() > 1 && p.getAdapter() != null) {
/* 276 */         return RawTypeSet.Mode.MUST_BE_REFERENCE;
/*     */       }
/* 278 */       if (this.target.hasClass())
/*     */       {
/*     */         
/* 281 */         return RawTypeSet.Mode.CAN_BE_TYPEREF;
/*     */       }
/* 283 */       return RawTypeSet.Mode.SHOULD_BE_TYPEREF;
/*     */     }
/*     */     
/*     */     protected boolean isListOfValues() {
/* 287 */       return this.target.getProperty().isValueList();
/*     */     }
/*     */     
/*     */     protected ID id() {
/* 291 */       return this.target.getProperty().id();
/*     */     }
/*     */ 
/*     */     
/*     */     protected MimeType getExpectedMimeType() {
/* 296 */       return this.target.getProperty().getExpectedMimeType();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public static final class XmlTypeRef
/*     */     extends RawTypeSet.Ref
/*     */   {
/*     */     private final XSElementDecl decl;
/*     */     private final TypeUse target;
/*     */     
/*     */     public XmlTypeRef(XSElementDecl decl) {
/* 308 */       this.decl = decl;
/* 309 */       SimpleTypeBuilder stb = (SimpleTypeBuilder)Ring.get(SimpleTypeBuilder.class);
/* 310 */       stb.refererStack.push(decl);
/* 311 */       TypeUse r = ((ClassSelector)Ring.get(ClassSelector.class)).bindToType(decl.getType(), (XSComponent)decl);
/* 312 */       stb.refererStack.pop();
/* 313 */       this.target = r;
/*     */     }
/*     */     
/*     */     protected CTypeRef toTypeRef(CElementPropertyInfo ep) {
/* 317 */       if (ep != null && this.target.getAdapterUse() != null)
/* 318 */         ep.setAdapter(this.target.getAdapterUse()); 
/* 319 */       return new CTypeRef(this.target.getInfo(), this.decl);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     protected void toElementRef(CReferencePropertyInfo prop) {
/* 330 */       CClassInfo scope = ((ClassSelector)Ring.get(ClassSelector.class)).getCurrentBean();
/* 331 */       Model model = (Model)Ring.get(Model.class);
/*     */       
/* 333 */       CCustomizations custs = ((BGMBuilder)Ring.get(BGMBuilder.class)).getBindInfo((XSComponent)this.decl).toCustomizationList();
/*     */       
/* 335 */       if (this.target instanceof CClassInfo && ((BIGlobalBinding)Ring.get(BIGlobalBinding.class)).isSimpleMode()) {
/*     */ 
/*     */         
/* 338 */         CClassInfo bean = new CClassInfo(model, (CClassInfoParent)scope, model.getNameConverter().toClassName(this.decl.getName()), this.decl.getLocator(), null, BGMBuilder.getName((XSDeclaration)this.decl), (XSComponent)this.decl, custs);
/*     */         
/* 340 */         bean.setBaseClass((CClass)this.target);
/* 341 */         prop.getElements().add(bean);
/*     */       } else {
/*     */         
/* 344 */         CElementInfo e = new CElementInfo(model, BGMBuilder.getName((XSDeclaration)this.decl), (CClassInfoParent)scope, this.target, this.decl.getDefaultValue(), this.decl, custs, this.decl.getLocator());
/* 345 */         prop.getElements().add(e);
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     protected RawTypeSet.Mode canBeType(RawTypeSet parent) {
/* 353 */       if ((parent.refs.size() > 1 || !parent.mul.isAtMostOnce()) && this.target.idUse() != ID.NONE)
/* 354 */         return RawTypeSet.Mode.MUST_BE_REFERENCE; 
/* 355 */       if (parent.refs.size() > 1 && this.target.getAdapterUse() != null) {
/* 356 */         return RawTypeSet.Mode.MUST_BE_REFERENCE;
/*     */       }
/*     */ 
/*     */       
/* 360 */       if (this.decl.isNillable() && parent.mul.isOptional()) {
/* 361 */         return RawTypeSet.Mode.CAN_BE_TYPEREF;
/*     */       }
/* 363 */       return RawTypeSet.Mode.SHOULD_BE_TYPEREF;
/*     */     }
/*     */     
/*     */     protected boolean isListOfValues() {
/* 367 */       return this.target.isCollection();
/*     */     }
/*     */     
/*     */     protected ID id() {
/* 371 */       return this.target.idUse();
/*     */     }
/*     */ 
/*     */     
/*     */     protected MimeType getExpectedMimeType() {
/* 376 */       return this.target.getExpectedMimeType();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\xjc\reader\xmlschema\RawTypeSetBuilder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */