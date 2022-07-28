/*     */ package com.sun.tools.internal.jxc;
/*     */
/*     */ import com.sun.tools.internal.jxc.ap.Options;
/*     */ import com.sun.tools.internal.jxc.ap.SchemaGenerator;
/*     */ import com.sun.tools.internal.xjc.BadCommandLineException;
/*     */ import com.sun.xml.internal.bind.util.Which;
/*     */ import java.io.File;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
/*     */ import java.net.MalformedURLException;
/*     */ import java.net.URISyntaxException;
/*     */ import java.net.URL;
/*     */ import java.net.URLClassLoader;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ import javax.lang.model.SourceVersion;
/*     */ import javax.tools.Diagnostic;
/*     */ import javax.tools.DiagnosticCollector;
/*     */ import javax.tools.JavaCompiler;
/*     */ import javax.tools.JavaFileObject;
/*     */ import javax.tools.OptionChecker;
/*     */ import javax.tools.StandardJavaFileManager;
/*     */ import javax.tools.ToolProvider;
/*     */ import javax.xml.bind.JAXBContext;
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */ public class SchemaGenerator
/*     */ {
/*     */   public static void main(String[] args) throws Exception {
/*  65 */     System.exit(run(args));
/*     */   }
/*     */
/*     */   public static int run(String[] args) throws Exception {
/*     */     try {
/*  70 */       ClassLoader cl = SecureLoader.getClassClassLoader(SchemaGenerator.class);
/*  71 */       if (cl == null) {
/*  72 */         cl = SecureLoader.getSystemClassLoader();
/*     */       }
/*  74 */       return run(args, cl);
/*  75 */     } catch (Exception e) {
/*  76 */       System.err.println(e.getMessage());
/*  77 */       return -1;
/*     */     }
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
/*     */   public static int run(String[] args, ClassLoader classLoader) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
/*  94 */     Options options = new Options();
/*  95 */     if (args.length == 0) {
/*  96 */       usage();
/*  97 */       return -1;
/*     */     }
/*  99 */     for (String arg : args) {
/* 100 */       if (arg.equals("-help")) {
/* 101 */         usage();
/* 102 */         return -1;
/*     */       }
/*     */
/* 105 */       if (arg.equals("-version")) {
/* 106 */         System.out.println(Messages.VERSION.format(new Object[0]));
/* 107 */         return -1;
/*     */       }
/*     */
/* 110 */       if (arg.equals("-fullversion")) {
/* 111 */         System.out.println(Messages.FULLVERSION.format(new Object[0]));
/* 112 */         return -1;
/*     */       }
/*     */     }
/*     */
/*     */
/*     */     try {
/* 118 */       options.parseArguments(args);
/* 119 */     } catch (BadCommandLineException e) {
/*     */
/*     */
/* 122 */       System.out.println(e.getMessage());
/* 123 */       System.out.println();
/* 124 */       usage();
/* 125 */       return -1;
/*     */     }
/*     */
/* 128 */     Class<?> schemagenRunner = classLoader.loadClass(Runner.class.getName());
/* 129 */     Method compileMethod = schemagenRunner.getDeclaredMethod("compile", new Class[] { String[].class, File.class });
/*     */
/* 131 */     List<String> aptargs = new ArrayList<>();
/*     */
/* 133 */     if (options.encoding != null) {
/* 134 */       aptargs.add("-encoding");
/* 135 */       aptargs.add(options.encoding);
/*     */     }
/*     */
/* 138 */     aptargs.add("-cp");
/* 139 */     aptargs.add(setClasspath(options.classpath));
/*     */
/* 141 */     if (options.targetDir != null) {
/* 142 */       aptargs.add("-d");
/* 143 */       aptargs.add(options.targetDir.getPath());
/*     */     }
/*     */
/* 146 */     aptargs.addAll(options.arguments);
/*     */
/* 148 */     String[] argsarray = aptargs.<String>toArray(new String[aptargs.size()]);
/* 149 */     return ((Boolean)compileMethod.invoke(null, new Object[] { argsarray, options.episodeFile })).booleanValue() ? 0 : 1;
/*     */   }
/*     */
/*     */   private static String setClasspath(String givenClasspath) {
/* 153 */     StringBuilder cp = new StringBuilder();
/* 154 */     appendPath(cp, givenClasspath);
/* 155 */     ClassLoader cl = Thread.currentThread().getContextClassLoader();
/* 156 */     while (cl != null) {
/* 157 */       if (cl instanceof URLClassLoader) {
/* 158 */         for (URL url : ((URLClassLoader)cl).getURLs()) {
/* 159 */           appendPath(cp, url.getPath());
/*     */         }
/*     */       }
/* 162 */       cl = cl.getParent();
/*     */     }
/*     */
/* 165 */     appendPath(cp, findJaxbApiJar());
/* 166 */     return cp.toString();
/*     */   }
/*     */
/*     */   private static void appendPath(StringBuilder cp, String url) {
/* 170 */     if (url == null || url.trim().isEmpty())
/*     */       return;
/* 172 */     if (cp.length() != 0)
/* 173 */       cp.append(File.pathSeparatorChar);
/* 174 */     cp.append(url);
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
/*     */   private static String findJaxbApiJar() {
/* 189 */     String url = Which.which(JAXBContext.class);
/* 190 */     if (url == null) return null;
/*     */
/* 192 */     if (!url.startsWith("jar:") || url.lastIndexOf('!') == -1)
/*     */     {
/* 194 */       return null;
/*     */     }
/* 196 */     String jarFileUrl = url.substring(4, url.lastIndexOf('!'));
/* 197 */     if (!jarFileUrl.startsWith("file:")) {
/* 198 */       return null;
/*     */     }
/*     */     try {
/* 201 */       File f = new File((new URL(jarFileUrl)).toURI());
/* 202 */       if (f.exists() && f.getName().endsWith(".jar")) {
/* 203 */         return f.getPath();
/*     */       }
/* 205 */       f = new File((new URL(jarFileUrl)).getFile());
/* 206 */       if (f.exists() && f.getName().endsWith(".jar")) {
/* 207 */         return f.getPath();
/*     */       }
/* 209 */     } catch (URISyntaxException ex) {
/* 210 */       Logger.getLogger(SchemaGenerator.class.getName()).log(Level.SEVERE, (String)null, ex);
/* 211 */     } catch (MalformedURLException ex) {
/* 212 */       Logger.getLogger(SchemaGenerator.class.getName()).log(Level.SEVERE, (String)null, ex);
/*     */     }
/* 214 */     return null;
/*     */   }
/*     */
/*     */   private static void usage() {
/* 218 */     System.out.println(Messages.USAGE.format(new Object[0]));
/*     */   }
/*     */
/*     */   public static final class Runner
/*     */   {
/*     */     public static boolean compile(String[] args, File episode) throws Exception {
/* 224 */       JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
/* 225 */       DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<>();
/* 226 */       StandardJavaFileManager fileManager = compiler.getStandardFileManager(diagnostics, null, null);
/* 227 */       JavacOptions options = JavacOptions.parse(compiler, fileManager, args);
/* 228 */       List<String> unrecognizedOptions = options.getUnrecognizedOptions();
/* 229 */       if (!unrecognizedOptions.isEmpty())
/* 230 */         Logger.getLogger(SchemaGenerator.class.getName()).log(Level.WARNING, "Unrecognized options found: {0}", unrecognizedOptions);
/* 231 */       Iterable<? extends JavaFileObject> compilationUnits = fileManager.getJavaFileObjectsFromFiles(options.getFiles());
/* 232 */       JavaCompiler.CompilationTask task = compiler.getTask(null, fileManager, diagnostics, options
/*     */
/*     */
/*     */
/* 236 */           .getRecognizedOptions(), options
/* 237 */           .getClassNames(), compilationUnits);
/*     */
/* 239 */       SchemaGenerator r = new SchemaGenerator();
/* 240 */       if (episode != null)
/* 241 */         r.setEpisodeFile(episode);
/* 242 */       task.setProcessors((Iterable)Collections.singleton(r));
/* 243 */       boolean res = task.call().booleanValue();
/*     */
/* 245 */       for (Diagnostic<? extends JavaFileObject> d : diagnostics.getDiagnostics()) {
/* 246 */         System.err.println(d.toString());
/*     */       }
/* 248 */       return res;
/*     */     }
/*     */   }
/*     */
/*     */
/*     */   private static final class JavacOptions
/*     */   {
/*     */     private final List<String> recognizedOptions;
/*     */
/*     */     private final List<String> classNames;
/*     */
/*     */     private final List<File> files;
/*     */     private final List<String> unrecognizedOptions;
/*     */
/*     */     private JavacOptions(List<String> recognizedOptions, List<String> classNames, List<File> files, List<String> unrecognizedOptions) {
/* 263 */       this.recognizedOptions = recognizedOptions;
/* 264 */       this.classNames = classNames;
/* 265 */       this.files = files;
/* 266 */       this.unrecognizedOptions = unrecognizedOptions;
/*     */     }
/*     */
/*     */     public static JavacOptions parse(OptionChecker primary, OptionChecker secondary, String... arguments) {
/* 270 */       List<String> recognizedOptions = new ArrayList<>();
/* 271 */       List<String> unrecognizedOptions = new ArrayList<>();
/* 272 */       List<String> classNames = new ArrayList<>();
/* 273 */       List<File> files = new ArrayList<>();
/* 274 */       for (int i = 0; i < arguments.length; i++) {
/* 275 */         String argument = arguments[i];
/* 276 */         int optionCount = primary.isSupportedOption(argument);
/* 277 */         if (optionCount < 0) {
/* 278 */           optionCount = secondary.isSupportedOption(argument);
/*     */         }
/* 280 */         if (optionCount < 0)
/* 281 */         { File file = new File(argument);
/* 282 */           if (file.exists()) {
/* 283 */             files.add(file);
/* 284 */           } else if (SourceVersion.isName(argument)) {
/* 285 */             classNames.add(argument);
/*     */           } else {
/* 287 */             unrecognizedOptions.add(argument);
/*     */           }  }
/* 289 */         else { for (int j = 0; j < optionCount + 1; j++) {
/* 290 */             int index = i + j;
/* 291 */             if (index == arguments.length) throw new IllegalArgumentException(argument);
/* 292 */             recognizedOptions.add(arguments[index]);
/*     */           }
/* 294 */           i += optionCount; }
/*     */
/*     */       }
/* 297 */       return new JavacOptions(recognizedOptions, classNames, files, unrecognizedOptions);
/*     */     }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */     public List<String> getRecognizedOptions() {
/* 306 */       return Collections.unmodifiableList(this.recognizedOptions);
/*     */     }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */     public List<File> getFiles() {
/* 315 */       return Collections.unmodifiableList(this.files);
/*     */     }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */     public List<String> getClassNames() {
/* 324 */       return Collections.unmodifiableList(this.classNames);
/*     */     }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */     public List<String> getUnrecognizedOptions() {
/* 333 */       return Collections.unmodifiableList(this.unrecognizedOptions);
/*     */     }
/*     */
/*     */
/*     */     public String toString() {
/* 338 */       return String.format("recognizedOptions = %s; classNames = %s; files = %s; unrecognizedOptions = %s", new Object[] { this.recognizedOptions, this.classNames, this.files, this.unrecognizedOptions });
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\jxc\SchemaGenerator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
