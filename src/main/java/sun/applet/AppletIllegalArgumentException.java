/*    */ package sun.applet;
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
/*    */ public class AppletIllegalArgumentException
/*    */   extends IllegalArgumentException
/*    */ {
/* 35 */   private String key = null;
/*    */   
/*    */   public AppletIllegalArgumentException(String paramString) {
/* 38 */     super(paramString);
/* 39 */     this.key = paramString;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getLocalizedMessage() {
/* 44 */     return amh.getMessage(this.key);
/*    */   }
/*    */   
/* 47 */   private static AppletMessageHandler amh = new AppletMessageHandler("appletillegalargumentexception");
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\sun\applet\AppletIllegalArgumentException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */