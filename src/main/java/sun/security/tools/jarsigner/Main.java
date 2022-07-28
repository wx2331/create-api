/*      */ package sun.security.tools.jarsigner;
/*      */ import com.sun.jarsigner.ContentSigner;
/*      */ import java.io.ByteArrayInputStream;
/*      */ import java.io.ByteArrayOutputStream;
/*      */ import java.io.File;
/*      */ import java.io.FileInputStream;
/*      */ import java.io.FileNotFoundException;
/*      */ import java.io.FileOutputStream;
/*      */ import java.io.IOException;
/*      */ import java.io.InputStream;
/*      */ import java.io.PrintStream;
/*      */ import java.lang.reflect.Constructor;
/*      */ import java.net.MalformedURLException;
/*      */ import java.net.SocketTimeoutException;
/*      */ import java.net.URI;
/*      */ import java.net.URL;
/*      */ import java.net.URLClassLoader;
/*      */ import java.security.CodeSigner;
/*      */ import java.security.CryptoPrimitive;
/*      */ import java.security.InvalidAlgorithmParameterException;
/*      */ import java.security.Key;
/*      */ import java.security.KeyStore;
/*      */ import java.security.KeyStoreException;
/*      */ import java.security.MessageDigest;
/*      */ import java.security.NoSuchAlgorithmException;
/*      */ import java.security.NoSuchProviderException;
/*      */ import java.security.PrivateKey;
/*      */ import java.security.Provider;
/*      */ import java.security.PublicKey;
/*      */ import java.security.Security;
/*      */ import java.security.Timestamp;
/*      */ import java.security.UnrecoverableKeyException;
/*      */ import java.security.cert.Certificate;
/*      */ import java.security.cert.CertificateException;
/*      */ import java.security.cert.CertificateExpiredException;
/*      */ import java.security.cert.CertificateFactory;
/*      */ import java.security.cert.CertificateNotYetValidException;
/*      */ import java.security.cert.CertificateParsingException;
/*      */ import java.security.cert.PKIXBuilderParameters;
/*      */ import java.security.cert.TrustAnchor;
/*      */ import java.security.cert.X509Certificate;
/*      */ import java.text.Collator;
/*      */ import java.text.MessageFormat;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Arrays;
/*      */ import java.util.Base64;
/*      */ import java.util.Calendar;
/*      */ import java.util.Collections;
/*      */ import java.util.Date;
/*      */ import java.util.EnumSet;
/*      */ import java.util.Enumeration;
/*      */ import java.util.HashMap;
/*      */ import java.util.HashSet;
/*      */ import java.util.Hashtable;
/*      */ import java.util.IdentityHashMap;
/*      */ import java.util.LinkedHashMap;
/*      */ import java.util.List;
/*      */ import java.util.Locale;
/*      */ import java.util.Map;
/*      */ import java.util.ResourceBundle;
/*      */ import java.util.Set;
/*      */ import java.util.TimeZone;
/*      */ import java.util.Vector;
/*      */ import java.util.jar.Attributes;
/*      */ import java.util.jar.JarEntry;
/*      */ import java.util.jar.JarFile;
/*      */ import java.util.jar.Manifest;
/*      */ import java.util.stream.Collectors;
/*      */ import java.util.zip.ZipEntry;
/*      */ import java.util.zip.ZipFile;
/*      */ import java.util.zip.ZipOutputStream;
/*      */ import sun.security.pkcs.PKCS7;
/*      */ import sun.security.pkcs.SignerInfo;
/*      */ import sun.security.timestamp.TimestampToken;
/*      */ import sun.security.tools.KeyStoreUtil;
/*      */ import sun.security.tools.PathList;
/*      */ import sun.security.util.DerInputStream;
/*      */ import sun.security.util.DerValue;
/*      */ import sun.security.util.DisabledAlgorithmConstraints;
/*      */ import sun.security.util.KeyUtil;
/*      */ import sun.security.util.ManifestDigester;
/*      */ import sun.security.util.Password;
/*      */ import sun.security.util.SignatureFileVerifier;
/*      */ import sun.security.validator.Validator;
/*      */ import sun.security.validator.ValidatorException;
/*      */ import sun.security.x509.AlgorithmId;
/*      */ import sun.security.x509.NetscapeCertTypeExtension;
/*      */ 
/*      */ public class Main {
/*   90 */   private static final ResourceBundle rb = ResourceBundle.getBundle("sun.security.tools.jarsigner.Resources");
/*   91 */   private static final Collator collator = Collator.getInstance(); private static final String META_INF = "META-INF/";
/*      */   
/*      */   static {
/*   94 */     collator.setStrength(0);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*   99 */   private static final Class<?>[] PARAM_STRING = new Class[] { String.class };
/*      */   
/*      */   private static final String NONE = "NONE";
/*      */   
/*      */   private static final String P11KEYSTORE = "PKCS11";
/*      */   
/*      */   private static final long SIX_MONTHS = 15552000000L;
/*      */   private static final long ONE_YEAR = 31622400000L;
/*  107 */   private static final DisabledAlgorithmConstraints DISABLED_CHECK = new DisabledAlgorithmConstraints("jdk.jar.disabledAlgorithms");
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  112 */   private static final Set<CryptoPrimitive> DIGEST_PRIMITIVE_SET = Collections.unmodifiableSet(EnumSet.of(CryptoPrimitive.MESSAGE_DIGEST));
/*      */   
/*  114 */   private static final Set<CryptoPrimitive> SIG_PRIMITIVE_SET = Collections.unmodifiableSet(EnumSet.of(CryptoPrimitive.SIGNATURE));
/*      */   
/*      */   static final String VERSION = "1.0";
/*      */   static final int IN_KEYSTORE = 1;
/*      */   static final int IN_SCOPE = 2;
/*      */   static final int NOT_ALIAS = 4;
/*      */   static final int SIGNED_BY_ALIAS = 8;
/*      */   X509Certificate[] certChain;
/*      */   PrivateKey privateKey;
/*      */   KeyStore store;
/*      */   String keystore;
/*      */   
/*      */   public static void main(String[] paramArrayOfString) throws Exception {
/*  127 */     Main main = new Main();
/*  128 */     main.run(paramArrayOfString);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   boolean nullStream = false;
/*      */   
/*      */   boolean token = false;
/*      */   
/*      */   String jarfile;
/*      */   
/*      */   String alias;
/*      */   
/*  141 */   List<String> ckaliases = new ArrayList<>();
/*      */   char[] storepass;
/*      */   boolean protectedPath;
/*      */   String storetype;
/*      */   String providerName;
/*  146 */   Vector<String> providers = null;
/*      */   
/*  148 */   HashMap<String, String> providerArgs = new HashMap<>();
/*      */   char[] keypass;
/*      */   String sigfile;
/*      */   String sigalg;
/*  152 */   String digestalg = "SHA-256";
/*      */   String signedjar;
/*      */   String tsaUrl;
/*      */   String tsaAlias;
/*      */   String altCertChain;
/*      */   String tSAPolicyID;
/*  158 */   String tSADigestAlg = "SHA-256";
/*      */   boolean verify = false;
/*  160 */   String verbose = null;
/*      */   
/*      */   boolean showcerts = false;
/*      */   
/*      */   boolean debug = false;
/*      */   boolean signManifest = true;
/*      */   boolean externalSF = true;
/*      */   boolean strict = false;
/*  168 */   private ByteArrayOutputStream baos = new ByteArrayOutputStream(2048);
/*  169 */   private byte[] buffer = new byte[8192];
/*  170 */   private ContentSigner signingMechanism = null;
/*  171 */   private String altSignerClass = null;
/*  172 */   private String altSignerClasspath = null;
/*  173 */   private ZipFile zipFile = null;
/*      */   
/*      */   private boolean hasExpiringCert = false;
/*      */   
/*      */   private boolean hasExpiringTsaCert = false;
/*      */   
/*      */   private boolean noTimestamp = true;
/*      */   
/*  181 */   private Date expireDate = null;
/*  182 */   private Date tsaExpireDate = null;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   boolean hasTimestampBlock = false;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  197 */   private int weakAlg = 0;
/*      */   
/*      */   private boolean hasExpiredCert = false;
/*      */   private boolean hasExpiredTsaCert = false;
/*      */   private boolean notYetValidCert = false;
/*      */   private boolean chainNotValidated = false;
/*      */   private boolean tsaChainNotValidated = false;
/*      */   private boolean notSignedByAlias = false;
/*      */   private boolean aliasNotInStore = false;
/*      */   private boolean hasUnsignedEntry = false;
/*      */   private boolean badKeyUsage = false;
/*      */   private boolean badExtendedKeyUsage = false;
/*      */   private boolean badNetscapeCertType = false;
/*      */   private boolean signerSelfSigned = false;
/*  211 */   private Throwable chainNotValidatedReason = null;
/*  212 */   private Throwable tsaChainNotValidatedReason = null;
/*      */   
/*      */   private boolean seeWeak = false;
/*      */   
/*      */   PKIXBuilderParameters pkixParameters;
/*  217 */   Set<X509Certificate> trustedCerts = new HashSet<>();
/*      */   
/*      */   public void run(String[] paramArrayOfString) {
/*      */     try {
/*  221 */       parseArgs(paramArrayOfString);
/*      */ 
/*      */       
/*  224 */       if (this.providers != null) {
/*  225 */         ClassLoader classLoader = ClassLoader.getSystemClassLoader();
/*  226 */         Enumeration<String> enumeration = this.providers.elements();
/*  227 */         while (enumeration.hasMoreElements()) {
/*  228 */           Class<?> clazz; Object object; String str1 = enumeration.nextElement();
/*      */           
/*  230 */           if (classLoader != null) {
/*  231 */             clazz = classLoader.loadClass(str1);
/*      */           } else {
/*  233 */             clazz = Class.forName(str1);
/*      */           } 
/*      */           
/*  236 */           String str2 = this.providerArgs.get(str1);
/*      */           
/*  238 */           if (str2 == null) {
/*  239 */             object = clazz.newInstance();
/*      */           } else {
/*      */             
/*  242 */             Constructor<?> constructor = clazz.getConstructor(PARAM_STRING);
/*  243 */             object = constructor.newInstance(new Object[] { str2 });
/*      */           } 
/*      */           
/*  246 */           if (!(object instanceof Provider)) {
/*      */             
/*  248 */             MessageFormat messageFormat = new MessageFormat(rb.getString("provName.not.a.provider"));
/*  249 */             Object[] arrayOfObject = { str1 };
/*  250 */             throw new Exception(messageFormat.format(arrayOfObject));
/*      */           } 
/*  252 */           Security.addProvider((Provider)object);
/*      */         } 
/*      */       } 
/*      */       
/*  256 */       if (this.verify) {
/*      */         try {
/*  258 */           loadKeyStore(this.keystore, false);
/*  259 */         } catch (Exception exception) {
/*  260 */           if (this.keystore != null || this.storepass != null) {
/*  261 */             System.out.println(rb.getString("jarsigner.error.") + exception
/*  262 */                 .getMessage());
/*  263 */             System.exit(1);
/*      */           } 
/*      */         } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  271 */         verifyJar(this.jarfile);
/*      */       } else {
/*  273 */         loadKeyStore(this.keystore, true);
/*  274 */         getAliasInfo(this.alias);
/*      */ 
/*      */         
/*  277 */         if (this.altSignerClass != null) {
/*  278 */           this.signingMechanism = loadSigningMechanism(this.altSignerClass, this.altSignerClasspath);
/*      */         }
/*      */         
/*  281 */         signJar(this.jarfile, this.alias, paramArrayOfString);
/*      */       } 
/*  283 */     } catch (Exception exception) {
/*  284 */       System.out.println(rb.getString("jarsigner.error.") + exception);
/*  285 */       if (this.debug) {
/*  286 */         exception.printStackTrace();
/*      */       }
/*  288 */       System.exit(1);
/*      */     } finally {
/*      */       
/*  291 */       if (this.keypass != null) {
/*  292 */         Arrays.fill(this.keypass, ' ');
/*  293 */         this.keypass = null;
/*      */       } 
/*      */       
/*  296 */       if (this.storepass != null) {
/*  297 */         Arrays.fill(this.storepass, ' ');
/*  298 */         this.storepass = null;
/*      */       } 
/*      */     } 
/*      */     
/*  302 */     if (this.strict) {
/*  303 */       int i = 0;
/*  304 */       if (this.weakAlg != 0 || this.chainNotValidated || this.hasExpiredCert || this.hasExpiredTsaCert || this.notYetValidCert || this.signerSelfSigned)
/*      */       {
/*  306 */         i |= 0x4;
/*      */       }
/*  308 */       if (this.badKeyUsage || this.badExtendedKeyUsage || this.badNetscapeCertType) {
/*  309 */         i |= 0x8;
/*      */       }
/*  311 */       if (this.hasUnsignedEntry) {
/*  312 */         i |= 0x10;
/*      */       }
/*  314 */       if (this.notSignedByAlias || this.aliasNotInStore) {
/*  315 */         i |= 0x20;
/*      */       }
/*  317 */       if (this.tsaChainNotValidated) {
/*  318 */         i |= 0x40;
/*      */       }
/*  320 */       if (i != 0) {
/*  321 */         System.exit(i);
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   void parseArgs(String[] paramArrayOfString) {
/*  331 */     byte b = 0;
/*      */     
/*  333 */     if (paramArrayOfString.length == 0) fullusage(); 
/*  334 */     for (b = 0; b < paramArrayOfString.length; b++) {
/*      */       
/*  336 */       String str1 = paramArrayOfString[b];
/*  337 */       String str2 = null;
/*      */       
/*  339 */       if (str1.startsWith("-")) {
/*  340 */         int i = str1.indexOf(':');
/*  341 */         if (i > 0) {
/*  342 */           str2 = str1.substring(i + 1);
/*  343 */           str1 = str1.substring(0, i);
/*      */         } 
/*      */       } 
/*      */       
/*  347 */       if (!str1.startsWith("-")) {
/*  348 */         if (this.jarfile == null) {
/*  349 */           this.jarfile = str1;
/*      */         } else {
/*  351 */           this.alias = str1;
/*  352 */           this.ckaliases.add(this.alias);
/*      */         } 
/*  354 */       } else if (collator.compare(str1, "-keystore") == 0) {
/*  355 */         if (++b == paramArrayOfString.length) usageNoArg(); 
/*  356 */         this.keystore = paramArrayOfString[b];
/*  357 */       } else if (collator.compare(str1, "-storepass") == 0) {
/*  358 */         if (++b == paramArrayOfString.length) usageNoArg(); 
/*  359 */         this.storepass = getPass(str2, paramArrayOfString[b]);
/*  360 */       } else if (collator.compare(str1, "-storetype") == 0) {
/*  361 */         if (++b == paramArrayOfString.length) usageNoArg(); 
/*  362 */         this.storetype = paramArrayOfString[b];
/*  363 */       } else if (collator.compare(str1, "-providerName") == 0) {
/*  364 */         if (++b == paramArrayOfString.length) usageNoArg(); 
/*  365 */         this.providerName = paramArrayOfString[b];
/*  366 */       } else if (collator.compare(str1, "-provider") == 0 || collator
/*  367 */         .compare(str1, "-providerClass") == 0) {
/*  368 */         if (++b == paramArrayOfString.length) usageNoArg(); 
/*  369 */         if (this.providers == null) {
/*  370 */           this.providers = new Vector<>(3);
/*      */         }
/*  372 */         this.providers.add(paramArrayOfString[b]);
/*      */         
/*  374 */         if (paramArrayOfString.length > b + 1) {
/*  375 */           str1 = paramArrayOfString[b + 1];
/*  376 */           if (collator.compare(str1, "-providerArg") == 0) {
/*  377 */             if (paramArrayOfString.length == b + 2) usageNoArg(); 
/*  378 */             this.providerArgs.put(paramArrayOfString[b], paramArrayOfString[b + 2]);
/*  379 */             b += 2;
/*      */           } 
/*      */         } 
/*  382 */       } else if (collator.compare(str1, "-protected") == 0) {
/*  383 */         this.protectedPath = true;
/*  384 */       } else if (collator.compare(str1, "-certchain") == 0) {
/*  385 */         if (++b == paramArrayOfString.length) usageNoArg(); 
/*  386 */         this.altCertChain = paramArrayOfString[b];
/*  387 */       } else if (collator.compare(str1, "-tsapolicyid") == 0) {
/*  388 */         if (++b == paramArrayOfString.length) usageNoArg(); 
/*  389 */         this.tSAPolicyID = paramArrayOfString[b];
/*  390 */       } else if (collator.compare(str1, "-tsadigestalg") == 0) {
/*  391 */         if (++b == paramArrayOfString.length) usageNoArg(); 
/*  392 */         this.tSADigestAlg = paramArrayOfString[b];
/*  393 */       } else if (collator.compare(str1, "-debug") == 0) {
/*  394 */         this.debug = true;
/*  395 */       } else if (collator.compare(str1, "-keypass") == 0) {
/*  396 */         if (++b == paramArrayOfString.length) usageNoArg(); 
/*  397 */         this.keypass = getPass(str2, paramArrayOfString[b]);
/*  398 */       } else if (collator.compare(str1, "-sigfile") == 0) {
/*  399 */         if (++b == paramArrayOfString.length) usageNoArg(); 
/*  400 */         this.sigfile = paramArrayOfString[b];
/*  401 */       } else if (collator.compare(str1, "-signedjar") == 0) {
/*  402 */         if (++b == paramArrayOfString.length) usageNoArg(); 
/*  403 */         this.signedjar = paramArrayOfString[b];
/*  404 */       } else if (collator.compare(str1, "-tsa") == 0) {
/*  405 */         if (++b == paramArrayOfString.length) usageNoArg(); 
/*  406 */         this.tsaUrl = paramArrayOfString[b];
/*  407 */       } else if (collator.compare(str1, "-tsacert") == 0) {
/*  408 */         if (++b == paramArrayOfString.length) usageNoArg(); 
/*  409 */         this.tsaAlias = paramArrayOfString[b];
/*  410 */       } else if (collator.compare(str1, "-altsigner") == 0) {
/*  411 */         if (++b == paramArrayOfString.length) usageNoArg(); 
/*  412 */         this.altSignerClass = paramArrayOfString[b];
/*  413 */       } else if (collator.compare(str1, "-altsignerpath") == 0) {
/*  414 */         if (++b == paramArrayOfString.length) usageNoArg(); 
/*  415 */         this.altSignerClasspath = paramArrayOfString[b];
/*  416 */       } else if (collator.compare(str1, "-sectionsonly") == 0) {
/*  417 */         this.signManifest = false;
/*  418 */       } else if (collator.compare(str1, "-internalsf") == 0) {
/*  419 */         this.externalSF = false;
/*  420 */       } else if (collator.compare(str1, "-verify") == 0) {
/*  421 */         this.verify = true;
/*  422 */       } else if (collator.compare(str1, "-verbose") == 0) {
/*  423 */         this.verbose = (str2 != null) ? str2 : "all";
/*  424 */       } else if (collator.compare(str1, "-sigalg") == 0) {
/*  425 */         if (++b == paramArrayOfString.length) usageNoArg(); 
/*  426 */         this.sigalg = paramArrayOfString[b];
/*  427 */       } else if (collator.compare(str1, "-digestalg") == 0) {
/*  428 */         if (++b == paramArrayOfString.length) usageNoArg(); 
/*  429 */         this.digestalg = paramArrayOfString[b];
/*  430 */       } else if (collator.compare(str1, "-certs") == 0) {
/*  431 */         this.showcerts = true;
/*  432 */       } else if (collator.compare(str1, "-strict") == 0) {
/*  433 */         this.strict = true;
/*  434 */       } else if (collator.compare(str1, "-h") == 0 || collator
/*  435 */         .compare(str1, "-help") == 0) {
/*  436 */         fullusage();
/*      */       } else {
/*  438 */         System.err.println(rb
/*  439 */             .getString("Illegal.option.") + str1);
/*  440 */         usage();
/*      */       } 
/*      */     } 
/*      */ 
/*      */     
/*  445 */     if (this.verbose == null) this.showcerts = false;
/*      */     
/*  447 */     if (this.jarfile == null) {
/*  448 */       System.err.println(rb.getString("Please.specify.jarfile.name"));
/*  449 */       usage();
/*      */     } 
/*  451 */     if (!this.verify && this.alias == null) {
/*  452 */       System.err.println(rb.getString("Please.specify.alias.name"));
/*  453 */       usage();
/*      */     } 
/*  455 */     if (!this.verify && this.ckaliases.size() > 1) {
/*  456 */       System.err.println(rb.getString("Only.one.alias.can.be.specified"));
/*  457 */       usage();
/*      */     } 
/*      */     
/*  460 */     if (this.storetype == null) {
/*  461 */       this.storetype = KeyStore.getDefaultType();
/*      */     }
/*  463 */     this.storetype = KeyStoreUtil.niceStoreTypeName(this.storetype);
/*      */     
/*      */     try {
/*  466 */       if (this.signedjar != null) if ((new File(this.signedjar)).getCanonicalPath().equals((new File(this.jarfile))
/*  467 */             .getCanonicalPath())) {
/*  468 */           this.signedjar = null;
/*      */         } 
/*  470 */     } catch (IOException iOException) {}
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  475 */     if ("PKCS11".equalsIgnoreCase(this.storetype) || 
/*  476 */       KeyStoreUtil.isWindowsKeyStore(this.storetype)) {
/*  477 */       this.token = true;
/*  478 */       if (this.keystore == null) {
/*  479 */         this.keystore = "NONE";
/*      */       }
/*      */     } 
/*      */     
/*  483 */     if ("NONE".equals(this.keystore)) {
/*  484 */       this.nullStream = true;
/*      */     }
/*      */     
/*  487 */     if (this.token && !this.nullStream) {
/*  488 */       System.err.println(MessageFormat.format(rb
/*  489 */             .getString(".keystore.must.be.NONE.if.storetype.is.{0}"), new Object[] { this.storetype }));
/*  490 */       usage();
/*      */     } 
/*      */     
/*  493 */     if (this.token && this.keypass != null) {
/*  494 */       System.err.println(MessageFormat.format(rb
/*  495 */             .getString(".keypass.can.not.be.specified.if.storetype.is.{0}"), new Object[] { this.storetype }));
/*  496 */       usage();
/*      */     } 
/*      */     
/*  499 */     if (this.protectedPath && (
/*  500 */       this.storepass != null || this.keypass != null)) {
/*  501 */       System.err.println(rb
/*  502 */           .getString("If.protected.is.specified.then.storepass.and.keypass.must.not.be.specified"));
/*  503 */       usage();
/*      */     } 
/*      */     
/*  506 */     if (KeyStoreUtil.isWindowsKeyStore(this.storetype) && (
/*  507 */       this.storepass != null || this.keypass != null)) {
/*  508 */       System.err.println(rb
/*  509 */           .getString("If.keystore.is.not.password.protected.then.storepass.and.keypass.must.not.be.specified"));
/*  510 */       usage();
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   static char[] getPass(String paramString1, String paramString2) {
/*  516 */     char[] arrayOfChar = KeyStoreUtil.getPassWithModifier(paramString1, paramString2, rb);
/*  517 */     if (arrayOfChar != null) return arrayOfChar; 
/*  518 */     usage();
/*  519 */     return null;
/*      */   }
/*      */   
/*      */   static void usageNoArg() {
/*  523 */     System.out.println(rb.getString("Option.lacks.argument"));
/*  524 */     usage();
/*      */   }
/*      */   
/*      */   static void usage() {
/*  528 */     System.out.println();
/*  529 */     System.out.println(rb.getString("Please.type.jarsigner.help.for.usage"));
/*  530 */     System.exit(1);
/*      */   }
/*      */   
/*      */   static void fullusage() {
/*  534 */     System.out.println(rb
/*  535 */         .getString("Usage.jarsigner.options.jar.file.alias"));
/*  536 */     System.out.println(rb
/*  537 */         .getString(".jarsigner.verify.options.jar.file.alias."));
/*  538 */     System.out.println();
/*  539 */     System.out.println(rb
/*  540 */         .getString(".keystore.url.keystore.location"));
/*  541 */     System.out.println();
/*  542 */     System.out.println(rb
/*  543 */         .getString(".storepass.password.password.for.keystore.integrity"));
/*  544 */     System.out.println();
/*  545 */     System.out.println(rb
/*  546 */         .getString(".storetype.type.keystore.type"));
/*  547 */     System.out.println();
/*  548 */     System.out.println(rb
/*  549 */         .getString(".keypass.password.password.for.private.key.if.different."));
/*  550 */     System.out.println();
/*  551 */     System.out.println(rb
/*  552 */         .getString(".certchain.file.name.of.alternative.certchain.file"));
/*  553 */     System.out.println();
/*  554 */     System.out.println(rb
/*  555 */         .getString(".sigfile.file.name.of.SF.DSA.file"));
/*  556 */     System.out.println();
/*  557 */     System.out.println(rb
/*  558 */         .getString(".signedjar.file.name.of.signed.JAR.file"));
/*  559 */     System.out.println();
/*  560 */     System.out.println(rb
/*  561 */         .getString(".digestalg.algorithm.name.of.digest.algorithm"));
/*  562 */     System.out.println();
/*  563 */     System.out.println(rb
/*  564 */         .getString(".sigalg.algorithm.name.of.signature.algorithm"));
/*  565 */     System.out.println();
/*  566 */     System.out.println(rb
/*  567 */         .getString(".verify.verify.a.signed.JAR.file"));
/*  568 */     System.out.println();
/*  569 */     System.out.println(rb
/*  570 */         .getString(".verbose.suboptions.verbose.output.when.signing.verifying."));
/*  571 */     System.out.println(rb
/*  572 */         .getString(".suboptions.can.be.all.grouped.or.summary"));
/*  573 */     System.out.println();
/*  574 */     System.out.println(rb
/*  575 */         .getString(".certs.display.certificates.when.verbose.and.verifying"));
/*  576 */     System.out.println();
/*  577 */     System.out.println(rb
/*  578 */         .getString(".tsa.url.location.of.the.Timestamping.Authority"));
/*  579 */     System.out.println();
/*  580 */     System.out.println(rb
/*  581 */         .getString(".tsacert.alias.public.key.certificate.for.Timestamping.Authority"));
/*  582 */     System.out.println();
/*  583 */     System.out.println(rb
/*  584 */         .getString(".tsapolicyid.tsapolicyid.for.Timestamping.Authority"));
/*  585 */     System.out.println();
/*  586 */     System.out.println(rb
/*  587 */         .getString(".tsadigestalg.algorithm.of.digest.data.in.timestamping.request"));
/*  588 */     System.out.println();
/*  589 */     System.out.println(rb
/*  590 */         .getString(".altsigner.class.class.name.of.an.alternative.signing.mechanism"));
/*  591 */     System.out.println();
/*  592 */     System.out.println(rb
/*  593 */         .getString(".altsignerpath.pathlist.location.of.an.alternative.signing.mechanism"));
/*  594 */     System.out.println();
/*  595 */     System.out.println(rb
/*  596 */         .getString(".internalsf.include.the.SF.file.inside.the.signature.block"));
/*  597 */     System.out.println();
/*  598 */     System.out.println(rb
/*  599 */         .getString(".sectionsonly.don.t.compute.hash.of.entire.manifest"));
/*  600 */     System.out.println();
/*  601 */     System.out.println(rb
/*  602 */         .getString(".protected.keystore.has.protected.authentication.path"));
/*  603 */     System.out.println();
/*  604 */     System.out.println(rb
/*  605 */         .getString(".providerName.name.provider.name"));
/*  606 */     System.out.println();
/*  607 */     System.out.println(rb
/*  608 */         .getString(".providerClass.class.name.of.cryptographic.service.provider.s"));
/*  609 */     System.out.println(rb
/*  610 */         .getString(".providerArg.arg.master.class.file.and.constructor.argument"));
/*  611 */     System.out.println();
/*  612 */     System.out.println(rb
/*  613 */         .getString(".strict.treat.warnings.as.errors"));
/*  614 */     System.out.println();
/*      */     
/*  616 */     System.exit(0);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   void verifyJar(String paramString) throws Exception {
/*  622 */     int i = 0;
/*  623 */     JarFile jarFile = null;
/*  624 */     HashMap<Object, Object> hashMap1 = new HashMap<>();
/*  625 */     HashMap<Object, Object> hashMap2 = new HashMap<>();
/*  626 */     HashMap<Object, Object> hashMap3 = new HashMap<>();
/*  627 */     HashMap<Object, Object> hashMap4 = new HashMap<>();
/*      */     
/*      */     try {
/*  630 */       jarFile = new JarFile(paramString, true);
/*  631 */       Vector<JarEntry> vector = new Vector();
/*  632 */       byte[] arrayOfByte = new byte[8192];
/*      */       
/*  634 */       Enumeration<JarEntry> enumeration = jarFile.entries();
/*  635 */       while (enumeration.hasMoreElements()) {
/*  636 */         JarEntry jarEntry = enumeration.nextElement();
/*  637 */         vector.addElement(jarEntry);
/*  638 */         try (InputStream null = jarFile.getInputStream(jarEntry)) {
/*  639 */           String str = jarEntry.getName();
/*  640 */           if (signatureRelated(str) && 
/*  641 */             SignatureFileVerifier.isBlockOrSF(str)) {
/*  642 */             String str1 = str.substring(str.lastIndexOf('/') + 1, str
/*  643 */                 .lastIndexOf('.'));
/*      */             try {
/*  645 */               if (str.endsWith(".SF")) {
/*  646 */                 Manifest manifest1 = new Manifest(inputStream);
/*  647 */                 boolean bool1 = false;
/*  648 */                 for (Object object : manifest1.getMainAttributes().keySet()) {
/*  649 */                   String str2 = object.toString();
/*  650 */                   if (str2.endsWith("-Digest-Manifest")) {
/*  651 */                     hashMap1.put(str1, str2
/*  652 */                         .substring(0, str2.length() - 16));
/*  653 */                     bool1 = true;
/*      */                     break;
/*      */                   } 
/*      */                 } 
/*  657 */                 if (!bool1) {
/*  658 */                   hashMap4.putIfAbsent(str1, 
/*  659 */                       String.format(rb
/*  660 */                         .getString("history.unparsable"), new Object[] { str }));
/*      */                 }
/*      */               } else {
/*      */                 
/*  664 */                 hashMap3.put(str1, str);
/*  665 */                 hashMap2.put(str1, new PKCS7(inputStream));
/*      */               } 
/*  667 */             } catch (IOException iOException) {
/*  668 */               hashMap4.putIfAbsent(str1, String.format(rb
/*  669 */                     .getString("history.unparsable"), new Object[] { str }));
/*      */             } 
/*      */           } else {
/*  672 */             while (inputStream.read(arrayOfByte, 0, arrayOfByte.length) != -1);
/*      */           } 
/*      */         } 
/*      */       } 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  680 */       Manifest manifest = jarFile.getManifest();
/*  681 */       boolean bool = false;
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  686 */       LinkedHashMap<Object, Object> linkedHashMap = new LinkedHashMap<>();
/*      */       
/*  688 */       if (manifest != null) {
/*  689 */         if (this.verbose != null) System.out.println(); 
/*  690 */         Enumeration<JarEntry> enumeration1 = vector.elements();
/*      */         
/*  692 */         String str = rb.getString("6SPACE");
/*      */         
/*  694 */         while (enumeration1.hasMoreElements()) {
/*  695 */           JarEntry jarEntry = enumeration1.nextElement();
/*  696 */           String str1 = jarEntry.getName();
/*      */ 
/*      */           
/*  699 */           bool = (bool || SignatureFileVerifier.isBlockOrSF(str1)) ? true : false;
/*      */           
/*  701 */           CodeSigner[] arrayOfCodeSigner = jarEntry.getCodeSigners();
/*  702 */           byte b = (arrayOfCodeSigner != null) ? 1 : 0;
/*  703 */           i |= b;
/*  704 */           this.hasUnsignedEntry |= (!jarEntry.isDirectory() && b == 0 && 
/*  705 */             !signatureRelated(str1)) ? 1 : 0;
/*      */           
/*  707 */           int j = inKeyStore(arrayOfCodeSigner);
/*      */           
/*  709 */           boolean bool1 = ((j & 0x1) != 0) ? true : false;
/*  710 */           boolean bool2 = ((j & 0x2) != 0) ? true : false;
/*      */           
/*  712 */           this.notSignedByAlias |= ((j & 0x4) != 0) ? 1 : 0;
/*  713 */           if (this.keystore != null) {
/*  714 */             this.aliasNotInStore |= (b != 0 && !bool1 && !bool2) ? 1 : 0;
/*      */           }
/*      */ 
/*      */           
/*  718 */           StringBuffer stringBuffer = null;
/*  719 */           if (this.verbose != null) {
/*  720 */             stringBuffer = new StringBuffer();
/*      */ 
/*      */ 
/*      */             
/*  724 */             boolean bool3 = (manifest.getAttributes(str1) != null || manifest.getAttributes("./" + str1) != null || manifest.getAttributes("/" + str1) != null) ? true : false;
/*  725 */             stringBuffer.append(((b != 0) ? rb
/*  726 */                 .getString("s") : rb.getString("SPACE")) + (bool3 ? rb
/*  727 */                 .getString("m") : rb.getString("SPACE")) + (bool1 ? rb
/*  728 */                 .getString("k") : rb.getString("SPACE")) + (bool2 ? rb
/*  729 */                 .getString("i") : rb.getString("SPACE")) + (((j & 0x4) != 0) ? "X" : " ") + rb
/*      */                 
/*  731 */                 .getString("SPACE"));
/*  732 */             stringBuffer.append("|");
/*      */           } 
/*      */ 
/*      */ 
/*      */           
/*  737 */           if (b != 0) {
/*  738 */             if (this.showcerts) stringBuffer.append('\n'); 
/*  739 */             for (CodeSigner codeSigner : arrayOfCodeSigner) {
/*      */ 
/*      */ 
/*      */               
/*  743 */               String str2 = signerInfo(codeSigner, str);
/*  744 */               if (this.showcerts) {
/*  745 */                 stringBuffer.append(str2);
/*  746 */                 stringBuffer.append('\n');
/*      */               } 
/*      */             } 
/*  749 */           } else if (this.showcerts && !this.verbose.equals("all")) {
/*      */ 
/*      */             
/*  752 */             if (signatureRelated(str1)) {
/*  753 */               stringBuffer.append("\n" + str + rb.getString(".Signature.related.entries.") + "\n\n");
/*      */             } else {
/*      */               
/*  756 */               stringBuffer.append("\n" + str + rb.getString(".Unsigned.entries.") + "\n\n");
/*      */             } 
/*      */           } 
/*      */ 
/*      */           
/*  761 */           if (this.verbose != null) {
/*  762 */             String str2 = stringBuffer.toString();
/*  763 */             if (signatureRelated(str1))
/*      */             {
/*      */               
/*  766 */               str2 = "-" + str2;
/*      */             }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */             
/*  773 */             if (!linkedHashMap.containsKey(str2)) {
/*  774 */               linkedHashMap.put(str2, new ArrayList());
/*      */             }
/*      */             
/*  777 */             StringBuffer stringBuffer1 = new StringBuffer();
/*  778 */             String str3 = Long.toString(jarEntry.getSize());
/*  779 */             for (int k = 6 - str3.length(); k > 0; k--) {
/*  780 */               stringBuffer1.append(' ');
/*      */             }
/*  782 */             stringBuffer1.append(str3).append(' ')
/*  783 */               .append((new Date(jarEntry.getTime())).toString());
/*  784 */             stringBuffer1.append(' ').append(str1);
/*      */             
/*  786 */             ((List<String>)linkedHashMap.get(str2)).add(stringBuffer1.toString());
/*      */           } 
/*      */         } 
/*      */       } 
/*  790 */       if (this.verbose != null) {
/*  791 */         for (Map.Entry<Object, Object> entry : linkedHashMap.entrySet()) {
/*  792 */           List<String> list = (List)entry.getValue();
/*  793 */           String str = (String)entry.getKey();
/*  794 */           if (str.charAt(0) == '-') {
/*  795 */             str = str.substring(1);
/*      */           }
/*  797 */           int j = str.indexOf('|');
/*  798 */           if (this.verbose.equals("all")) {
/*  799 */             for (String str1 : list) {
/*  800 */               System.out.println(str.substring(0, j) + str1);
/*  801 */               System.out.printf(str.substring(j + 1), new Object[0]);
/*      */             }  continue;
/*      */           } 
/*  804 */           if (this.verbose.equals("grouped")) {
/*  805 */             for (String str1 : list) {
/*  806 */               System.out.println(str.substring(0, j) + str1);
/*      */             }
/*  808 */           } else if (this.verbose.equals("summary")) {
/*  809 */             System.out.print(str.substring(0, j));
/*  810 */             if (list.size() > 1) {
/*  811 */               System.out.println((String)list.get(0) + " " + 
/*  812 */                   String.format(rb.getString(".and.d.more."), new Object[] {
/*  813 */                       Integer.valueOf(list.size() - 1) }));
/*      */             } else {
/*  815 */               System.out.println(list.get(0));
/*      */             } 
/*      */           } 
/*  818 */           System.out.printf(str.substring(j + 1), new Object[0]);
/*      */         } 
/*      */         
/*  821 */         System.out.println();
/*  822 */         System.out.println(rb.getString(".s.signature.was.verified."));
/*      */         
/*  824 */         System.out.println(rb.getString(".m.entry.is.listed.in.manifest"));
/*      */         
/*  826 */         System.out.println(rb.getString(".k.at.least.one.certificate.was.found.in.keystore"));
/*      */         
/*  828 */         System.out.println(rb.getString(".i.at.least.one.certificate.was.found.in.identity.scope"));
/*      */         
/*  830 */         if (this.ckaliases.size() > 0) {
/*  831 */           System.out.println(rb.getString(".X.not.signed.by.specified.alias.es."));
/*      */         }
/*      */       } 
/*      */       
/*  835 */       if (manifest == null) {
/*  836 */         System.out.println();
/*  837 */         System.out.println(rb.getString("no.manifest."));
/*      */       } 
/*      */ 
/*      */ 
/*      */       
/*  842 */       if (!hashMap1.isEmpty() || 
/*  843 */         !hashMap2.isEmpty() || 
/*  844 */         !hashMap4.isEmpty()) {
/*  845 */         if (this.verbose != null) {
/*  846 */           System.out.println();
/*      */         }
/*  848 */         for (String str : hashMap2.keySet()) {
/*  849 */           if (!hashMap1.containsKey(str)) {
/*  850 */             hashMap4.putIfAbsent(str, String.format(rb
/*  851 */                   .getString("history.nosf"), new Object[] { str }));
/*      */           }
/*      */         } 
/*  854 */         for (String str : hashMap1.keySet()) {
/*  855 */           PKCS7 pKCS7 = (PKCS7)hashMap2.get(str);
/*  856 */           if (pKCS7 != null) {
/*      */             String str1;
/*      */             try {
/*  859 */               SignerInfo signerInfo = pKCS7.getSignerInfos()[0];
/*  860 */               X509Certificate x509Certificate = signerInfo.getCertificate(pKCS7);
/*  861 */               String str2 = (String)hashMap1.get(str);
/*  862 */               String str3 = AlgorithmId.makeSigAlg(signerInfo
/*  863 */                   .getDigestAlgorithmId().getName(), signerInfo
/*  864 */                   .getDigestEncryptionAlgorithmId().getName());
/*  865 */               PublicKey publicKey = x509Certificate.getPublicKey();
/*  866 */               PKCS7 pKCS71 = signerInfo.getTsToken();
/*  867 */               if (pKCS71 != null) {
/*  868 */                 this.hasTimestampBlock = true;
/*  869 */                 SignerInfo signerInfo1 = pKCS71.getSignerInfos()[0];
/*  870 */                 X509Certificate x509Certificate1 = signerInfo1.getCertificate(pKCS71);
/*  871 */                 byte[] arrayOfByte1 = pKCS71.getContentInfo().getData();
/*  872 */                 TimestampToken timestampToken = new TimestampToken(arrayOfByte1);
/*  873 */                 PublicKey publicKey1 = x509Certificate1.getPublicKey();
/*  874 */                 String str4 = timestampToken.getHashAlgorithm().getName();
/*  875 */                 String str5 = AlgorithmId.makeSigAlg(signerInfo1
/*  876 */                     .getDigestAlgorithmId().getName(), signerInfo1
/*  877 */                     .getDigestEncryptionAlgorithmId().getName());
/*  878 */                 Calendar calendar = Calendar.getInstance(
/*  879 */                     TimeZone.getTimeZone("UTC"), 
/*  880 */                     Locale.getDefault(Locale.Category.FORMAT));
/*  881 */                 calendar.setTime(timestampToken.getDate());
/*  882 */                 str1 = String.format(rb
/*  883 */                     .getString("history.with.ts"), new Object[] { x509Certificate
/*  884 */                       .getSubjectX500Principal(), 
/*  885 */                       withWeak(str2, DIGEST_PRIMITIVE_SET), 
/*  886 */                       withWeak(str3, SIG_PRIMITIVE_SET), 
/*  887 */                       withWeak(publicKey), calendar, x509Certificate1
/*      */                       
/*  889 */                       .getSubjectX500Principal(), 
/*  890 */                       withWeak(str4, DIGEST_PRIMITIVE_SET), 
/*  891 */                       withWeak(str5, SIG_PRIMITIVE_SET), 
/*  892 */                       withWeak(publicKey1) });
/*      */               } else {
/*  894 */                 str1 = String.format(rb
/*  895 */                     .getString("history.without.ts"), new Object[] { x509Certificate
/*  896 */                       .getSubjectX500Principal(), 
/*  897 */                       withWeak(str2, DIGEST_PRIMITIVE_SET), 
/*  898 */                       withWeak(str3, SIG_PRIMITIVE_SET), 
/*  899 */                       withWeak(publicKey) });
/*      */               } 
/*  901 */             } catch (Exception exception) {
/*      */ 
/*      */               
/*  904 */               str1 = String.format(rb
/*  905 */                   .getString("history.unparsable"), new Object[] { hashMap3
/*  906 */                     .get(str) });
/*      */             } 
/*  908 */             if (this.verbose != null)
/*  909 */               System.out.println(str1); 
/*      */             continue;
/*      */           } 
/*  912 */           hashMap4.putIfAbsent(str, String.format(rb
/*  913 */                 .getString("history.nobk"), new Object[] { str }));
/*      */         } 
/*      */         
/*  916 */         if (this.verbose != null) {
/*  917 */           for (String str : hashMap4.keySet()) {
/*  918 */             System.out.println((String)hashMap4.get(str));
/*      */           }
/*      */         }
/*      */       } 
/*  922 */       System.out.println();
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  927 */       if (!this.aliasNotInStore && this.keystore != null) {
/*  928 */         this.signerSelfSigned = false;
/*      */       }
/*      */       
/*  931 */       if (i == 0) {
/*  932 */         if (this.seeWeak) {
/*  933 */           if (this.verbose != null) {
/*  934 */             System.out.println(rb.getString("jar.treated.unsigned.see.weak.verbose"));
/*  935 */             System.out.println("\n  jdk.jar.disabledAlgorithms=" + 
/*      */                 
/*  937 */                 Security.getProperty("jdk.jar.disabledAlgorithms"));
/*      */           } else {
/*  939 */             System.out.println(rb.getString("jar.treated.unsigned.see.weak"));
/*      */           } 
/*  941 */         } else if (bool) {
/*  942 */           System.out.println(rb.getString("jar.treated.unsigned"));
/*      */         } else {
/*  944 */           System.out.println(rb.getString("jar.is.unsigned"));
/*      */         } 
/*      */       } else {
/*  947 */         displayMessagesAndResult(false);
/*      */       } 
/*      */       return;
/*  950 */     } catch (Exception exception) {
/*  951 */       System.out.println(rb.getString("jarsigner.") + exception);
/*  952 */       if (this.debug) {
/*  953 */         exception.printStackTrace();
/*      */       }
/*      */     } finally {
/*  956 */       if (jarFile != null) {
/*  957 */         jarFile.close();
/*      */       }
/*      */     } 
/*      */     
/*  961 */     System.exit(1);
/*      */   }
/*      */   
/*      */   private void displayMessagesAndResult(boolean paramBoolean) {
/*      */     String str;
/*  966 */     ArrayList<String> arrayList1 = new ArrayList();
/*  967 */     ArrayList<String> arrayList2 = new ArrayList();
/*  968 */     ArrayList<String> arrayList3 = new ArrayList();
/*      */ 
/*      */     
/*  971 */     boolean bool = (this.expireDate == null || this.expireDate.after(new Date())) ? true : false;
/*      */     
/*  973 */     if (this.badKeyUsage || this.badExtendedKeyUsage || this.badNetscapeCertType || this.notYetValidCert || this.chainNotValidated || this.hasExpiredCert || this.hasUnsignedEntry || this.signerSelfSigned || this.weakAlg != 0 || this.aliasNotInStore || this.notSignedByAlias || this.tsaChainNotValidated || (this.hasExpiredTsaCert && !bool)) {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  980 */       if (this.strict) {
/*  981 */         str = rb.getString(paramBoolean ? "jar.signed.with.signer.errors." : "jar.verified.with.signer.errors.");
/*      */       }
/*      */       else {
/*      */         
/*  985 */         str = rb.getString(paramBoolean ? "jar.signed." : "jar.verified.");
/*      */       } 
/*      */ 
/*      */ 
/*      */       
/*  990 */       if (this.badKeyUsage) {
/*  991 */         arrayList1.add(rb.getString(paramBoolean ? "The.signer.certificate.s.KeyUsage.extension.doesn.t.allow.code.signing." : "This.jar.contains.entries.whose.signer.certificate.s.KeyUsage.extension.doesn.t.allow.code.signing."));
/*      */       }
/*      */ 
/*      */ 
/*      */       
/*  996 */       if (this.badExtendedKeyUsage) {
/*  997 */         arrayList1.add(rb.getString(paramBoolean ? "The.signer.certificate.s.ExtendedKeyUsage.extension.doesn.t.allow.code.signing." : "This.jar.contains.entries.whose.signer.certificate.s.ExtendedKeyUsage.extension.doesn.t.allow.code.signing."));
/*      */       }
/*      */ 
/*      */ 
/*      */       
/* 1002 */       if (this.badNetscapeCertType) {
/* 1003 */         arrayList1.add(rb.getString(paramBoolean ? "The.signer.certificate.s.NetscapeCertType.extension.doesn.t.allow.code.signing." : "This.jar.contains.entries.whose.signer.certificate.s.NetscapeCertType.extension.doesn.t.allow.code.signing."));
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1009 */       if (this.hasUnsignedEntry) {
/* 1010 */         arrayList1.add(rb.getString("This.jar.contains.unsigned.entries.which.have.not.been.integrity.checked."));
/*      */       }
/*      */       
/* 1013 */       if (this.hasExpiredCert) {
/* 1014 */         arrayList1.add(rb.getString(paramBoolean ? "The.signer.certificate.has.expired." : "This.jar.contains.entries.whose.signer.certificate.has.expired."));
/*      */       }
/*      */ 
/*      */       
/* 1018 */       if (this.notYetValidCert) {
/* 1019 */         arrayList1.add(rb.getString(paramBoolean ? "The.signer.certificate.is.not.yet.valid." : "This.jar.contains.entries.whose.signer.certificate.is.not.yet.valid."));
/*      */       }
/*      */ 
/*      */ 
/*      */       
/* 1024 */       if (this.chainNotValidated) {
/* 1025 */         arrayList1.add(String.format(rb.getString(paramBoolean ? "The.signer.s.certificate.chain.is.invalid.reason.1" : "This.jar.contains.entries.whose.certificate.chain.is.invalid.reason.1"), new Object[] { this.chainNotValidatedReason
/*      */ 
/*      */                 
/* 1028 */                 .getLocalizedMessage() }));
/*      */       }
/*      */       
/* 1031 */       if (this.hasExpiredTsaCert) {
/* 1032 */         arrayList1.add(rb.getString("The.timestamp.has.expired."));
/*      */       }
/* 1034 */       if (this.tsaChainNotValidated) {
/* 1035 */         arrayList1.add(String.format(rb.getString(paramBoolean ? "The.tsa.certificate.chain.is.invalid.reason.1" : "This.jar.contains.entries.whose.tsa.certificate.chain.is.invalid.reason.1"), new Object[] { this.tsaChainNotValidatedReason
/*      */ 
/*      */                 
/* 1038 */                 .getLocalizedMessage() }));
/*      */       }
/*      */ 
/*      */       
/* 1042 */       if (this.notSignedByAlias) {
/* 1043 */         arrayList1.add(rb
/* 1044 */             .getString("This.jar.contains.signed.entries.which.is.not.signed.by.the.specified.alias.es."));
/*      */       }
/*      */ 
/*      */       
/* 1048 */       if (this.aliasNotInStore) {
/* 1049 */         arrayList1.add(rb.getString("This.jar.contains.signed.entries.that.s.not.signed.by.alias.in.this.keystore."));
/*      */       }
/*      */       
/* 1052 */       if (this.signerSelfSigned) {
/* 1053 */         arrayList1.add(rb.getString(paramBoolean ? "The.signer.s.certificate.is.self.signed." : "This.jar.contains.entries.whose.signer.certificate.is.self.signed."));
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1060 */       if ((this.weakAlg & 0x1) == 1) {
/* 1061 */         arrayList1.add(String.format(rb
/* 1062 */               .getString("The.1.algorithm.specified.for.the.2.option.is.considered.a.security.risk."), new Object[] { this.digestalg, "-digestalg" }));
/*      */       }
/*      */ 
/*      */       
/* 1066 */       if ((this.weakAlg & 0x2) == 2) {
/* 1067 */         arrayList1.add(String.format(rb
/* 1068 */               .getString("The.1.algorithm.specified.for.the.2.option.is.considered.a.security.risk."), new Object[] { this.sigalg, "-sigalg" }));
/*      */       }
/*      */       
/* 1071 */       if ((this.weakAlg & 0x4) == 4) {
/* 1072 */         arrayList1.add(String.format(rb
/* 1073 */               .getString("The.1.algorithm.specified.for.the.2.option.is.considered.a.security.risk."), new Object[] { this.tSADigestAlg, "-tsadigestalg" }));
/*      */       }
/*      */       
/* 1076 */       if ((this.weakAlg & 0x8) == 8) {
/* 1077 */         arrayList1.add(String.format(rb
/* 1078 */               .getString("The.1.signing.key.has.a.keysize.of.2.which.is.considered.a.security.risk."), new Object[] { this.privateKey
/* 1079 */                 .getAlgorithm(), Integer.valueOf(KeyUtil.getKeySize(this.privateKey)) }));
/*      */       }
/*      */     } else {
/* 1082 */       str = rb.getString(paramBoolean ? "jar.signed." : "jar.verified.");
/*      */     } 
/*      */     
/* 1085 */     if (this.hasExpiredTsaCert)
/*      */     {
/* 1087 */       this.hasExpiringTsaCert = false;
/*      */     }
/*      */     
/* 1090 */     if (this.hasExpiringCert || (this.hasExpiringTsaCert && this.expireDate != null) || (this.noTimestamp && this.expireDate != null) || (this.hasExpiredTsaCert && bool)) {
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1095 */       if (this.hasExpiredTsaCert && bool) {
/* 1096 */         if (this.expireDate != null) {
/* 1097 */           arrayList2.add(String.format(rb
/* 1098 */                 .getString("The.timestamp.expired.1.but.usable.2"), new Object[] { this.tsaExpireDate, this.expireDate }));
/*      */         }
/*      */ 
/*      */ 
/*      */         
/* 1103 */         this.hasExpiredTsaCert = false;
/*      */       } 
/* 1105 */       if (this.hasExpiringCert) {
/* 1106 */         arrayList2.add(rb.getString(paramBoolean ? "The.signer.certificate.will.expire.within.six.months." : "This.jar.contains.entries.whose.signer.certificate.will.expire.within.six.months."));
/*      */       }
/*      */ 
/*      */       
/* 1110 */       if (this.hasExpiringTsaCert && this.expireDate != null) {
/* 1111 */         if (this.expireDate.after(this.tsaExpireDate)) {
/* 1112 */           arrayList2.add(String.format(rb.getString("The.timestamp.will.expire.within.one.year.on.1.but.2"), new Object[] { this.tsaExpireDate, this.expireDate }));
/*      */         } else {
/*      */           
/* 1115 */           arrayList2.add(String.format(rb.getString("The.timestamp.will.expire.within.one.year.on.1"), new Object[] { this.tsaExpireDate }));
/*      */         } 
/*      */       }
/*      */       
/* 1119 */       if (this.noTimestamp && this.expireDate != null) {
/* 1120 */         if (this.hasTimestampBlock) {
/* 1121 */           arrayList2.add(String.format(rb.getString(paramBoolean ? "invalid.timestamp.signing" : "bad.timestamp.verifying"), new Object[] { this.expireDate }));
/*      */         }
/*      */         else {
/*      */           
/* 1125 */           arrayList2.add(String.format(rb.getString(paramBoolean ? "no.timestamp.signing" : "no.timestamp.verifying"), new Object[] { this.expireDate }));
/*      */         } 
/*      */       }
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/* 1132 */     System.out.println(str);
/* 1133 */     if (this.strict) {
/* 1134 */       if (!arrayList1.isEmpty()) {
/* 1135 */         System.out.println();
/* 1136 */         System.out.println(rb.getString("Error."));
/* 1137 */         arrayList1.forEach(System.out::println);
/*      */       } 
/* 1139 */       if (!arrayList2.isEmpty()) {
/* 1140 */         System.out.println();
/* 1141 */         System.out.println(rb.getString("Warning."));
/* 1142 */         arrayList2.forEach(System.out::println);
/*      */       }
/*      */     
/* 1145 */     } else if (!arrayList1.isEmpty() || !arrayList2.isEmpty()) {
/* 1146 */       System.out.println();
/* 1147 */       System.out.println(rb.getString("Warning."));
/* 1148 */       arrayList1.forEach(System.out::println);
/* 1149 */       arrayList2.forEach(System.out::println);
/*      */     } 
/*      */     
/* 1152 */     if (!paramBoolean && (!arrayList1.isEmpty() || !arrayList2.isEmpty()) && (
/* 1153 */       this.verbose == null || !this.showcerts)) {
/* 1154 */       System.out.println();
/* 1155 */       System.out.println(rb.getString("Re.run.with.the.verbose.and.certs.options.for.more.details."));
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/* 1160 */     if (paramBoolean || this.verbose != null) {
/*      */       
/* 1162 */       if (!this.hasExpiringCert && !this.hasExpiredCert && this.expireDate != null && bool)
/*      */       {
/* 1164 */         arrayList3.add(String.format(rb.getString("The.signer.certificate.will.expire.on.1."), new Object[] { this.expireDate }));
/*      */       }
/*      */       
/* 1167 */       if (!this.noTimestamp && 
/* 1168 */         !this.hasExpiringTsaCert && !this.hasExpiredTsaCert && this.tsaExpireDate != null) {
/* 1169 */         if (bool) {
/* 1170 */           arrayList3.add(String.format(rb.getString("The.timestamp.will.expire.on.1."), new Object[] { this.tsaExpireDate }));
/*      */         } else {
/*      */           
/* 1173 */           arrayList3.add(String.format(rb.getString("signer.cert.expired.1.but.timestamp.good.2."), new Object[] { this.expireDate, this.tsaExpireDate }));
/*      */         } 
/*      */       }
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1182 */     if (!arrayList3.isEmpty()) {
/* 1183 */       System.out.println();
/* 1184 */       arrayList3.forEach(System.out::println);
/*      */     } 
/*      */   }
/*      */   
/*      */   private String withWeak(String paramString, Set<CryptoPrimitive> paramSet) {
/* 1189 */     if (DISABLED_CHECK.permits(paramSet, paramString, null)) {
/* 1190 */       return paramString;
/*      */     }
/* 1192 */     this.seeWeak = true;
/* 1193 */     return String.format(rb.getString("with.weak"), new Object[] { paramString });
/*      */   }
/*      */ 
/*      */   
/*      */   private String withWeak(PublicKey paramPublicKey) {
/* 1198 */     if (DISABLED_CHECK.permits(SIG_PRIMITIVE_SET, paramPublicKey)) {
/* 1199 */       return String.format(rb
/* 1200 */           .getString("key.bit"), new Object[] { Integer.valueOf(KeyUtil.getKeySize(paramPublicKey)) });
/*      */     }
/* 1202 */     this.seeWeak = true;
/* 1203 */     return String.format(rb
/* 1204 */         .getString("key.bit.weak"), new Object[] { Integer.valueOf(KeyUtil.getKeySize(paramPublicKey)) });
/*      */   }
/*      */ 
/*      */   
/* 1208 */   private static MessageFormat validityTimeForm = null;
/* 1209 */   private static MessageFormat notYetTimeForm = null;
/* 1210 */   private static MessageFormat expiredTimeForm = null;
/* 1211 */   private static MessageFormat expiringTimeForm = null;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   String printCert(boolean paramBoolean1, String paramString, Certificate paramCertificate, Date paramDate, boolean paramBoolean2) throws Exception {
/* 1232 */     StringBuilder stringBuilder = new StringBuilder();
/* 1233 */     String str1 = rb.getString("SPACE");
/* 1234 */     X509Certificate x509Certificate = null;
/*      */     
/* 1236 */     if (paramCertificate instanceof X509Certificate) {
/* 1237 */       x509Certificate = (X509Certificate)paramCertificate;
/* 1238 */       stringBuilder.append(paramString).append(x509Certificate.getType())
/* 1239 */         .append(rb.getString("COMMA"))
/* 1240 */         .append(x509Certificate.getSubjectDN().getName());
/*      */     } else {
/* 1242 */       stringBuilder.append(paramString).append(paramCertificate.getType());
/*      */     } 
/*      */     
/* 1245 */     String str2 = this.storeHash.get(paramCertificate);
/* 1246 */     if (str2 != null) {
/* 1247 */       stringBuilder.append(str1).append(str2);
/*      */     }
/*      */     
/* 1250 */     if (x509Certificate != null) {
/*      */       
/* 1252 */       stringBuilder.append("\n").append(paramString).append("[");
/*      */       
/* 1254 */       if (this.trustedCerts.contains(x509Certificate)) {
/* 1255 */         stringBuilder.append(rb.getString("trusted.certificate"));
/*      */       } else {
/* 1257 */         Date date = x509Certificate.getNotAfter();
/*      */         try {
/* 1259 */           boolean bool = true;
/* 1260 */           if (paramBoolean1) {
/* 1261 */             if (this.tsaExpireDate == null || this.tsaExpireDate.after(date)) {
/* 1262 */               this.tsaExpireDate = date;
/*      */             }
/*      */           }
/* 1265 */           else if (this.expireDate == null || this.expireDate.after(date)) {
/* 1266 */             this.expireDate = date;
/*      */           } 
/*      */           
/* 1269 */           if (paramDate == null) {
/* 1270 */             x509Certificate.checkValidity();
/*      */             
/* 1272 */             long l = paramBoolean1 ? 31622400000L : 15552000000L;
/* 1273 */             if (date.getTime() < System.currentTimeMillis() + l) {
/* 1274 */               if (paramBoolean1) {
/* 1275 */                 this.hasExpiringTsaCert = true;
/*      */               } else {
/* 1277 */                 this.hasExpiringCert = true;
/*      */               } 
/* 1279 */               if (expiringTimeForm == null)
/*      */               {
/* 1281 */                 expiringTimeForm = new MessageFormat(rb.getString("certificate.will.expire.on"));
/*      */               }
/* 1283 */               Object[] arrayOfObject = { date };
/* 1284 */               stringBuilder.append(expiringTimeForm.format(arrayOfObject));
/* 1285 */               bool = false;
/*      */             } 
/*      */           } else {
/* 1288 */             x509Certificate.checkValidity(paramDate);
/*      */           } 
/* 1290 */           if (bool) {
/* 1291 */             if (validityTimeForm == null)
/*      */             {
/* 1293 */               validityTimeForm = new MessageFormat(rb.getString("certificate.is.valid.from"));
/*      */             }
/* 1295 */             Object[] arrayOfObject = { x509Certificate.getNotBefore(), date };
/* 1296 */             stringBuilder.append(validityTimeForm.format(arrayOfObject));
/*      */           } 
/* 1298 */         } catch (CertificateExpiredException certificateExpiredException) {
/* 1299 */           if (paramBoolean1) {
/* 1300 */             this.hasExpiredTsaCert = true;
/*      */           } else {
/* 1302 */             this.hasExpiredCert = true;
/*      */           } 
/*      */           
/* 1305 */           if (expiredTimeForm == null)
/*      */           {
/* 1307 */             expiredTimeForm = new MessageFormat(rb.getString("certificate.expired.on"));
/*      */           }
/* 1309 */           Object[] arrayOfObject = { date };
/* 1310 */           stringBuilder.append(expiredTimeForm.format(arrayOfObject));
/*      */         }
/* 1312 */         catch (CertificateNotYetValidException certificateNotYetValidException) {
/* 1313 */           if (!paramBoolean1) this.notYetValidCert = true;
/*      */           
/* 1315 */           if (notYetTimeForm == null)
/*      */           {
/* 1317 */             notYetTimeForm = new MessageFormat(rb.getString("certificate.is.not.valid.until"));
/*      */           }
/* 1319 */           Object[] arrayOfObject = { x509Certificate.getNotBefore() };
/* 1320 */           stringBuilder.append(notYetTimeForm.format(arrayOfObject));
/*      */         } 
/*      */       } 
/* 1323 */       stringBuilder.append("]");
/*      */       
/* 1325 */       if (paramBoolean2) {
/* 1326 */         boolean[] arrayOfBoolean = new boolean[3];
/* 1327 */         checkCertUsage(x509Certificate, arrayOfBoolean);
/* 1328 */         if (arrayOfBoolean[0] || arrayOfBoolean[1] || arrayOfBoolean[2]) {
/* 1329 */           String str = "";
/* 1330 */           if (arrayOfBoolean[0]) {
/* 1331 */             str = "KeyUsage";
/*      */           }
/* 1333 */           if (arrayOfBoolean[1]) {
/* 1334 */             if (str.length() > 0) str = str + ", "; 
/* 1335 */             str = str + "ExtendedKeyUsage";
/*      */           } 
/* 1337 */           if (arrayOfBoolean[2]) {
/* 1338 */             if (str.length() > 0) str = str + ", "; 
/* 1339 */             str = str + "NetscapeCertType";
/*      */           } 
/* 1341 */           stringBuilder.append("\n").append(paramString)
/* 1342 */             .append(MessageFormat.format(rb.getString(".{0}.extension.does.not.support.code.signing."), new Object[] { str }));
/*      */         } 
/*      */       } 
/*      */     } 
/*      */     
/* 1347 */     return stringBuilder.toString();
/*      */   }
/*      */   
/* 1350 */   private static MessageFormat signTimeForm = null;
/*      */ 
/*      */   
/*      */   private String printTimestamp(String paramString, Timestamp paramTimestamp) {
/* 1354 */     if (signTimeForm == null)
/*      */     {
/* 1356 */       signTimeForm = new MessageFormat(rb.getString("entry.was.signed.on"));
/*      */     }
/* 1358 */     Object[] arrayOfObject = { paramTimestamp.getTimestamp() };
/*      */     
/* 1360 */     return paramString + "[" + signTimeForm
/* 1361 */       .format(arrayOfObject) + "]";
/*      */   }
/*      */   
/* 1364 */   private Map<CodeSigner, Integer> cacheForInKS = new IdentityHashMap<>();
/*      */   
/*      */   private int inKeyStoreForOneSigner(CodeSigner paramCodeSigner) {
/* 1367 */     if (this.cacheForInKS.containsKey(paramCodeSigner)) {
/* 1368 */       return ((Integer)this.cacheForInKS.get(paramCodeSigner)).intValue();
/*      */     }
/*      */     
/* 1371 */     boolean bool = false;
/* 1372 */     int i = 0;
/* 1373 */     List<? extends Certificate> list = paramCodeSigner.getSignerCertPath().getCertificates();
/* 1374 */     for (Certificate certificate : list) {
/* 1375 */       String str = this.storeHash.get(certificate);
/* 1376 */       if (str != null) {
/* 1377 */         if (str.startsWith("(")) {
/* 1378 */           i |= 0x1;
/* 1379 */         } else if (str.startsWith("[")) {
/* 1380 */           i |= 0x2;
/*      */         } 
/* 1382 */         if (this.ckaliases.contains(str.substring(1, str.length() - 1)))
/* 1383 */           i |= 0x8; 
/*      */         continue;
/*      */       } 
/* 1386 */       if (this.store != null) {
/*      */         try {
/* 1388 */           str = this.store.getCertificateAlias(certificate);
/* 1389 */         } catch (KeyStoreException keyStoreException) {}
/*      */ 
/*      */         
/* 1392 */         if (str != null) {
/* 1393 */           this.storeHash.put(certificate, "(" + str + ")");
/* 1394 */           bool = true;
/* 1395 */           i |= 0x1;
/*      */         } 
/*      */       } 
/* 1398 */       if (this.ckaliases.contains(str)) {
/* 1399 */         i |= 0x8;
/*      */       }
/*      */     } 
/*      */     
/* 1403 */     this.cacheForInKS.put(paramCodeSigner, Integer.valueOf(i));
/* 1404 */     return i;
/*      */   }
/*      */   
/* 1407 */   Hashtable<Certificate, String> storeHash = new Hashtable<>();
/*      */ 
/*      */   
/*      */   int inKeyStore(CodeSigner[] paramArrayOfCodeSigner) {
/* 1411 */     if (paramArrayOfCodeSigner == null) {
/* 1412 */       return 0;
/*      */     }
/* 1414 */     int i = 0;
/*      */     
/* 1416 */     for (CodeSigner codeSigner : paramArrayOfCodeSigner) {
/* 1417 */       int j = inKeyStoreForOneSigner(codeSigner);
/* 1418 */       i |= j;
/*      */     } 
/* 1420 */     if (this.ckaliases.size() > 0 && (i & 0x8) == 0) {
/* 1421 */       i |= 0x4;
/*      */     }
/* 1423 */     return i;
/*      */   }
/*      */ 
/*      */   
/*      */   void signJar(String paramString1, String paramString2, String[] paramArrayOfString) throws Exception {
/*      */     String str1;
/* 1429 */     DisabledAlgorithmConstraints disabledAlgorithmConstraints = new DisabledAlgorithmConstraints("jdk.certpath.disabledAlgorithms");
/*      */ 
/*      */ 
/*      */     
/* 1433 */     if (this.digestalg != null && !disabledAlgorithmConstraints.permits(
/* 1434 */         Collections.singleton(CryptoPrimitive.MESSAGE_DIGEST), this.digestalg, null)) {
/* 1435 */       this.weakAlg |= 0x1;
/*      */     }
/* 1437 */     if (this.tSADigestAlg != null && !disabledAlgorithmConstraints.permits(
/* 1438 */         Collections.singleton(CryptoPrimitive.MESSAGE_DIGEST), this.tSADigestAlg, null)) {
/* 1439 */       this.weakAlg |= 0x4;
/*      */     }
/* 1441 */     if (this.sigalg != null && !disabledAlgorithmConstraints.permits(
/* 1442 */         Collections.singleton(CryptoPrimitive.SIGNATURE), this.sigalg, null)) {
/* 1443 */       this.weakAlg |= 0x2;
/*      */     }
/*      */     
/* 1446 */     boolean bool1 = false;
/* 1447 */     X509Certificate x509Certificate = null;
/*      */     
/* 1449 */     if (this.sigfile == null) {
/* 1450 */       this.sigfile = paramString2;
/* 1451 */       bool1 = true;
/*      */     } 
/*      */     
/* 1454 */     if (this.sigfile.length() > 8) {
/* 1455 */       this.sigfile = this.sigfile.substring(0, 8).toUpperCase(Locale.ENGLISH);
/*      */     } else {
/* 1457 */       this.sigfile = this.sigfile.toUpperCase(Locale.ENGLISH);
/*      */     } 
/*      */     
/* 1460 */     StringBuilder stringBuilder = new StringBuilder(this.sigfile.length());
/* 1461 */     for (byte b = 0; b < this.sigfile.length(); b++) {
/* 1462 */       char c = this.sigfile.charAt(b);
/* 1463 */       if ((c < 'A' || c > 'Z') && (c < '0' || c > '9') && c != '-' && c != '_')
/*      */       {
/*      */ 
/*      */ 
/*      */         
/* 1468 */         if (bool1) {
/*      */           
/* 1470 */           c = '_';
/*      */         } else {
/* 1472 */           throw new RuntimeException(rb
/*      */               
/* 1474 */               .getString("signature.filename.must.consist.of.the.following.characters.A.Z.0.9.or."));
/*      */         } 
/*      */       }
/* 1477 */       stringBuilder.append(c);
/*      */     } 
/*      */     
/* 1480 */     this.sigfile = stringBuilder.toString();
/*      */ 
/*      */     
/* 1483 */     if (this.signedjar == null) { str1 = paramString1 + ".sig"; }
/* 1484 */     else { str1 = this.signedjar; }
/*      */     
/* 1486 */     File file1 = new File(paramString1);
/* 1487 */     File file2 = new File(str1);
/*      */ 
/*      */     
/*      */     try {
/* 1491 */       this.zipFile = new ZipFile(paramString1);
/* 1492 */     } catch (IOException iOException) {
/* 1493 */       error(rb.getString("unable.to.open.jar.file.") + paramString1, iOException);
/*      */     } 
/*      */     
/* 1496 */     FileOutputStream fileOutputStream = null;
/*      */     try {
/* 1498 */       fileOutputStream = new FileOutputStream(file2);
/* 1499 */     } catch (IOException iOException) {
/* 1500 */       error(rb.getString("unable.to.create.") + str1, iOException);
/*      */     } 
/*      */     
/* 1503 */     PrintStream printStream = new PrintStream(fileOutputStream);
/* 1504 */     ZipOutputStream zipOutputStream = new ZipOutputStream(printStream);
/*      */ 
/*      */     
/* 1507 */     String str2 = ("META-INF/" + this.sigfile + ".SF").toUpperCase(Locale.ENGLISH);
/* 1508 */     String str3 = ("META-INF/" + this.sigfile + ".DSA").toUpperCase(Locale.ENGLISH);
/*      */     
/* 1510 */     Manifest manifest = new Manifest();
/* 1511 */     Map<String, Attributes> map = manifest.getEntries();
/*      */ 
/*      */     
/* 1514 */     Attributes attributes = null;
/*      */     
/* 1516 */     boolean bool2 = false;
/* 1517 */     boolean bool3 = false;
/* 1518 */     byte[] arrayOfByte = null;
/*      */     
/*      */     try {
/* 1521 */       MessageDigest[] arrayOfMessageDigest = { MessageDigest.getInstance(this.digestalg) };
/*      */       
/*      */       ZipEntry zipEntry1;
/*      */       
/* 1525 */       if ((zipEntry1 = getManifestFile(this.zipFile)) != null) {
/*      */         
/* 1527 */         arrayOfByte = getBytes(this.zipFile, zipEntry1);
/* 1528 */         manifest.read(new ByteArrayInputStream(arrayOfByte));
/* 1529 */         attributes = (Attributes)manifest.getMainAttributes().clone();
/*      */       } else {
/*      */         
/* 1532 */         Attributes attributes1 = manifest.getMainAttributes();
/* 1533 */         attributes1.putValue(Attributes.Name.MANIFEST_VERSION.toString(), "1.0");
/*      */         
/* 1535 */         String str4 = System.getProperty("java.vendor");
/* 1536 */         String str5 = System.getProperty("java.version");
/* 1537 */         attributes1.putValue("Created-By", str5 + " (" + str4 + ")");
/*      */         
/* 1539 */         zipEntry1 = new ZipEntry("META-INF/MANIFEST.MF");
/* 1540 */         bool3 = true;
/*      */       } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1554 */       Vector<ZipEntry> vector = new Vector();
/*      */       
/* 1556 */       boolean bool = false;
/*      */       
/* 1558 */       Enumeration<? extends ZipEntry> enumeration1 = this.zipFile.entries();
/* 1559 */       while (enumeration1.hasMoreElements()) {
/* 1560 */         ZipEntry zipEntry = enumeration1.nextElement();
/*      */         
/* 1562 */         if (zipEntry.getName().startsWith("META-INF/")) {
/*      */ 
/*      */           
/* 1565 */           vector.addElement(zipEntry);
/*      */           
/* 1567 */           if (SignatureFileVerifier.isBlockOrSF(zipEntry
/* 1568 */               .getName().toUpperCase(Locale.ENGLISH))) {
/* 1569 */             bool = true;
/*      */           }
/*      */           
/* 1572 */           if (signatureRelated(zipEntry.getName())) {
/*      */             continue;
/*      */           }
/*      */         } 
/*      */ 
/*      */         
/* 1578 */         if (manifest.getAttributes(zipEntry.getName()) != null) {
/*      */ 
/*      */           
/* 1581 */           if (updateDigests(zipEntry, this.zipFile, arrayOfMessageDigest, manifest) == true)
/*      */           {
/* 1583 */             bool2 = true; }  continue;
/*      */         } 
/* 1585 */         if (!zipEntry.isDirectory()) {
/*      */           
/* 1587 */           Attributes attributes1 = getDigestAttributes(zipEntry, this.zipFile, arrayOfMessageDigest);
/*      */           
/* 1589 */           map.put(zipEntry.getName(), attributes1);
/* 1590 */           bool2 = true;
/*      */         } 
/*      */       } 
/*      */ 
/*      */       
/* 1595 */       if (bool2) {
/* 1596 */         ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
/* 1597 */         manifest.write(byteArrayOutputStream);
/* 1598 */         if (bool) {
/* 1599 */           byte[] arrayOfByte1 = byteArrayOutputStream.toByteArray();
/* 1600 */           if (arrayOfByte != null && attributes
/* 1601 */             .equals(manifest.getMainAttributes())) {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */             
/* 1614 */             int i = findHeaderEnd(arrayOfByte1);
/* 1615 */             int j = findHeaderEnd(arrayOfByte);
/*      */             
/* 1617 */             if (i == j) {
/* 1618 */               System.arraycopy(arrayOfByte, 0, arrayOfByte1, 0, j);
/*      */             } else {
/*      */               
/* 1621 */               byte[] arrayOfByte2 = new byte[j + arrayOfByte1.length - i];
/*      */               
/* 1623 */               System.arraycopy(arrayOfByte, 0, arrayOfByte2, 0, j);
/* 1624 */               System.arraycopy(arrayOfByte1, i, arrayOfByte2, j, arrayOfByte1.length - i);
/*      */               
/* 1626 */               arrayOfByte1 = arrayOfByte2;
/*      */             } 
/*      */           } 
/* 1629 */           arrayOfByte = arrayOfByte1;
/*      */         } else {
/* 1631 */           arrayOfByte = byteArrayOutputStream.toByteArray();
/*      */         } 
/*      */       } 
/*      */ 
/*      */       
/* 1636 */       if (bool2)
/*      */       {
/* 1638 */         zipEntry1 = new ZipEntry("META-INF/MANIFEST.MF");
/*      */       }
/* 1640 */       if (this.verbose != null) {
/* 1641 */         if (bool3) {
/* 1642 */           System.out.println(rb.getString(".adding.") + zipEntry1
/* 1643 */               .getName());
/* 1644 */         } else if (bool2) {
/* 1645 */           System.out.println(rb.getString(".updating.") + zipEntry1
/* 1646 */               .getName());
/*      */         } 
/*      */       }
/* 1649 */       zipOutputStream.putNextEntry(zipEntry1);
/* 1650 */       zipOutputStream.write(arrayOfByte);
/*      */ 
/*      */       
/* 1653 */       ManifestDigester manifestDigester = new ManifestDigester(arrayOfByte);
/* 1654 */       SignatureFile signatureFile = new SignatureFile(arrayOfMessageDigest, manifest, manifestDigester, this.sigfile, this.signManifest);
/*      */ 
/*      */       
/* 1657 */       if (this.tsaAlias != null) {
/* 1658 */         x509Certificate = getTsaCert(this.tsaAlias);
/*      */       }
/*      */       
/* 1661 */       if (this.tsaUrl == null && x509Certificate == null) {
/* 1662 */         this.noTimestamp = true;
/*      */       }
/*      */       
/* 1665 */       SignatureFile.Block block = null;
/*      */ 
/*      */       
/*      */       try {
/* 1669 */         block = signatureFile.generateBlock(this.privateKey, this.sigalg, this.certChain, this.externalSF, this.tsaUrl, x509Certificate, this.tSAPolicyID, this.tSADigestAlg, this.signingMechanism, paramArrayOfString, this.zipFile);
/*      */       
/*      */       }
/* 1672 */       catch (SocketTimeoutException socketTimeoutException) {
/*      */         
/* 1674 */         error(rb.getString("unable.to.sign.jar.") + rb
/* 1675 */             .getString("no.response.from.the.Timestamping.Authority.") + "\n  -J-Dhttp.proxyHost=<hostname>\n  -J-Dhttp.proxyPort=<portnumber>\n" + rb
/*      */ 
/*      */             
/* 1678 */             .getString("or") + "\n  -J-Dhttps.proxyHost=<hostname> \n  -J-Dhttps.proxyPort=<portnumber> ", socketTimeoutException);
/*      */       } 
/*      */ 
/*      */ 
/*      */       
/* 1683 */       str2 = signatureFile.getMetaName();
/* 1684 */       str3 = block.getMetaName();
/*      */       
/* 1686 */       ZipEntry zipEntry2 = new ZipEntry(str2);
/* 1687 */       ZipEntry zipEntry3 = new ZipEntry(str3);
/*      */       
/* 1689 */       long l = System.currentTimeMillis();
/* 1690 */       zipEntry2.setTime(l);
/* 1691 */       zipEntry3.setTime(l);
/*      */ 
/*      */       
/* 1694 */       zipOutputStream.putNextEntry(zipEntry2);
/* 1695 */       signatureFile.write(zipOutputStream);
/* 1696 */       if (this.verbose != null) {
/* 1697 */         if (this.zipFile.getEntry(str2) != null) {
/* 1698 */           System.out.println(rb.getString(".updating.") + str2);
/*      */         } else {
/*      */           
/* 1701 */           System.out.println(rb.getString(".adding.") + str2);
/*      */         } 
/*      */       }
/*      */ 
/*      */       
/* 1706 */       if (this.verbose != null) {
/* 1707 */         if (this.tsaUrl != null || x509Certificate != null) {
/* 1708 */           System.out.println(rb
/* 1709 */               .getString("requesting.a.signature.timestamp"));
/*      */         }
/* 1711 */         if (this.tsaUrl != null) {
/* 1712 */           System.out.println(rb.getString("TSA.location.") + this.tsaUrl);
/*      */         }
/* 1714 */         if (x509Certificate != null) {
/* 1715 */           URI uRI = TimestampedSigner.getTimestampingURI(x509Certificate);
/* 1716 */           if (uRI != null) {
/* 1717 */             System.out.println(rb.getString("TSA.location.") + uRI);
/*      */           }
/*      */           
/* 1720 */           System.out.println(rb.getString("TSA.certificate.") + 
/* 1721 */               printCert(true, "", x509Certificate, null, false));
/*      */         } 
/* 1723 */         if (this.signingMechanism != null) {
/* 1724 */           System.out.println(rb
/* 1725 */               .getString("using.an.alternative.signing.mechanism"));
/*      */         }
/*      */       } 
/*      */ 
/*      */       
/* 1730 */       zipOutputStream.putNextEntry(zipEntry3);
/* 1731 */       block.write(zipOutputStream);
/* 1732 */       if (this.verbose != null) {
/* 1733 */         if (this.zipFile.getEntry(str3) != null) {
/* 1734 */           System.out.println(rb.getString(".updating.") + str3);
/*      */         } else {
/*      */           
/* 1737 */           System.out.println(rb.getString(".adding.") + str3);
/*      */         } 
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1744 */       for (byte b1 = 0; b1 < vector.size(); b1++) {
/* 1745 */         ZipEntry zipEntry = vector.elementAt(b1);
/* 1746 */         if (!zipEntry.getName().equalsIgnoreCase("META-INF/MANIFEST.MF") && 
/* 1747 */           !zipEntry.getName().equalsIgnoreCase(str2) && 
/* 1748 */           !zipEntry.getName().equalsIgnoreCase(str3)) {
/* 1749 */           writeEntry(this.zipFile, zipOutputStream, zipEntry);
/*      */         }
/*      */       } 
/*      */ 
/*      */       
/* 1754 */       Enumeration<? extends ZipEntry> enumeration2 = this.zipFile.entries();
/* 1755 */       while (enumeration2.hasMoreElements()) {
/* 1756 */         ZipEntry zipEntry = enumeration2.nextElement();
/*      */         
/* 1758 */         if (!zipEntry.getName().startsWith("META-INF/")) {
/* 1759 */           if (this.verbose != null)
/* 1760 */             if (manifest.getAttributes(zipEntry.getName()) != null) {
/* 1761 */               System.out.println(rb.getString(".signing.") + zipEntry
/* 1762 */                   .getName());
/*      */             } else {
/* 1764 */               System.out.println(rb.getString(".adding.") + zipEntry
/* 1765 */                   .getName());
/*      */             }  
/* 1767 */           writeEntry(this.zipFile, zipOutputStream, zipEntry);
/*      */         } 
/*      */       } 
/* 1770 */     } catch (IOException iOException) {
/* 1771 */       error(rb.getString("unable.to.sign.jar.") + iOException, iOException);
/*      */     } finally {
/*      */       
/* 1774 */       if (this.zipFile != null) {
/* 1775 */         this.zipFile.close();
/* 1776 */         this.zipFile = null;
/*      */       } 
/*      */       
/* 1779 */       if (zipOutputStream != null) {
/* 1780 */         zipOutputStream.close();
/*      */       }
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1787 */     try (JarFile null = new JarFile(file2)) {
/* 1788 */       PKCS7 pKCS7 = new PKCS7(jarFile.getInputStream(jarFile.getEntry("META-INF/" + this.sigfile + "." + this.privateKey
/* 1789 */               .getAlgorithm())));
/* 1790 */       Timestamp timestamp = null;
/*      */       try {
/* 1792 */         SignerInfo signerInfo = pKCS7.getSignerInfos()[0];
/* 1793 */         if (signerInfo.getTsToken() != null) {
/* 1794 */           this.hasTimestampBlock = true;
/*      */         }
/* 1796 */         timestamp = signerInfo.getTimestamp();
/* 1797 */       } catch (Exception exception) {
/* 1798 */         this.tsaChainNotValidated = true;
/* 1799 */         this.tsaChainNotValidatedReason = exception;
/*      */       } 
/*      */       
/* 1802 */       String str = certsAndTSInfo("", "    ", Arrays.asList((Certificate[])this.certChain), timestamp);
/* 1803 */       if (this.verbose != null) {
/* 1804 */         System.out.println(str);
/*      */       }
/* 1806 */     } catch (Exception exception) {
/* 1807 */       if (this.debug) {
/* 1808 */         exception.printStackTrace();
/*      */       }
/*      */     } 
/*      */     
/* 1812 */     if (this.signedjar == null)
/*      */     {
/*      */ 
/*      */       
/* 1816 */       if (!file2.renameTo(file1)) {
/* 1817 */         File file = new File(paramString1 + ".orig");
/*      */         
/* 1819 */         if (file1.renameTo(file)) {
/* 1820 */           if (file2.renameTo(file1)) {
/* 1821 */             file.delete();
/*      */           } else {
/*      */             
/* 1824 */             MessageFormat messageFormat = new MessageFormat(rb.getString("attempt.to.rename.signedJarFile.to.jarFile.failed"));
/* 1825 */             Object[] arrayOfObject = { file2, file1 };
/* 1826 */             error(messageFormat.format(arrayOfObject));
/*      */           } 
/*      */         } else {
/*      */           
/* 1830 */           MessageFormat messageFormat = new MessageFormat(rb.getString("attempt.to.rename.jarFile.to.origJar.failed"));
/* 1831 */           Object[] arrayOfObject = { file1, file };
/* 1832 */           error(messageFormat.format(arrayOfObject));
/*      */         } 
/*      */       } 
/*      */     }
/*      */     
/* 1837 */     displayMessagesAndResult(true);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private int findHeaderEnd(byte[] paramArrayOfbyte) {
/* 1848 */     boolean bool = true;
/* 1849 */     int i = paramArrayOfbyte.length;
/* 1850 */     for (byte b = 0; b < i; b++) {
/* 1851 */       switch (paramArrayOfbyte[b]) {
/*      */         case 13:
/* 1853 */           if (b < i - 1 && paramArrayOfbyte[b + 1] == 10) b++;
/*      */         
/*      */         case 10:
/* 1856 */           if (bool) return b + 1; 
/* 1857 */           bool = true;
/*      */           break;
/*      */         default:
/* 1860 */           bool = false;
/*      */           break;
/*      */       } 
/*      */ 
/*      */     
/*      */     } 
/* 1866 */     return i;
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
/*      */   private boolean signatureRelated(String paramString) {
/* 1879 */     return SignatureFileVerifier.isSigningRelated(paramString);
/*      */   }
/*      */   
/* 1882 */   Map<CodeSigner, String> cacheForSignerInfo = new IdentityHashMap<>();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private String signerInfo(CodeSigner paramCodeSigner, String paramString) throws Exception {
/* 1889 */     if (this.cacheForSignerInfo.containsKey(paramCodeSigner)) {
/* 1890 */       return this.cacheForSignerInfo.get(paramCodeSigner);
/*      */     }
/* 1892 */     List<? extends Certificate> list = paramCodeSigner.getSignerCertPath().getCertificates();
/*      */     
/* 1894 */     Timestamp timestamp = paramCodeSigner.getTimestamp();
/* 1895 */     String str1 = "";
/* 1896 */     if (timestamp != null) {
/* 1897 */       str1 = printTimestamp(paramString, timestamp) + "\n";
/*      */     }
/*      */ 
/*      */     
/* 1901 */     String str2 = certsAndTSInfo(paramString, paramString, list, timestamp);
/* 1902 */     this.cacheForSignerInfo.put(paramCodeSigner, str1 + str2);
/* 1903 */     return str2;
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private String certsAndTSInfo(String paramString1, String paramString2, List<? extends Certificate> paramList, Timestamp paramTimestamp) throws Exception {
/*      */     Date date;
/* 1923 */     if (paramTimestamp != null) {
/* 1924 */       date = paramTimestamp.getTimestamp();
/* 1925 */       this.noTimestamp = false;
/*      */     } else {
/* 1927 */       date = null;
/*      */     } 
/*      */ 
/*      */     
/* 1931 */     boolean bool = true;
/* 1932 */     StringBuilder stringBuilder = new StringBuilder();
/* 1933 */     stringBuilder.append(paramString1).append(rb.getString("...Signer")).append('\n');
/* 1934 */     for (Certificate certificate : paramList) {
/* 1935 */       stringBuilder.append(printCert(false, paramString2, certificate, date, bool));
/* 1936 */       stringBuilder.append('\n');
/* 1937 */       bool = false;
/*      */     } 
/*      */     try {
/* 1940 */       validateCertChain("code signing", paramList, paramTimestamp);
/* 1941 */     } catch (Exception exception) {
/* 1942 */       this.chainNotValidated = true;
/* 1943 */       this.chainNotValidatedReason = exception;
/* 1944 */       stringBuilder.append(paramString2).append(rb.getString(".Invalid.certificate.chain."))
/* 1945 */         .append(exception.getLocalizedMessage()).append("]\n");
/*      */     } 
/* 1947 */     if (paramTimestamp != null) {
/* 1948 */       stringBuilder.append(paramString1).append(rb.getString("...TSA")).append('\n');
/* 1949 */       for (Certificate certificate : paramTimestamp.getSignerCertPath().getCertificates()) {
/* 1950 */         stringBuilder.append(printCert(true, paramString2, certificate, null, false));
/* 1951 */         stringBuilder.append('\n');
/*      */       } 
/*      */       try {
/* 1954 */         validateCertChain("tsa server", paramTimestamp
/* 1955 */             .getSignerCertPath().getCertificates(), null);
/* 1956 */       } catch (Exception exception) {
/* 1957 */         this.tsaChainNotValidated = true;
/* 1958 */         this.tsaChainNotValidatedReason = exception;
/* 1959 */         stringBuilder.append(paramString2).append(rb.getString(".Invalid.TSA.certificate.chain."))
/* 1960 */           .append(exception.getLocalizedMessage()).append("]\n");
/*      */       } 
/*      */     } 
/* 1963 */     if (paramList.size() == 1 && 
/* 1964 */       KeyStoreUtil.isSelfSigned((X509Certificate)paramList.get(0))) {
/* 1965 */       this.signerSelfSigned = true;
/*      */     }
/*      */     
/* 1968 */     return stringBuilder.toString();
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private void writeEntry(ZipFile paramZipFile, ZipOutputStream paramZipOutputStream, ZipEntry paramZipEntry) throws IOException {
/* 1974 */     ZipEntry zipEntry = new ZipEntry(paramZipEntry.getName());
/* 1975 */     zipEntry.setMethod(paramZipEntry.getMethod());
/* 1976 */     zipEntry.setTime(paramZipEntry.getTime());
/* 1977 */     zipEntry.setComment(paramZipEntry.getComment());
/* 1978 */     zipEntry.setExtra(paramZipEntry.getExtra());
/* 1979 */     if (paramZipEntry.getMethod() == 0) {
/* 1980 */       zipEntry.setSize(paramZipEntry.getSize());
/* 1981 */       zipEntry.setCrc(paramZipEntry.getCrc());
/*      */     } 
/* 1983 */     paramZipOutputStream.putNextEntry(zipEntry);
/* 1984 */     writeBytes(paramZipFile, paramZipEntry, paramZipOutputStream);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private synchronized void writeBytes(ZipFile paramZipFile, ZipEntry paramZipEntry, ZipOutputStream paramZipOutputStream) throws IOException {
/* 1994 */     InputStream inputStream = null;
/*      */     try {
/* 1996 */       inputStream = paramZipFile.getInputStream(paramZipEntry);
/* 1997 */       long l = paramZipEntry.getSize();
/*      */       int i;
/* 1999 */       while (l > 0L && (i = inputStream.read(this.buffer, 0, this.buffer.length)) != -1) {
/* 2000 */         paramZipOutputStream.write(this.buffer, 0, i);
/* 2001 */         l -= i;
/*      */       } 
/*      */     } finally {
/* 2004 */       if (inputStream != null) {
/* 2005 */         inputStream.close();
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   void loadKeyStore(String paramString, boolean paramBoolean) {
/* 2012 */     if (!this.nullStream && paramString == null) {
/* 2013 */       paramString = System.getProperty("user.home") + File.separator + ".keystore";
/*      */     }
/*      */ 
/*      */     
/*      */     try {
/*      */       try {
/* 2019 */         KeyStore keyStore = KeyStoreUtil.getCacertsKeyStore();
/* 2020 */         if (keyStore != null) {
/* 2021 */           Enumeration<String> enumeration = keyStore.aliases();
/* 2022 */           while (enumeration.hasMoreElements()) {
/* 2023 */             String str = enumeration.nextElement();
/*      */             try {
/* 2025 */               this.trustedCerts.add((X509Certificate)keyStore.getCertificate(str));
/* 2026 */             } catch (Exception exception) {}
/*      */           }
/*      */         
/*      */         }
/*      */       
/* 2031 */       } catch (Exception exception) {}
/*      */ 
/*      */ 
/*      */       
/* 2035 */       if (this.providerName == null) {
/* 2036 */         this.store = KeyStore.getInstance(this.storetype);
/*      */       } else {
/* 2038 */         this.store = KeyStore.getInstance(this.storetype, this.providerName);
/*      */       } 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 2044 */       if (this.token && this.storepass == null && !this.protectedPath && 
/* 2045 */         !KeyStoreUtil.isWindowsKeyStore(this.storetype)) {
/* 2046 */         this
/* 2047 */           .storepass = getPass(rb.getString("Enter.Passphrase.for.keystore."));
/* 2048 */       } else if (!this.token && this.storepass == null && paramBoolean) {
/* 2049 */         this
/* 2050 */           .storepass = getPass(rb.getString("Enter.Passphrase.for.keystore."));
/*      */       } 
/*      */       
/*      */       try {
/* 2054 */         if (this.nullStream) {
/* 2055 */           this.store.load(null, this.storepass);
/*      */         } else {
/* 2057 */           paramString = paramString.replace(File.separatorChar, '/');
/* 2058 */           URL uRL = null;
/*      */           try {
/* 2060 */             uRL = new URL(paramString);
/* 2061 */           } catch (MalformedURLException malformedURLException) {
/*      */             
/* 2063 */             uRL = (new File(paramString)).toURI().toURL();
/*      */           } 
/* 2065 */           InputStream inputStream = null;
/*      */           try {
/* 2067 */             inputStream = uRL.openStream();
/* 2068 */             this.store.load(inputStream, this.storepass);
/*      */           } finally {
/* 2070 */             if (inputStream != null) {
/* 2071 */               inputStream.close();
/*      */             }
/*      */           } 
/*      */         } 
/* 2075 */         Enumeration<String> enumeration = this.store.aliases();
/* 2076 */         while (enumeration.hasMoreElements()) {
/* 2077 */           String str = enumeration.nextElement();
/*      */           try {
/* 2079 */             X509Certificate x509Certificate = (X509Certificate)this.store.getCertificate(str);
/*      */ 
/*      */             
/* 2082 */             if (this.store.isCertificateEntry(str) || x509Certificate
/* 2083 */               .getSubjectDN().equals(x509Certificate.getIssuerDN())) {
/* 2084 */               this.trustedCerts.add(x509Certificate);
/*      */             }
/* 2086 */           } catch (Exception exception) {}
/*      */         } 
/*      */       } finally {
/*      */ 
/*      */         
/*      */         try {
/* 2092 */           this
/*      */ 
/*      */             
/* 2095 */             .pkixParameters = new PKIXBuilderParameters((Set<TrustAnchor>)this.trustedCerts.stream().map(paramX509Certificate -> new TrustAnchor(paramX509Certificate, null)).collect(Collectors.toSet()), null);
/*      */           
/* 2097 */           this.pkixParameters.setRevocationEnabled(false);
/* 2098 */         } catch (InvalidAlgorithmParameterException invalidAlgorithmParameterException) {}
/*      */       }
/*      */     
/*      */     }
/* 2102 */     catch (IOException iOException) {
/* 2103 */       throw new RuntimeException(rb.getString("keystore.load.") + iOException
/* 2104 */           .getMessage());
/* 2105 */     } catch (CertificateException certificateException) {
/* 2106 */       throw new RuntimeException(rb.getString("certificate.exception.") + certificateException
/* 2107 */           .getMessage());
/* 2108 */     } catch (NoSuchProviderException noSuchProviderException) {
/* 2109 */       throw new RuntimeException(rb.getString("keystore.load.") + noSuchProviderException
/* 2110 */           .getMessage());
/* 2111 */     } catch (NoSuchAlgorithmException noSuchAlgorithmException) {
/* 2112 */       throw new RuntimeException(rb.getString("keystore.load.") + noSuchAlgorithmException
/* 2113 */           .getMessage());
/* 2114 */     } catch (KeyStoreException keyStoreException) {
/* 2115 */       throw new RuntimeException(rb
/* 2116 */           .getString("unable.to.instantiate.keystore.class.") + keyStoreException
/* 2117 */           .getMessage());
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   X509Certificate getTsaCert(String paramString) {
/* 2123 */     Certificate certificate = null;
/*      */     
/*      */     try {
/* 2126 */       certificate = this.store.getCertificate(paramString);
/* 2127 */     } catch (KeyStoreException keyStoreException) {}
/*      */ 
/*      */     
/* 2130 */     if (certificate == null || !(certificate instanceof X509Certificate)) {
/*      */       
/* 2132 */       MessageFormat messageFormat = new MessageFormat(rb.getString("Certificate.not.found.for.alias.alias.must.reference.a.valid.KeyStore.entry.containing.an.X.509.public.key.certificate.for.the"));
/* 2133 */       Object[] arrayOfObject = { paramString, paramString };
/* 2134 */       error(messageFormat.format(arrayOfObject));
/*      */     } 
/* 2136 */     return (X509Certificate)certificate;
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   void checkCertUsage(X509Certificate paramX509Certificate, boolean[] paramArrayOfboolean) {
/* 2156 */     if (paramArrayOfboolean != null) {
/* 2157 */       paramArrayOfboolean[2] = false; paramArrayOfboolean[1] = false; paramArrayOfboolean[0] = false;
/*      */     } 
/*      */     
/* 2160 */     boolean[] arrayOfBoolean = paramX509Certificate.getKeyUsage();
/* 2161 */     if (arrayOfBoolean != null) {
/* 2162 */       arrayOfBoolean = Arrays.copyOf(arrayOfBoolean, 9);
/* 2163 */       if (!arrayOfBoolean[0] && !arrayOfBoolean[1] && 
/* 2164 */         paramArrayOfboolean != null) {
/* 2165 */         paramArrayOfboolean[0] = true;
/* 2166 */         this.badKeyUsage = true;
/*      */       } 
/*      */     } 
/*      */ 
/*      */     
/*      */     try {
/* 2172 */       List<String> list = paramX509Certificate.getExtendedKeyUsage();
/* 2173 */       if (list != null && 
/* 2174 */         !list.contains("2.5.29.37.0") && 
/* 2175 */         !list.contains("1.3.6.1.5.5.7.3.3") && 
/* 2176 */         paramArrayOfboolean != null) {
/* 2177 */         paramArrayOfboolean[1] = true;
/* 2178 */         this.badExtendedKeyUsage = true;
/*      */       }
/*      */     
/*      */     }
/* 2182 */     catch (CertificateParsingException certificateParsingException) {}
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     try {
/* 2189 */       byte[] arrayOfByte = paramX509Certificate.getExtensionValue("2.16.840.1.113730.1.1");
/* 2190 */       if (arrayOfByte != null) {
/* 2191 */         DerInputStream derInputStream = new DerInputStream(arrayOfByte);
/* 2192 */         byte[] arrayOfByte1 = derInputStream.getOctetString();
/*      */         
/* 2194 */         arrayOfByte1 = (new DerValue(arrayOfByte1)).getUnalignedBitString().toByteArray();
/*      */         
/* 2196 */         NetscapeCertTypeExtension netscapeCertTypeExtension = new NetscapeCertTypeExtension(arrayOfByte1);
/*      */ 
/*      */         
/* 2199 */         Boolean bool = netscapeCertTypeExtension.get("object_signing");
/* 2200 */         if (!bool.booleanValue() && 
/* 2201 */           paramArrayOfboolean != null) {
/* 2202 */           paramArrayOfboolean[2] = true;
/* 2203 */           this.badNetscapeCertType = true;
/*      */         }
/*      */       
/*      */       } 
/* 2207 */     } catch (IOException iOException) {}
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   void getAliasInfo(String paramString) throws Exception {
/* 2215 */     Key key = null;
/*      */     
/*      */     try {
/* 2218 */       Certificate[] arrayOfCertificate = null;
/* 2219 */       if (this.altCertChain != null) {
/* 2220 */         try (FileInputStream null = new FileInputStream(this.altCertChain)) {
/*      */ 
/*      */           
/* 2223 */           arrayOfCertificate = CertificateFactory.getInstance("X.509").generateCertificates(fileInputStream).<Certificate>toArray(new Certificate[0]);
/* 2224 */         } catch (FileNotFoundException fileNotFoundException) {
/* 2225 */           error(rb.getString("File.specified.by.certchain.does.not.exist"));
/* 2226 */         } catch (CertificateException|IOException certificateException) {
/* 2227 */           error(rb.getString("Cannot.restore.certchain.from.file.specified"));
/*      */         } 
/*      */       } else {
/*      */         try {
/* 2231 */           arrayOfCertificate = this.store.getCertificateChain(paramString);
/* 2232 */         } catch (KeyStoreException keyStoreException) {}
/*      */       } 
/*      */ 
/*      */       
/* 2236 */       if (arrayOfCertificate == null || arrayOfCertificate.length == 0) {
/* 2237 */         if (this.altCertChain != null) {
/* 2238 */           error(rb
/* 2239 */               .getString("Certificate.chain.not.found.in.the.file.specified."));
/*      */         } else {
/*      */           
/* 2242 */           MessageFormat messageFormat = new MessageFormat(rb.getString("Certificate.chain.not.found.for.alias.alias.must.reference.a.valid.KeyStore.key.entry.containing.a.private.key.and"));
/* 2243 */           Object[] arrayOfObject = { paramString, paramString };
/* 2244 */           error(messageFormat.format(arrayOfObject));
/*      */         } 
/*      */       }
/*      */       
/* 2248 */       this.certChain = new X509Certificate[arrayOfCertificate.length];
/* 2249 */       for (byte b = 0; b < arrayOfCertificate.length; b++) {
/* 2250 */         if (!(arrayOfCertificate[b] instanceof X509Certificate)) {
/* 2251 */           error(rb
/* 2252 */               .getString("found.non.X.509.certificate.in.signer.s.chain"));
/*      */         }
/* 2254 */         this.certChain[b] = (X509Certificate)arrayOfCertificate[b];
/*      */       } 
/*      */       
/*      */       try {
/* 2258 */         if (!this.token && this.keypass == null)
/* 2259 */         { key = this.store.getKey(paramString, this.storepass); }
/*      */         else
/* 2261 */         { key = this.store.getKey(paramString, this.keypass); } 
/* 2262 */       } catch (UnrecoverableKeyException unrecoverableKeyException) {
/* 2263 */         if (this.token)
/* 2264 */           throw unrecoverableKeyException; 
/* 2265 */         if (this.keypass == null) {
/*      */ 
/*      */           
/* 2268 */           MessageFormat messageFormat = new MessageFormat(rb.getString("Enter.key.password.for.alias."));
/* 2269 */           Object[] arrayOfObject = { paramString };
/* 2270 */           this.keypass = getPass(messageFormat.format(arrayOfObject));
/* 2271 */           key = this.store.getKey(paramString, this.keypass);
/*      */         } 
/*      */       } 
/* 2274 */     } catch (NoSuchAlgorithmException noSuchAlgorithmException) {
/* 2275 */       error(noSuchAlgorithmException.getMessage());
/* 2276 */     } catch (UnrecoverableKeyException unrecoverableKeyException) {
/* 2277 */       error(rb.getString("unable.to.recover.key.from.keystore"));
/* 2278 */     } catch (KeyStoreException keyStoreException) {}
/*      */ 
/*      */ 
/*      */     
/* 2282 */     if (!(key instanceof PrivateKey)) {
/*      */       
/* 2284 */       MessageFormat messageFormat = new MessageFormat(rb.getString("key.associated.with.alias.not.a.private.key"));
/* 2285 */       Object[] arrayOfObject = { paramString };
/* 2286 */       error(messageFormat.format(arrayOfObject));
/*      */     } else {
/* 2288 */       this.privateKey = (PrivateKey)key;
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   void error(String paramString) {
/* 2294 */     System.out.println(rb.getString("jarsigner.") + paramString);
/* 2295 */     System.exit(1);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   void error(String paramString, Exception paramException) {
/* 2301 */     System.out.println(rb.getString("jarsigner.") + paramString);
/* 2302 */     if (this.debug) {
/* 2303 */       paramException.printStackTrace();
/*      */     }
/* 2305 */     System.exit(1);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   void validateCertChain(String paramString, List<? extends Certificate> paramList, Timestamp paramTimestamp) throws Exception {
/*      */     try {
/* 2317 */       Validator.getInstance("PKIX", paramString, this.pkixParameters)
/*      */ 
/*      */         
/* 2320 */         .validate(paramList.<X509Certificate>toArray(new X509Certificate[paramList.size()]), null, paramTimestamp);
/*      */     }
/* 2322 */     catch (Exception exception) {
/* 2323 */       if (this.debug) {
/* 2324 */         exception.printStackTrace();
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 2330 */       if (paramString.equals("tsa server") && exception instanceof ValidatorException)
/*      */       {
/*      */         
/* 2333 */         if (exception.getCause() != null && exception
/* 2334 */           .getCause() instanceof java.security.cert.CertPathValidatorException) {
/* 2335 */           exception = (Exception)exception.getCause();
/* 2336 */           Throwable throwable = exception.getCause();
/* 2337 */           if (throwable instanceof CertificateExpiredException && this.hasExpiredTsaCert) {
/*      */             return;
/*      */           }
/*      */         } 
/*      */       }
/*      */ 
/*      */ 
/*      */       
/* 2345 */       if (paramString.equals("code signing") && exception instanceof ValidatorException) {
/*      */ 
/*      */         
/* 2348 */         if (exception.getCause() != null && exception
/* 2349 */           .getCause() instanceof java.security.cert.CertPathValidatorException) {
/* 2350 */           exception = (Exception)exception.getCause();
/* 2351 */           Throwable throwable = exception.getCause();
/* 2352 */           if ((throwable instanceof CertificateExpiredException && this.hasExpiredCert) || (throwable instanceof CertificateNotYetValidException && this.notYetValidCert)) {
/*      */             return;
/*      */           }
/*      */         } 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 2360 */         if (exception instanceof ValidatorException) {
/* 2361 */           ValidatorException validatorException = (ValidatorException)exception;
/* 2362 */           if (validatorException.getErrorType() == ValidatorException.T_EE_EXTENSIONS && (this.badKeyUsage || this.badExtendedKeyUsage || this.badNetscapeCertType)) {
/*      */             return;
/*      */           }
/*      */         } 
/*      */       } 
/*      */ 
/*      */ 
/*      */       
/* 2370 */       throw exception;
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   char[] getPass(String paramString) {
/* 2376 */     System.err.print(paramString);
/* 2377 */     System.err.flush();
/*      */     try {
/* 2379 */       char[] arrayOfChar = Password.readPassword(System.in);
/*      */       
/* 2381 */       if (arrayOfChar == null) {
/* 2382 */         error(rb.getString("you.must.enter.key.password"));
/*      */       } else {
/* 2384 */         return arrayOfChar;
/*      */       } 
/* 2386 */     } catch (IOException iOException) {
/* 2387 */       error(rb.getString("unable.to.read.password.") + iOException.getMessage());
/*      */     } 
/*      */     
/* 2390 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private synchronized byte[] getBytes(ZipFile paramZipFile, ZipEntry paramZipEntry) throws IOException {
/* 2400 */     InputStream inputStream = null;
/*      */     try {
/* 2402 */       inputStream = paramZipFile.getInputStream(paramZipEntry);
/* 2403 */       this.baos.reset();
/* 2404 */       long l = paramZipEntry.getSize();
/*      */       int i;
/* 2406 */       while (l > 0L && (i = inputStream.read(this.buffer, 0, this.buffer.length)) != -1) {
/* 2407 */         this.baos.write(this.buffer, 0, i);
/* 2408 */         l -= i;
/*      */       } 
/*      */     } finally {
/* 2411 */       if (inputStream != null) {
/* 2412 */         inputStream.close();
/*      */       }
/*      */     } 
/*      */     
/* 2416 */     return this.baos.toByteArray();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private ZipEntry getManifestFile(ZipFile paramZipFile) {
/* 2424 */     ZipEntry zipEntry = paramZipFile.getEntry("META-INF/MANIFEST.MF");
/* 2425 */     if (zipEntry == null) {
/*      */       
/* 2427 */       Enumeration<? extends ZipEntry> enumeration = paramZipFile.entries();
/* 2428 */       while (enumeration.hasMoreElements() && zipEntry == null) {
/* 2429 */         zipEntry = enumeration.nextElement();
/*      */         
/* 2431 */         if (!"META-INF/MANIFEST.MF".equalsIgnoreCase(zipEntry.getName())) {
/* 2432 */           zipEntry = null;
/*      */         }
/*      */       } 
/*      */     } 
/* 2436 */     return zipEntry;
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
/*      */   private synchronized String[] getDigests(ZipEntry paramZipEntry, ZipFile paramZipFile, MessageDigest[] paramArrayOfMessageDigest) throws IOException {
/* 2448 */     InputStream inputStream = null;
/*      */     try {
/* 2450 */       inputStream = paramZipFile.getInputStream(paramZipEntry);
/* 2451 */       long l = paramZipEntry.getSize(); int i;
/* 2452 */       while (l > 0L && (
/* 2453 */         i = inputStream.read(this.buffer, 0, this.buffer.length)) != -1) {
/* 2454 */         for (byte b1 = 0; b1 < paramArrayOfMessageDigest.length; b1++) {
/* 2455 */           paramArrayOfMessageDigest[b1].update(this.buffer, 0, i);
/*      */         }
/* 2457 */         l -= i;
/*      */       } 
/*      */     } finally {
/* 2460 */       if (inputStream != null) {
/* 2461 */         inputStream.close();
/*      */       }
/*      */     } 
/*      */ 
/*      */     
/* 2466 */     String[] arrayOfString = new String[paramArrayOfMessageDigest.length];
/* 2467 */     for (byte b = 0; b < paramArrayOfMessageDigest.length; b++) {
/* 2468 */       arrayOfString[b] = Base64.getEncoder().encodeToString(paramArrayOfMessageDigest[b].digest());
/*      */     }
/* 2470 */     return arrayOfString;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private Attributes getDigestAttributes(ZipEntry paramZipEntry, ZipFile paramZipFile, MessageDigest[] paramArrayOfMessageDigest) throws IOException {
/* 2481 */     String[] arrayOfString = getDigests(paramZipEntry, paramZipFile, paramArrayOfMessageDigest);
/* 2482 */     Attributes attributes = new Attributes();
/*      */     
/* 2484 */     for (byte b = 0; b < paramArrayOfMessageDigest.length; b++) {
/* 2485 */       attributes.putValue(paramArrayOfMessageDigest[b].getAlgorithm() + "-Digest", arrayOfString[b]);
/*      */     }
/*      */     
/* 2488 */     return attributes;
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
/*      */ 
/*      */   
/*      */   private boolean updateDigests(ZipEntry paramZipEntry, ZipFile paramZipFile, MessageDigest[] paramArrayOfMessageDigest, Manifest paramManifest) throws IOException {
/* 2504 */     boolean bool = false;
/*      */     
/* 2506 */     Attributes attributes = paramManifest.getAttributes(paramZipEntry.getName());
/* 2507 */     String[] arrayOfString = getDigests(paramZipEntry, paramZipFile, paramArrayOfMessageDigest);
/*      */     
/* 2509 */     for (byte b = 0; b < paramArrayOfMessageDigest.length; b++) {
/*      */       
/* 2511 */       String str = null;
/*      */       
/*      */       try {
/* 2514 */         AlgorithmId algorithmId = AlgorithmId.get(paramArrayOfMessageDigest[b].getAlgorithm());
/* 2515 */         for (Attributes.Name name : attributes.keySet()) {
/* 2516 */           if (name instanceof Attributes.Name) {
/* 2517 */             String str1 = ((Attributes.Name)name).toString();
/* 2518 */             if (str1.toUpperCase(Locale.ENGLISH).endsWith("-DIGEST")) {
/* 2519 */               String str2 = str1.substring(0, str1.length() - 7);
/* 2520 */               if (AlgorithmId.get(str2).equals(algorithmId)) {
/* 2521 */                 str = str1;
/*      */                 break;
/*      */               } 
/*      */             } 
/*      */           } 
/*      */         } 
/* 2527 */       } catch (NoSuchAlgorithmException noSuchAlgorithmException) {}
/*      */ 
/*      */ 
/*      */       
/* 2531 */       if (str == null) {
/* 2532 */         str = paramArrayOfMessageDigest[b].getAlgorithm() + "-Digest";
/* 2533 */         attributes.putValue(str, arrayOfString[b]);
/* 2534 */         bool = true;
/*      */       }
/*      */       else {
/*      */         
/* 2538 */         String str1 = attributes.getValue(str);
/* 2539 */         if (!str1.equalsIgnoreCase(arrayOfString[b])) {
/* 2540 */           attributes.putValue(str, arrayOfString[b]);
/* 2541 */           bool = true;
/*      */         } 
/*      */       } 
/*      */     } 
/* 2545 */     return bool;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private ContentSigner loadSigningMechanism(String paramString1, String paramString2) throws Exception {
/* 2556 */     String str = null;
/*      */ 
/*      */     
/* 2559 */     str = PathList.appendPath(System.getProperty("env.class.path"), str);
/* 2560 */     str = PathList.appendPath(System.getProperty("java.class.path"), str);
/* 2561 */     str = PathList.appendPath(paramString2, str);
/* 2562 */     URL[] arrayOfURL = PathList.pathToURLs(str);
/* 2563 */     URLClassLoader uRLClassLoader = new URLClassLoader(arrayOfURL);
/*      */ 
/*      */     
/* 2566 */     Class<?> clazz = uRLClassLoader.loadClass(paramString1);
/*      */ 
/*      */     
/* 2569 */     Object object = clazz.newInstance();
/* 2570 */     if (!(object instanceof ContentSigner)) {
/*      */       
/* 2572 */       MessageFormat messageFormat = new MessageFormat(rb.getString("signerClass.is.not.a.signing.mechanism"));
/* 2573 */       Object[] arrayOfObject = { clazz.getName() };
/* 2574 */       throw new IllegalArgumentException(messageFormat.format(arrayOfObject));
/*      */     } 
/* 2576 */     return (ContentSigner)object;
/*      */   }
/*      */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\sun\security\tools\jarsigner\Main.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */