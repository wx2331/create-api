/*     */ package sun.tools.attach;
/*     */ 
/*     */ import com.sun.tools.attach.AttachNotSupportedException;
/*     */ import com.sun.tools.attach.AttachPermission;
/*     */ import com.sun.tools.attach.VirtualMachineDescriptor;
/*     */ import com.sun.tools.attach.spi.AttachProvider;
/*     */ import java.security.Permission;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import sun.jvmstat.monitor.HostIdentifier;
/*     */ import sun.jvmstat.monitor.MonitoredHost;
/*     */ import sun.jvmstat.monitor.MonitoredVm;
/*     */ import sun.jvmstat.monitor.MonitoredVmUtil;
/*     */ import sun.jvmstat.monitor.VmIdentifier;
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
/*     */ public abstract class HotSpotAttachProvider
/*     */   extends AttachProvider
/*     */ {
/*     */   private static final String JVM_VERSION = "java.property.java.vm.version";
/*     */   
/*     */   public void checkAttachPermission() {
/*  60 */     SecurityManager securityManager = System.getSecurityManager();
/*  61 */     if (securityManager != null) {
/*  62 */       securityManager.checkPermission((Permission)new AttachPermission("attachVirtualMachine"));
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<VirtualMachineDescriptor> listVirtualMachines() {
/*     */     MonitoredHost monitoredHost;
/*     */     Set set;
/*  74 */     ArrayList<VirtualMachineDescriptor> arrayList = new ArrayList();
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/*  80 */       monitoredHost = MonitoredHost.getMonitoredHost(new HostIdentifier((String)null));
/*  81 */       set = monitoredHost.activeVms();
/*  82 */     } catch (Throwable throwable) {
/*  83 */       if (throwable instanceof ExceptionInInitializerError) {
/*  84 */         throwable = throwable.getCause();
/*     */       }
/*  86 */       if (throwable instanceof ThreadDeath) {
/*  87 */         throw (ThreadDeath)throwable;
/*     */       }
/*  89 */       if (throwable instanceof SecurityException) {
/*  90 */         return arrayList;
/*     */       }
/*  92 */       throw new InternalError(throwable);
/*     */     } 
/*     */     
/*  95 */     for (Integer integer : set) {
/*  96 */       String str1 = integer.toString();
/*  97 */       String str2 = str1;
/*  98 */       boolean bool = false;
/*  99 */       MonitoredVm monitoredVm = null;
/*     */       try {
/* 101 */         monitoredVm = monitoredHost.getMonitoredVm(new VmIdentifier(str1));
/*     */         try {
/* 103 */           bool = MonitoredVmUtil.isAttachable(monitoredVm);
/*     */           
/* 105 */           str2 = MonitoredVmUtil.commandLine(monitoredVm);
/* 106 */         } catch (Exception exception) {}
/*     */         
/* 108 */         if (bool) {
/* 109 */           arrayList.add(new HotSpotVirtualMachineDescriptor(this, str1, str2));
/*     */         }
/* 111 */       } catch (Throwable throwable) {
/* 112 */         if (throwable instanceof ThreadDeath) {
/* 113 */           throw (ThreadDeath)throwable;
/*     */         }
/*     */       } finally {
/* 116 */         if (monitoredVm != null) {
/* 117 */           monitoredVm.detach();
/*     */         }
/*     */       } 
/*     */     } 
/* 121 */     return arrayList;
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
/*     */ 
/*     */   
/*     */   void testAttachable(String paramString) throws AttachNotSupportedException {
/* 138 */     MonitoredVm monitoredVm = null;
/*     */     try {
/* 140 */       VmIdentifier vmIdentifier = new VmIdentifier(paramString);
/* 141 */       MonitoredHost monitoredHost = MonitoredHost.getMonitoredHost(vmIdentifier);
/* 142 */       monitoredVm = monitoredHost.getMonitoredVm(vmIdentifier);
/*     */       
/* 144 */       if (MonitoredVmUtil.isAttachable(monitoredVm)) {
/*     */         return;
/*     */       }
/*     */     }
/* 148 */     catch (Throwable throwable) {
/* 149 */       if (throwable instanceof ThreadDeath) {
/* 150 */         ThreadDeath threadDeath = (ThreadDeath)throwable;
/* 151 */         throw threadDeath;
/*     */       } 
/*     */       
/*     */       return;
/*     */     } finally {
/* 156 */       if (monitoredVm != null) {
/* 157 */         monitoredVm.detach();
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/* 162 */     throw new AttachNotSupportedException("The VM does not support the attach mechanism");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static class HotSpotVirtualMachineDescriptor
/*     */     extends VirtualMachineDescriptor
/*     */   {
/*     */     HotSpotVirtualMachineDescriptor(AttachProvider param1AttachProvider, String param1String1, String param1String2) {
/* 174 */       super(param1AttachProvider, param1String1, param1String2);
/*     */     }
/*     */     
/*     */     public boolean isAttachable() {
/* 178 */       return true;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\sun\tools\attach\HotSpotAttachProvider.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */