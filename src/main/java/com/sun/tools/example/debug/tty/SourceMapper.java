/*     */ package com.sun.tools.example.debug.tty;
/*     */ 
/*     */ import com.sun.jdi.AbsentInformationException;
/*     */ import com.sun.jdi.Location;
/*     */ import java.io.BufferedReader;
/*     */ import java.io.File;
/*     */ import java.io.FileReader;
/*     */ import java.io.IOException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.StringTokenizer;
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
/*     */ class SourceMapper
/*     */ {
/*     */   private final String[] dirs;
/*     */   
/*     */   SourceMapper(List<String> paramList) {
/*  53 */     ArrayList<String> arrayList = new ArrayList();
/*  54 */     for (String str : paramList) {
/*     */ 
/*     */       
/*  57 */       if (!str.endsWith(".jar") && 
/*  58 */         !str.endsWith(".zip")) {
/*  59 */         arrayList.add(str);
/*     */       }
/*     */     } 
/*  62 */     this.dirs = arrayList.<String>toArray(new String[0]);
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
/*     */   SourceMapper(String paramString) {
/*  74 */     StringTokenizer stringTokenizer = new StringTokenizer(paramString, File.pathSeparator);
/*     */     
/*  76 */     ArrayList<String> arrayList = new ArrayList();
/*  77 */     while (stringTokenizer.hasMoreTokens()) {
/*  78 */       String str = stringTokenizer.nextToken();
/*     */ 
/*     */       
/*  81 */       if (!str.endsWith(".jar") && 
/*  82 */         !str.endsWith(".zip")) {
/*  83 */         arrayList.add(str);
/*     */       }
/*     */     } 
/*  86 */     this.dirs = arrayList.<String>toArray(new String[0]);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   String getSourcePath() {
/*  93 */     byte b = 0;
/*     */     
/*  95 */     if (this.dirs.length < 1) {
/*  96 */       return "";
/*     */     }
/*  98 */     StringBuffer stringBuffer = new StringBuffer(this.dirs[b++]);
/*     */     
/* 100 */     for (; b < this.dirs.length; b++) {
/* 101 */       stringBuffer.append(File.pathSeparator);
/* 102 */       stringBuffer.append(this.dirs[b]);
/*     */     } 
/* 104 */     return stringBuffer.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   File sourceFile(Location paramLocation) {
/*     */     try {
/* 113 */       String str1 = paramLocation.sourceName();
/* 114 */       String str2 = paramLocation.declaringType().name();
/* 115 */       int i = str2.lastIndexOf('.');
/* 116 */       String str3 = (i >= 0) ? str2.substring(0, i + 1) : "";
/* 117 */       String str4 = str3.replace('.', File.separatorChar) + str1;
/* 118 */       for (byte b = 0; b < this.dirs.length; b++) {
/* 119 */         File file = new File(this.dirs[b], str4);
/* 120 */         if (file.exists()) {
/* 121 */           return file;
/*     */         }
/*     */       } 
/* 124 */       return null;
/* 125 */     } catch (AbsentInformationException absentInformationException) {
/* 126 */       return null;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   BufferedReader sourceReader(Location paramLocation) {
/* 137 */     File file = sourceFile(paramLocation);
/* 138 */     if (file == null) {
/* 139 */       return null;
/*     */     }
/*     */     try {
/* 142 */       return new BufferedReader(new FileReader(file));
/* 143 */     } catch (IOException iOException) {
/*     */       
/* 145 */       return null;
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\example\debug\tty\SourceMapper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */