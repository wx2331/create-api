/*     */ package com.sun.tools.internal.ws.processor.modeler;
/*     */ 
/*     */ import com.sun.tools.internal.ws.processor.model.java.JavaSimpleType;
/*     */ import java.util.HashMap;
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
/*     */ public final class JavaSimpleTypeCreator
/*     */ {
/*  43 */   public static final JavaSimpleType BOOLEAN_JAVATYPE = new JavaSimpleType(ModelerConstants.BOOLEAN_CLASSNAME.getValue(), ModelerConstants.FALSE_STR.getValue());
/*  44 */   public static final JavaSimpleType BOXED_BOOLEAN_JAVATYPE = new JavaSimpleType(ModelerConstants.BOXED_BOOLEAN_CLASSNAME.getValue(), ModelerConstants.NULL_STR.getValue());
/*  45 */   public static final JavaSimpleType BYTE_JAVATYPE = new JavaSimpleType(ModelerConstants.BYTE_CLASSNAME.getValue(), "(byte)" + ModelerConstants.ZERO_STR.getValue());
/*  46 */   public static final JavaSimpleType BYTE_ARRAY_JAVATYPE = new JavaSimpleType(ModelerConstants.BYTE_ARRAY_CLASSNAME.getValue(), ModelerConstants.NULL_STR.getValue());
/*  47 */   public static final JavaSimpleType BOXED_BYTE_JAVATYPE = new JavaSimpleType(ModelerConstants.BOXED_BYTE_CLASSNAME.getValue(), ModelerConstants.NULL_STR.getValue());
/*  48 */   public static final JavaSimpleType BOXED_BYTE_ARRAY_JAVATYPE = new JavaSimpleType(ModelerConstants.BOXED_BYTE_ARRAY_CLASSNAME.getValue(), ModelerConstants.NULL_STR.getValue());
/*  49 */   public static final JavaSimpleType DOUBLE_JAVATYPE = new JavaSimpleType(ModelerConstants.DOUBLE_CLASSNAME.getValue(), ModelerConstants.ZERO_STR.getValue());
/*  50 */   public static final JavaSimpleType BOXED_DOUBLE_JAVATYPE = new JavaSimpleType(ModelerConstants.BOXED_DOUBLE_CLASSNAME.getValue(), ModelerConstants.NULL_STR.getValue());
/*  51 */   public static final JavaSimpleType FLOAT_JAVATYPE = new JavaSimpleType(ModelerConstants.FLOAT_CLASSNAME.getValue(), ModelerConstants.ZERO_STR.getValue());
/*  52 */   public static final JavaSimpleType BOXED_FLOAT_JAVATYPE = new JavaSimpleType(ModelerConstants.BOXED_FLOAT_CLASSNAME.getValue(), ModelerConstants.NULL_STR.getValue());
/*  53 */   public static final JavaSimpleType INT_JAVATYPE = new JavaSimpleType(ModelerConstants.INT_CLASSNAME.getValue(), ModelerConstants.ZERO_STR.getValue());
/*  54 */   public static final JavaSimpleType BOXED_INTEGER_JAVATYPE = new JavaSimpleType(ModelerConstants.BOXED_INTEGER_CLASSNAME.getValue(), ModelerConstants.NULL_STR.getValue());
/*  55 */   public static final JavaSimpleType LONG_JAVATYPE = new JavaSimpleType(ModelerConstants.LONG_CLASSNAME.getValue(), ModelerConstants.ZERO_STR.getValue());
/*  56 */   public static final JavaSimpleType BOXED_LONG_JAVATYPE = new JavaSimpleType(ModelerConstants.BOXED_LONG_CLASSNAME.getValue(), ModelerConstants.NULL_STR.getValue());
/*  57 */   public static final JavaSimpleType SHORT_JAVATYPE = new JavaSimpleType(ModelerConstants.SHORT_CLASSNAME.getValue(), "(short)" + ModelerConstants.ZERO_STR.getValue());
/*  58 */   public static final JavaSimpleType BOXED_SHORT_JAVATYPE = new JavaSimpleType(ModelerConstants.BOXED_SHORT_CLASSNAME.getValue(), ModelerConstants.NULL_STR.getValue());
/*  59 */   public static final JavaSimpleType DECIMAL_JAVATYPE = new JavaSimpleType(ModelerConstants.BIGDECIMAL_CLASSNAME.getValue(), ModelerConstants.NULL_STR.getValue());
/*  60 */   public static final JavaSimpleType BIG_INTEGER_JAVATYPE = new JavaSimpleType(ModelerConstants.BIGINTEGER_CLASSNAME.getValue(), ModelerConstants.NULL_STR.getValue());
/*  61 */   public static final JavaSimpleType CALENDAR_JAVATYPE = new JavaSimpleType(ModelerConstants.CALENDAR_CLASSNAME.getValue(), ModelerConstants.NULL_STR.getValue());
/*  62 */   public static final JavaSimpleType DATE_JAVATYPE = new JavaSimpleType(ModelerConstants.DATE_CLASSNAME.getValue(), ModelerConstants.NULL_STR.getValue());
/*  63 */   public static final JavaSimpleType STRING_JAVATYPE = new JavaSimpleType(ModelerConstants.STRING_CLASSNAME.getValue(), ModelerConstants.NULL_STR.getValue());
/*  64 */   public static final JavaSimpleType STRING_ARRAY_JAVATYPE = new JavaSimpleType(ModelerConstants.STRING_ARRAY_CLASSNAME.getValue(), ModelerConstants.NULL_STR.getValue());
/*  65 */   public static final JavaSimpleType QNAME_JAVATYPE = new JavaSimpleType(ModelerConstants.QNAME_CLASSNAME.getValue(), ModelerConstants.NULL_STR.getValue());
/*  66 */   public static final JavaSimpleType VOID_JAVATYPE = new JavaSimpleType(ModelerConstants.VOID_CLASSNAME.getValue(), null);
/*  67 */   public static final JavaSimpleType OBJECT_JAVATYPE = new JavaSimpleType(ModelerConstants.OBJECT_CLASSNAME.getValue(), null);
/*  68 */   public static final JavaSimpleType SOAPELEMENT_JAVATYPE = new JavaSimpleType(ModelerConstants.SOAPELEMENT_CLASSNAME.getValue(), null);
/*  69 */   public static final JavaSimpleType URI_JAVATYPE = new JavaSimpleType(ModelerConstants.URI_CLASSNAME.getValue(), null);
/*     */ 
/*     */   
/*  72 */   public static final JavaSimpleType IMAGE_JAVATYPE = new JavaSimpleType(ModelerConstants.IMAGE_CLASSNAME.getValue(), null);
/*  73 */   public static final JavaSimpleType MIME_MULTIPART_JAVATYPE = new JavaSimpleType(ModelerConstants.MIME_MULTIPART_CLASSNAME.getValue(), null);
/*  74 */   public static final JavaSimpleType SOURCE_JAVATYPE = new JavaSimpleType(ModelerConstants.SOURCE_CLASSNAME.getValue(), null);
/*  75 */   public static final JavaSimpleType DATA_HANDLER_JAVATYPE = new JavaSimpleType(ModelerConstants.DATA_HANDLER_CLASSNAME.getValue(), null);
/*     */ 
/*     */   
/*  78 */   private static final Map<String, JavaSimpleType> JAVA_TYPES = new HashMap<>(31);
/*     */   
/*     */   static {
/*  81 */     JAVA_TYPES.put(ModelerConstants.BOOLEAN_CLASSNAME.getValue(), BOOLEAN_JAVATYPE);
/*  82 */     JAVA_TYPES.put(ModelerConstants.BOXED_BOOLEAN_CLASSNAME.getValue(), BOXED_BOOLEAN_JAVATYPE);
/*  83 */     JAVA_TYPES.put(ModelerConstants.BYTE_CLASSNAME.getValue(), BYTE_JAVATYPE);
/*  84 */     JAVA_TYPES.put(ModelerConstants.BYTE_ARRAY_CLASSNAME.getValue(), BYTE_ARRAY_JAVATYPE);
/*  85 */     JAVA_TYPES.put(ModelerConstants.BOXED_BYTE_CLASSNAME.getValue(), BOXED_BYTE_JAVATYPE);
/*  86 */     JAVA_TYPES.put(ModelerConstants.BOXED_BYTE_ARRAY_CLASSNAME.getValue(), BOXED_BYTE_ARRAY_JAVATYPE);
/*  87 */     JAVA_TYPES.put(ModelerConstants.DOUBLE_CLASSNAME.getValue(), DOUBLE_JAVATYPE);
/*  88 */     JAVA_TYPES.put(ModelerConstants.BOXED_DOUBLE_CLASSNAME.getValue(), BOXED_DOUBLE_JAVATYPE);
/*  89 */     JAVA_TYPES.put(ModelerConstants.FLOAT_CLASSNAME.getValue(), FLOAT_JAVATYPE);
/*  90 */     JAVA_TYPES.put(ModelerConstants.BOXED_FLOAT_CLASSNAME.getValue(), BOXED_FLOAT_JAVATYPE);
/*  91 */     JAVA_TYPES.put(ModelerConstants.INT_CLASSNAME.getValue(), INT_JAVATYPE);
/*  92 */     JAVA_TYPES.put(ModelerConstants.BOXED_INTEGER_CLASSNAME.getValue(), BOXED_INTEGER_JAVATYPE);
/*  93 */     JAVA_TYPES.put(ModelerConstants.LONG_CLASSNAME.getValue(), LONG_JAVATYPE);
/*  94 */     JAVA_TYPES.put(ModelerConstants.BOXED_LONG_CLASSNAME.getValue(), BOXED_LONG_JAVATYPE);
/*  95 */     JAVA_TYPES.put(ModelerConstants.SHORT_CLASSNAME.getValue(), SHORT_JAVATYPE);
/*  96 */     JAVA_TYPES.put(ModelerConstants.BOXED_SHORT_CLASSNAME.getValue(), BOXED_SHORT_JAVATYPE);
/*  97 */     JAVA_TYPES.put(ModelerConstants.BIGDECIMAL_CLASSNAME.getValue(), DECIMAL_JAVATYPE);
/*  98 */     JAVA_TYPES.put(ModelerConstants.BIGINTEGER_CLASSNAME.getValue(), BIG_INTEGER_JAVATYPE);
/*  99 */     JAVA_TYPES.put(ModelerConstants.CALENDAR_CLASSNAME.getValue(), CALENDAR_JAVATYPE);
/* 100 */     JAVA_TYPES.put(ModelerConstants.DATE_CLASSNAME.getValue(), DATE_JAVATYPE);
/* 101 */     JAVA_TYPES.put(ModelerConstants.STRING_CLASSNAME.getValue(), STRING_JAVATYPE);
/* 102 */     JAVA_TYPES.put(ModelerConstants.STRING_ARRAY_CLASSNAME.getValue(), STRING_ARRAY_JAVATYPE);
/* 103 */     JAVA_TYPES.put(ModelerConstants.QNAME_CLASSNAME.getValue(), QNAME_JAVATYPE);
/* 104 */     JAVA_TYPES.put(ModelerConstants.VOID_CLASSNAME.getValue(), VOID_JAVATYPE);
/* 105 */     JAVA_TYPES.put(ModelerConstants.OBJECT_CLASSNAME.getValue(), OBJECT_JAVATYPE);
/* 106 */     JAVA_TYPES.put(ModelerConstants.SOAPELEMENT_CLASSNAME.getValue(), SOAPELEMENT_JAVATYPE);
/* 107 */     JAVA_TYPES.put(ModelerConstants.URI_CLASSNAME.getValue(), URI_JAVATYPE);
/* 108 */     JAVA_TYPES.put(ModelerConstants.IMAGE_CLASSNAME.getValue(), IMAGE_JAVATYPE);
/* 109 */     JAVA_TYPES.put(ModelerConstants.MIME_MULTIPART_CLASSNAME.getValue(), MIME_MULTIPART_JAVATYPE);
/* 110 */     JAVA_TYPES.put(ModelerConstants.SOURCE_CLASSNAME.getValue(), SOURCE_JAVATYPE);
/* 111 */     JAVA_TYPES.put(ModelerConstants.DATA_HANDLER_CLASSNAME.getValue(), DATA_HANDLER_JAVATYPE);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static JavaSimpleType getJavaSimpleType(String className) {
/* 119 */     return JAVA_TYPES.get(className);
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\ws\processor\modeler\JavaSimpleTypeCreator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */