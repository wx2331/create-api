/*     */ package com.sun.xml.internal.dtdparser;
/*     */ 
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.CharConversionException;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.PushbackInputStream;
/*     */ import java.io.Reader;
/*     */ import java.util.Hashtable;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ final class XmlReader
/*     */   extends Reader
/*     */ {
/*     */   private static final int MAXPUSHBACK = 512;
/*     */   private Reader in;
/*     */   private String assignedEncoding;
/*     */   private boolean closed;
/*     */   
/*     */   public static Reader createReader(InputStream in) throws IOException {
/*  97 */     return new XmlReader(in);
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
/*     */   public static Reader createReader(InputStream in, String encoding) throws IOException {
/* 112 */     if (encoding == null)
/* 113 */       return new XmlReader(in); 
/* 114 */     if ("UTF-8".equalsIgnoreCase(encoding) || "UTF8"
/* 115 */       .equalsIgnoreCase(encoding))
/* 116 */       return new Utf8Reader(in); 
/* 117 */     if ("US-ASCII".equalsIgnoreCase(encoding) || "ASCII"
/* 118 */       .equalsIgnoreCase(encoding))
/* 119 */       return new AsciiReader(in); 
/* 120 */     if ("ISO-8859-1".equalsIgnoreCase(encoding))
/*     */     {
/*     */       
/* 123 */       return new Iso8859_1Reader(in);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 132 */     return new InputStreamReader(in, std2java(encoding));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 140 */   private static final Hashtable charsets = new Hashtable<>(31);
/*     */   
/*     */   static {
/* 143 */     charsets.put("UTF-16", "Unicode");
/* 144 */     charsets.put("ISO-10646-UCS-2", "Unicode");
/*     */ 
/*     */ 
/*     */     
/* 148 */     charsets.put("EBCDIC-CP-US", "cp037");
/* 149 */     charsets.put("EBCDIC-CP-CA", "cp037");
/* 150 */     charsets.put("EBCDIC-CP-NL", "cp037");
/* 151 */     charsets.put("EBCDIC-CP-WT", "cp037");
/*     */     
/* 153 */     charsets.put("EBCDIC-CP-DK", "cp277");
/* 154 */     charsets.put("EBCDIC-CP-NO", "cp277");
/* 155 */     charsets.put("EBCDIC-CP-FI", "cp278");
/* 156 */     charsets.put("EBCDIC-CP-SE", "cp278");
/*     */     
/* 158 */     charsets.put("EBCDIC-CP-IT", "cp280");
/* 159 */     charsets.put("EBCDIC-CP-ES", "cp284");
/* 160 */     charsets.put("EBCDIC-CP-GB", "cp285");
/* 161 */     charsets.put("EBCDIC-CP-FR", "cp297");
/*     */     
/* 163 */     charsets.put("EBCDIC-CP-AR1", "cp420");
/* 164 */     charsets.put("EBCDIC-CP-HE", "cp424");
/* 165 */     charsets.put("EBCDIC-CP-BE", "cp500");
/* 166 */     charsets.put("EBCDIC-CP-CH", "cp500");
/*     */     
/* 168 */     charsets.put("EBCDIC-CP-ROECE", "cp870");
/* 169 */     charsets.put("EBCDIC-CP-YU", "cp870");
/* 170 */     charsets.put("EBCDIC-CP-IS", "cp871");
/* 171 */     charsets.put("EBCDIC-CP-AR2", "cp918");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static String std2java(String encoding) {
/* 181 */     String temp = encoding.toUpperCase();
/* 182 */     temp = (String)charsets.get(temp);
/* 183 */     return (temp != null) ? temp : encoding;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getEncoding() {
/* 190 */     return this.assignedEncoding;
/*     */   }
/*     */   
/*     */   private XmlReader(InputStream stream) throws IOException {
/* 194 */     super(stream);
/*     */ 
/*     */     
/*     */     PushbackInputStream pb;
/*     */ 
/*     */     
/* 200 */     if (stream instanceof PushbackInputStream) {
/* 201 */       pb = (PushbackInputStream)stream;
/*     */     } else {
/* 203 */       pb = new PushbackInputStream(stream, 512);
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 209 */     byte[] buf = new byte[4];
/* 210 */     int len = pb.read(buf);
/* 211 */     if (len > 0) {
/* 212 */       pb.unread(buf, 0, len);
/*     */     }
/* 214 */     if (len == 4) {
/* 215 */       switch (buf[0] & 0xFF) {
/*     */         
/*     */         case 0:
/* 218 */           if (buf[1] == 60 && buf[2] == 0 && buf[3] == 63) {
/* 219 */             setEncoding(pb, "UnicodeBig");
/*     */             return;
/*     */           } 
/*     */           break;
/*     */ 
/*     */         
/*     */         case 60:
/* 226 */           switch (buf[1] & 0xFF) {
/*     */             default:
/*     */               break;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */             
/*     */             case 0:
/* 235 */               if (buf[2] == 63 && buf[3] == 0) {
/* 236 */                 setEncoding(pb, "UnicodeLittle");
/*     */                 return;
/*     */               } 
/*     */               break;
/*     */             
/*     */             case 63:
/*     */               break;
/*     */           } 
/* 244 */           if (buf[2] != 120 || buf[3] != 109) {
/*     */             break;
/*     */           }
/*     */ 
/*     */ 
/*     */           
/* 250 */           useEncodingDecl(pb, "UTF8");
/*     */           return;
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*     */         case 76:
/* 257 */           if (buf[1] == 111 && (0xFF & buf[2]) == 167 && (0xFF & buf[3]) == 148) {
/*     */ 
/*     */             
/* 260 */             useEncodingDecl(pb, "CP037");
/*     */             return;
/*     */           } 
/*     */           break;
/*     */ 
/*     */ 
/*     */         
/*     */         case 254:
/* 268 */           if ((buf[1] & 0xFF) != 255)
/*     */             break; 
/* 270 */           setEncoding(pb, "UTF-16");
/*     */           return;
/*     */ 
/*     */         
/*     */         case 255:
/* 275 */           if ((buf[1] & 0xFF) != 254)
/*     */             break; 
/* 277 */           setEncoding(pb, "UTF-16");
/*     */           return;
/*     */       } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     }
/* 289 */     setEncoding(pb, "UTF-8");
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
/*     */   private void useEncodingDecl(PushbackInputStream pb, String encoding) throws IOException {
/* 304 */     byte[] buffer = new byte[512];
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 315 */     int len = pb.read(buffer, 0, buffer.length);
/* 316 */     pb.unread(buffer, 0, len);
/* 317 */     Reader r = new InputStreamReader(new ByteArrayInputStream(buffer, 4, len), encoding);
/*     */ 
/*     */ 
/*     */     
/*     */     int c;
/*     */ 
/*     */     
/* 324 */     if ((c = r.read()) != 108) {
/* 325 */       setEncoding(pb, "UTF-8");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       return;
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 338 */     StringBuffer buf = new StringBuffer();
/* 339 */     StringBuffer keyBuf = null;
/* 340 */     String key = null;
/* 341 */     boolean sawEq = false;
/* 342 */     char quoteChar = Character.MIN_VALUE;
/* 343 */     boolean sawQuestion = false;
/*     */     
/*     */     int i;
/* 346 */     label81: for (i = 0; i < 507 && (
/* 347 */       c = r.read()) != -1; i++) {
/*     */ 
/*     */ 
/*     */       
/* 351 */       if (c == 32 || c == 9 || c == 10 || c == 13) {
/*     */         continue;
/*     */       }
/*     */       
/* 355 */       if (i == 0) {
/*     */         break;
/*     */       }
/*     */       
/* 359 */       if (c == 63) {
/* 360 */         sawQuestion = true;
/* 361 */       } else if (sawQuestion) {
/* 362 */         if (c == 62)
/*     */           break; 
/* 364 */         sawQuestion = false;
/*     */       } 
/*     */ 
/*     */       
/* 368 */       if (key == null || !sawEq) {
/* 369 */         if (keyBuf == null) {
/* 370 */           if (!Character.isWhitespace((char)c))
/*     */           
/* 372 */           { keyBuf = buf;
/* 373 */             buf.setLength(0);
/* 374 */             buf.append((char)c);
/* 375 */             sawEq = false; } 
/* 376 */         } else if (Character.isWhitespace((char)c)) {
/* 377 */           key = keyBuf.toString();
/* 378 */         } else if (c == 61) {
/* 379 */           if (key == null)
/* 380 */             key = keyBuf.toString(); 
/* 381 */           sawEq = true;
/* 382 */           keyBuf = null;
/* 383 */           quoteChar = Character.MIN_VALUE;
/*     */         } else {
/* 385 */           keyBuf.append((char)c);
/*     */         } 
/*     */         
/*     */         continue;
/*     */       } 
/* 390 */       if (Character.isWhitespace((char)c))
/*     */         continue; 
/* 392 */       if (c == 34 || c == 39) {
/* 393 */         if (quoteChar == '\000') {
/* 394 */           quoteChar = (char)c;
/* 395 */           buf.setLength(0); continue;
/*     */         } 
/* 397 */         if (c == quoteChar) {
/* 398 */           if ("encoding".equals(key)) {
/* 399 */             this.assignedEncoding = buf.toString();
/*     */ 
/*     */             
/* 402 */             for (i = 0; i < this.assignedEncoding.length(); i++) {
/* 403 */               c = this.assignedEncoding.charAt(i);
/* 404 */               if ((c >= 65 && c <= 90) || (c >= 97 && c <= 122)) {
/*     */                 continue;
/*     */               }
/* 407 */               if (i == 0)
/*     */                 break label81; 
/* 409 */               if (i > 0) { if (c == 45 || (c >= 48 && c <= 57) || c == 46 || c == 95) {
/*     */                   continue;
/*     */                 }
/*     */                 
/*     */                 break label81; }
/*     */               
/*     */               break label81;
/*     */             } 
/* 417 */             setEncoding(pb, this.assignedEncoding);
/*     */             
/*     */             return;
/*     */           } 
/* 421 */           key = null;
/*     */           
/*     */           continue;
/*     */         } 
/*     */       } 
/* 426 */       buf.append((char)c);
/*     */       continue;
/*     */     } 
/* 429 */     setEncoding(pb, "UTF-8");
/*     */   }
/*     */ 
/*     */   
/*     */   private void setEncoding(InputStream stream, String encoding) throws IOException {
/* 434 */     this.assignedEncoding = encoding;
/* 435 */     this.in = createReader(stream, encoding);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int read(char[] buf, int off, int len) throws IOException {
/* 444 */     if (this.closed)
/* 445 */       return -1; 
/* 446 */     int val = this.in.read(buf, off, len);
/* 447 */     if (val == -1)
/* 448 */       close(); 
/* 449 */     return val;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int read() throws IOException {
/* 458 */     if (this.closed)
/* 459 */       throw new IOException("closed"); 
/* 460 */     int val = this.in.read();
/* 461 */     if (val == -1)
/* 462 */       close(); 
/* 463 */     return val;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean markSupported() {
/* 470 */     return (this.in == null) ? false : this.in.markSupported();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void mark(int value) throws IOException {
/* 480 */     if (this.in != null) this.in.mark(value);
/*     */   
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void reset() throws IOException {
/* 487 */     if (this.in != null) this.in.reset();
/*     */   
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public long skip(long value) throws IOException {
/* 494 */     return (this.in == null) ? 0L : this.in.skip(value);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean ready() throws IOException {
/* 501 */     return (this.in == null) ? false : this.in.ready();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void close() throws IOException {
/* 508 */     if (this.closed)
/*     */       return; 
/* 510 */     this.in.close();
/* 511 */     this.in = null;
/* 512 */     this.closed = true;
/*     */   }
/*     */ 
/*     */   
/*     */   static abstract class BaseReader
/*     */     extends Reader
/*     */   {
/*     */     protected InputStream instream;
/*     */     
/*     */     protected byte[] buffer;
/*     */     
/*     */     protected int start;
/*     */     
/*     */     protected int finish;
/*     */ 
/*     */     
/*     */     BaseReader(InputStream stream) {
/* 529 */       super(stream);
/*     */       
/* 531 */       this.instream = stream;
/* 532 */       this.buffer = new byte[8192];
/*     */     }
/*     */     
/*     */     public boolean ready() throws IOException {
/* 536 */       return (this.instream == null || this.finish - this.start > 0 || this.instream
/*     */         
/* 538 */         .available() != 0);
/*     */     }
/*     */ 
/*     */     
/*     */     public void close() throws IOException {
/* 543 */       if (this.instream != null) {
/* 544 */         this.instream.close();
/* 545 */         this.start = this.finish = 0;
/* 546 */         this.buffer = null;
/* 547 */         this.instream = null;
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static final class Utf8Reader
/*     */     extends BaseReader
/*     */   {
/*     */     private char nextChar;
/*     */ 
/*     */ 
/*     */     
/*     */     Utf8Reader(InputStream stream) {
/* 563 */       super(stream);
/*     */     }
/*     */     
/*     */     public int read(char[] buf, int offset, int len) throws IOException {
/* 567 */       int i = 0, c = 0;
/*     */       
/* 569 */       if (len <= 0) {
/* 570 */         return 0;
/*     */       }
/*     */       
/* 573 */       if (this.nextChar != '\000') {
/* 574 */         buf[offset + i++] = this.nextChar;
/* 575 */         this.nextChar = Character.MIN_VALUE;
/*     */       } 
/*     */       
/* 578 */       while (i < len) {
/*     */         
/* 580 */         if (this.finish <= this.start) {
/* 581 */           if (this.instream == null) {
/* 582 */             c = -1;
/*     */             break;
/*     */           } 
/* 585 */           this.start = 0;
/* 586 */           this.finish = this.instream.read(this.buffer, 0, this.buffer.length);
/* 587 */           if (this.finish <= 0) {
/* 588 */             close();
/* 589 */             c = -1;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */             
/*     */             break;
/*     */           } 
/*     */         } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 606 */         c = this.buffer[this.start] & 0xFF;
/* 607 */         if ((c & 0x80) == 0) {
/*     */           
/* 609 */           this.start++;
/* 610 */           buf[offset + i++] = (char)c;
/*     */ 
/*     */ 
/*     */           
/*     */           continue;
/*     */         } 
/*     */ 
/*     */         
/* 618 */         int off = this.start;
/*     */ 
/*     */         
/*     */         try {
/* 622 */           if ((this.buffer[off] & 0xE0) == 192) {
/* 623 */             c = (this.buffer[off++] & 0x1F) << 6;
/* 624 */             c += this.buffer[off++] & 0x3F;
/*     */ 
/*     */ 
/*     */           
/*     */           }
/* 629 */           else if ((this.buffer[off] & 0xF0) == 224) {
/* 630 */             c = (this.buffer[off++] & 0xF) << 12;
/* 631 */             c += (this.buffer[off++] & 0x3F) << 6;
/* 632 */             c += this.buffer[off++] & 0x3F;
/*     */ 
/*     */ 
/*     */           
/*     */           }
/* 637 */           else if ((this.buffer[off] & 0xF8) == 240) {
/* 638 */             c = (this.buffer[off++] & 0x7) << 18;
/* 639 */             c += (this.buffer[off++] & 0x3F) << 12;
/* 640 */             c += (this.buffer[off++] & 0x3F) << 6;
/* 641 */             c += this.buffer[off++] & 0x3F;
/*     */ 
/*     */ 
/*     */ 
/*     */             
/* 646 */             if (c > 1114111) {
/* 647 */               throw new CharConversionException("UTF-8 encoding of character 0x00" + 
/* 648 */                   Integer.toHexString(c) + " can't be converted to Unicode.");
/*     */             }
/*     */ 
/*     */             
/* 652 */             c -= 65536;
/* 653 */             this.nextChar = (char)(56320 + (c & 0x3FF));
/* 654 */             c = 55296 + (c >> 10);
/*     */           
/*     */           }
/*     */           else {
/*     */             
/* 659 */             throw new CharConversionException("Unconvertible UTF-8 character beginning with 0x" + 
/*     */                 
/* 661 */                 Integer.toHexString(this.buffer[this.start] & 0xFF));
/*     */           } 
/* 663 */         } catch (ArrayIndexOutOfBoundsException e) {
/*     */           
/* 665 */           c = 0;
/*     */         } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 674 */         if (off > this.finish) {
/* 675 */           System.arraycopy(this.buffer, this.start, this.buffer, 0, this.finish - this.start);
/*     */           
/* 677 */           this.finish -= this.start;
/* 678 */           this.start = 0;
/* 679 */           off = this.instream.read(this.buffer, this.finish, this.buffer.length - this.finish);
/*     */           
/* 681 */           if (off < 0) {
/* 682 */             close();
/* 683 */             throw new CharConversionException("Partial UTF-8 char");
/*     */           } 
/* 685 */           this.finish += off;
/*     */ 
/*     */           
/*     */           continue;
/*     */         } 
/*     */ 
/*     */         
/* 692 */         this.start++; for (; this.start < off; this.start++) {
/* 693 */           if ((this.buffer[this.start] & 0xC0) != 128) {
/* 694 */             close();
/* 695 */             throw new CharConversionException("Malformed UTF-8 char -- is an XML encoding declaration missing?");
/*     */           } 
/*     */         } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 703 */         buf[offset + i++] = (char)c;
/* 704 */         if (this.nextChar != '\000' && i < len) {
/* 705 */           buf[offset + i++] = this.nextChar;
/* 706 */           this.nextChar = Character.MIN_VALUE;
/*     */         } 
/*     */       } 
/* 709 */       if (i > 0)
/* 710 */         return i; 
/* 711 */       return (c == -1) ? -1 : 0;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static final class AsciiReader
/*     */     extends BaseReader
/*     */   {
/*     */     AsciiReader(InputStream in) {
/* 725 */       super(in);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public int read(char[] buf, int offset, int len) throws IOException {
/* 731 */       if (this.instream == null)
/* 732 */         return -1; 
/*     */       int i;
/* 734 */       for (i = 0; i < len; i++) {
/* 735 */         if (this.start >= this.finish) {
/* 736 */           this.start = 0;
/* 737 */           this.finish = this.instream.read(this.buffer, 0, this.buffer.length);
/* 738 */           if (this.finish <= 0) {
/* 739 */             if (this.finish <= 0)
/* 740 */               close(); 
/*     */             break;
/*     */           } 
/*     */         } 
/* 744 */         int c = this.buffer[this.start++];
/* 745 */         if ((c & 0x80) != 0)
/* 746 */           throw new CharConversionException("Illegal ASCII character, 0x" + 
/* 747 */               Integer.toHexString(c & 0xFF)); 
/* 748 */         buf[offset + i] = (char)c;
/*     */       } 
/* 750 */       if (i == 0 && this.finish <= 0)
/* 751 */         return -1; 
/* 752 */       return i;
/*     */     }
/*     */   }
/*     */   
/*     */   static final class Iso8859_1Reader extends BaseReader {
/*     */     Iso8859_1Reader(InputStream in) {
/* 758 */       super(in);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public int read(char[] buf, int offset, int len) throws IOException {
/* 764 */       if (this.instream == null)
/* 765 */         return -1; 
/*     */       int i;
/* 767 */       for (i = 0; i < len; i++) {
/* 768 */         if (this.start >= this.finish) {
/* 769 */           this.start = 0;
/* 770 */           this.finish = this.instream.read(this.buffer, 0, this.buffer.length);
/* 771 */           if (this.finish <= 0) {
/* 772 */             if (this.finish <= 0)
/* 773 */               close(); 
/*     */             break;
/*     */           } 
/*     */         } 
/* 777 */         buf[offset + i] = (char)(0xFF & this.buffer[this.start++]);
/*     */       } 
/* 779 */       if (i == 0 && this.finish <= 0)
/* 780 */         return -1; 
/* 781 */       return i;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\xml\internal\dtdparser\XmlReader.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */