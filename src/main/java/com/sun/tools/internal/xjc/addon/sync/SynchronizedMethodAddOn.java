/*    */ package com.sun.tools.internal.xjc.addon.sync;
/*    */ 
/*    */ import com.sun.codemodel.internal.JMethod;
/*    */ import com.sun.tools.internal.xjc.BadCommandLineException;
/*    */ import com.sun.tools.internal.xjc.Options;
/*    */ import com.sun.tools.internal.xjc.Plugin;
/*    */ import com.sun.tools.internal.xjc.outline.ClassOutline;
/*    */ import com.sun.tools.internal.xjc.outline.Outline;
/*    */ import java.io.IOException;
/*    */ import org.xml.sax.ErrorHandler;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ public class SynchronizedMethodAddOn
/*    */   extends Plugin
/*    */ {
/*    */   public String getOptionName() {
/* 48 */     return "Xsync-methods";
/*    */   }
/*    */   
/*    */   public String getUsage() {
/* 52 */     return "  -Xsync-methods     :  generate accessor methods with the 'synchronized' keyword";
/*    */   }
/*    */   
/*    */   public int parseArgument(Options opt, String[] args, int i) throws BadCommandLineException, IOException {
/* 56 */     return 0;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean run(Outline model, Options opt, ErrorHandler errorHandler) {
/* 61 */     for (ClassOutline co : model.getClasses()) {
/* 62 */       augument(co);
/*    */     }
/* 64 */     return true;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private void augument(ClassOutline co) {
/* 71 */     for (JMethod m : co.implClass.methods())
/* 72 */       m.getMods().setSynchronized(true); 
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\xjc\addon\sync\SynchronizedMethodAddOn.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */