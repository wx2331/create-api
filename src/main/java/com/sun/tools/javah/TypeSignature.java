/*     */ package com.sun.tools.javah;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.StringTokenizer;
/*     */ import javax.lang.model.element.Name;
/*     */ import javax.lang.model.element.TypeElement;
/*     */ import javax.lang.model.type.ArrayType;
/*     */ import javax.lang.model.type.DeclaredType;
/*     */ import javax.lang.model.type.NoType;
/*     */ import javax.lang.model.type.PrimitiveType;
/*     */ import javax.lang.model.type.TypeKind;
/*     */ import javax.lang.model.type.TypeMirror;
/*     */ import javax.lang.model.type.TypeVariable;
/*     */ import javax.lang.model.util.Elements;
/*     */ import javax.lang.model.util.SimpleTypeVisitor8;
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
/*     */ public class TypeSignature
/*     */ {
/*     */   Elements elems;
/*     */   private static final String SIG_VOID = "V";
/*     */   private static final String SIG_BOOLEAN = "Z";
/*     */   private static final String SIG_BYTE = "B";
/*     */   private static final String SIG_CHAR = "C";
/*     */   private static final String SIG_SHORT = "S";
/*     */   private static final String SIG_INT = "I";
/*     */   private static final String SIG_LONG = "J";
/*     */   private static final String SIG_FLOAT = "F";
/*     */   private static final String SIG_DOUBLE = "D";
/*     */   private static final String SIG_ARRAY = "[";
/*     */   private static final String SIG_CLASS = "L";
/*     */   
/*     */   static class SignatureException
/*     */     extends Exception
/*     */   {
/*     */     private static final long serialVersionUID = 1L;
/*     */     
/*     */     SignatureException(String param1String) {
/*  58 */       super(param1String);
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
/*     */   public TypeSignature(Elements paramElements) {
/*  81 */     this.elems = paramElements;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getTypeSignature(String paramString) throws SignatureException {
/*  88 */     return getParamJVMSignature(paramString);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getTypeSignature(String paramString, TypeMirror paramTypeMirror) throws SignatureException {
/*  96 */     String str1 = null;
/*  97 */     String str2 = null;
/*  98 */     ArrayList<String> arrayList = new ArrayList();
/*  99 */     String str3 = null;
/* 100 */     String str4 = null;
/* 101 */     String str5 = null;
/* 102 */     String str6 = null;
/* 103 */     int i = 0;
/*     */     
/* 105 */     int j = -1;
/* 106 */     int k = -1;
/* 107 */     StringTokenizer stringTokenizer = null;
/* 108 */     boolean bool = false;
/*     */ 
/*     */     
/* 111 */     if (paramString != null) {
/* 112 */       j = paramString.indexOf("(");
/* 113 */       k = paramString.indexOf(")");
/*     */     } 
/*     */     
/* 116 */     if (j != -1 && k != -1 && j + 1 < paramString
/* 117 */       .length() && k < paramString
/* 118 */       .length()) {
/* 119 */       str1 = paramString.substring(j + 1, k);
/*     */     }
/*     */ 
/*     */     
/* 123 */     if (str1 != null) {
/* 124 */       if (str1.indexOf(",") != -1) {
/* 125 */         stringTokenizer = new StringTokenizer(str1, ",");
/* 126 */         if (stringTokenizer != null) {
/* 127 */           while (stringTokenizer.hasMoreTokens()) {
/* 128 */             arrayList.add(stringTokenizer.nextToken());
/*     */           }
/*     */         }
/*     */       } else {
/* 132 */         arrayList.add(str1);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/* 137 */     str2 = "(";
/*     */ 
/*     */     
/* 140 */     while (arrayList.isEmpty() != true) {
/* 141 */       str3 = ((String)arrayList.remove(bool)).trim();
/* 142 */       str4 = getParamJVMSignature(str3);
/* 143 */       if (str4 != null) {
/* 144 */         str2 = str2 + str4;
/*     */       }
/*     */     } 
/*     */     
/* 148 */     str2 = str2 + ")";
/*     */ 
/*     */ 
/*     */     
/* 152 */     str6 = "";
/* 153 */     if (paramTypeMirror != null) {
/* 154 */       i = dimensions(paramTypeMirror);
/*     */     }
/*     */ 
/*     */     
/* 158 */     while (i-- > 0) {
/* 159 */       str6 = str6 + "[";
/*     */     }
/* 161 */     if (paramTypeMirror != null) {
/* 162 */       str5 = qualifiedTypeName(paramTypeMirror);
/* 163 */       str6 = str6 + getComponentType(str5);
/*     */     } else {
/* 165 */       System.out.println("Invalid return type.");
/*     */     } 
/*     */     
/* 168 */     str2 = str2 + str6;
/*     */     
/* 170 */     return str2;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private String getParamJVMSignature(String paramString) throws SignatureException {
/* 177 */     String str1 = "";
/* 178 */     String str2 = "";
/*     */     
/* 180 */     if (paramString != null) {
/*     */       
/* 182 */       if (paramString.indexOf("[]") != -1) {
/*     */         
/* 184 */         int i = paramString.indexOf("[]");
/* 185 */         str2 = paramString.substring(0, i);
/* 186 */         String str = paramString.substring(i);
/* 187 */         if (str != null)
/* 188 */           while (str.indexOf("[]") != -1) {
/* 189 */             str1 = str1 + "[";
/* 190 */             int j = str.indexOf("]") + 1;
/* 191 */             if (j < str.length()) {
/* 192 */               str = str.substring(j); continue;
/*     */             } 
/* 194 */             str = "";
/*     */           }  
/*     */       } else {
/* 197 */         str2 = paramString;
/*     */       } 
/* 199 */       str1 = str1 + getComponentType(str2);
/*     */     } 
/* 201 */     return str1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private String getComponentType(String paramString) throws SignatureException {
/* 209 */     String str = "";
/*     */     
/* 211 */     if (paramString != null) {
/* 212 */       if (paramString.equals("void")) { str = str + "V"; }
/* 213 */       else if (paramString.equals("boolean")) { str = str + "Z"; }
/* 214 */       else if (paramString.equals("byte")) { str = str + "B"; }
/* 215 */       else if (paramString.equals("char")) { str = str + "C"; }
/* 216 */       else if (paramString.equals("short")) { str = str + "S"; }
/* 217 */       else if (paramString.equals("int")) { str = str + "I"; }
/* 218 */       else if (paramString.equals("long")) { str = str + "J"; }
/* 219 */       else if (paramString.equals("float")) { str = str + "F"; }
/* 220 */       else if (paramString.equals("double")) { str = str + "D"; }
/*     */       
/* 222 */       else if (!paramString.equals(""))
/* 223 */       { TypeElement typeElement = this.elems.getTypeElement(paramString);
/*     */         
/* 225 */         if (typeElement == null) {
/* 226 */           throw new SignatureException(paramString);
/*     */         }
/* 228 */         String str1 = typeElement.getQualifiedName().toString();
/* 229 */         String str2 = str1.replace('.', '/');
/* 230 */         str = str + "L";
/* 231 */         str = str + str2;
/* 232 */         str = str + ";"; }
/*     */     
/*     */     }
/*     */ 
/*     */     
/* 237 */     return str;
/*     */   }
/*     */   
/*     */   int dimensions(TypeMirror paramTypeMirror) {
/* 241 */     if (paramTypeMirror.getKind() != TypeKind.ARRAY)
/* 242 */       return 0; 
/* 243 */     return 1 + dimensions(((ArrayType)paramTypeMirror).getComponentType());
/*     */   }
/*     */ 
/*     */   
/*     */   String qualifiedTypeName(TypeMirror paramTypeMirror) {
/* 248 */     SimpleTypeVisitor8<Name, Void> simpleTypeVisitor8 = new SimpleTypeVisitor8<Name, Void>()
/*     */       {
/*     */         public Name visitArray(ArrayType param1ArrayType, Void param1Void) {
/* 251 */           return param1ArrayType.getComponentType().<Name, Void>accept(this, param1Void);
/*     */         }
/*     */ 
/*     */         
/*     */         public Name visitDeclared(DeclaredType param1DeclaredType, Void param1Void) {
/* 256 */           return ((TypeElement)param1DeclaredType.asElement()).getQualifiedName();
/*     */         }
/*     */ 
/*     */         
/*     */         public Name visitPrimitive(PrimitiveType param1PrimitiveType, Void param1Void) {
/* 261 */           return TypeSignature.this.elems.getName(param1PrimitiveType.toString());
/*     */         }
/*     */ 
/*     */         
/*     */         public Name visitNoType(NoType param1NoType, Void param1Void) {
/* 266 */           if (param1NoType.getKind() == TypeKind.VOID)
/* 267 */             return TypeSignature.this.elems.getName("void"); 
/* 268 */           return defaultAction(param1NoType, param1Void);
/*     */         }
/*     */ 
/*     */         
/*     */         public Name visitTypeVariable(TypeVariable param1TypeVariable, Void param1Void) {
/* 273 */           return param1TypeVariable.getUpperBound().<Name, Void>accept(this, param1Void);
/*     */         }
/*     */       };
/* 276 */     return ((Name)simpleTypeVisitor8.visit(paramTypeMirror)).toString();
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\javah\TypeSignature.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */