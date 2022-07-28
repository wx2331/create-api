/*     */ package com.sun.tools.internal.ws.wsdl.document.jaxws;
/*     */ 
/*     */ import com.sun.tools.internal.ws.wsdl.framework.ExtensionImpl;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import javax.xml.namespace.QName;
/*     */ import org.w3c.dom.Element;
/*     */ import org.xml.sax.Locator;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class JAXWSBinding
/*     */   extends ExtensionImpl
/*     */ {
/*     */   private String wsdlNamespace;
/*     */   private String wsdlLocation;
/*     */   private String node;
/*     */   private String version;
/*     */   private CustomName jaxwsPackage;
/*     */   private List<Parameter> parameters;
/*     */   private Boolean enableWrapperStyle;
/*     */   private Boolean enableAsyncMapping;
/*     */   private Boolean enableMimeContentMapping;
/*     */   private Boolean isProvider;
/*     */   private Set<Element> jaxbBindings;
/*     */   private CustomName className;
/*     */   private CustomName methodName;
/*     */   
/*     */   public JAXWSBinding(Locator locator) {
/*  48 */     super(locator);
/*  49 */     this.jaxbBindings = new HashSet<>();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void validateThis() {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public QName getElementName() {
/*  67 */     return JAXWSBindingsConstants.JAXWS_BINDINGS;
/*     */   }
/*     */   
/*     */   public QName getWSDLElementName() {
/*  71 */     return getElementName();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addExtension(ExtensionImpl e) {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Iterable<ExtensionImpl> extensions() {
/*  87 */     return null;
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Boolean isEnableAsyncMapping() {
/* 108 */     return this.enableAsyncMapping;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setEnableAsyncMapping(Boolean enableAsyncMapping) {
/* 114 */     this.enableAsyncMapping = enableAsyncMapping;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Boolean isEnableMimeContentMapping() {
/* 120 */     return this.enableMimeContentMapping;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setEnableMimeContentMapping(Boolean enableMimeContentMapping) {
/* 126 */     this.enableMimeContentMapping = enableMimeContentMapping;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Boolean isEnableWrapperStyle() {
/* 132 */     return this.enableWrapperStyle;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setEnableWrapperStyle(Boolean enableWrapperStyle) {
/* 138 */     this.enableWrapperStyle = enableWrapperStyle;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public CustomName getJaxwsPackage() {
/* 144 */     return this.jaxwsPackage;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setJaxwsPackage(CustomName jaxwsPackage) {
/* 150 */     this.jaxwsPackage = jaxwsPackage;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getNode() {
/* 156 */     return this.node;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setNode(String node) {
/* 162 */     this.node = node;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getVersion() {
/* 168 */     return this.version;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setVersion(String version) {
/* 174 */     this.version = version;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getWsdlLocation() {
/* 181 */     return this.wsdlLocation;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setWsdlLocation(String wsdlLocation) {
/* 188 */     this.wsdlLocation = wsdlLocation;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getWsdlNamespace() {
/* 195 */     return this.wsdlNamespace;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setWsdlNamespace(String wsdlNamespace) {
/* 203 */     this.wsdlNamespace = wsdlNamespace;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Set<Element> getJaxbBindings() {
/* 210 */     return this.jaxbBindings;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addJaxbBindings(Element jaxbBinding) {
/* 217 */     if (this.jaxbBindings == null)
/*     */       return; 
/* 219 */     this.jaxbBindings.add(jaxbBinding);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Boolean isProvider() {
/* 227 */     return this.isProvider;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setProvider(Boolean isProvider) {
/* 233 */     this.isProvider = isProvider;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CustomName getMethodName() {
/* 240 */     return this.methodName;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMethodName(CustomName methodName) {
/* 246 */     this.methodName = methodName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Iterator<Parameter> parameters() {
/* 253 */     return this.parameters.iterator();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addParameter(Parameter parameter) {
/* 260 */     if (this.parameters == null)
/* 261 */       this.parameters = new ArrayList<>(); 
/* 262 */     this.parameters.add(parameter);
/*     */   }
/*     */   
/*     */   public String getParameterName(String msgName, String wsdlPartName, QName element, boolean wrapperStyle) {
/* 266 */     if (msgName == null || wsdlPartName == null || element == null || this.parameters == null)
/* 267 */       return null; 
/* 268 */     for (Parameter param : this.parameters) {
/* 269 */       if (param.getMessageName().equals(msgName) && param.getPart().equals(wsdlPartName)) {
/* 270 */         if (wrapperStyle && param.getElement() != null) {
/* 271 */           if (param.getElement().equals(element))
/* 272 */             return param.getName();  continue;
/* 273 */         }  if (!wrapperStyle) {
/* 274 */           return param.getName();
/*     */         }
/*     */       } 
/*     */     } 
/* 278 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CustomName getClassName() {
/* 285 */     return this.className;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setClassName(CustomName className) {
/* 291 */     this.className = className;
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\ws\wsdl\document\jaxws\JAXWSBinding.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */