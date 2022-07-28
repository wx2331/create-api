/*     */ package com.sun.tools.internal.ws.processor.model;
/*     */ 
/*     */ import com.sun.tools.internal.ws.processor.model.java.JavaMethod;
/*     */ import com.sun.tools.internal.ws.wsdl.document.soap.SOAPStyle;
/*     */ import com.sun.tools.internal.ws.wsdl.document.soap.SOAPUse;
/*     */ import com.sun.tools.internal.ws.wsdl.framework.Entity;
/*     */ import com.sun.xml.internal.ws.spi.db.BindingHelper;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.Set;
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
/*     */ public class Operation
/*     */   extends ModelObject
/*     */ {
/*     */   private String customizedName;
/*     */   private boolean _isWrapped;
/*     */   private QName _name;
/*     */   private String _uniqueName;
/*     */   private Request _request;
/*     */   private Response _response;
/*     */   private JavaMethod _javaMethod;
/*     */   private String _soapAction;
/*     */   private SOAPStyle _style;
/*     */   private SOAPUse _use;
/*     */   private Set<String> _faultNames;
/*     */   private Set<Fault> _faults;
/*     */   private com.sun.tools.internal.ws.wsdl.document.Operation wsdlOperation;
/*     */   
/*     */   public Operation(Entity entity) {
/*  46 */     super(entity);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 244 */     this._isWrapped = true;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 251 */     this._style = SOAPStyle.DOCUMENT;
/* 252 */     this._use = SOAPUse.LITERAL; } public Operation(QName name, Entity entity) { super(entity); this._isWrapped = true; this._style = SOAPStyle.DOCUMENT; this._use = SOAPUse.LITERAL;
/*     */     this._name = name;
/*     */     this._uniqueName = name.getLocalPart();
/*     */     this._faultNames = new HashSet<>();
/*     */     this._faults = new HashSet<>(); }
/*     */ 
/*     */   
/*     */   public Operation(Operation operation, Entity entity) {
/*     */     this(operation._name, entity);
/*     */     this._style = operation._style;
/*     */     this._use = operation._use;
/*     */     this.customizedName = operation.customizedName;
/*     */   }
/*     */   
/*     */   public QName getName() {
/*     */     return this._name;
/*     */   }
/*     */   
/*     */   public void setName(QName n) {
/*     */     this._name = n;
/*     */   }
/*     */   
/*     */   public String getUniqueName() {
/*     */     return this._uniqueName;
/*     */   }
/*     */   
/*     */   public void setUniqueName(String s) {
/*     */     this._uniqueName = s;
/*     */   }
/*     */   
/*     */   public Request getRequest() {
/*     */     return this._request;
/*     */   }
/*     */   
/*     */   public void setRequest(Request r) {
/*     */     this._request = r;
/*     */   }
/*     */   
/*     */   public Response getResponse() {
/*     */     return this._response;
/*     */   }
/*     */   
/*     */   public void setResponse(Response r) {
/*     */     this._response = r;
/*     */   }
/*     */   
/*     */   public boolean isOverloaded() {
/*     */     return !this._name.getLocalPart().equals(this._uniqueName);
/*     */   }
/*     */   
/*     */   public void addFault(Fault f) {
/*     */     if (this._faultNames.contains(f.getName()))
/*     */       throw new ModelException("model.uniqueness", new Object[0]); 
/*     */     this._faultNames.add(f.getName());
/*     */     this._faults.add(f);
/*     */   }
/*     */   
/*     */   public Iterator<Fault> getFaults() {
/*     */     return this._faults.iterator();
/*     */   }
/*     */   
/*     */   public Set<Fault> getFaultsSet() {
/*     */     return this._faults;
/*     */   }
/*     */   
/*     */   public void setFaultsSet(Set<Fault> s) {
/*     */     this._faults = s;
/*     */     initializeFaultNames();
/*     */   }
/*     */   
/*     */   private void initializeFaultNames() {
/*     */     this._faultNames = new HashSet<>();
/*     */     if (this._faults != null)
/*     */       for (Iterator<Fault> iter = this._faults.iterator(); iter.hasNext(); ) {
/*     */         Fault f = iter.next();
/*     */         if (f.getName() != null && this._faultNames.contains(f.getName()))
/*     */           throw new ModelException("model.uniqueness", new Object[0]); 
/*     */         this._faultNames.add(f.getName());
/*     */       }  
/*     */   }
/*     */   
/*     */   public Iterator<Fault> getAllFaults() {
/*     */     Set<Fault> allFaults = getAllFaultsSet();
/*     */     return allFaults.iterator();
/*     */   }
/*     */   
/*     */   public Set<Fault> getAllFaultsSet() {
/*     */     Set<Fault> transSet = new HashSet();
/*     */     transSet.addAll(this._faults);
/*     */     Iterator<Fault> iter = this._faults.iterator();
/*     */     while (iter.hasNext()) {
/*     */       Set<? extends Fault> tmpSet = ((Fault)iter.next()).getAllFaultsSet();
/*     */       transSet.addAll(tmpSet);
/*     */     } 
/*     */     return transSet;
/*     */   }
/*     */   
/*     */   public int getFaultCount() {
/*     */     return this._faults.size();
/*     */   }
/*     */   
/*     */   public Set<Block> getAllFaultBlocks() {
/*     */     Set<Block> blocks = new HashSet<>();
/*     */     Iterator<Fault> faults = this._faults.iterator();
/*     */     while (faults.hasNext()) {
/*     */       Fault f = faults.next();
/*     */       blocks.add(f.getBlock());
/*     */     } 
/*     */     return blocks;
/*     */   }
/*     */   
/*     */   public JavaMethod getJavaMethod() {
/*     */     return this._javaMethod;
/*     */   }
/*     */   
/*     */   public void setJavaMethod(JavaMethod i) {
/*     */     this._javaMethod = i;
/*     */   }
/*     */   
/*     */   public String getSOAPAction() {
/*     */     return this._soapAction;
/*     */   }
/*     */   
/*     */   public void setSOAPAction(String s) {
/*     */     this._soapAction = s;
/*     */   }
/*     */   
/*     */   public SOAPStyle getStyle() {
/*     */     return this._style;
/*     */   }
/*     */   
/*     */   public void setStyle(SOAPStyle s) {
/*     */     this._style = s;
/*     */   }
/*     */   
/*     */   public SOAPUse getUse() {
/*     */     return this._use;
/*     */   }
/*     */   
/*     */   public void setUse(SOAPUse u) {
/*     */     this._use = u;
/*     */   }
/*     */   
/*     */   public boolean isWrapped() {
/*     */     return this._isWrapped;
/*     */   }
/*     */   
/*     */   public void setWrapped(boolean isWrapped) {
/*     */     this._isWrapped = isWrapped;
/*     */   }
/*     */   
/*     */   public void accept(ModelVisitor visitor) throws Exception {
/*     */     visitor.visit(this);
/*     */   }
/*     */   
/*     */   public void setCustomizedName(String name) {
/*     */     this.customizedName = name;
/*     */   }
/*     */   
/*     */   public String getCustomizedName() {
/*     */     return this.customizedName;
/*     */   }
/*     */   
/*     */   public String getJavaMethodName() {
/*     */     if (this._javaMethod != null)
/*     */       return this._javaMethod.getName(); 
/*     */     if (this.customizedName != null)
/*     */       return this.customizedName; 
/*     */     return BindingHelper.mangleNameToVariableName(this._name.getLocalPart());
/*     */   }
/*     */   
/*     */   public com.sun.tools.internal.ws.wsdl.document.Operation getWSDLPortTypeOperation() {
/*     */     return this.wsdlOperation;
/*     */   }
/*     */   
/*     */   public void setWSDLPortTypeOperation(com.sun.tools.internal.ws.wsdl.document.Operation wsdlOperation) {
/*     */     this.wsdlOperation = wsdlOperation;
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\ws\processor\model\Operation.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */