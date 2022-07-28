/*     */ package com.sun.tools.example.debug.expr;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public interface ExpressionParserConstants
/*     */ {
/*     */   public static final int EOF = 0;
/*     */   public static final int SINGLE_LINE_COMMENT = 6;
/*     */   public static final int FORMAL_COMMENT = 7;
/*     */   public static final int MULTI_LINE_COMMENT = 8;
/*     */   public static final int ABSTRACT = 9;
/*     */   public static final int BOOLEAN = 10;
/*     */   public static final int BREAK = 11;
/*     */   public static final int BYTE = 12;
/*     */   public static final int CASE = 13;
/*     */   public static final int CATCH = 14;
/*     */   public static final int CHAR = 15;
/*     */   public static final int CLASS = 16;
/*     */   public static final int CONST = 17;
/*     */   public static final int CONTINUE = 18;
/*     */   public static final int _DEFAULT = 19;
/*     */   public static final int DO = 20;
/*     */   public static final int DOUBLE = 21;
/*     */   public static final int ELSE = 22;
/*     */   public static final int EXTENDS = 23;
/*     */   public static final int FALSE = 24;
/*     */   public static final int FINAL = 25;
/*     */   public static final int FINALLY = 26;
/*     */   public static final int FLOAT = 27;
/*     */   public static final int FOR = 28;
/*     */   public static final int GOTO = 29;
/*     */   public static final int IF = 30;
/*     */   public static final int IMPLEMENTS = 31;
/*     */   public static final int IMPORT = 32;
/*     */   public static final int INSTANCEOF = 33;
/*     */   public static final int INT = 34;
/*     */   public static final int INTERFACE = 35;
/*     */   public static final int LONG = 36;
/*     */   public static final int NATIVE = 37;
/*     */   public static final int NEW = 38;
/*     */   public static final int NULL = 39;
/*     */   public static final int PACKAGE = 40;
/*     */   public static final int PRIVATE = 41;
/*     */   public static final int PROTECTED = 42;
/*     */   public static final int PUBLIC = 43;
/*     */   public static final int RETURN = 44;
/*     */   public static final int SHORT = 45;
/*     */   public static final int STATIC = 46;
/*     */   public static final int SUPER = 47;
/*     */   public static final int SWITCH = 48;
/*     */   public static final int SYNCHRONIZED = 49;
/*     */   public static final int THIS = 50;
/*     */   public static final int THROW = 51;
/*     */   public static final int THROWS = 52;
/*     */   public static final int TRANSIENT = 53;
/*     */   public static final int TRUE = 54;
/*     */   public static final int TRY = 55;
/*     */   public static final int VOID = 56;
/*     */   public static final int VOLATILE = 57;
/*     */   public static final int WHILE = 58;
/*     */   public static final int INTEGER_LITERAL = 59;
/*     */   public static final int DECIMAL_LITERAL = 60;
/*     */   public static final int HEX_LITERAL = 61;
/*     */   public static final int OCTAL_LITERAL = 62;
/*     */   public static final int FLOATING_POINT_LITERAL = 63;
/*     */   public static final int EXPONENT = 64;
/*     */   public static final int CHARACTER_LITERAL = 65;
/*     */   public static final int STRING_LITERAL = 66;
/*     */   public static final int IDENTIFIER = 67;
/*     */   public static final int LETTER = 68;
/*     */   public static final int DIGIT = 69;
/*     */   public static final int LPAREN = 70;
/*     */   public static final int RPAREN = 71;
/*     */   public static final int LBRACE = 72;
/*     */   public static final int RBRACE = 73;
/*     */   public static final int LBRACKET = 74;
/*     */   public static final int RBRACKET = 75;
/*     */   public static final int SEMICOLON = 76;
/*     */   public static final int COMMA = 77;
/*     */   public static final int DOT = 78;
/*     */   public static final int ASSIGN = 79;
/*     */   public static final int GT = 80;
/*     */   public static final int LT = 81;
/*     */   public static final int BANG = 82;
/*     */   public static final int TILDE = 83;
/*     */   public static final int HOOK = 84;
/*     */   public static final int COLON = 85;
/*     */   public static final int EQ = 86;
/*     */   public static final int LE = 87;
/*     */   public static final int GE = 88;
/*     */   public static final int NE = 89;
/*     */   public static final int SC_OR = 90;
/*     */   public static final int SC_AND = 91;
/*     */   public static final int INCR = 92;
/*     */   public static final int DECR = 93;
/*     */   public static final int PLUS = 94;
/*     */   public static final int MINUS = 95;
/*     */   public static final int STAR = 96;
/*     */   public static final int SLASH = 97;
/*     */   public static final int BIT_AND = 98;
/*     */   public static final int BIT_OR = 99;
/*     */   public static final int XOR = 100;
/*     */   public static final int REM = 101;
/*     */   public static final int LSHIFT = 102;
/*     */   public static final int RSIGNEDSHIFT = 103;
/*     */   public static final int RUNSIGNEDSHIFT = 104;
/*     */   public static final int PLUSASSIGN = 105;
/*     */   public static final int MINUSASSIGN = 106;
/*     */   public static final int STARASSIGN = 107;
/*     */   public static final int SLASHASSIGN = 108;
/*     */   public static final int ANDASSIGN = 109;
/*     */   public static final int ORASSIGN = 110;
/*     */   public static final int XORASSIGN = 111;
/*     */   public static final int REMASSIGN = 112;
/*     */   public static final int LSHIFTASSIGN = 113;
/*     */   public static final int RSIGNEDSHIFTASSIGN = 114;
/*     */   public static final int RUNSIGNEDSHIFTASSIGN = 115;
/*     */   public static final int DEFAULT = 0;
/* 154 */   public static final String[] tokenImage = new String[] { "<EOF>", "\" \"", "\"\\t\"", "\"\\n\"", "\"\\r\"", "\"\\f\"", "<SINGLE_LINE_COMMENT>", "<FORMAL_COMMENT>", "<MULTI_LINE_COMMENT>", "\"abstract\"", "\"boolean\"", "\"break\"", "\"byte\"", "\"case\"", "\"catch\"", "\"char\"", "\"class\"", "\"const\"", "\"continue\"", "\"default\"", "\"do\"", "\"double\"", "\"else\"", "\"extends\"", "\"false\"", "\"final\"", "\"finally\"", "\"float\"", "\"for\"", "\"goto\"", "\"if\"", "\"implements\"", "\"import\"", "\"instanceof\"", "\"int\"", "\"interface\"", "\"long\"", "\"native\"", "\"new\"", "\"null\"", "\"package\"", "\"private\"", "\"protected\"", "\"public\"", "\"return\"", "\"short\"", "\"static\"", "\"super\"", "\"switch\"", "\"synchronized\"", "\"this\"", "\"throw\"", "\"throws\"", "\"transient\"", "\"true\"", "\"try\"", "\"void\"", "\"volatile\"", "\"while\"", "<INTEGER_LITERAL>", "<DECIMAL_LITERAL>", "<HEX_LITERAL>", "<OCTAL_LITERAL>", "<FLOATING_POINT_LITERAL>", "<EXPONENT>", "<CHARACTER_LITERAL>", "<STRING_LITERAL>", "<IDENTIFIER>", "<LETTER>", "<DIGIT>", "\"(\"", "\")\"", "\"{\"", "\"}\"", "\"[\"", "\"]\"", "\";\"", "\",\"", "\".\"", "\"=\"", "\">\"", "\"<\"", "\"!\"", "\"~\"", "\"?\"", "\":\"", "\"==\"", "\"<=\"", "\">=\"", "\"!=\"", "\"||\"", "\"&&\"", "\"++\"", "\"--\"", "\"+\"", "\"-\"", "\"*\"", "\"/\"", "\"&\"", "\"|\"", "\"^\"", "\"%\"", "\"<<\"", "\">>\"", "\">>>\"", "\"+=\"", "\"-=\"", "\"*=\"", "\"/=\"", "\"&=\"", "\"|=\"", "\"^=\"", "\"%=\"", "\"<<=\"", "\">>=\"", "\">>>=\"" };
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\example\debug\expr\ExpressionParserConstants.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */