/*     */ package sun.jvmstat.perfdata.monitor.v2_0;
/*     */ 
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.IntBuffer;
/*     */ import java.nio.LongBuffer;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Map;
/*     */ import sun.jvmstat.monitor.IntegerMonitor;
/*     */ import sun.jvmstat.monitor.Monitor;
/*     */ import sun.jvmstat.monitor.MonitorException;
/*     */ import sun.jvmstat.monitor.Units;
/*     */ import sun.jvmstat.monitor.Variability;
/*     */ import sun.jvmstat.perfdata.monitor.MonitorDataException;
/*     */ import sun.jvmstat.perfdata.monitor.MonitorStatus;
/*     */ import sun.jvmstat.perfdata.monitor.MonitorStructureException;
/*     */ import sun.jvmstat.perfdata.monitor.MonitorTypeException;
/*     */ import sun.jvmstat.perfdata.monitor.PerfDataBufferImpl;
/*     */ import sun.jvmstat.perfdata.monitor.PerfIntegerMonitor;
/*     */ import sun.jvmstat.perfdata.monitor.PerfLongMonitor;
/*     */ import sun.jvmstat.perfdata.monitor.PerfStringConstantMonitor;
/*     */ import sun.jvmstat.perfdata.monitor.PerfStringVariableMonitor;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class PerfDataBuffer
/*     */   extends PerfDataBufferImpl
/*     */ {
/*     */   private static final boolean DEBUG = false;
/*  67 */   private static final int syncWaitMs = Integer.getInteger("sun.jvmstat.perdata.syncWaitMs", 5000).intValue();
/*  68 */   private static final ArrayList EMPTY_LIST = new ArrayList(0);
/*     */   
/*     */   private static final int PERFDATA_ENTRYLENGTH_OFFSET = 0;
/*     */   
/*     */   private static final int PERFDATA_ENTRYLENGTH_SIZE = 4;
/*     */   
/*     */   private static final int PERFDATA_NAMEOFFSET_OFFSET = 4;
/*     */   
/*     */   private static final int PERFDATA_NAMEOFFSET_SIZE = 4;
/*     */   
/*     */   private static final int PERFDATA_VECTORLENGTH_OFFSET = 8;
/*     */   
/*     */   private static final int PERFDATA_VECTORLENGTH_SIZE = 4;
/*     */   
/*     */   private static final int PERFDATA_DATATYPE_OFFSET = 12;
/*     */   
/*     */   private static final int PERFDATA_DATATYPE_SIZE = 1;
/*     */   
/*     */   private static final int PERFDATA_FLAGS_OFFSET = 13;
/*     */   
/*     */   private static final int PERFDATA_FLAGS_SIZE = 1;
/*     */   
/*     */   private static final int PERFDATA_DATAUNITS_OFFSET = 14;
/*     */   
/*     */   private static final int PERFDATA_DATAUNITS_SIZE = 1;
/*     */   
/*     */   private static final int PERFDATA_DATAVAR_OFFSET = 15;
/*     */   
/*     */   private static final int PERFDATA_DATAVAR_SIZE = 1;
/*     */   
/*     */   private static final int PERFDATA_DATAOFFSET_OFFSET = 16;
/*     */   
/*     */   private static final int PERFDATA_DATAOFFSET_SIZE = 4;
/*     */   
/*     */   PerfDataBufferPrologue prologue;
/*     */   
/*     */   int nextEntry;
/*     */   
/*     */   long lastNumEntries;
/*     */   
/*     */   IntegerMonitor overflow;
/*     */   ArrayList<Monitor> insertedMonitors;
/*     */   
/*     */   public PerfDataBuffer(ByteBuffer paramByteBuffer, int paramInt) throws MonitorException {
/* 112 */     super(paramByteBuffer, paramInt);
/* 113 */     this.prologue = new PerfDataBufferPrologue(paramByteBuffer);
/* 114 */     this.buffer.order(this.prologue.getByteOrder());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void buildMonitorMap(Map<String, Monitor> paramMap) throws MonitorException {
/* 121 */     assert Thread.holdsLock(this);
/*     */ 
/*     */     
/* 124 */     this.buffer.rewind();
/*     */ 
/*     */     
/* 127 */     buildPseudoMonitors(paramMap);
/*     */ 
/*     */ 
/*     */     
/* 131 */     synchWithTarget();
/*     */ 
/*     */     
/* 134 */     this.nextEntry = this.prologue.getEntryOffset();
/*     */ 
/*     */     
/* 137 */     int i = this.prologue.getNumEntries();
/*     */ 
/*     */     
/* 140 */     Monitor monitor = getNextMonitorEntry();
/* 141 */     while (monitor != null) {
/* 142 */       paramMap.put(monitor.getName(), monitor);
/* 143 */       monitor = getNextMonitorEntry();
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 157 */     this.lastNumEntries = i;
/*     */ 
/*     */     
/* 160 */     this.insertedMonitors = new ArrayList<>(paramMap.values());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void getNewMonitors(Map<String, Monitor> paramMap) throws MonitorException {
/* 167 */     assert Thread.holdsLock(this);
/*     */     
/* 169 */     int i = this.prologue.getNumEntries();
/*     */     
/* 171 */     if (i > this.lastNumEntries) {
/* 172 */       this.lastNumEntries = i;
/* 173 */       Monitor monitor = getNextMonitorEntry();
/*     */       
/* 175 */       while (monitor != null) {
/* 176 */         String str = monitor.getName();
/*     */ 
/*     */         
/* 179 */         if (!paramMap.containsKey(str)) {
/* 180 */           paramMap.put(str, monitor);
/* 181 */           if (this.insertedMonitors != null) {
/* 182 */             this.insertedMonitors.add(monitor);
/*     */           }
/*     */         } 
/* 185 */         monitor = getNextMonitorEntry();
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected MonitorStatus getMonitorStatus(Map<String, Monitor> paramMap) throws MonitorException {
/* 194 */     assert Thread.holdsLock(this);
/* 195 */     assert this.insertedMonitors != null;
/*     */ 
/*     */     
/* 198 */     getNewMonitors(paramMap);
/*     */ 
/*     */     
/* 201 */     ArrayList arrayList = EMPTY_LIST;
/* 202 */     ArrayList<Monitor> arrayList1 = this.insertedMonitors;
/*     */     
/* 204 */     this.insertedMonitors = new ArrayList<>();
/* 205 */     return new MonitorStatus(arrayList1, arrayList);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void buildPseudoMonitors(Map<String, Monitor> paramMap) {
/* 212 */     PerfIntegerMonitor perfIntegerMonitor = null;
/* 213 */     String str = null;
/* 214 */     IntBuffer intBuffer = null;
/*     */     
/* 216 */     str = "sun.perfdata.majorVersion";
/* 217 */     intBuffer = this.prologue.majorVersionBuffer();
/* 218 */     perfIntegerMonitor = new PerfIntegerMonitor(str, Units.NONE, Variability.CONSTANT, false, intBuffer);
/*     */     
/* 220 */     paramMap.put(str, perfIntegerMonitor);
/*     */     
/* 222 */     str = "sun.perfdata.minorVersion";
/* 223 */     intBuffer = this.prologue.minorVersionBuffer();
/* 224 */     perfIntegerMonitor = new PerfIntegerMonitor(str, Units.NONE, Variability.CONSTANT, false, intBuffer);
/*     */     
/* 226 */     paramMap.put(str, perfIntegerMonitor);
/*     */     
/* 228 */     str = "sun.perfdata.size";
/* 229 */     intBuffer = this.prologue.sizeBuffer();
/* 230 */     perfIntegerMonitor = new PerfIntegerMonitor(str, Units.BYTES, Variability.MONOTONIC, false, intBuffer);
/*     */     
/* 232 */     paramMap.put(str, perfIntegerMonitor);
/*     */     
/* 234 */     str = "sun.perfdata.used";
/* 235 */     intBuffer = this.prologue.usedBuffer();
/* 236 */     perfIntegerMonitor = new PerfIntegerMonitor(str, Units.BYTES, Variability.MONOTONIC, false, intBuffer);
/*     */     
/* 238 */     paramMap.put(str, perfIntegerMonitor);
/*     */     
/* 240 */     str = "sun.perfdata.overflow";
/* 241 */     intBuffer = this.prologue.overflowBuffer();
/* 242 */     perfIntegerMonitor = new PerfIntegerMonitor(str, Units.BYTES, Variability.MONOTONIC, false, intBuffer);
/*     */     
/* 244 */     paramMap.put(str, perfIntegerMonitor);
/* 245 */     this.overflow = (IntegerMonitor)perfIntegerMonitor;
/*     */     
/* 247 */     str = "sun.perfdata.timestamp";
/* 248 */     LongBuffer longBuffer = this.prologue.modificationTimeStampBuffer();
/* 249 */     PerfLongMonitor perfLongMonitor = new PerfLongMonitor(str, Units.TICKS, Variability.MONOTONIC, false, longBuffer);
/*     */     
/* 251 */     paramMap.put(str, perfLongMonitor);
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
/*     */   protected void synchWithTarget() throws MonitorException {
/* 264 */     long l = System.currentTimeMillis() + syncWaitMs;
/*     */ 
/*     */     
/* 267 */     log("synchWithTarget: " + this.lvmid + " ");
/* 268 */     while (!this.prologue.isAccessible()) {
/*     */       
/* 270 */       log(".");
/*     */ 
/*     */       
/* 273 */       try { Thread.sleep(20L); } catch (InterruptedException interruptedException) {}
/*     */       
/* 275 */       if (System.currentTimeMillis() > l) {
/* 276 */         logln("failed: " + this.lvmid);
/* 277 */         throw new MonitorException("Could not synchronize with target");
/*     */       } 
/*     */     } 
/* 280 */     logln("success: " + this.lvmid);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Monitor getNextMonitorEntry() throws MonitorException {
/*     */     PerfStringVariableMonitor perfStringVariableMonitor;
/* 290 */     PerfLongMonitor perfLongMonitor = null;
/*     */ 
/*     */     
/* 293 */     if (this.nextEntry % 4 != 0) {
/* 294 */       throw new MonitorStructureException("Misaligned entry index: " + 
/*     */           
/* 296 */           Integer.toHexString(this.nextEntry));
/*     */     }
/*     */ 
/*     */     
/* 300 */     if (this.nextEntry < 0 || this.nextEntry > this.buffer.limit()) {
/* 301 */       throw new MonitorStructureException("Entry index out of bounds: " + 
/*     */           
/* 303 */           Integer.toHexString(this.nextEntry) + ", limit = " + 
/* 304 */           Integer.toHexString(this.buffer.limit()));
/*     */     }
/*     */ 
/*     */     
/* 308 */     if (this.nextEntry == this.buffer.limit()) {
/* 309 */       logln("getNextMonitorEntry(): nextEntry == buffer.limit(): returning");
/*     */       
/* 311 */       return null;
/*     */     } 
/*     */     
/* 314 */     this.buffer.position(this.nextEntry);
/*     */     
/* 316 */     int i = this.buffer.position();
/* 317 */     int j = this.buffer.getInt();
/*     */ 
/*     */     
/* 320 */     if (j < 0 || j > this.buffer.limit()) {
/* 321 */       throw new MonitorStructureException("Invalid entry length: entryLength = " + j + " (0x" + 
/*     */           
/* 323 */           Integer.toHexString(j) + ")");
/*     */     }
/*     */ 
/*     */     
/* 327 */     if (i + j > this.buffer.limit()) {
/* 328 */       throw new MonitorStructureException("Entry extends beyond end of buffer:  entryStart = 0x" + 
/*     */           
/* 330 */           Integer.toHexString(i) + " entryLength = 0x" + 
/* 331 */           Integer.toHexString(j) + " buffer limit = 0x" + 
/* 332 */           Integer.toHexString(this.buffer.limit()));
/*     */     }
/*     */     
/* 335 */     if (j == 0)
/*     */     {
/* 337 */       return null;
/*     */     }
/*     */ 
/*     */     
/* 341 */     int k = this.buffer.getInt();
/* 342 */     int m = this.buffer.getInt();
/* 343 */     byte b1 = this.buffer.get();
/* 344 */     byte b2 = this.buffer.get();
/* 345 */     byte b3 = this.buffer.get();
/* 346 */     byte b4 = this.buffer.get();
/* 347 */     int n = this.buffer.getInt();
/*     */     
/* 349 */     dump_entry_fixed(i, k, m, b1, b2, b3, b4, n);
/*     */ 
/*     */ 
/*     */     
/* 353 */     Units units = Units.toUnits(b3);
/* 354 */     Variability variability = Variability.toVariability(b4);
/* 355 */     TypeCode typeCode = null;
/* 356 */     boolean bool = ((b2 & 0x1) != 0) ? true : false;
/*     */     
/*     */     try {
/* 359 */       typeCode = TypeCode.toTypeCode(b1);
/*     */     }
/* 361 */     catch (IllegalArgumentException illegalArgumentException) {
/* 362 */       throw new MonitorStructureException("Illegal type code encountered: entry_offset = 0x" + 
/*     */           
/* 364 */           Integer.toHexString(this.nextEntry) + ", type_code = " + 
/* 365 */           Integer.toHexString(b1));
/*     */     } 
/*     */ 
/*     */     
/* 369 */     if (k > j) {
/* 370 */       throw new MonitorStructureException("Field extends beyond entry bounds entry_offset = 0x" + 
/*     */           
/* 372 */           Integer.toHexString(this.nextEntry) + ", name_offset = 0x" + 
/* 373 */           Integer.toHexString(k));
/*     */     }
/*     */ 
/*     */     
/* 377 */     if (n > j) {
/* 378 */       throw new MonitorStructureException("Field extends beyond entry bounds: entry_offset = 0x" + 
/*     */           
/* 380 */           Integer.toHexString(this.nextEntry) + ", data_offset = 0x" + 
/* 381 */           Integer.toHexString(n));
/*     */     }
/*     */ 
/*     */     
/* 385 */     if (variability == Variability.INVALID) {
/* 386 */       throw new MonitorDataException("Invalid variability attribute: entry_offset = 0x" + 
/*     */           
/* 388 */           Integer.toHexString(this.nextEntry) + ", variability = 0x" + 
/* 389 */           Integer.toHexString(b4));
/*     */     }
/*     */     
/* 392 */     if (units == Units.INVALID) {
/* 393 */       throw new MonitorDataException("Invalid units attribute: entry_offset = 0x" + 
/*     */           
/* 395 */           Integer.toHexString(this.nextEntry) + ", units = 0x" + 
/* 396 */           Integer.toHexString(b3));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 407 */     assert this.buffer.position() == i + k;
/* 408 */     assert n > k;
/*     */ 
/*     */     
/* 411 */     int i1 = n - k;
/*     */ 
/*     */     
/* 414 */     assert i1 < j;
/*     */ 
/*     */ 
/*     */     
/* 418 */     byte[] arrayOfByte = new byte[i1];
/* 419 */     byte b = 0;
/*     */     byte b5;
/* 421 */     while ((b5 = this.buffer.get()) != 0 && b < i1) {
/* 422 */       arrayOfByte[b++] = b5;
/*     */     }
/*     */     
/* 425 */     assert b < i1;
/*     */ 
/*     */     
/* 428 */     assert this.buffer.position() <= i + n;
/*     */ 
/*     */     
/* 431 */     String str = new String(arrayOfByte, 0, b);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 437 */     int i2 = j - n;
/*     */ 
/*     */     
/* 440 */     this.buffer.position(i + n);
/*     */     
/* 442 */     dump_entry_variable(str, this.buffer, i2);
/*     */     
/* 444 */     if (m == 0) {
/*     */       
/* 446 */       if (typeCode == TypeCode.LONG) {
/* 447 */         LongBuffer longBuffer = this.buffer.asLongBuffer();
/* 448 */         longBuffer.limit(1);
/* 449 */         perfLongMonitor = new PerfLongMonitor(str, units, variability, bool, longBuffer);
/*     */ 
/*     */       
/*     */       }
/*     */       else {
/*     */ 
/*     */         
/* 456 */         throw new MonitorTypeException("Unexpected type code encountered: entry_offset = 0x" + 
/*     */             
/* 458 */             Integer.toHexString(this.nextEntry) + ", name = " + str + ", type_code = " + typeCode + " (0x" + 
/*     */ 
/*     */             
/* 461 */             Integer.toHexString(b1) + ")");
/*     */       }
/*     */     
/*     */     }
/* 465 */     else if (typeCode == TypeCode.BYTE) {
/* 466 */       if (units != Units.STRING)
/*     */       {
/* 468 */         throw new MonitorTypeException("Unexpected vector type encounterd: entry_offset = " + 
/*     */ 
/*     */             
/* 471 */             Integer.toHexString(this.nextEntry) + ", name = " + str + ", type_code = " + typeCode + " (0x" + 
/*     */ 
/*     */             
/* 474 */             Integer.toHexString(b1) + "), units = " + units + " (0x" + 
/*     */             
/* 476 */             Integer.toHexString(b3) + ")");
/*     */       }
/*     */       
/* 479 */       ByteBuffer byteBuffer = this.buffer.slice();
/* 480 */       byteBuffer.limit(m);
/*     */       
/* 482 */       if (variability == Variability.CONSTANT) {
/* 483 */         PerfStringConstantMonitor perfStringConstantMonitor = new PerfStringConstantMonitor(str, bool, byteBuffer);
/*     */       }
/* 485 */       else if (variability == Variability.VARIABLE) {
/* 486 */         perfStringVariableMonitor = new PerfStringVariableMonitor(str, bool, byteBuffer, m - 1);
/*     */       } else {
/* 488 */         if (variability == Variability.MONOTONIC)
/*     */         {
/* 490 */           throw new MonitorDataException("Unexpected variability attribute: entry_offset = 0x" + 
/*     */ 
/*     */               
/* 493 */               Integer.toHexString(this.nextEntry) + " name = " + str + ", variability = " + variability + " (0x" + 
/*     */ 
/*     */               
/* 496 */               Integer.toHexString(b4) + ")");
/*     */         }
/*     */ 
/*     */         
/*     */         assert false;
/*     */       } 
/*     */     } else {
/* 503 */       throw new MonitorTypeException("Unexpected type code encountered: entry_offset = 0x" + 
/*     */ 
/*     */           
/* 506 */           Integer.toHexString(this.nextEntry) + ", name = " + str + ", type_code = " + typeCode + " (0x" + 
/*     */ 
/*     */           
/* 509 */           Integer.toHexString(b1) + ")");
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 514 */     this.nextEntry = i + j;
/* 515 */     return (Monitor)perfStringVariableMonitor;
/*     */   }
/*     */   
/*     */   private void dumpAll(Map<String, Monitor> paramMap, int paramInt) {}
/*     */   
/*     */   private void dump_entry_fixed(int paramInt1, int paramInt2, int paramInt3, byte paramByte1, byte paramByte2, byte paramByte3, byte paramByte4, int paramInt4) {}
/*     */   
/*     */   private void dump_entry_variable(String paramString, ByteBuffer paramByteBuffer, int paramInt) {}
/*     */   
/*     */   private void logln(String paramString) {}
/*     */   
/*     */   private void log(String paramString) {}
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\sun\jvmstat\perfdata\monitor\v2_0\PerfDataBuffer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */