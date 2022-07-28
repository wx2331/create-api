/*     */ package com.sun.tools.corba.se.idl;
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
/*     */ class Token
/*     */ {
/*     */   static final int Any = 0;
/*     */   static final int Attribute = 1;
/*     */   static final int Boolean = 2;
/*     */   static final int Case = 3;
/*     */   static final int Char = 4;
/*     */   static final int Const = 5;
/*     */   static final int Context = 6;
/*     */   static final int Default = 7;
/*     */   static final int Double = 8;
/*     */   static final int Enum = 9;
/*     */   static final int Exception = 10;
/*     */   static final int FALSE = 11;
/*     */   static final int Fixed = 12;
/*     */   static final int Float = 13;
/*     */   static final int In = 14;
/*     */   static final int Inout = 15;
/*     */   static final int Interface = 16;
/*     */   static final int Long = 17;
/*     */   static final int Module = 18;
/*     */   static final int Native = 19;
/*     */   static final int Object = 20;
/*     */   static final int Octet = 21;
/*     */   static final int Oneway = 22;
/*     */   static final int Out = 23;
/*     */   static final int Raises = 24;
/*     */   static final int Readonly = 25;
/*     */   static final int Sequence = 26;
/*     */   static final int Short = 27;
/*     */   static final int String = 28;
/*     */   static final int Struct = 29;
/*     */   static final int Switch = 30;
/*     */   static final int TRUE = 31;
/*     */   static final int Typedef = 32;
/*     */   static final int Unsigned = 33;
/*     */   static final int Union = 34;
/*     */   static final int Void = 35;
/*     */   static final int Wchar = 36;
/*     */   static final int Wstring = 37;
/*     */   static final int Init = 38;
/*     */   static final int Abstract = 39;
/*     */   static final int Custom = 40;
/*     */   static final int Private = 41;
/*     */   static final int Public = 42;
/*     */   static final int Supports = 43;
/*     */   static final int Truncatable = 44;
/*     */   static final int ValueBase = 45;
/*     */   static final int Valuetype = 46;
/*     */   static final int Factory = 47;
/*     */   static final int Component = 48;
/*     */   static final int Consumes = 49;
/*     */   static final int Emits = 50;
/*     */   static final int Finder = 51;
/*     */   static final int GetRaises = 52;
/*     */   static final int Home = 53;
/*     */   static final int Import = 54;
/*     */   static final int Local = 55;
/*     */   static final int Manages = 56;
/*     */   static final int Multiple = 57;
/*     */   static final int PrimaryKey = 58;
/*     */   static final int Provides = 59;
/*     */   static final int Publishes = 60;
/*     */   static final int SetRaises = 61;
/*     */   static final int TypeId = 62;
/*     */   static final int TypePrefix = 63;
/*     */   static final int Uses = 64;
/*     */   static final int Identifier = 80;
/*     */   static final int MacroIdentifier = 81;
/*     */   static final int Semicolon = 100;
/*     */   static final int LeftBrace = 101;
/*     */   static final int RightBrace = 102;
/*     */   static final int Colon = 103;
/*     */   static final int Comma = 104;
/*     */   static final int Equal = 105;
/*     */   static final int Plus = 106;
/*     */   static final int Minus = 107;
/*     */   static final int LeftParen = 108;
/*     */   static final int RightParen = 109;
/*     */   static final int LessThan = 110;
/*     */   static final int GreaterThan = 111;
/*     */   static final int LeftBracket = 112;
/*     */   static final int RightBracket = 113;
/*     */   static final int Apostrophe = 114;
/*     */   static final int Quote = 115;
/*     */   static final int Backslash = 116;
/*     */   static final int Bar = 117;
/*     */   static final int Carat = 118;
/*     */   static final int Ampersand = 119;
/*     */   static final int Star = 120;
/*     */   static final int Slash = 121;
/*     */   static final int Percent = 122;
/*     */   static final int Tilde = 123;
/*     */   static final int DoubleColon = 124;
/*     */   static final int ShiftLeft = 125;
/*     */   static final int ShiftRight = 126;
/*     */   static final int Period = 127;
/*     */   static final int Hash = 128;
/*     */   static final int Exclamation = 129;
/*     */   static final int DoubleEqual = 130;
/*     */   static final int NotEqual = 131;
/*     */   static final int GreaterEqual = 132;
/*     */   static final int LessEqual = 133;
/*     */   static final int DoubleBar = 134;
/*     */   static final int DoubleAmpersand = 135;
/*     */   static final int BooleanLiteral = 200;
/*     */   static final int CharacterLiteral = 201;
/*     */   static final int IntegerLiteral = 202;
/*     */   static final int FloatingPointLiteral = 203;
/*     */   static final int StringLiteral = 204;
/*     */   static final int Literal = 205;
/*     */   static final int Define = 300;
/*     */   static final int Undef = 301;
/*     */   static final int If = 302;
/*     */   static final int Ifdef = 303;
/*     */   static final int Ifndef = 304;
/*     */   static final int Else = 305;
/*     */   static final int Elif = 306;
/*     */   static final int Include = 307;
/*     */   static final int Endif = 308;
/*     */   static final int Line = 309;
/*     */   static final int Error = 310;
/*     */   static final int Pragma = 311;
/*     */   static final int Null = 312;
/*     */   static final int Unknown = 313;
/*     */   static final int Defined = 400;
/*     */   static final int EOF = 999;
/* 214 */   static final String[] Keywords = new String[] { "any", "attribute", "boolean", "case", "char", "const", "context", "default", "double", "enum", "exception", "FALSE", "fixed", "float", "in", "inout", "interface", "long", "module", "native", "Object", "octet", "oneway", "out", "raises", "readonly", "sequence", "short", "string", "struct", "switch", "TRUE", "typedef", "unsigned", "union", "void", "wchar", "wstring", "init", "abstract", "custom", "private", "public", "supports", "truncatable", "ValueBase", "valuetype", "factory", "component", "consumes", "emits", "finder", "getRaises", "home", "import", "local", "manages", "multiple", "primaryKey", "provides", "publishes", "setRaises", "supports", "typeId", "typePrefix", "uses" };
/*     */ 
/*     */   
/*     */   private static final int FirstKeyword = 0;
/*     */ 
/*     */   
/*     */   private static final int LastKeyword = 64;
/*     */ 
/*     */   
/*     */   private static final int First22Keyword = 0;
/*     */ 
/*     */   
/*     */   private static final int Last22Keyword = 37;
/*     */ 
/*     */   
/*     */   private static final int First23Keyword = 38;
/*     */ 
/*     */   
/*     */   private static final int Last23Keyword = 46;
/*     */   
/*     */   private static final int First24rtfKeyword = 39;
/*     */   
/*     */   private static final int Last24rtfKeyword = 47;
/*     */   
/*     */   private static final int First30Keyword = 48;
/*     */   
/*     */   private static final int Last30Keyword = 64;
/*     */   
/*     */   private static final int CORBA_LEVEL_22 = 0;
/*     */   
/*     */   private static final int CORBA_LEVEL_23 = 1;
/*     */   
/*     */   private static final int CORBA_LEVEL_24RTF = 2;
/*     */   
/*     */   private static final int CORBA_LEVEL_30 = 3;
/*     */   
/*     */   static final int FirstSymbol = 100;
/*     */   
/*     */   static final int LastSymbol = 199;
/*     */ 
/*     */   
/*     */   boolean isKeyword() {
/* 256 */     return (this.type >= 0 && this.type <= 64);
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
/*     */   private static int getLevel(float paramFloat) {
/* 300 */     if (paramFloat < 2.3F)
/* 301 */       return 0; 
/* 302 */     if (Util.absDelta(paramFloat, 2.3F) < 0.001F)
/* 303 */       return 1; 
/* 304 */     if (paramFloat < 3.0F)
/* 305 */       return 2; 
/* 306 */     return 3;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static int getLastKeyword(int paramInt) {
/* 312 */     if (paramInt == 0)
/* 313 */       return 37; 
/* 314 */     if (paramInt == 1)
/* 315 */       return 46; 
/* 316 */     if (paramInt == 2)
/* 317 */       return 47; 
/* 318 */     return 64;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Token makeKeywordToken(String paramString, float paramFloat, boolean paramBoolean, boolean[] paramArrayOfboolean) {
/* 340 */     int i = getLevel(paramFloat);
/* 341 */     int j = getLastKeyword(i);
/* 342 */     boolean bool = false;
/* 343 */     paramArrayOfboolean[0] = false;
/*     */ 
/*     */     
/* 346 */     for (byte b = 0; b <= 64; b++) {
/* 347 */       if (paramString.equals(Keywords[b])) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 355 */         if (b == 38) {
/* 356 */           if (i == 1) {
/* 357 */             bool = true;
/*     */           } else {
/*     */             break;
/*     */           } 
/*     */         }
/* 362 */         if (b > j) {
/* 363 */           paramArrayOfboolean[0] = paramArrayOfboolean[0] | paramBoolean;
/*     */           
/*     */           break;
/*     */         } 
/* 367 */         if (paramString.equals("TRUE") || paramString.equals("FALSE")) {
/* 368 */           return new Token(200, paramString);
/*     */         }
/* 370 */         return new Token(b, bool);
/* 371 */       }  if (paramString.equalsIgnoreCase(Keywords[b])) {
/*     */ 
/*     */ 
/*     */         
/* 375 */         paramArrayOfboolean[0] = paramArrayOfboolean[0] | true;
/*     */         
/*     */         break;
/*     */       } 
/*     */     } 
/* 380 */     return null;
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
/* 391 */   static final String[] Symbols = new String[] { ";", "{", "}", ":", ",", "=", "+", "-", "(", ")", "<", ">", "[", "]", "'", "\"", "\\", "|", "^", "&", "*", "/", "%", "~", "::", "<<", ">>", ".", "#", "!", "==", "!=", ">=", "<=", "||", "&&" };
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static final int FirstLiteral = 200;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static final int LastLiteral = 299;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 406 */   static final String[] Literals = new String[] {
/* 407 */       Util.getMessage("Token.boolLit"), 
/* 408 */       Util.getMessage("Token.charLit"), 
/* 409 */       Util.getMessage("Token.intLit"), 
/* 410 */       Util.getMessage("Token.floatLit"), 
/* 411 */       Util.getMessage("Token.stringLit"), 
/* 412 */       Util.getMessage("Token.literal")
/*     */     };
/*     */ 
/*     */   
/*     */   static final int FirstDirective = 300;
/*     */ 
/*     */   
/*     */   static final int LastDirective = 399;
/*     */ 
/*     */ 
/*     */   
/*     */   boolean isDirective() {
/* 424 */     return (this.type >= 300 && this.type <= 399);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 431 */   static final String[] Directives = new String[] { "define", "undef", "if", "ifdef", "ifndef", "else", "elif", "include", "endif", "line", "error", "pragma", "" };
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static final int FirstSpecial = 400;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static final int LastSpecial = 499;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 446 */   static final String[] Special = new String[] { "defined" };
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
/*     */   int type;
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
/*     */   String name;
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
/*     */   Comment comment;
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
/*     */   boolean isEscaped;
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
/*     */   boolean collidesWithKeyword;
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
/*     */   boolean isDeprecated;
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
/*     */   boolean isWide;
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
/*     */   Token(int paramInt) {
/* 663 */     this.name = null;
/*     */ 
/*     */ 
/*     */     
/* 667 */     this.comment = null;
/*     */ 
/*     */ 
/*     */     
/* 671 */     this.isEscaped = false;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 676 */     this.collidesWithKeyword = false;
/*     */ 
/*     */ 
/*     */     
/* 680 */     this.isDeprecated = false;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 687 */     this.isWide = false; this.type = paramInt; } Token(int paramInt, boolean paramBoolean) { this.name = null; this.comment = null; this.isEscaped = false; this.collidesWithKeyword = false; this.isDeprecated = false; this.isWide = false; this.type = paramInt; this.isDeprecated = paramBoolean; } Token(int paramInt, String paramString) { this.name = null; this.comment = null; this.isEscaped = false; this.collidesWithKeyword = false; this.isDeprecated = false; this.isWide = false;
/*     */     this.type = paramInt;
/*     */     this.name = paramString; }
/*     */ 
/*     */   
/*     */   Token(int paramInt, String paramString, boolean paramBoolean) {
/*     */     this(paramInt, paramString);
/*     */     this.isWide = paramBoolean;
/*     */   }
/*     */   
/*     */   Token(int paramInt, String paramString, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3) {
/*     */     this(paramInt, paramString);
/*     */     this.isEscaped = paramBoolean1;
/*     */     this.collidesWithKeyword = paramBoolean2;
/*     */     this.isDeprecated = paramBoolean3;
/*     */   }
/*     */   
/*     */   public String toString() {
/*     */     if (this.type == 80)
/*     */       return this.name; 
/*     */     if (this.type == 81)
/*     */       return this.name + '('; 
/*     */     return toString(this.type);
/*     */   }
/*     */   
/*     */   static String toString(int paramInt) {
/*     */     if (paramInt <= 64)
/*     */       return Keywords[paramInt]; 
/*     */     if (paramInt == 80 || paramInt == 81)
/*     */       return Util.getMessage("Token.identifier"); 
/*     */     if (paramInt <= 199)
/*     */       return Symbols[paramInt - 100]; 
/*     */     if (paramInt <= 299)
/*     */       return Literals[paramInt - 200]; 
/*     */     if (paramInt <= 399)
/*     */       return Directives[paramInt - 300]; 
/*     */     if (paramInt <= 499)
/*     */       return Special[paramInt - 400]; 
/*     */     if (paramInt == 999)
/*     */       return Util.getMessage("Token.endOfFile"); 
/*     */     return Util.getMessage("Token.unknown");
/*     */   }
/*     */   
/*     */   boolean equals(Token paramToken) {
/*     */     if (this.type == paramToken.type) {
/*     */       if (this.name == null)
/*     */         return (paramToken.name == null); 
/*     */       return this.name.equals(paramToken.name);
/*     */     } 
/*     */     return false;
/*     */   }
/*     */   
/*     */   boolean equals(int paramInt) {
/*     */     return (this.type == paramInt);
/*     */   }
/*     */   
/*     */   boolean equals(String paramString) {
/*     */     return (this.type == 80 && this.name.equals(paramString));
/*     */   }
/*     */   
/*     */   public boolean isEscaped() {
/*     */     return (this.type == 80 && this.isEscaped);
/*     */   }
/*     */   
/*     */   public boolean collidesWithKeyword() {
/*     */     return this.collidesWithKeyword;
/*     */   }
/*     */   
/*     */   public boolean isDeprecated() {
/*     */     return this.isDeprecated;
/*     */   }
/*     */   
/*     */   public boolean isWide() {
/*     */     return this.isWide;
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\corba\se\idl\Token.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */