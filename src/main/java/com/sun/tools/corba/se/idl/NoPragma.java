/*    */ package com.sun.tools.corba.se.idl;
/*    */ 
/*    */ import java.io.IOException;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ class NoPragma
/*    */   extends PragmaHandler
/*    */ {
/*    */   public boolean process(String paramString1, String paramString2) throws IOException {
/* 46 */     parseException(Util.getMessage("Preprocessor.unknownPragma", paramString1));
/* 47 */     skipToEOL();
/* 48 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\corba\se\idl\NoPragma.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */