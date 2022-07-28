/*     */ package com.sun.xml.internal.rngom.digested;
/*     */ 
/*     */ import javax.xml.stream.XMLStreamException;
/*     */ import javax.xml.stream.XMLStreamWriter;
/*     */ import org.w3c.dom.Attr;
/*     */ import org.w3c.dom.CDATASection;
/*     */ import org.w3c.dom.Comment;
/*     */ import org.w3c.dom.Document;
/*     */ import org.w3c.dom.DocumentFragment;
/*     */ import org.w3c.dom.Element;
/*     */ import org.w3c.dom.EntityReference;
/*     */ import org.w3c.dom.NamedNodeMap;
/*     */ import org.w3c.dom.Node;
/*     */ import org.w3c.dom.NodeList;
/*     */ import org.w3c.dom.ProcessingInstruction;
/*     */ import org.w3c.dom.Text;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class DOMPrinter
/*     */ {
/*     */   protected XMLStreamWriter out;
/*     */   
/*     */   public DOMPrinter(XMLStreamWriter out) {
/*  73 */     this.out = out;
/*     */   }
/*     */   
/*     */   public void print(Node node) throws XMLStreamException {
/*  77 */     switch (node.getNodeType()) {
/*     */       case 9:
/*  79 */         visitDocument((Document)node);
/*     */       
/*     */       case 11:
/*  82 */         visitDocumentFragment((DocumentFragment)node);
/*     */       
/*     */       case 1:
/*  85 */         visitElement((Element)node);
/*     */       
/*     */       case 3:
/*  88 */         visitText((Text)node);
/*     */       
/*     */       case 4:
/*  91 */         visitCDATASection((CDATASection)node);
/*     */       
/*     */       case 7:
/*  94 */         visitProcessingInstruction((ProcessingInstruction)node);
/*     */       
/*     */       case 5:
/*  97 */         visitReference((EntityReference)node);
/*     */       
/*     */       case 8:
/* 100 */         visitComment((Comment)node);
/*     */ 
/*     */       
/*     */       case 10:
/*     */         return;
/*     */     } 
/*     */     
/* 107 */     throw new XMLStreamException("Unexpected DOM Node Type " + node
/* 108 */         .getNodeType());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void visitChildren(Node node) throws XMLStreamException {
/* 115 */     NodeList nodeList = node.getChildNodes();
/* 116 */     if (nodeList != null) {
/* 117 */       for (int i = 0; i < nodeList.getLength(); i++) {
/* 118 */         print(nodeList.item(i));
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected void visitDocument(Document document) throws XMLStreamException {
/* 125 */     this.out.writeStartDocument();
/* 126 */     print(document.getDocumentElement());
/* 127 */     this.out.writeEndDocument();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void visitDocumentFragment(DocumentFragment documentFragment) throws XMLStreamException {
/* 132 */     visitChildren(documentFragment);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void visitElement(Element node) throws XMLStreamException {
/* 137 */     this.out.writeStartElement(node.getPrefix(), node
/* 138 */         .getLocalName(), node
/* 139 */         .getNamespaceURI());
/*     */     
/* 141 */     NamedNodeMap attrs = node.getAttributes();
/* 142 */     for (int i = 0; i < attrs.getLength(); i++) {
/* 143 */       visitAttr((Attr)attrs.item(i));
/*     */     }
/* 145 */     visitChildren(node);
/* 146 */     this.out.writeEndElement();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void visitAttr(Attr node) throws XMLStreamException {
/* 151 */     String name = node.getLocalName();
/* 152 */     if (name.equals("xmlns")) {
/* 153 */       this.out.writeDefaultNamespace(node.getNamespaceURI());
/*     */     } else {
/* 155 */       String prefix = node.getPrefix();
/* 156 */       if (prefix != null && prefix.equals("xmlns")) {
/* 157 */         this.out.writeNamespace(prefix, node.getNamespaceURI());
/* 158 */       } else if (prefix != null) {
/* 159 */         this.out.writeAttribute(prefix, node
/* 160 */             .getNamespaceURI(), name, node
/*     */             
/* 162 */             .getNodeValue());
/*     */       } else {
/*     */         
/* 165 */         this.out.writeAttribute(node.getNamespaceURI(), name, node
/*     */             
/* 167 */             .getNodeValue());
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void visitComment(Comment comment) throws XMLStreamException {
/* 173 */     this.out.writeComment(comment.getData());
/*     */   }
/*     */   
/*     */   protected void visitText(Text node) throws XMLStreamException {
/* 177 */     this.out.writeCharacters(node.getNodeValue());
/*     */   }
/*     */   
/*     */   protected void visitCDATASection(CDATASection cdata) throws XMLStreamException {
/* 181 */     this.out.writeCData(cdata.getNodeValue());
/*     */   }
/*     */ 
/*     */   
/*     */   protected void visitProcessingInstruction(ProcessingInstruction processingInstruction) throws XMLStreamException {
/* 186 */     this.out.writeProcessingInstruction(processingInstruction.getNodeName(), processingInstruction
/* 187 */         .getData());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void visitReference(EntityReference entityReference) throws XMLStreamException {
/* 193 */     visitChildren(entityReference);
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\xml\internal\rngom\digested\DOMPrinter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */