/*     */ package com.sun.tools.attach.spi;
/*     */ 
/*     */ import com.sun.tools.attach.AttachNotSupportedException;
/*     */ import com.sun.tools.attach.AttachPermission;
/*     */ import com.sun.tools.attach.VirtualMachine;
/*     */ import com.sun.tools.attach.VirtualMachineDescriptor;
/*     */ import java.io.IOException;
/*     */ import java.security.Permission;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.ServiceLoader;
/*     */ import jdk.Exported;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Exported
/*     */ public abstract class AttachProvider
/*     */ {
/*  80 */   private static final Object lock = new Object();
/*  81 */   private static List<AttachProvider> providers = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected AttachProvider() {
/*  92 */     SecurityManager securityManager = System.getSecurityManager();
/*  93 */     if (securityManager != null) {
/*  94 */       securityManager.checkPermission((Permission)new AttachPermission("createAttachProvider"));
/*     */     }
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract String name();
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
/*     */   public abstract String type();
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
/*     */   public abstract VirtualMachine attachVirtualMachine(String paramString) throws AttachNotSupportedException, IOException;
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
/*     */   public VirtualMachine attachVirtualMachine(VirtualMachineDescriptor paramVirtualMachineDescriptor) throws AttachNotSupportedException, IOException {
/* 191 */     if (paramVirtualMachineDescriptor.provider() != this) {
/* 192 */       throw new AttachNotSupportedException("provider mismatch");
/*     */     }
/* 194 */     return attachVirtualMachine(paramVirtualMachineDescriptor.id());
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
/*     */   public abstract List<VirtualMachineDescriptor> listVirtualMachines();
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
/*     */   public static List<AttachProvider> providers() {
/* 248 */     synchronized (lock) {
/* 249 */       if (providers == null) {
/* 250 */         providers = new ArrayList<>();
/*     */ 
/*     */         
/* 253 */         ServiceLoader<AttachProvider> serviceLoader = ServiceLoader.load(AttachProvider.class, AttachProvider.class
/* 254 */             .getClassLoader());
/*     */         
/* 256 */         Iterator<AttachProvider> iterator = serviceLoader.iterator();
/*     */         
/* 258 */         while (iterator.hasNext()) {
/*     */           try {
/* 260 */             providers.add(iterator.next());
/* 261 */           } catch (Throwable throwable) {
/* 262 */             if (throwable instanceof ThreadDeath) {
/* 263 */               ThreadDeath threadDeath = (ThreadDeath)throwable;
/* 264 */               throw threadDeath;
/*     */             } 
/*     */             
/* 267 */             System.err.println(throwable);
/*     */           } 
/*     */         } 
/*     */       } 
/* 271 */       return Collections.unmodifiableList(providers);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\attach\spi\AttachProvider.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */