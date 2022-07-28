/*    */ package com.sun.tools.hat.internal.parser;
/*    */ 
/*    */ import com.sun.tools.hat.internal.model.Snapshot;
/*    */ import java.io.BufferedInputStream;
/*    */ import java.io.FileInputStream;
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
/*    */ public abstract class Reader
/*    */ {
/*    */   protected PositionDataInputStream in;
/*    */   
/*    */   protected Reader(PositionDataInputStream paramPositionDataInputStream) {
/* 50 */     this.in = paramPositionDataInputStream;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public abstract Snapshot read() throws IOException;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static Snapshot readFile(String paramString, boolean paramBoolean, int paramInt) throws IOException {
/* 68 */     int i = 1;
/* 69 */     int j = paramString.lastIndexOf('#');
/* 70 */     if (j > -1) {
/* 71 */       String str = paramString.substring(j + 1, paramString.length());
/*    */       try {
/* 73 */         i = Integer.parseInt(str, 10);
/* 74 */       } catch (NumberFormatException numberFormatException) {
/* 75 */         String str1 = "In file name \"" + paramString + "\", a dump number was expected after the :, but \"" + str + "\" was found instead.";
/*    */ 
/*    */ 
/*    */         
/* 79 */         System.err.println(str1);
/* 80 */         throw new IOException(str1);
/*    */       } 
/* 82 */       paramString = paramString.substring(0, j);
/*    */     } 
/* 84 */     PositionDataInputStream positionDataInputStream = new PositionDataInputStream(new BufferedInputStream(new FileInputStream(paramString)));
/*    */     
/*    */     try {
/* 87 */       int k = positionDataInputStream.readInt();
/* 88 */       if (k == 1245795905) {
/* 89 */         HprofReader hprofReader = new HprofReader(paramString, positionDataInputStream, i, paramBoolean, paramInt);
/*    */ 
/*    */         
/* 92 */         return hprofReader.read();
/*    */       } 
/* 94 */       throw new IOException("Unrecognized magic number: " + k);
/*    */     } finally {
/*    */       
/* 97 */       positionDataInputStream.close();
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\hat\internal\parser\Reader.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */