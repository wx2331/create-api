/*    */ package sun.tools.jstat;
/*    */ 
/*    */ import java.util.Comparator;
/*    */ import sun.jvmstat.monitor.Monitor;
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
/*    */ class AscendingMonitorComparator
/*    */   implements Comparator<Monitor>
/*    */ {
/*    */   public int compare(Monitor paramMonitor1, Monitor paramMonitor2) {
/* 39 */     String str1 = paramMonitor1.getName();
/* 40 */     String str2 = paramMonitor2.getName();
/* 41 */     return str1.compareTo(str2);
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\sun\tools\jstat\AscendingMonitorComparator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */