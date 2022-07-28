/*     */ package com.sun.tools.hat.internal.server;
/*     */ 
/*     */ import com.sun.tools.hat.internal.model.JavaClass;
/*     */ import com.sun.tools.hat.internal.model.JavaField;
/*     */ import com.sun.tools.hat.internal.model.JavaHeapObject;
/*     */ import com.sun.tools.hat.internal.model.JavaObject;
/*     */ import com.sun.tools.hat.internal.model.JavaStatic;
/*     */ import com.sun.tools.hat.internal.model.JavaThing;
/*     */ import com.sun.tools.hat.internal.model.Root;
/*     */ import com.sun.tools.hat.internal.model.Snapshot;
/*     */ import com.sun.tools.hat.internal.model.StackFrame;
/*     */ import com.sun.tools.hat.internal.model.StackTrace;
/*     */ import com.sun.tools.hat.internal.util.Misc;
/*     */ import java.io.PrintWriter;
/*     */ import java.io.StringWriter;
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.net.URLEncoder;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ abstract class QueryHandler
/*     */ {
/*     */   protected String urlStart;
/*     */   protected String query;
/*     */   protected PrintWriter out;
/*     */   protected Snapshot snapshot;
/*     */   
/*     */   abstract void run();
/*     */   
/*     */   void setUrlStart(String paramString) {
/*  61 */     this.urlStart = paramString;
/*     */   }
/*     */   
/*     */   void setQuery(String paramString) {
/*  65 */     this.query = paramString;
/*     */   }
/*     */   
/*     */   void setOutput(PrintWriter paramPrintWriter) {
/*  69 */     this.out = paramPrintWriter;
/*     */   }
/*     */   
/*     */   void setSnapshot(Snapshot paramSnapshot) {
/*  73 */     this.snapshot = paramSnapshot;
/*     */   }
/*     */   
/*     */   protected String encodeForURL(String paramString) {
/*     */     try {
/*  78 */       paramString = URLEncoder.encode(paramString, "UTF-8");
/*  79 */     } catch (UnsupportedEncodingException unsupportedEncodingException) {
/*     */       
/*  81 */       unsupportedEncodingException.printStackTrace();
/*     */     } 
/*  83 */     return paramString;
/*     */   }
/*     */   
/*     */   protected void startHtml(String paramString) {
/*  87 */     this.out.print("<html><title>");
/*  88 */     print(paramString);
/*  89 */     this.out.println("</title>");
/*  90 */     this.out.println("<body bgcolor=\"#ffffff\"><center><h1>");
/*  91 */     print(paramString);
/*  92 */     this.out.println("</h1></center>");
/*     */   }
/*     */   
/*     */   protected void endHtml() {
/*  96 */     this.out.println("</body></html>");
/*     */   }
/*     */   
/*     */   protected void error(String paramString) {
/* 100 */     println(paramString);
/*     */   }
/*     */   
/*     */   protected void printAnchorStart() {
/* 104 */     this.out.print("<a href=\"");
/* 105 */     this.out.print(this.urlStart);
/*     */   }
/*     */   
/*     */   protected void printThingAnchorTag(long paramLong) {
/* 109 */     printAnchorStart();
/* 110 */     this.out.print("object/");
/* 111 */     printHex(paramLong);
/* 112 */     this.out.print("\">");
/*     */   }
/*     */   
/*     */   protected void printObject(JavaObject paramJavaObject) {
/* 116 */     printThing((JavaThing)paramJavaObject);
/*     */   }
/*     */   
/*     */   protected void printThing(JavaThing paramJavaThing) {
/* 120 */     if (paramJavaThing == null) {
/* 121 */       this.out.print("null");
/*     */       return;
/*     */     } 
/* 124 */     if (paramJavaThing instanceof JavaHeapObject) {
/* 125 */       JavaHeapObject javaHeapObject = (JavaHeapObject)paramJavaThing;
/* 126 */       long l = javaHeapObject.getId();
/* 127 */       if (l != -1L) {
/* 128 */         if (javaHeapObject.isNew())
/* 129 */           this.out.println("<strong>"); 
/* 130 */         printThingAnchorTag(l);
/*     */       } 
/* 132 */       print(paramJavaThing.toString());
/* 133 */       if (l != -1L) {
/* 134 */         if (javaHeapObject.isNew())
/* 135 */           this.out.println("[new]</strong>"); 
/* 136 */         this.out.print(" (" + javaHeapObject.getSize() + " bytes)");
/* 137 */         this.out.println("</a>");
/*     */       } 
/*     */     } else {
/* 140 */       print(paramJavaThing.toString());
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void printRoot(Root paramRoot) {
/* 145 */     StackTrace stackTrace = paramRoot.getStackTrace();
/* 146 */     boolean bool = (stackTrace != null && (stackTrace.getFrames()).length != 0) ? true : false;
/* 147 */     if (bool) {
/* 148 */       printAnchorStart();
/* 149 */       this.out.print("rootStack/");
/* 150 */       printHex(paramRoot.getIndex());
/* 151 */       this.out.print("\">");
/*     */     } 
/* 153 */     print(paramRoot.getDescription());
/* 154 */     if (bool) {
/* 155 */       this.out.print("</a>");
/*     */     }
/*     */   }
/*     */   
/*     */   protected void printClass(JavaClass paramJavaClass) {
/* 160 */     if (paramJavaClass == null) {
/* 161 */       this.out.println("null");
/*     */       return;
/*     */     } 
/* 164 */     printAnchorStart();
/* 165 */     this.out.print("class/");
/* 166 */     print(encodeForURL(paramJavaClass));
/* 167 */     this.out.print("\">");
/* 168 */     print(paramJavaClass.toString());
/* 169 */     this.out.println("</a>");
/*     */   }
/*     */   
/*     */   protected String encodeForURL(JavaClass paramJavaClass) {
/* 173 */     if (paramJavaClass.getId() == -1L) {
/* 174 */       return encodeForURL(paramJavaClass.getName());
/*     */     }
/* 176 */     return paramJavaClass.getIdString();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void printField(JavaField paramJavaField) {
/* 181 */     print(paramJavaField.getName() + " (" + paramJavaField.getSignature() + ")");
/*     */   }
/*     */   
/*     */   protected void printStatic(JavaStatic paramJavaStatic) {
/* 185 */     JavaField javaField = paramJavaStatic.getField();
/* 186 */     printField(javaField);
/* 187 */     this.out.print(" : ");
/* 188 */     if (javaField.hasId()) {
/* 189 */       JavaThing javaThing = paramJavaStatic.getValue();
/* 190 */       printThing(javaThing);
/*     */     } else {
/* 192 */       print(paramJavaStatic.getValue().toString());
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void printStackTrace(StackTrace paramStackTrace) {
/* 197 */     StackFrame[] arrayOfStackFrame = paramStackTrace.getFrames();
/* 198 */     for (byte b = 0; b < arrayOfStackFrame.length; b++) {
/* 199 */       StackFrame stackFrame = arrayOfStackFrame[b];
/* 200 */       String str = stackFrame.getClassName();
/* 201 */       this.out.print("<font color=purple>");
/* 202 */       print(str);
/* 203 */       this.out.print("</font>");
/* 204 */       print("." + stackFrame.getMethodName() + "(" + stackFrame.getMethodSignature() + ")");
/* 205 */       this.out.print(" <bold>:</bold> ");
/* 206 */       print(stackFrame.getSourceFileName() + " line " + stackFrame.getLineNumber());
/* 207 */       this.out.println("<br>");
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void printException(Throwable paramThrowable) {
/* 212 */     println(paramThrowable.getMessage());
/* 213 */     this.out.println("<pre>");
/* 214 */     StringWriter stringWriter = new StringWriter();
/* 215 */     paramThrowable.printStackTrace(new PrintWriter(stringWriter));
/* 216 */     print(stringWriter.toString());
/* 217 */     this.out.println("</pre>");
/*     */   }
/*     */   
/*     */   protected void printHex(long paramLong) {
/* 221 */     if (this.snapshot.getIdentifierSize() == 4) {
/* 222 */       this.out.print(Misc.toHex((int)paramLong));
/*     */     } else {
/* 224 */       this.out.print(Misc.toHex(paramLong));
/*     */     } 
/*     */   }
/*     */   
/*     */   protected long parseHex(String paramString) {
/* 229 */     return Misc.parseHex(paramString);
/*     */   }
/*     */   
/*     */   protected void print(String paramString) {
/* 233 */     this.out.print(Misc.encodeHtml(paramString));
/*     */   }
/*     */   
/*     */   protected void println(String paramString) {
/* 237 */     this.out.println(Misc.encodeHtml(paramString));
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\hat\internal\server\QueryHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */