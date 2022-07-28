/*     */ package com.sun.tools.doclets.formats.html.markup;
/*     */ 
/*     */ import com.sun.tools.javac.util.StringUtils;
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
/*     */ public enum HtmlTag
/*     */ {
/*  41 */   A(BlockType.INLINE, EndTag.END),
/*  42 */   BLOCKQUOTE,
/*  43 */   BODY(BlockType.OTHER, EndTag.END),
/*  44 */   BR(BlockType.INLINE, EndTag.NOEND),
/*  45 */   CAPTION,
/*  46 */   CENTER,
/*  47 */   CODE(BlockType.INLINE, EndTag.END),
/*  48 */   DD,
/*  49 */   DIR,
/*  50 */   DIV,
/*  51 */   DL,
/*  52 */   DT,
/*  53 */   EM(BlockType.INLINE, EndTag.END),
/*  54 */   FONT(BlockType.INLINE, EndTag.END),
/*  55 */   FRAME(BlockType.OTHER, EndTag.NOEND),
/*  56 */   FRAMESET(BlockType.OTHER, EndTag.END),
/*  57 */   H1,
/*  58 */   H2,
/*  59 */   H3,
/*  60 */   H4,
/*  61 */   H5,
/*  62 */   H6,
/*  63 */   HEAD(BlockType.OTHER, EndTag.END),
/*  64 */   HR(BlockType.BLOCK, EndTag.NOEND),
/*  65 */   HTML(BlockType.OTHER, EndTag.END),
/*  66 */   I(BlockType.INLINE, EndTag.END),
/*  67 */   IMG(BlockType.INLINE, EndTag.NOEND),
/*  68 */   LI,
/*  69 */   LISTING,
/*  70 */   LINK(BlockType.OTHER, EndTag.NOEND),
/*  71 */   MENU,
/*  72 */   META(BlockType.OTHER, EndTag.NOEND),
/*  73 */   NOFRAMES(BlockType.OTHER, EndTag.END),
/*  74 */   NOSCRIPT(BlockType.OTHER, EndTag.END),
/*  75 */   OL,
/*  76 */   P,
/*  77 */   PRE,
/*  78 */   SCRIPT(BlockType.OTHER, EndTag.END),
/*  79 */   SMALL(BlockType.INLINE, EndTag.END),
/*  80 */   SPAN(BlockType.INLINE, EndTag.END),
/*  81 */   STRONG(BlockType.INLINE, EndTag.END),
/*  82 */   SUB(BlockType.INLINE, EndTag.END),
/*  83 */   TABLE,
/*  84 */   TBODY,
/*  85 */   TD,
/*  86 */   TH,
/*  87 */   TITLE(BlockType.OTHER, EndTag.END),
/*  88 */   TR,
/*  89 */   TT(BlockType.INLINE, EndTag.END),
/*  90 */   UL;
/*     */   
/*     */   public final BlockType blockType;
/*     */   
/*     */   public final EndTag endTag;
/*     */   
/*     */   public final String value;
/*     */   
/*     */   public enum BlockType
/*     */   {
/* 100 */     BLOCK,
/* 101 */     INLINE,
/* 102 */     OTHER;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public enum EndTag
/*     */   {
/* 109 */     END,
/* 110 */     NOEND;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   HtmlTag(BlockType paramBlockType, EndTag paramEndTag) {
/* 118 */     this.blockType = paramBlockType;
/* 119 */     this.endTag = paramEndTag;
/* 120 */     this.value = StringUtils.toLowerCase(name());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean endTagRequired() {
/* 130 */     return (this.endTag == EndTag.END);
/*     */   }
/*     */   
/*     */   public String toString() {
/* 134 */     return this.value;
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\doclets\formats\html\markup\HtmlTag.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */