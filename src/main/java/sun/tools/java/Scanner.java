/*      */ package sun.tools.java;
/*      */ 
/*      */ import java.io.IOException;
/*      */ import java.io.InputStream;
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
/*      */ public class Scanner
/*      */   implements Constants
/*      */ {
/*      */   public static final long OFFSETINC = 1L;
/*      */   public static final long LINEINC = 4294967296L;
/*      */   public static final int EOF = -1;
/*      */   public Environment env;
/*      */   protected ScannerInputReader in;
/*      */   public boolean scanComments = false;
/*      */   public int token;
/*      */   public long pos;
/*      */   public long prevPos;
/*      */   protected int ch;
/*      */   public char charValue;
/*      */   public int intValue;
/*      */   public long longValue;
/*      */   public float floatValue;
/*      */   public double doubleValue;
/*      */   public String stringValue;
/*      */   public Identifier idValue;
/*      */   public int radix;
/*      */   public String docComment;
/*      */   private int count;
/*  139 */   private char[] buffer = new char[1024];
/*      */   private void growBuffer() {
/*  141 */     char[] arrayOfChar = new char[this.buffer.length * 2];
/*  142 */     System.arraycopy(this.buffer, 0, arrayOfChar, 0, this.buffer.length);
/*  143 */     this.buffer = arrayOfChar;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void putc(int paramInt) {
/*  150 */     if (this.count == this.buffer.length) {
/*  151 */       growBuffer();
/*      */     }
/*  153 */     this.buffer[this.count++] = (char)paramInt;
/*      */   }
/*      */   
/*      */   private String bufferString() {
/*  157 */     return new String(this.buffer, 0, this.count);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Scanner(Environment paramEnvironment, InputStream paramInputStream) throws IOException {
/*  164 */     this.env = paramEnvironment;
/*  165 */     useInputStream(paramInputStream);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void useInputStream(InputStream paramInputStream) throws IOException {
/*      */     try {
/*  174 */       this.in = new ScannerInputReader(this.env, paramInputStream);
/*  175 */     } catch (Exception exception) {
/*  176 */       this.env.setCharacterEncoding(null);
/*  177 */       this.in = new ScannerInputReader(this.env, paramInputStream);
/*      */     } 
/*      */     
/*  180 */     this.ch = this.in.read();
/*  181 */     this.prevPos = this.in.pos;
/*      */     
/*  183 */     scan();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected Scanner(Environment paramEnvironment) {
/*  190 */     this.env = paramEnvironment;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void defineKeyword(int paramInt) {
/*  198 */     Identifier.lookup(opNames[paramInt]).setType(paramInt);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static {
/*  206 */     defineKeyword(92);
/*  207 */     defineKeyword(90);
/*  208 */     defineKeyword(91);
/*  209 */     defineKeyword(93);
/*  210 */     defineKeyword(94);
/*  211 */     defineKeyword(95);
/*  212 */     defineKeyword(96);
/*  213 */     defineKeyword(97);
/*  214 */     defineKeyword(98);
/*  215 */     defineKeyword(99);
/*  216 */     defineKeyword(100);
/*  217 */     defineKeyword(101);
/*  218 */     defineKeyword(102);
/*  219 */     defineKeyword(103);
/*  220 */     defineKeyword(104);
/*      */ 
/*      */     
/*  223 */     defineKeyword(70);
/*  224 */     defineKeyword(71);
/*  225 */     defineKeyword(72);
/*  226 */     defineKeyword(73);
/*  227 */     defineKeyword(74);
/*  228 */     defineKeyword(75);
/*  229 */     defineKeyword(76);
/*  230 */     defineKeyword(77);
/*  231 */     defineKeyword(78);
/*      */ 
/*      */     
/*  234 */     defineKeyword(25);
/*  235 */     defineKeyword(80);
/*  236 */     defineKeyword(81);
/*  237 */     defineKeyword(49);
/*  238 */     defineKeyword(82);
/*  239 */     defineKeyword(83);
/*  240 */     defineKeyword(84);
/*      */ 
/*      */     
/*  243 */     defineKeyword(110);
/*  244 */     defineKeyword(111);
/*  245 */     defineKeyword(112);
/*  246 */     defineKeyword(113);
/*  247 */     defineKeyword(114);
/*  248 */     defineKeyword(115);
/*  249 */     defineKeyword(144);
/*      */ 
/*      */     
/*  252 */     defineKeyword(120);
/*  253 */     defineKeyword(121);
/*  254 */     defineKeyword(122);
/*  255 */     defineKeyword(124);
/*  256 */     defineKeyword(125);
/*  257 */     defineKeyword(126);
/*  258 */     defineKeyword(127);
/*  259 */     defineKeyword(130);
/*  260 */     defineKeyword(129);
/*  261 */     defineKeyword(128);
/*  262 */     defineKeyword(131);
/*      */ 
/*      */     
/*  265 */     defineKeyword(123);
/*  266 */     defineKeyword(58);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void skipComment() throws IOException {
/*      */     while (true) {
/*  276 */       switch (this.ch) {
/*      */         case -1:
/*  278 */           this.env.error(this.pos, "eof.in.comment");
/*      */           return;
/*      */         
/*      */         case 42:
/*  282 */           if ((this.ch = this.in.read()) == 47) {
/*  283 */             this.ch = this.in.read();
/*      */             return;
/*      */           } 
/*      */           continue;
/*      */       } 
/*      */       
/*  289 */       this.ch = this.in.read();
/*      */     } 
/*      */   }
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
/*      */   private String scanDocComment() throws IOException {
/*  324 */     ScannerInputReader scannerInputReader = this.in;
/*      */ 
/*      */     
/*  327 */     char[] arrayOfChar = this.buffer;
/*  328 */     int j = 0;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     int i;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  339 */     while ((i = scannerInputReader.read()) == 42);
/*      */ 
/*      */ 
/*      */     
/*  343 */     if (i == 47) {
/*      */       
/*  345 */       this.ch = scannerInputReader.read();
/*  346 */       return "";
/*      */     } 
/*      */ 
/*      */     
/*  350 */     if (i == 10) {
/*  351 */       i = scannerInputReader.read();
/*      */     }
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
/*      */     label52: while (true) {
/*  365 */       switch (i) {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*      */         case 9:
/*      */         case 32:
/*  373 */           i = scannerInputReader.read();
/*      */           continue;
/*      */       } 
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
/*  393 */       if (i == 42) {
/*      */         
/*      */         do {
/*  396 */           i = scannerInputReader.read();
/*  397 */         } while (i == 42);
/*      */ 
/*      */         
/*  400 */         if (i == 47) {
/*      */ 
/*      */           
/*  403 */           this.ch = scannerInputReader.read();
/*      */ 
/*      */           
/*      */           break;
/*      */         } 
/*      */       } 
/*      */ 
/*      */       
/*      */       while (true) {
/*  412 */         switch (i) {
/*      */ 
/*      */           
/*      */           case -1:
/*  416 */             this.env.error(this.pos, "eof.in.comment");
/*  417 */             this.ch = -1;
/*      */             break;
/*      */ 
/*      */ 
/*      */           
/*      */           case 42:
/*  423 */             i = scannerInputReader.read();
/*  424 */             if (i == 47) {
/*      */ 
/*      */               
/*  427 */               this.ch = scannerInputReader.read();
/*      */               
/*      */               break;
/*      */             } 
/*      */             
/*  432 */             if (j == arrayOfChar.length) {
/*  433 */               growBuffer();
/*  434 */               arrayOfChar = this.buffer;
/*      */             } 
/*  436 */             arrayOfChar[j++] = '*';
/*      */             continue;
/*      */ 
/*      */ 
/*      */ 
/*      */           
/*      */           case 10:
/*  443 */             if (j == arrayOfChar.length) {
/*  444 */               growBuffer();
/*  445 */               arrayOfChar = this.buffer;
/*      */             } 
/*  447 */             arrayOfChar[j++] = '\n';
/*  448 */             i = scannerInputReader.read();
/*      */             continue label52;
/*      */         } 
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
/*  462 */         if (j == arrayOfChar.length) {
/*  463 */           growBuffer();
/*  464 */           arrayOfChar = this.buffer;
/*      */         } 
/*  466 */         arrayOfChar[j++] = (char)i;
/*  467 */         i = scannerInputReader.read();
/*      */       } 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       break;
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/*  478 */     if (j > 0) {
/*  479 */       int k = j - 1;
/*      */       
/*  481 */       while (k > -1) {
/*  482 */         switch (arrayOfChar[k]) {
/*      */           case '\t':
/*      */           case ' ':
/*      */           case '*':
/*  486 */             k--;
/*      */         } 
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
/*      */       } 
/*  501 */       j = k + 1;
/*      */ 
/*      */       
/*  504 */       return new String(arrayOfChar, 0, j);
/*      */     } 
/*  506 */     return "";
/*      */   }
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
/*      */   private void scanNumber() throws IOException {
/*      */     // Byte code:
/*      */     //   0: iconst_0
/*      */     //   1: istore_1
/*      */     //   2: iconst_0
/*      */     //   3: istore_2
/*      */     //   4: iconst_0
/*      */     //   5: istore_3
/*      */     //   6: aload_0
/*      */     //   7: aload_0
/*      */     //   8: getfield ch : I
/*      */     //   11: bipush #48
/*      */     //   13: if_icmpne -> 21
/*      */     //   16: bipush #8
/*      */     //   18: goto -> 23
/*      */     //   21: bipush #10
/*      */     //   23: putfield radix : I
/*      */     //   26: aload_0
/*      */     //   27: getfield ch : I
/*      */     //   30: bipush #48
/*      */     //   32: isub
/*      */     //   33: i2l
/*      */     //   34: lstore #4
/*      */     //   36: aload_0
/*      */     //   37: iconst_0
/*      */     //   38: putfield count : I
/*      */     //   41: aload_0
/*      */     //   42: aload_0
/*      */     //   43: getfield ch : I
/*      */     //   46: invokespecial putc : (I)V
/*      */     //   49: aload_0
/*      */     //   50: aload_0
/*      */     //   51: getfield in : Lsun/tools/java/ScannerInputReader;
/*      */     //   54: invokevirtual read : ()I
/*      */     //   57: dup_x1
/*      */     //   58: putfield ch : I
/*      */     //   61: tableswitch default -> 703, 46 -> 376, 47 -> 703, 48 -> 395, 49 -> 395, 50 -> 395, 51 -> 395, 52 -> 395, 53 -> 395, 54 -> 395, 55 -> 395, 56 -> 393, 57 -> 393, 58 -> 703, 59 -> 703, 60 -> 703, 61 -> 703, 62 -> 703, 63 -> 703, 64 -> 703, 65 -> 580, 66 -> 580, 67 -> 580, 68 -> 566, 69 -> 566, 70 -> 566, 71 -> 703, 72 -> 703, 73 -> 703, 74 -> 703, 75 -> 703, 76 -> 649, 77 -> 703, 78 -> 703, 79 -> 703, 80 -> 703, 81 -> 703, 82 -> 703, 83 -> 703, 84 -> 703, 85 -> 703, 86 -> 703, 87 -> 703, 88 -> 675, 89 -> 703, 90 -> 703, 91 -> 703, 92 -> 703, 93 -> 703, 94 -> 703, 95 -> 703, 96 -> 703, 97 -> 580, 98 -> 580, 99 -> 580, 100 -> 566, 101 -> 566, 102 -> 566, 103 -> 703, 104 -> 703, 105 -> 703, 106 -> 703, 107 -> 703, 108 -> 649, 109 -> 703, 110 -> 703, 111 -> 703, 112 -> 703, 113 -> 703, 114 -> 703, 115 -> 703, 116 -> 703, 117 -> 703, 118 -> 703, 119 -> 703, 120 -> 675
/*      */     //   376: aload_0
/*      */     //   377: getfield radix : I
/*      */     //   380: bipush #16
/*      */     //   382: if_icmpne -> 388
/*      */     //   385: goto -> 719
/*      */     //   388: aload_0
/*      */     //   389: invokespecial scanReal : ()V
/*      */     //   392: return
/*      */     //   393: iconst_1
/*      */     //   394: istore_1
/*      */     //   395: iconst_1
/*      */     //   396: istore_3
/*      */     //   397: aload_0
/*      */     //   398: aload_0
/*      */     //   399: getfield ch : I
/*      */     //   402: invokespecial putc : (I)V
/*      */     //   405: aload_0
/*      */     //   406: getfield radix : I
/*      */     //   409: bipush #10
/*      */     //   411: if_icmpne -> 481
/*      */     //   414: iload_2
/*      */     //   415: ifne -> 434
/*      */     //   418: lload #4
/*      */     //   420: ldc2_w 10
/*      */     //   423: lmul
/*      */     //   424: ldc2_w 10
/*      */     //   427: ldiv
/*      */     //   428: lload #4
/*      */     //   430: lcmp
/*      */     //   431: ifeq -> 438
/*      */     //   434: iconst_1
/*      */     //   435: goto -> 439
/*      */     //   438: iconst_0
/*      */     //   439: istore_2
/*      */     //   440: lload #4
/*      */     //   442: ldc2_w 10
/*      */     //   445: lmul
/*      */     //   446: aload_0
/*      */     //   447: getfield ch : I
/*      */     //   450: bipush #48
/*      */     //   452: isub
/*      */     //   453: i2l
/*      */     //   454: ladd
/*      */     //   455: lstore #4
/*      */     //   457: iload_2
/*      */     //   458: ifne -> 472
/*      */     //   461: lload #4
/*      */     //   463: lconst_1
/*      */     //   464: lsub
/*      */     //   465: ldc2_w -1
/*      */     //   468: lcmp
/*      */     //   469: ifge -> 476
/*      */     //   472: iconst_1
/*      */     //   473: goto -> 477
/*      */     //   476: iconst_0
/*      */     //   477: istore_2
/*      */     //   478: goto -> 49
/*      */     //   481: aload_0
/*      */     //   482: getfield radix : I
/*      */     //   485: bipush #8
/*      */     //   487: if_icmpne -> 528
/*      */     //   490: iload_2
/*      */     //   491: ifne -> 504
/*      */     //   494: lload #4
/*      */     //   496: bipush #61
/*      */     //   498: lushr
/*      */     //   499: lconst_0
/*      */     //   500: lcmp
/*      */     //   501: ifeq -> 508
/*      */     //   504: iconst_1
/*      */     //   505: goto -> 509
/*      */     //   508: iconst_0
/*      */     //   509: istore_2
/*      */     //   510: lload #4
/*      */     //   512: iconst_3
/*      */     //   513: lshl
/*      */     //   514: aload_0
/*      */     //   515: getfield ch : I
/*      */     //   518: bipush #48
/*      */     //   520: isub
/*      */     //   521: i2l
/*      */     //   522: ladd
/*      */     //   523: lstore #4
/*      */     //   525: goto -> 49
/*      */     //   528: iload_2
/*      */     //   529: ifne -> 542
/*      */     //   532: lload #4
/*      */     //   534: bipush #60
/*      */     //   536: lushr
/*      */     //   537: lconst_0
/*      */     //   538: lcmp
/*      */     //   539: ifeq -> 546
/*      */     //   542: iconst_1
/*      */     //   543: goto -> 547
/*      */     //   546: iconst_0
/*      */     //   547: istore_2
/*      */     //   548: lload #4
/*      */     //   550: iconst_4
/*      */     //   551: lshl
/*      */     //   552: aload_0
/*      */     //   553: getfield ch : I
/*      */     //   556: bipush #48
/*      */     //   558: isub
/*      */     //   559: i2l
/*      */     //   560: ladd
/*      */     //   561: lstore #4
/*      */     //   563: goto -> 49
/*      */     //   566: aload_0
/*      */     //   567: getfield radix : I
/*      */     //   570: bipush #16
/*      */     //   572: if_icmpeq -> 580
/*      */     //   575: aload_0
/*      */     //   576: invokespecial scanReal : ()V
/*      */     //   579: return
/*      */     //   580: iconst_1
/*      */     //   581: istore_3
/*      */     //   582: aload_0
/*      */     //   583: aload_0
/*      */     //   584: getfield ch : I
/*      */     //   587: invokespecial putc : (I)V
/*      */     //   590: aload_0
/*      */     //   591: getfield radix : I
/*      */     //   594: bipush #16
/*      */     //   596: if_icmpeq -> 602
/*      */     //   599: goto -> 719
/*      */     //   602: iload_2
/*      */     //   603: ifne -> 616
/*      */     //   606: lload #4
/*      */     //   608: bipush #60
/*      */     //   610: lushr
/*      */     //   611: lconst_0
/*      */     //   612: lcmp
/*      */     //   613: ifeq -> 620
/*      */     //   616: iconst_1
/*      */     //   617: goto -> 621
/*      */     //   620: iconst_0
/*      */     //   621: istore_2
/*      */     //   622: lload #4
/*      */     //   624: iconst_4
/*      */     //   625: lshl
/*      */     //   626: ldc2_w 10
/*      */     //   629: ladd
/*      */     //   630: aload_0
/*      */     //   631: getfield ch : I
/*      */     //   634: i2c
/*      */     //   635: invokestatic toLowerCase : (C)C
/*      */     //   638: i2l
/*      */     //   639: ladd
/*      */     //   640: ldc2_w 97
/*      */     //   643: lsub
/*      */     //   644: lstore #4
/*      */     //   646: goto -> 49
/*      */     //   649: aload_0
/*      */     //   650: aload_0
/*      */     //   651: getfield in : Lsun/tools/java/ScannerInputReader;
/*      */     //   654: invokevirtual read : ()I
/*      */     //   657: putfield ch : I
/*      */     //   660: aload_0
/*      */     //   661: lload #4
/*      */     //   663: putfield longValue : J
/*      */     //   666: aload_0
/*      */     //   667: bipush #66
/*      */     //   669: putfield token : I
/*      */     //   672: goto -> 719
/*      */     //   675: aload_0
/*      */     //   676: getfield count : I
/*      */     //   679: iconst_1
/*      */     //   680: if_icmpne -> 719
/*      */     //   683: aload_0
/*      */     //   684: getfield radix : I
/*      */     //   687: bipush #8
/*      */     //   689: if_icmpne -> 719
/*      */     //   692: aload_0
/*      */     //   693: bipush #16
/*      */     //   695: putfield radix : I
/*      */     //   698: iconst_0
/*      */     //   699: istore_3
/*      */     //   700: goto -> 49
/*      */     //   703: aload_0
/*      */     //   704: lload #4
/*      */     //   706: l2i
/*      */     //   707: putfield intValue : I
/*      */     //   710: aload_0
/*      */     //   711: bipush #65
/*      */     //   713: putfield token : I
/*      */     //   716: goto -> 719
/*      */     //   719: aload_0
/*      */     //   720: getfield ch : I
/*      */     //   723: i2c
/*      */     //   724: invokestatic isJavaLetterOrDigit : (C)Z
/*      */     //   727: ifne -> 739
/*      */     //   730: aload_0
/*      */     //   731: getfield ch : I
/*      */     //   734: bipush #46
/*      */     //   736: if_icmpne -> 800
/*      */     //   739: aload_0
/*      */     //   740: getfield env : Lsun/tools/java/Environment;
/*      */     //   743: aload_0
/*      */     //   744: getfield in : Lsun/tools/java/ScannerInputReader;
/*      */     //   747: getfield pos : J
/*      */     //   750: ldc 'invalid.number'
/*      */     //   752: invokevirtual error : (JLjava/lang/String;)V
/*      */     //   755: aload_0
/*      */     //   756: aload_0
/*      */     //   757: getfield in : Lsun/tools/java/ScannerInputReader;
/*      */     //   760: invokevirtual read : ()I
/*      */     //   763: putfield ch : I
/*      */     //   766: aload_0
/*      */     //   767: getfield ch : I
/*      */     //   770: i2c
/*      */     //   771: invokestatic isJavaLetterOrDigit : (C)Z
/*      */     //   774: ifne -> 755
/*      */     //   777: aload_0
/*      */     //   778: getfield ch : I
/*      */     //   781: bipush #46
/*      */     //   783: if_icmpeq -> 755
/*      */     //   786: aload_0
/*      */     //   787: iconst_0
/*      */     //   788: putfield intValue : I
/*      */     //   791: aload_0
/*      */     //   792: bipush #65
/*      */     //   794: putfield token : I
/*      */     //   797: goto -> 1138
/*      */     //   800: aload_0
/*      */     //   801: getfield radix : I
/*      */     //   804: bipush #8
/*      */     //   806: if_icmpne -> 840
/*      */     //   809: iload_1
/*      */     //   810: ifeq -> 840
/*      */     //   813: aload_0
/*      */     //   814: iconst_0
/*      */     //   815: putfield intValue : I
/*      */     //   818: aload_0
/*      */     //   819: bipush #65
/*      */     //   821: putfield token : I
/*      */     //   824: aload_0
/*      */     //   825: getfield env : Lsun/tools/java/Environment;
/*      */     //   828: aload_0
/*      */     //   829: getfield pos : J
/*      */     //   832: ldc 'invalid.octal.number'
/*      */     //   834: invokevirtual error : (JLjava/lang/String;)V
/*      */     //   837: goto -> 1138
/*      */     //   840: aload_0
/*      */     //   841: getfield radix : I
/*      */     //   844: bipush #16
/*      */     //   846: if_icmpne -> 880
/*      */     //   849: iload_3
/*      */     //   850: ifne -> 880
/*      */     //   853: aload_0
/*      */     //   854: iconst_0
/*      */     //   855: putfield intValue : I
/*      */     //   858: aload_0
/*      */     //   859: bipush #65
/*      */     //   861: putfield token : I
/*      */     //   864: aload_0
/*      */     //   865: getfield env : Lsun/tools/java/Environment;
/*      */     //   868: aload_0
/*      */     //   869: getfield pos : J
/*      */     //   872: ldc 'invalid.hex.number'
/*      */     //   874: invokevirtual error : (JLjava/lang/String;)V
/*      */     //   877: goto -> 1138
/*      */     //   880: aload_0
/*      */     //   881: getfield token : I
/*      */     //   884: bipush #65
/*      */     //   886: if_icmpne -> 1034
/*      */     //   889: iload_2
/*      */     //   890: ifne -> 922
/*      */     //   893: lload #4
/*      */     //   895: ldc2_w -4294967296
/*      */     //   898: land
/*      */     //   899: lconst_0
/*      */     //   900: lcmp
/*      */     //   901: ifne -> 922
/*      */     //   904: aload_0
/*      */     //   905: getfield radix : I
/*      */     //   908: bipush #10
/*      */     //   910: if_icmpne -> 926
/*      */     //   913: lload #4
/*      */     //   915: ldc2_w 2147483648
/*      */     //   918: lcmp
/*      */     //   919: ifle -> 926
/*      */     //   922: iconst_1
/*      */     //   923: goto -> 927
/*      */     //   926: iconst_0
/*      */     //   927: istore_2
/*      */     //   928: iload_2
/*      */     //   929: ifeq -> 1138
/*      */     //   932: aload_0
/*      */     //   933: iconst_0
/*      */     //   934: putfield intValue : I
/*      */     //   937: aload_0
/*      */     //   938: getfield radix : I
/*      */     //   941: lookupswitch default -> 1024, 8 -> 976, 10 -> 992, 16 -> 1008
/*      */     //   976: aload_0
/*      */     //   977: getfield env : Lsun/tools/java/Environment;
/*      */     //   980: aload_0
/*      */     //   981: getfield pos : J
/*      */     //   984: ldc 'overflow.int.oct'
/*      */     //   986: invokevirtual error : (JLjava/lang/String;)V
/*      */     //   989: goto -> 1138
/*      */     //   992: aload_0
/*      */     //   993: getfield env : Lsun/tools/java/Environment;
/*      */     //   996: aload_0
/*      */     //   997: getfield pos : J
/*      */     //   1000: ldc 'overflow.int.dec'
/*      */     //   1002: invokevirtual error : (JLjava/lang/String;)V
/*      */     //   1005: goto -> 1138
/*      */     //   1008: aload_0
/*      */     //   1009: getfield env : Lsun/tools/java/Environment;
/*      */     //   1012: aload_0
/*      */     //   1013: getfield pos : J
/*      */     //   1016: ldc 'overflow.int.hex'
/*      */     //   1018: invokevirtual error : (JLjava/lang/String;)V
/*      */     //   1021: goto -> 1138
/*      */     //   1024: new sun/tools/java/CompilerError
/*      */     //   1027: dup
/*      */     //   1028: ldc 'invalid radix'
/*      */     //   1030: invokespecial <init> : (Ljava/lang/String;)V
/*      */     //   1033: athrow
/*      */     //   1034: iload_2
/*      */     //   1035: ifeq -> 1138
/*      */     //   1038: aload_0
/*      */     //   1039: lconst_0
/*      */     //   1040: putfield longValue : J
/*      */     //   1043: aload_0
/*      */     //   1044: getfield radix : I
/*      */     //   1047: lookupswitch default -> 1128, 8 -> 1080, 10 -> 1096, 16 -> 1112
/*      */     //   1080: aload_0
/*      */     //   1081: getfield env : Lsun/tools/java/Environment;
/*      */     //   1084: aload_0
/*      */     //   1085: getfield pos : J
/*      */     //   1088: ldc 'overflow.long.oct'
/*      */     //   1090: invokevirtual error : (JLjava/lang/String;)V
/*      */     //   1093: goto -> 1138
/*      */     //   1096: aload_0
/*      */     //   1097: getfield env : Lsun/tools/java/Environment;
/*      */     //   1100: aload_0
/*      */     //   1101: getfield pos : J
/*      */     //   1104: ldc 'overflow.long.dec'
/*      */     //   1106: invokevirtual error : (JLjava/lang/String;)V
/*      */     //   1109: goto -> 1138
/*      */     //   1112: aload_0
/*      */     //   1113: getfield env : Lsun/tools/java/Environment;
/*      */     //   1116: aload_0
/*      */     //   1117: getfield pos : J
/*      */     //   1120: ldc 'overflow.long.hex'
/*      */     //   1122: invokevirtual error : (JLjava/lang/String;)V
/*      */     //   1125: goto -> 1138
/*      */     //   1128: new sun/tools/java/CompilerError
/*      */     //   1131: dup
/*      */     //   1132: ldc 'invalid radix'
/*      */     //   1134: invokespecial <init> : (Ljava/lang/String;)V
/*      */     //   1137: athrow
/*      */     //   1138: return
/*      */     // Line number table:
/*      */     //   Java source line number -> byte code offset
/*      */     //   #515	-> 0
/*      */     //   #516	-> 2
/*      */     //   #517	-> 4
/*      */     //   #518	-> 6
/*      */     //   #519	-> 26
/*      */     //   #520	-> 36
/*      */     //   #521	-> 41
/*      */     //   #524	-> 49
/*      */     //   #526	-> 376
/*      */     //   #527	-> 385
/*      */     //   #528	-> 388
/*      */     //   #529	-> 392
/*      */     //   #534	-> 393
/*      */     //   #537	-> 395
/*      */     //   #538	-> 397
/*      */     //   #539	-> 405
/*      */     //   #540	-> 414
/*      */     //   #541	-> 440
/*      */     //   #542	-> 457
/*      */     //   #543	-> 481
/*      */     //   #544	-> 490
/*      */     //   #545	-> 510
/*      */     //   #547	-> 528
/*      */     //   #548	-> 548
/*      */     //   #550	-> 563
/*      */     //   #553	-> 566
/*      */     //   #554	-> 575
/*      */     //   #555	-> 579
/*      */     //   #559	-> 580
/*      */     //   #560	-> 582
/*      */     //   #561	-> 590
/*      */     //   #562	-> 599
/*      */     //   #563	-> 602
/*      */     //   #564	-> 622
/*      */     //   #565	-> 635
/*      */     //   #566	-> 646
/*      */     //   #569	-> 649
/*      */     //   #570	-> 660
/*      */     //   #571	-> 666
/*      */     //   #572	-> 672
/*      */     //   #577	-> 675
/*      */     //   #578	-> 692
/*      */     //   #579	-> 698
/*      */     //   #580	-> 700
/*      */     //   #587	-> 703
/*      */     //   #588	-> 710
/*      */     //   #589	-> 716
/*      */     //   #598	-> 719
/*      */     //   #599	-> 739
/*      */     //   #600	-> 755
/*      */     //   #601	-> 766
/*      */     //   #602	-> 786
/*      */     //   #603	-> 791
/*      */     //   #604	-> 800
/*      */     //   #606	-> 813
/*      */     //   #607	-> 818
/*      */     //   #608	-> 824
/*      */     //   #609	-> 840
/*      */     //   #611	-> 853
/*      */     //   #612	-> 858
/*      */     //   #613	-> 864
/*      */     //   #615	-> 880
/*      */     //   #618	-> 889
/*      */     //   #622	-> 928
/*      */     //   #623	-> 932
/*      */     //   #627	-> 937
/*      */     //   #629	-> 976
/*      */     //   #630	-> 989
/*      */     //   #632	-> 992
/*      */     //   #633	-> 1005
/*      */     //   #635	-> 1008
/*      */     //   #636	-> 1021
/*      */     //   #638	-> 1024
/*      */     //   #642	-> 1034
/*      */     //   #643	-> 1038
/*      */     //   #647	-> 1043
/*      */     //   #649	-> 1080
/*      */     //   #650	-> 1093
/*      */     //   #652	-> 1096
/*      */     //   #653	-> 1109
/*      */     //   #655	-> 1112
/*      */     //   #656	-> 1125
/*      */     //   #658	-> 1128
/*      */     //   #663	-> 1138
/*      */   }
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
/*      */   private void scanReal() throws IOException {
/*  672 */     boolean bool1 = false;
/*  673 */     boolean bool2 = false;
/*      */     
/*  675 */     if (this.ch == 46) {
/*  676 */       putc(this.ch);
/*  677 */       this.ch = this.in.read();
/*      */     } 
/*      */ 
/*      */     
/*  681 */     for (;; this.ch = this.in.read()) {
/*  682 */       char c; switch (this.ch) { case 48: case 49: case 50: case 51: case 52: case 53: case 54: case 55:
/*      */         case 56:
/*      */         case 57:
/*  685 */           putc(this.ch);
/*      */           break;
/*      */         case 69:
/*      */         case 101:
/*  689 */           if (bool1)
/*      */             break; 
/*  691 */           putc(this.ch);
/*  692 */           bool1 = true;
/*      */           break;
/*      */         case 43:
/*      */         case 45:
/*  696 */           c = this.buffer[this.count - 1];
/*  697 */           if (c != 'e' && c != 'E')
/*      */             break; 
/*  699 */           putc(this.ch);
/*      */           break;
/*      */         case 70:
/*      */         case 102:
/*  703 */           this.ch = this.in.read();
/*  704 */           bool2 = true;
/*      */           break;
/*      */         case 68:
/*      */         case 100:
/*  708 */           this.ch = this.in.read();
/*      */           break;
/*      */ 
/*      */         
/*      */         default:
/*      */           break; }
/*      */ 
/*      */     
/*      */     } 
/*  717 */     if (Character.isJavaLetterOrDigit((char)this.ch) || this.ch == 46) {
/*  718 */       this.env.error(this.in.pos, "invalid.number"); while (true) {
/*  719 */         this.ch = this.in.read();
/*  720 */         if (!Character.isJavaLetterOrDigit((char)this.ch) && this.ch != 46)
/*  721 */         { this.doubleValue = 0.0D;
/*  722 */           this.token = 68; return; } 
/*      */       } 
/*  724 */     }  this.token = bool2 ? 67 : 68;
/*      */     try {
/*  726 */       char c = this.buffer[this.count - 1];
/*  727 */       if (c == 'e' || c == 'E' || c == '+' || c == '-') {
/*      */         
/*  729 */         this.env.error(this.in.pos - 1L, "float.format");
/*  730 */       } else if (bool2) {
/*  731 */         String str = bufferString();
/*  732 */         this.floatValue = Float.valueOf(str).floatValue();
/*  733 */         if (Float.isInfinite(this.floatValue)) {
/*  734 */           this.env.error(this.pos, "overflow.float");
/*  735 */         } else if (this.floatValue == 0.0F && !looksLikeZero(str)) {
/*  736 */           this.env.error(this.pos, "underflow.float");
/*      */         } 
/*      */       } else {
/*  739 */         String str = bufferString();
/*  740 */         this.doubleValue = Double.valueOf(str).doubleValue();
/*  741 */         if (Double.isInfinite(this.doubleValue)) {
/*  742 */           this.env.error(this.pos, "overflow.double");
/*  743 */         } else if (this.doubleValue == 0.0D && !looksLikeZero(str)) {
/*  744 */           this.env.error(this.pos, "underflow.double");
/*      */         } 
/*      */       } 
/*  747 */     } catch (NumberFormatException numberFormatException) {
/*  748 */       this.env.error(this.pos, "float.format");
/*  749 */       this.doubleValue = 0.0D;
/*  750 */       this.floatValue = 0.0F;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static boolean looksLikeZero(String paramString) {
/*  759 */     int i = paramString.length();
/*  760 */     for (byte b = 0; b < i; b++) {
/*  761 */       switch (paramString.charAt(b)) { case '1': case '2': case '3': case '4': case '5':
/*      */         case '6':
/*      */         case '7':
/*      */         case '8':
/*      */         case '9':
/*  766 */           return false;
/*      */         case 'E': case 'F': case 'e': case 'f':
/*  768 */           return true; }
/*      */     
/*      */     } 
/*  771 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private int scanEscapeChar() throws IOException {
/*      */     int i;
/*      */     byte b;
/*  780 */     long l = this.in.pos;
/*      */     
/*  782 */     switch (this.ch = this.in.read()) { case 48: case 49: case 50: case 51: case 52: case 53:
/*      */       case 54:
/*      */       case 55:
/*  785 */         i = this.ch - 48;
/*  786 */         for (b = 2; b > 0; b--) {
/*  787 */           switch (this.ch = this.in.read()) { case 48: case 49: case 50: case 51: case 52: case 53:
/*      */             case 54:
/*      */             case 55:
/*  790 */               i = (i << 3) + this.ch - 48;
/*      */               break;
/*      */             
/*      */             default:
/*  794 */               if (i > 255) {
/*  795 */                 this.env.error(l, "invalid.escape.char");
/*      */               }
/*  797 */               return i; }
/*      */         
/*      */         } 
/*  800 */         this.ch = this.in.read();
/*  801 */         if (i > 255) {
/*  802 */           this.env.error(l, "invalid.escape.char");
/*      */         }
/*  804 */         return i;
/*      */       
/*      */       case 114:
/*  807 */         this.ch = this.in.read(); return 13;
/*  808 */       case 110: this.ch = this.in.read(); return 10;
/*  809 */       case 102: this.ch = this.in.read(); return 12;
/*  810 */       case 98: this.ch = this.in.read(); return 8;
/*  811 */       case 116: this.ch = this.in.read(); return 9;
/*  812 */       case 92: this.ch = this.in.read(); return 92;
/*  813 */       case 34: this.ch = this.in.read(); return 34;
/*  814 */       case 39: this.ch = this.in.read(); return 39; }
/*      */ 
/*      */     
/*  817 */     this.env.error(l, "invalid.escape.char");
/*  818 */     this.ch = this.in.read();
/*  819 */     return -1;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void scanString() throws IOException {
/*  827 */     this.token = 69;
/*  828 */     this.count = 0;
/*  829 */     this.ch = this.in.read();
/*      */     
/*      */     while (true) {
/*      */       int i;
/*  833 */       switch (this.ch) {
/*      */         case -1:
/*  835 */           this.env.error(this.pos, "eof.in.string");
/*  836 */           this.stringValue = bufferString();
/*      */           return;
/*      */         
/*      */         case 10:
/*      */         case 13:
/*  841 */           this.ch = this.in.read();
/*  842 */           this.env.error(this.pos, "newline.in.string");
/*  843 */           this.stringValue = bufferString();
/*      */           return;
/*      */         
/*      */         case 34:
/*  847 */           this.ch = this.in.read();
/*  848 */           this.stringValue = bufferString();
/*      */           return;
/*      */         
/*      */         case 92:
/*  852 */           i = scanEscapeChar();
/*  853 */           if (i >= 0) {
/*  854 */             putc((char)i);
/*      */           }
/*      */           continue;
/*      */       } 
/*      */ 
/*      */       
/*  860 */       putc(this.ch);
/*  861 */       this.ch = this.in.read();
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void scanCharacter() throws IOException {
/*      */     int i;
/*  872 */     this.token = 63;
/*      */     
/*  874 */     switch (this.ch = this.in.read()) {
/*      */       case 92:
/*  876 */         i = scanEscapeChar();
/*  877 */         this.charValue = (char)((i >= 0) ? i : Character.MIN_VALUE);
/*      */         break;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       case 39:
/*  886 */         this.charValue = Character.MIN_VALUE;
/*  887 */         this.env.error(this.pos, "invalid.char.constant");
/*  888 */         this.ch = this.in.read();
/*  889 */         while (this.ch == 39) {
/*  890 */           this.ch = this.in.read();
/*      */         }
/*      */         return;
/*      */       
/*      */       case 10:
/*      */       case 13:
/*  896 */         this.charValue = Character.MIN_VALUE;
/*  897 */         this.env.error(this.pos, "invalid.char.constant");
/*      */         return;
/*      */       
/*      */       default:
/*  901 */         this.charValue = (char)this.ch;
/*  902 */         this.ch = this.in.read();
/*      */         break;
/*      */     } 
/*      */     
/*  906 */     if (this.ch == 39) {
/*  907 */       this.ch = this.in.read();
/*      */     } else {
/*  909 */       this.env.error(this.pos, "invalid.char.constant");
/*      */       while (true) {
/*  911 */         switch (this.ch) {
/*      */           case 39:
/*  913 */             this.ch = this.in.read();
/*      */             return;
/*      */           case -1:
/*      */           case 10:
/*      */           case 59:
/*      */             return;
/*      */         } 
/*  920 */         this.ch = this.in.read();
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void scanIdentifier() throws IOException {
/*  931 */     this.count = 0;
/*      */     
/*      */     while (true) {
/*  934 */       putc(this.ch);
/*  935 */       switch (this.ch = this.in.read()) { case 36: case 48: case 49: case 50: case 51: case 52: case 53: case 54: case 55: case 56: case 57: case 65: case 66: case 67: case 68: case 69: case 70: case 71: case 72: case 73: case 74: case 75: case 76: case 77: case 78: case 79: case 80: case 81: case 82: case 83: case 84: case 85: case 86: case 87: case 88: case 89: case 90: case 95: case 97: case 98: case 99: case 100: case 101: case 102: case 103: case 104:
/*      */         case 105:
/*      */         case 106:
/*      */         case 107:
/*      */         case 108:
/*      */         case 109:
/*      */         case 110:
/*      */         case 111:
/*      */         case 112:
/*      */         case 113:
/*      */         case 114:
/*      */         case 115:
/*      */         case 116:
/*      */         case 117:
/*      */         case 118:
/*      */         case 119:
/*      */         case 120:
/*      */         case 121:
/*      */         case 122:
/*  954 */           continue; }  if (!Character.isJavaLetterOrDigit((char)this.ch)) {
/*  955 */         this.idValue = Identifier.lookup(bufferString());
/*  956 */         this.token = this.idValue.getType();
/*      */         return;
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public long getEndPos() {
/*  968 */     return this.in.pos;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public IdentifierToken getIdToken() {
/*  976 */     return (this.token != 60) ? null : new IdentifierToken(this.pos, this.idValue);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public long scan() throws IOException {
/*  984 */     return xscan();
/*      */   }
/*      */   
/*      */   protected long xscan() throws IOException {
/*  988 */     ScannerInputReader scannerInputReader = this.in;
/*  989 */     long l = this.pos;
/*  990 */     this.prevPos = scannerInputReader.pos;
/*  991 */     this.docComment = null;
/*      */     while (true) {
/*  993 */       this.pos = scannerInputReader.pos;
/*      */       
/*  995 */       switch (this.ch) {
/*      */         case -1:
/*  997 */           this.token = -1;
/*  998 */           return l;
/*      */         
/*      */         case 10:
/* 1001 */           if (this.scanComments) {
/* 1002 */             this.ch = 32;
/*      */ 
/*      */ 
/*      */             
/* 1006 */             this.token = 146;
/* 1007 */             return l;
/*      */           } 
/*      */         case 9:
/*      */         case 12:
/*      */         case 32:
/* 1012 */           this.ch = scannerInputReader.read();
/*      */           continue;
/*      */         
/*      */         case 47:
/* 1016 */           switch (this.ch = scannerInputReader.read()) {
/*      */             
/*      */             case 47:
/* 1019 */               while ((this.ch = scannerInputReader.read()) != -1 && this.ch != 10);
/* 1020 */               if (this.scanComments) {
/* 1021 */                 this.token = 146;
/* 1022 */                 return l;
/*      */               } 
/*      */               continue;
/*      */             
/*      */             case 42:
/* 1027 */               this.ch = scannerInputReader.read();
/* 1028 */               if (this.ch == 42) {
/* 1029 */                 this.docComment = scanDocComment();
/*      */               } else {
/* 1031 */                 skipComment();
/*      */               } 
/* 1033 */               if (this.scanComments) {
/* 1034 */                 return l;
/*      */               }
/*      */               continue;
/*      */             
/*      */             case 61:
/* 1039 */               this.ch = scannerInputReader.read();
/* 1040 */               this.token = 3;
/* 1041 */               return l;
/*      */           } 
/*      */           
/* 1044 */           this.token = 31;
/* 1045 */           return l;
/*      */ 
/*      */ 
/*      */         
/*      */         case 34:
/* 1050 */           scanString();
/* 1051 */           return l;
/*      */         
/*      */         case 39:
/* 1054 */           scanCharacter();
/* 1055 */           return l;
/*      */         case 48: case 49: case 50: case 51: case 52: case 53: case 54: case 55:
/*      */         case 56:
/*      */         case 57:
/* 1059 */           scanNumber();
/* 1060 */           return l;
/*      */         
/*      */         case 46:
/* 1063 */           switch (this.ch = scannerInputReader.read()) { case 48: case 49: case 50: case 51: case 52: case 53: case 54: case 55:
/*      */             case 56:
/*      */             case 57:
/* 1066 */               this.count = 0;
/* 1067 */               putc(46);
/* 1068 */               scanReal();
/*      */ 
/*      */ 
/*      */ 
/*      */               
/* 1073 */               return l; }  this.token = 46; return l;
/*      */         
/*      */         case 123:
/* 1076 */           this.ch = scannerInputReader.read();
/* 1077 */           this.token = 138;
/* 1078 */           return l;
/*      */         
/*      */         case 125:
/* 1081 */           this.ch = scannerInputReader.read();
/* 1082 */           this.token = 139;
/* 1083 */           return l;
/*      */         
/*      */         case 40:
/* 1086 */           this.ch = scannerInputReader.read();
/* 1087 */           this.token = 140;
/* 1088 */           return l;
/*      */         
/*      */         case 41:
/* 1091 */           this.ch = scannerInputReader.read();
/* 1092 */           this.token = 141;
/* 1093 */           return l;
/*      */         
/*      */         case 91:
/* 1096 */           this.ch = scannerInputReader.read();
/* 1097 */           this.token = 142;
/* 1098 */           return l;
/*      */         
/*      */         case 93:
/* 1101 */           this.ch = scannerInputReader.read();
/* 1102 */           this.token = 143;
/* 1103 */           return l;
/*      */         
/*      */         case 44:
/* 1106 */           this.ch = scannerInputReader.read();
/* 1107 */           this.token = 0;
/* 1108 */           return l;
/*      */         
/*      */         case 59:
/* 1111 */           this.ch = scannerInputReader.read();
/* 1112 */           this.token = 135;
/* 1113 */           return l;
/*      */         
/*      */         case 63:
/* 1116 */           this.ch = scannerInputReader.read();
/* 1117 */           this.token = 137;
/* 1118 */           return l;
/*      */         
/*      */         case 126:
/* 1121 */           this.ch = scannerInputReader.read();
/* 1122 */           this.token = 38;
/* 1123 */           return l;
/*      */         
/*      */         case 58:
/* 1126 */           this.ch = scannerInputReader.read();
/* 1127 */           this.token = 136;
/* 1128 */           return l;
/*      */         
/*      */         case 45:
/* 1131 */           switch (this.ch = scannerInputReader.read()) {
/*      */             case 45:
/* 1133 */               this.ch = scannerInputReader.read();
/* 1134 */               this.token = 51;
/* 1135 */               return l;
/*      */             
/*      */             case 61:
/* 1138 */               this.ch = scannerInputReader.read();
/* 1139 */               this.token = 6;
/* 1140 */               return l;
/*      */           } 
/* 1142 */           this.token = 30;
/* 1143 */           return l;
/*      */         
/*      */         case 43:
/* 1146 */           switch (this.ch = scannerInputReader.read()) {
/*      */             case 43:
/* 1148 */               this.ch = scannerInputReader.read();
/* 1149 */               this.token = 50;
/* 1150 */               return l;
/*      */             
/*      */             case 61:
/* 1153 */               this.ch = scannerInputReader.read();
/* 1154 */               this.token = 5;
/* 1155 */               return l;
/*      */           } 
/* 1157 */           this.token = 29;
/* 1158 */           return l;
/*      */         
/*      */         case 60:
/* 1161 */           switch (this.ch = scannerInputReader.read()) {
/*      */             case 60:
/* 1163 */               if ((this.ch = scannerInputReader.read()) == 61) {
/* 1164 */                 this.ch = scannerInputReader.read();
/* 1165 */                 this.token = 7;
/* 1166 */                 return l;
/*      */               } 
/* 1168 */               this.token = 26;
/* 1169 */               return l;
/*      */             
/*      */             case 61:
/* 1172 */               this.ch = scannerInputReader.read();
/* 1173 */               this.token = 23;
/* 1174 */               return l;
/*      */           } 
/* 1176 */           this.token = 24;
/* 1177 */           return l;
/*      */         
/*      */         case 62:
/* 1180 */           switch (this.ch = scannerInputReader.read()) {
/*      */             case 62:
/* 1182 */               switch (this.ch = scannerInputReader.read()) {
/*      */                 case 61:
/* 1184 */                   this.ch = scannerInputReader.read();
/* 1185 */                   this.token = 8;
/* 1186 */                   return l;
/*      */                 
/*      */                 case 62:
/* 1189 */                   if ((this.ch = scannerInputReader.read()) == 61) {
/* 1190 */                     this.ch = scannerInputReader.read();
/* 1191 */                     this.token = 9;
/* 1192 */                     return l;
/*      */                   } 
/* 1194 */                   this.token = 28;
/* 1195 */                   return l;
/*      */               } 
/* 1197 */               this.token = 27;
/* 1198 */               return l;
/*      */             
/*      */             case 61:
/* 1201 */               this.ch = scannerInputReader.read();
/* 1202 */               this.token = 21;
/* 1203 */               return l;
/*      */           } 
/* 1205 */           this.token = 22;
/* 1206 */           return l;
/*      */         
/*      */         case 124:
/* 1209 */           switch (this.ch = scannerInputReader.read()) {
/*      */             case 124:
/* 1211 */               this.ch = scannerInputReader.read();
/* 1212 */               this.token = 14;
/* 1213 */               return l;
/*      */             
/*      */             case 61:
/* 1216 */               this.ch = scannerInputReader.read();
/* 1217 */               this.token = 11;
/* 1218 */               return l;
/*      */           } 
/* 1220 */           this.token = 16;
/* 1221 */           return l;
/*      */         
/*      */         case 38:
/* 1224 */           switch (this.ch = scannerInputReader.read()) {
/*      */             case 38:
/* 1226 */               this.ch = scannerInputReader.read();
/* 1227 */               this.token = 15;
/* 1228 */               return l;
/*      */             
/*      */             case 61:
/* 1231 */               this.ch = scannerInputReader.read();
/* 1232 */               this.token = 10;
/* 1233 */               return l;
/*      */           } 
/* 1235 */           this.token = 18;
/* 1236 */           return l;
/*      */         
/*      */         case 61:
/* 1239 */           if ((this.ch = scannerInputReader.read()) == 61) {
/* 1240 */             this.ch = scannerInputReader.read();
/* 1241 */             this.token = 20;
/* 1242 */             return l;
/*      */           } 
/* 1244 */           this.token = 1;
/* 1245 */           return l;
/*      */         
/*      */         case 37:
/* 1248 */           if ((this.ch = scannerInputReader.read()) == 61) {
/* 1249 */             this.ch = scannerInputReader.read();
/* 1250 */             this.token = 4;
/* 1251 */             return l;
/*      */           } 
/* 1253 */           this.token = 32;
/* 1254 */           return l;
/*      */         
/*      */         case 94:
/* 1257 */           if ((this.ch = scannerInputReader.read()) == 61) {
/* 1258 */             this.ch = scannerInputReader.read();
/* 1259 */             this.token = 12;
/* 1260 */             return l;
/*      */           } 
/* 1262 */           this.token = 17;
/* 1263 */           return l;
/*      */         
/*      */         case 33:
/* 1266 */           if ((this.ch = scannerInputReader.read()) == 61) {
/* 1267 */             this.ch = scannerInputReader.read();
/* 1268 */             this.token = 19;
/* 1269 */             return l;
/*      */           } 
/* 1271 */           this.token = 37;
/* 1272 */           return l;
/*      */         
/*      */         case 42:
/* 1275 */           if ((this.ch = scannerInputReader.read()) == 61) {
/* 1276 */             this.ch = scannerInputReader.read();
/* 1277 */             this.token = 2;
/* 1278 */             return l;
/*      */           } 
/* 1280 */           this.token = 33;
/* 1281 */           return l;
/*      */         case 36: case 65: case 66: case 67: case 68: case 69: case 70: case 71: case 72: case 73: case 74: case 75: case 76: case 77: case 78: case 79: case 80: case 81: case 82: case 83: case 84: case 85: case 86: case 87: case 88: case 89: case 90: case 95: case 97: case 98: case 99: case 100: case 101: case 102: case 103: case 104: case 105: case 106: case 107: case 108: case 109: case 110: case 111:
/*      */         case 112:
/*      */         case 113:
/*      */         case 114:
/*      */         case 115:
/*      */         case 116:
/*      */         case 117:
/*      */         case 118:
/*      */         case 119:
/*      */         case 120:
/*      */         case 121:
/*      */         case 122:
/* 1294 */           scanIdentifier();
/* 1295 */           return l;
/*      */ 
/*      */         
/*      */         case 26:
/* 1299 */           if ((this.ch = scannerInputReader.read()) == -1) {
/* 1300 */             this.token = -1;
/* 1301 */             return l;
/*      */           } 
/* 1303 */           this.env.error(this.pos, "funny.char");
/* 1304 */           this.ch = scannerInputReader.read();
/*      */           continue;
/*      */       } 
/*      */ 
/*      */       
/* 1309 */       if (Character.isJavaLetter((char)this.ch)) {
/* 1310 */         scanIdentifier();
/* 1311 */         return l;
/*      */       } 
/* 1313 */       this.env.error(this.pos, "funny.char");
/* 1314 */       this.ch = scannerInputReader.read();
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void match(int paramInt1, int paramInt2) throws IOException {
/* 1325 */     byte b = 1;
/*      */     
/*      */     while (true) {
/* 1328 */       scan();
/* 1329 */       if (this.token == paramInt1) {
/* 1330 */         b++; continue;
/* 1331 */       }  if (this.token == paramInt2) {
/* 1332 */         if (--b == 0)
/*      */           return;  continue;
/*      */       } 
/* 1335 */       if (this.token == -1) {
/* 1336 */         this.env.error(this.pos, "unbalanced.paren");
/*      */         return;
/*      */       } 
/*      */     } 
/*      */   }
/*      */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\sun\tools\java\Scanner.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */