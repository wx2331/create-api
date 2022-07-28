/*     */ package com.sun.tools.hat.internal.oql;
/*     */ 
/*     */ import com.sun.tools.hat.internal.model.JavaClass;
/*     */ import com.sun.tools.hat.internal.model.JavaHeapObject;
/*     */ import com.sun.tools.hat.internal.model.Snapshot;
/*     */ import java.io.InputStream;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.Reader;
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.Enumeration;
/*     */ import java.util.StringTokenizer;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class OQLEngine
/*     */ {
/*     */   private Object engine;
/*     */   private Method evalMethod;
/*     */   private Method invokeMethod;
/*     */   private Snapshot snapshot;
/*     */   
/*     */   static {
/*     */     try {
/*  49 */       Class<?> clazz = Class.forName("javax.script.ScriptEngineManager");
/*  50 */       Object object1 = clazz.newInstance();
/*     */ 
/*     */       
/*  53 */       Method method = clazz.getMethod("getEngineByName", new Class[] { String.class });
/*     */       
/*  55 */       Object object2 = method.invoke(object1, new Object[] { "js" });
/*  56 */       oqlSupported = (object2 != null);
/*  57 */     } catch (Exception exception) {
/*  58 */       oqlSupported = false;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static boolean isOQLSupported() {
/*  64 */     return oqlSupported;
/*     */   }
/*     */   
/*     */   public OQLEngine(Snapshot paramSnapshot) {
/*  68 */     if (!isOQLSupported()) {
/*  69 */       throw new UnsupportedOperationException("OQL not supported");
/*     */     }
/*  71 */     init(paramSnapshot);
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
/*     */   public synchronized void executeQuery(String paramString, ObjectVisitor paramObjectVisitor) throws OQLException {
/*  84 */     debugPrint("query : " + paramString);
/*  85 */     StringTokenizer stringTokenizer = new StringTokenizer(paramString);
/*  86 */     if (stringTokenizer.hasMoreTokens()) {
/*  87 */       String str = stringTokenizer.nextToken();
/*  88 */       if (!str.equals("select")) {
/*     */ 
/*     */         
/*     */         try {
/*  92 */           Object object = evalScript(paramString);
/*  93 */           paramObjectVisitor.visit(object);
/*  94 */         } catch (Exception exception) {
/*  95 */           throw new OQLException(exception);
/*     */         } 
/*     */         return;
/*     */       } 
/*     */     } else {
/* 100 */       throw new OQLException("query syntax error: no 'select' clause");
/*     */     } 
/*     */     
/* 103 */     String str1 = "";
/* 104 */     boolean bool1 = false;
/* 105 */     while (stringTokenizer.hasMoreTokens()) {
/* 106 */       String str = stringTokenizer.nextToken();
/* 107 */       if (str.equals("from")) {
/* 108 */         bool1 = true;
/*     */         break;
/*     */       } 
/* 111 */       str1 = str1 + " " + str;
/*     */     } 
/*     */     
/* 114 */     if (str1.equals("")) {
/* 115 */       throw new OQLException("query syntax error: 'select' expression can not be empty");
/*     */     }
/*     */     
/* 118 */     String str2 = null;
/* 119 */     boolean bool2 = false;
/* 120 */     String str3 = null;
/* 121 */     String str4 = null;
/*     */     
/* 123 */     if (bool1) {
/* 124 */       if (stringTokenizer.hasMoreTokens()) {
/* 125 */         String str = stringTokenizer.nextToken();
/* 126 */         if (str.equals("instanceof")) {
/* 127 */           bool2 = true;
/* 128 */           if (!stringTokenizer.hasMoreTokens()) {
/* 129 */             throw new OQLException("no class name after 'instanceof'");
/*     */           }
/* 131 */           str2 = stringTokenizer.nextToken();
/*     */         } else {
/* 133 */           str2 = str;
/*     */         } 
/*     */       } else {
/* 136 */         throw new OQLException("query syntax error: class name must follow 'from'");
/*     */       } 
/*     */       
/* 139 */       if (stringTokenizer.hasMoreTokens()) {
/* 140 */         str4 = stringTokenizer.nextToken();
/* 141 */         if (str4.equals("where")) {
/* 142 */           throw new OQLException("query syntax error: identifier should follow class name");
/*     */         }
/* 144 */         if (stringTokenizer.hasMoreTokens()) {
/* 145 */           String str = stringTokenizer.nextToken();
/* 146 */           if (!str.equals("where")) {
/* 147 */             throw new OQLException("query syntax error: 'where' clause expected after 'from' clause");
/*     */           }
/*     */           
/* 150 */           str3 = "";
/* 151 */           while (stringTokenizer.hasMoreTokens()) {
/* 152 */             str3 = str3 + " " + stringTokenizer.nextToken();
/*     */           }
/* 154 */           if (str3.equals("")) {
/* 155 */             throw new OQLException("query syntax error: 'where' clause cannot have empty expression");
/*     */           }
/*     */         } 
/*     */       } else {
/* 159 */         throw new OQLException("query syntax error: identifier should follow class name");
/*     */       } 
/*     */     } 
/*     */     
/* 163 */     executeQuery(new OQLQuery(str1, bool2, str2, str4, str3), paramObjectVisitor);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void executeQuery(OQLQuery paramOQLQuery, ObjectVisitor paramObjectVisitor) throws OQLException {
/* 169 */     JavaClass javaClass = null;
/* 170 */     if (paramOQLQuery.className != null) {
/* 171 */       javaClass = this.snapshot.findClass(paramOQLQuery.className);
/* 172 */       if (javaClass == null) {
/* 173 */         throw new OQLException(paramOQLQuery.className + " is not found!");
/*     */       }
/*     */     } 
/*     */     
/* 177 */     StringBuffer stringBuffer = new StringBuffer();
/* 178 */     stringBuffer.append("function __select__(");
/* 179 */     if (paramOQLQuery.identifier != null) {
/* 180 */       stringBuffer.append(paramOQLQuery.identifier);
/*     */     }
/* 182 */     stringBuffer.append(") { return ");
/* 183 */     stringBuffer.append(paramOQLQuery.selectExpr.replace('\n', ' '));
/* 184 */     stringBuffer.append("; }");
/*     */     
/* 186 */     String str1 = stringBuffer.toString();
/* 187 */     debugPrint(str1);
/* 188 */     String str2 = null;
/* 189 */     if (paramOQLQuery.whereExpr != null) {
/* 190 */       stringBuffer = new StringBuffer();
/* 191 */       stringBuffer.append("function __where__(");
/* 192 */       stringBuffer.append(paramOQLQuery.identifier);
/* 193 */       stringBuffer.append(") { return ");
/* 194 */       stringBuffer.append(paramOQLQuery.whereExpr.replace('\n', ' '));
/* 195 */       stringBuffer.append("; }");
/* 196 */       str2 = stringBuffer.toString();
/*     */     } 
/* 198 */     debugPrint(str2);
/*     */ 
/*     */     
/*     */     try {
/* 202 */       this.evalMethod.invoke(this.engine, new Object[] { str1 });
/* 203 */       if (str2 != null) {
/* 204 */         this.evalMethod.invoke(this.engine, new Object[] { str2 });
/*     */       }
/*     */       
/* 207 */       if (paramOQLQuery.className != null) {
/* 208 */         Enumeration<JavaHeapObject> enumeration = javaClass.getInstances(paramOQLQuery.isInstanceOf);
/* 209 */         while (enumeration.hasMoreElements()) {
/* 210 */           JavaHeapObject javaHeapObject = enumeration.nextElement();
/* 211 */           Object[] arrayOfObject = { wrapJavaObject(javaHeapObject) };
/* 212 */           boolean bool = (str2 == null);
/* 213 */           if (!bool) {
/* 214 */             Object object = call("__where__", arrayOfObject);
/* 215 */             if (object instanceof Boolean) {
/* 216 */               bool = ((Boolean)object).booleanValue();
/* 217 */             } else if (object instanceof Number) {
/* 218 */               bool = (((Number)object).intValue() != 0);
/*     */             } else {
/* 220 */               bool = (object != null);
/*     */             } 
/*     */           } 
/*     */           
/* 224 */           if (bool) {
/* 225 */             Object object = call("__select__", arrayOfObject);
/* 226 */             if (paramObjectVisitor.visit(object))
/*     */               return; 
/*     */           } 
/*     */         } 
/*     */       } else {
/* 231 */         Object object = call("__select__", new Object[0]);
/* 232 */         paramObjectVisitor.visit(object);
/*     */       } 
/* 234 */     } catch (Exception exception) {
/* 235 */       throw new OQLException(exception);
/*     */     } 
/*     */   }
/*     */   
/*     */   public Object evalScript(String paramString) throws Exception {
/* 240 */     return this.evalMethod.invoke(this.engine, new Object[] { paramString });
/*     */   }
/*     */   
/*     */   public Object wrapJavaObject(JavaHeapObject paramJavaHeapObject) throws Exception {
/* 244 */     return call("wrapJavaObject", new Object[] { paramJavaHeapObject });
/*     */   }
/*     */   
/*     */   public Object toHtml(Object paramObject) throws Exception {
/* 248 */     return call("toHtml", new Object[] { paramObject });
/*     */   }
/*     */   
/*     */   public Object call(String paramString, Object[] paramArrayOfObject) throws Exception {
/* 252 */     return this.invokeMethod.invoke(this.engine, new Object[] { paramString, paramArrayOfObject });
/*     */   }
/*     */   
/*     */   private static void debugPrint(String paramString) {
/* 256 */     if (debug) System.out.println(paramString); 
/*     */   }
/*     */   
/*     */   private void init(Snapshot paramSnapshot) throws RuntimeException {
/* 260 */     this.snapshot = paramSnapshot;
/*     */     
/*     */     try {
/* 263 */       Class<?> clazz1 = Class.forName("javax.script.ScriptEngineManager");
/* 264 */       Object object = clazz1.newInstance();
/*     */ 
/*     */       
/* 267 */       Method method1 = clazz1.getMethod("getEngineByName", new Class[] { String.class });
/*     */       
/* 269 */       this.engine = method1.invoke(object, new Object[] { "js" });
/*     */ 
/*     */       
/* 272 */       InputStream inputStream = getInitStream();
/* 273 */       Class<?> clazz2 = Class.forName("javax.script.ScriptEngine");
/* 274 */       this.evalMethod = clazz2.getMethod("eval", new Class[] { Reader.class });
/*     */       
/* 276 */       this.evalMethod.invoke(this.engine, new Object[] { new InputStreamReader(inputStream) });
/*     */ 
/*     */ 
/*     */       
/* 280 */       Class<?> clazz3 = Class.forName("javax.script.Invocable");
/*     */       
/* 282 */       this.evalMethod = clazz2.getMethod("eval", new Class[] { String.class });
/*     */       
/* 284 */       this.invokeMethod = clazz3.getMethod("invokeFunction", new Class[] { String.class, Object[].class });
/*     */ 
/*     */ 
/*     */       
/* 288 */       Method method2 = clazz2.getMethod("put", new Class[] { String.class, Object.class });
/*     */ 
/*     */ 
/*     */       
/* 292 */       method2.invoke(this.engine, new Object[] { "heap", 
/* 293 */             call("wrapHeapSnapshot", new Object[] { paramSnapshot }) });
/*     */     }
/* 295 */     catch (Exception exception) {
/* 296 */       if (debug) exception.printStackTrace(); 
/* 297 */       throw new RuntimeException(exception);
/*     */     } 
/*     */   }
/*     */   
/*     */   private InputStream getInitStream() {
/* 302 */     return getClass().getResourceAsStream("/com/sun/tools/hat/resources/hat.js");
/*     */   }
/*     */   
/*     */   private static boolean debug = false;
/*     */   private static boolean oqlSupported;
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\hat\internal\oql\OQLEngine.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */