/*     */ package com.sun.tools.classfile;
/*     */ 
/*     */ import java.util.Map;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ClassTranslator
/*     */   implements ConstantPool.Visitor<ConstantPool.CPInfo, Map<Object, Object>>
/*     */ {
/*     */   public ClassFile translate(ClassFile paramClassFile, Map<Object, Object> paramMap) {
/*  66 */     ClassFile classFile = (ClassFile)paramMap.get(paramClassFile);
/*  67 */     if (classFile == null) {
/*  68 */       ConstantPool constantPool = translate(paramClassFile.constant_pool, paramMap);
/*  69 */       Field[] arrayOfField = translate(paramClassFile.fields, paramClassFile.constant_pool, paramMap);
/*  70 */       Method[] arrayOfMethod = translateMethods(paramClassFile.methods, paramClassFile.constant_pool, paramMap);
/*  71 */       Attributes attributes = translateAttributes(paramClassFile.attributes, paramClassFile.constant_pool, paramMap);
/*     */ 
/*     */       
/*  74 */       if (constantPool == paramClassFile.constant_pool && arrayOfField == paramClassFile.fields && arrayOfMethod == paramClassFile.methods && attributes == paramClassFile.attributes) {
/*     */ 
/*     */ 
/*     */         
/*  78 */         classFile = paramClassFile;
/*     */       } else {
/*  80 */         classFile = new ClassFile(paramClassFile.magic, paramClassFile.minor_version, paramClassFile.major_version, constantPool, paramClassFile.access_flags, paramClassFile.this_class, paramClassFile.super_class, paramClassFile.interfaces, arrayOfField, arrayOfMethod, attributes);
/*     */       } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*  92 */       paramMap.put(paramClassFile, classFile);
/*     */     } 
/*  94 */     return classFile;
/*     */   }
/*     */   
/*     */   ConstantPool translate(ConstantPool paramConstantPool, Map<Object, Object> paramMap) {
/*  98 */     ConstantPool constantPool = (ConstantPool)paramMap.get(paramConstantPool);
/*  99 */     if (constantPool == null) {
/* 100 */       ConstantPool.CPInfo[] arrayOfCPInfo = new ConstantPool.CPInfo[paramConstantPool.size()];
/* 101 */       int i = 1; int j;
/* 102 */       for (j = 0; j < paramConstantPool.size(); ) {
/*     */         ConstantPool.CPInfo cPInfo1;
/*     */         try {
/* 105 */           cPInfo1 = paramConstantPool.get(j);
/* 106 */         } catch (InvalidIndex invalidIndex) {
/* 107 */           throw new IllegalStateException(invalidIndex);
/*     */         } 
/* 109 */         ConstantPool.CPInfo cPInfo2 = translate(cPInfo1, paramMap);
/* 110 */         i &= (cPInfo1 == cPInfo2) ? 1 : 0;
/* 111 */         arrayOfCPInfo[j] = cPInfo2;
/* 112 */         if (cPInfo1.getTag() != cPInfo2.getTag())
/* 113 */           throw new IllegalStateException(); 
/* 114 */         j += cPInfo1.size();
/*     */       } 
/*     */       
/* 117 */       if (i != 0) {
/* 118 */         constantPool = paramConstantPool;
/*     */       } else {
/* 120 */         constantPool = new ConstantPool(arrayOfCPInfo);
/*     */       } 
/* 122 */       paramMap.put(paramConstantPool, constantPool);
/*     */     } 
/* 124 */     return constantPool;
/*     */   }
/*     */   
/*     */   ConstantPool.CPInfo translate(ConstantPool.CPInfo paramCPInfo, Map<Object, Object> paramMap) {
/* 128 */     ConstantPool.CPInfo cPInfo = (ConstantPool.CPInfo)paramMap.get(paramCPInfo);
/* 129 */     if (cPInfo == null) {
/* 130 */       cPInfo = paramCPInfo.<ConstantPool.CPInfo, Map<Object, Object>>accept(this, paramMap);
/* 131 */       paramMap.put(paramCPInfo, cPInfo);
/*     */     } 
/* 133 */     return cPInfo;
/*     */   }
/*     */   
/*     */   Field[] translate(Field[] paramArrayOfField, ConstantPool paramConstantPool, Map<Object, Object> paramMap) {
/* 137 */     Field[] arrayOfField = (Field[])paramMap.get(paramArrayOfField);
/* 138 */     if (arrayOfField == null) {
/* 139 */       arrayOfField = new Field[paramArrayOfField.length];
/* 140 */       for (byte b = 0; b < paramArrayOfField.length; b++)
/* 141 */         arrayOfField[b] = translate(paramArrayOfField[b], paramConstantPool, paramMap); 
/* 142 */       if (equal(paramArrayOfField, arrayOfField))
/* 143 */         arrayOfField = paramArrayOfField; 
/* 144 */       paramMap.put(paramArrayOfField, arrayOfField);
/*     */     } 
/* 146 */     return arrayOfField;
/*     */   }
/*     */   
/*     */   Field translate(Field paramField, ConstantPool paramConstantPool, Map<Object, Object> paramMap) {
/* 150 */     Field field = (Field)paramMap.get(paramField);
/* 151 */     if (field == null) {
/* 152 */       Attributes attributes = translateAttributes(paramField.attributes, paramConstantPool, paramMap);
/*     */ 
/*     */       
/* 155 */       if (attributes == paramField.attributes) {
/* 156 */         field = paramField;
/*     */       } else {
/* 158 */         field = new Field(paramField.access_flags, paramField.name_index, paramField.descriptor, attributes);
/*     */       } 
/*     */ 
/*     */ 
/*     */       
/* 163 */       paramMap.put(paramField, field);
/*     */     } 
/* 165 */     return field;
/*     */   }
/*     */   
/*     */   Method[] translateMethods(Method[] paramArrayOfMethod, ConstantPool paramConstantPool, Map<Object, Object> paramMap) {
/* 169 */     Method[] arrayOfMethod = (Method[])paramMap.get(paramArrayOfMethod);
/* 170 */     if (arrayOfMethod == null) {
/* 171 */       arrayOfMethod = new Method[paramArrayOfMethod.length];
/* 172 */       for (byte b = 0; b < paramArrayOfMethod.length; b++)
/* 173 */         arrayOfMethod[b] = translate(paramArrayOfMethod[b], paramConstantPool, paramMap); 
/* 174 */       if (equal(paramArrayOfMethod, arrayOfMethod))
/* 175 */         arrayOfMethod = paramArrayOfMethod; 
/* 176 */       paramMap.put(paramArrayOfMethod, arrayOfMethod);
/*     */     } 
/* 178 */     return arrayOfMethod;
/*     */   }
/*     */   
/*     */   Method translate(Method paramMethod, ConstantPool paramConstantPool, Map<Object, Object> paramMap) {
/* 182 */     Method method = (Method)paramMap.get(paramMethod);
/* 183 */     if (method == null) {
/* 184 */       Attributes attributes = translateAttributes(paramMethod.attributes, paramConstantPool, paramMap);
/*     */ 
/*     */       
/* 187 */       if (attributes == paramMethod.attributes) {
/* 188 */         method = paramMethod;
/*     */       } else {
/* 190 */         method = new Method(paramMethod.access_flags, paramMethod.name_index, paramMethod.descriptor, attributes);
/*     */       } 
/*     */ 
/*     */ 
/*     */       
/* 195 */       paramMap.put(paramMethod, method);
/*     */     } 
/* 197 */     return method;
/*     */   }
/*     */ 
/*     */   
/*     */   Attributes translateAttributes(Attributes paramAttributes, ConstantPool paramConstantPool, Map<Object, Object> paramMap) {
/* 202 */     Attributes attributes = (Attributes)paramMap.get(paramAttributes);
/* 203 */     if (attributes == null) {
/* 204 */       Attribute[] arrayOfAttribute = new Attribute[paramAttributes.size()];
/* 205 */       ConstantPool constantPool = translate(paramConstantPool, paramMap);
/* 206 */       boolean bool = true;
/* 207 */       for (byte b = 0; b < paramAttributes.size(); b++) {
/* 208 */         Attribute attribute1 = paramAttributes.get(b);
/* 209 */         Attribute attribute2 = translate(attribute1, paramMap);
/* 210 */         if (attribute2 != attribute1)
/* 211 */           bool = false; 
/* 212 */         arrayOfAttribute[b] = attribute2;
/*     */       } 
/* 214 */       if (constantPool == paramConstantPool && bool) {
/* 215 */         attributes = paramAttributes;
/*     */       } else {
/* 217 */         attributes = new Attributes(constantPool, arrayOfAttribute);
/* 218 */       }  paramMap.put(paramAttributes, attributes);
/*     */     } 
/* 220 */     return attributes;
/*     */   }
/*     */   
/*     */   Attribute translate(Attribute paramAttribute, Map<Object, Object> paramMap) {
/* 224 */     Attribute attribute = (Attribute)paramMap.get(paramAttribute);
/* 225 */     if (attribute == null) {
/* 226 */       attribute = paramAttribute;
/*     */       
/* 228 */       paramMap.put(paramAttribute, attribute);
/*     */     } 
/* 230 */     return attribute;
/*     */   }
/*     */   
/*     */   private static <T> boolean equal(T[] paramArrayOfT1, T[] paramArrayOfT2) {
/* 234 */     if (paramArrayOfT1 == null || paramArrayOfT2 == null)
/* 235 */       return (paramArrayOfT1 == paramArrayOfT2); 
/* 236 */     if (paramArrayOfT1.length != paramArrayOfT2.length)
/* 237 */       return false; 
/* 238 */     for (byte b = 0; b < paramArrayOfT1.length; b++) {
/* 239 */       if (paramArrayOfT1[b] != paramArrayOfT2[b])
/* 240 */         return false; 
/*     */     } 
/* 242 */     return true;
/*     */   }
/*     */   
/*     */   public ConstantPool.CPInfo visitClass(ConstantPool.CONSTANT_Class_info paramCONSTANT_Class_info, Map<Object, Object> paramMap) {
/* 246 */     ConstantPool.CONSTANT_Class_info cONSTANT_Class_info = (ConstantPool.CONSTANT_Class_info)paramMap.get(paramCONSTANT_Class_info);
/* 247 */     if (cONSTANT_Class_info == null) {
/* 248 */       ConstantPool constantPool = translate(paramCONSTANT_Class_info.cp, paramMap);
/* 249 */       if (constantPool == paramCONSTANT_Class_info.cp) {
/* 250 */         cONSTANT_Class_info = paramCONSTANT_Class_info;
/*     */       } else {
/* 252 */         cONSTANT_Class_info = new ConstantPool.CONSTANT_Class_info(constantPool, paramCONSTANT_Class_info.name_index);
/* 253 */       }  paramMap.put(paramCONSTANT_Class_info, cONSTANT_Class_info);
/*     */     } 
/* 255 */     return paramCONSTANT_Class_info;
/*     */   }
/*     */   
/*     */   public ConstantPool.CPInfo visitDouble(ConstantPool.CONSTANT_Double_info paramCONSTANT_Double_info, Map<Object, Object> paramMap) {
/* 259 */     ConstantPool.CONSTANT_Double_info cONSTANT_Double_info = (ConstantPool.CONSTANT_Double_info)paramMap.get(paramCONSTANT_Double_info);
/* 260 */     if (cONSTANT_Double_info == null) {
/* 261 */       cONSTANT_Double_info = paramCONSTANT_Double_info;
/* 262 */       paramMap.put(paramCONSTANT_Double_info, cONSTANT_Double_info);
/*     */     } 
/* 264 */     return paramCONSTANT_Double_info;
/*     */   }
/*     */   
/*     */   public ConstantPool.CPInfo visitFieldref(ConstantPool.CONSTANT_Fieldref_info paramCONSTANT_Fieldref_info, Map<Object, Object> paramMap) {
/* 268 */     ConstantPool.CONSTANT_Fieldref_info cONSTANT_Fieldref_info = (ConstantPool.CONSTANT_Fieldref_info)paramMap.get(paramCONSTANT_Fieldref_info);
/* 269 */     if (cONSTANT_Fieldref_info == null) {
/* 270 */       ConstantPool constantPool = translate(paramCONSTANT_Fieldref_info.cp, paramMap);
/* 271 */       if (constantPool == paramCONSTANT_Fieldref_info.cp) {
/* 272 */         cONSTANT_Fieldref_info = paramCONSTANT_Fieldref_info;
/*     */       } else {
/* 274 */         cONSTANT_Fieldref_info = new ConstantPool.CONSTANT_Fieldref_info(constantPool, paramCONSTANT_Fieldref_info.class_index, paramCONSTANT_Fieldref_info.name_and_type_index);
/* 275 */       }  paramMap.put(paramCONSTANT_Fieldref_info, cONSTANT_Fieldref_info);
/*     */     } 
/* 277 */     return paramCONSTANT_Fieldref_info;
/*     */   }
/*     */   
/*     */   public ConstantPool.CPInfo visitFloat(ConstantPool.CONSTANT_Float_info paramCONSTANT_Float_info, Map<Object, Object> paramMap) {
/* 281 */     ConstantPool.CONSTANT_Float_info cONSTANT_Float_info = (ConstantPool.CONSTANT_Float_info)paramMap.get(paramCONSTANT_Float_info);
/* 282 */     if (cONSTANT_Float_info == null) {
/* 283 */       cONSTANT_Float_info = paramCONSTANT_Float_info;
/* 284 */       paramMap.put(paramCONSTANT_Float_info, cONSTANT_Float_info);
/*     */     } 
/* 286 */     return paramCONSTANT_Float_info;
/*     */   }
/*     */   
/*     */   public ConstantPool.CPInfo visitInteger(ConstantPool.CONSTANT_Integer_info paramCONSTANT_Integer_info, Map<Object, Object> paramMap) {
/* 290 */     ConstantPool.CONSTANT_Integer_info cONSTANT_Integer_info = (ConstantPool.CONSTANT_Integer_info)paramMap.get(paramCONSTANT_Integer_info);
/* 291 */     if (cONSTANT_Integer_info == null) {
/* 292 */       cONSTANT_Integer_info = paramCONSTANT_Integer_info;
/* 293 */       paramMap.put(paramCONSTANT_Integer_info, cONSTANT_Integer_info);
/*     */     } 
/* 295 */     return paramCONSTANT_Integer_info;
/*     */   }
/*     */   
/*     */   public ConstantPool.CPInfo visitInterfaceMethodref(ConstantPool.CONSTANT_InterfaceMethodref_info paramCONSTANT_InterfaceMethodref_info, Map<Object, Object> paramMap) {
/* 299 */     ConstantPool.CONSTANT_InterfaceMethodref_info cONSTANT_InterfaceMethodref_info = (ConstantPool.CONSTANT_InterfaceMethodref_info)paramMap.get(paramCONSTANT_InterfaceMethodref_info);
/* 300 */     if (cONSTANT_InterfaceMethodref_info == null) {
/* 301 */       ConstantPool constantPool = translate(paramCONSTANT_InterfaceMethodref_info.cp, paramMap);
/* 302 */       if (constantPool == paramCONSTANT_InterfaceMethodref_info.cp) {
/* 303 */         cONSTANT_InterfaceMethodref_info = paramCONSTANT_InterfaceMethodref_info;
/*     */       } else {
/* 305 */         cONSTANT_InterfaceMethodref_info = new ConstantPool.CONSTANT_InterfaceMethodref_info(constantPool, paramCONSTANT_InterfaceMethodref_info.class_index, paramCONSTANT_InterfaceMethodref_info.name_and_type_index);
/* 306 */       }  paramMap.put(paramCONSTANT_InterfaceMethodref_info, cONSTANT_InterfaceMethodref_info);
/*     */     } 
/* 308 */     return paramCONSTANT_InterfaceMethodref_info;
/*     */   }
/*     */   
/*     */   public ConstantPool.CPInfo visitInvokeDynamic(ConstantPool.CONSTANT_InvokeDynamic_info paramCONSTANT_InvokeDynamic_info, Map<Object, Object> paramMap) {
/* 312 */     ConstantPool.CONSTANT_InvokeDynamic_info cONSTANT_InvokeDynamic_info = (ConstantPool.CONSTANT_InvokeDynamic_info)paramMap.get(paramCONSTANT_InvokeDynamic_info);
/* 313 */     if (cONSTANT_InvokeDynamic_info == null) {
/* 314 */       ConstantPool constantPool = translate(paramCONSTANT_InvokeDynamic_info.cp, paramMap);
/* 315 */       if (constantPool == paramCONSTANT_InvokeDynamic_info.cp) {
/* 316 */         cONSTANT_InvokeDynamic_info = paramCONSTANT_InvokeDynamic_info;
/*     */       } else {
/* 318 */         cONSTANT_InvokeDynamic_info = new ConstantPool.CONSTANT_InvokeDynamic_info(constantPool, paramCONSTANT_InvokeDynamic_info.bootstrap_method_attr_index, paramCONSTANT_InvokeDynamic_info.name_and_type_index);
/*     */       } 
/* 320 */       paramMap.put(paramCONSTANT_InvokeDynamic_info, cONSTANT_InvokeDynamic_info);
/*     */     } 
/* 322 */     return paramCONSTANT_InvokeDynamic_info;
/*     */   }
/*     */   
/*     */   public ConstantPool.CPInfo visitLong(ConstantPool.CONSTANT_Long_info paramCONSTANT_Long_info, Map<Object, Object> paramMap) {
/* 326 */     ConstantPool.CONSTANT_Long_info cONSTANT_Long_info = (ConstantPool.CONSTANT_Long_info)paramMap.get(paramCONSTANT_Long_info);
/* 327 */     if (cONSTANT_Long_info == null) {
/* 328 */       cONSTANT_Long_info = paramCONSTANT_Long_info;
/* 329 */       paramMap.put(paramCONSTANT_Long_info, cONSTANT_Long_info);
/*     */     } 
/* 331 */     return paramCONSTANT_Long_info;
/*     */   }
/*     */   
/*     */   public ConstantPool.CPInfo visitNameAndType(ConstantPool.CONSTANT_NameAndType_info paramCONSTANT_NameAndType_info, Map<Object, Object> paramMap) {
/* 335 */     ConstantPool.CONSTANT_NameAndType_info cONSTANT_NameAndType_info = (ConstantPool.CONSTANT_NameAndType_info)paramMap.get(paramCONSTANT_NameAndType_info);
/* 336 */     if (cONSTANT_NameAndType_info == null) {
/* 337 */       ConstantPool constantPool = translate(paramCONSTANT_NameAndType_info.cp, paramMap);
/* 338 */       if (constantPool == paramCONSTANT_NameAndType_info.cp) {
/* 339 */         cONSTANT_NameAndType_info = paramCONSTANT_NameAndType_info;
/*     */       } else {
/* 341 */         cONSTANT_NameAndType_info = new ConstantPool.CONSTANT_NameAndType_info(constantPool, paramCONSTANT_NameAndType_info.name_index, paramCONSTANT_NameAndType_info.type_index);
/* 342 */       }  paramMap.put(paramCONSTANT_NameAndType_info, cONSTANT_NameAndType_info);
/*     */     } 
/* 344 */     return paramCONSTANT_NameAndType_info;
/*     */   }
/*     */   
/*     */   public ConstantPool.CPInfo visitMethodref(ConstantPool.CONSTANT_Methodref_info paramCONSTANT_Methodref_info, Map<Object, Object> paramMap) {
/* 348 */     ConstantPool.CONSTANT_Methodref_info cONSTANT_Methodref_info = (ConstantPool.CONSTANT_Methodref_info)paramMap.get(paramCONSTANT_Methodref_info);
/* 349 */     if (cONSTANT_Methodref_info == null) {
/* 350 */       ConstantPool constantPool = translate(paramCONSTANT_Methodref_info.cp, paramMap);
/* 351 */       if (constantPool == paramCONSTANT_Methodref_info.cp) {
/* 352 */         cONSTANT_Methodref_info = paramCONSTANT_Methodref_info;
/*     */       } else {
/* 354 */         cONSTANT_Methodref_info = new ConstantPool.CONSTANT_Methodref_info(constantPool, paramCONSTANT_Methodref_info.class_index, paramCONSTANT_Methodref_info.name_and_type_index);
/* 355 */       }  paramMap.put(paramCONSTANT_Methodref_info, cONSTANT_Methodref_info);
/*     */     } 
/* 357 */     return paramCONSTANT_Methodref_info;
/*     */   }
/*     */   
/*     */   public ConstantPool.CPInfo visitMethodHandle(ConstantPool.CONSTANT_MethodHandle_info paramCONSTANT_MethodHandle_info, Map<Object, Object> paramMap) {
/* 361 */     ConstantPool.CONSTANT_MethodHandle_info cONSTANT_MethodHandle_info = (ConstantPool.CONSTANT_MethodHandle_info)paramMap.get(paramCONSTANT_MethodHandle_info);
/* 362 */     if (cONSTANT_MethodHandle_info == null) {
/* 363 */       ConstantPool constantPool = translate(paramCONSTANT_MethodHandle_info.cp, paramMap);
/* 364 */       if (constantPool == paramCONSTANT_MethodHandle_info.cp) {
/* 365 */         cONSTANT_MethodHandle_info = paramCONSTANT_MethodHandle_info;
/*     */       } else {
/* 367 */         cONSTANT_MethodHandle_info = new ConstantPool.CONSTANT_MethodHandle_info(constantPool, paramCONSTANT_MethodHandle_info.reference_kind, paramCONSTANT_MethodHandle_info.reference_index);
/*     */       } 
/* 369 */       paramMap.put(paramCONSTANT_MethodHandle_info, cONSTANT_MethodHandle_info);
/*     */     } 
/* 371 */     return paramCONSTANT_MethodHandle_info;
/*     */   }
/*     */   
/*     */   public ConstantPool.CPInfo visitMethodType(ConstantPool.CONSTANT_MethodType_info paramCONSTANT_MethodType_info, Map<Object, Object> paramMap) {
/* 375 */     ConstantPool.CONSTANT_MethodType_info cONSTANT_MethodType_info = (ConstantPool.CONSTANT_MethodType_info)paramMap.get(paramCONSTANT_MethodType_info);
/* 376 */     if (cONSTANT_MethodType_info == null) {
/* 377 */       ConstantPool constantPool = translate(paramCONSTANT_MethodType_info.cp, paramMap);
/* 378 */       if (constantPool == paramCONSTANT_MethodType_info.cp) {
/* 379 */         cONSTANT_MethodType_info = paramCONSTANT_MethodType_info;
/*     */       } else {
/* 381 */         cONSTANT_MethodType_info = new ConstantPool.CONSTANT_MethodType_info(constantPool, paramCONSTANT_MethodType_info.descriptor_index);
/*     */       } 
/* 383 */       paramMap.put(paramCONSTANT_MethodType_info, cONSTANT_MethodType_info);
/*     */     } 
/* 385 */     return paramCONSTANT_MethodType_info;
/*     */   }
/*     */   
/*     */   public ConstantPool.CPInfo visitString(ConstantPool.CONSTANT_String_info paramCONSTANT_String_info, Map<Object, Object> paramMap) {
/* 389 */     ConstantPool.CONSTANT_String_info cONSTANT_String_info = (ConstantPool.CONSTANT_String_info)paramMap.get(paramCONSTANT_String_info);
/* 390 */     if (cONSTANT_String_info == null) {
/* 391 */       ConstantPool constantPool = translate(paramCONSTANT_String_info.cp, paramMap);
/* 392 */       if (constantPool == paramCONSTANT_String_info.cp) {
/* 393 */         cONSTANT_String_info = paramCONSTANT_String_info;
/*     */       } else {
/* 395 */         cONSTANT_String_info = new ConstantPool.CONSTANT_String_info(constantPool, paramCONSTANT_String_info.string_index);
/* 396 */       }  paramMap.put(paramCONSTANT_String_info, cONSTANT_String_info);
/*     */     } 
/* 398 */     return paramCONSTANT_String_info;
/*     */   }
/*     */   
/*     */   public ConstantPool.CPInfo visitUtf8(ConstantPool.CONSTANT_Utf8_info paramCONSTANT_Utf8_info, Map<Object, Object> paramMap) {
/* 402 */     ConstantPool.CONSTANT_Utf8_info cONSTANT_Utf8_info = (ConstantPool.CONSTANT_Utf8_info)paramMap.get(paramCONSTANT_Utf8_info);
/* 403 */     if (cONSTANT_Utf8_info == null) {
/* 404 */       cONSTANT_Utf8_info = paramCONSTANT_Utf8_info;
/* 405 */       paramMap.put(paramCONSTANT_Utf8_info, cONSTANT_Utf8_info);
/*     */     } 
/* 407 */     return paramCONSTANT_Utf8_info;
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\classfile\ClassTranslator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */