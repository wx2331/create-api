/*     */ package com.sun.tools.internal.xjc.reader.relaxng;
/*     */ 
/*     */ import com.sun.tools.internal.xjc.model.CBuiltinLeafInfo;
/*     */ import com.sun.tools.internal.xjc.model.TypeUse;
/*     */ import com.sun.tools.internal.xjc.model.TypeUseFactory;
/*     */ import com.sun.xml.internal.rngom.digested.DAttributePattern;
/*     */ import com.sun.xml.internal.rngom.digested.DChoicePattern;
/*     */ import com.sun.xml.internal.rngom.digested.DContainerPattern;
/*     */ import com.sun.xml.internal.rngom.digested.DDataPattern;
/*     */ import com.sun.xml.internal.rngom.digested.DElementPattern;
/*     */ import com.sun.xml.internal.rngom.digested.DEmptyPattern;
/*     */ import com.sun.xml.internal.rngom.digested.DGrammarPattern;
/*     */ import com.sun.xml.internal.rngom.digested.DGroupPattern;
/*     */ import com.sun.xml.internal.rngom.digested.DInterleavePattern;
/*     */ import com.sun.xml.internal.rngom.digested.DListPattern;
/*     */ import com.sun.xml.internal.rngom.digested.DMixedPattern;
/*     */ import com.sun.xml.internal.rngom.digested.DNotAllowedPattern;
/*     */ import com.sun.xml.internal.rngom.digested.DOneOrMorePattern;
/*     */ import com.sun.xml.internal.rngom.digested.DOptionalPattern;
/*     */ import com.sun.xml.internal.rngom.digested.DPattern;
/*     */ import com.sun.xml.internal.rngom.digested.DPatternVisitor;
/*     */ import com.sun.xml.internal.rngom.digested.DRefPattern;
/*     */ import com.sun.xml.internal.rngom.digested.DTextPattern;
/*     */ import com.sun.xml.internal.rngom.digested.DValuePattern;
/*     */ import com.sun.xml.internal.rngom.digested.DZeroOrMorePattern;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ final class TypeUseBinder
/*     */   implements DPatternVisitor<TypeUse>
/*     */ {
/*     */   private final RELAXNGCompiler compiler;
/*     */   
/*     */   public TypeUseBinder(RELAXNGCompiler compiler) {
/*  66 */     this.compiler = compiler;
/*     */   }
/*     */ 
/*     */   
/*     */   public TypeUse onGrammar(DGrammarPattern p) {
/*  71 */     return (TypeUse)CBuiltinLeafInfo.STRING;
/*     */   }
/*     */ 
/*     */   
/*     */   public TypeUse onChoice(DChoicePattern p) {
/*  76 */     return (TypeUse)CBuiltinLeafInfo.STRING;
/*     */   }
/*     */   
/*     */   public TypeUse onData(DDataPattern p) {
/*  80 */     return onDataType(p.getDatatypeLibrary(), p.getType());
/*     */   }
/*     */   
/*     */   public TypeUse onValue(DValuePattern p) {
/*  84 */     return onDataType(p.getDatatypeLibrary(), p.getType());
/*     */   }
/*     */   
/*     */   private TypeUse onDataType(String datatypeLibrary, String type) {
/*  88 */     DatatypeLib lib = this.compiler.datatypes.get(datatypeLibrary);
/*  89 */     if (lib != null) {
/*  90 */       TypeUse use = lib.get(type);
/*  91 */       if (use != null) {
/*  92 */         return use;
/*     */       }
/*     */     } 
/*     */     
/*  96 */     return (TypeUse)CBuiltinLeafInfo.STRING;
/*     */   }
/*     */   
/*     */   public TypeUse onInterleave(DInterleavePattern p) {
/* 100 */     return onContainer((DContainerPattern)p);
/*     */   }
/*     */   
/*     */   public TypeUse onGroup(DGroupPattern p) {
/* 104 */     return onContainer((DContainerPattern)p);
/*     */   }
/*     */   
/*     */   private TypeUse onContainer(DContainerPattern p) {
/* 108 */     TypeUse t = null;
/* 109 */     for (DPattern child : p) {
/* 110 */       TypeUse s = (TypeUse)child.accept(this);
/* 111 */       if (t != null && t != s)
/* 112 */         return (TypeUse)CBuiltinLeafInfo.STRING; 
/* 113 */       t = s;
/*     */     } 
/* 115 */     return t;
/*     */   }
/*     */ 
/*     */   
/*     */   public TypeUse onNotAllowed(DNotAllowedPattern p) {
/* 120 */     return error();
/*     */   }
/*     */   
/*     */   public TypeUse onEmpty(DEmptyPattern p) {
/* 124 */     return (TypeUse)CBuiltinLeafInfo.STRING;
/*     */   }
/*     */   
/*     */   public TypeUse onList(DListPattern p) {
/* 128 */     return (TypeUse)p.getChild().accept(this);
/*     */   }
/*     */   
/*     */   public TypeUse onOneOrMore(DOneOrMorePattern p) {
/* 132 */     return TypeUseFactory.makeCollection((TypeUse)p.getChild().accept(this));
/*     */   }
/*     */   
/*     */   public TypeUse onZeroOrMore(DZeroOrMorePattern p) {
/* 136 */     return TypeUseFactory.makeCollection((TypeUse)p.getChild().accept(this));
/*     */   }
/*     */   
/*     */   public TypeUse onOptional(DOptionalPattern p) {
/* 140 */     return (TypeUse)CBuiltinLeafInfo.STRING;
/*     */   }
/*     */ 
/*     */   
/*     */   public TypeUse onRef(DRefPattern p) {
/* 145 */     return (TypeUse)p.getTarget().getPattern().accept(this);
/*     */   }
/*     */   
/*     */   public TypeUse onText(DTextPattern p) {
/* 149 */     return (TypeUse)CBuiltinLeafInfo.STRING;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TypeUse onAttribute(DAttributePattern p) {
/* 158 */     return error();
/*     */   }
/*     */   
/*     */   public TypeUse onElement(DElementPattern p) {
/* 162 */     return error();
/*     */   }
/*     */   
/*     */   public TypeUse onMixed(DMixedPattern p) {
/* 166 */     return error();
/*     */   }
/*     */   
/*     */   private TypeUse error() {
/* 170 */     throw new IllegalStateException();
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\xjc\reader\relaxng\TypeUseBinder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */