/*     */ package com.sun.tools.internal.ws.wscompile;
/*     */
/*     */ import com.sun.codemodel.internal.JCodeModel;
/*     */ import com.sun.tools.internal.ws.processor.generator.GeneratorExtension;
/*     */ import com.sun.tools.internal.ws.resources.ConfigurationMessages;
/*     */ import com.sun.tools.internal.ws.resources.WscompileMessages;
/*     */ import com.sun.tools.internal.ws.util.ForkEntityResolver;
/*     */ import com.sun.tools.internal.ws.wsdl.document.jaxws.JAXWSBindingsConstants;
/*     */ import com.sun.tools.internal.xjc.BadCommandLineException;
/*     */ import com.sun.tools.internal.xjc.Options;
/*     */ import com.sun.tools.internal.xjc.api.SchemaCompiler;
/*     */ import com.sun.tools.internal.xjc.api.SpecVersion;
/*     */ import com.sun.tools.internal.xjc.api.XJC;
/*     */ import com.sun.tools.internal.xjc.reader.Util;
/*     */ import com.sun.xml.internal.ws.api.streaming.XMLStreamReaderFactory;
/*     */ import com.sun.xml.internal.ws.streaming.XMLStreamReaderUtil;
/*     */ import com.sun.xml.internal.ws.util.JAXWSUtils;
/*     */ import com.sun.xml.internal.ws.util.ServiceFinder;
/*     */ import com.sun.xml.internal.ws.util.xml.XmlUtil;
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.Reader;
/*     */ import java.lang.reflect.Array;
/*     */ import java.net.MalformedURLException;
/*     */ import java.net.URL;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ import javax.xml.namespace.QName;
/*     */ import javax.xml.stream.XMLStreamReader;
/*     */ import org.w3c.dom.Element;
/*     */ import org.xml.sax.EntityResolver;
/*     */ import org.xml.sax.InputSource;
/*     */ import org.xml.sax.helpers.LocatorImpl;
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */ public class WsimportOptions
/*     */   extends Options
/*     */ {
/*     */   public String wsdlLocation;
/*  82 */   public EntityResolver entityResolver = null;
/*     */
/*     */
/*     */
/*     */
/*     */
/*  88 */   public String defaultPackage = null;
/*     */
/*     */
/*     */
/*     */
/*  93 */   public String clientjar = null;
/*     */
/*     */
/*     */
/*     */
/*     */   public boolean additionalHeaders;
/*     */
/*     */
/*     */
/*     */
/* 103 */   public File implDestDir = null;
/*     */
/*     */
/*     */
/*     */
/*     */
/* 109 */   public String implServiceName = null;
/*     */
/*     */
/*     */
/*     */
/*     */
/* 115 */   public String implPortName = null;
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   public boolean isGenerateJWS = false;
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   public boolean disableSSLHostnameVerification;
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   public boolean useBaseResourceAndURLToLoadWSDL = false;
/*     */
/*     */
/*     */
/*     */
/*     */
/* 139 */   private SchemaCompiler schemaCompiler = XJC.createSchemaCompiler();
/*     */
/*     */
/*     */
/*     */
/* 144 */   public File authFile = null;
/*     */
/*     */
/*     */
/* 148 */   public static final String defaultAuthfile = System.getProperty("user.home") + System.getProperty("file.separator") + ".metro" +
/* 149 */     System.getProperty("file.separator") + "auth";
/*     */
/*     */
/*     */
/*     */   public boolean disableAuthenticator;
/*     */
/*     */
/*     */
/* 157 */   public String proxyAuth = null;
/* 158 */   private String proxyHost = null;
/* 159 */   private String proxyPort = null;
/*     */
/*     */
/*     */
/*     */
/* 164 */   public HashMap<String, String> extensionOptions = new HashMap<>();
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   private List<Plugin> allPlugins;
/*     */
/*     */
/*     */
/*     */
/*     */
/* 177 */   public final List<Plugin> activePlugins = new ArrayList<>(); private JCodeModel codeModel;
/*     */
/*     */   public JCodeModel getCodeModel() {
/* 180 */     if (this.codeModel == null)
/* 181 */       this.codeModel = new JCodeModel();
/* 182 */     return this.codeModel;
/*     */   }
/*     */
/*     */   public SchemaCompiler getSchemaCompiler() {
/* 186 */     this.schemaCompiler.setTargetVersion(SpecVersion.parse(this.target.getVersion()));
/* 187 */     if (this.entityResolver != null)
/*     */     {
/* 189 */       this.schemaCompiler.setEntityResolver(this.entityResolver);
/*     */     }
/* 191 */     return this.schemaCompiler;
/*     */   }
/*     */
/*     */   public void setCodeModel(JCodeModel codeModel) {
/* 195 */     this.codeModel = codeModel;
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/* 203 */   public List<String> cmdlineJars = new ArrayList<>();
/*     */
/*     */
/*     */
/*     */
/*     */   public boolean noAddressingBbinding;
/*     */
/*     */
/*     */
/*     */
/*     */   public List<Plugin> getAllPlugins() {
/* 214 */     if (this.allPlugins == null) {
/* 215 */       this.allPlugins = new ArrayList<>();
/* 216 */       this.allPlugins.addAll(Arrays.asList(findServices(Plugin.class, getClassLoader())));
/*     */     }
/* 218 */     return this.allPlugins;
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
/*     */   public final void parseArguments(String[] args) throws BadCommandLineException {
/* 230 */     for (int i = 0; i < args.length; i++) {
/* 231 */       if (args[i].length() == 0)
/* 232 */         throw new BadCommandLineException();
/* 233 */       if (args[i].charAt(0) == '-') {
/* 234 */         int j = parseArguments(args, i);
/* 235 */         if (j == 0)
/* 236 */           throw new BadCommandLineException(WscompileMessages.WSCOMPILE_INVALID_OPTION(args[i]));
/* 237 */         i += j - 1;
/*     */       }
/* 239 */       else if (args[i].endsWith(".jar")) {
/*     */
/*     */         try {
/* 242 */           this.cmdlineJars.add(args[i]);
/* 243 */           this.schemaCompiler.getOptions().scanEpisodeFile(new File(args[i]));
/*     */         }
/* 245 */         catch (BadCommandLineException e) {
/*     */
/* 247 */           throw new BadCommandLineException(e.getMessage(), e);
/*     */         }
/*     */       } else {
/* 250 */         addFile(args[i]);
/*     */       }
/*     */     }
/*     */
/*     */
/* 255 */     if (this.encoding != null && (this.schemaCompiler.getOptions()).encoding == null) {
/*     */       try {
/* 257 */         this.schemaCompiler.getOptions().parseArgument(new String[] { "-encoding", this.encoding }, 0);
/*     */       }
/* 259 */       catch (BadCommandLineException ex) {
/* 260 */         Logger.getLogger(WsimportOptions.class.getName()).log(Level.SEVERE, (String)null, (Throwable)ex);
/*     */       }
/*     */     }
/*     */
/* 264 */     if (this.destDir == null)
/* 265 */       this.destDir = new File(".");
/* 266 */     if (this.sourceDir == null) {
/* 267 */       this.sourceDir = this.destDir;
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
/*     */   public int parseArguments(String[] args, int i) throws BadCommandLineException {
/* 279 */     int j = super.parseArguments(args, i);
/* 280 */     if (j > 0) return j;
/*     */
/* 282 */     if (args[i].equals("-b")) {
/* 283 */       addBindings(requireArgument("-b", args, ++i));
/* 284 */       return 2;
/* 285 */     }  if (args[i].equals("-wsdllocation")) {
/* 286 */       this.wsdlLocation = requireArgument("-wsdllocation", args, ++i);
/* 287 */       return 2;
/* 288 */     }  if (args[i].equals("-XadditionalHeaders")) {
/* 289 */       this.additionalHeaders = true;
/* 290 */       return 1;
/* 291 */     }  if (args[i].equals("-XdisableSSLHostnameVerification")) {
/* 292 */       this.disableSSLHostnameVerification = true;
/* 293 */       return 1;
/* 294 */     }  if (args[i].equals("-p")) {
/* 295 */       this.defaultPackage = requireArgument("-p", args, ++i);
/* 296 */       return 2;
/* 297 */     }  if (args[i].equals("-catalog")) {
/* 298 */       String catalog = requireArgument("-catalog", args, ++i);
/*     */       try {
/* 300 */         if (this.entityResolver == null) {
/* 301 */           if (catalog != null && catalog.length() > 0)
/* 302 */             this.entityResolver = XmlUtil.createEntityResolver(JAXWSUtils.getFileOrURL(JAXWSUtils.absolutize(Util.escapeSpace(catalog))));
/* 303 */         } else if (catalog != null && catalog.length() > 0) {
/* 304 */           EntityResolver er = XmlUtil.createEntityResolver(JAXWSUtils.getFileOrURL(JAXWSUtils.absolutize(Util.escapeSpace(catalog))));
/* 305 */           this.entityResolver = (EntityResolver)new ForkEntityResolver(er, this.entityResolver);
/*     */         }
/* 307 */       } catch (IOException e) {
/* 308 */         throw new BadCommandLineException(WscompileMessages.WSIMPORT_FAILED_TO_PARSE(catalog, e.getMessage()));
/*     */       }
/* 310 */       return 2;
/* 311 */     }  if (args[i].startsWith("-httpproxy:")) {
/* 312 */       String value = args[i].substring(11);
/* 313 */       if (value.length() == 0) {
/* 314 */         throw new BadCommandLineException(WscompileMessages.WSCOMPILE_INVALID_OPTION(args[i]));
/*     */       }
/* 316 */       parseProxy(value);
/* 317 */       if (this.proxyHost != null || this.proxyPort != null) {
/* 318 */         System.setProperty("proxySet", "true");
/*     */       }
/* 320 */       if (this.proxyHost != null) {
/* 321 */         System.setProperty("proxyHost", this.proxyHost);
/*     */       }
/* 323 */       if (this.proxyPort != null) {
/* 324 */         System.setProperty("proxyPort", this.proxyPort);
/*     */       }
/* 326 */       return 1;
/* 327 */     }  if (args[i].equals("-Xno-addressing-databinding")) {
/* 328 */       this.noAddressingBbinding = true;
/* 329 */       return 1;
/* 330 */     }  if (args[i].startsWith("-B")) {
/*     */
/* 332 */       String[] subCmd = new String[args.length - i];
/* 333 */       System.arraycopy(args, i, subCmd, 0, subCmd.length);
/* 334 */       subCmd[0] = subCmd[0].substring(2);
/*     */
/* 336 */       Options jaxbOptions = this.schemaCompiler.getOptions();
/*     */       try {
/* 338 */         int r = jaxbOptions.parseArgument(subCmd, 0);
/* 339 */         if (r == 0)
/*     */         {
/* 341 */           throw new BadCommandLineException(WscompileMessages.WSIMPORT_NO_SUCH_JAXB_OPTION(subCmd[0]));
/*     */         }
/* 343 */         return r;
/* 344 */       } catch (BadCommandLineException e) {
/*     */
/* 346 */         throw new BadCommandLineException(e.getMessage(), e);
/*     */       }
/* 348 */     }  if (args[i].equals("-Xauthfile")) {
/* 349 */       String authfile = requireArgument("-Xauthfile", args, ++i);
/* 350 */       this.authFile = new File(authfile);
/* 351 */       return 2;
/* 352 */     }  if (args[i].equals("-clientjar")) {
/* 353 */       this.clientjar = requireArgument("-clientjar", args, ++i);
/* 354 */       return 2;
/* 355 */     }  if (args[i].equals("-implDestDir")) {
/* 356 */       this.implDestDir = new File(requireArgument("-implDestDir", args, ++i));
/* 357 */       if (!this.implDestDir.exists())
/* 358 */         throw new BadCommandLineException(WscompileMessages.WSCOMPILE_NO_SUCH_DIRECTORY(this.implDestDir.getPath()));
/* 359 */       return 2;
/* 360 */     }  if (args[i].equals("-implServiceName")) {
/* 361 */       this.implServiceName = requireArgument("-implServiceName", args, ++i);
/* 362 */       return 2;
/* 363 */     }  if (args[i].equals("-implPortName")) {
/* 364 */       this.implPortName = requireArgument("-implPortName", args, ++i);
/* 365 */       return 2;
/* 366 */     }  if (args[i].equals("-generateJWS")) {
/* 367 */       this.isGenerateJWS = true;
/* 368 */       return 1;
/* 369 */     }  if (args[i].equals("-XuseBaseResourceAndURLToLoadWSDL")) {
/* 370 */       this.useBaseResourceAndURLToLoadWSDL = true;
/* 371 */       return 1;
/* 372 */     }  if (args[i].equals("-XdisableAuthenticator")) {
/* 373 */       this.disableAuthenticator = true;
/* 374 */       return 1;
/*     */     }
/*     */
/*     */
/* 378 */     for (GeneratorExtension f : ServiceFinder.<GeneratorExtension>find(GeneratorExtension.class)) {
/* 379 */       if (f.validateOption(args[i])) {
/* 380 */         this.extensionOptions.put(args[i], requireArgument(args[i], args, ++i));
/* 381 */         return 2;
/*     */       }
/*     */     }
/*     */
/*     */
/* 386 */     for (Plugin plugin : getAllPlugins()) {
/*     */       try {
/* 388 */         if (('-' + plugin.getOptionName()).equals(args[i])) {
/* 389 */           this.activePlugins.add(plugin);
/* 390 */           plugin.onActivated(this);
/* 391 */           return 1;
/*     */         }
/* 393 */         int r = plugin.parseArgument(this, args, i);
/* 394 */         if (r != 0) {
/* 395 */           return r;
/*     */         }
/* 397 */       } catch (IOException e) {
/* 398 */         throw new BadCommandLineException(e.getMessage(), e);
/*     */       }
/*     */     }
/*     */
/* 402 */     return 0;
/*     */   }
/*     */
/*     */   public void validate() throws BadCommandLineException {
/* 406 */     if (this.wsdls.isEmpty()) {
/* 407 */       throw new BadCommandLineException(WscompileMessages.WSIMPORT_MISSING_FILE());
/*     */     }
/*     */
/* 410 */     if (this.wsdlLocation != null && this.clientjar != null) {
/* 411 */       throw new BadCommandLineException(WscompileMessages.WSIMPORT_WSDLLOCATION_CLIENTJAR());
/*     */     }
/* 413 */     if (this.wsdlLocation == null) {
/* 414 */       this.wsdlLocation = ((InputSource)this.wsdls.get(0)).getSystemId();
/*     */     }
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */   protected void addFile(String arg) throws BadCommandLineException {
/* 422 */     addFile(arg, this.wsdls, ".wsdl");
/*     */   }
/*     */
/* 425 */   private final List<InputSource> wsdls = new ArrayList<>();
/* 426 */   private final List<InputSource> schemas = new ArrayList<>();
/* 427 */   private final List<InputSource> bindingFiles = new ArrayList<>();
/* 428 */   private final List<InputSource> jaxwsCustomBindings = new ArrayList<>();
/* 429 */   private final List<InputSource> jaxbCustomBindings = new ArrayList<>();
/* 430 */   private final List<Element> handlerConfigs = new ArrayList<>();
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   public Element getHandlerChainConfiguration() {
/* 439 */     if (this.handlerConfigs.size() > 0)
/* 440 */       return this.handlerConfigs.get(0);
/* 441 */     return null;
/*     */   }
/*     */
/*     */   public void addHandlerChainConfiguration(Element config) {
/* 445 */     this.handlerConfigs.add(config);
/*     */   }
/*     */
/*     */   public InputSource[] getWSDLs() {
/* 449 */     return this.wsdls.<InputSource>toArray(new InputSource[this.wsdls.size()]);
/*     */   }
/*     */
/*     */   public InputSource[] getSchemas() {
/* 453 */     return this.schemas.<InputSource>toArray(new InputSource[this.schemas.size()]);
/*     */   }
/*     */
/*     */   public InputSource[] getWSDLBindings() {
/* 457 */     return this.jaxwsCustomBindings.<InputSource>toArray(new InputSource[this.jaxwsCustomBindings.size()]);
/*     */   }
/*     */
/*     */   public InputSource[] getSchemaBindings() {
/* 461 */     return this.jaxbCustomBindings.<InputSource>toArray(new InputSource[this.jaxbCustomBindings.size()]);
/*     */   }
/*     */
/*     */   public void addWSDL(File source) {
/* 465 */     addWSDL(fileToInputSource(source));
/*     */   }
/*     */
/*     */   public void addWSDL(InputSource is) {
/* 469 */     this.wsdls.add(absolutize(is));
/*     */   }
/*     */
/*     */   public void addSchema(File source) {
/* 473 */     addSchema(fileToInputSource(source));
/*     */   }
/*     */
/*     */   public void addSchema(InputSource is) {
/* 477 */     this.schemas.add(is);
/*     */   }
/*     */
/*     */   private InputSource fileToInputSource(File source) {
/*     */     try {
/* 482 */       String url = source.toURL().toExternalForm();
/* 483 */       return new InputSource(Util.escapeSpace(url));
/* 484 */     } catch (MalformedURLException e) {
/* 485 */       return new InputSource(source.getPath());
/*     */     }
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */   public void addGrammarRecursive(File dir) {
/* 493 */     addRecursive(dir, ".wsdl", this.wsdls);
/* 494 */     addRecursive(dir, ".xsd", this.schemas);
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */   public void addWSDLBindFile(InputSource is) {
/* 501 */     this.jaxwsCustomBindings.add(new RereadInputSource(absolutize(is)));
/*     */   }
/*     */
/*     */   public void addSchemmaBindFile(InputSource is) {
/* 505 */     this.jaxbCustomBindings.add(new RereadInputSource(absolutize(is)));
/*     */   }
/*     */
/*     */   private void addRecursive(File dir, String suffix, List<InputSource> result) {
/* 509 */     File[] files = dir.listFiles();
/* 510 */     if (files == null)
/*     */       return;
/* 512 */     for (File f : files) {
/* 513 */       if (f.isDirectory()) {
/* 514 */         addRecursive(f, suffix, result);
/* 515 */       } else if (f.getPath().endsWith(suffix)) {
/* 516 */         result.add(absolutize(fileToInputSource(f)));
/*     */       }
/*     */     }
/*     */   }
/*     */
/*     */
/*     */   private InputSource absolutize(InputSource is) {
/*     */     try {
/* 524 */       URL baseURL = (new File(".")).getCanonicalFile().toURL();
/* 525 */       is.setSystemId((new URL(baseURL, is.getSystemId())).toExternalForm());
/* 526 */     } catch (IOException iOException) {}
/*     */
/*     */
/* 529 */     return is;
/*     */   }
/*     */
/*     */   public void addBindings(String name) throws BadCommandLineException {
/* 533 */     addFile(name, this.bindingFiles, (String)null);
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   private void addFile(String name, List<InputSource> target, String suffix) throws BadCommandLineException {
/*     */     Object src;
/*     */     try {
/* 546 */       src = Util.getFileOrURL(name);
/* 547 */     } catch (IOException e) {
/* 548 */       throw new BadCommandLineException(WscompileMessages.WSIMPORT_NOT_A_FILE_NOR_URL(name));
/*     */     }
/* 550 */     if (src instanceof URL) {
/* 551 */       target.add(absolutize(new InputSource(Util.escapeSpace(((URL)src).toExternalForm()))));
/*     */     } else {
/* 553 */       File fsrc = (File)src;
/* 554 */       if (fsrc.isDirectory()) {
/* 555 */         addRecursive(fsrc, suffix, target);
/*     */       } else {
/* 557 */         target.add(absolutize(fileToInputSource(fsrc)));
/*     */       }
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
/*     */   public final void parseBindings(ErrorReceiver receiver) {
/* 573 */     for (InputSource is : this.bindingFiles) {
/*     */
/* 575 */       XMLStreamReader reader = XMLStreamReaderFactory.create(is, true);
/* 576 */       XMLStreamReaderUtil.nextElementContent(reader);
/* 577 */       if (reader.getName().equals(JAXWSBindingsConstants.JAXWS_BINDINGS)) {
/* 578 */         this.jaxwsCustomBindings.add(new RereadInputSource(is)); continue;
/* 579 */       }  if (reader.getName().equals(JAXWSBindingsConstants.JAXB_BINDINGS) || reader
/* 580 */         .getName().equals(new QName("http://www.w3.org/2001/XMLSchema", "schema"))) {
/* 581 */         this.jaxbCustomBindings.add(new RereadInputSource(is)); continue;
/*     */       }
/* 583 */       LocatorImpl locator = new LocatorImpl();
/* 584 */       locator.setSystemId(reader.getLocation().getSystemId());
/* 585 */       locator.setPublicId(reader.getLocation().getPublicId());
/* 586 */       locator.setLineNumber(reader.getLocation().getLineNumber());
/* 587 */       locator.setColumnNumber(reader.getLocation().getColumnNumber());
/* 588 */       receiver.warning(locator, ConfigurationMessages.CONFIGURATION_NOT_BINDING_FILE(is.getSystemId()));
/*     */     }
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   public String getExtensionOption(String argument) {
/* 597 */     return this.extensionOptions.get(argument);
/*     */   }
/*     */
/*     */   private void parseProxy(String text) throws BadCommandLineException {
/* 601 */     int i = text.lastIndexOf('@');
/* 602 */     int j = text.lastIndexOf(':');
/*     */
/* 604 */     if (i > 0) {
/* 605 */       this.proxyAuth = text.substring(0, i);
/* 606 */       if (j > i) {
/* 607 */         this.proxyHost = text.substring(i + 1, j);
/* 608 */         this.proxyPort = text.substring(j + 1);
/*     */       } else {
/* 610 */         this.proxyHost = text.substring(i + 1);
/* 611 */         this.proxyPort = "8080";
/*     */       }
/*     */
/*     */     }
/* 615 */     else if (j < 0) {
/*     */
/* 617 */       this.proxyHost = text;
/* 618 */       this.proxyPort = "8080";
/*     */     } else {
/* 620 */       this.proxyHost = text.substring(0, j);
/* 621 */       this.proxyPort = text.substring(j + 1);
/*     */     }
/*     */
/*     */     try {
/* 625 */       Integer.valueOf(this.proxyPort);
/* 626 */     } catch (NumberFormatException e) {
/* 627 */       throw new BadCommandLineException(WscompileMessages.WSIMPORT_ILLEGAL_PROXY(text));
/*     */     }
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   private static <T> T[] findServices(Class<T> clazz, ClassLoader classLoader) {
/* 636 */     ServiceFinder<T> serviceFinder = ServiceFinder.find(clazz, classLoader);
/* 637 */     List<T> r = new ArrayList<>();
/* 638 */     for (T t : serviceFinder) {
/* 639 */       r.add(t);
/*     */     }
/* 641 */     return r.toArray((T[])Array.newInstance(clazz, r.size()));
/*     */   }
/*     */   private static final class ByteStream extends ByteArrayOutputStream { private ByteStream() {}
/*     */
/*     */     byte[] getBuffer() {
/* 646 */       return this.buf;
/*     */     } }
/*     */
/*     */
/*     */   private static final class RereadInputStream extends InputStream {
/*     */     private InputStream is;
/*     */     private ByteStream bs;
/*     */
/*     */     RereadInputStream(InputStream is) {
/* 655 */       this.is = is;
/* 656 */       this.bs = new ByteStream();
/*     */     }
/*     */
/*     */
/*     */     public int available() throws IOException {
/* 661 */       return this.is.available();
/*     */     }
/*     */
/*     */
/*     */     public void close() throws IOException {
/* 666 */       if (this.bs != null) {
/* 667 */         InputStream i = new ByteArrayInputStream(this.bs.getBuffer());
/* 668 */         this.bs = null;
/* 669 */         this.is.close();
/* 670 */         this.is = i;
/*     */       }
/*     */     }
/*     */
/*     */
/*     */     public synchronized void mark(int readlimit) {
/* 676 */       this.is.mark(readlimit);
/*     */     }
/*     */
/*     */
/*     */     public boolean markSupported() {
/* 681 */       return this.is.markSupported();
/*     */     }
/*     */
/*     */
/*     */     public int read() throws IOException {
/* 686 */       int r = this.is.read();
/* 687 */       if (this.bs != null)
/* 688 */         this.bs.write(r);
/* 689 */       return r;
/*     */     }
/*     */
/*     */
/*     */     public int read(byte[] b, int off, int len) throws IOException {
/* 694 */       int r = this.is.read(b, off, len);
/* 695 */       if (r > 0 && this.bs != null)
/* 696 */         this.bs.write(b, off, r);
/* 697 */       return r;
/*     */     }
/*     */
/*     */
/*     */     public int read(byte[] b) throws IOException {
/* 702 */       int r = this.is.read(b);
/* 703 */       if (r > 0 && this.bs != null)
/* 704 */         this.bs.write(b, 0, r);
/* 705 */       return r;
/*     */     }
/*     */
/*     */
/*     */     public synchronized void reset() throws IOException {
/* 710 */       this.is.reset();
/*     */     }
/*     */   }
/*     */
/*     */   private static final class RereadInputSource extends InputSource {
/*     */     private InputSource is;
/*     */
/*     */     RereadInputSource(InputSource is) {
/* 718 */       this.is = is;
/*     */     }
/*     */
/*     */
/*     */     public InputStream getByteStream() {
/* 723 */       InputStream i = this.is.getByteStream();
/* 724 */       if (i != null && !(i instanceof RereadInputStream)) {
/* 725 */         i = new RereadInputStream(i);
/* 726 */         this.is.setByteStream(i);
/*     */       }
/* 728 */       return i;
/*     */     }
/*     */
/*     */
/*     */
/*     */     public Reader getCharacterStream() {
/* 734 */       return this.is.getCharacterStream();
/*     */     }
/*     */
/*     */
/*     */     public String getEncoding() {
/* 739 */       return this.is.getEncoding();
/*     */     }
/*     */
/*     */
/*     */     public String getPublicId() {
/* 744 */       return this.is.getPublicId();
/*     */     }
/*     */
/*     */
/*     */     public String getSystemId() {
/* 749 */       return this.is.getSystemId();
/*     */     }
/*     */
/*     */
/*     */     public void setByteStream(InputStream byteStream) {
/* 754 */       this.is.setByteStream(byteStream);
/*     */     }
/*     */
/*     */
/*     */     public void setCharacterStream(Reader characterStream) {
/* 759 */       this.is.setCharacterStream(characterStream);
/*     */     }
/*     */
/*     */
/*     */     public void setEncoding(String encoding) {
/* 764 */       this.is.setEncoding(encoding);
/*     */     }
/*     */
/*     */
/*     */     public void setPublicId(String publicId) {
/* 769 */       this.is.setPublicId(publicId);
/*     */     }
/*     */
/*     */
/*     */     public void setSystemId(String systemId) {
/* 774 */       this.is.setSystemId(systemId);
/*     */     }
/*     */   }
/*     */
/*     */
/*     */   protected void disableXmlSecurity() {
/* 780 */     super.disableXmlSecurity();
/* 781 */     (this.schemaCompiler.getOptions()).disableXmlSecurity = true;
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\ws\wscompile\WsimportOptions.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
