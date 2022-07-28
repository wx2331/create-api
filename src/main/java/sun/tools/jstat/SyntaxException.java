/*    */ package sun.tools.jstat;
/*    */ 
/*    */ import java.util.Set;
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
/*    */ public class SyntaxException
/*    */   extends ParserException
/*    */ {
/*    */   private String message;
/*    */   
/*    */   public SyntaxException(String paramString) {
/* 43 */     this.message = paramString;
/*    */   }
/*    */   
/*    */   public SyntaxException(int paramInt, String paramString1, String paramString2) {
/* 47 */     this.message = "Syntax error at line " + paramInt + ": Expected " + paramString1 + ", Found " + paramString2;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public SyntaxException(int paramInt, String paramString, Token paramToken) {
/* 53 */     this
/*    */       
/* 55 */       .message = "Syntax error at line " + paramInt + ": Expected " + paramString + ", Found " + paramToken.toMessage();
/*    */   }
/*    */   
/*    */   public SyntaxException(int paramInt, Token paramToken1, Token paramToken2) {
/* 59 */     this
/*    */       
/* 61 */       .message = "Syntax error at line " + paramInt + ": Expected " + paramToken1.toMessage() + ", Found " + paramToken2.toMessage();
/*    */   }
/*    */   
/*    */   public SyntaxException(int paramInt, Set paramSet, Token paramToken) {
/* 65 */     StringBuilder stringBuilder = new StringBuilder();
/*    */     
/* 67 */     stringBuilder.append("Syntax error at line " + paramInt + ": Expected one of '");
/*    */     
/* 69 */     boolean bool = true;
/* 70 */     for (String str : paramSet) {
/*    */       
/* 72 */       if (bool) {
/* 73 */         stringBuilder.append(str);
/* 74 */         bool = false; continue;
/*    */       } 
/* 76 */       stringBuilder.append("|" + str);
/*    */     } 
/*    */ 
/*    */     
/* 80 */     stringBuilder.append("', Found " + paramToken.toMessage());
/* 81 */     this.message = stringBuilder.toString();
/*    */   }
/*    */   
/*    */   public String getMessage() {
/* 85 */     return this.message;
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\sun\tools\jstat\SyntaxException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */