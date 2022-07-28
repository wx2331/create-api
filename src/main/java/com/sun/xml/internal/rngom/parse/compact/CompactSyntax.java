/*      */ package com.sun.xml.internal.rngom.parse.compact;
/*      */ 
/*      */ import com.sun.xml.internal.rngom.ast.builder.Annotations;
/*      */ import com.sun.xml.internal.rngom.ast.builder.BuildException;
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
/*      */ import java.io.InputStream;
/*      */ import java.io.Reader;
/*      */ import java.io.UnsupportedEncodingException;
/*      */ import java.net.MalformedURLException;
/*      */ import java.net.URL;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Arrays;
/*      */ import java.util.Collections;
/*      */ import java.util.Enumeration;
/*      */ import java.util.Hashtable;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
/*      */ import org.xml.sax.ErrorHandler;
/*      */ import org.xml.sax.SAXException;
/*      */ import org.xml.sax.SAXParseException;
/*      */ import org.xml.sax.helpers.LocatorImpl;
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
/*      */ public class CompactSyntax
/*      */   implements Context, CompactSyntaxConstants
/*      */ {
/*      */   private static final int IN_ELEMENT = 0;
/*      */   private static final int IN_ATTRIBUTE = 1;
/*      */   private static final int IN_ANY_NAME = 2;
/*      */   private static final int IN_NS_NAME = 4;
/*      */   private String defaultNamespace;
/*   95 */   private String compatibilityPrefix = null;
/*      */   
/*      */   private SchemaBuilder sb;
/*      */   
/*      */   private NameClassBuilder ncb;
/*      */   
/*      */   private String sourceUri;
/*      */   private CompactParseable parseable;
/*      */   private ErrorHandler eh;
/*  104 */   private final Hashtable namespaceTable = new Hashtable<>();
/*  105 */   private final Hashtable datatypesTable = new Hashtable<>();
/*      */   private boolean hadError = false;
/*  107 */   private static final Localizer localizer = new Localizer(new Localizer(Parseable.class), CompactSyntax.class);
/*  108 */   private final Hashtable attributeNameTable = new Hashtable<>();
/*      */ 
/*      */   
/*      */   private boolean annotationsIncludeElements = false;
/*      */   
/*      */   private String inheritedNs;
/*      */   
/*      */   private CommentList topLevelComments;
/*      */ 
/*      */   
/*      */   final class LocatedString
/*      */   {
/*      */     private final String str;
/*      */     
/*      */     private final Token tok;
/*      */ 
/*      */     
/*      */     LocatedString(String str, Token tok) {
/*  126 */       this.str = str;
/*  127 */       this.tok = tok;
/*      */     }
/*      */     
/*      */     String getString() {
/*  131 */       return this.str;
/*      */     }
/*      */     
/*      */     Location getLocation() {
/*  135 */       return CompactSyntax.this.makeLocation(this.tok);
/*      */     }
/*      */     
/*      */     Token getToken() {
/*  139 */       return this.tok;
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public CompactSyntax(CompactParseable parseable, Reader r, String sourceUri, SchemaBuilder sb, ErrorHandler eh, String inheritedNs) {
/*  145 */     this(r);
/*  146 */     this.sourceUri = sourceUri;
/*  147 */     this.parseable = parseable;
/*  148 */     this.sb = sb;
/*  149 */     this.ncb = sb.getNameClassBuilder();
/*  150 */     this.eh = eh;
/*      */ 
/*      */     
/*  153 */     this.topLevelComments = sb.makeCommentList();
/*  154 */     this.inheritedNs = this.defaultNamespace = new String(inheritedNs);
/*      */   }
/*      */   
/*      */   ParsedPattern parse(Scope scope) throws IllegalSchemaException {
/*      */     try {
/*  159 */       ParsedPattern p = Input(scope);
/*  160 */       if (!this.hadError) {
/*  161 */         return p;
/*      */       }
/*  163 */     } catch (ParseException e) {
/*  164 */       error("syntax_error", e.getMessage(), e.currentToken.next);
/*      */     }
/*  166 */     catch (EscapeSyntaxException e) {
/*  167 */       reportEscapeSyntaxException(e);
/*      */     } 
/*  169 */     throw new IllegalSchemaException();
/*      */   }
/*      */   
/*      */   ParsedPattern parseInclude(IncludedGrammar g) throws IllegalSchemaException {
/*      */     try {
/*  174 */       ParsedPattern p = IncludedGrammar(g);
/*  175 */       if (!this.hadError) {
/*  176 */         return p;
/*      */       }
/*  178 */     } catch (ParseException e) {
/*  179 */       error("syntax_error", e.getMessage(), e.currentToken.next);
/*      */     }
/*  181 */     catch (EscapeSyntaxException e) {
/*  182 */       reportEscapeSyntaxException(e);
/*      */     } 
/*  184 */     throw new IllegalSchemaException();
/*      */   }
/*      */   
/*      */   private void checkNsName(int context, LocatedString ns) {
/*  188 */     if ((context & 0x4) != 0)
/*  189 */       error("ns_name_except_contains_ns_name", ns.getToken()); 
/*      */   }
/*      */   
/*      */   private void checkAnyName(int context, Token t) {
/*  193 */     if ((context & 0x4) != 0)
/*  194 */       error("ns_name_except_contains_any_name", t); 
/*  195 */     if ((context & 0x2) != 0)
/*  196 */       error("any_name_except_contains_any_name", t); 
/*      */   }
/*      */   
/*      */   private void error(String key, Token tok) {
/*  200 */     doError(localizer.message(key), tok);
/*      */   }
/*      */   
/*      */   private void error(String key, String arg, Token tok) {
/*  204 */     doError(localizer.message(key, arg), tok);
/*      */   }
/*      */   
/*      */   private void error(String key, String arg1, String arg2, Token tok) {
/*  208 */     doError(localizer.message(key, arg1, arg2), tok);
/*      */   }
/*      */   
/*      */   private void doError(String message, Token tok) {
/*  212 */     this.hadError = true;
/*  213 */     if (this.eh != null) {
/*  214 */       LocatorImpl loc = new LocatorImpl();
/*  215 */       loc.setLineNumber(tok.beginLine);
/*  216 */       loc.setColumnNumber(tok.beginColumn);
/*  217 */       loc.setSystemId(this.sourceUri);
/*      */       try {
/*  219 */         this.eh.error(new SAXParseException(message, loc));
/*      */       }
/*  221 */       catch (SAXException se) {
/*  222 */         throw new BuildException(se);
/*      */       } 
/*      */     } 
/*      */   }
/*      */   
/*      */   private void reportEscapeSyntaxException(EscapeSyntaxException e) {
/*  228 */     if (this.eh != null) {
/*  229 */       LocatorImpl loc = new LocatorImpl();
/*  230 */       loc.setLineNumber(e.getLineNumber());
/*  231 */       loc.setColumnNumber(e.getColumnNumber());
/*  232 */       loc.setSystemId(this.sourceUri);
/*      */       try {
/*  234 */         this.eh.error(new SAXParseException(localizer.message(e.getKey()), loc));
/*      */       }
/*  236 */       catch (SAXException se) {
/*  237 */         throw new BuildException(se);
/*      */       } 
/*      */     } 
/*      */   }
/*      */   
/*      */   private static String unquote(String s) {
/*  243 */     if (s.length() >= 6 && s.charAt(0) == s.charAt(1)) {
/*  244 */       s = s.replace(false, '\n');
/*  245 */       return s.substring(3, s.length() - 3);
/*      */     } 
/*      */     
/*  248 */     return s.substring(1, s.length() - 1);
/*      */   }
/*      */   
/*      */   Location makeLocation(Token t) {
/*  252 */     return this.sb.makeLocation(this.sourceUri, t.beginLine, t.beginColumn);
/*      */   }
/*      */   
/*      */   private static ParsedPattern[] addPattern(ParsedPattern[] patterns, int i, ParsedPattern p) {
/*  256 */     if (i >= patterns.length) {
/*  257 */       ParsedPattern[] oldPatterns = patterns;
/*  258 */       patterns = new ParsedPattern[oldPatterns.length * 2];
/*  259 */       System.arraycopy(oldPatterns, 0, patterns, 0, oldPatterns.length);
/*      */     } 
/*  261 */     patterns[i] = p;
/*  262 */     return patterns;
/*      */   }
/*      */   
/*      */   String getCompatibilityPrefix() {
/*  266 */     if (this.compatibilityPrefix == null) {
/*  267 */       this.compatibilityPrefix = "a";
/*  268 */       while (this.namespaceTable.get(this.compatibilityPrefix) != null)
/*  269 */         this.compatibilityPrefix += "a"; 
/*      */     } 
/*  271 */     return this.compatibilityPrefix;
/*      */   }
/*      */   
/*      */   public String resolveNamespacePrefix(String prefix) {
/*  275 */     String result = (String)this.namespaceTable.get(prefix);
/*  276 */     if (result.length() == 0)
/*  277 */       return null; 
/*  278 */     return result;
/*      */   }
/*      */   
/*      */   public Enumeration prefixes() {
/*  282 */     return this.namespaceTable.keys();
/*      */   }
/*      */   
/*      */   public String getBaseUri() {
/*  286 */     return this.sourceUri;
/*      */   }
/*      */   
/*      */   public boolean isUnparsedEntity(String entityName) {
/*  290 */     return false;
/*      */   }
/*      */   
/*      */   public boolean isNotation(String notationName) {
/*  294 */     return false;
/*      */   }
/*      */   
/*      */   public Context copy() {
/*  298 */     return this;
/*      */   }
/*      */   
/*      */   private Context getContext() {
/*  302 */     return this;
/*      */   }
/*      */   
/*      */   private CommentList getComments() {
/*  306 */     return getComments(getTopLevelComments());
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private CommentList getTopLevelComments() {
/*  312 */     CommentList tem = this.topLevelComments;
/*  313 */     this.topLevelComments = null;
/*  314 */     return tem;
/*      */   }
/*      */   
/*      */   private void noteTopLevelComments() {
/*  318 */     this.topLevelComments = getComments(this.topLevelComments);
/*      */   }
/*      */   
/*      */   private void topLevelComments(GrammarSection section) {
/*  322 */     section.topLevelComment(getComments(null));
/*      */   }
/*      */   
/*  325 */   private Token lastCommentSourceToken = null; public CompactSyntaxTokenManager token_source; JavaCharStream jj_input_stream; public Token token; public Token jj_nt; private int jj_ntk; private Token jj_scanpos; private Token jj_lastpos; private int jj_la; private int jj_gen;
/*      */   
/*      */   private CommentList getComments(CommentList comments) {
/*  328 */     Token nextToken = getToken(1);
/*  329 */     if (this.lastCommentSourceToken != nextToken) {
/*  330 */       if (this.lastCommentSourceToken == null)
/*  331 */         this.lastCommentSourceToken = this.token; 
/*      */       do {
/*  333 */         this.lastCommentSourceToken = this.lastCommentSourceToken.next;
/*  334 */         Token t = this.lastCommentSourceToken.specialToken;
/*  335 */         if (t == null)
/*  336 */           continue;  while (t.specialToken != null)
/*  337 */           t = t.specialToken; 
/*  338 */         if (comments == null)
/*  339 */           comments = this.sb.makeCommentList(); 
/*  340 */         for (; t != null; t = t.next) {
/*  341 */           String s = mungeComment(t.image);
/*  342 */           Location loc = makeLocation(t);
/*  343 */           if (t.next != null && t.next.kind == 44) {
/*      */             
/*  345 */             StringBuffer buf = new StringBuffer(s);
/*      */             do {
/*  347 */               t = t.next;
/*  348 */               buf.append('\n');
/*  349 */               buf.append(mungeComment(t.image));
/*  350 */             } while (t.next != null && t.next.kind == 44);
/*      */             
/*  352 */             s = buf.toString();
/*      */           } 
/*  354 */           comments.addComment(s, loc);
/*      */         }
/*      */       
/*  357 */       } while (this.lastCommentSourceToken != nextToken);
/*      */     } 
/*  359 */     return comments;
/*      */   }
/*      */   
/*      */   private ParsedPattern afterComments(ParsedPattern p) {
/*  363 */     CommentList comments = getComments(null);
/*  364 */     if (comments == null)
/*  365 */       return p; 
/*  366 */     return this.sb.commentAfter(p, comments);
/*      */   }
/*      */   
/*      */   private ParsedNameClass afterComments(ParsedNameClass nc) {
/*  370 */     CommentList comments = getComments(null);
/*  371 */     if (comments == null)
/*  372 */       return nc; 
/*  373 */     return this.ncb.commentAfter(nc, comments);
/*      */   }
/*      */   
/*      */   private static String mungeComment(String image) {
/*  377 */     int i = image.indexOf('#') + 1;
/*  378 */     while (i < image.length() && image.charAt(i) == '#')
/*  379 */       i++; 
/*  380 */     if (i < image.length() && image.charAt(i) == ' ')
/*  381 */       i++; 
/*  382 */     return image.substring(i);
/*      */   }
/*      */   
/*      */   private Annotations getCommentsAsAnnotations() {
/*  386 */     CommentList comments = getComments();
/*  387 */     if (comments == null)
/*  388 */       return null; 
/*  389 */     return this.sb.makeAnnotations(comments, getContext());
/*      */   }
/*      */   
/*      */   private Annotations addCommentsToChildAnnotations(Annotations a) {
/*  393 */     CommentList comments = getComments();
/*  394 */     if (comments == null)
/*  395 */       return a; 
/*  396 */     if (a == null)
/*  397 */       a = this.sb.makeAnnotations(null, getContext()); 
/*  398 */     a.addComment(comments);
/*  399 */     return a;
/*      */   }
/*      */   
/*      */   private Annotations addCommentsToLeadingAnnotations(Annotations a) {
/*  403 */     CommentList comments = getComments();
/*  404 */     if (comments == null)
/*  405 */       return a; 
/*  406 */     if (a == null)
/*  407 */       return this.sb.makeAnnotations(comments, getContext()); 
/*  408 */     a.addLeadingComment(comments);
/*  409 */     return a;
/*      */   }
/*      */   
/*      */   private Annotations getTopLevelCommentsAsAnnotations() {
/*  413 */     CommentList comments = getTopLevelComments();
/*  414 */     if (comments == null)
/*  415 */       return null; 
/*  416 */     return this.sb.makeAnnotations(comments, getContext());
/*      */   }
/*      */   
/*      */   private void clearAttributeList() {
/*  420 */     this.attributeNameTable.clear();
/*      */   }
/*      */   
/*      */   private void addAttribute(Annotations a, String ns, String localName, String prefix, String value, Token tok) {
/*  424 */     String key = ns + "#" + localName;
/*  425 */     if (this.attributeNameTable.get(key) != null) {
/*  426 */       error("duplicate_attribute", ns, localName, tok);
/*      */     } else {
/*  428 */       this.attributeNameTable.put(key, key);
/*  429 */       a.addAttribute(ns, localName, prefix, value, makeLocation(tok));
/*      */     } 
/*      */   }
/*      */   
/*      */   private void checkExcept(Token[] except) {
/*  434 */     if (except[0] != null)
/*  435 */       error("except_missing_parentheses", except[0]); 
/*      */   }
/*      */   
/*      */   private String lookupPrefix(String prefix, Token t) {
/*  439 */     String ns = (String)this.namespaceTable.get(prefix);
/*  440 */     if (ns == null) {
/*  441 */       error("undeclared_prefix", prefix, t);
/*  442 */       return "#error";
/*      */     } 
/*  444 */     return ns;
/*      */   }
/*      */   private String lookupDatatype(String prefix, Token t) {
/*  447 */     String ns = (String)this.datatypesTable.get(prefix);
/*  448 */     if (ns == null) {
/*  449 */       error("undeclared_prefix", prefix, t);
/*  450 */       return "";
/*      */     } 
/*  452 */     return ns;
/*      */   }
/*      */   private String resolve(String str) {
/*      */     try {
/*  456 */       return (new URL(new URL(this.sourceUri), str)).toString();
/*      */     }
/*  458 */     catch (MalformedURLException malformedURLException) {
/*  459 */       return str;
/*      */     } 
/*      */   }
/*      */   public final ParsedPattern Input(Scope scope) throws ParseException {
/*      */     ParsedPattern p;
/*  464 */     Preamble();
/*  465 */     if (jj_2_1(2147483647))
/*  466 */     { p = TopLevelGrammar(scope); }
/*      */     else
/*  468 */     { switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk)
/*      */       { case 1:
/*      */         case 10:
/*      */         case 17:
/*      */         case 18:
/*      */         case 19:
/*      */         case 26:
/*      */         case 27:
/*      */         case 28:
/*      */         case 31:
/*      */         case 32:
/*      */         case 33:
/*      */         case 34:
/*      */         case 35:
/*      */         case 36:
/*      */         case 40:
/*      */         case 43:
/*      */         case 54:
/*      */         case 55:
/*      */         case 57:
/*      */         case 58:
/*  489 */           p = Expr(true, scope, null, null);
/*  490 */           p = afterComments(p);
/*  491 */           jj_consume_token(0);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */           
/*  499 */           return p; }  this.jj_la1[0] = this.jj_gen; jj_consume_token(-1); throw new ParseException(); }  return p;
/*      */   }
/*      */ 
/*      */   
/*      */   public final void TopLevelLookahead() throws ParseException {
/*  504 */     switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
/*      */       case 57:
/*  506 */         jj_consume_token(57);
/*  507 */         jj_consume_token(1);
/*      */         return;
/*      */       case 54:
/*      */       case 55:
/*  511 */         Identifier();
/*  512 */         switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
/*      */           case 1:
/*  514 */             jj_consume_token(1);
/*      */             return;
/*      */           case 2:
/*  517 */             jj_consume_token(2);
/*      */             return;
/*      */           case 3:
/*  520 */             jj_consume_token(3);
/*      */             return;
/*      */           case 4:
/*  523 */             jj_consume_token(4);
/*      */             return;
/*      */         } 
/*  526 */         this.jj_la1[1] = this.jj_gen;
/*  527 */         jj_consume_token(-1);
/*  528 */         throw new ParseException();
/*      */ 
/*      */       
/*      */       case 5:
/*      */       case 6:
/*      */       case 7:
/*  534 */         LookaheadGrammarKeyword();
/*      */         return;
/*      */       case 1:
/*  537 */         LookaheadBody();
/*  538 */         LookaheadAfterAnnotations();
/*      */         return;
/*      */       case 40:
/*      */       case 43:
/*  542 */         LookaheadDocumentation();
/*  543 */         switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
/*      */           case 1:
/*  545 */             LookaheadBody();
/*      */             break;
/*      */           default:
/*  548 */             this.jj_la1[2] = this.jj_gen;
/*      */             break;
/*      */         } 
/*  551 */         LookaheadAfterAnnotations();
/*      */         return;
/*      */     } 
/*  554 */     this.jj_la1[3] = this.jj_gen;
/*  555 */     jj_consume_token(-1);
/*  556 */     throw new ParseException();
/*      */   }
/*      */ 
/*      */   
/*      */   public final void LookaheadAfterAnnotations() throws ParseException {
/*  561 */     switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
/*      */       case 54:
/*      */       case 55:
/*  564 */         Identifier();
/*  565 */         switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
/*      */           case 2:
/*  567 */             jj_consume_token(2);
/*      */             return;
/*      */           case 3:
/*  570 */             jj_consume_token(3);
/*      */             return;
/*      */           case 4:
/*  573 */             jj_consume_token(4);
/*      */             return;
/*      */         } 
/*  576 */         this.jj_la1[4] = this.jj_gen;
/*  577 */         jj_consume_token(-1);
/*  578 */         throw new ParseException();
/*      */ 
/*      */       
/*      */       case 5:
/*      */       case 6:
/*      */       case 7:
/*  584 */         LookaheadGrammarKeyword();
/*      */         return;
/*      */     } 
/*  587 */     this.jj_la1[5] = this.jj_gen;
/*  588 */     jj_consume_token(-1);
/*  589 */     throw new ParseException();
/*      */   }
/*      */ 
/*      */   
/*      */   public final void LookaheadGrammarKeyword() throws ParseException {
/*  594 */     switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
/*      */       case 5:
/*  596 */         jj_consume_token(5);
/*      */         return;
/*      */       case 6:
/*  599 */         jj_consume_token(6);
/*      */         return;
/*      */       case 7:
/*  602 */         jj_consume_token(7);
/*      */         return;
/*      */     } 
/*  605 */     this.jj_la1[6] = this.jj_gen;
/*  606 */     jj_consume_token(-1);
/*  607 */     throw new ParseException();
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void LookaheadDocumentation() throws ParseException {
/*      */     while (true) {
/*  614 */       switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
/*      */         case 40:
/*  616 */           jj_consume_token(40);
/*      */           break;
/*      */         case 43:
/*  619 */           jj_consume_token(43);
/*      */           break;
/*      */         default:
/*  622 */           this.jj_la1[7] = this.jj_gen;
/*  623 */           jj_consume_token(-1);
/*  624 */           throw new ParseException();
/*      */       } 
/*      */       
/*      */       while (true) {
/*  628 */         switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
/*      */           case 41:
/*      */             break;
/*      */           
/*      */           default:
/*  633 */             this.jj_la1[8] = this.jj_gen;
/*      */             break;
/*      */         } 
/*  636 */         jj_consume_token(41);
/*      */       } 
/*  638 */       switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
/*      */         case 40:
/*      */         case 43:
/*      */           continue;
/*      */       }  break;
/*      */     } 
/*  644 */     this.jj_la1[9] = this.jj_gen;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final void LookaheadBody() throws ParseException {
/*  651 */     jj_consume_token(1);
/*      */     
/*      */     while (true) {
/*  654 */       switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
/*      */         case 1:
/*      */         case 2:
/*      */         case 5:
/*      */         case 6:
/*      */         case 7:
/*      */         case 8:
/*      */         case 10:
/*      */         case 13:
/*      */         case 14:
/*      */         case 15:
/*      */         case 16:
/*      */         case 17:
/*      */         case 18:
/*      */         case 19:
/*      */         case 26:
/*      */         case 27:
/*      */         case 31:
/*      */         case 32:
/*      */         case 33:
/*      */         case 34:
/*      */         case 35:
/*      */         case 36:
/*      */         case 54:
/*      */         case 55:
/*      */         case 57:
/*      */         case 58:
/*      */           break;
/*      */         
/*      */         default:
/*  684 */           this.jj_la1[10] = this.jj_gen;
/*      */           break;
/*      */       } 
/*  687 */       switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
/*      */         case 57:
/*  689 */           jj_consume_token(57);
/*      */           continue;
/*      */         case 5:
/*      */         case 6:
/*      */         case 7:
/*      */         case 10:
/*      */         case 13:
/*      */         case 14:
/*      */         case 15:
/*      */         case 16:
/*      */         case 17:
/*      */         case 18:
/*      */         case 19:
/*      */         case 26:
/*      */         case 27:
/*      */         case 31:
/*      */         case 32:
/*      */         case 33:
/*      */         case 34:
/*      */         case 35:
/*      */         case 36:
/*      */         case 54:
/*      */         case 55:
/*  712 */           UnprefixedName();
/*      */           continue;
/*      */         case 2:
/*  715 */           jj_consume_token(2);
/*      */           continue;
/*      */         case 58:
/*  718 */           jj_consume_token(58);
/*      */           continue;
/*      */         case 8:
/*  721 */           jj_consume_token(8);
/*      */           continue;
/*      */         case 1:
/*  724 */           LookaheadBody();
/*      */           continue;
/*      */       } 
/*  727 */       this.jj_la1[11] = this.jj_gen;
/*  728 */       jj_consume_token(-1);
/*  729 */       throw new ParseException();
/*      */     } 
/*      */     
/*  732 */     jj_consume_token(9);
/*      */   }
/*      */ 
/*      */   
/*      */   public final ParsedPattern IncludedGrammar(IncludedGrammar g) throws ParseException {
/*      */     Annotations a;
/*  738 */     Preamble();
/*  739 */     if (jj_2_2(2147483647))
/*  740 */     { a = GrammarBody((GrammarSection)g, (Scope)g, getTopLevelCommentsAsAnnotations()); }
/*      */     else
/*  742 */     { ParsedPattern p; switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk)
/*      */       { case 1:
/*      */         case 10:
/*      */         case 40:
/*      */         case 43:
/*  747 */           a = Annotations();
/*  748 */           jj_consume_token(10);
/*  749 */           jj_consume_token(11);
/*  750 */           a = GrammarBody((GrammarSection)g, (Scope)g, a);
/*  751 */           topLevelComments((GrammarSection)g);
/*  752 */           jj_consume_token(12);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */           
/*  760 */           p = afterComments(g.endIncludedGrammar(this.sb.makeLocation(this.sourceUri, 1, 1), a));
/*  761 */           jj_consume_token(0);
/*  762 */           return p; }  this.jj_la1[12] = this.jj_gen; jj_consume_token(-1); throw new ParseException(); }  ParsedPattern parsedPattern = afterComments(g.endIncludedGrammar(this.sb.makeLocation(this.sourceUri, 1, 1), a)); jj_consume_token(0); return parsedPattern;
/*      */   }
/*      */ 
/*      */   
/*      */   public final ParsedPattern TopLevelGrammar(Scope scope) throws ParseException {
/*  767 */     Annotations a = getTopLevelCommentsAsAnnotations();
/*      */ 
/*      */     
/*  770 */     Grammar g = this.sb.makeGrammar(scope);
/*  771 */     a = GrammarBody((GrammarSection)g, (Scope)g, a);
/*  772 */     ParsedPattern p = afterComments(g.endGrammar(this.sb.makeLocation(this.sourceUri, 1, 1), a));
/*  773 */     jj_consume_token(0);
/*  774 */     return p;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void Preamble() throws ParseException {
/*      */     while (true) {
/*  781 */       switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
/*      */         case 13:
/*      */         case 14:
/*      */         case 16:
/*      */           break;
/*      */         
/*      */         default:
/*  788 */           this.jj_la1[13] = this.jj_gen;
/*      */           break;
/*      */       } 
/*  791 */       switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
/*      */         case 13:
/*      */         case 14:
/*  794 */           NamespaceDecl();
/*      */           continue;
/*      */         case 16:
/*  797 */           DatatypesDecl();
/*      */           continue;
/*      */       } 
/*  800 */       this.jj_la1[14] = this.jj_gen;
/*  801 */       jj_consume_token(-1);
/*  802 */       throw new ParseException();
/*      */     } 
/*      */     
/*  805 */     this.namespaceTable.put("xml", "http://www.w3.org/XML/1998/namespace");
/*  806 */     if (this.datatypesTable.get("xsd") == null)
/*  807 */       this.datatypesTable.put("xsd", "http://www.w3.org/2001/XMLSchema-datatypes"); 
/*      */   }
/*      */   
/*      */   public final void NamespaceDecl() throws ParseException {
/*  811 */     LocatedString prefix = null;
/*  812 */     boolean isDefault = false;
/*      */     
/*  814 */     noteTopLevelComments();
/*  815 */     switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
/*      */       case 13:
/*  817 */         jj_consume_token(13);
/*  818 */         prefix = UnprefixedName();
/*      */         break;
/*      */       case 14:
/*  821 */         jj_consume_token(14);
/*  822 */         isDefault = true;
/*  823 */         jj_consume_token(13);
/*  824 */         switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
/*      */           case 5:
/*      */           case 6:
/*      */           case 7:
/*      */           case 10:
/*      */           case 13:
/*      */           case 14:
/*      */           case 15:
/*      */           case 16:
/*      */           case 17:
/*      */           case 18:
/*      */           case 19:
/*      */           case 26:
/*      */           case 27:
/*      */           case 31:
/*      */           case 32:
/*      */           case 33:
/*      */           case 34:
/*      */           case 35:
/*      */           case 36:
/*      */           case 54:
/*      */           case 55:
/*  846 */             prefix = UnprefixedName();
/*      */             break;
/*      */         } 
/*  849 */         this.jj_la1[15] = this.jj_gen;
/*      */         break;
/*      */ 
/*      */       
/*      */       default:
/*  854 */         this.jj_la1[16] = this.jj_gen;
/*  855 */         jj_consume_token(-1);
/*  856 */         throw new ParseException();
/*      */     } 
/*  858 */     jj_consume_token(2);
/*  859 */     String namespaceName = NamespaceName();
/*  860 */     if (isDefault)
/*  861 */       this.defaultNamespace = namespaceName; 
/*  862 */     if (prefix != null) {
/*  863 */       if (prefix.getString().equals("xmlns")) {
/*  864 */         error("xmlns_prefix", prefix.getToken());
/*  865 */       } else if (prefix.getString().equals("xml")) {
/*  866 */         if (!namespaceName.equals("http://www.w3.org/XML/1998/namespace")) {
/*  867 */           error("xml_prefix_bad_uri", prefix.getToken());
/*      */         }
/*  869 */       } else if (namespaceName.equals("http://www.w3.org/XML/1998/namespace")) {
/*  870 */         error("xml_uri_bad_prefix", prefix.getToken());
/*      */       } else {
/*  872 */         if (namespaceName.equals("http://relaxng.org/ns/compatibility/annotations/1.0"))
/*  873 */           this.compatibilityPrefix = prefix.getString(); 
/*  874 */         this.namespaceTable.put(prefix.getString(), namespaceName);
/*      */       } 
/*      */     }
/*      */   }
/*      */   
/*      */   public final String NamespaceName() throws ParseException {
/*      */     String r;
/*  881 */     switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
/*      */       case 58:
/*  883 */         r = Literal();
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
/*  894 */         return r;case 15: jj_consume_token(15); r = this.inheritedNs; return r;
/*      */     } 
/*      */     this.jj_la1[17] = this.jj_gen;
/*      */     jj_consume_token(-1);
/*      */     throw new ParseException();
/*      */   }
/*      */   public final void DatatypesDecl() throws ParseException {
/*  901 */     noteTopLevelComments();
/*  902 */     jj_consume_token(16);
/*  903 */     LocatedString prefix = UnprefixedName();
/*  904 */     jj_consume_token(2);
/*  905 */     String uri = Literal();
/*  906 */     this.datatypesTable.put(prefix.getString(), uri);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final ParsedPattern AnnotatedPrimaryExpr(boolean topLevel, Scope scope, Token[] except) throws ParseException {
/*  914 */     Annotations a = Annotations();
/*  915 */     ParsedPattern p = PrimaryExpr(topLevel, scope, a, except);
/*      */     
/*      */     while (true) {
/*  918 */       switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
/*      */         case 59:
/*      */           break;
/*      */         
/*      */         default:
/*  923 */           this.jj_la1[18] = this.jj_gen;
/*      */           break;
/*      */       } 
/*  926 */       Token t = jj_consume_token(59);
/*  927 */       ParsedElementAnnotation e = AnnotationElement(false);
/*  928 */       if (topLevel) {
/*  929 */         error("top_level_follow_annotation", t); continue;
/*      */       } 
/*  931 */       p = this.sb.annotateAfter(p, e);
/*      */     } 
/*  933 */     return p;
/*      */   }
/*      */ 
/*      */   
/*      */   public final ParsedPattern PrimaryExpr(boolean topLevel, Scope scope, Annotations a, Token[] except) throws ParseException {
/*      */     ParsedPattern p;
/*  939 */     switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
/*      */       case 26:
/*  941 */         p = ElementExpr(scope, a);
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
/*  990 */         return p;case 27: p = AttributeExpr(scope, a); return p;case 10: p = GrammarExpr(scope, a); return p;case 33: p = ExternalRefExpr(scope, a); return p;case 31: p = ListExpr(scope, a); return p;case 32: p = MixedExpr(scope, a); return p;case 28: p = ParenExpr(topLevel, scope, a); return p;case 54: case 55: p = IdentifierExpr(scope, a); return p;case 34: p = ParentExpr(scope, a); return p;case 35: case 36: case 57: p = DataExpr(topLevel, scope, a, except); return p;case 58: p = ValueExpr(topLevel, a); return p;case 18: p = TextExpr(a); return p;case 17: p = EmptyExpr(a); return p;case 19: p = NotAllowedExpr(a); return p;
/*      */     } 
/*      */     this.jj_la1[19] = this.jj_gen;
/*      */     jj_consume_token(-1);
/*      */     throw new ParseException();
/*      */   } public final ParsedPattern EmptyExpr(Annotations a) throws ParseException {
/*  996 */     Token t = jj_consume_token(17);
/*  997 */     return this.sb.makeEmpty(makeLocation(t), a);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final ParsedPattern TextExpr(Annotations a) throws ParseException {
/* 1003 */     Token t = jj_consume_token(18);
/* 1004 */     return this.sb.makeText(makeLocation(t), a);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final ParsedPattern NotAllowedExpr(Annotations a) throws ParseException {
/* 1010 */     Token t = jj_consume_token(19);
/* 1011 */     return this.sb.makeNotAllowed(makeLocation(t), a);
/*      */   }
/*      */ 
/*      */   
/*      */   public final ParsedPattern Expr(boolean topLevel, Scope scope, Token t, Annotations a) throws ParseException {
/* 1016 */     List<ParsedPattern> patterns = new ArrayList();
/*      */     
/* 1018 */     boolean[] hadOccur = new boolean[1];
/* 1019 */     Token[] except = new Token[1];
/* 1020 */     ParsedPattern p = UnaryExpr(topLevel, scope, hadOccur, except);
/* 1021 */     patterns.add(p);
/* 1022 */     switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
/*      */       case 20:
/*      */       case 21:
/*      */       case 22:
/* 1026 */         switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
/*      */           case 20:
/* 1028 */             checkExcept(except);
/*      */             
/*      */             while (true) {
/* 1031 */               t = jj_consume_token(20);
/* 1032 */               p = UnaryExpr(topLevel, scope, null, except);
/* 1033 */               patterns.add(p); checkExcept(except);
/* 1034 */               switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
/*      */                 case 20:
/*      */                   continue;
/*      */               }  break;
/*      */             } 
/* 1039 */             this.jj_la1[20] = this.jj_gen;
/*      */ 
/*      */ 
/*      */             
/* 1043 */             p = this.sb.makeChoice(patterns, makeLocation(t), a);
/*      */             break;
/*      */           
/*      */           case 21:
/*      */             while (true) {
/* 1048 */               t = jj_consume_token(21);
/* 1049 */               p = UnaryExpr(topLevel, scope, null, except);
/* 1050 */               patterns.add(p); checkExcept(except);
/* 1051 */               switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
/*      */                 case 21:
/*      */                   continue;
/*      */               }  break;
/*      */             } 
/* 1056 */             this.jj_la1[21] = this.jj_gen;
/*      */ 
/*      */ 
/*      */             
/* 1060 */             p = this.sb.makeInterleave(patterns, makeLocation(t), a);
/*      */             break;
/*      */           
/*      */           case 22:
/*      */             while (true) {
/* 1065 */               t = jj_consume_token(22);
/* 1066 */               p = UnaryExpr(topLevel, scope, null, except);
/* 1067 */               patterns.add(p); checkExcept(except);
/* 1068 */               switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
/*      */                 case 22:
/*      */                   continue;
/*      */               }  break;
/*      */             } 
/* 1073 */             this.jj_la1[22] = this.jj_gen;
/*      */ 
/*      */ 
/*      */             
/* 1077 */             p = this.sb.makeGroup(patterns, makeLocation(t), a);
/*      */             break;
/*      */         } 
/* 1080 */         this.jj_la1[23] = this.jj_gen;
/* 1081 */         jj_consume_token(-1);
/* 1082 */         throw new ParseException();
/*      */ 
/*      */       
/*      */       default:
/* 1086 */         this.jj_la1[24] = this.jj_gen;
/*      */         break;
/*      */     } 
/* 1089 */     if (patterns.size() == 1 && a != null)
/* 1090 */       if (hadOccur[0]) {
/* 1091 */         p = this.sb.annotate(p, a);
/*      */       } else {
/* 1093 */         p = this.sb.makeGroup(patterns, makeLocation(t), a);
/*      */       }  
/* 1095 */     return p;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final ParsedPattern UnaryExpr(boolean topLevel, Scope scope, boolean[] hadOccur, Token[] except) throws ParseException {
/*      */     Token t;
/* 1103 */     ParsedPattern p = AnnotatedPrimaryExpr(topLevel, scope, except);
/* 1104 */     switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk)
/*      */     { case 23:
/*      */       case 24:
/*      */       case 25:
/* 1108 */         if (hadOccur != null) hadOccur[0] = true; 
/* 1109 */         p = afterComments(p);
/* 1110 */         switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
/*      */           case 23:
/* 1112 */             t = jj_consume_token(23);
/* 1113 */             checkExcept(except); p = this.sb.makeOneOrMore(p, makeLocation(t), null);
/*      */             break;
/*      */           case 24:
/* 1116 */             t = jj_consume_token(24);
/* 1117 */             checkExcept(except); p = this.sb.makeOptional(p, makeLocation(t), null);
/*      */             break;
/*      */           case 25:
/* 1120 */             t = jj_consume_token(25);
/* 1121 */             checkExcept(except); p = this.sb.makeZeroOrMore(p, makeLocation(t), null);
/*      */             break;
/*      */           default:
/* 1124 */             this.jj_la1[25] = this.jj_gen;
/* 1125 */             jj_consume_token(-1);
/* 1126 */             throw new ParseException();
/*      */         } 
/*      */         
/*      */         while (true) {
/* 1130 */           switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
/*      */             case 59:
/*      */               break;
/*      */             
/*      */             default:
/* 1135 */               this.jj_la1[26] = this.jj_gen;
/*      */               break;
/*      */           } 
/* 1138 */           t = jj_consume_token(59);
/* 1139 */           ParsedElementAnnotation e = AnnotationElement(false);
/* 1140 */           if (topLevel) {
/* 1141 */             error("top_level_follow_annotation", t); continue;
/*      */           } 
/* 1143 */           p = this.sb.annotateAfter(p, e);
/*      */         } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 1150 */         return p; }  this.jj_la1[27] = this.jj_gen; return p;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final ParsedPattern ElementExpr(Scope scope, Annotations a) throws ParseException {
/* 1158 */     Token t = jj_consume_token(26);
/* 1159 */     ParsedNameClass nc = NameClass(0, null);
/* 1160 */     jj_consume_token(11);
/* 1161 */     ParsedPattern p = Expr(false, scope, null, null);
/* 1162 */     p = afterComments(p);
/* 1163 */     jj_consume_token(12);
/* 1164 */     return this.sb.makeElement(nc, p, makeLocation(t), a);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final ParsedPattern AttributeExpr(Scope scope, Annotations a) throws ParseException {
/* 1172 */     Token t = jj_consume_token(27);
/* 1173 */     ParsedNameClass nc = NameClass(1, null);
/* 1174 */     jj_consume_token(11);
/* 1175 */     ParsedPattern p = Expr(false, scope, null, null);
/* 1176 */     p = afterComments(p);
/* 1177 */     jj_consume_token(12);
/* 1178 */     return this.sb.makeAttribute(nc, p, makeLocation(t), a);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final ParsedNameClass NameClass(int context, Annotations[] pa) throws ParseException {
/*      */     ParsedNameClass nc;
/* 1185 */     Annotations a = Annotations();
/* 1186 */     switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
/*      */       case 5:
/*      */       case 6:
/*      */       case 7:
/*      */       case 10:
/*      */       case 13:
/*      */       case 14:
/*      */       case 15:
/*      */       case 16:
/*      */       case 17:
/*      */       case 18:
/*      */       case 19:
/*      */       case 26:
/*      */       case 27:
/*      */       case 28:
/*      */       case 31:
/*      */       case 32:
/*      */       case 33:
/*      */       case 34:
/*      */       case 35:
/*      */       case 36:
/*      */       case 54:
/*      */       case 55:
/*      */       case 57:
/* 1210 */         nc = PrimaryNameClass(context, a);
/* 1211 */         nc = AnnotateAfter(nc);
/* 1212 */         nc = NameClassAlternatives(context, nc, pa);
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
/* 1225 */         return nc;case 25: nc = AnyNameExceptClass(context, a, pa); return nc;case 56: nc = NsNameExceptClass(context, a, pa); return nc;
/*      */     } 
/*      */     this.jj_la1[28] = this.jj_gen;
/*      */     jj_consume_token(-1);
/*      */     throw new ParseException();
/*      */   }
/*      */   public final ParsedNameClass AnnotateAfter(ParsedNameClass nc) throws ParseException {
/*      */     while (true) {
/* 1233 */       switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
/*      */         case 59:
/*      */           break;
/*      */         
/*      */         default:
/* 1238 */           this.jj_la1[29] = this.jj_gen;
/*      */           break;
/*      */       } 
/* 1241 */       jj_consume_token(59);
/* 1242 */       ParsedElementAnnotation e = AnnotationElement(false);
/* 1243 */       nc = this.ncb.annotateAfter(nc, e);
/*      */     } 
/* 1245 */     return nc;
/*      */   }
/*      */   
/*      */   public final ParsedNameClass NameClassAlternatives(int context, ParsedNameClass nc, Annotations[] pa) throws ParseException {
/*      */     Token t;
/*      */     ParsedNameClass[] nameClasses;
/*      */     int nNameClasses;
/*      */     Annotations a;
/* 1253 */     switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk)
/*      */     { case 20:
/* 1255 */         nameClasses = new ParsedNameClass[2];
/* 1256 */         nameClasses[0] = nc;
/* 1257 */         nNameClasses = 1;
/*      */         
/*      */         while (true) {
/* 1260 */           t = jj_consume_token(20);
/* 1261 */           nc = BasicNameClass(context);
/* 1262 */           nc = AnnotateAfter(nc);
/* 1263 */           if (nNameClasses >= nameClasses.length) {
/* 1264 */             ParsedNameClass[] oldNameClasses = nameClasses;
/* 1265 */             nameClasses = new ParsedNameClass[oldNameClasses.length * 2];
/* 1266 */             System.arraycopy(oldNameClasses, 0, nameClasses, 0, oldNameClasses.length);
/*      */           } 
/* 1268 */           nameClasses[nNameClasses++] = nc;
/* 1269 */           switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
/*      */             case 20:
/*      */               continue;
/*      */           }  break;
/*      */         } 
/* 1274 */         this.jj_la1[30] = this.jj_gen;
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 1279 */         if (pa == null) {
/* 1280 */           a = null;
/*      */         } else {
/* 1282 */           a = pa[0];
/* 1283 */           pa[0] = null;
/*      */         } 
/* 1285 */         nc = this.ncb.makeChoice(Arrays.<ParsedNameClass>asList(nameClasses).subList(0, nNameClasses), makeLocation(t), a);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 1291 */         return nc; }  this.jj_la1[31] = this.jj_gen; return nc;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final ParsedNameClass BasicNameClass(int context) throws ParseException {
/*      */     ParsedNameClass nc;
/* 1298 */     Annotations a = Annotations();
/* 1299 */     switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
/*      */       case 5:
/*      */       case 6:
/*      */       case 7:
/*      */       case 10:
/*      */       case 13:
/*      */       case 14:
/*      */       case 15:
/*      */       case 16:
/*      */       case 17:
/*      */       case 18:
/*      */       case 19:
/*      */       case 26:
/*      */       case 27:
/*      */       case 28:
/*      */       case 31:
/*      */       case 32:
/*      */       case 33:
/*      */       case 34:
/*      */       case 35:
/*      */       case 36:
/*      */       case 54:
/*      */       case 55:
/*      */       case 57:
/* 1323 */         nc = PrimaryNameClass(context, a);
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
/* 1334 */         return nc;case 25: case 56: nc = OpenNameClass(context, a); return nc;
/*      */     } 
/*      */     this.jj_la1[32] = this.jj_gen;
/*      */     jj_consume_token(-1);
/*      */     throw new ParseException();
/*      */   } public final ParsedNameClass PrimaryNameClass(int context, Annotations a) throws ParseException { ParsedNameClass nc;
/* 1340 */     switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
/*      */       case 5:
/*      */       case 6:
/*      */       case 7:
/*      */       case 10:
/*      */       case 13:
/*      */       case 14:
/*      */       case 15:
/*      */       case 16:
/*      */       case 17:
/*      */       case 18:
/*      */       case 19:
/*      */       case 26:
/*      */       case 27:
/*      */       case 31:
/*      */       case 32:
/*      */       case 33:
/*      */       case 34:
/*      */       case 35:
/*      */       case 36:
/*      */       case 54:
/*      */       case 55:
/* 1362 */         nc = UnprefixedNameClass(context, a);
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
/* 1375 */         return nc;case 57: nc = PrefixedNameClass(a); return nc;case 28: nc = ParenNameClass(context, a); return nc;
/*      */     } 
/*      */     this.jj_la1[33] = this.jj_gen;
/*      */     jj_consume_token(-1);
/*      */     throw new ParseException(); } public final ParsedNameClass OpenNameClass(int context, Annotations a) throws ParseException {
/*      */     Token t;
/*      */     LocatedString ns;
/* 1382 */     switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
/*      */       case 56:
/* 1384 */         ns = NsName();
/* 1385 */         checkNsName(context, ns); return this.ncb.makeNsName(ns.getString(), ns.getLocation(), a);
/*      */       
/*      */       case 25:
/* 1388 */         t = jj_consume_token(25);
/* 1389 */         checkAnyName(context, t); return this.ncb.makeAnyName(makeLocation(t), a);
/*      */     } 
/*      */     
/* 1392 */     this.jj_la1[34] = this.jj_gen;
/* 1393 */     jj_consume_token(-1);
/* 1394 */     throw new ParseException();
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final ParsedNameClass UnprefixedNameClass(int context, Annotations a) throws ParseException {
/*      */     String ns;
/* 1401 */     LocatedString name = UnprefixedName();
/*      */     
/* 1403 */     if ((context & 0x1) == 1) {
/* 1404 */       ns = "";
/*      */     } else {
/* 1406 */       ns = this.defaultNamespace;
/* 1407 */     }  return this.ncb.makeName(ns, name.getString(), null, name.getLocation(), a);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final ParsedNameClass PrefixedNameClass(Annotations a) throws ParseException {
/* 1413 */     Token t = jj_consume_token(57);
/* 1414 */     String qn = t.image;
/* 1415 */     int colon = qn.indexOf(':');
/* 1416 */     String prefix = qn.substring(0, colon);
/* 1417 */     return this.ncb.makeName(lookupPrefix(prefix, t), qn.substring(colon + 1), prefix, makeLocation(t), a);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final ParsedNameClass NsNameExceptClass(int context, Annotations a, Annotations[] pa) throws ParseException {
/* 1424 */     LocatedString ns = NsName();
/* 1425 */     checkNsName(context, ns);
/* 1426 */     switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk)
/*      */     { case 30:
/* 1428 */         nc = ExceptNameClass(context | 0x4);
/* 1429 */         nc = this.ncb.makeNsName(ns.getString(), nc, ns.getLocation(), a);
/* 1430 */         nc = AnnotateAfter(nc);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 1438 */         return nc; }  this.jj_la1[35] = this.jj_gen; ParsedNameClass nc = this.ncb.makeNsName(ns.getString(), ns.getLocation(), a); nc = AnnotateAfter(nc); nc = NameClassAlternatives(context, nc, pa); return nc;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final LocatedString NsName() throws ParseException {
/* 1444 */     Token t = jj_consume_token(56);
/* 1445 */     String qn = t.image;
/* 1446 */     String prefix = qn.substring(0, qn.length() - 2);
/* 1447 */     return new LocatedString(lookupPrefix(prefix, t), t);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final ParsedNameClass AnyNameExceptClass(int context, Annotations a, Annotations[] pa) throws ParseException {
/* 1454 */     Token t = jj_consume_token(25);
/* 1455 */     checkAnyName(context, t);
/* 1456 */     switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk)
/*      */     { case 30:
/* 1458 */         nc = ExceptNameClass(context | 0x2);
/* 1459 */         nc = this.ncb.makeAnyName(nc, makeLocation(t), a);
/* 1460 */         nc = AnnotateAfter(nc);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 1468 */         return nc; }  this.jj_la1[36] = this.jj_gen; ParsedNameClass nc = this.ncb.makeAnyName(makeLocation(t), a); nc = AnnotateAfter(nc); nc = NameClassAlternatives(context, nc, pa); return nc;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final ParsedNameClass ParenNameClass(int context, Annotations a) throws ParseException {
/* 1475 */     Annotations[] pa = { a };
/* 1476 */     Token t = jj_consume_token(28);
/* 1477 */     ParsedNameClass nc = NameClass(context, pa);
/* 1478 */     nc = afterComments(nc);
/* 1479 */     jj_consume_token(29);
/* 1480 */     if (pa[0] != null)
/* 1481 */       nc = this.ncb.makeChoice(Collections.singletonList(nc), makeLocation(t), pa[0]); 
/* 1482 */     return nc;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final ParsedNameClass ExceptNameClass(int context) throws ParseException {
/* 1488 */     jj_consume_token(30);
/* 1489 */     ParsedNameClass nc = BasicNameClass(context);
/* 1490 */     return nc;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final ParsedPattern ListExpr(Scope scope, Annotations a) throws ParseException {
/* 1497 */     Token t = jj_consume_token(31);
/* 1498 */     jj_consume_token(11);
/* 1499 */     ParsedPattern p = Expr(false, scope, null, null);
/* 1500 */     p = afterComments(p);
/* 1501 */     jj_consume_token(12);
/* 1502 */     return this.sb.makeList(p, makeLocation(t), a);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final ParsedPattern MixedExpr(Scope scope, Annotations a) throws ParseException {
/* 1509 */     Token t = jj_consume_token(32);
/* 1510 */     jj_consume_token(11);
/* 1511 */     ParsedPattern p = Expr(false, scope, null, null);
/* 1512 */     p = afterComments(p);
/* 1513 */     jj_consume_token(12);
/* 1514 */     return this.sb.makeMixed(p, makeLocation(t), a);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final ParsedPattern GrammarExpr(Scope scope, Annotations a) throws ParseException {
/* 1521 */     Token t = jj_consume_token(10);
/* 1522 */     Grammar g = this.sb.makeGrammar(scope);
/* 1523 */     jj_consume_token(11);
/* 1524 */     a = GrammarBody((GrammarSection)g, (Scope)g, a);
/* 1525 */     topLevelComments((GrammarSection)g);
/* 1526 */     jj_consume_token(12);
/* 1527 */     return g.endGrammar(makeLocation(t), a);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final ParsedPattern ParenExpr(boolean topLevel, Scope scope, Annotations a) throws ParseException {
/* 1534 */     Token t = jj_consume_token(28);
/* 1535 */     ParsedPattern p = Expr(topLevel, scope, t, a);
/* 1536 */     p = afterComments(p);
/* 1537 */     jj_consume_token(29);
/* 1538 */     return p;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final Annotations GrammarBody(GrammarSection section, Scope scope, Annotations a) throws ParseException {
/* 1546 */     while (jj_2_3(2)) {
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1551 */       ParsedElementAnnotation e = AnnotationElementNotKeyword();
/* 1552 */       if (a == null) a = this.sb.makeAnnotations(null, getContext());  a.addElement(e);
/*      */     } 
/*      */     
/*      */     while (true) {
/* 1556 */       switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
/*      */         case 1:
/*      */         case 5:
/*      */         case 6:
/*      */         case 7:
/*      */         case 40:
/*      */         case 43:
/*      */         case 54:
/*      */         case 55:
/*      */           break;
/*      */         
/*      */         default:
/* 1568 */           this.jj_la1[37] = this.jj_gen;
/*      */           break;
/*      */       } 
/* 1571 */       GrammarComponent(section, scope);
/*      */     } 
/* 1573 */     return a;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final void GrammarComponent(GrammarSection section, Scope scope) throws ParseException {
/* 1580 */     Annotations a = Annotations();
/* 1581 */     switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
/*      */       case 5:
/*      */       case 54:
/*      */       case 55:
/* 1585 */         Definition(section, scope, a);
/*      */         break;
/*      */       case 7:
/* 1588 */         Include(section, scope, a);
/*      */         break;
/*      */       case 6:
/* 1591 */         Div(section, scope, a);
/*      */         break;
/*      */       default:
/* 1594 */         this.jj_la1[38] = this.jj_gen;
/* 1595 */         jj_consume_token(-1);
/* 1596 */         throw new ParseException();
/*      */     } 
/*      */ 
/*      */     
/* 1600 */     while (jj_2_4(2)) {
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1605 */       ParsedElementAnnotation e = AnnotationElementNotKeyword();
/* 1606 */       section.topLevelAnnotation(e);
/*      */     } 
/*      */   }
/*      */   
/*      */   public final void Definition(GrammarSection section, Scope scope, Annotations a) throws ParseException {
/* 1611 */     switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
/*      */       case 54:
/*      */       case 55:
/* 1614 */         Define(section, scope, a);
/*      */         return;
/*      */       case 5:
/* 1617 */         Start(section, scope, a);
/*      */         return;
/*      */     } 
/* 1620 */     this.jj_la1[39] = this.jj_gen;
/* 1621 */     jj_consume_token(-1);
/* 1622 */     throw new ParseException();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final void Start(GrammarSection section, Scope scope, Annotations a) throws ParseException {
/* 1630 */     Token t = jj_consume_token(5);
/* 1631 */     GrammarSection.Combine combine = AssignOp();
/* 1632 */     ParsedPattern p = Expr(false, scope, null, null);
/* 1633 */     section.define("\000#start\000", combine, p, makeLocation(t), a);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final void Define(GrammarSection section, Scope scope, Annotations a) throws ParseException {
/* 1640 */     LocatedString name = Identifier();
/* 1641 */     GrammarSection.Combine combine = AssignOp();
/* 1642 */     ParsedPattern p = Expr(false, scope, null, null);
/* 1643 */     section.define(name.getString(), combine, p, name.getLocation(), a);
/*      */   }
/*      */   
/*      */   public final GrammarSection.Combine AssignOp() throws ParseException {
/* 1647 */     switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
/*      */       case 2:
/* 1649 */         jj_consume_token(2);
/* 1650 */         return null;
/*      */       
/*      */       case 4:
/* 1653 */         jj_consume_token(4);
/* 1654 */         return GrammarSection.COMBINE_CHOICE;
/*      */       
/*      */       case 3:
/* 1657 */         jj_consume_token(3);
/* 1658 */         return GrammarSection.COMBINE_INTERLEAVE;
/*      */     } 
/*      */     
/* 1661 */     this.jj_la1[40] = this.jj_gen;
/* 1662 */     jj_consume_token(-1);
/* 1663 */     throw new ParseException();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final void Include(GrammarSection section, Scope scope, Annotations a) throws ParseException {
/* 1672 */     Include include = section.makeInclude();
/* 1673 */     Token t = jj_consume_token(7);
/* 1674 */     String href = Literal();
/* 1675 */     String ns = Inherit();
/* 1676 */     switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
/*      */       case 11:
/* 1678 */         jj_consume_token(11);
/* 1679 */         a = IncludeBody((GrammarSection)include, scope, a);
/* 1680 */         topLevelComments((GrammarSection)include);
/* 1681 */         jj_consume_token(12);
/*      */         break;
/*      */       default:
/* 1684 */         this.jj_la1[41] = this.jj_gen;
/*      */         break;
/*      */     } 
/*      */     try {
/* 1688 */       include.endInclude(this.parseable, resolve(href), ns, makeLocation(t), a);
/*      */     }
/* 1690 */     catch (IllegalSchemaException illegalSchemaException) {}
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final Annotations IncludeBody(GrammarSection section, Scope scope, Annotations a) throws ParseException {
/* 1697 */     while (jj_2_5(2)) {
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1702 */       ParsedElementAnnotation e = AnnotationElementNotKeyword();
/* 1703 */       if (a == null) a = this.sb.makeAnnotations(null, getContext());  a.addElement(e);
/*      */     } 
/*      */     
/*      */     while (true) {
/* 1707 */       switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
/*      */         case 1:
/*      */         case 5:
/*      */         case 6:
/*      */         case 40:
/*      */         case 43:
/*      */         case 54:
/*      */         case 55:
/*      */           break;
/*      */         
/*      */         default:
/* 1718 */           this.jj_la1[42] = this.jj_gen;
/*      */           break;
/*      */       } 
/* 1721 */       IncludeComponent(section, scope);
/*      */     } 
/* 1723 */     return a;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final void IncludeComponent(GrammarSection section, Scope scope) throws ParseException {
/* 1730 */     Annotations a = Annotations();
/* 1731 */     switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
/*      */       case 5:
/*      */       case 54:
/*      */       case 55:
/* 1735 */         Definition(section, scope, a);
/*      */         break;
/*      */       case 6:
/* 1738 */         IncludeDiv(section, scope, a);
/*      */         break;
/*      */       default:
/* 1741 */         this.jj_la1[43] = this.jj_gen;
/* 1742 */         jj_consume_token(-1);
/* 1743 */         throw new ParseException();
/*      */     } 
/*      */ 
/*      */     
/* 1747 */     while (jj_2_6(2)) {
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1752 */       ParsedElementAnnotation e = AnnotationElementNotKeyword();
/* 1753 */       section.topLevelAnnotation(e);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public final void Div(GrammarSection section, Scope scope, Annotations a) throws ParseException {
/* 1759 */     Div div = section.makeDiv();
/* 1760 */     Token t = jj_consume_token(6);
/* 1761 */     jj_consume_token(11);
/* 1762 */     a = GrammarBody((GrammarSection)div, scope, a);
/* 1763 */     topLevelComments((GrammarSection)div);
/* 1764 */     jj_consume_token(12);
/* 1765 */     div.endDiv(makeLocation(t), a);
/*      */   }
/*      */ 
/*      */   
/*      */   public final void IncludeDiv(GrammarSection section, Scope scope, Annotations a) throws ParseException {
/* 1770 */     Div div = section.makeDiv();
/* 1771 */     Token t = jj_consume_token(6);
/* 1772 */     jj_consume_token(11);
/* 1773 */     a = IncludeBody((GrammarSection)div, scope, a);
/* 1774 */     topLevelComments((GrammarSection)div);
/* 1775 */     jj_consume_token(12);
/* 1776 */     div.endDiv(makeLocation(t), a);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final ParsedPattern ExternalRefExpr(Scope scope, Annotations a) throws ParseException {
/* 1783 */     Token t = jj_consume_token(33);
/* 1784 */     String href = Literal();
/* 1785 */     String ns = Inherit();
/*      */     try {
/* 1787 */       return this.sb.makeExternalRef(this.parseable, resolve(href), ns, scope, makeLocation(t), a);
/*      */     }
/* 1789 */     catch (IllegalSchemaException e) {
/* 1790 */       return this.sb.makeErrorPattern();
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public final String Inherit() throws ParseException {
/* 1796 */     String ns = null;
/* 1797 */     switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
/*      */       case 15:
/* 1799 */         jj_consume_token(15);
/* 1800 */         jj_consume_token(2);
/* 1801 */         ns = Prefix();
/*      */         break;
/*      */       default:
/* 1804 */         this.jj_la1[44] = this.jj_gen;
/*      */         break;
/*      */     } 
/* 1807 */     if (ns == null)
/* 1808 */       ns = this.defaultNamespace; 
/* 1809 */     return ns;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final ParsedPattern ParentExpr(Scope scope, Annotations a) throws ParseException {
/* 1815 */     jj_consume_token(34);
/* 1816 */     a = addCommentsToChildAnnotations(a);
/* 1817 */     LocatedString name = Identifier();
/* 1818 */     if (scope == null) {
/* 1819 */       error("parent_ref_outside_grammar", name.getToken());
/* 1820 */       return this.sb.makeErrorPattern();
/*      */     } 
/* 1822 */     return scope.makeParentRef(name.getString(), name.getLocation(), a);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final ParsedPattern IdentifierExpr(Scope scope, Annotations a) throws ParseException {
/* 1829 */     LocatedString name = Identifier();
/* 1830 */     if (scope == null) {
/* 1831 */       error("ref_outside_grammar", name.getToken());
/* 1832 */       return this.sb.makeErrorPattern();
/*      */     } 
/* 1834 */     return scope.makeRef(name.getString(), name.getLocation(), a);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final ParsedPattern ValueExpr(boolean topLevel, Annotations a) throws ParseException {
/* 1841 */     LocatedString s = LocatedLiteral();
/* 1842 */     if (topLevel && this.annotationsIncludeElements) {
/* 1843 */       error("top_level_follow_annotation", s.getToken());
/* 1844 */       a = null;
/*      */     } 
/* 1846 */     return this.sb.makeValue("", "token", s.getString(), getContext(), this.defaultNamespace, s.getLocation(), a);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final ParsedPattern DataExpr(boolean topLevel, Scope scope, Annotations a, Token[] except) throws ParseException {
/* 1854 */     String datatypeUri = null;
/* 1855 */     String s = null;
/* 1856 */     ParsedPattern e = null;
/*      */     
/* 1858 */     Token datatypeToken = DatatypeName();
/* 1859 */     String datatype = datatypeToken.image;
/* 1860 */     Location loc = makeLocation(datatypeToken);
/* 1861 */     int colon = datatype.indexOf(':');
/* 1862 */     if (colon < 0) {
/* 1863 */       datatypeUri = "";
/*      */     } else {
/* 1865 */       String prefix = datatype.substring(0, colon);
/* 1866 */       datatypeUri = lookupDatatype(prefix, datatypeToken);
/* 1867 */       datatype = datatype.substring(colon + 1);
/*      */     } 
/* 1869 */     switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
/*      */       case 58:
/* 1871 */         s = Literal();
/* 1872 */         if (topLevel && this.annotationsIncludeElements) {
/* 1873 */           error("top_level_follow_annotation", datatypeToken);
/* 1874 */           a = null;
/*      */         } 
/* 1876 */         return this.sb.makeValue(datatypeUri, datatype, s, getContext(), this.defaultNamespace, loc, a);
/*      */     } 
/*      */     
/* 1879 */     this.jj_la1[48] = this.jj_gen;
/* 1880 */     DataPatternBuilder dpb = this.sb.makeDataPatternBuilder(datatypeUri, datatype, loc);
/* 1881 */     switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
/*      */       case 11:
/* 1883 */         Params(dpb);
/* 1884 */         switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
/*      */           case 30:
/* 1886 */             e = Except(scope, except);
/*      */             break;
/*      */         } 
/* 1889 */         this.jj_la1[45] = this.jj_gen;
/*      */         break;
/*      */ 
/*      */       
/*      */       default:
/* 1894 */         this.jj_la1[47] = this.jj_gen;
/* 1895 */         switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
/*      */           case 30:
/* 1897 */             e = Except(scope, except);
/*      */             break;
/*      */         } 
/* 1900 */         this.jj_la1[46] = this.jj_gen;
/*      */         break;
/*      */     } 
/*      */     
/* 1904 */     return (e == null) ? dpb.makePattern(loc, a) : dpb.makePattern(e, loc, a);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final Token DatatypeName() throws ParseException {
/*      */     Token t;
/* 1911 */     switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
/*      */       case 35:
/* 1913 */         t = jj_consume_token(35);
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
/* 1926 */         return t;case 36: t = jj_consume_token(36); return t;case 57: t = jj_consume_token(57); return t;
/*      */     } 
/*      */     this.jj_la1[49] = this.jj_gen;
/*      */     jj_consume_token(-1);
/*      */     throw new ParseException();
/*      */   } public final LocatedString Identifier() throws ParseException { LocatedString s;
/*      */     Token t;
/* 1933 */     switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
/*      */       case 54:
/* 1935 */         t = jj_consume_token(54);
/* 1936 */         s = new LocatedString(t.image, t);
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
/* 1947 */         return s;case 55: t = jj_consume_token(55); s = new LocatedString(t.image.substring(1), t); return s;
/*      */     } 
/*      */     this.jj_la1[50] = this.jj_gen;
/*      */     jj_consume_token(-1);
/*      */     throw new ParseException(); }
/*      */   public final String Prefix() throws ParseException { Token t;
/*      */     String prefix;
/* 1954 */     switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
/*      */       case 54:
/* 1956 */         t = jj_consume_token(54);
/* 1957 */         prefix = t.image;
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
/* 1990 */         return lookupPrefix(prefix, t);case 55: t = jj_consume_token(55); prefix = t.image.substring(1); return lookupPrefix(prefix, t);case 5: case 6: case 7: case 10: case 13: case 14: case 15: case 16: case 17: case 18: case 19: case 26: case 27: case 31: case 32: case 33: case 34: case 35: case 36: t = Keyword(); prefix = t.image; return lookupPrefix(prefix, t);
/*      */     } 
/*      */     this.jj_la1[51] = this.jj_gen;
/*      */     jj_consume_token(-1);
/*      */     throw new ParseException(); }
/*      */   public final LocatedString UnprefixedName() throws ParseException { LocatedString s;
/*      */     Token t;
/* 1997 */     switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
/*      */       case 54:
/*      */       case 55:
/* 2000 */         s = Identifier();
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
/* 2029 */         return s;case 5: case 6: case 7: case 10: case 13: case 14: case 15: case 16: case 17: case 18: case 19: case 26: case 27: case 31: case 32: case 33: case 34: case 35: case 36: t = Keyword(); s = new LocatedString(t.image, t); return s;
/*      */     } 
/*      */     this.jj_la1[52] = this.jj_gen;
/*      */     jj_consume_token(-1);
/*      */     throw new ParseException(); } public final void Params(DataPatternBuilder dpb) throws ParseException {
/* 2034 */     jj_consume_token(11);
/*      */     
/*      */     while (true) {
/* 2037 */       switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
/*      */         case 1:
/*      */         case 5:
/*      */         case 6:
/*      */         case 7:
/*      */         case 10:
/*      */         case 13:
/*      */         case 14:
/*      */         case 15:
/*      */         case 16:
/*      */         case 17:
/*      */         case 18:
/*      */         case 19:
/*      */         case 26:
/*      */         case 27:
/*      */         case 31:
/*      */         case 32:
/*      */         case 33:
/*      */         case 34:
/*      */         case 35:
/*      */         case 36:
/*      */         case 40:
/*      */         case 43:
/*      */         case 54:
/*      */         case 55:
/*      */           break;
/*      */         
/*      */         default:
/* 2065 */           this.jj_la1[53] = this.jj_gen;
/*      */           break;
/*      */       } 
/* 2068 */       Param(dpb);
/*      */     } 
/* 2070 */     jj_consume_token(12);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final void Param(DataPatternBuilder dpb) throws ParseException {
/* 2077 */     Annotations a = Annotations();
/* 2078 */     LocatedString name = UnprefixedName();
/* 2079 */     jj_consume_token(2);
/* 2080 */     a = addCommentsToLeadingAnnotations(a);
/* 2081 */     String value = Literal();
/* 2082 */     dpb.addParam(name.getString(), value, getContext(), this.defaultNamespace, name.getLocation(), a);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final ParsedPattern Except(Scope scope, Token[] except) throws ParseException {
/* 2089 */     Token[] innerExcept = new Token[1];
/* 2090 */     Token t = jj_consume_token(30);
/* 2091 */     Annotations a = Annotations();
/* 2092 */     ParsedPattern p = PrimaryExpr(false, scope, a, innerExcept);
/* 2093 */     checkExcept(innerExcept);
/* 2094 */     except[0] = t;
/* 2095 */     return p;
/*      */   }
/*      */   
/*      */   public final ParsedElementAnnotation Documentation() throws ParseException {
/*      */     Token t;
/* 2100 */     CommentList comments = getComments();
/*      */ 
/*      */     
/* 2103 */     switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
/*      */       case 40:
/* 2105 */         t = jj_consume_token(40);
/*      */         break;
/*      */       case 43:
/* 2108 */         t = jj_consume_token(43);
/*      */         break;
/*      */       default:
/* 2111 */         this.jj_la1[54] = this.jj_gen;
/* 2112 */         jj_consume_token(-1);
/* 2113 */         throw new ParseException();
/*      */     } 
/* 2115 */     ElementAnnotationBuilder eab = this.sb.makeElementAnnotationBuilder("http://relaxng.org/ns/compatibility/annotations/1.0", "documentation", 
/*      */         
/* 2117 */         getCompatibilityPrefix(), 
/* 2118 */         makeLocation(t), comments, 
/*      */         
/* 2120 */         getContext());
/* 2121 */     eab.addText(mungeComment(t.image), makeLocation(t), null);
/*      */     
/*      */     while (true) {
/* 2124 */       switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
/*      */         case 41:
/*      */           break;
/*      */         
/*      */         default:
/* 2129 */           this.jj_la1[55] = this.jj_gen;
/*      */           break;
/*      */       } 
/* 2132 */       t = jj_consume_token(41);
/* 2133 */       eab.addText("\n" + mungeComment(t.image), makeLocation(t), null);
/*      */     } 
/* 2135 */     return eab.makeElementAnnotation();
/*      */   }
/*      */ 
/*      */   
/*      */   public final Annotations Annotations() throws ParseException {
/* 2140 */     CommentList comments = getComments();
/* 2141 */     Annotations a = null;
/*      */     
/* 2143 */     switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
/*      */       case 40:
/*      */       case 43:
/* 2146 */         a = this.sb.makeAnnotations(comments, getContext());
/*      */         
/*      */         while (true) {
/* 2149 */           ParsedElementAnnotation e = Documentation();
/* 2150 */           a.addElement(e);
/* 2151 */           switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
/*      */             case 40:
/*      */             case 43:
/*      */               continue;
/*      */           }  break;
/*      */         } 
/* 2157 */         this.jj_la1[56] = this.jj_gen;
/*      */ 
/*      */ 
/*      */         
/* 2161 */         comments = getComments();
/* 2162 */         if (comments != null)
/* 2163 */           a.addLeadingComment(comments); 
/*      */         break;
/*      */       default:
/* 2166 */         this.jj_la1[57] = this.jj_gen;
/*      */         break;
/*      */     } 
/* 2169 */     switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
/*      */       case 1:
/* 2171 */         jj_consume_token(1);
/* 2172 */         if (a == null) a = this.sb.makeAnnotations(comments, getContext());  clearAttributeList(); this.annotationsIncludeElements = false;
/*      */ 
/*      */         
/* 2175 */         while (jj_2_7(2))
/*      */         {
/*      */ 
/*      */ 
/*      */           
/* 2180 */           PrefixedAnnotationAttribute(a, false);
/*      */         }
/*      */         
/*      */         while (true) {
/* 2184 */           switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
/*      */             case 5:
/*      */             case 6:
/*      */             case 7:
/*      */             case 10:
/*      */             case 13:
/*      */             case 14:
/*      */             case 15:
/*      */             case 16:
/*      */             case 17:
/*      */             case 18:
/*      */             case 19:
/*      */             case 26:
/*      */             case 27:
/*      */             case 31:
/*      */             case 32:
/*      */             case 33:
/*      */             case 34:
/*      */             case 35:
/*      */             case 36:
/*      */             case 54:
/*      */             case 55:
/*      */             case 57:
/*      */               break;
/*      */             
/*      */             default:
/* 2210 */               this.jj_la1[58] = this.jj_gen;
/*      */               break;
/*      */           } 
/* 2213 */           ParsedElementAnnotation e = AnnotationElement(false);
/* 2214 */           a.addElement(e); this.annotationsIncludeElements = true;
/*      */         } 
/* 2216 */         a.addComment(getComments());
/* 2217 */         jj_consume_token(9);
/*      */         break;
/*      */       default:
/* 2220 */         this.jj_la1[59] = this.jj_gen;
/*      */         break;
/*      */     } 
/* 2223 */     if (a == null && comments != null)
/* 2224 */       a = this.sb.makeAnnotations(comments, getContext()); 
/* 2225 */     return a;
/*      */   }
/*      */ 
/*      */   
/*      */   public final void AnnotationAttribute(Annotations a) throws ParseException {
/* 2230 */     switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
/*      */       case 57:
/* 2232 */         PrefixedAnnotationAttribute(a, true);
/*      */         return;
/*      */       case 5:
/*      */       case 6:
/*      */       case 7:
/*      */       case 10:
/*      */       case 13:
/*      */       case 14:
/*      */       case 15:
/*      */       case 16:
/*      */       case 17:
/*      */       case 18:
/*      */       case 19:
/*      */       case 26:
/*      */       case 27:
/*      */       case 31:
/*      */       case 32:
/*      */       case 33:
/*      */       case 34:
/*      */       case 35:
/*      */       case 36:
/*      */       case 54:
/*      */       case 55:
/* 2255 */         UnprefixedAnnotationAttribute(a);
/*      */         return;
/*      */     } 
/* 2258 */     this.jj_la1[60] = this.jj_gen;
/* 2259 */     jj_consume_token(-1);
/* 2260 */     throw new ParseException();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final void PrefixedAnnotationAttribute(Annotations a, boolean nested) throws ParseException {
/* 2267 */     Token t = jj_consume_token(57);
/* 2268 */     jj_consume_token(2);
/* 2269 */     String value = Literal();
/* 2270 */     String qn = t.image;
/* 2271 */     int colon = qn.indexOf(':');
/* 2272 */     String prefix = qn.substring(0, colon);
/* 2273 */     String ns = lookupPrefix(prefix, t);
/* 2274 */     if (ns == this.inheritedNs) {
/* 2275 */       error("inherited_annotation_namespace", t);
/* 2276 */     } else if (ns.length() == 0 && !nested) {
/* 2277 */       error("unqualified_annotation_attribute", t);
/* 2278 */     } else if (ns.equals("http://relaxng.org/ns/structure/1.0") && !nested) {
/* 2279 */       error("relax_ng_namespace", t);
/*      */ 
/*      */ 
/*      */     
/*      */     }
/* 2284 */     else if (ns.equals("http://www.w3.org/2000/xmlns")) {
/* 2285 */       error("xmlns_annotation_attribute_uri", t);
/*      */     } else {
/* 2287 */       if (ns.length() == 0)
/* 2288 */         prefix = null; 
/* 2289 */       addAttribute(a, ns, qn.substring(colon + 1), prefix, value, t);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void UnprefixedAnnotationAttribute(Annotations a) throws ParseException {
/* 2296 */     LocatedString name = UnprefixedName();
/* 2297 */     jj_consume_token(2);
/* 2298 */     String value = Literal();
/* 2299 */     if (name.getString().equals("xmlns")) {
/* 2300 */       error("xmlns_annotation_attribute", name.getToken());
/*      */     } else {
/* 2302 */       addAttribute(a, "", name.getString(), null, value, name.getToken());
/*      */     } 
/*      */   }
/*      */   
/*      */   public final ParsedElementAnnotation AnnotationElement(boolean nested) throws ParseException { ParsedElementAnnotation a;
/* 2307 */     switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
/*      */       case 57:
/* 2309 */         a = PrefixedAnnotationElement(nested);
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
/* 2339 */         return a;case 5: case 6: case 7: case 10: case 13: case 14: case 15: case 16: case 17: case 18: case 19: case 26: case 27: case 31: case 32: case 33: case 34: case 35: case 36: case 54: case 55: a = UnprefixedAnnotationElement(); return a;
/*      */     } 
/*      */     this.jj_la1[61] = this.jj_gen;
/*      */     jj_consume_token(-1);
/*      */     throw new ParseException(); }
/*      */   public final ParsedElementAnnotation AnnotationElementNotKeyword() throws ParseException { ParsedElementAnnotation a;
/* 2345 */     switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
/*      */       case 57:
/* 2347 */         a = PrefixedAnnotationElement(false);
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
/* 2358 */         return a;case 54: case 55: a = IdentifierAnnotationElement(); return a;
/*      */     } 
/*      */     this.jj_la1[62] = this.jj_gen;
/*      */     jj_consume_token(-1);
/*      */     throw new ParseException(); } public final ParsedElementAnnotation PrefixedAnnotationElement(boolean nested) throws ParseException {
/* 2363 */     CommentList comments = getComments();
/*      */ 
/*      */     
/* 2366 */     Token t = jj_consume_token(57);
/* 2367 */     String qn = t.image;
/* 2368 */     int colon = qn.indexOf(':');
/* 2369 */     String prefix = qn.substring(0, colon);
/* 2370 */     String ns = lookupPrefix(prefix, t);
/* 2371 */     if (ns == this.inheritedNs) {
/* 2372 */       error("inherited_annotation_namespace", t);
/* 2373 */       ns = "";
/*      */     }
/* 2375 */     else if (!nested && ns.equals("http://relaxng.org/ns/structure/1.0")) {
/* 2376 */       error("relax_ng_namespace", t);
/* 2377 */       ns = "";
/*      */     
/*      */     }
/* 2380 */     else if (ns.length() == 0) {
/* 2381 */       prefix = null;
/*      */     } 
/* 2383 */     ElementAnnotationBuilder eab = this.sb.makeElementAnnotationBuilder(ns, qn.substring(colon + 1), prefix, 
/* 2384 */         makeLocation(t), comments, getContext());
/* 2385 */     AnnotationElementContent(eab);
/* 2386 */     return eab.makeElementAnnotation();
/*      */   }
/*      */ 
/*      */   
/*      */   public final ParsedElementAnnotation UnprefixedAnnotationElement() throws ParseException {
/* 2391 */     CommentList comments = getComments();
/*      */ 
/*      */     
/* 2394 */     LocatedString name = UnprefixedName();
/* 2395 */     ElementAnnotationBuilder eab = this.sb.makeElementAnnotationBuilder("", name.getString(), null, name
/* 2396 */         .getLocation(), comments, getContext());
/* 2397 */     AnnotationElementContent(eab);
/* 2398 */     return eab.makeElementAnnotation();
/*      */   }
/*      */ 
/*      */   
/*      */   public final ParsedElementAnnotation IdentifierAnnotationElement() throws ParseException {
/* 2403 */     CommentList comments = getComments();
/*      */ 
/*      */     
/* 2406 */     LocatedString name = Identifier();
/* 2407 */     ElementAnnotationBuilder eab = this.sb.makeElementAnnotationBuilder("", name.getString(), null, name
/* 2408 */         .getLocation(), comments, getContext());
/* 2409 */     AnnotationElementContent(eab);
/* 2410 */     return eab.makeElementAnnotation();
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void AnnotationElementContent(ElementAnnotationBuilder eab) throws ParseException {
/* 2416 */     jj_consume_token(1);
/* 2417 */     clearAttributeList();
/*      */ 
/*      */     
/* 2420 */     while (jj_2_8(2))
/*      */     {
/*      */ 
/*      */ 
/*      */       
/* 2425 */       AnnotationAttribute((Annotations)eab);
/*      */     }
/*      */     label34: while (true) {
/*      */       ParsedElementAnnotation e;
/* 2429 */       switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
/*      */         case 5:
/*      */         case 6:
/*      */         case 7:
/*      */         case 10:
/*      */         case 13:
/*      */         case 14:
/*      */         case 15:
/*      */         case 16:
/*      */         case 17:
/*      */         case 18:
/*      */         case 19:
/*      */         case 26:
/*      */         case 27:
/*      */         case 31:
/*      */         case 32:
/*      */         case 33:
/*      */         case 34:
/*      */         case 35:
/*      */         case 36:
/*      */         case 54:
/*      */         case 55:
/*      */         case 57:
/*      */         case 58:
/*      */           break;
/*      */         
/*      */         default:
/* 2456 */           this.jj_la1[63] = this.jj_gen;
/*      */           break;
/*      */       } 
/* 2459 */       switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
/*      */         case 58:
/* 2461 */           AnnotationElementLiteral(eab);
/*      */           
/*      */           while (true) {
/* 2464 */             switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
/*      */               case 8:
/*      */                 break;
/*      */               
/*      */               default:
/* 2469 */                 this.jj_la1[64] = this.jj_gen;
/*      */                 continue label34;
/*      */             } 
/* 2472 */             jj_consume_token(8);
/* 2473 */             AnnotationElementLiteral(eab);
/*      */           } 
/*      */         
/*      */         case 5:
/*      */         case 6:
/*      */         case 7:
/*      */         case 10:
/*      */         case 13:
/*      */         case 14:
/*      */         case 15:
/*      */         case 16:
/*      */         case 17:
/*      */         case 18:
/*      */         case 19:
/*      */         case 26:
/*      */         case 27:
/*      */         case 31:
/*      */         case 32:
/*      */         case 33:
/*      */         case 34:
/*      */         case 35:
/*      */         case 36:
/*      */         case 54:
/*      */         case 55:
/*      */         case 57:
/* 2498 */           e = AnnotationElement(true);
/* 2499 */           eab.addElement(e);
/*      */           continue;
/*      */       } 
/* 2502 */       this.jj_la1[65] = this.jj_gen;
/* 2503 */       jj_consume_token(-1);
/* 2504 */       throw new ParseException();
/*      */     } 
/*      */     
/* 2507 */     eab.addComment(getComments());
/* 2508 */     jj_consume_token(9);
/*      */   }
/*      */ 
/*      */   
/*      */   public final void AnnotationElementLiteral(ElementAnnotationBuilder eab) throws ParseException {
/* 2513 */     CommentList comments = getComments();
/* 2514 */     Token t = jj_consume_token(58);
/* 2515 */     eab.addText(unquote(t.image), makeLocation(t), comments);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final String Literal() throws ParseException {
/*      */     StringBuffer buf;
/* 2522 */     Token t = jj_consume_token(58);
/* 2523 */     String s = unquote(t.image);
/* 2524 */     switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk)
/*      */     { case 8:
/* 2526 */         buf = new StringBuffer(s);
/*      */         
/*      */         while (true) {
/* 2529 */           jj_consume_token(8);
/* 2530 */           t = jj_consume_token(58);
/* 2531 */           buf.append(unquote(t.image));
/* 2532 */           switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
/*      */             case 8:
/*      */               continue;
/*      */           }  break;
/*      */         } 
/* 2537 */         this.jj_la1[66] = this.jj_gen;
/*      */ 
/*      */ 
/*      */         
/* 2541 */         s = buf.toString();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 2547 */         return s; }  this.jj_la1[67] = this.jj_gen; return s;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final LocatedString LocatedLiteral() throws ParseException {
/*      */     StringBuffer buf;
/* 2556 */     Token t = jj_consume_token(58);
/* 2557 */     String s = unquote(t.image);
/* 2558 */     switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk)
/*      */     { case 8:
/* 2560 */         buf = new StringBuffer(s);
/*      */         
/*      */         while (true) {
/* 2563 */           jj_consume_token(8);
/* 2564 */           Token t2 = jj_consume_token(58);
/* 2565 */           buf.append(unquote(t2.image));
/* 2566 */           switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
/*      */             case 8:
/*      */               continue;
/*      */           }  break;
/*      */         } 
/* 2571 */         this.jj_la1[68] = this.jj_gen;
/*      */ 
/*      */ 
/*      */         
/* 2575 */         s = buf.toString();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 2581 */         return new LocatedString(s, t); }  this.jj_la1[69] = this.jj_gen; return new LocatedString(s, t);
/*      */   }
/*      */   
/*      */   public final Token Keyword() throws ParseException
/*      */   {
/*      */     Token t;
/* 2587 */     switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
/*      */       case 26:
/* 2589 */         t = jj_consume_token(26);
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
/* 2650 */         return t;case 27: t = jj_consume_token(27); return t;case 13: t = jj_consume_token(13); return t;case 31: t = jj_consume_token(31); return t;case 32: t = jj_consume_token(32); return t;case 10: t = jj_consume_token(10); return t;case 17: t = jj_consume_token(17); return t;case 18: t = jj_consume_token(18); return t;case 34: t = jj_consume_token(34); return t;case 33: t = jj_consume_token(33); return t;case 19: t = jj_consume_token(19); return t;case 5: t = jj_consume_token(5); return t;case 7: t = jj_consume_token(7); return t;case 14: t = jj_consume_token(14); return t;case 15: t = jj_consume_token(15); return t;case 35: t = jj_consume_token(35); return t;case 36: t = jj_consume_token(36); return t;case 16: t = jj_consume_token(16); return t;case 6: t = jj_consume_token(6); return t;
/*      */     } 
/*      */     this.jj_la1[70] = this.jj_gen;
/*      */     jj_consume_token(-1);
/*      */     throw new ParseException(); } private boolean jj_2_1(int xla) {
/* 2655 */     this.jj_la = xla; this.jj_lastpos = this.jj_scanpos = this.token; 
/* 2656 */     try { return !jj_3_1(); }
/* 2657 */     catch (LookaheadSuccess ls) { return true; }
/* 2658 */     finally { jj_save(0, xla); }
/*      */   
/*      */   }
/*      */   private boolean jj_2_2(int xla) {
/* 2662 */     this.jj_la = xla; this.jj_lastpos = this.jj_scanpos = this.token; 
/* 2663 */     try { return !jj_3_2(); }
/* 2664 */     catch (LookaheadSuccess ls) { return true; }
/* 2665 */     finally { jj_save(1, xla); }
/*      */   
/*      */   }
/*      */   private boolean jj_2_3(int xla) {
/* 2669 */     this.jj_la = xla; this.jj_lastpos = this.jj_scanpos = this.token; 
/* 2670 */     try { return !jj_3_3(); }
/* 2671 */     catch (LookaheadSuccess ls) { return true; }
/* 2672 */     finally { jj_save(2, xla); }
/*      */   
/*      */   }
/*      */   private boolean jj_2_4(int xla) {
/* 2676 */     this.jj_la = xla; this.jj_lastpos = this.jj_scanpos = this.token; 
/* 2677 */     try { return !jj_3_4(); }
/* 2678 */     catch (LookaheadSuccess ls) { return true; }
/* 2679 */     finally { jj_save(3, xla); }
/*      */   
/*      */   }
/*      */   private boolean jj_2_5(int xla) {
/* 2683 */     this.jj_la = xla; this.jj_lastpos = this.jj_scanpos = this.token; 
/* 2684 */     try { return !jj_3_5(); }
/* 2685 */     catch (LookaheadSuccess ls) { return true; }
/* 2686 */     finally { jj_save(4, xla); }
/*      */   
/*      */   }
/*      */   private boolean jj_2_6(int xla) {
/* 2690 */     this.jj_la = xla; this.jj_lastpos = this.jj_scanpos = this.token; 
/* 2691 */     try { return !jj_3_6(); }
/* 2692 */     catch (LookaheadSuccess ls) { return true; }
/* 2693 */     finally { jj_save(5, xla); }
/*      */   
/*      */   }
/*      */   private boolean jj_2_7(int xla) {
/* 2697 */     this.jj_la = xla; this.jj_lastpos = this.jj_scanpos = this.token; 
/* 2698 */     try { return !jj_3_7(); }
/* 2699 */     catch (LookaheadSuccess ls) { return true; }
/* 2700 */     finally { jj_save(6, xla); }
/*      */   
/*      */   }
/*      */   private boolean jj_2_8(int xla) {
/* 2704 */     this.jj_la = xla; this.jj_lastpos = this.jj_scanpos = this.token; 
/* 2705 */     try { return !jj_3_8(); }
/* 2706 */     catch (LookaheadSuccess ls) { return true; }
/* 2707 */     finally { jj_save(7, xla); }
/*      */   
/*      */   }
/*      */   private boolean jj_3R_43() {
/* 2711 */     if (jj_scan_token(1)) return true;
/*      */     
/*      */     while (true) {
/* 2714 */       Token xsp = this.jj_scanpos;
/* 2715 */       if (jj_3R_52()) { this.jj_scanpos = xsp;
/*      */         
/* 2717 */         if (jj_scan_token(9)) return true; 
/* 2718 */         return false; }
/*      */     
/*      */     } 
/*      */   } private boolean jj_3R_51() {
/* 2722 */     if (jj_scan_token(55)) return true; 
/* 2723 */     return false;
/*      */   }
/*      */   
/*      */   private boolean jj_3R_50() {
/* 2727 */     if (jj_scan_token(54)) return true; 
/* 2728 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   private boolean jj_3R_41() {
/* 2733 */     Token xsp = this.jj_scanpos;
/* 2734 */     if (jj_3R_50()) {
/* 2735 */       this.jj_scanpos = xsp;
/* 2736 */       if (jj_3R_51()) return true; 
/*      */     } 
/* 2738 */     return false;
/*      */   }
/*      */   
/*      */   private boolean jj_3R_47() {
/* 2742 */     if (jj_scan_token(57)) return true; 
/* 2743 */     if (jj_3R_56()) return true; 
/* 2744 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   private boolean jj_3R_55() {
/* 2749 */     Token xsp = this.jj_scanpos;
/* 2750 */     if (jj_scan_token(40)) {
/* 2751 */       this.jj_scanpos = xsp;
/* 2752 */       if (jj_scan_token(43)) return true; 
/*      */     } 
/*      */     while (true) {
/* 2755 */       xsp = this.jj_scanpos;
/* 2756 */       if (jj_scan_token(41)) { this.jj_scanpos = xsp;
/*      */         
/* 2758 */         return false; }
/*      */     
/*      */     } 
/*      */   }
/*      */   private boolean jj_3R_45() {
/* 2763 */     if (jj_3R_55()) return true; 
/*      */     while (true) {
/* 2765 */       Token xsp = this.jj_scanpos;
/* 2766 */       if (jj_3R_55()) { this.jj_scanpos = xsp;
/*      */         
/* 2768 */         return false; }
/*      */     
/*      */     } 
/*      */   } private boolean jj_3R_38() {
/* 2772 */     if (jj_3R_48()) return true; 
/* 2773 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   private boolean jj_3R_42() {
/* 2778 */     Token xsp = this.jj_scanpos;
/* 2779 */     if (jj_scan_token(5)) {
/* 2780 */       this.jj_scanpos = xsp;
/* 2781 */       if (jj_scan_token(6)) {
/* 2782 */         this.jj_scanpos = xsp;
/* 2783 */         if (jj_scan_token(7)) return true; 
/*      */       } 
/*      */     } 
/* 2786 */     return false;
/*      */   }
/*      */   
/*      */   private boolean jj_3R_37() {
/* 2790 */     if (jj_3R_47()) return true; 
/* 2791 */     return false;
/*      */   }
/*      */   
/*      */   private boolean jj_3R_54() {
/* 2795 */     if (jj_3R_42()) return true; 
/* 2796 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   private boolean jj_3R_29() {
/* 2801 */     Token xsp = this.jj_scanpos;
/* 2802 */     if (jj_3R_37()) {
/* 2803 */       this.jj_scanpos = xsp;
/* 2804 */       if (jj_3R_38()) return true; 
/*      */     } 
/* 2806 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   private boolean jj_3R_44() {
/* 2811 */     Token xsp = this.jj_scanpos;
/* 2812 */     if (jj_3R_53()) {
/* 2813 */       this.jj_scanpos = xsp;
/* 2814 */       if (jj_3R_54()) return true; 
/*      */     } 
/* 2816 */     return false;
/*      */   }
/*      */   
/*      */   private boolean jj_3R_53() {
/* 2820 */     if (jj_3R_41()) return true;
/*      */     
/* 2822 */     Token xsp = this.jj_scanpos;
/* 2823 */     if (jj_scan_token(2)) {
/* 2824 */       this.jj_scanpos = xsp;
/* 2825 */       if (jj_scan_token(3)) {
/* 2826 */         this.jj_scanpos = xsp;
/* 2827 */         if (jj_scan_token(4)) return true; 
/*      */       } 
/*      */     } 
/* 2830 */     return false;
/*      */   }
/*      */   
/*      */   private boolean jj_3R_36() {
/* 2834 */     if (jj_3R_45()) return true;
/*      */     
/* 2836 */     Token xsp = this.jj_scanpos;
/* 2837 */     if (jj_3R_46()) this.jj_scanpos = xsp; 
/* 2838 */     if (jj_3R_44()) return true; 
/* 2839 */     return false;
/*      */   }
/*      */   
/*      */   private boolean jj_3R_35() {
/* 2843 */     if (jj_3R_43()) return true; 
/* 2844 */     if (jj_3R_44()) return true; 
/* 2845 */     return false;
/*      */   }
/*      */   
/*      */   private boolean jj_3R_34() {
/* 2849 */     if (jj_3R_42()) return true; 
/* 2850 */     return false;
/*      */   }
/*      */   
/*      */   private boolean jj_3R_33() {
/* 2854 */     if (jj_3R_41()) return true;
/*      */     
/* 2856 */     Token xsp = this.jj_scanpos;
/* 2857 */     if (jj_scan_token(1)) {
/* 2858 */       this.jj_scanpos = xsp;
/* 2859 */       if (jj_scan_token(2)) {
/* 2860 */         this.jj_scanpos = xsp;
/* 2861 */         if (jj_scan_token(3)) {
/* 2862 */           this.jj_scanpos = xsp;
/* 2863 */           if (jj_scan_token(4)) return true; 
/*      */         } 
/*      */       } 
/*      */     } 
/* 2867 */     return false;
/*      */   }
/*      */   
/*      */   private boolean jj_3_1() {
/* 2871 */     if (jj_3R_28()) return true; 
/* 2872 */     return false;
/*      */   }
/*      */   
/*      */   private boolean jj_3R_32() {
/* 2876 */     if (jj_scan_token(57)) return true; 
/* 2877 */     if (jj_scan_token(1)) return true; 
/* 2878 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   private boolean jj_3R_28() {
/* 2883 */     Token xsp = this.jj_scanpos;
/* 2884 */     if (jj_3R_32()) {
/* 2885 */       this.jj_scanpos = xsp;
/* 2886 */       if (jj_3R_33()) {
/* 2887 */         this.jj_scanpos = xsp;
/* 2888 */         if (jj_3R_34()) {
/* 2889 */           this.jj_scanpos = xsp;
/* 2890 */           if (jj_3R_35()) {
/* 2891 */             this.jj_scanpos = xsp;
/* 2892 */             if (jj_3R_36()) return true; 
/*      */           } 
/*      */         } 
/*      */       } 
/*      */     } 
/* 2897 */     return false;
/*      */   }
/*      */   
/*      */   private boolean jj_3R_59() {
/* 2901 */     if (jj_3R_43()) return true; 
/* 2902 */     return false;
/*      */   }
/*      */   
/*      */   private boolean jj_3_8() {
/* 2906 */     if (jj_3R_31()) return true; 
/* 2907 */     return false;
/*      */   }
/*      */   
/*      */   private boolean jj_3R_56() {
/* 2911 */     if (jj_scan_token(1)) return true; 
/* 2912 */     return false;
/*      */   }
/*      */   
/*      */   private boolean jj_3R_49() {
/* 2916 */     if (jj_3R_57()) return true; 
/* 2917 */     if (jj_scan_token(2)) return true; 
/* 2918 */     return false;
/*      */   }
/*      */   
/*      */   private boolean jj_3R_40() {
/* 2922 */     if (jj_3R_49()) return true; 
/* 2923 */     return false;
/*      */   }
/*      */   
/*      */   private boolean jj_3_4() {
/* 2927 */     if (jj_3R_29()) return true; 
/* 2928 */     return false;
/*      */   }
/*      */   
/*      */   private boolean jj_3R_48() {
/* 2932 */     if (jj_3R_41()) return true; 
/* 2933 */     if (jj_3R_56()) return true; 
/* 2934 */     return false;
/*      */   }
/*      */   
/*      */   private boolean jj_3_3() {
/* 2938 */     if (jj_3R_29()) return true; 
/* 2939 */     return false;
/*      */   }
/*      */   
/*      */   private boolean jj_3_6() {
/* 2943 */     if (jj_3R_29()) return true; 
/* 2944 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   private boolean jj_3R_62() {
/* 2949 */     Token xsp = this.jj_scanpos;
/* 2950 */     if (jj_scan_token(26)) {
/* 2951 */       this.jj_scanpos = xsp;
/* 2952 */       if (jj_scan_token(27)) {
/* 2953 */         this.jj_scanpos = xsp;
/* 2954 */         if (jj_scan_token(13)) {
/* 2955 */           this.jj_scanpos = xsp;
/* 2956 */           if (jj_scan_token(31)) {
/* 2957 */             this.jj_scanpos = xsp;
/* 2958 */             if (jj_scan_token(32)) {
/* 2959 */               this.jj_scanpos = xsp;
/* 2960 */               if (jj_scan_token(10)) {
/* 2961 */                 this.jj_scanpos = xsp;
/* 2962 */                 if (jj_scan_token(17)) {
/* 2963 */                   this.jj_scanpos = xsp;
/* 2964 */                   if (jj_scan_token(18)) {
/* 2965 */                     this.jj_scanpos = xsp;
/* 2966 */                     if (jj_scan_token(34)) {
/* 2967 */                       this.jj_scanpos = xsp;
/* 2968 */                       if (jj_scan_token(33)) {
/* 2969 */                         this.jj_scanpos = xsp;
/* 2970 */                         if (jj_scan_token(19)) {
/* 2971 */                           this.jj_scanpos = xsp;
/* 2972 */                           if (jj_scan_token(5)) {
/* 2973 */                             this.jj_scanpos = xsp;
/* 2974 */                             if (jj_scan_token(7)) {
/* 2975 */                               this.jj_scanpos = xsp;
/* 2976 */                               if (jj_scan_token(14)) {
/* 2977 */                                 this.jj_scanpos = xsp;
/* 2978 */                                 if (jj_scan_token(15)) {
/* 2979 */                                   this.jj_scanpos = xsp;
/* 2980 */                                   if (jj_scan_token(35)) {
/* 2981 */                                     this.jj_scanpos = xsp;
/* 2982 */                                     if (jj_scan_token(36)) {
/* 2983 */                                       this.jj_scanpos = xsp;
/* 2984 */                                       if (jj_scan_token(16)) {
/* 2985 */                                         this.jj_scanpos = xsp;
/* 2986 */                                         if (jj_scan_token(6)) return true; 
/*      */                                       } 
/*      */                                     } 
/*      */                                   } 
/*      */                                 } 
/*      */                               } 
/*      */                             } 
/*      */                           } 
/*      */                         } 
/*      */                       } 
/*      */                     } 
/*      */                   } 
/*      */                 } 
/*      */               } 
/*      */             } 
/*      */           } 
/*      */         } 
/*      */       } 
/*      */     } 
/* 3005 */     return false;
/*      */   }
/*      */   
/*      */   private boolean jj_3R_61() {
/* 3009 */     if (jj_3R_62()) return true; 
/* 3010 */     return false;
/*      */   }
/*      */   
/*      */   private boolean jj_3_2() {
/* 3014 */     if (jj_3R_28()) return true; 
/* 3015 */     return false;
/*      */   }
/*      */   
/*      */   private boolean jj_3R_30() {
/* 3019 */     if (jj_scan_token(57)) return true; 
/* 3020 */     if (jj_scan_token(2)) return true; 
/* 3021 */     return false;
/*      */   }
/*      */   
/*      */   private boolean jj_3R_60() {
/* 3025 */     if (jj_3R_41()) return true; 
/* 3026 */     return false;
/*      */   }
/*      */   
/*      */   private boolean jj_3R_58() {
/* 3030 */     if (jj_3R_57()) return true; 
/* 3031 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   private boolean jj_3R_57() {
/* 3036 */     Token xsp = this.jj_scanpos;
/* 3037 */     if (jj_3R_60()) {
/* 3038 */       this.jj_scanpos = xsp;
/* 3039 */       if (jj_3R_61()) return true; 
/*      */     } 
/* 3041 */     return false;
/*      */   }
/*      */   
/*      */   private boolean jj_3_5() {
/* 3045 */     if (jj_3R_29()) return true; 
/* 3046 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   private boolean jj_3R_31() {
/* 3051 */     Token xsp = this.jj_scanpos;
/* 3052 */     if (jj_3R_39()) {
/* 3053 */       this.jj_scanpos = xsp;
/* 3054 */       if (jj_3R_40()) return true; 
/*      */     } 
/* 3056 */     return false;
/*      */   }
/*      */   
/*      */   private boolean jj_3R_39() {
/* 3060 */     if (jj_3R_30()) return true; 
/* 3061 */     return false;
/*      */   }
/*      */   
/*      */   private boolean jj_3_7() {
/* 3065 */     if (jj_3R_30()) return true; 
/* 3066 */     return false;
/*      */   }
/*      */   
/*      */   private boolean jj_3R_46() {
/* 3070 */     if (jj_3R_43()) return true; 
/* 3071 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   private boolean jj_3R_52() {
/* 3076 */     Token xsp = this.jj_scanpos;
/* 3077 */     if (jj_scan_token(57)) {
/* 3078 */       this.jj_scanpos = xsp;
/* 3079 */       if (jj_3R_58()) {
/* 3080 */         this.jj_scanpos = xsp;
/* 3081 */         if (jj_scan_token(2)) {
/* 3082 */           this.jj_scanpos = xsp;
/* 3083 */           if (jj_scan_token(58)) {
/* 3084 */             this.jj_scanpos = xsp;
/* 3085 */             if (jj_scan_token(8)) {
/* 3086 */               this.jj_scanpos = xsp;
/* 3087 */               if (jj_3R_59()) return true; 
/*      */             } 
/*      */           } 
/*      */         } 
/*      */       } 
/*      */     } 
/* 3093 */     return false;
/*      */   }
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
/* 3107 */   private final int[] jj_la1 = new int[71]; private static int[] jj_la1_0;
/*      */   private static int[] jj_la1_1;
/*      */   
/*      */   static {
/* 3111 */     jj_la1_init_0();
/* 3112 */     jj_la1_init_1();
/*      */   }
/*      */   private static void jj_la1_init_0() {
/* 3115 */     jj_la1_0 = new int[] { -1676803070, 30, 2, 226, 28, 224, 224, 0, 0, 0, -1945115162, -1945115162, 1026, 90112, 90112, -1945115424, 24576, 32768, 0, -1676803072, 1048576, 2097152, 4194304, 7340032, 7340032, 58720256, 0, 58720256, -1643125536, 0, 1048576, 1048576, -1643125536, -1676679968, 33554432, 1073741824, 1073741824, 226, 224, 32, 28, 2048, 98, 96, 32768, 1073741824, 1073741824, 2048, 0, 0, 0, -1945115424, -1945115424, -1945115422, 0, 0, 0, 0, -1945115424, 2, -1945115424, -1945115424, 0, -1945115424, 256, -1945115424, 256, 256, 256, 256, -1945115424 };
/*      */   }
/*      */   private static void jj_la1_init_1() {
/* 3118 */     jj_la1_1 = new int[] { 113248543, 0, 0, 46139648, 0, 12582912, 0, 2304, 512, 2304, 113246239, 113246239, 2304, 0, 0, 12582943, 0, 67108864, 134217728, 113246239, 0, 0, 0, 0, 0, 0, 134217728, 0, 62914591, 134217728, 0, 0, 62914591, 46137375, 16777216, 0, 0, 12585216, 12582912, 12582912, 0, 0, 12585216, 12582912, 0, 0, 0, 0, 67108864, 33554456, 12582912, 12582943, 12582943, 12585247, 2304, 512, 2304, 2304, 46137375, 0, 46137375, 46137375, 46137344, 113246239, 0, 113246239, 0, 0, 0, 0, 31 };
/*      */   }
/* 3120 */   private final JJCalls[] jj_2_rtns = new JJCalls[8];
/*      */   private boolean jj_rescan = false;
/* 3122 */   private int jj_gc = 0; private final LookaheadSuccess jj_ls; private List<int[]> jj_expentries; private int[] jj_expentry; private int jj_kind; private int[] jj_lasttokens;
/*      */   private int jj_endpos;
/*      */   
/*      */   public CompactSyntax(InputStream stream) {
/* 3126 */     this(stream, null);
/*      */   }
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
/*      */   public void ReInit(InputStream stream) {
/* 3141 */     ReInit(stream, null);
/*      */   }
/*      */   public void ReInit(InputStream stream, String encoding) {
/*      */     
/* 3145 */     try { this.jj_input_stream.ReInit(stream, encoding, 1, 1); } catch (UnsupportedEncodingException e) { throw new RuntimeException(e); }
/* 3146 */      this.token_source.ReInit(this.jj_input_stream);
/* 3147 */     this.token = new Token();
/* 3148 */     this.jj_ntk = -1;
/* 3149 */     this.jj_gen = 0; int i;
/* 3150 */     for (i = 0; i < 71; ) { this.jj_la1[i] = -1; i++; }
/* 3151 */      for (i = 0; i < this.jj_2_rtns.length; ) { this.jj_2_rtns[i] = new JJCalls(); i++; }
/*      */   
/*      */   }
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
/*      */   public void ReInit(Reader stream) {
/* 3167 */     this.jj_input_stream.ReInit(stream, 1, 1);
/* 3168 */     this.token_source.ReInit(this.jj_input_stream);
/* 3169 */     this.token = new Token();
/* 3170 */     this.jj_ntk = -1;
/* 3171 */     this.jj_gen = 0; int i;
/* 3172 */     for (i = 0; i < 71; ) { this.jj_la1[i] = -1; i++; }
/* 3173 */      for (i = 0; i < this.jj_2_rtns.length; ) { this.jj_2_rtns[i] = new JJCalls(); i++; }
/*      */   
/*      */   }
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
/*      */   public void ReInit(CompactSyntaxTokenManager tm) {
/* 3188 */     this.token_source = tm;
/* 3189 */     this.token = new Token();
/* 3190 */     this.jj_ntk = -1;
/* 3191 */     this.jj_gen = 0; int i;
/* 3192 */     for (i = 0; i < 71; ) { this.jj_la1[i] = -1; i++; }
/* 3193 */      for (i = 0; i < this.jj_2_rtns.length; ) { this.jj_2_rtns[i] = new JJCalls(); i++; }
/*      */   
/*      */   }
/*      */   private Token jj_consume_token(int kind) throws ParseException {
/*      */     Token oldToken;
/* 3198 */     if ((oldToken = this.token).next != null) { this.token = this.token.next; }
/* 3199 */     else { this.token = this.token.next = this.token_source.getNextToken(); }
/* 3200 */      this.jj_ntk = -1;
/* 3201 */     if (this.token.kind == kind) {
/* 3202 */       this.jj_gen++;
/* 3203 */       if (++this.jj_gc > 100) {
/* 3204 */         this.jj_gc = 0;
/* 3205 */         for (int i = 0; i < this.jj_2_rtns.length; i++) {
/* 3206 */           JJCalls c = this.jj_2_rtns[i];
/* 3207 */           while (c != null) {
/* 3208 */             if (c.gen < this.jj_gen) c.first = null; 
/* 3209 */             c = c.next;
/*      */           } 
/*      */         } 
/*      */       } 
/* 3213 */       return this.token;
/*      */     } 
/* 3215 */     this.token = oldToken;
/* 3216 */     this.jj_kind = kind;
/* 3217 */     throw generateParseException();
/*      */   }
/*      */   private static final class LookaheadSuccess extends Error {
/*      */     private LookaheadSuccess() {} }
/* 3221 */   public CompactSyntax(InputStream stream, String encoding) { this.jj_ls = new LookaheadSuccess();
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
/* 3270 */     this.jj_expentries = (List)new ArrayList<>();
/*      */     
/* 3272 */     this.jj_kind = -1;
/* 3273 */     this.jj_lasttokens = new int[100]; try { this.jj_input_stream = new JavaCharStream(stream, encoding, 1, 1); } catch (UnsupportedEncodingException e) { throw new RuntimeException(e); }  this.token_source = new CompactSyntaxTokenManager(this.jj_input_stream); this.token = new Token(); this.jj_ntk = -1; this.jj_gen = 0; int i; for (i = 0; i < 71; ) { this.jj_la1[i] = -1; i++; }  for (i = 0; i < this.jj_2_rtns.length; ) { this.jj_2_rtns[i] = new JJCalls(); i++; }  } public CompactSyntax(Reader stream) { this.jj_ls = new LookaheadSuccess(); this.jj_expentries = (List)new ArrayList<>(); this.jj_kind = -1; this.jj_lasttokens = new int[100]; this.jj_input_stream = new JavaCharStream(stream, 1, 1); this.token_source = new CompactSyntaxTokenManager(this.jj_input_stream); this.token = new Token(); this.jj_ntk = -1; this.jj_gen = 0; int i; for (i = 0; i < 71; ) { this.jj_la1[i] = -1; i++; }  for (i = 0; i < this.jj_2_rtns.length; ) { this.jj_2_rtns[i] = new JJCalls(); i++; }  } public CompactSyntax(CompactSyntaxTokenManager tm) { this.jj_ls = new LookaheadSuccess(); this.jj_expentries = (List)new ArrayList<>(); this.jj_kind = -1; this.jj_lasttokens = new int[100]; this.token_source = tm; this.token = new Token(); this.jj_ntk = -1; this.jj_gen = 0; int i; for (i = 0; i < 71; ) { this.jj_la1[i] = -1; i++; }  for (i = 0; i < this.jj_2_rtns.length; ) { this.jj_2_rtns[i] = new JJCalls(); i++; }  }
/*      */   private boolean jj_scan_token(int kind) { if (this.jj_scanpos == this.jj_lastpos) { this.jj_la--; if (this.jj_scanpos.next == null) { this.jj_lastpos = this.jj_scanpos = this.jj_scanpos.next = this.token_source.getNextToken(); } else { this.jj_lastpos = this.jj_scanpos = this.jj_scanpos.next; }  } else { this.jj_scanpos = this.jj_scanpos.next; }  if (this.jj_rescan) { int i = 0; Token tok = this.token; while (tok != null && tok != this.jj_scanpos) { i++; tok = tok.next; }  if (tok != null)
/*      */         jj_add_error_token(kind, i);  }  if (this.jj_scanpos.kind != kind)
/*      */       return true;  if (this.jj_la == 0 && this.jj_scanpos == this.jj_lastpos)
/* 3277 */       throw this.jj_ls;  return false; } private void jj_add_error_token(int kind, int pos) { if (pos >= 100)
/* 3278 */       return;  if (pos == this.jj_endpos + 1)
/* 3279 */     { this.jj_lasttokens[this.jj_endpos++] = kind; }
/* 3280 */     else if (this.jj_endpos != 0)
/* 3281 */     { this.jj_expentry = new int[this.jj_endpos];
/* 3282 */       for (int i = 0; i < this.jj_endpos; i++)
/* 3283 */         this.jj_expentry[i] = this.jj_lasttokens[i]; 
/*      */       Iterator<?> it;
/* 3285 */       label31: for (it = this.jj_expentries.iterator(); it.hasNext(); ) {
/* 3286 */         int[] oldentry = (int[])it.next();
/* 3287 */         if (oldentry.length == this.jj_expentry.length) {
/* 3288 */           for (int j = 0; j < this.jj_expentry.length; j++) {
/* 3289 */             if (oldentry[j] != this.jj_expentry[j]) {
/*      */               continue label31;
/*      */             }
/*      */           } 
/* 3293 */           this.jj_expentries.add(this.jj_expentry);
/*      */           break;
/*      */         } 
/*      */       } 
/* 3297 */       if (pos != 0) this.jj_lasttokens[(this.jj_endpos = pos) - 1] = kind;  }  }
/*      */   public final Token getNextToken() { if (this.token.next != null) { this.token = this.token.next; } else { this.token = this.token.next = this.token_source.getNextToken(); }  this.jj_ntk = -1; this.jj_gen++; return this.token; }
/*      */   public final Token getToken(int index) { Token t = this.token; for (int i = 0; i < index; i++) { if (t.next != null) { t = t.next; } else { t = t.next = this.token_source.getNextToken(); }
/*      */        }
/*      */      return t; }
/*      */   private int jj_ntk() { if ((this.jj_nt = this.token.next) == null)
/* 3303 */       return this.jj_ntk = (this.token.next = this.token_source.getNextToken()).kind;  return this.jj_ntk = this.jj_nt.kind; } public ParseException generateParseException() { this.jj_expentries.clear();
/* 3304 */     boolean[] la1tokens = new boolean[61];
/* 3305 */     if (this.jj_kind >= 0) {
/* 3306 */       la1tokens[this.jj_kind] = true;
/* 3307 */       this.jj_kind = -1;
/*      */     }  int i;
/* 3309 */     for (i = 0; i < 71; i++) {
/* 3310 */       if (this.jj_la1[i] == this.jj_gen) {
/* 3311 */         for (int k = 0; k < 32; k++) {
/* 3312 */           if ((jj_la1_0[i] & 1 << k) != 0) {
/* 3313 */             la1tokens[k] = true;
/*      */           }
/* 3315 */           if ((jj_la1_1[i] & 1 << k) != 0) {
/* 3316 */             la1tokens[32 + k] = true;
/*      */           }
/*      */         } 
/*      */       }
/*      */     } 
/* 3321 */     for (i = 0; i < 61; i++) {
/* 3322 */       if (la1tokens[i]) {
/* 3323 */         this.jj_expentry = new int[1];
/* 3324 */         this.jj_expentry[0] = i;
/* 3325 */         this.jj_expentries.add(this.jj_expentry);
/*      */       } 
/*      */     } 
/* 3328 */     this.jj_endpos = 0;
/* 3329 */     jj_rescan_token();
/* 3330 */     jj_add_error_token(0, 0);
/* 3331 */     int[][] exptokseq = new int[this.jj_expentries.size()][];
/* 3332 */     for (int j = 0; j < this.jj_expentries.size(); j++) {
/* 3333 */       exptokseq[j] = this.jj_expentries.get(j);
/*      */     }
/* 3335 */     return new ParseException(this.token, exptokseq, tokenImage); }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final void enable_tracing() {}
/*      */ 
/*      */   
/*      */   public final void disable_tracing() {}
/*      */ 
/*      */   
/*      */   private void jj_rescan_token() {
/* 3347 */     this.jj_rescan = true;
/* 3348 */     for (int i = 0; i < 8; i++) {
/*      */       try {
/* 3350 */         JJCalls p = this.jj_2_rtns[i];
/*      */         do {
/* 3352 */           if (p.gen > this.jj_gen) {
/* 3353 */             this.jj_la = p.arg; this.jj_lastpos = this.jj_scanpos = p.first;
/* 3354 */             switch (i) { case 0:
/* 3355 */                 jj_3_1(); break;
/* 3356 */               case 1: jj_3_2(); break;
/* 3357 */               case 2: jj_3_3(); break;
/* 3358 */               case 3: jj_3_4(); break;
/* 3359 */               case 4: jj_3_5(); break;
/* 3360 */               case 5: jj_3_6(); break;
/* 3361 */               case 6: jj_3_7(); break;
/* 3362 */               case 7: jj_3_8(); break; }
/*      */           
/*      */           } 
/* 3365 */           p = p.next;
/* 3366 */         } while (p != null);
/* 3367 */       } catch (LookaheadSuccess lookaheadSuccess) {}
/*      */     } 
/* 3369 */     this.jj_rescan = false;
/*      */   }
/*      */   
/*      */   private void jj_save(int index, int xla) {
/* 3373 */     JJCalls p = this.jj_2_rtns[index];
/* 3374 */     while (p.gen > this.jj_gen) {
/* 3375 */       if (p.next == null) { p = p.next = new JJCalls(); break; }
/* 3376 */        p = p.next;
/*      */     } 
/* 3378 */     p.gen = this.jj_gen + xla - this.jj_la; p.first = this.token; p.arg = xla;
/*      */   }
/*      */   
/*      */   static final class JJCalls {
/*      */     int gen;
/*      */     Token first;
/*      */     int arg;
/*      */     JJCalls next;
/*      */   }
/*      */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\xml\internal\rngom\parse\compact\CompactSyntax.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */