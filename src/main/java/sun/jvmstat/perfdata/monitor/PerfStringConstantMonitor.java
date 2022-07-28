/*    */ package sun.jvmstat.perfdata.monitor;
/*    */ 
/*    */ import java.nio.ByteBuffer;
/*    */ import sun.jvmstat.monitor.Variability;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class PerfStringConstantMonitor
/*    */   extends PerfStringMonitor
/*    */ {
/*    */   String data;
/*    */   
/*    */   public PerfStringConstantMonitor(String paramString, boolean paramBoolean, ByteBuffer paramByteBuffer) {
/* 57 */     super(paramString, Variability.CONSTANT, paramBoolean, paramByteBuffer);
/* 58 */     this.data = super.stringValue();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Object getValue() {
/* 65 */     return this.data;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String stringValue() {
/* 72 */     return this.data;
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\sun\jvmstat\perfdata\monitor\PerfStringConstantMonitor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */