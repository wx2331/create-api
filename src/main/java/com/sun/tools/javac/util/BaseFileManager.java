/*     */ package com.sun.tools.javac.util;
/*     */ 
/*     */ import com.sun.tools.javac.code.Lint;
/*     */ import com.sun.tools.javac.code.Source;
/*     */ import com.sun.tools.javac.file.FSInfo;
/*     */ import com.sun.tools.javac.file.Locations;
/*     */ import com.sun.tools.javac.main.Option;
/*     */ import com.sun.tools.javac.main.OptionHelper;
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStreamWriter;
/*     */ import java.lang.ref.SoftReference;
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.net.URL;
/*     */ import java.net.URLClassLoader;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.CharBuffer;
/*     */ import java.nio.charset.Charset;
/*     */ import java.nio.charset.CharsetDecoder;
/*     */ import java.nio.charset.CoderResult;
/*     */ import java.nio.charset.CodingErrorAction;
/*     */ import java.nio.charset.IllegalCharsetNameException;
/*     */ import java.nio.charset.UnsupportedCharsetException;
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class BaseFileManager
/*     */ {
/*     */   public Log log;
/*     */   protected Charset charset;
/*     */   protected Options options;
/*     */   protected String classLoaderClass;
/*     */   protected Locations locations;
/*     */   
/*     */   protected BaseFileManager(Charset paramCharset) {
/* 359 */     this.contentCache = new HashMap<>(); this.charset = paramCharset; this.byteBufferCache = new ByteBufferCache(); this.locations = createLocations();
/*     */   }
/*     */   public void setContext(Context paramContext) { this.log = Log.instance(paramContext); this.options = Options.instance(paramContext); this.classLoaderClass = this.options.get("procloader"); this.locations.update(this.log, this.options, Lint.instance(paramContext), FSInfo.instance(paramContext)); }
/*     */   protected Locations createLocations() { return new Locations(); } protected Source getSource() { String str = this.options.get(Option.SOURCE); Source source = null; if (str != null) source = Source.lookup(str);  return (source != null) ? source : Source.DEFAULT; } protected ClassLoader getClassLoader(URL[] paramArrayOfURL) { ClassLoader classLoader = getClass().getClassLoader(); if (this.classLoaderClass != null) try { Class<? extends ClassLoader> clazz = Class.forName(this.classLoaderClass).asSubclass(ClassLoader.class); Class[] arrayOfClass = { URL[].class, ClassLoader.class }; Constructor<? extends ClassLoader> constructor = clazz.getConstructor(arrayOfClass); return constructor.newInstance(new Object[] { paramArrayOfURL, classLoader }); } catch (Throwable throwable) {}  return new URLClassLoader(paramArrayOfURL, classLoader); } public boolean handleOption(String paramString, Iterator<String> paramIterator) { OptionHelper.GrumpyHelper grumpyHelper = new OptionHelper.GrumpyHelper(this.log) {
/*     */         public String get(Option param1Option) { return BaseFileManager.this.options.get(param1Option.getText()); } public void put(String param1String1, String param1String2) { BaseFileManager.this.options.put(param1String1, param1String2); } public void remove(String param1String) { BaseFileManager.this.options.remove(param1String); }
/*     */       }; for (Option option : javacFileManagerOptions) { if (option.matches(paramString)) { if (option.hasArg()) { if (paramIterator.hasNext() && !option.process((OptionHelper)grumpyHelper, paramString, paramIterator.next())) return true;  } else if (!option.process((OptionHelper)grumpyHelper, paramString)) { return true; }  throw new IllegalArgumentException(paramString); }  }  return false; } private static final Set<Option> javacFileManagerOptions = Option.getJavacFileManagerOptions(); private String defaultEncodingName; private final ByteBufferCache byteBufferCache; protected final Map<JavaFileObject, ContentCacheEntry> contentCache; public int isSupportedOption(String paramString) { for (Option option : javacFileManagerOptions) { if (option.matches(paramString)) return option.hasArg() ? 1 : 0;  }  return -1; } public abstract boolean isDefaultBootClassPath(); private String getDefaultEncodingName() { if (this.defaultEncodingName == null) this.defaultEncodingName = (new OutputStreamWriter(new ByteArrayOutputStream())).getEncoding();  return this.defaultEncodingName; } public String getEncodingName() { String str = this.options.get(Option.ENCODING); if (str == null) return getDefaultEncodingName();  return str; } public CharBuffer decode(ByteBuffer paramByteBuffer, boolean paramBoolean) { CharsetDecoder charsetDecoder; CoderResult coderResult; String str = getEncodingName(); try { charsetDecoder = getDecoder(str, paramBoolean); } catch (IllegalCharsetNameException illegalCharsetNameException) { this.log.error("unsupported.encoding", new Object[] { str }); return (CharBuffer)CharBuffer.allocate(1).flip(); } catch (UnsupportedCharsetException unsupportedCharsetException) { this.log.error("unsupported.encoding", new Object[] { str }); return (CharBuffer)CharBuffer.allocate(1).flip(); }  float f = charsetDecoder.averageCharsPerByte() * 0.8F + charsetDecoder.maxCharsPerByte() * 0.2F; CharBuffer charBuffer = CharBuffer.allocate(10 + (int)(paramByteBuffer.remaining() * f)); while (true) { coderResult = charsetDecoder.decode(paramByteBuffer, charBuffer, true); charBuffer.flip(); if (coderResult.isUnderflow()) { if (charBuffer.limit() == charBuffer.capacity()) { charBuffer = CharBuffer.allocate(charBuffer.capacity() + 1).put(charBuffer); charBuffer.flip(); }  return charBuffer; }  if (coderResult.isOverflow()) { int i = 10 + charBuffer.capacity() + (int)(paramByteBuffer.remaining() * charsetDecoder.maxCharsPerByte()); charBuffer = CharBuffer.allocate(i).put(charBuffer); continue; }  if (coderResult.isMalformed() || coderResult.isUnmappable()) { if (!getSource().allowEncodingErrors()) { this.log.error(new JCDiagnostic.SimpleDiagnosticPosition(charBuffer.limit()), "illegal.char.for.encoding", new Object[] { (this.charset == null) ? str : this.charset.name() }); } else { this.log.warning(new JCDiagnostic.SimpleDiagnosticPosition(charBuffer.limit()), "illegal.char.for.encoding", new Object[] { (this.charset == null) ? str : this.charset.name() }); }  paramByteBuffer.position(paramByteBuffer.position() + coderResult.length()); charBuffer.position(charBuffer.limit()); charBuffer.limit(charBuffer.capacity()); charBuffer.put('�'); continue; }  break; }  throw new AssertionError(coderResult); } public CharsetDecoder getDecoder(String paramString, boolean paramBoolean) { CodingErrorAction codingErrorAction; Charset charset = (this.charset == null) ? Charset.forName(paramString) : this.charset; CharsetDecoder charsetDecoder = charset.newDecoder(); if (paramBoolean) { codingErrorAction = CodingErrorAction.REPLACE; } else { codingErrorAction = CodingErrorAction.REPORT; }  return charsetDecoder.onMalformedInput(codingErrorAction).onUnmappableCharacter(codingErrorAction); } public ByteBuffer makeByteBuffer(InputStream paramInputStream) throws IOException { int i = paramInputStream.available(); if (i < 1024) i = 1024;  ByteBuffer byteBuffer = this.byteBufferCache.get(i); int j = 0; while (paramInputStream.available() != 0) { if (j >= i) byteBuffer = ByteBuffer.allocate(i <<= 1).put((ByteBuffer)byteBuffer.flip());  int k = paramInputStream.read(byteBuffer.array(), j, i - j); if (k < 0) break;  byteBuffer.position(j += k); }  return (ByteBuffer)byteBuffer.flip(); } public void recycleByteBuffer(ByteBuffer paramByteBuffer) { this.byteBufferCache.put(paramByteBuffer); } private static class ByteBufferCache {
/*     */     private ByteBuffer cached; private ByteBufferCache() {} ByteBuffer get(int param1Int) { if (param1Int < 20480) param1Int = 20480;  ByteBuffer byteBuffer = (this.cached != null && this.cached.capacity() >= param1Int) ? (ByteBuffer)this.cached.clear() : ByteBuffer.allocate(param1Int + param1Int >> 1); this.cached = null; return byteBuffer; } void put(ByteBuffer param1ByteBuffer) { this.cached = param1ByteBuffer; }
/*     */   } public CharBuffer getCachedContent(JavaFileObject paramJavaFileObject) { ContentCacheEntry contentCacheEntry = this.contentCache.get(paramJavaFileObject); if (contentCacheEntry == null) return null;  if (!contentCacheEntry.isValid(paramJavaFileObject)) { this.contentCache.remove(paramJavaFileObject); return null; }  return contentCacheEntry.getValue(); } public void cache(JavaFileObject paramJavaFileObject, CharBuffer paramCharBuffer) { this.contentCache.put(paramJavaFileObject, new ContentCacheEntry(paramJavaFileObject, paramCharBuffer)); } public void flushCache(JavaFileObject paramJavaFileObject) { this.contentCache.remove(paramJavaFileObject); } protected static class ContentCacheEntry {
/* 367 */     final long timestamp; ContentCacheEntry(JavaFileObject param1JavaFileObject, CharBuffer param1CharBuffer) { this.timestamp = param1JavaFileObject.getLastModified();
/* 368 */       this.ref = new SoftReference<>(param1CharBuffer); }
/*     */     
/*     */     final SoftReference<CharBuffer> ref;
/*     */     boolean isValid(JavaFileObject param1JavaFileObject) {
/* 372 */       return (this.timestamp == param1JavaFileObject.getLastModified());
/*     */     }
/*     */     
/*     */     CharBuffer getValue() {
/* 376 */       return this.ref.get();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public static JavaFileObject.Kind getKind(String paramString) {
/* 382 */     if (paramString.endsWith(JavaFileObject.Kind.CLASS.extension))
/* 383 */       return JavaFileObject.Kind.CLASS; 
/* 384 */     if (paramString.endsWith(JavaFileObject.Kind.SOURCE.extension))
/* 385 */       return JavaFileObject.Kind.SOURCE; 
/* 386 */     if (paramString.endsWith(JavaFileObject.Kind.HTML.extension)) {
/* 387 */       return JavaFileObject.Kind.HTML;
/*     */     }
/* 389 */     return JavaFileObject.Kind.OTHER;
/*     */   }
/*     */   
/*     */   protected static <T> T nullCheck(T paramT) {
/* 393 */     paramT.getClass();
/* 394 */     return paramT;
/*     */   }
/*     */   
/*     */   protected static <T> Collection<T> nullCheck(Collection<T> paramCollection) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: invokeinterface iterator : ()Ljava/util/Iterator;
/*     */     //   6: astore_1
/*     */     //   7: aload_1
/*     */     //   8: invokeinterface hasNext : ()Z
/*     */     //   13: ifeq -> 31
/*     */     //   16: aload_1
/*     */     //   17: invokeinterface next : ()Ljava/lang/Object;
/*     */     //   22: astore_2
/*     */     //   23: aload_2
/*     */     //   24: invokevirtual getClass : ()Ljava/lang/Class;
/*     */     //   27: pop
/*     */     //   28: goto -> 7
/*     */     //   31: aload_0
/*     */     //   32: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #398	-> 0
/*     */     //   #399	-> 23
/*     */     //   #400	-> 31
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\java\\util\BaseFileManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */