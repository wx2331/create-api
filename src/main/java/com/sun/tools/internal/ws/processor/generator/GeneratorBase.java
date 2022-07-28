/*     */ package com.sun.tools.internal.ws.processor.generator;
/*     */ 
/*     */ import com.sun.codemodel.internal.ClassType;
/*     */ import com.sun.codemodel.internal.JAnnotationUse;
/*     */ import com.sun.codemodel.internal.JClassAlreadyExistsException;
/*     */ import com.sun.codemodel.internal.JCodeModel;
/*     */ import com.sun.codemodel.internal.JDefinedClass;
/*     */ import com.sun.tools.internal.ws.ToolVersion;
/*     */ import com.sun.tools.internal.ws.processor.model.Block;
/*     */ import com.sun.tools.internal.ws.processor.model.Fault;
/*     */ import com.sun.tools.internal.ws.processor.model.Model;
/*     */ import com.sun.tools.internal.ws.processor.model.ModelVisitor;
/*     */ import com.sun.tools.internal.ws.processor.model.Operation;
/*     */ import com.sun.tools.internal.ws.processor.model.Parameter;
/*     */ import com.sun.tools.internal.ws.processor.model.Port;
/*     */ import com.sun.tools.internal.ws.processor.model.Request;
/*     */ import com.sun.tools.internal.ws.processor.model.Response;
/*     */ import com.sun.tools.internal.ws.processor.model.Service;
/*     */ import com.sun.tools.internal.ws.processor.util.DirectoryUtil;
/*     */ import com.sun.tools.internal.ws.processor.util.IndentingWriter;
/*     */ import com.sun.tools.internal.ws.wscompile.ErrorReceiver;
/*     */ import com.sun.tools.internal.ws.wscompile.WsimportOptions;
/*     */ import com.sun.xml.internal.ws.util.xml.XmlUtil;
/*     */ import java.io.File;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.OutputStreamWriter;
/*     */ import java.io.Writer;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import javax.annotation.processing.Filer;
/*     */ import javax.jws.HandlerChain;
/*     */ import javax.tools.FileObject;
/*     */ import javax.tools.StandardLocation;
/*     */ import javax.xml.transform.Transformer;
/*     */ import javax.xml.transform.dom.DOMSource;
/*     */ import javax.xml.transform.stream.StreamResult;
/*     */ import org.w3c.dom.Element;
/*     */ import org.w3c.dom.NodeList;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class GeneratorBase
/*     */   implements ModelVisitor
/*     */ {
/*     */   private File destDir;
/*     */   private String targetVersion;
/*     */   protected boolean donotOverride;
/*     */   protected JCodeModel cm;
/*     */   protected Model model;
/*     */   protected String wsdlLocation;
/*     */   protected ErrorReceiver receiver;
/*     */   protected WsimportOptions options;
/*     */   
/*     */   public void init(Model model, WsimportOptions options, ErrorReceiver receiver) {
/*  82 */     this.model = model;
/*  83 */     this.options = options;
/*  84 */     this.destDir = options.destDir;
/*  85 */     this.receiver = receiver;
/*  86 */     this.wsdlLocation = options.wsdlLocation;
/*  87 */     this.targetVersion = options.target.getVersion();
/*  88 */     this.cm = options.getCodeModel();
/*     */   }
/*     */   
/*     */   public void doGeneration() {
/*     */     try {
/*  93 */       this.model.accept(this);
/*  94 */     } catch (Exception e) {
/*  95 */       this.receiver.error(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void visit(Model model) throws Exception {
/* 101 */     for (Service service : model.getServices()) {
/* 102 */       service.accept(this);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void visit(Service service) throws Exception {
/* 108 */     for (Port port : service.getPorts()) {
/* 109 */       port.accept(this);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void visit(Port port) throws Exception {
/* 115 */     for (Operation operation : port.getOperations()) {
/* 116 */       operation.accept(this);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void visit(Operation operation) throws Exception {
/* 122 */     operation.getRequest().accept(this);
/* 123 */     if (operation.getResponse() != null) {
/* 124 */       operation.getResponse().accept(this);
/*     */     }
/* 126 */     Iterator<Fault> faults = operation.getFaultsSet().iterator();
/* 127 */     if (faults != null)
/*     */     {
/* 129 */       while (faults.hasNext()) {
/* 130 */         Fault fault = faults.next();
/* 131 */         fault.accept(this);
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void visit(Parameter param) throws Exception {}
/*     */ 
/*     */   
/*     */   public void visit(Block block) throws Exception {}
/*     */ 
/*     */   
/*     */   public void visit(Response response) throws Exception {}
/*     */ 
/*     */   
/*     */   public void visit(Request request) throws Exception {}
/*     */ 
/*     */   
/*     */   public void visit(Fault fault) throws Exception {}
/*     */   
/*     */   public List<String> getJAXWSClassComment() {
/* 152 */     return getJAXWSClassComment(this.targetVersion);
/*     */   }
/*     */   
/*     */   public static List<String> getJAXWSClassComment(String targetVersion) {
/* 156 */     List<String> comments = new ArrayList<>();
/* 157 */     comments.add("This class was generated by the JAX-WS RI.\n");
/* 158 */     comments.add(ToolVersion.VERSION.BUILD_VERSION + "\n");
/* 159 */     comments.add("Generated source version: " + targetVersion);
/* 160 */     return comments;
/*     */   }
/*     */   
/*     */   protected JDefinedClass getClass(String className, ClassType type) throws JClassAlreadyExistsException {
/*     */     JDefinedClass cls;
/*     */     try {
/* 166 */       cls = this.cm._class(className, type);
/* 167 */     } catch (JClassAlreadyExistsException e) {
/* 168 */       cls = this.cm._getClass(className);
/* 169 */       if (cls == null) {
/* 170 */         throw e;
/*     */       }
/*     */     } 
/* 173 */     return cls;
/*     */   }
/*     */   
/*     */   protected void log(String msg) {
/* 177 */     if (this.options.verbose) {
/* 178 */       System.out.println("[" + 
/*     */           
/* 180 */           Names.stripQualifier(getClass().getName()) + ": " + msg + "]");
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void writeHandlerConfig(String className, JDefinedClass cls, WsimportOptions options) {
/* 188 */     Element e = options.getHandlerChainConfiguration();
/* 189 */     if (e == null) {
/*     */       return;
/*     */     }
/* 192 */     JAnnotationUse handlerChainAnn = cls.annotate(this.cm.ref(HandlerChain.class));
/* 193 */     NodeList nl = e.getElementsByTagNameNS("http://java.sun.com/xml/ns/javaee", "handler-chain");
/*     */     
/* 195 */     if (nl.getLength() > 0) {
/* 196 */       String fName = getHandlerConfigFileName(className);
/* 197 */       handlerChainAnn.param("file", fName);
/* 198 */       generateHandlerChainFile(e, className);
/*     */     } 
/*     */   }
/*     */   
/*     */   private String getHandlerConfigFileName(String fullName) {
/* 203 */     String name = Names.stripQualifier(fullName);
/* 204 */     return name + "_handler.xml";
/*     */   }
/*     */ 
/*     */   
/*     */   private void generateHandlerChainFile(Element hChains, String name) {
/* 209 */     Filer filer = this.options.filer;
/*     */     
/*     */     try {
/*     */       IndentingWriter p;
/*     */       
/* 214 */       if (filer != null) {
/* 215 */         FileObject jfo = filer.createResource(StandardLocation.SOURCE_OUTPUT, 
/* 216 */             Names.getPackageName(name), getHandlerConfigFileName(name), new javax.lang.model.element.Element[0]);
/* 217 */         this.options.addGeneratedFile(new File(jfo.toUri()));
/* 218 */         p = new IndentingWriter(new OutputStreamWriter(jfo.openOutputStream()));
/*     */       } else {
/* 220 */         String hcName = getHandlerConfigFileName(name);
/* 221 */         File packageDir = DirectoryUtil.getOutputDirectoryFor(name, this.destDir);
/* 222 */         File hcFile = new File(packageDir, hcName);
/* 223 */         this.options.addGeneratedFile(hcFile);
/* 224 */         p = new IndentingWriter(new OutputStreamWriter(new FileOutputStream(hcFile)));
/*     */       } 
/*     */       
/* 227 */       Transformer it = XmlUtil.newTransformer();
/*     */       
/* 229 */       it.setOutputProperty("method", "xml");
/* 230 */       it.setOutputProperty("indent", "yes");
/* 231 */       it.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
/*     */ 
/*     */       
/* 234 */       it.setOutputProperty("encoding", "UTF-8");
/* 235 */       it.transform(new DOMSource(hChains), new StreamResult((Writer)p));
/* 236 */       p.close();
/* 237 */     } catch (Exception e) {
/* 238 */       throw new GeneratorException("generator.nestedGeneratorError", new Object[] { e });
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\ws\processor\generator\GeneratorBase.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */