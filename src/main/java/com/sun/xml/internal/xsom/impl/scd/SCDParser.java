/*     */ package com.sun.xml.internal.xsom.impl.scd;
/*     */ 
/*     */ import com.sun.xml.internal.xsom.XSDeclaration;
/*     */ import com.sun.xml.internal.xsom.impl.UName;
/*     */ import java.io.InputStream;
/*     */ import java.io.Reader;
/*     */ import java.io.StringReader;
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Vector;
/*     */ import javax.xml.namespace.NamespaceContext;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SCDParser
/*     */   implements SCDParserConstants
/*     */ {
/*     */   private NamespaceContext nsc;
/*     */   public SCDParserTokenManager token_source;
/*     */   SimpleCharStream jj_input_stream;
/*     */   public Token token;
/*     */   public Token jj_nt;
/*     */   private int jj_ntk;
/*     */   private int jj_gen;
/*     */   
/*     */   public SCDParser(String text, NamespaceContext nsc) {
/*  37 */     this(new StringReader(text));
/*  38 */     this.nsc = nsc;
/*     */   }
/*     */   private String trim(String s) {
/*  41 */     return s.substring(1, s.length() - 1);
/*     */   }
/*     */   private String resolvePrefix(String prefix) throws ParseException {
/*     */     try {
/*  45 */       String r = this.nsc.getNamespaceURI(prefix);
/*     */       
/*  47 */       if (prefix.equals(""))
/*  48 */         return r; 
/*  49 */       if (!r.equals(""))
/*  50 */         return r; 
/*  51 */     } catch (IllegalArgumentException illegalArgumentException) {}
/*     */ 
/*     */     
/*  54 */     throw new ParseException("Unbound prefix: " + prefix);
/*     */   }
/*     */ 
/*     */   
/*     */   public final UName QName() throws ParseException {
/*  59 */     Token l = null;
/*  60 */     Token p = jj_consume_token(12);
/*  61 */     switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
/*     */       case 15:
/*  63 */         jj_consume_token(15);
/*  64 */         l = jj_consume_token(12);
/*     */         break;
/*     */       default:
/*  67 */         this.jj_la1[0] = this.jj_gen;
/*     */         break;
/*     */     } 
/*  70 */     if (l == null) {
/*  71 */       return new UName(resolvePrefix(""), p.image);
/*     */     }
/*  73 */     return new UName(resolvePrefix(p.image), l.image);
/*     */   }
/*     */ 
/*     */   
/*     */   public final String Prefix() throws ParseException {
/*     */     Token p;
/*  79 */     switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
/*     */       case 12:
/*  81 */         p = jj_consume_token(12);
/*  82 */         return resolvePrefix(p.image);
/*     */     } 
/*     */     
/*  85 */     this.jj_la1[1] = this.jj_gen;
/*  86 */     return resolvePrefix("");
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final List RelativeSchemaComponentPath() throws ParseException {
/*  92 */     List<Step.Any> steps = new ArrayList();
/*     */     
/*  94 */     switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
/*     */       case 16:
/*     */       case 17:
/*  97 */         switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
/*     */           case 16:
/*  99 */             jj_consume_token(16);
/* 100 */             steps.add(new Step.Any((Axis)Axis.ROOT));
/*     */             break;
/*     */           case 17:
/* 103 */             jj_consume_token(17);
/* 104 */             steps.add(new Step.Any(Axis.DESCENDANTS));
/*     */             break;
/*     */         } 
/* 107 */         this.jj_la1[2] = this.jj_gen;
/* 108 */         jj_consume_token(-1);
/* 109 */         throw new ParseException();
/*     */ 
/*     */       
/*     */       default:
/* 113 */         this.jj_la1[3] = this.jj_gen;
/*     */         break;
/*     */     } 
/* 116 */     Step s = Step();
/* 117 */     steps.add(s);
/*     */     
/*     */     while (true) {
/* 120 */       switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
/*     */         case 16:
/*     */         case 17:
/*     */           break;
/*     */         
/*     */         default:
/* 126 */           this.jj_la1[4] = this.jj_gen;
/*     */           break;
/*     */       } 
/* 129 */       switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
/*     */         case 16:
/* 131 */           jj_consume_token(16);
/*     */           break;
/*     */         case 17:
/* 134 */           jj_consume_token(17);
/* 135 */           steps.add(new Step.Any(Axis.DESCENDANTS));
/*     */           break;
/*     */         default:
/* 138 */           this.jj_la1[5] = this.jj_gen;
/* 139 */           jj_consume_token(-1);
/* 140 */           throw new ParseException();
/*     */       } 
/* 142 */       s = Step();
/* 143 */       steps.add(s);
/*     */     } 
/* 145 */     return steps;
/*     */   }
/*     */   
/*     */   public final Step Step() throws ParseException { Step s;
/*     */     String p;
/*     */     Token n;
/* 151 */     switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
/*     */       case 18:
/*     */       case 19:
/* 154 */         switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
/*     */           case 18:
/* 156 */             jj_consume_token(18);
/*     */             break;
/*     */           case 19:
/* 159 */             jj_consume_token(19);
/*     */             break;
/*     */           default:
/* 162 */             this.jj_la1[6] = this.jj_gen;
/* 163 */             jj_consume_token(-1);
/* 164 */             throw new ParseException();
/*     */         } 
/* 166 */         s = NameOrWildcard(Axis.ATTRIBUTE);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 348 */         return s;case 12: case 20: case 45: switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) { case 20: jj_consume_token(20); break;default: this.jj_la1[7] = this.jj_gen; break; }  s = NameOrWildcard(Axis.ELEMENT); switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) { case 13: Predicate(s); return s; }  this.jj_la1[8] = this.jj_gen; return s;case 21: jj_consume_token(21); s = NameOrWildcard(Axis.SUBSTITUTION_GROUP); return s;case 22: case 23: switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) { case 22: jj_consume_token(22); break;case 23: jj_consume_token(23); break;default: this.jj_la1[9] = this.jj_gen; jj_consume_token(-1); throw new ParseException(); }  s = NameOrWildcardOrAnonymous(Axis.TYPE_DEFINITION); return s;case 24: jj_consume_token(24); s = NameOrWildcard(Axis.BASETYPE); return s;case 25: jj_consume_token(25); s = NameOrWildcard(Axis.PRIMITIVE_TYPE); return s;case 26: jj_consume_token(26); s = NameOrWildcardOrAnonymous(Axis.ITEM_TYPE); return s;case 27: jj_consume_token(27); s = NameOrWildcardOrAnonymous(Axis.MEMBER_TYPE); switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) { case 13: Predicate(s); return s; }  this.jj_la1[10] = this.jj_gen; return s;case 28: jj_consume_token(28); s = NameOrWildcardOrAnonymous(Axis.SCOPE); return s;case 29: jj_consume_token(29); s = NameOrWildcard(Axis.ATTRIBUTE_GROUP); return s;case 30: jj_consume_token(30); s = NameOrWildcard(Axis.MODEL_GROUP_DECL); return s;case 31: jj_consume_token(31); s = NameOrWildcard(Axis.IDENTITY_CONSTRAINT); return s;case 32: jj_consume_token(32); s = NameOrWildcard(Axis.REFERENCED_KEY); return s;case 33: jj_consume_token(33); s = NameOrWildcard(Axis.NOTATION); return s;case 34: jj_consume_token(34); s = new Step.Any((Axis)Axis.MODELGROUP_SEQUENCE); switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) { case 13: Predicate(s); return s; }  this.jj_la1[11] = this.jj_gen; return s;case 35: jj_consume_token(35); s = new Step.Any((Axis)Axis.MODELGROUP_CHOICE); switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) { case 13: Predicate(s); return s; }  this.jj_la1[12] = this.jj_gen; return s;case 36: jj_consume_token(36); s = new Step.Any((Axis)Axis.MODELGROUP_ALL); switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) { case 13: Predicate(s); return s; }  this.jj_la1[13] = this.jj_gen; return s;case 37: jj_consume_token(37); s = new Step.Any((Axis)Axis.MODELGROUP_ANY); switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) { case 13: Predicate(s); return s; }  this.jj_la1[14] = this.jj_gen; return s;case 38: jj_consume_token(38); s = new Step.Any((Axis)Axis.WILDCARD); switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) { case 13: Predicate(s); return s; }  this.jj_la1[15] = this.jj_gen; return s;case 39: jj_consume_token(39); s = new Step.Any((Axis)Axis.ATTRIBUTE_WILDCARD); return s;case 40: jj_consume_token(40); s = new Step.Any((Axis)Axis.FACET); return s;case 41: jj_consume_token(41); n = jj_consume_token(14); s = new Step.Facet(Axis.FACET, n.image); return s;case 42: jj_consume_token(42); s = new Step.Any(Axis.DESCENDANTS); return s;case 43: jj_consume_token(43); p = Prefix(); s = new Step.Schema(Axis.X_SCHEMA, p); return s;case 44: jj_consume_token(44); s = new Step.Any((Axis)Axis.X_SCHEMA); return s;
/*     */     } 
/*     */     this.jj_la1[16] = this.jj_gen;
/*     */     jj_consume_token(-1);
/*     */     throw new ParseException(); } public final Step NameOrWildcard(Axis<? extends XSDeclaration> a) throws ParseException {
/*     */     UName un;
/* 354 */     switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
/*     */       case 12:
/* 356 */         un = QName();
/* 357 */         return new Step.Named(a, un);
/*     */       
/*     */       case 45:
/* 360 */         jj_consume_token(45);
/* 361 */         return new Step.Any((Axis)a);
/*     */     } 
/*     */     
/* 364 */     this.jj_la1[17] = this.jj_gen;
/* 365 */     jj_consume_token(-1);
/* 366 */     throw new ParseException();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final Step NameOrWildcardOrAnonymous(Axis<? extends XSDeclaration> a) throws ParseException {
/*     */     UName un;
/* 373 */     switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
/*     */       case 12:
/* 375 */         un = QName();
/* 376 */         return new Step.Named(a, un);
/*     */       
/*     */       case 45:
/* 379 */         jj_consume_token(45);
/* 380 */         return new Step.Any((Axis)a);
/*     */       
/*     */       case 46:
/* 383 */         jj_consume_token(46);
/* 384 */         return new Step.AnonymousType((Axis)a);
/*     */     } 
/*     */     
/* 387 */     this.jj_la1[18] = this.jj_gen;
/* 388 */     jj_consume_token(-1);
/* 389 */     throw new ParseException();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final int Predicate(Step s) throws ParseException {
/* 396 */     Token t = jj_consume_token(13);
/* 397 */     return s.predicate = Integer.parseInt(trim(t.image));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 406 */   private final int[] jj_la1 = new int[19]; private static int[] jj_la1_0; private static int[] jj_la1_1; private Vector jj_expentries; private int[] jj_expentry;
/*     */   private int jj_kind;
/*     */   
/*     */   static {
/* 410 */     jj_la1_0();
/* 411 */     jj_la1_1();
/*     */   }
/*     */   private static void jj_la1_0() {
/* 414 */     jj_la1_0 = new int[] { 32768, 4096, 196608, 196608, 196608, 196608, 786432, 1048576, 8192, 12582912, 8192, 8192, 8192, 8192, 8192, 8192, -258048, 4096, 4096 };
/*     */   }
/*     */   private static void jj_la1_1() {
/* 417 */     jj_la1_1 = new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 16383, 8192, 24576 };
/*     */   }
/*     */   
/*     */   public SCDParser(InputStream stream) {
/* 421 */     this(stream, (String)null);
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
/*     */   public void ReInit(InputStream stream) {
/* 433 */     ReInit(stream, null);
/*     */   } public void ReInit(InputStream stream, String encoding) {
/*     */     
/* 436 */     try { this.jj_input_stream.ReInit(stream, encoding, 1, 1); } catch (UnsupportedEncodingException e) { throw new RuntimeException(e); }
/* 437 */      this.token_source.ReInit(this.jj_input_stream);
/* 438 */     this.token = new Token();
/* 439 */     this.jj_ntk = -1;
/* 440 */     this.jj_gen = 0;
/* 441 */     for (int i = 0; i < 19; ) { this.jj_la1[i] = -1; i++; }
/*     */   
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
/*     */   public void ReInit(Reader stream) {
/* 454 */     this.jj_input_stream.ReInit(stream, 1, 1);
/* 455 */     this.token_source.ReInit(this.jj_input_stream);
/* 456 */     this.token = new Token();
/* 457 */     this.jj_ntk = -1;
/* 458 */     this.jj_gen = 0;
/* 459 */     for (int i = 0; i < 19; ) { this.jj_la1[i] = -1; i++; }
/*     */   
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void ReInit(SCDParserTokenManager tm) {
/* 471 */     this.token_source = tm;
/* 472 */     this.token = new Token();
/* 473 */     this.jj_ntk = -1;
/* 474 */     this.jj_gen = 0;
/* 475 */     for (int i = 0; i < 19; ) { this.jj_la1[i] = -1; i++; }
/*     */   
/*     */   }
/*     */   private final Token jj_consume_token(int kind) throws ParseException {
/*     */     Token oldToken;
/* 480 */     if ((oldToken = this.token).next != null) { this.token = this.token.next; }
/* 481 */     else { this.token = this.token.next = this.token_source.getNextToken(); }
/* 482 */      this.jj_ntk = -1;
/* 483 */     if (this.token.kind == kind) {
/* 484 */       this.jj_gen++;
/* 485 */       return this.token;
/*     */     } 
/* 487 */     this.token = oldToken;
/* 488 */     this.jj_kind = kind;
/* 489 */     throw generateParseException();
/*     */   }
/*     */   
/*     */   public final Token getNextToken() {
/* 493 */     if (this.token.next != null) { this.token = this.token.next; }
/* 494 */     else { this.token = this.token.next = this.token_source.getNextToken(); }
/* 495 */      this.jj_ntk = -1;
/* 496 */     this.jj_gen++;
/* 497 */     return this.token;
/*     */   }
/*     */   
/*     */   public final Token getToken(int index) {
/* 501 */     Token t = this.token;
/* 502 */     for (int i = 0; i < index; i++) {
/* 503 */       if (t.next != null) { t = t.next; }
/* 504 */       else { t = t.next = this.token_source.getNextToken(); }
/*     */     
/* 506 */     }  return t;
/*     */   }
/*     */   
/*     */   private final int jj_ntk() {
/* 510 */     if ((this.jj_nt = this.token.next) == null) {
/* 511 */       return this.jj_ntk = (this.token.next = this.token_source.getNextToken()).kind;
/*     */     }
/* 513 */     return this.jj_ntk = this.jj_nt.kind;
/*     */   }
/*     */   
/* 516 */   public SCDParser(InputStream stream, String encoding) { this.jj_expentries = new Vector();
/*     */     
/* 518 */     this.jj_kind = -1; try { this.jj_input_stream = new SimpleCharStream(stream, encoding, 1, 1); } catch (UnsupportedEncodingException e) { throw new RuntimeException(e); }  this.token_source = new SCDParserTokenManager(this.jj_input_stream); this.token = new Token(); this.jj_ntk = -1; this.jj_gen = 0; for (int i = 0; i < 19; ) { this.jj_la1[i] = -1; i++; }  } public SCDParser(Reader stream) { this.jj_expentries = new Vector(); this.jj_kind = -1; this.jj_input_stream = new SimpleCharStream(stream, 1, 1); this.token_source = new SCDParserTokenManager(this.jj_input_stream); this.token = new Token(); this.jj_ntk = -1; this.jj_gen = 0; for (int i = 0; i < 19; ) { this.jj_la1[i] = -1; i++; }  } public SCDParser(SCDParserTokenManager tm) { this.jj_expentries = new Vector(); this.jj_kind = -1; this.token_source = tm; this.token = new Token(); this.jj_ntk = -1; this.jj_gen = 0; for (int i = 0; i < 19; ) {
/*     */       this.jj_la1[i] = -1;
/*     */       i++;
/* 521 */     }  } public ParseException generateParseException() { this.jj_expentries.removeAllElements();
/* 522 */     boolean[] la1tokens = new boolean[47]; int i;
/* 523 */     for (i = 0; i < 47; i++) {
/* 524 */       la1tokens[i] = false;
/*     */     }
/* 526 */     if (this.jj_kind >= 0) {
/* 527 */       la1tokens[this.jj_kind] = true;
/* 528 */       this.jj_kind = -1;
/*     */     } 
/* 530 */     for (i = 0; i < 19; i++) {
/* 531 */       if (this.jj_la1[i] == this.jj_gen) {
/* 532 */         for (int k = 0; k < 32; k++) {
/* 533 */           if ((jj_la1_0[i] & 1 << k) != 0) {
/* 534 */             la1tokens[k] = true;
/*     */           }
/* 536 */           if ((jj_la1_1[i] & 1 << k) != 0) {
/* 537 */             la1tokens[32 + k] = true;
/*     */           }
/*     */         } 
/*     */       }
/*     */     } 
/* 542 */     for (i = 0; i < 47; i++) {
/* 543 */       if (la1tokens[i]) {
/* 544 */         this.jj_expentry = new int[1];
/* 545 */         this.jj_expentry[0] = i;
/* 546 */         this.jj_expentries.addElement(this.jj_expentry);
/*     */       } 
/*     */     } 
/* 549 */     int[][] exptokseq = new int[this.jj_expentries.size()][];
/* 550 */     for (int j = 0; j < this.jj_expentries.size(); j++) {
/* 551 */       exptokseq[j] = this.jj_expentries.elementAt(j);
/*     */     }
/* 553 */     return new ParseException(this.token, exptokseq, tokenImage); }
/*     */ 
/*     */   
/*     */   public final void enable_tracing() {}
/*     */   
/*     */   public final void disable_tracing() {}
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\xml\internal\xsom\impl\scd\SCDParser.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */