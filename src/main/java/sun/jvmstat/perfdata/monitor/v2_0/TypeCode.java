/*     */ package sun.jvmstat.perfdata.monitor.v2_0;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class TypeCode
/*     */ {
/*     */   private final String name;
/*     */   private final char value;
/*  39 */   public static final TypeCode BOOLEAN = new TypeCode("boolean", 'Z');
/*  40 */   public static final TypeCode CHAR = new TypeCode("char", 'C');
/*  41 */   public static final TypeCode FLOAT = new TypeCode("float", 'F');
/*  42 */   public static final TypeCode DOUBLE = new TypeCode("double", 'D');
/*  43 */   public static final TypeCode BYTE = new TypeCode("byte", 'B');
/*  44 */   public static final TypeCode SHORT = new TypeCode("short", 'S');
/*  45 */   public static final TypeCode INT = new TypeCode("int", 'I');
/*  46 */   public static final TypeCode LONG = new TypeCode("long", 'J');
/*  47 */   public static final TypeCode OBJECT = new TypeCode("object", 'L');
/*  48 */   public static final TypeCode ARRAY = new TypeCode("array", '[');
/*  49 */   public static final TypeCode VOID = new TypeCode("void", 'V');
/*     */   
/*  51 */   private static TypeCode[] basicTypes = new TypeCode[] { LONG, BYTE, BOOLEAN, CHAR, FLOAT, DOUBLE, SHORT, INT, OBJECT, ARRAY, VOID };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/*  62 */     return this.name;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int toChar() {
/*  71 */     return this.value;
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
/*     */   public static TypeCode toTypeCode(char paramChar) {
/*  84 */     for (byte b = 0; b < basicTypes.length; b++) {
/*  85 */       if ((basicTypes[b]).value == paramChar) {
/*  86 */         return basicTypes[b];
/*     */       }
/*     */     } 
/*  89 */     throw new IllegalArgumentException();
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
/*     */   public static TypeCode toTypeCode(byte paramByte) {
/* 103 */     return toTypeCode((char)paramByte);
/*     */   }
/*     */   
/*     */   private TypeCode(String paramString, char paramChar) {
/* 107 */     this.name = paramString;
/* 108 */     this.value = paramChar;
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\sun\jvmstat\perfdata\monitor\v2_0\TypeCode.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */