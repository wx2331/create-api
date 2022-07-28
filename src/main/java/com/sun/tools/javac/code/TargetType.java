/*     */ package com.sun.tools.javac.code;
/*     */ 
/*     */ import com.sun.tools.javac.util.Assert;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public enum TargetType
/*     */ {
/*  47 */   CLASS_TYPE_PARAMETER(0),
/*     */ 
/*     */   
/*  50 */   METHOD_TYPE_PARAMETER(1),
/*     */ 
/*     */   
/*  53 */   CLASS_EXTENDS(16),
/*     */ 
/*     */   
/*  56 */   CLASS_TYPE_PARAMETER_BOUND(17),
/*     */ 
/*     */   
/*  59 */   METHOD_TYPE_PARAMETER_BOUND(18),
/*     */ 
/*     */   
/*  62 */   FIELD(19),
/*     */ 
/*     */   
/*  65 */   METHOD_RETURN(20),
/*     */ 
/*     */   
/*  68 */   METHOD_RECEIVER(21),
/*     */ 
/*     */   
/*  71 */   METHOD_FORMAL_PARAMETER(22),
/*     */ 
/*     */   
/*  74 */   THROWS(23),
/*     */ 
/*     */   
/*  77 */   LOCAL_VARIABLE(64, true),
/*     */ 
/*     */   
/*  80 */   RESOURCE_VARIABLE(65, true),
/*     */ 
/*     */   
/*  83 */   EXCEPTION_PARAMETER(66, true),
/*     */ 
/*     */   
/*  86 */   INSTANCEOF(67, true),
/*     */ 
/*     */   
/*  89 */   NEW(68, true),
/*     */ 
/*     */   
/*  92 */   CONSTRUCTOR_REFERENCE(69, true),
/*     */ 
/*     */   
/*  95 */   METHOD_REFERENCE(70, true),
/*     */ 
/*     */   
/*  98 */   CAST(71, true),
/*     */ 
/*     */   
/* 101 */   CONSTRUCTOR_INVOCATION_TYPE_ARGUMENT(72, true),
/*     */ 
/*     */   
/* 104 */   METHOD_INVOCATION_TYPE_ARGUMENT(73, true),
/*     */ 
/*     */   
/* 107 */   CONSTRUCTOR_REFERENCE_TYPE_ARGUMENT(74, true),
/*     */ 
/*     */   
/* 110 */   METHOD_REFERENCE_TYPE_ARGUMENT(75, true),
/*     */ 
/*     */   
/* 113 */   UNKNOWN(255);
/*     */   
/*     */   private static final int MAXIMUM_TARGET_TYPE_VALUE = 75;
/*     */   
/*     */   private final int targetTypeValue;
/*     */   
/*     */   private final boolean isLocal;
/*     */   
/*     */   private static final TargetType[] targets;
/*     */ 
/*     */   
/*     */   TargetType(int paramInt1, boolean paramBoolean) {
/* 125 */     if (paramInt1 < 0 || paramInt1 > 255)
/*     */     {
/* 127 */       Assert.error("Attribute type value needs to be an unsigned byte: " + String.format("0x%02X", new Object[] { Integer.valueOf(paramInt1) })); } 
/* 128 */     this.targetTypeValue = paramInt1;
/* 129 */     this.isLocal = paramBoolean;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isLocal() {
/* 140 */     return this.isLocal;
/*     */   }
/*     */   
/*     */   public int targetTypeValue() {
/* 144 */     return this.targetTypeValue;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   static {
/* 150 */     targets = new TargetType[76];
/* 151 */     TargetType[] arrayOfTargetType = values();
/* 152 */     for (TargetType targetType : arrayOfTargetType) {
/* 153 */       if (targetType.targetTypeValue != UNKNOWN.targetTypeValue)
/* 154 */         targets[targetType.targetTypeValue] = targetType; 
/*     */     } 
/* 156 */     for (byte b = 0; b <= 75; b++) {
/* 157 */       if (targets[b] == null)
/* 158 */         targets[b] = UNKNOWN; 
/*     */     } 
/*     */   }
/*     */   
/*     */   public static boolean isValidTargetTypeValue(int paramInt) {
/* 163 */     if (paramInt == UNKNOWN.targetTypeValue) {
/* 164 */       return true;
/*     */     }
/* 166 */     return (paramInt >= 0 && paramInt < targets.length);
/*     */   }
/*     */   
/*     */   public static TargetType fromTargetTypeValue(int paramInt) {
/* 170 */     if (paramInt == UNKNOWN.targetTypeValue) {
/* 171 */       return UNKNOWN;
/*     */     }
/* 173 */     if (paramInt < 0 || paramInt >= targets.length)
/* 174 */       Assert.error("Unknown TargetType: " + paramInt); 
/* 175 */     return targets[paramInt];
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\javac\code\TargetType.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */