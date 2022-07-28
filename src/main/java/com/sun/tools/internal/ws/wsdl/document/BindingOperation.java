/*     */ package com.sun.tools.internal.ws.wsdl.document;
/*     */ 
/*     */ import com.sun.tools.internal.ws.api.wsdl.TWSDLExtensible;
/*     */ import com.sun.tools.internal.ws.api.wsdl.TWSDLExtension;
/*     */ import com.sun.tools.internal.ws.wsdl.framework.Entity;
/*     */ import com.sun.tools.internal.ws.wsdl.framework.EntityAction;
/*     */ import com.sun.tools.internal.ws.wsdl.framework.ExtensibilityHelper;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class BindingOperation
/*     */   extends Entity
/*     */   implements TWSDLExtensible
/*     */ {
/*     */   private ExtensibilityHelper _helper;
/*     */   private Documentation _documentation;
/*     */   private String _name;
/*     */   private BindingInput _input;
/*     */   private BindingOutput _output;
/*     */   private List<BindingFault> _faults;
/*     */   private OperationStyle _style;
/*     */   private String _uniqueKey;
/*     */   private TWSDLExtensible parent;
/*     */   
/*     */   public BindingOperation(Locator locator) {
/*  47 */     super(locator);
/*  48 */     this._faults = new ArrayList<>();
/*  49 */     this._helper = new ExtensibilityHelper();
/*     */   }
/*     */   
/*     */   public String getName() {
/*  53 */     return this._name;
/*     */   }
/*     */   
/*     */   public void setName(String name) {
/*  57 */     this._name = name;
/*     */   }
/*     */   
/*     */   public String getUniqueKey() {
/*  61 */     if (this._uniqueKey == null) {
/*  62 */       StringBuilder sb = new StringBuilder();
/*  63 */       sb.append(this._name);
/*  64 */       sb.append(' ');
/*  65 */       if (this._input != null) {
/*  66 */         sb.append(this._input.getName());
/*     */       } else {
/*  68 */         sb.append(this._name);
/*  69 */         if (this._style == OperationStyle.REQUEST_RESPONSE) {
/*  70 */           sb.append("Request");
/*  71 */         } else if (this._style == OperationStyle.SOLICIT_RESPONSE) {
/*  72 */           sb.append("Response");
/*     */         } 
/*     */       } 
/*  75 */       sb.append(' ');
/*  76 */       if (this._output != null) {
/*  77 */         sb.append(this._output.getName());
/*     */       } else {
/*  79 */         sb.append(this._name);
/*  80 */         if (this._style == OperationStyle.SOLICIT_RESPONSE) {
/*  81 */           sb.append("Solicit");
/*  82 */         } else if (this._style == OperationStyle.REQUEST_RESPONSE) {
/*  83 */           sb.append("Response");
/*     */         } 
/*     */       } 
/*  86 */       this._uniqueKey = sb.toString();
/*     */     } 
/*     */     
/*  89 */     return this._uniqueKey;
/*     */   }
/*     */   
/*     */   public OperationStyle getStyle() {
/*  93 */     return this._style;
/*     */   }
/*     */   
/*     */   public void setStyle(OperationStyle s) {
/*  97 */     this._style = s;
/*     */   }
/*     */   
/*     */   public BindingInput getInput() {
/* 101 */     return this._input;
/*     */   }
/*     */   
/*     */   public void setInput(BindingInput i) {
/* 105 */     this._input = i;
/*     */   }
/*     */   
/*     */   public BindingOutput getOutput() {
/* 109 */     return this._output;
/*     */   }
/*     */   
/*     */   public void setOutput(BindingOutput o) {
/* 113 */     this._output = o;
/*     */   }
/*     */   
/*     */   public void addFault(BindingFault f) {
/* 117 */     this._faults.add(f);
/*     */   }
/*     */   
/*     */   public Iterable<BindingFault> faults() {
/* 121 */     return this._faults;
/*     */   }
/*     */ 
/*     */   
/*     */   public QName getElementName() {
/* 126 */     return WSDLConstants.QNAME_OPERATION;
/*     */   }
/*     */   
/*     */   public Documentation getDocumentation() {
/* 130 */     return this._documentation;
/*     */   }
/*     */   
/*     */   public void setDocumentation(Documentation d) {
/* 134 */     this._documentation = d;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getNameValue() {
/* 139 */     return getName();
/*     */   }
/*     */ 
/*     */   
/*     */   public String getNamespaceURI() {
/* 144 */     return (this.parent == null) ? null : this.parent.getNamespaceURI();
/*     */   }
/*     */ 
/*     */   
/*     */   public QName getWSDLElementName() {
/* 149 */     return getElementName();
/*     */   }
/*     */ 
/*     */   
/*     */   public void addExtension(TWSDLExtension e) {
/* 154 */     this._helper.addExtension(e);
/*     */   }
/*     */ 
/*     */   
/*     */   public Iterable<TWSDLExtension> extensions() {
/* 159 */     return this._helper.extensions();
/*     */   }
/*     */ 
/*     */   
/*     */   public TWSDLExtensible getParent() {
/* 164 */     return this.parent;
/*     */   }
/*     */ 
/*     */   
/*     */   public void withAllSubEntitiesDo(EntityAction action) {
/* 169 */     if (this._input != null) {
/* 170 */       action.perform(this._input);
/*     */     }
/* 172 */     if (this._output != null) {
/* 173 */       action.perform(this._output);
/*     */     }
/* 175 */     for (BindingFault _fault : this._faults) {
/* 176 */       action.perform(_fault);
/*     */     }
/* 178 */     this._helper.withAllSubEntitiesDo(action);
/*     */   }
/*     */   
/*     */   public void accept(WSDLDocumentVisitor visitor) throws Exception {
/* 182 */     visitor.preVisit(this);
/*     */     
/* 184 */     this._helper.accept(visitor);
/* 185 */     if (this._input != null) {
/* 186 */       this._input.accept(visitor);
/*     */     }
/* 188 */     if (this._output != null) {
/* 189 */       this._output.accept(visitor);
/*     */     }
/* 191 */     for (BindingFault _fault : this._faults) {
/* 192 */       _fault.accept(visitor);
/*     */     }
/* 194 */     visitor.postVisit(this);
/*     */   }
/*     */ 
/*     */   
/*     */   public void validateThis() {
/* 199 */     if (this._name == null) {
/* 200 */       failValidation("validation.missingRequiredAttribute", "name");
/*     */     }
/* 202 */     if (this._style == null) {
/* 203 */       failValidation("validation.missingRequiredProperty", "style");
/*     */     }
/*     */ 
/*     */     
/* 207 */     if (this._style == OperationStyle.ONE_WAY) {
/* 208 */       if (this._input == null) {
/* 209 */         failValidation("validation.missingRequiredSubEntity", "input");
/*     */       }
/* 211 */       if (this._output != null) {
/* 212 */         failValidation("validation.invalidSubEntity", "output");
/*     */       }
/* 214 */       if (this._faults != null && !this._faults.isEmpty()) {
/* 215 */         failValidation("validation.invalidSubEntity", "fault");
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
/*     */   public void setParent(TWSDLExtensible parent) {
/* 230 */     this.parent = parent;
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\ws\wsdl\document\BindingOperation.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */