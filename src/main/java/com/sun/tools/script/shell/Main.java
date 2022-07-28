/*     */ package com.sun.tools.script.shell;
/*     */ 
/*     */ import java.io.BufferedReader;
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.FileNotFoundException;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.PrintStream;
/*     */ import java.io.Reader;
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.net.MalformedURLException;
/*     */ import java.net.URL;
/*     */ import java.net.URLClassLoader;
/*     */ import java.text.MessageFormat;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import java.util.ResourceBundle;
/*     */ import javax.script.ScriptEngine;
/*     */ import javax.script.ScriptEngineFactory;
/*     */ import javax.script.ScriptEngineManager;
/*     */ import javax.script.ScriptException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Main
/*     */ {
/*     */   private static final int EXIT_SUCCESS = 0;
/*     */   private static final int EXIT_CMD_NO_CLASSPATH = 1;
/*     */   private static final int EXIT_CMD_NO_FILE = 2;
/*     */   private static final int EXIT_CMD_NO_SCRIPT = 3;
/*     */   private static final int EXIT_CMD_NO_LANG = 4;
/*     */   private static final int EXIT_CMD_NO_ENCODING = 5;
/*     */   private static final int EXIT_CMD_NO_PROPNAME = 6;
/*     */   private static final int EXIT_UNKNOWN_OPTION = 7;
/*     */   private static final int EXIT_ENGINE_NOT_FOUND = 8;
/*     */   
/*     */   public static void main(String[] paramArrayOfString) {
/*  44 */     String[] arrayOfString = processOptions(paramArrayOfString);
/*     */ 
/*     */     
/*  47 */     for (Command command : scripts) {
/*  48 */       command.run(arrayOfString);
/*     */     }
/*     */     
/*  51 */     System.exit(0);
/*     */   }
/*     */ 
/*     */   
/*     */   private static final int EXIT_NO_ENCODING_FOUND = 9;
/*     */   private static final int EXIT_SCRIPT_ERROR = 10;
/*     */   private static final int EXIT_FILE_NOT_FOUND = 11;
/*     */   private static final int EXIT_MULTIPLE_STDIN = 12;
/*     */   private static final String DEFAULT_LANGUAGE = "js";
/*     */   private static List<Command> scripts;
/*     */   private static ScriptEngineManager engineManager;
/*     */   private static Map<String, ScriptEngine> engines;
/*     */   private static ResourceBundle msgRes;
/*     */   
/*     */   private static String[] processOptions(String[] paramArrayOfString) {
/*  66 */     String str1 = "js";
/*     */     
/*  68 */     String str2 = null;
/*     */ 
/*     */     
/*  71 */     checkClassPath(paramArrayOfString);
/*     */ 
/*     */     
/*  74 */     boolean bool1 = false;
/*     */     
/*  76 */     boolean bool2 = false;
/*  77 */     byte b = 0; while (true) { if (b < paramArrayOfString.length)
/*  78 */       { String str = paramArrayOfString[b];
/*  79 */         if (str.equals("-classpath") || str
/*  80 */           .equals("-cp"))
/*     */         
/*  82 */         { b++;
/*     */            }
/*     */         
/*     */         else
/*     */         
/*  87 */         { if (!str.startsWith("-")) {
/*     */             int i, j;
/*     */             
/*  90 */             if (bool1) {
/*     */ 
/*     */               
/*  93 */               i = paramArrayOfString.length - b;
/*  94 */               j = b;
/*     */             
/*     */             }
/*     */             else {
/*     */               
/*  99 */               i = paramArrayOfString.length - b - 1;
/* 100 */               j = b + 1;
/* 101 */               ScriptEngine scriptEngine = getScriptEngine(str1);
/* 102 */               addFileSource(scriptEngine, paramArrayOfString[b], str2);
/*     */             } 
/*     */             
/* 105 */             String[] arrayOfString = new String[i];
/* 106 */             System.arraycopy(paramArrayOfString, j, arrayOfString, 0, i);
/* 107 */             return arrayOfString;
/*     */           } 
/*     */           
/* 110 */           if (str.startsWith("-D"))
/* 111 */           { String str3 = str.substring(2);
/* 112 */             int i = str3.indexOf('=');
/* 113 */             if (i != -1) {
/* 114 */               System.setProperty(str3.substring(0, i), str3
/* 115 */                   .substring(i + 1));
/*     */             }
/* 117 */             else if (!str3.equals("")) {
/* 118 */               System.setProperty(str3, "");
/*     */             } else {
/*     */               
/* 121 */               usage(6);
/*     */             }  }
/*     */           else
/*     */           
/* 125 */           { if (str.equals("-?") || str.equals("-help"))
/* 126 */             { usage(0); }
/* 127 */             else { if (str.equals("-e"))
/* 128 */               { bool1 = true;
/* 129 */                 if (++b == paramArrayOfString.length) {
/* 130 */                   usage(3);
/*     */                 }
/* 132 */                 ScriptEngine scriptEngine = getScriptEngine(str1);
/* 133 */                 addStringSource(scriptEngine, paramArrayOfString[b]); }
/*     */               
/* 135 */               else if (str.equals("-encoding"))
/* 136 */               { if (++b == paramArrayOfString.length)
/* 137 */                   usage(5); 
/* 138 */                 str2 = paramArrayOfString[b]; }
/*     */               
/* 140 */               else if (str.equals("-f"))
/* 141 */               { bool1 = true;
/* 142 */                 if (++b == paramArrayOfString.length)
/* 143 */                   usage(2); 
/* 144 */                 ScriptEngine scriptEngine = getScriptEngine(str1);
/* 145 */                 if (paramArrayOfString[b].equals("-")) {
/* 146 */                   if (bool2) {
/* 147 */                     usage(12);
/*     */                   } else {
/* 149 */                     bool2 = true;
/*     */                   } 
/* 151 */                   addInteractiveMode(scriptEngine);
/*     */                 } else {
/* 153 */                   addFileSource(scriptEngine, paramArrayOfString[b], str2);
/*     */                 }
/*     */                  }
/* 156 */               else if (str.equals("-l"))
/* 157 */               { if (++b == paramArrayOfString.length)
/* 158 */                   usage(4); 
/* 159 */                 str1 = paramArrayOfString[b]; }
/*     */               else
/* 161 */               { if (str.equals("-q")) {
/* 162 */                   listScriptEngines();
/*     */                 }
/*     */                 
/* 165 */                 usage(7); }  b++; }  usage(7); }  }  }
/*     */       else { break; }
/*     */        b++; }
/* 168 */      if (!bool1) {
/* 169 */       ScriptEngine scriptEngine = getScriptEngine(str1);
/* 170 */       addInteractiveMode(scriptEngine);
/*     */     } 
/* 172 */     return new String[0];
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void addInteractiveMode(final ScriptEngine se) {
/* 180 */     scripts.add(new Command() {
/*     */           public void run(String[] param1ArrayOfString) {
/* 182 */             Main.setScriptArguments(se, param1ArrayOfString);
/* 183 */             Main.processSource(se, "-", null);
/*     */           }
/*     */         });
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
/*     */   private static void addFileSource(final ScriptEngine se, final String fileName, final String encoding) {
/* 197 */     scripts.add(new Command() {
/*     */           public void run(String[] param1ArrayOfString) {
/* 199 */             Main.setScriptArguments(se, param1ArrayOfString);
/* 200 */             Main.processSource(se, fileName, encoding);
/*     */           }
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void addStringSource(final ScriptEngine se, final String source) {
/* 212 */     scripts.add(new Command() {
/*     */           public void run(String[] param1ArrayOfString) {
/* 214 */             Main.setScriptArguments(se, param1ArrayOfString);
/* 215 */             String str = Main.setScriptFilename(se, "<string>");
/*     */             try {
/* 217 */               Main.evaluateString(se, source);
/*     */             } finally {
/* 219 */               Main.setScriptFilename(se, str);
/*     */             } 
/*     */           }
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void listScriptEngines() {
/* 229 */     List<ScriptEngineFactory> list = engineManager.getEngineFactories();
/* 230 */     for (ScriptEngineFactory scriptEngineFactory : list) {
/* 231 */       getError().println(getMessage("engine.info", new Object[] { scriptEngineFactory
/* 232 */               .getLanguageName(), scriptEngineFactory
/* 233 */               .getLanguageVersion(), scriptEngineFactory
/* 234 */               .getEngineName(), scriptEngineFactory
/* 235 */               .getEngineVersion() }));
/*     */     } 
/*     */     
/* 238 */     System.exit(0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void processSource(ScriptEngine paramScriptEngine, String paramString1, String paramString2) {
/* 249 */     if (paramString1.equals("-")) {
/*     */       
/* 251 */       BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(getIn()));
/* 252 */       boolean bool = false;
/* 253 */       String str = getPrompt(paramScriptEngine);
/* 254 */       paramScriptEngine.put("javax.script.filename", "<STDIN>");
/* 255 */       while (!bool) {
/* 256 */         getError().print(str);
/* 257 */         String str1 = "";
/*     */         try {
/* 259 */           str1 = bufferedReader.readLine();
/* 260 */         } catch (IOException iOException) {
/* 261 */           getError().println(iOException.toString());
/*     */         } 
/* 263 */         if (str1 == null) {
/* 264 */           bool = true;
/*     */           break;
/*     */         } 
/* 267 */         Object object = evaluateString(paramScriptEngine, str1, false);
/* 268 */         if (object != null) {
/* 269 */           object = object.toString();
/* 270 */           if (object == null) {
/* 271 */             object = "null";
/*     */           }
/* 273 */           getError().println(object);
/*     */         } 
/*     */       } 
/*     */     } else {
/* 277 */       FileInputStream fileInputStream = null;
/*     */       try {
/* 279 */         fileInputStream = new FileInputStream(paramString1);
/* 280 */       } catch (FileNotFoundException fileNotFoundException) {
/* 281 */         getError().println(getMessage("file.not.found", new Object[] { paramString1 }));
/*     */         
/* 283 */         System.exit(11);
/*     */       } 
/* 285 */       evaluateStream(paramScriptEngine, fileInputStream, paramString1, paramString2);
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
/*     */   private static Object evaluateString(ScriptEngine paramScriptEngine, String paramString, boolean paramBoolean) {
/*     */     try {
/* 298 */       return paramScriptEngine.eval(paramString);
/* 299 */     } catch (ScriptException scriptException) {
/* 300 */       getError().println(getMessage("string.script.error", new Object[] { scriptException
/* 301 */               .getMessage() }));
/* 302 */       if (paramBoolean)
/* 303 */         System.exit(10); 
/* 304 */     } catch (Exception exception) {
/* 305 */       exception.printStackTrace(getError());
/* 306 */       if (paramBoolean) {
/* 307 */         System.exit(10);
/*     */       }
/*     */     } 
/* 310 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void evaluateString(ScriptEngine paramScriptEngine, String paramString) {
/* 319 */     evaluateString(paramScriptEngine, paramString, true);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static Object evaluateReader(ScriptEngine paramScriptEngine, Reader paramReader, String paramString) {
/* 330 */     String str = setScriptFilename(paramScriptEngine, paramString);
/*     */     try {
/* 332 */       return paramScriptEngine.eval(paramReader);
/* 333 */     } catch (ScriptException scriptException) {
/* 334 */       getError().println(getMessage("file.script.error", new Object[] { paramString, scriptException
/* 335 */               .getMessage() }));
/* 336 */       System.exit(10);
/* 337 */     } catch (Exception exception) {
/* 338 */       exception.printStackTrace(getError());
/* 339 */       System.exit(10);
/*     */     } finally {
/* 341 */       setScriptFilename(paramScriptEngine, str);
/*     */     } 
/* 343 */     return null;
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
/*     */   private static Object evaluateStream(ScriptEngine paramScriptEngine, InputStream paramInputStream, String paramString1, String paramString2) {
/* 355 */     BufferedReader bufferedReader = null;
/* 356 */     if (paramString2 != null) {
/*     */       try {
/* 358 */         bufferedReader = new BufferedReader(new InputStreamReader(paramInputStream, paramString2));
/*     */       }
/* 360 */       catch (UnsupportedEncodingException unsupportedEncodingException) {
/* 361 */         getError().println(getMessage("encoding.unsupported", new Object[] { paramString2 }));
/*     */         
/* 363 */         System.exit(9);
/*     */       } 
/*     */     } else {
/* 366 */       bufferedReader = new BufferedReader(new InputStreamReader(paramInputStream));
/*     */     } 
/* 368 */     return evaluateReader(paramScriptEngine, bufferedReader, paramString1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void usage(int paramInt) {
/* 376 */     getError().println(getMessage("main.usage", new Object[] { PROGRAM_NAME }));
/*     */     
/* 378 */     System.exit(paramInt);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static String getPrompt(ScriptEngine paramScriptEngine) {
/* 386 */     List<String> list = paramScriptEngine.getFactory().getNames();
/* 387 */     return (String)list.get(0) + "> ";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static String getMessage(String paramString, Object[] paramArrayOfObject) {
/* 394 */     return MessageFormat.format(msgRes.getString(paramString), paramArrayOfObject);
/*     */   }
/*     */ 
/*     */   
/*     */   private static InputStream getIn() {
/* 399 */     return System.in;
/*     */   }
/*     */ 
/*     */   
/*     */   private static PrintStream getError() {
/* 404 */     return System.err;
/*     */   }
/*     */ 
/*     */   
/*     */   private static ScriptEngine getScriptEngine(String paramString) {
/* 409 */     ScriptEngine scriptEngine = engines.get(paramString);
/* 410 */     if (scriptEngine == null) {
/* 411 */       scriptEngine = engineManager.getEngineByName(paramString);
/* 412 */       if (scriptEngine == null) {
/* 413 */         getError().println(getMessage("engine.not.found", new Object[] { paramString }));
/*     */         
/* 415 */         System.exit(8);
/*     */       } 
/*     */ 
/*     */       
/* 419 */       initScriptEngine(scriptEngine);
/*     */       
/* 421 */       engines.put(paramString, scriptEngine);
/*     */     } 
/* 423 */     return scriptEngine;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static void initScriptEngine(ScriptEngine paramScriptEngine) {
/* 429 */     paramScriptEngine.put("engine", paramScriptEngine);
/*     */ 
/*     */     
/* 432 */     List<String> list = paramScriptEngine.getFactory().getExtensions();
/* 433 */     InputStream inputStream = null;
/* 434 */     ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
/* 435 */     for (String str : list) {
/* 436 */       inputStream = classLoader.getResourceAsStream("com/sun/tools/script/shell/init." + str);
/*     */       
/* 438 */       if (inputStream != null)
/*     */         break; 
/* 440 */     }  if (inputStream != null) {
/* 441 */       evaluateStream(paramScriptEngine, inputStream, "<system-init>", null);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void checkClassPath(String[] paramArrayOfString) {
/* 452 */     String str = null;
/* 453 */     for (byte b = 0; b < paramArrayOfString.length; b++) {
/* 454 */       if (paramArrayOfString[b].equals("-classpath") || paramArrayOfString[b]
/* 455 */         .equals("-cp")) {
/* 456 */         if (++b == paramArrayOfString.length) {
/*     */           
/* 458 */           usage(1);
/*     */         } else {
/* 460 */           str = paramArrayOfString[b];
/*     */         } 
/*     */       }
/*     */     } 
/*     */     
/* 465 */     if (str != null) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 477 */       ClassLoader classLoader = Main.class.getClassLoader();
/* 478 */       URL[] arrayOfURL = pathToURLs(str);
/* 479 */       URLClassLoader uRLClassLoader = new URLClassLoader(arrayOfURL, classLoader);
/* 480 */       Thread.currentThread().setContextClassLoader(uRLClassLoader);
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 486 */     engineManager = new ScriptEngineManager();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static URL[] pathToURLs(String paramString) {
/* 497 */     String[] arrayOfString = paramString.split(File.pathSeparator);
/* 498 */     URL[] arrayOfURL = new URL[arrayOfString.length];
/* 499 */     byte b = 0;
/* 500 */     while (b < arrayOfString.length) {
/* 501 */       URL uRL = fileToURL(new File(arrayOfString[b]));
/* 502 */       if (uRL != null) {
/* 503 */         arrayOfURL[b++] = uRL;
/*     */       }
/*     */     } 
/* 506 */     if (arrayOfURL.length != b) {
/* 507 */       URL[] arrayOfURL1 = new URL[b];
/* 508 */       System.arraycopy(arrayOfURL, 0, arrayOfURL1, 0, b);
/* 509 */       arrayOfURL = arrayOfURL1;
/*     */     } 
/* 511 */     return arrayOfURL;
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
/*     */   private static URL fileToURL(File paramFile) {
/*     */     try {
/* 524 */       str = paramFile.getCanonicalPath();
/* 525 */     } catch (IOException iOException) {
/* 526 */       str = paramFile.getAbsolutePath();
/*     */     } 
/* 528 */     String str = str.replace(File.separatorChar, '/');
/* 529 */     if (!str.startsWith("/")) {
/* 530 */       str = "/" + str;
/*     */     }
/*     */     
/* 533 */     if (!paramFile.isFile()) {
/* 534 */       str = str + "/";
/*     */     }
/*     */     try {
/* 537 */       return new URL("file", "", str);
/* 538 */     } catch (MalformedURLException malformedURLException) {
/* 539 */       throw new IllegalArgumentException("file");
/*     */     } 
/*     */   }
/*     */   
/*     */   private static void setScriptArguments(ScriptEngine paramScriptEngine, String[] paramArrayOfString) {
/* 544 */     paramScriptEngine.put("arguments", paramArrayOfString);
/* 545 */     paramScriptEngine.put("javax.script.argv", paramArrayOfString);
/*     */   }
/*     */   
/*     */   private static String setScriptFilename(ScriptEngine paramScriptEngine, String paramString) {
/* 549 */     String str = (String)paramScriptEngine.get("javax.script.filename");
/* 550 */     paramScriptEngine.put("javax.script.filename", paramString);
/* 551 */     return str;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 579 */   private static String BUNDLE_NAME = "com.sun.tools.script.shell.messages";
/* 580 */   private static String PROGRAM_NAME = "jrunscript";
/*     */   
/*     */   static {
/* 583 */     scripts = new ArrayList<>();
/* 584 */     engines = new HashMap<>();
/* 585 */     msgRes = ResourceBundle.getBundle(BUNDLE_NAME, Locale.getDefault());
/*     */   }
/*     */   
/*     */   private static interface Command {
/*     */     void run(String[] param1ArrayOfString);
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\script\shell\Main.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */