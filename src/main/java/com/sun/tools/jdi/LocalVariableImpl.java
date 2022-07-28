/*     */ package com.sun.tools.jdi;
/*     */ 
/*     */ import com.sun.jdi.AbsentInformationException;
/*     */ import com.sun.jdi.ClassNotLoadedException;
/*     */ import com.sun.jdi.InternalException;
/*     */ import com.sun.jdi.LocalVariable;
/*     */ import com.sun.jdi.Location;
/*     */ import com.sun.jdi.Method;
/*     */ import com.sun.jdi.Mirror;
/*     */ import com.sun.jdi.StackFrame;
/*     */ import com.sun.jdi.Type;
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
/*     */ public class LocalVariableImpl
/*     */   extends MirrorImpl
/*     */   implements LocalVariable, ValueContainer
/*     */ {
/*     */   private final Method method;
/*     */   private final int slot;
/*     */   private final Location scopeStart;
/*     */   private final Location scopeEnd;
/*     */   private final String name;
/*     */   private final String signature;
/*  38 */   private String genericSignature = null;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   LocalVariableImpl(VirtualMachine paramVirtualMachine, Method paramMethod, int paramInt, Location paramLocation1, Location paramLocation2, String paramString1, String paramString2, String paramString3) {
/*  44 */     super(paramVirtualMachine);
/*  45 */     this.method = paramMethod;
/*  46 */     this.slot = paramInt;
/*  47 */     this.scopeStart = paramLocation1;
/*  48 */     this.scopeEnd = paramLocation2;
/*  49 */     this.name = paramString1;
/*  50 */     this.signature = paramString2;
/*  51 */     if (paramString3 != null && paramString3.length() > 0) {
/*  52 */       this.genericSignature = paramString3;
/*     */     } else {
/*     */       
/*  55 */       this.genericSignature = null;
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean equals(Object paramObject) {
/*  60 */     if (paramObject != null && paramObject instanceof LocalVariableImpl) {
/*  61 */       LocalVariableImpl localVariableImpl = (LocalVariableImpl)paramObject;
/*  62 */       return (slot() == localVariableImpl.slot() && this.scopeStart != null && this.scopeStart
/*     */         
/*  64 */         .equals(localVariableImpl.scopeStart) && super
/*  65 */         .equals(paramObject));
/*     */     } 
/*  67 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int hashCode() {
/*  75 */     return (this.scopeStart.hashCode() << 4) + slot();
/*     */   }
/*     */   
/*     */   public int compareTo(LocalVariable paramLocalVariable) {
/*  79 */     LocalVariableImpl localVariableImpl = (LocalVariableImpl)paramLocalVariable;
/*     */     
/*  81 */     int i = this.scopeStart.compareTo(localVariableImpl.scopeStart);
/*  82 */     if (i == 0) {
/*  83 */       i = slot() - localVariableImpl.slot();
/*     */     }
/*  85 */     return i;
/*     */   }
/*     */   
/*     */   public String name() {
/*  89 */     return this.name;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String typeName() {
/*  97 */     JNITypeParser jNITypeParser = new JNITypeParser(this.signature);
/*  98 */     return jNITypeParser.typeName();
/*     */   }
/*     */   
/*     */   public Type type() throws ClassNotLoadedException {
/* 102 */     return findType(signature());
/*     */   }
/*     */   
/*     */   public Type findType(String paramString) throws ClassNotLoadedException {
/* 106 */     ReferenceTypeImpl referenceTypeImpl = (ReferenceTypeImpl)this.method.declaringType();
/* 107 */     return referenceTypeImpl.findType(paramString);
/*     */   }
/*     */   
/*     */   public String signature() {
/* 111 */     return this.signature;
/*     */   }
/*     */   
/*     */   public String genericSignature() {
/* 115 */     return this.genericSignature;
/*     */   }
/*     */   
/*     */   public boolean isVisible(StackFrame paramStackFrame) {
/* 119 */     validateMirror((Mirror)paramStackFrame);
/* 120 */     Method method = paramStackFrame.location().method();
/*     */     
/* 122 */     if (!method.equals(this.method)) {
/* 123 */       throw new IllegalArgumentException("frame method different than variable's method");
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 130 */     if (method.isNative()) {
/* 131 */       return false;
/*     */     }
/*     */     
/* 134 */     return (this.scopeStart.compareTo(paramStackFrame.location()) <= 0 && this.scopeEnd
/* 135 */       .compareTo(paramStackFrame.location()) >= 0);
/*     */   }
/*     */   
/*     */   public boolean isArgument() {
/*     */     try {
/* 140 */       MethodImpl methodImpl = (MethodImpl)this.scopeStart.method();
/* 141 */       return (this.slot < methodImpl.argSlotCount());
/* 142 */     } catch (AbsentInformationException absentInformationException) {
/*     */       
/* 144 */       throw new InternalException();
/*     */     } 
/*     */   }
/*     */   
/*     */   int slot() {
/* 149 */     return this.slot;
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
/*     */   boolean hides(LocalVariable paramLocalVariable) {
/* 165 */     LocalVariableImpl localVariableImpl = (LocalVariableImpl)paramLocalVariable;
/* 166 */     if (!this.method.equals(localVariableImpl.method) || 
/* 167 */       !this.name.equals(localVariableImpl.name)) {
/* 168 */       return false;
/*     */     }
/* 170 */     return (this.scopeStart.compareTo(localVariableImpl.scopeStart) > 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 175 */     return name() + " in " + this.method.toString() + "@" + this.scopeStart
/* 176 */       .toString();
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\jdi\LocalVariableImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */