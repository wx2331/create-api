/*      */ package com.sun.tools.javac.parser;
/*      */
/*      */ import com.sun.source.doctree.AttributeTree;
/*      */ import com.sun.source.doctree.DocTree;
/*      */ import com.sun.tools.javac.tree.DCTree;
/*      */ import com.sun.tools.javac.tree.DocTreeMaker;
/*      */ import com.sun.tools.javac.tree.JCTree;
/*      */ import com.sun.tools.javac.util.DiagnosticSource;
/*      */ import com.sun.tools.javac.util.List;
/*      */ import com.sun.tools.javac.util.ListBuffer;
/*      */ import com.sun.tools.javac.util.Log;
/*      */ import com.sun.tools.javac.util.Name;
/*      */ import com.sun.tools.javac.util.Names;
/*      */ import com.sun.tools.javac.util.Options;
/*      */ import com.sun.tools.javac.util.StringUtils;
/*      */ import java.text.BreakIterator;
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
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */ public class DocCommentParser
/*      */ {
/*      */   final ParserFactory fac;
/*      */   final DiagnosticSource diagSource;
/*      */   final Tokens.Comment comment;
/*      */   final DocTreeMaker m;
/*      */   final Names names;
/*      */   BreakIterator sentenceBreaker;
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
/*   74 */       super(param1String);
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
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*   97 */   int textStart = -1;
/*   98 */   int lastNonWhite = -1;
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   boolean newline = true;
/*      */
/*      */
/*      */
/*      */
/*      */   Map<Name, TagParser> tagParsers;
/*      */
/*      */
/*      */
/*      */
/*      */   Set<String> htmlBlockTags;
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   DCTree.DCDocComment parse() {
/*  121 */     String str = this.comment.getText();
/*  122 */     this.buf = new char[str.length() + 1];
/*  123 */     str.getChars(0, str.length(), this.buf, 0);
/*  124 */     this.buf[this.buf.length - 1] = '\032';
/*  125 */     this.buflen = this.buf.length - 1;
/*  126 */     this.bp = -1;
/*  127 */     nextChar();
/*      */
/*  129 */     List<DCTree> list1 = blockContent();
/*  130 */     List<DCTree> list2 = blockTags();
/*      */
/*      */
/*  133 */     ListBuffer listBuffer = new ListBuffer();
/*      */
/*  135 */     for (; list1.nonEmpty(); list1 = list1.tail) {
/*  136 */       String str1; int i; DCTree dCTree1 = (DCTree)list1.head;
/*  137 */       switch (dCTree1.getKind()) {
/*      */         case BLOCK:
/*  139 */           str1 = ((DCTree.DCText)dCTree1).getBody();
/*  140 */           i = getSentenceBreak(str1);
/*  141 */           if (i > 0) {
/*  142 */             int j = i;
/*  143 */             while (j > 0 && isWhitespace(str1.charAt(j - 1)))
/*  144 */               j--;
/*  145 */             listBuffer.add(this.m.at(dCTree1.pos).Text(str1.substring(0, j)));
/*  146 */             int k = i;
/*  147 */             while (k < str1.length() && isWhitespace(str1.charAt(k)))
/*  148 */               k++;
/*  149 */             list1 = list1.tail;
/*  150 */             if (k < str1.length())
/*  151 */               list1 = list1.prepend(this.m.at(dCTree1.pos + k).Text(str1.substring(k)));  break;
/*      */           }
/*  153 */           if (list1.tail.nonEmpty() &&
/*  154 */             isSentenceBreak((DCTree)list1.tail.head)) {
/*  155 */             int j = str1.length() - 1;
/*  156 */             while (j > 0 && isWhitespace(str1.charAt(j)))
/*  157 */               j--;
/*  158 */             listBuffer.add(this.m.at(dCTree1.pos).Text(str1.substring(0, j + 1)));
/*  159 */             list1 = list1.tail;
/*      */             break;
/*      */           }
/*      */           break;
/*      */
/*      */
/*      */         case INLINE:
/*      */         case null:
/*  167 */           if (isSentenceBreak(dCTree1))
/*      */             break;
/*      */           break;
/*      */       }
/*  171 */       listBuffer.add(dCTree1);
/*      */     }
/*      */
/*      */
/*  175 */     DCTree dCTree = getFirst((List<DCTree>[])new List[] { listBuffer.toList(), list1, list2 });
/*  176 */     boolean bool = (dCTree == null) ? true : dCTree.pos;
/*      */
/*  178 */     return this.m.at(bool).DocComment(this.comment, listBuffer.toList(), list1, list2);
/*      */   }
/*      */
/*      */
/*      */   void nextChar() {
/*  183 */     this.ch = this.buf[(this.bp < this.buflen) ? ++this.bp : this.buflen];
/*  184 */     switch (this.ch) { case '\n': case '\f':
/*      */       case '\r':
/*  186 */         this.newline = true;
/*      */         break; }
/*      */
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   protected List<DCTree> blockContent() {
/*  197 */     ListBuffer<DCTree> listBuffer = new ListBuffer();
/*  198 */     this.textStart = -1;
/*      */
/*      */
/*  201 */     while (this.bp < this.buflen) {
/*  202 */       switch (this.ch) { case '\n': case '\f':
/*      */         case '\r':
/*  204 */           this.newline = true;
/*      */
/*      */         case '\t':
/*      */         case ' ':
/*  208 */           nextChar();
/*      */           continue;
/*      */
/*      */         case '&':
/*  212 */           entity(listBuffer);
/*      */           continue;
/*      */
/*      */         case '<':
/*  216 */           this.newline = false;
/*  217 */           addPendingText(listBuffer, this.bp - 1);
/*  218 */           listBuffer.add(html());
/*  219 */           if (this.textStart == -1) {
/*  220 */             this.textStart = this.bp;
/*  221 */             this.lastNonWhite = -1;
/*      */           }
/*      */           continue;
/*      */
/*      */         case '>':
/*  226 */           this.newline = false;
/*  227 */           addPendingText(listBuffer, this.bp - 1);
/*  228 */           listBuffer.add(this.m.at(this.bp).Erroneous(newString(this.bp, this.bp + 1), this.diagSource, "dc.bad.gt", new Object[0]));
/*  229 */           nextChar();
/*  230 */           if (this.textStart == -1) {
/*  231 */             this.textStart = this.bp;
/*  232 */             this.lastNonWhite = -1;
/*      */           }
/*      */           continue;
/*      */
/*      */         case '{':
/*  237 */           inlineTag(listBuffer);
/*      */           continue;
/*      */
/*      */         case '@':
/*  241 */           if (this.newline) {
/*  242 */             addPendingText(listBuffer, this.lastNonWhite);
/*      */             break;
/*      */           }
/*      */           break; }
/*      */
/*      */
/*  248 */       this.newline = false;
/*  249 */       if (this.textStart == -1)
/*  250 */         this.textStart = this.bp;
/*  251 */       this.lastNonWhite = this.bp;
/*  252 */       nextChar();
/*      */     }
/*      */
/*      */
/*  256 */     if (this.lastNonWhite != -1) {
/*  257 */       addPendingText(listBuffer, this.lastNonWhite);
/*      */     }
/*  259 */     return listBuffer.toList();
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   protected List<DCTree> blockTags() {
/*  268 */     ListBuffer listBuffer = new ListBuffer();
/*  269 */     while (this.ch == '@')
/*  270 */       listBuffer.add(blockTag());
/*  271 */     return listBuffer.toList();
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   protected DCTree blockTag() {
/*  280 */     int i = this.bp;
/*      */     try {
/*  282 */       nextChar();
/*  283 */       if (isIdentifierStart(this.ch)) {
/*  284 */         Name name = readTagName();
/*  285 */         TagParser tagParser = this.tagParsers.get(name);
/*  286 */         if (tagParser == null) {
/*  287 */           List<DCTree> list = blockContent();
/*  288 */           return (DCTree)this.m.at(i).UnknownBlockTag(name, list);
/*      */         }
/*  290 */         switch (tagParser.getKind()) {
/*      */           case BLOCK:
/*  292 */             return tagParser.parse(i);
/*      */           case INLINE:
/*  294 */             return (DCTree)erroneous("dc.bad.inline.tag", i);
/*      */         }
/*      */
/*      */       }
/*  298 */       blockContent();
/*      */
/*  300 */       return (DCTree)erroneous("dc.no.tag.name", i);
/*  301 */     } catch (ParseException parseException) {
/*  302 */       blockContent();
/*  303 */       return (DCTree)erroneous(parseException.getMessage(), i);
/*      */     }
/*      */   }
/*      */
/*      */   protected void inlineTag(ListBuffer<DCTree> paramListBuffer) {
/*  308 */     this.newline = false;
/*  309 */     nextChar();
/*  310 */     if (this.ch == '@') {
/*  311 */       addPendingText(paramListBuffer, this.bp - 2);
/*  312 */       paramListBuffer.add(inlineTag());
/*  313 */       this.textStart = this.bp;
/*  314 */       this.lastNonWhite = -1;
/*      */     } else {
/*  316 */       if (this.textStart == -1)
/*  317 */         this.textStart = this.bp - 1;
/*  318 */       this.lastNonWhite = this.bp;
/*      */     }
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   protected DCTree inlineTag() {
/*  329 */     int i = this.bp - 1;
/*      */     try {
/*  331 */       nextChar();
/*  332 */       if (isIdentifierStart(this.ch)) {
/*  333 */         Name name = readTagName();
/*  334 */         skipWhitespace();
/*      */
/*  336 */         TagParser tagParser = this.tagParsers.get(name);
/*  337 */         if (tagParser == null) {
/*  338 */           DCTree dCTree = inlineText();
/*  339 */           if (dCTree != null) {
/*  340 */             nextChar();
/*  341 */             return (DCTree)this.m.at(i).UnknownInlineTag(name, List.of(dCTree)).setEndPos(this.bp);
/*      */           }
/*  343 */         } else if (tagParser.getKind() == TagParser.Kind.INLINE) {
/*  344 */           DCTree.DCEndPosTree dCEndPosTree = (DCTree.DCEndPosTree)tagParser.parse(i);
/*  345 */           if (dCEndPosTree != null) {
/*  346 */             return (DCTree)dCEndPosTree.setEndPos(this.bp);
/*      */           }
/*      */         } else {
/*  349 */           inlineText();
/*  350 */           nextChar();
/*      */         }
/*      */       }
/*  353 */       return (DCTree)erroneous("dc.no.tag.name", i);
/*  354 */     } catch (ParseException parseException) {
/*  355 */       return (DCTree)erroneous(parseException.getMessage(), i);
/*      */     }
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   protected DCTree inlineText() throws ParseException {
/*  365 */     skipWhitespace();
/*  366 */     int i = this.bp;
/*  367 */     byte b = 1;
/*      */
/*      */
/*  370 */     while (this.bp < this.buflen) {
/*  371 */       switch (this.ch) { case '\n': case '\f':
/*      */         case '\r':
/*  373 */           this.newline = true;
/*      */           break;
/*      */
/*      */         case '\t':
/*      */         case ' ':
/*      */           break;
/*      */         case '{':
/*  380 */           this.newline = false;
/*  381 */           this.lastNonWhite = this.bp;
/*  382 */           b++;
/*      */           break;
/*      */
/*      */         case '}':
/*  386 */           if (--b == 0) {
/*  387 */             return (DCTree)this.m.at(i).Text(newString(i, this.bp));
/*      */           }
/*  389 */           this.newline = false;
/*  390 */           this.lastNonWhite = this.bp;
/*      */           break;
/*      */
/*      */         case '@':
/*  394 */           if (this.newline)
/*      */             break;
/*  396 */           this.newline = false;
/*  397 */           this.lastNonWhite = this.bp;
/*      */           break;
/*      */
/*      */         default:
/*  401 */           this.newline = false;
/*  402 */           this.lastNonWhite = this.bp;
/*      */           break; }
/*      */
/*  405 */       nextChar();
/*      */     }
/*  407 */     throw new ParseException("dc.unterminated.inline.tag");
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   protected DCTree.DCReference reference(boolean paramBoolean) throws ParseException {
/*      */     JCTree jCTree;
/*      */     Name name;
/*      */     List<JCTree> list;
/*  419 */     int i = this.bp;
/*  420 */     byte b = 0;
/*      */
/*      */
/*      */
/*      */
/*  425 */     while (this.bp < this.buflen) {
/*  426 */       switch (this.ch) { case '\n': case '\f':
/*      */         case '\r':
/*  428 */           this.newline = true;
/*      */
/*      */         case '\t':
/*      */         case ' ':
/*  432 */           if (!b) {
/*      */             break;
/*      */           }
/*      */           break;
/*      */         case '(':
/*      */         case '<':
/*  438 */           this.newline = false;
/*  439 */           b++;
/*      */           break;
/*      */
/*      */         case ')':
/*      */         case '>':
/*  444 */           this.newline = false;
/*  445 */           b--;
/*      */           break;
/*      */
/*      */         case '}':
/*  449 */           if (this.bp == i)
/*  450 */             return null;
/*  451 */           this.newline = false;
/*      */           break;
/*      */
/*      */         case '@':
/*  455 */           if (this.newline) {
/*      */             break;
/*      */           }
/*      */
/*      */         default:
/*  460 */           this.newline = false;
/*      */           break; }
/*      */
/*  463 */       nextChar();
/*      */     }
/*      */
/*  466 */     if (b != 0) {
/*  467 */       throw new ParseException("dc.unterminated.signature");
/*      */     }
/*  469 */     String str = newString(i, this.bp);
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*  476 */     Log.DeferredDiagnosticHandler deferredDiagnosticHandler = new Log.DeferredDiagnosticHandler(this.fac.log);
/*      */
/*      */
/*      */     try {
/*  480 */       int j = str.indexOf("#");
/*  481 */       int k = str.indexOf("(", j + 1);
/*  482 */       if (j == -1) {
/*  483 */         if (k == -1) {
/*  484 */           jCTree = parseType(str);
/*  485 */           name = null;
/*      */         } else {
/*  487 */           jCTree = null;
/*  488 */           name = parseMember(str.substring(0, k));
/*      */         }
/*      */       } else {
/*  491 */         jCTree = (j == 0) ? null : parseType(str.substring(0, j));
/*  492 */         if (k == -1) {
/*  493 */           name = parseMember(str.substring(j + 1));
/*      */         } else {
/*  495 */           name = parseMember(str.substring(j + 1, k));
/*      */         }
/*      */       }
/*  498 */       if (k < 0) {
/*  499 */         list = null;
/*      */       } else {
/*  501 */         int m = str.indexOf(")", k);
/*  502 */         if (m != str.length() - 1)
/*  503 */           throw new ParseException("dc.ref.bad.parens");
/*  504 */         list = parseParams(str.substring(k + 1, m));
/*      */       }
/*      */
/*  507 */       if (!deferredDiagnosticHandler.getDiagnostics().isEmpty()) {
/*  508 */         throw new ParseException("dc.ref.syntax.error");
/*      */       }
/*      */     } finally {
/*  511 */       this.fac.log.popDiagnosticHandler((Log.DiagnosticHandler)deferredDiagnosticHandler);
/*      */     }
/*      */
/*  514 */     return (DCTree.DCReference)this.m.at(i).Reference(str, jCTree, name, list).setEndPos(this.bp);
/*      */   }
/*      */
/*      */   JCTree parseType(String paramString) throws ParseException {
/*  518 */     JavacParser javacParser = this.fac.newParser(paramString, false, false, false);
/*  519 */     JCTree.JCExpression jCExpression = javacParser.parseType();
/*  520 */     if ((javacParser.token()).kind != Tokens.TokenKind.EOF)
/*  521 */       throw new ParseException("dc.ref.unexpected.input");
/*  522 */     return (JCTree)jCExpression;
/*      */   }
/*      */
/*      */   Name parseMember(String paramString) throws ParseException {
/*  526 */     JavacParser javacParser = this.fac.newParser(paramString, false, false, false);
/*  527 */     Name name = javacParser.ident();
/*  528 */     if ((javacParser.token()).kind != Tokens.TokenKind.EOF)
/*  529 */       throw new ParseException("dc.ref.unexpected.input");
/*  530 */     return name;
/*      */   }
/*      */
/*      */   List<JCTree> parseParams(String paramString) throws ParseException {
/*  534 */     if (paramString.trim().isEmpty()) {
/*  535 */       return List.nil();
/*      */     }
/*  537 */     JavacParser javacParser = this.fac.newParser(paramString.replace("...", "[]"), false, false, false);
/*  538 */     ListBuffer listBuffer = new ListBuffer();
/*  539 */     listBuffer.add(javacParser.parseType());
/*      */
/*  541 */     if ((javacParser.token()).kind == Tokens.TokenKind.IDENTIFIER) {
/*  542 */       javacParser.nextToken();
/*      */     }
/*  544 */     while ((javacParser.token()).kind == Tokens.TokenKind.COMMA) {
/*  545 */       javacParser.nextToken();
/*  546 */       listBuffer.add(javacParser.parseType());
/*      */
/*  548 */       if ((javacParser.token()).kind == Tokens.TokenKind.IDENTIFIER) {
/*  549 */         javacParser.nextToken();
/*      */       }
/*      */     }
/*  552 */     if ((javacParser.token()).kind != Tokens.TokenKind.EOF) {
/*  553 */       throw new ParseException("dc.ref.unexpected.input");
/*      */     }
/*  555 */     return listBuffer.toList();
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   protected DCTree.DCIdentifier identifier() throws ParseException {
/*  565 */     skipWhitespace();
/*  566 */     int i = this.bp;
/*      */
/*  568 */     if (isJavaIdentifierStart(this.ch)) {
/*  569 */       Name name = readJavaIdentifier();
/*  570 */       return this.m.at(i).Identifier(name);
/*      */     }
/*      */
/*  573 */     throw new ParseException("dc.identifier.expected");
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   protected DCTree.DCText quotedString() {
/*  582 */     int i = this.bp;
/*  583 */     nextChar();
/*      */
/*      */
/*  586 */     while (this.bp < this.buflen) {
/*  587 */       switch (this.ch) { case '\n': case '\f':
/*      */         case '\r':
/*  589 */           this.newline = true;
/*      */           break;
/*      */
/*      */
/*      */
/*      */
/*      */         case '"':
/*  596 */           nextChar();
/*      */
/*  598 */           return this.m.at(i).Text(newString(i, this.bp));
/*      */
/*      */         case '@':
/*  601 */           if (this.newline)
/*      */             break;
/*      */           break; }
/*      */
/*  605 */       nextChar();
/*      */     }
/*  607 */     return null;
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   protected List<DCTree> inlineContent() {
/*  617 */     ListBuffer<DCTree> listBuffer = new ListBuffer();
/*      */
/*  619 */     skipWhitespace();
/*  620 */     int i = this.bp;
/*  621 */     byte b = 1;
/*  622 */     this.textStart = -1;
/*      */
/*      */
/*  625 */     while (this.bp < this.buflen) {
/*      */
/*  627 */       switch (this.ch) { case '\n': case '\f':
/*      */         case '\r':
/*  629 */           this.newline = true;
/*      */
/*      */         case '\t':
/*      */         case ' ':
/*  633 */           nextChar();
/*      */           continue;
/*      */
/*      */         case '&':
/*  637 */           entity(listBuffer);
/*      */           continue;
/*      */
/*      */         case '<':
/*  641 */           this.newline = false;
/*  642 */           addPendingText(listBuffer, this.bp - 1);
/*  643 */           listBuffer.add(html());
/*      */           continue;
/*      */
/*      */         case '{':
/*  647 */           this.newline = false;
/*  648 */           b++;
/*  649 */           nextChar();
/*      */           continue;
/*      */
/*      */         case '}':
/*  653 */           this.newline = false;
/*  654 */           if (--b == 0) {
/*  655 */             addPendingText(listBuffer, this.bp - 1);
/*  656 */             nextChar();
/*  657 */             return listBuffer.toList();
/*      */           }
/*  659 */           nextChar();
/*      */           continue;
/*      */
/*      */         case '@':
/*  663 */           if (this.newline) {
/*      */             break;
/*      */           }
/*      */           break; }
/*      */
/*  668 */       if (this.textStart == -1)
/*  669 */         this.textStart = this.bp;
/*  670 */       nextChar();
/*      */     }
/*      */
/*      */
/*      */
/*  675 */     return List.of(erroneous("dc.unterminated.inline.tag", i));
/*      */   }
/*      */
/*      */   protected void entity(ListBuffer<DCTree> paramListBuffer) {
/*  679 */     this.newline = false;
/*  680 */     addPendingText(paramListBuffer, this.bp - 1);
/*  681 */     paramListBuffer.add(entity());
/*  682 */     if (this.textStart == -1) {
/*  683 */       this.textStart = this.bp;
/*  684 */       this.lastNonWhite = -1;
/*      */     }
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   protected DCTree entity() {
/*  693 */     int i = this.bp;
/*  694 */     nextChar();
/*  695 */     Name name = null;
/*  696 */     boolean bool = false;
/*  697 */     if (this.ch == '#') {
/*  698 */       int j = this.bp;
/*  699 */       nextChar();
/*  700 */       if (isDecimalDigit(this.ch)) {
/*  701 */         nextChar();
/*  702 */         while (isDecimalDigit(this.ch))
/*  703 */           nextChar();
/*  704 */         name = this.names.fromChars(this.buf, j, this.bp - j);
/*  705 */       } else if (this.ch == 'x' || this.ch == 'X') {
/*  706 */         nextChar();
/*  707 */         if (isHexDigit(this.ch)) {
/*  708 */           nextChar();
/*  709 */           while (isHexDigit(this.ch))
/*  710 */             nextChar();
/*  711 */           name = this.names.fromChars(this.buf, j, this.bp - j);
/*      */         }
/*      */       }
/*  714 */     } else if (isIdentifierStart(this.ch)) {
/*  715 */       name = readIdentifier();
/*      */     }
/*      */
/*  718 */     if (name == null) {
/*  719 */       return (DCTree)erroneous("dc.bad.entity", i);
/*      */     }
/*  721 */     if (this.ch != ';')
/*  722 */       return (DCTree)erroneous("dc.missing.semicolon", i);
/*  723 */     nextChar();
/*  724 */     return (DCTree)this.m.at(i).Entity(name);
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   protected DCTree html() {
/*  733 */     int i = this.bp;
/*  734 */     nextChar();
/*  735 */     if (isIdentifierStart(this.ch)) {
/*  736 */       Name name = readIdentifier();
/*  737 */       List<DCTree> list = htmlAttrs();
/*  738 */       if (list != null) {
/*  739 */         boolean bool = false;
/*  740 */         if (this.ch == '/') {
/*  741 */           nextChar();
/*  742 */           bool = true;
/*      */         }
/*  744 */         if (this.ch == '>') {
/*  745 */           nextChar();
/*  746 */           return (DCTree)this.m.at(i).StartElement(name, list, bool).setEndPos(this.bp);
/*      */         }
/*      */       }
/*  749 */     } else if (this.ch == '/') {
/*  750 */       nextChar();
/*  751 */       if (isIdentifierStart(this.ch)) {
/*  752 */         Name name = readIdentifier();
/*  753 */         skipWhitespace();
/*  754 */         if (this.ch == '>') {
/*  755 */           nextChar();
/*  756 */           return (DCTree)this.m.at(i).EndElement(name);
/*      */         }
/*      */       }
/*  759 */     } else if (this.ch == '!') {
/*  760 */       nextChar();
/*  761 */       if (this.ch == '-') {
/*  762 */         nextChar();
/*  763 */         if (this.ch == '-') {
/*  764 */           nextChar();
/*  765 */           while (this.bp < this.buflen) {
/*  766 */             byte b = 0;
/*  767 */             while (this.ch == '-') {
/*  768 */               b++;
/*  769 */               nextChar();
/*      */             }
/*      */
/*      */
/*  773 */             if (b >= 2 && this.ch == '>') {
/*  774 */               nextChar();
/*  775 */               return (DCTree)this.m.at(i).Comment(newString(i, this.bp));
/*      */             }
/*      */
/*  778 */             nextChar();
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */
/*  784 */     this.bp = i + 1;
/*  785 */     this.ch = this.buf[this.bp];
/*  786 */     return (DCTree)erroneous("dc.malformed.html", i);
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   protected List<DCTree> htmlAttrs() {
/*  795 */     ListBuffer listBuffer = new ListBuffer();
/*  796 */     skipWhitespace();
/*      */
/*      */
/*  799 */     label35: while (isIdentifierStart(this.ch)) {
/*  800 */       int i = this.bp;
/*  801 */       Name name = readIdentifier();
/*  802 */       skipWhitespace();
/*  803 */       List list = null;
/*  804 */       AttributeTree.ValueKind valueKind = AttributeTree.ValueKind.EMPTY;
/*  805 */       if (this.ch == '=') {
/*  806 */         ListBuffer<DCTree> listBuffer1 = new ListBuffer();
/*  807 */         nextChar();
/*  808 */         skipWhitespace();
/*  809 */         if (this.ch == '\'' || this.ch == '"') {
/*  810 */           valueKind = (this.ch == '\'') ? AttributeTree.ValueKind.SINGLE : AttributeTree.ValueKind.DOUBLE;
/*  811 */           char c = this.ch;
/*  812 */           nextChar();
/*  813 */           this.textStart = this.bp;
/*  814 */           while (this.bp < this.buflen && this.ch != c) {
/*  815 */             if (this.newline && this.ch == '@') {
/*  816 */               listBuffer.add(erroneous("dc.unterminated.string", i));
/*      */
/*      */
/*      */               break label35;
/*      */             }
/*      */
/*      */
/*  823 */             attrValueChar(listBuffer1);
/*      */           }
/*  825 */           addPendingText(listBuffer1, this.bp - 1);
/*  826 */           nextChar();
/*      */         } else {
/*  828 */           valueKind = AttributeTree.ValueKind.UNQUOTED;
/*  829 */           this.textStart = this.bp;
/*  830 */           while (this.bp < this.buflen && !isUnquotedAttrValueTerminator(this.ch)) {
/*  831 */             attrValueChar(listBuffer1);
/*      */           }
/*  833 */           addPendingText(listBuffer1, this.bp - 1);
/*      */         }
/*  835 */         skipWhitespace();
/*  836 */         list = listBuffer1.toList();
/*      */       }
/*  838 */       DCTree.DCAttribute dCAttribute = this.m.at(i).Attribute(name, valueKind, list);
/*  839 */       listBuffer.add(dCAttribute);
/*      */     }
/*      */
/*  842 */     return listBuffer.toList();
/*      */   }
/*      */
/*      */   protected void attrValueChar(ListBuffer<DCTree> paramListBuffer) {
/*  846 */     switch (this.ch) {
/*      */       case '&':
/*  848 */         entity(paramListBuffer);
/*      */         return;
/*      */
/*      */       case '{':
/*  852 */         inlineTag(paramListBuffer);
/*      */         return;
/*      */     }
/*      */
/*  856 */     nextChar();
/*      */   }
/*      */
/*      */
/*      */   protected void addPendingText(ListBuffer<DCTree> paramListBuffer, int paramInt) {
/*  861 */     if (this.textStart != -1) {
/*  862 */       if (this.textStart <= paramInt) {
/*  863 */         paramListBuffer.add(this.m.at(this.textStart).Text(newString(this.textStart, paramInt + 1)));
/*      */       }
/*  865 */       this.textStart = -1;
/*      */     }
/*      */   }
/*      */
/*      */   protected DCTree.DCErroneous erroneous(String paramString, int paramInt) {
/*  870 */     int i = this.bp - 1;
/*      */
/*  872 */     while (i > paramInt) {
/*  873 */       switch (this.buf[i]) { case '\n': case '\f':
/*      */         case '\r':
/*  875 */           this.newline = true; break;
/*      */         case '\t':
/*      */         case ' ':
/*      */           break;
/*      */         default:
/*      */           break; }
/*      */
/*  882 */       i--;
/*      */     }
/*  884 */     this.textStart = -1;
/*  885 */     return this.m.at(paramInt).Erroneous(newString(paramInt, i + 1), this.diagSource, paramString, new Object[0]);
/*      */   }
/*      */
/*      */
/*      */   <T> T getFirst(List<T>... paramVarArgs) {
/*  890 */     for (List<T> list : paramVarArgs) {
/*  891 */       if (list.nonEmpty())
/*  892 */         return (T)list.head;
/*      */     }
/*  894 */     return null;
/*      */   }
/*      */
/*      */   protected boolean isIdentifierStart(char paramChar) {
/*  898 */     return Character.isUnicodeIdentifierStart(paramChar);
/*      */   }
/*      */
/*      */   protected Name readIdentifier() {
/*  902 */     int i = this.bp;
/*  903 */     nextChar();
/*  904 */     while (this.bp < this.buflen && Character.isUnicodeIdentifierPart(this.ch))
/*  905 */       nextChar();
/*  906 */     return this.names.fromChars(this.buf, i, this.bp - i);
/*      */   }
/*      */
/*      */   protected Name readTagName() {
/*  910 */     int i = this.bp;
/*  911 */     nextChar();
/*  912 */     while (this.bp < this.buflen && (Character.isUnicodeIdentifierPart(this.ch) || this.ch == '.'))
/*  913 */       nextChar();
/*  914 */     return this.names.fromChars(this.buf, i, this.bp - i);
/*      */   }
/*      */
/*      */   protected boolean isJavaIdentifierStart(char paramChar) {
/*  918 */     return Character.isJavaIdentifierStart(paramChar);
/*      */   }
/*      */
/*      */   protected Name readJavaIdentifier() {
/*  922 */     int i = this.bp;
/*  923 */     nextChar();
/*  924 */     while (this.bp < this.buflen && Character.isJavaIdentifierPart(this.ch))
/*  925 */       nextChar();
/*  926 */     return this.names.fromChars(this.buf, i, this.bp - i);
/*      */   }
/*      */
/*      */   protected boolean isDecimalDigit(char paramChar) {
/*  930 */     return ('0' <= paramChar && paramChar <= '9');
/*      */   }
/*      */
/*      */   protected boolean isHexDigit(char paramChar) {
/*  934 */     return (('0' <= paramChar && paramChar <= '9') || ('a' <= paramChar && paramChar <= 'f') || ('A' <= paramChar && paramChar <= 'F'));
/*      */   }
/*      */
/*      */
/*      */
/*      */   protected boolean isUnquotedAttrValueTerminator(char paramChar) {
/*  940 */     switch (paramChar) { case '\t': case '\n': case '\f': case '\r': case ' ': case '"': case '\'':
/*      */       case '<':
/*      */       case '=':
/*      */       case '>':
/*      */       case '`':
/*  945 */         return true; }
/*      */
/*  947 */     return false;
/*      */   }
/*      */
/*      */
/*      */   protected boolean isWhitespace(char paramChar) {
/*  952 */     return Character.isWhitespace(paramChar);
/*      */   }
/*      */
/*      */   protected void skipWhitespace() {
/*  956 */     while (isWhitespace(this.ch))
/*  957 */       nextChar();
/*      */   }
/*      */
/*      */   protected int getSentenceBreak(String paramString) {
/*  961 */     if (this.sentenceBreaker != null) {
/*  962 */       this.sentenceBreaker.setText(paramString);
/*  963 */       int i = this.sentenceBreaker.next();
/*  964 */       return (i == paramString.length()) ? -1 : i;
/*      */     }
/*      */
/*      */
/*  968 */     boolean bool = false;
/*  969 */     for (byte b = 0; b < paramString.length(); b++) {
/*  970 */       switch (paramString.charAt(b)) {
/*      */         case '.':
/*  972 */           bool = true;
/*      */           break;
/*      */
/*      */         case '\t':
/*      */         case '\n':
/*      */         case '\f':
/*      */         case '\r':
/*      */         case ' ':
/*  980 */           if (bool) {
/*  981 */             return b;
/*      */           }
/*      */           break;
/*      */         default:
/*  985 */           bool = false;
/*      */           break;
/*      */       }
/*      */     }
/*  989 */     return -1;
/*      */   }
/*      */
/*      */   DocCommentParser(ParserFactory paramParserFactory, DiagnosticSource paramDiagnosticSource, Tokens.Comment paramComment) {
/*  993 */     this.htmlBlockTags = new HashSet<>(Arrays.asList(new String[] { "h1", "h2", "h3", "h4", "h5", "h6", "p", "pre" })); this.fac = paramParserFactory; this.diagSource = paramDiagnosticSource; this.comment = paramComment; this.names = paramParserFactory.names; this.m = paramParserFactory.docTreeMaker; Locale locale = (paramParserFactory.locale == null) ? Locale.getDefault() : paramParserFactory.locale; Options options = paramParserFactory.options;
/*      */     boolean bool = options.isSet("breakIterator");
/*      */     if (bool || !locale.getLanguage().equals(Locale.ENGLISH.getLanguage()))
/*      */       this.sentenceBreaker = BreakIterator.getSentenceInstance(locale);
/*  997 */     initTagParsers(); } protected boolean isSentenceBreak(Name paramName) { return this.htmlBlockTags.contains(StringUtils.toLowerCase(paramName.toString())); }
/*      */
/*      */
/*      */   protected boolean isSentenceBreak(DCTree paramDCTree) {
/* 1001 */     switch (paramDCTree.getKind()) {
/*      */       case INLINE:
/* 1003 */         return isSentenceBreak(((DCTree.DCStartElement)paramDCTree).getName());
/*      */
/*      */       case null:
/* 1006 */         return isSentenceBreak(((DCTree.DCEndElement)paramDCTree).getName());
/*      */     }
/* 1008 */     return false;
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   String newString(int paramInt1, int paramInt2) {
/* 1016 */     return new String(this.buf, paramInt1, paramInt2 - paramInt1);
/*      */   }
/*      */   static abstract class TagParser { Kind kind; DocTree.Kind treeKind;
/*      */
/* 1020 */     enum Kind { INLINE, BLOCK; }
/*      */
/*      */
/*      */
/*      */
/*      */     TagParser(Kind param1Kind, DocTree.Kind param1Kind1) {
/* 1026 */       this.kind = param1Kind;
/* 1027 */       this.treeKind = param1Kind1;
/*      */     }
/*      */
/*      */     Kind getKind() {
/* 1031 */       return this.kind;
/*      */     }
/*      */
/*      */     DocTree.Kind getTreeKind() {
/* 1035 */       return this.treeKind;
/*      */     }
/*      */
/*      */     abstract DCTree parse(int param1Int) throws ParseException; }
/*      */
/*      */   enum Kind {
/*      */     INLINE, BLOCK;
/*      */   }
/*      */
/*      */   private void initTagParsers() {
/* 1045 */     TagParser[] arrayOfTagParser = { new TagParser(TagParser.Kind.BLOCK, DocTree.Kind.AUTHOR)
/*      */         {
/*      */           public DCTree parse(int param1Int)
/*      */           {
/* 1049 */             List<DCTree> list = DocCommentParser.this.blockContent();
/* 1050 */             return (DCTree)DocCommentParser.this.m.at(param1Int).Author(list);
/*      */           }
/*      */         }, new TagParser(TagParser.Kind.INLINE, DocTree.Kind.CODE)
/*      */         {
/*      */
/*      */           public DCTree parse(int param1Int) throws ParseException
/*      */           {
/* 1057 */             DCTree dCTree = DocCommentParser.this.inlineText();
/* 1058 */             DocCommentParser.this.nextChar();
/* 1059 */             return (DCTree)DocCommentParser.this.m.at(param1Int).Code((DCTree.DCText)dCTree);
/*      */           }
/*      */         }, new TagParser(TagParser.Kind.BLOCK, DocTree.Kind.DEPRECATED)
/*      */         {
/*      */
/*      */           public DCTree parse(int param1Int)
/*      */           {
/* 1066 */             List<DCTree> list = DocCommentParser.this.blockContent();
/* 1067 */             return (DCTree)DocCommentParser.this.m.at(param1Int).Deprecated(list);
/*      */           }
/*      */         }, new TagParser(TagParser.Kind.INLINE, DocTree.Kind.DOC_ROOT)
/*      */         {
/*      */
/*      */           public DCTree parse(int param1Int) throws ParseException
/*      */           {
/* 1074 */             if (DocCommentParser.this.ch == '}') {
/* 1075 */               DocCommentParser.this.nextChar();
/* 1076 */               return (DCTree)DocCommentParser.this.m.at(param1Int).DocRoot();
/*      */             }
/* 1078 */             DocCommentParser.this.inlineText();
/* 1079 */             DocCommentParser.this.nextChar();
/* 1080 */             throw new ParseException("dc.unexpected.content");
/*      */           }
/*      */         }, new TagParser(TagParser.Kind.BLOCK, DocTree.Kind.EXCEPTION)
/*      */         {
/*      */
/*      */           public DCTree parse(int param1Int) throws ParseException
/*      */           {
/* 1087 */             DocCommentParser.this.skipWhitespace();
/* 1088 */             DCTree.DCReference dCReference = DocCommentParser.this.reference(false);
/* 1089 */             List<DCTree> list = DocCommentParser.this.blockContent();
/* 1090 */             return (DCTree)DocCommentParser.this.m.at(param1Int).Exception(dCReference, list);
/*      */           }
/*      */         }, new TagParser(TagParser.Kind.INLINE, DocTree.Kind.INHERIT_DOC)
/*      */         {
/*      */
/*      */           public DCTree parse(int param1Int) throws ParseException
/*      */           {
/* 1097 */             if (DocCommentParser.this.ch == '}') {
/* 1098 */               DocCommentParser.this.nextChar();
/* 1099 */               return (DCTree)DocCommentParser.this.m.at(param1Int).InheritDoc();
/*      */             }
/* 1101 */             DocCommentParser.this.inlineText();
/* 1102 */             DocCommentParser.this.nextChar();
/* 1103 */             throw new ParseException("dc.unexpected.content");
/*      */           }
/*      */         }, new TagParser(TagParser.Kind.INLINE, DocTree.Kind.LINK)
/*      */         {
/*      */
/*      */           public DCTree parse(int param1Int) throws ParseException
/*      */           {
/* 1110 */             DCTree.DCReference dCReference = DocCommentParser.this.reference(true);
/* 1111 */             List<DCTree> list = DocCommentParser.this.inlineContent();
/* 1112 */             return (DCTree)DocCommentParser.this.m.at(param1Int).Link(dCReference, list);
/*      */           }
/*      */         }, new TagParser(TagParser.Kind.INLINE, DocTree.Kind.LINK_PLAIN)
/*      */         {
/*      */
/*      */           public DCTree parse(int param1Int) throws ParseException
/*      */           {
/* 1119 */             DCTree.DCReference dCReference = DocCommentParser.this.reference(true);
/* 1120 */             List<DCTree> list = DocCommentParser.this.inlineContent();
/* 1121 */             return (DCTree)DocCommentParser.this.m.at(param1Int).LinkPlain(dCReference, list);
/*      */           }
/*      */         }, new TagParser(TagParser.Kind.INLINE, DocTree.Kind.LITERAL)
/*      */         {
/*      */
/*      */           public DCTree parse(int param1Int) throws ParseException
/*      */           {
/* 1128 */             DCTree dCTree = DocCommentParser.this.inlineText();
/* 1129 */             DocCommentParser.this.nextChar();
/* 1130 */             return (DCTree)DocCommentParser.this.m.at(param1Int).Literal((DCTree.DCText)dCTree);
/*      */           }
/*      */         }, new TagParser(TagParser.Kind.BLOCK, DocTree.Kind.PARAM)
/*      */         {
/*      */
/*      */           public DCTree parse(int param1Int) throws ParseException
/*      */           {
/* 1137 */             DocCommentParser.this.skipWhitespace();
/*      */
/* 1139 */             boolean bool = false;
/* 1140 */             if (DocCommentParser.this.ch == '<') {
/* 1141 */               bool = true;
/* 1142 */               DocCommentParser.this.nextChar();
/*      */             }
/*      */
/* 1145 */             DCTree.DCIdentifier dCIdentifier = DocCommentParser.this.identifier();
/*      */
/* 1147 */             if (bool) {
/* 1148 */               if (DocCommentParser.this.ch != '>')
/* 1149 */                 throw new ParseException("dc.gt.expected");
/* 1150 */               DocCommentParser.this.nextChar();
/*      */             }
/*      */
/* 1153 */             DocCommentParser.this.skipWhitespace();
/* 1154 */             List<DCTree> list = DocCommentParser.this.blockContent();
/* 1155 */             return (DCTree)DocCommentParser.this.m.at(param1Int).Param(bool, dCIdentifier, list);
/*      */           }
/*      */         },
/*      */         new TagParser(TagParser.Kind.BLOCK, DocTree.Kind.RETURN)
/*      */         {
/*      */           public DCTree parse(int param1Int)
/*      */           {
/* 1162 */             List<DCTree> list = DocCommentParser.this.blockContent();
/* 1163 */             return (DCTree)DocCommentParser.this.m.at(param1Int).Return(list);
/*      */           }
/*      */         }, new TagParser(TagParser.Kind.BLOCK, DocTree.Kind.SEE)
/*      */         {
/*      */           public DCTree parse(int param1Int) throws ParseException {
/*      */             DCTree.DCText dCText;
/*      */             List<DCTree> list;
/* 1170 */             DocCommentParser.this.skipWhitespace();
/* 1171 */             switch (DocCommentParser.this.ch)
/*      */             { case '"':
/* 1173 */                 dCText = DocCommentParser.this.quotedString();
/* 1174 */                 if (dCText != null) {
/* 1175 */                   DocCommentParser.this.skipWhitespace();
/* 1176 */                   if (DocCommentParser.this.ch == '@' || (DocCommentParser.this.ch == '\032' && DocCommentParser.this.bp == DocCommentParser.this.buf.length - 1))
/*      */                   {
/* 1178 */                     return (DCTree)DocCommentParser.this.m.at(param1Int).See(List.of(dCText));
/*      */                   }
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
/*      */
/*      */
/* 1206 */                 throw new ParseException("dc.unexpected.content");case '<': list = DocCommentParser.this.blockContent(); if (list != null) return (DCTree)DocCommentParser.this.m.at(param1Int).See(list);  throw new ParseException("dc.unexpected.content");case '@': if (DocCommentParser.this.newline) throw new ParseException("dc.no.content");  throw new ParseException("dc.unexpected.content");case '\032': if (DocCommentParser.this.bp == DocCommentParser.this.buf.length - 1) throw new ParseException("dc.no.content");  throw new ParseException("dc.unexpected.content"); }  if (DocCommentParser.this.isJavaIdentifierStart(DocCommentParser.this.ch) || DocCommentParser.this.ch == '#') { DCTree.DCReference dCReference = DocCommentParser.this.reference(true); List<DCTree> list1 = DocCommentParser.this.blockContent(); return (DCTree)DocCommentParser.this.m.at(param1Int).See(list1.prepend(dCReference)); }  throw new ParseException("dc.unexpected.content");
/*      */           }
/*      */         }, new TagParser(TagParser.Kind.BLOCK, DocTree.Kind.SERIAL_DATA)
/*      */         {
/*      */
/*      */           public DCTree parse(int param1Int)
/*      */           {
/* 1213 */             List<DCTree> list = DocCommentParser.this.blockContent();
/* 1214 */             return (DCTree)DocCommentParser.this.m.at(param1Int).SerialData(list);
/*      */           }
/*      */         }, new TagParser(TagParser.Kind.BLOCK, DocTree.Kind.SERIAL_FIELD)
/*      */         {
/*      */
/*      */           public DCTree parse(int param1Int) throws ParseException
/*      */           {
/* 1221 */             DocCommentParser.this.skipWhitespace();
/* 1222 */             DCTree.DCIdentifier dCIdentifier = DocCommentParser.this.identifier();
/* 1223 */             DocCommentParser.this.skipWhitespace();
/* 1224 */             DCTree.DCReference dCReference = DocCommentParser.this.reference(false);
/* 1225 */             List<DCTree> list = null;
/* 1226 */             if (DocCommentParser.this.isWhitespace(DocCommentParser.this.ch)) {
/* 1227 */               DocCommentParser.this.skipWhitespace();
/* 1228 */               list = DocCommentParser.this.blockContent();
/*      */             }
/* 1230 */             return (DCTree)DocCommentParser.this.m.at(param1Int).SerialField(dCIdentifier, dCReference, list);
/*      */           }
/*      */         }, new TagParser(TagParser.Kind.BLOCK, DocTree.Kind.SERIAL)
/*      */         {
/*      */
/*      */           public DCTree parse(int param1Int)
/*      */           {
/* 1237 */             List<DCTree> list = DocCommentParser.this.blockContent();
/* 1238 */             return (DCTree)DocCommentParser.this.m.at(param1Int).Serial(list);
/*      */           }
/*      */         }, new TagParser(TagParser.Kind.BLOCK, DocTree.Kind.SINCE)
/*      */         {
/*      */
/*      */           public DCTree parse(int param1Int)
/*      */           {
/* 1245 */             List<DCTree> list = DocCommentParser.this.blockContent();
/* 1246 */             return (DCTree)DocCommentParser.this.m.at(param1Int).Since(list);
/*      */           }
/*      */         }, new TagParser(TagParser.Kind.BLOCK, DocTree.Kind.THROWS)
/*      */         {
/*      */
/*      */           public DCTree parse(int param1Int) throws ParseException
/*      */           {
/* 1253 */             DocCommentParser.this.skipWhitespace();
/* 1254 */             DCTree.DCReference dCReference = DocCommentParser.this.reference(false);
/* 1255 */             List<DCTree> list = DocCommentParser.this.blockContent();
/* 1256 */             return (DCTree)DocCommentParser.this.m.at(param1Int).Throws(dCReference, list);
/*      */           }
/*      */         }, new TagParser(TagParser.Kind.INLINE, DocTree.Kind.VALUE)
/*      */         {
/*      */
/*      */           public DCTree parse(int param1Int) throws ParseException
/*      */           {
/* 1263 */             DCTree.DCReference dCReference = DocCommentParser.this.reference(true);
/* 1264 */             DocCommentParser.this.skipWhitespace();
/* 1265 */             if (DocCommentParser.this.ch == '}') {
/* 1266 */               DocCommentParser.this.nextChar();
/* 1267 */               return (DCTree)DocCommentParser.this.m.at(param1Int).Value(dCReference);
/*      */             }
/* 1269 */             DocCommentParser.this.nextChar();
/* 1270 */             throw new ParseException("dc.unexpected.content");
/*      */           }
/*      */         }, new TagParser(TagParser.Kind.BLOCK, DocTree.Kind.VERSION)
/*      */         {
/*      */
/*      */           public DCTree parse(int param1Int)
/*      */           {
/* 1277 */             List<DCTree> list = DocCommentParser.this.blockContent();
/* 1278 */             return (DCTree)DocCommentParser.this.m.at(param1Int).Version(list);
/*      */           }
/*      */         } };
/*      */
/*      */
/* 1283 */     this.tagParsers = new HashMap<>();
/* 1284 */     for (TagParser tagParser : arrayOfTagParser)
/* 1285 */       this.tagParsers.put(this.names.fromString((tagParser.getTreeKind()).tagName), tagParser);
/*      */   }
/*      */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\javac\parser\DocCommentParser.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
