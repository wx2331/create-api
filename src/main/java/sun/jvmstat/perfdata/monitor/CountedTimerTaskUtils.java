/*    */ package sun.jvmstat.perfdata.monitor;
/*    */ 
/*    */ import java.util.Timer;
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
/*    */ 
/*    */ 
/*    */ public class CountedTimerTaskUtils
/*    */ {
/*    */   private static final boolean DEBUG = false;
/*    */   
/*    */   public static void reschedule(Timer paramTimer, CountedTimerTask paramCountedTimerTask1, CountedTimerTask paramCountedTimerTask2, int paramInt1, int paramInt2) {
/* 57 */     long l1 = System.currentTimeMillis();
/* 58 */     long l2 = paramCountedTimerTask1.scheduledExecutionTime();
/* 59 */     long l3 = l1 - l2;
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
/* 73 */     long l4 = 0L;
/* 74 */     if (paramCountedTimerTask1.executionCount() > 0L) {
/* 75 */       long l = paramInt2 - l3;
/* 76 */       l4 = (l >= 0L) ? l : 0L;
/*    */     } 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 85 */     paramTimer.schedule(paramCountedTimerTask2, l4, paramInt2);
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\sun\jvmstat\perfdata\monitor\CountedTimerTaskUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */