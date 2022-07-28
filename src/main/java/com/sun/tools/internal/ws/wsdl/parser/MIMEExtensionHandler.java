/*     */ package com.sun.tools.internal.ws.wsdl.parser;
/*     */ 
/*     */ import com.sun.tools.internal.ws.api.wsdl.TWSDLExtensible;
/*     */ import com.sun.tools.internal.ws.api.wsdl.TWSDLExtension;
/*     */ import com.sun.tools.internal.ws.api.wsdl.TWSDLParserContext;
/*     */ import com.sun.tools.internal.ws.util.xml.XmlUtil;
/*     */ import com.sun.tools.internal.ws.wsdl.document.WSDLConstants;
/*     */ import com.sun.tools.internal.ws.wsdl.document.mime.MIMEConstants;
/*     */ import com.sun.tools.internal.ws.wsdl.document.mime.MIMEContent;
/*     */ import com.sun.tools.internal.ws.wsdl.document.mime.MIMEMultipartRelated;
/*     */ import com.sun.tools.internal.ws.wsdl.document.mime.MIMEPart;
/*     */ import com.sun.tools.internal.ws.wsdl.document.mime.MIMEXml;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import org.w3c.dom.Element;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class MIMEExtensionHandler
/*     */   extends AbstractExtensionHandler
/*     */ {
/*     */   public MIMEExtensionHandler(Map<String, AbstractExtensionHandler> extensionHandlerMap) {
/*  46 */     super(extensionHandlerMap);
/*     */   }
/*     */   
/*     */   public String getNamespaceURI() {
/*  50 */     return "http://schemas.xmlsoap.org/wsdl/mime/";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean doHandleExtension(TWSDLParserContext context, TWSDLExtensible parent, Element e) {
/*  58 */     if (parent.getWSDLElementName().equals(WSDLConstants.QNAME_OUTPUT))
/*  59 */       return handleInputOutputExtension(context, parent, e); 
/*  60 */     if (parent.getWSDLElementName().equals(WSDLConstants.QNAME_INPUT))
/*  61 */       return handleInputOutputExtension(context, parent, e); 
/*  62 */     if (parent.getWSDLElementName().equals(MIMEConstants.QNAME_PART)) {
/*  63 */       return handleMIMEPartExtension(context, parent, e);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*  68 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean handleInputOutputExtension(TWSDLParserContext context, TWSDLExtensible parent, Element e) {
/*  76 */     if (XmlUtil.matchesTagNS(e, MIMEConstants.QNAME_MULTIPART_RELATED)) {
/*  77 */       context.push();
/*  78 */       context.registerNamespaces(e);
/*     */       
/*  80 */       MIMEMultipartRelated mpr = new MIMEMultipartRelated(context.getLocation(e));
/*     */       
/*  82 */       for (Iterator iter = XmlUtil.getAllChildren(e); iter.hasNext(); ) {
/*  83 */         Element e2 = Util.nextElement(iter);
/*  84 */         if (e2 == null) {
/*     */           break;
/*     */         }
/*  87 */         if (XmlUtil.matchesTagNS(e2, MIMEConstants.QNAME_PART)) {
/*  88 */           context.push();
/*  89 */           context.registerNamespaces(e2);
/*     */           
/*  91 */           MIMEPart part = new MIMEPart(context.getLocation(e2));
/*     */ 
/*     */           
/*  94 */           String name = XmlUtil.getAttributeOrNull(e2, "name");
/*  95 */           if (name != null) {
/*  96 */             part.setName(name);
/*     */           }
/*     */           
/*  99 */           Iterator iter2 = XmlUtil.getAllChildren(e2);
/* 100 */           while (iter2.hasNext()) {
/*     */             
/* 102 */             Element e3 = Util.nextElement(iter2);
/* 103 */             if (e3 == null) {
/*     */               break;
/*     */             }
/* 106 */             AbstractExtensionHandler h = getExtensionHandlers().get(e3.getNamespaceURI());
/* 107 */             boolean handled = false;
/* 108 */             if (h != null) {
/* 109 */               handled = h.doHandleExtension(context, (TWSDLExtensible)part, e3);
/*     */             }
/*     */             
/* 112 */             if (!handled) {
/*     */               
/* 114 */               String required = XmlUtil.getAttributeNSOrNull(e3, "required", "http://schemas.xmlsoap.org/wsdl/");
/*     */ 
/*     */ 
/*     */               
/* 118 */               if (required != null && required
/* 119 */                 .equals("true")) {
/* 120 */                 Util.fail("parsing.requiredExtensibilityElement", e3
/*     */                     
/* 122 */                     .getTagName(), e3
/* 123 */                     .getNamespaceURI());
/*     */               }
/*     */             } 
/*     */           } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 134 */           mpr.add(part);
/* 135 */           context.pop();
/*     */           
/*     */           continue;
/*     */         } 
/*     */         
/* 140 */         Util.fail("parsing.invalidElement", e2
/*     */             
/* 142 */             .getTagName(), e2
/* 143 */             .getNamespaceURI());
/*     */       } 
/*     */ 
/*     */       
/* 147 */       parent.addExtension((TWSDLExtension)mpr);
/* 148 */       context.pop();
/*     */ 
/*     */ 
/*     */       
/* 152 */       return true;
/* 153 */     }  if (XmlUtil.matchesTagNS(e, MIMEConstants.QNAME_CONTENT)) {
/* 154 */       MIMEContent content = parseMIMEContent(context, e);
/* 155 */       parent.addExtension((TWSDLExtension)content);
/* 156 */       return true;
/* 157 */     }  if (XmlUtil.matchesTagNS(e, MIMEConstants.QNAME_MIME_XML)) {
/* 158 */       MIMEXml mimeXml = parseMIMEXml(context, e);
/* 159 */       parent.addExtension((TWSDLExtension)mimeXml);
/* 160 */       return true;
/*     */     } 
/* 162 */     Util.fail("parsing.invalidExtensionElement", e
/*     */         
/* 164 */         .getTagName(), e
/* 165 */         .getNamespaceURI());
/* 166 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean handleMIMEPartExtension(TWSDLParserContext context, TWSDLExtensible parent, Element e) {
/* 175 */     if (XmlUtil.matchesTagNS(e, MIMEConstants.QNAME_CONTENT)) {
/* 176 */       MIMEContent content = parseMIMEContent(context, e);
/* 177 */       parent.addExtension((TWSDLExtension)content);
/* 178 */       return true;
/* 179 */     }  if (XmlUtil.matchesTagNS(e, MIMEConstants.QNAME_MIME_XML)) {
/* 180 */       MIMEXml mimeXml = parseMIMEXml(context, e);
/* 181 */       parent.addExtension((TWSDLExtension)mimeXml);
/* 182 */       return true;
/*     */     } 
/* 184 */     Util.fail("parsing.invalidExtensionElement", e
/*     */         
/* 186 */         .getTagName(), e
/* 187 */         .getNamespaceURI());
/* 188 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   protected MIMEContent parseMIMEContent(TWSDLParserContext context, Element e) {
/* 193 */     context.push();
/* 194 */     context.registerNamespaces(e);
/*     */     
/* 196 */     MIMEContent content = new MIMEContent(context.getLocation(e));
/*     */     
/* 198 */     String part = XmlUtil.getAttributeOrNull(e, "part");
/* 199 */     if (part != null) {
/* 200 */       content.setPart(part);
/*     */     }
/*     */     
/* 203 */     String type = XmlUtil.getAttributeOrNull(e, "type");
/* 204 */     if (type != null) {
/* 205 */       content.setType(type);
/*     */     }
/*     */     
/* 208 */     context.pop();
/*     */     
/* 210 */     return content;
/*     */   }
/*     */   
/*     */   protected MIMEXml parseMIMEXml(TWSDLParserContext context, Element e) {
/* 214 */     context.push();
/* 215 */     context.registerNamespaces(e);
/*     */     
/* 217 */     MIMEXml mimeXml = new MIMEXml(context.getLocation(e));
/*     */     
/* 219 */     String part = XmlUtil.getAttributeOrNull(e, "part");
/* 220 */     if (part != null) {
/* 221 */       mimeXml.setPart(part);
/*     */     }
/*     */     
/* 224 */     context.pop();
/*     */     
/* 226 */     return mimeXml;
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\ws\wsdl\parser\MIMEExtensionHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */