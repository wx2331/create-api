/*     */ package com.sun.jdi;
/*     */ 
/*     */ import java.security.BasicPermission;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ public final class JDIPermission
/*     */   extends BasicPermission
/*     */ {
/*     */   private static final long serialVersionUID = -6988461416938786271L;
/*     */   
/*     */   public JDIPermission(String paramString) {
/*  91 */     super(paramString);
/*  92 */     if (!paramString.equals("virtualMachineManager")) {
/*  93 */       throw new IllegalArgumentException("name: " + paramString);
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
/*     */   public JDIPermission(String paramString1, String paramString2) throws IllegalArgumentException {
/* 106 */     super(paramString1);
/* 107 */     if (!paramString1.equals("virtualMachineManager")) {
/* 108 */       throw new IllegalArgumentException("name: " + paramString1);
/*     */     }
/* 110 */     if (paramString2 != null && paramString2.length() > 0)
/* 111 */       throw new IllegalArgumentException("actions: " + paramString2); 
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\jdi\JDIPermission.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */