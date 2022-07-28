/*     */ package sun.tools.jcmd;
/*     */ 
/*     */ import com.sun.tools.attach.AttachNotSupportedException;
/*     */ import com.sun.tools.attach.AttachOperationFailedException;
/*     */ import com.sun.tools.attach.VirtualMachine;
/*     */ import com.sun.tools.attach.VirtualMachineDescriptor;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.net.URISyntaxException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Comparator;
/*     */ import java.util.List;
/*     */ import sun.jvmstat.monitor.Monitor;
/*     */ import sun.jvmstat.monitor.MonitorException;
/*     */ import sun.jvmstat.monitor.MonitoredHost;
/*     */ import sun.jvmstat.monitor.MonitoredVm;
/*     */ import sun.jvmstat.monitor.MonitoredVmUtil;
/*     */ import sun.jvmstat.monitor.VmIdentifier;
/*     */ import sun.tools.attach.HotSpotVirtualMachine;
/*     */ import sun.tools.jstat.JStatLogger;
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
/*     */ public class JCmd
/*     */ {
/*     */   public static void main(String[] paramArrayOfString) {
/*  53 */     Arguments arguments = null;
/*     */     try {
/*  55 */       arguments = new Arguments(paramArrayOfString);
/*  56 */     } catch (IllegalArgumentException illegalArgumentException) {
/*  57 */       System.err.println("Error parsing arguments: " + illegalArgumentException.getMessage() + "\n");
/*     */       
/*  59 */       Arguments.usage();
/*  60 */       System.exit(1);
/*     */     } 
/*     */     
/*  63 */     if (arguments.isShowUsage()) {
/*  64 */       Arguments.usage();
/*  65 */       System.exit(1);
/*     */     } 
/*     */     
/*  68 */     if (arguments.isListProcesses()) {
/*  69 */       List list = VirtualMachine.list();
/*  70 */       for (VirtualMachineDescriptor virtualMachineDescriptor : list) {
/*  71 */         System.out.println(virtualMachineDescriptor.id() + " " + virtualMachineDescriptor.displayName());
/*     */       }
/*  73 */       System.exit(0);
/*     */     } 
/*     */     
/*  76 */     ArrayList<String> arrayList = new ArrayList();
/*  77 */     if (arguments.getPid() == 0) {
/*     */       
/*  79 */       List list = VirtualMachine.list();
/*  80 */       for (VirtualMachineDescriptor virtualMachineDescriptor : list) {
/*  81 */         if (!isJCmdProcess(virtualMachineDescriptor)) {
/*  82 */           arrayList.add(virtualMachineDescriptor.id());
/*     */         }
/*     */       } 
/*  85 */     } else if (arguments.getProcessSubstring() != null) {
/*     */       
/*  87 */       List list = VirtualMachine.list();
/*  88 */       for (VirtualMachineDescriptor virtualMachineDescriptor : list) {
/*  89 */         if (isJCmdProcess(virtualMachineDescriptor)) {
/*     */           continue;
/*     */         }
/*     */         try {
/*  93 */           String str = getMainClass(virtualMachineDescriptor);
/*  94 */           if (str != null && str
/*  95 */             .indexOf(arguments.getProcessSubstring()) != -1) {
/*  96 */             arrayList.add(virtualMachineDescriptor.id());
/*     */           }
/*  98 */         } catch (MonitorException|URISyntaxException monitorException) {
/*  99 */           if (monitorException.getMessage() != null) {
/* 100 */             System.err.println(monitorException.getMessage()); continue;
/*     */           } 
/* 102 */           Throwable throwable = monitorException.getCause();
/* 103 */           if (throwable != null && throwable.getMessage() != null) {
/* 104 */             System.err.println(throwable.getMessage()); continue;
/*     */           } 
/* 106 */           monitorException.printStackTrace();
/*     */         } 
/*     */       } 
/*     */ 
/*     */       
/* 111 */       if (arrayList.isEmpty()) {
/* 112 */         System.err.println("Could not find any processes matching : '" + arguments
/* 113 */             .getProcessSubstring() + "'");
/* 114 */         System.exit(1);
/*     */       } 
/* 116 */     } else if (arguments.getPid() == -1) {
/* 117 */       System.err.println("Invalid pid specified");
/* 118 */       System.exit(1);
/*     */     } else {
/*     */       
/* 121 */       arrayList.add(arguments.getPid() + "");
/*     */     } 
/*     */     
/* 124 */     boolean bool = true;
/* 125 */     for (String str : arrayList) {
/* 126 */       System.out.println(str + ":");
/* 127 */       if (arguments.isListCounters()) {
/* 128 */         listCounters(str); continue;
/*     */       } 
/*     */       try {
/* 131 */         executeCommandForPid(str, arguments.getCommand());
/* 132 */       } catch (AttachOperationFailedException attachOperationFailedException) {
/* 133 */         System.err.println(attachOperationFailedException.getMessage());
/* 134 */         bool = false;
/* 135 */       } catch (Exception exception) {
/* 136 */         exception.printStackTrace();
/* 137 */         bool = false;
/*     */       } 
/*     */     } 
/*     */     
/* 141 */     System.exit(bool ? 0 : 1);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static void executeCommandForPid(String paramString1, String paramString2) throws AttachNotSupportedException, IOException, UnsupportedEncodingException {
/* 147 */     VirtualMachine virtualMachine = VirtualMachine.attach(paramString1);
/*     */ 
/*     */ 
/*     */     
/* 151 */     HotSpotVirtualMachine hotSpotVirtualMachine = (HotSpotVirtualMachine)virtualMachine;
/* 152 */     String[] arrayOfString1 = paramString2.split("\\n");
/* 153 */     String[] arrayOfString2 = arrayOfString1; int i = arrayOfString2.length; byte b = 0; while (true) { if (b < i) { String str = arrayOfString2[b];
/* 154 */         if (str.trim().equals("stop")) {
/*     */           break;
/*     */         }
/* 157 */         try (InputStream null = hotSpotVirtualMachine.executeJCmd(str)) {
/*     */           
/* 159 */           byte[] arrayOfByte = new byte[256];
/*     */           
/* 161 */           boolean bool = false;
/*     */         }  }
/*     */       else
/*     */       { break; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       b++; }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 175 */     virtualMachine.detach();
/*     */   }
/*     */ 
/*     */   
/*     */   private static void listCounters(String paramString) {
/* 180 */     VmIdentifier vmIdentifier = null;
/*     */     try {
/* 182 */       vmIdentifier = new VmIdentifier(paramString);
/* 183 */     } catch (URISyntaxException uRISyntaxException) {
/* 184 */       System.err.println("Malformed VM Identifier: " + paramString);
/*     */       return;
/*     */     } 
/*     */     try {
/* 188 */       MonitoredHost monitoredHost = MonitoredHost.getMonitoredHost(vmIdentifier);
/* 189 */       MonitoredVm monitoredVm = monitoredHost.getMonitoredVm(vmIdentifier, -1);
/* 190 */       JStatLogger jStatLogger = new JStatLogger(monitoredVm);
/* 191 */       jStatLogger.printSnapShot("\\w*", new AscendingMonitorComparator(), false, true, System.out);
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 196 */       monitoredHost.detach(monitoredVm);
/* 197 */     } catch (MonitorException monitorException) {
/* 198 */       monitorException.printStackTrace();
/*     */     } 
/*     */   }
/*     */   
/*     */   private static boolean isJCmdProcess(VirtualMachineDescriptor paramVirtualMachineDescriptor) {
/*     */     try {
/* 204 */       String str = getMainClass(paramVirtualMachineDescriptor);
/* 205 */       return (str != null && str.equals(JCmd.class.getName()));
/* 206 */     } catch (URISyntaxException|MonitorException uRISyntaxException) {
/* 207 */       return false;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private static String getMainClass(VirtualMachineDescriptor paramVirtualMachineDescriptor) throws URISyntaxException, MonitorException {
/*     */     try {
/* 214 */       String str = null;
/* 215 */       VmIdentifier vmIdentifier = new VmIdentifier(paramVirtualMachineDescriptor.id());
/* 216 */       MonitoredHost monitoredHost = MonitoredHost.getMonitoredHost(vmIdentifier);
/* 217 */       MonitoredVm monitoredVm = monitoredHost.getMonitoredVm(vmIdentifier, -1);
/* 218 */       str = MonitoredVmUtil.mainClass(monitoredVm, true);
/* 219 */       monitoredHost.detach(monitoredVm);
/* 220 */       return str;
/* 221 */     } catch (NullPointerException nullPointerException) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 228 */       return null;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static class AscendingMonitorComparator
/*     */     implements Comparator<Monitor>
/*     */   {
/*     */     public int compare(Monitor param1Monitor1, Monitor param1Monitor2) {
/* 239 */       String str1 = param1Monitor1.getName();
/* 240 */       String str2 = param1Monitor2.getName();
/* 241 */       return str1.compareTo(str2);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\sun\tools\jcmd\JCmd.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */