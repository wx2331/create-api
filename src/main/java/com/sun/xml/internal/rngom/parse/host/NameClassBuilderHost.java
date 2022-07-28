/*     */ package com.sun.xml.internal.rngom.parse.host;
/*     */ 
/*     */ import com.sun.xml.internal.rngom.ast.builder.Annotations;
/*     */ import com.sun.xml.internal.rngom.ast.builder.BuildException;
/*     */ import com.sun.xml.internal.rngom.ast.builder.CommentList;
/*     */ import com.sun.xml.internal.rngom.ast.builder.NameClassBuilder;
/*     */ import com.sun.xml.internal.rngom.ast.om.Location;
/*     */ import com.sun.xml.internal.rngom.ast.om.ParsedElementAnnotation;
/*     */ import com.sun.xml.internal.rngom.ast.om.ParsedNameClass;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ final class NameClassBuilderHost
/*     */   extends Base
/*     */   implements NameClassBuilder
/*     */ {
/*     */   final NameClassBuilder lhs;
/*     */   final NameClassBuilder rhs;
/*     */   
/*     */   NameClassBuilderHost(NameClassBuilder lhs, NameClassBuilder rhs) {
/*  69 */     this.lhs = lhs;
/*  70 */     this.rhs = rhs;
/*     */   }
/*     */   
/*     */   public ParsedNameClass annotate(ParsedNameClass _nc, Annotations _anno) throws BuildException {
/*  74 */     ParsedNameClassHost nc = (ParsedNameClassHost)_nc;
/*  75 */     AnnotationsHost anno = cast(_anno);
/*     */     
/*  77 */     return new ParsedNameClassHost(this.lhs
/*  78 */         .annotate(nc.lhs, anno.lhs), this.rhs
/*  79 */         .annotate(nc.rhs, anno.rhs));
/*     */   }
/*     */   
/*     */   public ParsedNameClass annotateAfter(ParsedNameClass _nc, ParsedElementAnnotation _e) throws BuildException {
/*  83 */     ParsedNameClassHost nc = (ParsedNameClassHost)_nc;
/*  84 */     ParsedElementAnnotationHost e = (ParsedElementAnnotationHost)_e;
/*     */     
/*  86 */     return new ParsedNameClassHost(this.lhs
/*  87 */         .annotateAfter(nc.lhs, e.lhs), this.rhs
/*  88 */         .annotateAfter(nc.rhs, e.rhs));
/*     */   }
/*     */   
/*     */   public ParsedNameClass commentAfter(ParsedNameClass _nc, CommentList _comments) throws BuildException {
/*  92 */     ParsedNameClassHost nc = (ParsedNameClassHost)_nc;
/*  93 */     CommentListHost comments = (CommentListHost)_comments;
/*     */     
/*  95 */     return new ParsedNameClassHost(this.lhs
/*  96 */         .commentAfter(nc.lhs, (comments == null) ? null : comments.lhs), this.rhs
/*  97 */         .commentAfter(nc.rhs, (comments == null) ? null : comments.rhs));
/*     */   }
/*     */   
/*     */   public ParsedNameClass makeChoice(List _nameClasses, Location _loc, Annotations _anno) {
/* 101 */     List<ParsedNameClass> lnc = new ArrayList<>();
/* 102 */     List<ParsedNameClass> rnc = new ArrayList<>();
/* 103 */     for (int i = 0; i < _nameClasses.size(); i++) {
/* 104 */       lnc.add(((ParsedNameClassHost)_nameClasses.get(i)).lhs);
/* 105 */       rnc.add(((ParsedNameClassHost)_nameClasses.get(i)).rhs);
/*     */     } 
/* 107 */     LocationHost loc = cast(_loc);
/* 108 */     AnnotationsHost anno = cast(_anno);
/*     */     
/* 110 */     return new ParsedNameClassHost(this.lhs
/* 111 */         .makeChoice(lnc, loc.lhs, anno.lhs), this.rhs
/* 112 */         .makeChoice(rnc, loc.rhs, anno.rhs));
/*     */   }
/*     */   
/*     */   public ParsedNameClass makeName(String ns, String localName, String prefix, Location _loc, Annotations _anno) {
/* 116 */     LocationHost loc = cast(_loc);
/* 117 */     AnnotationsHost anno = cast(_anno);
/*     */     
/* 119 */     return new ParsedNameClassHost(this.lhs
/* 120 */         .makeName(ns, localName, prefix, loc.lhs, anno.lhs), this.rhs
/* 121 */         .makeName(ns, localName, prefix, loc.rhs, anno.rhs));
/*     */   }
/*     */   
/*     */   public ParsedNameClass makeNsName(String ns, Location _loc, Annotations _anno) {
/* 125 */     LocationHost loc = cast(_loc);
/* 126 */     AnnotationsHost anno = cast(_anno);
/*     */     
/* 128 */     return new ParsedNameClassHost(this.lhs
/* 129 */         .makeNsName(ns, loc.lhs, anno.lhs), this.rhs
/* 130 */         .makeNsName(ns, loc.rhs, anno.rhs));
/*     */   }
/*     */   
/*     */   public ParsedNameClass makeNsName(String ns, ParsedNameClass _except, Location _loc, Annotations _anno) {
/* 134 */     ParsedNameClassHost except = (ParsedNameClassHost)_except;
/* 135 */     LocationHost loc = cast(_loc);
/* 136 */     AnnotationsHost anno = cast(_anno);
/*     */     
/* 138 */     return new ParsedNameClassHost(this.lhs
/* 139 */         .makeNsName(ns, except.lhs, loc.lhs, anno.lhs), this.rhs
/* 140 */         .makeNsName(ns, except.rhs, loc.rhs, anno.rhs));
/*     */   }
/*     */   
/*     */   public ParsedNameClass makeAnyName(Location _loc, Annotations _anno) {
/* 144 */     LocationHost loc = cast(_loc);
/* 145 */     AnnotationsHost anno = cast(_anno);
/*     */     
/* 147 */     return new ParsedNameClassHost(this.lhs
/* 148 */         .makeAnyName(loc.lhs, anno.lhs), this.rhs
/* 149 */         .makeAnyName(loc.rhs, anno.rhs));
/*     */   }
/*     */   
/*     */   public ParsedNameClass makeAnyName(ParsedNameClass _except, Location _loc, Annotations _anno) {
/* 153 */     ParsedNameClassHost except = (ParsedNameClassHost)_except;
/* 154 */     LocationHost loc = cast(_loc);
/* 155 */     AnnotationsHost anno = cast(_anno);
/*     */     
/* 157 */     return new ParsedNameClassHost(this.lhs
/* 158 */         .makeAnyName(except.lhs, loc.lhs, anno.lhs), this.rhs
/* 159 */         .makeAnyName(except.rhs, loc.rhs, anno.rhs));
/*     */   }
/*     */   
/*     */   public ParsedNameClass makeErrorNameClass() {
/* 163 */     return new ParsedNameClassHost(this.lhs.makeErrorNameClass(), this.rhs.makeErrorNameClass());
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\xml\internal\rngom\parse\host\NameClassBuilderHost.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */