/*     */ package com.sun.tools.internal.ws.wsdl.document;
/*     */ 
/*     */ import com.sun.codemodel.internal.JClass;
/*     */ import com.sun.tools.internal.ws.api.wsdl.TWSDLExtensible;
/*     */ import com.sun.tools.internal.ws.api.wsdl.TWSDLExtension;
/*     */ import com.sun.tools.internal.ws.api.wsdl.TWSDLOperation;
/*     */ import com.sun.tools.internal.ws.wsdl.framework.Entity;
/*     */ import com.sun.tools.internal.ws.wsdl.framework.EntityAction;
/*     */ import com.sun.tools.internal.ws.wsdl.framework.ExtensibilityHelper;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import javax.xml.namespace.QName;
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
/*     */ public class Operation
/*     */   extends Entity
/*     */   implements TWSDLOperation
/*     */ {
/*     */   private TWSDLExtensible parent;
/*     */   private Documentation _documentation;
/*     */   private String _name;
/*     */   private Input _input;
/*     */   private Output _output;
/*     */   private List<Fault> _faults;
/*     */   private OperationStyle _style;
/*     */   private String _parameterOrder;
/*     */   private String _uniqueKey;
/*     */   private ExtensibilityHelper _helper;
/*     */   private final Map<String, JClass> faultClassMap;
/*     */   private final Map<String, JClass> unmodifiableFaultClassMap;
/*     */   
/*     */   public Operation(Locator locator) {
/*  48 */     super(locator);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 253 */     this.faultClassMap = new HashMap<>();
/* 254 */     this.unmodifiableFaultClassMap = Collections.unmodifiableMap(this.faultClassMap);
/*     */     this._faults = new ArrayList<>();
/*     */     this._helper = new ExtensibilityHelper();
/*     */   }
/*     */   
/*     */   public String getName() {
/*     */     return this._name;
/*     */   }
/*     */   
/*     */   public void setName(String name) {
/*     */     this._name = name;
/*     */   }
/*     */   
/*     */   public String getUniqueKey() {
/*     */     if (this._uniqueKey == null) {
/*     */       StringBuffer sb = new StringBuffer();
/*     */       sb.append(this._name);
/*     */       sb.append(' ');
/*     */       if (this._input != null) {
/*     */         sb.append(this._input.getName());
/*     */       } else {
/*     */         sb.append(this._name);
/*     */         if (this._style == OperationStyle.REQUEST_RESPONSE) {
/*     */           sb.append("Request");
/*     */         } else if (this._style == OperationStyle.SOLICIT_RESPONSE) {
/*     */           sb.append("Response");
/*     */         } 
/*     */       } 
/*     */       sb.append(' ');
/*     */       if (this._output != null) {
/*     */         sb.append(this._output.getName());
/*     */       } else {
/*     */         sb.append(this._name);
/*     */         if (this._style == OperationStyle.SOLICIT_RESPONSE) {
/*     */           sb.append("Solicit");
/*     */         } else if (this._style == OperationStyle.REQUEST_RESPONSE) {
/*     */           sb.append("Response");
/*     */         } 
/*     */       } 
/*     */       this._uniqueKey = sb.toString();
/*     */     } 
/*     */     return this._uniqueKey;
/*     */   }
/*     */   
/*     */   public OperationStyle getStyle() {
/*     */     return this._style;
/*     */   }
/*     */   
/*     */   public void setStyle(OperationStyle s) {
/*     */     this._style = s;
/*     */   }
/*     */   
/*     */   public Input getInput() {
/*     */     return this._input;
/*     */   }
/*     */   
/*     */   public void setInput(Input i) {
/*     */     this._input = i;
/*     */   }
/*     */   
/*     */   public Output getOutput() {
/*     */     return this._output;
/*     */   }
/*     */   
/*     */   public void setOutput(Output o) {
/*     */     this._output = o;
/*     */   }
/*     */   
/*     */   public void addFault(Fault f) {
/*     */     this._faults.add(f);
/*     */   }
/*     */   
/*     */   public Iterable<Fault> faults() {
/*     */     return this._faults;
/*     */   }
/*     */   
/*     */   public String getParameterOrder() {
/*     */     return this._parameterOrder;
/*     */   }
/*     */   
/*     */   public void setParameterOrder(String s) {
/*     */     this._parameterOrder = s;
/*     */   }
/*     */   
/*     */   public QName getElementName() {
/*     */     return WSDLConstants.QNAME_OPERATION;
/*     */   }
/*     */   
/*     */   public Documentation getDocumentation() {
/*     */     return this._documentation;
/*     */   }
/*     */   
/*     */   public void setDocumentation(Documentation d) {
/*     */     this._documentation = d;
/*     */   }
/*     */   
/*     */   public void withAllSubEntitiesDo(EntityAction action) {
/*     */     super.withAllSubEntitiesDo(action);
/*     */     if (this._input != null)
/*     */       action.perform(this._input); 
/*     */     if (this._output != null)
/*     */       action.perform(this._output); 
/*     */     for (Fault _fault : this._faults)
/*     */       action.perform(_fault); 
/*     */     this._helper.withAllSubEntitiesDo(action);
/*     */   }
/*     */   
/*     */   public void accept(WSDLDocumentVisitor visitor) throws Exception {
/*     */     visitor.preVisit(this);
/*     */     if (this._input != null)
/*     */       this._input.accept(visitor); 
/*     */     if (this._output != null)
/*     */       this._output.accept(visitor); 
/*     */     for (Fault _fault : this._faults)
/*     */       _fault.accept(visitor); 
/*     */     visitor.postVisit(this);
/*     */   }
/*     */   
/*     */   public void validateThis() {
/*     */     if (this._name == null)
/*     */       failValidation("validation.missingRequiredAttribute", "name"); 
/*     */     if (this._style == null)
/*     */       failValidation("validation.missingRequiredProperty", "style"); 
/*     */     if (this._style == OperationStyle.ONE_WAY) {
/*     */       if (this._input == null)
/*     */         failValidation("validation.missingRequiredSubEntity", "input"); 
/*     */       if (this._output != null)
/*     */         failValidation("validation.invalidSubEntity", "output"); 
/*     */       if (this._faults != null && this._faults.size() != 0)
/*     */         failValidation("validation.invalidSubEntity", "fault"); 
/*     */     } else if (this._style == OperationStyle.NOTIFICATION && this._parameterOrder != null) {
/*     */       failValidation("validation.invalidAttribute", "parameterOrder");
/*     */     } 
/*     */   }
/*     */   
/*     */   public String getNameValue() {
/*     */     return getName();
/*     */   }
/*     */   
/*     */   public String getNamespaceURI() {
/*     */     return this.parent.getNamespaceURI();
/*     */   }
/*     */   
/*     */   public QName getWSDLElementName() {
/*     */     return getElementName();
/*     */   }
/*     */   
/*     */   public void addExtension(TWSDLExtension e) {
/*     */     this._helper.addExtension(e);
/*     */   }
/*     */   
/*     */   public Iterable<? extends TWSDLExtension> extensions() {
/*     */     return this._helper.extensions();
/*     */   }
/*     */   
/*     */   public TWSDLExtensible getParent() {
/*     */     return this.parent;
/*     */   }
/*     */   
/*     */   public void setParent(TWSDLExtensible parent) {
/*     */     this.parent = parent;
/*     */   }
/*     */   
/*     */   public Map<String, JClass> getFaults() {
/*     */     return this.unmodifiableFaultClassMap;
/*     */   }
/*     */   
/*     */   public void putFault(String faultName, JClass exception) {
/*     */     this.faultClassMap.put(faultName, exception);
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\ws\wsdl\document\Operation.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */