/*    */ package com.sun.tools.corba.se.idl;
/*    */ 
/*    */ import java.io.CharArrayWriter;
/*    */ import java.io.File;
/*    */ import java.io.FileWriter;
/*    */ import java.io.IOException;
/*    */ import java.io.PrintWriter;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ public class GenFileStream
/*    */   extends PrintWriter
/*    */ {
/*    */   private CharArrayWriter charArrayWriter;
/*    */   private static CharArrayWriter tmpCharArrayWriter;
/*    */   private String name;
/*    */   
/*    */   public GenFileStream(String paramString) {
/* 58 */     super(tmpCharArrayWriter = new CharArrayWriter());
/* 59 */     this.charArrayWriter = tmpCharArrayWriter;
/* 60 */     this.name = paramString;
/*    */   }
/*    */ 
/*    */   
/*    */   public void close() {
/* 65 */     File file = new File(this.name);
/*    */     
/*    */     try {
/* 68 */       if (checkError()) {
/* 69 */         throw new IOException();
/*    */       }
/*    */ 
/*    */ 
/*    */       
/* 74 */       FileWriter fileWriter = new FileWriter(file);
/* 75 */       fileWriter.write(this.charArrayWriter.toCharArray());
/* 76 */       fileWriter.close();
/*    */     }
/* 78 */     catch (IOException iOException) {
/*    */       
/* 80 */       String[] arrayOfString = { this.name, iOException.toString() };
/* 81 */       System.err.println(Util.getMessage("GenFileStream.1", arrayOfString));
/*    */     } 
/* 83 */     super.close();
/*    */   }
/*    */ 
/*    */   
/*    */   public String name() {
/* 88 */     return this.name;
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\corba\se\idl\GenFileStream.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */