/*     */ package com.sun.tools.corba.se.idl.toJavaPortable;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class NameModifierImpl
/*     */   implements NameModifier
/*     */ {
/*     */   private String prefix;
/*     */   private String suffix;
/*     */   
/*     */   public NameModifierImpl() {
/*  36 */     this.prefix = null;
/*  37 */     this.suffix = null;
/*     */   }
/*     */ 
/*     */   
/*     */   public NameModifierImpl(String paramString1, String paramString2) {
/*  42 */     this.prefix = paramString1;
/*  43 */     this.suffix = paramString2;
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
/*     */   public NameModifierImpl(String paramString) {
/*  57 */     int i = paramString.indexOf('%');
/*  58 */     int j = paramString.lastIndexOf('%');
/*     */     
/*  60 */     if (i != j) {
/*  61 */       throw new IllegalArgumentException(
/*  62 */           Util.getMessage("NameModifier.TooManyPercent"));
/*     */     }
/*  64 */     if (i == -1) {
/*  65 */       throw new IllegalArgumentException(
/*  66 */           Util.getMessage("NameModifier.NoPercent"));
/*     */     }
/*  68 */     for (byte b = 0; b < paramString.length(); b++) {
/*  69 */       char c = paramString.charAt(b);
/*  70 */       if (invalidChar(c, (b == 0))) {
/*  71 */         char[] arrayOfChar = { c };
/*  72 */         throw new IllegalArgumentException(
/*  73 */             Util.getMessage("NameModifier.InvalidChar", new String(arrayOfChar)));
/*     */       } 
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/*  79 */     this.prefix = paramString.substring(0, i);
/*  80 */     this.suffix = paramString.substring(i + 1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean invalidChar(char paramChar, boolean paramBoolean) {
/*  89 */     if ('A' <= paramChar && paramChar <= 'Z')
/*  90 */       return false; 
/*  91 */     if ('a' <= paramChar && paramChar <= 'z')
/*  92 */       return false; 
/*  93 */     if ('0' <= paramChar && paramChar <= '9')
/*  94 */       return paramBoolean; 
/*  95 */     if (paramChar == '%')
/*  96 */       return false; 
/*  97 */     if (paramChar == '$')
/*  98 */       return false; 
/*  99 */     if (paramChar == '_') {
/* 100 */       return false;
/*     */     }
/* 102 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public String makeName(String paramString) {
/* 107 */     StringBuffer stringBuffer = new StringBuffer();
/*     */     
/* 109 */     if (this.prefix != null) {
/* 110 */       stringBuffer.append(this.prefix);
/*     */     }
/* 112 */     stringBuffer.append(paramString);
/*     */     
/* 114 */     if (this.suffix != null) {
/* 115 */       stringBuffer.append(this.suffix);
/*     */     }
/* 117 */     return stringBuffer.toString();
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\corba\se\idl\toJavaPortable\NameModifierImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */