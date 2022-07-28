/*    */ package sun.tools.native2ascii.resources;
/*    */ 
/*    */ import java.util.ListResourceBundle;
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
/*    */ public class MsgNative2ascii
/*    */   extends ListResourceBundle
/*    */ {
/*    */   public Object[][] getContents() {
/* 33 */     return new Object[][] { { "err.bad.arg", "-encoding requires argument" }, { "err.cannot.read", "{0} could not be read." }, { "err.cannot.write", "{0} could not be written." }, { "usage", "Usage: native2ascii [-reverse] [-encoding encoding] [inputfile [outputfile]]" } };
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\sun\tools\native2ascii\resources\MsgNative2ascii.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */