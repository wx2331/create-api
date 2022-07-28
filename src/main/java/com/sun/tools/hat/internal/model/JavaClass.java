/*     */ package com.sun.tools.hat.internal.model;
/*     */ 
/*     */ import com.sun.tools.hat.internal.parser.ReadBuffer;
/*     */ import com.sun.tools.hat.internal.util.CompositeEnumeration;
/*     */ import java.util.Enumeration;
/*     */ import java.util.Vector;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class JavaClass
/*     */   extends JavaHeapObject
/*     */ {
/*     */   private long id;
/*     */   private String name;
/*     */   private JavaThing superclass;
/*     */   private JavaThing loader;
/*     */   private JavaThing signers;
/*     */   private JavaThing protectionDomain;
/*     */   private JavaField[] fields;
/*     */   private JavaStatic[] statics;
/*  63 */   private static final JavaClass[] EMPTY_CLASS_ARRAY = new JavaClass[0];
/*     */   
/*  65 */   private JavaClass[] subclasses = EMPTY_CLASS_ARRAY;
/*     */ 
/*     */   
/*  68 */   private Vector<JavaHeapObject> instances = new Vector<>();
/*     */ 
/*     */ 
/*     */   
/*     */   private Snapshot mySnapshot;
/*     */ 
/*     */   
/*     */   private int instanceSize;
/*     */ 
/*     */   
/*     */   private int totalNumFields;
/*     */ 
/*     */ 
/*     */   
/*     */   public JavaClass(long paramLong1, String paramString, long paramLong2, long paramLong3, long paramLong4, long paramLong5, JavaField[] paramArrayOfJavaField, JavaStatic[] paramArrayOfJavaStatic, int paramInt) {
/*  83 */     this.id = paramLong1;
/*  84 */     this.name = paramString;
/*  85 */     this.superclass = new JavaObjectRef(paramLong2);
/*  86 */     this.loader = new JavaObjectRef(paramLong3);
/*  87 */     this.signers = new JavaObjectRef(paramLong4);
/*  88 */     this.protectionDomain = new JavaObjectRef(paramLong5);
/*  89 */     this.fields = paramArrayOfJavaField;
/*  90 */     this.statics = paramArrayOfJavaStatic;
/*  91 */     this.instanceSize = paramInt;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JavaClass(String paramString, long paramLong1, long paramLong2, long paramLong3, long paramLong4, JavaField[] paramArrayOfJavaField, JavaStatic[] paramArrayOfJavaStatic, int paramInt) {
/*  98 */     this(-1L, paramString, paramLong1, paramLong2, paramLong3, paramLong4, paramArrayOfJavaField, paramArrayOfJavaStatic, paramInt);
/*     */   }
/*     */ 
/*     */   
/*     */   public final JavaClass getClazz() {
/* 103 */     return this.mySnapshot.getJavaLangClass();
/*     */   }
/*     */   
/*     */   public final int getIdentifierSize() {
/* 107 */     return this.mySnapshot.getIdentifierSize();
/*     */   }
/*     */   
/*     */   public final int getMinimumObjectSize() {
/* 111 */     return this.mySnapshot.getMinimumObjectSize();
/*     */   }
/*     */   
/*     */   public void resolve(Snapshot paramSnapshot) {
/* 115 */     if (this.mySnapshot != null) {
/*     */       return;
/*     */     }
/* 118 */     this.mySnapshot = paramSnapshot;
/* 119 */     resolveSuperclass(paramSnapshot);
/* 120 */     if (this.superclass != null) {
/* 121 */       ((JavaClass)this.superclass).addSubclass(this);
/*     */     }
/*     */     
/* 124 */     this.loader = this.loader.dereference(paramSnapshot, null);
/* 125 */     this.signers = this.signers.dereference(paramSnapshot, null);
/* 126 */     this.protectionDomain = this.protectionDomain.dereference(paramSnapshot, null);
/*     */     
/* 128 */     for (byte b = 0; b < this.statics.length; b++) {
/* 129 */       this.statics[b].resolve(this, paramSnapshot);
/*     */     }
/* 131 */     paramSnapshot.getJavaLangClass().addInstance(this);
/* 132 */     super.resolve(paramSnapshot);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void resolveSuperclass(Snapshot paramSnapshot) {
/* 143 */     if (this.superclass != null) {
/*     */ 
/*     */       
/* 146 */       this.totalNumFields = this.fields.length;
/* 147 */       this.superclass = this.superclass.dereference(paramSnapshot, null);
/* 148 */       if (this.superclass == paramSnapshot.getNullThing()) {
/* 149 */         this.superclass = null;
/*     */       } else {
/*     */         try {
/* 152 */           JavaClass javaClass = (JavaClass)this.superclass;
/* 153 */           javaClass.resolveSuperclass(paramSnapshot);
/* 154 */           this.totalNumFields += javaClass.totalNumFields;
/* 155 */         } catch (ClassCastException classCastException) {
/* 156 */           System.out.println("Warning!  Superclass of " + this.name + " is " + this.superclass);
/* 157 */           this.superclass = null;
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean isString() {
/* 164 */     return (this.mySnapshot.getJavaLangString() == this);
/*     */   }
/*     */   
/*     */   public boolean isClassLoader() {
/* 168 */     return this.mySnapshot.getJavaLangClassLoader().isAssignableFrom(this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JavaField getField(int paramInt) {
/* 175 */     if (paramInt < 0 || paramInt >= this.fields.length) {
/* 176 */       throw new Error("No field " + paramInt + " for " + this.name);
/*     */     }
/* 178 */     return this.fields[paramInt];
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getNumFieldsForInstance() {
/* 186 */     return this.totalNumFields;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JavaField getFieldForInstance(int paramInt) {
/* 194 */     if (this.superclass != null) {
/* 195 */       JavaClass javaClass = (JavaClass)this.superclass;
/* 196 */       if (paramInt < javaClass.totalNumFields) {
/* 197 */         return javaClass.getFieldForInstance(paramInt);
/*     */       }
/* 199 */       paramInt -= javaClass.totalNumFields;
/*     */     } 
/* 201 */     return getField(paramInt);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JavaClass getClassForField(int paramInt) {
/* 211 */     if (this.superclass != null) {
/* 212 */       JavaClass javaClass = (JavaClass)this.superclass;
/* 213 */       if (paramInt < javaClass.totalNumFields) {
/* 214 */         return javaClass.getClassForField(paramInt);
/*     */       }
/*     */     } 
/* 217 */     return this;
/*     */   }
/*     */   
/*     */   public long getId() {
/* 221 */     return this.id;
/*     */   }
/*     */   
/*     */   public String getName() {
/* 225 */     return this.name;
/*     */   }
/*     */   
/*     */   public boolean isArray() {
/* 229 */     return (this.name.indexOf('[') != -1);
/*     */   }
/*     */   
/*     */   public Enumeration getInstances(boolean paramBoolean) {
/* 233 */     if (paramBoolean) {
/* 234 */       CompositeEnumeration compositeEnumeration; Enumeration<JavaHeapObject> enumeration = this.instances.elements();
/* 235 */       for (byte b = 0; b < this.subclasses.length; b++)
/*     */       {
/* 237 */         compositeEnumeration = new CompositeEnumeration(enumeration, this.subclasses[b].getInstances(true));
/*     */       }
/* 239 */       return (Enumeration)compositeEnumeration;
/*     */     } 
/* 241 */     return this.instances.elements();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getInstancesCount(boolean paramBoolean) {
/* 249 */     int i = this.instances.size();
/* 250 */     if (paramBoolean) {
/* 251 */       for (byte b = 0; b < this.subclasses.length; b++) {
/* 252 */         i += this.subclasses[b].getInstancesCount(paramBoolean);
/*     */       }
/*     */     }
/* 255 */     return i;
/*     */   }
/*     */   
/*     */   public JavaClass[] getSubclasses() {
/* 259 */     return this.subclasses;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JavaClass getSuperclass() {
/* 266 */     return (JavaClass)this.superclass;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JavaThing getLoader() {
/* 273 */     return this.loader;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isBootstrap() {
/* 280 */     return (this.loader == this.mySnapshot.getNullThing());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JavaThing getSigners() {
/* 287 */     return this.signers;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JavaThing getProtectionDomain() {
/* 294 */     return this.protectionDomain;
/*     */   }
/*     */   
/*     */   public JavaField[] getFields() {
/* 298 */     return this.fields;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JavaField[] getFieldsForInstance() {
/* 305 */     Vector<JavaField> vector = new Vector();
/* 306 */     addFields(vector);
/* 307 */     JavaField[] arrayOfJavaField = new JavaField[vector.size()];
/* 308 */     for (byte b = 0; b < vector.size(); b++) {
/* 309 */       arrayOfJavaField[b] = vector.elementAt(b);
/*     */     }
/* 311 */     return arrayOfJavaField;
/*     */   }
/*     */ 
/*     */   
/*     */   public JavaStatic[] getStatics() {
/* 316 */     return this.statics;
/*     */   }
/*     */ 
/*     */   
/*     */   public JavaThing getStaticField(String paramString) {
/* 321 */     for (byte b = 0; b < this.statics.length; b++) {
/* 322 */       JavaStatic javaStatic = this.statics[b];
/* 323 */       if (javaStatic.getField().getName().equals(paramString)) {
/* 324 */         return javaStatic.getValue();
/*     */       }
/*     */     } 
/* 327 */     return null;
/*     */   }
/*     */   
/*     */   public String toString() {
/* 331 */     return "class " + this.name;
/*     */   }
/*     */   
/*     */   public int compareTo(JavaThing paramJavaThing) {
/* 335 */     if (paramJavaThing instanceof JavaClass) {
/* 336 */       return this.name.compareTo(((JavaClass)paramJavaThing).name);
/*     */     }
/* 338 */     return super.compareTo(paramJavaThing);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isAssignableFrom(JavaClass paramJavaClass) {
/* 347 */     if (this == paramJavaClass)
/* 348 */       return true; 
/* 349 */     if (paramJavaClass == null) {
/* 350 */       return false;
/*     */     }
/* 352 */     return isAssignableFrom((JavaClass)paramJavaClass.superclass);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String describeReferenceTo(JavaThing paramJavaThing, Snapshot paramSnapshot) {
/* 362 */     for (byte b = 0; b < this.statics.length; b++) {
/* 363 */       JavaField javaField = this.statics[b].getField();
/* 364 */       if (javaField.hasId()) {
/* 365 */         JavaThing javaThing = this.statics[b].getValue();
/* 366 */         if (javaThing == paramJavaThing) {
/* 367 */           return "static field " + javaField.getName();
/*     */         }
/*     */       } 
/*     */     } 
/* 371 */     return super.describeReferenceTo(paramJavaThing, paramSnapshot);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getInstanceSize() {
/* 379 */     return this.instanceSize + this.mySnapshot.getMinimumObjectSize();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long getTotalInstanceSize() {
/* 388 */     int i = this.instances.size();
/* 389 */     if (i == 0 || !isArray()) {
/* 390 */       return (i * this.instanceSize);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 395 */     long l = 0L;
/* 396 */     for (byte b = 0; b < i; b++) {
/* 397 */       JavaThing javaThing = this.instances.elementAt(b);
/* 398 */       l += javaThing.getSize();
/*     */     } 
/* 400 */     return l;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getSize() {
/* 407 */     JavaClass javaClass = this.mySnapshot.getJavaLangClass();
/* 408 */     if (javaClass == null) {
/* 409 */       return 0;
/*     */     }
/* 411 */     return javaClass.getInstanceSize();
/*     */   }
/*     */ 
/*     */   
/*     */   public void visitReferencedObjects(JavaHeapObjectVisitor paramJavaHeapObjectVisitor) {
/* 416 */     super.visitReferencedObjects(paramJavaHeapObjectVisitor);
/* 417 */     JavaClass javaClass = getSuperclass();
/* 418 */     if (javaClass != null) paramJavaHeapObjectVisitor.visit(getSuperclass());
/*     */ 
/*     */     
/* 421 */     JavaThing javaThing = getLoader();
/* 422 */     if (javaThing instanceof JavaHeapObject) {
/* 423 */       paramJavaHeapObjectVisitor.visit((JavaHeapObject)javaThing);
/*     */     }
/* 425 */     javaThing = getSigners();
/* 426 */     if (javaThing instanceof JavaHeapObject) {
/* 427 */       paramJavaHeapObjectVisitor.visit((JavaHeapObject)javaThing);
/*     */     }
/* 429 */     javaThing = getProtectionDomain();
/* 430 */     if (javaThing instanceof JavaHeapObject) {
/* 431 */       paramJavaHeapObjectVisitor.visit((JavaHeapObject)javaThing);
/*     */     }
/*     */     
/* 434 */     for (byte b = 0; b < this.statics.length; b++) {
/* 435 */       JavaField javaField = this.statics[b].getField();
/* 436 */       if (!paramJavaHeapObjectVisitor.exclude(this, javaField) && javaField.hasId()) {
/* 437 */         javaThing = this.statics[b].getValue();
/* 438 */         if (javaThing instanceof JavaHeapObject) {
/* 439 */           paramJavaHeapObjectVisitor.visit((JavaHeapObject)javaThing);
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   final ReadBuffer getReadBuffer() {
/* 447 */     return this.mySnapshot.getReadBuffer();
/*     */   }
/*     */   
/*     */   final void setNew(JavaHeapObject paramJavaHeapObject, boolean paramBoolean) {
/* 451 */     this.mySnapshot.setNew(paramJavaHeapObject, paramBoolean);
/*     */   }
/*     */   
/*     */   final boolean isNew(JavaHeapObject paramJavaHeapObject) {
/* 455 */     return this.mySnapshot.isNew(paramJavaHeapObject);
/*     */   }
/*     */   
/*     */   final StackTrace getSiteTrace(JavaHeapObject paramJavaHeapObject) {
/* 459 */     return this.mySnapshot.getSiteTrace(paramJavaHeapObject);
/*     */   }
/*     */   
/*     */   final void addReferenceFromRoot(Root paramRoot, JavaHeapObject paramJavaHeapObject) {
/* 463 */     this.mySnapshot.addReferenceFromRoot(paramRoot, paramJavaHeapObject);
/*     */   }
/*     */   
/*     */   final Root getRoot(JavaHeapObject paramJavaHeapObject) {
/* 467 */     return this.mySnapshot.getRoot(paramJavaHeapObject);
/*     */   }
/*     */   
/*     */   final Snapshot getSnapshot() {
/* 471 */     return this.mySnapshot;
/*     */   }
/*     */   
/*     */   void addInstance(JavaHeapObject paramJavaHeapObject) {
/* 475 */     this.instances.addElement(paramJavaHeapObject);
/*     */   }
/*     */ 
/*     */   
/*     */   private void addFields(Vector<JavaField> paramVector) {
/* 480 */     if (this.superclass != null) {
/* 481 */       ((JavaClass)this.superclass).addFields(paramVector);
/*     */     }
/* 483 */     for (byte b = 0; b < this.fields.length; b++)
/* 484 */       paramVector.addElement(this.fields[b]); 
/*     */   }
/*     */   
/*     */   private void addSubclassInstances(Vector<JavaHeapObject> paramVector) {
/*     */     byte b;
/* 489 */     for (b = 0; b < this.subclasses.length; b++) {
/* 490 */       this.subclasses[b].addSubclassInstances(paramVector);
/*     */     }
/* 492 */     for (b = 0; b < this.instances.size(); b++) {
/* 493 */       paramVector.addElement(this.instances.elementAt(b));
/*     */     }
/*     */   }
/*     */   
/*     */   private void addSubclass(JavaClass paramJavaClass) {
/* 498 */     JavaClass[] arrayOfJavaClass = new JavaClass[this.subclasses.length + 1];
/* 499 */     System.arraycopy(this.subclasses, 0, arrayOfJavaClass, 0, this.subclasses.length);
/* 500 */     arrayOfJavaClass[this.subclasses.length] = paramJavaClass;
/* 501 */     this.subclasses = arrayOfJavaClass;
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\hat\internal\model\JavaClass.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */