/*     */ package com.sun.xml.internal.rngom.parse.host;
/*     */ 
/*     */ import com.sun.xml.internal.rngom.ast.builder.Annotations;
/*     */ import com.sun.xml.internal.rngom.ast.builder.BuildException;
/*     */ import com.sun.xml.internal.rngom.ast.builder.CommentList;
/*     */ import com.sun.xml.internal.rngom.ast.builder.DataPatternBuilder;
/*     */ import com.sun.xml.internal.rngom.ast.builder.ElementAnnotationBuilder;
/*     */ import com.sun.xml.internal.rngom.ast.builder.Grammar;
/*     */ import com.sun.xml.internal.rngom.ast.builder.NameClassBuilder;
/*     */ import com.sun.xml.internal.rngom.ast.builder.SchemaBuilder;
/*     */ import com.sun.xml.internal.rngom.ast.builder.Scope;
/*     */ import com.sun.xml.internal.rngom.ast.om.Location;
/*     */ import com.sun.xml.internal.rngom.ast.om.ParsedElementAnnotation;
/*     */ import com.sun.xml.internal.rngom.ast.om.ParsedNameClass;
/*     */ import com.sun.xml.internal.rngom.ast.om.ParsedPattern;
/*     */ import com.sun.xml.internal.rngom.parse.Context;
/*     */ import com.sun.xml.internal.rngom.parse.IllegalSchemaException;
/*     */ import com.sun.xml.internal.rngom.parse.Parseable;
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
/*     */ public class SchemaBuilderHost
/*     */   extends Base
/*     */   implements SchemaBuilder
/*     */ {
/*     */   final SchemaBuilder lhs;
/*     */   final SchemaBuilder rhs;
/*     */   
/*     */   public SchemaBuilderHost(SchemaBuilder lhs, SchemaBuilder rhs) {
/*  78 */     this.lhs = lhs;
/*  79 */     this.rhs = rhs;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ParsedPattern annotate(ParsedPattern _p, Annotations _anno) throws BuildException {
/*  85 */     ParsedPatternHost p = (ParsedPatternHost)_p;
/*  86 */     AnnotationsHost a = cast(_anno);
/*     */     
/*  88 */     return new ParsedPatternHost(this.lhs
/*  89 */         .annotate(p.lhs, a.lhs), this.rhs
/*  90 */         .annotate(p.lhs, a.lhs));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ParsedPattern annotateAfter(ParsedPattern _p, ParsedElementAnnotation _e) throws BuildException {
/*  96 */     ParsedPatternHost p = (ParsedPatternHost)_p;
/*  97 */     ParsedElementAnnotationHost e = (ParsedElementAnnotationHost)_e;
/*  98 */     return new ParsedPatternHost(this.lhs
/*  99 */         .annotateAfter(p.lhs, e.lhs), this.rhs
/* 100 */         .annotateAfter(p.rhs, e.rhs));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ParsedPattern commentAfter(ParsedPattern _p, CommentList _comments) throws BuildException {
/* 106 */     ParsedPatternHost p = (ParsedPatternHost)_p;
/* 107 */     CommentListHost comments = (CommentListHost)_comments;
/*     */     
/* 109 */     return new ParsedPatternHost(this.lhs
/* 110 */         .commentAfter(p.lhs, (comments == null) ? null : comments.lhs), this.rhs
/* 111 */         .commentAfter(p.rhs, (comments == null) ? null : comments.rhs));
/*     */   }
/*     */   
/*     */   public ParsedPattern expandPattern(ParsedPattern _p) throws BuildException, IllegalSchemaException {
/* 115 */     ParsedPatternHost p = (ParsedPatternHost)_p;
/* 116 */     return new ParsedPatternHost(this.lhs
/* 117 */         .expandPattern(p.lhs), this.rhs
/* 118 */         .expandPattern(p.rhs));
/*     */   }
/*     */   
/*     */   public NameClassBuilder getNameClassBuilder() throws BuildException {
/* 122 */     return new NameClassBuilderHost(this.lhs.getNameClassBuilder(), this.rhs.getNameClassBuilder());
/*     */   }
/*     */   
/*     */   public Annotations makeAnnotations(CommentList _comments, Context context) {
/* 126 */     CommentListHost comments = (CommentListHost)_comments;
/* 127 */     Annotations l = this.lhs.makeAnnotations((comments != null) ? comments.lhs : null, context);
/* 128 */     Annotations r = this.rhs.makeAnnotations((comments != null) ? comments.rhs : null, context);
/* 129 */     if (l == null || r == null)
/* 130 */       throw new IllegalArgumentException("annotations cannot be null"); 
/* 131 */     return new AnnotationsHost(l, r);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ParsedPattern makeAttribute(ParsedNameClass _nc, ParsedPattern _p, Location _loc, Annotations _anno) throws BuildException {
/* 137 */     ParsedNameClassHost nc = (ParsedNameClassHost)_nc;
/* 138 */     ParsedPatternHost p = (ParsedPatternHost)_p;
/* 139 */     LocationHost loc = cast(_loc);
/* 140 */     AnnotationsHost anno = cast(_anno);
/*     */     
/* 142 */     return new ParsedPatternHost(this.lhs
/* 143 */         .makeAttribute(nc.lhs, p.lhs, loc.lhs, anno.lhs), this.rhs
/* 144 */         .makeAttribute(nc.rhs, p.rhs, loc.rhs, anno.rhs));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ParsedPattern makeChoice(List patterns, Location _loc, Annotations _anno) throws BuildException {
/* 150 */     List<ParsedPattern> lp = new ArrayList<>();
/* 151 */     List<ParsedPattern> rp = new ArrayList<>();
/* 152 */     for (int i = 0; i < patterns.size(); i++) {
/* 153 */       lp.add(((ParsedPatternHost)patterns.get(i)).lhs);
/* 154 */       rp.add(((ParsedPatternHost)patterns.get(i)).rhs);
/*     */     } 
/* 156 */     LocationHost loc = cast(_loc);
/* 157 */     AnnotationsHost anno = cast(_anno);
/*     */     
/* 159 */     return new ParsedPatternHost(this.lhs
/* 160 */         .makeChoice(lp, loc.lhs, anno.lhs), this.rhs
/* 161 */         .makeChoice(rp, loc.rhs, anno.rhs));
/*     */   }
/*     */   
/*     */   public CommentList makeCommentList() {
/* 165 */     return new CommentListHost(this.lhs
/* 166 */         .makeCommentList(), this.rhs
/* 167 */         .makeCommentList());
/*     */   }
/*     */ 
/*     */   
/*     */   public DataPatternBuilder makeDataPatternBuilder(String datatypeLibrary, String type, Location _loc) throws BuildException {
/* 172 */     LocationHost loc = cast(_loc);
/*     */     
/* 174 */     return new DataPatternBuilderHost(this.lhs
/* 175 */         .makeDataPatternBuilder(datatypeLibrary, type, loc.lhs), this.rhs
/* 176 */         .makeDataPatternBuilder(datatypeLibrary, type, loc.rhs));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ParsedPattern makeElement(ParsedNameClass _nc, ParsedPattern _p, Location _loc, Annotations _anno) throws BuildException {
/* 182 */     ParsedNameClassHost nc = (ParsedNameClassHost)_nc;
/* 183 */     ParsedPatternHost p = (ParsedPatternHost)_p;
/* 184 */     LocationHost loc = cast(_loc);
/* 185 */     AnnotationsHost anno = cast(_anno);
/*     */     
/* 187 */     return new ParsedPatternHost(this.lhs
/* 188 */         .makeElement(nc.lhs, p.lhs, loc.lhs, anno.lhs), this.rhs
/* 189 */         .makeElement(nc.rhs, p.rhs, loc.rhs, anno.rhs));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ElementAnnotationBuilder makeElementAnnotationBuilder(String ns, String localName, String prefix, Location _loc, CommentList _comments, Context context) {
/* 195 */     LocationHost loc = cast(_loc);
/* 196 */     CommentListHost comments = (CommentListHost)_comments;
/*     */     
/* 198 */     return new ElementAnnotationBuilderHost(this.lhs
/* 199 */         .makeElementAnnotationBuilder(ns, localName, prefix, loc.lhs, (comments == null) ? null : comments.lhs, context), this.rhs
/* 200 */         .makeElementAnnotationBuilder(ns, localName, prefix, loc.rhs, (comments == null) ? null : comments.rhs, context));
/*     */   }
/*     */   
/*     */   public ParsedPattern makeEmpty(Location _loc, Annotations _anno) {
/* 204 */     LocationHost loc = cast(_loc);
/* 205 */     AnnotationsHost anno = cast(_anno);
/*     */     
/* 207 */     return new ParsedPatternHost(this.lhs
/* 208 */         .makeEmpty(loc.lhs, anno.lhs), this.rhs
/* 209 */         .makeEmpty(loc.rhs, anno.rhs));
/*     */   }
/*     */   
/*     */   public ParsedPattern makeErrorPattern() {
/* 213 */     return new ParsedPatternHost(this.lhs
/* 214 */         .makeErrorPattern(), this.rhs
/* 215 */         .makeErrorPattern());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ParsedPattern makeExternalRef(Parseable current, String uri, String ns, Scope _scope, Location _loc, Annotations _anno) throws BuildException, IllegalSchemaException {
/* 222 */     ScopeHost scope = (ScopeHost)_scope;
/* 223 */     LocationHost loc = cast(_loc);
/* 224 */     AnnotationsHost anno = cast(_anno);
/*     */     
/* 226 */     return new ParsedPatternHost(this.lhs
/* 227 */         .makeExternalRef(current, uri, ns, scope.lhs, loc.lhs, anno.lhs), this.rhs
/* 228 */         .makeExternalRef(current, uri, ns, scope.rhs, loc.rhs, anno.rhs));
/*     */   }
/*     */   
/*     */   public Grammar makeGrammar(Scope _parent) {
/* 232 */     ScopeHost parent = (ScopeHost)_parent;
/*     */     
/* 234 */     return new GrammarHost(this.lhs
/* 235 */         .makeGrammar((parent != null) ? parent.lhs : null), this.rhs
/* 236 */         .makeGrammar((parent != null) ? parent.rhs : null));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ParsedPattern makeGroup(List patterns, Location _loc, Annotations _anno) throws BuildException {
/* 242 */     List<ParsedPattern> lp = new ArrayList<>();
/* 243 */     List<ParsedPattern> rp = new ArrayList<>();
/* 244 */     for (int i = 0; i < patterns.size(); i++) {
/* 245 */       lp.add(((ParsedPatternHost)patterns.get(i)).lhs);
/* 246 */       rp.add(((ParsedPatternHost)patterns.get(i)).rhs);
/*     */     } 
/* 248 */     LocationHost loc = cast(_loc);
/* 249 */     AnnotationsHost anno = cast(_anno);
/*     */     
/* 251 */     return new ParsedPatternHost(this.lhs
/* 252 */         .makeGroup(lp, loc.lhs, anno.lhs), this.rhs
/* 253 */         .makeGroup(rp, loc.rhs, anno.rhs));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ParsedPattern makeInterleave(List patterns, Location _loc, Annotations _anno) throws BuildException {
/* 259 */     List<ParsedPattern> lp = new ArrayList<>();
/* 260 */     List<ParsedPattern> rp = new ArrayList<>();
/* 261 */     for (int i = 0; i < patterns.size(); i++) {
/* 262 */       lp.add(((ParsedPatternHost)patterns.get(i)).lhs);
/* 263 */       rp.add(((ParsedPatternHost)patterns.get(i)).rhs);
/*     */     } 
/* 265 */     LocationHost loc = cast(_loc);
/* 266 */     AnnotationsHost anno = cast(_anno);
/*     */     
/* 268 */     return new ParsedPatternHost(this.lhs
/* 269 */         .makeInterleave(lp, loc.lhs, anno.lhs), this.rhs
/* 270 */         .makeInterleave(rp, loc.rhs, anno.rhs));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ParsedPattern makeList(ParsedPattern _p, Location _loc, Annotations _anno) throws BuildException {
/* 276 */     ParsedPatternHost p = (ParsedPatternHost)_p;
/* 277 */     LocationHost loc = cast(_loc);
/* 278 */     AnnotationsHost anno = cast(_anno);
/*     */     
/* 280 */     return new ParsedPatternHost(this.lhs
/* 281 */         .makeList(p.lhs, loc.lhs, anno.lhs), this.rhs
/* 282 */         .makeList(p.rhs, loc.rhs, anno.rhs));
/*     */   }
/*     */ 
/*     */   
/*     */   public Location makeLocation(String systemId, int lineNumber, int columnNumber) {
/* 287 */     return new LocationHost(this.lhs
/* 288 */         .makeLocation(systemId, lineNumber, columnNumber), this.rhs
/* 289 */         .makeLocation(systemId, lineNumber, columnNumber));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ParsedPattern makeMixed(ParsedPattern _p, Location _loc, Annotations _anno) throws BuildException {
/* 295 */     ParsedPatternHost p = (ParsedPatternHost)_p;
/* 296 */     LocationHost loc = cast(_loc);
/* 297 */     AnnotationsHost anno = cast(_anno);
/*     */     
/* 299 */     return new ParsedPatternHost(this.lhs
/* 300 */         .makeMixed(p.lhs, loc.lhs, anno.lhs), this.rhs
/* 301 */         .makeMixed(p.rhs, loc.rhs, anno.rhs));
/*     */   }
/*     */   
/*     */   public ParsedPattern makeNotAllowed(Location _loc, Annotations _anno) {
/* 305 */     LocationHost loc = cast(_loc);
/* 306 */     AnnotationsHost anno = cast(_anno);
/*     */     
/* 308 */     return new ParsedPatternHost(this.lhs
/* 309 */         .makeNotAllowed(loc.lhs, anno.lhs), this.rhs
/* 310 */         .makeNotAllowed(loc.rhs, anno.rhs));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ParsedPattern makeOneOrMore(ParsedPattern _p, Location _loc, Annotations _anno) throws BuildException {
/* 316 */     ParsedPatternHost p = (ParsedPatternHost)_p;
/* 317 */     LocationHost loc = cast(_loc);
/* 318 */     AnnotationsHost anno = cast(_anno);
/*     */     
/* 320 */     return new ParsedPatternHost(this.lhs
/* 321 */         .makeOneOrMore(p.lhs, loc.lhs, anno.lhs), this.rhs
/* 322 */         .makeOneOrMore(p.rhs, loc.rhs, anno.rhs));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ParsedPattern makeZeroOrMore(ParsedPattern _p, Location _loc, Annotations _anno) throws BuildException {
/* 328 */     ParsedPatternHost p = (ParsedPatternHost)_p;
/* 329 */     LocationHost loc = cast(_loc);
/* 330 */     AnnotationsHost anno = cast(_anno);
/*     */     
/* 332 */     return new ParsedPatternHost(this.lhs
/* 333 */         .makeZeroOrMore(p.lhs, loc.lhs, anno.lhs), this.rhs
/* 334 */         .makeZeroOrMore(p.rhs, loc.rhs, anno.rhs));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ParsedPattern makeOptional(ParsedPattern _p, Location _loc, Annotations _anno) throws BuildException {
/* 340 */     ParsedPatternHost p = (ParsedPatternHost)_p;
/* 341 */     LocationHost loc = cast(_loc);
/* 342 */     AnnotationsHost anno = cast(_anno);
/*     */     
/* 344 */     return new ParsedPatternHost(this.lhs
/* 345 */         .makeOptional(p.lhs, loc.lhs, anno.lhs), this.rhs
/* 346 */         .makeOptional(p.rhs, loc.rhs, anno.rhs));
/*     */   }
/*     */   
/*     */   public ParsedPattern makeText(Location _loc, Annotations _anno) {
/* 350 */     LocationHost loc = cast(_loc);
/* 351 */     AnnotationsHost anno = cast(_anno);
/*     */     
/* 353 */     return new ParsedPatternHost(this.lhs
/* 354 */         .makeText(loc.lhs, anno.lhs), this.rhs
/* 355 */         .makeText(loc.rhs, anno.rhs));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ParsedPattern makeValue(String datatypeLibrary, String type, String value, Context c, String ns, Location _loc, Annotations _anno) throws BuildException {
/* 361 */     LocationHost loc = cast(_loc);
/* 362 */     AnnotationsHost anno = cast(_anno);
/*     */     
/* 364 */     return new ParsedPatternHost(this.lhs
/* 365 */         .makeValue(datatypeLibrary, type, value, c, ns, loc.lhs, anno.lhs), this.rhs
/* 366 */         .makeValue(datatypeLibrary, type, value, c, ns, loc.rhs, anno.rhs));
/*     */   }
/*     */   
/*     */   public boolean usesComments() {
/* 370 */     return (this.lhs.usesComments() || this.rhs.usesComments());
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\xml\internal\rngom\parse\host\SchemaBuilderHost.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */