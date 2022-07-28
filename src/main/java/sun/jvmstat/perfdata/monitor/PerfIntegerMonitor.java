/*    */ package sun.jvmstat.perfdata.monitor;
/*    */ 
/*    */ import java.nio.IntBuffer;
/*    */ import sun.jvmstat.monitor.AbstractMonitor;
/*    */ import sun.jvmstat.monitor.IntegerMonitor;
/*    */ import sun.jvmstat.monitor.Units;
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
/*    */ public class PerfIntegerMonitor
/*    */   extends AbstractMonitor
/*    */   implements IntegerMonitor
/*    */ {
/*    */   IntBuffer ib;
/*    */   
/*    */   public PerfIntegerMonitor(String paramString, Units paramUnits, Variability paramVariability, boolean paramBoolean, IntBuffer paramIntBuffer) {
/* 57 */     super(paramString, paramUnits, paramVariability, paramBoolean);
/* 58 */     this.ib = paramIntBuffer;
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
/*    */   public Object getValue() {
/* 70 */     return new Integer(this.ib.get(0));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public int intValue() {
/* 79 */     return this.ib.get(0);
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\sun\jvmstat\perfdata\monitor\PerfIntegerMonitor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */