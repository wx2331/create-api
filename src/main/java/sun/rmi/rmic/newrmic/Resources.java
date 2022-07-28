/*    */ package sun.rmi.rmic.newrmic;
/*    */ 
/*    */ import java.text.MessageFormat;
/*    */ import java.util.MissingResourceException;
/*    */ import java.util.ResourceBundle;
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
/*    */ 
/*    */ 
/*    */ public final class Resources
/*    */ {
/* 43 */   private static ResourceBundle resources = null;
/* 44 */   private static ResourceBundle resourcesExt = null;
/*    */   
/*    */   static {
/*    */     try {
/* 48 */       resources = ResourceBundle.getBundle("sun.rmi.rmic.resources.rmic");
/* 49 */     } catch (MissingResourceException missingResourceException) {}
/*    */ 
/*    */ 
/*    */     
/*    */     try {
/* 54 */       resourcesExt = ResourceBundle.getBundle("sun.rmi.rmic.resources.rmicext");
/* 55 */     } catch (MissingResourceException missingResourceException) {}
/*    */   }
/*    */ 
/*    */   
/*    */   private Resources() {
/* 60 */     throw new AssertionError();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static String getText(String paramString, String... paramVarArgs) {
/* 67 */     String str = getString(paramString);
/* 68 */     if (str == null) {
/* 69 */       str = "missing resource key: key = \"" + paramString + "\", arguments = \"{0}\", \"{1}\", \"{2}\"";
/*    */     }
/*    */     
/* 72 */     return MessageFormat.format(str, (Object[])paramVarArgs);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private static String getString(String paramString) {
/* 79 */     if (resourcesExt != null) {
/*    */       try {
/* 81 */         return resourcesExt.getString(paramString);
/* 82 */       } catch (MissingResourceException missingResourceException) {}
/*    */     }
/*    */     
/* 85 */     if (resources != null) {
/*    */       try {
/* 87 */         return resources.getString(paramString);
/* 88 */       } catch (MissingResourceException missingResourceException) {
/* 89 */         return null;
/*    */       } 
/*    */     }
/* 92 */     return "missing resource bundle: key = \"" + paramString + "\", arguments = \"{0}\", \"{1}\", \"{2}\"";
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\sun\rmi\rmic\newrmic\Resources.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */