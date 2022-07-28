/*     */ package com.sun.tools.javac.api;
/*     */ 
/*     */ import com.sun.source.util.JavacTask;
/*     */ import com.sun.tools.javac.Main;
/*     */ import com.sun.tools.javac.file.JavacFileManager;
/*     */ import com.sun.tools.javac.main.Main;
/*     */ import com.sun.tools.javac.main.Option;
/*     */ import com.sun.tools.javac.main.OptionHelper;
/*     */ import com.sun.tools.javac.util.ClientCodeException;
/*     */ import com.sun.tools.javac.util.Context;
/*     */ import com.sun.tools.javac.util.Log;
/*     */ import com.sun.tools.javac.util.Options;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.io.OutputStreamWriter;
/*     */ import java.io.PrintWriter;
/*     */ import java.io.Writer;
/*     */ import java.nio.charset.Charset;
/*     */ import java.util.Collections;
/*     */ import java.util.EnumSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.Locale;
/*     */ import java.util.Set;
/*     */ import javax.lang.model.SourceVersion;
/*     */ import javax.tools.DiagnosticListener;
/*     */ import javax.tools.JavaCompiler;
/*     */ import javax.tools.JavaFileManager;
/*     */ import javax.tools.JavaFileObject;
/*     */ import javax.tools.StandardJavaFileManager;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class JavacTool
/*     */   implements JavaCompiler
/*     */ {
/*     */   public static JavacTool create() {
/*  81 */     return new JavacTool();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JavacFileManager getStandardFileManager(DiagnosticListener<? super JavaFileObject> paramDiagnosticListener, Locale paramLocale, Charset paramCharset) {
/*  88 */     Context context = new Context();
/*  89 */     context.put(Locale.class, paramLocale);
/*  90 */     if (paramDiagnosticListener != null)
/*  91 */       context.put(DiagnosticListener.class, paramDiagnosticListener); 
/*  92 */     PrintWriter printWriter = (paramCharset == null) ? new PrintWriter(System.err, true) : new PrintWriter(new OutputStreamWriter(System.err, paramCharset), true);
/*     */ 
/*     */     
/*  95 */     context.put(Log.outKey, printWriter);
/*  96 */     return new JavacFileManager(context, true, paramCharset);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JavacTask getTask(Writer paramWriter, JavaFileManager paramJavaFileManager, DiagnosticListener<? super JavaFileObject> paramDiagnosticListener, Iterable<String> paramIterable1, Iterable<String> paramIterable2, Iterable<? extends JavaFileObject> paramIterable) {
/* 106 */     Context context = new Context();
/* 107 */     return getTask(paramWriter, paramJavaFileManager, paramDiagnosticListener, paramIterable1, paramIterable2, paramIterable, context);
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
/*     */   public JavacTask getTask(Writer paramWriter, JavaFileManager paramJavaFileManager, DiagnosticListener<? super JavaFileObject> paramDiagnosticListener, Iterable<String> paramIterable1, Iterable<String> paramIterable2, Iterable<? extends JavaFileObject> paramIterable, Context paramContext) {
/*     */     try {
/*     */       JavacFileManager javacFileManager;
/* 121 */       ClientCodeWrapper clientCodeWrapper = ClientCodeWrapper.instance(paramContext);
/*     */       
/* 123 */       if (paramIterable1 != null)
/* 124 */         for (String str : paramIterable1)
/* 125 */           str.getClass();  
/* 126 */       if (paramIterable2 != null)
/* 127 */         for (String str : paramIterable2) {
/* 128 */           if (!SourceVersion.isName(str))
/* 129 */             throw new IllegalArgumentException("Not a valid class name: " + str); 
/*     */         }  
/* 131 */       if (paramIterable != null) {
/* 132 */         paramIterable = clientCodeWrapper.wrapJavaFileObjects(paramIterable);
/* 133 */         for (JavaFileObject javaFileObject : paramIterable) {
/* 134 */           if (javaFileObject.getKind() != JavaFileObject.Kind.SOURCE) {
/*     */             
/* 136 */             String str = "Compilation unit is not of SOURCE kind: \"" + javaFileObject.getName() + "\"";
/* 137 */             throw new IllegalArgumentException(str);
/*     */           } 
/*     */         } 
/*     */       } 
/*     */       
/* 142 */       if (paramDiagnosticListener != null) {
/* 143 */         paramContext.put(DiagnosticListener.class, clientCodeWrapper.wrap(paramDiagnosticListener));
/*     */       }
/* 145 */       if (paramWriter == null) {
/* 146 */         paramContext.put(Log.outKey, new PrintWriter(System.err, true));
/*     */       } else {
/* 148 */         paramContext.put(Log.outKey, new PrintWriter(paramWriter, true));
/*     */       } 
/* 150 */       if (paramJavaFileManager == null)
/* 151 */         javacFileManager = getStandardFileManager(paramDiagnosticListener, (Locale)null, (Charset)null); 
/* 152 */       JavaFileManager javaFileManager = clientCodeWrapper.wrap((JavaFileManager)javacFileManager);
/*     */       
/* 154 */       paramContext.put(JavaFileManager.class, javaFileManager);
/*     */       
/* 156 */       processOptions(paramContext, javaFileManager, paramIterable1);
/* 157 */       Main main = new Main("javacTask", (PrintWriter)paramContext.get(Log.outKey));
/* 158 */       return new JavacTaskImpl(main, paramIterable1, paramContext, paramIterable2, paramIterable);
/* 159 */     } catch (ClientCodeException clientCodeException) {
/* 160 */       throw new RuntimeException(clientCodeException.getCause());
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void processOptions(Context paramContext, JavaFileManager paramJavaFileManager, Iterable<String> paramIterable) {
/* 168 */     if (paramIterable == null) {
/*     */       return;
/*     */     }
/* 171 */     final Options optionTable = Options.instance(paramContext);
/* 172 */     Log log = Log.instance(paramContext);
/*     */ 
/*     */     
/* 175 */     Option[] arrayOfOption = (Option[])Option.getJavacToolOptions().toArray((Object[])new Option[0]);
/* 176 */     OptionHelper.GrumpyHelper grumpyHelper = new OptionHelper.GrumpyHelper(log)
/*     */       {
/*     */         public String get(Option param1Option) {
/* 179 */           return optionTable.get(param1Option.getText());
/*     */         }
/*     */ 
/*     */         
/*     */         public void put(String param1String1, String param1String2) {
/* 184 */           optionTable.put(param1String1, param1String2);
/*     */         }
/*     */ 
/*     */         
/*     */         public void remove(String param1String) {
/* 189 */           optionTable.remove(param1String);
/*     */         }
/*     */       };
/*     */     
/* 193 */     Iterator<String> iterator = paramIterable.iterator();
/* 194 */     while (iterator.hasNext()) {
/* 195 */       String str = iterator.next();
/*     */       byte b;
/* 197 */       for (b = 0; b < arrayOfOption.length && 
/* 198 */         !arrayOfOption[b].matches(str); b++);
/*     */ 
/*     */       
/* 201 */       if (b == arrayOfOption.length) {
/* 202 */         if (paramJavaFileManager.handleOption(str, iterator)) {
/*     */           continue;
/*     */         }
/* 205 */         String str1 = log.localize(Log.PrefixKind.JAVAC, "err.invalid.flag", new Object[] { str });
/* 206 */         throw new IllegalArgumentException(str1);
/*     */       } 
/*     */ 
/*     */       
/* 210 */       Option option = arrayOfOption[b];
/* 211 */       if (option.hasArg()) {
/* 212 */         if (!iterator.hasNext()) {
/* 213 */           String str2 = log.localize(Log.PrefixKind.JAVAC, "err.req.arg", new Object[] { str });
/* 214 */           throw new IllegalArgumentException(str2);
/*     */         } 
/* 216 */         String str1 = iterator.next();
/* 217 */         if (option.process((OptionHelper)grumpyHelper, str, str1))
/*     */         {
/*     */           
/* 220 */           throw new IllegalArgumentException(str + " " + str1); }  continue;
/*     */       } 
/* 222 */       if (option.process((OptionHelper)grumpyHelper, str))
/*     */       {
/*     */         
/* 225 */         throw new IllegalArgumentException(str);
/*     */       }
/*     */     } 
/*     */     
/* 229 */     options.notifyListeners();
/*     */   }
/*     */   
/*     */   public int run(InputStream paramInputStream, OutputStream paramOutputStream1, OutputStream paramOutputStream2, String... paramVarArgs) {
/* 233 */     if (paramOutputStream2 == null)
/* 234 */       paramOutputStream2 = System.err; 
/* 235 */     for (String str : paramVarArgs)
/* 236 */       str.getClass(); 
/* 237 */     return Main.compile(paramVarArgs, new PrintWriter(paramOutputStream2, true));
/*     */   }
/*     */   
/*     */   public Set<SourceVersion> getSourceVersions() {
/* 241 */     return Collections.unmodifiableSet(EnumSet.range(SourceVersion.RELEASE_3, 
/* 242 */           SourceVersion.latest()));
/*     */   }
/*     */   
/*     */   public int isSupportedOption(String paramString) {
/* 246 */     Set set = Option.getJavacToolOptions();
/* 247 */     for (Option option : set) {
/* 248 */       if (option.matches(paramString))
/* 249 */         return option.hasArg() ? 1 : 0; 
/*     */     } 
/* 251 */     return -1;
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\javac\api\JavacTool.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */