/*     */ package sun.tools.java;
/*     */ 
/*     */ import java.io.FilterReader;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.UnsupportedEncodingException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ScannerInputReader
/*     */   extends FilterReader
/*     */   implements Constants
/*     */ {
/*     */   Environment env;
/*     */   long pos;
/*     */   private long chpos;
/*  64 */   private int pushBack = -1;
/*     */   
/*     */   private static final int BUFFERLEN = 10240;
/*     */   private final char[] buffer;
/*     */   private int currentIndex;
/*     */   private int numChars;
/*     */   
/*     */   public ScannerInputReader(Environment paramEnvironment, InputStream paramInputStream) throws UnsupportedEncodingException {
/*  72 */     super((paramEnvironment.getCharacterEncoding() != null) ? new InputStreamReader(paramInputStream, paramEnvironment
/*  73 */           .getCharacterEncoding()) : new InputStreamReader(paramInputStream));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  91 */     this.buffer = new char[10240];
/*     */     this.currentIndex = 0;
/*     */     this.numChars = 0;
/*     */     this.env = paramEnvironment;
/*     */     this.chpos = 4294967296L;
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
/*     */   private int getNextChar() throws IOException {
/* 108 */     if (this.currentIndex >= this.numChars) {
/* 109 */       this.numChars = this.in.read(this.buffer);
/* 110 */       if (this.numChars == -1)
/*     */       {
/* 112 */         return -1;
/*     */       }
/*     */ 
/*     */       
/* 116 */       this.currentIndex = 0;
/*     */     } 
/*     */     
/* 119 */     return this.buffer[this.currentIndex++];
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int read(char[] paramArrayOfchar, int paramInt1, int paramInt2) {
/* 125 */     throw new CompilerError("ScannerInputReader is not a fully implemented reader.");
/*     */   }
/*     */   
/*     */   public int read() throws IOException {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_0
/*     */     //   2: getfield chpos : J
/*     */     //   5: putfield pos : J
/*     */     //   8: aload_0
/*     */     //   9: dup
/*     */     //   10: getfield chpos : J
/*     */     //   13: lconst_1
/*     */     //   14: ladd
/*     */     //   15: putfield chpos : J
/*     */     //   18: aload_0
/*     */     //   19: getfield pushBack : I
/*     */     //   22: istore_1
/*     */     //   23: iload_1
/*     */     //   24: iconst_m1
/*     */     //   25: if_icmpne -> 108
/*     */     //   28: aload_0
/*     */     //   29: getfield currentIndex : I
/*     */     //   32: aload_0
/*     */     //   33: getfield numChars : I
/*     */     //   36: if_icmplt -> 72
/*     */     //   39: aload_0
/*     */     //   40: aload_0
/*     */     //   41: getfield in : Ljava/io/Reader;
/*     */     //   44: aload_0
/*     */     //   45: getfield buffer : [C
/*     */     //   48: invokevirtual read : ([C)I
/*     */     //   51: putfield numChars : I
/*     */     //   54: aload_0
/*     */     //   55: getfield numChars : I
/*     */     //   58: iconst_m1
/*     */     //   59: if_icmpne -> 67
/*     */     //   62: iconst_m1
/*     */     //   63: istore_1
/*     */     //   64: goto -> 113
/*     */     //   67: aload_0
/*     */     //   68: iconst_0
/*     */     //   69: putfield currentIndex : I
/*     */     //   72: aload_0
/*     */     //   73: getfield buffer : [C
/*     */     //   76: aload_0
/*     */     //   77: dup
/*     */     //   78: getfield currentIndex : I
/*     */     //   81: dup_x1
/*     */     //   82: iconst_1
/*     */     //   83: iadd
/*     */     //   84: putfield currentIndex : I
/*     */     //   87: caload
/*     */     //   88: istore_1
/*     */     //   89: goto -> 113
/*     */     //   92: astore_2
/*     */     //   93: aload_0
/*     */     //   94: getfield env : Lsun/tools/java/Environment;
/*     */     //   97: aload_0
/*     */     //   98: getfield pos : J
/*     */     //   101: ldc 'invalid.encoding.char'
/*     */     //   103: invokevirtual error : (JLjava/lang/String;)V
/*     */     //   106: iconst_m1
/*     */     //   107: ireturn
/*     */     //   108: aload_0
/*     */     //   109: iconst_m1
/*     */     //   110: putfield pushBack : I
/*     */     //   113: iload_1
/*     */     //   114: lookupswitch default -> 704, -2 -> 156, 10 -> 645, 13 -> 660, 92 -> 159
/*     */     //   156: bipush #92
/*     */     //   158: ireturn
/*     */     //   159: aload_0
/*     */     //   160: invokespecial getNextChar : ()I
/*     */     //   163: dup
/*     */     //   164: istore_1
/*     */     //   165: bipush #117
/*     */     //   167: if_icmpeq -> 189
/*     */     //   170: aload_0
/*     */     //   171: iload_1
/*     */     //   172: bipush #92
/*     */     //   174: if_icmpne -> 182
/*     */     //   177: bipush #-2
/*     */     //   179: goto -> 183
/*     */     //   182: iload_1
/*     */     //   183: putfield pushBack : I
/*     */     //   186: bipush #92
/*     */     //   188: ireturn
/*     */     //   189: aload_0
/*     */     //   190: dup
/*     */     //   191: getfield chpos : J
/*     */     //   194: lconst_1
/*     */     //   195: ladd
/*     */     //   196: putfield chpos : J
/*     */     //   199: aload_0
/*     */     //   200: invokespecial getNextChar : ()I
/*     */     //   203: dup
/*     */     //   204: istore_1
/*     */     //   205: bipush #117
/*     */     //   207: if_icmpne -> 223
/*     */     //   210: aload_0
/*     */     //   211: dup
/*     */     //   212: getfield chpos : J
/*     */     //   215: lconst_1
/*     */     //   216: ladd
/*     */     //   217: putfield chpos : J
/*     */     //   220: goto -> 199
/*     */     //   223: iconst_0
/*     */     //   224: istore_2
/*     */     //   225: iconst_0
/*     */     //   226: istore_3
/*     */     //   227: iload_3
/*     */     //   228: iconst_4
/*     */     //   229: if_icmpge -> 551
/*     */     //   232: iload_1
/*     */     //   233: tableswitch default -> 510, 48 -> 468, 49 -> 468, 50 -> 468, 51 -> 468, 52 -> 468, 53 -> 468, 54 -> 468, 55 -> 468, 56 -> 468, 57 -> 468, 58 -> 510, 59 -> 510, 60 -> 510, 61 -> 510, 62 -> 510, 63 -> 510, 64 -> 510, 65 -> 495, 66 -> 495, 67 -> 495, 68 -> 495, 69 -> 495, 70 -> 495, 71 -> 510, 72 -> 510, 73 -> 510, 74 -> 510, 75 -> 510, 76 -> 510, 77 -> 510, 78 -> 510, 79 -> 510, 80 -> 510, 81 -> 510, 82 -> 510, 83 -> 510, 84 -> 510, 85 -> 510, 86 -> 510, 87 -> 510, 88 -> 510, 89 -> 510, 90 -> 510, 91 -> 510, 92 -> 510, 93 -> 510, 94 -> 510, 95 -> 510, 96 -> 510, 97 -> 480, 98 -> 480, 99 -> 480, 100 -> 480, 101 -> 480, 102 -> 480
/*     */     //   468: iload_2
/*     */     //   469: iconst_4
/*     */     //   470: ishl
/*     */     //   471: iload_1
/*     */     //   472: iadd
/*     */     //   473: bipush #48
/*     */     //   475: isub
/*     */     //   476: istore_2
/*     */     //   477: goto -> 530
/*     */     //   480: iload_2
/*     */     //   481: iconst_4
/*     */     //   482: ishl
/*     */     //   483: bipush #10
/*     */     //   485: iadd
/*     */     //   486: iload_1
/*     */     //   487: iadd
/*     */     //   488: bipush #97
/*     */     //   490: isub
/*     */     //   491: istore_2
/*     */     //   492: goto -> 530
/*     */     //   495: iload_2
/*     */     //   496: iconst_4
/*     */     //   497: ishl
/*     */     //   498: bipush #10
/*     */     //   500: iadd
/*     */     //   501: iload_1
/*     */     //   502: iadd
/*     */     //   503: bipush #65
/*     */     //   505: isub
/*     */     //   506: istore_2
/*     */     //   507: goto -> 530
/*     */     //   510: aload_0
/*     */     //   511: getfield env : Lsun/tools/java/Environment;
/*     */     //   514: aload_0
/*     */     //   515: getfield pos : J
/*     */     //   518: ldc 'invalid.escape.char'
/*     */     //   520: invokevirtual error : (JLjava/lang/String;)V
/*     */     //   523: aload_0
/*     */     //   524: iload_1
/*     */     //   525: putfield pushBack : I
/*     */     //   528: iload_2
/*     */     //   529: ireturn
/*     */     //   530: iinc #3, 1
/*     */     //   533: aload_0
/*     */     //   534: dup
/*     */     //   535: getfield chpos : J
/*     */     //   538: lconst_1
/*     */     //   539: ladd
/*     */     //   540: putfield chpos : J
/*     */     //   543: aload_0
/*     */     //   544: invokespecial getNextChar : ()I
/*     */     //   547: istore_1
/*     */     //   548: goto -> 227
/*     */     //   551: aload_0
/*     */     //   552: iload_1
/*     */     //   553: putfield pushBack : I
/*     */     //   556: iload_2
/*     */     //   557: lookupswitch default -> 643, 10 -> 584, 13 -> 599
/*     */     //   584: aload_0
/*     */     //   585: dup
/*     */     //   586: getfield chpos : J
/*     */     //   589: ldc2_w 4294967296
/*     */     //   592: ladd
/*     */     //   593: putfield chpos : J
/*     */     //   596: bipush #10
/*     */     //   598: ireturn
/*     */     //   599: aload_0
/*     */     //   600: invokespecial getNextChar : ()I
/*     */     //   603: dup
/*     */     //   604: istore_1
/*     */     //   605: bipush #10
/*     */     //   607: if_icmpeq -> 618
/*     */     //   610: aload_0
/*     */     //   611: iload_1
/*     */     //   612: putfield pushBack : I
/*     */     //   615: goto -> 628
/*     */     //   618: aload_0
/*     */     //   619: dup
/*     */     //   620: getfield chpos : J
/*     */     //   623: lconst_1
/*     */     //   624: ladd
/*     */     //   625: putfield chpos : J
/*     */     //   628: aload_0
/*     */     //   629: dup
/*     */     //   630: getfield chpos : J
/*     */     //   633: ldc2_w 4294967296
/*     */     //   636: ladd
/*     */     //   637: putfield chpos : J
/*     */     //   640: bipush #10
/*     */     //   642: ireturn
/*     */     //   643: iload_2
/*     */     //   644: ireturn
/*     */     //   645: aload_0
/*     */     //   646: dup
/*     */     //   647: getfield chpos : J
/*     */     //   650: ldc2_w 4294967296
/*     */     //   653: ladd
/*     */     //   654: putfield chpos : J
/*     */     //   657: bipush #10
/*     */     //   659: ireturn
/*     */     //   660: aload_0
/*     */     //   661: invokespecial getNextChar : ()I
/*     */     //   664: dup
/*     */     //   665: istore_1
/*     */     //   666: bipush #10
/*     */     //   668: if_icmpeq -> 679
/*     */     //   671: aload_0
/*     */     //   672: iload_1
/*     */     //   673: putfield pushBack : I
/*     */     //   676: goto -> 689
/*     */     //   679: aload_0
/*     */     //   680: dup
/*     */     //   681: getfield chpos : J
/*     */     //   684: lconst_1
/*     */     //   685: ladd
/*     */     //   686: putfield chpos : J
/*     */     //   689: aload_0
/*     */     //   690: dup
/*     */     //   691: getfield chpos : J
/*     */     //   694: ldc2_w 4294967296
/*     */     //   697: ladd
/*     */     //   698: putfield chpos : J
/*     */     //   701: bipush #10
/*     */     //   703: ireturn
/*     */     //   704: iload_1
/*     */     //   705: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #130	-> 0
/*     */     //   #131	-> 8
/*     */     //   #133	-> 18
/*     */     //   #134	-> 23
/*     */     //   #140	-> 28
/*     */     //   #141	-> 39
/*     */     //   #142	-> 54
/*     */     //   #144	-> 62
/*     */     //   #145	-> 64
/*     */     //   #149	-> 67
/*     */     //   #151	-> 72
/*     */     //   #157	-> 89
/*     */     //   #153	-> 92
/*     */     //   #154	-> 93
/*     */     //   #156	-> 106
/*     */     //   #159	-> 108
/*     */     //   #163	-> 113
/*     */     //   #167	-> 156
/*     */     //   #170	-> 159
/*     */     //   #171	-> 170
/*     */     //   #172	-> 186
/*     */     //   #175	-> 189
/*     */     //   #176	-> 199
/*     */     //   #177	-> 210
/*     */     //   #181	-> 223
/*     */     //   #182	-> 225
/*     */     //   #183	-> 232
/*     */     //   #186	-> 468
/*     */     //   #187	-> 477
/*     */     //   #190	-> 480
/*     */     //   #191	-> 492
/*     */     //   #194	-> 495
/*     */     //   #195	-> 507
/*     */     //   #198	-> 510
/*     */     //   #199	-> 523
/*     */     //   #200	-> 528
/*     */     //   #182	-> 530
/*     */     //   #203	-> 551
/*     */     //   #209	-> 556
/*     */     //   #211	-> 584
/*     */     //   #212	-> 596
/*     */     //   #214	-> 599
/*     */     //   #215	-> 610
/*     */     //   #217	-> 618
/*     */     //   #219	-> 628
/*     */     //   #220	-> 640
/*     */     //   #222	-> 643
/*     */     //   #226	-> 645
/*     */     //   #227	-> 657
/*     */     //   #230	-> 660
/*     */     //   #231	-> 671
/*     */     //   #233	-> 679
/*     */     //   #235	-> 689
/*     */     //   #236	-> 701
/*     */     //   #239	-> 704
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   28	64	92	java/io/CharConversionException
/*     */     //   67	89	92	java/io/CharConversionException
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\sun\tools\java\ScannerInputReader.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */