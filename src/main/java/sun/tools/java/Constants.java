/*     */ package sun.tools.java;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public interface Constants
/*     */   extends RuntimeConstants
/*     */ {
/*     */   public static final boolean tracing = true;
/*  56 */   public static final Identifier idAppend = Identifier.lookup("append");
/*  57 */   public static final Identifier idClassInit = Identifier.lookup("<clinit>");
/*  58 */   public static final Identifier idCode = Identifier.lookup("Code");
/*  59 */   public static final Identifier idInit = Identifier.lookup("<init>");
/*  60 */   public static final Identifier idLength = Identifier.lookup("length");
/*  61 */   public static final Identifier idNull = Identifier.lookup("");
/*  62 */   public static final Identifier idStar = Identifier.lookup("*");
/*  63 */   public static final Identifier idSuper = Identifier.lookup("super");
/*  64 */   public static final Identifier idThis = Identifier.lookup("this");
/*  65 */   public static final Identifier idClass = Identifier.lookup("class");
/*  66 */   public static final Identifier idToString = Identifier.lookup("toString");
/*  67 */   public static final Identifier idValueOf = Identifier.lookup("valueOf");
/*  68 */   public static final Identifier idNew = Identifier.lookup("new");
/*  69 */   public static final Identifier idGetClass = Identifier.lookup("getClass");
/*  70 */   public static final Identifier idTYPE = Identifier.lookup("TYPE");
/*  71 */   public static final Identifier idFinallyReturnValue = Identifier.lookup("<return>");
/*     */   
/*  73 */   public static final Identifier idJavaLang = Identifier.lookup("java.lang");
/*     */   
/*  75 */   public static final Identifier idJavaLangCloneable = Identifier.lookup("java.lang.Cloneable");
/*     */   
/*  77 */   public static final Identifier idJavaLangError = Identifier.lookup("java.lang.Error");
/*  78 */   public static final Identifier idJavaLangException = Identifier.lookup("java.lang.Exception");
/*  79 */   public static final Identifier idJavaLangObject = Identifier.lookup("java.lang.Object");
/*  80 */   public static final Identifier idJavaLangClass = Identifier.lookup("java.lang.Class");
/*     */   
/*  82 */   public static final Identifier idJavaLangRuntimeException = Identifier.lookup("java.lang.RuntimeException");
/*  83 */   public static final Identifier idJavaLangString = Identifier.lookup("java.lang.String");
/*     */   
/*  85 */   public static final Identifier idJavaLangStringBuffer = Identifier.lookup("java.lang.StringBuffer");
/*  86 */   public static final Identifier idJavaLangThrowable = Identifier.lookup("java.lang.Throwable");
/*     */   
/*  88 */   public static final Identifier idJavaIoSerializable = Identifier.lookup("java.io.Serializable");
/*     */ 
/*     */   
/*  91 */   public static final Identifier idConstantValue = Identifier.lookup("ConstantValue");
/*  92 */   public static final Identifier idLocalVariableTable = Identifier.lookup("LocalVariableTable");
/*  93 */   public static final Identifier idLineNumberTable = Identifier.lookup("LineNumberTable");
/*     */   
/*  95 */   public static final Identifier idCoverageTable = Identifier.lookup("CoverageTable");
/*     */   
/*  97 */   public static final Identifier idSourceFile = Identifier.lookup("SourceFile");
/*  98 */   public static final Identifier idDocumentation = Identifier.lookup("Documentation");
/*  99 */   public static final Identifier idDeprecated = Identifier.lookup("Deprecated");
/* 100 */   public static final Identifier idSynthetic = Identifier.lookup("Synthetic");
/* 101 */   public static final Identifier idExceptions = Identifier.lookup("Exceptions");
/* 102 */   public static final Identifier idInnerClasses = Identifier.lookup("InnerClasses");
/*     */ 
/*     */   
/* 105 */   public static final Identifier idClone = Identifier.lookup("clone");
/*     */   
/*     */   public static final char SIGC_INNERCLASS = '$';
/*     */   
/*     */   public static final String SIG_INNERCLASS = "$";
/*     */   
/*     */   public static final String prefixThis = "this$";
/*     */   
/*     */   public static final String prefixVal = "val$";
/*     */   
/*     */   public static final String prefixLoc = "loc$";
/*     */   
/*     */   public static final String prefixAccess = "access$";
/*     */   
/*     */   public static final String prefixClass = "class$";
/*     */   
/*     */   public static final String prefixArray = "array$";
/*     */   
/*     */   public static final int F_VERBOSE = 1;
/*     */   
/*     */   public static final int F_DUMP = 2;
/*     */   
/*     */   public static final int F_WARNINGS = 4;
/*     */   
/*     */   public static final int F_DEBUG_LINES = 4096;
/*     */   
/*     */   public static final int F_DEBUG_VARS = 8192;
/*     */   
/*     */   public static final int F_DEBUG_SOURCE = 262144;
/*     */   
/*     */   public static final int F_OPT = 16384;
/*     */   
/*     */   public static final int F_OPT_INTERCLASS = 32768;
/*     */   
/*     */   public static final int F_DEPENDENCIES = 32;
/*     */   
/*     */   public static final int F_COVERAGE = 64;
/*     */   
/*     */   public static final int F_COVDATA = 128;
/*     */   
/*     */   public static final int F_DEPRECATION = 512;
/*     */   
/*     */   public static final int F_PRINT_DEPENDENCIES = 1024;
/*     */   
/*     */   public static final int F_VERSION12 = 2048;
/*     */   
/*     */   public static final int F_ERRORSREPORTED = 65536;
/*     */   
/*     */   public static final int F_STRICTDEFAULT = 131072;
/*     */   
/*     */   public static final int M_PUBLIC = 1;
/*     */   
/*     */   public static final int M_PRIVATE = 2;
/*     */   
/*     */   public static final int M_PROTECTED = 4;
/*     */   
/*     */   public static final int M_STATIC = 8;
/*     */   
/*     */   public static final int M_TRANSIENT = 128;
/*     */   
/*     */   public static final int M_SYNCHRONIZED = 32;
/*     */   
/*     */   public static final int M_ABSTRACT = 1024;
/*     */   
/*     */   public static final int M_NATIVE = 256;
/*     */   
/*     */   public static final int M_FINAL = 16;
/*     */   
/*     */   public static final int M_VOLATILE = 64;
/*     */   
/*     */   public static final int M_INTERFACE = 512;
/*     */   
/*     */   public static final int M_ANONYMOUS = 65536;
/*     */   
/*     */   public static final int M_LOCAL = 131072;
/*     */   
/*     */   public static final int M_DEPRECATED = 262144;
/*     */   
/*     */   public static final int M_SYNTHETIC = 524288;
/*     */   
/*     */   public static final int M_INLINEABLE = 1048576;
/*     */   
/*     */   public static final int M_STRICTFP = 2097152;
/*     */   
/*     */   public static final String paraDeprecated = "@deprecated";
/*     */   
/*     */   public static final int MM_CLASS = 2098705;
/*     */   
/*     */   public static final int MM_MEMBER = 31;
/*     */   
/*     */   public static final int MM_FIELD = 223;
/*     */   
/*     */   public static final int MM_METHOD = 2098495;
/*     */   
/*     */   public static final int ACCM_CLASS = 3633;
/*     */   
/*     */   public static final int ACCM_MEMBER = 31;
/*     */   
/*     */   public static final int ACCM_INNERCLASS = 3615;
/*     */   
/*     */   public static final int ACCM_FIELD = 223;
/*     */   
/*     */   public static final int ACCM_METHOD = 3391;
/*     */   
/*     */   public static final int TC_BOOLEAN = 0;
/*     */   
/*     */   public static final int TC_BYTE = 1;
/*     */   
/*     */   public static final int TC_CHAR = 2;
/*     */   
/*     */   public static final int TC_SHORT = 3;
/*     */   
/*     */   public static final int TC_INT = 4;
/*     */   
/*     */   public static final int TC_LONG = 5;
/*     */   
/*     */   public static final int TC_FLOAT = 6;
/*     */   
/*     */   public static final int TC_DOUBLE = 7;
/*     */   
/*     */   public static final int TC_NULL = 8;
/*     */   
/*     */   public static final int TC_ARRAY = 9;
/*     */   
/*     */   public static final int TC_CLASS = 10;
/*     */   
/*     */   public static final int TC_VOID = 11;
/*     */   
/*     */   public static final int TC_METHOD = 12;
/*     */   
/*     */   public static final int TC_ERROR = 13;
/*     */   
/*     */   public static final int CT_FIRST_KIND = 1;
/*     */   
/*     */   public static final int CT_METHOD = 1;
/*     */   
/*     */   public static final int CT_FIKT_METHOD = 2;
/*     */   
/*     */   public static final int CT_BLOCK = 3;
/*     */   
/*     */   public static final int CT_FIKT_RET = 4;
/*     */   
/*     */   public static final int CT_CASE = 5;
/*     */   
/*     */   public static final int CT_SWITH_WO_DEF = 6;
/*     */   
/*     */   public static final int CT_BRANCH_TRUE = 7;
/*     */   
/*     */   public static final int CT_BRANCH_FALSE = 8;
/*     */   
/*     */   public static final int CT_LAST_KIND = 8;
/*     */   
/*     */   public static final int TM_NULL = 256;
/*     */   
/*     */   public static final int TM_VOID = 2048;
/*     */   
/*     */   public static final int TM_BOOLEAN = 1;
/*     */   
/*     */   public static final int TM_BYTE = 2;
/*     */   
/*     */   public static final int TM_CHAR = 4;
/*     */   
/*     */   public static final int TM_SHORT = 8;
/*     */   
/*     */   public static final int TM_INT = 16;
/*     */   
/*     */   public static final int TM_LONG = 32;
/*     */   
/*     */   public static final int TM_FLOAT = 64;
/*     */   
/*     */   public static final int TM_DOUBLE = 128;
/*     */   
/*     */   public static final int TM_ARRAY = 512;
/*     */   
/*     */   public static final int TM_CLASS = 1024;
/*     */   
/*     */   public static final int TM_METHOD = 4096;
/*     */   
/*     */   public static final int TM_ERROR = 8192;
/*     */   
/*     */   public static final int TM_INT32 = 30;
/*     */   
/*     */   public static final int TM_NUM32 = 94;
/*     */   
/*     */   public static final int TM_NUM64 = 160;
/*     */   
/*     */   public static final int TM_INTEGER = 62;
/*     */   
/*     */   public static final int TM_REAL = 192;
/*     */   
/*     */   public static final int TM_NUMBER = 254;
/*     */   
/*     */   public static final int TM_REFERENCE = 1792;
/*     */   
/*     */   public static final int CS_UNDEFINED = 0;
/*     */   
/*     */   public static final int CS_UNDECIDED = 1;
/*     */   
/*     */   public static final int CS_BINARY = 2;
/*     */   
/*     */   public static final int CS_SOURCE = 3;
/*     */   
/*     */   public static final int CS_PARSED = 4;
/*     */   
/*     */   public static final int CS_CHECKED = 5;
/*     */   
/*     */   public static final int CS_COMPILED = 6;
/*     */   
/*     */   public static final int CS_NOTFOUND = 7;
/*     */   
/*     */   public static final int ATT_ALL = -1;
/*     */   
/*     */   public static final int ATT_CODE = 2;
/*     */   
/*     */   public static final int ATT_ALLCLASSES = 4;
/*     */   
/*     */   public static final int WHEREOFFSETBITS = 32;
/*     */   
/*     */   public static final long MAXFILESIZE = 4294967295L;
/*     */   
/*     */   public static final long MAXLINENUMBER = 4294967295L;
/*     */   
/*     */   public static final int COMMA = 0;
/*     */   
/*     */   public static final int ASSIGN = 1;
/*     */   
/*     */   public static final int ASGMUL = 2;
/*     */   
/*     */   public static final int ASGDIV = 3;
/*     */   
/*     */   public static final int ASGREM = 4;
/*     */   
/*     */   public static final int ASGADD = 5;
/*     */   
/*     */   public static final int ASGSUB = 6;
/*     */   
/*     */   public static final int ASGLSHIFT = 7;
/*     */   
/*     */   public static final int ASGRSHIFT = 8;
/*     */   
/*     */   public static final int ASGURSHIFT = 9;
/*     */   
/*     */   public static final int ASGBITAND = 10;
/*     */   
/*     */   public static final int ASGBITOR = 11;
/*     */   
/*     */   public static final int ASGBITXOR = 12;
/*     */   
/*     */   public static final int COND = 13;
/*     */   
/*     */   public static final int OR = 14;
/*     */   
/*     */   public static final int AND = 15;
/*     */   
/*     */   public static final int BITOR = 16;
/*     */   
/*     */   public static final int BITXOR = 17;
/*     */   
/*     */   public static final int BITAND = 18;
/*     */   
/*     */   public static final int NE = 19;
/*     */   
/*     */   public static final int EQ = 20;
/*     */   
/*     */   public static final int GE = 21;
/*     */   
/*     */   public static final int GT = 22;
/*     */   
/*     */   public static final int LE = 23;
/*     */   
/*     */   public static final int LT = 24;
/*     */   
/*     */   public static final int INSTANCEOF = 25;
/*     */   
/*     */   public static final int LSHIFT = 26;
/*     */   
/*     */   public static final int RSHIFT = 27;
/*     */   
/*     */   public static final int URSHIFT = 28;
/*     */   
/*     */   public static final int ADD = 29;
/*     */   
/*     */   public static final int SUB = 30;
/*     */   
/*     */   public static final int DIV = 31;
/*     */   
/*     */   public static final int REM = 32;
/*     */   
/*     */   public static final int MUL = 33;
/*     */   
/*     */   public static final int CAST = 34;
/*     */   
/*     */   public static final int POS = 35;
/*     */   
/*     */   public static final int NEG = 36;
/*     */   
/*     */   public static final int NOT = 37;
/*     */   
/*     */   public static final int BITNOT = 38;
/*     */   
/*     */   public static final int PREINC = 39;
/*     */   
/*     */   public static final int PREDEC = 40;
/*     */   
/*     */   public static final int NEWARRAY = 41;
/*     */   
/*     */   public static final int NEWINSTANCE = 42;
/*     */   
/*     */   public static final int NEWFROMNAME = 43;
/*     */   
/*     */   public static final int POSTINC = 44;
/*     */   
/*     */   public static final int POSTDEC = 45;
/*     */   
/*     */   public static final int FIELD = 46;
/*     */   
/*     */   public static final int METHOD = 47;
/*     */   
/*     */   public static final int ARRAYACCESS = 48;
/*     */   
/*     */   public static final int NEW = 49;
/*     */   
/*     */   public static final int INC = 50;
/*     */   
/*     */   public static final int DEC = 51;
/*     */   
/*     */   public static final int CONVERT = 55;
/*     */   
/*     */   public static final int EXPR = 56;
/*     */   
/*     */   public static final int ARRAY = 57;
/*     */   
/*     */   public static final int GOTO = 58;
/*     */   
/*     */   public static final int IDENT = 60;
/*     */   
/*     */   public static final int BOOLEANVAL = 61;
/*     */   
/*     */   public static final int BYTEVAL = 62;
/*     */   
/*     */   public static final int CHARVAL = 63;
/*     */   
/*     */   public static final int SHORTVAL = 64;
/*     */   
/*     */   public static final int INTVAL = 65;
/*     */   
/*     */   public static final int LONGVAL = 66;
/*     */   
/*     */   public static final int FLOATVAL = 67;
/*     */   
/*     */   public static final int DOUBLEVAL = 68;
/*     */   
/*     */   public static final int STRINGVAL = 69;
/*     */   
/*     */   public static final int BYTE = 70;
/*     */   
/*     */   public static final int CHAR = 71;
/*     */   
/*     */   public static final int SHORT = 72;
/*     */   
/*     */   public static final int INT = 73;
/*     */   
/*     */   public static final int LONG = 74;
/*     */   
/*     */   public static final int FLOAT = 75;
/*     */   
/*     */   public static final int DOUBLE = 76;
/*     */   
/*     */   public static final int VOID = 77;
/*     */   
/*     */   public static final int BOOLEAN = 78;
/*     */   
/*     */   public static final int TRUE = 80;
/*     */   
/*     */   public static final int FALSE = 81;
/*     */   
/*     */   public static final int THIS = 82;
/*     */   
/*     */   public static final int SUPER = 83;
/*     */   public static final int NULL = 84;
/*     */   public static final int IF = 90;
/*     */   public static final int ELSE = 91;
/*     */   public static final int FOR = 92;
/*     */   public static final int WHILE = 93;
/*     */   public static final int DO = 94;
/*     */   public static final int SWITCH = 95;
/*     */   public static final int CASE = 96;
/*     */   public static final int DEFAULT = 97;
/*     */   public static final int BREAK = 98;
/*     */   public static final int CONTINUE = 99;
/*     */   public static final int RETURN = 100;
/*     */   public static final int TRY = 101;
/*     */   public static final int CATCH = 102;
/*     */   public static final int FINALLY = 103;
/*     */   public static final int THROW = 104;
/*     */   public static final int STAT = 105;
/*     */   public static final int EXPRESSION = 106;
/*     */   public static final int DECLARATION = 107;
/*     */   public static final int VARDECLARATION = 108;
/*     */   public static final int IMPORT = 110;
/*     */   public static final int CLASS = 111;
/*     */   public static final int EXTENDS = 112;
/*     */   public static final int IMPLEMENTS = 113;
/*     */   public static final int INTERFACE = 114;
/*     */   public static final int PACKAGE = 115;
/*     */   public static final int PRIVATE = 120;
/*     */   public static final int PUBLIC = 121;
/*     */   public static final int PROTECTED = 122;
/*     */   public static final int CONST = 123;
/*     */   public static final int STATIC = 124;
/*     */   public static final int TRANSIENT = 125;
/*     */   public static final int SYNCHRONIZED = 126;
/*     */   public static final int NATIVE = 127;
/*     */   public static final int FINAL = 128;
/*     */   public static final int VOLATILE = 129;
/*     */   public static final int ABSTRACT = 130;
/*     */   public static final int STRICTFP = 131;
/*     */   public static final int SEMICOLON = 135;
/*     */   public static final int COLON = 136;
/*     */   public static final int QUESTIONMARK = 137;
/*     */   public static final int LBRACE = 138;
/*     */   public static final int RBRACE = 139;
/*     */   public static final int LPAREN = 140;
/*     */   public static final int RPAREN = 141;
/*     */   public static final int LSQBRACKET = 142;
/*     */   public static final int RSQBRACKET = 143;
/*     */   public static final int THROWS = 144;
/*     */   public static final int ERROR = 145;
/*     */   public static final int COMMENT = 146;
/*     */   public static final int TYPE = 147;
/*     */   public static final int LENGTH = 148;
/*     */   public static final int INLINERETURN = 149;
/*     */   public static final int INLINEMETHOD = 150;
/*     */   public static final int INLINENEWINSTANCE = 151;
/* 539 */   public static final int[] opPrecedence = new int[] { 10, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 12, 13, 14, 15, 16, 17, 18, 18, 19, 19, 19, 19, 19, 20, 20, 20, 21, 21, 22, 22, 22, 23, 24, 24, 24, 24, 24, 24, 25, 25, 26, 26, 26, 26, 26, 26 };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 550 */   public static final String[] opNames = new String[] { ",", "=", "*=", "/=", "%=", "+=", "-=", "<<=", ">>=", ">>>=", "&=", "|=", "^=", "?:", "||", "&&", "|", "^", "&", "!=", "==", ">=", ">", "<=", "<", "instanceof", "<<", ">>", ">>>", "+", "-", "/", "%", "*", "cast", "+", "-", "!", "~", "++", "--", "new", "new", "new", "++", "--", "field", "method", "[]", "new", "++", "--", null, null, null, "convert", "expr", "array", "goto", null, "Identifier", "boolean", "byte", "char", "short", "int", "long", "float", "double", "string", "byte", "char", "short", "int", "long", "float", "double", "void", "boolean", null, "true", "false", "this", "super", "null", null, null, null, null, null, "if", "else", "for", "while", "do", "switch", "case", "default", "break", "continue", "return", "try", "catch", "finally", "throw", "stat", "expression", "declaration", "declaration", null, "import", "class", "extends", "implements", "interface", "package", null, null, null, null, "private", "public", "protected", "const", "static", "transient", "synchronized", "native", "final", "volatile", "abstract", "strictfp", null, null, null, ";", ":", "?", "{", "}", "(", ")", "[", "]", "throws", "error", "comment", "type", "length", "inline-return", "inline-method", "inline-new" };
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\sun\tools\java\Constants.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */