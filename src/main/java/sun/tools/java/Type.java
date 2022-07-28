/*     */ package sun.tools.java;
/*     */ 
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
/*     */ public class Type
/*     */   implements Constants
/*     */ {
/*  60 */   private static final Hashtable typeHash = new Hashtable<>(231);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected int typeCode;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String typeSig;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  79 */   public static final Type[] noArgs = new Type[0];
/*  80 */   public static final Type tError = new Type(13, "?");
/*  81 */   public static final Type tPackage = new Type(13, ".");
/*  82 */   public static final Type tNull = new Type(8, "*");
/*  83 */   public static final Type tVoid = new Type(11, "V");
/*  84 */   public static final Type tBoolean = new Type(0, "Z");
/*  85 */   public static final Type tByte = new Type(1, "B");
/*  86 */   public static final Type tChar = new Type(2, "C");
/*  87 */   public static final Type tShort = new Type(3, "S");
/*  88 */   public static final Type tInt = new Type(4, "I");
/*  89 */   public static final Type tFloat = new Type(6, "F");
/*  90 */   public static final Type tLong = new Type(5, "J");
/*  91 */   public static final Type tDouble = new Type(7, "D");
/*  92 */   public static final Type tObject = tClass(idJavaLangObject);
/*  93 */   public static final Type tClassDesc = tClass(idJavaLangClass);
/*  94 */   public static final Type tString = tClass(idJavaLangString);
/*  95 */   public static final Type tCloneable = tClass(idJavaLangCloneable);
/*  96 */   public static final Type tSerializable = tClass(idJavaIoSerializable);
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Type(int paramInt, String paramString) {
/* 102 */     this.typeCode = paramInt;
/* 103 */     this.typeSig = paramString;
/* 104 */     typeHash.put(paramString, this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final String getTypeSignature() {
/* 111 */     return this.typeSig;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final int getTypeCode() {
/* 118 */     return this.typeCode;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final int getTypeMask() {
/* 128 */     return 1 << this.typeCode;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final boolean isType(int paramInt) {
/* 135 */     return (this.typeCode == paramInt);
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
/*     */   public boolean isVoidArray() {
/* 148 */     if (!isType(9)) {
/* 149 */       return false;
/*     */     }
/*     */     
/* 152 */     Type type = this;
/* 153 */     while (type.isType(9)) {
/* 154 */       type = type.getElementType();
/*     */     }
/* 156 */     return type.isType(11);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final boolean inMask(int paramInt) {
/* 164 */     return ((1 << this.typeCode & paramInt) != 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static synchronized Type tArray(Type paramType) {
/* 171 */     String str = new String("[" + paramType.getTypeSignature());
/* 172 */     Type type = (Type)typeHash.get(str);
/* 173 */     if (type == null) {
/* 174 */       type = new ArrayType(str, paramType);
/*     */     }
/* 176 */     return type;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Type getElementType() {
/* 184 */     throw new CompilerError("getElementType");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getArrayDimension() {
/* 192 */     return 0;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static synchronized Type tClass(Identifier paramIdentifier) {
/* 200 */     if (paramIdentifier.isInner()) {
/* 201 */       Type type1 = tClass(mangleInnerType(paramIdentifier));
/* 202 */       if (type1.getClassName() != paramIdentifier)
/*     */       {
/*     */         
/* 205 */         changeClassName(type1.getClassName(), paramIdentifier); } 
/* 206 */       return type1;
/*     */     } 
/*     */     
/* 209 */     if (paramIdentifier.typeObject != null) {
/* 210 */       return paramIdentifier.typeObject;
/*     */     }
/*     */ 
/*     */     
/* 214 */     String str = new String("L" + paramIdentifier.toString().replace('.', '/') + ";");
/*     */     
/* 216 */     Type type = (Type)typeHash.get(str);
/* 217 */     if (type == null) {
/* 218 */       type = new ClassType(str, paramIdentifier);
/*     */     }
/*     */     
/* 221 */     paramIdentifier.typeObject = type;
/* 222 */     return type;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Identifier getClassName() {
/* 229 */     throw new CompilerError("getClassName:" + this);
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
/*     */   public static Identifier mangleInnerType(Identifier paramIdentifier) {
/* 242 */     if (!paramIdentifier.isInner()) return paramIdentifier; 
/* 243 */     Identifier identifier = Identifier.lookup(paramIdentifier
/* 244 */         .getFlatName().toString()
/* 245 */         .replace('.', '$'));
/* 246 */     if (identifier.isInner()) throw new CompilerError("mangle " + identifier); 
/* 247 */     return Identifier.lookup(paramIdentifier.getQualifier(), identifier);
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
/*     */   static void changeClassName(Identifier paramIdentifier1, Identifier paramIdentifier2) {
/* 263 */     ((ClassType)tClass(paramIdentifier1)).className = paramIdentifier2;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static synchronized Type tMethod(Type paramType) {
/* 270 */     return tMethod(paramType, noArgs);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static synchronized Type tMethod(Type paramType, Type[] paramArrayOfType) {
/* 277 */     StringBuffer stringBuffer = new StringBuffer();
/* 278 */     stringBuffer.append("(");
/* 279 */     for (byte b = 0; b < paramArrayOfType.length; b++) {
/* 280 */       stringBuffer.append(paramArrayOfType[b].getTypeSignature());
/*     */     }
/* 282 */     stringBuffer.append(")");
/* 283 */     stringBuffer.append(paramType.getTypeSignature());
/*     */     
/* 285 */     String str = stringBuffer.toString();
/* 286 */     Type type = (Type)typeHash.get(str);
/* 287 */     if (type == null) {
/* 288 */       type = new MethodType(str, paramType, paramArrayOfType);
/*     */     }
/* 290 */     return type;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Type getReturnType() {
/* 297 */     throw new CompilerError("getReturnType");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Type[] getArgumentTypes() {
/* 304 */     throw new CompilerError("getArgumentTypes");
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static synchronized Type tType(String paramString) {
/*     */     Type[] arrayOfType1;
/*     */     byte b1, b2;
/* 312 */     Type arrayOfType2[], type = (Type)typeHash.get(paramString);
/* 313 */     if (type != null) {
/* 314 */       return type;
/*     */     }
/*     */     
/* 317 */     switch (paramString.charAt(0)) {
/*     */       case '[':
/* 319 */         return tArray(tType(paramString.substring(1)));
/*     */       
/*     */       case 'L':
/* 322 */         return tClass(Identifier.lookup(paramString.substring(1, paramString.length() - 1).replace('/', '.')));
/*     */       
/*     */       case '(':
/* 325 */         arrayOfType1 = new Type[8];
/* 326 */         b1 = 0;
/*     */ 
/*     */         
/* 329 */         for (b2 = 1; paramString.charAt(b2) != ')'; b2 = b) {
/* 330 */           byte b; for (b = b2; paramString.charAt(b) == '['; b++);
/* 331 */           if (paramString.charAt(b++) == 'L') {
/* 332 */             while (paramString.charAt(b++) != ';');
/*     */           }
/* 334 */           if (b1 == arrayOfType1.length) {
/* 335 */             Type[] arrayOfType = new Type[b1 * 2];
/* 336 */             System.arraycopy(arrayOfType1, 0, arrayOfType, 0, b1);
/* 337 */             arrayOfType1 = arrayOfType;
/*     */           } 
/* 339 */           arrayOfType1[b1++] = tType(paramString.substring(b2, b));
/*     */         } 
/*     */         
/* 342 */         arrayOfType2 = new Type[b1];
/* 343 */         System.arraycopy(arrayOfType1, 0, arrayOfType2, 0, b1);
/* 344 */         return tMethod(tType(paramString.substring(b2 + 1)), arrayOfType2);
/*     */     } 
/*     */ 
/*     */     
/* 348 */     throw new CompilerError("invalid TypeSignature:" + paramString);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equalArguments(Type paramType) {
/* 357 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int stackSize() {
/* 366 */     switch (this.typeCode) {
/*     */       case 11:
/*     */       case 13:
/* 369 */         return 0;
/*     */       case 0:
/*     */       case 1:
/*     */       case 2:
/*     */       case 3:
/*     */       case 4:
/*     */       case 6:
/*     */       case 9:
/*     */       case 10:
/* 378 */         return 1;
/*     */       case 5:
/*     */       case 7:
/* 381 */         return 2;
/*     */     } 
/* 383 */     throw new CompilerError("stackSize " + toString());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getTypeCodeOffset() {
/* 394 */     switch (this.typeCode) {
/*     */       case 0:
/*     */       case 1:
/*     */       case 2:
/*     */       case 3:
/*     */       case 4:
/* 400 */         return 0;
/*     */       case 5:
/* 402 */         return 1;
/*     */       case 6:
/* 404 */         return 2;
/*     */       case 7:
/* 406 */         return 3;
/*     */       case 8:
/*     */       case 9:
/*     */       case 10:
/* 410 */         return 4;
/*     */     } 
/* 412 */     throw new CompilerError("invalid typecode: " + this.typeCode);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String typeString(String paramString, boolean paramBoolean1, boolean paramBoolean2) {
/* 420 */     String str = null;
/*     */     
/* 422 */     switch (this.typeCode) { case 8:
/* 423 */         str = "null"; break;
/* 424 */       case 11: str = "void"; break;
/* 425 */       case 0: str = "boolean"; break;
/* 426 */       case 1: str = "byte"; break;
/* 427 */       case 2: str = "char"; break;
/* 428 */       case 3: str = "short"; break;
/* 429 */       case 4: str = "int"; break;
/* 430 */       case 5: str = "long"; break;
/* 431 */       case 6: str = "float"; break;
/* 432 */       case 7: str = "double"; break;
/* 433 */       case 13: str = "<error>";
/* 434 */         if (this == tPackage) str = "<package>";  break;
/*     */       default:
/* 436 */         str = "unknown";
/*     */         break; }
/*     */     
/* 439 */     return (paramString.length() > 0) ? (str + " " + paramString) : str;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String typeString(String paramString) {
/* 446 */     return typeString(paramString, false, true);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 453 */     return typeString("", false, true);
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\sun\tools\java\Type.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */