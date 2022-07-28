/*    */ package sun.jvmstat.perfdata.monitor;
/*    */ 
/*    */ import java.nio.LongBuffer;
/*    */ import sun.jvmstat.monitor.AbstractMonitor;
/*    */ import sun.jvmstat.monitor.LongMonitor;
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
/*    */ public class PerfLongMonitor
/*    */   extends AbstractMonitor
/*    */   implements LongMonitor
/*    */ {
/*    */   LongBuffer lb;
/*    */   
/*    */   public PerfLongMonitor(String paramString, Units paramUnits, Variability paramVariability, boolean paramBoolean, LongBuffer paramLongBuffer) {
/* 56 */     super(paramString, paramUnits, paramVariability, paramBoolean);
/* 57 */     this.lb = paramLongBuffer;
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
/* 69 */     return new Long(this.lb.get(0));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public long longValue() {
/* 78 */     return this.lb.get(0);
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\sun\jvmstat\perfdata\monitor\PerfLongMonitor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */