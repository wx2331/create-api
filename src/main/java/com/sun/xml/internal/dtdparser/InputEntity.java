/*     */ package com.sun.xml.internal.dtdparser;
/*     */ 
/*     */ import java.io.CharConversionException;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.Reader;
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.net.URL;
/*     */ import java.util.Locale;
/*     */ import org.xml.sax.InputSource;
/*     */ import org.xml.sax.SAXException;
/*     */ import org.xml.sax.SAXParseException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class InputEntity
/*     */ {
/*     */   private int start;
/*     */   private int finish;
/*     */   private char[] buf;
/*  60 */   private int lineNumber = 1;
/*     */ 
/*     */   
/*     */   private boolean returnedFirstHalf = false;
/*     */ 
/*     */   
/*     */   private boolean maybeInCRLF = false;
/*     */ 
/*     */   
/*     */   private String name;
/*     */   
/*     */   private InputEntity next;
/*     */   
/*     */   private InputSource input;
/*     */   
/*     */   private Reader reader;
/*     */   
/*     */   private boolean isClosed;
/*     */   
/*     */   private DTDEventListener errHandler;
/*     */   
/*     */   private Locale locale;
/*     */   
/*     */   private StringBuffer rememberedText;
/*     */   
/*     */   private int startRemember;
/*     */   
/*     */   private boolean isPE;
/*     */   
/*     */   private static final int BUFSIZ = 8193;
/*     */   
/*  91 */   private static final char[] newline = new char[] { '\n' };
/*     */   
/*     */   public static InputEntity getInputEntity(DTDEventListener h, Locale l) {
/*  94 */     InputEntity retval = new InputEntity();
/*  95 */     retval.errHandler = h;
/*  96 */     retval.locale = l;
/*  97 */     return retval;
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
/*     */   public boolean isInternal() {
/* 111 */     return (this.reader == null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isDocument() {
/* 118 */     return (this.next == null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isParameterEntity() {
/* 126 */     return this.isPE;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getName() {
/* 133 */     return this.name;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void init(InputSource in, String name, InputEntity stack, boolean isPE) throws IOException, SAXException {
/* 143 */     this.input = in;
/* 144 */     this.isPE = isPE;
/* 145 */     this.reader = in.getCharacterStream();
/*     */     
/* 147 */     if (this.reader == null) {
/* 148 */       InputStream bytes = in.getByteStream();
/*     */       
/* 150 */       if (bytes == null) {
/* 151 */         this.reader = XmlReader.createReader((new URL(in.getSystemId()))
/* 152 */             .openStream());
/* 153 */       } else if (in.getEncoding() != null) {
/* 154 */         this.reader = XmlReader.createReader(in.getByteStream(), in
/* 155 */             .getEncoding());
/*     */       } else {
/* 157 */         this.reader = XmlReader.createReader(in.getByteStream());
/*     */       } 
/* 159 */     }  this.next = stack;
/* 160 */     this.buf = new char[8193];
/* 161 */     this.name = name;
/* 162 */     checkRecursion(stack);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void init(char[] b, String name, InputEntity stack, boolean isPE) throws SAXException {
/* 171 */     this.next = stack;
/* 172 */     this.buf = b;
/* 173 */     this.finish = b.length;
/* 174 */     this.name = name;
/* 175 */     this.isPE = isPE;
/* 176 */     checkRecursion(stack);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void checkRecursion(InputEntity stack) throws SAXException {
/* 182 */     if (stack == null)
/*     */       return; 
/* 184 */     for (stack = stack.next; stack != null; stack = stack.next) {
/* 185 */       if (stack.name != null && stack.name.equals(this.name)) {
/* 186 */         fatal("P-069", new Object[] { this.name });
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public InputEntity pop() throws IOException {
/* 193 */     close();
/* 194 */     return this.next;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isEOF() throws IOException, SAXException {
/* 204 */     if (this.start >= this.finish) {
/* 205 */       fillbuf();
/* 206 */       return (this.start >= this.finish);
/*     */     } 
/* 208 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getEncoding() {
/* 217 */     if (this.reader == null)
/* 218 */       return null; 
/* 219 */     if (this.reader instanceof XmlReader) {
/* 220 */       return ((XmlReader)this.reader).getEncoding();
/*     */     }
/*     */ 
/*     */     
/* 224 */     if (this.reader instanceof InputStreamReader)
/* 225 */       return ((InputStreamReader)this.reader).getEncoding(); 
/* 226 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public char getNameChar() throws IOException, SAXException {
/* 237 */     if (this.finish <= this.start)
/* 238 */       fillbuf(); 
/* 239 */     if (this.finish > this.start) {
/* 240 */       char c = this.buf[this.start++];
/* 241 */       if (XmlChars.isNameChar(c))
/* 242 */         return c; 
/* 243 */       this.start--;
/*     */     } 
/* 245 */     return Character.MIN_VALUE;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public char getc() throws IOException, SAXException {
/* 255 */     if (this.finish <= this.start)
/* 256 */       fillbuf(); 
/* 257 */     if (this.finish > this.start) {
/* 258 */       char c = this.buf[this.start++];
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 264 */       if (this.returnedFirstHalf) {
/* 265 */         if (c >= '?' && c <= '?') {
/* 266 */           this.returnedFirstHalf = false;
/* 267 */           return c;
/*     */         } 
/* 269 */         fatal("P-070", new Object[] { Integer.toHexString(c) });
/*     */       } 
/* 271 */       if ((c >= ' ' && c <= '퟿') || c == '\t' || (c >= '' && c <= '�'))
/*     */       {
/*     */ 
/*     */         
/* 275 */         return c;
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 281 */       if (c == '\r' && !isInternal()) {
/* 282 */         this.maybeInCRLF = true;
/* 283 */         c = getc();
/* 284 */         if (c != '\n')
/* 285 */           ungetc(); 
/* 286 */         this.maybeInCRLF = false;
/*     */         
/* 288 */         this.lineNumber++;
/* 289 */         return '\n';
/*     */       } 
/* 291 */       if (c == '\n' || c == '\r') {
/* 292 */         if (!isInternal() && !this.maybeInCRLF)
/* 293 */           this.lineNumber++; 
/* 294 */         return c;
/*     */       } 
/*     */ 
/*     */       
/* 298 */       if (c >= '?' && c < '?') {
/* 299 */         this.returnedFirstHalf = true;
/* 300 */         return c;
/*     */       } 
/*     */       
/* 303 */       fatal("P-071", new Object[] { Integer.toHexString(c) });
/*     */     } 
/* 305 */     throw new EndOfInputException();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean peekc(char c) throws IOException, SAXException {
/* 314 */     if (this.finish <= this.start)
/* 315 */       fillbuf(); 
/* 316 */     if (this.finish > this.start) {
/* 317 */       if (this.buf[this.start] == c) {
/* 318 */         this.start++;
/* 319 */         return true;
/*     */       } 
/* 321 */       return false;
/*     */     } 
/* 323 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void ungetc() {
/* 332 */     if (this.start == 0)
/* 333 */       throw new InternalError("ungetc"); 
/* 334 */     this.start--;
/*     */     
/* 336 */     if (this.buf[this.start] == '\n' || this.buf[this.start] == '\r') {
/* 337 */       if (!isInternal())
/* 338 */         this.lineNumber--; 
/* 339 */     } else if (this.returnedFirstHalf) {
/* 340 */       this.returnedFirstHalf = false;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean maybeWhitespace() throws IOException, SAXException {
/* 351 */     boolean isSpace = false;
/* 352 */     boolean sawCR = false;
/*     */ 
/*     */     
/*     */     while (true) {
/* 356 */       if (this.finish <= this.start)
/* 357 */         fillbuf(); 
/* 358 */       if (this.finish <= this.start) {
/* 359 */         return isSpace;
/*     */       }
/* 361 */       char c = this.buf[this.start++];
/* 362 */       if (c == ' ' || c == '\t' || c == '\n' || c == '\r') {
/* 363 */         isSpace = true;
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 368 */         if ((c == '\n' || c == '\r') && !isInternal()) {
/* 369 */           if (c != '\n' || !sawCR) {
/* 370 */             this.lineNumber++;
/* 371 */             sawCR = false;
/*     */           } 
/* 373 */           if (c == '\r')
/* 374 */             sawCR = true; 
/*     */         }  continue;
/*     */       }  break;
/* 377 */     }  this.start--;
/* 378 */     return isSpace;
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
/*     */   public boolean parsedContent(DTDEventListener docHandler) throws IOException, SAXException {
/*     */     int first;
/*     */     int last;
/*     */     boolean sawContent;
/* 407 */     for (first = last = this.start, sawContent = false;; last++) {
/*     */ 
/*     */       
/* 410 */       if (last >= this.finish) {
/* 411 */         if (last > first) {
/*     */           
/* 413 */           docHandler.characters(this.buf, first, last - first);
/* 414 */           sawContent = true;
/* 415 */           this.start = last;
/*     */         } 
/* 417 */         if (isEOF())
/* 418 */           return sawContent; 
/* 419 */         first = this.start;
/* 420 */         last = first - 1;
/*     */       }
/*     */       else {
/*     */         
/* 424 */         char c = this.buf[last];
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 435 */         if ((c <= ']' || c > '퟿') && (c >= '&' || c < ' ') && (c <= '<' || c >= ']') && (c <= '&' || c >= '<') && c != '\t' && (c < '' || c > '�'))
/*     */         
/*     */         { 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 445 */           if (c == '<' || c == '&') {
/*     */             break;
/*     */           }
/*     */           
/* 449 */           if (c == '\n')
/* 450 */           { if (!isInternal()) {
/* 451 */               this.lineNumber++;
/*     */ 
/*     */             
/*     */             }
/*     */             
/*     */              }
/*     */           
/* 458 */           else if (c == '\r')
/* 459 */           { if (!isInternal())
/*     */             {
/*     */               
/* 462 */               docHandler.characters(this.buf, first, last - first);
/* 463 */               docHandler.characters(newline, 0, 1);
/* 464 */               sawContent = true;
/* 465 */               this.lineNumber++;
/* 466 */               if (this.finish > last + 1 && 
/* 467 */                 this.buf[last + 1] == '\n') {
/* 468 */                 last++;
/*     */               }
/*     */ 
/*     */               
/* 472 */               first = this.start = last + 1;
/*     */             
/*     */             }
/*     */              }
/*     */           
/* 477 */           else if (c == ']')
/* 478 */           { switch (this.finish - last) {
/*     */ 
/*     */               
/*     */               case 2:
/* 482 */                 if (this.buf[last + 1] != ']') {
/*     */                   break;
/*     */                 }
/*     */               
/*     */               case 1:
/* 487 */                 if (this.reader == null || this.isClosed)
/*     */                   break; 
/* 489 */                 if (last == first)
/* 490 */                   throw new InternalError("fillbuf"); 
/* 491 */                 last--;
/* 492 */                 if (last > first) {
/*     */                   
/* 494 */                   docHandler.characters(this.buf, first, last - first);
/* 495 */                   sawContent = true;
/* 496 */                   this.start = last;
/*     */                 } 
/* 498 */                 fillbuf();
/* 499 */                 first = last = this.start;
/*     */                 break;
/*     */ 
/*     */ 
/*     */               
/*     */               default:
/* 505 */                 if (this.buf[last + 1] == ']' && this.buf[last + 2] == '>') {
/* 506 */                   fatal("P-072", null);
/*     */                 }
/*     */                 break;
/*     */             } 
/*     */             
/*     */              }
/* 512 */           else if (c >= '?' && c <= '?')
/* 513 */           { if (last + 1 >= this.finish) {
/* 514 */               if (last > first) {
/*     */                 
/* 516 */                 docHandler.characters(this.buf, first, last - first);
/* 517 */                 sawContent = true;
/* 518 */                 this.start = last + 1;
/*     */               } 
/* 520 */               if (isEOF())
/* 521 */                 fatal("P-081", new Object[] {
/* 522 */                       Integer.toHexString(c)
/*     */                     }); 
/* 524 */               first = this.start;
/* 525 */               last = first;
/*     */             
/*     */             }
/* 528 */             else if (checkSurrogatePair(last)) {
/* 529 */               last++;
/*     */             } else {
/* 531 */               last--;
/*     */ 
/*     */ 
/*     */               
/*     */               break;
/*     */             }  }
/*     */           else
/* 538 */           { fatal("P-071", new Object[] { Integer.toHexString(c) }); }  } 
/*     */       } 
/* 540 */     }  if (last == first) {
/* 541 */       return sawContent;
/*     */     }
/* 543 */     docHandler.characters(this.buf, first, last - first);
/* 544 */     this.start = last;
/* 545 */     return true;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean unparsedContent(DTDEventListener docHandler, boolean ignorableWhitespace, String whitespaceInvalidMessage) throws IOException, SAXException {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: ldc '![CDATA['
/*     */     //   3: aconst_null
/*     */     //   4: invokevirtual peek : (Ljava/lang/String;[C)Z
/*     */     //   7: ifne -> 12
/*     */     //   10: iconst_0
/*     */     //   11: ireturn
/*     */     //   12: aload_1
/*     */     //   13: invokeinterface startCDATA : ()V
/*     */     //   18: iconst_0
/*     */     //   19: istore #5
/*     */     //   21: iload_2
/*     */     //   22: istore #7
/*     */     //   24: aload_0
/*     */     //   25: getfield start : I
/*     */     //   28: istore #4
/*     */     //   30: iload #4
/*     */     //   32: aload_0
/*     */     //   33: getfield finish : I
/*     */     //   36: if_icmpge -> 395
/*     */     //   39: aload_0
/*     */     //   40: getfield buf : [C
/*     */     //   43: iload #4
/*     */     //   45: caload
/*     */     //   46: istore #6
/*     */     //   48: iload #6
/*     */     //   50: invokestatic isChar : (I)Z
/*     */     //   53: ifne -> 117
/*     */     //   56: iconst_0
/*     */     //   57: istore #7
/*     */     //   59: iload #6
/*     */     //   61: ldc 55296
/*     */     //   63: if_icmplt -> 94
/*     */     //   66: iload #6
/*     */     //   68: ldc 57343
/*     */     //   70: if_icmpgt -> 94
/*     */     //   73: aload_0
/*     */     //   74: iload #4
/*     */     //   76: invokespecial checkSurrogatePair : (I)Z
/*     */     //   79: ifeq -> 88
/*     */     //   82: iinc #4, 1
/*     */     //   85: goto -> 389
/*     */     //   88: iinc #4, -1
/*     */     //   91: goto -> 395
/*     */     //   94: aload_0
/*     */     //   95: ldc 'P-071'
/*     */     //   97: iconst_1
/*     */     //   98: anewarray java/lang/Object
/*     */     //   101: dup
/*     */     //   102: iconst_0
/*     */     //   103: aload_0
/*     */     //   104: getfield buf : [C
/*     */     //   107: iload #4
/*     */     //   109: caload
/*     */     //   110: invokestatic toHexString : (I)Ljava/lang/String;
/*     */     //   113: aastore
/*     */     //   114: invokespecial fatal : (Ljava/lang/String;[Ljava/lang/Object;)V
/*     */     //   117: iload #6
/*     */     //   119: bipush #10
/*     */     //   121: if_icmpne -> 144
/*     */     //   124: aload_0
/*     */     //   125: invokevirtual isInternal : ()Z
/*     */     //   128: ifne -> 389
/*     */     //   131: aload_0
/*     */     //   132: dup
/*     */     //   133: getfield lineNumber : I
/*     */     //   136: iconst_1
/*     */     //   137: iadd
/*     */     //   138: putfield lineNumber : I
/*     */     //   141: goto -> 389
/*     */     //   144: iload #6
/*     */     //   146: bipush #13
/*     */     //   148: if_icmpne -> 314
/*     */     //   151: aload_0
/*     */     //   152: invokevirtual isInternal : ()Z
/*     */     //   155: ifeq -> 161
/*     */     //   158: goto -> 389
/*     */     //   161: iload #7
/*     */     //   163: ifeq -> 233
/*     */     //   166: aload_3
/*     */     //   167: ifnull -> 198
/*     */     //   170: aload_0
/*     */     //   171: getfield errHandler : Lcom/sun/xml/internal/dtdparser/DTDEventListener;
/*     */     //   174: new org/xml/sax/SAXParseException
/*     */     //   177: dup
/*     */     //   178: getstatic com/sun/xml/internal/dtdparser/DTDParser.messages : Lcom/sun/xml/internal/dtdparser/DTDParser$Catalog;
/*     */     //   181: aload_0
/*     */     //   182: getfield locale : Ljava/util/Locale;
/*     */     //   185: aload_3
/*     */     //   186: invokevirtual getMessage : (Ljava/util/Locale;Ljava/lang/String;)Ljava/lang/String;
/*     */     //   189: aconst_null
/*     */     //   190: invokespecial <init> : (Ljava/lang/String;Lorg/xml/sax/Locator;)V
/*     */     //   193: invokeinterface error : (Lorg/xml/sax/SAXParseException;)V
/*     */     //   198: aload_1
/*     */     //   199: aload_0
/*     */     //   200: getfield buf : [C
/*     */     //   203: aload_0
/*     */     //   204: getfield start : I
/*     */     //   207: iload #4
/*     */     //   209: aload_0
/*     */     //   210: getfield start : I
/*     */     //   213: isub
/*     */     //   214: invokeinterface ignorableWhitespace : ([CII)V
/*     */     //   219: aload_1
/*     */     //   220: getstatic com/sun/xml/internal/dtdparser/InputEntity.newline : [C
/*     */     //   223: iconst_0
/*     */     //   224: iconst_1
/*     */     //   225: invokeinterface ignorableWhitespace : ([CII)V
/*     */     //   230: goto -> 265
/*     */     //   233: aload_1
/*     */     //   234: aload_0
/*     */     //   235: getfield buf : [C
/*     */     //   238: aload_0
/*     */     //   239: getfield start : I
/*     */     //   242: iload #4
/*     */     //   244: aload_0
/*     */     //   245: getfield start : I
/*     */     //   248: isub
/*     */     //   249: invokeinterface characters : ([CII)V
/*     */     //   254: aload_1
/*     */     //   255: getstatic com/sun/xml/internal/dtdparser/InputEntity.newline : [C
/*     */     //   258: iconst_0
/*     */     //   259: iconst_1
/*     */     //   260: invokeinterface characters : ([CII)V
/*     */     //   265: aload_0
/*     */     //   266: dup
/*     */     //   267: getfield lineNumber : I
/*     */     //   270: iconst_1
/*     */     //   271: iadd
/*     */     //   272: putfield lineNumber : I
/*     */     //   275: aload_0
/*     */     //   276: getfield finish : I
/*     */     //   279: iload #4
/*     */     //   281: iconst_1
/*     */     //   282: iadd
/*     */     //   283: if_icmple -> 303
/*     */     //   286: aload_0
/*     */     //   287: getfield buf : [C
/*     */     //   290: iload #4
/*     */     //   292: iconst_1
/*     */     //   293: iadd
/*     */     //   294: caload
/*     */     //   295: bipush #10
/*     */     //   297: if_icmpne -> 303
/*     */     //   300: iinc #4, 1
/*     */     //   303: aload_0
/*     */     //   304: iload #4
/*     */     //   306: iconst_1
/*     */     //   307: iadd
/*     */     //   308: putfield start : I
/*     */     //   311: goto -> 389
/*     */     //   314: iload #6
/*     */     //   316: bipush #93
/*     */     //   318: if_icmpeq -> 341
/*     */     //   321: iload #6
/*     */     //   323: bipush #32
/*     */     //   325: if_icmpeq -> 389
/*     */     //   328: iload #6
/*     */     //   330: bipush #9
/*     */     //   332: if_icmpeq -> 389
/*     */     //   335: iconst_0
/*     */     //   336: istore #7
/*     */     //   338: goto -> 389
/*     */     //   341: iload #4
/*     */     //   343: iconst_2
/*     */     //   344: iadd
/*     */     //   345: aload_0
/*     */     //   346: getfield finish : I
/*     */     //   349: if_icmpge -> 395
/*     */     //   352: aload_0
/*     */     //   353: getfield buf : [C
/*     */     //   356: iload #4
/*     */     //   358: iconst_1
/*     */     //   359: iadd
/*     */     //   360: caload
/*     */     //   361: bipush #93
/*     */     //   363: if_icmpne -> 386
/*     */     //   366: aload_0
/*     */     //   367: getfield buf : [C
/*     */     //   370: iload #4
/*     */     //   372: iconst_2
/*     */     //   373: iadd
/*     */     //   374: caload
/*     */     //   375: bipush #62
/*     */     //   377: if_icmpne -> 386
/*     */     //   380: iconst_1
/*     */     //   381: istore #5
/*     */     //   383: goto -> 395
/*     */     //   386: iconst_0
/*     */     //   387: istore #7
/*     */     //   389: iinc #4, 1
/*     */     //   392: goto -> 30
/*     */     //   395: iload #7
/*     */     //   397: ifeq -> 456
/*     */     //   400: aload_3
/*     */     //   401: ifnull -> 432
/*     */     //   404: aload_0
/*     */     //   405: getfield errHandler : Lcom/sun/xml/internal/dtdparser/DTDEventListener;
/*     */     //   408: new org/xml/sax/SAXParseException
/*     */     //   411: dup
/*     */     //   412: getstatic com/sun/xml/internal/dtdparser/DTDParser.messages : Lcom/sun/xml/internal/dtdparser/DTDParser$Catalog;
/*     */     //   415: aload_0
/*     */     //   416: getfield locale : Ljava/util/Locale;
/*     */     //   419: aload_3
/*     */     //   420: invokevirtual getMessage : (Ljava/util/Locale;Ljava/lang/String;)Ljava/lang/String;
/*     */     //   423: aconst_null
/*     */     //   424: invokespecial <init> : (Ljava/lang/String;Lorg/xml/sax/Locator;)V
/*     */     //   427: invokeinterface error : (Lorg/xml/sax/SAXParseException;)V
/*     */     //   432: aload_1
/*     */     //   433: aload_0
/*     */     //   434: getfield buf : [C
/*     */     //   437: aload_0
/*     */     //   438: getfield start : I
/*     */     //   441: iload #4
/*     */     //   443: aload_0
/*     */     //   444: getfield start : I
/*     */     //   447: isub
/*     */     //   448: invokeinterface ignorableWhitespace : ([CII)V
/*     */     //   453: goto -> 477
/*     */     //   456: aload_1
/*     */     //   457: aload_0
/*     */     //   458: getfield buf : [C
/*     */     //   461: aload_0
/*     */     //   462: getfield start : I
/*     */     //   465: iload #4
/*     */     //   467: aload_0
/*     */     //   468: getfield start : I
/*     */     //   471: isub
/*     */     //   472: invokeinterface characters : ([CII)V
/*     */     //   477: iload #5
/*     */     //   479: ifeq -> 493
/*     */     //   482: aload_0
/*     */     //   483: iload #4
/*     */     //   485: iconst_3
/*     */     //   486: iadd
/*     */     //   487: putfield start : I
/*     */     //   490: goto -> 516
/*     */     //   493: aload_0
/*     */     //   494: iload #4
/*     */     //   496: putfield start : I
/*     */     //   499: aload_0
/*     */     //   500: invokevirtual isEOF : ()Z
/*     */     //   503: ifeq -> 513
/*     */     //   506: aload_0
/*     */     //   507: ldc 'P-073'
/*     */     //   509: aconst_null
/*     */     //   510: invokespecial fatal : (Ljava/lang/String;[Ljava/lang/Object;)V
/*     */     //   513: goto -> 18
/*     */     //   516: aload_1
/*     */     //   517: invokeinterface endCDATA : ()V
/*     */     //   522: iconst_1
/*     */     //   523: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #577	-> 0
/*     */     //   #578	-> 10
/*     */     //   #579	-> 12
/*     */     //   #585	-> 18
/*     */     //   #590	-> 21
/*     */     //   #592	-> 24
/*     */     //   #593	-> 39
/*     */     //   #598	-> 48
/*     */     //   #599	-> 56
/*     */     //   #600	-> 59
/*     */     //   #601	-> 73
/*     */     //   #602	-> 82
/*     */     //   #603	-> 85
/*     */     //   #605	-> 88
/*     */     //   #606	-> 91
/*     */     //   #609	-> 94
/*     */     //   #610	-> 110
/*     */     //   #609	-> 114
/*     */     //   #612	-> 117
/*     */     //   #613	-> 124
/*     */     //   #614	-> 131
/*     */     //   #617	-> 144
/*     */     //   #619	-> 151
/*     */     //   #620	-> 158
/*     */     //   #622	-> 161
/*     */     //   #623	-> 166
/*     */     //   #624	-> 170
/*     */     //   #626	-> 198
/*     */     //   #628	-> 219
/*     */     //   #631	-> 233
/*     */     //   #632	-> 254
/*     */     //   #634	-> 265
/*     */     //   #635	-> 275
/*     */     //   #636	-> 286
/*     */     //   #637	-> 300
/*     */     //   #641	-> 303
/*     */     //   #642	-> 311
/*     */     //   #644	-> 314
/*     */     //   #645	-> 321
/*     */     //   #646	-> 335
/*     */     //   #649	-> 341
/*     */     //   #650	-> 352
/*     */     //   #651	-> 380
/*     */     //   #652	-> 383
/*     */     //   #654	-> 386
/*     */     //   #592	-> 389
/*     */     //   #661	-> 395
/*     */     //   #662	-> 400
/*     */     //   #663	-> 404
/*     */     //   #665	-> 432
/*     */     //   #668	-> 456
/*     */     //   #670	-> 477
/*     */     //   #671	-> 482
/*     */     //   #672	-> 490
/*     */     //   #674	-> 493
/*     */     //   #675	-> 499
/*     */     //   #676	-> 506
/*     */     //   #677	-> 513
/*     */     //   #678	-> 516
/*     */     //   #679	-> 522
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   48	347	6	c	C
/*     */     //   21	492	5	done	Z
/*     */     //   24	489	7	white	Z
/*     */     //   0	524	0	this	Lcom/sun/xml/internal/dtdparser/InputEntity;
/*     */     //   0	524	1	docHandler	Lcom/sun/xml/internal/dtdparser/DTDEventListener;
/*     */     //   0	524	2	ignorableWhitespace	Z
/*     */     //   0	524	3	whitespaceInvalidMessage	Ljava/lang/String;
/*     */     //   30	494	4	last	I
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean checkSurrogatePair(int offset) throws SAXException {
/* 686 */     if (offset + 1 >= this.finish) {
/* 687 */       return false;
/*     */     }
/* 689 */     char c1 = this.buf[offset++];
/* 690 */     char c2 = this.buf[offset];
/*     */     
/* 692 */     if (c1 >= '?' && c1 < '?' && c2 >= '?' && c2 <= '?')
/* 693 */       return true; 
/* 694 */     fatal("P-074", new Object[] {
/* 695 */           Integer.toHexString(c1 & Character.MAX_VALUE), 
/* 696 */           Integer.toHexString(c2 & Character.MAX_VALUE)
/*     */         });
/* 698 */     return false;
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
/*     */   public boolean ignorableWhitespace(DTDEventListener handler) throws IOException, SAXException {
/* 712 */     boolean isSpace = false;
/*     */ 
/*     */ 
/*     */     
/* 716 */     int first = this.start; while (true) {
/* 717 */       if (this.finish <= this.start) {
/* 718 */         if (isSpace)
/* 719 */           handler.ignorableWhitespace(this.buf, first, this.start - first); 
/* 720 */         fillbuf();
/* 721 */         first = this.start;
/*     */       } 
/* 723 */       if (this.finish <= this.start) {
/* 724 */         return isSpace;
/*     */       }
/* 726 */       char c = this.buf[this.start++];
/* 727 */       switch (c) {
/*     */         case '\n':
/* 729 */           if (!isInternal()) {
/* 730 */             this.lineNumber++;
/*     */           }
/*     */         
/*     */         case '\t':
/*     */         case ' ':
/* 735 */           isSpace = true;
/*     */           continue;
/*     */         
/*     */         case '\r':
/* 739 */           isSpace = true;
/* 740 */           if (!isInternal())
/* 741 */             this.lineNumber++; 
/* 742 */           handler.ignorableWhitespace(this.buf, first, this.start - 1 - first);
/*     */           
/* 744 */           handler.ignorableWhitespace(newline, 0, 1);
/* 745 */           if (this.start < this.finish && this.buf[this.start] == '\n')
/* 746 */             this.start++; 
/* 747 */           first = this.start; continue;
/*     */       } 
/*     */       break;
/*     */     } 
/* 751 */     ungetc();
/* 752 */     if (isSpace)
/* 753 */       handler.ignorableWhitespace(this.buf, first, this.start - first); 
/* 754 */     return isSpace;
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
/*     */   public boolean peek(String next, char[] chars) throws IOException, SAXException {
/*     */     int len, i;
/* 772 */     if (chars != null) {
/* 773 */       len = chars.length;
/*     */     } else {
/* 775 */       len = next.length();
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 780 */     if (this.finish <= this.start || this.finish - this.start < len) {
/* 781 */       fillbuf();
/*     */     }
/*     */     
/* 784 */     if (this.finish <= this.start) {
/* 785 */       return false;
/*     */     }
/*     */     
/* 788 */     if (chars != null) {
/* 789 */       for (i = 0; i < len && this.start + i < this.finish; i++) {
/* 790 */         if (this.buf[this.start + i] != chars[i])
/* 791 */           return false; 
/*     */       } 
/*     */     } else {
/* 794 */       for (i = 0; i < len && this.start + i < this.finish; i++) {
/* 795 */         if (this.buf[this.start + i] != next.charAt(i)) {
/* 796 */           return false;
/*     */         }
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 802 */     if (i < len) {
/* 803 */       if (this.reader == null || this.isClosed) {
/* 804 */         return false;
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 813 */       if (len > this.buf.length) {
/* 814 */         fatal("P-077", new Object[] { new Integer(this.buf.length) });
/*     */       }
/* 816 */       fillbuf();
/* 817 */       return peek(next, chars);
/*     */     } 
/*     */     
/* 820 */     this.start += len;
/* 821 */     return true;
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
/*     */   public void startRemembering() {
/* 833 */     if (this.startRemember != 0)
/* 834 */       throw new InternalError(); 
/* 835 */     this.startRemember = this.start;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String rememberText() {
/*     */     String retval;
/* 844 */     if (this.rememberedText != null) {
/* 845 */       this.rememberedText.append(this.buf, this.startRemember, this.start - this.startRemember);
/*     */       
/* 847 */       retval = this.rememberedText.toString();
/*     */     } else {
/* 849 */       retval = new String(this.buf, this.startRemember, this.start - this.startRemember);
/*     */     } 
/*     */     
/* 852 */     this.startRemember = 0;
/* 853 */     this.rememberedText = null;
/* 854 */     return retval;
/*     */   }
/*     */ 
/*     */   
/*     */   private InputEntity getTopEntity() {
/* 859 */     InputEntity current = this;
/*     */ 
/*     */ 
/*     */     
/* 863 */     while (current != null && current.input == null)
/* 864 */       current = current.next; 
/* 865 */     return (current == null) ? this : current;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getPublicId() {
/* 873 */     InputEntity where = getTopEntity();
/* 874 */     if (where == this)
/* 875 */       return this.input.getPublicId(); 
/* 876 */     return where.getPublicId();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getSystemId() {
/* 884 */     InputEntity where = getTopEntity();
/* 885 */     if (where == this)
/* 886 */       return this.input.getSystemId(); 
/* 887 */     return where.getSystemId();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getLineNumber() {
/* 895 */     InputEntity where = getTopEntity();
/* 896 */     if (where == this)
/* 897 */       return this.lineNumber; 
/* 898 */     return where.getLineNumber();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getColumnNumber() {
/* 906 */     return -1;
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
/*     */   private void fillbuf() throws IOException, SAXException {
/* 926 */     if (this.reader == null || this.isClosed) {
/*     */       return;
/*     */     }
/*     */     
/* 930 */     if (this.startRemember != 0) {
/* 931 */       if (this.rememberedText == null)
/* 932 */         this.rememberedText = new StringBuffer(this.buf.length); 
/* 933 */       this.rememberedText.append(this.buf, this.startRemember, this.start - this.startRemember);
/*     */     } 
/*     */ 
/*     */     
/* 937 */     boolean extra = (this.finish > 0 && this.start > 0);
/*     */ 
/*     */     
/* 940 */     if (extra)
/* 941 */       this.start--; 
/* 942 */     int len = this.finish - this.start;
/*     */     
/* 944 */     System.arraycopy(this.buf, this.start, this.buf, 0, len);
/* 945 */     this.start = 0;
/* 946 */     this.finish = len;
/*     */     
/*     */     try {
/* 949 */       len = this.buf.length - len;
/* 950 */       len = this.reader.read(this.buf, this.finish, len);
/* 951 */     } catch (UnsupportedEncodingException e) {
/* 952 */       fatal("P-075", new Object[] { e.getMessage() });
/* 953 */     } catch (CharConversionException e) {
/* 954 */       fatal("P-076", new Object[] { e.getMessage() });
/*     */     } 
/* 956 */     if (len >= 0) {
/* 957 */       this.finish += len;
/*     */     } else {
/* 959 */       close();
/* 960 */     }  if (extra) {
/* 961 */       this.start++;
/*     */     }
/* 963 */     if (this.startRemember != 0)
/*     */     {
/* 965 */       this.startRemember = 1;
/*     */     }
/*     */   }
/*     */   
/*     */   public void close() {
/*     */     try {
/* 971 */       if (this.reader != null && !this.isClosed)
/* 972 */         this.reader.close(); 
/* 973 */       this.isClosed = true;
/* 974 */     } catch (IOException iOException) {}
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void fatal(String messageId, Object[] params) throws SAXException {
/* 983 */     SAXParseException x = new SAXParseException(DTDParser.messages.getMessage(this.locale, messageId, params), null);
/*     */ 
/*     */     
/* 986 */     close();
/* 987 */     this.errHandler.fatalError(x);
/* 988 */     throw x;
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\xml\internal\dtdparser\InputEntity.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */