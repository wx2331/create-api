/*     */ package com.sun.tools.internal.jxc.ap;
/*     */ 
/*     */ import com.sun.tools.internal.jxc.api.JXC;
/*     */ import com.sun.tools.internal.xjc.api.ErrorListener;
/*     */ import com.sun.tools.internal.xjc.api.J2SJAXBModel;
/*     */ import com.sun.tools.internal.xjc.api.Reference;
/*     */ import java.io.File;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import javax.annotation.processing.AbstractProcessor;
/*     */ import javax.annotation.processing.ProcessingEnvironment;
/*     */ import javax.annotation.processing.RoundEnvironment;
/*     */ import javax.annotation.processing.SupportedAnnotationTypes;
/*     */ import javax.lang.model.SourceVersion;
/*     */ import javax.lang.model.element.Element;
/*     */ import javax.lang.model.element.ElementKind;
/*     */ import javax.lang.model.element.TypeElement;
/*     */ import javax.lang.model.util.ElementFilter;
/*     */ import javax.tools.Diagnostic;
/*     */ import javax.tools.StandardLocation;
/*     */ import javax.xml.bind.SchemaOutputResolver;
/*     */ import javax.xml.transform.Result;
/*     */ import javax.xml.transform.stream.StreamResult;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @SupportedAnnotationTypes({"*"})
/*     */ public class SchemaGenerator
/*     */   extends AbstractProcessor
/*     */ {
/*  73 */   private final Map<String, File> schemaLocations = new HashMap<>();
/*     */ 
/*     */   
/*     */   private File episodeFile;
/*     */ 
/*     */ 
/*     */   
/*     */   public SchemaGenerator(Map<String, File> m) {
/*  81 */     this.schemaLocations.putAll(m);
/*     */   }
/*     */   
/*     */   public void setEpisodeFile(File episodeFile) {
/*  85 */     this.episodeFile = episodeFile;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
/*  90 */     ErrorReceiverImpl errorListener = new ErrorReceiverImpl(this.processingEnv);
/*     */     
/*  92 */     List<Reference> classes = new ArrayList<>();
/*     */ 
/*     */     
/*  95 */     filterClass(classes, roundEnv.getRootElements());
/*     */     
/*  97 */     J2SJAXBModel model = JXC.createJavaCompiler().bind(classes, Collections.emptyMap(), null, this.processingEnv);
/*  98 */     if (model == null) {
/*  99 */       return false;
/*     */     }
/*     */     try {
/* 102 */       model.generateSchema(new SchemaOutputResolver()
/*     */           {
/*     */             public Result createOutput(String namespaceUri, String suggestedFileName) throws IOException {
/*     */               File file;
/*     */               OutputStream out;
/* 107 */               if (SchemaGenerator.this.schemaLocations.containsKey(namespaceUri)) {
/* 108 */                 file = (File)SchemaGenerator.this.schemaLocations.get(namespaceUri);
/* 109 */                 if (file == null) return null; 
/* 110 */                 out = new FileOutputStream(file);
/*     */               } else {
/*     */                 
/* 113 */                 file = new File(suggestedFileName);
/*     */                 
/* 115 */                 out = SchemaGenerator.this.processingEnv.getFiler().createResource(StandardLocation.CLASS_OUTPUT, "", suggestedFileName, new Element[0]).openOutputStream();
/* 116 */                 file = file.getAbsoluteFile();
/*     */               } 
/*     */               
/* 119 */               StreamResult ss = new StreamResult(out);
/* 120 */               SchemaGenerator.this.processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, "Writing " + file);
/* 121 */               ss.setSystemId(file.toURL().toExternalForm());
/* 122 */               return ss;
/*     */             }
/*     */           }(ErrorListener)errorListener);
/*     */       
/* 126 */       if (this.episodeFile != null) {
/* 127 */         this.processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, "Writing " + this.episodeFile);
/* 128 */         model.generateEpisodeFile(new StreamResult(this.episodeFile));
/*     */       } 
/* 130 */     } catch (IOException e) {
/* 131 */       errorListener.error(e.getMessage(), e);
/*     */     } 
/* 133 */     return false;
/*     */   }
/*     */   
/*     */   private void filterClass(List<Reference> classes, Collection<? extends Element> elements) {
/* 137 */     for (Element element : elements) {
/* 138 */       if (element.getKind().equals(ElementKind.CLASS) || element.getKind().equals(ElementKind.ENUM)) {
/* 139 */         classes.add(new Reference((TypeElement)element, this.processingEnv));
/* 140 */         filterClass(classes, (Collection)ElementFilter.typesIn(element.getEnclosedElements()));
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public SourceVersion getSupportedSourceVersion() {
/* 147 */     if (SourceVersion.latest().compareTo(SourceVersion.RELEASE_6) > 0) {
/* 148 */       return SourceVersion.valueOf("RELEASE_7");
/*     */     }
/* 150 */     return SourceVersion.RELEASE_6;
/*     */   }
/*     */   
/*     */   public SchemaGenerator() {}
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\jxc\ap\SchemaGenerator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */