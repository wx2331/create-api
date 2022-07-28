/*     */ package com.sun.tools.corba.se.idl;
/*     */ 
/*     */ import java.security.MessageDigest;
/*     */ import java.util.Hashtable;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ValueRepositoryId
/*     */ {
/*     */   private MessageDigest sha;
/*     */   private int index;
/*     */   private Hashtable types;
/*     */   private String hashcode;
/*     */   
/*     */   public ValueRepositoryId() {
/*     */     try {
/*  57 */       this.sha = MessageDigest.getInstance("SHA-1");
/*     */     }
/*  59 */     catch (Exception exception) {}
/*     */     
/*  61 */     this.index = 0;
/*  62 */     this.types = new Hashtable<>();
/*  63 */     this.hashcode = null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addValue(int paramInt) {
/*  70 */     this.sha.update((byte)(paramInt >> 24 & 0xF));
/*  71 */     this.sha.update((byte)(paramInt >> 16 & 0xF));
/*  72 */     this.sha.update((byte)(paramInt >> 8 & 0xF));
/*  73 */     this.sha.update((byte)(paramInt & 0xF));
/*  74 */     this.index++;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addType(SymtabEntry paramSymtabEntry) {
/*  82 */     this.types.put(paramSymtabEntry, new Integer(this.index));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isNewType(SymtabEntry paramSymtabEntry) {
/*  94 */     Object object = this.types.get(paramSymtabEntry);
/*  95 */     if (object == null) {
/*     */       
/*  97 */       addType(paramSymtabEntry);
/*  98 */       return true;
/*     */     } 
/* 100 */     addValue(-1);
/* 101 */     addValue(((Integer)object).intValue());
/* 102 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getHashcode() {
/* 112 */     if (this.hashcode == null) {
/*     */       
/* 114 */       byte[] arrayOfByte = this.sha.digest();
/* 115 */       this
/*     */ 
/*     */         
/* 118 */         .hashcode = hexOf(arrayOfByte[0]) + hexOf(arrayOfByte[1]) + hexOf(arrayOfByte[2]) + hexOf(arrayOfByte[3]) + hexOf(arrayOfByte[4]) + hexOf(arrayOfByte[5]) + hexOf(arrayOfByte[6]) + hexOf(arrayOfByte[7]);
/*     */     } 
/* 120 */     return this.hashcode;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static String hexOf(byte paramByte) {
/* 126 */     int i = paramByte >> 4 & 0xF;
/* 127 */     int j = paramByte & 0xF;
/* 128 */     return "0123456789ABCDEF".substring(i, i + 1) + "0123456789ABCDEF"
/* 129 */       .substring(j, j + 1);
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\corba\se\idl\ValueRepositoryId.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */