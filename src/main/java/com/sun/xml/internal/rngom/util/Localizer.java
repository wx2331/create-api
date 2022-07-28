/*     */ package com.sun.xml.internal.rngom.util;
/*     */ 
/*     */ import java.text.MessageFormat;
/*     */ import java.util.MissingResourceException;
/*     */ import java.util.ResourceBundle;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Localizer
/*     */ {
/*     */   private final Class cls;
/*     */   private ResourceBundle bundle;
/*     */   private final Localizer parent;
/*     */   
/*     */   public Localizer(Class cls) {
/*  65 */     this(null, cls);
/*     */   }
/*     */   
/*     */   public Localizer(Localizer parent, Class cls) {
/*  69 */     this.parent = parent;
/*  70 */     this.cls = cls;
/*     */   }
/*     */   
/*     */   private String getString(String key) {
/*     */     try {
/*  75 */       return getBundle().getString(key);
/*  76 */     } catch (MissingResourceException e) {
/*     */       
/*  78 */       if (this.parent != null) {
/*  79 */         return this.parent.getString(key);
/*     */       }
/*  81 */       throw e;
/*     */     } 
/*     */   }
/*     */   
/*     */   public String message(String key) {
/*  86 */     return MessageFormat.format(getString(key), new Object[0]);
/*     */   }
/*     */   
/*     */   public String message(String key, Object arg) {
/*  90 */     return MessageFormat.format(getString(key), new Object[] { arg });
/*     */   }
/*     */ 
/*     */   
/*     */   public String message(String key, Object arg1, Object arg2) {
/*  95 */     return MessageFormat.format(getString(key), new Object[] { arg1, arg2 });
/*     */   }
/*     */ 
/*     */   
/*     */   public String message(String key, Object[] args) {
/* 100 */     return MessageFormat.format(getString(key), args);
/*     */   }
/*     */   
/*     */   private ResourceBundle getBundle() {
/* 104 */     if (this.bundle == null) {
/* 105 */       String s = this.cls.getName();
/* 106 */       int i = s.lastIndexOf('.');
/* 107 */       if (i > 0) {
/* 108 */         s = s.substring(0, i + 1);
/*     */       } else {
/* 110 */         s = "";
/* 111 */       }  this.bundle = ResourceBundle.getBundle(s + "Messages");
/*     */     } 
/* 113 */     return this.bundle;
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\xml\internal\rngo\\util\Localizer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */