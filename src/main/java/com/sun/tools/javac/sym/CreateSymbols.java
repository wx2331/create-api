/*     */ package com.sun.tools.javac.sym;
/*     */
/*     */ import com.sun.tools.javac.Main;
/*     */ import com.sun.tools.javac.api.JavacTaskImpl;
/*     */ import com.sun.tools.javac.code.Attribute;
/*     */ import com.sun.tools.javac.code.Scope;
/*     */ import com.sun.tools.javac.code.Symbol;
/*     */ import com.sun.tools.javac.code.Symtab;
/*     */ import com.sun.tools.javac.code.Type;
/*     */ import com.sun.tools.javac.code.Types;
/*     */ import com.sun.tools.javac.jvm.ClassWriter;
/*     */ import com.sun.tools.javac.jvm.Pool;
/*     */ import com.sun.tools.javac.main.JavaCompiler;
/*     */ import com.sun.tools.javac.processing.JavacProcessingEnvironment;
/*     */ import com.sun.tools.javac.util.List;
/*     */ import com.sun.tools.javac.util.Names;
/*     */ import com.sun.tools.javac.util.Pair;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.EnumSet;
/*     */ import java.util.Enumeration;
/*     */ import java.util.HashSet;
/*     */ import java.util.Map;
/*     */ import java.util.ResourceBundle;
/*     */ import java.util.Set;
/*     */ import javax.annotation.processing.AbstractProcessor;
/*     */ import javax.annotation.processing.RoundEnvironment;
/*     */ import javax.annotation.processing.SupportedAnnotationTypes;
/*     */ import javax.annotation.processing.SupportedOptions;
/*     */ import javax.lang.model.SourceVersion;
/*     */ import javax.lang.model.element.ElementKind;
/*     */ import javax.lang.model.element.TypeElement;
/*     */ import javax.tools.Diagnostic;
/*     */ import javax.tools.JavaCompiler;
/*     */ import javax.tools.JavaFileManager;
/*     */ import javax.tools.JavaFileObject;
/*     */ import javax.tools.StandardJavaFileManager;
/*     */ import javax.tools.StandardLocation;
/*     */ import javax.tools.ToolProvider;
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */ @SupportedOptions({"com.sun.tools.javac.sym.Jar", "com.sun.tools.javac.sym.Dest", "com.sun.tools.javac.sym.Profiles"})
/*     */ @SupportedAnnotationTypes({"*"})
/*     */ public class CreateSymbols
/*     */   extends AbstractProcessor
/*     */ {
/*     */   static Set<String> getLegacyPackages() {
/*  98 */     ResourceBundle resourceBundle = ResourceBundle.getBundle("com.sun.tools.javac.resources.legacy");
/*  99 */     HashSet<String> hashSet = new HashSet();
/* 100 */     for (Enumeration<String> enumeration = resourceBundle.getKeys(); enumeration.hasMoreElements();)
/* 101 */       hashSet.add(enumeration.nextElement());
/* 102 */     return hashSet;
/*     */   }
/*     */
/*     */   public boolean process(Set<? extends TypeElement> paramSet, RoundEnvironment paramRoundEnvironment) {
/*     */     try {
/* 107 */       if (paramRoundEnvironment.processingOver())
/* 108 */         createSymbols();
/* 109 */     } catch (IOException iOException) {
/* 110 */       String str = iOException.getLocalizedMessage();
/* 111 */       if (str == null)
/* 112 */         str = iOException.toString();
/* 113 */       this.processingEnv.getMessager()
/* 114 */         .printMessage(Diagnostic.Kind.ERROR, str);
/* 115 */     } catch (Throwable throwable1) {
/* 116 */       throwable1.printStackTrace();
/* 117 */       Throwable throwable2 = throwable1.getCause();
/* 118 */       if (throwable2 == null)
/* 119 */         throwable2 = throwable1;
/* 120 */       String str = throwable2.getLocalizedMessage();
/* 121 */       if (str == null)
/* 122 */         str = throwable2.toString();
/* 123 */       this.processingEnv.getMessager()
/* 124 */         .printMessage(Diagnostic.Kind.ERROR, str);
/*     */     }
/* 126 */     return true;
/*     */   }
/*     */
/*     */   void createSymbols() throws IOException {
/* 130 */     Set<String> set1 = getLegacyPackages();
/* 131 */     Set<String> set2 = getLegacyPackages();
/* 132 */     HashSet<String> hashSet1 = new HashSet();
/*     */
/* 134 */     Set set = ((JavacProcessingEnvironment)this.processingEnv).getSpecifiedPackages();
/* 135 */     Map<String, String> map = this.processingEnv.getOptions();
/* 136 */     String str1 = map.get("com.sun.tools.javac.sym.Jar");
/* 137 */     if (str1 == null)
/* 138 */       throw new RuntimeException("Must use -Acom.sun.tools.javac.sym.Jar=LOCATION_OF_JAR");
/* 139 */     String str2 = map.get("com.sun.tools.javac.sym.Dest");
/* 140 */     if (str2 == null)
/* 141 */       throw new RuntimeException("Must use -Acom.sun.tools.javac.sym.Dest=LOCATION_OF_JAR");
/* 142 */     String str3 = map.get("com.sun.tools.javac.sym.Profiles");
/* 143 */     if (str3 == null)
/* 144 */       throw new RuntimeException("Must use -Acom.sun.tools.javac.sym.Profiles=PROFILES_SPEC");
/* 145 */     Profiles profiles = Profiles.read(new File(str3));
/*     */
/* 147 */     for (Symbol.PackageSymbol packageSymbol : set) {
/* 148 */       String str = packageSymbol.getQualifiedName().toString();
/* 149 */       set2.remove(str);
/* 150 */       hashSet1.add(str);
/*     */     }
/*     */
/* 153 */     JavaCompiler javaCompiler = ToolProvider.getSystemJavaCompiler();
/* 154 */     StandardJavaFileManager standardJavaFileManager = javaCompiler.getStandardFileManager(null, null, null);
/* 155 */     JavaFileManager.Location location = StandardLocation.locationFor(str1);
/* 156 */     File file1 = new File(str1);
/* 157 */     standardJavaFileManager.setLocation(location, (Iterable<? extends File>)List.of(file1));
/* 158 */     standardJavaFileManager.setLocation(StandardLocation.CLASS_PATH, (Iterable<? extends File>)List.nil());
/* 159 */     standardJavaFileManager.setLocation(StandardLocation.SOURCE_PATH, (Iterable<? extends File>)List.nil());
/*     */
/* 161 */     ArrayList<File> arrayList = new ArrayList();
/* 162 */     arrayList.add(file1);
/* 163 */     for (File file : standardJavaFileManager.getLocation(StandardLocation.PLATFORM_CLASS_PATH)) {
/* 164 */       if (!(new File(file.getName())).equals(new File("rt.jar")))
/* 165 */         arrayList.add(file);
/*     */     }
/* 167 */     System.err.println("Using boot class path = " + arrayList);
/* 168 */     standardJavaFileManager.setLocation(StandardLocation.PLATFORM_CLASS_PATH, arrayList);
/*     */
/*     */
/* 171 */     File file2 = new File(str2);
/* 172 */     if (!file2.exists() &&
/* 173 */       !file2.mkdirs())
/* 174 */       throw new RuntimeException("Could not create " + file2);
/* 175 */     standardJavaFileManager.setLocation(StandardLocation.CLASS_OUTPUT, (Iterable<? extends File>)List.of(file2));
/* 176 */     HashSet<String> hashSet2 = new HashSet();
/* 177 */     HashSet<String> hashSet3 = new HashSet();
/* 178 */     List list = List.of("-XDdev");
/*     */
/*     */
/*     */
/* 182 */     JavacTaskImpl javacTaskImpl = (JavacTaskImpl)javaCompiler.getTask(null, standardJavaFileManager, null, (Iterable<String>)list, null, null);
/*     */
/* 184 */     JavaCompiler javaCompiler1 = JavaCompiler.instance(javacTaskImpl.getContext());
/* 185 */     ClassWriter classWriter = ClassWriter.instance(javacTaskImpl.getContext());
/* 186 */     Symtab symtab = Symtab.instance(javacTaskImpl.getContext());
/* 187 */     Names names = Names.instance(javacTaskImpl.getContext());
/*     */
/*     */
/* 190 */     Attribute.Compound compound = new Attribute.Compound(symtab.proprietaryType, List.nil());
/* 191 */     Attribute.Compound[] arrayOfCompound = new Attribute.Compound[profiles.getProfileCount() + 1];
/* 192 */     Symbol.MethodSymbol methodSymbol = (Symbol.MethodSymbol)(symtab.profileType.tsym.members().lookup(names.value)).sym;
/* 193 */     for (byte b = 1; b < arrayOfCompound.length; b++) {
/* 194 */       arrayOfCompound[b] = new Attribute.Compound(symtab.profileType,
/* 195 */           List.of(new Pair(methodSymbol, new Attribute.Constant((Type)symtab.intType,
/* 196 */                 Integer.valueOf(b)))));
/*     */     }
/*     */
/* 199 */     Type.moreInfo = true;
/* 200 */     Types types = Types.instance(javacTaskImpl.getContext());
/* 201 */     Pool pool = new Pool(types);
/* 202 */     for (JavaFileObject javaFileObject : standardJavaFileManager.list(location, "", EnumSet.of(JavaFileObject.Kind.CLASS), true)) {
/* 203 */       String str4 = standardJavaFileManager.inferBinaryName(location, javaFileObject);
/* 204 */       int i = str4.lastIndexOf('.');
/* 205 */       String str5 = (i == -1) ? "" : str4.substring(0, i);
/* 206 */       boolean bool = false;
/* 207 */       if (hashSet1.contains(str5)) {
/* 208 */         if (!set1.contains(str5)) {
/* 209 */           hashSet3.add(str5);
/*     */         }
/* 211 */       } else if (set2.contains(str5)) {
/* 212 */         bool = true;
/*     */       }
/*     */       else {
/*     */
/* 216 */         hashSet2.add(str5);
/*     */         continue;
/*     */       }
/* 219 */       Symbol.TypeSymbol typeSymbol = (Symbol.TypeSymbol)javaCompiler1.resolveIdent(str4);
/* 220 */       if (typeSymbol.kind != 2) {
/* 221 */         if (str4.indexOf('$') < 0) {
/* 222 */           System.err.println("Ignoring (other) " + str4 + " : " + typeSymbol);
/* 223 */           System.err.println("   " + typeSymbol.getClass().getSimpleName() + " " + typeSymbol.type);
/*     */         }
/*     */         continue;
/*     */       }
/* 227 */       typeSymbol.complete();
/* 228 */       if (typeSymbol.getEnclosingElement().getKind() != ElementKind.PACKAGE) {
/* 229 */         System.err.println("Ignoring (bad) " + typeSymbol.getQualifiedName());
/*     */         continue;
/*     */       }
/* 232 */       Symbol.ClassSymbol classSymbol = (Symbol.ClassSymbol)typeSymbol;
/* 233 */       if (bool) {
/* 234 */         classSymbol.prependAttributes(List.of(compound));
/*     */       }
/* 236 */       int j = profiles.getProfile(classSymbol.fullname.toString().replace(".", "/"));
/* 237 */       if (0 < j && j < arrayOfCompound.length)
/* 238 */         classSymbol.prependAttributes(List.of(arrayOfCompound[j]));
/* 239 */       writeClass(pool, classSymbol, classWriter);
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
/*     */
/*     */   void writeClass(Pool paramPool, Symbol.ClassSymbol paramClassSymbol, ClassWriter paramClassWriter) throws IOException {
/*     */     try {
/* 258 */       paramPool.reset();
/* 259 */       paramClassSymbol.pool = paramPool;
/* 260 */       paramClassWriter.writeClass(paramClassSymbol);
/* 261 */       for (Scope.Entry entry = (paramClassSymbol.members()).elems; entry != null; entry = entry.sibling) {
/* 262 */         if (entry.sym.kind == 2) {
/* 263 */           Symbol.ClassSymbol classSymbol = (Symbol.ClassSymbol)entry.sym;
/* 264 */           classSymbol.complete();
/* 265 */           writeClass(paramPool, classSymbol, paramClassWriter);
/*     */         }
/*     */       }
/* 268 */     } catch (ClassWriter.StringOverflow stringOverflow) {
/* 269 */       throw new RuntimeException(stringOverflow);
/* 270 */     } catch (ClassWriter.PoolOverflow poolOverflow) {
/* 271 */       throw new RuntimeException(poolOverflow);
/*     */     }
/*     */   }
/*     */
/*     */   public SourceVersion getSupportedSourceVersion() {
/* 276 */     return SourceVersion.latest();
/*     */   }
/*     */
/*     */
/*     */   public static void main(String... paramVarArgs) {
/* 281 */     String str1 = paramVarArgs[0];
/* 282 */     String str2 = paramVarArgs[1];
/* 283 */     paramVarArgs = new String[] { "-Xbootclasspath:" + str1, "-XDprocess.packages", "-proc:only", "-processor", "com.sun.tools.javac.sym.CreateSymbols", "-Acom.sun.tools.javac.sym.Jar=" + str1, "-Acom.sun.tools.javac.sym.Dest=" + str2, "java.applet", "java.awt", "java.awt.color", "java.awt.datatransfer", "java.awt.dnd", "java.awt.event", "java.awt.font", "java.awt.geom", "java.awt.im", "java.awt.im.spi", "java.awt.image", "java.awt.image.renderable", "java.awt.print", "java.beans", "java.beans.beancontext", "java.io", "java.lang", "java.lang.annotation", "java.lang.instrument", "java.lang.management", "java.lang.ref", "java.lang.reflect", "java.math", "java.net", "java.nio", "java.nio.channels", "java.nio.channels.spi", "java.nio.charset", "java.nio.charset.spi", "java.rmi", "java.rmi.activation", "java.rmi.dgc", "java.rmi.registry", "java.rmi.server", "java.security", "java.security.acl", "java.security.cert", "java.security.interfaces", "java.security.spec", "java.sql", "java.text", "java.text.spi", "java.util", "java.util.concurrent", "java.util.concurrent.atomic", "java.util.concurrent.locks", "java.util.jar", "java.util.logging", "java.util.prefs", "java.util.regex", "java.util.spi", "java.util.zip", "javax.accessibility", "javax.activation", "javax.activity", "javax.annotation", "javax.annotation.processing", "javax.crypto", "javax.crypto.interfaces", "javax.crypto.spec", "javax.imageio", "javax.imageio.event", "javax.imageio.metadata", "javax.imageio.plugins.jpeg", "javax.imageio.plugins.bmp", "javax.imageio.spi", "javax.imageio.stream", "javax.jws", "javax.jws.soap", "javax.lang.model", "javax.lang.model.element", "javax.lang.model.type", "javax.lang.model.util", "javax.management", "javax.management.loading", "javax.management.monitor", "javax.management.relation", "javax.management.openmbean", "javax.management.timer", "javax.management.modelmbean", "javax.management.remote", "javax.management.remote.rmi", "javax.naming", "javax.naming.directory", "javax.naming.event", "javax.naming.ldap", "javax.naming.spi", "javax.net", "javax.net.ssl", "javax.print", "javax.print.attribute", "javax.print.attribute.standard", "javax.print.event", "javax.rmi", "javax.rmi.CORBA", "javax.rmi.ssl", "javax.script", "javax.security.auth", "javax.security.auth.callback", "javax.security.auth.kerberos", "javax.security.auth.login", "javax.security.auth.spi", "javax.security.auth.x500", "javax.security.cert", "javax.security.sasl", "javax.sound.sampled", "javax.sound.sampled.spi", "javax.sound.midi", "javax.sound.midi.spi", "javax.sql", "javax.sql.rowset", "javax.sql.rowset.serial", "javax.sql.rowset.spi", "javax.swing", "javax.swing.border", "javax.swing.colorchooser", "javax.swing.filechooser", "javax.swing.event", "javax.swing.table", "javax.swing.text", "javax.swing.text.html", "javax.swing.text.html.parser", "javax.swing.text.rtf", "javax.swing.tree", "javax.swing.undo", "javax.swing.plaf", "javax.swing.plaf.basic", "javax.swing.plaf.metal", "javax.swing.plaf.multi", "javax.swing.plaf.synth", "javax.tools", "javax.transaction", "javax.transaction.xa", "javax.xml.parsers", "javax.xml.bind", "javax.xml.bind.annotation", "javax.xml.bind.annotation.adapters", "javax.xml.bind.attachment", "javax.xml.bind.helpers", "javax.xml.bind.util", "javax.xml.soap", "javax.xml.ws", "javax.xml.ws.handler", "javax.xml.ws.handler.soap", "javax.xml.ws.http", "javax.xml.ws.soap", "javax.xml.ws.spi", "javax.xml.transform", "javax.xml.transform.sax", "javax.xml.transform.dom", "javax.xml.transform.stax", "javax.xml.transform.stream", "javax.xml", "javax.xml.crypto", "javax.xml.crypto.dom", "javax.xml.crypto.dsig", "javax.xml.crypto.dsig.dom", "javax.xml.crypto.dsig.keyinfo", "javax.xml.crypto.dsig.spec", "javax.xml.datatype", "javax.xml.validation", "javax.xml.namespace", "javax.xml.xpath", "javax.xml.stream", "javax.xml.stream.events", "javax.xml.stream.util", "org.ietf.jgss", "org.omg.CORBA", "org.omg.CORBA.DynAnyPackage", "org.omg.CORBA.ORBPackage", "org.omg.CORBA.TypeCodePackage", "org.omg.stub.java.rmi", "org.omg.CORBA.portable", "org.omg.CORBA_2_3", "org.omg.CORBA_2_3.portable", "org.omg.CosNaming", "org.omg.CosNaming.NamingContextExtPackage", "org.omg.CosNaming.NamingContextPackage", "org.omg.SendingContext", "org.omg.PortableServer", "org.omg.PortableServer.CurrentPackage", "org.omg.PortableServer.POAPackage", "org.omg.PortableServer.POAManagerPackage", "org.omg.PortableServer.ServantLocatorPackage", "org.omg.PortableServer.portable", "org.omg.PortableInterceptor", "org.omg.PortableInterceptor.ORBInitInfoPackage", "org.omg.Messaging", "org.omg.IOP", "org.omg.IOP.CodecFactoryPackage", "org.omg.IOP.CodecPackage", "org.omg.Dynamic", "org.omg.DynamicAny", "org.omg.DynamicAny.DynAnyPackage", "org.omg.DynamicAny.DynAnyFactoryPackage", "org.w3c.dom", "org.w3c.dom.events", "org.w3c.dom.bootstrap", "org.w3c.dom.ls", "org.xml.sax", "org.xml.sax.ext", "org.xml.sax.helpers", "com.sun.java.browser.dom", "org.w3c.dom", "org.w3c.dom.bootstrap", "org.w3c.dom.ls", "org.w3c.dom.ranges", "org.w3c.dom.traversal", "org.w3c.dom.html", "org.w3c.dom.stylesheets", "org.w3c.dom.css", "org.w3c.dom.events", "org.w3c.dom.views", "com.sun.management", "com.sun.security.auth", "com.sun.security.auth.callback", "com.sun.security.auth.login", "com.sun.security.auth.module", "com.sun.security.jgss", "com.sun.net.httpserver", "com.sun.net.httpserver.spi", "javax.smartcardio" };
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/* 516 */     Main.compile(paramVarArgs);
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\javac\sym\CreateSymbols.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
