/*     */ package com.sun.tools.javac.util;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Names
/*     */ {
/*  39 */   public static final Context.Key<Names> namesKey = new Context.Key<>();
/*     */   
/*     */   public static Names instance(Context paramContext) {
/*  42 */     Names names = paramContext.<Names>get(namesKey);
/*  43 */     if (names == null) {
/*  44 */       names = new Names(paramContext);
/*  45 */       paramContext.put(namesKey, names);
/*     */     } 
/*  47 */     return names;
/*     */   }
/*     */ 
/*     */   
/*     */   public final Name asterisk;
/*     */   
/*     */   public final Name comma;
/*     */   
/*     */   public final Name empty;
/*     */   
/*     */   public final Name hyphen;
/*     */   
/*     */   public final Name one;
/*     */   
/*     */   public final Name period;
/*     */   
/*     */   public final Name semicolon;
/*     */   
/*     */   public final Name slash;
/*     */   
/*     */   public final Name slashequals;
/*     */   
/*     */   public final Name _class;
/*     */   
/*     */   public final Name _default;
/*     */   
/*     */   public final Name _super;
/*     */   
/*     */   public final Name _this;
/*     */   
/*     */   public final Name _name;
/*     */   
/*     */   public final Name addSuppressed;
/*     */   
/*     */   public final Name any;
/*     */   
/*     */   public final Name append;
/*     */   
/*     */   public final Name clinit;
/*     */   
/*     */   public final Name clone;
/*     */   
/*     */   public final Name close;
/*     */   
/*     */   public final Name compareTo;
/*     */   
/*     */   public final Name deserializeLambda;
/*     */   public final Name desiredAssertionStatus;
/*     */   public final Name equals;
/*     */   public final Name error;
/*     */   public final Name family;
/*     */   public final Name finalize;
/*     */   public final Name forName;
/*     */   public final Name getClass;
/*     */   public final Name getClassLoader;
/*     */   public final Name getComponentType;
/*     */   public final Name getDeclaringClass;
/*     */   public final Name getMessage;
/*     */   public final Name hasNext;
/*     */   public final Name hashCode;
/*     */   public final Name init;
/*     */   public final Name initCause;
/*     */   public final Name iterator;
/*     */   public final Name length;
/*     */   public final Name next;
/*     */   public final Name ordinal;
/*     */   public final Name serialVersionUID;
/*     */   public final Name toString;
/*     */   public final Name value;
/*     */   public final Name valueOf;
/*     */   public final Name values;
/*     */   public final Name java_io_Serializable;
/*     */   public final Name java_lang_AutoCloseable;
/*     */   public final Name java_lang_Class;
/*     */   public final Name java_lang_Cloneable;
/*     */   public final Name java_lang_Enum;
/*     */   public final Name java_lang_Object;
/*     */   public final Name java_lang_invoke_MethodHandle;
/*     */   public final Name Array;
/*     */   public final Name Bound;
/*     */   public final Name Method;
/*     */   public final Name java_lang;
/*     */   public final Name Annotation;
/*     */   public final Name AnnotationDefault;
/*     */   public final Name BootstrapMethods;
/*     */   public final Name Bridge;
/*     */   public final Name CharacterRangeTable;
/*     */   public final Name Code;
/*     */   public final Name CompilationID;
/*     */   public final Name ConstantValue;
/*     */   public final Name Deprecated;
/*     */   public final Name EnclosingMethod;
/*     */   public final Name Enum;
/*     */   public final Name Exceptions;
/*     */   public final Name InnerClasses;
/*     */   public final Name LineNumberTable;
/*     */   public final Name LocalVariableTable;
/*     */   public final Name LocalVariableTypeTable;
/*     */   public final Name MethodParameters;
/*     */   public final Name RuntimeInvisibleAnnotations;
/*     */   public final Name RuntimeInvisibleParameterAnnotations;
/*     */   public final Name RuntimeInvisibleTypeAnnotations;
/*     */   public final Name RuntimeVisibleAnnotations;
/*     */   public final Name RuntimeVisibleParameterAnnotations;
/*     */   public final Name RuntimeVisibleTypeAnnotations;
/*     */   public final Name Signature;
/*     */   public final Name SourceFile;
/*     */   public final Name SourceID;
/*     */   public final Name StackMap;
/*     */   public final Name StackMapTable;
/*     */   public final Name Synthetic;
/*     */   public final Name Value;
/*     */   public final Name Varargs;
/*     */   public final Name ANNOTATION_TYPE;
/*     */   public final Name CONSTRUCTOR;
/*     */   public final Name FIELD;
/*     */   public final Name LOCAL_VARIABLE;
/*     */   public final Name METHOD;
/*     */   public final Name PACKAGE;
/*     */   public final Name PARAMETER;
/*     */   public final Name TYPE;
/*     */   public final Name TYPE_PARAMETER;
/*     */   public final Name TYPE_USE;
/*     */   public final Name CLASS;
/*     */   public final Name RUNTIME;
/*     */   public final Name SOURCE;
/*     */   public final Name T;
/*     */   public final Name deprecated;
/*     */   public final Name ex;
/*     */   public final Name package_info;
/*     */   public final Name lambda;
/*     */   public final Name metafactory;
/*     */   public final Name altMetafactory;
/*     */   public final Name dollarThis;
/*     */   public final Name.Table table;
/*     */   
/*     */   public Names(Context paramContext) {
/* 184 */     Options options = Options.instance(paramContext);
/* 185 */     this.table = createTable(options);
/*     */ 
/*     */     
/* 188 */     this.asterisk = fromString("*");
/* 189 */     this.comma = fromString(",");
/* 190 */     this.empty = fromString("");
/* 191 */     this.hyphen = fromString("-");
/* 192 */     this.one = fromString("1");
/* 193 */     this.period = fromString(".");
/* 194 */     this.semicolon = fromString(";");
/* 195 */     this.slash = fromString("/");
/* 196 */     this.slashequals = fromString("/=");
/*     */ 
/*     */     
/* 199 */     this._class = fromString("class");
/* 200 */     this._default = fromString("default");
/* 201 */     this._super = fromString("super");
/* 202 */     this._this = fromString("this");
/*     */ 
/*     */     
/* 205 */     this._name = fromString("name");
/* 206 */     this.addSuppressed = fromString("addSuppressed");
/* 207 */     this.any = fromString("<any>");
/* 208 */     this.append = fromString("append");
/* 209 */     this.clinit = fromString("<clinit>");
/* 210 */     this.clone = fromString("clone");
/* 211 */     this.close = fromString("close");
/* 212 */     this.compareTo = fromString("compareTo");
/* 213 */     this.deserializeLambda = fromString("$deserializeLambda$");
/* 214 */     this.desiredAssertionStatus = fromString("desiredAssertionStatus");
/* 215 */     this.equals = fromString("equals");
/* 216 */     this.error = fromString("<error>");
/* 217 */     this.family = fromString("family");
/* 218 */     this.finalize = fromString("finalize");
/* 219 */     this.forName = fromString("forName");
/* 220 */     this.getClass = fromString("getClass");
/* 221 */     this.getClassLoader = fromString("getClassLoader");
/* 222 */     this.getComponentType = fromString("getComponentType");
/* 223 */     this.getDeclaringClass = fromString("getDeclaringClass");
/* 224 */     this.getMessage = fromString("getMessage");
/* 225 */     this.hasNext = fromString("hasNext");
/* 226 */     this.hashCode = fromString("hashCode");
/* 227 */     this.init = fromString("<init>");
/* 228 */     this.initCause = fromString("initCause");
/* 229 */     this.iterator = fromString("iterator");
/* 230 */     this.length = fromString("length");
/* 231 */     this.next = fromString("next");
/* 232 */     this.ordinal = fromString("ordinal");
/* 233 */     this.serialVersionUID = fromString("serialVersionUID");
/* 234 */     this.toString = fromString("toString");
/* 235 */     this.value = fromString("value");
/* 236 */     this.valueOf = fromString("valueOf");
/* 237 */     this.values = fromString("values");
/* 238 */     this.dollarThis = fromString("$this");
/*     */ 
/*     */     
/* 241 */     this.java_io_Serializable = fromString("java.io.Serializable");
/* 242 */     this.java_lang_AutoCloseable = fromString("java.lang.AutoCloseable");
/* 243 */     this.java_lang_Class = fromString("java.lang.Class");
/* 244 */     this.java_lang_Cloneable = fromString("java.lang.Cloneable");
/* 245 */     this.java_lang_Enum = fromString("java.lang.Enum");
/* 246 */     this.java_lang_Object = fromString("java.lang.Object");
/* 247 */     this.java_lang_invoke_MethodHandle = fromString("java.lang.invoke.MethodHandle");
/*     */ 
/*     */     
/* 250 */     this.Array = fromString("Array");
/* 251 */     this.Bound = fromString("Bound");
/* 252 */     this.Method = fromString("Method");
/*     */ 
/*     */     
/* 255 */     this.java_lang = fromString("java.lang");
/*     */ 
/*     */     
/* 258 */     this.Annotation = fromString("Annotation");
/* 259 */     this.AnnotationDefault = fromString("AnnotationDefault");
/* 260 */     this.BootstrapMethods = fromString("BootstrapMethods");
/* 261 */     this.Bridge = fromString("Bridge");
/* 262 */     this.CharacterRangeTable = fromString("CharacterRangeTable");
/* 263 */     this.Code = fromString("Code");
/* 264 */     this.CompilationID = fromString("CompilationID");
/* 265 */     this.ConstantValue = fromString("ConstantValue");
/* 266 */     this.Deprecated = fromString("Deprecated");
/* 267 */     this.EnclosingMethod = fromString("EnclosingMethod");
/* 268 */     this.Enum = fromString("Enum");
/* 269 */     this.Exceptions = fromString("Exceptions");
/* 270 */     this.InnerClasses = fromString("InnerClasses");
/* 271 */     this.LineNumberTable = fromString("LineNumberTable");
/* 272 */     this.LocalVariableTable = fromString("LocalVariableTable");
/* 273 */     this.LocalVariableTypeTable = fromString("LocalVariableTypeTable");
/* 274 */     this.MethodParameters = fromString("MethodParameters");
/* 275 */     this.RuntimeInvisibleAnnotations = fromString("RuntimeInvisibleAnnotations");
/* 276 */     this.RuntimeInvisibleParameterAnnotations = fromString("RuntimeInvisibleParameterAnnotations");
/* 277 */     this.RuntimeInvisibleTypeAnnotations = fromString("RuntimeInvisibleTypeAnnotations");
/* 278 */     this.RuntimeVisibleAnnotations = fromString("RuntimeVisibleAnnotations");
/* 279 */     this.RuntimeVisibleParameterAnnotations = fromString("RuntimeVisibleParameterAnnotations");
/* 280 */     this.RuntimeVisibleTypeAnnotations = fromString("RuntimeVisibleTypeAnnotations");
/* 281 */     this.Signature = fromString("Signature");
/* 282 */     this.SourceFile = fromString("SourceFile");
/* 283 */     this.SourceID = fromString("SourceID");
/* 284 */     this.StackMap = fromString("StackMap");
/* 285 */     this.StackMapTable = fromString("StackMapTable");
/* 286 */     this.Synthetic = fromString("Synthetic");
/* 287 */     this.Value = fromString("Value");
/* 288 */     this.Varargs = fromString("Varargs");
/*     */ 
/*     */     
/* 291 */     this.ANNOTATION_TYPE = fromString("ANNOTATION_TYPE");
/* 292 */     this.CONSTRUCTOR = fromString("CONSTRUCTOR");
/* 293 */     this.FIELD = fromString("FIELD");
/* 294 */     this.LOCAL_VARIABLE = fromString("LOCAL_VARIABLE");
/* 295 */     this.METHOD = fromString("METHOD");
/* 296 */     this.PACKAGE = fromString("PACKAGE");
/* 297 */     this.PARAMETER = fromString("PARAMETER");
/* 298 */     this.TYPE = fromString("TYPE");
/* 299 */     this.TYPE_PARAMETER = fromString("TYPE_PARAMETER");
/* 300 */     this.TYPE_USE = fromString("TYPE_USE");
/*     */ 
/*     */     
/* 303 */     this.CLASS = fromString("CLASS");
/* 304 */     this.RUNTIME = fromString("RUNTIME");
/* 305 */     this.SOURCE = fromString("SOURCE");
/*     */ 
/*     */     
/* 308 */     this.T = fromString("T");
/* 309 */     this.deprecated = fromString("deprecated");
/* 310 */     this.ex = fromString("ex");
/* 311 */     this.package_info = fromString("package-info");
/*     */ 
/*     */     
/* 314 */     this.lambda = fromString("lambda$");
/* 315 */     this.metafactory = fromString("metafactory");
/* 316 */     this.altMetafactory = fromString("altMetafactory");
/*     */   }
/*     */   
/*     */   protected Name.Table createTable(Options paramOptions) {
/* 320 */     boolean bool = paramOptions.isSet("useUnsharedTable");
/* 321 */     if (bool) {
/* 322 */       return new UnsharedNameTable(this);
/*     */     }
/* 324 */     return new SharedNameTable(this);
/*     */   }
/*     */   
/*     */   public void dispose() {
/* 328 */     this.table.dispose();
/*     */   }
/*     */   
/*     */   public Name fromChars(char[] paramArrayOfchar, int paramInt1, int paramInt2) {
/* 332 */     return this.table.fromChars(paramArrayOfchar, paramInt1, paramInt2);
/*     */   }
/*     */   
/*     */   public Name fromString(String paramString) {
/* 336 */     return this.table.fromString(paramString);
/*     */   }
/*     */   
/*     */   public Name fromUtf(byte[] paramArrayOfbyte) {
/* 340 */     return this.table.fromUtf(paramArrayOfbyte);
/*     */   }
/*     */   
/*     */   public Name fromUtf(byte[] paramArrayOfbyte, int paramInt1, int paramInt2) {
/* 344 */     return this.table.fromUtf(paramArrayOfbyte, paramInt1, paramInt2);
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\java\\util\Names.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */