/*      */ package com.sun.xml.internal.rngom.parse.xml;
/*      */
/*      */ import com.sun.xml.internal.rngom.ast.builder.Annotations;
/*      */ import com.sun.xml.internal.rngom.ast.builder.CommentList;
/*      */ import com.sun.xml.internal.rngom.ast.builder.DataPatternBuilder;
/*      */ import com.sun.xml.internal.rngom.ast.builder.Div;
/*      */ import com.sun.xml.internal.rngom.ast.builder.ElementAnnotationBuilder;
/*      */ import com.sun.xml.internal.rngom.ast.builder.Grammar;
/*      */ import com.sun.xml.internal.rngom.ast.builder.GrammarSection;
/*      */ import com.sun.xml.internal.rngom.ast.builder.Include;
/*      */ import com.sun.xml.internal.rngom.ast.builder.IncludedGrammar;
/*      */ import com.sun.xml.internal.rngom.ast.builder.NameClassBuilder;
/*      */ import com.sun.xml.internal.rngom.ast.builder.SchemaBuilder;
/*      */ import com.sun.xml.internal.rngom.ast.builder.Scope;
/*      */ import com.sun.xml.internal.rngom.ast.om.Location;
/*      */ import com.sun.xml.internal.rngom.ast.om.ParsedElementAnnotation;
/*      */ import com.sun.xml.internal.rngom.ast.om.ParsedNameClass;
/*      */ import com.sun.xml.internal.rngom.ast.om.ParsedPattern;
/*      */ import com.sun.xml.internal.rngom.parse.Context;
/*      */ import com.sun.xml.internal.rngom.parse.IllegalSchemaException;
/*      */ import com.sun.xml.internal.rngom.parse.Parseable;
/*      */ import com.sun.xml.internal.rngom.util.Localizer;
/*      */ import com.sun.xml.internal.rngom.util.Uri;
/*      */ import com.sun.xml.internal.rngom.xml.sax.AbstractLexicalHandler;
/*      */ import com.sun.xml.internal.rngom.xml.sax.XmlBaseHandler;
/*      */ import com.sun.xml.internal.rngom.xml.util.Naming;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Arrays;
/*      */ import java.util.Enumeration;
/*      */ import java.util.Hashtable;
/*      */ import java.util.List;
/*      */ import java.util.Stack;
/*      */ import java.util.Vector;
/*      */ import org.xml.sax.Attributes;
/*      */ import org.xml.sax.ContentHandler;
/*      */ import org.xml.sax.ErrorHandler;
/*      */ import org.xml.sax.Locator;
/*      */ import org.xml.sax.SAXException;
/*      */ import org.xml.sax.SAXNotRecognizedException;
/*      */ import org.xml.sax.SAXNotSupportedException;
/*      */ import org.xml.sax.SAXParseException;
/*      */ import org.xml.sax.XMLReader;
/*      */ import org.xml.sax.helpers.DefaultHandler;
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */ class SchemaParser
/*      */ {
/*   94 */   private static final String relaxngURIPrefix = "http://relaxng.org/ns/structure/1.0"
/*   95 */     .substring(0, "http://relaxng.org/ns/structure/1.0".lastIndexOf('/') + 1);
/*      */   static final String relaxng10URI = "http://relaxng.org/ns/structure/1.0";
/*   97 */   private static final Localizer localizer = new Localizer(new Localizer(Parseable.class), SchemaParser.class);
/*      */
/*      */   private String relaxngURI;
/*      */
/*      */   private final XMLReader xr;
/*      */
/*      */   private final ErrorHandler eh;
/*      */
/*      */   private final SchemaBuilder schemaBuilder;
/*      */   private final NameClassBuilder nameClassBuilder;
/*      */   private ParsedPattern startPattern;
/*      */   private Locator locator;
/*  109 */   private final XmlBaseHandler xmlBaseHandler = new XmlBaseHandler();
/*  110 */   private final ContextImpl context = new ContextImpl(); private boolean hadError = false;
/*      */   private Hashtable patternTable;
/*      */   private Hashtable nameClassTable;
/*      */   private static final int INIT_CHILD_ALLOC = 5;
/*      */   private static final int PATTERN_CONTEXT = 0;
/*      */   private static final int ANY_NAME_CONTEXT = 1;
/*      */   private static final int NS_NAME_CONTEXT = 2;
/*      */   private SAXParseable parseable;
/*      */
/*      */   static class PrefixMapping { final String prefix;
/*      */
/*      */     PrefixMapping(String prefix, String uri, PrefixMapping next) {
/*  122 */       this.prefix = prefix;
/*  123 */       this.uri = uri;
/*  124 */       this.next = next;
/*      */     }
/*      */
/*      */     final String uri;
/*      */     final PrefixMapping next; }
/*      */
/*      */   static abstract class AbstractContext extends DtdContext implements Context { PrefixMapping prefixMapping;
/*      */
/*      */     AbstractContext() {
/*  133 */       this.prefixMapping = new PrefixMapping("xml", "http://www.w3.org/XML/1998/namespace", null);
/*      */     }
/*      */
/*      */     AbstractContext(AbstractContext context) {
/*  137 */       super(context);
/*  138 */       this.prefixMapping = context.prefixMapping;
/*      */     }
/*      */
/*      */     public String resolveNamespacePrefix(String prefix) {
/*  142 */       for (PrefixMapping p = this.prefixMapping; p != null; p = p.next) {
/*  143 */         if (p.prefix.equals(prefix)) {
/*  144 */           return p.uri;
/*      */         }
/*      */       }
/*  147 */       return null;
/*      */     }
/*      */
/*      */     public Enumeration prefixes() {
/*  151 */       Vector<String> v = new Vector();
/*  152 */       for (PrefixMapping p = this.prefixMapping; p != null; p = p.next) {
/*  153 */         if (!v.contains(p.prefix)) {
/*  154 */           v.addElement(p.prefix);
/*      */         }
/*      */       }
/*  157 */       return v.elements();
/*      */     }
/*      */
/*      */     public Context copy() {
/*  161 */       return new SavedContext(this);
/*      */     } }
/*      */
/*      */
/*      */   static class SavedContext
/*      */     extends AbstractContext {
/*      */     private final String baseUri;
/*      */
/*      */     SavedContext(AbstractContext context) {
/*  170 */       super(context);
/*  171 */       this.baseUri = context.getBaseUri();
/*      */     }
/*      */
/*      */     public String getBaseUri() {
/*  175 */       return this.baseUri;
/*      */     }
/*      */   }
/*      */
/*      */   class ContextImpl
/*      */     extends AbstractContext {
/*      */     public String getBaseUri() {
/*  182 */       return SchemaParser.this.xmlBaseHandler.getBaseUri();
/*      */     }
/*      */   }
/*      */
/*      */
/*      */
/*      */   abstract class Handler
/*      */     implements ContentHandler, CommentHandler
/*      */   {
/*      */     CommentList comments;
/*      */
/*      */
/*      */
/*      */     CommentList getComments() {
/*  196 */       CommentList tem = this.comments;
/*  197 */       this.comments = null;
/*  198 */       return tem;
/*      */     }
/*      */
/*      */     public void comment(String value) {
/*  202 */       if (this.comments == null) {
/*  203 */         this.comments = SchemaParser.this.schemaBuilder.makeCommentList();
/*      */       }
/*  205 */       this.comments.addComment(value, SchemaParser.this.makeLocation());
/*      */     }
/*      */
/*      */
/*      */     public void processingInstruction(String target, String date) {}
/*      */
/*      */
/*      */     public void skippedEntity(String name) {}
/*      */
/*      */
/*      */     public void ignorableWhitespace(char[] ch, int start, int len) {}
/*      */
/*      */
/*      */     public void startDocument() {}
/*      */
/*      */
/*      */     public void endDocument() {}
/*      */
/*      */     public void startPrefixMapping(String prefix, String uri) {
/*  224 */       SchemaParser.this.context.prefixMapping = new PrefixMapping(prefix, uri, SchemaParser.this.context.prefixMapping);
/*      */     }
/*      */
/*      */     public void endPrefixMapping(String prefix) {
/*  228 */       SchemaParser.this.context.prefixMapping = SchemaParser.this.context.prefixMapping.next;
/*      */     }
/*      */
/*      */     public void setDocumentLocator(Locator loc) {
/*  232 */       SchemaParser.this.locator = loc;
/*  233 */       SchemaParser.this.xmlBaseHandler.setLocator(loc);
/*      */     }
/*      */   }
/*      */
/*      */
/*      */   abstract class State
/*      */     extends Handler
/*      */   {
/*      */     State parent;
/*      */
/*      */     String nsInherit;
/*      */     String ns;
/*      */     String datatypeLibrary;
/*      */     Scope scope;
/*      */     Location startLocation;
/*      */     Annotations annotations;
/*      */
/*      */     void set() {
/*  251 */       SchemaParser.this.xr.setContentHandler(this);
/*      */     }
/*      */
/*      */     abstract State create();
/*      */
/*      */     abstract State createChildState(String param1String) throws SAXException;
/*      */
/*      */     void setParent(State parent) {
/*  259 */       this.parent = parent;
/*  260 */       this.nsInherit = parent.getNs();
/*  261 */       this.datatypeLibrary = parent.datatypeLibrary;
/*  262 */       this.scope = parent.scope;
/*  263 */       this.startLocation = SchemaParser.this.makeLocation();
/*  264 */       if (parent.comments != null) {
/*  265 */         this.annotations = SchemaParser.this.schemaBuilder.makeAnnotations(parent.comments, SchemaParser.this.getContext());
/*  266 */         parent.comments = null;
/*  267 */       } else if (parent instanceof RootState) {
/*  268 */         this.annotations = SchemaParser.this.schemaBuilder.makeAnnotations(null, SchemaParser.this.getContext());
/*      */       }
/*      */     }
/*      */
/*      */     String getNs() {
/*  273 */       return (this.ns == null) ? this.nsInherit : this.ns;
/*      */     }
/*      */
/*      */     boolean isRelaxNGElement(String uri) throws SAXException {
/*  277 */       return uri.equals(SchemaParser.this.relaxngURI);
/*      */     }
/*      */
/*      */
/*      */
/*      */
/*      */     public void startElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException {
/*  284 */       SchemaParser.this.xmlBaseHandler.startElement();
/*  285 */       if (isRelaxNGElement(namespaceURI)) {
/*  286 */         State state = createChildState(localName);
/*  287 */         if (state == null) {
/*  288 */           SchemaParser.this.xr.setContentHandler(new Skipper(this));
/*      */           return;
/*      */         }
/*  291 */         state.setParent(this);
/*  292 */         state.set();
/*  293 */         state.attributes(atts);
/*      */       } else {
/*  295 */         checkForeignElement();
/*  296 */         ForeignElementHandler feh = new ForeignElementHandler(this, getComments());
/*  297 */         feh.startElement(namespaceURI, localName, qName, atts);
/*  298 */         SchemaParser.this.xr.setContentHandler(feh);
/*      */       }
/*      */     }
/*      */
/*      */
/*      */
/*      */     public void endElement(String namespaceURI, String localName, String qName) throws SAXException {
/*  305 */       SchemaParser.this.xmlBaseHandler.endElement();
/*  306 */       this.parent.set();
/*  307 */       end();
/*      */     }
/*      */
/*      */     void setName(String name) throws SAXException {
/*  311 */       SchemaParser.this.error("illegal_name_attribute");
/*      */     }
/*      */
/*      */     void setOtherAttribute(String name, String value) throws SAXException {
/*  315 */       SchemaParser.this.error("illegal_attribute_ignored", name);
/*      */     }
/*      */
/*      */
/*      */     void endAttributes() throws SAXException {}
/*      */
/*      */
/*      */     void checkForeignElement() throws SAXException {}
/*      */
/*      */     void attributes(Attributes atts) throws SAXException {
/*  325 */       int len = atts.getLength();
/*  326 */       for (int i = 0; i < len; i++) {
/*  327 */         String uri = atts.getURI(i);
/*  328 */         if (uri.length() == 0) {
/*  329 */           String name = atts.getLocalName(i);
/*  330 */           if (name.equals("name")) {
/*  331 */             setName(atts.getValue(i).trim());
/*  332 */           } else if (name.equals("ns")) {
/*  333 */             this.ns = atts.getValue(i);
/*  334 */           } else if (name.equals("datatypeLibrary")) {
/*  335 */             this.datatypeLibrary = atts.getValue(i);
/*  336 */             SchemaParser.this.checkUri(this.datatypeLibrary);
/*  337 */             if (!this.datatypeLibrary.equals("") &&
/*  338 */               !Uri.isAbsolute(this.datatypeLibrary)) {
/*  339 */               SchemaParser.this.error("relative_datatype_library");
/*      */             }
/*  341 */             if (Uri.hasFragmentId(this.datatypeLibrary)) {
/*  342 */               SchemaParser.this.error("fragment_identifier_datatype_library");
/*      */             }
/*  344 */             this.datatypeLibrary = Uri.escapeDisallowedChars(this.datatypeLibrary);
/*      */           } else {
/*  346 */             setOtherAttribute(name, atts.getValue(i));
/*      */           }
/*  348 */         } else if (uri.equals(SchemaParser.this.relaxngURI)) {
/*  349 */           SchemaParser.this.error("qualified_attribute", atts.getLocalName(i));
/*  350 */         } else if (uri.equals("http://www.w3.org/XML/1998/namespace") && atts
/*  351 */           .getLocalName(i).equals("base")) {
/*  352 */           SchemaParser.this.xmlBaseHandler.xmlBaseAttribute(atts.getValue(i));
/*      */         } else {
/*  354 */           if (this.annotations == null) {
/*  355 */             this.annotations = SchemaParser.this.schemaBuilder.makeAnnotations(null, SchemaParser.this.getContext());
/*      */           }
/*  357 */           this.annotations.addAttribute(uri, atts.getLocalName(i), SchemaParser.this.findPrefix(atts.getQName(i), uri), atts
/*  358 */               .getValue(i), this.startLocation);
/*      */         }
/*      */       }
/*  361 */       endAttributes();
/*      */     }
/*      */
/*      */
/*      */
/*      */     abstract void end() throws SAXException;
/*      */
/*      */
/*      */
/*      */     void endChild(ParsedPattern pattern) {}
/*      */
/*      */
/*      */     void endChild(ParsedNameClass nc) {}
/*      */
/*      */
/*      */     public void startDocument() {}
/*      */
/*      */
/*      */     public void endDocument() {
/*  380 */       if (this.comments != null && SchemaParser.this.startPattern != null) {
/*  381 */         SchemaParser.this.startPattern = SchemaParser.this.schemaBuilder.commentAfter(SchemaParser.this.startPattern, this.comments);
/*  382 */         this.comments = null;
/*      */       }
/*      */     }
/*      */
/*      */     public void characters(char[] ch, int start, int len) throws SAXException {
/*  387 */       for (int i = 0; i < len; i++) {
/*  388 */         switch (ch[start + i]) {
/*      */           case '\t':
/*      */           case '\n':
/*      */           case '\r':
/*      */           case ' ':
/*      */             break;
/*      */           default:
/*  395 */             SchemaParser.this.error("illegal_characters_ignored");
/*      */             break;
/*      */         }
/*      */       }
/*      */     }
/*      */
/*      */     boolean isPatternNamespaceURI(String s) {
/*  402 */       return s.equals(SchemaParser.this.relaxngURI);
/*      */     }
/*      */
/*      */     void endForeignChild(ParsedElementAnnotation ea) {
/*  406 */       if (this.annotations == null) {
/*  407 */         this.annotations = SchemaParser.this.schemaBuilder.makeAnnotations(null, SchemaParser.this.getContext());
/*      */       }
/*  409 */       this.annotations.addElement(ea);
/*      */     }
/*      */
/*      */     void mergeLeadingComments() {
/*  413 */       if (this.comments != null) {
/*  414 */         if (this.annotations == null) {
/*  415 */           this.annotations = SchemaParser.this.schemaBuilder.makeAnnotations(this.comments, SchemaParser.this.getContext());
/*      */         } else {
/*  417 */           this.annotations.addLeadingComment(this.comments);
/*      */         }
/*  419 */         this.comments = null;
/*      */       }
/*      */     }
/*      */   }
/*      */
/*      */   class ForeignElementHandler
/*      */     extends Handler {
/*      */     final State nextState;
/*      */     ElementAnnotationBuilder builder;
/*  428 */     final Stack builderStack = new Stack();
/*      */     StringBuffer textBuf;
/*      */     Location textLoc;
/*      */
/*      */     ForeignElementHandler(State nextState, CommentList comments) {
/*  433 */       this.nextState = nextState;
/*  434 */       this.comments = comments;
/*      */     }
/*      */
/*      */
/*      */     public void startElement(String namespaceURI, String localName, String qName, Attributes atts) {
/*  439 */       flushText();
/*  440 */       if (this.builder != null) {
/*  441 */         this.builderStack.push(this.builder);
/*      */       }
/*  443 */       Location loc = SchemaParser.this.makeLocation();
/*  444 */       this.builder = SchemaParser.this.schemaBuilder.makeElementAnnotationBuilder(namespaceURI, localName, SchemaParser.this
/*      */
/*  446 */           .findPrefix(qName, namespaceURI), loc,
/*      */
/*  448 */           getComments(), SchemaParser.this
/*  449 */           .getContext());
/*  450 */       int len = atts.getLength();
/*  451 */       for (int i = 0; i < len; i++) {
/*  452 */         String uri = atts.getURI(i);
/*  453 */         this.builder.addAttribute(uri, atts.getLocalName(i), SchemaParser.this.findPrefix(atts.getQName(i), uri), atts
/*  454 */             .getValue(i), loc);
/*      */       }
/*      */     }
/*      */
/*      */
/*      */     public void endElement(String namespaceURI, String localName, String qName) {
/*  460 */       flushText();
/*  461 */       if (this.comments != null) {
/*  462 */         this.builder.addComment(getComments());
/*      */       }
/*  464 */       ParsedElementAnnotation ea = this.builder.makeElementAnnotation();
/*  465 */       if (this.builderStack.empty()) {
/*  466 */         this.nextState.endForeignChild(ea);
/*  467 */         this.nextState.set();
/*      */       } else {
/*  469 */         this.builder = this.builderStack.pop();
/*  470 */         this.builder.addElement(ea);
/*      */       }
/*      */     }
/*      */
/*      */     public void characters(char[] ch, int start, int length) {
/*  475 */       if (this.textBuf == null) {
/*  476 */         this.textBuf = new StringBuffer();
/*      */       }
/*  478 */       this.textBuf.append(ch, start, length);
/*  479 */       if (this.textLoc == null) {
/*  480 */         this.textLoc = SchemaParser.this.makeLocation();
/*      */       }
/*      */     }
/*      */
/*      */
/*      */     public void comment(String value) {
/*  486 */       flushText();
/*  487 */       super.comment(value);
/*      */     }
/*      */
/*      */     void flushText() {
/*  491 */       if (this.textBuf != null && this.textBuf.length() != 0) {
/*  492 */         this.builder.addText(this.textBuf.toString(), this.textLoc, getComments());
/*  493 */         this.textBuf.setLength(0);
/*      */       }
/*  495 */       this.textLoc = null;
/*      */     }
/*      */   }
/*      */
/*      */   static class Skipper
/*      */     extends DefaultHandler implements CommentHandler {
/*  501 */     int level = 1;
/*      */     final State nextState;
/*      */
/*      */     Skipper(State nextState) {
/*  505 */       this.nextState = nextState;
/*      */     }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */     public void startElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException {
/*  513 */       this.level++;
/*      */     }
/*      */
/*      */
/*      */
/*      */
/*      */     public void endElement(String namespaceURI, String localName, String qName) throws SAXException {
/*  520 */       if (--this.level == 0) {
/*  521 */         this.nextState.set();
/*      */       }
/*      */     }
/*      */
/*      */     public void comment(String value) {}
/*      */   }
/*      */
/*      */   abstract class EmptyContentState
/*      */     extends State
/*      */   {
/*      */     State createChildState(String localName) throws SAXException {
/*  532 */       SchemaParser.this.error("expected_empty", localName);
/*  533 */       return null;
/*      */     }
/*      */
/*      */     abstract ParsedPattern makePattern() throws SAXException;
/*      */
/*      */     void end() throws SAXException {
/*  539 */       if (this.comments != null) {
/*  540 */         if (this.annotations == null) {
/*  541 */           this.annotations = SchemaParser.this.schemaBuilder.makeAnnotations(null, SchemaParser.this.getContext());
/*      */         }
/*  543 */         this.annotations.addComment(this.comments);
/*  544 */         this.comments = null;
/*      */       }
/*  546 */       this.parent.endChild(makePattern());
/*      */     }
/*      */   }
/*      */
/*      */   abstract class PatternContainerState
/*      */     extends State
/*      */   {
/*      */     List<ParsedPattern> childPatterns;
/*      */
/*      */     State createChildState(String localName) throws SAXException {
/*  556 */       State state = (State)SchemaParser.this.patternTable.get(localName);
/*  557 */       if (state == null) {
/*  558 */         SchemaParser.this.error("expected_pattern", localName);
/*  559 */         return null;
/*      */       }
/*  561 */       return state.create();
/*      */     }
/*      */
/*      */     ParsedPattern buildPattern(List<ParsedPattern> patterns, Location loc, Annotations anno) throws SAXException {
/*  565 */       if (patterns.size() == 1 && anno == null) {
/*  566 */         return patterns.get(0);
/*      */       }
/*  568 */       return SchemaParser.this.schemaBuilder.makeGroup(patterns, loc, anno);
/*      */     }
/*      */
/*      */
/*      */     void endChild(ParsedPattern pattern) {
/*  573 */       if (this.childPatterns == null) {
/*  574 */         this.childPatterns = new ArrayList<>(5);
/*      */       }
/*  576 */       this.childPatterns.add(pattern);
/*      */     }
/*      */
/*      */
/*      */
/*      */     void endForeignChild(ParsedElementAnnotation ea) {
/*  582 */       super.endForeignChild(ea);
/*  583 */       if (this.childPatterns != null) {
/*  584 */         int idx = this.childPatterns.size() - 1;
/*  585 */         this.childPatterns.set(idx, SchemaParser.this.schemaBuilder.annotateAfter(this.childPatterns.get(idx), ea));
/*      */       }
/*      */     }
/*      */
/*      */     void end() throws SAXException {
/*  590 */       if (this.childPatterns == null) {
/*  591 */         SchemaParser.this.error("missing_children");
/*  592 */         endChild(SchemaParser.this.schemaBuilder.makeErrorPattern());
/*      */       }
/*  594 */       if (this.comments != null) {
/*  595 */         int idx = this.childPatterns.size() - 1;
/*  596 */         this.childPatterns.set(idx, SchemaParser.this.schemaBuilder.commentAfter(this.childPatterns.get(idx), this.comments));
/*  597 */         this.comments = null;
/*      */       }
/*  599 */       sendPatternToParent(buildPattern(this.childPatterns, this.startLocation, this.annotations));
/*      */     }
/*      */
/*      */     void sendPatternToParent(ParsedPattern p) {
/*  603 */       this.parent.endChild(p);
/*      */     }
/*      */   }
/*      */
/*      */   class GroupState
/*      */     extends PatternContainerState {
/*      */     State create() {
/*  610 */       return new GroupState();
/*      */     }
/*      */   }
/*      */
/*      */   class ZeroOrMoreState
/*      */     extends PatternContainerState {
/*      */     State create() {
/*  617 */       return new ZeroOrMoreState();
/*      */     }
/*      */
/*      */
/*      */     ParsedPattern buildPattern(List<ParsedPattern> patterns, Location loc, Annotations anno) throws SAXException {
/*  622 */       return SchemaParser.this.schemaBuilder.makeZeroOrMore(super.buildPattern(patterns, loc, (Annotations)null), loc, anno);
/*      */     }
/*      */   }
/*      */
/*      */   class OneOrMoreState
/*      */     extends PatternContainerState {
/*      */     State create() {
/*  629 */       return new OneOrMoreState();
/*      */     }
/*      */
/*      */
/*      */     ParsedPattern buildPattern(List<ParsedPattern> patterns, Location loc, Annotations anno) throws SAXException {
/*  634 */       return SchemaParser.this.schemaBuilder.makeOneOrMore(super.buildPattern(patterns, loc, (Annotations)null), loc, anno);
/*      */     }
/*      */   }
/*      */
/*      */   class OptionalState
/*      */     extends PatternContainerState {
/*      */     State create() {
/*  641 */       return new OptionalState();
/*      */     }
/*      */
/*      */
/*      */     ParsedPattern buildPattern(List<ParsedPattern> patterns, Location loc, Annotations anno) throws SAXException {
/*  646 */       return SchemaParser.this.schemaBuilder.makeOptional(super.buildPattern(patterns, loc, (Annotations)null), loc, anno);
/*      */     }
/*      */   }
/*      */
/*      */   class ListState
/*      */     extends PatternContainerState {
/*      */     State create() {
/*  653 */       return new ListState();
/*      */     }
/*      */
/*      */
/*      */     ParsedPattern buildPattern(List<ParsedPattern> patterns, Location loc, Annotations anno) throws SAXException {
/*  658 */       return SchemaParser.this.schemaBuilder.makeList(super.buildPattern(patterns, loc, (Annotations)null), loc, anno);
/*      */     }
/*      */   }
/*      */
/*      */   class ChoiceState
/*      */     extends PatternContainerState {
/*      */     State create() {
/*  665 */       return new ChoiceState();
/*      */     }
/*      */
/*      */
/*      */     ParsedPattern buildPattern(List<ParsedPattern> patterns, Location loc, Annotations anno) throws SAXException {
/*  670 */       return SchemaParser.this.schemaBuilder.makeChoice(patterns, loc, anno);
/*      */     }
/*      */   }
/*      */
/*      */   class InterleaveState
/*      */     extends PatternContainerState {
/*      */     State create() {
/*  677 */       return new InterleaveState();
/*      */     }
/*      */
/*      */
/*      */     ParsedPattern buildPattern(List<ParsedPattern> patterns, Location loc, Annotations anno) {
/*  682 */       return SchemaParser.this.schemaBuilder.makeInterleave(patterns, loc, anno);
/*      */     }
/*      */   }
/*      */
/*      */   class MixedState
/*      */     extends PatternContainerState {
/*      */     State create() {
/*  689 */       return new MixedState();
/*      */     }
/*      */
/*      */
/*      */     ParsedPattern buildPattern(List<ParsedPattern> patterns, Location loc, Annotations anno) throws SAXException {
/*  694 */       return SchemaParser.this.schemaBuilder.makeMixed(super.buildPattern(patterns, loc, (Annotations)null), loc, anno);
/*      */     }
/*      */   }
/*      */
/*      */
/*      */   class ElementState
/*      */     extends PatternContainerState
/*      */     implements NameClassRef
/*      */   {
/*      */     ParsedNameClass nameClass;
/*      */
/*      */     boolean nameClassWasAttribute;
/*      */
/*      */     String name;
/*      */
/*      */
/*      */     void setName(String name) {
/*  711 */       this.name = name;
/*      */     }
/*      */
/*      */     public void setNameClass(ParsedNameClass nc) {
/*  715 */       this.nameClass = nc;
/*      */     }
/*      */
/*      */
/*      */     void endAttributes() throws SAXException {
/*  720 */       if (this.name != null) {
/*  721 */         this.nameClass = SchemaParser.this.expandName(this.name, getNs(), null);
/*  722 */         this.nameClassWasAttribute = true;
/*      */       } else {
/*  724 */         (new NameClassChildState(this, this)).set();
/*      */       }
/*      */     }
/*      */
/*      */     State create() {
/*  729 */       return new ElementState();
/*      */     }
/*      */
/*      */
/*      */     ParsedPattern buildPattern(List<ParsedPattern> patterns, Location loc, Annotations anno) throws SAXException {
/*  734 */       return SchemaParser.this.schemaBuilder.makeElement(this.nameClass, super.buildPattern(patterns, loc, (Annotations)null), loc, anno);
/*      */     }
/*      */
/*      */
/*      */     void endForeignChild(ParsedElementAnnotation ea) {
/*  739 */       if (this.nameClassWasAttribute || this.childPatterns != null || this.nameClass == null) {
/*  740 */         super.endForeignChild(ea);
/*      */       } else {
/*  742 */         this.nameClass = SchemaParser.this.nameClassBuilder.annotateAfter(this.nameClass, ea);
/*      */       }
/*      */     }
/*      */   }
/*      */
/*      */   class RootState
/*      */     extends PatternContainerState
/*      */   {
/*      */     IncludedGrammar grammar;
/*      */
/*      */     RootState() {}
/*      */
/*      */     RootState(IncludedGrammar grammar, Scope scope, String ns) {
/*  755 */       this.grammar = grammar;
/*  756 */       this.scope = scope;
/*  757 */       this.nsInherit = ns;
/*  758 */       this.datatypeLibrary = "";
/*      */     }
/*      */
/*      */     State create() {
/*  762 */       return new RootState();
/*      */     }
/*      */
/*      */
/*      */     State createChildState(String localName) throws SAXException {
/*  767 */       if (this.grammar == null) {
/*  768 */         return super.createChildState(localName);
/*      */       }
/*  770 */       if (localName.equals("grammar")) {
/*  771 */         return new MergeGrammarState(this.grammar);
/*      */       }
/*  773 */       SchemaParser.this.error("expected_grammar", localName);
/*  774 */       return null;
/*      */     }
/*      */
/*      */
/*      */     void checkForeignElement() throws SAXException {
/*  779 */       SchemaParser.this.error("root_bad_namespace_uri", "http://relaxng.org/ns/structure/1.0");
/*      */     }
/*      */
/*      */
/*      */     void endChild(ParsedPattern pattern) {
/*  784 */       SchemaParser.this.startPattern = pattern;
/*      */     }
/*      */
/*      */
/*      */     boolean isRelaxNGElement(String uri) throws SAXException {
/*  789 */       if (!uri.startsWith(SchemaParser.relaxngURIPrefix)) {
/*  790 */         return false;
/*      */       }
/*  792 */       if (!uri.equals("http://relaxng.org/ns/structure/1.0")) {
/*  793 */         SchemaParser.this.warning("wrong_uri_version", "http://relaxng.org/ns/structure/1.0"
/*  794 */             .substring(SchemaParser.relaxngURIPrefix.length()), uri
/*  795 */             .substring(SchemaParser.relaxngURIPrefix.length()));
/*      */       }
/*  797 */       SchemaParser.this.relaxngURI = uri;
/*  798 */       return true;
/*      */     }
/*      */   }
/*      */
/*      */   class NotAllowedState
/*      */     extends EmptyContentState {
/*      */     State create() {
/*  805 */       return new NotAllowedState();
/*      */     }
/*      */
/*      */     ParsedPattern makePattern() {
/*  809 */       return SchemaParser.this.schemaBuilder.makeNotAllowed(this.startLocation, this.annotations);
/*      */     }
/*      */   }
/*      */
/*      */   class EmptyState
/*      */     extends EmptyContentState {
/*      */     State create() {
/*  816 */       return new EmptyState();
/*      */     }
/*      */
/*      */     ParsedPattern makePattern() {
/*  820 */       return SchemaParser.this.schemaBuilder.makeEmpty(this.startLocation, this.annotations);
/*      */     }
/*      */   }
/*      */
/*      */   class TextState
/*      */     extends EmptyContentState {
/*      */     State create() {
/*  827 */       return new TextState();
/*      */     }
/*      */
/*      */     ParsedPattern makePattern() {
/*  831 */       return SchemaParser.this.schemaBuilder.makeText(this.startLocation, this.annotations);
/*      */     } }
/*      */   class ValueState extends EmptyContentState { final StringBuffer buf;
/*      */     String type;
/*      */
/*      */     ValueState() {
/*  837 */       this.buf = new StringBuffer();
/*      */     }
/*      */
/*      */     State create() {
/*  841 */       return new ValueState();
/*      */     }
/*      */
/*      */
/*      */     void setOtherAttribute(String name, String value) throws SAXException {
/*  846 */       if (name.equals("type")) {
/*  847 */         this.type = SchemaParser.this.checkNCName(value.trim());
/*      */       } else {
/*  849 */         super.setOtherAttribute(name, value);
/*      */       }
/*      */     }
/*      */
/*      */
/*      */     public void characters(char[] ch, int start, int len) {
/*  855 */       this.buf.append(ch, start, len);
/*      */     }
/*      */
/*      */
/*      */     void checkForeignElement() throws SAXException {
/*  860 */       SchemaParser.this.error("value_contains_foreign_element");
/*      */     }
/*      */
/*      */     ParsedPattern makePattern() throws SAXException {
/*  864 */       if (this.type == null) {
/*  865 */         return makePattern("", "token");
/*      */       }
/*  867 */       return makePattern(this.datatypeLibrary, this.type);
/*      */     }
/*      */
/*      */
/*      */
/*      */     void end() throws SAXException {
/*  873 */       mergeLeadingComments();
/*  874 */       super.end();
/*      */     }
/*      */
/*      */     ParsedPattern makePattern(String datatypeLibrary, String type) {
/*  878 */       return SchemaParser.this.schemaBuilder.makeValue(datatypeLibrary, type, this.buf
/*      */
/*  880 */           .toString(), SchemaParser.this
/*  881 */           .getContext(),
/*  882 */           getNs(), this.startLocation, this.annotations);
/*      */     } }
/*      */
/*      */   class DataState extends State {
/*      */     String type;
/*      */     ParsedPattern except;
/*      */     DataPatternBuilder dpb;
/*      */
/*      */     DataState() {
/*  891 */       this.except = null;
/*  892 */       this.dpb = null;
/*      */     }
/*      */     State create() {
/*  895 */       return new DataState();
/*      */     }
/*      */
/*      */     State createChildState(String localName) throws SAXException {
/*  899 */       if (localName.equals("param")) {
/*  900 */         if (this.except != null) {
/*  901 */           SchemaParser.this.error("param_after_except");
/*      */         }
/*  903 */         return new ParamState(this.dpb);
/*      */       }
/*  905 */       if (localName.equals("except")) {
/*  906 */         if (this.except != null) {
/*  907 */           SchemaParser.this.error("multiple_except");
/*      */         }
/*  909 */         return new ChoiceState();
/*      */       }
/*  911 */       SchemaParser.this.error("expected_param_except", localName);
/*  912 */       return null;
/*      */     }
/*      */
/*      */
/*      */     void setOtherAttribute(String name, String value) throws SAXException {
/*  917 */       if (name.equals("type")) {
/*  918 */         this.type = SchemaParser.this.checkNCName(value.trim());
/*      */       } else {
/*  920 */         super.setOtherAttribute(name, value);
/*      */       }
/*      */     }
/*      */
/*      */
/*      */     void endAttributes() throws SAXException {
/*  926 */       if (this.type == null) {
/*  927 */         SchemaParser.this.error("missing_type_attribute");
/*      */       } else {
/*  929 */         this.dpb = SchemaParser.this.schemaBuilder.makeDataPatternBuilder(this.datatypeLibrary, this.type, this.startLocation);
/*      */       }
/*      */     }
/*      */
/*      */     void end() throws SAXException {
/*      */       ParsedPattern p;
/*  935 */       if (this.dpb != null) {
/*  936 */         if (this.except != null) {
/*  937 */           p = this.dpb.makePattern(this.except, this.startLocation, this.annotations);
/*      */         } else {
/*  939 */           p = this.dpb.makePattern(this.startLocation, this.annotations);
/*      */         }
/*      */       } else {
/*  942 */         p = SchemaParser.this.schemaBuilder.makeErrorPattern();
/*      */       }
/*      */
/*  945 */       this.parent.endChild(p);
/*      */     }
/*      */
/*      */
/*      */     void endChild(ParsedPattern pattern) {
/*  950 */       this.except = pattern;
/*      */     }
/*      */   }
/*      */
/*      */   class ParamState
/*      */     extends State {
/*  956 */     private final StringBuffer buf = new StringBuffer();
/*      */     private final DataPatternBuilder dpb;
/*      */     private String name;
/*      */
/*      */     ParamState(DataPatternBuilder dpb) {
/*  961 */       this.dpb = dpb;
/*      */     }
/*      */
/*      */     State create() {
/*  965 */       return new ParamState(null);
/*      */     }
/*      */
/*      */
/*      */     void setName(String name) throws SAXException {
/*  970 */       this.name = SchemaParser.this.checkNCName(name);
/*      */     }
/*      */
/*      */
/*      */     void endAttributes() throws SAXException {
/*  975 */       if (this.name == null) {
/*  976 */         SchemaParser.this.error("missing_name_attribute");
/*      */       }
/*      */     }
/*      */
/*      */     State createChildState(String localName) throws SAXException {
/*  981 */       SchemaParser.this.error("expected_empty", localName);
/*  982 */       return null;
/*      */     }
/*      */
/*      */
/*      */     public void characters(char[] ch, int start, int len) {
/*  987 */       this.buf.append(ch, start, len);
/*      */     }
/*      */
/*      */
/*      */     void checkForeignElement() throws SAXException {
/*  992 */       SchemaParser.this.error("param_contains_foreign_element");
/*      */     }
/*      */
/*      */     void end() throws SAXException {
/*  996 */       if (this.name == null) {
/*      */         return;
/*      */       }
/*  999 */       if (this.dpb == null) {
/*      */         return;
/*      */       }
/* 1002 */       mergeLeadingComments();
/* 1003 */       this.dpb.addParam(this.name, this.buf.toString(), SchemaParser.this.getContext(), getNs(), this.startLocation, this.annotations);
/*      */     }
/*      */   }
/*      */
/*      */   class AttributeState
/*      */     extends PatternContainerState implements NameClassRef {
/*      */     ParsedNameClass nameClass;
/*      */     boolean nameClassWasAttribute;
/*      */     String name;
/*      */
/*      */     State create() {
/* 1014 */       return new AttributeState();
/*      */     }
/*      */
/*      */
/*      */     void setName(String name) {
/* 1019 */       this.name = name;
/*      */     }
/*      */
/*      */     public void setNameClass(ParsedNameClass nc) {
/* 1023 */       this.nameClass = nc;
/*      */     }
/*      */
/*      */
/*      */     void endAttributes() throws SAXException {
/* 1028 */       if (this.name != null) {
/*      */         String nsUse;
/* 1030 */         if (this.ns != null) {
/* 1031 */           nsUse = this.ns;
/*      */         } else {
/* 1033 */           nsUse = "";
/*      */         }
/* 1035 */         this.nameClass = SchemaParser.this.expandName(this.name, nsUse, null);
/* 1036 */         this.nameClassWasAttribute = true;
/*      */       } else {
/* 1038 */         (new NameClassChildState(this, this)).set();
/*      */       }
/*      */     }
/*      */
/*      */
/*      */     void endForeignChild(ParsedElementAnnotation ea) {
/* 1044 */       if (this.nameClassWasAttribute || this.childPatterns != null || this.nameClass == null) {
/* 1045 */         super.endForeignChild(ea);
/*      */       } else {
/* 1047 */         this.nameClass = SchemaParser.this.nameClassBuilder.annotateAfter(this.nameClass, ea);
/*      */       }
/*      */     }
/*      */
/*      */
/*      */     void end() throws SAXException {
/* 1053 */       if (this.childPatterns == null) {
/* 1054 */         endChild(SchemaParser.this.schemaBuilder.makeText(this.startLocation, null));
/*      */       }
/* 1056 */       super.end();
/*      */     }
/*      */
/*      */
/*      */     ParsedPattern buildPattern(List<ParsedPattern> patterns, Location loc, Annotations anno) throws SAXException {
/* 1061 */       return SchemaParser.this.schemaBuilder.makeAttribute(this.nameClass, super.buildPattern(patterns, loc, (Annotations)null), loc, anno);
/*      */     }
/*      */
/*      */
/*      */     State createChildState(String localName) throws SAXException {
/* 1066 */       State tem = super.createChildState(localName);
/* 1067 */       if (tem != null && this.childPatterns != null) {
/* 1068 */         SchemaParser.this.error("attribute_multi_pattern");
/*      */       }
/* 1070 */       return tem;
/*      */     }
/*      */   }
/*      */
/*      */   abstract class SinglePatternContainerState
/*      */     extends PatternContainerState
/*      */   {
/*      */     State createChildState(String localName) throws SAXException {
/* 1078 */       if (this.childPatterns == null) {
/* 1079 */         return super.createChildState(localName);
/*      */       }
/* 1081 */       SchemaParser.this.error("too_many_children");
/* 1082 */       return null;
/*      */     }
/*      */   }
/*      */
/*      */   class GrammarSectionState
/*      */     extends State
/*      */   {
/*      */     GrammarSection section;
/*      */
/*      */     GrammarSectionState() {}
/*      */
/*      */     GrammarSectionState(GrammarSection section) {
/* 1094 */       this.section = section;
/*      */     }
/*      */
/*      */     State create() {
/* 1098 */       return new GrammarSectionState(null);
/*      */     }
/*      */
/*      */     State createChildState(String localName) throws SAXException {
/* 1102 */       if (localName.equals("define")) {
/* 1103 */         return new DefineState(this.section);
/*      */       }
/* 1105 */       if (localName.equals("start")) {
/* 1106 */         return new StartState(this.section);
/*      */       }
/* 1108 */       if (localName.equals("include")) {
/* 1109 */         Include include = this.section.makeInclude();
/* 1110 */         if (include != null) {
/* 1111 */           return new IncludeState(include);
/*      */         }
/*      */       }
/* 1114 */       if (localName.equals("div")) {
/* 1115 */         return new DivState(this.section.makeDiv());
/*      */       }
/* 1117 */       SchemaParser.this.error("expected_define", localName);
/*      */
/* 1119 */       return null;
/*      */     }
/*      */
/*      */     void end() throws SAXException {
/* 1123 */       if (this.comments != null) {
/* 1124 */         this.section.topLevelComment(this.comments);
/* 1125 */         this.comments = null;
/*      */       }
/*      */     }
/*      */
/*      */
/*      */     void endForeignChild(ParsedElementAnnotation ea) {
/* 1131 */       this.section.topLevelAnnotation(ea);
/*      */     }
/*      */   }
/*      */
/*      */   class DivState
/*      */     extends GrammarSectionState {
/*      */     final Div div;
/*      */
/*      */     DivState(Div div) {
/* 1140 */       super((GrammarSection)div);
/* 1141 */       this.div = div;
/*      */     }
/*      */
/*      */
/*      */     void end() throws SAXException {
/* 1146 */       super.end();
/* 1147 */       this.div.endDiv(this.startLocation, this.annotations);
/*      */     }
/*      */   }
/*      */
/*      */   class IncludeState
/*      */     extends GrammarSectionState {
/*      */     String href;
/*      */     final Include include;
/*      */
/*      */     IncludeState(Include include) {
/* 1157 */       super((GrammarSection)include);
/* 1158 */       this.include = include;
/*      */     }
/*      */
/*      */
/*      */     void setOtherAttribute(String name, String value) throws SAXException {
/* 1163 */       if (name.equals("href")) {
/* 1164 */         this.href = value;
/* 1165 */         SchemaParser.this.checkUri(this.href);
/*      */       } else {
/* 1167 */         super.setOtherAttribute(name, value);
/*      */       }
/*      */     }
/*      */
/*      */
/*      */     void endAttributes() throws SAXException {
/* 1173 */       if (this.href == null) {
/* 1174 */         SchemaParser.this.error("missing_href_attribute");
/*      */       } else {
/* 1176 */         this.href = SchemaParser.this.resolve(this.href);
/*      */       }
/*      */     }
/*      */
/*      */
/*      */     void end() throws SAXException {
/* 1182 */       super.end();
/* 1183 */       if (this.href != null) {
/*      */         try {
/* 1185 */           this.include.endInclude(SchemaParser.this.parseable, this.href, getNs(), this.startLocation, this.annotations);
/* 1186 */         } catch (IllegalSchemaException illegalSchemaException) {}
/*      */       }
/*      */     }
/*      */   }
/*      */
/*      */   class MergeGrammarState
/*      */     extends GrammarSectionState
/*      */   {
/*      */     final IncludedGrammar grammar;
/*      */
/*      */     MergeGrammarState(IncludedGrammar grammar) {
/* 1197 */       super((GrammarSection)grammar);
/* 1198 */       this.grammar = grammar;
/*      */     }
/*      */
/*      */
/*      */     void end() throws SAXException {
/* 1203 */       super.end();
/* 1204 */       this.parent.endChild(this.grammar.endIncludedGrammar(this.startLocation, this.annotations));
/*      */     }
/*      */   }
/*      */
/*      */   class GrammarState
/*      */     extends GrammarSectionState
/*      */   {
/*      */     Grammar grammar;
/*      */
/*      */     void setParent(State parent) {
/* 1214 */       super.setParent(parent);
/* 1215 */       this.grammar = SchemaParser.this.schemaBuilder.makeGrammar(this.scope);
/* 1216 */       this.section = (GrammarSection)this.grammar;
/* 1217 */       this.scope = (Scope)this.grammar;
/*      */     }
/*      */
/*      */
/*      */     State create() {
/* 1222 */       return new GrammarState();
/*      */     }
/*      */
/*      */
/*      */     void end() throws SAXException {
/* 1227 */       super.end();
/* 1228 */       this.parent.endChild(this.grammar.endGrammar(this.startLocation, this.annotations));
/*      */     }
/*      */   }
/*      */
/*      */   class RefState
/*      */     extends EmptyContentState {
/*      */     String name;
/*      */
/*      */     State create() {
/* 1237 */       return new RefState();
/*      */     }
/*      */
/*      */
/*      */     void endAttributes() throws SAXException {
/* 1242 */       if (this.name == null) {
/* 1243 */         SchemaParser.this.error("missing_name_attribute");
/*      */       }
/*      */     }
/*      */
/*      */
/*      */     void setName(String name) throws SAXException {
/* 1249 */       this.name = SchemaParser.this.checkNCName(name);
/*      */     }
/*      */
/*      */     ParsedPattern makePattern() throws SAXException {
/* 1253 */       if (this.name == null) {
/* 1254 */         return SchemaParser.this.schemaBuilder.makeErrorPattern();
/*      */       }
/* 1256 */       if (this.scope == null) {
/* 1257 */         SchemaParser.this.error("ref_outside_grammar", this.name);
/* 1258 */         return SchemaParser.this.schemaBuilder.makeErrorPattern();
/*      */       }
/* 1260 */       return this.scope.makeRef(this.name, this.startLocation, this.annotations);
/*      */     }
/*      */   }
/*      */
/*      */
/*      */   class ParentRefState
/*      */     extends RefState
/*      */   {
/*      */     State create() {
/* 1269 */       return new ParentRefState();
/*      */     }
/*      */
/*      */
/*      */     ParsedPattern makePattern() throws SAXException {
/* 1274 */       if (this.name == null) {
/* 1275 */         return SchemaParser.this.schemaBuilder.makeErrorPattern();
/*      */       }
/* 1277 */       if (this.scope == null) {
/* 1278 */         SchemaParser.this.error("parent_ref_outside_grammar", this.name);
/* 1279 */         return SchemaParser.this.schemaBuilder.makeErrorPattern();
/*      */       }
/* 1281 */       return this.scope.makeParentRef(this.name, this.startLocation, this.annotations);
/*      */     }
/*      */   }
/*      */
/*      */   class ExternalRefState
/*      */     extends EmptyContentState
/*      */   {
/*      */     String href;
/*      */
/*      */     State create() {
/* 1291 */       return new ExternalRefState();
/*      */     }
/*      */
/*      */
/*      */     void setOtherAttribute(String name, String value) throws SAXException {
/* 1296 */       if (name.equals("href")) {
/* 1297 */         this.href = value;
/* 1298 */         SchemaParser.this.checkUri(this.href);
/*      */       } else {
/* 1300 */         super.setOtherAttribute(name, value);
/*      */       }
/*      */     }
/*      */
/*      */
/*      */     void endAttributes() throws SAXException {
/* 1306 */       if (this.href == null) {
/* 1307 */         SchemaParser.this.error("missing_href_attribute");
/*      */       } else {
/* 1309 */         this.href = SchemaParser.this.resolve(this.href);
/*      */       }
/*      */     }
/*      */
/*      */     ParsedPattern makePattern() {
/* 1314 */       if (this.href != null) {
/*      */         try {
/* 1316 */           return SchemaParser.this.schemaBuilder.makeExternalRef(SchemaParser.this.parseable, this.href,
/*      */
/* 1318 */               getNs(), this.scope, this.startLocation, this.annotations);
/*      */
/*      */
/*      */         }
/* 1322 */         catch (IllegalSchemaException illegalSchemaException) {}
/*      */       }
/*      */
/* 1325 */       return SchemaParser.this.schemaBuilder.makeErrorPattern();
/*      */     }
/*      */   }
/*      */
/*      */   abstract class DefinitionState
/*      */     extends PatternContainerState {
/* 1331 */     GrammarSection.Combine combine = null;
/*      */     final GrammarSection section;
/*      */
/*      */     DefinitionState(GrammarSection section) {
/* 1335 */       this.section = section;
/*      */     }
/*      */
/*      */
/*      */     void setOtherAttribute(String name, String value) throws SAXException {
/* 1340 */       if (name.equals("combine")) {
/* 1341 */         value = value.trim();
/* 1342 */         if (value.equals("choice")) {
/* 1343 */           this.combine = GrammarSection.COMBINE_CHOICE;
/* 1344 */         } else if (value.equals("interleave")) {
/* 1345 */           this.combine = GrammarSection.COMBINE_INTERLEAVE;
/*      */         } else {
/* 1347 */           SchemaParser.this.error("combine_attribute_bad_value", value);
/*      */         }
/*      */       } else {
/* 1350 */         super.setOtherAttribute(name, value);
/*      */       }
/*      */     }
/*      */
/*      */
/*      */     ParsedPattern buildPattern(List<ParsedPattern> patterns, Location loc, Annotations anno) throws SAXException {
/* 1356 */       return super.buildPattern(patterns, loc, (Annotations)null);
/*      */     }
/*      */   }
/*      */
/*      */   class DefineState
/*      */     extends DefinitionState {
/*      */     String name;
/*      */
/*      */     DefineState(GrammarSection section) {
/* 1365 */       super(section);
/*      */     }
/*      */
/*      */     State create() {
/* 1369 */       return new DefineState(null);
/*      */     }
/*      */
/*      */
/*      */     void setName(String name) throws SAXException {
/* 1374 */       this.name = SchemaParser.this.checkNCName(name);
/*      */     }
/*      */
/*      */
/*      */     void endAttributes() throws SAXException {
/* 1379 */       if (this.name == null) {
/* 1380 */         SchemaParser.this.error("missing_name_attribute");
/*      */       }
/*      */     }
/*      */
/*      */
/*      */     void sendPatternToParent(ParsedPattern p) {
/* 1386 */       if (this.name != null)
/* 1387 */         this.section.define(this.name, this.combine, p, this.startLocation, this.annotations);
/*      */     }
/*      */   }
/*      */
/*      */   class StartState
/*      */     extends DefinitionState
/*      */   {
/*      */     StartState(GrammarSection section) {
/* 1395 */       super(section);
/*      */     }
/*      */
/*      */     State create() {
/* 1399 */       return new StartState(null);
/*      */     }
/*      */
/*      */
/*      */     void sendPatternToParent(ParsedPattern p) {
/* 1404 */       this.section.define("\000#start\000", this.combine, p, this.startLocation, this.annotations);
/*      */     }
/*      */
/*      */
/*      */     State createChildState(String localName) throws SAXException {
/* 1409 */       State tem = super.createChildState(localName);
/* 1410 */       if (tem != null && this.childPatterns != null) {
/* 1411 */         SchemaParser.this.error("start_multi_pattern");
/*      */       }
/* 1413 */       return tem;
/*      */     }
/*      */   }
/*      */
/*      */   abstract class NameClassContainerState
/*      */     extends State {
/*      */     State createChildState(String localName) throws SAXException {
/* 1420 */       State state = (State)SchemaParser.this.nameClassTable.get(localName);
/* 1421 */       if (state == null) {
/* 1422 */         SchemaParser.this.error("expected_name_class", localName);
/* 1423 */         return null;
/*      */       }
/* 1425 */       return state.create();
/*      */     }
/*      */   }
/*      */
/*      */   class NameClassChildState
/*      */     extends NameClassContainerState {
/*      */     final State prevState;
/*      */     final NameClassRef nameClassRef;
/*      */
/*      */     State create() {
/* 1435 */       return null;
/*      */     }
/*      */
/*      */     NameClassChildState(State prevState, NameClassRef nameClassRef) {
/* 1439 */       this.prevState = prevState;
/* 1440 */       this.nameClassRef = nameClassRef;
/* 1441 */       setParent(prevState.parent);
/* 1442 */       this.ns = prevState.ns;
/*      */     }
/*      */
/*      */
/*      */     void endChild(ParsedNameClass nameClass) {
/* 1447 */       this.nameClassRef.setNameClass(nameClass);
/* 1448 */       this.prevState.set();
/*      */     }
/*      */
/*      */
/*      */     void endForeignChild(ParsedElementAnnotation ea) {
/* 1453 */       this.prevState.endForeignChild(ea);
/*      */     }
/*      */
/*      */     void end() throws SAXException {
/* 1457 */       this.nameClassRef.setNameClass(SchemaParser.this.nameClassBuilder.makeErrorNameClass());
/* 1458 */       SchemaParser.this.error("missing_name_class");
/* 1459 */       this.prevState.set();
/* 1460 */       this.prevState.end();
/*      */     }
/*      */   }
/*      */
/*      */   abstract class NameClassBaseState
/*      */     extends State {
/*      */     abstract ParsedNameClass makeNameClass() throws SAXException;
/*      */
/*      */     void end() throws SAXException {
/* 1469 */       this.parent.endChild(makeNameClass());
/*      */     } }
/*      */
/*      */   class NameState extends NameClassBaseState { final StringBuffer buf;
/*      */
/*      */     NameState() {
/* 1475 */       this.buf = new StringBuffer();
/*      */     }
/*      */     State createChildState(String localName) throws SAXException {
/* 1478 */       SchemaParser.this.error("expected_name", localName);
/* 1479 */       return null;
/*      */     }
/*      */
/*      */     State create() {
/* 1483 */       return new NameState();
/*      */     }
/*      */
/*      */
/*      */     public void characters(char[] ch, int start, int len) {
/* 1488 */       this.buf.append(ch, start, len);
/*      */     }
/*      */
/*      */
/*      */     void checkForeignElement() throws SAXException {
/* 1493 */       SchemaParser.this.error("name_contains_foreign_element");
/*      */     }
/*      */
/*      */     ParsedNameClass makeNameClass() throws SAXException {
/* 1497 */       mergeLeadingComments();
/* 1498 */       return SchemaParser.this.expandName(this.buf.toString().trim(), getNs(), this.annotations);
/*      */     } }
/*      */
/*      */
/*      */   class AnyNameState
/*      */     extends NameClassBaseState
/*      */   {
/*      */     ParsedNameClass except;
/*      */
/*      */     AnyNameState() {
/* 1508 */       this.except = null;
/*      */     }
/*      */     State create() {
/* 1511 */       return new AnyNameState();
/*      */     }
/*      */
/*      */     State createChildState(String localName) throws SAXException {
/* 1515 */       if (localName.equals("except")) {
/* 1516 */         if (this.except != null) {
/* 1517 */           SchemaParser.this.error("multiple_except");
/*      */         }
/* 1519 */         return new NameClassChoiceState(getContext());
/*      */       }
/* 1521 */       SchemaParser.this.error("expected_except", localName);
/* 1522 */       return null;
/*      */     }
/*      */
/*      */     int getContext() {
/* 1526 */       return 1;
/*      */     }
/*      */
/*      */     ParsedNameClass makeNameClass() {
/* 1530 */       if (this.except == null) {
/* 1531 */         return makeNameClassNoExcept();
/*      */       }
/* 1533 */       return makeNameClassExcept(this.except);
/*      */     }
/*      */
/*      */
/*      */     ParsedNameClass makeNameClassNoExcept() {
/* 1538 */       return SchemaParser.this.nameClassBuilder.makeAnyName(this.startLocation, this.annotations);
/*      */     }
/*      */
/*      */     ParsedNameClass makeNameClassExcept(ParsedNameClass except) {
/* 1542 */       return SchemaParser.this.nameClassBuilder.makeAnyName(except, this.startLocation, this.annotations);
/*      */     }
/*      */
/*      */
/*      */     void endChild(ParsedNameClass nameClass) {
/* 1547 */       this.except = nameClass;
/*      */     }
/*      */   }
/*      */
/*      */   class NsNameState
/*      */     extends AnyNameState
/*      */   {
/*      */     State create() {
/* 1555 */       return new NsNameState();
/*      */     }
/*      */
/*      */
/*      */     ParsedNameClass makeNameClassNoExcept() {
/* 1560 */       return SchemaParser.this.nameClassBuilder.makeNsName(getNs(), null, null);
/*      */     }
/*      */
/*      */
/*      */     ParsedNameClass makeNameClassExcept(ParsedNameClass except) {
/* 1565 */       return SchemaParser.this.nameClassBuilder.makeNsName(getNs(), except, null, null);
/*      */     }
/*      */
/*      */
/*      */     int getContext() {
/* 1570 */       return 2;
/*      */     }
/*      */   }
/*      */
/*      */   class NameClassChoiceState
/*      */     extends NameClassContainerState {
/*      */     private ParsedNameClass[] nameClasses;
/*      */     private int nNameClasses;
/*      */     private int context;
/*      */
/*      */     NameClassChoiceState() {
/* 1581 */       this.context = 0;
/*      */     }
/*      */
/*      */     NameClassChoiceState(int context) {
/* 1585 */       this.context = context;
/*      */     }
/*      */
/*      */
/*      */     void setParent(State parent) {
/* 1590 */       super.setParent(parent);
/* 1591 */       if (parent instanceof NameClassChoiceState) {
/* 1592 */         this.context = ((NameClassChoiceState)parent).context;
/*      */       }
/*      */     }
/*      */
/*      */     State create() {
/* 1597 */       return new NameClassChoiceState();
/*      */     }
/*      */
/*      */
/*      */     State createChildState(String localName) throws SAXException {
/* 1602 */       if (localName.equals("anyName")) {
/* 1603 */         if (this.context >= 1) {
/* 1604 */           SchemaParser.this.error((this.context == 1) ? "any_name_except_contains_any_name" : "ns_name_except_contains_any_name");
/*      */
/*      */
/* 1607 */           return null;
/*      */         }
/* 1609 */       } else if (localName.equals("nsName") &&
/* 1610 */         this.context == 2) {
/* 1611 */         SchemaParser.this.error("ns_name_except_contains_ns_name");
/* 1612 */         return null;
/*      */       }
/*      */
/* 1615 */       return super.createChildState(localName);
/*      */     }
/*      */
/*      */
/*      */     void endChild(ParsedNameClass nc) {
/* 1620 */       if (this.nameClasses == null) {
/* 1621 */         this.nameClasses = new ParsedNameClass[5];
/* 1622 */       } else if (this.nNameClasses >= this.nameClasses.length) {
/* 1623 */         ParsedNameClass[] newNameClasses = new ParsedNameClass[this.nameClasses.length * 2];
/* 1624 */         System.arraycopy(this.nameClasses, 0, newNameClasses, 0, this.nameClasses.length);
/* 1625 */         this.nameClasses = newNameClasses;
/*      */       }
/* 1627 */       this.nameClasses[this.nNameClasses++] = nc;
/*      */     }
/*      */
/*      */
/*      */     void endForeignChild(ParsedElementAnnotation ea) {
/* 1632 */       if (this.nNameClasses == 0) {
/* 1633 */         super.endForeignChild(ea);
/*      */       } else {
/* 1635 */         this.nameClasses[this.nNameClasses - 1] = SchemaParser.this.nameClassBuilder.annotateAfter(this.nameClasses[this.nNameClasses - 1], ea);
/*      */       }
/*      */     }
/*      */
/*      */     void end() throws SAXException {
/* 1640 */       if (this.nNameClasses == 0) {
/* 1641 */         SchemaParser.this.error("missing_name_class");
/* 1642 */         this.parent.endChild(SchemaParser.this.nameClassBuilder.makeErrorNameClass());
/*      */         return;
/*      */       }
/* 1645 */       if (this.comments != null) {
/* 1646 */         this.nameClasses[this.nNameClasses - 1] = SchemaParser.this.nameClassBuilder.commentAfter(this.nameClasses[this.nNameClasses - 1], this.comments);
/* 1647 */         this.comments = null;
/*      */       }
/* 1649 */       this.parent.endChild(SchemaParser.this.nameClassBuilder.makeChoice(Arrays.asList(this.nameClasses).subList(0, this.nNameClasses), this.startLocation, this.annotations));
/*      */     }
/*      */   }
/*      */
/*      */   private void initPatternTable() {
/* 1654 */     this.patternTable = new Hashtable<>();
/* 1655 */     this.patternTable.put("zeroOrMore", new ZeroOrMoreState());
/* 1656 */     this.patternTable.put("oneOrMore", new OneOrMoreState());
/* 1657 */     this.patternTable.put("optional", new OptionalState());
/* 1658 */     this.patternTable.put("list", new ListState());
/* 1659 */     this.patternTable.put("choice", new ChoiceState());
/* 1660 */     this.patternTable.put("interleave", new InterleaveState());
/* 1661 */     this.patternTable.put("group", new GroupState());
/* 1662 */     this.patternTable.put("mixed", new MixedState());
/* 1663 */     this.patternTable.put("element", new ElementState());
/* 1664 */     this.patternTable.put("attribute", new AttributeState());
/* 1665 */     this.patternTable.put("empty", new EmptyState());
/* 1666 */     this.patternTable.put("text", new TextState());
/* 1667 */     this.patternTable.put("value", new ValueState());
/* 1668 */     this.patternTable.put("data", new DataState());
/* 1669 */     this.patternTable.put("notAllowed", new NotAllowedState());
/* 1670 */     this.patternTable.put("grammar", new GrammarState());
/* 1671 */     this.patternTable.put("ref", new RefState());
/* 1672 */     this.patternTable.put("parentRef", new ParentRefState());
/* 1673 */     this.patternTable.put("externalRef", new ExternalRefState());
/*      */   }
/*      */
/*      */   private void initNameClassTable() {
/* 1677 */     this.nameClassTable = new Hashtable<>();
/* 1678 */     this.nameClassTable.put("name", new NameState());
/* 1679 */     this.nameClassTable.put("anyName", new AnyNameState());
/* 1680 */     this.nameClassTable.put("nsName", new NsNameState());
/* 1681 */     this.nameClassTable.put("choice", new NameClassChoiceState());
/*      */   }
/*      */
/*      */   public ParsedPattern getParsedPattern() throws IllegalSchemaException {
/* 1685 */     if (this.hadError) {
/* 1686 */       throw new IllegalSchemaException();
/*      */     }
/* 1688 */     return this.startPattern;
/*      */   }
/*      */
/*      */   private void error(String key) throws SAXException {
/* 1692 */     error(key, this.locator);
/*      */   }
/*      */
/*      */   private void error(String key, String arg) throws SAXException {
/* 1696 */     error(key, arg, this.locator);
/*      */   }
/*      */
/*      */   void error(String key, String arg1, String arg2) throws SAXException {
/* 1700 */     error(key, arg1, arg2, this.locator);
/*      */   }
/*      */
/*      */   private void error(String key, Locator loc) throws SAXException {
/* 1704 */     error(new SAXParseException(localizer.message(key), loc));
/*      */   }
/*      */
/*      */   private void error(String key, String arg, Locator loc) throws SAXException {
/* 1708 */     error(new SAXParseException(localizer.message(key, arg), loc));
/*      */   }
/*      */
/*      */
/*      */   private void error(String key, String arg1, String arg2, Locator loc) throws SAXException {
/* 1713 */     error(new SAXParseException(localizer.message(key, arg1, arg2), loc));
/*      */   }
/*      */
/*      */   private void error(SAXParseException e) throws SAXException {
/* 1717 */     this.hadError = true;
/* 1718 */     if (this.eh != null) {
/* 1719 */       this.eh.error(e);
/*      */     }
/*      */   }
/*      */
/*      */   void warning(String key) throws SAXException {
/* 1724 */     warning(key, this.locator);
/*      */   }
/*      */
/*      */   private void warning(String key, String arg) throws SAXException {
/* 1728 */     warning(key, arg, this.locator);
/*      */   }
/*      */
/*      */   private void warning(String key, String arg1, String arg2) throws SAXException {
/* 1732 */     warning(key, arg1, arg2, this.locator);
/*      */   }
/*      */
/*      */   private void warning(String key, Locator loc) throws SAXException {
/* 1736 */     warning(new SAXParseException(localizer.message(key), loc));
/*      */   }
/*      */
/*      */   private void warning(String key, String arg, Locator loc) throws SAXException {
/* 1740 */     warning(new SAXParseException(localizer.message(key, arg), loc));
/*      */   }
/*      */
/*      */
/*      */   private void warning(String key, String arg1, String arg2, Locator loc) throws SAXException {
/* 1745 */     warning(new SAXParseException(localizer.message(key, arg1, arg2), loc));
/*      */   }
/*      */
/*      */   private void warning(SAXParseException e) throws SAXException {
/* 1749 */     if (this.eh != null) {
/* 1750 */       this.eh.warning(e);
/*      */     }
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   SchemaParser(SAXParseable parseable, XMLReader xr, ErrorHandler eh, SchemaBuilder schemaBuilder, IncludedGrammar grammar, Scope scope, String inheritedNs) throws SAXException {
/* 1761 */     this.parseable = parseable;
/* 1762 */     this.xr = xr;
/* 1763 */     this.eh = eh;
/* 1764 */     this.schemaBuilder = schemaBuilder;
/* 1765 */     this.nameClassBuilder = schemaBuilder.getNameClassBuilder();
/* 1766 */     if (eh != null) {
/* 1767 */       xr.setErrorHandler(eh);
/*      */     }
/* 1769 */     xr.setDTDHandler(this.context);
/* 1770 */     if (schemaBuilder.usesComments()) {
/*      */       try {
/* 1772 */         xr.setProperty("http://xml.org/sax/properties/lexical-handler", new LexicalHandlerImpl());
/* 1773 */       } catch (SAXNotRecognizedException e) {
/* 1774 */         warning("no_comment_support", xr.getClass().getName());
/* 1775 */       } catch (SAXNotSupportedException e) {
/* 1776 */         warning("no_comment_support", xr.getClass().getName());
/*      */       }
/*      */     }
/* 1779 */     initPatternTable();
/* 1780 */     initNameClassTable();
/* 1781 */     (new RootState(grammar, scope, inheritedNs)).set();
/*      */   }
/*      */
/*      */   private Context getContext() {
/* 1785 */     return this.context;
/*      */   }
/*      */   class LexicalHandlerImpl extends AbstractLexicalHandler { private boolean inDtd;
/*      */
/*      */     LexicalHandlerImpl() {
/* 1790 */       this.inDtd = false;
/*      */     }
/*      */
/*      */     public void startDTD(String s, String s1, String s2) throws SAXException {
/* 1794 */       this.inDtd = true;
/*      */     }
/*      */
/*      */
/*      */     public void endDTD() throws SAXException {
/* 1799 */       this.inDtd = false;
/*      */     }
/*      */
/*      */
/*      */     public void comment(char[] chars, int start, int length) throws SAXException {
/* 1804 */       if (!this.inDtd) {
/* 1805 */         ((CommentHandler)SchemaParser.this.xr.getContentHandler()).comment(new String(chars, start, length));
/*      */       }
/*      */     } }
/*      */
/*      */
/*      */   private ParsedNameClass expandName(String name, String ns, Annotations anno) throws SAXException {
/* 1811 */     int ic = name.indexOf(':');
/* 1812 */     if (ic == -1) {
/* 1813 */       return this.nameClassBuilder.makeName(ns, checkNCName(name), null, null, anno);
/*      */     }
/* 1815 */     String prefix = checkNCName(name.substring(0, ic));
/* 1816 */     String localName = checkNCName(name.substring(ic + 1));
/* 1817 */     for (PrefixMapping tem = this.context.prefixMapping; tem != null; tem = tem.next) {
/* 1818 */       if (tem.prefix.equals(prefix)) {
/* 1819 */         return this.nameClassBuilder.makeName(tem.uri, localName, prefix, null, anno);
/*      */       }
/*      */     }
/* 1822 */     error("undefined_prefix", prefix);
/* 1823 */     return this.nameClassBuilder.makeName("", localName, null, null, anno);
/*      */   }
/*      */
/*      */   private String findPrefix(String qName, String uri) {
/* 1827 */     String prefix = null;
/* 1828 */     if (qName == null || qName.equals("")) {
/* 1829 */       for (PrefixMapping p = this.context.prefixMapping; p != null; p = p.next) {
/* 1830 */         if (p.uri.equals(uri)) {
/* 1831 */           prefix = p.prefix;
/*      */           break;
/*      */         }
/*      */       }
/*      */     } else {
/* 1836 */       int off = qName.indexOf(':');
/* 1837 */       if (off > 0) {
/* 1838 */         prefix = qName.substring(0, off);
/*      */       }
/*      */     }
/* 1841 */     return prefix;
/*      */   }
/*      */
/*      */   private String checkNCName(String str) throws SAXException {
/* 1845 */     if (!Naming.isNcname(str)) {
/* 1846 */       error("invalid_ncname", str);
/*      */     }
/* 1848 */     return str;
/*      */   }
/*      */
/*      */   private String resolve(String systemId) throws SAXException {
/* 1852 */     if (Uri.hasFragmentId(systemId)) {
/* 1853 */       error("href_fragment_id");
/*      */     }
/* 1855 */     systemId = Uri.escapeDisallowedChars(systemId);
/* 1856 */     return Uri.resolve(this.xmlBaseHandler.getBaseUri(), systemId);
/*      */   }
/*      */
/*      */   private Location makeLocation() {
/* 1860 */     if (this.locator == null) {
/* 1861 */       return null;
/*      */     }
/* 1863 */     return this.schemaBuilder.makeLocation(this.locator.getSystemId(), this.locator
/* 1864 */         .getLineNumber(), this.locator
/* 1865 */         .getColumnNumber());
/*      */   }
/*      */
/*      */   private void checkUri(String s) throws SAXException {
/* 1869 */     if (!Uri.isValid(s))
/* 1870 */       error("invalid_uri", s);
/*      */   }
/*      */
/*      */   static interface CommentHandler {
/*      */     void comment(String param1String);
/*      */   }
/*      */
/*      */   static interface NameClassRef {
/*      */     void setNameClass(ParsedNameClass param1ParsedNameClass);
/*      */   }
/*      */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\xml\internal\rngom\parse\xml\SchemaParser.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
