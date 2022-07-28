/*     */ package com.sun.tools.javah;
/*     */ 
/*     */ import java.io.OutputStream;
/*     */ import java.io.PrintWriter;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import javax.lang.model.element.Element;
/*     */ import javax.lang.model.element.ExecutableElement;
/*     */ import javax.lang.model.element.Modifier;
/*     */ import javax.lang.model.element.Name;
/*     */ import javax.lang.model.element.TypeElement;
/*     */ import javax.lang.model.element.VariableElement;
/*     */ import javax.lang.model.type.ArrayType;
/*     */ import javax.lang.model.type.TypeKind;
/*     */ import javax.lang.model.type.TypeMirror;
/*     */ import javax.lang.model.util.ElementFilter;
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
/*     */ public class JNI
/*     */   extends Gen
/*     */ {
/*     */   JNI(Util paramUtil) {
/*  54 */     super(paramUtil);
/*     */   }
/*     */   
/*     */   public String getIncludes() {
/*  58 */     return "#include <jni.h>";
/*     */   }
/*     */   
/*     */   public void write(OutputStream paramOutputStream, TypeElement paramTypeElement) throws Util.Exit {
/*     */     try {
/*  63 */       String str = this.mangler.mangle(paramTypeElement.getQualifiedName(), 1);
/*  64 */       PrintWriter printWriter = wrapWriter(paramOutputStream);
/*  65 */       printWriter.println(guardBegin(str));
/*  66 */       printWriter.println(cppGuardBegin());
/*     */ 
/*     */       
/*  69 */       List<VariableElement> list = getAllFields(paramTypeElement);
/*     */       
/*  71 */       for (VariableElement variableElement : list) {
/*  72 */         if (!variableElement.getModifiers().contains(Modifier.STATIC))
/*     */           continue; 
/*  74 */         String str1 = null;
/*  75 */         str1 = defineForStatic(paramTypeElement, variableElement);
/*  76 */         if (str1 != null) {
/*  77 */           printWriter.println(str1);
/*     */         }
/*     */       } 
/*     */ 
/*     */       
/*  82 */       List<ExecutableElement> list1 = ElementFilter.methodsIn(paramTypeElement.getEnclosedElements());
/*  83 */       for (ExecutableElement executableElement : list1) {
/*  84 */         if (executableElement.getModifiers().contains(Modifier.NATIVE)) {
/*  85 */           TypeMirror typeMirror = this.types.erasure(executableElement.getReturnType());
/*  86 */           String str1 = signature(executableElement);
/*  87 */           TypeSignature typeSignature = new TypeSignature(this.elems);
/*  88 */           Name name = executableElement.getSimpleName();
/*  89 */           boolean bool = false;
/*  90 */           for (ExecutableElement executableElement1 : list1) {
/*  91 */             if (executableElement1 != executableElement && name
/*  92 */               .equals(executableElement1.getSimpleName()) && executableElement1
/*  93 */               .getModifiers().contains(Modifier.NATIVE)) {
/*  94 */               bool = true;
/*     */             }
/*     */           } 
/*  97 */           printWriter.println("/*");
/*  98 */           printWriter.println(" * Class:     " + str);
/*  99 */           printWriter.println(" * Method:    " + this.mangler
/* 100 */               .mangle(name, 2));
/* 101 */           printWriter.println(" * Signature: " + typeSignature.getTypeSignature(str1, typeMirror));
/* 102 */           printWriter.println(" */");
/* 103 */           printWriter.println("JNIEXPORT " + jniType(typeMirror) + " JNICALL " + this.mangler
/*     */               
/* 105 */               .mangleMethod(executableElement, paramTypeElement, bool ? 8 : 7));
/*     */ 
/*     */ 
/*     */           
/* 109 */           printWriter.print("  (JNIEnv *, ");
/* 110 */           List<? extends VariableElement> list2 = executableElement.getParameters();
/* 111 */           ArrayList<TypeMirror> arrayList = new ArrayList();
/* 112 */           for (VariableElement variableElement : list2) {
/* 113 */             arrayList.add(this.types.erasure(variableElement.asType()));
/*     */           }
/* 115 */           if (executableElement.getModifiers().contains(Modifier.STATIC)) {
/* 116 */             printWriter.print("jclass");
/*     */           } else {
/* 118 */             printWriter.print("jobject");
/*     */           } 
/* 120 */           for (TypeMirror typeMirror1 : arrayList) {
/* 121 */             printWriter.print(", ");
/* 122 */             printWriter.print(jniType(typeMirror1));
/*     */           } 
/* 124 */           printWriter.println(");" + this.lineSep);
/*     */         } 
/*     */       } 
/* 127 */       printWriter.println(cppGuardEnd());
/* 128 */       printWriter.println(guardEnd(str));
/* 129 */     } catch (SignatureException signatureException) {
/* 130 */       this.util.error("jni.sigerror", new Object[] { signatureException.getMessage() });
/*     */     } 
/*     */   }
/*     */   
/*     */   protected final String jniType(TypeMirror paramTypeMirror) throws Util.Exit {
/*     */     TypeMirror typeMirror;
/* 136 */     TypeElement typeElement1 = this.elems.getTypeElement("java.lang.Throwable");
/* 137 */     TypeElement typeElement2 = this.elems.getTypeElement("java.lang.Class");
/* 138 */     TypeElement typeElement3 = this.elems.getTypeElement("java.lang.String");
/* 139 */     Element element = this.types.asElement(paramTypeMirror);
/*     */ 
/*     */     
/* 142 */     switch (paramTypeMirror.getKind()) {
/*     */       case ARRAY:
/* 144 */         typeMirror = ((ArrayType)paramTypeMirror).getComponentType();
/* 145 */         switch (typeMirror.getKind()) { case BOOLEAN:
/* 146 */             return "jbooleanArray";
/* 147 */           case BYTE: return "jbyteArray";
/* 148 */           case CHAR: return "jcharArray";
/* 149 */           case SHORT: return "jshortArray";
/* 150 */           case INT: return "jintArray";
/* 151 */           case LONG: return "jlongArray";
/* 152 */           case FLOAT: return "jfloatArray";
/* 153 */           case DOUBLE: return "jdoubleArray";
/*     */           case ARRAY: case DECLARED:
/* 155 */             return "jobjectArray"; }
/* 156 */          throw new Error(typeMirror.toString());
/*     */ 
/*     */       
/*     */       case VOID:
/* 160 */         return "void";
/* 161 */       case BOOLEAN: return "jboolean";
/* 162 */       case BYTE: return "jbyte";
/* 163 */       case CHAR: return "jchar";
/* 164 */       case SHORT: return "jshort";
/* 165 */       case INT: return "jint";
/* 166 */       case LONG: return "jlong";
/* 167 */       case FLOAT: return "jfloat";
/* 168 */       case DOUBLE: return "jdouble";
/*     */       
/*     */       case DECLARED:
/* 171 */         if (element.equals(typeElement3))
/* 172 */           return "jstring"; 
/* 173 */         if (this.types.isAssignable(paramTypeMirror, typeElement1.asType()))
/* 174 */           return "jthrowable"; 
/* 175 */         if (this.types.isAssignable(paramTypeMirror, typeElement2.asType())) {
/* 176 */           return "jclass";
/*     */         }
/* 178 */         return "jobject";
/*     */     } 
/*     */ 
/*     */     
/* 182 */     this.util.bug("jni.unknown.type");
/* 183 */     return null;
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\javah\JNI.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */