/*     */ package com.sun.xml.internal.rngom.digested;
/*     */
/*     */ import com.sun.xml.internal.rngom.ast.builder.Annotations;
/*     */ import com.sun.xml.internal.rngom.ast.builder.BuildException;
/*     */ import com.sun.xml.internal.rngom.ast.builder.CommentList;
/*     */ import com.sun.xml.internal.rngom.ast.builder.Div;
/*     */ import com.sun.xml.internal.rngom.ast.builder.Grammar;
/*     */ import com.sun.xml.internal.rngom.ast.builder.GrammarSection;
/*     */ import com.sun.xml.internal.rngom.ast.builder.Include;
/*     */ import com.sun.xml.internal.rngom.ast.builder.Scope;
/*     */ import com.sun.xml.internal.rngom.ast.om.Location;
/*     */ import com.sun.xml.internal.rngom.ast.om.ParsedElementAnnotation;
/*     */ import com.sun.xml.internal.rngom.ast.om.ParsedPattern;
/*     */ import com.sun.xml.internal.rngom.ast.util.LocatorImpl;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import org.w3c.dom.Element;
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */ class GrammarBuilderImpl
/*     */   implements Grammar, Div
/*     */ {
/*     */   protected final DGrammarPattern grammar;
/*     */   protected final Scope parent;
/*     */   protected final DSchemaBuilderImpl sb;
/*     */   private List<Element> additionalElementAnnotations;
/*     */
/*     */   public GrammarBuilderImpl(DGrammarPattern p, Scope parent, DSchemaBuilderImpl sb) {
/*  82 */     this.grammar = p;
/*  83 */     this.parent = parent;
/*  84 */     this.sb = sb;
/*     */   }
/*     */
/*     */
/*     */   public ParsedPattern endGrammar(Location loc, Annotations anno) throws BuildException {
/*  89 */     if (anno != null &&
/*  90 */       this.grammar.annotation != null) {
/*  91 */       this.grammar.annotation.contents.addAll((((Annotation)anno).getResult()).contents);
/*     */     }
/*     */
/*  94 */     return this.grammar;
/*     */   }
/*     */
/*     */
/*     */   public void endDiv(Location loc, Annotations anno) throws BuildException {}
/*     */
/*     */   public void define(String name, Combine combine, ParsedPattern pattern, Location loc, Annotations anno) throws BuildException {
/* 101 */     if (name == "\000#start\000") {
/* 102 */       this.grammar.start = (DPattern)pattern;
/*     */     } else {
/*     */
/* 105 */       DDefine d = this.grammar.getOrAdd(name);
/* 106 */       d.setPattern((DPattern)pattern);
/* 107 */       if (anno != null) {
/* 108 */         d.annotation = ((Annotation)anno).getResult();
/*     */       }
/*     */     }
/*     */   }
/*     */
/*     */
/*     */   public void topLevelAnnotation(ParsedElementAnnotation ea) throws BuildException {
/* 115 */     if (this.additionalElementAnnotations == null) {
/* 116 */       this.additionalElementAnnotations = new ArrayList<>();
/*     */     }
/* 118 */     this.additionalElementAnnotations.add(((ElementWrapper)ea).element);
/* 119 */     if (this.grammar.annotation == null) {
/* 120 */       this.grammar.annotation = new DAnnotation();
/*     */     }
/* 122 */     this.grammar.annotation.contents.addAll(this.additionalElementAnnotations);
/*     */   }
/*     */
/*     */
/*     */   public void topLevelComment(CommentList comments) throws BuildException {}
/*     */
/*     */   public Div makeDiv() {
/* 129 */     return this;
/*     */   }
/*     */
/*     */   public Include makeInclude() {
/* 133 */     return new IncludeImpl(this.grammar, this.parent, this.sb);
/*     */   }
/*     */
/*     */   public ParsedPattern makeParentRef(String name, Location loc, Annotations anno) throws BuildException {
/* 137 */     return this.parent.makeRef(name, loc, anno);
/*     */   }
/*     */
/*     */   public ParsedPattern makeRef(String name, Location loc, Annotations anno) throws BuildException {
/* 141 */     return DSchemaBuilderImpl.wrap(new DRefPattern(this.grammar.getOrAdd(name)), (LocatorImpl)loc, (Annotation)anno);
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\xml\internal\rngom\digested\GrammarBuilderImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
