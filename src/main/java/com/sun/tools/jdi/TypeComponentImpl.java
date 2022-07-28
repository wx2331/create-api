/*     */ package com.sun.tools.jdi;
/*     */ 
/*     */ import com.sun.jdi.ReferenceType;
/*     */ import com.sun.jdi.TypeComponent;
/*     */ import com.sun.jdi.VirtualMachine;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class TypeComponentImpl
/*     */   extends MirrorImpl
/*     */   implements TypeComponent
/*     */ {
/*     */   protected final long ref;
/*     */   protected final String name;
/*     */   protected final String signature;
/*     */   protected final String genericSignature;
/*     */   protected final ReferenceTypeImpl declaringType;
/*     */   private final int modifiers;
/*     */   
/*     */   TypeComponentImpl(VirtualMachine paramVirtualMachine, ReferenceTypeImpl paramReferenceTypeImpl, long paramLong, String paramString1, String paramString2, String paramString3, int paramInt) {
/*  47 */     super(paramVirtualMachine);
/*  48 */     this.declaringType = paramReferenceTypeImpl;
/*  49 */     this.ref = paramLong;
/*  50 */     this.name = paramString1;
/*  51 */     this.signature = paramString2;
/*  52 */     if (paramString3 != null && paramString3.length() != 0) {
/*  53 */       this.genericSignature = paramString3;
/*     */     } else {
/*  55 */       this.genericSignature = null;
/*     */     } 
/*  57 */     this.modifiers = paramInt;
/*     */   }
/*     */   
/*     */   public String name() {
/*  61 */     return this.name;
/*     */   }
/*     */   
/*     */   public String signature() {
/*  65 */     return this.signature;
/*     */   }
/*     */   public String genericSignature() {
/*  68 */     return this.genericSignature;
/*     */   }
/*     */   
/*     */   public int modifiers() {
/*  72 */     return this.modifiers;
/*     */   }
/*     */   
/*     */   public ReferenceType declaringType() {
/*  76 */     return this.declaringType;
/*     */   }
/*     */   
/*     */   public boolean isStatic() {
/*  80 */     return isModifierSet(8);
/*     */   }
/*     */   
/*     */   public boolean isFinal() {
/*  84 */     return isModifierSet(16);
/*     */   }
/*     */   
/*     */   public boolean isPrivate() {
/*  88 */     return isModifierSet(2);
/*     */   }
/*     */   
/*     */   public boolean isPackagePrivate() {
/*  92 */     return !isModifierSet(7);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isProtected() {
/*  98 */     return isModifierSet(4);
/*     */   }
/*     */   
/*     */   public boolean isPublic() {
/* 102 */     return isModifierSet(1);
/*     */   }
/*     */   
/*     */   public boolean isSynthetic() {
/* 106 */     return isModifierSet(-268435456);
/*     */   }
/*     */   
/*     */   long ref() {
/* 110 */     return this.ref;
/*     */   }
/*     */   
/*     */   boolean isModifierSet(int paramInt) {
/* 114 */     return ((this.modifiers & paramInt) != 0);
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\jdi\TypeComponentImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */