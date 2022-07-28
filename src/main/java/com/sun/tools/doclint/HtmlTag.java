/*     */ package com.sun.tools.doclint;
/*     */
/*     */ import com.sun.tools.javac.util.StringUtils;
/*     */ import java.util.Collections;
/*     */ import java.util.EnumMap;
/*     */ import java.util.EnumSet;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import javax.lang.model.element.Name;
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
/*     */ public enum HtmlTag
/*     */ {
/*  58 */   A(BlockType.INLINE, EndKind.REQUIRED, new AttrMap[] {
/*  59 */       attrs(AttrKind.OK, new Attr[] { Attr.HREF, Attr.TARGET, Attr.NAME })
/*     */     }),
/*  61 */   B(BlockType.INLINE, EndKind.REQUIRED,
/*  62 */     EnumSet.of(Flag.EXPECT_CONTENT, Flag.NO_NEST), new AttrMap[0]),
/*     */
/*  64 */   BIG(BlockType.INLINE, EndKind.REQUIRED,
/*  65 */     EnumSet.of(Flag.EXPECT_CONTENT), new AttrMap[0]),
/*     */
/*  67 */   BLOCKQUOTE(BlockType.BLOCK, EndKind.REQUIRED,
/*  68 */     EnumSet.of(Flag.ACCEPTS_BLOCK, Flag.ACCEPTS_INLINE), new AttrMap[0]),
/*     */
/*  70 */   BODY(BlockType.OTHER, EndKind.REQUIRED, new AttrMap[0]),
/*     */
/*  72 */   BR(BlockType.INLINE, EndKind.NONE, new AttrMap[] {
/*  73 */       attrs(AttrKind.USE_CSS, new Attr[] { Attr.CLEAR })
/*     */     }),
/*  75 */   CAPTION(BlockType.TABLE_ITEM, EndKind.REQUIRED,
/*  76 */     EnumSet.of(Flag.ACCEPTS_INLINE, Flag.EXPECT_CONTENT), new AttrMap[0]),
/*     */
/*  78 */   CENTER(BlockType.BLOCK, EndKind.REQUIRED,
/*  79 */     EnumSet.of(Flag.ACCEPTS_BLOCK, Flag.ACCEPTS_INLINE), new AttrMap[0]),
/*     */
/*  81 */   CITE(BlockType.INLINE, EndKind.REQUIRED,
/*  82 */     EnumSet.of(Flag.EXPECT_CONTENT, Flag.NO_NEST), new AttrMap[0]),
/*     */
/*  84 */   CODE(BlockType.INLINE, EndKind.REQUIRED,
/*  85 */     EnumSet.of(Flag.EXPECT_CONTENT, Flag.NO_NEST), new AttrMap[0]),
/*     */
/*  87 */   DD(BlockType.LIST_ITEM, EndKind.OPTIONAL,
/*  88 */     EnumSet.of(Flag.ACCEPTS_BLOCK, Flag.ACCEPTS_INLINE, Flag.EXPECT_CONTENT), new AttrMap[0]),
/*     */
/*  90 */   DFN(BlockType.INLINE, EndKind.REQUIRED,
/*  91 */     EnumSet.of(Flag.EXPECT_CONTENT, Flag.NO_NEST), new AttrMap[0]),
/*     */
/*  93 */   DIV(BlockType.BLOCK, EndKind.REQUIRED,
/*  94 */     EnumSet.of(Flag.ACCEPTS_BLOCK, Flag.ACCEPTS_INLINE), new AttrMap[0]),
/*     */
/*  96 */   DL(BlockType.BLOCK, EndKind.REQUIRED,
/*  97 */     EnumSet.of(Flag.EXPECT_CONTENT), new AttrMap[] {
/*  98 */       attrs(AttrKind.USE_CSS, new Attr[] { Attr.COMPACT })
/*     */     }) {
/*     */     public boolean accepts(HtmlTag param1HtmlTag) {
/* 101 */       return (param1HtmlTag == DT || param1HtmlTag == DD);
/*     */     }
/*     */   },
/*     */
/* 105 */   DT(BlockType.LIST_ITEM, EndKind.OPTIONAL,
/* 106 */     EnumSet.of(Flag.ACCEPTS_INLINE, Flag.EXPECT_CONTENT), new AttrMap[0]),
/*     */
/* 108 */   EM(BlockType.INLINE, EndKind.REQUIRED,
/* 109 */     EnumSet.of(Flag.NO_NEST), new AttrMap[0]),
/*     */
/* 111 */   FONT(BlockType.INLINE, EndKind.REQUIRED,
/* 112 */     EnumSet.of(Flag.EXPECT_CONTENT), new AttrMap[] {
/* 113 */       attrs(AttrKind.USE_CSS, new Attr[] { Attr.SIZE, Attr.COLOR, Attr.FACE })
/*     */     }),
/* 115 */   FRAME(BlockType.OTHER, EndKind.NONE, new AttrMap[0]),
/*     */
/* 117 */   FRAMESET(BlockType.OTHER, EndKind.REQUIRED, new AttrMap[0]),
/*     */
/* 119 */   H1(BlockType.BLOCK, EndKind.REQUIRED, new AttrMap[0]),
/* 120 */   H2(BlockType.BLOCK, EndKind.REQUIRED, new AttrMap[0]),
/* 121 */   H3(BlockType.BLOCK, EndKind.REQUIRED, new AttrMap[0]),
/* 122 */   H4(BlockType.BLOCK, EndKind.REQUIRED, new AttrMap[0]),
/* 123 */   H5(BlockType.BLOCK, EndKind.REQUIRED, new AttrMap[0]),
/* 124 */   H6(BlockType.BLOCK, EndKind.REQUIRED, new AttrMap[0]),
/*     */
/* 126 */   HEAD(BlockType.OTHER, EndKind.REQUIRED, new AttrMap[0]),
/*     */
/* 128 */   HR(BlockType.BLOCK, EndKind.NONE, new AttrMap[] {
/* 129 */       attrs(AttrKind.OK, new Attr[] { Attr.WIDTH })
/*     */     }),
/* 131 */   HTML(BlockType.OTHER, EndKind.REQUIRED, new AttrMap[0]),
/*     */
/* 133 */   I(BlockType.INLINE, EndKind.REQUIRED,
/* 134 */     EnumSet.of(Flag.EXPECT_CONTENT, Flag.NO_NEST), new AttrMap[0]),
/*     */
/* 136 */   IMG(BlockType.INLINE, EndKind.NONE, new AttrMap[] {
/* 137 */       attrs(AttrKind.OK, new Attr[] { Attr.SRC, Attr.ALT, Attr.HEIGHT, Attr.WIDTH
/* 138 */         }), attrs(AttrKind.OBSOLETE, new Attr[] { Attr.NAME
/* 139 */         }), attrs(AttrKind.USE_CSS, new Attr[] { Attr.ALIGN, Attr.HSPACE, Attr.VSPACE, Attr.BORDER })
/*     */     }),
/* 141 */   LI(BlockType.LIST_ITEM, EndKind.OPTIONAL,
/* 142 */     EnumSet.of(Flag.ACCEPTS_BLOCK, Flag.ACCEPTS_INLINE), new AttrMap[] {
/* 143 */       attrs(AttrKind.OK, new Attr[] { Attr.VALUE })
/*     */     }),
/* 145 */   LINK(BlockType.OTHER, EndKind.NONE, new AttrMap[0]),
/*     */
/* 147 */   MENU(BlockType.BLOCK, EndKind.REQUIRED, new AttrMap[0])
/*     */   {
/*     */     public boolean accepts(HtmlTag param1HtmlTag) {
/* 150 */       return (param1HtmlTag == LI);
/*     */     }
/*     */   },
/*     */
/* 154 */   META(BlockType.OTHER, EndKind.NONE, new AttrMap[0]),
/*     */
/* 156 */   NOFRAMES(BlockType.OTHER, EndKind.REQUIRED, new AttrMap[0]),
/*     */
/* 158 */   NOSCRIPT(BlockType.BLOCK, EndKind.REQUIRED, new AttrMap[0]),
/*     */
/* 160 */   OL(BlockType.BLOCK, EndKind.REQUIRED,
/* 161 */     EnumSet.of(Flag.EXPECT_CONTENT), new AttrMap[] {
/* 162 */       attrs(AttrKind.OK, new Attr[] { Attr.START, Attr.TYPE })
/*     */     }) {
/*     */     public boolean accepts(HtmlTag param1HtmlTag) {
/* 165 */       return (param1HtmlTag == LI);
/*     */     }
/*     */   },
/*     */
/* 169 */   P(BlockType.BLOCK, EndKind.OPTIONAL,
/* 170 */     EnumSet.of(Flag.EXPECT_CONTENT), new AttrMap[] {
/* 171 */       attrs(AttrKind.USE_CSS, new Attr[] { Attr.ALIGN })
/*     */     }),
/* 173 */   PRE(BlockType.BLOCK, EndKind.REQUIRED,
/* 174 */     EnumSet.of(Flag.EXPECT_CONTENT), new AttrMap[0])
/*     */   {
/*     */     public boolean accepts(HtmlTag param1HtmlTag) {
/* 177 */       switch (param1HtmlTag) { case BLOCK: case INLINE: case OTHER: case null:
/*     */         case null:
/* 179 */           return false; }
/*     */
/* 181 */       return (param1HtmlTag.blockType == BlockType.INLINE);
/*     */     }
/*     */   },
/*     */
/*     */
/* 186 */   SCRIPT(BlockType.OTHER, EndKind.REQUIRED, new AttrMap[] {
/* 187 */       attrs(AttrKind.OK, new Attr[] { Attr.SRC })
/*     */     }),
/* 189 */   SMALL(BlockType.INLINE, EndKind.REQUIRED,
/* 190 */     EnumSet.of(Flag.EXPECT_CONTENT), new AttrMap[0]),
/*     */
/* 192 */   SPAN(BlockType.INLINE, EndKind.REQUIRED,
/* 193 */     EnumSet.of(Flag.EXPECT_CONTENT), new AttrMap[0]),
/*     */
/* 195 */   STRONG(BlockType.INLINE, EndKind.REQUIRED,
/* 196 */     EnumSet.of(Flag.EXPECT_CONTENT), new AttrMap[0]),
/*     */
/* 198 */   SUB(BlockType.INLINE, EndKind.REQUIRED,
/* 199 */     EnumSet.of(Flag.EXPECT_CONTENT, Flag.NO_NEST), new AttrMap[0]),
/*     */
/* 201 */   SUP(BlockType.INLINE, EndKind.REQUIRED,
/* 202 */     EnumSet.of(Flag.EXPECT_CONTENT, Flag.NO_NEST), new AttrMap[0]),
/*     */
/* 204 */   TABLE(BlockType.BLOCK, EndKind.REQUIRED,
/* 205 */     EnumSet.of(Flag.EXPECT_CONTENT), new AttrMap[] {
/* 206 */       attrs(AttrKind.OK, new Attr[] { Attr.SUMMARY, Attr.FRAME, Attr.RULES, Attr.BORDER, Attr.CELLPADDING, Attr.CELLSPACING, Attr.WIDTH
/*     */
/* 208 */         }), attrs(AttrKind.USE_CSS, new Attr[] { Attr.ALIGN, Attr.BGCOLOR })
/*     */     }) {
/*     */     public boolean accepts(HtmlTag param1HtmlTag) {
/* 211 */       switch (param1HtmlTag) { case null: case null:
/*     */         case null:
/*     */         case null:
/*     */         case null:
/* 215 */           return true; }
/*     */
/* 217 */       return false;
/*     */     }
/*     */   },
/*     */
/*     */
/* 222 */   TBODY(BlockType.TABLE_ITEM, EndKind.REQUIRED,
/* 223 */     EnumSet.of(Flag.EXPECT_CONTENT), new AttrMap[] {
/* 224 */       attrs(AttrKind.OK, new Attr[] { Attr.ALIGN, Attr.CHAR, Attr.CHAROFF, Attr.VALIGN })
/*     */     }) {
/*     */     public boolean accepts(HtmlTag param1HtmlTag) {
/* 227 */       return (param1HtmlTag == TR);
/*     */     }
/*     */   },
/*     */
/* 231 */   TD(BlockType.TABLE_ITEM, EndKind.OPTIONAL,
/* 232 */     EnumSet.of(Flag.ACCEPTS_BLOCK, Flag.ACCEPTS_INLINE), new AttrMap[] {
/* 233 */       attrs(AttrKind.OK, new Attr[] { Attr.COLSPAN, Attr.ROWSPAN, Attr.HEADERS, Attr.SCOPE, Attr.ABBR, Attr.AXIS, Attr.ALIGN, Attr.CHAR, Attr.CHAROFF, Attr.VALIGN
/*     */
/* 235 */         }), attrs(AttrKind.USE_CSS, new Attr[] { Attr.WIDTH, Attr.BGCOLOR, Attr.HEIGHT, Attr.NOWRAP })
/*     */     }),
/* 237 */   TFOOT(BlockType.TABLE_ITEM, EndKind.REQUIRED, new AttrMap[] {
/* 238 */       attrs(AttrKind.OK, new Attr[] { Attr.ALIGN, Attr.CHAR, Attr.CHAROFF, Attr.VALIGN })
/*     */     }) {
/*     */     public boolean accepts(HtmlTag param1HtmlTag) {
/* 241 */       return (param1HtmlTag == TR);
/*     */     }
/*     */   },
/*     */
/* 245 */   TH(BlockType.TABLE_ITEM, EndKind.OPTIONAL,
/* 246 */     EnumSet.of(Flag.ACCEPTS_BLOCK, Flag.ACCEPTS_INLINE), new AttrMap[] {
/* 247 */       attrs(AttrKind.OK, new Attr[] { Attr.COLSPAN, Attr.ROWSPAN, Attr.HEADERS, Attr.SCOPE, Attr.ABBR, Attr.AXIS, Attr.ALIGN, Attr.CHAR, Attr.CHAROFF, Attr.VALIGN
/*     */
/* 249 */         }), attrs(AttrKind.USE_CSS, new Attr[] { Attr.WIDTH, Attr.BGCOLOR, Attr.HEIGHT, Attr.NOWRAP })
/*     */     }),
/* 251 */   THEAD(BlockType.TABLE_ITEM, EndKind.REQUIRED, new AttrMap[] {
/* 252 */       attrs(AttrKind.OK, new Attr[] { Attr.ALIGN, Attr.CHAR, Attr.CHAROFF, Attr.VALIGN })
/*     */     }) {
/*     */     public boolean accepts(HtmlTag param1HtmlTag) {
/* 255 */       return (param1HtmlTag == TR);
/*     */     }
/*     */   },
/*     */
/* 259 */   TITLE(BlockType.OTHER, EndKind.REQUIRED, new AttrMap[0]),
/*     */
/* 261 */   TR(BlockType.TABLE_ITEM, EndKind.OPTIONAL, new AttrMap[] {
/* 262 */       attrs(AttrKind.OK, new Attr[] { Attr.ALIGN, Attr.CHAR, Attr.CHAROFF, Attr.VALIGN
/* 263 */         }), attrs(AttrKind.USE_CSS, new Attr[] { Attr.BGCOLOR })
/*     */     }) {
/*     */     public boolean accepts(HtmlTag param1HtmlTag) {
/* 266 */       return (param1HtmlTag == TH || param1HtmlTag == TD);
/*     */     }
/*     */   },
/*     */
/* 270 */   TT(BlockType.INLINE, EndKind.REQUIRED,
/* 271 */     EnumSet.of(Flag.EXPECT_CONTENT, Flag.NO_NEST), new AttrMap[0]),
/*     */
/* 273 */   U(BlockType.INLINE, EndKind.REQUIRED,
/* 274 */     EnumSet.of(Flag.EXPECT_CONTENT, Flag.NO_NEST), new AttrMap[0]),
/*     */
/* 276 */   UL(BlockType.BLOCK, EndKind.REQUIRED,
/* 277 */     EnumSet.of(Flag.EXPECT_CONTENT), new AttrMap[] {
/* 278 */       attrs(AttrKind.OK, new Attr[] { Attr.COMPACT, Attr.TYPE })
/*     */     }) {
/*     */     public boolean accepts(HtmlTag param1HtmlTag) {
/* 281 */       return (param1HtmlTag == LI);
/*     */     }
/*     */   },
/*     */
/* 285 */   VAR(BlockType.INLINE, EndKind.REQUIRED, new AttrMap[0]);
/*     */   public final BlockType blockType; public final EndKind endKind;
/*     */   public final Set<Flag> flags;
/*     */   private final Map<Attr, AttrKind> attrs;
/*     */   private static final Map<String, HtmlTag> index;
/*     */
/* 291 */   public enum BlockType { BLOCK,
/* 292 */     INLINE,
/* 293 */     LIST_ITEM,
/* 294 */     TABLE_ITEM,
/* 295 */     OTHER; }
/*     */
/*     */
/*     */
/*     */
/*     */   public enum EndKind
/*     */   {
/* 302 */     NONE,
/* 303 */     OPTIONAL,
/* 304 */     REQUIRED;
/*     */   }
/*     */
/*     */   public enum Flag {
/* 308 */     ACCEPTS_BLOCK,
/* 309 */     ACCEPTS_INLINE,
/* 310 */     EXPECT_CONTENT,
/* 311 */     NO_NEST;
/*     */   }
/*     */
/*     */   public enum Attr {
/* 315 */     ABBR,
/* 316 */     ALIGN,
/* 317 */     ALT,
/* 318 */     AXIS,
/* 319 */     BGCOLOR,
/* 320 */     BORDER,
/* 321 */     CELLSPACING,
/* 322 */     CELLPADDING,
/* 323 */     CHAR,
/* 324 */     CHAROFF,
/* 325 */     CLEAR,
/* 326 */     CLASS,
/* 327 */     COLOR,
/* 328 */     COLSPAN,
/* 329 */     COMPACT,
/* 330 */     FACE,
/* 331 */     FRAME,
/* 332 */     HEADERS,
/* 333 */     HEIGHT,
/* 334 */     HREF,
/* 335 */     HSPACE,
/* 336 */     ID,
/* 337 */     NAME,
/* 338 */     NOWRAP,
/* 339 */     REVERSED,
/* 340 */     ROWSPAN,
/* 341 */     RULES,
/* 342 */     SCOPE,
/* 343 */     SIZE,
/* 344 */     SPACE,
/* 345 */     SRC,
/* 346 */     START,
/* 347 */     STYLE,
/* 348 */     SUMMARY,
/* 349 */     TARGET,
/* 350 */     TYPE,
/* 351 */     VALIGN,
/* 352 */     VALUE,
/* 353 */     VSPACE,
/* 354 */     WIDTH;
/*     */
/*     */
/*     */
/*     */
/*     */
/* 360 */     static final Map<String, Attr> index = new HashMap<>();
/*     */     static {
/* 362 */       for (Attr attr : values())
/* 363 */         index.put(attr.getText(), attr);
/*     */     }
/*     */     public String getText() {
/*     */       return StringUtils.toLowerCase(name());
/*     */     } }
/*     */
/* 369 */   public enum AttrKind { INVALID,
/* 370 */     OBSOLETE,
/* 371 */     USE_CSS,
/* 372 */     OK; }
/*     */
/*     */
/*     */   private static class AttrMap
/*     */     extends EnumMap<Attr, AttrKind> {
/*     */     private static final long serialVersionUID = 0L;
/*     */
/*     */     AttrMap() {
/* 380 */       super(Attr.class);
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
/*     */
/*     */
/*     */
/*     */   HtmlTag(BlockType paramBlockType, EndKind paramEndKind, Set<Flag> paramSet, AttrMap... paramVarArgs) {
/* 395 */     this.blockType = paramBlockType;
/* 396 */     this.endKind = paramEndKind;
/* 397 */     this.flags = paramSet;
/* 398 */     this.attrs = new EnumMap<>(Attr.class);
/* 399 */     for (AttrMap attrMap : paramVarArgs)
/* 400 */       this.attrs.putAll(attrMap);
/* 401 */     this.attrs.put(Attr.CLASS, AttrKind.OK);
/* 402 */     this.attrs.put(Attr.ID, AttrKind.OK);
/* 403 */     this.attrs.put(Attr.STYLE, AttrKind.OK);
/*     */   }
/*     */
/*     */   public boolean accepts(HtmlTag paramHtmlTag) {
/* 407 */     if (this.flags.contains(Flag.ACCEPTS_BLOCK) && this.flags.contains(Flag.ACCEPTS_INLINE))
/* 408 */       return (paramHtmlTag.blockType == BlockType.BLOCK || paramHtmlTag.blockType == BlockType.INLINE);
/* 409 */     if (this.flags.contains(Flag.ACCEPTS_BLOCK))
/* 410 */       return (paramHtmlTag.blockType == BlockType.BLOCK);
/* 411 */     if (this.flags.contains(Flag.ACCEPTS_INLINE)) {
/* 412 */       return (paramHtmlTag.blockType == BlockType.INLINE);
/*     */     }
/* 414 */     switch (this.blockType) {
/*     */       case BLOCK:
/*     */       case INLINE:
/* 417 */         return (paramHtmlTag.blockType == BlockType.INLINE);
/*     */
/*     */
/*     */       case OTHER:
/* 421 */         return true;
/*     */     }
/*     */
/*     */
/* 425 */     throw new AssertionError(this + ":" + paramHtmlTag);
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */   public boolean acceptsText() {
/* 432 */     return accepts(B);
/*     */   }
/*     */
/*     */   public String getText() {
/* 436 */     return StringUtils.toLowerCase(name());
/*     */   }
/*     */
/*     */   public Attr getAttr(Name paramName) {
/* 440 */     return Attr.index.get(StringUtils.toLowerCase(paramName.toString()));
/*     */   }
/*     */
/*     */   public AttrKind getAttrKind(Name paramName) {
/* 444 */     AttrKind attrKind = this.attrs.get(getAttr(paramName));
/* 445 */     return (attrKind == null) ? AttrKind.INVALID : attrKind;
/*     */   }
/*     */
/*     */   private static AttrMap attrs(AttrKind paramAttrKind, Attr... paramVarArgs) {
/* 449 */     AttrMap attrMap = new AttrMap();
/* 450 */     for (Attr attr : paramVarArgs) attrMap.put(attr, paramAttrKind);
/* 451 */     return attrMap;
/*     */   }
/*     */   static {
/* 454 */     index = new HashMap<>();
/*     */
/* 456 */     for (HtmlTag htmlTag : values()) {
/* 457 */       index.put(htmlTag.getText(), htmlTag);
/*     */     }
/*     */   }
/*     */
/*     */   static HtmlTag get(Name paramName) {
/* 462 */     return index.get(StringUtils.toLowerCase(paramName.toString()));
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\doclint\HtmlTag.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
