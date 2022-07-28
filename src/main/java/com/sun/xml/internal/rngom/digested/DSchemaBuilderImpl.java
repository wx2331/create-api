/*     */ package com.sun.xml.internal.rngom.digested;
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
/*     */ import com.sun.xml.internal.rngom.ast.util.LocatorImpl;
/*     */ import com.sun.xml.internal.rngom.nc.NameClass;
/*     */ import com.sun.xml.internal.rngom.nc.NameClassBuilderImpl;
/*     */ import com.sun.xml.internal.rngom.parse.Context;
/*     */ import com.sun.xml.internal.rngom.parse.IllegalSchemaException;
/*     */ import com.sun.xml.internal.rngom.parse.Parseable;
/*     */ import java.util.List;
/*     */ import javax.xml.parsers.DocumentBuilderFactory;
/*     */ import javax.xml.parsers.ParserConfigurationException;
/*     */ import org.w3c.dom.Document;
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
/*     */ public class DSchemaBuilderImpl
/*     */   implements SchemaBuilder<NameClass, DPattern, ElementWrapper, LocatorImpl, Annotation, CommentListImpl>
/*     */ {
/*  76 */   private final NameClassBuilder ncb = (NameClassBuilder)new NameClassBuilderImpl();
/*     */ 
/*     */   
/*     */   private final Document dom;
/*     */ 
/*     */ 
/*     */   
/*     */   public DSchemaBuilderImpl() {
/*     */     try {
/*  85 */       DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
/*  86 */       dbf.setNamespaceAware(true);
/*  87 */       this.dom = dbf.newDocumentBuilder().newDocument();
/*  88 */     } catch (ParserConfigurationException e) {
/*     */       
/*  90 */       throw new InternalError(e.getMessage());
/*     */     } 
/*     */   }
/*     */   
/*     */   public NameClassBuilder getNameClassBuilder() throws BuildException {
/*  95 */     return this.ncb;
/*     */   }
/*     */   
/*     */   static DPattern wrap(DPattern p, LocatorImpl loc, Annotation anno) {
/*  99 */     p.location = (Locator)loc;
/* 100 */     if (anno != null)
/* 101 */       p.annotation = anno.getResult(); 
/* 102 */     return p;
/*     */   }
/*     */   
/*     */   static DContainerPattern addAll(DContainerPattern parent, List<DPattern> children) {
/* 106 */     for (DPattern c : children)
/* 107 */       parent.add(c); 
/* 108 */     return parent;
/*     */   }
/*     */   
/*     */   static DUnaryPattern addBody(DUnaryPattern parent, ParsedPattern _body, LocatorImpl loc) {
/* 112 */     parent.setChild((DPattern)_body);
/* 113 */     return parent;
/*     */   }
/*     */   
/*     */   public DPattern makeChoice(List<DPattern> patterns, LocatorImpl loc, Annotation anno) throws BuildException {
/* 117 */     return wrap(addAll(new DChoicePattern(), patterns), loc, anno);
/*     */   }
/*     */   
/*     */   public DPattern makeInterleave(List<DPattern> patterns, LocatorImpl loc, Annotation anno) throws BuildException {
/* 121 */     return wrap(addAll(new DInterleavePattern(), patterns), loc, anno);
/*     */   }
/*     */   
/*     */   public DPattern makeGroup(List<DPattern> patterns, LocatorImpl loc, Annotation anno) throws BuildException {
/* 125 */     return wrap(addAll(new DGroupPattern(), patterns), loc, anno);
/*     */   }
/*     */   
/*     */   public DPattern makeOneOrMore(DPattern p, LocatorImpl loc, Annotation anno) throws BuildException {
/* 129 */     return wrap(addBody(new DOneOrMorePattern(), p, loc), loc, anno);
/*     */   }
/*     */   
/*     */   public DPattern makeZeroOrMore(DPattern p, LocatorImpl loc, Annotation anno) throws BuildException {
/* 133 */     return wrap(addBody(new DZeroOrMorePattern(), p, loc), loc, anno);
/*     */   }
/*     */   
/*     */   public DPattern makeOptional(DPattern p, LocatorImpl loc, Annotation anno) throws BuildException {
/* 137 */     return wrap(addBody(new DOptionalPattern(), p, loc), loc, anno);
/*     */   }
/*     */   
/*     */   public DPattern makeList(DPattern p, LocatorImpl loc, Annotation anno) throws BuildException {
/* 141 */     return wrap(addBody(new DListPattern(), p, loc), loc, anno);
/*     */   }
/*     */   
/*     */   public DPattern makeMixed(DPattern p, LocatorImpl loc, Annotation anno) throws BuildException {
/* 145 */     return wrap(addBody(new DMixedPattern(), p, loc), loc, anno);
/*     */   }
/*     */   
/*     */   public DPattern makeEmpty(LocatorImpl loc, Annotation anno) {
/* 149 */     return wrap(new DEmptyPattern(), loc, anno);
/*     */   }
/*     */   
/*     */   public DPattern makeNotAllowed(LocatorImpl loc, Annotation anno) {
/* 153 */     return wrap(new DNotAllowedPattern(), loc, anno);
/*     */   }
/*     */   
/*     */   public DPattern makeText(LocatorImpl loc, Annotation anno) {
/* 157 */     return wrap(new DTextPattern(), loc, anno);
/*     */   }
/*     */   
/*     */   public DPattern makeAttribute(NameClass nc, DPattern p, LocatorImpl loc, Annotation anno) throws BuildException {
/* 161 */     return wrap(addBody(new DAttributePattern(nc), p, loc), loc, anno);
/*     */   }
/*     */   
/*     */   public DPattern makeElement(NameClass nc, DPattern p, LocatorImpl loc, Annotation anno) throws BuildException {
/* 165 */     return wrap(addBody(new DElementPattern(nc), p, loc), loc, anno);
/*     */   }
/*     */   
/*     */   public DataPatternBuilder makeDataPatternBuilder(String datatypeLibrary, String type, LocatorImpl loc) throws BuildException {
/* 169 */     return new DataPatternBuilderImpl(datatypeLibrary, type, (Location)loc);
/*     */   }
/*     */   
/*     */   public DPattern makeValue(String datatypeLibrary, String type, String value, Context c, String ns, LocatorImpl loc, Annotation anno) throws BuildException {
/* 173 */     return wrap(new DValuePattern(datatypeLibrary, type, value, c.copy(), ns), loc, anno);
/*     */   }
/*     */   
/*     */   public Grammar makeGrammar(Scope parent) {
/* 177 */     return new GrammarBuilderImpl(new DGrammarPattern(), parent, this);
/*     */   }
/*     */ 
/*     */   
/*     */   public DPattern annotate(DPattern p, Annotation anno) throws BuildException {
/* 182 */     return p;
/*     */   }
/*     */ 
/*     */   
/*     */   public DPattern annotateAfter(DPattern p, ElementWrapper e) throws BuildException {
/* 187 */     return p;
/*     */   }
/*     */ 
/*     */   
/*     */   public DPattern commentAfter(DPattern p, CommentListImpl comments) throws BuildException {
/* 192 */     return p;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public DPattern makeExternalRef(Parseable current, String uri, String ns, Scope<DPattern, ElementWrapper, LocatorImpl, Annotation, CommentListImpl> scope, LocatorImpl loc, Annotation anno) throws BuildException, IllegalSchemaException {
/* 198 */     return null;
/*     */   }
/*     */   
/*     */   public LocatorImpl makeLocation(String systemId, int lineNumber, int columnNumber) {
/* 202 */     return new LocatorImpl(systemId, lineNumber, columnNumber);
/*     */   }
/*     */   
/*     */   public Annotation makeAnnotations(CommentListImpl comments, Context context) {
/* 206 */     return new Annotation();
/*     */   }
/*     */   
/*     */   public ElementAnnotationBuilder makeElementAnnotationBuilder(String ns, String localName, String prefix, LocatorImpl loc, CommentListImpl comments, Context context) {
/*     */     String qname;
/* 211 */     if (prefix == null) {
/* 212 */       qname = localName;
/*     */     } else {
/* 214 */       qname = prefix + ':' + localName;
/* 215 */     }  return new ElementAnnotationBuilderImpl(this.dom.createElementNS(ns, qname));
/*     */   }
/*     */   
/*     */   public CommentListImpl makeCommentList() {
/* 219 */     return null;
/*     */   }
/*     */   
/*     */   public DPattern makeErrorPattern() {
/* 223 */     return new DNotAllowedPattern();
/*     */   }
/*     */   
/*     */   public boolean usesComments() {
/* 227 */     return false;
/*     */   }
/*     */   
/*     */   public DPattern expandPattern(DPattern p) throws BuildException, IllegalSchemaException {
/* 231 */     return p;
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\xml\internal\rngom\digested\DSchemaBuilderImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */