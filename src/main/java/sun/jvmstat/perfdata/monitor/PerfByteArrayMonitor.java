/*     */ package sun.jvmstat.perfdata.monitor;
/*     */ 
/*     */ import java.nio.ByteBuffer;
/*     */ import sun.jvmstat.monitor.AbstractMonitor;
/*     */ import sun.jvmstat.monitor.ByteArrayMonitor;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class PerfByteArrayMonitor
/*     */   extends AbstractMonitor
/*     */   implements ByteArrayMonitor
/*     */ {
/*     */   ByteBuffer bb;
/*     */   
/*     */   public PerfByteArrayMonitor(String paramString, Units paramUnits, Variability paramVariability, boolean paramBoolean, ByteBuffer paramByteBuffer, int paramInt) {
/*  64 */     super(paramString, paramUnits, paramVariability, paramBoolean, paramInt);
/*  65 */     this.bb = paramByteBuffer;
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
/*     */   public Object getValue() {
/*  78 */     return byteArrayValue();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public byte[] byteArrayValue() {
/*  88 */     this.bb.position(0);
/*  89 */     byte[] arrayOfByte = new byte[this.bb.limit()];
/*     */ 
/*     */     
/*  92 */     this.bb.get(arrayOfByte);
/*     */     
/*  94 */     return arrayOfByte;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public byte byteAt(int paramInt) {
/* 104 */     this.bb.position(paramInt);
/* 105 */     return this.bb.get();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getMaximumLength() {
/* 114 */     return this.bb.limit();
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\sun\jvmstat\perfdata\monitor\PerfByteArrayMonitor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */