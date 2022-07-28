/*      */ package com.sun.tools.internal.xjc;
/*      */ 
/*      */ import com.sun.codemodel.internal.CodeWriter;
/*      */ import com.sun.codemodel.internal.writer.FileCodeWriter;
/*      */ import com.sun.codemodel.internal.writer.PrologCodeWriter;
/*      */ import com.sun.istack.internal.tools.DefaultAuthenticator;
/*      */ import com.sun.org.apache.xml.internal.resolver.CatalogManager;
/*      */ import com.sun.org.apache.xml.internal.resolver.tools.CatalogResolver;
/*      */ import com.sun.tools.internal.xjc.api.ClassNameAllocator;
/*      */ import com.sun.tools.internal.xjc.api.SpecVersion;
/*      */ import com.sun.tools.internal.xjc.generator.bean.field.FieldRendererFactory;
/*      */ import com.sun.tools.internal.xjc.reader.Util;
/*      */ import com.sun.tools.internal.xjc.util.Util;
/*      */ import com.sun.xml.internal.bind.Util;
/*      */ import com.sun.xml.internal.bind.api.impl.NameConverter;
/*      */ import java.io.BufferedReader;
/*      */ import java.io.File;
/*      */ import java.io.FileInputStream;
/*      */ import java.io.IOException;
/*      */ import java.io.InputStreamReader;
/*      */ import java.io.PrintWriter;
/*      */ import java.io.StringWriter;
/*      */ import java.lang.reflect.Array;
/*      */ import java.lang.reflect.InvocationTargetException;
/*      */ import java.net.MalformedURLException;
/*      */ import java.net.URI;
/*      */ import java.net.URISyntaxException;
/*      */ import java.net.URL;
/*      */ import java.net.URLClassLoader;
/*      */ import java.nio.charset.Charset;
/*      */ import java.nio.charset.IllegalCharsetNameException;
/*      */ import java.text.SimpleDateFormat;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Arrays;
/*      */ import java.util.Date;
/*      */ import java.util.Enumeration;
/*      */ import java.util.HashSet;
/*      */ import java.util.List;
/*      */ import java.util.Locale;
/*      */ import java.util.Set;
/*      */ import java.util.logging.Level;
/*      */ import java.util.logging.Logger;
/*      */ import org.xml.sax.EntityResolver;
/*      */ import org.xml.sax.InputSource;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class Options
/*      */ {
/*      */   public boolean debugMode;
/*      */   public boolean verbose;
/*      */   public boolean quiet;
/*      */   public boolean readOnly;
/*      */   public boolean noFileHeader;
/*      */   public boolean enableIntrospection;
/*      */   public boolean contentForWildcard;
/*      */   public String encoding;
/*      */   public boolean disableXmlSecurity;
/*      */   public boolean strictCheck = true;
/*      */   public boolean runtime14 = false;
/*      */   public boolean automaticNameConflictResolution = false;
/*      */   public static final int STRICT = 1;
/*      */   public static final int EXTENSION = 2;
/*  151 */   public int compatibilityMode = 1;
/*      */   
/*      */   public boolean isExtensionMode() {
/*  154 */     return (this.compatibilityMode == 2);
/*      */   }
/*      */   
/*  157 */   private static final Logger logger = Util.getClassLogger();
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  162 */   public SpecVersion target = SpecVersion.LATEST;
/*      */   public File targetDir;
/*      */   public EntityResolver entityResolver;
/*      */   private Language schemaLanguage;
/*      */   public String defaultPackage;
/*      */   public String defaultPackage2; private final List<InputSource> grammars; private final List<InputSource> bindFiles; private String proxyHost; private String proxyPort; public String proxyAuth; public final List<Plugin> activePlugins; private List<Plugin> allPlugins; public final Set<String> pluginURIs; public ClassNameAllocator classNameAllocator; public boolean packageLevelAnnotations; private FieldRendererFactory fieldRendererFactory; private Plugin fieldRendererFactoryOwner; private NameConverter nameConverter; private Plugin nameConverterOwner; public final List<URL> classpaths; private static String pluginLoadFailure; public FieldRendererFactory getFieldRendererFactory() { return this.fieldRendererFactory; } public void setFieldRendererFactory(FieldRendererFactory frf, Plugin owner) throws BadCommandLineException { if (frf == null)
/*      */       throw new IllegalArgumentException();  if (this.fieldRendererFactoryOwner != null)
/*      */       throw new BadCommandLineException(Messages.format("FIELD_RENDERER_CONFLICT", new Object[] { this.fieldRendererFactoryOwner.getOptionName(), owner.getOptionName() }));  this.fieldRendererFactoryOwner = owner;
/*      */     this.fieldRendererFactory = frf; } public NameConverter getNameConverter() { return this.nameConverter; } public void setNameConverter(NameConverter nc, Plugin owner) throws BadCommandLineException { if (nc == null)
/*      */       throw new IllegalArgumentException(); 
/*      */     if (this.nameConverter != null)
/*      */       throw new BadCommandLineException(Messages.format("NAME_CONVERTER_CONFLICT", new Object[] { this.nameConverterOwner.getOptionName(), owner.getOptionName() })); 
/*      */     this.nameConverterOwner = owner;
/*      */     this.nameConverter = nc; } public List<Plugin> getAllPlugins() { if (this.allPlugins == null) {
/*      */       this.allPlugins = new ArrayList<>();
/*      */       ClassLoader ucl = getUserClassLoader(SecureLoader.getClassClassLoader(getClass()));
/*      */       this.allPlugins.addAll(Arrays.asList(findServices(Plugin.class, ucl)));
/*      */     } 
/*  180 */     return this.allPlugins; } public Options() { this.targetDir = new File(".");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  187 */     this.entityResolver = null;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  193 */     this.schemaLanguage = null;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  199 */     this.defaultPackage = null;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  205 */     this.defaultPackage2 = null;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  210 */     this.grammars = new ArrayList<>();
/*      */     
/*  212 */     this.bindFiles = new ArrayList<>();
/*      */ 
/*      */     
/*  215 */     this.proxyHost = null;
/*  216 */     this.proxyPort = null;
/*  217 */     this.proxyAuth = null;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  222 */     this.activePlugins = new ArrayList<>();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  235 */     this.pluginURIs = new HashSet<>();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  245 */     this.packageLevelAnnotations = true;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  250 */     this.fieldRendererFactory = new FieldRendererFactory();
/*      */ 
/*      */ 
/*      */     
/*  254 */     this.fieldRendererFactoryOwner = null;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  260 */     this.nameConverter = null;
/*      */ 
/*      */ 
/*      */     
/*  264 */     this.nameConverterOwner = null;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  457 */     this.classpaths = new ArrayList<>(); try { Class.forName("javax.xml.bind.JAXBPermission"); } catch (ClassNotFoundException cnfe) { this.target = SpecVersion.V2_1; }
/*      */      }
/*      */   public Language getSchemaLanguage() { if (this.schemaLanguage == null)
/*      */       this.schemaLanguage = guessSchemaLanguage();  return this.schemaLanguage; }
/*      */   public void setSchemaLanguage(Language _schemaLanguage) { this.schemaLanguage = _schemaLanguage; }
/*      */   public InputSource[] getGrammars() { return this.grammars.<InputSource>toArray(new InputSource[this.grammars.size()]); }
/*  463 */   public void addGrammar(InputSource is) { this.grammars.add(absolutize(is)); } public ClassLoader getUserClassLoader(ClassLoader parent) { if (this.classpaths.isEmpty())
/*  464 */       return parent; 
/*  465 */     return new URLClassLoader(this.classpaths
/*  466 */         .<URL>toArray(new URL[this.classpaths.size()]), parent); } private InputSource fileToInputSource(File source) { try { String url = source.toURL().toExternalForm(); return new InputSource(Util.escapeSpace(url)); } catch (MalformedURLException e) { return new InputSource(source.getPath()); }  }
/*      */   public void addGrammar(File source) { addGrammar(fileToInputSource(source)); }
/*      */   public void addGrammarRecursive(File dir) { addRecursive(dir, ".xsd", this.grammars); }
/*      */   private void addRecursive(File dir, String suffix, List<InputSource> result) { File[] files = dir.listFiles(); if (files == null)
/*      */       return;  for (File f : files) { if (f.isDirectory()) { addRecursive(f, suffix, result); }
/*      */       else if (f.getPath().endsWith(suffix)) { result.add(absolutize(fileToInputSource(f))); }
/*      */        }
/*      */      }
/*      */   private InputSource absolutize(InputSource is) { try { URL baseURL = (new File(".")).getCanonicalFile().toURL(); is.setSystemId((new URL(baseURL, is.getSystemId())).toExternalForm()); }
/*      */     catch (IOException e) { logger.log(Level.FINE, "{0}, {1}", new Object[] { is.getSystemId(), e.getLocalizedMessage() }); }
/*      */      return is; }
/*      */   public InputSource[] getBindFiles() { return this.bindFiles.<InputSource>toArray(new InputSource[this.bindFiles.size()]); }
/*      */   public void addBindFile(InputSource is) { this.bindFiles.add(absolutize(is)); }
/*      */   public void addBindFile(File bindFile) { this.bindFiles.add(fileToInputSource(bindFile)); }
/*      */   public void addBindFileRecursive(File dir) { addRecursive(dir, ".xjb", this.bindFiles); }
/*  481 */   public int parseArgument(String[] args, int i) throws BadCommandLineException { if (args[i].equals("-classpath") || args[i].equals("-cp")) {
/*  482 */       String a = requireArgument(args[i], args, ++i);
/*  483 */       for (String p : a.split(File.pathSeparator)) {
/*  484 */         File file = new File(p);
/*      */         try {
/*  486 */           this.classpaths.add(file.toURL());
/*  487 */         } catch (MalformedURLException e) {
/*  488 */           throw new BadCommandLineException(
/*  489 */               Messages.format("Driver.NotAValidFileName", new Object[] { file }), e);
/*      */         } 
/*      */       } 
/*  492 */       return 2;
/*      */     } 
/*  494 */     if (args[i].equals("-d")) {
/*  495 */       this.targetDir = new File(requireArgument("-d", args, ++i));
/*  496 */       if (!this.targetDir.exists())
/*  497 */         throw new BadCommandLineException(
/*  498 */             Messages.format("Driver.NonExistentDir", new Object[] { this.targetDir })); 
/*  499 */       return 2;
/*      */     } 
/*  501 */     if (args[i].equals("-readOnly")) {
/*  502 */       this.readOnly = true;
/*  503 */       return 1;
/*      */     } 
/*  505 */     if (args[i].equals("-p")) {
/*  506 */       this.defaultPackage = requireArgument("-p", args, ++i);
/*  507 */       if (this.defaultPackage.length() == 0)
/*      */       {
/*      */         
/*  510 */         this.packageLevelAnnotations = false;
/*      */       }
/*  512 */       return 2;
/*      */     } 
/*  514 */     if (args[i].equals("-debug")) {
/*  515 */       this.debugMode = true;
/*  516 */       this.verbose = true;
/*  517 */       return 1;
/*      */     } 
/*  519 */     if (args[i].equals("-nv")) {
/*  520 */       this.strictCheck = false;
/*  521 */       return 1;
/*      */     } 
/*  523 */     if (args[i].equals("-npa")) {
/*  524 */       this.packageLevelAnnotations = false;
/*  525 */       return 1;
/*      */     } 
/*  527 */     if (args[i].equals("-no-header")) {
/*  528 */       this.noFileHeader = true;
/*  529 */       return 1;
/*      */     } 
/*  531 */     if (args[i].equals("-verbose")) {
/*  532 */       this.verbose = true;
/*  533 */       return 1;
/*      */     } 
/*  535 */     if (args[i].equals("-quiet")) {
/*  536 */       this.quiet = true;
/*  537 */       return 1;
/*      */     } 
/*  539 */     if (args[i].equals("-XexplicitAnnotation")) {
/*  540 */       this.runtime14 = true;
/*  541 */       return 1;
/*      */     } 
/*  543 */     if (args[i].equals("-enableIntrospection")) {
/*  544 */       this.enableIntrospection = true;
/*  545 */       return 1;
/*      */     } 
/*  547 */     if (args[i].equals("-disableXmlSecurity")) {
/*  548 */       this.disableXmlSecurity = true;
/*  549 */       return 1;
/*      */     } 
/*  551 */     if (args[i].equals("-contentForWildcard")) {
/*  552 */       this.contentForWildcard = true;
/*  553 */       return 1;
/*      */     } 
/*  555 */     if (args[i].equals("-XautoNameResolution")) {
/*  556 */       this.automaticNameConflictResolution = true;
/*  557 */       return 1;
/*      */     } 
/*  559 */     if (args[i].equals("-b")) {
/*  560 */       addFile(requireArgument("-b", args, ++i), this.bindFiles, ".xjb");
/*  561 */       return 2;
/*      */     } 
/*  563 */     if (args[i].equals("-dtd")) {
/*  564 */       this.schemaLanguage = Language.DTD;
/*  565 */       return 1;
/*      */     } 
/*  567 */     if (args[i].equals("-relaxng")) {
/*  568 */       this.schemaLanguage = Language.RELAXNG;
/*  569 */       return 1;
/*      */     } 
/*  571 */     if (args[i].equals("-relaxng-compact")) {
/*  572 */       this.schemaLanguage = Language.RELAXNG_COMPACT;
/*  573 */       return 1;
/*      */     } 
/*  575 */     if (args[i].equals("-xmlschema")) {
/*  576 */       this.schemaLanguage = Language.XMLSCHEMA;
/*  577 */       return 1;
/*      */     } 
/*  579 */     if (args[i].equals("-wsdl")) {
/*  580 */       this.schemaLanguage = Language.WSDL;
/*  581 */       return 1;
/*      */     } 
/*  583 */     if (args[i].equals("-extension")) {
/*  584 */       this.compatibilityMode = 2;
/*  585 */       return 1;
/*      */     } 
/*  587 */     if (args[i].equals("-target")) {
/*  588 */       String token = requireArgument("-target", args, ++i);
/*  589 */       this.target = SpecVersion.parse(token);
/*  590 */       if (this.target == null)
/*  591 */         throw new BadCommandLineException(Messages.format("Driver.ILLEGAL_TARGET_VERSION", new Object[] { token })); 
/*  592 */       return 2;
/*      */     } 
/*  594 */     if (args[i].equals("-httpproxyfile")) {
/*  595 */       if (i == args.length - 1 || args[i + 1].startsWith("-")) {
/*  596 */         throw new BadCommandLineException(
/*  597 */             Messages.format("Driver.MISSING_PROXYFILE", new Object[0]));
/*      */       }
/*      */       
/*  600 */       File file = new File(args[++i]);
/*  601 */       if (!file.exists()) {
/*  602 */         throw new BadCommandLineException(
/*  603 */             Messages.format("Driver.NO_SUCH_FILE", new Object[] { file }));
/*      */       }
/*      */       
/*      */       try {
/*  607 */         BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
/*  608 */         parseProxy(in.readLine());
/*  609 */         in.close();
/*  610 */       } catch (IOException e) {
/*  611 */         throw new BadCommandLineException(
/*  612 */             Messages.format("Driver.FailedToParse", new Object[] { file, e.getMessage() }), e);
/*      */       } 
/*      */       
/*  615 */       return 2;
/*      */     } 
/*  617 */     if (args[i].equals("-httpproxy")) {
/*  618 */       if (i == args.length - 1 || args[i + 1].startsWith("-")) {
/*  619 */         throw new BadCommandLineException(
/*  620 */             Messages.format("Driver.MISSING_PROXY", new Object[0]));
/*      */       }
/*      */       
/*  623 */       parseProxy(args[++i]);
/*  624 */       return 2;
/*      */     } 
/*  626 */     if (args[i].equals("-host")) {
/*  627 */       this.proxyHost = requireArgument("-host", args, ++i);
/*  628 */       return 2;
/*      */     } 
/*  630 */     if (args[i].equals("-port")) {
/*  631 */       this.proxyPort = requireArgument("-port", args, ++i);
/*  632 */       return 2;
/*      */     } 
/*  634 */     if (args[i].equals("-catalog")) {
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  639 */       File catalogFile = new File(requireArgument("-catalog", args, ++i));
/*      */       try {
/*  641 */         addCatalog(catalogFile);
/*  642 */       } catch (IOException e) {
/*  643 */         throw new BadCommandLineException(
/*  644 */             Messages.format("Driver.FailedToParse", new Object[] { catalogFile, e.getMessage() }), e);
/*      */       } 
/*  646 */       return 2;
/*      */     } 
/*  648 */     if (args[i].equals("-Xtest-class-name-allocator")) {
/*  649 */       this.classNameAllocator = new ClassNameAllocator() {
/*      */           public String assignClassName(String packageName, String className) {
/*  651 */             System.out.printf("assignClassName(%s,%s)\n", new Object[] { packageName, className });
/*  652 */             return className + "_Type";
/*      */           }
/*      */         };
/*  655 */       return 1;
/*      */     } 
/*      */     
/*  658 */     if (args[i].equals("-encoding")) {
/*  659 */       this.encoding = requireArgument("-encoding", args, ++i);
/*      */       try {
/*  661 */         if (!Charset.isSupported(this.encoding)) {
/*  662 */           throw new BadCommandLineException(
/*  663 */               Messages.format("Driver.UnsupportedEncoding", new Object[] { this.encoding }));
/*      */         }
/*  665 */       } catch (IllegalCharsetNameException icne) {
/*  666 */         throw new BadCommandLineException(
/*  667 */             Messages.format("Driver.UnsupportedEncoding", new Object[] { this.encoding }));
/*      */       } 
/*  669 */       return 2;
/*      */     } 
/*      */ 
/*      */     
/*  673 */     for (Plugin plugin : getAllPlugins()) {
/*      */       try {
/*  675 */         if (('-' + plugin.getOptionName()).equals(args[i])) {
/*  676 */           this.activePlugins.add(plugin);
/*  677 */           plugin.onActivated(this);
/*  678 */           this.pluginURIs.addAll(plugin.getCustomizationURIs());
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */           
/*  684 */           int j = plugin.parseArgument(this, args, i);
/*  685 */           if (j != 0) {
/*  686 */             return j;
/*      */           }
/*  688 */           return 1;
/*      */         } 
/*      */         
/*  691 */         int r = plugin.parseArgument(this, args, i);
/*  692 */         if (r != 0) return r; 
/*  693 */       } catch (IOException e) {
/*  694 */         throw new BadCommandLineException(e.getMessage(), e);
/*      */       } 
/*      */     } 
/*      */     
/*  698 */     return 0; }
/*      */ 
/*      */   
/*      */   private void parseProxy(String text) throws BadCommandLineException {
/*  702 */     int i = text.lastIndexOf('@');
/*  703 */     int j = text.lastIndexOf(':');
/*      */     
/*  705 */     if (i > 0) {
/*  706 */       this.proxyAuth = text.substring(0, i);
/*  707 */       if (j > i) {
/*  708 */         this.proxyHost = text.substring(i + 1, j);
/*  709 */         this.proxyPort = text.substring(j + 1);
/*      */       } else {
/*  711 */         this.proxyHost = text.substring(i + 1);
/*  712 */         this.proxyPort = "80";
/*      */       }
/*      */     
/*      */     }
/*  716 */     else if (j < 0) {
/*      */       
/*  718 */       this.proxyHost = text;
/*  719 */       this.proxyPort = "80";
/*      */     } else {
/*  721 */       this.proxyHost = text.substring(0, j);
/*  722 */       this.proxyPort = text.substring(j + 1);
/*      */     } 
/*      */     
/*      */     try {
/*  726 */       Integer.valueOf(this.proxyPort);
/*  727 */     } catch (NumberFormatException e) {
/*  728 */       throw new BadCommandLineException(Messages.format("Driver.ILLEGAL_PROXY", new Object[] { text }));
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String requireArgument(String optionName, String[] args, int i) throws BadCommandLineException {
/*  736 */     if (i == args.length || args[i].startsWith("-")) {
/*  737 */       throw new BadCommandLineException(
/*  738 */           Messages.format("Driver.MissingOperand", new Object[] { optionName }));
/*      */     }
/*  740 */     return args[i];
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void addFile(String name, List<InputSource> target, String suffix) throws BadCommandLineException {
/*      */     Object src;
/*      */     try {
/*  754 */       src = Util.getFileOrURL(name);
/*  755 */     } catch (IOException e) {
/*  756 */       throw new BadCommandLineException(
/*  757 */           Messages.format("Driver.NotAFileNorURL", new Object[] { name }));
/*      */     } 
/*  759 */     if (src instanceof URL) {
/*  760 */       target.add(absolutize(new InputSource(Util.escapeSpace(((URL)src).toExternalForm()))));
/*      */     } else {
/*  762 */       File fsrc = (File)src;
/*  763 */       if (fsrc.isDirectory()) {
/*  764 */         addRecursive(fsrc, suffix, target);
/*      */       } else {
/*  766 */         target.add(absolutize(fileToInputSource(fsrc)));
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void addCatalog(File catalogFile) throws IOException {
/*  775 */     if (this.entityResolver == null) {
/*  776 */       CatalogManager.getStaticManager().setIgnoreMissingProperties(true);
/*  777 */       this.entityResolver = new CatalogResolver(true);
/*      */     } 
/*  779 */     ((CatalogResolver)this.entityResolver).getCatalog().parseCatalog(catalogFile.getPath());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void parseArguments(String[] args) throws BadCommandLineException {
/*  790 */     for (int i = 0; i < args.length; i++) {
/*  791 */       if (args[i].length() == 0)
/*  792 */         throw new BadCommandLineException(); 
/*  793 */       if (args[i].charAt(0) == '-') {
/*  794 */         int j = parseArgument(args, i);
/*  795 */         if (j == 0)
/*  796 */           throw new BadCommandLineException(
/*  797 */               Messages.format("Driver.UnrecognizedParameter", new Object[] { args[i] })); 
/*  798 */         i += j - 1;
/*      */       }
/*  800 */       else if (args[i].endsWith(".jar")) {
/*  801 */         scanEpisodeFile(new File(args[i]));
/*      */       } else {
/*  803 */         addFile(args[i], this.grammars, ".xsd");
/*      */       } 
/*      */     } 
/*      */ 
/*      */     
/*  808 */     if (this.proxyHost != null || this.proxyPort != null) {
/*  809 */       if (this.proxyHost != null && this.proxyPort != null)
/*  810 */       { System.setProperty("http.proxyHost", this.proxyHost);
/*  811 */         System.setProperty("http.proxyPort", this.proxyPort);
/*  812 */         System.setProperty("https.proxyHost", this.proxyHost);
/*  813 */         System.setProperty("https.proxyPort", this.proxyPort); }
/*  814 */       else { if (this.proxyHost == null) {
/*  815 */           throw new BadCommandLineException(
/*  816 */               Messages.format("Driver.MissingProxyHost", new Object[0]));
/*      */         }
/*  818 */         throw new BadCommandLineException(
/*  819 */             Messages.format("Driver.MissingProxyPort", new Object[0])); }
/*      */       
/*  821 */       if (this.proxyAuth != null) {
/*  822 */         DefaultAuthenticator.getAuthenticator().setProxyAuth(this.proxyAuth);
/*      */       }
/*      */     } 
/*      */     
/*  826 */     if (this.grammars.isEmpty()) {
/*  827 */       throw new BadCommandLineException(
/*  828 */           Messages.format("Driver.MissingGrammar", new Object[0]));
/*      */     }
/*  830 */     if (this.schemaLanguage == null) {
/*  831 */       this.schemaLanguage = guessSchemaLanguage();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  838 */     if (pluginLoadFailure != null) {
/*  839 */       throw new BadCommandLineException(
/*  840 */           Messages.format("PLUGIN_LOAD_FAILURE", new Object[] { pluginLoadFailure }));
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void scanEpisodeFile(File jar) throws BadCommandLineException {
/*      */     try {
/*  848 */       URLClassLoader ucl = new URLClassLoader(new URL[] { jar.toURL() });
/*  849 */       Enumeration<URL> resources = ucl.findResources("META-INF/sun-jaxb.episode");
/*  850 */       while (resources.hasMoreElements()) {
/*  851 */         URL url = resources.nextElement();
/*  852 */         addBindFile(new InputSource(url.toExternalForm()));
/*      */       } 
/*  854 */     } catch (IOException e) {
/*  855 */       throw new BadCommandLineException(
/*  856 */           Messages.format("FAILED_TO_LOAD", new Object[] { jar, e.getMessage() }), e);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Language guessSchemaLanguage() {
/*  868 */     if (this.grammars != null && this.grammars.size() > 0) {
/*  869 */       String name = ((InputSource)this.grammars.get(0)).getSystemId().toLowerCase();
/*      */       
/*  871 */       if (name.endsWith(".rng"))
/*  872 */         return Language.RELAXNG; 
/*  873 */       if (name.endsWith(".rnc"))
/*  874 */         return Language.RELAXNG_COMPACT; 
/*  875 */       if (name.endsWith(".dtd"))
/*  876 */         return Language.DTD; 
/*  877 */       if (name.endsWith(".wsdl")) {
/*  878 */         return Language.WSDL;
/*      */       }
/*      */     } 
/*      */     
/*  882 */     return Language.XMLSCHEMA;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public CodeWriter createCodeWriter() throws IOException {
/*  889 */     return createCodeWriter((CodeWriter)new FileCodeWriter(this.targetDir, this.readOnly, this.encoding));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public CodeWriter createCodeWriter(CodeWriter core) {
/*  896 */     if (this.noFileHeader) {
/*  897 */       return core;
/*      */     }
/*  899 */     return (CodeWriter)new PrologCodeWriter(core, getPrologComment());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getPrologComment() {
/*  913 */     String format = Messages.format("Driver.DateFormat", new Object[0]) + " '" + Messages.format("Driver.At", new Object[0]) + "' " + Messages.format("Driver.TimeFormat", new Object[0]);
/*  914 */     SimpleDateFormat dateFormat = new SimpleDateFormat(format, Locale.ENGLISH);
/*      */     
/*  916 */     return Messages.format("Driver.FilePrologComment", new Object[] { dateFormat
/*      */           
/*  918 */           .format(new Date()) });
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static <T> T[] findServices(Class<T> clazz, ClassLoader classLoader) {
/*  932 */     boolean debug = (Util.getSystemProperty(Options.class, "findServices") != null);
/*      */ 
/*      */ 
/*      */     
/*      */     try {
/*  937 */       Class<?> serviceLoader = Class.forName("java.util.ServiceLoader");
/*  938 */       if (debug)
/*  939 */         System.out.println("Using java.util.ServiceLoader"); 
/*  940 */       Iterable<T> itr = (Iterable<T>)serviceLoader.getMethod("load", new Class[] { Class.class, ClassLoader.class }).invoke(null, new Object[] { clazz, classLoader });
/*  941 */       List<T> r = new ArrayList<>();
/*  942 */       for (T t : itr)
/*  943 */         r.add(t); 
/*  944 */       return r.toArray((T[])Array.newInstance(clazz, r.size()));
/*  945 */     } catch (ClassNotFoundException classNotFoundException) {
/*      */     
/*  947 */     } catch (IllegalAccessException e) {
/*  948 */       Error x = new IllegalAccessError();
/*  949 */       x.initCause(e);
/*  950 */       throw x;
/*  951 */     } catch (InvocationTargetException e) {
/*  952 */       Throwable x = e.getTargetException();
/*  953 */       if (x instanceof RuntimeException)
/*  954 */         throw (RuntimeException)x; 
/*  955 */       if (x instanceof Error)
/*  956 */         throw (Error)x; 
/*  957 */       throw new Error(x);
/*  958 */     } catch (NoSuchMethodException e) {
/*  959 */       Error x = new NoSuchMethodError();
/*  960 */       x.initCause(e);
/*  961 */       throw x;
/*      */     } 
/*      */     
/*  964 */     String serviceId = "META-INF/services/" + clazz.getName();
/*      */ 
/*      */     
/*  967 */     Set<String> classNames = new HashSet<>();
/*      */     
/*  969 */     if (debug) {
/*  970 */       System.out.println("Looking for " + serviceId + " for add-ons");
/*      */     }
/*      */ 
/*      */     
/*      */     try {
/*  975 */       Enumeration<URL> e = classLoader.getResources(serviceId);
/*  976 */       if (e == null) return (T[])Array.newInstance(clazz, 0);
/*      */       
/*  978 */       ArrayList<T> a = new ArrayList<>();
/*  979 */       while (e.hasMoreElements()) {
/*  980 */         URL url = e.nextElement();
/*  981 */         BufferedReader reader = null;
/*      */         
/*  983 */         if (debug) {
/*  984 */           System.out.println("Checking " + url + " for an add-on");
/*      */         }
/*      */         
/*      */         try {
/*  988 */           reader = new BufferedReader(new InputStreamReader(url.openStream()));
/*      */           String impl;
/*  990 */           while ((impl = reader.readLine()) != null) {
/*      */             
/*  992 */             impl = impl.trim();
/*  993 */             if (classNames.add(impl)) {
/*  994 */               Class<?> implClass = classLoader.loadClass(impl);
/*  995 */               if (!clazz.isAssignableFrom(implClass)) {
/*  996 */                 pluginLoadFailure = impl + " is not a subclass of " + clazz + ". Skipping";
/*  997 */                 if (debug)
/*  998 */                   System.out.println(pluginLoadFailure); 
/*      */                 continue;
/*      */               } 
/* 1001 */               if (debug) {
/* 1002 */                 System.out.println("Attempting to instanciate " + impl);
/*      */               }
/* 1004 */               a.add(clazz.cast(implClass.newInstance()));
/*      */             } 
/*      */           } 
/* 1007 */           reader.close();
/* 1008 */         } catch (Exception ex) {
/*      */           
/* 1010 */           StringWriter w = new StringWriter();
/* 1011 */           ex.printStackTrace(new PrintWriter(w));
/* 1012 */           pluginLoadFailure = w.toString();
/* 1013 */           if (debug) {
/* 1014 */             System.out.println(pluginLoadFailure);
/*      */           }
/* 1016 */           if (reader != null) {
/*      */             try {
/* 1018 */               reader.close();
/* 1019 */             } catch (IOException iOException) {}
/*      */           }
/*      */         } 
/*      */       } 
/*      */ 
/*      */ 
/*      */       
/* 1026 */       return a.toArray((T[])Array.newInstance(clazz, a.size()));
/* 1027 */     } catch (Throwable e) {
/*      */       
/* 1029 */       StringWriter w = new StringWriter();
/* 1030 */       e.printStackTrace(new PrintWriter(w));
/* 1031 */       pluginLoadFailure = w.toString();
/* 1032 */       if (debug) {
/* 1033 */         System.out.println(pluginLoadFailure);
/*      */       }
/* 1035 */       return (T[])Array.newInstance(clazz, 0);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public static String getBuildID() {
/* 1041 */     return Messages.format("Driver.BuildID", new Object[0]);
/*      */   }
/*      */   
/*      */   public static String normalizeSystemId(String systemId) {
/*      */     try {
/* 1046 */       systemId = (new URI(systemId)).normalize().toString();
/* 1047 */     } catch (URISyntaxException uRISyntaxException) {}
/*      */ 
/*      */     
/* 1050 */     return systemId;
/*      */   }
/*      */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\xjc\Options.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */