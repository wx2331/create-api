/*     */ package com.sun.tools.javac.file;
/*     */
/*     */ import com.sun.tools.javac.util.BaseFileManager;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.Reader;
/*     */ import java.net.URI;
/*     */ import java.net.URISyntaxException;
/*     */ import java.nio.charset.CharsetDecoder;
/*     */ import javax.lang.model.element.Modifier;
/*     */ import javax.lang.model.element.NestingKind;
/*     */ import javax.tools.FileObject;
/*     */ import javax.tools.JavaFileObject;
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
/*     */ public abstract class BaseFileObject
/*     */   implements JavaFileObject
/*     */ {
/*     */   protected final JavacFileManager fileManager;
/*     */
/*     */   protected BaseFileObject(JavacFileManager paramJavacFileManager) {
/*  52 */     this.fileManager = paramJavacFileManager;
/*     */   }
/*     */
/*     */
/*     */
/*     */   public abstract String getShortName();
/*     */
/*     */
/*     */   public String toString() {
/*  61 */     return getClass().getSimpleName() + "[" + getName() + "]";
/*     */   }
/*     */   public NestingKind getNestingKind() {
/*  64 */     return null;
/*     */   } public Modifier getAccessLevel() {
/*  66 */     return null;
/*     */   }
/*     */   public Reader openReader(boolean paramBoolean) throws IOException {
/*  69 */     return new InputStreamReader(openInputStream(), getDecoder(paramBoolean));
/*     */   }
/*     */
/*     */   protected CharsetDecoder getDecoder(boolean paramBoolean) {
/*  73 */     throw new UnsupportedOperationException();
/*     */   }
/*     */
/*     */   protected abstract String inferBinaryName(Iterable<? extends File> paramIterable);
/*     */
/*     */   protected static Kind getKind(String paramString) {
/*  79 */     return BaseFileManager.getKind(paramString);
/*     */   }
/*     */
/*     */   protected static String removeExtension(String paramString) {
/*  83 */     int i = paramString.lastIndexOf(".");
/*  84 */     return (i == -1) ? paramString : paramString.substring(0, i);
/*     */   }
/*     */
/*     */   protected static URI createJarUri(File paramFile, String paramString) {
/*  88 */     URI uRI = paramFile.toURI().normalize();
/*  89 */     String str = paramString.startsWith("/") ? "!" : "!/";
/*     */
/*     */     try {
/*  92 */       return new URI("jar:" + uRI + str + paramString);
/*  93 */     } catch (URISyntaxException uRISyntaxException) {
/*  94 */       throw new CannotCreateUriError(uRI + str + paramString, uRISyntaxException);
/*     */     }
/*     */   }
/*     */
/*     */   protected static class CannotCreateUriError
/*     */     extends Error {
/*     */     private static final long serialVersionUID = 9101708840997613546L;
/*     */
/*     */     public CannotCreateUriError(String param1String, Throwable param1Throwable) {
/* 103 */       super(param1String, param1Throwable);
/*     */     }
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   public static String getSimpleName(FileObject paramFileObject) {
/* 112 */     URI uRI = paramFileObject.toUri();
/* 113 */     String str = uRI.getSchemeSpecificPart();
/* 114 */     return str.substring(str.lastIndexOf("/") + 1);
/*     */   }
/*     */
/*     */   public abstract boolean equals(Object paramObject);
/*     */
/*     */   public abstract int hashCode();
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\javac\file\BaseFileObject.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
