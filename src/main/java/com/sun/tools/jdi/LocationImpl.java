/*     */ package com.sun.tools.jdi;
/*     */ 
/*     */ import com.sun.jdi.AbsentInformationException;
/*     */ import com.sun.jdi.Location;
/*     */ import com.sun.jdi.Method;
/*     */ import com.sun.jdi.ReferenceType;
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
/*     */ public class LocationImpl
/*     */   extends MirrorImpl
/*     */   implements Location
/*     */ {
/*     */   private final ReferenceTypeImpl declaringType;
/*     */   private Method method;
/*     */   private long methodRef;
/*     */   private long codeIndex;
/*  37 */   private LineInfo baseLineInfo = null;
/*  38 */   private LineInfo otherLineInfo = null;
/*     */ 
/*     */   
/*     */   LocationImpl(VirtualMachine paramVirtualMachine, Method paramMethod, long paramLong) {
/*  42 */     super(paramVirtualMachine);
/*     */     
/*  44 */     this.method = paramMethod;
/*  45 */     this.codeIndex = paramMethod.isNative() ? -1L : paramLong;
/*  46 */     this.declaringType = (ReferenceTypeImpl)paramMethod.declaringType();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   LocationImpl(VirtualMachine paramVirtualMachine, ReferenceTypeImpl paramReferenceTypeImpl, long paramLong1, long paramLong2) {
/*  56 */     super(paramVirtualMachine);
/*     */     
/*  58 */     this.method = null;
/*  59 */     this.codeIndex = paramLong2;
/*  60 */     this.declaringType = paramReferenceTypeImpl;
/*  61 */     this.methodRef = paramLong1;
/*     */   }
/*     */   
/*     */   public boolean equals(Object paramObject) {
/*  65 */     if (paramObject != null && paramObject instanceof Location) {
/*  66 */       Location location = (Location)paramObject;
/*  67 */       return (method().equals(location.method()) && 
/*  68 */         codeIndex() == location.codeIndex() && super
/*  69 */         .equals(paramObject));
/*     */     } 
/*  71 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int hashCode() {
/*  79 */     return method().hashCode() + (int)codeIndex();
/*     */   }
/*     */   
/*     */   public int compareTo(Location paramLocation) {
/*  83 */     LocationImpl locationImpl = (LocationImpl)paramLocation;
/*  84 */     int i = method().compareTo(locationImpl.method());
/*  85 */     if (i == 0) {
/*  86 */       long l = codeIndex() - locationImpl.codeIndex();
/*  87 */       if (l < 0L)
/*  88 */         return -1; 
/*  89 */       if (l > 0L) {
/*  90 */         return 1;
/*     */       }
/*  92 */       return 0;
/*     */     } 
/*  94 */     return i;
/*     */   }
/*     */   
/*     */   public ReferenceType declaringType() {
/*  98 */     return this.declaringType;
/*     */   }
/*     */   
/*     */   public Method method() {
/* 102 */     if (this.method == null) {
/* 103 */       this.method = this.declaringType.getMethodMirror(this.methodRef);
/* 104 */       if (this.method.isNative()) {
/* 105 */         this.codeIndex = -1L;
/*     */       }
/*     */     } 
/* 108 */     return this.method;
/*     */   }
/*     */   
/*     */   public long codeIndex() {
/* 112 */     method();
/* 113 */     return this.codeIndex;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   LineInfo getBaseLineInfo(SDE.Stratum paramStratum) {
/* 120 */     if (this.baseLineInfo != null) {
/* 121 */       return this.baseLineInfo;
/*     */     }
/*     */ 
/*     */     
/* 125 */     MethodImpl methodImpl = (MethodImpl)method();
/* 126 */     LineInfo lineInfo = methodImpl.codeIndexToLineInfo(paramStratum, 
/* 127 */         codeIndex());
/*     */ 
/*     */     
/* 130 */     addBaseLineInfo(lineInfo);
/*     */     
/* 132 */     return lineInfo;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   LineInfo getLineInfo(SDE.Stratum paramStratum) {
/* 139 */     if (paramStratum.isJava()) {
/* 140 */       return getBaseLineInfo(paramStratum);
/*     */     }
/*     */ 
/*     */     
/* 144 */     LineInfo lineInfo = this.otherLineInfo;
/* 145 */     if (lineInfo != null && paramStratum
/* 146 */       .id().equals(lineInfo.liStratum())) {
/* 147 */       return lineInfo;
/*     */     }
/*     */     
/* 150 */     int i = lineNumber("Java");
/*     */     
/* 152 */     SDE.LineStratum lineStratum = paramStratum.lineStratum(this.declaringType, i);
/*     */     
/* 154 */     if (lineStratum != null && lineStratum.lineNumber() != -1) {
/*     */ 
/*     */ 
/*     */       
/* 158 */       lineInfo = new StratumLineInfo(paramStratum.id(), lineStratum.lineNumber(), lineStratum.sourceName(), lineStratum.sourcePath());
/*     */     } else {
/*     */       
/* 161 */       MethodImpl methodImpl = (MethodImpl)method();
/* 162 */       lineInfo = methodImpl.codeIndexToLineInfo(paramStratum, 
/* 163 */           codeIndex());
/*     */     } 
/*     */ 
/*     */     
/* 167 */     addStratumLineInfo(lineInfo);
/*     */     
/* 169 */     return lineInfo;
/*     */   }
/*     */   
/*     */   void addStratumLineInfo(LineInfo paramLineInfo) {
/* 173 */     this.otherLineInfo = paramLineInfo;
/*     */   }
/*     */   
/*     */   void addBaseLineInfo(LineInfo paramLineInfo) {
/* 177 */     this.baseLineInfo = paramLineInfo;
/*     */   }
/*     */   
/*     */   public String sourceName() throws AbsentInformationException {
/* 181 */     return sourceName(this.vm.getDefaultStratum());
/*     */   }
/*     */ 
/*     */   
/*     */   public String sourceName(String paramString) throws AbsentInformationException {
/* 186 */     return sourceName(this.declaringType.stratum(paramString));
/*     */   }
/*     */ 
/*     */   
/*     */   String sourceName(SDE.Stratum paramStratum) throws AbsentInformationException {
/* 191 */     return getLineInfo(paramStratum).liSourceName();
/*     */   }
/*     */   
/*     */   public String sourcePath() throws AbsentInformationException {
/* 195 */     return sourcePath(this.vm.getDefaultStratum());
/*     */   }
/*     */ 
/*     */   
/*     */   public String sourcePath(String paramString) throws AbsentInformationException {
/* 200 */     return sourcePath(this.declaringType.stratum(paramString));
/*     */   }
/*     */ 
/*     */   
/*     */   String sourcePath(SDE.Stratum paramStratum) throws AbsentInformationException {
/* 205 */     return getLineInfo(paramStratum).liSourcePath();
/*     */   }
/*     */   
/*     */   public int lineNumber() {
/* 209 */     return lineNumber(this.vm.getDefaultStratum());
/*     */   }
/*     */   
/*     */   public int lineNumber(String paramString) {
/* 213 */     return lineNumber(this.declaringType.stratum(paramString));
/*     */   }
/*     */   
/*     */   int lineNumber(SDE.Stratum paramStratum) {
/* 217 */     return getLineInfo(paramStratum).liLineNumber();
/*     */   }
/*     */   
/*     */   public String toString() {
/* 221 */     if (lineNumber() == -1) {
/* 222 */       return method().toString() + "+" + codeIndex();
/*     */     }
/* 224 */     return declaringType().name() + ":" + lineNumber();
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\jdi\LocationImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */