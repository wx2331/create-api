/*     */ package com.sun.xml.internal.rngom.parse.host;
/*     */
/*     */ import com.sun.xml.internal.rngom.ast.builder.Annotations;
/*     */ import com.sun.xml.internal.rngom.ast.builder.BuildException;
/*     */ import com.sun.xml.internal.rngom.ast.builder.CommentList;
/*     */ import com.sun.xml.internal.rngom.ast.builder.Div;
/*     */ import com.sun.xml.internal.rngom.ast.builder.GrammarSection;
/*     */ import com.sun.xml.internal.rngom.ast.builder.Include;
/*     */ import com.sun.xml.internal.rngom.ast.om.Location;
/*     */ import com.sun.xml.internal.rngom.ast.om.ParsedElementAnnotation;
/*     */ import com.sun.xml.internal.rngom.ast.om.ParsedPattern;
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */ public class GrammarSectionHost
/*     */   extends Base
/*     */   implements GrammarSection
/*     */ {
/*     */   private final GrammarSection lhs;
/*     */   private final GrammarSection rhs;
/*     */
/*     */   GrammarSectionHost(GrammarSection lhs, GrammarSection rhs) {
/*  68 */     this.lhs = lhs;
/*  69 */     this.rhs = rhs;
/*  70 */     if (lhs == null || rhs == null) {
/*  71 */       throw new IllegalArgumentException();
/*     */     }
/*     */   }
/*     */
/*     */   public void define(String name, Combine combine, ParsedPattern _pattern, Location _loc, Annotations _anno) throws BuildException {
/*  76 */     ParsedPatternHost pattern = (ParsedPatternHost)_pattern;
/*  77 */     LocationHost loc = cast(_loc);
/*  78 */     AnnotationsHost anno = cast(_anno);
/*     */
/*  80 */     this.lhs.define(name, combine, pattern.lhs, loc.lhs, anno.lhs);
/*  81 */     this.rhs.define(name, combine, pattern.rhs, loc.rhs, anno.rhs);
/*     */   }
/*     */
/*     */   public Div makeDiv() {
/*  85 */     return new DivHost(this.lhs.makeDiv(), this.rhs.makeDiv());
/*     */   }
/*     */
/*     */   public Include makeInclude() {
/*  89 */     Include l = this.lhs.makeInclude();
/*  90 */     if (l == null) return null;
/*  91 */     return new IncludeHost(l, this.rhs.makeInclude());
/*     */   }
/*     */
/*     */   public void topLevelAnnotation(ParsedElementAnnotation _ea) throws BuildException {
/*  95 */     ParsedElementAnnotationHost ea = (ParsedElementAnnotationHost)_ea;
/*  96 */     this.lhs.topLevelAnnotation((ea == null) ? null : ea.lhs);
/*  97 */     this.rhs.topLevelAnnotation((ea == null) ? null : ea.rhs);
/*     */   }
/*     */
/*     */   public void topLevelComment(CommentList _comments) throws BuildException {
/* 101 */     CommentListHost comments = (CommentListHost)_comments;
/*     */
/* 103 */     this.lhs.topLevelComment((comments == null) ? null : comments.lhs);
/* 104 */     this.rhs.topLevelComment((comments == null) ? null : comments.rhs);
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\xml\internal\rngom\parse\host\GrammarSectionHost.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
