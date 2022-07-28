/*     */ package sun.jvmstat.perfdata.monitor;
/*     */ 
/*     */ import java.util.List;
/*     */ import sun.jvmstat.monitor.Monitor;
/*     */ import sun.jvmstat.monitor.MonitorException;
/*     */ import sun.jvmstat.monitor.VmIdentifier;
/*     */ import sun.jvmstat.monitor.event.VmListener;
/*     */ import sun.jvmstat.monitor.remote.BufferedMonitoredVm;
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
/*     */ public abstract class AbstractMonitoredVm
/*     */   implements BufferedMonitoredVm
/*     */ {
/*     */   protected VmIdentifier vmid;
/*     */   protected AbstractPerfDataBuffer pdb;
/*     */   protected int interval;
/*     */   
/*     */   public AbstractMonitoredVm(VmIdentifier paramVmIdentifier, int paramInt) throws MonitorException {
/*  70 */     this.vmid = paramVmIdentifier;
/*  71 */     this.interval = paramInt;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public VmIdentifier getVmIdentifier() {
/*  78 */     return this.vmid;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Monitor findByName(String paramString) throws MonitorException {
/*  85 */     return this.pdb.findByName(paramString);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<Monitor> findByPattern(String paramString) throws MonitorException {
/*  92 */     return this.pdb.findByPattern(paramString);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void detach() {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setInterval(int paramInt) {
/* 112 */     this.interval = paramInt;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getInterval() {
/* 119 */     return this.interval;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setLastException(Exception paramException) {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Exception getLastException() {
/* 134 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void clearLastException() {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isErrored() {
/* 149 */     return false;
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
/*     */   public MonitorStatus getMonitorStatus() throws MonitorException {
/* 161 */     return this.pdb.getMonitorStatus();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract void addVmListener(VmListener paramVmListener);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract void removeVmListener(VmListener paramVmListener);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public byte[] getBytes() {
/* 184 */     return this.pdb.getBytes();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getCapacity() {
/* 191 */     return this.pdb.getCapacity();
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\sun\jvmstat\perfdata\monitor\AbstractMonitoredVm.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */