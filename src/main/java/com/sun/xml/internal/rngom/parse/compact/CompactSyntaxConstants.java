/*     */ package com.sun.xml.internal.rngom.parse.compact;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public interface CompactSyntaxConstants
/*     */ {
/*     */   public static final int EOF = 0;
/*     */   public static final int NEWLINE = 37;
/*     */   public static final int NOT_NEWLINE = 38;
/*     */   public static final int WS = 39;
/*     */   public static final int DOCUMENTATION = 40;
/*     */   public static final int DOCUMENTATION_CONTINUE = 41;
/*     */   public static final int SINGLE_LINE_COMMENT = 42;
/*     */   public static final int DOCUMENTATION_AFTER_SINGLE_LINE_COMMENT = 43;
/*     */   public static final int SINGLE_LINE_COMMENT_CONTINUE = 44;
/*     */   public static final int BASE_CHAR = 45;
/*     */   public static final int IDEOGRAPHIC = 46;
/*     */   public static final int LETTER = 47;
/*     */   public static final int COMBINING_CHAR = 48;
/*     */   public static final int DIGIT = 49;
/*     */   public static final int EXTENDER = 50;
/*     */   public static final int NMSTART = 51;
/*     */   public static final int NMCHAR = 52;
/*     */   public static final int NCNAME = 53;
/*     */   public static final int IDENTIFIER = 54;
/*     */   public static final int ESCAPED_IDENTIFIER = 55;
/*     */   public static final int PREFIX_STAR = 56;
/*     */   public static final int PREFIXED_NAME = 57;
/*     */   public static final int LITERAL = 58;
/*     */   public static final int FANNOTATE = 59;
/*     */   public static final int ILLEGAL_CHAR = 60;
/*     */   public static final int DEFAULT = 0;
/*     */   public static final int AFTER_SINGLE_LINE_COMMENT = 1;
/*     */   public static final int AFTER_DOCUMENTATION = 2;
/* 115 */   public static final String[] tokenImage = new String[] { "<EOF>", "\"[\"", "\"=\"", "\"&=\"", "\"|=\"", "\"start\"", "\"div\"", "\"include\"", "\"~\"", "\"]\"", "\"grammar\"", "\"{\"", "\"}\"", "\"namespace\"", "\"default\"", "\"inherit\"", "\"datatypes\"", "\"empty\"", "\"text\"", "\"notAllowed\"", "\"|\"", "\"&\"", "\",\"", "\"+\"", "\"?\"", "\"*\"", "\"element\"", "\"attribute\"", "\"(\"", "\")\"", "\"-\"", "\"list\"", "\"mixed\"", "\"external\"", "\"parent\"", "\"string\"", "\"token\"", "<NEWLINE>", "<NOT_NEWLINE>", "<WS>", "<DOCUMENTATION>", "<DOCUMENTATION_CONTINUE>", "<SINGLE_LINE_COMMENT>", "<DOCUMENTATION_AFTER_SINGLE_LINE_COMMENT>", "<SINGLE_LINE_COMMENT_CONTINUE>", "<BASE_CHAR>", "<IDEOGRAPHIC>", "<LETTER>", "<COMBINING_CHAR>", "<DIGIT>", "<EXTENDER>", "<NMSTART>", "<NMCHAR>", "<NCNAME>", "<IDENTIFIER>", "<ESCAPED_IDENTIFIER>", "<PREFIX_STAR>", "<PREFIXED_NAME>", "<LITERAL>", "\">>\"", "<ILLEGAL_CHAR>" };
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\xml\internal\rngom\parse\compact\CompactSyntaxConstants.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */