/*     */ package com.sun.tools.jdi;
/*     */ 
/*     */ import com.sun.jdi.AbsentInformationException;
/*     */ import com.sun.jdi.InternalException;
/*     */ import com.sun.jdi.LocalVariable;
/*     */ import com.sun.jdi.Location;
/*     */ import com.sun.jdi.VirtualMachine;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class NonConcreteMethodImpl
/*     */   extends MethodImpl
/*     */ {
/*  44 */   private Location location = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   NonConcreteMethodImpl(VirtualMachine paramVirtualMachine, ReferenceTypeImpl paramReferenceTypeImpl, long paramLong, String paramString1, String paramString2, String paramString3, int paramInt) {
/*  53 */     super(paramVirtualMachine, paramReferenceTypeImpl, paramLong, paramString1, paramString2, paramString3, paramInt);
/*     */   }
/*     */ 
/*     */   
/*     */   public Location location() {
/*  58 */     if (isAbstract()) {
/*  59 */       return null;
/*     */     }
/*  61 */     if (this.location == null) {
/*  62 */       this.location = new LocationImpl((VirtualMachine)this.vm, this, -1L);
/*     */     }
/*  64 */     return this.location;
/*     */   }
/*     */ 
/*     */   
/*     */   public List<Location> allLineLocations(String paramString1, String paramString2) {
/*  69 */     return new ArrayList<>(0);
/*     */   }
/*     */ 
/*     */   
/*     */   public List<Location> allLineLocations(SDE.Stratum paramStratum, String paramString) {
/*  74 */     return new ArrayList<>(0);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public List<Location> locationsOfLine(String paramString1, String paramString2, int paramInt) {
/*  80 */     return new ArrayList<>(0);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public List<Location> locationsOfLine(SDE.Stratum paramStratum, String paramString, int paramInt) {
/*  86 */     return new ArrayList<>(0);
/*     */   }
/*     */   
/*     */   public Location locationOfCodeIndex(long paramLong) {
/*  90 */     return null;
/*     */   }
/*     */   
/*     */   public List<LocalVariable> variables() throws AbsentInformationException {
/*  94 */     throw new AbsentInformationException();
/*     */   }
/*     */   
/*     */   public List<LocalVariable> variablesByName(String paramString) throws AbsentInformationException {
/*  98 */     throw new AbsentInformationException();
/*     */   }
/*     */   
/*     */   public List<LocalVariable> arguments() throws AbsentInformationException {
/* 102 */     throw new AbsentInformationException();
/*     */   }
/*     */   
/*     */   public byte[] bytecodes() {
/* 106 */     return new byte[0];
/*     */   }
/*     */   
/*     */   int argSlotCount() throws AbsentInformationException {
/* 110 */     throw new InternalException("should not get here");
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\jdi\NonConcreteMethodImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */