/*     */ package com.sun.xml.internal.rngom.binary;
/*     */
/*     */ import com.sun.xml.internal.rngom.ast.builder.Annotations;
/*     */ import com.sun.xml.internal.rngom.ast.builder.BuildException;
/*     */ import com.sun.xml.internal.rngom.ast.builder.CommentList;
/*     */ import com.sun.xml.internal.rngom.ast.builder.DataPatternBuilder;
/*     */ import com.sun.xml.internal.rngom.ast.builder.Div;
/*     */ import com.sun.xml.internal.rngom.ast.builder.ElementAnnotationBuilder;
/*     */ import com.sun.xml.internal.rngom.ast.builder.Grammar;
/*     */ import com.sun.xml.internal.rngom.ast.builder.GrammarSection;
/*     */ import com.sun.xml.internal.rngom.ast.builder.Include;
/*     */ import com.sun.xml.internal.rngom.ast.builder.IncludedGrammar;
/*     */ import com.sun.xml.internal.rngom.ast.builder.NameClassBuilder;
/*     */ import com.sun.xml.internal.rngom.ast.builder.SchemaBuilder;
/*     */ import com.sun.xml.internal.rngom.ast.builder.Scope;
/*     */ import com.sun.xml.internal.rngom.ast.om.Location;
/*     */ import com.sun.xml.internal.rngom.ast.om.ParsedElementAnnotation;
/*     */ import com.sun.xml.internal.rngom.ast.om.ParsedNameClass;
/*     */ import com.sun.xml.internal.rngom.ast.om.ParsedPattern;
/*     */ import com.sun.xml.internal.rngom.ast.util.LocatorImpl;
/*     */ import com.sun.xml.internal.rngom.dt.CascadingDatatypeLibraryFactory;
/*     */ import com.sun.xml.internal.rngom.dt.builtin.BuiltinDatatypeLibraryFactory;
/*     */ import com.sun.xml.internal.rngom.nc.NameClass;
/*     */ import com.sun.xml.internal.rngom.nc.NameClassBuilderImpl;
/*     */ import com.sun.xml.internal.rngom.parse.Context;
/*     */ import com.sun.xml.internal.rngom.parse.IllegalSchemaException;
/*     */ import com.sun.xml.internal.rngom.parse.Parseable;
/*     */ import com.sun.xml.internal.rngom.util.Localizer;
/*     */ import java.util.Enumeration;
/*     */ import java.util.Hashtable;
/*     */ import java.util.List;
/*     */ import org.relaxng.datatype.Datatype;
/*     */ import org.relaxng.datatype.DatatypeBuilder;
/*     */ import org.relaxng.datatype.DatatypeException;
/*     */ import org.relaxng.datatype.DatatypeLibrary;
/*     */ import org.relaxng.datatype.DatatypeLibraryFactory;
/*     */ import org.relaxng.datatype.ValidationContext;
/*     */ import org.relaxng.datatype.helpers.DatatypeLibraryLoader;
/*     */ import org.xml.sax.ErrorHandler;
/*     */ import org.xml.sax.Locator;
/*     */ import org.xml.sax.SAXException;
/*     */ import org.xml.sax.SAXParseException;
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */ public class SchemaBuilderImpl
/*     */   implements SchemaBuilder, ElementAnnotationBuilder, CommentList
/*     */ {
/*     */   private final SchemaBuilderImpl parent;
/*     */   private boolean hadError = false;
/*     */   private final SchemaPatternBuilder pb;
/*     */   private final DatatypeLibraryFactory datatypeLibraryFactory;
/*     */   private final String inheritNs;
/*     */   private final ErrorHandler eh;
/*     */   private final OpenIncludes openIncludes;
/*  99 */   private final NameClassBuilder ncb = (NameClassBuilder)new NameClassBuilderImpl();
/* 100 */   static final Localizer localizer = new Localizer(SchemaBuilderImpl.class);
/*     */
/*     */   static class OpenIncludes
/*     */   {
/*     */     final String uri;
/*     */     final OpenIncludes parent;
/*     */
/*     */     OpenIncludes(String uri, OpenIncludes parent) {
/* 108 */       this.uri = uri;
/* 109 */       this.parent = parent;
/*     */     }
/*     */   }
/*     */
/*     */
/*     */   public ParsedPattern expandPattern(ParsedPattern _pattern) throws BuildException, IllegalSchemaException {
/* 115 */     Pattern pattern = (Pattern)_pattern;
/* 116 */     if (!this.hadError) {
/*     */       try {
/* 118 */         pattern.checkRecursion(0);
/* 119 */         pattern = pattern.expand(this.pb);
/* 120 */         pattern.checkRestrictions(0, null, null);
/* 121 */         if (!this.hadError) {
/* 122 */           return pattern;
/*     */         }
/* 124 */       } catch (SAXParseException e) {
/* 125 */         error(e);
/* 126 */       } catch (SAXException e) {
/* 127 */         throw new BuildException(e);
/* 128 */       } catch (RestrictionViolationException e) {
/* 129 */         if (e.getName() != null) {
/* 130 */           error(e.getMessageId(), e.getName().toString(), e
/* 131 */               .getLocator());
/*     */         } else {
/* 133 */           error(e.getMessageId(), e.getLocator());
/*     */         }
/*     */       }
/*     */     }
/* 137 */     throw new IllegalSchemaException();
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   public SchemaBuilderImpl(ErrorHandler eh) {
/* 145 */     this(eh, (DatatypeLibraryFactory)new CascadingDatatypeLibraryFactory((DatatypeLibraryFactory)new DatatypeLibraryLoader(), (DatatypeLibraryFactory)new BuiltinDatatypeLibraryFactory((DatatypeLibraryFactory)new DatatypeLibraryLoader())), new SchemaPatternBuilder());
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
/*     */
/*     */
/*     */
/*     */
/*     */   public SchemaBuilderImpl(ErrorHandler eh, DatatypeLibraryFactory datatypeLibraryFactory, SchemaPatternBuilder pb) {
/* 161 */     this.parent = null;
/* 162 */     this.eh = eh;
/* 163 */     this.datatypeLibraryFactory = datatypeLibraryFactory;
/* 164 */     this.pb = pb;
/* 165 */     this.inheritNs = "";
/* 166 */     this.openIncludes = null;
/*     */   }
/*     */
/*     */
/*     */
/*     */   private SchemaBuilderImpl(String inheritNs, String uri, SchemaBuilderImpl parent) {
/* 172 */     this.parent = parent;
/* 173 */     this.eh = parent.eh;
/* 174 */     this.datatypeLibraryFactory = parent.datatypeLibraryFactory;
/* 175 */     this.pb = parent.pb;
/* 176 */     this.inheritNs = inheritNs;
/* 177 */     this.openIncludes = new OpenIncludes(uri, parent.openIncludes);
/*     */   }
/*     */
/*     */   public NameClassBuilder getNameClassBuilder() {
/* 181 */     return this.ncb;
/*     */   }
/*     */
/*     */
/*     */   public ParsedPattern makeChoice(List<Pattern> patterns, Location loc, Annotations anno) throws BuildException {
/* 186 */     if (patterns.isEmpty()) {
/* 187 */       throw new IllegalArgumentException();
/*     */     }
/* 189 */     Pattern result = patterns.get(0);
/* 190 */     for (int i = 1; i < patterns.size(); i++) {
/* 191 */       result = this.pb.makeChoice(result, patterns.get(i));
/*     */     }
/* 193 */     return result;
/*     */   }
/*     */
/*     */
/*     */   public ParsedPattern makeInterleave(List<Pattern> patterns, Location loc, Annotations anno) throws BuildException {
/* 198 */     if (patterns.isEmpty()) {
/* 199 */       throw new IllegalArgumentException();
/*     */     }
/* 201 */     Pattern result = patterns.get(0);
/* 202 */     for (int i = 1; i < patterns.size(); i++) {
/* 203 */       result = this.pb.makeInterleave(result, patterns.get(i));
/*     */     }
/* 205 */     return result;
/*     */   }
/*     */
/*     */
/*     */   public ParsedPattern makeGroup(List<Pattern> patterns, Location loc, Annotations anno) throws BuildException {
/* 210 */     if (patterns.isEmpty()) {
/* 211 */       throw new IllegalArgumentException();
/*     */     }
/* 213 */     Pattern result = patterns.get(0);
/* 214 */     for (int i = 1; i < patterns.size(); i++) {
/* 215 */       result = this.pb.makeGroup(result, patterns.get(i));
/*     */     }
/* 217 */     return result;
/*     */   }
/*     */
/*     */
/*     */   public ParsedPattern makeOneOrMore(ParsedPattern p, Location loc, Annotations anno) throws BuildException {
/* 222 */     return this.pb.makeOneOrMore((Pattern)p);
/*     */   }
/*     */
/*     */
/*     */   public ParsedPattern makeZeroOrMore(ParsedPattern p, Location loc, Annotations anno) throws BuildException {
/* 227 */     return this.pb.makeZeroOrMore((Pattern)p);
/*     */   }
/*     */
/*     */
/*     */   public ParsedPattern makeOptional(ParsedPattern p, Location loc, Annotations anno) throws BuildException {
/* 232 */     return this.pb.makeOptional((Pattern)p);
/*     */   }
/*     */
/*     */
/*     */   public ParsedPattern makeList(ParsedPattern p, Location loc, Annotations anno) throws BuildException {
/* 237 */     return this.pb.makeList((Pattern)p, (Locator)loc);
/*     */   }
/*     */
/*     */
/*     */   public ParsedPattern makeMixed(ParsedPattern p, Location loc, Annotations anno) throws BuildException {
/* 242 */     return this.pb.makeMixed((Pattern)p);
/*     */   }
/*     */
/*     */   public ParsedPattern makeEmpty(Location loc, Annotations anno) {
/* 246 */     return this.pb.makeEmpty();
/*     */   }
/*     */
/*     */   public ParsedPattern makeNotAllowed(Location loc, Annotations anno) {
/* 250 */     return this.pb.makeUnexpandedNotAllowed();
/*     */   }
/*     */
/*     */   public ParsedPattern makeText(Location loc, Annotations anno) {
/* 254 */     return this.pb.makeText();
/*     */   }
/*     */
/*     */   public ParsedPattern makeErrorPattern() {
/* 258 */     return this.pb.makeError();
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   public ParsedPattern makeAttribute(ParsedNameClass nc, ParsedPattern p, Location loc, Annotations anno) throws BuildException {
/* 266 */     return this.pb.makeAttribute((NameClass)nc, (Pattern)p, (Locator)loc);
/*     */   }
/*     */
/*     */
/*     */   public ParsedPattern makeElement(ParsedNameClass nc, ParsedPattern p, Location loc, Annotations anno) throws BuildException {
/* 271 */     return this.pb.makeElement((NameClass)nc, (Pattern)p, (Locator)loc);
/*     */   }
/*     */
/*     */   private class DummyDataPatternBuilder
/*     */     implements DataPatternBuilder
/*     */   {
/*     */     private DummyDataPatternBuilder() {}
/*     */
/*     */     public void addParam(String name, String value, Context context, String ns, Location loc, Annotations anno) throws BuildException {}
/*     */
/*     */     public ParsedPattern makePattern(Location loc, Annotations anno) throws BuildException {
/* 282 */       return SchemaBuilderImpl.this.pb.makeError();
/*     */     }
/*     */
/*     */
/*     */     public ParsedPattern makePattern(ParsedPattern except, Location loc, Annotations anno) throws BuildException {
/* 287 */       return SchemaBuilderImpl.this.pb.makeError();
/*     */     }
/*     */
/*     */     public void annotation(ParsedElementAnnotation ea) {}
/*     */   }
/*     */
/*     */   private static class ValidationContextImpl
/*     */     implements ValidationContext
/*     */   {
/*     */     private ValidationContext vc;
/*     */     private String ns;
/*     */
/*     */     ValidationContextImpl(ValidationContext vc, String ns) {
/* 300 */       this.vc = vc;
/* 301 */       this.ns = (ns.length() == 0) ? null : ns;
/*     */     }
/*     */
/*     */     public String resolveNamespacePrefix(String prefix) {
/* 305 */       return (prefix.length() == 0) ? this.ns : this.vc.resolveNamespacePrefix(prefix);
/*     */     }
/*     */
/*     */     public String getBaseUri() {
/* 309 */       return this.vc.getBaseUri();
/*     */     }
/*     */
/*     */     public boolean isUnparsedEntity(String entityName) {
/* 313 */       return this.vc.isUnparsedEntity(entityName);
/*     */     }
/*     */
/*     */     public boolean isNotation(String notationName) {
/* 317 */       return this.vc.isNotation(notationName);
/*     */     }
/*     */   }
/*     */
/*     */   private class DataPatternBuilderImpl
/*     */     implements DataPatternBuilder {
/*     */     private DatatypeBuilder dtb;
/*     */
/*     */     DataPatternBuilderImpl(DatatypeBuilder dtb) {
/* 326 */       this.dtb = dtb;
/*     */     }
/*     */
/*     */
/*     */     public void addParam(String name, String value, Context context, String ns, Location loc, Annotations anno) throws BuildException {
/*     */       try {
/* 332 */         this.dtb.addParameter(name, value, new ValidationContextImpl((ValidationContext)context, ns));
/* 333 */       } catch (DatatypeException e) {
/* 334 */         String displayedParam, detail = e.getMessage();
/* 335 */         int pos = e.getIndex();
/*     */
/* 337 */         if (pos == -1) {
/* 338 */           displayedParam = null;
/*     */         } else {
/* 340 */           displayedParam = displayParam(value, pos);
/*     */         }
/* 342 */         if (displayedParam != null) {
/* 343 */           if (detail != null) {
/* 344 */             SchemaBuilderImpl.this.error("invalid_param_detail_display", detail, displayedParam, (Locator)loc);
/*     */           } else {
/* 346 */             SchemaBuilderImpl.this.error("invalid_param_display", displayedParam, (Locator)loc);
/*     */           }
/* 348 */         } else if (detail != null) {
/* 349 */           SchemaBuilderImpl.this.error("invalid_param_detail", detail, (Locator)loc);
/*     */         } else {
/* 351 */           SchemaBuilderImpl.this.error("invalid_param", (Locator)loc);
/*     */         }
/*     */       }
/*     */     }
/*     */
/*     */     String displayParam(String value, int pos) {
/* 357 */       if (pos < 0) {
/* 358 */         pos = 0;
/* 359 */       } else if (pos > value.length()) {
/* 360 */         pos = value.length();
/*     */       }
/* 362 */       return SchemaBuilderImpl.localizer.message("display_param", value.substring(0, pos), value.substring(pos));
/*     */     }
/*     */
/*     */
/*     */     public ParsedPattern makePattern(Location loc, Annotations anno) throws BuildException {
/*     */       try {
/* 368 */         return SchemaBuilderImpl.this.pb.makeData(this.dtb.createDatatype());
/* 369 */       } catch (DatatypeException e) {
/* 370 */         String detail = e.getMessage();
/* 371 */         if (detail != null) {
/* 372 */           SchemaBuilderImpl.this.error("invalid_params_detail", detail, (Locator)loc);
/*     */         } else {
/* 374 */           SchemaBuilderImpl.this.error("invalid_params", (Locator)loc);
/*     */         }
/* 376 */         return SchemaBuilderImpl.this.pb.makeError();
/*     */       }
/*     */     }
/*     */
/*     */
/*     */     public ParsedPattern makePattern(ParsedPattern except, Location loc, Annotations anno) throws BuildException {
/*     */       try {
/* 383 */         return SchemaBuilderImpl.this.pb.makeDataExcept(this.dtb.createDatatype(), (Pattern)except, (Locator)loc);
/* 384 */       } catch (DatatypeException e) {
/* 385 */         String detail = e.getMessage();
/* 386 */         if (detail != null) {
/* 387 */           SchemaBuilderImpl.this.error("invalid_params_detail", detail, (Locator)loc);
/*     */         } else {
/* 389 */           SchemaBuilderImpl.this.error("invalid_params", (Locator)loc);
/*     */         }
/* 391 */         return SchemaBuilderImpl.this.pb.makeError();
/*     */       }
/*     */     }
/*     */
/*     */
/*     */     public void annotation(ParsedElementAnnotation ea) {}
/*     */   }
/*     */
/*     */
/*     */   public DataPatternBuilder makeDataPatternBuilder(String datatypeLibrary, String type, Location loc) throws BuildException {
/* 401 */     DatatypeLibrary dl = this.datatypeLibraryFactory.createDatatypeLibrary(datatypeLibrary);
/* 402 */     if (dl == null) {
/* 403 */       error("unrecognized_datatype_library", datatypeLibrary, (Locator)loc);
/*     */     } else {
/*     */       try {
/* 406 */         return new DataPatternBuilderImpl(dl.createDatatypeBuilder(type));
/* 407 */       } catch (DatatypeException e) {
/* 408 */         String detail = e.getMessage();
/* 409 */         if (detail != null) {
/* 410 */           error("unsupported_datatype_detail", datatypeLibrary, type, detail, (Locator)loc);
/*     */         } else {
/* 412 */           error("unrecognized_datatype", datatypeLibrary, type, (Locator)loc);
/*     */         }
/*     */       }
/*     */     }
/* 416 */     return new DummyDataPatternBuilder();
/*     */   }
/*     */
/*     */
/*     */   public ParsedPattern makeValue(String datatypeLibrary, String type, String value, Context context, String ns, Location loc, Annotations anno) throws BuildException {
/* 421 */     DatatypeLibrary dl = this.datatypeLibraryFactory.createDatatypeLibrary(datatypeLibrary);
/* 422 */     if (dl == null) {
/* 423 */       error("unrecognized_datatype_library", datatypeLibrary, (Locator)loc);
/*     */     } else {
/*     */       try {
/* 426 */         DatatypeBuilder dtb = dl.createDatatypeBuilder(type);
/*     */         try {
/* 428 */           Datatype dt = dtb.createDatatype();
/* 429 */           Object obj = dt.createValue(value, new ValidationContextImpl((ValidationContext)context, ns));
/* 430 */           if (obj != null) {
/* 431 */             return this.pb.makeValue(dt, obj);
/*     */           }
/* 433 */           error("invalid_value", value, (Locator)loc);
/* 434 */         } catch (DatatypeException e) {
/* 435 */           String detail = e.getMessage();
/* 436 */           if (detail != null) {
/* 437 */             error("datatype_requires_param_detail", detail, (Locator)loc);
/*     */           } else {
/* 439 */             error("datatype_requires_param", (Locator)loc);
/*     */           }
/*     */         }
/* 442 */       } catch (DatatypeException e) {
/* 443 */         error("unrecognized_datatype", datatypeLibrary, type, (Locator)loc);
/*     */       }
/*     */     }
/* 446 */     return this.pb.makeError();
/*     */   }
/*     */
/*     */   static class GrammarImpl
/*     */     implements Grammar, Div, IncludedGrammar {
/*     */     private final SchemaBuilderImpl sb;
/*     */     private final Hashtable defines;
/*     */     private final RefPattern startRef;
/*     */     private final Scope parent;
/*     */
/*     */     private GrammarImpl(SchemaBuilderImpl sb, Scope parent) {
/* 457 */       this.sb = sb;
/* 458 */       this.parent = parent;
/* 459 */       this.defines = new Hashtable<>();
/* 460 */       this.startRef = new RefPattern(null);
/*     */     }
/*     */
/*     */     protected GrammarImpl(SchemaBuilderImpl sb, GrammarImpl g) {
/* 464 */       this.sb = sb;
/* 465 */       this.parent = g.parent;
/* 466 */       this.startRef = g.startRef;
/* 467 */       this.defines = g.defines;
/*     */     }
/*     */
/*     */     public ParsedPattern endGrammar(Location loc, Annotations anno) throws BuildException {
/* 471 */       Enumeration<String> e = this.defines.keys();
/* 472 */       while (e.hasMoreElements()) {
/* 473 */         String name = e.nextElement();
/* 474 */         RefPattern rp = (RefPattern)this.defines.get(name);
/* 475 */         if (rp.getPattern() == null) {
/* 476 */           this.sb.error("reference_to_undefined", name, rp.getRefLocator());
/* 477 */           rp.setPattern(this.sb.pb.makeError());
/*     */         }
/*     */       }
/* 480 */       Pattern start = this.startRef.getPattern();
/* 481 */       if (start == null) {
/* 482 */         this.sb.error("missing_start_element", (Locator)loc);
/* 483 */         start = this.sb.pb.makeError();
/*     */       }
/* 485 */       return start;
/*     */     }
/*     */
/*     */
/*     */     public void endDiv(Location loc, Annotations anno) throws BuildException {}
/*     */
/*     */
/*     */     public ParsedPattern endIncludedGrammar(Location loc, Annotations anno) throws BuildException {
/* 493 */       return null;
/*     */     }
/*     */
/*     */
/*     */     public void define(String name, Combine combine, ParsedPattern pattern, Location loc, Annotations anno) throws BuildException {
/* 498 */       define(lookup(name), combine, pattern, loc);
/*     */     }
/*     */
/*     */     private void define(RefPattern rp, Combine combine, ParsedPattern pattern, Location loc) throws BuildException {
/*     */       Pattern p;
/* 503 */       switch (rp.getReplacementStatus()) {
/*     */         case 0:
/* 505 */           if (combine == null) {
/* 506 */             if (rp.isCombineImplicit()) {
/* 507 */               if (rp.getName() == null) {
/* 508 */                 this.sb.error("duplicate_start", (Locator)loc);
/*     */               } else {
/* 510 */                 this.sb.error("duplicate_define", rp.getName(), (Locator)loc);
/*     */               }
/*     */             } else {
/* 513 */               rp.setCombineImplicit();
/*     */             }
/*     */           } else {
/* 516 */             byte combineType = (combine == COMBINE_CHOICE) ? 1 : 2;
/* 517 */             if (rp.getCombineType() != 0 && rp
/* 518 */               .getCombineType() != combineType) {
/* 519 */               if (rp.getName() == null) {
/* 520 */                 this.sb.error("conflict_combine_start", (Locator)loc);
/*     */               } else {
/* 522 */                 this.sb.error("conflict_combine_define", rp.getName(), (Locator)loc);
/*     */               }
/*     */             }
/* 525 */             rp.setCombineType(combineType);
/*     */           }
/* 527 */           p = (Pattern)pattern;
/* 528 */           if (rp.getPattern() == null) {
/* 529 */             rp.setPattern(p); break;
/* 530 */           }  if (rp.getCombineType() == 2) {
/* 531 */             rp.setPattern(this.sb.pb.makeInterleave(rp.getPattern(), p)); break;
/*     */           }
/* 533 */           rp.setPattern(this.sb.pb.makeChoice(rp.getPattern(), p));
/*     */           break;
/*     */
/*     */         case 1:
/* 537 */           rp.setReplacementStatus((byte)2);
/*     */           break;
/*     */       }
/*     */     }
/*     */
/*     */
/*     */
/*     */     public void topLevelAnnotation(ParsedElementAnnotation ea) throws BuildException {}
/*     */
/*     */
/*     */     public void topLevelComment(CommentList comments) throws BuildException {}
/*     */
/*     */
/*     */     private RefPattern lookup(String name) {
/* 551 */       if (name == "\000#start\000") {
/* 552 */         return this.startRef;
/*     */       }
/* 554 */       return lookup1(name);
/*     */     }
/*     */
/*     */     private RefPattern lookup1(String name) {
/* 558 */       RefPattern p = (RefPattern)this.defines.get(name);
/* 559 */       if (p == null) {
/* 560 */         p = new RefPattern(name);
/* 561 */         this.defines.put(name, p);
/*     */       }
/* 563 */       return p;
/*     */     }
/*     */
/*     */     public ParsedPattern makeRef(String name, Location loc, Annotations anno) throws BuildException {
/* 567 */       RefPattern p = lookup1(name);
/* 568 */       if (p.getRefLocator() == null && loc != null) {
/* 569 */         p.setRefLocator((Locator)loc);
/*     */       }
/* 571 */       return p;
/*     */     }
/*     */
/*     */
/*     */     public ParsedPattern makeParentRef(String name, Location loc, Annotations anno) throws BuildException {
/* 576 */       if (this.parent == null) {
/* 577 */         this.sb.error("parent_ref_outside_grammar", (Locator)loc);
/* 578 */         return this.sb.makeErrorPattern();
/*     */       }
/* 580 */       return this.parent.makeRef(name, loc, anno);
/*     */     }
/*     */
/*     */     public Div makeDiv() {
/* 584 */       return this;
/*     */     }
/*     */
/*     */     public Include makeInclude() {
/* 588 */       return new IncludeImpl(this.sb, this);
/*     */     } }
/*     */
/*     */   static class Override {
/*     */     RefPattern prp;
/*     */
/*     */     Override(RefPattern prp, Override next) {
/* 595 */       this.prp = prp;
/* 596 */       this.next = next;
/*     */     }
/*     */
/*     */     Override next;
/*     */     byte replacementStatus;
/*     */   }
/*     */
/*     */   private static class IncludeImpl
/*     */     implements Include, Div {
/*     */     private SchemaBuilderImpl sb;
/*     */     private Override overrides;
/*     */     private GrammarImpl grammar;
/*     */
/*     */     private IncludeImpl(SchemaBuilderImpl sb, GrammarImpl grammar) {
/* 610 */       this.sb = sb;
/* 611 */       this.grammar = grammar;
/*     */     }
/*     */
/*     */
/*     */     public void define(String name, Combine combine, ParsedPattern pattern, Location loc, Annotations anno) throws BuildException {
/* 616 */       RefPattern rp = this.grammar.lookup(name);
/* 617 */       this.overrides = new Override(rp, this.overrides);
/* 618 */       this.grammar.define(rp, combine, pattern, loc);
/*     */     }
/*     */
/*     */
/*     */
/*     */     public void endDiv(Location loc, Annotations anno) throws BuildException {}
/*     */
/*     */
/*     */     public void topLevelAnnotation(ParsedElementAnnotation ea) throws BuildException {}
/*     */
/*     */
/*     */     public void topLevelComment(CommentList comments) throws BuildException {}
/*     */
/*     */
/*     */     public Div makeDiv() {
/* 633 */       return this;
/*     */     }
/*     */
/*     */
/*     */     public void endInclude(Parseable current, String uri, String ns, Location loc, Annotations anno) throws BuildException {
/* 638 */       OpenIncludes inc = this.sb.openIncludes;
/* 639 */       for (; inc != null;
/* 640 */         inc = inc.parent) {
/* 641 */         if (inc.uri.equals(uri)) {
/* 642 */           this.sb.error("recursive_include", uri, (Locator)loc);
/*     */
/*     */           return;
/*     */         }
/*     */       }
/* 647 */       for (Override o = this.overrides; o != null; o = o.next) {
/* 648 */         o.replacementStatus = o.prp.getReplacementStatus();
/* 649 */         o.prp.setReplacementStatus((byte)1);
/*     */       }
/*     */       try {
/* 652 */         SchemaBuilderImpl isb = new SchemaBuilderImpl(ns, uri, this.sb);
/* 653 */         current.parseInclude(uri, isb, new GrammarImpl(isb, this.grammar), ns);
/* 654 */         for (Override override2 = this.overrides; override2 != null; override2 = override2.next) {
/* 655 */           if (override2.prp.getReplacementStatus() == 1) {
/* 656 */             if (override2.prp.getName() == null) {
/* 657 */               this.sb.error("missing_start_replacement", (Locator)loc);
/*     */             } else {
/* 659 */               this.sb.error("missing_define_replacement", override2.prp.getName(), (Locator)loc);
/*     */             }
/*     */           }
/*     */         }
/* 663 */       } catch (IllegalSchemaException e) {
/* 664 */         this.sb.noteError();
/*     */       } finally {
/* 666 */         for (Override override = this.overrides; override != null; override = override.next) {
/* 667 */           override.prp.setReplacementStatus(override.replacementStatus);
/*     */         }
/*     */       }
/*     */     }
/*     */
/*     */     public Include makeInclude() {
/* 673 */       return null;
/*     */     }
/*     */   }
/*     */
/*     */   public Grammar makeGrammar(Scope parent) {
/* 678 */     return new GrammarImpl(this, parent);
/*     */   }
/*     */
/*     */   public ParsedPattern annotate(ParsedPattern p, Annotations anno) throws BuildException {
/* 682 */     return p;
/*     */   }
/*     */
/*     */   public ParsedPattern annotateAfter(ParsedPattern p, ParsedElementAnnotation e) throws BuildException {
/* 686 */     return p;
/*     */   }
/*     */
/*     */   public ParsedPattern commentAfter(ParsedPattern p, CommentList comments) throws BuildException {
/* 690 */     return p;
/*     */   }
/*     */
/*     */
/*     */
/*     */   public ParsedPattern makeExternalRef(Parseable current, String uri, String ns, Scope scope, Location loc, Annotations anno) throws BuildException {
/* 696 */     OpenIncludes inc = this.openIncludes;
/* 697 */     for (; inc != null;
/* 698 */       inc = inc.parent) {
/* 699 */       if (inc.uri.equals(uri)) {
/* 700 */         error("recursive_include", uri, (Locator)loc);
/* 701 */         return this.pb.makeError();
/*     */       }
/*     */     }
/*     */     try {
/* 705 */       return current.parseExternal(uri, new SchemaBuilderImpl(ns, uri, this), scope, ns);
/* 706 */     } catch (IllegalSchemaException e) {
/* 707 */       noteError();
/* 708 */       return this.pb.makeError();
/*     */     }
/*     */   }
/*     */
/*     */   public Location makeLocation(String systemId, int lineNumber, int columnNumber) {
/* 713 */     return (Location)new LocatorImpl(systemId, lineNumber, columnNumber);
/*     */   }
/*     */
/*     */   public Annotations makeAnnotations(CommentList comments, Context context) {
/* 717 */     return (Annotations)this;
/*     */   }
/*     */
/*     */
/*     */   public ElementAnnotationBuilder makeElementAnnotationBuilder(String ns, String localName, String prefix, Location loc, CommentList comments, Context context) {
/* 722 */     return this;
/*     */   }
/*     */
/*     */   public CommentList makeCommentList() {
/* 726 */     return this;
/*     */   }
/*     */
/*     */
/*     */
/*     */   public void addComment(String value, Location loc) throws BuildException {}
/*     */
/*     */
/*     */
/*     */   public void addAttribute(String ns, String localName, String prefix, String value, Location loc) {}
/*     */
/*     */
/*     */
/*     */   public void addElement(ParsedElementAnnotation ea) {}
/*     */
/*     */
/*     */   public void addComment(CommentList comments) throws BuildException {}
/*     */
/*     */
/*     */   public void addLeadingComment(CommentList comments) throws BuildException {}
/*     */
/*     */
/*     */   public ParsedElementAnnotation makeElementAnnotation() {
/* 749 */     return null;
/*     */   }
/*     */
/*     */
/*     */   public void addText(String value, Location loc, CommentList comments) throws BuildException {}
/*     */
/*     */   public boolean usesComments() {
/* 756 */     return false;
/*     */   }
/*     */
/*     */   private void error(SAXParseException message) throws BuildException {
/* 760 */     noteError();
/*     */     try {
/* 762 */       if (this.eh != null) {
/* 763 */         this.eh.error(message);
/*     */       }
/* 765 */     } catch (SAXException e) {
/* 766 */       throw new BuildException(e);
/*     */     }
/*     */   }
/*     */
/*     */   private void error(String key, Locator loc) throws BuildException {
/* 771 */     error(new SAXParseException(localizer.message(key), loc));
/*     */   }
/*     */
/*     */   private void error(String key, String arg, Locator loc) throws BuildException {
/* 775 */     error(new SAXParseException(localizer.message(key, arg), loc));
/*     */   }
/*     */
/*     */   private void error(String key, String arg1, String arg2, Locator loc) throws BuildException {
/* 779 */     error(new SAXParseException(localizer.message(key, arg1, arg2), loc));
/*     */   }
/*     */
/*     */   private void error(String key, String arg1, String arg2, String arg3, Locator loc) throws BuildException {
/* 783 */     error(new SAXParseException(localizer.message(key, new Object[] { arg1, arg2, arg3 }), loc));
/*     */   }
/*     */
/*     */   private void noteError() {
/* 787 */     if (!this.hadError && this.parent != null) {
/* 788 */       this.parent.noteError();
/*     */     }
/* 790 */     this.hadError = true;
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\xml\internal\rngom\binary\SchemaBuilderImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
