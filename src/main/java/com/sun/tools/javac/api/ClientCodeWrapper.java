/*     */ package com.sun.tools.javac.api;
/*     */
/*     */ import com.sun.source.util.TaskEvent;
/*     */ import com.sun.source.util.TaskListener;
/*     */ import com.sun.tools.javac.util.ClientCodeException;
/*     */ import com.sun.tools.javac.util.Context;
/*     */ import com.sun.tools.javac.util.JCDiagnostic;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.io.Reader;
/*     */ import java.io.Writer;
/*     */ import java.lang.annotation.ElementType;
/*     */ import java.lang.annotation.Retention;
/*     */ import java.lang.annotation.RetentionPolicy;
/*     */ import java.lang.annotation.Target;
/*     */ import java.net.URI;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import javax.lang.model.element.Modifier;
/*     */ import javax.lang.model.element.NestingKind;
/*     */ import javax.tools.Diagnostic;
/*     */ import javax.tools.DiagnosticListener;
/*     */ import javax.tools.FileObject;
/*     */ import javax.tools.JavaFileManager;
/*     */ import javax.tools.JavaFileObject;
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */ public class ClientCodeWrapper
/*     */ {
/*     */   Map<Class<?>, Boolean> trustedClasses;
/*     */
/*     */   public static ClientCodeWrapper instance(Context paramContext) {
/*  97 */     ClientCodeWrapper clientCodeWrapper = (ClientCodeWrapper)paramContext.get(ClientCodeWrapper.class);
/*  98 */     if (clientCodeWrapper == null)
/*  99 */       clientCodeWrapper = new ClientCodeWrapper(paramContext);
/* 100 */     return clientCodeWrapper;
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   protected ClientCodeWrapper(Context paramContext) {
/* 110 */     this.trustedClasses = new HashMap<>();
/*     */   }
/*     */
/*     */   public JavaFileManager wrap(JavaFileManager paramJavaFileManager) {
/* 114 */     if (isTrusted(paramJavaFileManager))
/* 115 */       return paramJavaFileManager;
/* 116 */     return new WrappedJavaFileManager(paramJavaFileManager);
/*     */   }
/*     */
/*     */   public FileObject wrap(FileObject paramFileObject) {
/* 120 */     if (isTrusted(paramFileObject))
/* 121 */       return paramFileObject;
/* 122 */     return new WrappedFileObject(paramFileObject);
/*     */   }
/*     */
/*     */   FileObject unwrap(FileObject paramFileObject) {
/* 126 */     if (paramFileObject instanceof WrappedFileObject) {
/* 127 */       return ((WrappedFileObject)paramFileObject).clientFileObject;
/*     */     }
/* 129 */     return paramFileObject;
/*     */   }
/*     */
/*     */   public JavaFileObject wrap(JavaFileObject paramJavaFileObject) {
/* 133 */     if (isTrusted(paramJavaFileObject))
/* 134 */       return paramJavaFileObject;
/* 135 */     return new WrappedJavaFileObject(paramJavaFileObject);
/*     */   }
/*     */
/*     */   public Iterable<JavaFileObject> wrapJavaFileObjects(Iterable<? extends JavaFileObject> paramIterable) {
/* 139 */     ArrayList<JavaFileObject> arrayList = new ArrayList();
/* 140 */     for (JavaFileObject javaFileObject : paramIterable)
/* 141 */       arrayList.add(wrap(javaFileObject));
/* 142 */     return Collections.unmodifiableList(arrayList);
/*     */   }
/*     */
/*     */   JavaFileObject unwrap(JavaFileObject paramJavaFileObject) {
/* 146 */     if (paramJavaFileObject instanceof WrappedJavaFileObject) {
/* 147 */       return (JavaFileObject)((WrappedJavaFileObject)paramJavaFileObject).clientFileObject;
/*     */     }
/* 149 */     return paramJavaFileObject;
/*     */   }
/*     */
/*     */   public <T> DiagnosticListener<T> wrap(DiagnosticListener<T> paramDiagnosticListener) {
/* 153 */     if (isTrusted(paramDiagnosticListener))
/* 154 */       return paramDiagnosticListener;
/* 155 */     return new WrappedDiagnosticListener<>(paramDiagnosticListener);
/*     */   }
/*     */
/*     */   TaskListener wrap(TaskListener paramTaskListener) {
/* 159 */     if (isTrusted(paramTaskListener))
/* 160 */       return paramTaskListener;
/* 161 */     return new WrappedTaskListener(paramTaskListener);
/*     */   }
/*     */
/*     */   TaskListener unwrap(TaskListener paramTaskListener) {
/* 165 */     if (paramTaskListener instanceof WrappedTaskListener) {
/* 166 */       return ((WrappedTaskListener)paramTaskListener).clientTaskListener;
/*     */     }
/* 168 */     return paramTaskListener;
/*     */   }
/*     */
/*     */   Collection<TaskListener> unwrap(Collection<? extends TaskListener> paramCollection) {
/* 172 */     ArrayList<TaskListener> arrayList = new ArrayList(paramCollection.size());
/* 173 */     for (TaskListener taskListener : paramCollection)
/* 174 */       arrayList.add(unwrap(taskListener));
/* 175 */     return arrayList;
/*     */   }
/*     */
/*     */
/*     */   private <T> Diagnostic<T> unwrap(Diagnostic<T> paramDiagnostic) {
/* 180 */     if (paramDiagnostic instanceof JCDiagnostic) {
/* 181 */       JCDiagnostic jCDiagnostic = (JCDiagnostic)paramDiagnostic;
/* 182 */       return new DiagnosticSourceUnwrapper(jCDiagnostic);
/*     */     }
/* 184 */     return paramDiagnostic;
/*     */   }
/*     */
/*     */
/*     */   protected boolean isTrusted(Object paramObject) {
/* 189 */     Class<?> clazz = paramObject.getClass();
/* 190 */     Boolean bool = this.trustedClasses.get(clazz);
/* 191 */     if (bool == null) {
/* 192 */       bool = Boolean.valueOf((clazz.getName().startsWith("com.sun.tools.javac.") || clazz
/* 193 */           .isAnnotationPresent((Class)Trusted.class)));
/* 194 */       this.trustedClasses.put(clazz, bool);
/*     */     }
/* 196 */     return bool.booleanValue();
/*     */   }
/*     */
/*     */   private String wrappedToString(Class<?> paramClass, Object paramObject) {
/* 200 */     return paramClass.getSimpleName() + "[" + paramObject + "]";
/*     */   }
/*     */
/*     */
/*     */   protected class WrappedJavaFileManager
/*     */     implements JavaFileManager
/*     */   {
/*     */     protected JavaFileManager clientJavaFileManager;
/*     */
/*     */
/*     */     WrappedJavaFileManager(JavaFileManager param1JavaFileManager) {
/* 211 */       param1JavaFileManager.getClass();
/* 212 */       this.clientJavaFileManager = param1JavaFileManager;
/*     */     }
/*     */
/*     */
/*     */     public ClassLoader getClassLoader(Location param1Location) {
/*     */       try {
/* 218 */         return this.clientJavaFileManager.getClassLoader(param1Location);
/* 219 */       } catch (ClientCodeException clientCodeException) {
/* 220 */         throw clientCodeException;
/* 221 */       } catch (RuntimeException runtimeException) {
/* 222 */         throw new ClientCodeException(runtimeException);
/* 223 */       } catch (Error error) {
/* 224 */         throw new ClientCodeException(error);
/*     */       }
/*     */     }
/*     */
/*     */
/*     */     public Iterable<JavaFileObject> list(Location param1Location, String param1String, Set<JavaFileObject.Kind> param1Set, boolean param1Boolean) throws IOException {
/*     */       try {
/* 231 */         return ClientCodeWrapper.this.wrapJavaFileObjects(this.clientJavaFileManager.list(param1Location, param1String, param1Set, param1Boolean));
/* 232 */       } catch (ClientCodeException clientCodeException) {
/* 233 */         throw clientCodeException;
/* 234 */       } catch (RuntimeException runtimeException) {
/* 235 */         throw new ClientCodeException(runtimeException);
/* 236 */       } catch (Error error) {
/* 237 */         throw new ClientCodeException(error);
/*     */       }
/*     */     }
/*     */
/*     */
/*     */     public String inferBinaryName(Location param1Location, JavaFileObject param1JavaFileObject) {
/*     */       try {
/* 244 */         return this.clientJavaFileManager.inferBinaryName(param1Location, ClientCodeWrapper.this.unwrap(param1JavaFileObject));
/* 245 */       } catch (ClientCodeException clientCodeException) {
/* 246 */         throw clientCodeException;
/* 247 */       } catch (RuntimeException runtimeException) {
/* 248 */         throw new ClientCodeException(runtimeException);
/* 249 */       } catch (Error error) {
/* 250 */         throw new ClientCodeException(error);
/*     */       }
/*     */     }
/*     */
/*     */
/*     */     public boolean isSameFile(FileObject param1FileObject1, FileObject param1FileObject2) {
/*     */       try {
/* 257 */         return this.clientJavaFileManager.isSameFile(ClientCodeWrapper.this.unwrap(param1FileObject1), ClientCodeWrapper.this.unwrap(param1FileObject2));
/* 258 */       } catch (ClientCodeException clientCodeException) {
/* 259 */         throw clientCodeException;
/* 260 */       } catch (RuntimeException runtimeException) {
/* 261 */         throw new ClientCodeException(runtimeException);
/* 262 */       } catch (Error error) {
/* 263 */         throw new ClientCodeException(error);
/*     */       }
/*     */     }
/*     */
/*     */
/*     */     public boolean handleOption(String param1String, Iterator<String> param1Iterator) {
/*     */       try {
/* 270 */         return this.clientJavaFileManager.handleOption(param1String, param1Iterator);
/* 271 */       } catch (ClientCodeException clientCodeException) {
/* 272 */         throw clientCodeException;
/* 273 */       } catch (RuntimeException runtimeException) {
/* 274 */         throw new ClientCodeException(runtimeException);
/* 275 */       } catch (Error error) {
/* 276 */         throw new ClientCodeException(error);
/*     */       }
/*     */     }
/*     */
/*     */
/*     */     public boolean hasLocation(Location param1Location) {
/*     */       try {
/* 283 */         return this.clientJavaFileManager.hasLocation(param1Location);
/* 284 */       } catch (ClientCodeException clientCodeException) {
/* 285 */         throw clientCodeException;
/* 286 */       } catch (RuntimeException runtimeException) {
/* 287 */         throw new ClientCodeException(runtimeException);
/* 288 */       } catch (Error error) {
/* 289 */         throw new ClientCodeException(error);
/*     */       }
/*     */     }
/*     */
/*     */
/*     */     public JavaFileObject getJavaFileForInput(Location param1Location, String param1String, JavaFileObject.Kind param1Kind) throws IOException {
/*     */       try {
/* 296 */         return ClientCodeWrapper.this.wrap(this.clientJavaFileManager.getJavaFileForInput(param1Location, param1String, param1Kind));
/* 297 */       } catch (ClientCodeException clientCodeException) {
/* 298 */         throw clientCodeException;
/* 299 */       } catch (RuntimeException runtimeException) {
/* 300 */         throw new ClientCodeException(runtimeException);
/* 301 */       } catch (Error error) {
/* 302 */         throw new ClientCodeException(error);
/*     */       }
/*     */     }
/*     */
/*     */
/*     */     public JavaFileObject getJavaFileForOutput(Location param1Location, String param1String, JavaFileObject.Kind param1Kind, FileObject param1FileObject) throws IOException {
/*     */       try {
/* 309 */         return ClientCodeWrapper.this.wrap(this.clientJavaFileManager.getJavaFileForOutput(param1Location, param1String, param1Kind, ClientCodeWrapper.this.unwrap(param1FileObject)));
/* 310 */       } catch (ClientCodeException clientCodeException) {
/* 311 */         throw clientCodeException;
/* 312 */       } catch (RuntimeException runtimeException) {
/* 313 */         throw new ClientCodeException(runtimeException);
/* 314 */       } catch (Error error) {
/* 315 */         throw new ClientCodeException(error);
/*     */       }
/*     */     }
/*     */
/*     */
/*     */     public FileObject getFileForInput(Location param1Location, String param1String1, String param1String2) throws IOException {
/*     */       try {
/* 322 */         return ClientCodeWrapper.this.wrap(this.clientJavaFileManager.getFileForInput(param1Location, param1String1, param1String2));
/* 323 */       } catch (ClientCodeException clientCodeException) {
/* 324 */         throw clientCodeException;
/* 325 */       } catch (RuntimeException runtimeException) {
/* 326 */         throw new ClientCodeException(runtimeException);
/* 327 */       } catch (Error error) {
/* 328 */         throw new ClientCodeException(error);
/*     */       }
/*     */     }
/*     */
/*     */
/*     */     public FileObject getFileForOutput(Location param1Location, String param1String1, String param1String2, FileObject param1FileObject) throws IOException {
/*     */       try {
/* 335 */         return ClientCodeWrapper.this.wrap(this.clientJavaFileManager.getFileForOutput(param1Location, param1String1, param1String2, ClientCodeWrapper.this.unwrap(param1FileObject)));
/* 336 */       } catch (ClientCodeException clientCodeException) {
/* 337 */         throw clientCodeException;
/* 338 */       } catch (RuntimeException runtimeException) {
/* 339 */         throw new ClientCodeException(runtimeException);
/* 340 */       } catch (Error error) {
/* 341 */         throw new ClientCodeException(error);
/*     */       }
/*     */     }
/*     */
/*     */
/*     */     public void flush() throws IOException {
/*     */       try {
/* 348 */         this.clientJavaFileManager.flush();
/* 349 */       } catch (ClientCodeException clientCodeException) {
/* 350 */         throw clientCodeException;
/* 351 */       } catch (RuntimeException runtimeException) {
/* 352 */         throw new ClientCodeException(runtimeException);
/* 353 */       } catch (Error error) {
/* 354 */         throw new ClientCodeException(error);
/*     */       }
/*     */     }
/*     */
/*     */
/*     */     public void close() throws IOException {
/*     */       try {
/* 361 */         this.clientJavaFileManager.close();
/* 362 */       } catch (ClientCodeException clientCodeException) {
/* 363 */         throw clientCodeException;
/* 364 */       } catch (RuntimeException runtimeException) {
/* 365 */         throw new ClientCodeException(runtimeException);
/* 366 */       } catch (Error error) {
/* 367 */         throw new ClientCodeException(error);
/*     */       }
/*     */     }
/*     */
/*     */
/*     */     public int isSupportedOption(String param1String) {
/*     */       try {
/* 374 */         return this.clientJavaFileManager.isSupportedOption(param1String);
/* 375 */       } catch (ClientCodeException clientCodeException) {
/* 376 */         throw clientCodeException;
/* 377 */       } catch (RuntimeException runtimeException) {
/* 378 */         throw new ClientCodeException(runtimeException);
/* 379 */       } catch (Error error) {
/* 380 */         throw new ClientCodeException(error);
/*     */       }
/*     */     }
/*     */
/*     */
/*     */     public String toString() {
/* 386 */       return ClientCodeWrapper.this.wrappedToString(getClass(), this.clientJavaFileManager);
/*     */     } }
/*     */
/*     */   protected class WrappedFileObject implements FileObject {
/*     */     protected FileObject clientFileObject;
/*     */
/*     */     WrappedFileObject(FileObject param1FileObject) {
/* 393 */       param1FileObject.getClass();
/* 394 */       this.clientFileObject = param1FileObject;
/*     */     }
/*     */
/*     */
/*     */     public URI toUri() {
/*     */       try {
/* 400 */         return this.clientFileObject.toUri();
/* 401 */       } catch (ClientCodeException clientCodeException) {
/* 402 */         throw clientCodeException;
/* 403 */       } catch (RuntimeException runtimeException) {
/* 404 */         throw new ClientCodeException(runtimeException);
/* 405 */       } catch (Error error) {
/* 406 */         throw new ClientCodeException(error);
/*     */       }
/*     */     }
/*     */
/*     */
/*     */     public String getName() {
/*     */       try {
/* 413 */         return this.clientFileObject.getName();
/* 414 */       } catch (ClientCodeException clientCodeException) {
/* 415 */         throw clientCodeException;
/* 416 */       } catch (RuntimeException runtimeException) {
/* 417 */         throw new ClientCodeException(runtimeException);
/* 418 */       } catch (Error error) {
/* 419 */         throw new ClientCodeException(error);
/*     */       }
/*     */     }
/*     */
/*     */
/*     */     public InputStream openInputStream() throws IOException {
/*     */       try {
/* 426 */         return this.clientFileObject.openInputStream();
/* 427 */       } catch (ClientCodeException clientCodeException) {
/* 428 */         throw clientCodeException;
/* 429 */       } catch (RuntimeException runtimeException) {
/* 430 */         throw new ClientCodeException(runtimeException);
/* 431 */       } catch (Error error) {
/* 432 */         throw new ClientCodeException(error);
/*     */       }
/*     */     }
/*     */
/*     */
/*     */     public OutputStream openOutputStream() throws IOException {
/*     */       try {
/* 439 */         return this.clientFileObject.openOutputStream();
/* 440 */       } catch (ClientCodeException clientCodeException) {
/* 441 */         throw clientCodeException;
/* 442 */       } catch (RuntimeException runtimeException) {
/* 443 */         throw new ClientCodeException(runtimeException);
/* 444 */       } catch (Error error) {
/* 445 */         throw new ClientCodeException(error);
/*     */       }
/*     */     }
/*     */
/*     */
/*     */     public Reader openReader(boolean param1Boolean) throws IOException {
/*     */       try {
/* 452 */         return this.clientFileObject.openReader(param1Boolean);
/* 453 */       } catch (ClientCodeException clientCodeException) {
/* 454 */         throw clientCodeException;
/* 455 */       } catch (RuntimeException runtimeException) {
/* 456 */         throw new ClientCodeException(runtimeException);
/* 457 */       } catch (Error error) {
/* 458 */         throw new ClientCodeException(error);
/*     */       }
/*     */     }
/*     */
/*     */
/*     */     public CharSequence getCharContent(boolean param1Boolean) throws IOException {
/*     */       try {
/* 465 */         return this.clientFileObject.getCharContent(param1Boolean);
/* 466 */       } catch (ClientCodeException clientCodeException) {
/* 467 */         throw clientCodeException;
/* 468 */       } catch (RuntimeException runtimeException) {
/* 469 */         throw new ClientCodeException(runtimeException);
/* 470 */       } catch (Error error) {
/* 471 */         throw new ClientCodeException(error);
/*     */       }
/*     */     }
/*     */
/*     */
/*     */     public Writer openWriter() throws IOException {
/*     */       try {
/* 478 */         return this.clientFileObject.openWriter();
/* 479 */       } catch (ClientCodeException clientCodeException) {
/* 480 */         throw clientCodeException;
/* 481 */       } catch (RuntimeException runtimeException) {
/* 482 */         throw new ClientCodeException(runtimeException);
/* 483 */       } catch (Error error) {
/* 484 */         throw new ClientCodeException(error);
/*     */       }
/*     */     }
/*     */
/*     */
/*     */     public long getLastModified() {
/*     */       try {
/* 491 */         return this.clientFileObject.getLastModified();
/* 492 */       } catch (ClientCodeException clientCodeException) {
/* 493 */         throw clientCodeException;
/* 494 */       } catch (RuntimeException runtimeException) {
/* 495 */         throw new ClientCodeException(runtimeException);
/* 496 */       } catch (Error error) {
/* 497 */         throw new ClientCodeException(error);
/*     */       }
/*     */     }
/*     */
/*     */
/*     */     public boolean delete() {
/*     */       try {
/* 504 */         return this.clientFileObject.delete();
/* 505 */       } catch (ClientCodeException clientCodeException) {
/* 506 */         throw clientCodeException;
/* 507 */       } catch (RuntimeException runtimeException) {
/* 508 */         throw new ClientCodeException(runtimeException);
/* 509 */       } catch (Error error) {
/* 510 */         throw new ClientCodeException(error);
/*     */       }
/*     */     }
/*     */
/*     */
/*     */     public String toString() {
/* 516 */       return ClientCodeWrapper.this.wrappedToString(getClass(), this.clientFileObject);
/*     */     }
/*     */   }
/*     */
/*     */   protected class WrappedJavaFileObject extends WrappedFileObject implements JavaFileObject {
/*     */     WrappedJavaFileObject(JavaFileObject param1JavaFileObject) {
/* 522 */       super(param1JavaFileObject);
/*     */     }
/*     */
/*     */
/*     */     public Kind getKind() {
/*     */       try {
/* 528 */         return ((JavaFileObject)this.clientFileObject).getKind();
/* 529 */       } catch (ClientCodeException clientCodeException) {
/* 530 */         throw clientCodeException;
/* 531 */       } catch (RuntimeException runtimeException) {
/* 532 */         throw new ClientCodeException(runtimeException);
/* 533 */       } catch (Error error) {
/* 534 */         throw new ClientCodeException(error);
/*     */       }
/*     */     }
/*     */
/*     */
/*     */     public boolean isNameCompatible(String param1String, Kind param1Kind) {
/*     */       try {
/* 541 */         return ((JavaFileObject)this.clientFileObject).isNameCompatible(param1String, param1Kind);
/* 542 */       } catch (ClientCodeException clientCodeException) {
/* 543 */         throw clientCodeException;
/* 544 */       } catch (RuntimeException runtimeException) {
/* 545 */         throw new ClientCodeException(runtimeException);
/* 546 */       } catch (Error error) {
/* 547 */         throw new ClientCodeException(error);
/*     */       }
/*     */     }
/*     */
/*     */
/*     */     public NestingKind getNestingKind() {
/*     */       try {
/* 554 */         return ((JavaFileObject)this.clientFileObject).getNestingKind();
/* 555 */       } catch (ClientCodeException clientCodeException) {
/* 556 */         throw clientCodeException;
/* 557 */       } catch (RuntimeException runtimeException) {
/* 558 */         throw new ClientCodeException(runtimeException);
/* 559 */       } catch (Error error) {
/* 560 */         throw new ClientCodeException(error);
/*     */       }
/*     */     }
/*     */
/*     */
/*     */     public Modifier getAccessLevel() {
/*     */       try {
/* 567 */         return ((JavaFileObject)this.clientFileObject).getAccessLevel();
/* 568 */       } catch (ClientCodeException clientCodeException) {
/* 569 */         throw clientCodeException;
/* 570 */       } catch (RuntimeException runtimeException) {
/* 571 */         throw new ClientCodeException(runtimeException);
/* 572 */       } catch (Error error) {
/* 573 */         throw new ClientCodeException(error);
/*     */       }
/*     */     }
/*     */
/*     */
/*     */     public String toString() {
/* 579 */       return ClientCodeWrapper.this.wrappedToString(getClass(), this.clientFileObject);
/*     */     } }
/*     */
/*     */   protected class WrappedDiagnosticListener<T> implements DiagnosticListener<T> {
/*     */     protected DiagnosticListener<T> clientDiagnosticListener;
/*     */
/*     */     WrappedDiagnosticListener(DiagnosticListener<T> param1DiagnosticListener) {
/* 586 */       param1DiagnosticListener.getClass();
/* 587 */       this.clientDiagnosticListener = param1DiagnosticListener;
/*     */     }
/*     */
/*     */
/*     */     public void report(Diagnostic<? extends T> param1Diagnostic) {
/*     */       try {
/* 593 */         this.clientDiagnosticListener.report(ClientCodeWrapper.this.unwrap((Diagnostic)param1Diagnostic));
/* 594 */       } catch (ClientCodeException clientCodeException) {
/* 595 */         throw clientCodeException;
/* 596 */       } catch (RuntimeException runtimeException) {
/* 597 */         throw new ClientCodeException(runtimeException);
/* 598 */       } catch (Error error) {
/* 599 */         throw new ClientCodeException(error);
/*     */       }
/*     */     }
/*     */
/*     */
/*     */     public String toString() {
/* 605 */       return ClientCodeWrapper.this.wrappedToString(getClass(), this.clientDiagnosticListener);
/*     */     }
/*     */   }
/*     */
/*     */   public class DiagnosticSourceUnwrapper implements Diagnostic<JavaFileObject> {
/*     */     public final JCDiagnostic d;
/*     */
/*     */     DiagnosticSourceUnwrapper(JCDiagnostic param1JCDiagnostic) {
/* 613 */       this.d = param1JCDiagnostic;
/*     */     }
/*     */
/*     */     public Kind getKind() {
/* 617 */       return this.d.getKind();
/*     */     }
/*     */
/*     */     public JavaFileObject getSource() {
/* 621 */       return ClientCodeWrapper.this.unwrap(this.d.getSource());
/*     */     }
/*     */
/*     */     public long getPosition() {
/* 625 */       return this.d.getPosition();
/*     */     }
/*     */
/*     */     public long getStartPosition() {
/* 629 */       return this.d.getStartPosition();
/*     */     }
/*     */
/*     */     public long getEndPosition() {
/* 633 */       return this.d.getEndPosition();
/*     */     }
/*     */
/*     */     public long getLineNumber() {
/* 637 */       return this.d.getLineNumber();
/*     */     }
/*     */
/*     */     public long getColumnNumber() {
/* 641 */       return this.d.getColumnNumber();
/*     */     }
/*     */
/*     */     public String getCode() {
/* 645 */       return this.d.getCode();
/*     */     }
/*     */
/*     */     public String getMessage(Locale param1Locale) {
/* 649 */       return this.d.getMessage(param1Locale);
/*     */     }
/*     */
/*     */
/*     */     public String toString() {
/* 654 */       return this.d.toString();
/*     */     } }
/*     */
/*     */   protected class WrappedTaskListener implements TaskListener {
/*     */     protected TaskListener clientTaskListener;
/*     */
/*     */     WrappedTaskListener(TaskListener param1TaskListener) {
/* 661 */       param1TaskListener.getClass();
/* 662 */       this.clientTaskListener = param1TaskListener;
/*     */     }
/*     */
/*     */
/*     */     public void started(TaskEvent param1TaskEvent) {
/*     */       try {
/* 668 */         this.clientTaskListener.started(param1TaskEvent);
/* 669 */       } catch (ClientCodeException clientCodeException) {
/* 670 */         throw clientCodeException;
/* 671 */       } catch (RuntimeException runtimeException) {
/* 672 */         throw new ClientCodeException(runtimeException);
/* 673 */       } catch (Error error) {
/* 674 */         throw new ClientCodeException(error);
/*     */       }
/*     */     }
/*     */
/*     */
/*     */     public void finished(TaskEvent param1TaskEvent) {
/*     */       try {
/* 681 */         this.clientTaskListener.finished(param1TaskEvent);
/* 682 */       } catch (ClientCodeException clientCodeException) {
/* 683 */         throw clientCodeException;
/* 684 */       } catch (RuntimeException runtimeException) {
/* 685 */         throw new ClientCodeException(runtimeException);
/* 686 */       } catch (Error error) {
/* 687 */         throw new ClientCodeException(error);
/*     */       }
/*     */     }
/*     */
/*     */
/*     */     public String toString() {
/* 693 */       return ClientCodeWrapper.this.wrappedToString(getClass(), this.clientTaskListener);
/*     */     }
/*     */   }
/*     */
/*     */   @Retention(RetentionPolicy.RUNTIME)
/*     */   @Target({ElementType.TYPE})
/*     */   public static @interface Trusted {}
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\javac\api\ClientCodeWrapper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
