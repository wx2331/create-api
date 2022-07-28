/*     */ package com.sun.tools.classfile;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
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
/*     */ public class Signature
/*     */   extends Descriptor
/*     */ {
/*     */   private String sig;
/*     */   private int sigp;
/*     */   private Type type;
/*     */   
/*     */   public Signature(int paramInt) {
/*  43 */     super(paramInt);
/*     */   }
/*     */   
/*     */   public Type getType(ConstantPool paramConstantPool) throws ConstantPoolException {
/*  47 */     if (this.type == null)
/*  48 */       this.type = parse(getValue(paramConstantPool)); 
/*  49 */     return this.type;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getParameterCount(ConstantPool paramConstantPool) throws ConstantPoolException {
/*  54 */     Type.MethodType methodType = (Type.MethodType)getType(paramConstantPool);
/*  55 */     return methodType.paramTypes.size();
/*     */   }
/*     */ 
/*     */   
/*     */   public String getParameterTypes(ConstantPool paramConstantPool) throws ConstantPoolException {
/*  60 */     Type.MethodType methodType = (Type.MethodType)getType(paramConstantPool);
/*  61 */     StringBuilder stringBuilder = new StringBuilder();
/*  62 */     stringBuilder.append("(");
/*  63 */     String str = "";
/*  64 */     for (Type type : methodType.paramTypes) {
/*  65 */       stringBuilder.append(str);
/*  66 */       stringBuilder.append(type);
/*  67 */       str = ", ";
/*     */     } 
/*  69 */     stringBuilder.append(")");
/*  70 */     return stringBuilder.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   public String getReturnType(ConstantPool paramConstantPool) throws ConstantPoolException {
/*  75 */     Type.MethodType methodType = (Type.MethodType)getType(paramConstantPool);
/*  76 */     return methodType.returnType.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   public String getFieldType(ConstantPool paramConstantPool) throws ConstantPoolException {
/*  81 */     return getType(paramConstantPool).toString();
/*     */   }
/*     */   
/*     */   private Type parse(String paramString) {
/*  85 */     this.sig = paramString;
/*  86 */     this.sigp = 0;
/*     */     
/*  88 */     List<Type.TypeParamType> list = null;
/*  89 */     if (paramString.charAt(this.sigp) == '<') {
/*  90 */       list = parseTypeParamTypes();
/*     */     }
/*  92 */     if (paramString.charAt(this.sigp) == '(') {
/*  93 */       List<Type> list1 = parseTypeSignatures(')');
/*  94 */       Type type = parseTypeSignature();
/*  95 */       ArrayList<Type> arrayList1 = null;
/*  96 */       while (this.sigp < paramString.length() && paramString.charAt(this.sigp) == '^') {
/*  97 */         this.sigp++;
/*  98 */         if (arrayList1 == null)
/*  99 */           arrayList1 = new ArrayList(); 
/* 100 */         arrayList1.add(parseTypeSignature());
/*     */       } 
/* 102 */       return new Type.MethodType(list, list1, type, arrayList1);
/*     */     } 
/* 104 */     Type type1 = parseTypeSignature();
/* 105 */     if (list == null && this.sigp == paramString.length())
/* 106 */       return type1; 
/* 107 */     Type type2 = type1;
/* 108 */     ArrayList<Type> arrayList = null;
/* 109 */     while (this.sigp < paramString.length()) {
/* 110 */       if (arrayList == null)
/* 111 */         arrayList = new ArrayList(); 
/* 112 */       arrayList.add(parseTypeSignature());
/*     */     } 
/* 114 */     return new Type.ClassSigType(list, type2, arrayList);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private Type parseTypeSignature() {
/* 120 */     switch (this.sig.charAt(this.sigp)) {
/*     */       case 'B':
/* 122 */         this.sigp++;
/* 123 */         return new Type.SimpleType("byte");
/*     */       
/*     */       case 'C':
/* 126 */         this.sigp++;
/* 127 */         return new Type.SimpleType("char");
/*     */       
/*     */       case 'D':
/* 130 */         this.sigp++;
/* 131 */         return new Type.SimpleType("double");
/*     */       
/*     */       case 'F':
/* 134 */         this.sigp++;
/* 135 */         return new Type.SimpleType("float");
/*     */       
/*     */       case 'I':
/* 138 */         this.sigp++;
/* 139 */         return new Type.SimpleType("int");
/*     */       
/*     */       case 'J':
/* 142 */         this.sigp++;
/* 143 */         return new Type.SimpleType("long");
/*     */       
/*     */       case 'L':
/* 146 */         return parseClassTypeSignature();
/*     */       
/*     */       case 'S':
/* 149 */         this.sigp++;
/* 150 */         return new Type.SimpleType("short");
/*     */       
/*     */       case 'T':
/* 153 */         return parseTypeVariableSignature();
/*     */       
/*     */       case 'V':
/* 156 */         this.sigp++;
/* 157 */         return new Type.SimpleType("void");
/*     */       
/*     */       case 'Z':
/* 160 */         this.sigp++;
/* 161 */         return new Type.SimpleType("boolean");
/*     */       
/*     */       case '[':
/* 164 */         this.sigp++;
/* 165 */         return new Type.ArrayType(parseTypeSignature());
/*     */       
/*     */       case '*':
/* 168 */         this.sigp++;
/* 169 */         return new Type.WildcardType();
/*     */       
/*     */       case '+':
/* 172 */         this.sigp++;
/* 173 */         return new Type.WildcardType(Type.WildcardType.Kind.EXTENDS, parseTypeSignature());
/*     */       
/*     */       case '-':
/* 176 */         this.sigp++;
/* 177 */         return new Type.WildcardType(Type.WildcardType.Kind.SUPER, parseTypeSignature());
/*     */     } 
/*     */     
/* 180 */     throw new IllegalStateException(debugInfo());
/*     */   }
/*     */ 
/*     */   
/*     */   private List<Type> parseTypeSignatures(char paramChar) {
/* 185 */     this.sigp++;
/* 186 */     ArrayList<Type> arrayList = new ArrayList();
/* 187 */     while (this.sig.charAt(this.sigp) != paramChar)
/* 188 */       arrayList.add(parseTypeSignature()); 
/* 189 */     this.sigp++;
/* 190 */     return arrayList;
/*     */   }
/*     */   
/*     */   private Type parseClassTypeSignature() {
/* 194 */     assert this.sig.charAt(this.sigp) == 'L';
/* 195 */     this.sigp++;
/* 196 */     return parseClassTypeSignatureRest();
/*     */   }
/*     */   private Type parseClassTypeSignatureRest() {
/*     */     char c;
/* 200 */     StringBuilder stringBuilder = new StringBuilder();
/* 201 */     List<Type> list = null;
/* 202 */     Type.ClassType classType = null;
/*     */ 
/*     */     
/*     */     do {
/* 206 */       switch (c = this.sig.charAt(this.sigp)) {
/*     */         case '<':
/* 208 */           list = parseTypeSignatures('>');
/*     */           break;
/*     */         
/*     */         case '.':
/*     */         case ';':
/* 213 */           this.sigp++;
/* 214 */           classType = new Type.ClassType(classType, stringBuilder.toString(), list);
/* 215 */           stringBuilder.setLength(0);
/* 216 */           list = null;
/*     */           break;
/*     */         
/*     */         default:
/* 220 */           this.sigp++;
/* 221 */           stringBuilder.append(c);
/*     */           break;
/*     */       } 
/* 224 */     } while (c != ';');
/*     */     
/* 226 */     return classType;
/*     */   }
/*     */   
/*     */   private List<Type.TypeParamType> parseTypeParamTypes() {
/* 230 */     assert this.sig.charAt(this.sigp) == '<';
/* 231 */     this.sigp++;
/* 232 */     ArrayList<Type.TypeParamType> arrayList = new ArrayList();
/* 233 */     while (this.sig.charAt(this.sigp) != '>')
/* 234 */       arrayList.add(parseTypeParamType()); 
/* 235 */     this.sigp++;
/* 236 */     return arrayList;
/*     */   }
/*     */   
/*     */   private Type.TypeParamType parseTypeParamType() {
/* 240 */     int i = this.sig.indexOf(":", this.sigp);
/* 241 */     String str = this.sig.substring(this.sigp, i);
/* 242 */     Type type = null;
/* 243 */     ArrayList<Type> arrayList = null;
/* 244 */     this.sigp = i + 1;
/* 245 */     if (this.sig.charAt(this.sigp) != ':')
/* 246 */       type = parseTypeSignature(); 
/* 247 */     while (this.sig.charAt(this.sigp) == ':') {
/* 248 */       this.sigp++;
/* 249 */       if (arrayList == null)
/* 250 */         arrayList = new ArrayList(); 
/* 251 */       arrayList.add(parseTypeSignature());
/*     */     } 
/* 253 */     return new Type.TypeParamType(str, type, arrayList);
/*     */   }
/*     */   
/*     */   private Type parseTypeVariableSignature() {
/* 257 */     this.sigp++;
/* 258 */     int i = this.sig.indexOf(';', this.sigp);
/* 259 */     Type.SimpleType simpleType = new Type.SimpleType(this.sig.substring(this.sigp, i));
/* 260 */     this.sigp = i + 1;
/* 261 */     return simpleType;
/*     */   }
/*     */   
/*     */   private String debugInfo() {
/* 265 */     return this.sig.substring(0, this.sigp) + "!" + this.sig.charAt(this.sigp) + "!" + this.sig.substring(this.sigp + 1);
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\classfile\Signature.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */