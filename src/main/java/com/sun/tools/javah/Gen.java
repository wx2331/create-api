/*     */ package com.sun.tools.javah;
/*     */ 
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.FileNotFoundException;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.io.OutputStreamWriter;
/*     */ import java.io.PrintWriter;
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import java.util.Stack;
/*     */ import javax.annotation.processing.ProcessingEnvironment;
/*     */ import javax.lang.model.element.ExecutableElement;
/*     */ import javax.lang.model.element.Modifier;
/*     */ import javax.lang.model.element.Name;
/*     */ import javax.lang.model.element.TypeElement;
/*     */ import javax.lang.model.element.VariableElement;
/*     */ import javax.lang.model.util.ElementFilter;
/*     */ import javax.lang.model.util.Elements;
/*     */ import javax.lang.model.util.Types;
/*     */ import javax.tools.FileObject;
/*     */ import javax.tools.JavaFileManager;
/*     */ import javax.tools.JavaFileObject;
/*     */ import javax.tools.StandardLocation;
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
/*     */ public abstract class Gen
/*     */ {
/*  71 */   protected String lineSep = System.getProperty("line.separator");
/*     */ 
/*     */   
/*     */   protected ProcessingEnvironment processingEnvironment;
/*     */ 
/*     */   
/*     */   protected Types types;
/*     */ 
/*     */   
/*     */   protected Elements elems;
/*     */   
/*     */   protected Mangle mangler;
/*     */   
/*     */   protected Util util;
/*     */   
/*     */   protected Set<TypeElement> classes;
/*     */   
/*  88 */   private static final boolean isWindows = System.getProperty("os.name").startsWith("Windows");
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected JavaFileManager fileManager;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected JavaFileObject outFile;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean force;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setFileManager(JavaFileManager paramJavaFileManager) {
/* 110 */     this.fileManager = paramJavaFileManager;
/*     */   }
/*     */   
/*     */   public void setOutFile(JavaFileObject paramJavaFileObject) {
/* 114 */     this.outFile = paramJavaFileObject;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setClasses(Set<TypeElement> paramSet) {
/* 119 */     this.classes = paramSet;
/*     */   }
/*     */   
/*     */   void setProcessingEnvironment(ProcessingEnvironment paramProcessingEnvironment) {
/* 123 */     this.processingEnvironment = paramProcessingEnvironment;
/* 124 */     this.elems = paramProcessingEnvironment.getElementUtils();
/* 125 */     this.types = paramProcessingEnvironment.getTypeUtils();
/* 126 */     this.mangler = new Mangle(this.elems, this.types);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected Gen(Util paramUtil) {
/* 132 */     this.force = false;
/*     */     this.util = paramUtil;
/*     */   } public void setForce(boolean paramBoolean) {
/* 135 */     this.force = paramBoolean;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected PrintWriter wrapWriter(OutputStream paramOutputStream) throws Util.Exit {
/*     */     try {
/* 144 */       return new PrintWriter(new OutputStreamWriter(paramOutputStream, "ISO8859_1"), true);
/* 145 */     } catch (UnsupportedEncodingException unsupportedEncodingException) {
/* 146 */       this.util.bug("encoding.iso8859_1.not.found");
/* 147 */       return null;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void run() throws IOException, ClassNotFoundException, Util.Exit {
/* 159 */     boolean bool = false;
/* 160 */     if (this.outFile != null) {
/*     */       
/* 162 */       ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(8192);
/* 163 */       writeFileTop(byteArrayOutputStream);
/*     */       
/* 165 */       for (TypeElement typeElement : this.classes) {
/* 166 */         write(byteArrayOutputStream, typeElement);
/*     */       }
/*     */       
/* 169 */       writeIfChanged(byteArrayOutputStream.toByteArray(), this.outFile);
/*     */     } else {
/*     */       
/* 172 */       for (TypeElement typeElement : this.classes) {
/* 173 */         ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(8192);
/* 174 */         writeFileTop(byteArrayOutputStream);
/* 175 */         write(byteArrayOutputStream, typeElement);
/* 176 */         writeIfChanged(byteArrayOutputStream.toByteArray(), getFileObject(typeElement.getQualifiedName()));
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void writeIfChanged(byte[] paramArrayOfbyte, FileObject paramFileObject) throws IOException {
/* 187 */     boolean bool = false;
/* 188 */     String str = "[No need to update file ";
/*     */     
/* 190 */     if (this.force) {
/* 191 */       bool = true;
/* 192 */       str = "[Forcefully writing file ";
/*     */     } else {
/*     */ 
/*     */       
/*     */       try {
/*     */ 
/*     */ 
/*     */         
/* 200 */         InputStream inputStream = paramFileObject.openInputStream();
/* 201 */         byte[] arrayOfByte = readBytes(inputStream);
/* 202 */         if (!Arrays.equals(arrayOfByte, paramArrayOfbyte)) {
/* 203 */           bool = true;
/* 204 */           str = "[Overwriting file ";
/*     */         }
/*     */       
/* 207 */       } catch (FileNotFoundException fileNotFoundException) {
/* 208 */         bool = true;
/* 209 */         str = "[Creating file ";
/*     */       } 
/*     */     } 
/*     */     
/* 213 */     if (this.util.verbose) {
/* 214 */       this.util.log(str + paramFileObject + "]");
/*     */     }
/* 216 */     if (bool) {
/* 217 */       OutputStream outputStream = paramFileObject.openOutputStream();
/* 218 */       outputStream.write(paramArrayOfbyte);
/* 219 */       outputStream.close();
/*     */     } 
/*     */   }
/*     */   
/*     */   protected byte[] readBytes(InputStream paramInputStream) throws IOException {
/*     */     try {
/* 225 */       byte[] arrayOfByte = new byte[paramInputStream.available() + 1];
/* 226 */       int i = 0;
/*     */       int j;
/* 228 */       while ((j = paramInputStream.read(arrayOfByte, i, arrayOfByte.length - i)) != -1) {
/* 229 */         i += j;
/* 230 */         if (i == arrayOfByte.length) {
/* 231 */           arrayOfByte = Arrays.copyOf(arrayOfByte, arrayOfByte.length * 2);
/*     */         }
/*     */       } 
/* 234 */       return Arrays.copyOf(arrayOfByte, i);
/*     */     } finally {
/* 236 */       paramInputStream.close();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected String defineForStatic(TypeElement paramTypeElement, VariableElement paramVariableElement) throws Util.Exit {
/* 242 */     Name name1 = paramTypeElement.getQualifiedName();
/* 243 */     Name name2 = paramVariableElement.getSimpleName();
/*     */     
/* 245 */     String str1 = this.mangler.mangle(name1, 1);
/* 246 */     String str2 = this.mangler.mangle(name2, 2);
/*     */     
/* 248 */     if (!paramVariableElement.getModifiers().contains(Modifier.STATIC)) {
/* 249 */       this.util.bug("tried.to.define.non.static");
/*     */     }
/* 251 */     if (paramVariableElement.getModifiers().contains(Modifier.FINAL)) {
/* 252 */       Object object = null;
/*     */       
/* 254 */       object = paramVariableElement.getConstantValue();
/*     */       
/* 256 */       if (object != null) {
/* 257 */         String str = null;
/* 258 */         if (object instanceof Integer || object instanceof Byte || object instanceof Short) {
/*     */ 
/*     */ 
/*     */           
/* 262 */           str = object.toString() + "L";
/* 263 */         } else if (object instanceof Boolean) {
/* 264 */           str = ((Boolean)object).booleanValue() ? "1L" : "0L";
/* 265 */         } else if (object instanceof Character) {
/* 266 */           Character character = (Character)object;
/* 267 */           str = String.valueOf(character.charValue() & Character.MAX_VALUE) + "L";
/* 268 */         } else if (object instanceof Long) {
/*     */           
/* 270 */           if (isWindows)
/* 271 */           { str = object.toString() + "i64"; }
/*     */           else
/* 273 */           { str = object.toString() + "LL"; } 
/* 274 */         } else if (object instanceof Float) {
/*     */           
/* 276 */           float f = ((Float)object).floatValue();
/* 277 */           if (Float.isInfinite(f))
/* 278 */           { str = ((f < 0.0F) ? "-" : "") + "Inff"; }
/*     */           else
/* 280 */           { str = object.toString() + "f"; } 
/* 281 */         } else if (object instanceof Double) {
/*     */           
/* 283 */           double d = ((Double)object).doubleValue();
/* 284 */           if (Double.isInfinite(d)) {
/* 285 */             str = ((d < 0.0D) ? "-" : "") + "InfD";
/*     */           } else {
/* 287 */             str = object.toString();
/*     */           } 
/* 289 */         }  if (str != null) {
/* 290 */           StringBuilder stringBuilder = new StringBuilder("#undef ");
/* 291 */           stringBuilder.append(str1); stringBuilder.append("_"); stringBuilder.append(str2); stringBuilder.append(this.lineSep);
/* 292 */           stringBuilder.append("#define "); stringBuilder.append(str1); stringBuilder.append("_");
/* 293 */           stringBuilder.append(str2); stringBuilder.append(" "); stringBuilder.append(str);
/* 294 */           return stringBuilder.toString();
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 299 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String cppGuardBegin() {
/* 306 */     return "#ifdef __cplusplus" + this.lineSep + "extern \"C\" {" + this.lineSep + "#endif";
/*     */   }
/*     */   
/*     */   protected String cppGuardEnd() {
/* 310 */     return "#ifdef __cplusplus" + this.lineSep + "}" + this.lineSep + "#endif";
/*     */   }
/*     */   
/*     */   protected String guardBegin(String paramString) {
/* 314 */     return "/* Header for class " + paramString + " */" + this.lineSep + this.lineSep + "#ifndef _Included_" + paramString + this.lineSep + "#define _Included_" + paramString;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected String guardEnd(String paramString) {
/* 320 */     return "#endif";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void writeFileTop(OutputStream paramOutputStream) throws Util.Exit {
/* 327 */     PrintWriter printWriter = wrapWriter(paramOutputStream);
/* 328 */     printWriter.println("/* DO NOT EDIT THIS FILE - it is machine generated */" + this.lineSep + 
/* 329 */         getIncludes());
/*     */   }
/*     */   
/*     */   protected String baseFileName(CharSequence paramCharSequence) {
/* 333 */     return this.mangler.mangle(paramCharSequence, 1);
/*     */   }
/*     */   
/*     */   protected FileObject getFileObject(CharSequence paramCharSequence) throws IOException {
/* 337 */     String str = baseFileName(paramCharSequence) + getFileSuffix();
/* 338 */     return this.fileManager.getFileForOutput(StandardLocation.SOURCE_OUTPUT, "", str, null);
/*     */   }
/*     */   
/*     */   protected String getFileSuffix() {
/* 342 */     return ".h";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   List<VariableElement> getAllFields(TypeElement paramTypeElement) {
/* 350 */     ArrayList<VariableElement> arrayList = new ArrayList();
/* 351 */     TypeElement typeElement = null;
/* 352 */     Stack<TypeElement> stack = new Stack();
/*     */     
/* 354 */     typeElement = paramTypeElement;
/*     */     while (true) {
/* 356 */       stack.push(typeElement);
/* 357 */       TypeElement typeElement1 = (TypeElement)this.types.asElement(typeElement.getSuperclass());
/* 358 */       if (typeElement1 == null)
/*     */         break; 
/* 360 */       typeElement = typeElement1;
/*     */     } 
/*     */     
/* 363 */     while (!stack.empty()) {
/* 364 */       typeElement = stack.pop();
/* 365 */       arrayList.addAll(ElementFilter.fieldsIn(typeElement.getEnclosedElements()));
/*     */     } 
/*     */     
/* 368 */     return arrayList;
/*     */   }
/*     */ 
/*     */   
/*     */   String signature(ExecutableElement paramExecutableElement) {
/* 373 */     StringBuilder stringBuilder = new StringBuilder("(");
/* 374 */     String str = "";
/* 375 */     for (VariableElement variableElement : paramExecutableElement.getParameters()) {
/* 376 */       stringBuilder.append(str);
/* 377 */       stringBuilder.append(this.types.erasure(variableElement.asType()).toString());
/* 378 */       str = ",";
/*     */     } 
/* 380 */     stringBuilder.append(")");
/* 381 */     return stringBuilder.toString();
/*     */   }
/*     */   
/*     */   protected abstract void write(OutputStream paramOutputStream, TypeElement paramTypeElement) throws Util.Exit;
/*     */   
/*     */   protected abstract String getIncludes();
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\javah\Gen.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */