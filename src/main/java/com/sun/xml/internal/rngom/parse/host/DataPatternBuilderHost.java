/*     */ package com.sun.xml.internal.rngom.parse.host;
/*     */ 
/*     */ import com.sun.xml.internal.rngom.ast.builder.Annotations;
/*     */ import com.sun.xml.internal.rngom.ast.builder.BuildException;
/*     */ import com.sun.xml.internal.rngom.ast.builder.DataPatternBuilder;
/*     */ import com.sun.xml.internal.rngom.ast.om.Location;
/*     */ import com.sun.xml.internal.rngom.ast.om.ParsedElementAnnotation;
/*     */ import com.sun.xml.internal.rngom.ast.om.ParsedPattern;
/*     */ import com.sun.xml.internal.rngom.parse.Context;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ final class DataPatternBuilderHost
/*     */   extends Base
/*     */   implements DataPatternBuilder
/*     */ {
/*     */   final DataPatternBuilder lhs;
/*     */   final DataPatternBuilder rhs;
/*     */   
/*     */   DataPatternBuilderHost(DataPatternBuilder lhs, DataPatternBuilder rhs) {
/*  66 */     this.lhs = lhs;
/*  67 */     this.rhs = rhs;
/*     */   }
/*     */   
/*     */   public void addParam(String name, String value, Context context, String ns, Location _loc, Annotations _anno) throws BuildException {
/*  71 */     LocationHost loc = cast(_loc);
/*  72 */     AnnotationsHost anno = cast(_anno);
/*     */     
/*  74 */     this.lhs.addParam(name, value, context, ns, loc.lhs, anno.lhs);
/*  75 */     this.rhs.addParam(name, value, context, ns, loc.rhs, anno.rhs);
/*     */   }
/*     */   
/*     */   public void annotation(ParsedElementAnnotation _ea) {
/*  79 */     ParsedElementAnnotationHost ea = (ParsedElementAnnotationHost)_ea;
/*     */     
/*  81 */     this.lhs.annotation(ea.lhs);
/*  82 */     this.rhs.annotation(ea.rhs);
/*     */   }
/*     */   
/*     */   public ParsedPattern makePattern(Location _loc, Annotations _anno) throws BuildException {
/*  86 */     LocationHost loc = cast(_loc);
/*  87 */     AnnotationsHost anno = cast(_anno);
/*     */     
/*  89 */     return new ParsedPatternHost(this.lhs
/*  90 */         .makePattern(loc.lhs, anno.lhs), this.rhs
/*  91 */         .makePattern(loc.rhs, anno.rhs));
/*     */   }
/*     */   
/*     */   public ParsedPattern makePattern(ParsedPattern _except, Location _loc, Annotations _anno) throws BuildException {
/*  95 */     ParsedPatternHost except = (ParsedPatternHost)_except;
/*  96 */     LocationHost loc = cast(_loc);
/*  97 */     AnnotationsHost anno = cast(_anno);
/*     */     
/*  99 */     return new ParsedPatternHost(this.lhs
/* 100 */         .makePattern(except.lhs, loc.lhs, anno.lhs), this.rhs
/* 101 */         .makePattern(except.rhs, loc.rhs, anno.rhs));
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\xml\internal\rngom\parse\host\DataPatternBuilderHost.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */