/*      */ package com.sun.xml.internal.rngom.parse.compact;
/*      */ 
/*      */ import java.io.IOException;
/*      */ import java.io.PrintStream;
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
/*      */ public class CompactSyntaxTokenManager
/*      */   implements CompactSyntaxConstants
/*      */ {
/*   89 */   public PrintStream debugStream = System.out;
/*      */   public void setDebugStream(PrintStream ds) {
/*   91 */     this.debugStream = ds;
/*      */   }
/*      */   private final int jjStopStringLiteralDfa_0(int pos, long active0) {
/*   94 */     switch (pos) {
/*      */       
/*      */       case 0:
/*   97 */         if ((active0 & 0x1F8C0FE4E0L) != 0L) {
/*      */           
/*   99 */           this.jjmatchedKind = 54;
/*  100 */           return 43;
/*      */         } 
/*  102 */         if ((active0 & 0x800000000000000L) != 0L) {
/*      */           
/*  104 */           this.jjmatchedKind = 60;
/*  105 */           return -1;
/*      */         } 
/*  107 */         return -1;
/*      */       case 1:
/*  109 */         if ((active0 & 0x1F8C0FE4E0L) != 0L) {
/*      */           
/*  111 */           this.jjmatchedKind = 54;
/*  112 */           this.jjmatchedPos = 1;
/*  113 */           return 43;
/*      */         } 
/*  115 */         if ((active0 & 0x800000000000000L) != 0L) {
/*      */           
/*  117 */           if (this.jjmatchedPos == 0) {
/*      */             
/*  119 */             this.jjmatchedKind = 60;
/*  120 */             this.jjmatchedPos = 0;
/*      */           } 
/*  122 */           return -1;
/*      */         } 
/*  124 */         return -1;
/*      */       case 2:
/*  126 */         if ((active0 & 0x1F8C0FE4A0L) != 0L) {
/*      */           
/*  128 */           this.jjmatchedKind = 54;
/*  129 */           this.jjmatchedPos = 2;
/*  130 */           return 43;
/*      */         } 
/*  132 */         if ((active0 & 0x40L) != 0L)
/*  133 */           return 43; 
/*  134 */         return -1;
/*      */       case 3:
/*  136 */         if ((active0 & 0x1F0C0BE4A0L) != 0L) {
/*      */           
/*  138 */           this.jjmatchedKind = 54;
/*  139 */           this.jjmatchedPos = 3;
/*  140 */           return 43;
/*      */         } 
/*  142 */         if ((active0 & 0x80040000L) != 0L)
/*  143 */           return 43; 
/*  144 */         return -1;
/*      */       case 4:
/*  146 */         if ((active0 & 0xE0C09E480L) != 0L) {
/*      */           
/*  148 */           this.jjmatchedKind = 54;
/*  149 */           this.jjmatchedPos = 4;
/*  150 */           return 43;
/*      */         } 
/*  152 */         if ((active0 & 0x1100020020L) != 0L)
/*  153 */           return 43; 
/*  154 */         return -1;
/*      */       case 5:
/*  156 */         if ((active0 & 0x20C09E480L) != 0L) {
/*      */           
/*  158 */           this.jjmatchedKind = 54;
/*  159 */           this.jjmatchedPos = 5;
/*  160 */           return 43;
/*      */         } 
/*  162 */         if ((active0 & 0xC00000000L) != 0L)
/*  163 */           return 43; 
/*  164 */         return -1;
/*      */       case 6:
/*  166 */         if ((active0 & 0x208092000L) != 0L) {
/*      */           
/*  168 */           this.jjmatchedKind = 54;
/*  169 */           this.jjmatchedPos = 6;
/*  170 */           return 43;
/*      */         } 
/*  172 */         if ((active0 & 0x400C480L) != 0L)
/*  173 */           return 43; 
/*  174 */         return -1;
/*      */       case 7:
/*  176 */         if ((active0 & 0x8092000L) != 0L) {
/*      */           
/*  178 */           this.jjmatchedKind = 54;
/*  179 */           this.jjmatchedPos = 7;
/*  180 */           return 43;
/*      */         } 
/*  182 */         if ((active0 & 0x200000000L) != 0L)
/*  183 */           return 43; 
/*  184 */         return -1;
/*      */       case 8:
/*  186 */         if ((active0 & 0x80000L) != 0L) {
/*      */           
/*  188 */           this.jjmatchedKind = 54;
/*  189 */           this.jjmatchedPos = 8;
/*  190 */           return 43;
/*      */         } 
/*  192 */         if ((active0 & 0x8012000L) != 0L)
/*  193 */           return 43; 
/*  194 */         return -1;
/*      */     } 
/*  196 */     return -1;
/*      */   }
/*      */ 
/*      */   
/*      */   private final int jjStartNfa_0(int pos, long active0) {
/*  201 */     return jjMoveNfa_0(jjStopStringLiteralDfa_0(pos, active0), pos + 1);
/*      */   }
/*      */   
/*      */   private int jjStopAtPos(int pos, int kind) {
/*  205 */     this.jjmatchedKind = kind;
/*  206 */     this.jjmatchedPos = pos;
/*  207 */     return pos + 1;
/*      */   }
/*      */   
/*      */   private int jjMoveStringLiteralDfa0_0() {
/*  211 */     switch (this.curChar) {
/*      */       
/*      */       case '&':
/*  214 */         this.jjmatchedKind = 21;
/*  215 */         return jjMoveStringLiteralDfa1_0(8L);
/*      */       case '(':
/*  217 */         return jjStopAtPos(0, 28);
/*      */       case ')':
/*  219 */         return jjStopAtPos(0, 29);
/*      */       case '*':
/*  221 */         return jjStopAtPos(0, 25);
/*      */       case '+':
/*  223 */         return jjStopAtPos(0, 23);
/*      */       case ',':
/*  225 */         return jjStopAtPos(0, 22);
/*      */       case '-':
/*  227 */         return jjStopAtPos(0, 30);
/*      */       case '=':
/*  229 */         return jjStopAtPos(0, 2);
/*      */       case '>':
/*  231 */         return jjMoveStringLiteralDfa1_0(576460752303423488L);
/*      */       case '?':
/*  233 */         return jjStopAtPos(0, 24);
/*      */       case '[':
/*  235 */         return jjStopAtPos(0, 1);
/*      */       case ']':
/*  237 */         return jjStopAtPos(0, 9);
/*      */       case 'a':
/*  239 */         return jjMoveStringLiteralDfa1_0(134217728L);
/*      */       case 'd':
/*  241 */         return jjMoveStringLiteralDfa1_0(81984L);
/*      */       case 'e':
/*  243 */         return jjMoveStringLiteralDfa1_0(8657174528L);
/*      */       case 'g':
/*  245 */         return jjMoveStringLiteralDfa1_0(1024L);
/*      */       case 'i':
/*  247 */         return jjMoveStringLiteralDfa1_0(32896L);
/*      */       case 'l':
/*  249 */         return jjMoveStringLiteralDfa1_0(2147483648L);
/*      */       case 'm':
/*  251 */         return jjMoveStringLiteralDfa1_0(4294967296L);
/*      */       case 'n':
/*  253 */         return jjMoveStringLiteralDfa1_0(532480L);
/*      */       case 'p':
/*  255 */         return jjMoveStringLiteralDfa1_0(17179869184L);
/*      */       case 's':
/*  257 */         return jjMoveStringLiteralDfa1_0(34359738400L);
/*      */       case 't':
/*  259 */         return jjMoveStringLiteralDfa1_0(68719738880L);
/*      */       case '{':
/*  261 */         return jjStopAtPos(0, 11);
/*      */       case '|':
/*  263 */         this.jjmatchedKind = 20;
/*  264 */         return jjMoveStringLiteralDfa1_0(16L);
/*      */       case '}':
/*  266 */         return jjStopAtPos(0, 12);
/*      */       case '~':
/*  268 */         return jjStopAtPos(0, 8);
/*      */     } 
/*  270 */     return jjMoveNfa_0(3, 0);
/*      */   }
/*      */   
/*      */   private int jjMoveStringLiteralDfa1_0(long active0) {
/*      */     try {
/*  275 */       this.curChar = this.input_stream.readChar();
/*  276 */     } catch (IOException e) {
/*  277 */       jjStopStringLiteralDfa_0(0, active0);
/*  278 */       return 1;
/*      */     } 
/*  280 */     switch (this.curChar) {
/*      */       
/*      */       case '=':
/*  283 */         if ((active0 & 0x8L) != 0L)
/*  284 */           return jjStopAtPos(1, 3); 
/*  285 */         if ((active0 & 0x10L) != 0L)
/*  286 */           return jjStopAtPos(1, 4); 
/*      */         break;
/*      */       case '>':
/*  289 */         if ((active0 & 0x800000000000000L) != 0L)
/*  290 */           return jjStopAtPos(1, 59); 
/*      */         break;
/*      */       case 'a':
/*  293 */         return jjMoveStringLiteralDfa2_0(active0, 17179942912L);
/*      */       case 'e':
/*  295 */         return jjMoveStringLiteralDfa2_0(active0, 278528L);
/*      */       case 'i':
/*  297 */         return jjMoveStringLiteralDfa2_0(active0, 6442451008L);
/*      */       case 'l':
/*  299 */         return jjMoveStringLiteralDfa2_0(active0, 67108864L);
/*      */       case 'm':
/*  301 */         return jjMoveStringLiteralDfa2_0(active0, 131072L);
/*      */       case 'n':
/*  303 */         return jjMoveStringLiteralDfa2_0(active0, 32896L);
/*      */       case 'o':
/*  305 */         return jjMoveStringLiteralDfa2_0(active0, 68720001024L);
/*      */       case 'r':
/*  307 */         return jjMoveStringLiteralDfa2_0(active0, 1024L);
/*      */       case 't':
/*  309 */         return jjMoveStringLiteralDfa2_0(active0, 34493956128L);
/*      */       case 'x':
/*  311 */         return jjMoveStringLiteralDfa2_0(active0, 8589934592L);
/*      */     } 
/*      */ 
/*      */     
/*  315 */     return jjStartNfa_0(0, active0);
/*      */   }
/*      */   
/*      */   private int jjMoveStringLiteralDfa2_0(long old0, long active0) {
/*  319 */     if ((active0 &= old0) == 0L)
/*  320 */       return jjStartNfa_0(0, old0);  try {
/*  321 */       this.curChar = this.input_stream.readChar();
/*  322 */     } catch (IOException e) {
/*  323 */       jjStopStringLiteralDfa_0(1, active0);
/*  324 */       return 2;
/*      */     } 
/*  326 */     switch (this.curChar) {
/*      */       
/*      */       case 'a':
/*  329 */         return jjMoveStringLiteralDfa3_0(active0, 1056L);
/*      */       case 'c':
/*  331 */         return jjMoveStringLiteralDfa3_0(active0, 128L);
/*      */       case 'e':
/*  333 */         return jjMoveStringLiteralDfa3_0(active0, 67108864L);
/*      */       case 'f':
/*  335 */         return jjMoveStringLiteralDfa3_0(active0, 16384L);
/*      */       case 'h':
/*  337 */         return jjMoveStringLiteralDfa3_0(active0, 32768L);
/*      */       case 'k':
/*  339 */         return jjMoveStringLiteralDfa3_0(active0, 68719476736L);
/*      */       case 'm':
/*  341 */         return jjMoveStringLiteralDfa3_0(active0, 8192L);
/*      */       case 'p':
/*  343 */         return jjMoveStringLiteralDfa3_0(active0, 131072L);
/*      */       case 'r':
/*  345 */         return jjMoveStringLiteralDfa3_0(active0, 51539607552L);
/*      */       case 's':
/*  347 */         return jjMoveStringLiteralDfa3_0(active0, 2147483648L);
/*      */       case 't':
/*  349 */         return jjMoveStringLiteralDfa3_0(active0, 8724742144L);
/*      */       case 'v':
/*  351 */         if ((active0 & 0x40L) != 0L)
/*  352 */           return jjStartNfaWithStates_0(2, 6, 43); 
/*      */         break;
/*      */       case 'x':
/*  355 */         return jjMoveStringLiteralDfa3_0(active0, 4295229440L);
/*      */     } 
/*      */ 
/*      */     
/*  359 */     return jjStartNfa_0(1, active0);
/*      */   }
/*      */   
/*      */   private int jjMoveStringLiteralDfa3_0(long old0, long active0) {
/*  363 */     if ((active0 &= old0) == 0L)
/*  364 */       return jjStartNfa_0(1, old0);  try {
/*  365 */       this.curChar = this.input_stream.readChar();
/*  366 */     } catch (IOException e) {
/*  367 */       jjStopStringLiteralDfa_0(2, active0);
/*  368 */       return 3;
/*      */     } 
/*  370 */     switch (this.curChar) {
/*      */       
/*      */       case 'A':
/*  373 */         return jjMoveStringLiteralDfa4_0(active0, 524288L);
/*      */       case 'a':
/*  375 */         return jjMoveStringLiteralDfa4_0(active0, 81920L);
/*      */       case 'e':
/*  377 */         return jjMoveStringLiteralDfa4_0(active0, 98784288768L);
/*      */       case 'i':
/*  379 */         return jjMoveStringLiteralDfa4_0(active0, 34359738368L);
/*      */       case 'l':
/*  381 */         return jjMoveStringLiteralDfa4_0(active0, 128L);
/*      */       case 'm':
/*  383 */         return jjMoveStringLiteralDfa4_0(active0, 67109888L);
/*      */       case 'r':
/*  385 */         return jjMoveStringLiteralDfa4_0(active0, 134217760L);
/*      */       case 't':
/*  387 */         if ((active0 & 0x40000L) != 0L)
/*  388 */           return jjStartNfaWithStates_0(3, 18, 43); 
/*  389 */         if ((active0 & 0x80000000L) != 0L)
/*  390 */           return jjStartNfaWithStates_0(3, 31, 43); 
/*  391 */         return jjMoveStringLiteralDfa4_0(active0, 131072L);
/*      */     } 
/*      */ 
/*      */     
/*  395 */     return jjStartNfa_0(2, active0);
/*      */   }
/*      */   
/*      */   private int jjMoveStringLiteralDfa4_0(long old0, long active0) {
/*  399 */     if ((active0 &= old0) == 0L)
/*  400 */       return jjStartNfa_0(2, old0);  try {
/*  401 */       this.curChar = this.input_stream.readChar();
/*  402 */     } catch (IOException e) {
/*  403 */       jjStopStringLiteralDfa_0(3, active0);
/*  404 */       return 4;
/*      */     } 
/*  406 */     switch (this.curChar) {
/*      */       
/*      */       case 'd':
/*  409 */         if ((active0 & 0x100000000L) != 0L)
/*  410 */           return jjStartNfaWithStates_0(4, 32, 43); 
/*      */         break;
/*      */       case 'e':
/*  413 */         return jjMoveStringLiteralDfa5_0(active0, 67108864L);
/*      */       case 'i':
/*  415 */         return jjMoveStringLiteralDfa5_0(active0, 134217728L);
/*      */       case 'l':
/*  417 */         return jjMoveStringLiteralDfa5_0(active0, 524288L);
/*      */       case 'm':
/*  419 */         return jjMoveStringLiteralDfa5_0(active0, 1024L);
/*      */       case 'n':
/*  421 */         if ((active0 & 0x1000000000L) != 0L)
/*  422 */           return jjStartNfaWithStates_0(4, 36, 43); 
/*  423 */         return jjMoveStringLiteralDfa5_0(active0, 51539607552L);
/*      */       case 'r':
/*  425 */         return jjMoveStringLiteralDfa5_0(active0, 8589967360L);
/*      */       case 's':
/*  427 */         return jjMoveStringLiteralDfa5_0(active0, 8192L);
/*      */       case 't':
/*  429 */         if ((active0 & 0x20L) != 0L)
/*  430 */           return jjStartNfaWithStates_0(4, 5, 43); 
/*  431 */         return jjMoveStringLiteralDfa5_0(active0, 65536L);
/*      */       case 'u':
/*  433 */         return jjMoveStringLiteralDfa5_0(active0, 16512L);
/*      */       case 'y':
/*  435 */         if ((active0 & 0x20000L) != 0L) {
/*  436 */           return jjStartNfaWithStates_0(4, 17, 43);
/*      */         }
/*      */         break;
/*      */     } 
/*      */     
/*  441 */     return jjStartNfa_0(3, active0);
/*      */   }
/*      */   
/*      */   private int jjMoveStringLiteralDfa5_0(long old0, long active0) {
/*  445 */     if ((active0 &= old0) == 0L)
/*  446 */       return jjStartNfa_0(3, old0);  try {
/*  447 */       this.curChar = this.input_stream.readChar();
/*  448 */     } catch (IOException e) {
/*  449 */       jjStopStringLiteralDfa_0(4, active0);
/*  450 */       return 5;
/*      */     } 
/*  452 */     switch (this.curChar) {
/*      */       
/*      */       case 'a':
/*  455 */         return jjMoveStringLiteralDfa6_0(active0, 1024L);
/*      */       case 'b':
/*  457 */         return jjMoveStringLiteralDfa6_0(active0, 134217728L);
/*      */       case 'd':
/*  459 */         return jjMoveStringLiteralDfa6_0(active0, 128L);
/*      */       case 'g':
/*  461 */         if ((active0 & 0x800000000L) != 0L)
/*  462 */           return jjStartNfaWithStates_0(5, 35, 43); 
/*      */         break;
/*      */       case 'i':
/*  465 */         return jjMoveStringLiteralDfa6_0(active0, 32768L);
/*      */       case 'l':
/*  467 */         return jjMoveStringLiteralDfa6_0(active0, 540672L);
/*      */       case 'n':
/*  469 */         return jjMoveStringLiteralDfa6_0(active0, 8657043456L);
/*      */       case 'p':
/*  471 */         return jjMoveStringLiteralDfa6_0(active0, 8192L);
/*      */       case 't':
/*  473 */         if ((active0 & 0x400000000L) != 0L)
/*  474 */           return jjStartNfaWithStates_0(5, 34, 43); 
/*      */         break;
/*      */       case 'y':
/*  477 */         return jjMoveStringLiteralDfa6_0(active0, 65536L);
/*      */     } 
/*      */ 
/*      */     
/*  481 */     return jjStartNfa_0(4, active0);
/*      */   }
/*      */   
/*      */   private int jjMoveStringLiteralDfa6_0(long old0, long active0) {
/*  485 */     if ((active0 &= old0) == 0L)
/*  486 */       return jjStartNfa_0(4, old0);  try {
/*  487 */       this.curChar = this.input_stream.readChar();
/*  488 */     } catch (IOException e) {
/*  489 */       jjStopStringLiteralDfa_0(5, active0);
/*  490 */       return 6;
/*      */     } 
/*  492 */     switch (this.curChar) {
/*      */       
/*      */       case 'a':
/*  495 */         return jjMoveStringLiteralDfa7_0(active0, 8589942784L);
/*      */       case 'e':
/*  497 */         if ((active0 & 0x80L) != 0L)
/*  498 */           return jjStartNfaWithStates_0(6, 7, 43); 
/*      */         break;
/*      */       case 'o':
/*  501 */         return jjMoveStringLiteralDfa7_0(active0, 524288L);
/*      */       case 'p':
/*  503 */         return jjMoveStringLiteralDfa7_0(active0, 65536L);
/*      */       case 'r':
/*  505 */         if ((active0 & 0x400L) != 0L)
/*  506 */           return jjStartNfaWithStates_0(6, 10, 43); 
/*      */         break;
/*      */       case 't':
/*  509 */         if ((active0 & 0x4000L) != 0L)
/*  510 */           return jjStartNfaWithStates_0(6, 14, 43); 
/*  511 */         if ((active0 & 0x8000L) != 0L)
/*  512 */           return jjStartNfaWithStates_0(6, 15, 43); 
/*  513 */         if ((active0 & 0x4000000L) != 0L)
/*  514 */           return jjStartNfaWithStates_0(6, 26, 43); 
/*      */         break;
/*      */       case 'u':
/*  517 */         return jjMoveStringLiteralDfa7_0(active0, 134217728L);
/*      */     } 
/*      */ 
/*      */     
/*  521 */     return jjStartNfa_0(5, active0);
/*      */   }
/*      */   
/*      */   private int jjMoveStringLiteralDfa7_0(long old0, long active0) {
/*  525 */     if ((active0 &= old0) == 0L)
/*  526 */       return jjStartNfa_0(5, old0);  try {
/*  527 */       this.curChar = this.input_stream.readChar();
/*  528 */     } catch (IOException e) {
/*  529 */       jjStopStringLiteralDfa_0(6, active0);
/*  530 */       return 7;
/*      */     } 
/*  532 */     switch (this.curChar) {
/*      */       
/*      */       case 'c':
/*  535 */         return jjMoveStringLiteralDfa8_0(active0, 8192L);
/*      */       case 'e':
/*  537 */         return jjMoveStringLiteralDfa8_0(active0, 65536L);
/*      */       case 'l':
/*  539 */         if ((active0 & 0x200000000L) != 0L)
/*  540 */           return jjStartNfaWithStates_0(7, 33, 43); 
/*      */         break;
/*      */       case 't':
/*  543 */         return jjMoveStringLiteralDfa8_0(active0, 134217728L);
/*      */       case 'w':
/*  545 */         return jjMoveStringLiteralDfa8_0(active0, 524288L);
/*      */     } 
/*      */ 
/*      */     
/*  549 */     return jjStartNfa_0(6, active0);
/*      */   }
/*      */   
/*      */   private int jjMoveStringLiteralDfa8_0(long old0, long active0) {
/*  553 */     if ((active0 &= old0) == 0L)
/*  554 */       return jjStartNfa_0(6, old0);  try {
/*  555 */       this.curChar = this.input_stream.readChar();
/*  556 */     } catch (IOException e) {
/*  557 */       jjStopStringLiteralDfa_0(7, active0);
/*  558 */       return 8;
/*      */     } 
/*  560 */     switch (this.curChar) {
/*      */       
/*      */       case 'e':
/*  563 */         if ((active0 & 0x2000L) != 0L)
/*  564 */           return jjStartNfaWithStates_0(8, 13, 43); 
/*  565 */         if ((active0 & 0x8000000L) != 0L)
/*  566 */           return jjStartNfaWithStates_0(8, 27, 43); 
/*  567 */         return jjMoveStringLiteralDfa9_0(active0, 524288L);
/*      */       case 's':
/*  569 */         if ((active0 & 0x10000L) != 0L) {
/*  570 */           return jjStartNfaWithStates_0(8, 16, 43);
/*      */         }
/*      */         break;
/*      */     } 
/*      */     
/*  575 */     return jjStartNfa_0(7, active0);
/*      */   }
/*      */   
/*      */   private int jjMoveStringLiteralDfa9_0(long old0, long active0) {
/*  579 */     if ((active0 &= old0) == 0L)
/*  580 */       return jjStartNfa_0(7, old0);  try {
/*  581 */       this.curChar = this.input_stream.readChar();
/*  582 */     } catch (IOException e) {
/*  583 */       jjStopStringLiteralDfa_0(8, active0);
/*  584 */       return 9;
/*      */     } 
/*  586 */     switch (this.curChar) {
/*      */       
/*      */       case 'd':
/*  589 */         if ((active0 & 0x80000L) != 0L) {
/*  590 */           return jjStartNfaWithStates_0(9, 19, 43);
/*      */         }
/*      */         break;
/*      */     } 
/*      */     
/*  595 */     return jjStartNfa_0(8, active0);
/*      */   }
/*      */   
/*      */   private int jjStartNfaWithStates_0(int pos, int kind, int state) {
/*  599 */     this.jjmatchedKind = kind;
/*  600 */     this.jjmatchedPos = pos; 
/*  601 */     try { this.curChar = this.input_stream.readChar(); }
/*  602 */     catch (IOException e) { return pos + 1; }
/*  603 */      return jjMoveNfa_0(state, pos + 1);
/*      */   }
/*  605 */   static final long[] jjbitVec0 = new long[] { -2L, -1L, -1L, -1L };
/*      */ 
/*      */   
/*  608 */   static final long[] jjbitVec2 = new long[] { 0L, 0L, -1L, -1L };
/*      */ 
/*      */   
/*  611 */   static final long[] jjbitVec3 = new long[] { 0L, -16384L, -17590038560769L, 8388607L };
/*      */ 
/*      */   
/*  614 */   static final long[] jjbitVec4 = new long[] { 0L, 0L, 0L, -36028797027352577L };
/*      */ 
/*      */   
/*  617 */   static final long[] jjbitVec5 = new long[] { 9219994337134247935L, 9223372036854775294L, -1L, -274156627316187121L };
/*      */ 
/*      */   
/*  620 */   static final long[] jjbitVec6 = new long[] { 16777215L, -65536L, -576458553280167937L, 3L };
/*      */ 
/*      */   
/*  623 */   static final long[] jjbitVec7 = new long[] { 0L, 0L, -17179879616L, 4503588160110591L };
/*      */ 
/*      */   
/*  626 */   static final long[] jjbitVec8 = new long[] { -8194L, -536936449L, -65533L, 234134404065073567L };
/*      */ 
/*      */   
/*  629 */   static final long[] jjbitVec9 = new long[] { -562949953421312L, -8547991553L, 127L, 1979120929931264L };
/*      */ 
/*      */   
/*  632 */   static final long[] jjbitVec10 = new long[] { 576460743713488896L, -562949953419266L, 9007199254740991999L, 412319973375L };
/*      */ 
/*      */   
/*  635 */   static final long[] jjbitVec11 = new long[] { 2594073385365405664L, 17163091968L, 271902628478820320L, 844440767823872L };
/*      */ 
/*      */   
/*  638 */   static final long[] jjbitVec12 = new long[] { 247132830528276448L, 7881300924956672L, 2589004636761075680L, 4294967296L };
/*      */ 
/*      */   
/*  641 */   static final long[] jjbitVec13 = new long[] { 2579997437506199520L, 15837691904L, 270153412153034720L, 0L };
/*      */ 
/*      */   
/*  644 */   static final long[] jjbitVec14 = new long[] { 283724577500946400L, 12884901888L, 283724577500946400L, 13958643712L };
/*      */ 
/*      */   
/*  647 */   static final long[] jjbitVec15 = new long[] { 288228177128316896L, 12884901888L, 0L, 0L };
/*      */ 
/*      */   
/*  650 */   static final long[] jjbitVec16 = new long[] { 3799912185593854L, 63L, 2309621682768192918L, 31L };
/*      */ 
/*      */   
/*  653 */   static final long[] jjbitVec17 = new long[] { 0L, 4398046510847L, 0L, 0L };
/*      */ 
/*      */   
/*  656 */   static final long[] jjbitVec18 = new long[] { 0L, 0L, -4294967296L, 36028797018898495L };
/*      */ 
/*      */   
/*  659 */   static final long[] jjbitVec19 = new long[] { 5764607523034749677L, 12493387738468353L, -756383734487318528L, 144405459145588743L };
/*      */ 
/*      */   
/*  662 */   static final long[] jjbitVec20 = new long[] { -1L, -1L, -4026531841L, 288230376151711743L };
/*      */ 
/*      */   
/*  665 */   static final long[] jjbitVec21 = new long[] { -3233808385L, 4611686017001275199L, 6908521828386340863L, 2295745090394464220L };
/*      */ 
/*      */   
/*  668 */   static final long[] jjbitVec22 = new long[] { 83837761617920L, 0L, 7L, 0L };
/*      */ 
/*      */   
/*  671 */   static final long[] jjbitVec23 = new long[] { 4389456576640L, -2L, -8587837441L, 576460752303423487L };
/*      */ 
/*      */   
/*  674 */   static final long[] jjbitVec24 = new long[] { 35184372088800L, 0L, 0L, 0L };
/*      */ 
/*      */   
/*  677 */   static final long[] jjbitVec25 = new long[] { -1L, -1L, 274877906943L, 0L };
/*      */ 
/*      */   
/*  680 */   static final long[] jjbitVec26 = new long[] { -1L, -1L, 68719476735L, 0L };
/*      */ 
/*      */   
/*  683 */   static final long[] jjbitVec27 = new long[] { 0L, 0L, 36028797018963968L, -36028797027352577L };
/*      */ 
/*      */   
/*  686 */   static final long[] jjbitVec28 = new long[] { 16777215L, -65536L, -576458553280167937L, 196611L };
/*      */ 
/*      */   
/*  689 */   static final long[] jjbitVec29 = new long[] { -1L, 12884901951L, -17179879488L, 4503588160110591L };
/*      */ 
/*      */   
/*  692 */   static final long[] jjbitVec30 = new long[] { -8194L, -536936449L, -65413L, 234134404065073567L };
/*      */ 
/*      */   
/*  695 */   static final long[] jjbitVec31 = new long[] { -562949953421312L, -8547991553L, -4899916411759099777L, 1979120929931286L };
/*      */ 
/*      */   
/*  698 */   static final long[] jjbitVec32 = new long[] { 576460743713488896L, -277081224642561L, 9007199254740991999L, 288017070894841855L };
/*      */ 
/*      */   
/*  701 */   static final long[] jjbitVec33 = new long[] { -864691128455135250L, 281268803485695L, -3186861885341720594L, 1125692414638495L };
/*      */ 
/*      */   
/*  704 */   static final long[] jjbitVec34 = new long[] { -3211631683292264476L, 9006925953907079L, -869759877059465234L, 281204393786303L };
/*      */ 
/*      */   
/*  707 */   static final long[] jjbitVec35 = new long[] { -878767076314341394L, 281215949093263L, -4341532606274353172L, 280925229301191L };
/*      */ 
/*      */   
/*  710 */   static final long[] jjbitVec36 = new long[] { -4327961440926441490L, 281212990012895L, -4327961440926441492L, 281214063754719L };
/*      */ 
/*      */   
/*  713 */   static final long[] jjbitVec37 = new long[] { -4323457841299070996L, 281212992110031L, 0L, 0L };
/*      */ 
/*      */   
/*  716 */   static final long[] jjbitVec38 = new long[] { 576320014815068158L, 67076095L, 4323293666156225942L, 67059551L };
/*      */ 
/*      */   
/*  719 */   static final long[] jjbitVec39 = new long[] { -4422530440275951616L, -558551906910465L, 215680200883507167L, 0L };
/*      */ 
/*      */   
/*  722 */   static final long[] jjbitVec40 = new long[] { 0L, 0L, 0L, 9126739968L };
/*      */ 
/*      */   
/*  725 */   static final long[] jjbitVec41 = new long[] { 17732914942836896L, -2L, -6876561409L, 8646911284551352319L };
/*      */ 
/*      */ 
/*      */   
/*      */   private int jjMoveNfa_0(int startState, int curPos) {
/*  730 */     int startsAt = 0;
/*  731 */     this.jjnewStateCnt = 43;
/*  732 */     int i = 1;
/*  733 */     this.jjstateSet[0] = startState;
/*  734 */     int kind = Integer.MAX_VALUE;
/*      */     
/*      */     while (true) {
/*  737 */       if (++this.jjround == Integer.MAX_VALUE)
/*  738 */         ReInitRounds(); 
/*  739 */       if (this.curChar < '@') {
/*      */         
/*  741 */         long l = 1L << this.curChar;
/*      */         
/*      */         do {
/*  744 */           switch (this.jjstateSet[--i]) {
/*      */             
/*      */             case 3:
/*  747 */               if ((0xFFFFFFFFFFFFF9FFL & l) != 0L)
/*      */               {
/*  749 */                 if (kind > 60)
/*  750 */                   kind = 60; 
/*      */               }
/*  752 */               if ((0x100000601L & l) != 0L) {
/*      */                 
/*  754 */                 if (kind > 39)
/*  755 */                   kind = 39; 
/*  756 */                 jjCheckNAdd(0);
/*      */               }
/*  758 */               else if (this.curChar == '\'') {
/*  759 */                 this.jjstateSet[this.jjnewStateCnt++] = 31;
/*  760 */               } else if (this.curChar == '"') {
/*  761 */                 this.jjstateSet[this.jjnewStateCnt++] = 22;
/*  762 */               } else if (this.curChar == '#') {
/*      */                 
/*  764 */                 if (kind > 42)
/*  765 */                   kind = 42; 
/*  766 */                 jjCheckNAdd(5);
/*      */               } 
/*  768 */               if (this.curChar == '\'') {
/*  769 */                 jjCheckNAddTwoStates(13, 14); break;
/*  770 */               }  if (this.curChar == '"') {
/*  771 */                 jjCheckNAddTwoStates(10, 11); break;
/*  772 */               }  if (this.curChar == '#')
/*  773 */                 this.jjstateSet[this.jjnewStateCnt++] = 1; 
/*      */               break;
/*      */             case 43:
/*  776 */               if ((0x3FF600000000000L & l) != 0L) {
/*  777 */                 jjCheckNAddTwoStates(39, 40);
/*  778 */               } else if (this.curChar == ':') {
/*  779 */                 this.jjstateSet[this.jjnewStateCnt++] = 41;
/*  780 */               }  if ((0x3FF600000000000L & l) != 0L) {
/*  781 */                 jjCheckNAddTwoStates(36, 38);
/*  782 */               } else if (this.curChar == ':') {
/*  783 */                 this.jjstateSet[this.jjnewStateCnt++] = 37;
/*  784 */               }  if ((0x3FF600000000000L & l) != 0L) {
/*      */                 
/*  786 */                 if (kind > 54)
/*  787 */                   kind = 54; 
/*  788 */                 jjCheckNAdd(35);
/*      */               } 
/*      */               break;
/*      */             case 0:
/*  792 */               if ((0x100000601L & l) == 0L)
/*      */                 break; 
/*  794 */               if (kind > 39)
/*  795 */                 kind = 39; 
/*  796 */               jjCheckNAdd(0);
/*      */               break;
/*      */             case 1:
/*  799 */               if (this.curChar != '#')
/*      */                 break; 
/*  801 */               if (kind > 40)
/*  802 */                 kind = 40; 
/*  803 */               jjCheckNAdd(2);
/*      */               break;
/*      */             case 2:
/*  806 */               if ((0xFFFFFFFFFFFFFBFEL & l) == 0L)
/*      */                 break; 
/*  808 */               if (kind > 40)
/*  809 */                 kind = 40; 
/*  810 */               jjCheckNAdd(2);
/*      */               break;
/*      */             case 4:
/*  813 */               if (this.curChar != '#')
/*      */                 break; 
/*  815 */               if (kind > 42)
/*  816 */                 kind = 42; 
/*  817 */               jjCheckNAdd(5);
/*      */               break;
/*      */             case 5:
/*  820 */               if ((0xFFFFFFFFFFFFFBFEL & l) == 0L)
/*      */                 break; 
/*  822 */               if (kind > 42)
/*  823 */                 kind = 42; 
/*  824 */               jjCheckNAdd(5);
/*      */               break;
/*      */             case 8:
/*  827 */               if ((0x3FF600000000000L & l) == 0L)
/*      */                 break; 
/*  829 */               if (kind > 55)
/*  830 */                 kind = 55; 
/*  831 */               this.jjstateSet[this.jjnewStateCnt++] = 8;
/*      */               break;
/*      */             case 9:
/*  834 */               if (this.curChar == '"')
/*  835 */                 jjCheckNAddTwoStates(10, 11); 
/*      */               break;
/*      */             case 10:
/*  838 */               if ((0xFFFFFFFBFFFFFFFEL & l) != 0L)
/*  839 */                 jjCheckNAddTwoStates(10, 11); 
/*      */               break;
/*      */             case 11:
/*      */             case 20:
/*  843 */               if (this.curChar == '"' && kind > 58)
/*  844 */                 kind = 58; 
/*      */               break;
/*      */             case 12:
/*  847 */               if (this.curChar == '\'')
/*  848 */                 jjCheckNAddTwoStates(13, 14); 
/*      */               break;
/*      */             case 13:
/*  851 */               if ((0xFFFFFF7FFFFFFFFEL & l) != 0L)
/*  852 */                 jjCheckNAddTwoStates(13, 14); 
/*      */               break;
/*      */             case 14:
/*      */             case 29:
/*  856 */               if (this.curChar == '\'' && kind > 58)
/*  857 */                 kind = 58; 
/*      */               break;
/*      */             case 15:
/*  860 */               if (this.curChar == '"')
/*  861 */                 jjCheckNAddStates(0, 2); 
/*      */               break;
/*      */             case 16:
/*  864 */               if ((0xFFFFFFFBFFFFFFFFL & l) != 0L)
/*  865 */                 jjCheckNAddStates(0, 2); 
/*      */               break;
/*      */             case 17:
/*      */             case 19:
/*  869 */               if (this.curChar == '"')
/*  870 */                 jjCheckNAdd(16); 
/*      */               break;
/*      */             case 18:
/*  873 */               if (this.curChar == '"')
/*  874 */                 jjAddStates(3, 4); 
/*      */               break;
/*      */             case 21:
/*  877 */               if (this.curChar == '"')
/*  878 */                 this.jjstateSet[this.jjnewStateCnt++] = 20; 
/*      */               break;
/*      */             case 22:
/*  881 */               if (this.curChar == '"')
/*  882 */                 this.jjstateSet[this.jjnewStateCnt++] = 15; 
/*      */               break;
/*      */             case 23:
/*  885 */               if (this.curChar == '"')
/*  886 */                 this.jjstateSet[this.jjnewStateCnt++] = 22; 
/*      */               break;
/*      */             case 24:
/*  889 */               if (this.curChar == '\'')
/*  890 */                 jjCheckNAddStates(5, 7); 
/*      */               break;
/*      */             case 25:
/*  893 */               if ((0xFFFFFF7FFFFFFFFFL & l) != 0L)
/*  894 */                 jjCheckNAddStates(5, 7); 
/*      */               break;
/*      */             case 26:
/*      */             case 28:
/*  898 */               if (this.curChar == '\'')
/*  899 */                 jjCheckNAdd(25); 
/*      */               break;
/*      */             case 27:
/*  902 */               if (this.curChar == '\'')
/*  903 */                 jjAddStates(8, 9); 
/*      */               break;
/*      */             case 30:
/*  906 */               if (this.curChar == '\'')
/*  907 */                 this.jjstateSet[this.jjnewStateCnt++] = 29; 
/*      */               break;
/*      */             case 31:
/*  910 */               if (this.curChar == '\'')
/*  911 */                 this.jjstateSet[this.jjnewStateCnt++] = 24; 
/*      */               break;
/*      */             case 32:
/*  914 */               if (this.curChar == '\'')
/*  915 */                 this.jjstateSet[this.jjnewStateCnt++] = 31; 
/*      */               break;
/*      */             case 33:
/*  918 */               if ((0xFFFFFFFFFFFFF9FFL & l) != 0L && kind > 60)
/*  919 */                 kind = 60; 
/*      */               break;
/*      */             case 35:
/*  922 */               if ((0x3FF600000000000L & l) == 0L)
/*      */                 break; 
/*  924 */               if (kind > 54)
/*  925 */                 kind = 54; 
/*  926 */               jjCheckNAdd(35);
/*      */               break;
/*      */             case 36:
/*  929 */               if ((0x3FF600000000000L & l) != 0L)
/*  930 */                 jjCheckNAddTwoStates(36, 38); 
/*      */               break;
/*      */             case 37:
/*  933 */               if (this.curChar == '*' && kind > 56)
/*  934 */                 kind = 56; 
/*      */               break;
/*      */             case 38:
/*  937 */               if (this.curChar == ':')
/*  938 */                 this.jjstateSet[this.jjnewStateCnt++] = 37; 
/*      */               break;
/*      */             case 39:
/*  941 */               if ((0x3FF600000000000L & l) != 0L)
/*  942 */                 jjCheckNAddTwoStates(39, 40); 
/*      */               break;
/*      */             case 40:
/*  945 */               if (this.curChar == ':')
/*  946 */                 this.jjstateSet[this.jjnewStateCnt++] = 41; 
/*      */               break;
/*      */             case 42:
/*  949 */               if ((0x3FF600000000000L & l) == 0L)
/*      */                 break; 
/*  951 */               if (kind > 57)
/*  952 */                 kind = 57; 
/*  953 */               this.jjstateSet[this.jjnewStateCnt++] = 42;
/*      */               break;
/*      */           } 
/*      */         
/*  957 */         } while (i != startsAt);
/*      */       }
/*  959 */       else if (this.curChar < '') {
/*      */         
/*  961 */         long l = 1L << (this.curChar & 0x3F);
/*      */         
/*      */         do {
/*  964 */           switch (this.jjstateSet[--i]) {
/*      */             
/*      */             case 3:
/*  967 */               if (kind > 60)
/*  968 */                 kind = 60; 
/*  969 */               if ((0x7FFFFFE87FFFFFEL & l) != 0L) {
/*      */                 
/*  971 */                 if (kind > 54)
/*  972 */                   kind = 54; 
/*  973 */                 jjCheckNAddStates(10, 14); break;
/*      */               } 
/*  975 */               if (this.curChar == '\\')
/*  976 */                 this.jjstateSet[this.jjnewStateCnt++] = 7; 
/*      */               break;
/*      */             case 43:
/*  979 */               if ((0x7FFFFFE87FFFFFEL & l) != 0L)
/*  980 */                 jjCheckNAddTwoStates(39, 40); 
/*  981 */               if ((0x7FFFFFE87FFFFFEL & l) != 0L)
/*  982 */                 jjCheckNAddTwoStates(36, 38); 
/*  983 */               if ((0x7FFFFFE87FFFFFEL & l) != 0L) {
/*      */                 
/*  985 */                 if (kind > 54)
/*  986 */                   kind = 54; 
/*  987 */                 jjCheckNAdd(35);
/*      */               } 
/*      */               break;
/*      */             case 2:
/*  991 */               if (kind > 40)
/*  992 */                 kind = 40; 
/*  993 */               this.jjstateSet[this.jjnewStateCnt++] = 2;
/*      */               break;
/*      */             case 5:
/*  996 */               if (kind > 42)
/*  997 */                 kind = 42; 
/*  998 */               this.jjstateSet[this.jjnewStateCnt++] = 5;
/*      */               break;
/*      */             case 6:
/* 1001 */               if (this.curChar == '\\')
/* 1002 */                 this.jjstateSet[this.jjnewStateCnt++] = 7; 
/*      */               break;
/*      */             case 7:
/*      */             case 8:
/* 1006 */               if ((0x7FFFFFE87FFFFFEL & l) == 0L)
/*      */                 break; 
/* 1008 */               if (kind > 55)
/* 1009 */                 kind = 55; 
/* 1010 */               jjCheckNAdd(8);
/*      */               break;
/*      */             case 10:
/* 1013 */               jjAddStates(15, 16);
/*      */               break;
/*      */             case 13:
/* 1016 */               jjAddStates(17, 18);
/*      */               break;
/*      */             case 16:
/* 1019 */               jjAddStates(0, 2);
/*      */               break;
/*      */             case 25:
/* 1022 */               jjAddStates(5, 7);
/*      */               break;
/*      */             case 33:
/* 1025 */               if (kind > 60)
/* 1026 */                 kind = 60; 
/*      */               break;
/*      */             case 34:
/* 1029 */               if ((0x7FFFFFE87FFFFFEL & l) == 0L)
/*      */                 break; 
/* 1031 */               if (kind > 54)
/* 1032 */                 kind = 54; 
/* 1033 */               jjCheckNAddStates(10, 14);
/*      */               break;
/*      */             case 35:
/* 1036 */               if ((0x7FFFFFE87FFFFFEL & l) == 0L)
/*      */                 break; 
/* 1038 */               if (kind > 54)
/* 1039 */                 kind = 54; 
/* 1040 */               jjCheckNAdd(35);
/*      */               break;
/*      */             case 36:
/* 1043 */               if ((0x7FFFFFE87FFFFFEL & l) != 0L)
/* 1044 */                 jjCheckNAddTwoStates(36, 38); 
/*      */               break;
/*      */             case 39:
/* 1047 */               if ((0x7FFFFFE87FFFFFEL & l) != 0L)
/* 1048 */                 jjCheckNAddTwoStates(39, 40); 
/*      */               break;
/*      */             case 41:
/*      */             case 42:
/* 1052 */               if ((0x7FFFFFE87FFFFFEL & l) == 0L)
/*      */                 break; 
/* 1054 */               if (kind > 57)
/* 1055 */                 kind = 57; 
/* 1056 */               jjCheckNAdd(42);
/*      */               break;
/*      */           } 
/*      */         
/* 1060 */         } while (i != startsAt);
/*      */       }
/*      */       else {
/*      */         
/* 1064 */         int hiByte = this.curChar >> 8;
/* 1065 */         int i1 = hiByte >> 6;
/* 1066 */         long l1 = 1L << (hiByte & 0x3F);
/* 1067 */         int i2 = (this.curChar & 0xFF) >> 6;
/* 1068 */         long l2 = 1L << (this.curChar & 0x3F);
/*      */         
/*      */         do {
/* 1071 */           switch (this.jjstateSet[--i]) {
/*      */             
/*      */             case 3:
/* 1074 */               if (jjCanMove_0(hiByte, i1, i2, l1, l2))
/*      */               {
/* 1076 */                 if (kind > 60)
/* 1077 */                   kind = 60; 
/*      */               }
/* 1079 */               if (jjCanMove_1(hiByte, i1, i2, l1, l2)) {
/*      */                 
/* 1081 */                 if (kind > 54)
/* 1082 */                   kind = 54; 
/* 1083 */                 jjCheckNAddStates(10, 14);
/*      */               } 
/*      */               break;
/*      */             case 43:
/* 1087 */               if (jjCanMove_2(hiByte, i1, i2, l1, l2)) {
/*      */                 
/* 1089 */                 if (kind > 54)
/* 1090 */                   kind = 54; 
/* 1091 */                 jjCheckNAdd(35);
/*      */               } 
/* 1093 */               if (jjCanMove_2(hiByte, i1, i2, l1, l2))
/* 1094 */                 jjCheckNAddTwoStates(36, 38); 
/* 1095 */               if (jjCanMove_2(hiByte, i1, i2, l1, l2))
/* 1096 */                 jjCheckNAddTwoStates(39, 40); 
/*      */               break;
/*      */             case 2:
/* 1099 */               if (!jjCanMove_0(hiByte, i1, i2, l1, l2))
/*      */                 break; 
/* 1101 */               if (kind > 40)
/* 1102 */                 kind = 40; 
/* 1103 */               this.jjstateSet[this.jjnewStateCnt++] = 2;
/*      */               break;
/*      */             case 5:
/* 1106 */               if (!jjCanMove_0(hiByte, i1, i2, l1, l2))
/*      */                 break; 
/* 1108 */               if (kind > 42)
/* 1109 */                 kind = 42; 
/* 1110 */               this.jjstateSet[this.jjnewStateCnt++] = 5;
/*      */               break;
/*      */             case 7:
/* 1113 */               if (!jjCanMove_1(hiByte, i1, i2, l1, l2))
/*      */                 break; 
/* 1115 */               if (kind > 55)
/* 1116 */                 kind = 55; 
/* 1117 */               jjCheckNAdd(8);
/*      */               break;
/*      */             case 8:
/* 1120 */               if (!jjCanMove_2(hiByte, i1, i2, l1, l2))
/*      */                 break; 
/* 1122 */               if (kind > 55)
/* 1123 */                 kind = 55; 
/* 1124 */               jjCheckNAdd(8);
/*      */               break;
/*      */             case 10:
/* 1127 */               if (jjCanMove_0(hiByte, i1, i2, l1, l2))
/* 1128 */                 jjAddStates(15, 16); 
/*      */               break;
/*      */             case 13:
/* 1131 */               if (jjCanMove_0(hiByte, i1, i2, l1, l2))
/* 1132 */                 jjAddStates(17, 18); 
/*      */               break;
/*      */             case 16:
/* 1135 */               if (jjCanMove_0(hiByte, i1, i2, l1, l2))
/* 1136 */                 jjAddStates(0, 2); 
/*      */               break;
/*      */             case 25:
/* 1139 */               if (jjCanMove_0(hiByte, i1, i2, l1, l2))
/* 1140 */                 jjAddStates(5, 7); 
/*      */               break;
/*      */             case 33:
/* 1143 */               if (jjCanMove_0(hiByte, i1, i2, l1, l2) && kind > 60)
/* 1144 */                 kind = 60; 
/*      */               break;
/*      */             case 34:
/* 1147 */               if (!jjCanMove_1(hiByte, i1, i2, l1, l2))
/*      */                 break; 
/* 1149 */               if (kind > 54)
/* 1150 */                 kind = 54; 
/* 1151 */               jjCheckNAddStates(10, 14);
/*      */               break;
/*      */             case 35:
/* 1154 */               if (!jjCanMove_2(hiByte, i1, i2, l1, l2))
/*      */                 break; 
/* 1156 */               if (kind > 54)
/* 1157 */                 kind = 54; 
/* 1158 */               jjCheckNAdd(35);
/*      */               break;
/*      */             case 36:
/* 1161 */               if (jjCanMove_2(hiByte, i1, i2, l1, l2))
/* 1162 */                 jjCheckNAddTwoStates(36, 38); 
/*      */               break;
/*      */             case 39:
/* 1165 */               if (jjCanMove_2(hiByte, i1, i2, l1, l2))
/* 1166 */                 jjCheckNAddTwoStates(39, 40); 
/*      */               break;
/*      */             case 41:
/* 1169 */               if (!jjCanMove_1(hiByte, i1, i2, l1, l2))
/*      */                 break; 
/* 1171 */               if (kind > 57)
/* 1172 */                 kind = 57; 
/* 1173 */               jjCheckNAdd(42);
/*      */               break;
/*      */             case 42:
/* 1176 */               if (!jjCanMove_2(hiByte, i1, i2, l1, l2))
/*      */                 break; 
/* 1178 */               if (kind > 57)
/* 1179 */                 kind = 57; 
/* 1180 */               jjCheckNAdd(42);
/*      */               break;
/*      */           } 
/*      */         
/* 1184 */         } while (i != startsAt);
/*      */       } 
/* 1186 */       if (kind != Integer.MAX_VALUE) {
/*      */         
/* 1188 */         this.jjmatchedKind = kind;
/* 1189 */         this.jjmatchedPos = curPos;
/* 1190 */         kind = Integer.MAX_VALUE;
/*      */       } 
/* 1192 */       curPos++;
/* 1193 */       if ((i = this.jjnewStateCnt) == (startsAt = 43 - (this.jjnewStateCnt = startsAt)))
/* 1194 */         return curPos;  
/* 1195 */       try { this.curChar = this.input_stream.readChar(); }
/* 1196 */       catch (IOException e) { return curPos; }
/*      */     
/*      */     } 
/*      */   }
/*      */   private int jjMoveStringLiteralDfa0_1() {
/* 1201 */     return jjMoveNfa_1(1, 0);
/*      */   }
/*      */   
/*      */   private int jjMoveNfa_1(int startState, int curPos) {
/* 1205 */     int startsAt = 0;
/* 1206 */     this.jjnewStateCnt = 10;
/* 1207 */     int i = 1;
/* 1208 */     this.jjstateSet[0] = startState;
/* 1209 */     int kind = Integer.MAX_VALUE;
/*      */     
/*      */     while (true) {
/* 1212 */       if (++this.jjround == Integer.MAX_VALUE)
/* 1213 */         ReInitRounds(); 
/* 1214 */       if (this.curChar < '@') {
/*      */         
/* 1216 */         long l = 1L << this.curChar;
/*      */         
/*      */         do {
/* 1219 */           switch (this.jjstateSet[--i]) {
/*      */             
/*      */             case 1:
/* 1222 */               if ((0xFFFFFFFFFFFFF9FFL & l) != 0L)
/*      */               {
/* 1224 */                 if (kind > 60)
/* 1225 */                   kind = 60; 
/*      */               }
/* 1227 */               if ((0x100000601L & l) != 0L) {
/*      */                 
/* 1229 */                 if (kind > 39)
/* 1230 */                   kind = 39; 
/* 1231 */                 jjCheckNAdd(0);
/*      */               } 
/* 1233 */               if ((0x401L & l) != 0L)
/* 1234 */                 jjCheckNAddStates(19, 22); 
/*      */               break;
/*      */             case 0:
/* 1237 */               if ((0x100000601L & l) == 0L)
/*      */                 break; 
/* 1239 */               if (kind > 39)
/* 1240 */                 kind = 39; 
/* 1241 */               jjCheckNAdd(0);
/*      */               break;
/*      */             case 2:
/* 1244 */               if ((0x401L & l) != 0L)
/* 1245 */                 jjCheckNAddStates(19, 22); 
/*      */               break;
/*      */             case 3:
/* 1248 */               if ((0x100000200L & l) != 0L)
/* 1249 */                 jjCheckNAddTwoStates(3, 6); 
/*      */               break;
/*      */             case 4:
/* 1252 */               if (this.curChar != '#')
/*      */                 break; 
/* 1254 */               if (kind > 43)
/* 1255 */                 kind = 43; 
/* 1256 */               jjCheckNAdd(5);
/*      */               break;
/*      */             case 5:
/* 1259 */               if ((0xFFFFFFFFFFFFFBFEL & l) == 0L)
/*      */                 break; 
/* 1261 */               if (kind > 43)
/* 1262 */                 kind = 43; 
/* 1263 */               jjCheckNAdd(5);
/*      */               break;
/*      */             case 6:
/* 1266 */               if (this.curChar == '#')
/* 1267 */                 this.jjstateSet[this.jjnewStateCnt++] = 4; 
/*      */               break;
/*      */             case 7:
/* 1270 */               if ((0x100000200L & l) != 0L)
/* 1271 */                 jjCheckNAddTwoStates(7, 8); 
/*      */               break;
/*      */             case 8:
/* 1274 */               if (this.curChar != '#')
/*      */                 break; 
/* 1276 */               if (kind > 44)
/* 1277 */                 kind = 44; 
/* 1278 */               jjCheckNAdd(9);
/*      */               break;
/*      */             case 9:
/* 1281 */               if ((0xFFFFFFFFFFFFFBFEL & l) == 0L)
/*      */                 break; 
/* 1283 */               if (kind > 44)
/* 1284 */                 kind = 44; 
/* 1285 */               jjCheckNAdd(9);
/*      */               break;
/*      */           } 
/*      */         
/* 1289 */         } while (i != startsAt);
/*      */       }
/* 1291 */       else if (this.curChar < '') {
/*      */         
/* 1293 */         long l = 1L << (this.curChar & 0x3F);
/*      */         
/*      */         do {
/* 1296 */           switch (this.jjstateSet[--i]) {
/*      */             
/*      */             case 1:
/* 1299 */               if (kind > 60)
/* 1300 */                 kind = 60; 
/*      */               break;
/*      */             case 5:
/* 1303 */               if (kind > 43)
/* 1304 */                 kind = 43; 
/* 1305 */               this.jjstateSet[this.jjnewStateCnt++] = 5;
/*      */               break;
/*      */             case 9:
/* 1308 */               if (kind > 44)
/* 1309 */                 kind = 44; 
/* 1310 */               this.jjstateSet[this.jjnewStateCnt++] = 9;
/*      */               break;
/*      */           } 
/*      */         
/* 1314 */         } while (i != startsAt);
/*      */       }
/*      */       else {
/*      */         
/* 1318 */         int hiByte = this.curChar >> 8;
/* 1319 */         int i1 = hiByte >> 6;
/* 1320 */         long l1 = 1L << (hiByte & 0x3F);
/* 1321 */         int i2 = (this.curChar & 0xFF) >> 6;
/* 1322 */         long l2 = 1L << (this.curChar & 0x3F);
/*      */         
/*      */         do {
/* 1325 */           switch (this.jjstateSet[--i]) {
/*      */             
/*      */             case 1:
/* 1328 */               if (jjCanMove_0(hiByte, i1, i2, l1, l2) && kind > 60)
/* 1329 */                 kind = 60; 
/*      */               break;
/*      */             case 5:
/* 1332 */               if (!jjCanMove_0(hiByte, i1, i2, l1, l2))
/*      */                 break; 
/* 1334 */               if (kind > 43)
/* 1335 */                 kind = 43; 
/* 1336 */               this.jjstateSet[this.jjnewStateCnt++] = 5;
/*      */               break;
/*      */             case 9:
/* 1339 */               if (!jjCanMove_0(hiByte, i1, i2, l1, l2))
/*      */                 break; 
/* 1341 */               if (kind > 44)
/* 1342 */                 kind = 44; 
/* 1343 */               this.jjstateSet[this.jjnewStateCnt++] = 9;
/*      */               break;
/*      */           } 
/*      */         
/* 1347 */         } while (i != startsAt);
/*      */       } 
/* 1349 */       if (kind != Integer.MAX_VALUE) {
/*      */         
/* 1351 */         this.jjmatchedKind = kind;
/* 1352 */         this.jjmatchedPos = curPos;
/* 1353 */         kind = Integer.MAX_VALUE;
/*      */       } 
/* 1355 */       curPos++;
/* 1356 */       if ((i = this.jjnewStateCnt) == (startsAt = 10 - (this.jjnewStateCnt = startsAt)))
/* 1357 */         return curPos;  
/* 1358 */       try { this.curChar = this.input_stream.readChar(); }
/* 1359 */       catch (IOException e) { return curPos; }
/*      */     
/*      */     } 
/*      */   }
/*      */   private int jjMoveStringLiteralDfa0_2() {
/* 1364 */     return jjMoveNfa_2(1, 0);
/*      */   }
/*      */   
/*      */   private int jjMoveNfa_2(int startState, int curPos) {
/* 1368 */     int startsAt = 0;
/* 1369 */     this.jjnewStateCnt = 7;
/* 1370 */     int i = 1;
/* 1371 */     this.jjstateSet[0] = startState;
/* 1372 */     int kind = Integer.MAX_VALUE;
/*      */     
/*      */     while (true) {
/* 1375 */       if (++this.jjround == Integer.MAX_VALUE)
/* 1376 */         ReInitRounds(); 
/* 1377 */       if (this.curChar < '@') {
/*      */         
/* 1379 */         long l = 1L << this.curChar;
/*      */         
/*      */         do {
/* 1382 */           switch (this.jjstateSet[--i]) {
/*      */             
/*      */             case 1:
/* 1385 */               if ((0xFFFFFFFFFFFFF9FFL & l) != 0L)
/*      */               {
/* 1387 */                 if (kind > 60)
/* 1388 */                   kind = 60; 
/*      */               }
/* 1390 */               if ((0x100000601L & l) != 0L) {
/*      */                 
/* 1392 */                 if (kind > 39)
/* 1393 */                   kind = 39; 
/* 1394 */                 jjCheckNAdd(0);
/*      */               } 
/* 1396 */               if ((0x401L & l) != 0L)
/* 1397 */                 jjCheckNAddTwoStates(2, 5); 
/*      */               break;
/*      */             case 0:
/* 1400 */               if ((0x100000601L & l) == 0L)
/*      */                 break; 
/* 1402 */               if (kind > 39)
/* 1403 */                 kind = 39; 
/* 1404 */               jjCheckNAdd(0);
/*      */               break;
/*      */             case 2:
/* 1407 */               if ((0x100000200L & l) != 0L)
/* 1408 */                 jjCheckNAddTwoStates(2, 5); 
/*      */               break;
/*      */             case 3:
/* 1411 */               if (this.curChar != '#')
/*      */                 break; 
/* 1413 */               if (kind > 41)
/* 1414 */                 kind = 41; 
/* 1415 */               jjCheckNAdd(4);
/*      */               break;
/*      */             case 4:
/* 1418 */               if ((0xFFFFFFFFFFFFFBFEL & l) == 0L)
/*      */                 break; 
/* 1420 */               if (kind > 41)
/* 1421 */                 kind = 41; 
/* 1422 */               jjCheckNAdd(4);
/*      */               break;
/*      */             case 5:
/* 1425 */               if (this.curChar == '#')
/* 1426 */                 this.jjstateSet[this.jjnewStateCnt++] = 3; 
/*      */               break;
/*      */             case 6:
/* 1429 */               if ((0xFFFFFFFFFFFFF9FFL & l) != 0L && kind > 60) {
/* 1430 */                 kind = 60;
/*      */               }
/*      */               break;
/*      */           } 
/* 1434 */         } while (i != startsAt);
/*      */       }
/* 1436 */       else if (this.curChar < '') {
/*      */         
/* 1438 */         long l = 1L << (this.curChar & 0x3F);
/*      */         
/*      */         do {
/* 1441 */           switch (this.jjstateSet[--i]) {
/*      */             
/*      */             case 1:
/* 1444 */               if (kind > 60)
/* 1445 */                 kind = 60; 
/*      */               break;
/*      */             case 4:
/* 1448 */               if (kind > 41)
/* 1449 */                 kind = 41; 
/* 1450 */               this.jjstateSet[this.jjnewStateCnt++] = 4;
/*      */               break;
/*      */           } 
/*      */         
/* 1454 */         } while (i != startsAt);
/*      */       }
/*      */       else {
/*      */         
/* 1458 */         int hiByte = this.curChar >> 8;
/* 1459 */         int i1 = hiByte >> 6;
/* 1460 */         long l1 = 1L << (hiByte & 0x3F);
/* 1461 */         int i2 = (this.curChar & 0xFF) >> 6;
/* 1462 */         long l2 = 1L << (this.curChar & 0x3F);
/*      */         
/*      */         do {
/* 1465 */           switch (this.jjstateSet[--i]) {
/*      */             
/*      */             case 1:
/* 1468 */               if (jjCanMove_0(hiByte, i1, i2, l1, l2) && kind > 60)
/* 1469 */                 kind = 60; 
/*      */               break;
/*      */             case 4:
/* 1472 */               if (!jjCanMove_0(hiByte, i1, i2, l1, l2))
/*      */                 break; 
/* 1474 */               if (kind > 41)
/* 1475 */                 kind = 41; 
/* 1476 */               this.jjstateSet[this.jjnewStateCnt++] = 4;
/*      */               break;
/*      */           } 
/*      */         
/* 1480 */         } while (i != startsAt);
/*      */       } 
/* 1482 */       if (kind != Integer.MAX_VALUE) {
/*      */         
/* 1484 */         this.jjmatchedKind = kind;
/* 1485 */         this.jjmatchedPos = curPos;
/* 1486 */         kind = Integer.MAX_VALUE;
/*      */       } 
/* 1488 */       curPos++;
/* 1489 */       if ((i = this.jjnewStateCnt) == (startsAt = 7 - (this.jjnewStateCnt = startsAt)))
/* 1490 */         return curPos;  
/* 1491 */       try { this.curChar = this.input_stream.readChar(); }
/* 1492 */       catch (IOException e) { return curPos; }
/*      */     
/*      */     } 
/* 1495 */   } static final int[] jjnextStates = new int[] { 16, 17, 18, 19, 21, 25, 26, 27, 28, 30, 35, 36, 38, 39, 40, 10, 11, 13, 14, 3, 6, 7, 8 };
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static final boolean jjCanMove_0(int hiByte, int i1, int i2, long l1, long l2) {
/* 1501 */     switch (hiByte) {
/*      */       
/*      */       case 0:
/* 1504 */         return ((jjbitVec2[i2] & l2) != 0L);
/*      */     } 
/* 1506 */     if ((jjbitVec0[i1] & l1) != 0L)
/* 1507 */       return true; 
/* 1508 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   private static final boolean jjCanMove_1(int hiByte, int i1, int i2, long l1, long l2) {
/* 1513 */     switch (hiByte) {
/*      */       
/*      */       case 0:
/* 1516 */         return ((jjbitVec4[i2] & l2) != 0L);
/*      */       case 1:
/* 1518 */         return ((jjbitVec5[i2] & l2) != 0L);
/*      */       case 2:
/* 1520 */         return ((jjbitVec6[i2] & l2) != 0L);
/*      */       case 3:
/* 1522 */         return ((jjbitVec7[i2] & l2) != 0L);
/*      */       case 4:
/* 1524 */         return ((jjbitVec8[i2] & l2) != 0L);
/*      */       case 5:
/* 1526 */         return ((jjbitVec9[i2] & l2) != 0L);
/*      */       case 6:
/* 1528 */         return ((jjbitVec10[i2] & l2) != 0L);
/*      */       case 9:
/* 1530 */         return ((jjbitVec11[i2] & l2) != 0L);
/*      */       case 10:
/* 1532 */         return ((jjbitVec12[i2] & l2) != 0L);
/*      */       case 11:
/* 1534 */         return ((jjbitVec13[i2] & l2) != 0L);
/*      */       case 12:
/* 1536 */         return ((jjbitVec14[i2] & l2) != 0L);
/*      */       case 13:
/* 1538 */         return ((jjbitVec15[i2] & l2) != 0L);
/*      */       case 14:
/* 1540 */         return ((jjbitVec16[i2] & l2) != 0L);
/*      */       case 15:
/* 1542 */         return ((jjbitVec17[i2] & l2) != 0L);
/*      */       case 16:
/* 1544 */         return ((jjbitVec18[i2] & l2) != 0L);
/*      */       case 17:
/* 1546 */         return ((jjbitVec19[i2] & l2) != 0L);
/*      */       case 30:
/* 1548 */         return ((jjbitVec20[i2] & l2) != 0L);
/*      */       case 31:
/* 1550 */         return ((jjbitVec21[i2] & l2) != 0L);
/*      */       case 33:
/* 1552 */         return ((jjbitVec22[i2] & l2) != 0L);
/*      */       case 48:
/* 1554 */         return ((jjbitVec23[i2] & l2) != 0L);
/*      */       case 49:
/* 1556 */         return ((jjbitVec24[i2] & l2) != 0L);
/*      */       case 159:
/* 1558 */         return ((jjbitVec25[i2] & l2) != 0L);
/*      */       case 215:
/* 1560 */         return ((jjbitVec26[i2] & l2) != 0L);
/*      */     } 
/* 1562 */     if ((jjbitVec3[i1] & l1) != 0L)
/* 1563 */       return true; 
/* 1564 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   private static final boolean jjCanMove_2(int hiByte, int i1, int i2, long l1, long l2) {
/* 1569 */     switch (hiByte) {
/*      */       
/*      */       case 0:
/* 1572 */         return ((jjbitVec27[i2] & l2) != 0L);
/*      */       case 1:
/* 1574 */         return ((jjbitVec5[i2] & l2) != 0L);
/*      */       case 2:
/* 1576 */         return ((jjbitVec28[i2] & l2) != 0L);
/*      */       case 3:
/* 1578 */         return ((jjbitVec29[i2] & l2) != 0L);
/*      */       case 4:
/* 1580 */         return ((jjbitVec30[i2] & l2) != 0L);
/*      */       case 5:
/* 1582 */         return ((jjbitVec31[i2] & l2) != 0L);
/*      */       case 6:
/* 1584 */         return ((jjbitVec32[i2] & l2) != 0L);
/*      */       case 9:
/* 1586 */         return ((jjbitVec33[i2] & l2) != 0L);
/*      */       case 10:
/* 1588 */         return ((jjbitVec34[i2] & l2) != 0L);
/*      */       case 11:
/* 1590 */         return ((jjbitVec35[i2] & l2) != 0L);
/*      */       case 12:
/* 1592 */         return ((jjbitVec36[i2] & l2) != 0L);
/*      */       case 13:
/* 1594 */         return ((jjbitVec37[i2] & l2) != 0L);
/*      */       case 14:
/* 1596 */         return ((jjbitVec38[i2] & l2) != 0L);
/*      */       case 15:
/* 1598 */         return ((jjbitVec39[i2] & l2) != 0L);
/*      */       case 16:
/* 1600 */         return ((jjbitVec18[i2] & l2) != 0L);
/*      */       case 17:
/* 1602 */         return ((jjbitVec19[i2] & l2) != 0L);
/*      */       case 30:
/* 1604 */         return ((jjbitVec20[i2] & l2) != 0L);
/*      */       case 31:
/* 1606 */         return ((jjbitVec21[i2] & l2) != 0L);
/*      */       case 32:
/* 1608 */         return ((jjbitVec40[i2] & l2) != 0L);
/*      */       case 33:
/* 1610 */         return ((jjbitVec22[i2] & l2) != 0L);
/*      */       case 48:
/* 1612 */         return ((jjbitVec41[i2] & l2) != 0L);
/*      */       case 49:
/* 1614 */         return ((jjbitVec24[i2] & l2) != 0L);
/*      */       case 159:
/* 1616 */         return ((jjbitVec25[i2] & l2) != 0L);
/*      */       case 215:
/* 1618 */         return ((jjbitVec26[i2] & l2) != 0L);
/*      */     } 
/* 1620 */     if ((jjbitVec3[i1] & l1) != 0L)
/* 1621 */       return true; 
/* 1622 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/* 1627 */   public static final String[] jjstrLiteralImages = new String[] { "", "[", "=", "&=", "|=", "start", "div", "include", "~", "]", "grammar", "{", "}", "namespace", "default", "inherit", "datatypes", "empty", "text", "notAllowed", "|", "&", ",", "+", "?", "*", "element", "attribute", "(", ")", "-", "list", "mixed", "external", "parent", "string", "token", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, ">>", null };
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
/* 1640 */   public static final String[] lexStateNames = new String[] { "DEFAULT", "AFTER_SINGLE_LINE_COMMENT", "AFTER_DOCUMENTATION" };
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1647 */   public static final int[] jjnewLexState = new int[] { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 0, 2, -1, 1, 2, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 };
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1652 */   static final long[] jjtoToken = new long[] { 2287840842771070975L };
/*      */ 
/*      */   
/* 1655 */   static final long[] jjtoSkip = new long[] { 22539988369408L };
/*      */ 
/*      */   
/* 1658 */   static final long[] jjtoSpecial = new long[] { 21990232555520L };
/*      */   
/*      */   protected JavaCharStream input_stream;
/*      */   
/* 1662 */   private final int[] jjrounds = new int[43];
/* 1663 */   private final int[] jjstateSet = new int[86];
/* 1664 */   private final StringBuilder jjimage = new StringBuilder();
/* 1665 */   private StringBuilder image = this.jjimage;
/*      */   
/*      */   private int jjimageLen;
/*      */   private int lengthOfMatch;
/*      */   protected char curChar;
/*      */   int curLexState;
/*      */   int defaultLexState;
/*      */   int jjnewStateCnt;
/*      */   int jjround;
/*      */   int jjmatchedPos;
/*      */   int jjmatchedKind;
/*      */   
/*      */   public CompactSyntaxTokenManager(JavaCharStream stream, int lexState) {
/* 1678 */     this(stream);
/* 1679 */     SwitchTo(lexState);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void ReInit(JavaCharStream stream) {
/* 1685 */     this.jjmatchedPos = this.jjnewStateCnt = 0;
/* 1686 */     this.curLexState = this.defaultLexState;
/* 1687 */     this.input_stream = stream;
/* 1688 */     ReInitRounds();
/*      */   }
/*      */ 
/*      */   
/*      */   private void ReInitRounds() {
/* 1693 */     this.jjround = -2147483647;
/* 1694 */     for (int i = 43; i-- > 0;) {
/* 1695 */       this.jjrounds[i] = Integer.MIN_VALUE;
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public void ReInit(JavaCharStream stream, int lexState) {
/* 1701 */     ReInit(stream);
/* 1702 */     SwitchTo(lexState);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void SwitchTo(int lexState) {
/* 1708 */     if (lexState >= 3 || lexState < 0) {
/* 1709 */       throw new TokenMgrError("Error: Ignoring invalid lexical state : " + lexState + ". State unchanged.", 2);
/*      */     }
/* 1711 */     this.curLexState = lexState;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected Token jjFillToken() {
/* 1722 */     String im = jjstrLiteralImages[this.jjmatchedKind];
/* 1723 */     String curTokenImage = (im == null) ? this.input_stream.GetImage() : im;
/* 1724 */     int beginLine = this.input_stream.getBeginLine();
/* 1725 */     int beginColumn = this.input_stream.getBeginColumn();
/* 1726 */     int endLine = this.input_stream.getEndLine();
/* 1727 */     int endColumn = this.input_stream.getEndColumn();
/* 1728 */     Token t = Token.newToken(this.jjmatchedKind, curTokenImage);
/*      */     
/* 1730 */     t.beginLine = beginLine;
/* 1731 */     t.endLine = endLine;
/* 1732 */     t.beginColumn = beginColumn;
/* 1733 */     t.endColumn = endColumn;
/*      */     
/* 1735 */     return t;
/*      */   }
/*      */   public CompactSyntaxTokenManager(JavaCharStream stream) {
/* 1738 */     this.curLexState = 0;
/* 1739 */     this.defaultLexState = 0;
/*      */     this.input_stream = stream;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Token getNextToken() {
/* 1748 */     Token specialToken = null;
/*      */     
/* 1750 */     int curPos = 0;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     while (true) {
/*      */       try {
/* 1757 */         this.curChar = this.input_stream.BeginToken();
/*      */       }
/* 1759 */       catch (IOException e) {
/*      */         
/* 1761 */         this.jjmatchedKind = 0;
/* 1762 */         Token matchedToken = jjFillToken();
/* 1763 */         matchedToken.specialToken = specialToken;
/* 1764 */         return matchedToken;
/*      */       } 
/* 1766 */       this.image = this.jjimage;
/* 1767 */       this.image.setLength(0);
/* 1768 */       this.jjimageLen = 0;
/*      */       
/* 1770 */       switch (this.curLexState) {
/*      */         
/*      */         case 0:
/* 1773 */           this.jjmatchedKind = Integer.MAX_VALUE;
/* 1774 */           this.jjmatchedPos = 0;
/* 1775 */           curPos = jjMoveStringLiteralDfa0_0();
/*      */           break;
/*      */         case 1:
/* 1778 */           this.jjmatchedKind = Integer.MAX_VALUE;
/* 1779 */           this.jjmatchedPos = 0;
/* 1780 */           curPos = jjMoveStringLiteralDfa0_1();
/*      */           break;
/*      */         case 2:
/* 1783 */           this.jjmatchedKind = Integer.MAX_VALUE;
/* 1784 */           this.jjmatchedPos = 0;
/* 1785 */           curPos = jjMoveStringLiteralDfa0_2();
/*      */           break;
/*      */       } 
/* 1788 */       if (this.jjmatchedKind != Integer.MAX_VALUE) {
/*      */         
/* 1790 */         if (this.jjmatchedPos + 1 < curPos)
/* 1791 */           this.input_stream.backup(curPos - this.jjmatchedPos - 1); 
/* 1792 */         if ((jjtoToken[this.jjmatchedKind >> 6] & 1L << (this.jjmatchedKind & 0x3F)) != 0L) {
/*      */           
/* 1794 */           Token matchedToken = jjFillToken();
/* 1795 */           matchedToken.specialToken = specialToken;
/* 1796 */           if (jjnewLexState[this.jjmatchedKind] != -1)
/* 1797 */             this.curLexState = jjnewLexState[this.jjmatchedKind]; 
/* 1798 */           return matchedToken;
/*      */         } 
/*      */ 
/*      */         
/* 1802 */         if ((jjtoSpecial[this.jjmatchedKind >> 6] & 1L << (this.jjmatchedKind & 0x3F)) != 0L) {
/*      */           
/* 1804 */           Token matchedToken = jjFillToken();
/* 1805 */           if (specialToken == null) {
/* 1806 */             specialToken = matchedToken;
/*      */           } else {
/*      */             
/* 1809 */             matchedToken.specialToken = specialToken;
/* 1810 */             specialToken = specialToken.next = matchedToken;
/*      */           } 
/* 1812 */           SkipLexicalActions(matchedToken);
/*      */         } else {
/*      */           
/* 1815 */           SkipLexicalActions(null);
/* 1816 */         }  if (jjnewLexState[this.jjmatchedKind] != -1)
/* 1817 */           this.curLexState = jjnewLexState[this.jjmatchedKind];  continue;
/*      */       } 
/*      */       break;
/*      */     } 
/* 1821 */     int error_line = this.input_stream.getEndLine();
/* 1822 */     int error_column = this.input_stream.getEndColumn();
/* 1823 */     String error_after = null;
/* 1824 */     boolean EOFSeen = false; try {
/* 1825 */       this.input_stream.readChar(); this.input_stream.backup(1);
/* 1826 */     } catch (IOException e1) {
/* 1827 */       EOFSeen = true;
/* 1828 */       error_after = (curPos <= 1) ? "" : this.input_stream.GetImage();
/* 1829 */       if (this.curChar == '\n' || this.curChar == '\r') {
/* 1830 */         error_line++;
/* 1831 */         error_column = 0;
/*      */       } else {
/*      */         
/* 1834 */         error_column++;
/*      */       } 
/* 1836 */     }  if (!EOFSeen) {
/* 1837 */       this.input_stream.backup(1);
/* 1838 */       error_after = (curPos <= 1) ? "" : this.input_stream.GetImage();
/*      */     } 
/* 1840 */     throw new TokenMgrError(EOFSeen, this.curLexState, error_line, error_column, error_after, this.curChar, 0);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   void SkipLexicalActions(Token matchedToken) {
/* 1846 */     switch (this.jjmatchedKind) {
/*      */     
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private void jjCheckNAdd(int state) {
/* 1854 */     if (this.jjrounds[state] != this.jjround) {
/*      */       
/* 1856 */       this.jjstateSet[this.jjnewStateCnt++] = state;
/* 1857 */       this.jjrounds[state] = this.jjround;
/*      */     } 
/*      */   }
/*      */   
/*      */   private void jjAddStates(int start, int end) {
/*      */     do {
/* 1863 */       this.jjstateSet[this.jjnewStateCnt++] = jjnextStates[start];
/* 1864 */     } while (start++ != end);
/*      */   }
/*      */   
/*      */   private void jjCheckNAddTwoStates(int state1, int state2) {
/* 1868 */     jjCheckNAdd(state1);
/* 1869 */     jjCheckNAdd(state2);
/*      */   }
/*      */ 
/*      */   
/*      */   private void jjCheckNAddStates(int start, int end) {
/*      */     do {
/* 1875 */       jjCheckNAdd(jjnextStates[start]);
/* 1876 */     } while (start++ != end);
/*      */   }
/*      */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\xml\internal\rngom\parse\compact\CompactSyntaxTokenManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */