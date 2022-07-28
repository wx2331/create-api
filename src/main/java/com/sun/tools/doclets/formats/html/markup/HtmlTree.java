/*     */ package com.sun.tools.doclets.formats.html.markup;
/*     */ 
/*     */ import com.sun.tools.doclets.internal.toolkit.Content;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.DocletConstants;
/*     */ import java.io.IOException;
/*     */ import java.io.Writer;
/*     */ import java.nio.charset.Charset;
/*     */ import java.util.ArrayList;
/*     */ import java.util.BitSet;
/*     */ import java.util.Collections;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
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
/*     */ public class HtmlTree
/*     */   extends Content
/*     */ {
/*     */   private HtmlTag htmlTag;
/*  49 */   private Map<HtmlAttr, String> attrs = Collections.emptyMap();
/*  50 */   private List<Content> content = Collections.emptyList();
/*  51 */   public static final Content EMPTY = new StringContent("");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public HtmlTree(HtmlTag paramHtmlTag) {
/*  59 */     this.htmlTag = (HtmlTag)nullCheck(paramHtmlTag);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public HtmlTree(HtmlTag paramHtmlTag, Content... paramVarArgs) {
/*  69 */     this(paramHtmlTag);
/*  70 */     for (Content content : paramVarArgs) {
/*  71 */       addContent(content);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addAttr(HtmlAttr paramHtmlAttr, String paramString) {
/*  81 */     if (this.attrs.isEmpty())
/*  82 */       this.attrs = new LinkedHashMap<>(3); 
/*  83 */     this.attrs.put(nullCheck(paramHtmlAttr), escapeHtmlChars(paramString));
/*     */   }
/*     */   
/*     */   public void setTitle(Content paramContent) {
/*  87 */     addAttr(HtmlAttr.TITLE, stripHtml(paramContent));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addStyle(HtmlStyle paramHtmlStyle) {
/*  96 */     addAttr(HtmlAttr.CLASS, paramHtmlStyle.toString());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addContent(Content paramContent) {
/* 105 */     if (paramContent instanceof ContentBuilder) {
/* 106 */       for (Content content : ((ContentBuilder)paramContent).contents) {
/* 107 */         addContent(content);
/*     */       }
/*     */     }
/* 110 */     else if (paramContent == EMPTY || paramContent.isValid()) {
/* 111 */       if (this.content.isEmpty())
/* 112 */         this.content = new ArrayList<>(); 
/* 113 */       this.content.add(paramContent);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addContent(String paramString) {
/* 125 */     if (!this.content.isEmpty()) {
/* 126 */       Content content = this.content.get(this.content.size() - 1);
/* 127 */       if (content instanceof StringContent) {
/* 128 */         content.addContent(paramString);
/*     */       } else {
/* 130 */         addContent(new StringContent(paramString));
/*     */       } 
/*     */     } else {
/* 133 */       addContent(new StringContent(paramString));
/*     */     } 
/*     */   }
/*     */   public int charCount() {
/* 137 */     int i = 0;
/* 138 */     for (Content content : this.content)
/* 139 */       i += content.charCount(); 
/* 140 */     return i;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static String escapeHtmlChars(String paramString) {
/* 151 */     for (byte b = 0; b < paramString.length(); b++) {
/* 152 */       StringBuilder stringBuilder; char c = paramString.charAt(b);
/* 153 */       switch (c) { case '&':
/*     */         case '<':
/*     */         case '>':
/* 156 */           stringBuilder = new StringBuilder(paramString.substring(0, b));
/* 157 */           for (; b < paramString.length(); b++) {
/* 158 */             c = paramString.charAt(b);
/* 159 */             switch (c) { case '<':
/* 160 */                 stringBuilder.append("&lt;"); break;
/* 161 */               case '>': stringBuilder.append("&gt;"); break;
/* 162 */               case '&': stringBuilder.append("&amp;"); break;
/* 163 */               default: stringBuilder.append(c); break; }
/*     */           
/*     */           } 
/* 166 */           return stringBuilder.toString(); }
/*     */     
/*     */     } 
/* 169 */     return paramString;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 175 */   public static final BitSet NONENCODING_CHARS = new BitSet(256);
/*     */   
/*     */   static {
/*     */     byte b1;
/* 179 */     for (b1 = 97; b1 <= 122; b1++) {
/* 180 */       NONENCODING_CHARS.set(b1);
/*     */     }
/* 182 */     for (b1 = 65; b1 <= 90; b1++) {
/* 183 */       NONENCODING_CHARS.set(b1);
/*     */     }
/*     */     
/* 186 */     for (b1 = 48; b1 <= 57; b1++) {
/* 187 */       NONENCODING_CHARS.set(b1);
/*     */     }
/*     */     
/* 190 */     String str = ":/?#[]@!$&'()*+,;=";
/*     */     
/* 192 */     str = str + "-._~";
/* 193 */     for (byte b2 = 0; b2 < str.length(); b2++) {
/* 194 */       NONENCODING_CHARS.set(str.charAt(b2));
/*     */     }
/*     */   }
/*     */   
/*     */   private static String encodeURL(String paramString) {
/* 199 */     byte[] arrayOfByte = paramString.getBytes(Charset.forName("UTF-8"));
/* 200 */     StringBuilder stringBuilder = new StringBuilder();
/* 201 */     for (byte b = 0; b < arrayOfByte.length; b++) {
/* 202 */       byte b1 = arrayOfByte[b];
/* 203 */       if (NONENCODING_CHARS.get(b1 & 0xFF)) {
/* 204 */         stringBuilder.append((char)b1);
/*     */       } else {
/* 206 */         stringBuilder.append(String.format("%%%02X", new Object[] { Integer.valueOf(b1 & 0xFF) }));
/*     */       } 
/*     */     } 
/* 209 */     return stringBuilder.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static HtmlTree A(String paramString, Content paramContent) {
/* 220 */     HtmlTree htmlTree = new HtmlTree(HtmlTag.A, new Content[] { (Content)nullCheck(paramContent) });
/* 221 */     htmlTree.addAttr(HtmlAttr.HREF, encodeURL(paramString));
/* 222 */     return htmlTree;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static HtmlTree A_NAME(String paramString, Content paramContent) {
/* 233 */     HtmlTree htmlTree = A_NAME(paramString);
/* 234 */     htmlTree.addContent((Content)nullCheck(paramContent));
/* 235 */     return htmlTree;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static HtmlTree A_NAME(String paramString) {
/* 245 */     HtmlTree htmlTree = new HtmlTree(HtmlTag.A);
/* 246 */     htmlTree.addAttr(HtmlAttr.NAME, (String)nullCheck(paramString));
/* 247 */     return htmlTree;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static HtmlTree CAPTION(Content paramContent) {
/* 257 */     return new HtmlTree(HtmlTag.CAPTION, new Content[] { (Content)nullCheck(paramContent) });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static HtmlTree CODE(Content paramContent) {
/* 268 */     return new HtmlTree(HtmlTag.CODE, new Content[] { (Content)nullCheck(paramContent) });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static HtmlTree DD(Content paramContent) {
/* 279 */     return new HtmlTree(HtmlTag.DD, new Content[] { (Content)nullCheck(paramContent) });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static HtmlTree DL(Content paramContent) {
/* 290 */     return new HtmlTree(HtmlTag.DL, new Content[] { (Content)nullCheck(paramContent) });
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
/*     */   public static HtmlTree DIV(HtmlStyle paramHtmlStyle, Content paramContent) {
/* 303 */     HtmlTree htmlTree = new HtmlTree(HtmlTag.DIV, new Content[] { (Content)nullCheck(paramContent) });
/* 304 */     if (paramHtmlStyle != null)
/* 305 */       htmlTree.addStyle(paramHtmlStyle); 
/* 306 */     return htmlTree;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static HtmlTree DIV(Content paramContent) {
/* 316 */     return DIV((HtmlStyle)null, paramContent);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static HtmlTree DT(Content paramContent) {
/* 326 */     return new HtmlTree(HtmlTag.DT, new Content[] { (Content)nullCheck(paramContent) });
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
/*     */   public static HtmlTree FRAME(String paramString1, String paramString2, String paramString3, String paramString4) {
/* 340 */     HtmlTree htmlTree = new HtmlTree(HtmlTag.FRAME);
/* 341 */     htmlTree.addAttr(HtmlAttr.SRC, (String)nullCheck(paramString1));
/* 342 */     htmlTree.addAttr(HtmlAttr.NAME, (String)nullCheck(paramString2));
/* 343 */     htmlTree.addAttr(HtmlAttr.TITLE, (String)nullCheck(paramString3));
/* 344 */     if (paramString4 != null)
/* 345 */       htmlTree.addAttr(HtmlAttr.SCROLLING, paramString4); 
/* 346 */     return htmlTree;
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
/*     */   public static HtmlTree FRAME(String paramString1, String paramString2, String paramString3) {
/* 358 */     return FRAME(paramString1, paramString2, paramString3, (String)null);
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
/*     */   public static HtmlTree FRAMESET(String paramString1, String paramString2, String paramString3, String paramString4) {
/* 371 */     HtmlTree htmlTree = new HtmlTree(HtmlTag.FRAMESET);
/* 372 */     if (paramString1 != null)
/* 373 */       htmlTree.addAttr(HtmlAttr.COLS, paramString1); 
/* 374 */     if (paramString2 != null)
/* 375 */       htmlTree.addAttr(HtmlAttr.ROWS, paramString2); 
/* 376 */     htmlTree.addAttr(HtmlAttr.TITLE, (String)nullCheck(paramString3));
/* 377 */     htmlTree.addAttr(HtmlAttr.ONLOAD, (String)nullCheck(paramString4));
/* 378 */     return htmlTree;
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
/*     */   public static HtmlTree HEADING(HtmlTag paramHtmlTag, boolean paramBoolean, HtmlStyle paramHtmlStyle, Content paramContent) {
/* 393 */     HtmlTree htmlTree = new HtmlTree(paramHtmlTag, new Content[] { (Content)nullCheck(paramContent) });
/* 394 */     if (paramBoolean)
/* 395 */       htmlTree.setTitle(paramContent); 
/* 396 */     if (paramHtmlStyle != null)
/* 397 */       htmlTree.addStyle(paramHtmlStyle); 
/* 398 */     return htmlTree;
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
/*     */   public static HtmlTree HEADING(HtmlTag paramHtmlTag, HtmlStyle paramHtmlStyle, Content paramContent) {
/* 411 */     return HEADING(paramHtmlTag, false, paramHtmlStyle, paramContent);
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
/*     */   public static HtmlTree HEADING(HtmlTag paramHtmlTag, boolean paramBoolean, Content paramContent) {
/* 424 */     return HEADING(paramHtmlTag, paramBoolean, (HtmlStyle)null, paramContent);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static HtmlTree HEADING(HtmlTag paramHtmlTag, Content paramContent) {
/* 435 */     return HEADING(paramHtmlTag, false, (HtmlStyle)null, paramContent);
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
/*     */   public static HtmlTree HTML(String paramString, Content paramContent1, Content paramContent2) {
/* 448 */     HtmlTree htmlTree = new HtmlTree(HtmlTag.HTML, new Content[] { (Content)nullCheck(paramContent1), (Content)nullCheck(paramContent2) });
/* 449 */     htmlTree.addAttr(HtmlAttr.LANG, (String)nullCheck(paramString));
/* 450 */     return htmlTree;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static HtmlTree LI(Content paramContent) {
/* 460 */     return LI((HtmlStyle)null, paramContent);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static HtmlTree LI(HtmlStyle paramHtmlStyle, Content paramContent) {
/* 471 */     HtmlTree htmlTree = new HtmlTree(HtmlTag.LI, new Content[] { (Content)nullCheck(paramContent) });
/* 472 */     if (paramHtmlStyle != null)
/* 473 */       htmlTree.addStyle(paramHtmlStyle); 
/* 474 */     return htmlTree;
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
/*     */   public static HtmlTree LINK(String paramString1, String paramString2, String paramString3, String paramString4) {
/* 487 */     HtmlTree htmlTree = new HtmlTree(HtmlTag.LINK);
/* 488 */     htmlTree.addAttr(HtmlAttr.REL, (String)nullCheck(paramString1));
/* 489 */     htmlTree.addAttr(HtmlAttr.TYPE, (String)nullCheck(paramString2));
/* 490 */     htmlTree.addAttr(HtmlAttr.HREF, (String)nullCheck(paramString3));
/* 491 */     htmlTree.addAttr(HtmlAttr.TITLE, (String)nullCheck(paramString4));
/* 492 */     return htmlTree;
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
/*     */   public static HtmlTree META(String paramString1, String paramString2, String paramString3) {
/* 504 */     HtmlTree htmlTree = new HtmlTree(HtmlTag.META);
/* 505 */     String str = paramString2 + "; charset=" + paramString3;
/* 506 */     htmlTree.addAttr(HtmlAttr.HTTP_EQUIV, (String)nullCheck(paramString1));
/* 507 */     htmlTree.addAttr(HtmlAttr.CONTENT, str);
/* 508 */     return htmlTree;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static HtmlTree META(String paramString1, String paramString2) {
/* 519 */     HtmlTree htmlTree = new HtmlTree(HtmlTag.META);
/* 520 */     htmlTree.addAttr(HtmlAttr.NAME, (String)nullCheck(paramString1));
/* 521 */     htmlTree.addAttr(HtmlAttr.CONTENT, (String)nullCheck(paramString2));
/* 522 */     return htmlTree;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static HtmlTree NOSCRIPT(Content paramContent) {
/* 532 */     return new HtmlTree(HtmlTag.NOSCRIPT, new Content[] { (Content)nullCheck(paramContent) });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static HtmlTree P(Content paramContent) {
/* 543 */     return P((HtmlStyle)null, paramContent);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static HtmlTree P(HtmlStyle paramHtmlStyle, Content paramContent) {
/* 554 */     HtmlTree htmlTree = new HtmlTree(HtmlTag.P, new Content[] { (Content)nullCheck(paramContent) });
/* 555 */     if (paramHtmlStyle != null)
/* 556 */       htmlTree.addStyle(paramHtmlStyle); 
/* 557 */     return htmlTree;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static HtmlTree SCRIPT(String paramString1, String paramString2) {
/* 568 */     HtmlTree htmlTree = new HtmlTree(HtmlTag.SCRIPT);
/* 569 */     htmlTree.addAttr(HtmlAttr.TYPE, (String)nullCheck(paramString1));
/* 570 */     htmlTree.addAttr(HtmlAttr.SRC, (String)nullCheck(paramString2));
/* 571 */     return htmlTree;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static HtmlTree SMALL(Content paramContent) {
/* 581 */     return new HtmlTree(HtmlTag.SMALL, new Content[] { (Content)nullCheck(paramContent) });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static HtmlTree SPAN(Content paramContent) {
/* 592 */     return SPAN((HtmlStyle)null, paramContent);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static HtmlTree SPAN(HtmlStyle paramHtmlStyle, Content paramContent) {
/* 603 */     HtmlTree htmlTree = new HtmlTree(HtmlTag.SPAN, new Content[] { (Content)nullCheck(paramContent) });
/* 604 */     if (paramHtmlStyle != null)
/* 605 */       htmlTree.addStyle(paramHtmlStyle); 
/* 606 */     return htmlTree;
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
/*     */   public static HtmlTree SPAN(String paramString, HtmlStyle paramHtmlStyle, Content paramContent) {
/* 619 */     HtmlTree htmlTree = new HtmlTree(HtmlTag.SPAN, new Content[] { (Content)nullCheck(paramContent) });
/* 620 */     htmlTree.addAttr(HtmlAttr.ID, (String)nullCheck(paramString));
/* 621 */     if (paramHtmlStyle != null)
/* 622 */       htmlTree.addStyle(paramHtmlStyle); 
/* 623 */     return htmlTree;
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
/*     */   
/*     */   public static HtmlTree TABLE(HtmlStyle paramHtmlStyle, int paramInt1, int paramInt2, int paramInt3, String paramString, Content paramContent) {
/* 640 */     HtmlTree htmlTree = new HtmlTree(HtmlTag.TABLE, new Content[] { (Content)nullCheck(paramContent) });
/* 641 */     if (paramHtmlStyle != null)
/* 642 */       htmlTree.addStyle(paramHtmlStyle); 
/* 643 */     htmlTree.addAttr(HtmlAttr.BORDER, Integer.toString(paramInt1));
/* 644 */     htmlTree.addAttr(HtmlAttr.CELLPADDING, Integer.toString(paramInt2));
/* 645 */     htmlTree.addAttr(HtmlAttr.CELLSPACING, Integer.toString(paramInt3));
/* 646 */     htmlTree.addAttr(HtmlAttr.SUMMARY, (String)nullCheck(paramString));
/* 647 */     return htmlTree;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static HtmlTree TD(HtmlStyle paramHtmlStyle, Content paramContent) {
/* 658 */     HtmlTree htmlTree = new HtmlTree(HtmlTag.TD, new Content[] { (Content)nullCheck(paramContent) });
/* 659 */     if (paramHtmlStyle != null)
/* 660 */       htmlTree.addStyle(paramHtmlStyle); 
/* 661 */     return htmlTree;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static HtmlTree TD(Content paramContent) {
/* 671 */     return TD((HtmlStyle)null, paramContent);
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
/*     */   public static HtmlTree TH(HtmlStyle paramHtmlStyle, String paramString, Content paramContent) {
/* 683 */     HtmlTree htmlTree = new HtmlTree(HtmlTag.TH, new Content[] { (Content)nullCheck(paramContent) });
/* 684 */     if (paramHtmlStyle != null)
/* 685 */       htmlTree.addStyle(paramHtmlStyle); 
/* 686 */     htmlTree.addAttr(HtmlAttr.SCOPE, (String)nullCheck(paramString));
/* 687 */     return htmlTree;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static HtmlTree TH(String paramString, Content paramContent) {
/* 698 */     return TH((HtmlStyle)null, paramString, paramContent);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static HtmlTree TITLE(Content paramContent) {
/* 708 */     return new HtmlTree(HtmlTag.TITLE, new Content[] { (Content)nullCheck(paramContent) });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static HtmlTree TR(Content paramContent) {
/* 719 */     return new HtmlTree(HtmlTag.TR, new Content[] { (Content)nullCheck(paramContent) });
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
/*     */   public static HtmlTree UL(HtmlStyle paramHtmlStyle, Content paramContent) {
/* 731 */     HtmlTree htmlTree = new HtmlTree(HtmlTag.UL, new Content[] { (Content)nullCheck(paramContent) });
/* 732 */     htmlTree.addStyle((HtmlStyle)nullCheck(paramHtmlStyle));
/* 733 */     return htmlTree;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isEmpty() {
/* 740 */     return (!hasContent() && !hasAttrs());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hasContent() {
/* 749 */     return !this.content.isEmpty();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hasAttrs() {
/* 758 */     return !this.attrs.isEmpty();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hasAttr(HtmlAttr paramHtmlAttr) {
/* 768 */     return this.attrs.containsKey(paramHtmlAttr);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isValid() {
/* 779 */     switch (this.htmlTag) {
/*     */       case A:
/* 781 */         return (hasAttr(HtmlAttr.NAME) || (hasAttr(HtmlAttr.HREF) && hasContent()));
/*     */       case BR:
/* 783 */         return (!hasContent() && (!hasAttrs() || hasAttr(HtmlAttr.CLEAR)));
/*     */       case FRAME:
/* 785 */         return (hasAttr(HtmlAttr.SRC) && !hasContent());
/*     */       case HR:
/* 787 */         return !hasContent();
/*     */       case IMG:
/* 789 */         return (hasAttr(HtmlAttr.SRC) && hasAttr(HtmlAttr.ALT) && !hasContent());
/*     */       case LINK:
/* 791 */         return (hasAttr(HtmlAttr.HREF) && !hasContent());
/*     */       case META:
/* 793 */         return (hasAttr(HtmlAttr.CONTENT) && !hasContent());
/*     */       case SCRIPT:
/* 795 */         return ((hasAttr(HtmlAttr.TYPE) && hasAttr(HtmlAttr.SRC) && !hasContent()) || (
/* 796 */           hasAttr(HtmlAttr.TYPE) && hasContent()));
/*     */     } 
/* 798 */     return hasContent();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isInline() {
/* 808 */     return (this.htmlTag.blockType == HtmlTag.BlockType.INLINE);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean write(Writer paramWriter, boolean paramBoolean) throws IOException {
/* 816 */     if (!isInline() && !paramBoolean)
/* 817 */       paramWriter.write(DocletConstants.NL); 
/* 818 */     String str = this.htmlTag.toString();
/* 819 */     paramWriter.write("<");
/* 820 */     paramWriter.write(str);
/* 821 */     Iterator<HtmlAttr> iterator = this.attrs.keySet().iterator();
/*     */ 
/*     */     
/* 824 */     while (iterator.hasNext()) {
/* 825 */       HtmlAttr htmlAttr = iterator.next();
/* 826 */       String str1 = this.attrs.get(htmlAttr);
/* 827 */       paramWriter.write(" ");
/* 828 */       paramWriter.write(htmlAttr.toString());
/* 829 */       if (!str1.isEmpty()) {
/* 830 */         paramWriter.write("=\"");
/* 831 */         paramWriter.write(str1);
/* 832 */         paramWriter.write("\"");
/*     */       } 
/*     */     } 
/* 835 */     paramWriter.write(">");
/* 836 */     boolean bool = false;
/* 837 */     for (Content content : this.content)
/* 838 */       bool = content.write(paramWriter, bool); 
/* 839 */     if (this.htmlTag.endTagRequired()) {
/* 840 */       paramWriter.write("</");
/* 841 */       paramWriter.write(str);
/* 842 */       paramWriter.write(">");
/*     */     } 
/* 844 */     if (!isInline()) {
/* 845 */       paramWriter.write(DocletConstants.NL);
/* 846 */       return true;
/*     */     } 
/* 848 */     return false;
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
/*     */   private static String stripHtml(Content paramContent) {
/* 861 */     String str = paramContent.toString();
/*     */     
/* 863 */     str = str.replaceAll("\\<.*?>", " ");
/*     */     
/* 865 */     str = str.replaceAll("\\b\\s{2,}\\b", " ");
/*     */     
/* 867 */     return str.trim();
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\doclets\formats\html\markup\HtmlTree.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */