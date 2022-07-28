/*     */ package sun.jvmstat.perfdata.monitor.v1_0;
/*     */ 
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.IntBuffer;
/*     */ import java.nio.LongBuffer;
/*     */ import sun.jvmstat.monitor.MonitorException;
/*     */ import sun.jvmstat.perfdata.monitor.AbstractPerfDataBufferPrologue;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class PerfDataBufferPrologue
/*     */   extends AbstractPerfDataBufferPrologue
/*     */ {
/*     */   private static final int SUPPORTED_MAJOR_VERSION = 1;
/*     */   private static final int SUPPORTED_MINOR_VERSION = 0;
/*     */   static final int PERFDATA_PROLOG_USED_OFFSET = 8;
/*     */   static final int PERFDATA_PROLOG_USED_SIZE = 4;
/*     */   static final int PERFDATA_PROLOG_OVERFLOW_OFFSET = 12;
/*     */   static final int PERFDATA_PROLOG_OVERFLOW_SIZE = 4;
/*     */   static final int PERFDATA_PROLOG_MODTIMESTAMP_OFFSET = 16;
/*     */   static final int PERFDATA_PROLOG_MODTIMESTAMP_SIZE = 8;
/*     */   static final int PERFDATA_PROLOG_SIZE = 24;
/*     */   static final String PERFDATA_BUFFER_SIZE_NAME = "sun.perfdata.size";
/*     */   static final String PERFDATA_BUFFER_USED_NAME = "sun.perfdata.used";
/*     */   static final String PERFDATA_OVERFLOW_NAME = "sun.perfdata.overflow";
/*     */   static final String PERFDATA_MODTIMESTAMP_NAME = "sun.perfdata.timestamp";
/*     */   
/*     */   public PerfDataBufferPrologue(ByteBuffer paramByteBuffer) throws MonitorException {
/*  81 */     super(paramByteBuffer);
/*  82 */     assert getMajorVersion() == 1 && getMinorVersion() == 0;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean supportsAccessible() {
/*  89 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isAccessible() {
/*  96 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getUsed() {
/* 105 */     this.byteBuffer.position(8);
/* 106 */     return this.byteBuffer.getInt();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getBufferSize() {
/* 115 */     return this.byteBuffer.capacity();
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
/*     */   public int getOverflow() {
/* 127 */     this.byteBuffer.position(12);
/* 128 */     return this.byteBuffer.getInt();
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
/*     */   public long getModificationTimeStamp() {
/* 140 */     this.byteBuffer.position(16);
/* 141 */     return this.byteBuffer.getLong();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getSize() {
/* 148 */     return 24;
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
/*     */   public IntBuffer usedBuffer() {
/* 160 */     this.byteBuffer.position(8);
/* 161 */     IntBuffer intBuffer = this.byteBuffer.asIntBuffer();
/* 162 */     intBuffer.limit(1);
/* 163 */     return intBuffer;
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
/*     */   public IntBuffer sizeBuffer() {
/* 175 */     IntBuffer intBuffer = IntBuffer.allocate(1);
/* 176 */     intBuffer.put(this.byteBuffer.capacity());
/* 177 */     return intBuffer;
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
/*     */   public IntBuffer overflowBuffer() {
/* 189 */     this.byteBuffer.position(12);
/* 190 */     IntBuffer intBuffer = this.byteBuffer.asIntBuffer();
/* 191 */     intBuffer.limit(1);
/* 192 */     return intBuffer;
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
/*     */   public LongBuffer modificationTimeStampBuffer() {
/* 204 */     this.byteBuffer.position(16);
/* 205 */     LongBuffer longBuffer = this.byteBuffer.asLongBuffer();
/* 206 */     longBuffer.limit(1);
/* 207 */     return longBuffer;
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\sun\jvmstat\perfdata\monitor\v1_0\PerfDataBufferPrologue.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */