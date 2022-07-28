/*     */ package com.sun.tools.javac.parser;
/*     */
/*     */ import com.sun.tools.javac.api.Formattable;
/*     */ import com.sun.tools.javac.api.Messages;
/*     */ import com.sun.tools.javac.util.Context;
/*     */ import com.sun.tools.javac.util.Filter;
/*     */ import com.sun.tools.javac.util.List;
/*     */ import com.sun.tools.javac.util.ListBuffer;
/*     */ import com.sun.tools.javac.util.Name;
/*     */ import com.sun.tools.javac.util.Names;
/*     */ import java.util.Locale;
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
/*     */ public class Tokens
/*     */ {
/*     */   private final Names names;
/*     */   private final TokenKind[] key;
/*  59 */   private int maxKey = 0;
/*     */
/*     */
/*     */
/*  63 */   private Name[] tokenName = new Name[(TokenKind.values()).length];
/*     */
/*  65 */   public static final Context.Key<Tokens> tokensKey = new Context.Key();
/*     */
/*     */
/*     */   public static Tokens instance(Context paramContext) {
/*  69 */     Tokens tokens = (Tokens)paramContext.get(tokensKey);
/*  70 */     if (tokens == null)
/*  71 */       tokens = new Tokens(paramContext);
/*  72 */     return tokens;
/*     */   }
/*     */
/*     */   protected Tokens(Context paramContext) {
/*  76 */     paramContext.put(tokensKey, this);
/*  77 */     this.names = Names.instance(paramContext);
/*  78 */     for (TokenKind tokenKind : TokenKind.values()) {
/*  79 */       if (tokenKind.name != null) {
/*  80 */         enterKeyword(tokenKind.name, tokenKind);
/*     */       } else {
/*  82 */         this.tokenName[tokenKind.ordinal()] = null;
/*     */       }
/*     */     }
/*  85 */     this.key = new TokenKind[this.maxKey + 1];
/*  86 */     for (byte b = 0; b <= this.maxKey; ) { this.key[b] = TokenKind.IDENTIFIER; b++; }
/*  87 */      for (TokenKind tokenKind : TokenKind.values()) {
/*  88 */       if (tokenKind.name != null)
/*  89 */         this.key[this.tokenName[tokenKind.ordinal()].getIndex()] = tokenKind;
/*     */     }
/*     */   }
/*     */
/*     */   private void enterKeyword(String paramString, TokenKind paramTokenKind) {
/*  94 */     Name name = this.names.fromString(paramString);
/*  95 */     this.tokenName[paramTokenKind.ordinal()] = name;
/*  96 */     if (name.getIndex() > this.maxKey) this.maxKey = name.getIndex();
/*     */
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   TokenKind lookupKind(Name paramName) {
/* 105 */     return (paramName.getIndex() > this.maxKey) ? TokenKind.IDENTIFIER : this.key[paramName.getIndex()];
/*     */   }
/*     */
/*     */   TokenKind lookupKind(String paramString) {
/* 109 */     return lookupKind(this.names.fromString(paramString));
/*     */   }
/*     */
/*     */
/*     */
/*     */   public enum TokenKind
/*     */     implements Formattable, Filter<TokenKind>
/*     */   {
/* 117 */     EOF,
/* 118 */     ERROR,
/* 119 */     IDENTIFIER((String) Token.Tag.NAMED),
/* 120 */     ABSTRACT("abstract"),
/* 121 */     ASSERT("assert", Token.Tag.NAMED),
/* 122 */     BOOLEAN("boolean", Token.Tag.NAMED),
/* 123 */     BREAK("break"),
/* 124 */     BYTE("byte", Token.Tag.NAMED),
/* 125 */     CASE("case"),
/* 126 */     CATCH("catch"),
/* 127 */     CHAR("char", Token.Tag.NAMED),
/* 128 */     CLASS("class"),
/* 129 */     CONST("const"),
/* 130 */     CONTINUE("continue"),
/* 131 */     DEFAULT("default"),
/* 132 */     DO("do"),
/* 133 */     DOUBLE("double", Token.Tag.NAMED),
/* 134 */     ELSE("else"),
/* 135 */     ENUM("enum", Token.Tag.NAMED),
/* 136 */     EXTENDS("extends"),
/* 137 */     FINAL("final"),
/* 138 */     FINALLY("finally"),
/* 139 */     FLOAT("float", Token.Tag.NAMED),
/* 140 */     FOR("for"),
/* 141 */     GOTO("goto"),
/* 142 */     IF("if"),
/* 143 */     IMPLEMENTS("implements"),
/* 144 */     IMPORT("import"),
/* 145 */     INSTANCEOF("instanceof"),
/* 146 */     INT("int", Token.Tag.NAMED),
/* 147 */     INTERFACE("interface"),
/* 148 */     LONG("long", Token.Tag.NAMED),
/* 149 */     NATIVE("native"),
/* 150 */     NEW("new"),
/* 151 */     PACKAGE("package"),
/* 152 */     PRIVATE("private"),
/* 153 */     PROTECTED("protected"),
/* 154 */     PUBLIC("public"),
/* 155 */     RETURN("return"),
/* 156 */     SHORT("short", Token.Tag.NAMED),
/* 157 */     STATIC("static"),
/* 158 */     STRICTFP("strictfp"),
/* 159 */     SUPER("super", Token.Tag.NAMED),
/* 160 */     SWITCH("switch"),
/* 161 */     SYNCHRONIZED("synchronized"),
/* 162 */     THIS("this", Token.Tag.NAMED),
/* 163 */     THROW("throw"),
/* 164 */     THROWS("throws"),
/* 165 */     TRANSIENT("transient"),
/* 166 */     TRY("try"),
/* 167 */     VOID("void", Token.Tag.NAMED),
/* 168 */     VOLATILE("volatile"),
/* 169 */     WHILE("while"),
/* 170 */     INTLITERAL((String) Token.Tag.NUMERIC),
/* 171 */     LONGLITERAL((String) Token.Tag.NUMERIC),
/* 172 */     FLOATLITERAL((String) Token.Tag.NUMERIC),
/* 173 */     DOUBLELITERAL((String) Token.Tag.NUMERIC),
/* 174 */     CHARLITERAL((String) Token.Tag.NUMERIC),
/* 175 */     STRINGLITERAL((String) Token.Tag.STRING),
/* 176 */     TRUE("true", Token.Tag.NAMED),
/* 177 */     FALSE("false", Token.Tag.NAMED),
/* 178 */     NULL("null", Token.Tag.NAMED),
/* 179 */     UNDERSCORE("_", Token.Tag.NAMED),
/* 180 */     ARROW("->"),
/* 181 */     COLCOL("::"),
/* 182 */     LPAREN("("),
/* 183 */     RPAREN(")"),
/* 184 */     LBRACE("{"),
/* 185 */     RBRACE("}"),
/* 186 */     LBRACKET("["),
/* 187 */     RBRACKET("]"),
/* 188 */     SEMI(";"),
/* 189 */     COMMA(","),
/* 190 */     DOT("."),
/* 191 */     ELLIPSIS("..."),
/* 192 */     EQ("="),
/* 193 */     GT(">"),
/* 194 */     LT("<"),
/* 195 */     BANG("!"),
/* 196 */     TILDE("~"),
/* 197 */     QUES("?"),
/* 198 */     COLON(":"),
/* 199 */     EQEQ("=="),
/* 200 */     LTEQ("<="),
/* 201 */     GTEQ(">="),
/* 202 */     BANGEQ("!="),
/* 203 */     AMPAMP("&&"),
/* 204 */     BARBAR("||"),
/* 205 */     PLUSPLUS("++"),
/* 206 */     SUBSUB("--"),
/* 207 */     PLUS("+"),
/* 208 */     SUB("-"),
/* 209 */     STAR("*"),
/* 210 */     SLASH("/"),
/* 211 */     AMP("&"),
/* 212 */     BAR("|"),
/* 213 */     CARET("^"),
/* 214 */     PERCENT("%"),
/* 215 */     LTLT("<<"),
/* 216 */     GTGT(">>"),
/* 217 */     GTGTGT(">>>"),
/* 218 */     PLUSEQ("+="),
/* 219 */     SUBEQ("-="),
/* 220 */     STAREQ("*="),
/* 221 */     SLASHEQ("/="),
/* 222 */     AMPEQ("&="),
/* 223 */     BAREQ("|="),
/* 224 */     CARETEQ("^="),
/* 225 */     PERCENTEQ("%="),
/* 226 */     LTLTEQ("<<="),
/* 227 */     GTGTEQ(">>="),
/* 228 */     GTGTGTEQ(">>>="),
/* 229 */     MONKEYS_AT("@"),
/* 230 */     CUSTOM;
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */     public final String name;
/*     */
/*     */
/*     */
/*     */
/*     */     public final Token.Tag tag;
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */     TokenKind(String param1String1, Token.Tag param1Tag) {
/* 248 */       this.name = param1String1;
/* 249 */       this.tag = param1Tag;
/*     */     }
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
/*     */     public String toString() {
/*     */       // Byte code:
/*     */       //   0: getstatic com/sun/tools/javac/parser/Tokens$1.$SwitchMap$com$sun$tools$javac$parser$Tokens$TokenKind : [I
/*     */       //   3: aload_0
/*     */       //   4: invokevirtual ordinal : ()I
/*     */       //   7: iaload
/*     */       //   8: tableswitch default -> 151, 1 -> 96, 2 -> 99, 3 -> 102, 4 -> 105, 5 -> 108, 6 -> 111, 7 -> 114, 8 -> 117, 9 -> 120, 10 -> 123, 11 -> 123, 12 -> 123, 13 -> 123, 14 -> 123, 15 -> 123, 16 -> 123, 17 -> 123, 18 -> 123
/*     */       //   96: ldc 'token.identifier'
/*     */       //   98: areturn
/*     */       //   99: ldc 'token.character'
/*     */       //   101: areturn
/*     */       //   102: ldc 'token.string'
/*     */       //   104: areturn
/*     */       //   105: ldc 'token.integer'
/*     */       //   107: areturn
/*     */       //   108: ldc 'token.long-integer'
/*     */       //   110: areturn
/*     */       //   111: ldc 'token.float'
/*     */       //   113: areturn
/*     */       //   114: ldc 'token.double'
/*     */       //   116: areturn
/*     */       //   117: ldc 'token.bad-symbol'
/*     */       //   119: areturn
/*     */       //   120: ldc 'token.end-of-input'
/*     */       //   122: areturn
/*     */       //   123: new java/lang/StringBuilder
/*     */       //   126: dup
/*     */       //   127: invokespecial <init> : ()V
/*     */       //   130: ldc '''
/*     */       //   132: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */       //   135: aload_0
/*     */       //   136: getfield name : Ljava/lang/String;
/*     */       //   139: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */       //   142: ldc '''
/*     */       //   144: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */       //   147: invokevirtual toString : ()Ljava/lang/String;
/*     */       //   150: areturn
/*     */       //   151: aload_0
/*     */       //   152: getfield name : Ljava/lang/String;
/*     */       //   155: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #253	-> 0
/*     */       //   #255	-> 96
/*     */       //   #257	-> 99
/*     */       //   #259	-> 102
/*     */       //   #261	-> 105
/*     */       //   #263	-> 108
/*     */       //   #265	-> 111
/*     */       //   #267	-> 114
/*     */       //   #269	-> 117
/*     */       //   #271	-> 120
/*     */       //   #274	-> 123
/*     */       //   #276	-> 151
/*     */     }
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
/*     */     public String getKind() {
/* 281 */       return "Token";
/*     */     }
/*     */
/*     */     public String toString(Locale param1Locale, Messages param1Messages) {
/* 285 */       return (this.name != null) ? toString() : param1Messages.getLocalizedString(param1Locale, "compiler.misc." + toString(), new Object[0]);
/*     */     }
/*     */
/*     */
/*     */     public boolean accepts(TokenKind param1TokenKind) {
/* 290 */       return (this == param1TokenKind);
/*     */     } }
/*     */   public static interface Comment { String getText();
/*     */     int getSourcePos(int param1Int);
/*     */     CommentStyle getStyle();
/*     */     boolean isDeprecated();
/*     */
/* 297 */     public enum CommentStyle { LINE,
/* 298 */       BLOCK,
/* 299 */       JAVADOC; }
/*     */      }
/*     */
/*     */
/*     */
/*     */   public static class Token
/*     */   {
/*     */     public final TokenKind kind;
/*     */
/*     */     public final int pos;
/*     */
/*     */     public final int endPos;
/*     */
/*     */     public final List<Comment> comments;
/*     */
/*     */     enum Tag
/*     */     {
/* 316 */       DEFAULT,
/* 317 */       NAMED,
/* 318 */       STRING,
/* 319 */       NUMERIC;
/*     */     }
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
/*     */     Token(TokenKind param1TokenKind, int param1Int1, int param1Int2, List<Comment> param1List) {
/* 335 */       this.kind = param1TokenKind;
/* 336 */       this.pos = param1Int1;
/* 337 */       this.endPos = param1Int2;
/* 338 */       this.comments = param1List;
/* 339 */       checkKind();
/*     */     }
/*     */
/*     */     Token[] split(Tokens param1Tokens) {
/* 343 */       if (this.kind.name.length() < 2 || this.kind.tag != Tag.DEFAULT) {
/* 344 */         throw new AssertionError("Cant split" + this.kind);
/*     */       }
/*     */
/* 347 */       TokenKind tokenKind1 = param1Tokens.lookupKind(this.kind.name.substring(0, 1));
/* 348 */       TokenKind tokenKind2 = param1Tokens.lookupKind(this.kind.name.substring(1));
/*     */
/* 350 */       if (tokenKind1 == null || tokenKind2 == null) {
/* 351 */         throw new AssertionError("Cant split - bad subtokens");
/*     */       }
/* 353 */       return new Token[] { new Token(tokenKind1, this.pos, this.pos + tokenKind1.name
/* 354 */             .length(), this.comments), new Token(tokenKind2, this.pos + tokenKind1.name
/* 355 */             .length(), this.endPos, null) };
/*     */     }
/*     */
/*     */
/*     */     protected void checkKind() {
/* 360 */       if (this.kind.tag != Tag.DEFAULT) {
/* 361 */         throw new AssertionError("Bad token kind - expected " + Tag.STRING);
/*     */       }
/*     */     }
/*     */
/*     */     public Name name() {
/* 366 */       throw new UnsupportedOperationException();
/*     */     }
/*     */
/*     */     public String stringVal() {
/* 370 */       throw new UnsupportedOperationException();
/*     */     }
/*     */
/*     */     public int radix() {
/* 374 */       throw new UnsupportedOperationException();
/*     */     }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */     public Comment comment(Comment.CommentStyle param1CommentStyle) {
/* 382 */       List<Comment> list = getComments(Comment.CommentStyle.JAVADOC);
/* 383 */       return list.isEmpty() ? null : (Comment)list.head;
/*     */     }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */     public boolean deprecatedFlag() {
/* 393 */       for (Comment comment : getComments(Comment.CommentStyle.JAVADOC)) {
/* 394 */         if (comment.isDeprecated()) {
/* 395 */           return true;
/*     */         }
/*     */       }
/* 398 */       return false;
/*     */     }
/*     */
/*     */     private List<Comment> getComments(Comment.CommentStyle param1CommentStyle) {
/* 402 */       if (this.comments == null) {
/* 403 */         return List.nil();
/*     */       }
/* 405 */       ListBuffer listBuffer = new ListBuffer();
/* 406 */       for (Comment comment : this.comments) {
/* 407 */         if (comment.getStyle() == param1CommentStyle) {
/* 408 */           listBuffer.add(comment);
/*     */         }
/*     */       }
/* 411 */       return listBuffer.toList();
/*     */     }
/*     */   }
/*     */
/*     */   static final class NamedToken
/*     */     extends Token
/*     */   {
/*     */     public final Name name;
/*     */
/*     */     public NamedToken(TokenKind param1TokenKind, int param1Int1, int param1Int2, Name param1Name, List<Comment> param1List) {
/* 421 */       super(param1TokenKind, param1Int1, param1Int2, param1List);
/* 422 */       this.name = param1Name;
/*     */     }
/*     */
/*     */     protected void checkKind() {
/* 426 */       if (this.kind.tag != Tag.NAMED) {
/* 427 */         throw new AssertionError("Bad token kind - expected " + Tag.NAMED);
/*     */       }
/*     */     }
/*     */
/*     */
/*     */     public Name name() {
/* 433 */       return this.name;
/*     */     }
/*     */   }
/*     */
/*     */   static class StringToken
/*     */     extends Token {
/*     */     public final String stringVal;
/*     */
/*     */     public StringToken(TokenKind param1TokenKind, int param1Int1, int param1Int2, String param1String, List<Comment> param1List) {
/* 442 */       super(param1TokenKind, param1Int1, param1Int2, param1List);
/* 443 */       this.stringVal = param1String;
/*     */     }
/*     */
/*     */     protected void checkKind() {
/* 447 */       if (this.kind.tag != Tag.STRING) {
/* 448 */         throw new AssertionError("Bad token kind - expected " + Tag.STRING);
/*     */       }
/*     */     }
/*     */
/*     */
/*     */     public String stringVal() {
/* 454 */       return this.stringVal;
/*     */     }
/*     */   }
/*     */
/*     */   static final class NumericToken
/*     */     extends StringToken {
/*     */     public final int radix;
/*     */
/*     */     public NumericToken(TokenKind param1TokenKind, int param1Int1, int param1Int2, String param1String, int param1Int3, List<Comment> param1List) {
/* 463 */       super(param1TokenKind, param1Int1, param1Int2, param1String, param1List);
/* 464 */       this.radix = param1Int3;
/*     */     }
/*     */
/*     */     protected void checkKind() {
/* 468 */       if (this.kind.tag != Tag.NUMERIC) {
/* 469 */         throw new AssertionError("Bad token kind - expected " + Tag.NUMERIC);
/*     */       }
/*     */     }
/*     */
/*     */
/*     */     public int radix() {
/* 475 */       return this.radix;
/*     */     }
/*     */   }
/*     */
/* 479 */   public static final Token DUMMY = new Token(TokenKind.ERROR, 0, 0, null);
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\javac\parser\Tokens.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
