/*     */ package com.sun.tools.hat.internal.server;
/*     */ 
/*     */ import com.sun.tools.hat.internal.model.JavaClass;
/*     */ import com.sun.tools.hat.internal.model.JavaField;
/*     */ import com.sun.tools.hat.internal.model.JavaHeapObject;
/*     */ import com.sun.tools.hat.internal.model.JavaObject;
/*     */ import com.sun.tools.hat.internal.model.JavaObjectArray;
/*     */ import com.sun.tools.hat.internal.model.JavaThing;
/*     */ import com.sun.tools.hat.internal.model.JavaValueArray;
/*     */ import com.sun.tools.hat.internal.model.StackTrace;
/*     */ import com.sun.tools.hat.internal.util.ArraySorter;
/*     */ import com.sun.tools.hat.internal.util.Comparer;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class ObjectQuery
/*     */   extends ClassQuery
/*     */ {
/*     */   public void run() {
/*  54 */     startHtml("Object at " + this.query);
/*  55 */     long l = parseHex(this.query);
/*  56 */     JavaHeapObject javaHeapObject = this.snapshot.findThing(l);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  63 */     if (javaHeapObject == null) {
/*  64 */       error("object not found");
/*  65 */     } else if (javaHeapObject instanceof JavaClass) {
/*  66 */       printFullClass((JavaClass)javaHeapObject);
/*  67 */     } else if (javaHeapObject instanceof JavaValueArray) {
/*  68 */       print(((JavaValueArray)javaHeapObject).valueString(true));
/*  69 */       printAllocationSite(javaHeapObject);
/*  70 */       printReferencesTo(javaHeapObject);
/*  71 */     } else if (javaHeapObject instanceof JavaObjectArray) {
/*  72 */       printFullObjectArray((JavaObjectArray)javaHeapObject);
/*  73 */       printAllocationSite(javaHeapObject);
/*  74 */       printReferencesTo(javaHeapObject);
/*  75 */     } else if (javaHeapObject instanceof JavaObject) {
/*  76 */       printFullObject((JavaObject)javaHeapObject);
/*  77 */       printAllocationSite(javaHeapObject);
/*  78 */       printReferencesTo(javaHeapObject);
/*     */     } else {
/*     */       
/*  81 */       print(javaHeapObject.toString());
/*  82 */       printReferencesTo(javaHeapObject);
/*     */     } 
/*  84 */     endHtml();
/*     */   }
/*     */ 
/*     */   
/*     */   private void printFullObject(JavaObject paramJavaObject) {
/*  89 */     this.out.print("<h1>instance of ");
/*  90 */     print(paramJavaObject.toString());
/*  91 */     this.out.print(" <small>(" + paramJavaObject.getSize() + " bytes)</small>");
/*  92 */     this.out.println("</h1>\n");
/*     */     
/*  94 */     this.out.println("<h2>Class:</h2>");
/*  95 */     printClass(paramJavaObject.getClazz());
/*     */     
/*  97 */     this.out.println("<h2>Instance data members:</h2>");
/*  98 */     JavaThing[] arrayOfJavaThing = paramJavaObject.getFields();
/*  99 */     final JavaField[] fields = paramJavaObject.getClazz().getFieldsForInstance();
/* 100 */     Integer[] arrayOfInteger = new Integer[arrayOfJavaThing.length]; byte b;
/* 101 */     for (b = 0; b < arrayOfJavaThing.length; b++) {
/* 102 */       arrayOfInteger[b] = new Integer(b);
/*     */     }
/* 104 */     ArraySorter.sort((Object[])arrayOfInteger, new Comparer() {
/*     */           public int compare(Object param1Object1, Object param1Object2) {
/* 106 */             JavaField javaField1 = fields[((Integer)param1Object1).intValue()];
/* 107 */             JavaField javaField2 = fields[((Integer)param1Object2).intValue()];
/* 108 */             return javaField1.getName().compareTo(javaField2.getName());
/*     */           }
/*     */         });
/* 111 */     for (b = 0; b < arrayOfJavaThing.length; b++) {
/* 112 */       int i = arrayOfInteger[b].intValue();
/* 113 */       printField(arrayOfJavaField[i]);
/* 114 */       this.out.print(" : ");
/* 115 */       printThing(arrayOfJavaThing[i]);
/* 116 */       this.out.println("<br>");
/*     */     } 
/*     */   }
/*     */   
/*     */   private void printFullObjectArray(JavaObjectArray paramJavaObjectArray) {
/* 121 */     JavaThing[] arrayOfJavaThing = paramJavaObjectArray.getElements();
/* 122 */     this.out.println("<h1>Array of " + arrayOfJavaThing.length + " objects</h1>");
/*     */     
/* 124 */     this.out.println("<h2>Class:</h2>");
/* 125 */     printClass(paramJavaObjectArray.getClazz());
/*     */     
/* 127 */     this.out.println("<h2>Values</h2>");
/* 128 */     for (byte b = 0; b < arrayOfJavaThing.length; b++) {
/* 129 */       this.out.print("" + b + " : ");
/* 130 */       printThing(arrayOfJavaThing[b]);
/* 131 */       this.out.println("<br>");
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void printAllocationSite(JavaHeapObject paramJavaHeapObject) {
/* 139 */     StackTrace stackTrace = paramJavaHeapObject.getAllocatedFrom();
/* 140 */     if (stackTrace == null || (stackTrace.getFrames()).length == 0) {
/*     */       return;
/*     */     }
/* 143 */     this.out.println("<h2>Object allocated from:</h2>");
/* 144 */     printStackTrace(stackTrace);
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\hat\internal\server\ObjectQuery.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */