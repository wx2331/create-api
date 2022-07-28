/*    */ package com.sun.tools.internal.ws.resources;
/*    */ 
/*    */ import com.sun.istack.internal.localization.Localizable;
/*    */ import com.sun.istack.internal.localization.LocalizableMessageFactory;
/*    */ import com.sun.istack.internal.localization.Localizer;
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
/*    */ public final class JavacompilerMessages
/*    */ {
/* 39 */   private static final LocalizableMessageFactory messageFactory = new LocalizableMessageFactory("com.sun.tools.internal.ws.resources.javacompiler");
/* 40 */   private static final Localizer localizer = new Localizer();
/*    */   
/*    */   public static Localizable localizableJAVACOMPILER_CLASSPATH_ERROR(Object arg0) {
/* 43 */     return messageFactory.getMessage("javacompiler.classpath.error", new Object[] { arg0 });
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static String JAVACOMPILER_CLASSPATH_ERROR(Object arg0) {
/* 51 */     return localizer.localize(localizableJAVACOMPILER_CLASSPATH_ERROR(arg0));
/*    */   }
/*    */   
/*    */   public static Localizable localizableJAVACOMPILER_NOSUCHMETHOD_ERROR(Object arg0) {
/* 55 */     return messageFactory.getMessage("javacompiler.nosuchmethod.error", new Object[] { arg0 });
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static String JAVACOMPILER_NOSUCHMETHOD_ERROR(Object arg0) {
/* 63 */     return localizer.localize(localizableJAVACOMPILER_NOSUCHMETHOD_ERROR(arg0));
/*    */   }
/*    */   
/*    */   public static Localizable localizableJAVACOMPILER_ERROR(Object arg0) {
/* 67 */     return messageFactory.getMessage("javacompiler.error", new Object[] { arg0 });
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static String JAVACOMPILER_ERROR(Object arg0) {
/* 75 */     return localizer.localize(localizableJAVACOMPILER_ERROR(arg0));
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\ws\resources\JavacompilerMessages.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */