/*     */ package com.sun.tools.internal.xjc.reader.relaxng;
/*     */ 
/*     */ import com.sun.tools.internal.xjc.model.CAttributePropertyInfo;
/*     */ import com.sun.tools.internal.xjc.model.CClassInfo;
/*     */ import com.sun.tools.internal.xjc.model.CElementPropertyInfo;
/*     */ import com.sun.tools.internal.xjc.model.CPropertyInfo;
/*     */ import com.sun.tools.internal.xjc.model.CReferencePropertyInfo;
/*     */ import com.sun.tools.internal.xjc.model.Multiplicity;
/*     */ import com.sun.tools.internal.xjc.model.TypeUse;
/*     */ import com.sun.tools.internal.xjc.reader.RawTypeSet;
/*     */ import com.sun.xml.internal.bind.v2.model.core.ID;
/*     */ import com.sun.xml.internal.rngom.digested.DAttributePattern;
/*     */ import com.sun.xml.internal.rngom.digested.DChoicePattern;
/*     */ import com.sun.xml.internal.rngom.digested.DMixedPattern;
/*     */ import com.sun.xml.internal.rngom.digested.DOneOrMorePattern;
/*     */ import com.sun.xml.internal.rngom.digested.DOptionalPattern;
/*     */ import com.sun.xml.internal.rngom.digested.DPattern;
/*     */ import com.sun.xml.internal.rngom.digested.DPatternWalker;
/*     */ import com.sun.xml.internal.rngom.digested.DZeroOrMorePattern;
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
/*     */ final class ContentModelBinder
/*     */   extends DPatternWalker
/*     */ {
/*     */   private final RELAXNGCompiler compiler;
/*     */   private final CClassInfo clazz;
/*     */   private boolean insideOptional = false;
/*  60 */   private int iota = 1;
/*     */   
/*     */   public ContentModelBinder(RELAXNGCompiler compiler, CClassInfo clazz) {
/*  63 */     this.compiler = compiler;
/*  64 */     this.clazz = clazz;
/*     */   }
/*     */   
/*     */   public Void onMixed(DMixedPattern p) {
/*  68 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */   public Void onChoice(DChoicePattern p) {
/*  72 */     boolean old = this.insideOptional;
/*  73 */     this.insideOptional = true;
/*  74 */     super.onChoice(p);
/*  75 */     this.insideOptional = old;
/*  76 */     return null;
/*     */   }
/*     */   
/*     */   public Void onOptional(DOptionalPattern p) {
/*  80 */     boolean old = this.insideOptional;
/*  81 */     this.insideOptional = true;
/*  82 */     super.onOptional(p);
/*  83 */     this.insideOptional = old;
/*  84 */     return null;
/*     */   }
/*     */   
/*     */   public Void onZeroOrMore(DZeroOrMorePattern p) {
/*  88 */     return onRepeated((DPattern)p, true);
/*     */   }
/*     */   
/*     */   public Void onOneOrMore(DOneOrMorePattern p) {
/*  92 */     return onRepeated((DPattern)p, this.insideOptional);
/*     */   }
/*     */ 
/*     */   
/*     */   private Void onRepeated(DPattern p, boolean optional) {
/*  97 */     RawTypeSet rts = RawTypeSetBuilder.build(this.compiler, p, optional ? Multiplicity.STAR : Multiplicity.PLUS);
/*  98 */     if (rts.canBeTypeRefs == RawTypeSet.Mode.SHOULD_BE_TYPEREF) {
/*     */       
/* 100 */       CElementPropertyInfo prop = new CElementPropertyInfo(calcName(p), CElementPropertyInfo.CollectionMode.REPEATED_ELEMENT, ID.NONE, null, null, null, p.getLocation(), !optional);
/* 101 */       rts.addTo(prop);
/* 102 */       this.clazz.addProperty((CPropertyInfo)prop);
/*     */     } else {
/*     */       
/* 105 */       CReferencePropertyInfo prop = new CReferencePropertyInfo(calcName(p), true, !optional, false, null, null, p.getLocation(), false, false, false);
/* 106 */       rts.addTo(prop);
/* 107 */       this.clazz.addProperty((CPropertyInfo)prop);
/*     */     } 
/*     */     
/* 110 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public Void onAttribute(DAttributePattern p) {
/* 115 */     QName name = p.getName().listNames().iterator().next();
/*     */ 
/*     */ 
/*     */     
/* 119 */     CAttributePropertyInfo ap = new CAttributePropertyInfo(calcName((DPattern)p), null, null, p.getLocation(), name, (TypeUse)p.getChild().accept(this.compiler.typeUseBinder), null, !this.insideOptional);
/*     */     
/* 121 */     this.clazz.addProperty((CPropertyInfo)ap);
/*     */     
/* 123 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   private String calcName(DPattern p) {
/* 128 */     return "field" + this.iota++;
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\xjc\reader\relaxng\ContentModelBinder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */