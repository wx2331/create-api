/*     */ package com.sun.tools.hat.internal.server;
/*     */ 
/*     */ import com.sun.tools.hat.internal.model.Snapshot;
/*     */ import com.sun.tools.hat.internal.oql.OQLEngine;
/*     */ import com.sun.tools.hat.internal.util.Misc;
/*     */ import java.io.BufferedInputStream;
/*     */ import java.io.BufferedWriter;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStreamWriter;
/*     */ import java.io.PrintWriter;
/*     */ import java.net.Socket;
/*     */ import java.net.URLDecoder;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class HttpReader
/*     */   implements Runnable
/*     */ {
/*     */   private Socket socket;
/*     */   private PrintWriter out;
/*     */   private Snapshot snapshot;
/*     */   private OQLEngine engine;
/*     */   
/*     */   public HttpReader(Socket paramSocket, Snapshot paramSnapshot, OQLEngine paramOQLEngine) {
/*  65 */     this.socket = paramSocket;
/*  66 */     this.snapshot = paramSnapshot;
/*  67 */     this.engine = paramOQLEngine;
/*     */   }
/*     */   
/*     */   public void run() {
/*  71 */     BufferedInputStream bufferedInputStream = null; try {
/*     */       FinalizerObjectsQuery finalizerObjectsQuery;
/*  73 */       bufferedInputStream = new BufferedInputStream(this.socket.getInputStream());
/*  74 */       this
/*     */         
/*  76 */         .out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(this.socket.getOutputStream())));
/*  77 */       this.out.println("HTTP/1.0 200 OK");
/*  78 */       this.out.println("Cache-Control: no-cache");
/*  79 */       this.out.println("Pragma: no-cache");
/*  80 */       this.out.println();
/*  81 */       if (bufferedInputStream.read() != 71 || bufferedInputStream.read() != 69 || bufferedInputStream
/*  82 */         .read() != 84 || bufferedInputStream.read() != 32) {
/*  83 */         outputError("Protocol error");
/*     */       }
/*     */       
/*  86 */       StringBuilder stringBuilder = new StringBuilder(); int i;
/*  87 */       while ((i = bufferedInputStream.read()) != -1 && i != 32) {
/*  88 */         char c = (char)i;
/*  89 */         stringBuilder.append(c);
/*     */       } 
/*  91 */       String str = stringBuilder.toString();
/*  92 */       str = URLDecoder.decode(str, "UTF-8");
/*  93 */       AllClassesQuery allClassesQuery = null;
/*  94 */       if (this.snapshot == null) {
/*  95 */         outputError("The heap snapshot is still being read."); return;
/*     */       } 
/*  97 */       if (str.equals("/")) {
/*  98 */         allClassesQuery = new AllClassesQuery(true, (this.engine != null));
/*  99 */         allClassesQuery.setUrlStart("");
/* 100 */         allClassesQuery.setQuery("");
/* 101 */       } else if (str.startsWith("/oql/")) {
/* 102 */         if (this.engine != null) {
/* 103 */           OQLQuery oQLQuery = new OQLQuery(this.engine);
/* 104 */           oQLQuery.setUrlStart("");
/* 105 */           oQLQuery.setQuery(str.substring(5));
/*     */         } 
/* 107 */       } else if (str.startsWith("/oqlhelp/")) {
/* 108 */         if (this.engine != null) {
/* 109 */           OQLHelp oQLHelp = new OQLHelp();
/* 110 */           oQLHelp.setUrlStart("");
/* 111 */           oQLHelp.setQuery("");
/*     */         } 
/* 113 */       } else if (str.equals("/allClassesWithPlatform/")) {
/* 114 */         allClassesQuery = new AllClassesQuery(false, (this.engine != null));
/* 115 */         allClassesQuery.setUrlStart("../");
/* 116 */         allClassesQuery.setQuery("");
/* 117 */       } else if (str.equals("/showRoots/")) {
/* 118 */         AllRootsQuery allRootsQuery = new AllRootsQuery();
/* 119 */         allRootsQuery.setUrlStart("../");
/* 120 */         allRootsQuery.setQuery("");
/* 121 */       } else if (str.equals("/showInstanceCounts/includePlatform/")) {
/* 122 */         InstancesCountQuery instancesCountQuery = new InstancesCountQuery(false);
/* 123 */         instancesCountQuery.setUrlStart("../../");
/* 124 */         instancesCountQuery.setQuery("");
/* 125 */       } else if (str.equals("/showInstanceCounts/")) {
/* 126 */         InstancesCountQuery instancesCountQuery = new InstancesCountQuery(true);
/* 127 */         instancesCountQuery.setUrlStart("../");
/* 128 */         instancesCountQuery.setQuery("");
/* 129 */       } else if (str.startsWith("/instances/")) {
/* 130 */         InstancesQuery instancesQuery = new InstancesQuery(false);
/* 131 */         instancesQuery.setUrlStart("../");
/* 132 */         instancesQuery.setQuery(str.substring(11));
/* 133 */       } else if (str.startsWith("/newInstances/")) {
/* 134 */         InstancesQuery instancesQuery = new InstancesQuery(false, true);
/* 135 */         instancesQuery.setUrlStart("../");
/* 136 */         instancesQuery.setQuery(str.substring(14));
/* 137 */       } else if (str.startsWith("/allInstances/")) {
/* 138 */         InstancesQuery instancesQuery = new InstancesQuery(true);
/* 139 */         instancesQuery.setUrlStart("../");
/* 140 */         instancesQuery.setQuery(str.substring(14));
/* 141 */       } else if (str.startsWith("/allNewInstances/")) {
/* 142 */         InstancesQuery instancesQuery = new InstancesQuery(true, true);
/* 143 */         instancesQuery.setUrlStart("../");
/* 144 */         instancesQuery.setQuery(str.substring(17));
/* 145 */       } else if (str.startsWith("/object/")) {
/* 146 */         ObjectQuery objectQuery = new ObjectQuery();
/* 147 */         objectQuery.setUrlStart("../");
/* 148 */         objectQuery.setQuery(str.substring(8));
/* 149 */       } else if (str.startsWith("/class/")) {
/* 150 */         ClassQuery classQuery = new ClassQuery();
/* 151 */         classQuery.setUrlStart("../");
/* 152 */         classQuery.setQuery(str.substring(7));
/* 153 */       } else if (str.startsWith("/roots/")) {
/* 154 */         RootsQuery rootsQuery = new RootsQuery(false);
/* 155 */         rootsQuery.setUrlStart("../");
/* 156 */         rootsQuery.setQuery(str.substring(7));
/* 157 */       } else if (str.startsWith("/allRoots/")) {
/* 158 */         RootsQuery rootsQuery = new RootsQuery(true);
/* 159 */         rootsQuery.setUrlStart("../");
/* 160 */         rootsQuery.setQuery(str.substring(10));
/* 161 */       } else if (str.startsWith("/reachableFrom/")) {
/* 162 */         ReachableQuery reachableQuery = new ReachableQuery();
/* 163 */         reachableQuery.setUrlStart("../");
/* 164 */         reachableQuery.setQuery(str.substring(15));
/* 165 */       } else if (str.startsWith("/rootStack/")) {
/* 166 */         RootStackQuery rootStackQuery = new RootStackQuery();
/* 167 */         rootStackQuery.setUrlStart("../");
/* 168 */         rootStackQuery.setQuery(str.substring(11));
/* 169 */       } else if (str.startsWith("/histo/")) {
/* 170 */         HistogramQuery histogramQuery = new HistogramQuery();
/* 171 */         histogramQuery.setUrlStart("../");
/* 172 */         histogramQuery.setQuery(str.substring(7));
/* 173 */       } else if (str.startsWith("/refsByType/")) {
/* 174 */         RefsByTypeQuery refsByTypeQuery = new RefsByTypeQuery();
/* 175 */         refsByTypeQuery.setUrlStart("../");
/* 176 */         refsByTypeQuery.setQuery(str.substring(12));
/* 177 */       } else if (str.startsWith("/finalizerSummary/")) {
/* 178 */         FinalizerSummaryQuery finalizerSummaryQuery = new FinalizerSummaryQuery();
/* 179 */         finalizerSummaryQuery.setUrlStart("../");
/* 180 */         finalizerSummaryQuery.setQuery("");
/* 181 */       } else if (str.startsWith("/finalizerObjects/")) {
/* 182 */         finalizerObjectsQuery = new FinalizerObjectsQuery();
/* 183 */         finalizerObjectsQuery.setUrlStart("../");
/* 184 */         finalizerObjectsQuery.setQuery("");
/*     */       } 
/*     */       
/* 187 */       if (finalizerObjectsQuery != null) {
/* 188 */         finalizerObjectsQuery.setOutput(this.out);
/* 189 */         finalizerObjectsQuery.setSnapshot(this.snapshot);
/* 190 */         finalizerObjectsQuery.run();
/*     */       } else {
/* 192 */         outputError("Query '" + str + "' not implemented");
/*     */       } 
/* 194 */     } catch (IOException iOException) {
/* 195 */       iOException.printStackTrace();
/*     */     } finally {
/* 197 */       if (this.out != null) {
/* 198 */         this.out.close();
/*     */       }
/*     */       try {
/* 201 */         if (bufferedInputStream != null) {
/* 202 */           bufferedInputStream.close();
/*     */         }
/* 204 */       } catch (IOException iOException) {}
/*     */       
/*     */       try {
/* 207 */         this.socket.close();
/* 208 */       } catch (IOException iOException) {}
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void outputError(String paramString) {
/* 214 */     this.out.println();
/* 215 */     this.out.println("<html><body bgcolor=\"#ffffff\">");
/* 216 */     this.out.println(Misc.encodeHtml(paramString));
/* 217 */     this.out.println("</body></html>");
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\hat\internal\server\HttpReader.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */