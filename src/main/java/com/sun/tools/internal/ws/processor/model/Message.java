/*     */ package com.sun.tools.internal.ws.processor.model;
/*     */ 
/*     */ import com.sun.tools.internal.ws.resources.ModelMessages;
/*     */ import com.sun.tools.internal.ws.wscompile.AbortException;
/*     */ import com.sun.tools.internal.ws.wscompile.ErrorReceiver;
/*     */ import com.sun.tools.internal.ws.wsdl.framework.Entity;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
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
/*     */ public abstract class Message
/*     */   extends ModelObject
/*     */ {
/*     */   private Map<QName, Block> _attachmentBlocks;
/*     */   private Map<QName, Block> _bodyBlocks;
/*     */   private Map<QName, Block> _headerBlocks;
/*     */   private Map<QName, Block> _unboundBlocks;
/*     */   private List<Parameter> _parameters;
/*     */   private Map<String, Parameter> _parametersByName;
/*     */   
/*     */   protected Message(com.sun.tools.internal.ws.wsdl.document.Message entity, ErrorReceiver receiver) {
/*  42 */     super((Entity)entity);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 228 */     this._attachmentBlocks = new HashMap<>();
/* 229 */     this._bodyBlocks = new HashMap<>();
/* 230 */     this._headerBlocks = new HashMap<>();
/* 231 */     this._unboundBlocks = new HashMap<>();
/* 232 */     this._parameters = new ArrayList<>();
/* 233 */     this._parametersByName = new HashMap<>();
/*     */     setErrorReceiver(receiver);
/*     */   }
/*     */   
/*     */   public void addBodyBlock(Block b) {
/*     */     if (this._bodyBlocks.containsKey(b.getName())) {
/*     */       this.errorReceiver.error(getEntity().getLocator(), ModelMessages.MODEL_PART_NOT_UNIQUE(((com.sun.tools.internal.ws.wsdl.document.Message)getEntity()).getName(), b.getName()));
/*     */       throw new AbortException();
/*     */     } 
/*     */     this._bodyBlocks.put(b.getName(), b);
/*     */     b.setLocation(1);
/*     */   }
/*     */   
/*     */   public Iterator<Block> getBodyBlocks() {
/*     */     return this._bodyBlocks.values().iterator();
/*     */   }
/*     */   
/*     */   public int getBodyBlockCount() {
/*     */     return this._bodyBlocks.size();
/*     */   }
/*     */   
/*     */   public Map<QName, Block> getBodyBlocksMap() {
/*     */     return this._bodyBlocks;
/*     */   }
/*     */   
/*     */   public void setBodyBlocksMap(Map<QName, Block> m) {
/*     */     this._bodyBlocks = m;
/*     */   }
/*     */   
/*     */   public boolean isBodyEmpty() {
/*     */     return getBodyBlocks().hasNext();
/*     */   }
/*     */   
/*     */   public boolean isBodyEncoded() {
/*     */     boolean isEncoded = false;
/*     */     for (Iterator<Block> iter = getBodyBlocks(); iter.hasNext(); ) {
/*     */       Block bodyBlock = iter.next();
/*     */       if (bodyBlock.getType().isSOAPType())
/*     */         isEncoded = true; 
/*     */     } 
/*     */     return isEncoded;
/*     */   }
/*     */   
/*     */   public void addHeaderBlock(Block b) {
/*     */     if (this._headerBlocks.containsKey(b.getName())) {
/*     */       this.errorReceiver.error(getEntity().getLocator(), ModelMessages.MODEL_PART_NOT_UNIQUE(((com.sun.tools.internal.ws.wsdl.document.Message)getEntity()).getName(), b.getName()));
/*     */       throw new AbortException();
/*     */     } 
/*     */     this._headerBlocks.put(b.getName(), b);
/*     */     b.setLocation(2);
/*     */   }
/*     */   
/*     */   public Iterator<Block> getHeaderBlocks() {
/*     */     return this._headerBlocks.values().iterator();
/*     */   }
/*     */   
/*     */   public Collection<Block> getHeaderBlockCollection() {
/*     */     return this._headerBlocks.values();
/*     */   }
/*     */   
/*     */   public int getHeaderBlockCount() {
/*     */     return this._headerBlocks.size();
/*     */   }
/*     */   
/*     */   public Map<QName, Block> getHeaderBlocksMap() {
/*     */     return this._headerBlocks;
/*     */   }
/*     */   
/*     */   public void setHeaderBlocksMap(Map<QName, Block> m) {
/*     */     this._headerBlocks = m;
/*     */   }
/*     */   
/*     */   public void addAttachmentBlock(Block b) {
/*     */     if (this._attachmentBlocks.containsKey(b.getName())) {
/*     */       this.errorReceiver.error(getEntity().getLocator(), ModelMessages.MODEL_PART_NOT_UNIQUE(((com.sun.tools.internal.ws.wsdl.document.Message)getEntity()).getName(), b.getName()));
/*     */       throw new AbortException();
/*     */     } 
/*     */     this._attachmentBlocks.put(b.getName(), b);
/*     */     b.setLocation(3);
/*     */   }
/*     */   
/*     */   public void addUnboundBlock(Block b) {
/*     */     if (this._unboundBlocks.containsKey(b.getName()))
/*     */       return; 
/*     */     this._unboundBlocks.put(b.getName(), b);
/*     */     b.setLocation(0);
/*     */   }
/*     */   
/*     */   public Iterator<Block> getUnboundBlocks() {
/*     */     return this._unboundBlocks.values().iterator();
/*     */   }
/*     */   
/*     */   public Map<QName, Block> getUnboundBlocksMap() {
/*     */     return this._unboundBlocks;
/*     */   }
/*     */   
/*     */   public int getUnboundBlocksCount() {
/*     */     return this._unboundBlocks.size();
/*     */   }
/*     */   
/*     */   public void setUnboundBlocksMap(Map<QName, Block> m) {
/*     */     this._unboundBlocks = m;
/*     */   }
/*     */   
/*     */   public Iterator<Block> getAttachmentBlocks() {
/*     */     return this._attachmentBlocks.values().iterator();
/*     */   }
/*     */   
/*     */   public int getAttachmentBlockCount() {
/*     */     return this._attachmentBlocks.size();
/*     */   }
/*     */   
/*     */   public Map<QName, Block> getAttachmentBlocksMap() {
/*     */     return this._attachmentBlocks;
/*     */   }
/*     */   
/*     */   public void setAttachmentBlocksMap(Map<QName, Block> m) {
/*     */     this._attachmentBlocks = m;
/*     */   }
/*     */   
/*     */   public void addParameter(Parameter p) {
/*     */     if (this._parametersByName.containsKey(p.getName())) {
/*     */       this.errorReceiver.error(getEntity().getLocator(), ModelMessages.MODEL_PARAMETER_NOTUNIQUE(p.getName(), p.getName()));
/*     */       throw new AbortException();
/*     */     } 
/*     */     this._parameters.add(p);
/*     */     String name = (p.getCustomName() != null) ? p.getCustomName() : p.getName();
/*     */     this._parametersByName.put(name, p);
/*     */   }
/*     */   
/*     */   public Parameter getParameterByName(String name) {
/*     */     if (this._parametersByName.size() != this._parameters.size())
/*     */       initializeParametersByName(); 
/*     */     return this._parametersByName.get(name);
/*     */   }
/*     */   
/*     */   public Iterator<Parameter> getParameters() {
/*     */     return this._parameters.iterator();
/*     */   }
/*     */   
/*     */   public List<Parameter> getParametersList() {
/*     */     return this._parameters;
/*     */   }
/*     */   
/*     */   public void setParametersList(List<Parameter> l) {
/*     */     this._parameters = l;
/*     */   }
/*     */   
/*     */   private void initializeParametersByName() {
/*     */     this._parametersByName = new HashMap<>();
/*     */     if (this._parameters != null)
/*     */       for (Iterator<Parameter> iter = this._parameters.iterator(); iter.hasNext(); ) {
/*     */         Parameter param = iter.next();
/*     */         if (param.getName() != null && this._parametersByName.containsKey(param.getName())) {
/*     */           this.errorReceiver.error(getEntity().getLocator(), ModelMessages.MODEL_PARAMETER_NOTUNIQUE(param.getName(), param.getName()));
/*     */           throw new AbortException();
/*     */         } 
/*     */         this._parametersByName.put(param.getName(), param);
/*     */       }  
/*     */   }
/*     */   
/*     */   public Set<Block> getAllBlocks() {
/*     */     Set<Block> blocks = new HashSet<>();
/*     */     blocks.addAll(this._bodyBlocks.values());
/*     */     blocks.addAll(this._headerBlocks.values());
/*     */     blocks.addAll(this._attachmentBlocks.values());
/*     */     return blocks;
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\ws\processor\model\Message.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */