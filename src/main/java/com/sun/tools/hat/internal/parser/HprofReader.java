/*     */ package com.sun.tools.hat.internal.parser;
/*     */ 
/*     */ import com.sun.tools.hat.internal.model.ArrayTypeCodes;
/*     */ import com.sun.tools.hat.internal.model.JavaBoolean;
/*     */ import com.sun.tools.hat.internal.model.JavaByte;
/*     */ import com.sun.tools.hat.internal.model.JavaChar;
/*     */ import com.sun.tools.hat.internal.model.JavaClass;
/*     */ import com.sun.tools.hat.internal.model.JavaDouble;
/*     */ import com.sun.tools.hat.internal.model.JavaField;
/*     */ import com.sun.tools.hat.internal.model.JavaFloat;
/*     */ import com.sun.tools.hat.internal.model.JavaHeapObject;
/*     */ import com.sun.tools.hat.internal.model.JavaInt;
/*     */ import com.sun.tools.hat.internal.model.JavaLong;
/*     */ import com.sun.tools.hat.internal.model.JavaObject;
/*     */ import com.sun.tools.hat.internal.model.JavaObjectArray;
/*     */ import com.sun.tools.hat.internal.model.JavaObjectRef;
/*     */ import com.sun.tools.hat.internal.model.JavaShort;
/*     */ import com.sun.tools.hat.internal.model.JavaStatic;
/*     */ import com.sun.tools.hat.internal.model.JavaThing;
/*     */ import com.sun.tools.hat.internal.model.JavaValueArray;
/*     */ import com.sun.tools.hat.internal.model.Root;
/*     */ import com.sun.tools.hat.internal.model.Snapshot;
/*     */ import com.sun.tools.hat.internal.model.StackFrame;
/*     */ import com.sun.tools.hat.internal.model.StackTrace;
/*     */ import com.sun.tools.hat.internal.util.Misc;
/*     */ import java.io.EOFException;
/*     */ import java.io.IOException;
/*     */ import java.io.RandomAccessFile;
/*     */ import java.util.Date;
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
/*     */ public class HprofReader
/*     */   extends Reader
/*     */   implements ArrayTypeCodes
/*     */ {
/*     */   static final int MAGIC_NUMBER = 1245795905;
/*  51 */   private static final String[] VERSIONS = new String[] { " PROFILE 1.0\000", " PROFILE 1.0.1\000", " PROFILE 1.0.2\000" };
/*     */ 
/*     */   
/*     */   private static final int VERSION_JDK12BETA3 = 0;
/*     */ 
/*     */   
/*     */   private static final int VERSION_JDK12BETA4 = 1;
/*     */ 
/*     */   
/*     */   private static final int VERSION_JDK6 = 2;
/*     */ 
/*     */   
/*     */   static final int HPROF_UTF8 = 1;
/*     */ 
/*     */   
/*     */   static final int HPROF_LOAD_CLASS = 2;
/*     */ 
/*     */   
/*     */   static final int HPROF_UNLOAD_CLASS = 3;
/*     */ 
/*     */   
/*     */   static final int HPROF_FRAME = 4;
/*     */ 
/*     */   
/*     */   static final int HPROF_TRACE = 5;
/*     */ 
/*     */   
/*     */   static final int HPROF_ALLOC_SITES = 6;
/*     */ 
/*     */   
/*     */   static final int HPROF_HEAP_SUMMARY = 7;
/*     */   
/*     */   static final int HPROF_START_THREAD = 10;
/*     */   
/*     */   static final int HPROF_END_THREAD = 11;
/*     */   
/*     */   static final int HPROF_HEAP_DUMP = 12;
/*     */   
/*     */   static final int HPROF_CPU_SAMPLES = 13;
/*     */   
/*     */   static final int HPROF_CONTROL_SETTINGS = 14;
/*     */   
/*     */   static final int HPROF_LOCKSTATS_WAIT_TIME = 16;
/*     */   
/*     */   static final int HPROF_LOCKSTATS_HOLD_TIME = 17;
/*     */   
/*     */   static final int HPROF_GC_ROOT_UNKNOWN = 255;
/*     */   
/*     */   static final int HPROF_GC_ROOT_JNI_GLOBAL = 1;
/*     */   
/*     */   static final int HPROF_GC_ROOT_JNI_LOCAL = 2;
/*     */   
/*     */   static final int HPROF_GC_ROOT_JAVA_FRAME = 3;
/*     */   
/*     */   static final int HPROF_GC_ROOT_NATIVE_STACK = 4;
/*     */   
/*     */   static final int HPROF_GC_ROOT_STICKY_CLASS = 5;
/*     */   
/*     */   static final int HPROF_GC_ROOT_THREAD_BLOCK = 6;
/*     */   
/*     */   static final int HPROF_GC_ROOT_MONITOR_USED = 7;
/*     */   
/*     */   static final int HPROF_GC_ROOT_THREAD_OBJ = 8;
/*     */   
/*     */   static final int HPROF_GC_CLASS_DUMP = 32;
/*     */   
/*     */   static final int HPROF_GC_INSTANCE_DUMP = 33;
/*     */   
/*     */   static final int HPROF_GC_OBJ_ARRAY_DUMP = 34;
/*     */   
/*     */   static final int HPROF_GC_PRIM_ARRAY_DUMP = 35;
/*     */   
/*     */   static final int HPROF_HEAP_DUMP_SEGMENT = 28;
/*     */   
/*     */   static final int HPROF_HEAP_DUMP_END = 44;
/*     */   
/*     */   private static final int T_CLASS = 2;
/*     */   
/*     */   private int version;
/*     */   
/*     */   private int debugLevel;
/*     */   
/*     */   private long currPos;
/*     */   
/*     */   private int dumpsToSkip;
/*     */   
/*     */   private boolean callStack;
/*     */   
/*     */   private int identifierSize;
/*     */   
/*     */   private Hashtable<Long, String> names;
/*     */   
/*     */   private Hashtable<Integer, ThreadObject> threadObjects;
/*     */   
/*     */   private Hashtable<Long, String> classNameFromObjectID;
/*     */   
/*     */   private Hashtable<Integer, String> classNameFromSerialNo;
/*     */   
/*     */   private Hashtable<Long, StackFrame> stackFrames;
/*     */   
/*     */   private Hashtable<Integer, StackTrace> stackTraces;
/*     */   
/*     */   private Snapshot snapshot;
/*     */ 
/*     */   
/*     */   public HprofReader(String paramString, PositionDataInputStream paramPositionDataInputStream, int paramInt1, boolean paramBoolean, int paramInt2) throws IOException {
/* 157 */     super(paramPositionDataInputStream);
/* 158 */     RandomAccessFile randomAccessFile = new RandomAccessFile(paramString, "r");
/* 159 */     this.snapshot = new Snapshot(MappedReadBuffer.create(randomAccessFile));
/* 160 */     this.dumpsToSkip = paramInt1 - 1;
/* 161 */     this.callStack = paramBoolean;
/* 162 */     this.debugLevel = paramInt2;
/* 163 */     this.names = new Hashtable<>();
/* 164 */     this.threadObjects = new Hashtable<>(43);
/* 165 */     this.classNameFromObjectID = new Hashtable<>();
/* 166 */     if (paramBoolean) {
/* 167 */       this.stackFrames = new Hashtable<>(43);
/* 168 */       this.stackTraces = new Hashtable<>(43);
/* 169 */       this.classNameFromSerialNo = new Hashtable<>();
/*     */     } 
/*     */   }
/*     */   
/*     */   public Snapshot read() throws IOException {
/* 174 */     this.currPos = 4L;
/* 175 */     this.version = readVersionHeader();
/* 176 */     this.identifierSize = this.in.readInt();
/* 177 */     this.snapshot.setIdentifierSize(this.identifierSize);
/* 178 */     if (this.version >= 1) {
/* 179 */       this.snapshot.setNewStyleArrayClass(true);
/*     */     } else {
/* 181 */       this.snapshot.setNewStyleArrayClass(false);
/*     */     } 
/*     */     
/* 184 */     this.currPos += 4L;
/* 185 */     if (this.identifierSize != 4 && this.identifierSize != 8) {
/* 186 */       throw new IOException("I'm sorry, but I can't deal with an identifier size of " + this.identifierSize + ".  I can only deal with 4 or 8.");
/*     */     }
/* 188 */     System.out.println("Dump file created " + new Date(this.in.readLong()));
/* 189 */     this.currPos += 8L; while (true) {
/*     */       int i; long l3; int k; long l2; int j; long l4; int m; byte[] arrayOfByte; String str1; StackFrame[] arrayOfStackFrame; int n; String str2; byte b; long l5; String str3; int i1; Long long_;
/*     */       String str4, str5;
/*     */       int i2;
/*     */       try {
/* 194 */         i = this.in.readUnsignedByte();
/* 195 */       } catch (EOFException eOFException) {
/*     */         break;
/*     */       } 
/* 198 */       this.in.readInt();
/*     */ 
/*     */       
/* 201 */       long l1 = this.in.readInt() & 0xFFFFFFFFL;
/* 202 */       if (this.debugLevel > 0) {
/* 203 */         System.out.println("Read record type " + i + ", length " + l1 + " at position " + 
/*     */             
/* 205 */             toHex(this.currPos));
/*     */       }
/* 207 */       if (l1 < 0L) {
/* 208 */         throw new IOException("Bad record length of " + l1 + " at byte " + 
/* 209 */             toHex(this.currPos + 5L) + " of file.");
/*     */       }
/*     */       
/* 212 */       this.currPos += 9L + l1;
/* 213 */       switch (i) {
/*     */         case 1:
/* 215 */           l3 = readID();
/* 216 */           arrayOfByte = new byte[(int)l1 - this.identifierSize];
/* 217 */           this.in.readFully(arrayOfByte);
/* 218 */           this.names.put(new Long(l3), new String(arrayOfByte));
/*     */           continue;
/*     */         
/*     */         case 2:
/* 222 */           k = this.in.readInt();
/* 223 */           l4 = readID();
/* 224 */           n = this.in.readInt();
/* 225 */           l5 = readID();
/* 226 */           long_ = new Long(l4);
/* 227 */           str5 = getNameFromID(l5).replace('/', '.');
/* 228 */           this.classNameFromObjectID.put(long_, str5);
/* 229 */           if (this.classNameFromSerialNo != null) {
/* 230 */             this.classNameFromSerialNo.put(new Integer(k), str5);
/*     */           }
/*     */           continue;
/*     */ 
/*     */         
/*     */         case 12:
/* 236 */           if (this.dumpsToSkip <= 0) {
/*     */             try {
/* 238 */               readHeapDump(l1, this.currPos);
/* 239 */             } catch (EOFException eOFException) {
/* 240 */               handleEOF(eOFException, this.snapshot);
/*     */             } 
/* 242 */             if (this.debugLevel > 0) {
/* 243 */               System.out.println("    Finished processing instances in heap dump.");
/*     */             }
/* 245 */             return this.snapshot;
/*     */           } 
/* 247 */           this.dumpsToSkip--;
/* 248 */           skipBytes(l1);
/*     */           continue;
/*     */ 
/*     */ 
/*     */         
/*     */         case 44:
/* 254 */           if (this.version >= 2) {
/* 255 */             if (this.dumpsToSkip <= 0) {
/* 256 */               skipBytes(l1);
/* 257 */               return this.snapshot;
/*     */             } 
/*     */             
/* 260 */             this.dumpsToSkip--;
/*     */           }
/*     */           else {
/*     */             
/* 264 */             warn("Ignoring unrecognized record type " + i);
/*     */           } 
/* 266 */           skipBytes(l1);
/*     */           continue;
/*     */ 
/*     */         
/*     */         case 28:
/* 271 */           if (this.version >= 2) {
/* 272 */             if (this.dumpsToSkip <= 0) {
/*     */               
/*     */               try {
/* 275 */                 readHeapDump(l1, this.currPos);
/* 276 */               } catch (EOFException eOFException) {
/* 277 */                 handleEOF(eOFException, this.snapshot);
/*     */               } 
/*     */               continue;
/*     */             } 
/* 281 */             skipBytes(l1);
/*     */             
/*     */             continue;
/*     */           } 
/* 285 */           warn("Ignoring unrecognized record type " + i);
/* 286 */           skipBytes(l1);
/*     */           continue;
/*     */ 
/*     */ 
/*     */         
/*     */         case 4:
/* 292 */           if (this.stackFrames == null) {
/* 293 */             skipBytes(l1); continue;
/*     */           } 
/* 295 */           l2 = readID();
/* 296 */           str1 = getNameFromID(readID());
/* 297 */           str2 = getNameFromID(readID());
/* 298 */           str3 = getNameFromID(readID());
/* 299 */           i1 = this.in.readInt();
/* 300 */           str4 = this.classNameFromSerialNo.get(new Integer(i1));
/* 301 */           i2 = this.in.readInt();
/* 302 */           if (i2 < -3) {
/* 303 */             warn("Weird stack frame line number:  " + i2);
/* 304 */             i2 = -1;
/*     */           } 
/* 306 */           this.stackFrames.put(new Long(l2), new StackFrame(str1, str2, str4, str3, i2));
/*     */           continue;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*     */         case 5:
/* 314 */           if (this.stackTraces == null) {
/* 315 */             skipBytes(l1); continue;
/*     */           } 
/* 317 */           j = this.in.readInt();
/* 318 */           m = this.in.readInt();
/* 319 */           arrayOfStackFrame = new StackFrame[this.in.readInt()];
/* 320 */           for (b = 0; b < arrayOfStackFrame.length; b++) {
/* 321 */             long l = readID();
/* 322 */             arrayOfStackFrame[b] = this.stackFrames.get(new Long(l));
/* 323 */             if (arrayOfStackFrame[b] == null) {
/* 324 */               throw new IOException("Stack frame " + toHex(l) + " not found");
/*     */             }
/*     */           } 
/* 327 */           this.stackTraces.put(new Integer(j), new StackTrace(arrayOfStackFrame));
/*     */           continue;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*     */         case 3:
/*     */         case 6:
/*     */         case 7:
/*     */         case 10:
/*     */         case 11:
/*     */         case 13:
/*     */         case 14:
/*     */         case 16:
/*     */         case 17:
/* 343 */           skipBytes(l1);
/*     */           continue;
/*     */       } 
/*     */       
/* 347 */       skipBytes(l1);
/* 348 */       warn("Ignoring unrecognized record type " + i);
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 353 */     return this.snapshot;
/*     */   }
/*     */   
/*     */   private void skipBytes(long paramLong) throws IOException {
/* 357 */     while (paramLong > 0L) {
/* 358 */       long l = this.in.skip(paramLong);
/* 359 */       paramLong -= l;
/* 360 */       if (l == 0L)
/*     */       {
/* 362 */         throw new EOFException("Couldn't skip enough bytes");
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   private int readVersionHeader() throws IOException {
/* 368 */     int i = VERSIONS.length;
/* 369 */     boolean[] arrayOfBoolean = new boolean[VERSIONS.length]; byte b;
/* 370 */     for (b = 0; b < i; b++) {
/* 371 */       arrayOfBoolean[b] = true;
/*     */     }
/*     */     
/* 374 */     b = 0;
/* 375 */     while (i > 0) {
/* 376 */       char c = (char)this.in.readByte();
/* 377 */       this.currPos++;
/* 378 */       for (byte b1 = 0; b1 < VERSIONS.length; b1++) {
/* 379 */         if (arrayOfBoolean[b1]) {
/* 380 */           if (c != VERSIONS[b1].charAt(b)) {
/* 381 */             arrayOfBoolean[b1] = false;
/* 382 */             i--;
/* 383 */           } else if (b == VERSIONS[b1].length() - 1) {
/* 384 */             return b1;
/*     */           } 
/*     */         }
/*     */       } 
/* 388 */       b++;
/*     */     } 
/* 390 */     throw new IOException("Version string not recognized at byte " + (b + 3));
/*     */   }
/*     */   
/*     */   private void readHeapDump(long paramLong1, long paramLong2) throws IOException {
/* 394 */     while (paramLong1 > 0L) {
/* 395 */       long l2; int j; long l1; int m; long l3; int k, n; ThreadObject threadObject1, threadObject2; StackTrace stackTrace1, stackTrace2; int i = this.in.readUnsignedByte();
/* 396 */       if (this.debugLevel > 0) {
/* 397 */         System.out.println("    Read heap sub-record type " + i + " at position " + 
/*     */             
/* 399 */             toHex(paramLong2 - paramLong1));
/*     */       }
/* 401 */       paramLong1--;
/* 402 */       switch (i) {
/*     */         case 255:
/* 404 */           l2 = readID();
/* 405 */           paramLong1 -= this.identifierSize;
/* 406 */           this.snapshot.addRoot(new Root(l2, 0L, 1, ""));
/*     */           continue;
/*     */         
/*     */         case 8:
/* 410 */           l2 = readID();
/* 411 */           m = this.in.readInt();
/* 412 */           n = this.in.readInt();
/* 413 */           paramLong1 -= (this.identifierSize + 8);
/* 414 */           this.threadObjects.put(new Integer(m), new ThreadObject(l2, n));
/*     */           continue;
/*     */ 
/*     */         
/*     */         case 1:
/* 419 */           l2 = readID();
/* 420 */           l3 = readID();
/* 421 */           paramLong1 -= (2 * this.identifierSize);
/* 422 */           this.snapshot.addRoot(new Root(l2, 0L, 4, ""));
/*     */           continue;
/*     */         
/*     */         case 2:
/* 426 */           l2 = readID();
/* 427 */           k = this.in.readInt();
/* 428 */           n = this.in.readInt();
/* 429 */           paramLong1 -= (this.identifierSize + 8);
/* 430 */           threadObject2 = getThreadObjectFromSequence(k);
/* 431 */           stackTrace2 = getStackTraceFromSerial(threadObject2.stackSeq);
/* 432 */           if (stackTrace2 != null) {
/* 433 */             stackTrace2 = stackTrace2.traceForDepth(n + 1);
/*     */           }
/* 435 */           this.snapshot.addRoot(new Root(l2, threadObject2.threadId, 3, "", stackTrace2));
/*     */           continue;
/*     */ 
/*     */         
/*     */         case 3:
/* 440 */           l2 = readID();
/* 441 */           k = this.in.readInt();
/* 442 */           n = this.in.readInt();
/* 443 */           paramLong1 -= (this.identifierSize + 8);
/* 444 */           threadObject2 = getThreadObjectFromSequence(k);
/* 445 */           stackTrace2 = getStackTraceFromSerial(threadObject2.stackSeq);
/* 446 */           if (stackTrace2 != null) {
/* 447 */             stackTrace2 = stackTrace2.traceForDepth(n + 1);
/*     */           }
/* 449 */           this.snapshot.addRoot(new Root(l2, threadObject2.threadId, 7, "", stackTrace2));
/*     */           continue;
/*     */ 
/*     */         
/*     */         case 4:
/* 454 */           l2 = readID();
/* 455 */           k = this.in.readInt();
/* 456 */           paramLong1 -= (this.identifierSize + 4);
/* 457 */           threadObject1 = getThreadObjectFromSequence(k);
/* 458 */           stackTrace1 = getStackTraceFromSerial(threadObject1.stackSeq);
/* 459 */           this.snapshot.addRoot(new Root(l2, threadObject1.threadId, 8, "", stackTrace1));
/*     */           continue;
/*     */ 
/*     */         
/*     */         case 5:
/* 464 */           l2 = readID();
/* 465 */           paramLong1 -= this.identifierSize;
/* 466 */           this.snapshot.addRoot(new Root(l2, 0L, 2, ""));
/*     */           continue;
/*     */         
/*     */         case 6:
/* 470 */           l2 = readID();
/* 471 */           k = this.in.readInt();
/* 472 */           paramLong1 -= (this.identifierSize + 4);
/* 473 */           threadObject1 = getThreadObjectFromSequence(k);
/* 474 */           stackTrace1 = getStackTraceFromSerial(threadObject1.stackSeq);
/* 475 */           this.snapshot.addRoot(new Root(l2, threadObject1.threadId, 5, "", stackTrace1));
/*     */           continue;
/*     */ 
/*     */         
/*     */         case 7:
/* 480 */           l2 = readID();
/* 481 */           paramLong1 -= this.identifierSize;
/* 482 */           this.snapshot.addRoot(new Root(l2, 0L, 6, ""));
/*     */           continue;
/*     */         
/*     */         case 32:
/* 486 */           j = readClass();
/* 487 */           paramLong1 -= j;
/*     */           continue;
/*     */         
/*     */         case 33:
/* 491 */           j = readInstance();
/* 492 */           paramLong1 -= j;
/*     */           continue;
/*     */         
/*     */         case 34:
/* 496 */           l1 = readArray(false);
/* 497 */           paramLong1 -= l1;
/*     */           continue;
/*     */         
/*     */         case 35:
/* 501 */           l1 = readArray(true);
/* 502 */           paramLong1 -= l1;
/*     */           continue;
/*     */       } 
/*     */       
/* 506 */       throw new IOException("Unrecognized heap dump sub-record type:  " + i);
/*     */     } 
/*     */ 
/*     */     
/* 510 */     if (paramLong1 != 0L) {
/* 511 */       warn("Error reading heap dump or heap dump segment:  Byte count is " + paramLong1 + " instead of 0");
/* 512 */       skipBytes(paramLong1);
/*     */     } 
/* 514 */     if (this.debugLevel > 0) {
/* 515 */       System.out.println("    Finished heap sub-records.");
/*     */     }
/*     */   }
/*     */   
/*     */   private long readID() throws IOException {
/* 520 */     return (this.identifierSize == 4) ? (Snapshot.SMALL_ID_MASK & this.in
/* 521 */       .readInt()) : this.in.readLong();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private int readValue(JavaThing[] paramArrayOfJavaThing) throws IOException {
/* 530 */     byte b = this.in.readByte();
/* 531 */     return 1 + readValueForType(b, paramArrayOfJavaThing);
/*     */   }
/*     */ 
/*     */   
/*     */   private int readValueForType(byte paramByte, JavaThing[] paramArrayOfJavaThing) throws IOException {
/* 536 */     if (this.version >= 1) {
/* 537 */       paramByte = signatureFromTypeId(paramByte);
/*     */     }
/* 539 */     return readValueForTypeSignature(paramByte, paramArrayOfJavaThing); } private int readValueForTypeSignature(byte paramByte, JavaThing[] paramArrayOfJavaThing) throws IOException { long l2; byte b; short s; char c;
/*     */     int i;
/*     */     long l1;
/*     */     float f;
/*     */     double d;
/* 544 */     switch (paramByte) {
/*     */       case 76:
/*     */       case 91:
/* 547 */         l2 = readID();
/* 548 */         if (paramArrayOfJavaThing != null) {
/* 549 */           paramArrayOfJavaThing[0] = (JavaThing)new JavaObjectRef(l2);
/*     */         }
/* 551 */         return this.identifierSize;
/*     */       
/*     */       case 90:
/* 554 */         b = this.in.readByte();
/* 555 */         if (b != 0 && b != 1) {
/* 556 */           warn("Illegal boolean value read");
/*     */         }
/* 558 */         if (paramArrayOfJavaThing != null) {
/* 559 */           paramArrayOfJavaThing[0] = (JavaThing)new JavaBoolean((b != 0));
/*     */         }
/* 561 */         return 1;
/*     */       
/*     */       case 66:
/* 564 */         b = this.in.readByte();
/* 565 */         if (paramArrayOfJavaThing != null) {
/* 566 */           paramArrayOfJavaThing[0] = (JavaThing)new JavaByte(b);
/*     */         }
/* 568 */         return 1;
/*     */       
/*     */       case 83:
/* 571 */         s = this.in.readShort();
/* 572 */         if (paramArrayOfJavaThing != null) {
/* 573 */           paramArrayOfJavaThing[0] = (JavaThing)new JavaShort(s);
/*     */         }
/* 575 */         return 2;
/*     */       
/*     */       case 67:
/* 578 */         c = this.in.readChar();
/* 579 */         if (paramArrayOfJavaThing != null) {
/* 580 */           paramArrayOfJavaThing[0] = (JavaThing)new JavaChar(c);
/*     */         }
/* 582 */         return 2;
/*     */       
/*     */       case 73:
/* 585 */         i = this.in.readInt();
/* 586 */         if (paramArrayOfJavaThing != null) {
/* 587 */           paramArrayOfJavaThing[0] = (JavaThing)new JavaInt(i);
/*     */         }
/* 589 */         return 4;
/*     */       
/*     */       case 74:
/* 592 */         l1 = this.in.readLong();
/* 593 */         if (paramArrayOfJavaThing != null) {
/* 594 */           paramArrayOfJavaThing[0] = (JavaThing)new JavaLong(l1);
/*     */         }
/* 596 */         return 8;
/*     */       
/*     */       case 70:
/* 599 */         f = this.in.readFloat();
/* 600 */         if (paramArrayOfJavaThing != null) {
/* 601 */           paramArrayOfJavaThing[0] = (JavaThing)new JavaFloat(f);
/*     */         }
/* 603 */         return 4;
/*     */       
/*     */       case 68:
/* 606 */         d = this.in.readDouble();
/* 607 */         if (paramArrayOfJavaThing != null) {
/* 608 */           paramArrayOfJavaThing[0] = (JavaThing)new JavaDouble(d);
/*     */         }
/* 610 */         return 8;
/*     */     } 
/*     */     
/* 613 */     throw new IOException("Bad value signature:  " + paramByte); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private ThreadObject getThreadObjectFromSequence(int paramInt) throws IOException {
/* 620 */     ThreadObject threadObject = this.threadObjects.get(new Integer(paramInt));
/* 621 */     if (threadObject == null) {
/* 622 */       throw new IOException("Thread " + paramInt + " not found for JNI local ref");
/*     */     }
/*     */     
/* 625 */     return threadObject;
/*     */   }
/*     */   
/*     */   private String getNameFromID(long paramLong) throws IOException {
/* 629 */     return getNameFromID(new Long(paramLong));
/*     */   }
/*     */   
/*     */   private String getNameFromID(Long paramLong) throws IOException {
/* 633 */     if (paramLong.longValue() == 0L) {
/* 634 */       return "";
/*     */     }
/* 636 */     String str = this.names.get(paramLong);
/* 637 */     if (str == null) {
/* 638 */       warn("Name not found at " + toHex(paramLong.longValue()));
/* 639 */       return "unresolved name " + toHex(paramLong.longValue());
/*     */     } 
/* 641 */     return str;
/*     */   }
/*     */   
/*     */   private StackTrace getStackTraceFromSerial(int paramInt) throws IOException {
/* 645 */     if (this.stackTraces == null) {
/* 646 */       return null;
/*     */     }
/* 648 */     StackTrace stackTrace = this.stackTraces.get(new Integer(paramInt));
/* 649 */     if (stackTrace == null) {
/* 650 */       warn("Stack trace not found for serial # " + paramInt);
/*     */     }
/* 652 */     return stackTrace;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private int readClass() throws IOException {
/* 660 */     long l1 = readID();
/* 661 */     StackTrace stackTrace = getStackTraceFromSerial(this.in.readInt());
/* 662 */     long l2 = readID();
/* 663 */     long l3 = readID();
/* 664 */     long l4 = readID();
/* 665 */     long l5 = readID();
/* 666 */     long l6 = readID();
/* 667 */     long l7 = readID();
/* 668 */     int i = this.in.readInt();
/* 669 */     int j = 7 * this.identifierSize + 8;
/*     */     
/* 671 */     int k = this.in.readUnsignedShort();
/* 672 */     j += 2; int m;
/* 673 */     for (m = 0; m < k; m++) {
/* 674 */       int i1 = this.in.readUnsignedShort();
/* 675 */       j += 2;
/* 676 */       j += readValue(null);
/*     */     } 
/*     */     
/* 679 */     m = this.in.readUnsignedShort();
/* 680 */     j += 2;
/* 681 */     JavaThing[] arrayOfJavaThing = new JavaThing[1];
/* 682 */     JavaStatic[] arrayOfJavaStatic = new JavaStatic[m]; int n;
/* 683 */     for (n = 0; n < m; n++) {
/* 684 */       long l = readID();
/* 685 */       j += this.identifierSize;
/* 686 */       byte b1 = this.in.readByte();
/* 687 */       j++;
/* 688 */       j += readValueForType(b1, arrayOfJavaThing);
/* 689 */       String str1 = getNameFromID(l);
/* 690 */       if (this.version >= 1) {
/* 691 */         b1 = signatureFromTypeId(b1);
/*     */       }
/* 693 */       String str2 = "" + (char)b1;
/* 694 */       JavaField javaField = new JavaField(str1, str2);
/* 695 */       arrayOfJavaStatic[n] = new JavaStatic(javaField, arrayOfJavaThing[0]);
/*     */     } 
/*     */     
/* 698 */     n = this.in.readUnsignedShort();
/* 699 */     j += 2;
/* 700 */     JavaField[] arrayOfJavaField = new JavaField[n];
/* 701 */     for (byte b = 0; b < n; b++) {
/* 702 */       long l = readID();
/* 703 */       j += this.identifierSize;
/* 704 */       byte b1 = this.in.readByte();
/* 705 */       j++;
/* 706 */       String str1 = getNameFromID(l);
/* 707 */       if (this.version >= 1) {
/* 708 */         b1 = signatureFromTypeId(b1);
/*     */       }
/* 710 */       String str2 = "" + (char)b1;
/* 711 */       arrayOfJavaField[b] = new JavaField(str1, str2);
/*     */     } 
/* 713 */     String str = this.classNameFromObjectID.get(new Long(l1));
/* 714 */     if (str == null) {
/* 715 */       warn("Class name not found for " + toHex(l1));
/* 716 */       str = "unknown-name@" + toHex(l1);
/*     */     } 
/* 718 */     JavaClass javaClass = new JavaClass(l1, str, l2, l3, l4, l5, arrayOfJavaField, arrayOfJavaStatic, i);
/*     */ 
/*     */     
/* 721 */     this.snapshot.addClass(l1, javaClass);
/* 722 */     this.snapshot.setSiteTrace((JavaHeapObject)javaClass, stackTrace);
/*     */     
/* 724 */     return j;
/*     */   }
/*     */   
/*     */   private String toHex(long paramLong) {
/* 728 */     return Misc.toHex(paramLong);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private int readInstance() throws IOException {
/* 736 */     long l1 = this.in.position();
/* 737 */     long l2 = readID();
/* 738 */     StackTrace stackTrace = getStackTraceFromSerial(this.in.readInt());
/* 739 */     long l3 = readID();
/* 740 */     int i = this.in.readInt();
/* 741 */     int j = 2 * this.identifierSize + 8 + i;
/* 742 */     JavaObject javaObject = new JavaObject(l3, l1);
/* 743 */     skipBytes(i);
/* 744 */     this.snapshot.addHeapObject(l2, (JavaHeapObject)javaObject);
/* 745 */     this.snapshot.setSiteTrace((JavaHeapObject)javaObject, stackTrace);
/* 746 */     return j;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private long readArray(boolean paramBoolean) throws IOException {
/* 754 */     long l4, l1 = this.in.position();
/* 755 */     long l2 = readID();
/* 756 */     StackTrace stackTrace = getStackTraceFromSerial(this.in.readInt());
/* 757 */     int i = this.in.readInt();
/* 758 */     long l3 = (this.identifierSize + 8);
/*     */     
/* 760 */     if (paramBoolean) {
/* 761 */       l4 = this.in.readByte();
/* 762 */       l3++;
/*     */     } else {
/* 764 */       l4 = readID();
/* 765 */       l3 += this.identifierSize;
/*     */     } 
/*     */ 
/*     */     
/* 769 */     byte b1 = 0;
/* 770 */     byte b2 = 0;
/* 771 */     if (paramBoolean || this.version < 1) {
/* 772 */       switch ((int)l4) {
/*     */         case 4:
/* 774 */           b1 = 90;
/* 775 */           b2 = 1;
/*     */           break;
/*     */         
/*     */         case 5:
/* 779 */           b1 = 67;
/* 780 */           b2 = 2;
/*     */           break;
/*     */         
/*     */         case 6:
/* 784 */           b1 = 70;
/* 785 */           b2 = 4;
/*     */           break;
/*     */         
/*     */         case 7:
/* 789 */           b1 = 68;
/* 790 */           b2 = 8;
/*     */           break;
/*     */         
/*     */         case 8:
/* 794 */           b1 = 66;
/* 795 */           b2 = 1;
/*     */           break;
/*     */         
/*     */         case 9:
/* 799 */           b1 = 83;
/* 800 */           b2 = 2;
/*     */           break;
/*     */         
/*     */         case 10:
/* 804 */           b1 = 73;
/* 805 */           b2 = 4;
/*     */           break;
/*     */         
/*     */         case 11:
/* 809 */           b1 = 74;
/* 810 */           b2 = 8;
/*     */           break;
/*     */       } 
/*     */       
/* 814 */       if (this.version >= 1 && b1 == 0) {
/* 815 */         throw new IOException("Unrecognized typecode:  " + l4);
/*     */       }
/*     */     } 
/*     */     
/* 819 */     if (b1 != 0) {
/* 820 */       long l = b2 * i;
/* 821 */       l3 += l;
/* 822 */       JavaValueArray javaValueArray = new JavaValueArray(b1, l1);
/* 823 */       skipBytes(l);
/* 824 */       this.snapshot.addHeapObject(l2, (JavaHeapObject)javaValueArray);
/* 825 */       this.snapshot.setSiteTrace((JavaHeapObject)javaValueArray, stackTrace);
/*     */     } else {
/* 827 */       long l = i * this.identifierSize;
/* 828 */       l3 += l;
/* 829 */       JavaObjectArray javaObjectArray = new JavaObjectArray(l4, l1);
/* 830 */       skipBytes(l);
/* 831 */       this.snapshot.addHeapObject(l2, (JavaHeapObject)javaObjectArray);
/* 832 */       this.snapshot.setSiteTrace((JavaHeapObject)javaObjectArray, stackTrace);
/*     */     } 
/* 834 */     return l3;
/*     */   }
/*     */   
/*     */   private byte signatureFromTypeId(byte paramByte) throws IOException {
/* 838 */     switch (paramByte) {
/*     */       case 2:
/* 840 */         return 76;
/*     */       
/*     */       case 4:
/* 843 */         return 90;
/*     */       
/*     */       case 5:
/* 846 */         return 67;
/*     */       
/*     */       case 6:
/* 849 */         return 70;
/*     */       
/*     */       case 7:
/* 852 */         return 68;
/*     */       
/*     */       case 8:
/* 855 */         return 66;
/*     */       
/*     */       case 9:
/* 858 */         return 83;
/*     */       
/*     */       case 10:
/* 861 */         return 73;
/*     */       
/*     */       case 11:
/* 864 */         return 74;
/*     */     } 
/*     */     
/* 867 */     throw new IOException("Invalid type id of " + paramByte);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void handleEOF(EOFException paramEOFException, Snapshot paramSnapshot) {
/* 873 */     if (this.debugLevel > 0) {
/* 874 */       paramEOFException.printStackTrace();
/*     */     }
/* 876 */     warn("Unexpected EOF. Will miss information...");
/*     */     
/* 878 */     paramSnapshot.setUnresolvedObjectsOK(true);
/*     */   }
/*     */   
/*     */   private void warn(String paramString) {
/* 882 */     System.out.println("WARNING: " + paramString);
/*     */   }
/*     */ 
/*     */   
/*     */   private class ThreadObject
/*     */   {
/*     */     long threadId;
/*     */     
/*     */     int stackSeq;
/*     */ 
/*     */     
/*     */     ThreadObject(long param1Long, int param1Int) {
/* 894 */       this.threadId = param1Long;
/* 895 */       this.stackSeq = param1Int;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\hat\internal\parser\HprofReader.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */