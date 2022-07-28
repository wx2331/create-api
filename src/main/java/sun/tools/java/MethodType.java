/*     */ package sun.tools.java;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class MethodType
/*     */   extends Type
/*     */ {
/*     */   Type returnType;
/*     */   Type[] argTypes;
/*     */   
/*     */   MethodType(String paramString, Type paramType, Type[] paramArrayOfType) {
/*  56 */     super(12, paramString);
/*  57 */     this.returnType = paramType;
/*  58 */     this.argTypes = paramArrayOfType;
/*     */   }
/*     */   
/*     */   public Type getReturnType() {
/*  62 */     return this.returnType;
/*     */   }
/*     */   
/*     */   public Type[] getArgumentTypes() {
/*  66 */     return this.argTypes;
/*     */   }
/*     */   
/*     */   public boolean equalArguments(Type paramType) {
/*  70 */     if (paramType.typeCode != 12) {
/*  71 */       return false;
/*     */     }
/*  73 */     MethodType methodType = (MethodType)paramType;
/*  74 */     if (this.argTypes.length != methodType.argTypes.length) {
/*  75 */       return false;
/*     */     }
/*  77 */     for (int i = this.argTypes.length - 1; i >= 0; i--) {
/*  78 */       if (this.argTypes[i] != methodType.argTypes[i]) {
/*  79 */         return false;
/*     */       }
/*     */     } 
/*  82 */     return true;
/*     */   }
/*     */   
/*     */   public int stackSize() {
/*  86 */     int i = 0;
/*  87 */     for (byte b = 0; b < this.argTypes.length; b++) {
/*  88 */       i += this.argTypes[b].stackSize();
/*     */     }
/*  90 */     return i;
/*     */   }
/*     */   
/*     */   public String typeString(String paramString, boolean paramBoolean1, boolean paramBoolean2) {
/*  94 */     StringBuffer stringBuffer = new StringBuffer();
/*  95 */     stringBuffer.append(paramString);
/*  96 */     stringBuffer.append('(');
/*  97 */     for (byte b = 0; b < this.argTypes.length; b++) {
/*  98 */       if (b > 0) {
/*  99 */         stringBuffer.append(", ");
/*     */       }
/* 101 */       stringBuffer.append(this.argTypes[b].typeString("", paramBoolean1, paramBoolean2));
/*     */     } 
/* 103 */     stringBuffer.append(')');
/*     */     
/* 105 */     return paramBoolean2 ? getReturnType().typeString(stringBuffer.toString(), paramBoolean1, paramBoolean2) : stringBuffer.toString();
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\sun\tools\java\MethodType.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */