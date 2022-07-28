/*     */ package com.sun.tools.attach;
/*     */ 
/*     */ import com.sun.tools.attach.spi.AttachProvider;
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
/*     */ 
/*     */ 
/*     */ @Exported
/*     */ public class VirtualMachineDescriptor
/*     */ {
/*     */   private AttachProvider provider;
/*     */   private String id;
/*     */   private String displayName;
/*     */   private volatile int hash;
/*     */   
/*     */   public VirtualMachineDescriptor(AttachProvider paramAttachProvider, String paramString1, String paramString2) {
/*  78 */     if (paramAttachProvider == null) {
/*  79 */       throw new NullPointerException("provider cannot be null");
/*     */     }
/*  81 */     if (paramString1 == null) {
/*  82 */       throw new NullPointerException("identifier cannot be null");
/*     */     }
/*  84 */     if (paramString2 == null) {
/*  85 */       throw new NullPointerException("display name cannot be null");
/*     */     }
/*  87 */     this.provider = paramAttachProvider;
/*  88 */     this.id = paramString1;
/*  89 */     this.displayName = paramString2;
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
/*     */   public VirtualMachineDescriptor(AttachProvider paramAttachProvider, String paramString) {
/* 114 */     this(paramAttachProvider, paramString, paramString);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AttachProvider provider() {
/* 123 */     return this.provider;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String id() {
/* 132 */     return this.id;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String displayName() {
/* 141 */     return this.displayName;
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
/*     */   public int hashCode() {
/* 153 */     if (this.hash != 0) {
/* 154 */       return this.hash;
/*     */     }
/* 156 */     this.hash = this.provider.hashCode() * 127 + this.id.hashCode();
/* 157 */     return this.hash;
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
/*     */   public boolean equals(Object paramObject) {
/* 178 */     if (paramObject == this)
/* 179 */       return true; 
/* 180 */     if (!(paramObject instanceof VirtualMachineDescriptor))
/* 181 */       return false; 
/* 182 */     VirtualMachineDescriptor virtualMachineDescriptor = (VirtualMachineDescriptor)paramObject;
/* 183 */     if (virtualMachineDescriptor.provider() != provider()) {
/* 184 */       return false;
/*     */     }
/* 186 */     if (!virtualMachineDescriptor.id().equals(id())) {
/* 187 */       return false;
/*     */     }
/* 189 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 196 */     String str = this.provider.toString() + ": " + this.id;
/* 197 */     if (this.displayName != this.id) {
/* 198 */       str = str + " " + this.displayName;
/*     */     }
/* 200 */     return str;
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\attach\VirtualMachineDescriptor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */