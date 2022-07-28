/*      */ package com.sun.xml.internal.xsom.impl.scd;
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
/*      */ public class SCDParserTokenManager
/*      */   implements SCDParserConstants
/*      */ {
/*   35 */   public PrintStream debugStream = System.out; public void setDebugStream(PrintStream ds) {
/*   36 */     this.debugStream = ds;
/*      */   }
/*      */   private final int jjStopStringLiteralDfa_0(int pos, long active0) {
/*   39 */     switch (pos) {
/*      */       
/*      */       case 0:
/*   42 */         if ((active0 & 0x3C08000000L) != 0L) {
/*      */           
/*   44 */           this.jjmatchedKind = 12;
/*   45 */           return 103;
/*      */         } 
/*   47 */         if ((active0 & 0x400000L) != 0L) {
/*      */           
/*   49 */           this.jjmatchedKind = 12;
/*   50 */           return 55;
/*      */         } 
/*   52 */         if ((active0 & 0x30000000000L) != 0L) {
/*      */           
/*   54 */           this.jjmatchedKind = 12;
/*   55 */           return 68;
/*      */         } 
/*   57 */         if ((active0 & 0x2000000L) != 0L) {
/*      */           
/*   59 */           this.jjmatchedKind = 12;
/*   60 */           return 81;
/*      */         } 
/*   62 */         if ((active0 & 0x200000000L) != 0L) {
/*      */           
/*   64 */           this.jjmatchedKind = 12;
/*   65 */           return 23;
/*      */         } 
/*   67 */         if ((active0 & 0x40000000000L) != 0L) {
/*      */           
/*   69 */           this.jjmatchedKind = 12;
/*   70 */           return 34;
/*      */         } 
/*   72 */         if ((active0 & 0x100000L) != 0L) {
/*      */           
/*   74 */           this.jjmatchedKind = 12;
/*   75 */           return 91;
/*      */         } 
/*   77 */         if ((active0 & 0x18C1F4240000L) != 0L) {
/*      */           
/*   79 */           this.jjmatchedKind = 12;
/*   80 */           return 1;
/*      */         } 
/*   82 */         if ((active0 & 0x1000000L) != 0L) {
/*      */           
/*   84 */           this.jjmatchedKind = 12;
/*   85 */           return 16;
/*      */         } 
/*   87 */         return -1;
/*      */       case 1:
/*   89 */         if ((active0 & 0x1FFFFF740000L) != 0L) {
/*      */           
/*   91 */           this.jjmatchedKind = 12;
/*   92 */           this.jjmatchedPos = 1;
/*   93 */           return 1;
/*      */         } 
/*   95 */         return -1;
/*      */       case 2:
/*   97 */         if ((active0 & 0x1FFFFF740000L) != 0L) {
/*      */           
/*   99 */           this.jjmatchedKind = 12;
/*  100 */           this.jjmatchedPos = 2;
/*  101 */           return 1;
/*      */         } 
/*  103 */         return -1;
/*      */       case 3:
/*  105 */         if ((active0 & 0x4100000000L) != 0L) {
/*      */           
/*  107 */           if (this.jjmatchedPos < 2) {
/*      */             
/*  109 */             this.jjmatchedKind = 12;
/*  110 */             this.jjmatchedPos = 2;
/*      */           } 
/*  112 */           return -1;
/*      */         } 
/*  114 */         if ((active0 & 0x1FBEFF740000L) != 0L) {
/*      */           
/*  116 */           this.jjmatchedKind = 12;
/*  117 */           this.jjmatchedPos = 3;
/*  118 */           return 1;
/*      */         } 
/*  120 */         return -1;
/*      */       case 4:
/*  122 */         if ((active0 & 0x4100000000L) != 0L) {
/*      */           
/*  124 */           if (this.jjmatchedPos < 2) {
/*      */             
/*  126 */             this.jjmatchedKind = 12;
/*  127 */             this.jjmatchedPos = 2;
/*      */           } 
/*  129 */           return -1;
/*      */         } 
/*  131 */         if ((active0 & 0x400000L) != 0L) {
/*      */           
/*  133 */           if (this.jjmatchedPos < 3) {
/*      */             
/*  135 */             this.jjmatchedKind = 12;
/*  136 */             this.jjmatchedPos = 3;
/*      */           } 
/*  138 */           return -1;
/*      */         } 
/*  140 */         if ((active0 & 0x1FBEFF340000L) != 0L) {
/*      */           
/*  142 */           this.jjmatchedKind = 12;
/*  143 */           this.jjmatchedPos = 4;
/*  144 */           return 1;
/*      */         } 
/*  146 */         return -1;
/*      */       case 5:
/*  148 */         if ((active0 & 0x4000000000L) != 0L) {
/*      */           
/*  150 */           if (this.jjmatchedPos < 2) {
/*      */             
/*  152 */             this.jjmatchedKind = 12;
/*  153 */             this.jjmatchedPos = 2;
/*      */           } 
/*  155 */           return -1;
/*      */         } 
/*  157 */         if ((active0 & 0x33C50000000L) != 0L) {
/*      */           
/*  159 */           if (this.jjmatchedPos < 4) {
/*      */             
/*  161 */             this.jjmatchedKind = 12;
/*  162 */             this.jjmatchedPos = 4;
/*      */           } 
/*  164 */           return -1;
/*      */         } 
/*  166 */         if ((active0 & 0x400000L) != 0L) {
/*      */           
/*  168 */           if (this.jjmatchedPos < 3) {
/*      */             
/*  170 */             this.jjmatchedKind = 12;
/*  171 */             this.jjmatchedPos = 3;
/*      */           } 
/*  173 */           return -1;
/*      */         } 
/*  175 */         if ((active0 & 0x1C82AF340000L) != 0L) {
/*      */           
/*  177 */           this.jjmatchedKind = 12;
/*  178 */           this.jjmatchedPos = 5;
/*  179 */           return 1;
/*      */         } 
/*  181 */         return -1;
/*      */       case 6:
/*  183 */         if ((active0 & 0x33C50000000L) != 0L) {
/*      */           
/*  185 */           if (this.jjmatchedPos < 4) {
/*      */             
/*  187 */             this.jjmatchedKind = 12;
/*  188 */             this.jjmatchedPos = 4;
/*      */           } 
/*  190 */           return -1;
/*      */         } 
/*  192 */         if ((active0 & 0x1C82AF340000L) != 0L) {
/*      */           
/*  194 */           if (this.jjmatchedPos != 6) {
/*      */             
/*  196 */             this.jjmatchedKind = 12;
/*  197 */             this.jjmatchedPos = 6;
/*      */           } 
/*  199 */           return 1;
/*      */         } 
/*  201 */         return -1;
/*      */       case 7:
/*  203 */         if ((active0 & 0x100000L) != 0L) {
/*      */           
/*  205 */           if (this.jjmatchedPos < 6) {
/*      */             
/*  207 */             this.jjmatchedKind = 12;
/*  208 */             this.jjmatchedPos = 6;
/*      */           } 
/*  210 */           return -1;
/*      */         } 
/*  212 */         if ((active0 & 0x13C00000000L) != 0L) {
/*      */           
/*  214 */           if (this.jjmatchedPos < 4) {
/*      */             
/*  216 */             this.jjmatchedKind = 12;
/*  217 */             this.jjmatchedPos = 4;
/*      */           } 
/*  219 */           return -1;
/*      */         } 
/*  221 */         if ((active0 & 0x1C82AF240000L) != 0L) {
/*      */           
/*  223 */           this.jjmatchedKind = 12;
/*  224 */           this.jjmatchedPos = 7;
/*  225 */           return 1;
/*      */         } 
/*  227 */         return -1;
/*      */       case 8:
/*  229 */         if ((active0 & 0x480AA240000L) != 0L) {
/*      */           
/*  231 */           this.jjmatchedKind = 12;
/*  232 */           this.jjmatchedPos = 8;
/*  233 */           return 1;
/*      */         } 
/*  235 */         if ((active0 & 0x180205000000L) != 0L) {
/*      */           
/*  237 */           if (this.jjmatchedPos < 7) {
/*      */             
/*  239 */             this.jjmatchedKind = 12;
/*  240 */             this.jjmatchedPos = 7;
/*      */           } 
/*  242 */           return -1;
/*      */         } 
/*  244 */         if ((active0 & 0x100000L) != 0L) {
/*      */           
/*  246 */           if (this.jjmatchedPos < 6) {
/*      */             
/*  248 */             this.jjmatchedKind = 12;
/*  249 */             this.jjmatchedPos = 6;
/*      */           } 
/*  251 */           return -1;
/*      */         } 
/*  253 */         if ((active0 & 0x1C00000000L) != 0L) {
/*      */           
/*  255 */           if (this.jjmatchedPos < 4) {
/*      */             
/*  257 */             this.jjmatchedKind = 12;
/*  258 */             this.jjmatchedPos = 4;
/*      */           } 
/*  260 */           return -1;
/*      */         } 
/*  262 */         return -1;
/*      */       case 9:
/*  264 */         if ((active0 & 0x80AA200000L) != 0L) {
/*      */           
/*  266 */           if (this.jjmatchedPos != 9) {
/*      */             
/*  268 */             this.jjmatchedKind = 12;
/*  269 */             this.jjmatchedPos = 9;
/*      */           } 
/*  271 */           return 1;
/*      */         } 
/*  273 */         if ((active0 & 0x180205000000L) != 0L) {
/*      */           
/*  275 */           if (this.jjmatchedPos < 7) {
/*      */             
/*  277 */             this.jjmatchedKind = 12;
/*  278 */             this.jjmatchedPos = 7;
/*      */           } 
/*  280 */           return -1;
/*      */         } 
/*  282 */         if ((active0 & 0x40000040000L) != 0L) {
/*      */           
/*  284 */           if (this.jjmatchedPos < 8) {
/*      */             
/*  286 */             this.jjmatchedKind = 12;
/*  287 */             this.jjmatchedPos = 8;
/*      */           } 
/*  289 */           return -1;
/*      */         } 
/*  291 */         if ((active0 & 0x1C00000000L) != 0L) {
/*      */           
/*  293 */           if (this.jjmatchedPos < 4) {
/*      */             
/*  295 */             this.jjmatchedKind = 12;
/*  296 */             this.jjmatchedPos = 4;
/*      */           } 
/*  298 */           return -1;
/*      */         } 
/*  300 */         return -1;
/*      */       case 10:
/*  302 */         if ((active0 & 0x100000000000L) != 0L) {
/*      */           
/*  304 */           if (this.jjmatchedPos < 7) {
/*      */             
/*  306 */             this.jjmatchedKind = 12;
/*  307 */             this.jjmatchedPos = 7;
/*      */           } 
/*  309 */           return -1;
/*      */         } 
/*  311 */         if ((active0 & 0x8000000L) != 0L) {
/*      */           
/*  313 */           if (this.jjmatchedPos < 9) {
/*      */             
/*  315 */             this.jjmatchedKind = 12;
/*  316 */             this.jjmatchedPos = 9;
/*      */           } 
/*  318 */           return -1;
/*      */         } 
/*  320 */         if ((active0 & 0x40000040000L) != 0L) {
/*      */           
/*  322 */           if (this.jjmatchedPos < 8) {
/*      */             
/*  324 */             this.jjmatchedKind = 12;
/*  325 */             this.jjmatchedPos = 8;
/*      */           } 
/*  327 */           return -1;
/*      */         } 
/*  329 */         if ((active0 & 0x80A2200000L) != 0L) {
/*      */           
/*  331 */           this.jjmatchedKind = 12;
/*  332 */           this.jjmatchedPos = 10;
/*  333 */           return 1;
/*      */         } 
/*  335 */         if ((active0 & 0xC00000000L) != 0L) {
/*      */           
/*  337 */           if (this.jjmatchedPos < 4) {
/*      */             
/*  339 */             this.jjmatchedKind = 12;
/*  340 */             this.jjmatchedPos = 4;
/*      */           } 
/*  342 */           return -1;
/*      */         } 
/*  344 */         return -1;
/*      */       case 11:
/*  346 */         if ((active0 & 0x40000000000L) != 0L) {
/*      */           
/*  348 */           if (this.jjmatchedPos < 8) {
/*      */             
/*  350 */             this.jjmatchedKind = 12;
/*  351 */             this.jjmatchedPos = 8;
/*      */           } 
/*  353 */           return -1;
/*      */         } 
/*  355 */         if ((active0 & 0x8000000L) != 0L) {
/*      */           
/*  357 */           if (this.jjmatchedPos < 9) {
/*      */             
/*  359 */             this.jjmatchedKind = 12;
/*  360 */             this.jjmatchedPos = 9;
/*      */           } 
/*  362 */           return -1;
/*      */         } 
/*  364 */         if ((active0 & 0xC00000000L) != 0L) {
/*      */           
/*  366 */           if (this.jjmatchedPos < 4) {
/*      */             
/*  368 */             this.jjmatchedKind = 12;
/*  369 */             this.jjmatchedPos = 4;
/*      */           } 
/*  371 */           return -1;
/*      */         } 
/*  373 */         if ((active0 & 0x80A2200000L) != 0L) {
/*      */           
/*  375 */           this.jjmatchedKind = 12;
/*  376 */           this.jjmatchedPos = 11;
/*  377 */           return 1;
/*      */         } 
/*  379 */         return -1;
/*      */       case 12:
/*  381 */         if ((active0 & 0x8000000000L) != 0L) {
/*      */           
/*  383 */           if (this.jjmatchedPos < 11) {
/*      */             
/*  385 */             this.jjmatchedKind = 12;
/*  386 */             this.jjmatchedPos = 11;
/*      */           } 
/*  388 */           return -1;
/*      */         } 
/*  390 */         if ((active0 & 0xC00000000L) != 0L) {
/*      */           
/*  392 */           if (this.jjmatchedPos < 4) {
/*      */             
/*  394 */             this.jjmatchedKind = 12;
/*  395 */             this.jjmatchedPos = 4;
/*      */           } 
/*  397 */           return -1;
/*      */         } 
/*  399 */         if ((active0 & 0xA2200000L) != 0L) {
/*      */           
/*  401 */           this.jjmatchedKind = 12;
/*  402 */           this.jjmatchedPos = 12;
/*  403 */           return 1;
/*      */         } 
/*  405 */         return -1;
/*      */       case 13:
/*  407 */         if ((active0 & 0x8000000000L) != 0L) {
/*      */           
/*  409 */           if (this.jjmatchedPos < 11) {
/*      */             
/*  411 */             this.jjmatchedKind = 12;
/*  412 */             this.jjmatchedPos = 11;
/*      */           } 
/*  414 */           return -1;
/*      */         } 
/*  416 */         if ((active0 & 0x2000000L) != 0L) {
/*      */           
/*  418 */           if (this.jjmatchedPos < 12) {
/*      */             
/*  420 */             this.jjmatchedKind = 12;
/*  421 */             this.jjmatchedPos = 12;
/*      */           } 
/*  423 */           return -1;
/*      */         } 
/*  425 */         if ((active0 & 0x400000000L) != 0L) {
/*      */           
/*  427 */           if (this.jjmatchedPos < 4) {
/*      */             
/*  429 */             this.jjmatchedKind = 12;
/*  430 */             this.jjmatchedPos = 4;
/*      */           } 
/*  432 */           return -1;
/*      */         } 
/*  434 */         if ((active0 & 0xA0200000L) != 0L) {
/*      */           
/*  436 */           this.jjmatchedKind = 12;
/*  437 */           this.jjmatchedPos = 13;
/*  438 */           return 1;
/*      */         } 
/*  440 */         return -1;
/*      */       case 14:
/*  442 */         if ((active0 & 0x8000000000L) != 0L) {
/*      */           
/*  444 */           if (this.jjmatchedPos < 11) {
/*      */             
/*  446 */             this.jjmatchedKind = 12;
/*  447 */             this.jjmatchedPos = 11;
/*      */           } 
/*  449 */           return -1;
/*      */         } 
/*  451 */         if ((active0 & 0x20000000L) != 0L) {
/*      */           
/*  453 */           if (this.jjmatchedPos < 13) {
/*      */             
/*  455 */             this.jjmatchedKind = 12;
/*  456 */             this.jjmatchedPos = 13;
/*      */           } 
/*  458 */           return -1;
/*      */         } 
/*  460 */         if ((active0 & 0x2000000L) != 0L) {
/*      */           
/*  462 */           if (this.jjmatchedPos < 12) {
/*      */             
/*  464 */             this.jjmatchedKind = 12;
/*  465 */             this.jjmatchedPos = 12;
/*      */           } 
/*  467 */           return -1;
/*      */         } 
/*  469 */         if ((active0 & 0x400000000L) != 0L) {
/*      */           
/*  471 */           if (this.jjmatchedPos < 4) {
/*      */             
/*  473 */             this.jjmatchedKind = 12;
/*  474 */             this.jjmatchedPos = 4;
/*      */           } 
/*  476 */           return -1;
/*      */         } 
/*  478 */         if ((active0 & 0x80200000L) != 0L) {
/*      */           
/*  480 */           this.jjmatchedKind = 12;
/*  481 */           this.jjmatchedPos = 14;
/*  482 */           return 1;
/*      */         } 
/*  484 */         return -1;
/*      */       case 15:
/*  486 */         if ((active0 & 0x20000000L) != 0L) {
/*      */           
/*  488 */           if (this.jjmatchedPos < 13) {
/*      */             
/*  490 */             this.jjmatchedKind = 12;
/*  491 */             this.jjmatchedPos = 13;
/*      */           } 
/*  493 */           return -1;
/*      */         } 
/*  495 */         if ((active0 & 0x80200000L) != 0L) {
/*      */           
/*  497 */           this.jjmatchedKind = 12;
/*  498 */           this.jjmatchedPos = 15;
/*  499 */           return 1;
/*      */         } 
/*  501 */         return -1;
/*      */       case 16:
/*  503 */         if ((active0 & 0x80200000L) != 0L) {
/*      */           
/*  505 */           this.jjmatchedKind = 12;
/*  506 */           this.jjmatchedPos = 16;
/*  507 */           return 1;
/*      */         } 
/*  509 */         return -1;
/*      */       case 17:
/*  511 */         if ((active0 & 0x80200000L) != 0L) {
/*      */           
/*  513 */           if (this.jjmatchedPos < 16) {
/*      */             
/*  515 */             this.jjmatchedKind = 12;
/*  516 */             this.jjmatchedPos = 16;
/*      */           } 
/*  518 */           return -1;
/*      */         } 
/*  520 */         return -1;
/*      */     } 
/*  522 */     return -1;
/*      */   }
/*      */ 
/*      */   
/*      */   private final int jjStartNfa_0(int pos, long active0) {
/*  527 */     return jjMoveNfa_0(jjStopStringLiteralDfa_0(pos, active0), pos + 1);
/*      */   }
/*      */   
/*      */   private final int jjStopAtPos(int pos, int kind) {
/*  531 */     this.jjmatchedKind = kind;
/*  532 */     this.jjmatchedPos = pos;
/*  533 */     return pos + 1;
/*      */   }
/*      */   
/*      */   private final int jjStartNfaWithStates_0(int pos, int kind, int state) {
/*  537 */     this.jjmatchedKind = kind;
/*  538 */     this.jjmatchedPos = pos; 
/*  539 */     try { this.curChar = this.input_stream.readChar(); }
/*  540 */     catch (IOException e) { return pos + 1; }
/*  541 */      return jjMoveNfa_0(state, pos + 1);
/*      */   }
/*      */   
/*      */   private final int jjMoveStringLiteralDfa0_0() {
/*  545 */     switch (this.curChar) {
/*      */       
/*      */       case '*':
/*  548 */         return jjStopAtPos(0, 45);
/*      */       case '/':
/*  550 */         this.jjmatchedKind = 16;
/*  551 */         return jjMoveStringLiteralDfa1_0(131072L);
/*      */       case '0':
/*  553 */         return jjStopAtPos(0, 46);
/*      */       case ':':
/*  555 */         return jjStopAtPos(0, 15);
/*      */       case '@':
/*  557 */         return jjStopAtPos(0, 19);
/*      */       case 'a':
/*  559 */         return jjMoveStringLiteralDfa1_0(825170853888L);
/*      */       case 'b':
/*  561 */         return jjMoveStringLiteralDfa1_0(16777216L);
/*      */       case 'c':
/*  563 */         return jjMoveStringLiteralDfa1_0(4398046511104L);
/*      */       case 'e':
/*  565 */         return jjMoveStringLiteralDfa1_0(1048576L);
/*      */       case 'f':
/*  567 */         return jjMoveStringLiteralDfa1_0(3298534883328L);
/*      */       case 'g':
/*  569 */         return jjMoveStringLiteralDfa1_0(1073741824L);
/*      */       case 'i':
/*  571 */         return jjMoveStringLiteralDfa1_0(2214592512L);
/*      */       case 'k':
/*  573 */         return jjMoveStringLiteralDfa1_0(4294967296L);
/*      */       case 'm':
/*  575 */         return jjMoveStringLiteralDfa1_0(257832255488L);
/*      */       case 'n':
/*  577 */         return jjMoveStringLiteralDfa1_0(8589934592L);
/*      */       case 'p':
/*  579 */         return jjMoveStringLiteralDfa1_0(33554432L);
/*      */       case 's':
/*  581 */         return jjMoveStringLiteralDfa1_0(270532608L);
/*      */       case 't':
/*  583 */         return jjMoveStringLiteralDfa1_0(4194304L);
/*      */       case 'x':
/*  585 */         return jjMoveStringLiteralDfa1_0(26388279066624L);
/*      */       case '~':
/*  587 */         return jjStopAtPos(0, 23);
/*      */     } 
/*  589 */     return jjMoveNfa_0(0, 0);
/*      */   }
/*      */   
/*      */   private final int jjMoveStringLiteralDfa1_0(long active0) {
/*      */     try {
/*  594 */       this.curChar = this.input_stream.readChar();
/*  595 */     } catch (IOException e) {
/*  596 */       jjStopStringLiteralDfa_0(0, active0);
/*  597 */       return 1;
/*      */     } 
/*  599 */     switch (this.curChar) {
/*      */       
/*      */       case '-':
/*  602 */         return jjMoveStringLiteralDfa2_0(active0, 26388279066624L);
/*      */       case '/':
/*  604 */         if ((active0 & 0x20000L) != 0L)
/*  605 */           return jjStopAtPos(1, 17); 
/*      */         break;
/*      */       case 'a':
/*  608 */         return jjMoveStringLiteralDfa2_0(active0, 3298551660544L);
/*      */       case 'c':
/*  610 */         return jjMoveStringLiteralDfa2_0(active0, 268435456L);
/*      */       case 'd':
/*  612 */         return jjMoveStringLiteralDfa2_0(active0, 2147483648L);
/*      */       case 'e':
/*  614 */         return jjMoveStringLiteralDfa2_0(active0, 4429185024L);
/*      */       case 'l':
/*  616 */         return jjMoveStringLiteralDfa2_0(active0, 1048576L);
/*      */       case 'n':
/*  618 */         return jjMoveStringLiteralDfa2_0(active0, 824633720832L);
/*      */       case 'o':
/*  620 */         return jjMoveStringLiteralDfa2_0(active0, 4664334483456L);
/*      */       case 'r':
/*  622 */         return jjMoveStringLiteralDfa2_0(active0, 1107296256L);
/*      */       case 't':
/*  624 */         return jjMoveStringLiteralDfa2_0(active0, 604241920L);
/*      */       case 'u':
/*  626 */         return jjMoveStringLiteralDfa2_0(active0, 2097152L);
/*      */       case 'y':
/*  628 */         return jjMoveStringLiteralDfa2_0(active0, 4194304L);
/*      */     } 
/*      */ 
/*      */     
/*  632 */     return jjStartNfa_0(0, active0);
/*      */   }
/*      */   
/*      */   private final int jjMoveStringLiteralDfa2_0(long old0, long active0) {
/*  636 */     if ((active0 &= old0) == 0L)
/*  637 */       return jjStartNfa_0(0, old0);  try {
/*  638 */       this.curChar = this.input_stream.readChar();
/*  639 */     } catch (IOException e) {
/*  640 */       jjStopStringLiteralDfa_0(1, active0);
/*  641 */       return 2;
/*      */     } 
/*  643 */     switch (this.curChar) {
/*      */       
/*      */       case 'b':
/*  646 */         return jjMoveStringLiteralDfa3_0(active0, 2097152L);
/*      */       case 'c':
/*  648 */         return jjMoveStringLiteralDfa3_0(active0, 3298534883328L);
/*      */       case 'd':
/*  650 */         return jjMoveStringLiteralDfa3_0(active0, 257698037760L);
/*      */       case 'e':
/*  652 */         return jjMoveStringLiteralDfa3_0(active0, 2215641088L);
/*      */       case 'i':
/*  654 */         return jjMoveStringLiteralDfa3_0(active0, 33554432L);
/*      */       case 'm':
/*  656 */         return jjMoveStringLiteralDfa3_0(active0, 4398180728832L);
/*      */       case 'o':
/*  658 */         return jjMoveStringLiteralDfa3_0(active0, 1342177280L);
/*      */       case 'p':
/*  660 */         return jjMoveStringLiteralDfa3_0(active0, 4194304L);
/*      */       case 's':
/*  662 */         return jjMoveStringLiteralDfa3_0(active0, 26388295843840L);
/*      */       case 't':
/*  664 */         return jjMoveStringLiteralDfa3_0(active0, 9127067648L);
/*      */       case 'y':
/*  666 */         return jjMoveStringLiteralDfa3_0(active0, 828928688128L);
/*      */     } 
/*      */ 
/*      */     
/*  670 */     return jjStartNfa_0(1, active0);
/*      */   }
/*      */   
/*      */   private final int jjMoveStringLiteralDfa3_0(long old0, long active0) {
/*  674 */     if ((active0 &= old0) == 0L)
/*  675 */       return jjStartNfa_0(1, old0);  try {
/*  676 */       this.curChar = this.input_stream.readChar();
/*  677 */     } catch (IOException e) {
/*  678 */       jjStopStringLiteralDfa_0(2, active0);
/*  679 */       return 3;
/*      */     } 
/*  681 */     switch (this.curChar) {
/*      */       
/*      */       case ':':
/*  684 */         return jjMoveStringLiteralDfa4_0(active0, 279172874240L);
/*      */       case 'A':
/*  686 */         return jjMoveStringLiteralDfa4_0(active0, 549755813888L);
/*      */       case 'a':
/*  688 */         return jjMoveStringLiteralDfa4_0(active0, 8589934592L);
/*      */       case 'b':
/*  690 */         return jjMoveStringLiteralDfa4_0(active0, 134217728L);
/*      */       case 'c':
/*  692 */         return jjMoveStringLiteralDfa4_0(active0, 26388279066624L);
/*      */       case 'e':
/*  694 */         return jjMoveStringLiteralDfa4_0(active0, 3556253892608L);
/*      */       case 'm':
/*  696 */         return jjMoveStringLiteralDfa4_0(active0, 101711872L);
/*      */       case 'n':
/*  698 */         return jjMoveStringLiteralDfa4_0(active0, 2147483648L);
/*      */       case 'p':
/*  700 */         return jjMoveStringLiteralDfa4_0(active0, 4398314946560L);
/*      */       case 'r':
/*  702 */         return jjMoveStringLiteralDfa4_0(active0, 537133056L);
/*      */       case 's':
/*  704 */         return jjMoveStringLiteralDfa4_0(active0, 2097152L);
/*      */       case 'u':
/*  706 */         return jjMoveStringLiteralDfa4_0(active0, 1073741824L);
/*      */     } 
/*      */ 
/*      */     
/*  710 */     return jjStartNfa_0(2, active0);
/*      */   }
/*      */   
/*      */   private final int jjMoveStringLiteralDfa4_0(long old0, long active0) {
/*  714 */     if ((active0 &= old0) == 0L)
/*  715 */       return jjStartNfa_0(2, old0);  try {
/*  716 */       this.curChar = this.input_stream.readChar();
/*  717 */     } catch (IOException e) {
/*  718 */       jjStopStringLiteralDfa_0(3, active0);
/*  719 */       return 4;
/*      */     } 
/*  721 */     switch (this.curChar) {
/*      */       
/*      */       case ':':
/*  724 */         if ((active0 & 0x100000000L) != 0L)
/*  725 */           return jjStopAtPos(4, 32); 
/*  726 */         return jjMoveStringLiteralDfa5_0(active0, 274882101248L);
/*      */       case 'T':
/*  728 */         return jjMoveStringLiteralDfa5_0(active0, 83886080L);
/*      */       case 'e':
/*  730 */         return jjMoveStringLiteralDfa5_0(active0, 403701760L);
/*      */       case 'h':
/*  732 */         return jjMoveStringLiteralDfa5_0(active0, 26388279066624L);
/*      */       case 'i':
/*  734 */         return jjMoveStringLiteralDfa5_0(active0, 570687488L);
/*      */       case 'l':
/*  736 */         return jjMoveStringLiteralDfa5_0(active0, 257698037760L);
/*      */       case 'o':
/*  738 */         return jjMoveStringLiteralDfa5_0(active0, 4398046511104L);
/*      */       case 'p':
/*  740 */         return jjMoveStringLiteralDfa5_0(active0, 1073741824L);
/*      */       case 't':
/*  742 */         return jjMoveStringLiteralDfa5_0(active0, 3859030212608L);
/*      */     } 
/*      */ 
/*      */     
/*  746 */     return jjStartNfa_0(3, active0);
/*      */   }
/*      */   
/*      */   private final int jjMoveStringLiteralDfa5_0(long old0, long active0) {
/*  750 */     if ((active0 &= old0) == 0L)
/*  751 */       return jjStartNfa_0(3, old0);  try {
/*  752 */       this.curChar = this.input_stream.readChar();
/*  753 */     } catch (IOException e) {
/*  754 */       jjStopStringLiteralDfa_0(4, active0);
/*  755 */       return 5;
/*      */     } 
/*  757 */     switch (this.curChar) {
/*      */       
/*      */       case '*':
/*  760 */         if ((active0 & 0x4000000000L) != 0L)
/*  761 */           return jjStopAtPos(5, 38); 
/*      */         break;
/*      */       case ':':
/*  764 */         if ((active0 & 0x400000L) != 0L)
/*  765 */           return jjStopAtPos(5, 22); 
/*  766 */         return jjMoveStringLiteralDfa6_0(active0, 3557575098368L);
/*      */       case 'b':
/*  768 */         return jjMoveStringLiteralDfa6_0(active0, 537133056L);
/*      */       case 'e':
/*  770 */         return jjMoveStringLiteralDfa6_0(active0, 26388279066624L);
/*      */       case 'i':
/*  772 */         return jjMoveStringLiteralDfa6_0(active0, 10739515392L);
/*      */       case 'n':
/*  774 */         return jjMoveStringLiteralDfa6_0(active0, 4398047559680L);
/*      */       case 'r':
/*  776 */         return jjMoveStringLiteralDfa6_0(active0, 134217728L);
/*      */       case 't':
/*  778 */         return jjMoveStringLiteralDfa6_0(active0, 549789368320L);
/*      */       case 'y':
/*  780 */         return jjMoveStringLiteralDfa6_0(active0, 83886080L);
/*      */     } 
/*      */ 
/*      */     
/*  784 */     return jjStartNfa_0(4, active0);
/*      */   }
/*      */   
/*      */   private final int jjMoveStringLiteralDfa6_0(long old0, long active0) {
/*  788 */     if ((active0 &= old0) == 0L)
/*  789 */       return jjStartNfa_0(4, old0);  try {
/*  790 */       this.curChar = this.input_stream.readChar();
/*  791 */     } catch (IOException e) {
/*  792 */       jjStopStringLiteralDfa_0(5, active0);
/*  793 */       return 6;
/*      */     } 
/*  795 */     switch (this.curChar) {
/*      */       
/*      */       case ':':
/*  798 */         if ((active0 & 0x10000000L) != 0L)
/*  799 */           return jjStopAtPos(6, 28); 
/*  800 */         if ((active0 & 0x40000000L) != 0L)
/*  801 */           return jjStopAtPos(6, 30); 
/*  802 */         if ((active0 & 0x20000000000L) != 0L) {
/*      */           
/*  804 */           this.jjmatchedKind = 41;
/*  805 */           this.jjmatchedPos = 6;
/*      */         } 
/*  807 */         return jjMoveStringLiteralDfa7_0(active0, 1357209665536L);
/*      */       case 'T':
/*  809 */         return jjMoveStringLiteralDfa7_0(active0, 134217728L);
/*      */       case 'e':
/*  811 */         return jjMoveStringLiteralDfa7_0(active0, 4398046511104L);
/*      */       case 'i':
/*  813 */         return jjMoveStringLiteralDfa7_0(active0, 33554432L);
/*      */       case 'm':
/*  815 */         return jjMoveStringLiteralDfa7_0(active0, 26388279066624L);
/*      */       case 'o':
/*  817 */         return jjMoveStringLiteralDfa7_0(active0, 8589934592L);
/*      */       case 'p':
/*  819 */         return jjMoveStringLiteralDfa7_0(active0, 83886080L);
/*      */       case 'r':
/*  821 */         return jjMoveStringLiteralDfa7_0(active0, 549755813888L);
/*      */       case 't':
/*  823 */         return jjMoveStringLiteralDfa7_0(active0, 2150629376L);
/*      */       case 'u':
/*  825 */         return jjMoveStringLiteralDfa7_0(active0, 537133056L);
/*      */     } 
/*      */ 
/*      */     
/*  829 */     return jjStartNfa_0(5, active0);
/*      */   }
/*      */   
/*      */   private final int jjMoveStringLiteralDfa7_0(long old0, long active0) {
/*  833 */     if ((active0 &= old0) == 0L)
/*  834 */       return jjStartNfa_0(5, old0);  try {
/*  835 */       this.curChar = this.input_stream.readChar();
/*  836 */     } catch (IOException e) {
/*  837 */       jjStopStringLiteralDfa_0(6, active0);
/*  838 */       return 7;
/*      */     } 
/*  840 */     switch (this.curChar) {
/*      */       
/*      */       case '*':
/*  843 */         if ((active0 & 0x2000000000L) != 0L)
/*  844 */           return jjStopAtPos(7, 37); 
/*  845 */         if ((active0 & 0x10000000000L) != 0L)
/*  846 */           return jjStopAtPos(7, 40); 
/*      */         break;
/*      */       case ':':
/*  849 */         return jjMoveStringLiteralDfa8_0(active0, 1048576L);
/*      */       case 'a':
/*  851 */         return jjMoveStringLiteralDfa8_0(active0, 26456998543360L);
/*      */       case 'c':
/*  853 */         return jjMoveStringLiteralDfa8_0(active0, 34359738368L);
/*      */       case 'e':
/*  855 */         return jjMoveStringLiteralDfa8_0(active0, 83886080L);
/*      */       case 'i':
/*  857 */         return jjMoveStringLiteralDfa8_0(active0, 549755813888L);
/*      */       case 'n':
/*  859 */         return jjMoveStringLiteralDfa8_0(active0, 4406636445696L);
/*      */       case 's':
/*  861 */         return jjMoveStringLiteralDfa8_0(active0, 17179869184L);
/*      */       case 't':
/*  863 */         return jjMoveStringLiteralDfa8_0(active0, 537133056L);
/*      */       case 'u':
/*  865 */         return jjMoveStringLiteralDfa8_0(active0, 2097152L);
/*      */       case 'v':
/*  867 */         return jjMoveStringLiteralDfa8_0(active0, 33554432L);
/*      */       case 'y':
/*  869 */         return jjMoveStringLiteralDfa8_0(active0, 2281701376L);
/*      */     } 
/*      */ 
/*      */     
/*  873 */     return jjStartNfa_0(6, active0);
/*      */   }
/*      */   
/*      */   private final int jjMoveStringLiteralDfa8_0(long old0, long active0) {
/*  877 */     if ((active0 &= old0) == 0L)
/*  878 */       return jjStartNfa_0(6, old0);  try {
/*  879 */       this.curChar = this.input_stream.readChar();
/*  880 */     } catch (IOException e) {
/*  881 */       jjStopStringLiteralDfa_0(7, active0);
/*  882 */       return 8;
/*      */     } 
/*  884 */     switch (this.curChar) {
/*      */       
/*      */       case ':':
/*  887 */         if ((active0 & 0x100000L) != 0L)
/*  888 */           return jjStopAtPos(8, 20); 
/*  889 */         return jjMoveStringLiteralDfa9_0(active0, 26396952887296L);
/*      */       case 'C':
/*  891 */         return jjMoveStringLiteralDfa9_0(active0, 2147483648L);
/*      */       case 'b':
/*  893 */         return jjMoveStringLiteralDfa9_0(active0, 549755813888L);
/*      */       case 'e':
/*  895 */         return jjMoveStringLiteralDfa9_0(active0, 17750556672L);
/*      */       case 'h':
/*  897 */         return jjMoveStringLiteralDfa9_0(active0, 34359738368L);
/*      */       case 'l':
/*  899 */         return jjMoveStringLiteralDfa9_0(active0, 68719476736L);
/*      */       case 'p':
/*  901 */         return jjMoveStringLiteralDfa9_0(active0, 134217728L);
/*      */       case 't':
/*  903 */         return jjMoveStringLiteralDfa9_0(active0, 4398048608256L);
/*      */     } 
/*      */ 
/*      */     
/*  907 */     return jjStartNfa_0(7, active0);
/*      */   }
/*      */   
/*      */   private final int jjMoveStringLiteralDfa9_0(long old0, long active0) {
/*  911 */     if ((active0 &= old0) == 0L)
/*  912 */       return jjStartNfa_0(7, old0);  try {
/*  913 */       this.curChar = this.input_stream.readChar();
/*  914 */     } catch (IOException e) {
/*  915 */       jjStopStringLiteralDfa_0(8, active0);
/*  916 */       return 9;
/*      */     } 
/*  918 */     switch (this.curChar) {
/*      */       
/*      */       case ':':
/*  921 */         if ((active0 & 0x1000000L) != 0L)
/*  922 */           return jjStopAtPos(9, 24); 
/*  923 */         if ((active0 & 0x4000000L) != 0L)
/*  924 */           return jjStopAtPos(9, 26); 
/*  925 */         if ((active0 & 0x200000000L) != 0L)
/*  926 */           return jjStopAtPos(9, 33); 
/*  927 */         if ((active0 & 0x80000000000L) != 0L) {
/*      */           
/*  929 */           this.jjmatchedKind = 43;
/*  930 */           this.jjmatchedPos = 9;
/*      */         } 
/*  932 */         return jjMoveStringLiteralDfa10_0(active0, 21990232817664L);
/*      */       case 'G':
/*  934 */         return jjMoveStringLiteralDfa10_0(active0, 536870912L);
/*      */       case 'T':
/*  936 */         return jjMoveStringLiteralDfa10_0(active0, 33554432L);
/*      */       case 'e':
/*  938 */         return jjMoveStringLiteralDfa10_0(active0, 134217728L);
/*      */       case 'i':
/*  940 */         return jjMoveStringLiteralDfa10_0(active0, 2097152L);
/*      */       case 'l':
/*  942 */         if ((active0 & 0x1000000000L) != 0L)
/*  943 */           return jjStopAtPos(9, 36); 
/*      */         break;
/*      */       case 'o':
/*  946 */         return jjMoveStringLiteralDfa10_0(active0, 36507222016L);
/*      */       case 'q':
/*  948 */         return jjMoveStringLiteralDfa10_0(active0, 17179869184L);
/*      */       case 'u':
/*  950 */         return jjMoveStringLiteralDfa10_0(active0, 549755813888L);
/*      */     } 
/*      */ 
/*      */     
/*  954 */     return jjStartNfa_0(8, active0);
/*      */   }
/*      */   
/*      */   private final int jjMoveStringLiteralDfa10_0(long old0, long active0) {
/*  958 */     if ((active0 &= old0) == 0L)
/*  959 */       return jjStartNfa_0(8, old0);  try {
/*  960 */       this.curChar = this.input_stream.readChar();
/*  961 */     } catch (IOException e) {
/*  962 */       jjStopStringLiteralDfa_0(9, active0);
/*  963 */       return 10;
/*      */     } 
/*  965 */     switch (this.curChar) {
/*      */       
/*      */       case '*':
/*  968 */         if ((active0 & 0x100000000000L) != 0L)
/*  969 */           return jjStopAtPos(10, 44); 
/*      */         break;
/*      */       case ':':
/*  972 */         if ((active0 & 0x40000L) != 0L)
/*  973 */           return jjStopAtPos(10, 18); 
/*  974 */         return jjMoveStringLiteralDfa11_0(active0, 4398180728832L);
/*      */       case 'i':
/*  976 */         return jjMoveStringLiteralDfa11_0(active0, 34359738368L);
/*      */       case 'n':
/*  978 */         return jjMoveStringLiteralDfa11_0(active0, 2147483648L);
/*      */       case 'o':
/*  980 */         return jjMoveStringLiteralDfa11_0(active0, 2097152L);
/*      */       case 'r':
/*  982 */         return jjMoveStringLiteralDfa11_0(active0, 536870912L);
/*      */       case 't':
/*  984 */         return jjMoveStringLiteralDfa11_0(active0, 549755813888L);
/*      */       case 'u':
/*  986 */         return jjMoveStringLiteralDfa11_0(active0, 17179869184L);
/*      */       case 'y':
/*  988 */         return jjMoveStringLiteralDfa11_0(active0, 33554432L);
/*      */     } 
/*      */ 
/*      */     
/*  992 */     return jjStartNfa_0(9, active0);
/*      */   }
/*      */   
/*      */   private final int jjMoveStringLiteralDfa11_0(long old0, long active0) {
/*  996 */     if ((active0 &= old0) == 0L)
/*  997 */       return jjStartNfa_0(9, old0);  try {
/*  998 */       this.curChar = this.input_stream.readChar();
/*  999 */     } catch (IOException e) {
/* 1000 */       jjStopStringLiteralDfa_0(10, active0);
/* 1001 */       return 11;
/*      */     } 
/* 1003 */     switch (this.curChar) {
/*      */       
/*      */       case '*':
/* 1006 */         if ((active0 & 0x40000000000L) != 0L)
/* 1007 */           return jjStopAtPos(11, 42); 
/*      */         break;
/*      */       case ':':
/* 1010 */         if ((active0 & 0x8000000L) != 0L)
/* 1011 */           return jjStopAtPos(11, 27); 
/*      */         break;
/*      */       case 'c':
/* 1014 */         return jjMoveStringLiteralDfa12_0(active0, 34359738368L);
/*      */       case 'e':
/* 1016 */         return jjMoveStringLiteralDfa12_0(active0, 566935683072L);
/*      */       case 'n':
/* 1018 */         return jjMoveStringLiteralDfa12_0(active0, 2097152L);
/*      */       case 'o':
/* 1020 */         return jjMoveStringLiteralDfa12_0(active0, 536870912L);
/*      */       case 'p':
/* 1022 */         return jjMoveStringLiteralDfa12_0(active0, 33554432L);
/*      */       case 't':
/* 1024 */         return jjMoveStringLiteralDfa12_0(active0, 2147483648L);
/*      */     } 
/*      */ 
/*      */     
/* 1028 */     return jjStartNfa_0(10, active0);
/*      */   }
/*      */   
/*      */   private final int jjMoveStringLiteralDfa12_0(long old0, long active0) {
/* 1032 */     if ((active0 &= old0) == 0L)
/* 1033 */       return jjStartNfa_0(10, old0);  try {
/* 1034 */       this.curChar = this.input_stream.readChar();
/* 1035 */     } catch (IOException e) {
/* 1036 */       jjStopStringLiteralDfa_0(11, active0);
/* 1037 */       return 12;
/*      */     } 
/* 1039 */     switch (this.curChar) {
/*      */       
/*      */       case ':':
/* 1042 */         return jjMoveStringLiteralDfa13_0(active0, 549755813888L);
/*      */       case 'G':
/* 1044 */         return jjMoveStringLiteralDfa13_0(active0, 2097152L);
/*      */       case 'e':
/* 1046 */         if ((active0 & 0x800000000L) != 0L)
/* 1047 */           return jjStopAtPos(12, 35); 
/* 1048 */         return jjMoveStringLiteralDfa13_0(active0, 33554432L);
/*      */       case 'n':
/* 1050 */         return jjMoveStringLiteralDfa13_0(active0, 17179869184L);
/*      */       case 'r':
/* 1052 */         return jjMoveStringLiteralDfa13_0(active0, 2147483648L);
/*      */       case 'u':
/* 1054 */         return jjMoveStringLiteralDfa13_0(active0, 536870912L);
/*      */     } 
/*      */ 
/*      */     
/* 1058 */     return jjStartNfa_0(11, active0);
/*      */   }
/*      */   
/*      */   private final int jjMoveStringLiteralDfa13_0(long old0, long active0) {
/* 1062 */     if ((active0 &= old0) == 0L)
/* 1063 */       return jjStartNfa_0(11, old0);  try {
/* 1064 */       this.curChar = this.input_stream.readChar();
/* 1065 */     } catch (IOException e) {
/* 1066 */       jjStopStringLiteralDfa_0(12, active0);
/* 1067 */       return 13;
/*      */     } 
/* 1069 */     switch (this.curChar) {
/*      */       
/*      */       case ':':
/* 1072 */         return jjMoveStringLiteralDfa14_0(active0, 549789368320L);
/*      */       case 'a':
/* 1074 */         return jjMoveStringLiteralDfa14_0(active0, 2147483648L);
/*      */       case 'c':
/* 1076 */         return jjMoveStringLiteralDfa14_0(active0, 17179869184L);
/*      */       case 'p':
/* 1078 */         return jjMoveStringLiteralDfa14_0(active0, 536870912L);
/*      */       case 'r':
/* 1080 */         return jjMoveStringLiteralDfa14_0(active0, 2097152L);
/*      */     } 
/*      */ 
/*      */     
/* 1084 */     return jjStartNfa_0(12, active0);
/*      */   }
/*      */   
/*      */   private final int jjMoveStringLiteralDfa14_0(long old0, long active0) {
/* 1088 */     if ((active0 &= old0) == 0L)
/* 1089 */       return jjStartNfa_0(12, old0);  try {
/* 1090 */       this.curChar = this.input_stream.readChar();
/* 1091 */     } catch (IOException e) {
/* 1092 */       jjStopStringLiteralDfa_0(13, active0);
/* 1093 */       return 14;
/*      */     } 
/* 1095 */     switch (this.curChar) {
/*      */       
/*      */       case '*':
/* 1098 */         if ((active0 & 0x8000000000L) != 0L)
/* 1099 */           return jjStopAtPos(14, 39); 
/*      */         break;
/*      */       case ':':
/* 1102 */         if ((active0 & 0x2000000L) != 0L)
/* 1103 */           return jjStopAtPos(14, 25); 
/* 1104 */         return jjMoveStringLiteralDfa15_0(active0, 536870912L);
/*      */       case 'e':
/* 1106 */         if ((active0 & 0x400000000L) != 0L)
/* 1107 */           return jjStopAtPos(14, 34); 
/*      */         break;
/*      */       case 'i':
/* 1110 */         return jjMoveStringLiteralDfa15_0(active0, 2147483648L);
/*      */       case 'o':
/* 1112 */         return jjMoveStringLiteralDfa15_0(active0, 2097152L);
/*      */     } 
/*      */ 
/*      */     
/* 1116 */     return jjStartNfa_0(13, active0);
/*      */   }
/*      */   
/*      */   private final int jjMoveStringLiteralDfa15_0(long old0, long active0) {
/* 1120 */     if ((active0 &= old0) == 0L)
/* 1121 */       return jjStartNfa_0(13, old0);  try {
/* 1122 */       this.curChar = this.input_stream.readChar();
/* 1123 */     } catch (IOException e) {
/* 1124 */       jjStopStringLiteralDfa_0(14, active0);
/* 1125 */       return 15;
/*      */     } 
/* 1127 */     switch (this.curChar) {
/*      */       
/*      */       case ':':
/* 1130 */         if ((active0 & 0x20000000L) != 0L)
/* 1131 */           return jjStopAtPos(15, 29); 
/*      */         break;
/*      */       case 'n':
/* 1134 */         return jjMoveStringLiteralDfa16_0(active0, 2147483648L);
/*      */       case 'u':
/* 1136 */         return jjMoveStringLiteralDfa16_0(active0, 2097152L);
/*      */     } 
/*      */ 
/*      */     
/* 1140 */     return jjStartNfa_0(14, active0);
/*      */   }
/*      */   
/*      */   private final int jjMoveStringLiteralDfa16_0(long old0, long active0) {
/* 1144 */     if ((active0 &= old0) == 0L)
/* 1145 */       return jjStartNfa_0(14, old0);  try {
/* 1146 */       this.curChar = this.input_stream.readChar();
/* 1147 */     } catch (IOException e) {
/* 1148 */       jjStopStringLiteralDfa_0(15, active0);
/* 1149 */       return 16;
/*      */     } 
/* 1151 */     switch (this.curChar) {
/*      */       
/*      */       case 'p':
/* 1154 */         return jjMoveStringLiteralDfa17_0(active0, 2097152L);
/*      */       case 't':
/* 1156 */         return jjMoveStringLiteralDfa17_0(active0, 2147483648L);
/*      */     } 
/*      */ 
/*      */     
/* 1160 */     return jjStartNfa_0(15, active0);
/*      */   }
/*      */   
/*      */   private final int jjMoveStringLiteralDfa17_0(long old0, long active0) {
/* 1164 */     if ((active0 &= old0) == 0L)
/* 1165 */       return jjStartNfa_0(15, old0);  try {
/* 1166 */       this.curChar = this.input_stream.readChar();
/* 1167 */     } catch (IOException e) {
/* 1168 */       jjStopStringLiteralDfa_0(16, active0);
/* 1169 */       return 17;
/*      */     } 
/* 1171 */     switch (this.curChar) {
/*      */       
/*      */       case ':':
/* 1174 */         return jjMoveStringLiteralDfa18_0(active0, 2149580800L);
/*      */     } 
/*      */ 
/*      */     
/* 1178 */     return jjStartNfa_0(16, active0);
/*      */   }
/*      */   
/*      */   private final int jjMoveStringLiteralDfa18_0(long old0, long active0) {
/* 1182 */     if ((active0 &= old0) == 0L)
/* 1183 */       return jjStartNfa_0(16, old0);  try {
/* 1184 */       this.curChar = this.input_stream.readChar();
/* 1185 */     } catch (IOException e) {
/* 1186 */       jjStopStringLiteralDfa_0(17, active0);
/* 1187 */       return 18;
/*      */     } 
/* 1189 */     switch (this.curChar) {
/*      */       
/*      */       case ':':
/* 1192 */         if ((active0 & 0x200000L) != 0L)
/* 1193 */           return jjStopAtPos(18, 21); 
/* 1194 */         if ((active0 & 0x80000000L) != 0L) {
/* 1195 */           return jjStopAtPos(18, 31);
/*      */         }
/*      */         break;
/*      */     } 
/*      */     
/* 1200 */     return jjStartNfa_0(17, active0);
/*      */   }
/*      */   
/*      */   private final void jjCheckNAdd(int state) {
/* 1204 */     if (this.jjrounds[state] != this.jjround) {
/*      */       
/* 1206 */       this.jjstateSet[this.jjnewStateCnt++] = state;
/* 1207 */       this.jjrounds[state] = this.jjround;
/*      */     } 
/*      */   }
/*      */   
/*      */   private final void jjAddStates(int start, int end) {
/*      */     do {
/* 1213 */       this.jjstateSet[this.jjnewStateCnt++] = jjnextStates[start];
/* 1214 */     } while (start++ != end);
/*      */   }
/*      */   
/*      */   private final void jjCheckNAddTwoStates(int state1, int state2) {
/* 1218 */     jjCheckNAdd(state1);
/* 1219 */     jjCheckNAdd(state2);
/*      */   }
/*      */   
/*      */   private final void jjCheckNAddStates(int start, int end) {
/*      */     do {
/* 1224 */       jjCheckNAdd(jjnextStates[start]);
/* 1225 */     } while (start++ != end);
/*      */   }
/*      */   
/*      */   private final void jjCheckNAddStates(int start) {
/* 1229 */     jjCheckNAdd(jjnextStates[start]);
/* 1230 */     jjCheckNAdd(jjnextStates[start + 1]);
/*      */   }
/* 1232 */   static final long[] jjbitVec0 = new long[] { 0L, -16384L, -17590038560769L, 8388607L };
/*      */ 
/*      */   
/* 1235 */   static final long[] jjbitVec2 = new long[] { 0L, 0L, 0L, -36028797027352577L };
/*      */ 
/*      */   
/* 1238 */   static final long[] jjbitVec3 = new long[] { 9219994337134247935L, 9223372036854775294L, -1L, -274156627316187121L };
/*      */ 
/*      */   
/* 1241 */   static final long[] jjbitVec4 = new long[] { 16777215L, -65536L, -576458553280167937L, 3L };
/*      */ 
/*      */   
/* 1244 */   static final long[] jjbitVec5 = new long[] { 0L, 0L, -17179879616L, 4503588160110591L };
/*      */ 
/*      */   
/* 1247 */   static final long[] jjbitVec6 = new long[] { -8194L, -536936449L, -65533L, 234134404065073567L };
/*      */ 
/*      */   
/* 1250 */   static final long[] jjbitVec7 = new long[] { -562949953421312L, -8547991553L, 127L, 1979120929931264L };
/*      */ 
/*      */   
/* 1253 */   static final long[] jjbitVec8 = new long[] { 576460743713488896L, -562949953419266L, 9007199254740991999L, 412319973375L };
/*      */ 
/*      */   
/* 1256 */   static final long[] jjbitVec9 = new long[] { 2594073385365405664L, 17163091968L, 271902628478820320L, 844440767823872L };
/*      */ 
/*      */   
/* 1259 */   static final long[] jjbitVec10 = new long[] { 247132830528276448L, 7881300924956672L, 2589004636761075680L, 4294967296L };
/*      */ 
/*      */   
/* 1262 */   static final long[] jjbitVec11 = new long[] { 2579997437506199520L, 15837691904L, 270153412153034720L, 0L };
/*      */ 
/*      */   
/* 1265 */   static final long[] jjbitVec12 = new long[] { 283724577500946400L, 12884901888L, 283724577500946400L, 13958643712L };
/*      */ 
/*      */   
/* 1268 */   static final long[] jjbitVec13 = new long[] { 288228177128316896L, 12884901888L, 0L, 0L };
/*      */ 
/*      */   
/* 1271 */   static final long[] jjbitVec14 = new long[] { 3799912185593854L, 63L, 2309621682768192918L, 31L };
/*      */ 
/*      */   
/* 1274 */   static final long[] jjbitVec15 = new long[] { 0L, 4398046510847L, 0L, 0L };
/*      */ 
/*      */   
/* 1277 */   static final long[] jjbitVec16 = new long[] { 0L, 0L, -4294967296L, 36028797018898495L };
/*      */ 
/*      */   
/* 1280 */   static final long[] jjbitVec17 = new long[] { 5764607523034749677L, 12493387738468353L, -756383734487318528L, 144405459145588743L };
/*      */ 
/*      */   
/* 1283 */   static final long[] jjbitVec18 = new long[] { -1L, -1L, -4026531841L, 288230376151711743L };
/*      */ 
/*      */   
/* 1286 */   static final long[] jjbitVec19 = new long[] { -3233808385L, 4611686017001275199L, 6908521828386340863L, 2295745090394464220L };
/*      */ 
/*      */   
/* 1289 */   static final long[] jjbitVec20 = new long[] { 83837761617920L, 0L, 7L, 0L };
/*      */ 
/*      */   
/* 1292 */   static final long[] jjbitVec21 = new long[] { 4389456576640L, -2L, -8587837441L, 576460752303423487L };
/*      */ 
/*      */   
/* 1295 */   static final long[] jjbitVec22 = new long[] { 35184372088800L, 0L, 0L, 0L };
/*      */ 
/*      */   
/* 1298 */   static final long[] jjbitVec23 = new long[] { -1L, -1L, 274877906943L, 0L };
/*      */ 
/*      */   
/* 1301 */   static final long[] jjbitVec24 = new long[] { -1L, -1L, 68719476735L, 0L };
/*      */ 
/*      */   
/* 1304 */   static final long[] jjbitVec25 = new long[] { 0L, 0L, 36028797018963968L, -36028797027352577L };
/*      */ 
/*      */   
/* 1307 */   static final long[] jjbitVec26 = new long[] { 16777215L, -65536L, -576458553280167937L, 196611L };
/*      */ 
/*      */   
/* 1310 */   static final long[] jjbitVec27 = new long[] { -1L, 12884901951L, -17179879488L, 4503588160110591L };
/*      */ 
/*      */   
/* 1313 */   static final long[] jjbitVec28 = new long[] { -8194L, -536936449L, -65413L, 234134404065073567L };
/*      */ 
/*      */   
/* 1316 */   static final long[] jjbitVec29 = new long[] { -562949953421312L, -8547991553L, -4899916411759099777L, 1979120929931286L };
/*      */ 
/*      */   
/* 1319 */   static final long[] jjbitVec30 = new long[] { 576460743713488896L, -277081224642561L, 9007199254740991999L, 288017070894841855L };
/*      */ 
/*      */   
/* 1322 */   static final long[] jjbitVec31 = new long[] { -864691128455135250L, 281268803485695L, -3186861885341720594L, 1125692414638495L };
/*      */ 
/*      */   
/* 1325 */   static final long[] jjbitVec32 = new long[] { -3211631683292264476L, 9006925953907079L, -869759877059465234L, 281204393786303L };
/*      */ 
/*      */   
/* 1328 */   static final long[] jjbitVec33 = new long[] { -878767076314341394L, 281215949093263L, -4341532606274353172L, 280925229301191L };
/*      */ 
/*      */   
/* 1331 */   static final long[] jjbitVec34 = new long[] { -4327961440926441490L, 281212990012895L, -4327961440926441492L, 281214063754719L };
/*      */ 
/*      */   
/* 1334 */   static final long[] jjbitVec35 = new long[] { -4323457841299070996L, 281212992110031L, 0L, 0L };
/*      */ 
/*      */   
/* 1337 */   static final long[] jjbitVec36 = new long[] { 576320014815068158L, 67076095L, 4323293666156225942L, 67059551L };
/*      */ 
/*      */   
/* 1340 */   static final long[] jjbitVec37 = new long[] { -4422530440275951616L, -558551906910465L, 215680200883507167L, 0L };
/*      */ 
/*      */   
/* 1343 */   static final long[] jjbitVec38 = new long[] { 0L, 0L, 0L, 9126739968L };
/*      */ 
/*      */   
/* 1346 */   static final long[] jjbitVec39 = new long[] { 17732914942836896L, -2L, -6876561409L, 8646911284551352319L };
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private final int jjMoveNfa_0(int startState, int curPos) {
/* 1352 */     int startsAt = 0;
/* 1353 */     this.jjnewStateCnt = 148;
/* 1354 */     int i = 1;
/* 1355 */     this.jjstateSet[0] = startState;
/* 1356 */     int kind = Integer.MAX_VALUE;
/*      */     
/*      */     while (true) {
/* 1359 */       if (++this.jjround == Integer.MAX_VALUE)
/* 1360 */         ReInitRounds(); 
/* 1361 */       if (this.curChar < '@') {
/*      */         
/* 1363 */         long l = 1L << this.curChar;
/*      */         
/*      */         do {
/* 1366 */           switch (this.jjstateSet[--i]) {
/*      */             
/*      */             case 1:
/*      */             case 34:
/* 1370 */               if ((0x3FF600000000000L & l) == 0L)
/*      */                 break; 
/* 1372 */               if (kind > 12)
/* 1373 */                 kind = 12; 
/* 1374 */               jjCheckNAdd(1);
/*      */               break;
/*      */             case 91:
/* 1377 */               if ((0x3FF600000000000L & l) == 0L)
/*      */                 break; 
/* 1379 */               if (kind > 12)
/* 1380 */                 kind = 12; 
/* 1381 */               jjCheckNAdd(1);
/*      */               break;
/*      */             case 16:
/* 1384 */               if ((0x3FF600000000000L & l) == 0L)
/*      */                 break; 
/* 1386 */               if (kind > 12)
/* 1387 */                 kind = 12; 
/* 1388 */               jjCheckNAdd(1);
/*      */               break;
/*      */             case 55:
/* 1391 */               if ((0x3FF600000000000L & l) == 0L)
/*      */                 break; 
/* 1393 */               if (kind > 12)
/* 1394 */                 kind = 12; 
/* 1395 */               jjCheckNAdd(1);
/*      */               break;
/*      */             case 68:
/* 1398 */               if ((0x3FF600000000000L & l) == 0L)
/*      */                 break; 
/* 1400 */               if (kind > 12)
/* 1401 */                 kind = 12; 
/* 1402 */               jjCheckNAdd(1);
/*      */               break;
/*      */             case 103:
/* 1405 */               if ((0x3FF600000000000L & l) == 0L)
/*      */                 break; 
/* 1407 */               if (kind > 12)
/* 1408 */                 kind = 12; 
/* 1409 */               jjCheckNAdd(1);
/*      */               break;
/*      */             case 81:
/* 1412 */               if ((0x3FF600000000000L & l) == 0L)
/*      */                 break; 
/* 1414 */               if (kind > 12)
/* 1415 */                 kind = 12; 
/* 1416 */               jjCheckNAdd(1);
/*      */               break;
/*      */             case 23:
/* 1419 */               if ((0x3FF600000000000L & l) == 0L)
/*      */                 break; 
/* 1421 */               if (kind > 12)
/* 1422 */                 kind = 12; 
/* 1423 */               jjCheckNAdd(1);
/*      */               break;
/*      */             case 3:
/* 1426 */               if ((0x3FF000000000000L & l) != 0L) {
/* 1427 */                 jjAddStates(0, 1);
/*      */               }
/*      */               break;
/*      */           } 
/* 1431 */         } while (i != startsAt);
/*      */       }
/* 1433 */       else if (this.curChar < '') {
/*      */         
/* 1435 */         long l = 1L << (this.curChar & 0x3F);
/*      */         
/*      */         do {
/* 1438 */           switch (this.jjstateSet[--i]) {
/*      */             
/*      */             case 34:
/* 1441 */               if ((0x7FFFFFE87FFFFFEL & l) != 0L) {
/*      */                 
/* 1443 */                 if (kind > 12)
/* 1444 */                   kind = 12; 
/* 1445 */                 jjCheckNAdd(1);
/*      */               } 
/* 1447 */               if (this.curChar == 'a')
/* 1448 */                 this.jjstateSet[this.jjnewStateCnt++] = 33; 
/*      */               break;
/*      */             case 91:
/* 1451 */               if ((0x7FFFFFE87FFFFFEL & l) != 0L) {
/*      */                 
/* 1453 */                 if (kind > 12)
/* 1454 */                   kind = 12; 
/* 1455 */                 jjCheckNAdd(1);
/*      */               } 
/* 1457 */               if (this.curChar == 'n')
/* 1458 */                 this.jjstateSet[this.jjnewStateCnt++] = 90; 
/*      */               break;
/*      */             case 16:
/* 1461 */               if ((0x7FFFFFE87FFFFFEL & l) != 0L) {
/*      */                 
/* 1463 */                 if (kind > 12)
/* 1464 */                   kind = 12; 
/* 1465 */                 jjCheckNAdd(1);
/*      */               } 
/* 1467 */               if (this.curChar == 'o')
/* 1468 */                 this.jjstateSet[this.jjnewStateCnt++] = 15; 
/*      */               break;
/*      */             case 55:
/* 1471 */               if ((0x7FFFFFE87FFFFFEL & l) != 0L) {
/*      */                 
/* 1473 */                 if (kind > 12)
/* 1474 */                   kind = 12; 
/* 1475 */                 jjCheckNAdd(1);
/*      */               } 
/* 1477 */               if (this.curChar == 'o')
/* 1478 */                 this.jjstateSet[this.jjnewStateCnt++] = 54; 
/*      */               break;
/*      */             case 68:
/* 1481 */               if ((0x7FFFFFE87FFFFFEL & l) != 0L) {
/*      */                 
/* 1483 */                 if (kind > 12)
/* 1484 */                   kind = 12; 
/* 1485 */                 jjCheckNAdd(1);
/*      */               } 
/* 1487 */               if (this.curChar == 'r')
/* 1488 */                 this.jjstateSet[this.jjnewStateCnt++] = 67; 
/*      */               break;
/*      */             case 103:
/* 1491 */               if ((0x7FFFFFE87FFFFFEL & l) != 0L) {
/*      */                 
/* 1493 */                 if (kind > 12)
/* 1494 */                   kind = 12; 
/* 1495 */                 jjCheckNAdd(1);
/*      */               } 
/* 1497 */               if (this.curChar == 'a') {
/* 1498 */                 this.jjstateSet[this.jjnewStateCnt++] = 146;
/* 1499 */               } else if (this.curChar == 'i') {
/* 1500 */                 this.jjstateSet[this.jjnewStateCnt++] = 139;
/* 1501 */               }  if (this.curChar == 'a') {
/* 1502 */                 this.jjstateSet[this.jjnewStateCnt++] = 132;
/* 1503 */               } else if (this.curChar == 'i') {
/* 1504 */                 this.jjstateSet[this.jjnewStateCnt++] = 122;
/* 1505 */               }  if (this.curChar == 'a') {
/* 1506 */                 this.jjstateSet[this.jjnewStateCnt++] = 112; break;
/* 1507 */               }  if (this.curChar == 'i')
/* 1508 */                 this.jjstateSet[this.jjnewStateCnt++] = 102; 
/*      */               break;
/*      */             case 0:
/* 1511 */               if ((0x7FFFFFE87FFFFFEL & l) != 0L) {
/*      */                 
/* 1513 */                 if (kind > 12)
/* 1514 */                   kind = 12; 
/* 1515 */                 jjCheckNAdd(1);
/*      */               }
/* 1517 */               else if (this.curChar == '[') {
/* 1518 */                 this.jjstateSet[this.jjnewStateCnt++] = 3;
/* 1519 */               }  if (this.curChar == 'm') {
/* 1520 */                 jjAddStates(2, 7); break;
/* 1521 */               }  if (this.curChar == 'e') {
/* 1522 */                 this.jjstateSet[this.jjnewStateCnt++] = 91; break;
/* 1523 */               }  if (this.curChar == 'p') {
/* 1524 */                 this.jjstateSet[this.jjnewStateCnt++] = 81; break;
/* 1525 */               }  if (this.curChar == 'l') {
/* 1526 */                 this.jjstateSet[this.jjnewStateCnt++] = 74; break;
/* 1527 */               }  if (this.curChar == 'f') {
/* 1528 */                 this.jjstateSet[this.jjnewStateCnt++] = 68; break;
/* 1529 */               }  if (this.curChar == 't') {
/* 1530 */                 this.jjstateSet[this.jjnewStateCnt++] = 55; break;
/* 1531 */               }  if (this.curChar == 'w') {
/* 1532 */                 this.jjstateSet[this.jjnewStateCnt++] = 44; break;
/* 1533 */               }  if (this.curChar == 'c') {
/* 1534 */                 this.jjstateSet[this.jjnewStateCnt++] = 34; break;
/* 1535 */               }  if (this.curChar == 'n') {
/* 1536 */                 this.jjstateSet[this.jjnewStateCnt++] = 23; break;
/* 1537 */               }  if (this.curChar == 'b') {
/* 1538 */                 this.jjstateSet[this.jjnewStateCnt++] = 16; break;
/* 1539 */               }  if (this.curChar == 'o')
/* 1540 */                 this.jjstateSet[this.jjnewStateCnt++] = 10; 
/*      */               break;
/*      */             case 81:
/* 1543 */               if ((0x7FFFFFE87FFFFFEL & l) != 0L) {
/*      */                 
/* 1545 */                 if (kind > 12)
/* 1546 */                   kind = 12; 
/* 1547 */                 jjCheckNAdd(1);
/*      */               } 
/* 1549 */               if (this.curChar == 'a')
/* 1550 */                 this.jjstateSet[this.jjnewStateCnt++] = 80; 
/*      */               break;
/*      */             case 23:
/* 1553 */               if ((0x7FFFFFE87FFFFFEL & l) != 0L) {
/*      */                 
/* 1555 */                 if (kind > 12)
/* 1556 */                   kind = 12; 
/* 1557 */                 jjCheckNAdd(1);
/*      */               } 
/* 1559 */               if (this.curChar == 'u')
/* 1560 */                 this.jjstateSet[this.jjnewStateCnt++] = 22; 
/*      */               break;
/*      */             case 1:
/* 1563 */               if ((0x7FFFFFE87FFFFFEL & l) == 0L)
/*      */                 break; 
/* 1565 */               if (kind > 12)
/* 1566 */                 kind = 12; 
/* 1567 */               jjCheckNAdd(1);
/*      */               break;
/*      */             case 2:
/* 1570 */               if (this.curChar == '[')
/* 1571 */                 this.jjstateSet[this.jjnewStateCnt++] = 3; 
/*      */               break;
/*      */             case 4:
/* 1574 */               if (this.curChar == ']')
/* 1575 */                 kind = 13; 
/*      */               break;
/*      */             case 5:
/* 1578 */               if (this.curChar == 'd' && kind > 14)
/* 1579 */                 kind = 14; 
/*      */               break;
/*      */             case 6:
/*      */             case 12:
/* 1583 */               if (this.curChar == 'e')
/* 1584 */                 jjCheckNAdd(5); 
/*      */               break;
/*      */             case 7:
/* 1587 */               if (this.curChar == 'r')
/* 1588 */                 this.jjstateSet[this.jjnewStateCnt++] = 6; 
/*      */               break;
/*      */             case 8:
/* 1591 */               if (this.curChar == 'e')
/* 1592 */                 this.jjstateSet[this.jjnewStateCnt++] = 7; 
/*      */               break;
/*      */             case 9:
/* 1595 */               if (this.curChar == 'd')
/* 1596 */                 this.jjstateSet[this.jjnewStateCnt++] = 8; 
/*      */               break;
/*      */             case 10:
/* 1599 */               if (this.curChar == 'r')
/* 1600 */                 this.jjstateSet[this.jjnewStateCnt++] = 9; 
/*      */               break;
/*      */             case 11:
/* 1603 */               if (this.curChar == 'o')
/* 1604 */                 this.jjstateSet[this.jjnewStateCnt++] = 10; 
/*      */               break;
/*      */             case 13:
/* 1607 */               if (this.curChar == 'd')
/* 1608 */                 this.jjstateSet[this.jjnewStateCnt++] = 12; 
/*      */               break;
/*      */             case 14:
/* 1611 */               if (this.curChar == 'n')
/* 1612 */                 this.jjstateSet[this.jjnewStateCnt++] = 13; 
/*      */               break;
/*      */             case 15:
/* 1615 */               if (this.curChar == 'u')
/* 1616 */                 this.jjstateSet[this.jjnewStateCnt++] = 14; 
/*      */               break;
/*      */             case 17:
/* 1619 */               if (this.curChar == 'b')
/* 1620 */                 this.jjstateSet[this.jjnewStateCnt++] = 16; 
/*      */               break;
/*      */             case 18:
/* 1623 */               if (this.curChar == 'c' && kind > 14)
/* 1624 */                 kind = 14; 
/*      */               break;
/*      */             case 19:
/* 1627 */               if (this.curChar == 'i')
/* 1628 */                 this.jjstateSet[this.jjnewStateCnt++] = 18; 
/*      */               break;
/*      */             case 20:
/* 1631 */               if (this.curChar == 'r')
/* 1632 */                 this.jjstateSet[this.jjnewStateCnt++] = 19; 
/*      */               break;
/*      */             case 21:
/* 1635 */               if (this.curChar == 'e')
/* 1636 */                 this.jjstateSet[this.jjnewStateCnt++] = 20; 
/*      */               break;
/*      */             case 22:
/* 1639 */               if (this.curChar == 'm')
/* 1640 */                 this.jjstateSet[this.jjnewStateCnt++] = 21; 
/*      */               break;
/*      */             case 24:
/* 1643 */               if (this.curChar == 'n')
/* 1644 */                 this.jjstateSet[this.jjnewStateCnt++] = 23; 
/*      */               break;
/*      */             case 25:
/* 1647 */               if (this.curChar == 'y' && kind > 14)
/* 1648 */                 kind = 14; 
/*      */               break;
/*      */             case 26:
/* 1651 */               if (this.curChar == 't')
/* 1652 */                 this.jjstateSet[this.jjnewStateCnt++] = 25; 
/*      */               break;
/*      */             case 27:
/* 1655 */               if (this.curChar == 'i')
/* 1656 */                 this.jjstateSet[this.jjnewStateCnt++] = 26; 
/*      */               break;
/*      */             case 28:
/* 1659 */               if (this.curChar == 'l')
/* 1660 */                 this.jjstateSet[this.jjnewStateCnt++] = 27; 
/*      */               break;
/*      */             case 29:
/* 1663 */               if (this.curChar == 'a')
/* 1664 */                 this.jjstateSet[this.jjnewStateCnt++] = 28; 
/*      */               break;
/*      */             case 30:
/* 1667 */               if (this.curChar == 'n')
/* 1668 */                 this.jjstateSet[this.jjnewStateCnt++] = 29; 
/*      */               break;
/*      */             case 31:
/* 1671 */               if (this.curChar == 'i')
/* 1672 */                 this.jjstateSet[this.jjnewStateCnt++] = 30; 
/*      */               break;
/*      */             case 32:
/* 1675 */               if (this.curChar == 'd')
/* 1676 */                 this.jjstateSet[this.jjnewStateCnt++] = 31; 
/*      */               break;
/*      */             case 33:
/* 1679 */               if (this.curChar == 'r')
/* 1680 */                 this.jjstateSet[this.jjnewStateCnt++] = 32; 
/*      */               break;
/*      */             case 35:
/* 1683 */               if (this.curChar == 'c')
/* 1684 */                 this.jjstateSet[this.jjnewStateCnt++] = 34; 
/*      */               break;
/*      */             case 36:
/* 1687 */               if (this.curChar == 'e' && kind > 14)
/* 1688 */                 kind = 14; 
/*      */               break;
/*      */             case 37:
/* 1691 */               if (this.curChar == 'c')
/* 1692 */                 jjCheckNAdd(36); 
/*      */               break;
/*      */             case 38:
/* 1695 */               if (this.curChar == 'a')
/* 1696 */                 this.jjstateSet[this.jjnewStateCnt++] = 37; 
/*      */               break;
/*      */             case 39:
/* 1699 */               if (this.curChar == 'p')
/* 1700 */                 this.jjstateSet[this.jjnewStateCnt++] = 38; 
/*      */               break;
/*      */             case 40:
/* 1703 */               if (this.curChar == 'S')
/* 1704 */                 this.jjstateSet[this.jjnewStateCnt++] = 39; 
/*      */               break;
/*      */             case 41:
/* 1707 */               if (this.curChar == 'e')
/* 1708 */                 this.jjstateSet[this.jjnewStateCnt++] = 40; 
/*      */               break;
/*      */             case 42:
/* 1711 */               if (this.curChar == 't')
/* 1712 */                 this.jjstateSet[this.jjnewStateCnt++] = 41; 
/*      */               break;
/*      */             case 43:
/* 1715 */               if (this.curChar == 'i')
/* 1716 */                 this.jjstateSet[this.jjnewStateCnt++] = 42; 
/*      */               break;
/*      */             case 44:
/* 1719 */               if (this.curChar == 'h')
/* 1720 */                 this.jjstateSet[this.jjnewStateCnt++] = 43; 
/*      */               break;
/*      */             case 45:
/* 1723 */               if (this.curChar == 'w')
/* 1724 */                 this.jjstateSet[this.jjnewStateCnt++] = 44; 
/*      */               break;
/*      */             case 46:
/* 1727 */               if (this.curChar == 's' && kind > 14)
/* 1728 */                 kind = 14; 
/*      */               break;
/*      */             case 47:
/*      */             case 57:
/* 1732 */               if (this.curChar == 't')
/* 1733 */                 jjCheckNAdd(46); 
/*      */               break;
/*      */             case 48:
/* 1736 */               if (this.curChar == 'i')
/* 1737 */                 this.jjstateSet[this.jjnewStateCnt++] = 47; 
/*      */               break;
/*      */             case 49:
/* 1740 */               if (this.curChar == 'g')
/* 1741 */                 this.jjstateSet[this.jjnewStateCnt++] = 48; 
/*      */               break;
/*      */             case 50:
/* 1744 */               if (this.curChar == 'i')
/* 1745 */                 this.jjstateSet[this.jjnewStateCnt++] = 49; 
/*      */               break;
/*      */             case 51:
/* 1748 */               if (this.curChar == 'D')
/* 1749 */                 this.jjstateSet[this.jjnewStateCnt++] = 50; 
/*      */               break;
/*      */             case 52:
/* 1752 */               if (this.curChar == 'l')
/* 1753 */                 this.jjstateSet[this.jjnewStateCnt++] = 51; 
/*      */               break;
/*      */             case 53:
/* 1756 */               if (this.curChar == 'a')
/* 1757 */                 this.jjstateSet[this.jjnewStateCnt++] = 52; 
/*      */               break;
/*      */             case 54:
/* 1760 */               if (this.curChar == 't')
/* 1761 */                 this.jjstateSet[this.jjnewStateCnt++] = 53; 
/*      */               break;
/*      */             case 56:
/* 1764 */               if (this.curChar == 't')
/* 1765 */                 this.jjstateSet[this.jjnewStateCnt++] = 55; 
/*      */               break;
/*      */             case 58:
/* 1768 */               if (this.curChar == 'i')
/* 1769 */                 this.jjstateSet[this.jjnewStateCnt++] = 57; 
/*      */               break;
/*      */             case 59:
/* 1772 */               if (this.curChar == 'g')
/* 1773 */                 this.jjstateSet[this.jjnewStateCnt++] = 58; 
/*      */               break;
/*      */             case 60:
/* 1776 */               if (this.curChar == 'i')
/* 1777 */                 this.jjstateSet[this.jjnewStateCnt++] = 59; 
/*      */               break;
/*      */             case 61:
/* 1780 */               if (this.curChar == 'D')
/* 1781 */                 this.jjstateSet[this.jjnewStateCnt++] = 60; 
/*      */               break;
/*      */             case 62:
/* 1784 */               if (this.curChar == 'n')
/* 1785 */                 this.jjstateSet[this.jjnewStateCnt++] = 61; 
/*      */               break;
/*      */             case 63:
/* 1788 */               if (this.curChar == 'o')
/* 1789 */                 this.jjstateSet[this.jjnewStateCnt++] = 62; 
/*      */               break;
/*      */             case 64:
/* 1792 */               if (this.curChar == 'i')
/* 1793 */                 this.jjstateSet[this.jjnewStateCnt++] = 63; 
/*      */               break;
/*      */             case 65:
/* 1796 */               if (this.curChar == 't')
/* 1797 */                 this.jjstateSet[this.jjnewStateCnt++] = 64; 
/*      */               break;
/*      */             case 66:
/* 1800 */               if (this.curChar == 'c')
/* 1801 */                 this.jjstateSet[this.jjnewStateCnt++] = 65; 
/*      */               break;
/*      */             case 67:
/* 1804 */               if (this.curChar == 'a')
/* 1805 */                 this.jjstateSet[this.jjnewStateCnt++] = 66; 
/*      */               break;
/*      */             case 69:
/* 1808 */               if (this.curChar == 'f')
/* 1809 */                 this.jjstateSet[this.jjnewStateCnt++] = 68; 
/*      */               break;
/*      */             case 70:
/* 1812 */               if (this.curChar == 'h' && kind > 14)
/* 1813 */                 kind = 14; 
/*      */               break;
/*      */             case 71:
/*      */             case 134:
/*      */             case 141:
/* 1818 */               if (this.curChar == 't')
/* 1819 */                 jjCheckNAdd(70); 
/*      */               break;
/*      */             case 72:
/* 1822 */               if (this.curChar == 'g')
/* 1823 */                 this.jjstateSet[this.jjnewStateCnt++] = 71; 
/*      */               break;
/*      */             case 73:
/* 1826 */               if (this.curChar == 'n')
/* 1827 */                 this.jjstateSet[this.jjnewStateCnt++] = 72; 
/*      */               break;
/*      */             case 74:
/* 1830 */               if (this.curChar == 'e')
/* 1831 */                 this.jjstateSet[this.jjnewStateCnt++] = 73; 
/*      */               break;
/*      */             case 75:
/* 1834 */               if (this.curChar == 'l')
/* 1835 */                 this.jjstateSet[this.jjnewStateCnt++] = 74; 
/*      */               break;
/*      */             case 76:
/* 1838 */               if (this.curChar == 'n' && kind > 14)
/* 1839 */                 kind = 14; 
/*      */               break;
/*      */             case 77:
/* 1842 */               if (this.curChar == 'r')
/* 1843 */                 jjCheckNAdd(76); 
/*      */               break;
/*      */             case 78:
/* 1846 */               if (this.curChar == 'e')
/* 1847 */                 this.jjstateSet[this.jjnewStateCnt++] = 77; 
/*      */               break;
/*      */             case 79:
/* 1850 */               if (this.curChar == 't')
/* 1851 */                 this.jjstateSet[this.jjnewStateCnt++] = 78; 
/*      */               break;
/*      */             case 80:
/* 1854 */               if (this.curChar == 't')
/* 1855 */                 this.jjstateSet[this.jjnewStateCnt++] = 79; 
/*      */               break;
/*      */             case 82:
/* 1858 */               if (this.curChar == 'p')
/* 1859 */                 this.jjstateSet[this.jjnewStateCnt++] = 81; 
/*      */               break;
/*      */             case 83:
/* 1862 */               if (this.curChar == 'o')
/* 1863 */                 jjCheckNAdd(76); 
/*      */               break;
/*      */             case 84:
/* 1866 */               if (this.curChar == 'i')
/* 1867 */                 this.jjstateSet[this.jjnewStateCnt++] = 83; 
/*      */               break;
/*      */             case 85:
/* 1870 */               if (this.curChar == 't')
/* 1871 */                 this.jjstateSet[this.jjnewStateCnt++] = 84; 
/*      */               break;
/*      */             case 86:
/* 1874 */               if (this.curChar == 'a')
/* 1875 */                 this.jjstateSet[this.jjnewStateCnt++] = 85; 
/*      */               break;
/*      */             case 87:
/* 1878 */               if (this.curChar == 'r')
/* 1879 */                 this.jjstateSet[this.jjnewStateCnt++] = 86; 
/*      */               break;
/*      */             case 88:
/* 1882 */               if (this.curChar == 'e')
/* 1883 */                 this.jjstateSet[this.jjnewStateCnt++] = 87; 
/*      */               break;
/*      */             case 89:
/* 1886 */               if (this.curChar == 'm')
/* 1887 */                 this.jjstateSet[this.jjnewStateCnt++] = 88; 
/*      */               break;
/*      */             case 90:
/* 1890 */               if (this.curChar == 'u')
/* 1891 */                 this.jjstateSet[this.jjnewStateCnt++] = 89; 
/*      */               break;
/*      */             case 92:
/* 1894 */               if (this.curChar == 'e')
/* 1895 */                 this.jjstateSet[this.jjnewStateCnt++] = 91; 
/*      */               break;
/*      */             case 93:
/* 1898 */               if (this.curChar == 'm')
/* 1899 */                 jjAddStates(2, 7); 
/*      */               break;
/*      */             case 94:
/*      */             case 104:
/*      */             case 114:
/*      */             case 124:
/* 1905 */               if (this.curChar == 'v')
/* 1906 */                 jjCheckNAdd(36); 
/*      */               break;
/*      */             case 95:
/* 1909 */               if (this.curChar == 'i')
/* 1910 */                 this.jjstateSet[this.jjnewStateCnt++] = 94; 
/*      */               break;
/*      */             case 96:
/* 1913 */               if (this.curChar == 's')
/* 1914 */                 this.jjstateSet[this.jjnewStateCnt++] = 95; 
/*      */               break;
/*      */             case 97:
/* 1917 */               if (this.curChar == 'u')
/* 1918 */                 this.jjstateSet[this.jjnewStateCnt++] = 96; 
/*      */               break;
/*      */             case 98:
/* 1921 */               if (this.curChar == 'l')
/* 1922 */                 this.jjstateSet[this.jjnewStateCnt++] = 97; 
/*      */               break;
/*      */             case 99:
/* 1925 */               if (this.curChar == 'c')
/* 1926 */                 this.jjstateSet[this.jjnewStateCnt++] = 98; 
/*      */               break;
/*      */             case 100:
/* 1929 */               if (this.curChar == 'n')
/* 1930 */                 this.jjstateSet[this.jjnewStateCnt++] = 99; 
/*      */               break;
/*      */             case 101:
/* 1933 */               if (this.curChar == 'I')
/* 1934 */                 this.jjstateSet[this.jjnewStateCnt++] = 100; 
/*      */               break;
/*      */             case 102:
/* 1937 */               if (this.curChar == 'n')
/* 1938 */                 this.jjstateSet[this.jjnewStateCnt++] = 101; 
/*      */               break;
/*      */             case 105:
/* 1941 */               if (this.curChar == 'i')
/* 1942 */                 this.jjstateSet[this.jjnewStateCnt++] = 104; 
/*      */               break;
/*      */             case 106:
/* 1945 */               if (this.curChar == 's')
/* 1946 */                 this.jjstateSet[this.jjnewStateCnt++] = 105; 
/*      */               break;
/*      */             case 107:
/* 1949 */               if (this.curChar == 'u')
/* 1950 */                 this.jjstateSet[this.jjnewStateCnt++] = 106; 
/*      */               break;
/*      */             case 108:
/* 1953 */               if (this.curChar == 'l')
/* 1954 */                 this.jjstateSet[this.jjnewStateCnt++] = 107; 
/*      */               break;
/*      */             case 109:
/* 1957 */               if (this.curChar == 'c')
/* 1958 */                 this.jjstateSet[this.jjnewStateCnt++] = 108; 
/*      */               break;
/*      */             case 110:
/* 1961 */               if (this.curChar == 'n')
/* 1962 */                 this.jjstateSet[this.jjnewStateCnt++] = 109; 
/*      */               break;
/*      */             case 111:
/* 1965 */               if (this.curChar == 'I')
/* 1966 */                 this.jjstateSet[this.jjnewStateCnt++] = 110; 
/*      */               break;
/*      */             case 112:
/* 1969 */               if (this.curChar == 'x')
/* 1970 */                 this.jjstateSet[this.jjnewStateCnt++] = 111; 
/*      */               break;
/*      */             case 113:
/* 1973 */               if (this.curChar == 'a')
/* 1974 */                 this.jjstateSet[this.jjnewStateCnt++] = 112; 
/*      */               break;
/*      */             case 115:
/* 1977 */               if (this.curChar == 'i')
/* 1978 */                 this.jjstateSet[this.jjnewStateCnt++] = 114; 
/*      */               break;
/*      */             case 116:
/* 1981 */               if (this.curChar == 's')
/* 1982 */                 this.jjstateSet[this.jjnewStateCnt++] = 115; 
/*      */               break;
/*      */             case 117:
/* 1985 */               if (this.curChar == 'u')
/* 1986 */                 this.jjstateSet[this.jjnewStateCnt++] = 116; 
/*      */               break;
/*      */             case 118:
/* 1989 */               if (this.curChar == 'l')
/* 1990 */                 this.jjstateSet[this.jjnewStateCnt++] = 117; 
/*      */               break;
/*      */             case 119:
/* 1993 */               if (this.curChar == 'c')
/* 1994 */                 this.jjstateSet[this.jjnewStateCnt++] = 118; 
/*      */               break;
/*      */             case 120:
/* 1997 */               if (this.curChar == 'x')
/* 1998 */                 this.jjstateSet[this.jjnewStateCnt++] = 119; 
/*      */               break;
/*      */             case 121:
/* 2001 */               if (this.curChar == 'E')
/* 2002 */                 this.jjstateSet[this.jjnewStateCnt++] = 120; 
/*      */               break;
/*      */             case 122:
/* 2005 */               if (this.curChar == 'n')
/* 2006 */                 this.jjstateSet[this.jjnewStateCnt++] = 121; 
/*      */               break;
/*      */             case 123:
/* 2009 */               if (this.curChar == 'i')
/* 2010 */                 this.jjstateSet[this.jjnewStateCnt++] = 122; 
/*      */               break;
/*      */             case 125:
/* 2013 */               if (this.curChar == 'i')
/* 2014 */                 this.jjstateSet[this.jjnewStateCnt++] = 124; 
/*      */               break;
/*      */             case 126:
/* 2017 */               if (this.curChar == 's')
/* 2018 */                 this.jjstateSet[this.jjnewStateCnt++] = 125; 
/*      */               break;
/*      */             case 127:
/* 2021 */               if (this.curChar == 'u')
/* 2022 */                 this.jjstateSet[this.jjnewStateCnt++] = 126; 
/*      */               break;
/*      */             case 128:
/* 2025 */               if (this.curChar == 'l')
/* 2026 */                 this.jjstateSet[this.jjnewStateCnt++] = 127; 
/*      */               break;
/*      */             case 129:
/* 2029 */               if (this.curChar == 'c')
/* 2030 */                 this.jjstateSet[this.jjnewStateCnt++] = 128; 
/*      */               break;
/*      */             case 130:
/* 2033 */               if (this.curChar == 'x')
/* 2034 */                 this.jjstateSet[this.jjnewStateCnt++] = 129; 
/*      */               break;
/*      */             case 131:
/* 2037 */               if (this.curChar == 'E')
/* 2038 */                 this.jjstateSet[this.jjnewStateCnt++] = 130; 
/*      */               break;
/*      */             case 132:
/* 2041 */               if (this.curChar == 'x')
/* 2042 */                 this.jjstateSet[this.jjnewStateCnt++] = 131; 
/*      */               break;
/*      */             case 133:
/* 2045 */               if (this.curChar == 'a')
/* 2046 */                 this.jjstateSet[this.jjnewStateCnt++] = 132; 
/*      */               break;
/*      */             case 135:
/* 2049 */               if (this.curChar == 'g')
/* 2050 */                 this.jjstateSet[this.jjnewStateCnt++] = 134; 
/*      */               break;
/*      */             case 136:
/* 2053 */               if (this.curChar == 'n')
/* 2054 */                 this.jjstateSet[this.jjnewStateCnt++] = 135; 
/*      */               break;
/*      */             case 137:
/* 2057 */               if (this.curChar == 'e')
/* 2058 */                 this.jjstateSet[this.jjnewStateCnt++] = 136; 
/*      */               break;
/*      */             case 138:
/* 2061 */               if (this.curChar == 'L')
/* 2062 */                 this.jjstateSet[this.jjnewStateCnt++] = 137; 
/*      */               break;
/*      */             case 139:
/* 2065 */               if (this.curChar == 'n')
/* 2066 */                 this.jjstateSet[this.jjnewStateCnt++] = 138; 
/*      */               break;
/*      */             case 140:
/* 2069 */               if (this.curChar == 'i')
/* 2070 */                 this.jjstateSet[this.jjnewStateCnt++] = 139; 
/*      */               break;
/*      */             case 142:
/* 2073 */               if (this.curChar == 'g')
/* 2074 */                 this.jjstateSet[this.jjnewStateCnt++] = 141; 
/*      */               break;
/*      */             case 143:
/* 2077 */               if (this.curChar == 'n')
/* 2078 */                 this.jjstateSet[this.jjnewStateCnt++] = 142; 
/*      */               break;
/*      */             case 144:
/* 2081 */               if (this.curChar == 'e')
/* 2082 */                 this.jjstateSet[this.jjnewStateCnt++] = 143; 
/*      */               break;
/*      */             case 145:
/* 2085 */               if (this.curChar == 'L')
/* 2086 */                 this.jjstateSet[this.jjnewStateCnt++] = 144; 
/*      */               break;
/*      */             case 146:
/* 2089 */               if (this.curChar == 'x')
/* 2090 */                 this.jjstateSet[this.jjnewStateCnt++] = 145; 
/*      */               break;
/*      */             case 147:
/* 2093 */               if (this.curChar == 'a') {
/* 2094 */                 this.jjstateSet[this.jjnewStateCnt++] = 146;
/*      */               }
/*      */               break;
/*      */           } 
/* 2098 */         } while (i != startsAt);
/*      */       }
/*      */       else {
/*      */         
/* 2102 */         int hiByte = this.curChar >> 8;
/* 2103 */         int i1 = hiByte >> 6;
/* 2104 */         long l1 = 1L << (hiByte & 0x3F);
/* 2105 */         int i2 = (this.curChar & 0xFF) >> 6;
/* 2106 */         long l2 = 1L << (this.curChar & 0x3F);
/*      */         
/*      */         do {
/* 2109 */           switch (this.jjstateSet[--i]) {
/*      */             
/*      */             case 1:
/*      */             case 34:
/* 2113 */               if (!jjCanMove_1(hiByte, i1, i2, l1, l2))
/*      */                 break; 
/* 2115 */               if (kind > 12)
/* 2116 */                 kind = 12; 
/* 2117 */               jjCheckNAdd(1);
/*      */               break;
/*      */             case 91:
/* 2120 */               if (!jjCanMove_1(hiByte, i1, i2, l1, l2))
/*      */                 break; 
/* 2122 */               if (kind > 12)
/* 2123 */                 kind = 12; 
/* 2124 */               jjCheckNAdd(1);
/*      */               break;
/*      */             case 16:
/* 2127 */               if (!jjCanMove_1(hiByte, i1, i2, l1, l2))
/*      */                 break; 
/* 2129 */               if (kind > 12)
/* 2130 */                 kind = 12; 
/* 2131 */               jjCheckNAdd(1);
/*      */               break;
/*      */             case 55:
/* 2134 */               if (!jjCanMove_1(hiByte, i1, i2, l1, l2))
/*      */                 break; 
/* 2136 */               if (kind > 12)
/* 2137 */                 kind = 12; 
/* 2138 */               jjCheckNAdd(1);
/*      */               break;
/*      */             case 68:
/* 2141 */               if (!jjCanMove_1(hiByte, i1, i2, l1, l2))
/*      */                 break; 
/* 2143 */               if (kind > 12)
/* 2144 */                 kind = 12; 
/* 2145 */               jjCheckNAdd(1);
/*      */               break;
/*      */             case 103:
/* 2148 */               if (!jjCanMove_1(hiByte, i1, i2, l1, l2))
/*      */                 break; 
/* 2150 */               if (kind > 12)
/* 2151 */                 kind = 12; 
/* 2152 */               jjCheckNAdd(1);
/*      */               break;
/*      */             case 0:
/* 2155 */               if (!jjCanMove_0(hiByte, i1, i2, l1, l2))
/*      */                 break; 
/* 2157 */               if (kind > 12)
/* 2158 */                 kind = 12; 
/* 2159 */               jjCheckNAdd(1);
/*      */               break;
/*      */             case 81:
/* 2162 */               if (!jjCanMove_1(hiByte, i1, i2, l1, l2))
/*      */                 break; 
/* 2164 */               if (kind > 12)
/* 2165 */                 kind = 12; 
/* 2166 */               jjCheckNAdd(1);
/*      */               break;
/*      */             case 23:
/* 2169 */               if (!jjCanMove_1(hiByte, i1, i2, l1, l2))
/*      */                 break; 
/* 2171 */               if (kind > 12)
/* 2172 */                 kind = 12; 
/* 2173 */               jjCheckNAdd(1);
/*      */               break;
/*      */           } 
/*      */         
/* 2177 */         } while (i != startsAt);
/*      */       } 
/* 2179 */       if (kind != Integer.MAX_VALUE) {
/*      */         
/* 2181 */         this.jjmatchedKind = kind;
/* 2182 */         this.jjmatchedPos = curPos;
/* 2183 */         kind = Integer.MAX_VALUE;
/*      */       } 
/* 2185 */       curPos++;
/* 2186 */       if ((i = this.jjnewStateCnt) == (startsAt = 148 - (this.jjnewStateCnt = startsAt)))
/* 2187 */         return curPos;  
/* 2188 */       try { this.curChar = this.input_stream.readChar(); }
/* 2189 */       catch (IOException e) { return curPos; }
/*      */     
/*      */     } 
/* 2192 */   } static final int[] jjnextStates = new int[] { 3, 4, 103, 113, 123, 133, 140, 147 };
/*      */ 
/*      */ 
/*      */   
/*      */   private static final boolean jjCanMove_0(int hiByte, int i1, int i2, long l1, long l2) {
/* 2197 */     switch (hiByte) {
/*      */       
/*      */       case 0:
/* 2200 */         return ((jjbitVec2[i2] & l2) != 0L);
/*      */       case 1:
/* 2202 */         return ((jjbitVec3[i2] & l2) != 0L);
/*      */       case 2:
/* 2204 */         return ((jjbitVec4[i2] & l2) != 0L);
/*      */       case 3:
/* 2206 */         return ((jjbitVec5[i2] & l2) != 0L);
/*      */       case 4:
/* 2208 */         return ((jjbitVec6[i2] & l2) != 0L);
/*      */       case 5:
/* 2210 */         return ((jjbitVec7[i2] & l2) != 0L);
/*      */       case 6:
/* 2212 */         return ((jjbitVec8[i2] & l2) != 0L);
/*      */       case 9:
/* 2214 */         return ((jjbitVec9[i2] & l2) != 0L);
/*      */       case 10:
/* 2216 */         return ((jjbitVec10[i2] & l2) != 0L);
/*      */       case 11:
/* 2218 */         return ((jjbitVec11[i2] & l2) != 0L);
/*      */       case 12:
/* 2220 */         return ((jjbitVec12[i2] & l2) != 0L);
/*      */       case 13:
/* 2222 */         return ((jjbitVec13[i2] & l2) != 0L);
/*      */       case 14:
/* 2224 */         return ((jjbitVec14[i2] & l2) != 0L);
/*      */       case 15:
/* 2226 */         return ((jjbitVec15[i2] & l2) != 0L);
/*      */       case 16:
/* 2228 */         return ((jjbitVec16[i2] & l2) != 0L);
/*      */       case 17:
/* 2230 */         return ((jjbitVec17[i2] & l2) != 0L);
/*      */       case 30:
/* 2232 */         return ((jjbitVec18[i2] & l2) != 0L);
/*      */       case 31:
/* 2234 */         return ((jjbitVec19[i2] & l2) != 0L);
/*      */       case 33:
/* 2236 */         return ((jjbitVec20[i2] & l2) != 0L);
/*      */       case 48:
/* 2238 */         return ((jjbitVec21[i2] & l2) != 0L);
/*      */       case 49:
/* 2240 */         return ((jjbitVec22[i2] & l2) != 0L);
/*      */       case 159:
/* 2242 */         return ((jjbitVec23[i2] & l2) != 0L);
/*      */       case 215:
/* 2244 */         return ((jjbitVec24[i2] & l2) != 0L);
/*      */     } 
/* 2246 */     if ((jjbitVec0[i1] & l1) != 0L)
/* 2247 */       return true; 
/* 2248 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   private static final boolean jjCanMove_1(int hiByte, int i1, int i2, long l1, long l2) {
/* 2253 */     switch (hiByte) {
/*      */       
/*      */       case 0:
/* 2256 */         return ((jjbitVec25[i2] & l2) != 0L);
/*      */       case 1:
/* 2258 */         return ((jjbitVec3[i2] & l2) != 0L);
/*      */       case 2:
/* 2260 */         return ((jjbitVec26[i2] & l2) != 0L);
/*      */       case 3:
/* 2262 */         return ((jjbitVec27[i2] & l2) != 0L);
/*      */       case 4:
/* 2264 */         return ((jjbitVec28[i2] & l2) != 0L);
/*      */       case 5:
/* 2266 */         return ((jjbitVec29[i2] & l2) != 0L);
/*      */       case 6:
/* 2268 */         return ((jjbitVec30[i2] & l2) != 0L);
/*      */       case 9:
/* 2270 */         return ((jjbitVec31[i2] & l2) != 0L);
/*      */       case 10:
/* 2272 */         return ((jjbitVec32[i2] & l2) != 0L);
/*      */       case 11:
/* 2274 */         return ((jjbitVec33[i2] & l2) != 0L);
/*      */       case 12:
/* 2276 */         return ((jjbitVec34[i2] & l2) != 0L);
/*      */       case 13:
/* 2278 */         return ((jjbitVec35[i2] & l2) != 0L);
/*      */       case 14:
/* 2280 */         return ((jjbitVec36[i2] & l2) != 0L);
/*      */       case 15:
/* 2282 */         return ((jjbitVec37[i2] & l2) != 0L);
/*      */       case 16:
/* 2284 */         return ((jjbitVec16[i2] & l2) != 0L);
/*      */       case 17:
/* 2286 */         return ((jjbitVec17[i2] & l2) != 0L);
/*      */       case 30:
/* 2288 */         return ((jjbitVec18[i2] & l2) != 0L);
/*      */       case 31:
/* 2290 */         return ((jjbitVec19[i2] & l2) != 0L);
/*      */       case 32:
/* 2292 */         return ((jjbitVec38[i2] & l2) != 0L);
/*      */       case 33:
/* 2294 */         return ((jjbitVec20[i2] & l2) != 0L);
/*      */       case 48:
/* 2296 */         return ((jjbitVec39[i2] & l2) != 0L);
/*      */       case 49:
/* 2298 */         return ((jjbitVec22[i2] & l2) != 0L);
/*      */       case 159:
/* 2300 */         return ((jjbitVec23[i2] & l2) != 0L);
/*      */       case 215:
/* 2302 */         return ((jjbitVec24[i2] & l2) != 0L);
/*      */     } 
/* 2304 */     if ((jjbitVec0[i1] & l1) != 0L)
/* 2305 */       return true; 
/* 2306 */     return false;
/*      */   }
/*      */   
/* 2309 */   public static final String[] jjstrLiteralImages = new String[] { "", null, null, null, null, null, null, null, null, null, null, null, null, null, null, ":", "/", "//", "attribute::", "@", "element::", "substitutionGroup::", "type::", "~", "baseType::", "primitiveType::", "itemType::", "memberType::", "scope::", "attributeGroup::", "group::", "identityContraint::", "key::", "notation::", "model::sequence", "model::choice", "model::all", "model::*", "any::*", "anyAttribute::*", "facet::*", "facet::", "component::*", "x-schema::", "x-schema::*", "*", "0" };
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
/* 2323 */   public static final String[] lexStateNames = new String[] { "DEFAULT" };
/*      */ 
/*      */   
/* 2326 */   static final long[] jjtoToken = new long[] { 140737488351233L };
/*      */ 
/*      */   
/* 2329 */   static final long[] jjtoSkip = new long[] { 62L };
/*      */   
/*      */   protected SimpleCharStream input_stream;
/*      */   
/* 2333 */   private final int[] jjrounds = new int[148];
/* 2334 */   private final int[] jjstateSet = new int[296]; protected char curChar; int curLexState;
/*      */   int defaultLexState;
/*      */   int jjnewStateCnt;
/*      */   int jjround;
/*      */   int jjmatchedPos;
/*      */   int jjmatchedKind;
/*      */   
/*      */   public SCDParserTokenManager(SimpleCharStream stream, int lexState) {
/* 2342 */     this(stream);
/* 2343 */     SwitchTo(lexState);
/*      */   }
/*      */   
/*      */   public void ReInit(SimpleCharStream stream) {
/* 2347 */     this.jjmatchedPos = this.jjnewStateCnt = 0;
/* 2348 */     this.curLexState = this.defaultLexState;
/* 2349 */     this.input_stream = stream;
/* 2350 */     ReInitRounds();
/*      */   }
/*      */ 
/*      */   
/*      */   private final void ReInitRounds() {
/* 2355 */     this.jjround = -2147483647;
/* 2356 */     for (int i = 148; i-- > 0;)
/* 2357 */       this.jjrounds[i] = Integer.MIN_VALUE; 
/*      */   }
/*      */   
/*      */   public void ReInit(SimpleCharStream stream, int lexState) {
/* 2361 */     ReInit(stream);
/* 2362 */     SwitchTo(lexState);
/*      */   }
/*      */   
/*      */   public void SwitchTo(int lexState) {
/* 2366 */     if (lexState >= 1 || lexState < 0) {
/* 2367 */       throw new TokenMgrError("Error: Ignoring invalid lexical state : " + lexState + ". State unchanged.", 2);
/*      */     }
/* 2369 */     this.curLexState = lexState;
/*      */   }
/*      */ 
/*      */   
/*      */   protected Token jjFillToken() {
/* 2374 */     Token t = Token.newToken(this.jjmatchedKind);
/* 2375 */     t.kind = this.jjmatchedKind;
/* 2376 */     String im = jjstrLiteralImages[this.jjmatchedKind];
/* 2377 */     t.image = (im == null) ? this.input_stream.GetImage() : im;
/* 2378 */     t.beginLine = this.input_stream.getBeginLine();
/* 2379 */     t.beginColumn = this.input_stream.getBeginColumn();
/* 2380 */     t.endLine = this.input_stream.getEndLine();
/* 2381 */     t.endColumn = this.input_stream.getEndColumn();
/* 2382 */     return t;
/*      */   }
/*      */   public SCDParserTokenManager(SimpleCharStream stream) {
/* 2385 */     this.curLexState = 0;
/* 2386 */     this.defaultLexState = 0;
/*      */     this.input_stream = stream;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Token getNextToken() {
/* 2395 */     Token specialToken = null;
/*      */     
/* 2397 */     int curPos = 0;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     while (true) {
/*      */       try {
/* 2404 */         this.curChar = this.input_stream.BeginToken();
/*      */       }
/* 2406 */       catch (IOException e) {
/*      */         
/* 2408 */         this.jjmatchedKind = 0;
/* 2409 */         Token matchedToken = jjFillToken();
/* 2410 */         return matchedToken;
/*      */       } 
/*      */       
/* 2413 */       try { this.input_stream.backup(0);
/* 2414 */         while (this.curChar <= ' ' && (0x100003600L & 1L << this.curChar) != 0L) {
/* 2415 */           this.curChar = this.input_stream.BeginToken();
/*      */         } }
/* 2417 */       catch (IOException e1) { continue; }
/* 2418 */        this.jjmatchedKind = Integer.MAX_VALUE;
/* 2419 */       this.jjmatchedPos = 0;
/* 2420 */       curPos = jjMoveStringLiteralDfa0_0();
/* 2421 */       if (this.jjmatchedKind != Integer.MAX_VALUE) {
/*      */         
/* 2423 */         if (this.jjmatchedPos + 1 < curPos)
/* 2424 */           this.input_stream.backup(curPos - this.jjmatchedPos - 1); 
/* 2425 */         if ((jjtoToken[this.jjmatchedKind >> 6] & 1L << (this.jjmatchedKind & 0x3F)) != 0L) {
/*      */           
/* 2427 */           Token matchedToken = jjFillToken();
/* 2428 */           return matchedToken;
/*      */         } 
/*      */         
/*      */         continue;
/*      */       } 
/*      */       break;
/*      */     } 
/* 2435 */     int error_line = this.input_stream.getEndLine();
/* 2436 */     int error_column = this.input_stream.getEndColumn();
/* 2437 */     String error_after = null;
/* 2438 */     boolean EOFSeen = false; try {
/* 2439 */       this.input_stream.readChar(); this.input_stream.backup(1);
/* 2440 */     } catch (IOException e1) {
/* 2441 */       EOFSeen = true;
/* 2442 */       error_after = (curPos <= 1) ? "" : this.input_stream.GetImage();
/* 2443 */       if (this.curChar == '\n' || this.curChar == '\r') {
/* 2444 */         error_line++;
/* 2445 */         error_column = 0;
/*      */       } else {
/*      */         
/* 2448 */         error_column++;
/*      */       } 
/* 2450 */     }  if (!EOFSeen) {
/* 2451 */       this.input_stream.backup(1);
/* 2452 */       error_after = (curPos <= 1) ? "" : this.input_stream.GetImage();
/*      */     } 
/* 2454 */     throw new TokenMgrError(EOFSeen, this.curLexState, error_line, error_column, error_after, this.curChar, 0);
/*      */   }
/*      */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\xml\internal\xsom\impl\scd\SCDParserTokenManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */