/*    */ package com.sun.tools.internal.ws.wscompile.plugin.at_generated;
/*    */ 
/*    */ import com.sun.codemodel.internal.JAnnotatable;
/*    */ import com.sun.codemodel.internal.JClass;
/*    */ import com.sun.codemodel.internal.JCodeModel;
/*    */ import com.sun.codemodel.internal.JDefinedClass;
/*    */ import com.sun.codemodel.internal.JPackage;
/*    */ import com.sun.tools.internal.ws.ToolVersion;
/*    */ import com.sun.tools.internal.ws.processor.model.Model;
/*    */ import com.sun.tools.internal.ws.wscompile.ErrorReceiver;
/*    */ import com.sun.tools.internal.ws.wscompile.Plugin;
/*    */ import com.sun.tools.internal.ws.wscompile.WsimportOptions;
/*    */ import com.sun.tools.internal.ws.wscompile.WsimportTool;
/*    */ import java.text.SimpleDateFormat;
/*    */ import java.util.Date;
/*    */ import java.util.Iterator;
/*    */ import org.xml.sax.SAXException;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class PluginImpl
/*    */   extends Plugin
/*    */ {
/*    */   private JClass annotation;
/* 52 */   private String date = null;
/*    */ 
/*    */   
/*    */   public String getOptionName() {
/* 56 */     return "mark-generated";
/*    */   }
/*    */ 
/*    */   
/*    */   public String getUsage() {
/* 61 */     return "  -mark-generated    :  mark the generated code as @javax.annotation.Generated";
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean run(Model model, WsimportOptions wo, ErrorReceiver er) throws SAXException {
/* 66 */     JCodeModel cm = wo.getCodeModel();
/*    */     
/* 68 */     this.annotation = cm.ref("javax.annotation.Generated");
/*    */     
/* 70 */     for (Iterator<JPackage> i = cm.packages(); i.hasNext();) {
/* 71 */       for (Iterator<JDefinedClass> j = ((JPackage)i.next()).classes(); j.hasNext();) {
/* 72 */         annotate((JAnnotatable)j.next());
/*    */       }
/*    */     } 
/*    */     
/* 76 */     return true;
/*    */   }
/*    */   
/*    */   private void annotate(JAnnotatable m) {
/* 80 */     m.annotate(this.annotation)
/* 81 */       .param("value", WsimportTool.class.getName())
/* 82 */       .param("date", getISO8601Date())
/* 83 */       .param("comments", ToolVersion.VERSION.BUILD_VERSION);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private String getISO8601Date() {
/* 91 */     if (this.date == null) {
/* 92 */       StringBuilder tstamp = new StringBuilder();
/* 93 */       tstamp.append((new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ssZ")).format(new Date()));
/*    */ 
/*    */       
/* 96 */       tstamp.insert(tstamp.length() - 2, ':');
/* 97 */       this.date = tstamp.toString();
/*    */     } 
/* 99 */     return this.date;
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\ws\wscompile\plugin\at_generated\PluginImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */