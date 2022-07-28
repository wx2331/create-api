/*     */ package sun.jvmstat.perfdata.monitor;
/*     */ 
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.charset.Charset;
/*     */ import sun.jvmstat.monitor.StringMonitor;
/*     */ import sun.jvmstat.monitor.Units;
/*     */ import sun.jvmstat.monitor.Variability;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class PerfStringMonitor
/*     */   extends PerfByteArrayMonitor
/*     */   implements StringMonitor
/*     */ {
/*  41 */   private static Charset defaultCharset = Charset.defaultCharset();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PerfStringMonitor(String paramString, Variability paramVariability, boolean paramBoolean, ByteBuffer paramByteBuffer) {
/*  54 */     this(paramString, paramVariability, paramBoolean, paramByteBuffer, paramByteBuffer.limit());
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
/*     */   public PerfStringMonitor(String paramString, Variability paramVariability, boolean paramBoolean, ByteBuffer paramByteBuffer, int paramInt) {
/*  69 */     super(paramString, Units.STRING, paramVariability, paramBoolean, paramByteBuffer, paramInt);
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
/*     */   public Object getValue() {
/*  81 */     return stringValue();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String stringValue() {
/*  90 */     String str = "";
/*  91 */     byte[] arrayOfByte = byteArrayValue();
/*     */ 
/*     */     
/*  94 */     if (arrayOfByte == null || arrayOfByte.length <= 1 || arrayOfByte[0] == 0) {
/*  95 */       return str;
/*     */     }
/*     */     
/*     */     byte b;
/*  99 */     for (b = 0; b < arrayOfByte.length && arrayOfByte[b] != 0; b++);
/*     */     
/* 101 */     return new String(arrayOfByte, 0, b, defaultCharset);
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\sun\jvmstat\perfdata\monitor\PerfStringMonitor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */