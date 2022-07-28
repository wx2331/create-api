/*     */ package com.sun.tools.jdi;
/*     */ 
/*     */ import com.sun.jdi.Mirror;
/*     */ import com.sun.jdi.VMMismatchException;
/*     */ import com.sun.jdi.VirtualMachine;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
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
/*     */ abstract class MirrorImpl
/*     */   implements Mirror
/*     */ {
/*     */   protected VirtualMachineImpl vm;
/*     */   
/*     */   MirrorImpl(VirtualMachine paramVirtualMachine) {
/*  42 */     this.vm = (VirtualMachineImpl)paramVirtualMachine;
/*     */   }
/*     */   
/*     */   public VirtualMachine virtualMachine() {
/*  46 */     return (VirtualMachine)this.vm;
/*     */   }
/*     */   
/*     */   public boolean equals(Object paramObject) {
/*  50 */     if (paramObject != null && paramObject instanceof Mirror) {
/*  51 */       Mirror mirror = (Mirror)paramObject;
/*  52 */       return this.vm.equals(mirror.virtualMachine());
/*     */     } 
/*  54 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/*  59 */     return this.vm.hashCode();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void validateMirror(Mirror paramMirror) {
/*  67 */     if (!this.vm.equals(paramMirror.virtualMachine())) {
/*  68 */       throw new VMMismatchException(paramMirror.toString());
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void validateMirrorOrNull(Mirror paramMirror) {
/*  77 */     if (paramMirror != null && !this.vm.equals(paramMirror.virtualMachine())) {
/*  78 */       throw new VMMismatchException(paramMirror.toString());
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void validateMirrors(Collection<? extends Mirror> paramCollection) {
/*  87 */     Iterator<? extends Mirror> iterator = paramCollection.iterator();
/*  88 */     while (iterator.hasNext()) {
/*  89 */       MirrorImpl mirrorImpl = (MirrorImpl)iterator.next();
/*  90 */       if (!this.vm.equals(mirrorImpl.vm)) {
/*  91 */         throw new VMMismatchException(mirrorImpl.toString());
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void validateMirrorsOrNulls(Collection<? extends Mirror> paramCollection) {
/* 100 */     Iterator<? extends Mirror> iterator = paramCollection.iterator();
/* 101 */     while (iterator.hasNext()) {
/* 102 */       MirrorImpl mirrorImpl = (MirrorImpl)iterator.next();
/* 103 */       if (mirrorImpl != null && !this.vm.equals(mirrorImpl.vm))
/* 104 */         throw new VMMismatchException(mirrorImpl.toString()); 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\jdi\MirrorImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */