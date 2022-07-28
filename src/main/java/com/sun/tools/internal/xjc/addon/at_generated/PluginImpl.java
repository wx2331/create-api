/*     */ package com.sun.tools.internal.xjc.addon.at_generated;
/*     */ 
/*     */ import com.sun.codemodel.internal.JAnnotatable;
/*     */ import com.sun.codemodel.internal.JClass;
/*     */ import com.sun.codemodel.internal.JFieldVar;
/*     */ import com.sun.codemodel.internal.JMethod;
/*     */ import com.sun.tools.internal.xjc.Driver;
/*     */ import com.sun.tools.internal.xjc.Options;
/*     */ import com.sun.tools.internal.xjc.Plugin;
/*     */ import com.sun.tools.internal.xjc.outline.ClassOutline;
/*     */ import com.sun.tools.internal.xjc.outline.EnumOutline;
/*     */ import com.sun.tools.internal.xjc.outline.Outline;
/*     */ import java.text.SimpleDateFormat;
/*     */ import java.util.Date;
/*     */ import org.xml.sax.ErrorHandler;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class PluginImpl
/*     */   extends Plugin
/*     */ {
/*     */   private JClass annotation;
/*     */   
/*     */   public String getOptionName() {
/*  52 */     return "mark-generated";
/*     */   }
/*     */   
/*     */   public String getUsage() {
/*  56 */     return "  -mark-generated    :  mark the generated code as @javax.annotation.Generated";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean run(Outline model, Options opt, ErrorHandler errorHandler) {
/*  63 */     this.annotation = model.getCodeModel().ref("javax.annotation.Generated");
/*     */     
/*  65 */     for (ClassOutline co : model.getClasses())
/*  66 */       augument(co); 
/*  67 */     for (EnumOutline eo : model.getEnums()) {
/*  68 */       augument(eo);
/*     */     }
/*     */ 
/*     */     
/*  72 */     return true;
/*     */   }
/*     */   
/*     */   private void augument(EnumOutline eo) {
/*  76 */     annotate((JAnnotatable)eo.clazz);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void augument(ClassOutline co) {
/*  83 */     annotate((JAnnotatable)co.implClass);
/*  84 */     for (JMethod m : co.implClass.methods())
/*  85 */       annotate((JAnnotatable)m); 
/*  86 */     for (JFieldVar f : co.implClass.fields().values())
/*  87 */       annotate((JAnnotatable)f); 
/*     */   }
/*     */   
/*     */   private void annotate(JAnnotatable m) {
/*  91 */     m.annotate(this.annotation)
/*  92 */       .param("value", Driver.class.getName())
/*  93 */       .param("date", getISO8601Date())
/*  94 */       .param("comments", "JAXB RI v" + Options.getBuildID());
/*     */   }
/*     */ 
/*     */   
/*  98 */   private String date = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private String getISO8601Date() {
/* 105 */     if (this.date == null) {
/* 106 */       StringBuffer tstamp = new StringBuffer();
/* 107 */       tstamp.append((new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ssZ")).format(new Date()));
/*     */ 
/*     */       
/* 110 */       tstamp.insert(tstamp.length() - 2, ':');
/* 111 */       this.date = tstamp.toString();
/*     */     } 
/* 113 */     return this.date;
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\xjc\addon\at_generated\PluginImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */