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
/*    */ public class PerfStringVariableMonitor
/*    */   extends PerfStringMonitor
/*    */ {
/*    */   public PerfStringVariableMonitor(String paramString, boolean paramBoolean, ByteBuffer paramByteBuffer) {
/* 51 */     this(paramString, paramBoolean, paramByteBuffer, paramByteBuffer.limit());
/*    */   }
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
/*    */   public PerfStringVariableMonitor(String paramString, boolean paramBoolean, ByteBuffer paramByteBuffer, int paramInt) {
/* 66 */     super(paramString, Variability.VARIABLE, paramBoolean, paramByteBuffer, paramInt + 1);
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\sun\jvmstat\perfdata\monitor\PerfStringVariableMonitor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */