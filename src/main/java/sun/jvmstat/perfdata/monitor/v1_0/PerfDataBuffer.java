/*     */ package sun.jvmstat.perfdata.monitor.v1_0;
/*     */ 
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.IntBuffer;
/*     */ import java.nio.LongBuffer;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Map;
/*     */ import sun.jvmstat.monitor.IntegerMonitor;
/*     */ import sun.jvmstat.monitor.LongMonitor;
/*     */ import sun.jvmstat.monitor.Monitor;
/*     */ import sun.jvmstat.monitor.MonitorException;
/*     */ import sun.jvmstat.monitor.StringMonitor;
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
/*     */ public class PerfDataBuffer
/*     */   extends PerfDataBufferImpl
/*     */ {
/*     */   private static final boolean DEBUG = false;
/*  49 */   private static final int syncWaitMs = Integer.getInteger("sun.jvmstat.perdata.syncWaitMs", 5000).intValue();
/*  50 */   private static final ArrayList EMPTY_LIST = new ArrayList(0);
/*     */   
/*     */   private static final int PERFDATA_ENTRYLENGTH_OFFSET = 0;
/*     */   
/*     */   private static final int PERFDATA_ENTRYLENGTH_SIZE = 4;
/*     */   
/*     */   private static final int PERFDATA_NAMELENGTH_OFFSET = 4;
/*     */   
/*     */   private static final int PERFDATA_NAMELENGTH_SIZE = 4;
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
/*     */   private static final int PERFDATA_DATAATTR_OFFSET = 15;
/*     */   
/*     */   private static final int PERFDATA_DATAATTR_SIZE = 1;
/*     */   
/*     */   private static final int PERFDATA_NAME_OFFSET = 16;
/*     */   
/*     */   PerfDataBufferPrologue prologue;
/*     */   
/*     */   int nextEntry;
/*     */   
/*     */   int pollForEntry;
/*     */   int perfDataItem;
/*     */   long lastModificationTime;
/*     */   int lastUsed;
/*     */   IntegerMonitor overflow;
/*     */   ArrayList<Monitor> insertedMonitors;
/*     */   
/*     */   public PerfDataBuffer(ByteBuffer paramByteBuffer, int paramInt) throws MonitorException {
/*  94 */     super(paramByteBuffer, paramInt);
/*  95 */     this.prologue = new PerfDataBufferPrologue(paramByteBuffer);
/*  96 */     this.buffer.order(this.prologue.getByteOrder());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void buildMonitorMap(Map<String, Monitor> paramMap) throws MonitorException {
/* 103 */     assert Thread.holdsLock(this);
/*     */ 
/*     */     
/* 106 */     this.buffer.rewind();
/*     */ 
/*     */     
/* 109 */     buildPseudoMonitors(paramMap);
/*     */ 
/*     */     
/* 112 */     this.buffer.position(this.prologue.getSize());
/* 113 */     this.nextEntry = this.buffer.position();
/* 114 */     this.perfDataItem = 0;
/*     */     
/* 116 */     int i = this.prologue.getUsed();
/* 117 */     long l = this.prologue.getModificationTimeStamp();
/*     */     
/* 119 */     Monitor monitor = getNextMonitorEntry();
/* 120 */     while (monitor != null) {
/* 121 */       paramMap.put(monitor.getName(), monitor);
/* 122 */       monitor = getNextMonitorEntry();
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
/* 133 */     this.lastUsed = i;
/* 134 */     this.lastModificationTime = l;
/*     */ 
/*     */     
/* 137 */     synchWithTarget(paramMap);
/*     */ 
/*     */     
/* 140 */     kludge(paramMap);
/*     */     
/* 142 */     this.insertedMonitors = new ArrayList<>(paramMap.values());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void getNewMonitors(Map<String, Monitor> paramMap) throws MonitorException {
/* 149 */     assert Thread.holdsLock(this);
/*     */     
/* 151 */     int i = this.prologue.getUsed();
/* 152 */     long l = this.prologue.getModificationTimeStamp();
/*     */     
/* 154 */     if (i > this.lastUsed || this.lastModificationTime > l) {
/*     */       
/* 156 */       this.lastUsed = i;
/* 157 */       this.lastModificationTime = l;
/*     */       
/* 159 */       Monitor monitor = getNextMonitorEntry();
/* 160 */       while (monitor != null) {
/* 161 */         String str = monitor.getName();
/*     */ 
/*     */         
/* 164 */         if (!paramMap.containsKey(str)) {
/* 165 */           paramMap.put(str, monitor);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 173 */           if (this.insertedMonitors != null) {
/* 174 */             this.insertedMonitors.add(monitor);
/*     */           }
/*     */         } 
/* 177 */         monitor = getNextMonitorEntry();
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected MonitorStatus getMonitorStatus(Map<String, Monitor> paramMap) throws MonitorException {
/* 186 */     assert Thread.holdsLock(this);
/* 187 */     assert this.insertedMonitors != null;
/*     */ 
/*     */     
/* 190 */     getNewMonitors(paramMap);
/*     */ 
/*     */     
/* 193 */     ArrayList arrayList = EMPTY_LIST;
/* 194 */     ArrayList<Monitor> arrayList1 = this.insertedMonitors;
/*     */     
/* 196 */     this.insertedMonitors = new ArrayList<>();
/* 197 */     return new MonitorStatus(arrayList1, arrayList);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void buildPseudoMonitors(Map<String, Monitor> paramMap) {
/* 204 */     PerfIntegerMonitor perfIntegerMonitor = null;
/* 205 */     String str = null;
/* 206 */     IntBuffer intBuffer = null;
/*     */     
/* 208 */     str = "sun.perfdata.majorVersion";
/* 209 */     intBuffer = this.prologue.majorVersionBuffer();
/* 210 */     perfIntegerMonitor = new PerfIntegerMonitor(str, Units.NONE, Variability.CONSTANT, false, intBuffer);
/*     */     
/* 212 */     paramMap.put(str, perfIntegerMonitor);
/*     */     
/* 214 */     str = "sun.perfdata.minorVersion";
/* 215 */     intBuffer = this.prologue.minorVersionBuffer();
/* 216 */     perfIntegerMonitor = new PerfIntegerMonitor(str, Units.NONE, Variability.CONSTANT, false, intBuffer);
/*     */     
/* 218 */     paramMap.put(str, perfIntegerMonitor);
/*     */     
/* 220 */     str = "sun.perfdata.size";
/* 221 */     intBuffer = this.prologue.sizeBuffer();
/* 222 */     perfIntegerMonitor = new PerfIntegerMonitor(str, Units.BYTES, Variability.MONOTONIC, false, intBuffer);
/*     */     
/* 224 */     paramMap.put(str, perfIntegerMonitor);
/*     */     
/* 226 */     str = "sun.perfdata.used";
/* 227 */     intBuffer = this.prologue.usedBuffer();
/* 228 */     perfIntegerMonitor = new PerfIntegerMonitor(str, Units.BYTES, Variability.MONOTONIC, false, intBuffer);
/*     */     
/* 230 */     paramMap.put(str, perfIntegerMonitor);
/*     */     
/* 232 */     str = "sun.perfdata.overflow";
/* 233 */     intBuffer = this.prologue.overflowBuffer();
/* 234 */     perfIntegerMonitor = new PerfIntegerMonitor(str, Units.BYTES, Variability.MONOTONIC, false, intBuffer);
/*     */     
/* 236 */     paramMap.put(str, perfIntegerMonitor);
/* 237 */     this.overflow = (IntegerMonitor)perfIntegerMonitor;
/*     */     
/* 239 */     str = "sun.perfdata.timestamp";
/* 240 */     LongBuffer longBuffer = this.prologue.modificationTimeStampBuffer();
/* 241 */     PerfLongMonitor perfLongMonitor = new PerfLongMonitor(str, Units.TICKS, Variability.MONOTONIC, false, longBuffer);
/*     */     
/* 243 */     paramMap.put(str, perfLongMonitor);
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
/*     */   protected void synchWithTarget(Map<String, Monitor> paramMap) throws MonitorException {
/* 262 */     long l = System.currentTimeMillis() + syncWaitMs;
/*     */     
/* 264 */     String str = "hotspot.rt.hrt.ticks";
/* 265 */     LongMonitor longMonitor = (LongMonitor)pollFor(paramMap, str, l);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 271 */     log("synchWithTarget: " + this.lvmid + " ");
/* 272 */     while (longMonitor.longValue() == 0L) {
/* 273 */       log(".");
/*     */       
/* 275 */       try { Thread.sleep(20L); } catch (InterruptedException interruptedException) {}
/*     */       
/* 277 */       if (System.currentTimeMillis() > l) {
/* 278 */         lognl("failed: " + this.lvmid);
/* 279 */         throw new MonitorException("Could Not Synchronize with target");
/*     */       } 
/*     */     } 
/* 282 */     lognl("success: " + this.lvmid);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Monitor pollFor(Map<String, Monitor> paramMap, String paramString, long paramLong) throws MonitorException {
/* 292 */     Monitor monitor = null;
/*     */     
/* 294 */     log("polling for: " + this.lvmid + "," + paramString + " ");
/*     */     
/* 296 */     this.pollForEntry = this.nextEntry;
/* 297 */     while ((monitor = paramMap.get(paramString)) == null) {
/* 298 */       log(".");
/*     */       
/* 300 */       try { Thread.sleep(20L); } catch (InterruptedException interruptedException) {}
/*     */       
/* 302 */       long l = System.currentTimeMillis();
/* 303 */       if (l > paramLong || this.overflow.intValue() > 0) {
/* 304 */         lognl("failed: " + this.lvmid + "," + paramString);
/* 305 */         dumpAll(paramMap, this.lvmid);
/* 306 */         throw new MonitorException("Could not find expected counter");
/*     */       } 
/*     */       
/* 309 */       getNewMonitors(paramMap);
/*     */     } 
/* 311 */     lognl("success: " + this.lvmid + "," + paramString);
/* 312 */     return monitor;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void kludge(Map<String, Monitor> paramMap) {
/* 321 */     if (Boolean.getBoolean("sun.jvmstat.perfdata.disableKludge")) {
/*     */       return;
/*     */     }
/*     */ 
/*     */     
/* 326 */     String str = "java.vm.version";
/* 327 */     StringMonitor stringMonitor1 = (StringMonitor)paramMap.get(str);
/* 328 */     if (stringMonitor1 == null) {
/* 329 */       stringMonitor1 = (StringMonitor)findByAlias(str);
/*     */     }
/*     */     
/* 332 */     str = "java.vm.name";
/* 333 */     StringMonitor stringMonitor2 = (StringMonitor)paramMap.get(str);
/* 334 */     if (stringMonitor2 == null) {
/* 335 */       stringMonitor2 = (StringMonitor)findByAlias(str);
/*     */     }
/*     */     
/* 338 */     str = "hotspot.vm.args";
/* 339 */     StringMonitor stringMonitor3 = (StringMonitor)paramMap.get(str);
/* 340 */     if (stringMonitor3 == null) {
/* 341 */       stringMonitor3 = (StringMonitor)findByAlias(str);
/*     */     }
/*     */     
/* 344 */     assert stringMonitor2 != null && stringMonitor1 != null && stringMonitor3 != null;
/*     */     
/* 346 */     if (stringMonitor2.stringValue().indexOf("HotSpot") >= 0 && 
/* 347 */       stringMonitor1.stringValue().startsWith("1.4.2")) {
/* 348 */       kludgeMantis(paramMap, stringMonitor3);
/*     */     }
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
/*     */   private void kludgeMantis(Map<String, Monitor> paramMap, StringMonitor paramStringMonitor) {
/* 370 */     String str = "hotspot.gc.collector.0.name";
/* 371 */     StringMonitor stringMonitor = (StringMonitor)paramMap.get(str);
/*     */     
/* 373 */     if (stringMonitor.stringValue().compareTo("PSScavenge") == 0) {
/* 374 */       boolean bool = true;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 381 */       str = "hotspot.vm.flags";
/* 382 */       StringMonitor stringMonitor1 = (StringMonitor)paramMap.get(str);
/* 383 */       String str1 = stringMonitor1.stringValue() + " " + paramStringMonitor.stringValue();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 390 */       int i = str1.lastIndexOf("+AggressiveHeap");
/* 391 */       int j = str1.lastIndexOf("-UseAdaptiveSizePolicy");
/*     */       
/* 393 */       if (i != -1) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 399 */         if (j != -1 && j > i) {
/* 400 */           bool = false;
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*     */         }
/*     */ 
/*     */ 
/*     */       
/*     */       }
/* 410 */       else if (j != -1) {
/* 411 */         bool = false;
/*     */       } 
/*     */ 
/*     */       
/* 415 */       if (bool) {
/*     */ 
/*     */ 
/*     */         
/* 419 */         String str2 = "hotspot.gc.generation.0.space.0.size";
/* 420 */         String str3 = "hotspot.gc.generation.0.space.1.size";
/* 421 */         String str4 = "hotspot.gc.generation.0.space.2.size";
/* 422 */         paramMap.remove(str2);
/* 423 */         paramMap.remove(str3);
/* 424 */         paramMap.remove(str4);
/*     */ 
/*     */         
/* 427 */         String str5 = "hotspot.gc.generation.0.capacity.max";
/* 428 */         LongMonitor longMonitor = (LongMonitor)paramMap.get(str5);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 441 */         PerfLongMonitor perfLongMonitor = null;
/*     */         
/* 443 */         LongBuffer longBuffer = LongBuffer.allocate(1);
/* 444 */         longBuffer.put(longMonitor.longValue());
/* 445 */         perfLongMonitor = new PerfLongMonitor(str2, Units.BYTES, Variability.CONSTANT, false, longBuffer);
/*     */         
/* 447 */         paramMap.put(str2, perfLongMonitor);
/*     */         
/* 449 */         perfLongMonitor = new PerfLongMonitor(str3, Units.BYTES, Variability.CONSTANT, false, longBuffer);
/*     */         
/* 451 */         paramMap.put(str3, perfLongMonitor);
/*     */         
/* 453 */         perfLongMonitor = new PerfLongMonitor(str4, Units.BYTES, Variability.CONSTANT, false, longBuffer);
/*     */         
/* 455 */         paramMap.put(str4, perfLongMonitor);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Monitor getNextMonitorEntry() throws MonitorException {
/*     */     PerfStringVariableMonitor perfStringVariableMonitor;
/* 467 */     PerfLongMonitor perfLongMonitor = null;
/*     */ 
/*     */     
/* 470 */     if (this.nextEntry % 4 != 0) {
/* 471 */       throw new MonitorStructureException("Entry index not properly aligned: " + this.nextEntry);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 476 */     if (this.nextEntry < 0 || this.nextEntry > this.buffer.limit()) {
/* 477 */       throw new MonitorStructureException("Entry index out of bounds: nextEntry = " + this.nextEntry + ", limit = " + this.buffer
/*     */           
/* 479 */           .limit());
/*     */     }
/*     */ 
/*     */     
/* 483 */     if (this.nextEntry == this.buffer.limit()) {
/* 484 */       lognl("getNextMonitorEntry(): nextEntry == buffer.limit(): returning");
/*     */       
/* 486 */       return null;
/*     */     } 
/*     */     
/* 489 */     this.buffer.position(this.nextEntry);
/*     */     
/* 491 */     int i = this.buffer.position();
/* 492 */     int j = this.buffer.getInt();
/*     */ 
/*     */     
/* 495 */     if (j < 0 || j > this.buffer.limit()) {
/* 496 */       throw new MonitorStructureException("Invalid entry length: entryLength = " + j);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 501 */     if (i + j > this.buffer.limit()) {
/* 502 */       throw new MonitorStructureException("Entry extends beyond end of buffer:  entryStart = " + i + " entryLength = " + j + " buffer limit = " + this.buffer
/*     */ 
/*     */ 
/*     */           
/* 506 */           .limit());
/*     */     }
/*     */     
/* 509 */     if (j == 0)
/*     */     {
/* 511 */       return null;
/*     */     }
/*     */     
/* 514 */     int k = this.buffer.getInt();
/* 515 */     int m = this.buffer.getInt();
/* 516 */     byte b1 = this.buffer.get();
/* 517 */     byte b2 = this.buffer.get();
/* 518 */     Units units = Units.toUnits(this.buffer.get());
/* 519 */     Variability variability = Variability.toVariability(this.buffer.get());
/* 520 */     boolean bool = ((b2 & 0x1) != 0) ? true : false;
/*     */ 
/*     */     
/* 523 */     if (k <= 0 || k > j) {
/* 524 */       throw new MonitorStructureException("Invalid Monitor name length: " + k);
/*     */     }
/*     */ 
/*     */     
/* 528 */     if (m < 0 || m > j) {
/* 529 */       throw new MonitorStructureException("Invalid Monitor vector length: " + m);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 536 */     byte[] arrayOfByte = new byte[k - 1];
/* 537 */     for (byte b = 0; b < k - 1; b++) {
/* 538 */       arrayOfByte[b] = this.buffer.get();
/*     */     }
/*     */ 
/*     */     
/* 542 */     String str = new String(arrayOfByte, 0, k - 1);
/*     */     
/* 544 */     if (variability == Variability.INVALID) {
/* 545 */       throw new MonitorDataException("Invalid variability attribute: entry index = " + this.perfDataItem + " name = " + str);
/*     */     }
/*     */ 
/*     */     
/* 549 */     if (units == Units.INVALID) {
/* 550 */       throw new MonitorDataException("Invalid units attribute:  entry index = " + this.perfDataItem + " name = " + str);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 556 */     if (m == 0) {
/*     */       
/* 558 */       if (b1 == BasicType.LONG.intValue()) {
/* 559 */         int n = i + j - 8;
/* 560 */         this.buffer.position(n);
/* 561 */         LongBuffer longBuffer = this.buffer.asLongBuffer();
/* 562 */         longBuffer.limit(1);
/* 563 */         perfLongMonitor = new PerfLongMonitor(str, units, variability, bool, longBuffer);
/* 564 */         this.perfDataItem++;
/*     */       } else {
/*     */         
/* 567 */         throw new MonitorTypeException("Invalid Monitor type: entry index = " + this.perfDataItem + " name = " + str + " type = " + b1);
/*     */ 
/*     */       
/*     */       }
/*     */ 
/*     */     
/*     */     }
/* 574 */     else if (b1 == BasicType.BYTE.intValue()) {
/* 575 */       if (units != Units.STRING)
/*     */       {
/* 577 */         throw new MonitorTypeException("Invalid Monitor type: entry index = " + this.perfDataItem + " name = " + str + " type = " + b1);
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 583 */       int n = i + 16 + k;
/* 584 */       this.buffer.position(n);
/* 585 */       ByteBuffer byteBuffer = this.buffer.slice();
/* 586 */       byteBuffer.limit(m);
/* 587 */       byteBuffer.position(0);
/*     */       
/* 589 */       if (variability == Variability.CONSTANT) {
/* 590 */         PerfStringConstantMonitor perfStringConstantMonitor = new PerfStringConstantMonitor(str, bool, byteBuffer);
/*     */       }
/* 592 */       else if (variability == Variability.VARIABLE) {
/* 593 */         perfStringVariableMonitor = new PerfStringVariableMonitor(str, bool, byteBuffer, m - 1);
/*     */       }
/*     */       else {
/*     */         
/* 597 */         throw new MonitorDataException("Invalid variability attribute: entry index = " + this.perfDataItem + " name = " + str + " variability = " + variability);
/*     */       } 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 603 */       this.perfDataItem++;
/*     */     } else {
/*     */       
/* 606 */       throw new MonitorTypeException("Invalid Monitor type: entry index = " + this.perfDataItem + " name = " + str + " type = " + b1);
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 614 */     this.nextEntry = i + j;
/* 615 */     return (Monitor)perfStringVariableMonitor;
/*     */   }
/*     */   
/*     */   private void dumpAll(Map paramMap, int paramInt) {}
/*     */   
/*     */   private void lognl(String paramString) {}
/*     */   
/*     */   private void log(String paramString) {}
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\sun\jvmstat\perfdata\monitor\v1_0\PerfDataBuffer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */