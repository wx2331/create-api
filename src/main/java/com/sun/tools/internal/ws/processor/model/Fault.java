/*     */ package com.sun.tools.internal.ws.processor.model;
/*     */ 
/*     */ import com.sun.codemodel.internal.JClass;
/*     */ import com.sun.tools.internal.ws.processor.model.java.JavaException;
/*     */ import com.sun.tools.internal.ws.wsdl.framework.Entity;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Fault
/*     */   extends ModelObject
/*     */ {
/*     */   private boolean wsdlException;
/*     */   private String name;
/*     */   private Block block;
/*     */   private JavaException javaException;
/*     */   private Set subfaults;
/*     */   private QName elementName;
/*     */   private String javaMemberName;
/*     */   private JClass exceptionClass;
/*     */   private String wsdlFaultName;
/*     */   
/*     */   public Fault(Entity entity) {
/*  44 */     super(entity);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 152 */     this.wsdlException = true;
/*     */ 
/*     */ 
/*     */     
/* 156 */     this.subfaults = new HashSet();
/* 157 */     this.elementName = null;
/* 158 */     this.javaMemberName = null; } public Fault(String name, Entity entity) { super(entity); this.wsdlException = true; this.subfaults = new HashSet(); this.elementName = null; this.javaMemberName = null; this.name = name; }
/*     */   public String getName() { return this.name; }
/*     */   public void setName(String s) { this.name = s; }
/*     */   public Block getBlock() { return this.block; }
/* 162 */   public void setBlock(Block b) { this.block = b; } public JavaException getJavaException() { return this.javaException; } public void setJavaException(JavaException e) { this.javaException = e; } public void accept(ModelVisitor visitor) throws Exception { visitor.visit(this); } public Iterator getSubfaults() { if (this.subfaults.isEmpty()) return null;  return this.subfaults.iterator(); } public Set getSubfaultsSet() { return this.subfaults; } public void setSubfaultsSet(Set s) { this.subfaults = s; } public String getWsdlFaultName() { return this.wsdlFaultName; }
/*     */   public Iterator getAllFaults() { Set allFaults = getAllFaultsSet(); if (allFaults.isEmpty()) return null;  return allFaults.iterator(); }
/*     */   public Set getAllFaultsSet() { Set transSet = new HashSet(); Iterator<Fault> iter = this.subfaults.iterator(); while (iter.hasNext()) transSet.addAll(((Fault)iter.next()).getAllFaultsSet());  transSet.addAll(this.subfaults); return transSet; }
/*     */   public QName getElementName() { return this.elementName; }
/* 166 */   public void setElementName(QName elementName) { this.elementName = elementName; } public String getJavaMemberName() { return this.javaMemberName; } public void setJavaMemberName(String javaMemberName) { this.javaMemberName = javaMemberName; } public boolean isWsdlException() { return this.wsdlException; } public void setWsdlException(boolean wsdlFault) { this.wsdlException = wsdlFault; } public void setExceptionClass(JClass ex) { this.exceptionClass = ex; } public JClass getExceptionClass() { return this.exceptionClass; } public void setWsdlFaultName(String wsdlFaultName) { this.wsdlFaultName = wsdlFaultName; }
/*     */ 
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\ws\processor\model\Fault.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */