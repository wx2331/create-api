/*     */ package com.sun.tools.jdi;
/*     */ 
/*     */ import com.sun.jdi.JDIPermission;
/*     */ import com.sun.jdi.VMDisconnectedException;
/*     */ import com.sun.jdi.VirtualMachine;
/*     */ import com.sun.jdi.VirtualMachineManager;
/*     */ import com.sun.jdi.connect.AttachingConnector;
/*     */ import com.sun.jdi.connect.Connector;
/*     */ import com.sun.jdi.connect.LaunchingConnector;
/*     */ import com.sun.jdi.connect.ListeningConnector;
/*     */ import com.sun.jdi.connect.spi.Connection;
/*     */ import com.sun.jdi.connect.spi.TransportService;
/*     */ import java.io.IOException;
/*     */ import java.security.Permission;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.ResourceBundle;
/*     */ import java.util.ServiceLoader;
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
/*     */ public class VirtualMachineManagerImpl
/*     */   implements VirtualMachineManagerService
/*     */ {
/*  44 */   private List<Connector> connectors = new ArrayList<>();
/*  45 */   private LaunchingConnector defaultConnector = null;
/*  46 */   private List<VirtualMachine> targets = new ArrayList<>();
/*     */   private final ThreadGroup mainGroupForJDI;
/*  48 */   private ResourceBundle messages = null;
/*  49 */   private int vmSequenceNumber = 0;
/*     */   
/*     */   private static final int majorVersion = 1;
/*     */   private static final int minorVersion = 8;
/*  53 */   private static final Object lock = new Object();
/*     */   private static VirtualMachineManagerImpl vmm;
/*     */   
/*     */   public static VirtualMachineManager virtualMachineManager() {
/*  57 */     SecurityManager securityManager = System.getSecurityManager();
/*  58 */     if (securityManager != null) {
/*  59 */       JDIPermission jDIPermission = new JDIPermission("virtualMachineManager");
/*     */       
/*  61 */       securityManager.checkPermission((Permission)jDIPermission);
/*     */     } 
/*  63 */     synchronized (lock) {
/*  64 */       if (vmm == null) {
/*  65 */         vmm = new VirtualMachineManagerImpl();
/*     */       }
/*     */     } 
/*  68 */     return vmm;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected VirtualMachineManagerImpl() {
/*  76 */     ThreadGroup threadGroup1 = Thread.currentThread().getThreadGroup();
/*  77 */     ThreadGroup threadGroup2 = null;
/*  78 */     while ((threadGroup2 = threadGroup1.getParent()) != null) {
/*  79 */       threadGroup1 = threadGroup2;
/*     */     }
/*  81 */     this.mainGroupForJDI = new ThreadGroup(threadGroup1, "JDI main");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  87 */     ServiceLoader<Connector> serviceLoader = ServiceLoader.load(Connector.class, Connector.class.getClassLoader());
/*     */     
/*  89 */     Iterator<Connector> iterator = serviceLoader.iterator();
/*     */     
/*  91 */     while (iterator.hasNext()) {
/*     */       Connector connector;
/*     */       
/*     */       try {
/*  95 */         connector = iterator.next();
/*  96 */       } catch (ThreadDeath threadDeath) {
/*  97 */         throw threadDeath;
/*  98 */       } catch (Exception exception) {
/*  99 */         System.err.println(exception);
/*     */         continue;
/* 101 */       } catch (Error error) {
/* 102 */         System.err.println(error);
/*     */         
/*     */         continue;
/*     */       } 
/* 106 */       addConnector(connector);
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 114 */     ServiceLoader<TransportService> serviceLoader1 = ServiceLoader.load(TransportService.class, TransportService.class
/* 115 */         .getClassLoader());
/*     */ 
/*     */     
/* 118 */     Iterator<TransportService> iterator1 = serviceLoader1.iterator();
/*     */     
/* 120 */     while (iterator1.hasNext()) {
/*     */       TransportService transportService;
/*     */       
/*     */       try {
/* 124 */         transportService = iterator1.next();
/* 125 */       } catch (ThreadDeath threadDeath) {
/* 126 */         throw threadDeath;
/* 127 */       } catch (Exception exception) {
/* 128 */         System.err.println(exception);
/*     */         continue;
/* 130 */       } catch (Error error) {
/* 131 */         System.err.println(error);
/*     */         
/*     */         continue;
/*     */       } 
/* 135 */       addConnector(GenericAttachingConnector.create(transportService));
/* 136 */       addConnector(GenericListeningConnector.create(transportService));
/*     */     } 
/*     */ 
/*     */     
/* 140 */     if (allConnectors().size() == 0) {
/* 141 */       throw new Error("no Connectors loaded");
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 149 */     boolean bool = false;
/* 150 */     List<LaunchingConnector> list = launchingConnectors();
/* 151 */     for (LaunchingConnector launchingConnector : list) {
/* 152 */       if (launchingConnector.name().equals("com.sun.jdi.CommandLineLaunch")) {
/* 153 */         setDefaultConnector(launchingConnector);
/* 154 */         bool = true;
/*     */         break;
/*     */       } 
/*     */     } 
/* 158 */     if (!bool && list.size() > 0) {
/* 159 */       setDefaultConnector(list.get(0));
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public LaunchingConnector defaultConnector() {
/* 165 */     if (this.defaultConnector == null) {
/* 166 */       throw new Error("no default LaunchingConnector");
/*     */     }
/* 168 */     return this.defaultConnector;
/*     */   }
/*     */   
/*     */   public void setDefaultConnector(LaunchingConnector paramLaunchingConnector) {
/* 172 */     this.defaultConnector = paramLaunchingConnector;
/*     */   }
/*     */   
/*     */   public List<LaunchingConnector> launchingConnectors() {
/* 176 */     ArrayList<LaunchingConnector> arrayList = new ArrayList(this.connectors.size());
/* 177 */     for (Connector connector : this.connectors) {
/* 178 */       if (connector instanceof LaunchingConnector) {
/* 179 */         arrayList.add((LaunchingConnector)connector);
/*     */       }
/*     */     } 
/* 182 */     return Collections.unmodifiableList(arrayList);
/*     */   }
/*     */   
/*     */   public List<AttachingConnector> attachingConnectors() {
/* 186 */     ArrayList<AttachingConnector> arrayList = new ArrayList(this.connectors.size());
/* 187 */     for (Connector connector : this.connectors) {
/* 188 */       if (connector instanceof AttachingConnector) {
/* 189 */         arrayList.add((AttachingConnector)connector);
/*     */       }
/*     */     } 
/* 192 */     return Collections.unmodifiableList(arrayList);
/*     */   }
/*     */   
/*     */   public List<ListeningConnector> listeningConnectors() {
/* 196 */     ArrayList<ListeningConnector> arrayList = new ArrayList(this.connectors.size());
/* 197 */     for (Connector connector : this.connectors) {
/* 198 */       if (connector instanceof ListeningConnector) {
/* 199 */         arrayList.add((ListeningConnector)connector);
/*     */       }
/*     */     } 
/* 202 */     return Collections.unmodifiableList(arrayList);
/*     */   }
/*     */   
/*     */   public List<Connector> allConnectors() {
/* 206 */     return Collections.unmodifiableList(this.connectors);
/*     */   }
/*     */   
/*     */   public List<VirtualMachine> connectedVirtualMachines() {
/* 210 */     return Collections.unmodifiableList(this.targets);
/*     */   }
/*     */   
/*     */   public void addConnector(Connector paramConnector) {
/* 214 */     this.connectors.add(paramConnector);
/*     */   }
/*     */   
/*     */   public void removeConnector(Connector paramConnector) {
/* 218 */     this.connectors.remove(paramConnector);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized VirtualMachine createVirtualMachine(Connection paramConnection, Process paramProcess) throws IOException {
/*     */     VirtualMachineImpl virtualMachineImpl;
/* 225 */     if (!paramConnection.isOpen()) {
/* 226 */       throw new IllegalStateException("connection is not open");
/*     */     }
/*     */ 
/*     */     
/*     */     try {
/* 231 */       virtualMachineImpl = new VirtualMachineImpl(this, paramConnection, paramProcess, ++this.vmSequenceNumber);
/*     */     }
/* 233 */     catch (VMDisconnectedException vMDisconnectedException) {
/* 234 */       throw new IOException(vMDisconnectedException.getMessage());
/*     */     } 
/* 236 */     this.targets.add(virtualMachineImpl);
/* 237 */     return (VirtualMachine)virtualMachineImpl;
/*     */   }
/*     */   
/*     */   public VirtualMachine createVirtualMachine(Connection paramConnection) throws IOException {
/* 241 */     return createVirtualMachine(paramConnection, null);
/*     */   }
/*     */   
/*     */   public void addVirtualMachine(VirtualMachine paramVirtualMachine) {
/* 245 */     this.targets.add(paramVirtualMachine);
/*     */   }
/*     */   
/*     */   void disposeVirtualMachine(VirtualMachine paramVirtualMachine) {
/* 249 */     this.targets.remove(paramVirtualMachine);
/*     */   }
/*     */   
/*     */   public int majorInterfaceVersion() {
/* 253 */     return 1;
/*     */   }
/*     */   
/*     */   public int minorInterfaceVersion() {
/* 257 */     return 8;
/*     */   }
/*     */   
/*     */   ThreadGroup mainGroupForJDI() {
/* 261 */     return this.mainGroupForJDI;
/*     */   }
/*     */   
/*     */   String getString(String paramString) {
/* 265 */     if (this.messages == null) {
/* 266 */       this.messages = ResourceBundle.getBundle("com.sun.tools.jdi.resources.jdi");
/*     */     }
/* 268 */     return this.messages.getString(paramString);
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\jdi\VirtualMachineManagerImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */