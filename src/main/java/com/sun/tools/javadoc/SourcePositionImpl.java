/*     */ package com.sun.tools.javadoc;
/*     */ 
/*     */ import com.sun.javadoc.SourcePosition;
/*     */ import com.sun.tools.javac.util.Position;
/*     */ import java.io.File;
/*     */ import javax.tools.FileObject;
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
/*     */ public class SourcePositionImpl
/*     */   implements SourcePosition
/*     */ {
/*     */   FileObject filename;
/*     */   int position;
/*     */   Position.LineMap lineMap;
/*     */   
/*     */   public File file() {
/*  54 */     return (this.filename == null) ? null : new File(this.filename.getName());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public FileObject fileObject() {
/*  60 */     return this.filename;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int line() {
/*  66 */     if (this.lineMap == null) {
/*  67 */       return 0;
/*     */     }
/*  69 */     return this.lineMap.getLineNumber(this.position);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int column() {
/*  79 */     if (this.lineMap == null) {
/*  80 */       return 0;
/*     */     }
/*  82 */     return this.lineMap.getColumnNumber(this.position);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private SourcePositionImpl(FileObject paramFileObject, int paramInt, Position.LineMap paramLineMap) {
/*  89 */     this.filename = paramFileObject;
/*  90 */     this.position = paramInt;
/*  91 */     this.lineMap = paramLineMap;
/*     */   }
/*     */ 
/*     */   
/*     */   public static SourcePosition make(FileObject paramFileObject, int paramInt, Position.LineMap paramLineMap) {
/*  96 */     if (paramFileObject == null) return null; 
/*  97 */     return new SourcePositionImpl(paramFileObject, paramInt, paramLineMap);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 103 */     String str = this.filename.getName();
/* 104 */     if (str.endsWith(")")) {
/* 105 */       int i = str.lastIndexOf("(");
/* 106 */       if (i != -1)
/*     */       {
/*     */         
/* 109 */         str = str.substring(0, i) + File.separatorChar + str.substring(i + 1, str.length() - 1);
/*     */       }
/*     */     } 
/* 112 */     if (this.position == -1) {
/* 113 */       return str;
/*     */     }
/* 115 */     return str + ":" + line();
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\javadoc\SourcePositionImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */