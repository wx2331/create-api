/*     */ package com.sun.tools.internal.ws.wscompile;
/*     */ 
/*     */ import com.sun.tools.internal.ws.api.WsgenExtension;
/*     */ import com.sun.tools.internal.ws.api.WsgenProtocol;
/*     */ import com.sun.tools.internal.ws.resources.WscompileMessages;
/*     */ import com.sun.xml.internal.ws.api.BindingID;
/*     */ import com.sun.xml.internal.ws.util.ServiceFinder;
/*     */ import java.io.File;
/*     */ import java.util.ArrayList;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import javax.jws.WebService;
/*     */ import javax.xml.namespace.QName;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class WsgenOptions
/*     */   extends Options
/*     */ {
/*     */   public QName serviceName;
/*     */   public QName portName;
/*     */   public File nonclassDestDir;
/*     */   public boolean genWsdl;
/*     */   public boolean inlineSchemas;
/*  78 */   public String protocol = "soap1.1";
/*     */   
/*  80 */   public Set<String> protocols = new LinkedHashSet<>();
/*  81 */   public Map<String, String> nonstdProtocols = new LinkedHashMap<>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public File wsgenReport;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean doNotOverWrite;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean protocolSet = false;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 102 */   public List<String> externalMetadataFiles = new ArrayList<>();
/*     */ 
/*     */   
/*     */   private static final String SERVICENAME_OPTION = "-servicename";
/*     */ 
/*     */   
/*     */   private static final String PORTNAME_OPTION = "-portname";
/*     */ 
/*     */   
/*     */   private static final String HTTP = "http";
/*     */ 
/*     */   
/*     */   private static final String SOAP11 = "soap1.1";
/*     */   
/*     */   public static final String X_SOAP12 = "Xsoap1.2";
/*     */   
/*     */   List<String> endpoints;
/*     */   
/*     */   public Class endpoint;
/*     */   
/*     */   private boolean isImplClass;
/*     */ 
/*     */   
/*     */   protected int parseArguments(String[] args, int i) throws BadCommandLineException {
/* 126 */     int j = super.parseArguments(args, i);
/* 127 */     if (args[i].equals("-servicename")) {
/* 128 */       this.serviceName = QName.valueOf(requireArgument("-servicename", args, ++i));
/* 129 */       if (this.serviceName.getNamespaceURI() == null || this.serviceName.getNamespaceURI().length() == 0) {
/* 130 */         throw new BadCommandLineException(WscompileMessages.WSGEN_SERVICENAME_MISSING_NAMESPACE(args[i]));
/*     */       }
/* 132 */       if (this.serviceName.getLocalPart() == null || this.serviceName.getLocalPart().length() == 0) {
/* 133 */         throw new BadCommandLineException(WscompileMessages.WSGEN_SERVICENAME_MISSING_LOCALNAME(args[i]));
/*     */       }
/* 135 */       return 2;
/* 136 */     }  if (args[i].equals("-portname")) {
/* 137 */       this.portName = QName.valueOf(requireArgument("-portname", args, ++i));
/* 138 */       if (this.portName.getNamespaceURI() == null || this.portName.getNamespaceURI().length() == 0) {
/* 139 */         throw new BadCommandLineException(WscompileMessages.WSGEN_PORTNAME_MISSING_NAMESPACE(args[i]));
/*     */       }
/* 141 */       if (this.portName.getLocalPart() == null || this.portName.getLocalPart().length() == 0) {
/* 142 */         throw new BadCommandLineException(WscompileMessages.WSGEN_PORTNAME_MISSING_LOCALNAME(args[i]));
/*     */       }
/* 144 */       return 2;
/* 145 */     }  if (args[i].equals("-r")) {
/* 146 */       this.nonclassDestDir = new File(requireArgument("-r", args, ++i));
/* 147 */       if (!this.nonclassDestDir.exists()) {
/* 148 */         throw new BadCommandLineException(WscompileMessages.WSCOMPILE_NO_SUCH_DIRECTORY(this.nonclassDestDir.getPath()));
/*     */       }
/* 150 */       return 2;
/* 151 */     }  if (args[i].startsWith("-wsdl")) {
/* 152 */       this.genWsdl = true;
/*     */       
/* 154 */       String value = args[i].substring(5);
/* 155 */       int index = value.indexOf(':');
/* 156 */       if (index == 0) {
/* 157 */         value = value.substring(1);
/* 158 */         index = value.indexOf('/');
/* 159 */         if (index == -1) {
/* 160 */           this.protocol = value;
/*     */         } else {
/* 162 */           this.protocol = value.substring(0, index);
/*     */         } 
/* 164 */         this.protocolSet = true;
/*     */       } 
/* 166 */       return 1;
/* 167 */     }  if (args[i].equals("-XwsgenReport")) {
/*     */       
/* 169 */       this.wsgenReport = new File(requireArgument("-XwsgenReport", args, ++i));
/* 170 */       return 2;
/* 171 */     }  if (args[i].equals("-Xdonotoverwrite")) {
/* 172 */       this.doNotOverWrite = true;
/* 173 */       return 1;
/* 174 */     }  if (args[i].equals("-inlineSchemas")) {
/* 175 */       this.inlineSchemas = true;
/* 176 */       return 1;
/* 177 */     }  if ("-x".equals(args[i])) {
/* 178 */       this.externalMetadataFiles.add(requireArgument("-x", args, ++i));
/* 179 */       return 1;
/*     */     } 
/*     */     
/* 182 */     return j;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void addFile(String arg) {
/* 188 */     this.endpoints.add(arg);
/*     */   }
/*     */   
/* 191 */   public WsgenOptions() { this.endpoints = new ArrayList<>(); this.protocols.add("soap1.1"); this.protocols.add("Xsoap1.2");
/*     */     this.nonstdProtocols.put("Xsoap1.2", "http://java.sun.com/xml/ns/jaxws/2003/05/soap/bindings/HTTP/");
/*     */     ServiceFinder<WsgenExtension> extn = ServiceFinder.find(WsgenExtension.class);
/*     */     for (WsgenExtension ext : extn) {
/*     */       Class<?> clazz = ext.getClass();
/*     */       WsgenProtocol pro = clazz.<WsgenProtocol>getAnnotation(WsgenProtocol.class);
/*     */       this.protocols.add(pro.token());
/*     */       this.nonstdProtocols.put(pro.token(), pro.lexical());
/* 199 */     }  } public void validate() throws BadCommandLineException { if (this.nonclassDestDir == null) {
/* 200 */       this.nonclassDestDir = this.destDir;
/*     */     }
/* 202 */     if (!this.protocols.contains(this.protocol)) {
/* 203 */       throw new BadCommandLineException(WscompileMessages.WSGEN_INVALID_PROTOCOL(this.protocol, this.protocols));
/*     */     }
/*     */     
/* 206 */     if (this.endpoints.isEmpty()) {
/* 207 */       throw new BadCommandLineException(WscompileMessages.WSGEN_MISSING_FILE());
/*     */     }
/* 209 */     if (this.protocol == null || (this.protocol.equalsIgnoreCase("Xsoap1.2") && !isExtensionMode())) {
/* 210 */       throw new BadCommandLineException(WscompileMessages.WSGEN_SOAP_12_WITHOUT_EXTENSION());
/*     */     }
/*     */     
/* 213 */     if (this.nonstdProtocols.containsKey(this.protocol) && !isExtensionMode()) {
/* 214 */       throw new BadCommandLineException(WscompileMessages.WSGEN_PROTOCOL_WITHOUT_EXTENSION(this.protocol));
/*     */     }
/* 216 */     if (this.inlineSchemas && !this.genWsdl) {
/* 217 */       throw new BadCommandLineException(WscompileMessages.WSGEN_INLINE_SCHEMAS_ONLY_WITH_WSDL());
/*     */     }
/*     */     
/* 220 */     validateEndpointClass();
/* 221 */     validateArguments(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void validateEndpointClass() throws BadCommandLineException {
/* 227 */     Class clazz = null;
/* 228 */     for (String cls : this.endpoints) {
/* 229 */       clazz = getClass(cls);
/* 230 */       if (clazz == null) {
/*     */         continue;
/*     */       }
/* 233 */       if (clazz.isEnum() || clazz.isInterface() || clazz
/* 234 */         .isPrimitive()) {
/*     */         continue;
/*     */       }
/* 237 */       this.isImplClass = true;
/* 238 */       WebService webService = (WebService)clazz.getAnnotation(WebService.class);
/* 239 */       if (webService == null);
/*     */     } 
/*     */ 
/*     */     
/* 243 */     if (clazz == null) {
/* 244 */       throw new BadCommandLineException(WscompileMessages.WSGEN_CLASS_NOT_FOUND(this.endpoints.get(0)));
/*     */     }
/* 246 */     if (!this.isImplClass) {
/* 247 */       throw new BadCommandLineException(WscompileMessages.WSGEN_CLASS_MUST_BE_IMPLEMENTATION_CLASS(clazz.getName()));
/*     */     }
/* 249 */     this.endpoint = clazz;
/* 250 */     validateBinding();
/*     */   }
/*     */   
/*     */   private void validateBinding() throws BadCommandLineException {
/* 254 */     if (this.genWsdl) {
/* 255 */       BindingID binding = BindingID.parse(this.endpoint);
/* 256 */       if ((binding.equals(BindingID.SOAP12_HTTP) || binding
/* 257 */         .equals(BindingID.SOAP12_HTTP_MTOM)) && (
/* 258 */         !this.protocol.equals("Xsoap1.2") || !isExtensionMode())) {
/* 259 */         throw new BadCommandLineException(WscompileMessages.WSGEN_CANNOT_GEN_WSDL_FOR_SOAP_12_BINDING(binding.toString(), this.endpoint.getName()));
/*     */       }
/* 261 */       if (binding.equals(BindingID.XML_HTTP)) {
/* 262 */         throw new BadCommandLineException(WscompileMessages.WSGEN_CANNOT_GEN_WSDL_FOR_NON_SOAP_BINDING(binding.toString(), this.endpoint.getName()));
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   private void validateArguments() throws BadCommandLineException {
/* 268 */     if (!this.genWsdl) {
/* 269 */       if (this.serviceName != null) {
/* 270 */         throw new BadCommandLineException(WscompileMessages.WSGEN_WSDL_ARG_NO_GENWSDL("-servicename"));
/*     */       }
/* 272 */       if (this.portName != null) {
/* 273 */         throw new BadCommandLineException(WscompileMessages.WSGEN_WSDL_ARG_NO_GENWSDL("-portname"));
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   BindingID getBindingID(String protocol) {
/* 279 */     if (protocol.equals("soap1.1"))
/* 280 */       return BindingID.SOAP11_HTTP; 
/* 281 */     if (protocol.equals("Xsoap1.2"))
/* 282 */       return BindingID.SOAP12_HTTP; 
/* 283 */     String lexical = this.nonstdProtocols.get(protocol);
/* 284 */     return (lexical != null) ? BindingID.parse(lexical) : null;
/*     */   }
/*     */ 
/*     */   
/*     */   private Class getClass(String className) {
/*     */     try {
/* 290 */       return getClassLoader().loadClass(className);
/* 291 */     } catch (ClassNotFoundException e) {
/* 292 */       return null;
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\ws\wscompile\WsgenOptions.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */