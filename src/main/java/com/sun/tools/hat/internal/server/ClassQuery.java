/*     */ package com.sun.tools.hat.internal.server;
/*     */ 
/*     */ import com.sun.tools.hat.internal.model.JavaClass;
/*     */ import com.sun.tools.hat.internal.model.JavaField;
/*     */ import com.sun.tools.hat.internal.model.JavaHeapObject;
/*     */ import com.sun.tools.hat.internal.model.JavaStatic;
/*     */ import com.sun.tools.hat.internal.model.JavaThing;
/*     */ import com.sun.tools.hat.internal.util.ArraySorter;
/*     */ import com.sun.tools.hat.internal.util.Comparer;
/*     */ import java.util.Enumeration;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class ClassQuery
/*     */   extends QueryHandler
/*     */ {
/*     */   public void run() {
/*  54 */     startHtml("Class " + this.query);
/*  55 */     JavaClass javaClass = this.snapshot.findClass(this.query);
/*  56 */     if (javaClass == null) {
/*  57 */       error("class not found: " + this.query);
/*     */     } else {
/*  59 */       printFullClass(javaClass);
/*     */     } 
/*  61 */     endHtml();
/*     */   }
/*     */   
/*     */   protected void printFullClass(JavaClass paramJavaClass) {
/*  65 */     this.out.print("<h1>");
/*  66 */     print(paramJavaClass.toString());
/*  67 */     this.out.println("</h1>");
/*     */     
/*  69 */     this.out.println("<h2>Superclass:</h2>");
/*  70 */     printClass(paramJavaClass.getSuperclass());
/*     */     
/*  72 */     this.out.println("<h2>Loader Details</h2>");
/*  73 */     this.out.println("<h3>ClassLoader:</h3>");
/*  74 */     printThing(paramJavaClass.getLoader());
/*     */     
/*  76 */     this.out.println("<h3>Signers:</h3>");
/*  77 */     printThing(paramJavaClass.getSigners());
/*     */     
/*  79 */     this.out.println("<h3>Protection Domain:</h3>");
/*  80 */     printThing(paramJavaClass.getProtectionDomain());
/*     */     
/*  82 */     this.out.println("<h2>Subclasses:</h2>");
/*  83 */     JavaClass[] arrayOfJavaClass = paramJavaClass.getSubclasses();
/*  84 */     for (byte b1 = 0; b1 < arrayOfJavaClass.length; b1++) {
/*  85 */       this.out.print("    ");
/*  86 */       printClass(arrayOfJavaClass[b1]);
/*  87 */       this.out.println("<br>");
/*     */     } 
/*     */     
/*  90 */     this.out.println("<h2>Instance Data Members:</h2>");
/*  91 */     JavaField[] arrayOfJavaField = (JavaField[])paramJavaClass.getFields().clone();
/*  92 */     ArraySorter.sort((Object[])arrayOfJavaField, new Comparer() {
/*     */           public int compare(Object param1Object1, Object param1Object2) {
/*  94 */             JavaField javaField1 = (JavaField)param1Object1;
/*  95 */             JavaField javaField2 = (JavaField)param1Object2;
/*  96 */             return javaField1.getName().compareTo(javaField2.getName());
/*     */           }
/*     */         });
/*  99 */     for (byte b2 = 0; b2 < arrayOfJavaField.length; b2++) {
/* 100 */       this.out.print("    ");
/* 101 */       printField(arrayOfJavaField[b2]);
/* 102 */       this.out.println("<br>");
/*     */     } 
/*     */     
/* 105 */     this.out.println("<h2>Static Data Members:</h2>");
/* 106 */     JavaStatic[] arrayOfJavaStatic = paramJavaClass.getStatics();
/* 107 */     for (byte b3 = 0; b3 < arrayOfJavaStatic.length; b3++) {
/* 108 */       printStatic(arrayOfJavaStatic[b3]);
/* 109 */       this.out.println("<br>");
/*     */     } 
/*     */     
/* 112 */     this.out.println("<h2>Instances</h2>");
/*     */     
/* 114 */     printAnchorStart();
/* 115 */     print("instances/" + encodeForURL(paramJavaClass));
/* 116 */     this.out.print("\">");
/* 117 */     this.out.println("Exclude subclasses</a><br>");
/*     */     
/* 119 */     printAnchorStart();
/* 120 */     print("allInstances/" + encodeForURL(paramJavaClass));
/* 121 */     this.out.print("\">");
/* 122 */     this.out.println("Include subclasses</a><br>");
/*     */ 
/*     */     
/* 125 */     if (this.snapshot.getHasNewSet()) {
/* 126 */       this.out.println("<h2>New Instances</h2>");
/*     */       
/* 128 */       printAnchorStart();
/* 129 */       print("newInstances/" + encodeForURL(paramJavaClass));
/* 130 */       this.out.print("\">");
/* 131 */       this.out.println("Exclude subclasses</a><br>");
/*     */       
/* 133 */       printAnchorStart();
/* 134 */       print("allNewInstances/" + encodeForURL(paramJavaClass));
/* 135 */       this.out.print("\">");
/* 136 */       this.out.println("Include subclasses</a><br>");
/*     */     } 
/*     */     
/* 139 */     this.out.println("<h2>References summary by Type</h2>");
/* 140 */     printAnchorStart();
/* 141 */     print("refsByType/" + encodeForURL(paramJavaClass));
/* 142 */     this.out.print("\">");
/* 143 */     this.out.println("References summary by type</a>");
/*     */     
/* 145 */     printReferencesTo((JavaHeapObject)paramJavaClass);
/*     */   }
/*     */   
/*     */   protected void printReferencesTo(JavaHeapObject paramJavaHeapObject) {
/* 149 */     if (paramJavaHeapObject.getId() == -1L) {
/*     */       return;
/*     */     }
/* 152 */     this.out.println("<h2>References to this object:</h2>");
/* 153 */     this.out.flush();
/* 154 */     Enumeration<JavaHeapObject> enumeration = paramJavaHeapObject.getReferers();
/* 155 */     while (enumeration.hasMoreElements()) {
/* 156 */       JavaHeapObject javaHeapObject = enumeration.nextElement();
/* 157 */       printThing((JavaThing)javaHeapObject);
/* 158 */       print(" : " + javaHeapObject.describeReferenceTo((JavaThing)paramJavaHeapObject, this.snapshot));
/*     */ 
/*     */       
/* 161 */       this.out.println("<br>");
/*     */     } 
/*     */     
/* 164 */     this.out.println("<h2>Other Queries</h2>");
/* 165 */     this.out.println("Reference Chains from Rootset");
/* 166 */     long l = paramJavaHeapObject.getId();
/*     */     
/* 168 */     this.out.print("<ul><li>");
/* 169 */     printAnchorStart();
/* 170 */     this.out.print("roots/");
/* 171 */     printHex(l);
/* 172 */     this.out.print("\">");
/* 173 */     this.out.println("Exclude weak refs</a>");
/*     */     
/* 175 */     this.out.print("<li>");
/* 176 */     printAnchorStart();
/* 177 */     this.out.print("allRoots/");
/* 178 */     printHex(l);
/* 179 */     this.out.print("\">");
/* 180 */     this.out.println("Include weak refs</a></ul>");
/*     */     
/* 182 */     printAnchorStart();
/* 183 */     this.out.print("reachableFrom/");
/* 184 */     printHex(l);
/* 185 */     this.out.print("\">");
/* 186 */     this.out.println("Objects reachable from here</a><br>");
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\hat\internal\server\ClassQuery.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */