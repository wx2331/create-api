/*     */ package com.sun.tools.hat.internal.model;
/*     */ 
/*     */ import com.sun.tools.hat.internal.parser.ReadBuffer;
/*     */ import java.io.IOException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class JavaObject
/*     */   extends JavaLazyReadObject
/*     */ {
/*     */   private Object clazz;
/*     */   
/*     */   public JavaObject(long paramLong1, long paramLong2) {
/*  54 */     super(paramLong2);
/*  55 */     this.clazz = makeId(paramLong1);
/*     */   }
/*     */   
/*     */   public void resolve(Snapshot paramSnapshot) {
/*  59 */     if (this.clazz instanceof JavaClass) {
/*     */       return;
/*     */     }
/*  62 */     if (this.clazz instanceof Number) {
/*  63 */       long l = getIdValue((Number)this.clazz);
/*  64 */       this.clazz = paramSnapshot.findThing(l);
/*  65 */       if (!(this.clazz instanceof JavaClass)) {
/*  66 */         int i; warn("Class " + Long.toHexString(l) + " not found, adding fake class!");
/*     */ 
/*     */         
/*  69 */         ReadBuffer readBuffer = paramSnapshot.getReadBuffer();
/*  70 */         int j = paramSnapshot.getIdentifierSize();
/*  71 */         long l1 = getOffset() + (2 * j) + 4L;
/*     */         try {
/*  73 */           i = readBuffer.getInt(l1);
/*  74 */         } catch (IOException iOException) {
/*  75 */           throw new RuntimeException(iOException);
/*     */         } 
/*  77 */         this.clazz = paramSnapshot.addFakeInstanceClass(l, i);
/*     */       } 
/*     */     } else {
/*  80 */       throw new InternalError("should not reach here");
/*     */     } 
/*     */     
/*  83 */     JavaClass javaClass = (JavaClass)this.clazz;
/*  84 */     javaClass.resolve(paramSnapshot);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  89 */     parseFields(getValue(), true);
/*     */     
/*  91 */     javaClass.addInstance(this);
/*  92 */     super.resolve(paramSnapshot);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isSameTypeAs(JavaThing paramJavaThing) {
/* 100 */     if (!(paramJavaThing instanceof JavaObject)) {
/* 101 */       return false;
/*     */     }
/* 103 */     JavaObject javaObject = (JavaObject)paramJavaThing;
/* 104 */     return getClazz().equals(javaObject.getClazz());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JavaClass getClazz() {
/* 111 */     return (JavaClass)this.clazz;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public JavaThing[] getFields() {
/* 117 */     return parseFields(getValue(), false);
/*     */   }
/*     */ 
/*     */   
/*     */   public JavaThing getField(String paramString) {
/* 122 */     JavaThing[] arrayOfJavaThing = getFields();
/* 123 */     JavaField[] arrayOfJavaField = getClazz().getFieldsForInstance();
/* 124 */     for (byte b = 0; b < arrayOfJavaField.length; b++) {
/* 125 */       if (arrayOfJavaField[b].getName().equals(paramString)) {
/* 126 */         return arrayOfJavaThing[b];
/*     */       }
/*     */     } 
/* 129 */     return null;
/*     */   }
/*     */   
/*     */   public int compareTo(JavaThing paramJavaThing) {
/* 133 */     if (paramJavaThing instanceof JavaObject) {
/* 134 */       JavaObject javaObject = (JavaObject)paramJavaThing;
/* 135 */       return getClazz().getName().compareTo(javaObject.getClazz().getName());
/*     */     } 
/* 137 */     return super.compareTo(paramJavaThing);
/*     */   }
/*     */   
/*     */   public void visitReferencedObjects(JavaHeapObjectVisitor paramJavaHeapObjectVisitor) {
/* 141 */     super.visitReferencedObjects(paramJavaHeapObjectVisitor);
/* 142 */     JavaThing[] arrayOfJavaThing = getFields();
/* 143 */     for (byte b = 0; b < arrayOfJavaThing.length; b++) {
/* 144 */       if (arrayOfJavaThing[b] != null && (
/* 145 */         !paramJavaHeapObjectVisitor.mightExclude() || 
/* 146 */         !paramJavaHeapObjectVisitor.exclude(getClazz().getClassForField(b), 
/* 147 */           getClazz().getFieldForInstance(b))))
/*     */       {
/*     */         
/* 150 */         if (arrayOfJavaThing[b] instanceof JavaHeapObject) {
/* 151 */           paramJavaHeapObjectVisitor.visit((JavaHeapObject)arrayOfJavaThing[b]);
/*     */         }
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean refersOnlyWeaklyTo(Snapshot paramSnapshot, JavaThing paramJavaThing) {
/* 158 */     if (paramSnapshot.getWeakReferenceClass() != null) {
/* 159 */       int i = paramSnapshot.getReferentFieldIndex();
/* 160 */       if (paramSnapshot.getWeakReferenceClass().isAssignableFrom(getClazz())) {
/*     */ 
/*     */ 
/*     */         
/* 164 */         JavaThing[] arrayOfJavaThing = getFields();
/* 165 */         for (byte b = 0; b < arrayOfJavaThing.length; b++) {
/* 166 */           if (b != i && arrayOfJavaThing[b] == paramJavaThing) {
/* 167 */             return false;
/*     */           }
/*     */         } 
/* 170 */         return true;
/*     */       } 
/*     */     } 
/* 173 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String describeReferenceTo(JavaThing paramJavaThing, Snapshot paramSnapshot) {
/* 181 */     JavaThing[] arrayOfJavaThing = getFields();
/* 182 */     for (byte b = 0; b < arrayOfJavaThing.length; b++) {
/* 183 */       if (arrayOfJavaThing[b] == paramJavaThing) {
/* 184 */         JavaField javaField = getClazz().getFieldForInstance(b);
/* 185 */         return "field " + javaField.getName();
/*     */       } 
/*     */     } 
/* 188 */     return super.describeReferenceTo(paramJavaThing, paramSnapshot);
/*     */   }
/*     */   
/*     */   public String toString() {
/* 192 */     if (getClazz().isString()) {
/* 193 */       JavaThing javaThing = getField("value");
/* 194 */       if (javaThing instanceof JavaValueArray) {
/* 195 */         return ((JavaValueArray)javaThing).valueString();
/*     */       }
/* 197 */       return "null";
/*     */     } 
/*     */     
/* 200 */     return super.toString();
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
/*     */   protected final int readValueLength() throws IOException {
/* 216 */     JavaClass javaClass = getClazz();
/* 217 */     int i = javaClass.getIdentifierSize();
/* 218 */     long l = getOffset() + (2 * i) + 4L;
/* 219 */     return javaClass.getReadBuffer().getInt(l);
/*     */   }
/*     */   
/*     */   protected final byte[] readValue() throws IOException {
/* 223 */     JavaClass javaClass = getClazz();
/* 224 */     int i = javaClass.getIdentifierSize();
/* 225 */     ReadBuffer readBuffer = javaClass.getReadBuffer();
/* 226 */     long l = getOffset() + (2 * i) + 4L;
/* 227 */     int j = readBuffer.getInt(l);
/* 228 */     if (j == 0) {
/* 229 */       return Snapshot.EMPTY_BYTE_ARRAY;
/*     */     }
/* 231 */     byte[] arrayOfByte = new byte[j];
/* 232 */     readBuffer.get(l + 4L, arrayOfByte);
/* 233 */     return arrayOfByte;
/*     */   }
/*     */ 
/*     */   
/*     */   private JavaThing[] parseFields(byte[] paramArrayOfbyte, boolean paramBoolean) {
/* 238 */     JavaClass javaClass1 = getClazz();
/* 239 */     int i = javaClass1.getNumFieldsForInstance();
/* 240 */     JavaField[] arrayOfJavaField = javaClass1.getFields();
/* 241 */     JavaThing[] arrayOfJavaThing = new JavaThing[i];
/* 242 */     Snapshot snapshot = javaClass1.getSnapshot();
/* 243 */     int j = snapshot.getIdentifierSize();
/* 244 */     byte b1 = 0;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 255 */     i -= arrayOfJavaField.length;
/* 256 */     JavaClass javaClass2 = javaClass1;
/* 257 */     int k = 0;
/* 258 */     for (byte b2 = 0; b2 < arrayOfJavaThing.length; b2++, b1++) {
/* 259 */       long l2; byte b; short s; char c2; int m; long l1; float f; double d; JavaObjectRef javaObjectRef; while (b1 >= arrayOfJavaField.length) {
/* 260 */         javaClass2 = javaClass2.getSuperclass();
/* 261 */         arrayOfJavaField = javaClass2.getFields();
/* 262 */         b1 = 0;
/* 263 */         i -= arrayOfJavaField.length;
/*     */       } 
/* 265 */       JavaField javaField = arrayOfJavaField[b1];
/* 266 */       char c1 = javaField.getSignature().charAt(0);
/* 267 */       switch (c1) {
/*     */         case 'L':
/*     */         case '[':
/* 270 */           l2 = objectIdAt(k, paramArrayOfbyte);
/* 271 */           k += j;
/* 272 */           javaObjectRef = new JavaObjectRef(l2);
/* 273 */           arrayOfJavaThing[i + b1] = javaObjectRef.dereference(snapshot, javaField, paramBoolean);
/*     */           break;
/*     */         
/*     */         case 'Z':
/* 277 */           b = byteAt(k, paramArrayOfbyte);
/* 278 */           k++;
/* 279 */           arrayOfJavaThing[i + b1] = new JavaBoolean((b != 0));
/*     */           break;
/*     */         
/*     */         case 'B':
/* 283 */           b = byteAt(k, paramArrayOfbyte);
/* 284 */           k++;
/* 285 */           arrayOfJavaThing[i + b1] = new JavaByte(b);
/*     */           break;
/*     */         
/*     */         case 'S':
/* 289 */           s = shortAt(k, paramArrayOfbyte);
/* 290 */           k += 2;
/* 291 */           arrayOfJavaThing[i + b1] = new JavaShort(s);
/*     */           break;
/*     */         
/*     */         case 'C':
/* 295 */           c2 = charAt(k, paramArrayOfbyte);
/* 296 */           k += 2;
/* 297 */           arrayOfJavaThing[i + b1] = new JavaChar(c2);
/*     */           break;
/*     */         
/*     */         case 'I':
/* 301 */           m = intAt(k, paramArrayOfbyte);
/* 302 */           k += 4;
/* 303 */           arrayOfJavaThing[i + b1] = new JavaInt(m);
/*     */           break;
/*     */         
/*     */         case 'J':
/* 307 */           l1 = longAt(k, paramArrayOfbyte);
/* 308 */           k += 8;
/* 309 */           arrayOfJavaThing[i + b1] = new JavaLong(l1);
/*     */           break;
/*     */         
/*     */         case 'F':
/* 313 */           f = floatAt(k, paramArrayOfbyte);
/* 314 */           k += 4;
/* 315 */           arrayOfJavaThing[i + b1] = new JavaFloat(f);
/*     */           break;
/*     */         
/*     */         case 'D':
/* 319 */           d = doubleAt(k, paramArrayOfbyte);
/* 320 */           k += 8;
/* 321 */           arrayOfJavaThing[i + b1] = new JavaDouble(d);
/*     */           break;
/*     */         
/*     */         default:
/* 325 */           throw new RuntimeException("invalid signature: " + c1);
/*     */       } 
/*     */     } 
/* 328 */     return arrayOfJavaThing;
/*     */   }
/*     */   
/*     */   private void warn(String paramString) {
/* 332 */     System.out.println("WARNING: " + paramString);
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\hat\internal\model\JavaObject.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */