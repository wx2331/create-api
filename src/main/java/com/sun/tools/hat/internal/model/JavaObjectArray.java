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
/*     */ public class JavaObjectArray
/*     */   extends JavaLazyReadObject
/*     */ {
/*     */   private Object clazz;
/*     */   
/*     */   public JavaObjectArray(long paramLong1, long paramLong2) {
/*  46 */     super(paramLong2);
/*  47 */     this.clazz = makeId(paramLong1);
/*     */   }
/*     */   
/*     */   public JavaClass getClazz() {
/*  51 */     return (JavaClass)this.clazz;
/*     */   }
/*     */   
/*     */   public void resolve(Snapshot paramSnapshot) {
/*  55 */     if (this.clazz instanceof JavaClass) {
/*     */       return;
/*     */     }
/*  58 */     long l = getIdValue((Number)this.clazz);
/*  59 */     if (paramSnapshot.isNewStyleArrayClass()) {
/*     */       
/*  61 */       JavaHeapObject javaHeapObject = paramSnapshot.findThing(l);
/*  62 */       if (javaHeapObject instanceof JavaClass) {
/*  63 */         this.clazz = javaHeapObject;
/*     */       }
/*     */     } 
/*  66 */     if (!(this.clazz instanceof JavaClass)) {
/*  67 */       JavaHeapObject javaHeapObject = paramSnapshot.findThing(l);
/*  68 */       if (javaHeapObject != null && javaHeapObject instanceof JavaClass) {
/*  69 */         JavaClass javaClass = (JavaClass)javaHeapObject;
/*  70 */         String str = javaClass.getName();
/*  71 */         if (!str.startsWith("[")) {
/*  72 */           str = "L" + javaClass.getName() + ";";
/*     */         }
/*  74 */         this.clazz = paramSnapshot.getArrayClass(str);
/*     */       } 
/*     */     } 
/*     */     
/*  78 */     if (!(this.clazz instanceof JavaClass)) {
/*  79 */       this.clazz = paramSnapshot.getOtherArrayType();
/*     */     }
/*  81 */     ((JavaClass)this.clazz).addInstance(this);
/*  82 */     super.resolve(paramSnapshot);
/*     */   }
/*     */   
/*     */   public JavaThing[] getValues() {
/*  86 */     return getElements();
/*     */   }
/*     */   
/*     */   public JavaThing[] getElements() {
/*  90 */     Snapshot snapshot = getClazz().getSnapshot();
/*  91 */     byte[] arrayOfByte = getValue();
/*  92 */     int i = snapshot.getIdentifierSize();
/*  93 */     int j = arrayOfByte.length / i;
/*  94 */     JavaThing[] arrayOfJavaThing = new JavaThing[j];
/*  95 */     int k = 0;
/*  96 */     for (byte b = 0; b < arrayOfJavaThing.length; b++) {
/*  97 */       long l = objectIdAt(k, arrayOfByte);
/*  98 */       k += i;
/*  99 */       arrayOfJavaThing[b] = snapshot.findThing(l);
/*     */     } 
/* 101 */     return arrayOfJavaThing;
/*     */   }
/*     */   
/*     */   public int compareTo(JavaThing paramJavaThing) {
/* 105 */     if (paramJavaThing instanceof JavaObjectArray) {
/* 106 */       return 0;
/*     */     }
/* 108 */     return super.compareTo(paramJavaThing);
/*     */   }
/*     */   
/*     */   public int getLength() {
/* 112 */     return getValueLength() / getClazz().getIdentifierSize();
/*     */   }
/*     */   
/*     */   public void visitReferencedObjects(JavaHeapObjectVisitor paramJavaHeapObjectVisitor) {
/* 116 */     super.visitReferencedObjects(paramJavaHeapObjectVisitor);
/* 117 */     JavaThing[] arrayOfJavaThing = getElements();
/* 118 */     for (byte b = 0; b < arrayOfJavaThing.length; b++) {
/* 119 */       if (arrayOfJavaThing[b] != null && arrayOfJavaThing[b] instanceof JavaHeapObject) {
/* 120 */         paramJavaHeapObjectVisitor.visit((JavaHeapObject)arrayOfJavaThing[b]);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String describeReferenceTo(JavaThing paramJavaThing, Snapshot paramSnapshot) {
/* 130 */     JavaThing[] arrayOfJavaThing = getElements();
/* 131 */     for (byte b = 0; b < arrayOfJavaThing.length; b++) {
/* 132 */       if (arrayOfJavaThing[b] == paramJavaThing) {
/* 133 */         return "Element " + b + " of " + this;
/*     */       }
/*     */     } 
/* 136 */     return super.describeReferenceTo(paramJavaThing, paramSnapshot);
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
/*     */   protected final int readValueLength() throws IOException {
/* 150 */     JavaClass javaClass = getClazz();
/* 151 */     ReadBuffer readBuffer = javaClass.getReadBuffer();
/* 152 */     int i = javaClass.getIdentifierSize();
/* 153 */     long l = getOffset() + i + 4L;
/* 154 */     int j = readBuffer.getInt(l);
/* 155 */     return j * javaClass.getIdentifierSize();
/*     */   }
/*     */   
/*     */   protected final byte[] readValue() throws IOException {
/* 159 */     JavaClass javaClass = getClazz();
/* 160 */     ReadBuffer readBuffer = javaClass.getReadBuffer();
/* 161 */     int i = javaClass.getIdentifierSize();
/* 162 */     long l = getOffset() + i + 4L;
/* 163 */     int j = readBuffer.getInt(l);
/* 164 */     if (j == 0) {
/* 165 */       return Snapshot.EMPTY_BYTE_ARRAY;
/*     */     }
/* 167 */     byte[] arrayOfByte = new byte[j * i];
/* 168 */     readBuffer.get(l + 4L + i, arrayOfByte);
/* 169 */     return arrayOfByte;
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\hat\internal\model\JavaObjectArray.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */