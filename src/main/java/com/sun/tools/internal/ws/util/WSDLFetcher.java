/*     */ package com.sun.tools.internal.ws.util;
/*     */ 
/*     */ import com.sun.istack.internal.NotNull;
/*     */ import com.sun.tools.internal.ws.resources.WscompileMessages;
/*     */ import com.sun.tools.internal.ws.wscompile.WsimportListener;
/*     */ import com.sun.tools.internal.ws.wscompile.WsimportOptions;
/*     */ import com.sun.tools.internal.ws.wsdl.parser.DOMForest;
/*     */ import com.sun.tools.internal.ws.wsdl.parser.MetadataFinder;
/*     */ import com.sun.xml.internal.txw2.output.IndentingXMLStreamWriter;
/*     */ import com.sun.xml.internal.ws.api.server.PortAddressResolver;
/*     */ import com.sun.xml.internal.ws.streaming.SourceReaderFactory;
/*     */ import com.sun.xml.internal.ws.wsdl.parser.WSDLConstants;
/*     */ import com.sun.xml.internal.ws.wsdl.writer.DocumentLocationResolver;
/*     */ import com.sun.xml.internal.ws.wsdl.writer.WSDLPatcher;
/*     */ import java.io.File;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.net.MalformedURLException;
/*     */ import java.net.URL;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import javax.xml.namespace.QName;
/*     */ import javax.xml.stream.XMLOutputFactory;
/*     */ import javax.xml.stream.XMLStreamException;
/*     */ import javax.xml.stream.XMLStreamReader;
/*     */ import javax.xml.stream.XMLStreamWriter;
/*     */ import javax.xml.transform.dom.DOMSource;
/*     */ import org.w3c.dom.Document;
/*     */ import org.w3c.dom.Element;
/*     */ import org.w3c.dom.Node;
/*     */ import org.w3c.dom.NodeList;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class WSDLFetcher
/*     */ {
/*     */   private WsimportOptions options;
/*     */   private WsimportListener listener;
/*     */   
/*     */   public WSDLFetcher(WsimportOptions options, WsimportListener listener) {
/*  65 */     this.options = options;
/*  66 */     this.listener = listener;
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
/*     */   public String fetchWsdls(MetadataFinder forest) throws IOException, XMLStreamException {
/*  79 */     String rootWsdl = null;
/*  80 */     for (String root : forest.getRootDocuments()) {
/*  81 */       rootWsdl = root;
/*     */     }
/*     */     
/*  84 */     Set<String> externalRefs = forest.getExternalReferences();
/*  85 */     Map<String, String> documentMap = createDocumentMap(forest, getWSDLDownloadDir(), rootWsdl, externalRefs);
/*  86 */     String rootWsdlName = fetchFile(rootWsdl, (DOMForest)forest, documentMap, getWSDLDownloadDir());
/*  87 */     for (String reference : forest.getExternalReferences()) {
/*  88 */       fetchFile(reference, (DOMForest)forest, documentMap, getWSDLDownloadDir());
/*     */     }
/*  90 */     return WSDL_PATH + "/" + rootWsdlName;
/*     */   }
/*     */ 
/*     */   
/*     */   private String fetchFile(String doc, DOMForest forest, Map<String, String> documentMap, File destDir) throws IOException, XMLStreamException {
/*  95 */     DocumentLocationResolver docLocator = createDocResolver(doc, forest, documentMap);
/*  96 */     WSDLPatcher wsdlPatcher = new WSDLPatcher(new PortAddressResolver()
/*     */         {
/*     */           public String getAddressFor(@NotNull QName serviceName, @NotNull String portName) {
/*  99 */             return null;
/*     */           }
/*     */         },  docLocator);
/*     */     
/* 103 */     XMLStreamReader xsr = null;
/* 104 */     XMLStreamWriter xsw = null;
/* 105 */     OutputStream os = null;
/* 106 */     String resolvedRootWsdl = null;
/*     */     
/*     */     try {
/* 109 */       xsr = SourceReaderFactory.createSourceReader(new DOMSource(forest.get(doc)), false);
/* 110 */       XMLOutputFactory writerfactory = XMLOutputFactory.newInstance();
/* 111 */       resolvedRootWsdl = docLocator.getLocationFor(null, doc);
/* 112 */       File outFile = new File(destDir, resolvedRootWsdl);
/* 113 */       os = new FileOutputStream(outFile);
/* 114 */       if (this.options.verbose) {
/* 115 */         this.listener.message(WscompileMessages.WSIMPORT_DOCUMENT_DOWNLOAD(doc, outFile));
/*     */       }
/* 117 */       xsw = writerfactory.createXMLStreamWriter(os);
/*     */ 
/*     */       
/* 120 */       IndentingXMLStreamWriter indentingWriter = new IndentingXMLStreamWriter(xsw);
/* 121 */       wsdlPatcher.bridge(xsr, indentingWriter);
/* 122 */       this.options.addGeneratedFile(outFile);
/*     */     } finally {
/*     */       try {
/* 125 */         if (xsr != null) xsr.close(); 
/* 126 */         if (xsw != null) xsw.close(); 
/*     */       } finally {
/* 128 */         if (os != null) os.close(); 
/*     */       } 
/*     */     } 
/* 131 */     return resolvedRootWsdl;
/*     */   }
/*     */   
/*     */   private Map<String, String> createDocumentMap(MetadataFinder forest, File baseDir, String rootWsdl, Set<String> externalReferences) {
/*     */     String rootWsdlName;
/* 136 */     Map<String, String> map = new HashMap<>();
/* 137 */     String rootWsdlFileName = rootWsdl;
/*     */ 
/*     */     
/* 140 */     int slashIndex = rootWsdl.lastIndexOf("/");
/* 141 */     if (slashIndex >= 0) {
/* 142 */       rootWsdlFileName = rootWsdl.substring(slashIndex + 1);
/*     */     }
/* 144 */     if (!rootWsdlFileName.endsWith(WSDL_FILE_EXTENSION)) {
/* 145 */       Document rootWsdlDoc = forest.get(rootWsdl);
/* 146 */       NodeList serviceNodes = rootWsdlDoc.getElementsByTagNameNS(WSDLConstants.QNAME_SERVICE.getNamespaceURI(), WSDLConstants.QNAME_SERVICE.getLocalPart());
/* 147 */       if (serviceNodes.getLength() == 0) {
/* 148 */         rootWsdlName = "Service";
/*     */       } else {
/* 150 */         Node serviceNode = serviceNodes.item(0);
/* 151 */         String serviceName = ((Element)serviceNode).getAttribute("name");
/* 152 */         rootWsdlName = serviceName;
/*     */       } 
/* 154 */       rootWsdlFileName = rootWsdlName + WSDL_FILE_EXTENSION;
/*     */     } else {
/* 156 */       rootWsdlName = rootWsdlFileName.substring(0, rootWsdlFileName.length() - 5);
/*     */     } 
/*     */     
/* 159 */     map.put(rootWsdl, sanitize(rootWsdlFileName));
/*     */     
/* 161 */     int i = 1;
/* 162 */     for (String ref : externalReferences) {
/* 163 */       String fileExtn; Document refDoc = forest.get(ref);
/* 164 */       Element rootEl = refDoc.getDocumentElement();
/*     */       
/* 166 */       String fileName = null;
/* 167 */       int index = ref.lastIndexOf("/");
/* 168 */       if (index >= 0) {
/* 169 */         fileName = ref.substring(index + 1);
/*     */       }
/* 171 */       if (rootEl.getLocalName().equals(WSDLConstants.QNAME_DEFINITIONS.getLocalPart()) && rootEl.getNamespaceURI().equals("http://schemas.xmlsoap.org/wsdl/")) {
/* 172 */         fileExtn = WSDL_FILE_EXTENSION;
/* 173 */       } else if (rootEl.getLocalName().equals(WSDLConstants.QNAME_SCHEMA.getLocalPart()) && rootEl.getNamespaceURI().equals("http://www.w3.org/2001/XMLSchema")) {
/* 174 */         fileExtn = SCHEMA_FILE_EXTENSION;
/*     */       } else {
/* 176 */         fileExtn = ".xml";
/*     */       } 
/* 178 */       if (fileName != null && (fileName.endsWith(WSDL_FILE_EXTENSION) || fileName.endsWith(SCHEMA_FILE_EXTENSION))) {
/* 179 */         map.put(ref, rootWsdlName + "_" + fileName); continue;
/*     */       } 
/* 181 */       map.put(ref, rootWsdlName + "_metadata" + i++ + fileExtn);
/*     */     } 
/*     */     
/* 184 */     return map;
/*     */   }
/*     */   
/*     */   private DocumentLocationResolver createDocResolver(final String baseWsdl, final DOMForest forest, final Map<String, String> documentMap) {
/* 188 */     return new DocumentLocationResolver()
/*     */       {
/*     */         public String getLocationFor(String namespaceURI, String systemId) {
/*     */           try {
/* 192 */             URL reference = new URL(new URL(baseWsdl), systemId);
/* 193 */             systemId = reference.toExternalForm();
/* 194 */           } catch (MalformedURLException e) {
/* 195 */             throw new RuntimeException(e);
/*     */           } 
/* 197 */           if (documentMap.get(systemId) != null) {
/* 198 */             return (String)documentMap.get(systemId);
/*     */           }
/* 200 */           String parsedEntity = (String)forest.getReferencedEntityMap().get(systemId);
/* 201 */           return (String)documentMap.get(parsedEntity);
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */   
/*     */   private String sanitize(String fileName) {
/* 208 */     fileName = fileName.replace('?', '.');
/* 209 */     StringBuilder sb = new StringBuilder(fileName);
/* 210 */     for (int i = 0; i < sb.length(); i++) {
/* 211 */       char c = sb.charAt(i);
/* 212 */       if (!Character.isLetterOrDigit(c) && c != '/' && c != '.' && c != '_' && c != ' ' && c != '-')
/*     */       {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 220 */         sb.setCharAt(i, '_');
/*     */       }
/*     */     } 
/* 223 */     return sb.toString();
/*     */   }
/*     */   
/*     */   private File getWSDLDownloadDir() {
/* 227 */     File wsdlDir = new File(this.options.destDir, WSDL_PATH);
/* 228 */     boolean created = wsdlDir.mkdirs();
/* 229 */     if (this.options.verbose && !created) {
/* 230 */       this.listener.message(WscompileMessages.WSCOMPILE_NO_SUCH_DIRECTORY(wsdlDir));
/*     */     }
/* 232 */     return wsdlDir;
/*     */   }
/*     */   
/* 235 */   private static String WSDL_PATH = "META-INF/wsdl";
/* 236 */   private static String WSDL_FILE_EXTENSION = ".wsdl";
/* 237 */   private static String SCHEMA_FILE_EXTENSION = ".xsd";
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\w\\util\WSDLFetcher.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */