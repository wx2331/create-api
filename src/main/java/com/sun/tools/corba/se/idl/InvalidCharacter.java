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
/*    */ 
/*    */ public class InvalidCharacter
/*    */   extends IOException
/*    */ {
/*    */   public InvalidCharacter(String paramString1, String paramString2, int paramInt1, int paramInt2, char paramChar) {
/* 47 */     String str = "^";
/* 48 */     if (paramInt2 > 1) {
/*    */       
/* 50 */       byte[] arrayOfByte = new byte[paramInt2 - 1];
/* 51 */       for (byte b = 0; b < paramInt2 - 1; b++)
/* 52 */         arrayOfByte[b] = 32; 
/* 53 */       str = new String(arrayOfByte) + str;
/*    */     } 
/* 55 */     String[] arrayOfString = { paramString1, Integer.toString(paramInt1), "" + paramChar, Integer.toString(paramChar), paramString2, str };
/* 56 */     this.message = Util.getMessage("InvalidCharacter.1", arrayOfString);
/*    */   }
/*    */ 
/*    */   
/*    */   public String getMessage() {
/* 61 */     return this.message;
/*    */   }
/*    */   
/* 64 */   private String message = null;
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\corba\se\idl\InvalidCharacter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */