/*     */ package com.sun.tools.internal.ws.processor.model;
/*     */ 
/*     */ import java.util.Iterator;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ExtendedModelVisitor
/*     */ {
/*     */   public void visit(Model model) throws Exception {
/*  41 */     preVisit(model);
/*  42 */     for (Service service : model.getServices()) {
/*  43 */       preVisit(service);
/*  44 */       for (Port port : service.getPorts()) {
/*  45 */         preVisit(port);
/*  46 */         if (shouldVisit(port)) {
/*  47 */           for (Operation operation : port.getOperations()) {
/*  48 */             preVisit(operation);
/*  49 */             Request request = operation.getRequest();
/*  50 */             if (request != null) {
/*  51 */               preVisit(request);
/*  52 */               Iterator<Block> iterator2 = request.getHeaderBlocks();
/*  53 */               while (iterator2.hasNext()) {
/*     */                 
/*  55 */                 Block block = iterator2.next();
/*  56 */                 visitHeaderBlock(block);
/*     */               } 
/*  58 */               Iterator<Block> iterator1 = request.getBodyBlocks();
/*  59 */               while (iterator1.hasNext()) {
/*     */                 
/*  61 */                 Block block = iterator1.next();
/*  62 */                 visitBodyBlock(block);
/*     */               } 
/*  64 */               Iterator<Parameter> iterator = request.getParameters();
/*  65 */               while (iterator.hasNext()) {
/*     */                 
/*  67 */                 Parameter parameter = iterator.next();
/*  68 */                 visit(parameter);
/*     */               } 
/*  70 */               postVisit(request);
/*     */             } 
/*     */             
/*  73 */             Response response = operation.getResponse();
/*  74 */             if (response != null) {
/*  75 */               preVisit(response);
/*  76 */               Iterator<Block> iterator2 = response.getHeaderBlocks();
/*  77 */               while (iterator2.hasNext()) {
/*     */                 
/*  79 */                 Block block = iterator2.next();
/*  80 */                 visitHeaderBlock(block);
/*     */               } 
/*  82 */               Iterator<Block> iterator1 = response.getBodyBlocks();
/*  83 */               while (iterator1.hasNext()) {
/*     */                 
/*  85 */                 Block block = iterator1.next();
/*  86 */                 visitBodyBlock(block);
/*     */               } 
/*  88 */               Iterator<Parameter> iterator = response.getParameters();
/*  89 */               while (iterator.hasNext()) {
/*     */                 
/*  91 */                 Parameter parameter = iterator.next();
/*  92 */                 visit(parameter);
/*     */               } 
/*  94 */               postVisit(response);
/*     */             } 
/*     */             
/*  97 */             Iterator<Fault> iter4 = operation.getFaults();
/*  98 */             while (iter4.hasNext()) {
/*     */               
/* 100 */               Fault fault = iter4.next();
/* 101 */               preVisit(fault);
/* 102 */               visitFaultBlock(fault.getBlock());
/* 103 */               postVisit(fault);
/*     */             } 
/* 105 */             postVisit(operation);
/*     */           } 
/*     */         }
/* 108 */         postVisit(port);
/*     */       } 
/* 110 */       postVisit(service);
/*     */     } 
/* 112 */     postVisit(model);
/*     */   }
/*     */   
/*     */   protected boolean shouldVisit(Port port) {
/* 116 */     return true;
/*     */   }
/*     */   
/*     */   protected void preVisit(Model model) throws Exception {}
/*     */   
/*     */   protected void postVisit(Model model) throws Exception {}
/*     */   
/*     */   protected void preVisit(Service service) throws Exception {}
/*     */   
/*     */   protected void postVisit(Service service) throws Exception {}
/*     */   
/*     */   protected void preVisit(Port port) throws Exception {}
/*     */   
/*     */   protected void postVisit(Port port) throws Exception {}
/*     */   
/*     */   protected void preVisit(Operation operation) throws Exception {}
/*     */   
/*     */   protected void postVisit(Operation operation) throws Exception {}
/*     */   
/*     */   protected void preVisit(Request request) throws Exception {}
/*     */   
/*     */   protected void postVisit(Request request) throws Exception {}
/*     */   
/*     */   protected void preVisit(Response response) throws Exception {}
/*     */   
/*     */   protected void postVisit(Response response) throws Exception {}
/*     */   
/*     */   protected void preVisit(Fault fault) throws Exception {}
/*     */   
/*     */   protected void postVisit(Fault fault) throws Exception {}
/*     */   
/*     */   protected void visitBodyBlock(Block block) throws Exception {}
/*     */   
/*     */   protected void visitHeaderBlock(Block block) throws Exception {}
/*     */   
/*     */   protected void visitFaultBlock(Block block) throws Exception {}
/*     */   
/*     */   protected void visit(Parameter parameter) throws Exception {}
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\ws\processor\model\ExtendedModelVisitor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */