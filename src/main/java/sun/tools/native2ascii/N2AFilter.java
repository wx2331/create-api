/*    */ package sun.tools.native2ascii;
/*    */ 
/*    */ import java.io.FilterWriter;
/*    */ import java.io.IOException;
/*    */ import java.io.Writer;
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
/*    */ class N2AFilter
/*    */   extends FilterWriter
/*    */ {
/*    */   public N2AFilter(Writer paramWriter) {
/* 41 */     super(paramWriter);
/*    */   }
/*    */   public void write(char paramChar) throws IOException {
/* 44 */     char[] arrayOfChar = new char[1];
/* 45 */     arrayOfChar[0] = paramChar;
/* 46 */     write(arrayOfChar, 0, 1);
/*    */   }
/*    */ 
/*    */   
/*    */   public void write(char[] paramArrayOfchar, int paramInt1, int paramInt2) throws IOException {
/* 51 */     String str = System.getProperty("line.separator");
/*    */ 
/*    */     
/* 54 */     for (byte b = 0; b < paramInt2; b++) {
/* 55 */       if (paramArrayOfchar[b] > '') {
/*    */         
/* 57 */         this.out.write(92);
/* 58 */         this.out.write(117);
/*    */         
/* 60 */         String str1 = Integer.toHexString(paramArrayOfchar[b]);
/* 61 */         StringBuffer stringBuffer = new StringBuffer(str1);
/* 62 */         stringBuffer.reverse();
/* 63 */         int i = 4 - stringBuffer.length(); byte b1;
/* 64 */         for (b1 = 0; b1 < i; b1++) {
/* 65 */           stringBuffer.append('0');
/*    */         }
/* 67 */         for (b1 = 0; b1 < 4; b1++) {
/* 68 */           this.out.write(stringBuffer.charAt(3 - b1));
/*    */         }
/*    */       } else {
/* 71 */         this.out.write(paramArrayOfchar[b]);
/*    */       } 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\sun\tools\native2ascii\N2AFilter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */