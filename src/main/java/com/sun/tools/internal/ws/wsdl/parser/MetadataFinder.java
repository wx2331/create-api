/*     */ package com.sun.tools.internal.ws.wsdl.parser;
/*     */
/*     */ import com.sun.istack.internal.NotNull;
/*     */ import com.sun.istack.internal.Nullable;
/*     */ import com.sun.tools.internal.ws.resources.WscompileMessages;
/*     */ import com.sun.tools.internal.ws.resources.WsdlMessages;
/*     */ import com.sun.tools.internal.ws.wscompile.AbortException;
/*     */ import com.sun.tools.internal.ws.wscompile.ErrorReceiver;
/*     */ import com.sun.tools.internal.ws.wscompile.WsimportOptions;
/*     */ import com.sun.tools.internal.ws.wsdl.framework.ParseException;
/*     */ import com.sun.xml.internal.ws.api.wsdl.parser.MetaDataResolver;
/*     */ import com.sun.xml.internal.ws.api.wsdl.parser.MetadataResolverFactory;
/*     */ import com.sun.xml.internal.ws.api.wsdl.parser.ServiceDescriptor;
/*     */ import com.sun.xml.internal.ws.util.DOMUtil;
/*     */ import com.sun.xml.internal.ws.util.JAXWSUtils;
/*     */ import com.sun.xml.internal.ws.util.ServiceFinder;
/*     */ import com.sun.xml.internal.ws.wsdl.parser.WSDLConstants;
/*     */ import java.io.FileNotFoundException;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.net.HttpURLConnection;
/*     */ import java.net.URI;
/*     */ import java.net.URISyntaxException;
/*     */ import java.net.URL;
/*     */ import java.net.URLConnection;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import javax.net.ssl.HostnameVerifier;
/*     */ import javax.net.ssl.HttpsURLConnection;
/*     */ import javax.net.ssl.SSLSession;
/*     */ import javax.xml.transform.Source;
/*     */ import javax.xml.transform.dom.DOMSource;
/*     */ import org.w3c.dom.Document;
/*     */ import org.w3c.dom.Element;
/*     */ import org.w3c.dom.Node;
/*     */ import org.w3c.dom.NodeList;
/*     */ import org.xml.sax.EntityResolver;
/*     */ import org.xml.sax.InputSource;
/*     */ import org.xml.sax.SAXException;
/*     */ import org.xml.sax.SAXParseException;
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */ public final class MetadataFinder
/*     */   extends DOMForest
/*     */ {
/*     */   public boolean isMexMetadata;
/*     */   private String rootWSDL;
/*  71 */   private final Set<String> rootWsdls = new HashSet<>();
/*     */
/*     */   public MetadataFinder(InternalizationLogic logic, WsimportOptions options, ErrorReceiver errReceiver) {
/*  74 */     super(logic, new WSEntityResolver(options, errReceiver), options, errReceiver);
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */   public void parseWSDL() {
/*  81 */     for (InputSource value : this.options.getWSDLs()) {
/*  82 */       Element doc; String systemID = value.getSystemId();
/*  83 */       this.errorReceiver.pollAbort();
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */       try {
/*  90 */         if (this.options.entityResolver != null) {
/*  91 */           value = this.options.entityResolver.resolveEntity(null, systemID);
/*     */         }
/*  93 */         if (value == null) {
/*  94 */           value = new InputSource(systemID);
/*     */         }
/*  96 */         Document dom = parse(value, true);
/*     */
/*  98 */         doc = dom.getDocumentElement();
/*  99 */         if (doc == null) {
/*     */           continue;
/*     */         }
/*     */
/* 103 */         if (doc.getNamespaceURI() == null || !doc.getNamespaceURI().equals("http://schemas.xmlsoap.org/wsdl/") || !doc.getLocalName().equals("definitions")) {
/* 104 */           throw new SAXParseException(WsdlMessages.INVALID_WSDL(systemID, WSDLConstants.QNAME_DEFINITIONS, doc
/* 105 */                 .getNodeName(), Integer.valueOf(this.locatorTable.getStartLocation(doc).getLineNumber())), this.locatorTable.getStartLocation(doc));
/*     */         }
/* 107 */       } catch (FileNotFoundException e) {
/* 108 */         this.errorReceiver.error(WsdlMessages.FILE_NOT_FOUND(systemID), e);
/*     */         return;
/* 110 */       } catch (IOException e) {
/* 111 */         doc = getFromMetadataResolver(systemID, e);
/* 112 */       } catch (SAXParseException e) {
/* 113 */         doc = getFromMetadataResolver(systemID, e);
/* 114 */       } catch (SAXException e) {
/* 115 */         doc = getFromMetadataResolver(systemID, e);
/*     */       }
/*     */
/* 118 */       if (doc != null) {
/*     */
/*     */
/*     */
/* 122 */         NodeList schemas = doc.getElementsByTagNameNS("http://www.w3.org/2001/XMLSchema", "schema");
/* 123 */         for (int i = 0; i < schemas.getLength(); i++) {
/* 124 */           if (!this.inlinedSchemaElements.contains(schemas.item(i)))
/* 125 */             this.inlinedSchemaElements.add((Element)schemas.item(i));
/*     */         }
/*     */       }  continue;
/*     */     }
/* 129 */     identifyRootWsdls();
/*     */   }
/*     */
/*     */   public static class WSEntityResolver
/*     */     implements EntityResolver {
/*     */     WsimportOptions options;
/*     */     ErrorReceiver errorReceiver;
/* 136 */     private URLConnection c = null;
/*     */     private boolean doReset = false;
/*     */
/*     */     public WSEntityResolver(WsimportOptions options, ErrorReceiver errReceiver) {
/* 140 */       this.options = options;
/* 141 */       this.errorReceiver = errReceiver;
/*     */     }
/*     */
/*     */
/*     */     public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {
/* 146 */       InputSource inputSource = null;
/*     */
/* 148 */       if (this.options.entityResolver != null) {
/* 149 */         inputSource = this.options.entityResolver.resolveEntity(null, systemId);
/*     */       }
/* 151 */       if (inputSource == null) {
/* 152 */         inputSource = new InputSource(systemId);
/* 153 */         InputStream is = null;
/* 154 */         int redirects = 0;
/*     */
/* 156 */         URL url = JAXWSUtils.getFileOrURL(inputSource.getSystemId());
/* 157 */         URLConnection conn = url.openConnection();
/*     */         while (true) {
/* 159 */           if (conn instanceof HttpsURLConnection &&
/* 160 */             this.options.disableSSLHostnameVerification) {
/* 161 */             ((HttpsURLConnection)conn).setHostnameVerifier(new HttpClientVerifier());
/*     */           }
/*     */
/* 164 */           boolean redirect = false;
/* 165 */           if (conn instanceof HttpURLConnection) {
/* 166 */             ((HttpURLConnection)conn).setInstanceFollowRedirects(false);
/*     */           }
/*     */
/* 169 */           if (conn instanceof java.net.JarURLConnection &&
/* 170 */             conn.getUseCaches()) {
/* 171 */             this.doReset = true;
/* 172 */             conn.setDefaultUseCaches(false);
/* 173 */             this.c = conn;
/*     */           }
/*     */
/*     */
/*     */           try {
/* 178 */             is = conn.getInputStream();
/*     */           }
/* 180 */           catch (IOException e) {
/* 181 */             if (conn instanceof HttpURLConnection) {
/* 182 */               HttpURLConnection httpConn = (HttpURLConnection)conn;
/* 183 */               int code = httpConn.getResponseCode();
/* 184 */               if (code == 401) {
/* 185 */                 this.errorReceiver.error(new SAXParseException(WscompileMessages.WSIMPORT_AUTH_INFO_NEEDED(e.getMessage(), systemId, WsimportOptions.defaultAuthfile), null, e));
/*     */
/* 187 */                 throw new AbortException();
/*     */               }
/*     */             }
/*     */
/* 191 */             throw e;
/*     */           }
/*     */
/*     */
/*     */
/* 196 */           if (conn instanceof HttpURLConnection) {
/* 197 */             HttpURLConnection httpConn = (HttpURLConnection)conn;
/* 198 */             int code = httpConn.getResponseCode();
/* 199 */             if (code == 302 || code == 303) {
/*     */
/* 201 */               List<String> seeOther = httpConn.getHeaderFields().get("Location");
/* 202 */               if (seeOther != null && seeOther.size() > 0) {
/* 203 */                 URL newurl = new URL(url, seeOther.get(0));
/* 204 */                 if (!newurl.equals(url)) {
/* 205 */                   this.errorReceiver.info(new SAXParseException(WscompileMessages.WSIMPORT_HTTP_REDIRECT(Integer.valueOf(code), seeOther.get(0)), null));
/* 206 */                   url = newurl;
/* 207 */                   httpConn.disconnect();
/* 208 */                   if (redirects >= 5) {
/* 209 */                     this.errorReceiver.error(new SAXParseException(WscompileMessages.WSIMPORT_MAX_REDIRECT_ATTEMPT(), null));
/* 210 */                     throw new AbortException();
/*     */                   }
/* 212 */                   conn = url.openConnection();
/* 213 */                   inputSource.setSystemId(url.toExternalForm());
/* 214 */                   redirects++;
/* 215 */                   redirect = true;
/*     */                 }
/*     */               }
/*     */             }
/*     */           }
/* 220 */           if (!redirect) {
/* 221 */             inputSource.setByteStream(is); break;
/*     */           }
/*     */         }
/* 224 */       }  return inputSource;
/*     */     }
/*     */
/*     */
/*     */
/*     */     protected void finalize() throws Throwable {
/* 230 */       if (this.doReset)
/* 231 */         this.c.setDefaultUseCaches(true);
/*     */     }
/*     */   }
/*     */
/*     */   private static class HttpClientVerifier
/*     */     implements HostnameVerifier
/*     */   {
/*     */     private HttpClientVerifier() {}
/*     */
/*     */     public boolean verify(String s, SSLSession sslSession) {
/* 241 */       return true;
/*     */     }
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   @Nullable
/*     */   public String getRootWSDL() {
/* 251 */     return this.rootWSDL;
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */   @NotNull
/*     */   public Set<String> getRootWSDLs() {
/* 259 */     return this.rootWsdls;
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   private void identifyRootWsdls() {
/* 267 */     for (String location : this.rootDocuments) {
/* 268 */       Document doc = get(location);
/* 269 */       if (doc != null) {
/* 270 */         Element definition = doc.getDocumentElement();
/* 271 */         if (definition == null || definition.getLocalName() == null || definition.getNamespaceURI() == null)
/*     */           continue;
/* 273 */         if (definition.getNamespaceURI().equals("http://schemas.xmlsoap.org/wsdl/") && definition.getLocalName().equals("definitions")) {
/* 274 */           this.rootWsdls.add(location);
/*     */
/* 276 */           NodeList nl = definition.getElementsByTagNameNS("http://schemas.xmlsoap.org/wsdl/", "service");
/*     */
/*     */
/*     */
/* 280 */           if (nl.getLength() > 0) {
/* 281 */             this.rootWSDL = location;
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/* 286 */     if (this.rootWSDL == null) {
/* 287 */       StringBuilder strbuf = new StringBuilder();
/* 288 */       for (String str : this.rootWsdls) {
/* 289 */         strbuf.append(str);
/* 290 */         strbuf.append('\n');
/*     */       }
/* 292 */       this.errorReceiver.error(null, WsdlMessages.FAILED_NOSERVICE(strbuf.toString()));
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
/*     */   @Nullable
/*     */   private Element getFromMetadataResolver(String systemId, Exception ex) {
/* 307 */     ServiceDescriptor serviceDescriptor = null;
/* 308 */     for (MetadataResolverFactory resolverFactory : ServiceFinder.<MetadataResolverFactory>find(MetadataResolverFactory.class)) {
/* 309 */       MetaDataResolver resolver = resolverFactory.metadataResolver(this.options.entityResolver);
/*     */       try {
/* 311 */         serviceDescriptor = resolver.resolve(new URI(systemId));
/*     */
/* 313 */         if (serviceDescriptor != null)
/*     */           break;
/* 315 */       } catch (URISyntaxException e) {
/* 316 */         throw new ParseException(e);
/*     */       }
/*     */     }
/*     */
/* 320 */     if (serviceDescriptor != null) {
/* 321 */       this.errorReceiver.warning(new SAXParseException(WsdlMessages.TRY_WITH_MEX(ex.getMessage()), null, ex));
/* 322 */       return parseMetadata(systemId, serviceDescriptor);
/*     */     }
/* 324 */     this.errorReceiver.error(null, WsdlMessages.PARSING_UNABLE_TO_GET_METADATA(ex.getMessage(), WscompileMessages.WSIMPORT_NO_WSDL(systemId)), ex);
/*     */
/* 326 */     return null;
/*     */   }
/*     */
/*     */   private Element parseMetadata(@NotNull String systemId, @NotNull ServiceDescriptor serviceDescriptor) {
/* 330 */     List<? extends Source> mexWsdls = serviceDescriptor.getWSDLs();
/* 331 */     List<? extends Source> mexSchemas = serviceDescriptor.getSchemas();
/* 332 */     Document root = null;
/* 333 */     for (Source src : mexWsdls) {
/* 334 */       if (src instanceof DOMSource) {
/* 335 */         Document doc; Node n = ((DOMSource)src).getNode();
/*     */
/* 337 */         if (n.getNodeType() == 1 && n.getOwnerDocument() == null) {
/* 338 */           doc = DOMUtil.createDom();
/* 339 */           doc.importNode(n, true);
/*     */         } else {
/* 341 */           doc = n.getOwnerDocument();
/*     */         }
/*     */
/*     */
/* 345 */         if (root == null) {
/*     */
/* 347 */           NodeList nodeList = doc.getDocumentElement().getElementsByTagNameNS("http://schemas.xmlsoap.org/wsdl/", "service");
/* 348 */           if (nodeList.getLength() > 0) {
/* 349 */             root = doc;
/* 350 */             this.rootWSDL = src.getSystemId();
/*     */           }
/*     */         }
/* 353 */         NodeList nl = doc.getDocumentElement().getElementsByTagNameNS("http://schemas.xmlsoap.org/wsdl/", "import");
/* 354 */         for (int i = 0; i < nl.getLength(); i++) {
/* 355 */           Element imp = (Element)nl.item(i);
/* 356 */           String loc = imp.getAttribute("location");
/* 357 */           if (loc != null &&
/* 358 */             !this.externalReferences.contains(loc)) {
/* 359 */             this.externalReferences.add(loc);
/*     */           }
/*     */         }
/* 362 */         if (this.core.keySet().contains(systemId))
/* 363 */           this.core.remove(systemId);
/* 364 */         this.core.put(src.getSystemId(), doc);
/* 365 */         this.resolvedCache.put(systemId, doc.getDocumentURI());
/* 366 */         this.isMexMetadata = true;
/*     */       }
/*     */     }
/*     */
/*     */
/*     */
/*     */
/* 373 */     for (Source src : mexSchemas) {
/* 374 */       if (src instanceof DOMSource) {
/* 375 */         Node n = ((DOMSource)src).getNode();
/* 376 */         Element e = (n.getNodeType() == 1) ? (Element)n : DOMUtil.getFirstElementChild(n);
/* 377 */         this.inlinedSchemaElements.add(e);
/*     */       }
/*     */     }
/*     */
/*     */
/* 382 */     return root.getDocumentElement();
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\ws\wsdl\parser\MetadataFinder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
