/*      */ package com.sun.tools.javadoc;
/*      */
/*      */ import java.util.Arrays;
/*      */ import java.util.HashMap;
/*      */ import java.util.HashSet;
/*      */ import java.util.Locale;
/*      */ import java.util.Map;
/*      */ import java.util.Set;
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */ @Deprecated
/*      */ public class JavaScriptScanner
/*      */ {
/*      */   private Reporter reporter;
/*      */   protected char[] buf;
/*      */   protected int bp;
/*      */   protected int buflen;
/*      */   protected char ch;
/*      */
/*      */   static class ParseException
/*      */     extends Exception
/*      */   {
/*      */     private static final long serialVersionUID = 0L;
/*      */
/*      */     ParseException(String param1String) {
/*   51 */       super(param1String);
/*      */     }
/*      */   }
/*      */
/*      */
/*      */
/*      */   private boolean newline = true;
/*      */
/*      */
/*      */
/*      */   Map<String, TagParser> tagParsers;
/*      */
/*      */
/*      */
/*      */   Set<String> eventAttrs;
/*      */
/*      */
/*      */
/*      */   Set<String> uriAttrs;
/*      */
/*      */
/*      */
/*      */
/*      */   public JavaScriptScanner() {
/*   75 */     initTagParsers();
/*   76 */     initEventAttrs();
/*   77 */     initURIAttrs();
/*      */   }
/*      */
/*      */   public void parse(String paramString, Reporter paramReporter) {
/*   81 */     this.reporter = paramReporter;
/*   82 */     String str = paramString;
/*   83 */     this.buf = new char[str.length() + 1];
/*   84 */     str.getChars(0, str.length(), this.buf, 0);
/*   85 */     this.buf[this.buf.length - 1] = '\032';
/*   86 */     this.buflen = this.buf.length - 1;
/*   87 */     this.bp = -1;
/*   88 */     this.newline = true;
/*   89 */     nextChar();
/*      */
/*   91 */     blockContent();
/*   92 */     blockTags();
/*      */   }
/*      */
/*      */   private void checkHtmlTag(String paramString) {
/*   96 */     if (paramString.equalsIgnoreCase("script")) {
/*   97 */       this.reporter.report();
/*      */     }
/*      */   }
/*      */
/*      */   private void checkHtmlAttr(String paramString1, String paramString2) {
/*  102 */     String str = paramString1.toLowerCase(Locale.ENGLISH);
/*  103 */     if (this.eventAttrs.contains(str) || (this.uriAttrs
/*  104 */       .contains(str) && paramString2 != null && paramString2
/*  105 */       .toLowerCase(Locale.ENGLISH).trim().startsWith("javascript:"))) {
/*  106 */       this.reporter.report();
/*      */     }
/*      */   }
/*      */
/*      */   void nextChar() {
/*  111 */     this.ch = this.buf[(this.bp < this.buflen) ? ++this.bp : this.buflen];
/*  112 */     switch (this.ch) { case '\n': case '\f':
/*      */       case '\r':
/*  114 */         this.newline = true;
/*      */         break; }
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
/*      */   protected void blockContent() {
/*  127 */     while (this.bp < this.buflen) {
/*  128 */       switch (this.ch) { case '\n': case '\f':
/*      */         case '\r':
/*  130 */           this.newline = true;
/*      */
/*      */         case '\t':
/*      */         case ' ':
/*  134 */           nextChar();
/*      */           continue;
/*      */
/*      */         case '&':
/*  138 */           entity(null);
/*      */           continue;
/*      */
/*      */         case '<':
/*  142 */           html();
/*      */           continue;
/*      */
/*      */         case '>':
/*  146 */           this.newline = false;
/*  147 */           nextChar();
/*      */           continue;
/*      */
/*      */         case '{':
/*  151 */           inlineTag(null);
/*      */           continue;
/*      */
/*      */         case '@':
/*  155 */           if (this.newline) {
/*      */             break;
/*      */           }
/*      */           break; }
/*      */
/*      */
/*  161 */       this.newline = false;
/*  162 */       nextChar();
/*      */     }
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   protected void blockTags() {
/*  173 */     while (this.ch == '@') {
/*  174 */       blockTag();
/*      */     }
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   protected void blockTag() {
/*  183 */     int i = this.bp;
/*      */     try {
/*  185 */       nextChar();
/*  186 */       if (isIdentifierStart(this.ch)) {
/*  187 */         String str = readTagName();
/*  188 */         TagParser tagParser = this.tagParsers.get(str);
/*  189 */         if (tagParser == null) {
/*  190 */           blockContent();
/*      */         } else {
/*  192 */           switch (tagParser.getKind()) {
/*      */             case REMOVE_ALL:
/*  194 */               tagParser.parse(i);
/*      */               return;
/*      */             case REMOVE_FIRST_SPACE:
/*      */               return;
/*      */           }
/*      */         }
/*      */       }
/*  201 */       blockContent();
/*  202 */     } catch (ParseException parseException) {
/*  203 */       blockContent();
/*      */     }
/*      */   }
/*      */
/*      */   protected void inlineTag(Void paramVoid) {
/*  208 */     this.newline = false;
/*  209 */     nextChar();
/*  210 */     if (this.ch == '@') {
/*  211 */       inlineTag();
/*      */     }
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   protected void inlineTag() {
/*  222 */     int i = this.bp - 1;
/*      */     try {
/*  224 */       nextChar();
/*  225 */       if (isIdentifierStart(this.ch)) {
/*  226 */         String str = readTagName();
/*  227 */         TagParser tagParser = this.tagParsers.get(str);
/*      */
/*  229 */         if (tagParser == null) {
/*  230 */           skipWhitespace();
/*  231 */           inlineText(WhitespaceRetentionPolicy.REMOVE_ALL);
/*  232 */           nextChar();
/*      */         } else {
/*  234 */           skipWhitespace();
/*  235 */           if (tagParser.getKind() == TagParser.Kind.INLINE) {
/*  236 */             tagParser.parse(i);
/*      */           } else {
/*  238 */             inlineText(WhitespaceRetentionPolicy.REMOVE_ALL);
/*  239 */             nextChar();
/*      */           }
/*      */         }
/*      */       }
/*  243 */     } catch (ParseException parseException) {}
/*      */   }
/*      */
/*      */   private enum WhitespaceRetentionPolicy
/*      */   {
/*  248 */     RETAIN_ALL,
/*  249 */     REMOVE_FIRST_SPACE,
/*  250 */     REMOVE_ALL;
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   private void inlineText(WhitespaceRetentionPolicy paramWhitespaceRetentionPolicy) throws ParseException {
/*  259 */     switch (paramWhitespaceRetentionPolicy) {
/*      */       case REMOVE_ALL:
/*  261 */         skipWhitespace();
/*      */         break;
/*      */       case REMOVE_FIRST_SPACE:
/*  264 */         if (this.ch == ' ') {
/*  265 */           nextChar();
/*      */         }
/*      */         break;
/*      */     }
/*      */
/*      */
/*      */
/*      */
/*  273 */     int i = this.bp;
/*  274 */     byte b = 1;
/*      */
/*      */
/*  277 */     while (this.bp < this.buflen) {
/*  278 */       switch (this.ch) { case '\n': case '\f':
/*      */         case '\r':
/*  280 */           this.newline = true;
/*      */           break;
/*      */
/*      */         case '\t':
/*      */         case ' ':
/*      */           break;
/*      */         case '{':
/*  287 */           this.newline = false;
/*  288 */           b++;
/*      */           break;
/*      */
/*      */         case '}':
/*  292 */           if (--b == 0) {
/*      */             return;
/*      */           }
/*  295 */           this.newline = false;
/*      */           break;
/*      */
/*      */         case '@':
/*  299 */           if (this.newline)
/*      */             break;
/*  301 */           this.newline = false;
/*      */           break;
/*      */
/*      */         default:
/*  305 */           this.newline = false;
/*      */           break; }
/*      */
/*  308 */       nextChar();
/*      */     }
/*  310 */     throw new ParseException("dc.unterminated.inline.tag");
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
/*      */   protected void reference(boolean paramBoolean) throws ParseException {
/*  323 */     int i = this.bp;
/*  324 */     byte b = 0;
/*      */
/*      */
/*      */
/*      */
/*  329 */     while (this.bp < this.buflen) {
/*  330 */       switch (this.ch) { case '\n': case '\f':
/*      */         case '\r':
/*  332 */           this.newline = true;
/*      */
/*      */         case '\t':
/*      */         case ' ':
/*  336 */           if (!b) {
/*      */             break;
/*      */           }
/*      */           break;
/*      */         case '(':
/*      */         case '<':
/*  342 */           this.newline = false;
/*  343 */           b++;
/*      */           break;
/*      */
/*      */         case ')':
/*      */         case '>':
/*  348 */           this.newline = false;
/*  349 */           b--;
/*      */           break;
/*      */
/*      */         case '}':
/*  353 */           if (this.bp == i)
/*      */             return;
/*  355 */           this.newline = false;
/*      */           break;
/*      */
/*      */         case '@':
/*  359 */           if (this.newline) {
/*      */             break;
/*      */           }
/*      */
/*      */         default:
/*  364 */           this.newline = false;
/*      */           break; }
/*      */
/*  367 */       nextChar();
/*      */     }
/*      */
/*  370 */     if (b != 0) {
/*  371 */       throw new ParseException("dc.unterminated.signature");
/*      */     }
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   protected void identifier() throws ParseException {
/*  381 */     skipWhitespace();
/*  382 */     int i = this.bp;
/*      */
/*  384 */     if (isJavaIdentifierStart(this.ch)) {
/*  385 */       readJavaIdentifier();
/*      */
/*      */       return;
/*      */     }
/*  389 */     throw new ParseException("dc.identifier.expected");
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   protected void quotedString() {
/*  398 */     int i = this.bp;
/*  399 */     nextChar();
/*      */
/*      */
/*  402 */     while (this.bp < this.buflen) {
/*  403 */       switch (this.ch) { case '\n': case '\f':
/*      */         case '\r':
/*  405 */           this.newline = true;
/*      */           break;
/*      */
/*      */
/*      */
/*      */
/*      */         case '"':
/*  412 */           nextChar();
/*      */           return;
/*      */
/*      */
/*      */         case '@':
/*  417 */           if (this.newline)
/*      */             break;
/*      */           break; }
/*      */
/*  421 */       nextChar();
/*      */     }
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   protected void inlineWord() {
/*  431 */     int i = this.bp;
/*  432 */     byte b = 0;
/*      */
/*  434 */     while (this.bp < this.buflen) {
/*  435 */       switch (this.ch) {
/*      */         case '\n':
/*  437 */           this.newline = true;
/*      */         case '\t':
/*      */         case '\f':
/*      */         case '\r':
/*      */         case ' ':
/*      */           return;
/*      */         case '@':
/*  444 */           if (this.newline) {
/*      */             break;
/*      */           }
/*      */         case '{':
/*  448 */           b++;
/*      */           break;
/*      */
/*      */         case '}':
/*  452 */           if (b == 0 || --b == 0)
/*      */             return;
/*      */           break;
/*      */       }
/*  456 */       this.newline = false;
/*  457 */       nextChar();
/*      */     }
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   private void inlineContent() {
/*  469 */     skipWhitespace();
/*  470 */     int i = this.bp;
/*  471 */     byte b = 1;
/*      */
/*      */
/*  474 */     while (this.bp < this.buflen) {
/*      */
/*  476 */       switch (this.ch) { case '\n': case '\f':
/*      */         case '\r':
/*  478 */           this.newline = true;
/*      */
/*      */         case '\t':
/*      */         case ' ':
/*  482 */           nextChar();
/*      */           continue;
/*      */
/*      */         case '&':
/*  486 */           entity(null);
/*      */           continue;
/*      */
/*      */         case '<':
/*  490 */           this.newline = false;
/*  491 */           html();
/*      */           continue;
/*      */
/*      */         case '{':
/*  495 */           this.newline = false;
/*  496 */           b++;
/*  497 */           nextChar();
/*      */           continue;
/*      */
/*      */         case '}':
/*  501 */           this.newline = false;
/*  502 */           if (--b == 0) {
/*  503 */             nextChar();
/*      */             return;
/*      */           }
/*  506 */           nextChar();
/*      */           continue;
/*      */
/*      */         case '@':
/*  510 */           if (this.newline) {
/*      */             break;
/*      */           }
/*      */           break; }
/*      */
/*  515 */       nextChar();
/*      */     }
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */   protected void entity(Void paramVoid) {
/*  523 */     this.newline = false;
/*  524 */     entity();
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   protected void entity() {
/*  532 */     nextChar();
/*  533 */     String str = null;
/*  534 */     if (this.ch == '#') {
/*  535 */       int i = this.bp;
/*  536 */       nextChar();
/*  537 */       if (isDecimalDigit(this.ch)) {
/*  538 */         nextChar();
/*  539 */         while (isDecimalDigit(this.ch))
/*  540 */           nextChar();
/*  541 */         str = new String(this.buf, i, this.bp - i);
/*  542 */       } else if (this.ch == 'x' || this.ch == 'X') {
/*  543 */         nextChar();
/*  544 */         if (isHexDigit(this.ch)) {
/*  545 */           nextChar();
/*  546 */           while (isHexDigit(this.ch))
/*  547 */             nextChar();
/*  548 */           str = new String(this.buf, i, this.bp - i);
/*      */         }
/*      */       }
/*  551 */     } else if (isIdentifierStart(this.ch)) {
/*  552 */       str = readIdentifier();
/*      */     }
/*      */
/*  555 */     if (str != null) {
/*  556 */       if (this.ch != ';')
/*      */         return;
/*  558 */       nextChar();
/*      */     }
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   protected void html() {
/*  567 */     int i = this.bp;
/*  568 */     nextChar();
/*  569 */     if (isIdentifierStart(this.ch)) {
/*  570 */       String str = readIdentifier();
/*  571 */       checkHtmlTag(str);
/*  572 */       htmlAttrs();
/*  573 */       if (this.ch == '/') {
/*  574 */         nextChar();
/*      */       }
/*  576 */       if (this.ch == '>') {
/*  577 */         nextChar();
/*      */         return;
/*      */       }
/*  580 */     } else if (this.ch == '/') {
/*  581 */       nextChar();
/*  582 */       if (isIdentifierStart(this.ch)) {
/*  583 */         readIdentifier();
/*  584 */         skipWhitespace();
/*  585 */         if (this.ch == '>') {
/*  586 */           nextChar();
/*      */           return;
/*      */         }
/*      */       }
/*  590 */     } else if (this.ch == '!') {
/*  591 */       nextChar();
/*  592 */       if (this.ch == '-') {
/*  593 */         nextChar();
/*  594 */         if (this.ch == '-') {
/*  595 */           nextChar();
/*  596 */           while (this.bp < this.buflen) {
/*  597 */             byte b = 0;
/*  598 */             while (this.ch == '-') {
/*  599 */               b++;
/*  600 */               nextChar();
/*      */             }
/*      */
/*      */
/*      */
/*      */
/*  606 */             if (b >= 2 && this.ch == '>') {
/*  607 */               nextChar();
/*      */
/*      */               return;
/*      */             }
/*  611 */             nextChar();
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */
/*  617 */     this.bp = i + 1;
/*  618 */     this.ch = this.buf[this.bp];
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   protected void htmlAttrs() {
/*  627 */     skipWhitespace();
/*      */
/*      */
/*  630 */     label29: while (isIdentifierStart(this.ch)) {
/*  631 */       int i = this.bp;
/*  632 */       String str = readAttributeName();
/*  633 */       skipWhitespace();
/*  634 */       StringBuilder stringBuilder = new StringBuilder();
/*  635 */       if (this.ch == '=') {
/*  636 */         nextChar();
/*  637 */         skipWhitespace();
/*  638 */         if (this.ch == '\'' || this.ch == '"') {
/*  639 */           char c = this.ch;
/*  640 */           nextChar();
/*  641 */           while (this.bp < this.buflen && this.ch != c) {
/*  642 */             if (this.newline && this.ch == '@') {
/*      */               break label29;
/*      */             }
/*      */
/*      */
/*      */
/*      */
/*  649 */             stringBuilder.append(this.ch);
/*  650 */             nextChar();
/*      */           }
/*  652 */           nextChar();
/*      */         } else {
/*  654 */           while (this.bp < this.buflen && !isUnquotedAttrValueTerminator(this.ch)) {
/*  655 */             stringBuilder.append(this.ch);
/*  656 */             nextChar();
/*      */           }
/*      */         }
/*  659 */         skipWhitespace();
/*      */       }
/*  661 */       checkHtmlAttr(str, stringBuilder.toString());
/*      */     }
/*      */   }
/*      */
/*      */   protected void attrValueChar(Void paramVoid) {
/*  666 */     switch (this.ch) {
/*      */       case '&':
/*  668 */         entity(paramVoid);
/*      */         return;
/*      */
/*      */       case '{':
/*  672 */         inlineTag(paramVoid);
/*      */         return;
/*      */     }
/*      */
/*  676 */     nextChar();
/*      */   }
/*      */
/*      */
/*      */   protected boolean isIdentifierStart(char paramChar) {
/*  681 */     return Character.isUnicodeIdentifierStart(paramChar);
/*      */   }
/*      */
/*      */   protected String readIdentifier() {
/*  685 */     int i = this.bp;
/*  686 */     nextChar();
/*  687 */     while (this.bp < this.buflen && Character.isUnicodeIdentifierPart(this.ch))
/*  688 */       nextChar();
/*  689 */     return new String(this.buf, i, this.bp - i);
/*      */   }
/*      */
/*      */   protected String readAttributeName() {
/*  693 */     int i = this.bp;
/*  694 */     nextChar();
/*  695 */     while (this.bp < this.buflen && (Character.isUnicodeIdentifierPart(this.ch) || this.ch == '-'))
/*  696 */       nextChar();
/*  697 */     return new String(this.buf, i, this.bp - i);
/*      */   }
/*      */
/*      */   protected String readTagName() {
/*  701 */     int i = this.bp;
/*  702 */     nextChar();
/*  703 */     while (this.bp < this.buflen && (
/*  704 */       Character.isUnicodeIdentifierPart(this.ch) || this.ch == '.' || this.ch == '-' || this.ch == ':'))
/*      */     {
/*  706 */       nextChar();
/*      */     }
/*  708 */     return new String(this.buf, i, this.bp - i);
/*      */   }
/*      */
/*      */   protected boolean isJavaIdentifierStart(char paramChar) {
/*  712 */     return Character.isJavaIdentifierStart(paramChar);
/*      */   }
/*      */
/*      */   protected String readJavaIdentifier() {
/*  716 */     int i = this.bp;
/*  717 */     nextChar();
/*  718 */     while (this.bp < this.buflen && Character.isJavaIdentifierPart(this.ch))
/*  719 */       nextChar();
/*  720 */     return new String(this.buf, i, this.bp - i);
/*      */   }
/*      */
/*      */   protected boolean isDecimalDigit(char paramChar) {
/*  724 */     return ('0' <= paramChar && paramChar <= '9');
/*      */   }
/*      */
/*      */   protected boolean isHexDigit(char paramChar) {
/*  728 */     return (('0' <= paramChar && paramChar <= '9') || ('a' <= paramChar && paramChar <= 'f') || ('A' <= paramChar && paramChar <= 'F'));
/*      */   }
/*      */
/*      */
/*      */
/*      */   protected boolean isUnquotedAttrValueTerminator(char paramChar) {
/*  734 */     switch (paramChar) { case '\t': case '\n': case '\f': case '\r': case ' ': case '"': case '\'':
/*      */       case '<':
/*      */       case '=':
/*      */       case '>':
/*      */       case '`':
/*  739 */         return true; }
/*      */
/*  741 */     return false;
/*      */   }
/*      */
/*      */
/*      */   protected boolean isWhitespace(char paramChar) {
/*  746 */     return Character.isWhitespace(paramChar);
/*      */   }
/*      */
/*      */   protected void skipWhitespace() {
/*  750 */     while (isWhitespace(this.ch)) {
/*  751 */       nextChar();
/*      */     }
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   String newString(int paramInt1, int paramInt2) {
/*  760 */     return new String(this.buf, paramInt1, paramInt2 - paramInt1);
/*      */   }
/*      */   static abstract class TagParser { final Kind kind; final String name;
/*      */
/*  764 */     enum Kind { INLINE, BLOCK; }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */     TagParser(Kind param1Kind, String param1String) {
/*  771 */       this.kind = param1Kind;
/*  772 */       this.name = param1String;
/*      */     }
/*      */
/*      */     TagParser(Kind param1Kind, String param1String, boolean param1Boolean) {
/*  776 */       this(param1Kind, param1String);
/*      */     }
/*      */
/*      */     Kind getKind() {
/*  780 */       return this.kind;
/*      */     }
/*      */
/*      */     String getName() {
/*  784 */       return this.name;
/*      */     }
/*      */
/*      */     abstract void parse(int param1Int) throws ParseException; }
/*      */
/*      */
/*      */   enum Kind {
/*      */     INLINE, BLOCK;
/*      */   }
/*      */
/*      */   private void initTagParsers() {
/*  795 */     TagParser[] arrayOfTagParser = { new TagParser(TagParser.Kind.BLOCK, "author")
/*      */         {
/*      */
/*      */           public void parse(int param1Int)
/*      */           {
/*  800 */             JavaScriptScanner.this.blockContent();
/*      */           }
/*      */         }, new TagParser(TagParser.Kind.INLINE, "code", true)
/*      */         {
/*      */
/*      */
/*      */           public void parse(int param1Int) throws ParseException
/*      */           {
/*  808 */             JavaScriptScanner.this.inlineText(WhitespaceRetentionPolicy.REMOVE_FIRST_SPACE);
/*  809 */             JavaScriptScanner.this.nextChar();
/*      */           }
/*      */         }, new TagParser(TagParser.Kind.BLOCK, "deprecated")
/*      */         {
/*      */
/*      */
/*      */           public void parse(int param1Int)
/*      */           {
/*  817 */             JavaScriptScanner.this.blockContent();
/*      */           }
/*      */         }, new TagParser(TagParser.Kind.INLINE, "docRoot")
/*      */         {
/*      */
/*      */
/*      */           public void parse(int param1Int) throws ParseException
/*      */           {
/*  825 */             if (JavaScriptScanner.this.ch == '}') {
/*  826 */               JavaScriptScanner.this.nextChar();
/*      */               return;
/*      */             }
/*  829 */             JavaScriptScanner.this.inlineText(WhitespaceRetentionPolicy.REMOVE_ALL);
/*  830 */             JavaScriptScanner.this.nextChar();
/*  831 */             throw new ParseException("dc.unexpected.content");
/*      */           }
/*      */         }, new TagParser(TagParser.Kind.BLOCK, "exception")
/*      */         {
/*      */
/*      */
/*      */           public void parse(int param1Int) throws ParseException
/*      */           {
/*  839 */             JavaScriptScanner.this.skipWhitespace();
/*  840 */             JavaScriptScanner.this.reference(false);
/*  841 */             JavaScriptScanner.this.blockContent();
/*      */           }
/*      */         }, new TagParser(TagParser.Kind.BLOCK, "hidden")
/*      */         {
/*      */
/*      */
/*      */           public void parse(int param1Int)
/*      */           {
/*  849 */             JavaScriptScanner.this.blockContent();
/*      */           }
/*      */         }, new TagParser(TagParser.Kind.INLINE, "index")
/*      */         {
/*      */
/*      */
/*      */           public void parse(int param1Int) throws ParseException
/*      */           {
/*  857 */             JavaScriptScanner.this.skipWhitespace();
/*  858 */             if (JavaScriptScanner.this.ch == '}') {
/*  859 */               throw new ParseException("dc.no.content");
/*      */             }
/*  861 */             if (JavaScriptScanner.this.ch == '"') { JavaScriptScanner.this.quotedString(); } else { JavaScriptScanner.this.inlineWord(); }
/*  862 */              JavaScriptScanner.this.skipWhitespace();
/*  863 */             if (JavaScriptScanner.this.ch != '}') {
/*  864 */               JavaScriptScanner.this.inlineContent();
/*      */             } else {
/*  866 */               JavaScriptScanner.this.nextChar();
/*      */             }
/*      */           }
/*      */         }, new TagParser(TagParser.Kind.INLINE, "inheritDoc")
/*      */         {
/*      */
/*      */
/*      */           public void parse(int param1Int) throws ParseException
/*      */           {
/*  875 */             if (JavaScriptScanner.this.ch == '}') {
/*  876 */               JavaScriptScanner.this.nextChar();
/*      */               return;
/*      */             }
/*  879 */             JavaScriptScanner.this.inlineText(WhitespaceRetentionPolicy.REMOVE_ALL);
/*  880 */             JavaScriptScanner.this.nextChar();
/*  881 */             throw new ParseException("dc.unexpected.content");
/*      */           }
/*      */         }, new TagParser(TagParser.Kind.INLINE, "link")
/*      */         {
/*      */
/*      */
/*      */           public void parse(int param1Int) throws ParseException
/*      */           {
/*  889 */             JavaScriptScanner.this.reference(true);
/*  890 */             JavaScriptScanner.this.inlineContent();
/*      */           }
/*      */         }, new TagParser(TagParser.Kind.INLINE, "linkplain")
/*      */         {
/*      */
/*      */
/*      */           public void parse(int param1Int) throws ParseException
/*      */           {
/*  898 */             JavaScriptScanner.this.reference(true);
/*  899 */             JavaScriptScanner.this.inlineContent();
/*      */           }
/*      */         },
/*      */         new TagParser(TagParser.Kind.INLINE, "literal", true)
/*      */         {
/*      */
/*      */           public void parse(int param1Int) throws ParseException
/*      */           {
/*  907 */             JavaScriptScanner.this.inlineText(WhitespaceRetentionPolicy.REMOVE_FIRST_SPACE);
/*  908 */             JavaScriptScanner.this.nextChar();
/*      */           }
/*      */         }, new TagParser(TagParser.Kind.BLOCK, "param")
/*      */         {
/*      */
/*      */
/*      */           public void parse(int param1Int) throws ParseException
/*      */           {
/*  916 */             JavaScriptScanner.this.skipWhitespace();
/*      */
/*  918 */             boolean bool = false;
/*  919 */             if (JavaScriptScanner.this.ch == '<') {
/*  920 */               bool = true;
/*  921 */               JavaScriptScanner.this.nextChar();
/*      */             }
/*      */
/*  924 */             JavaScriptScanner.this.identifier();
/*      */
/*  926 */             if (bool) {
/*  927 */               if (JavaScriptScanner.this.ch != '>')
/*  928 */                 throw new ParseException("dc.gt.expected");
/*  929 */               JavaScriptScanner.this.nextChar();
/*      */             }
/*      */
/*  932 */             JavaScriptScanner.this.skipWhitespace();
/*  933 */             JavaScriptScanner.this.blockContent();
/*      */           }
/*      */         }, new TagParser(TagParser.Kind.BLOCK, "return")
/*      */         {
/*      */
/*      */
/*      */           public void parse(int param1Int)
/*      */           {
/*  941 */             JavaScriptScanner.this.blockContent();
/*      */           }
/*      */         }, new TagParser(TagParser.Kind.BLOCK, "see")
/*      */         {
/*      */
/*      */
/*      */           public void parse(int param1Int) throws ParseException
/*      */           {
/*  949 */             JavaScriptScanner.this.skipWhitespace();
/*  950 */             switch (JavaScriptScanner.this.ch)
/*      */             { case '"':
/*  952 */                 JavaScriptScanner.this.quotedString();
/*  953 */                 JavaScriptScanner.this.skipWhitespace();
/*  954 */                 if (JavaScriptScanner.this.ch == '@' || (JavaScriptScanner.this.ch == '\032' && JavaScriptScanner.this.bp == JavaScriptScanner.this.buf.length - 1)) {
/*      */                   return;
/*      */                 }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*  980 */                 throw new ParseException("dc.unexpected.content");case '<': JavaScriptScanner.this.blockContent(); return;case '@': if (JavaScriptScanner.this.newline) throw new ParseException("dc.no.content");  throw new ParseException("dc.unexpected.content");case '\032': if (JavaScriptScanner.this.bp == JavaScriptScanner.this.buf.length - 1) throw new ParseException("dc.no.content");  throw new ParseException("dc.unexpected.content"); }  if (JavaScriptScanner.this.isJavaIdentifierStart(JavaScriptScanner.this.ch) || JavaScriptScanner.this.ch == '#') { JavaScriptScanner.this.reference(true); JavaScriptScanner.this.blockContent(); }  throw new ParseException("dc.unexpected.content");
/*      */           }
/*      */         }, new TagParser(TagParser.Kind.BLOCK, "@serialData")
/*      */         {
/*      */
/*      */
/*      */           public void parse(int param1Int)
/*      */           {
/*  988 */             JavaScriptScanner.this.blockContent();
/*      */           }
/*      */         }, new TagParser(TagParser.Kind.BLOCK, "serialField")
/*      */         {
/*      */
/*      */
/*      */           public void parse(int param1Int) throws ParseException
/*      */           {
/*  996 */             JavaScriptScanner.this.skipWhitespace();
/*  997 */             JavaScriptScanner.this.identifier();
/*  998 */             JavaScriptScanner.this.skipWhitespace();
/*  999 */             JavaScriptScanner.this.reference(false);
/* 1000 */             if (JavaScriptScanner.this.isWhitespace(JavaScriptScanner.this.ch)) {
/* 1001 */               JavaScriptScanner.this.skipWhitespace();
/* 1002 */               JavaScriptScanner.this.blockContent();
/*      */             }
/*      */           }
/*      */         }, new TagParser(TagParser.Kind.BLOCK, "serial")
/*      */         {
/*      */
/*      */
/*      */           public void parse(int param1Int)
/*      */           {
/* 1011 */             JavaScriptScanner.this.blockContent();
/*      */           }
/*      */         }, new TagParser(TagParser.Kind.BLOCK, "since")
/*      */         {
/*      */
/*      */
/*      */           public void parse(int param1Int)
/*      */           {
/* 1019 */             JavaScriptScanner.this.blockContent();
/*      */           }
/*      */         }, new TagParser(TagParser.Kind.BLOCK, "throws")
/*      */         {
/*      */
/*      */
/*      */           public void parse(int param1Int) throws ParseException
/*      */           {
/* 1027 */             JavaScriptScanner.this.skipWhitespace();
/* 1028 */             JavaScriptScanner.this.reference(false);
/* 1029 */             JavaScriptScanner.this.blockContent();
/*      */           }
/*      */         }, new TagParser(TagParser.Kind.INLINE, "value")
/*      */         {
/*      */
/*      */
/*      */           public void parse(int param1Int) throws ParseException
/*      */           {
/* 1037 */             JavaScriptScanner.this.reference(true);
/* 1038 */             JavaScriptScanner.this.skipWhitespace();
/* 1039 */             if (JavaScriptScanner.this.ch == '}') {
/* 1040 */               JavaScriptScanner.this.nextChar();
/*      */               return;
/*      */             }
/* 1043 */             JavaScriptScanner.this.nextChar();
/* 1044 */             throw new ParseException("dc.unexpected.content");
/*      */           }
/*      */         },
/*      */         new TagParser(TagParser.Kind.BLOCK, "version")
/*      */         {
/*      */
/*      */           public void parse(int param1Int)
/*      */           {
/* 1052 */             JavaScriptScanner.this.blockContent();
/*      */           }
/*      */         } };
/*      */
/*      */
/* 1057 */     this.tagParsers = new HashMap<>();
/* 1058 */     for (TagParser tagParser : arrayOfTagParser) {
/* 1059 */       this.tagParsers.put(tagParser.getName(), tagParser);
/*      */     }
/*      */   }
/*      */
/*      */   private void initEventAttrs() {
/* 1064 */     this.eventAttrs = new HashSet<>(Arrays.asList(new String[] { "onabort", "onblur", "oncanplay", "oncanplaythrough", "onchange", "onclick", "oncontextmenu", "ondblclick", "ondrag", "ondragend", "ondragenter", "ondragleave", "ondragover", "ondragstart", "ondrop", "ondurationchange", "onemptied", "onended", "onerror", "onfocus", "oninput", "oninvalid", "onkeydown", "onkeypress", "onkeyup", "onload", "onloadeddata", "onloadedmetadata", "onloadstart", "onmousedown", "onmousemove", "onmouseout", "onmouseover", "onmouseup", "onmousewheel", "onpause", "onplay", "onplaying", "onprogress", "onratechange", "onreadystatechange", "onreset", "onscroll", "onseeked", "onseeking", "onselect", "onshow", "onstalled", "onsubmit", "onsuspend", "ontimeupdate", "onvolumechange", "onwaiting", "onunload" }));
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
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   private void initURIAttrs() {
/* 1092 */     this.uriAttrs = new HashSet<>(Arrays.asList(new String[] { "action", "cite", "classid", "codebase", "data", "datasrc", "for", "href", "longdesc", "profile", "src", "usemap" }));
/*      */   }
/*      */
/*      */   public static interface Reporter {
/*      */     void report();
/*      */   }
/*      */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\javadoc\JavaScriptScanner.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
