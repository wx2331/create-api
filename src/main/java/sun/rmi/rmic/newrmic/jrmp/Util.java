/*     */ package sun.rmi.rmic.newrmic.jrmp;
/*     */ 
/*     */ import com.sun.javadoc.ClassDoc;
/*     */ import com.sun.javadoc.MethodDoc;
/*     */ import com.sun.javadoc.Parameter;
/*     */ import com.sun.javadoc.Type;
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
/*     */ final class Util
/*     */ {
/*     */   private Util() {
/*  44 */     throw new AssertionError();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static String binaryNameOf(ClassDoc paramClassDoc) {
/*  51 */     String str1 = paramClassDoc.name().replace('.', '$');
/*  52 */     String str2 = paramClassDoc.containingPackage().name();
/*  53 */     return str2.equals("") ? str1 : (str2 + "." + str1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static String methodDescriptorOf(MethodDoc paramMethodDoc) {
/*  63 */     String str = "(";
/*  64 */     Parameter[] arrayOfParameter = paramMethodDoc.parameters();
/*  65 */     for (byte b = 0; b < arrayOfParameter.length; b++) {
/*  66 */       str = str + typeDescriptorOf(arrayOfParameter[b].type());
/*     */     }
/*  68 */     str = str + ")" + typeDescriptorOf(paramMethodDoc.returnType());
/*  69 */     return str;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static String typeDescriptorOf(Type paramType) {
/*     */     String str;
/*  78 */     ClassDoc classDoc = paramType.asClassDoc();
/*  79 */     if (classDoc == null) {
/*     */ 
/*     */ 
/*     */       
/*  83 */       String str1 = paramType.typeName();
/*  84 */       if (str1.equals("boolean")) {
/*  85 */         str = "Z";
/*  86 */       } else if (str1.equals("byte")) {
/*  87 */         str = "B";
/*  88 */       } else if (str1.equals("char")) {
/*  89 */         str = "C";
/*  90 */       } else if (str1.equals("short")) {
/*  91 */         str = "S";
/*  92 */       } else if (str1.equals("int")) {
/*  93 */         str = "I";
/*  94 */       } else if (str1.equals("long")) {
/*  95 */         str = "J";
/*  96 */       } else if (str1.equals("float")) {
/*  97 */         str = "F";
/*  98 */       } else if (str1.equals("double")) {
/*  99 */         str = "D";
/* 100 */       } else if (str1.equals("void")) {
/* 101 */         str = "V";
/*     */       } else {
/* 103 */         throw new AssertionError("unrecognized primitive type: " + str1);
/*     */       
/*     */       }
/*     */     
/*     */     }
/*     */     else {
/*     */       
/* 110 */       str = "L" + binaryNameOf(classDoc).replace('.', '/') + ";";
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 116 */     int i = paramType.dimension().length() / 2;
/* 117 */     for (byte b = 0; b < i; b++) {
/* 118 */       str = "[" + str;
/*     */     }
/*     */     
/* 121 */     return str;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static String getFriendlyUnqualifiedSignature(MethodDoc paramMethodDoc) {
/* 130 */     String str = paramMethodDoc.name() + "(";
/* 131 */     Parameter[] arrayOfParameter = paramMethodDoc.parameters();
/* 132 */     for (byte b = 0; b < arrayOfParameter.length; b++) {
/* 133 */       if (b > 0) {
/* 134 */         str = str + ", ";
/*     */       }
/* 136 */       Type type = arrayOfParameter[b].type();
/* 137 */       str = str + type.typeName() + type.dimension();
/*     */     } 
/* 139 */     str = str + ")";
/* 140 */     return str;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static boolean isVoid(Type paramType) {
/* 147 */     return (paramType.asClassDoc() == null && paramType.typeName().equals("void"));
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\sun\rmi\rmic\newrmic\jrmp\Util.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */