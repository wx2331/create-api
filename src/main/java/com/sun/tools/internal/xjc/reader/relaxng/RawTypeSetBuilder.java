/*     */ package com.sun.tools.internal.xjc.reader.relaxng;
/*     */ 
/*     */ import com.sun.tools.internal.xjc.model.CClassInfo;
/*     */ import com.sun.tools.internal.xjc.model.CElementPropertyInfo;
/*     */ import com.sun.tools.internal.xjc.model.CNonElement;
/*     */ import com.sun.tools.internal.xjc.model.CReferencePropertyInfo;
/*     */ import com.sun.tools.internal.xjc.model.CTypeInfo;
/*     */ import com.sun.tools.internal.xjc.model.CTypeRef;
/*     */ import com.sun.tools.internal.xjc.model.Multiplicity;
/*     */ import com.sun.tools.internal.xjc.reader.RawTypeSet;
/*     */ import com.sun.xml.internal.bind.v2.model.core.ID;
/*     */ import com.sun.xml.internal.rngom.digested.DAttributePattern;
/*     */ import com.sun.xml.internal.rngom.digested.DElementPattern;
/*     */ import com.sun.xml.internal.rngom.digested.DOneOrMorePattern;
/*     */ import com.sun.xml.internal.rngom.digested.DPattern;
/*     */ import com.sun.xml.internal.rngom.digested.DPatternVisitor;
/*     */ import com.sun.xml.internal.rngom.digested.DPatternWalker;
/*     */ import com.sun.xml.internal.rngom.digested.DZeroOrMorePattern;
/*     */ import java.util.HashSet;
/*     */ import java.util.Set;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class RawTypeSetBuilder
/*     */   extends DPatternWalker
/*     */ {
/*     */   private Multiplicity mul;
/*     */   
/*     */   public static RawTypeSet build(RELAXNGCompiler compiler, DPattern contentModel, Multiplicity mul) {
/*  54 */     RawTypeSetBuilder builder = new RawTypeSetBuilder(compiler, mul);
/*  55 */     contentModel.accept((DPatternVisitor)builder);
/*  56 */     return builder.create();
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
/*  67 */   private final Set<RawTypeSet.Ref> refs = new HashSet<>();
/*     */   
/*     */   private final RELAXNGCompiler compiler;
/*     */   
/*     */   public RawTypeSetBuilder(RELAXNGCompiler compiler, Multiplicity mul) {
/*  72 */     this.mul = mul;
/*  73 */     this.compiler = compiler;
/*     */   }
/*     */   
/*     */   private RawTypeSet create() {
/*  77 */     return new RawTypeSet(this.refs, this.mul);
/*     */   }
/*     */ 
/*     */   
/*     */   public Void onAttribute(DAttributePattern p) {
/*  82 */     return null;
/*     */   }
/*     */   
/*     */   public Void onElement(DElementPattern p) {
/*  86 */     CTypeInfo[] tis = this.compiler.classes.get(p);
/*  87 */     if (tis != null) {
/*  88 */       for (CTypeInfo ti : tis) {
/*  89 */         this.refs.add(new CClassInfoRef((CClassInfo)ti));
/*     */       }
/*     */     } else {
/*     */       assert false;
/*     */     } 
/*  94 */     return null;
/*     */   }
/*     */   
/*     */   public Void onZeroOrMore(DZeroOrMorePattern p) {
/*  98 */     this.mul = this.mul.makeRepeated();
/*  99 */     return super.onZeroOrMore(p);
/*     */   }
/*     */   
/*     */   public Void onOneOrMore(DOneOrMorePattern p) {
/* 103 */     this.mul = this.mul.makeRepeated();
/* 104 */     return super.onOneOrMore(p);
/*     */   }
/*     */   
/*     */   private static final class CClassInfoRef
/*     */     extends RawTypeSet.Ref
/*     */   {
/*     */     private final CClassInfo ci;
/*     */     
/*     */     CClassInfoRef(CClassInfo ci) {
/* 113 */       this.ci = ci;
/* 114 */       assert ci.isElement();
/*     */     }
/*     */     
/*     */     protected ID id() {
/* 118 */       return ID.NONE;
/*     */     }
/*     */     
/*     */     protected boolean isListOfValues() {
/* 122 */       return false;
/*     */     }
/*     */     
/*     */     protected RawTypeSet.Mode canBeType(RawTypeSet parent) {
/* 126 */       return RawTypeSet.Mode.SHOULD_BE_TYPEREF;
/*     */     }
/*     */     
/*     */     protected void toElementRef(CReferencePropertyInfo prop) {
/* 130 */       prop.getElements().add(this.ci);
/*     */     }
/*     */     
/*     */     protected CTypeRef toTypeRef(CElementPropertyInfo ep) {
/* 134 */       return new CTypeRef((CNonElement)this.ci, this.ci.getElementName(), this.ci.getTypeName(), false, null);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\xjc\reader\relaxng\RawTypeSetBuilder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */