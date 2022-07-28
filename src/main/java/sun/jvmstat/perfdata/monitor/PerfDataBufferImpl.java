/*     */ package sun.jvmstat.perfdata.monitor;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.net.MalformedURLException;
/*     */ import java.net.URL;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.TreeMap;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ import java.util.regex.PatternSyntaxException;
/*     */ import sun.jvmstat.monitor.Monitor;
/*     */ import sun.jvmstat.monitor.MonitorException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class PerfDataBufferImpl
/*     */ {
/*     */   protected ByteBuffer buffer;
/*     */   protected Map<String, Monitor> monitors;
/*     */   protected int lvmid;
/*     */   protected Map<String, ArrayList<String>> aliasMap;
/*     */   protected Map aliasCache;
/*     */   
/*     */   protected PerfDataBufferImpl(ByteBuffer paramByteBuffer, int paramInt) {
/*  80 */     this.buffer = paramByteBuffer;
/*  81 */     this.lvmid = paramInt;
/*  82 */     this.monitors = new TreeMap<>();
/*  83 */     this.aliasMap = new HashMap<>();
/*  84 */     this.aliasCache = new HashMap<>();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getLocalVmId() {
/*  94 */     return this.lvmid;
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
/*     */   public byte[] getBytes() {
/* 106 */     ByteBuffer byteBuffer = null;
/* 107 */     synchronized (this) {
/*     */ 
/*     */       
/*     */       try {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 118 */         if (this.monitors.isEmpty()) {
/* 119 */           buildMonitorMap(this.monitors);
/*     */         }
/* 121 */       } catch (MonitorException monitorException) {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 127 */       byteBuffer = this.buffer.duplicate();
/*     */     } 
/* 129 */     byteBuffer.rewind();
/* 130 */     byte[] arrayOfByte = new byte[byteBuffer.limit()];
/* 131 */     byteBuffer.get(arrayOfByte);
/* 132 */     return arrayOfByte;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getCapacity() {
/* 141 */     return this.buffer.capacity();
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
/*     */   ByteBuffer getByteBuffer() {
/* 153 */     return this.buffer;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void buildAliasMap() {
/* 162 */     assert Thread.holdsLock(this);
/*     */     
/* 164 */     URL uRL = null;
/* 165 */     String str = System.getProperty("sun.jvmstat.perfdata.aliasmap");
/*     */     
/* 167 */     if (str != null) {
/* 168 */       File file = new File(str);
/*     */       try {
/* 170 */         uRL = file.toURL();
/*     */       }
/* 172 */       catch (MalformedURLException malformedURLException) {
/* 173 */         throw new IllegalArgumentException(malformedURLException);
/*     */       } 
/*     */     } else {
/* 176 */       uRL = getClass().getResource("/sun/jvmstat/perfdata/resources/aliasmap");
/*     */     } 
/*     */ 
/*     */     
/* 180 */     assert uRL != null;
/*     */     
/* 182 */     AliasFileParser aliasFileParser = new AliasFileParser(uRL);
/*     */     
/*     */     try {
/* 185 */       aliasFileParser.parse(this.aliasMap);
/*     */     }
/* 187 */     catch (IOException iOException) {
/* 188 */       System.err.println("Error processing " + str + ": " + iOException
/* 189 */           .getMessage());
/* 190 */     } catch (SyntaxException syntaxException) {
/* 191 */       System.err.println("Syntax error parsing " + str + ": " + syntaxException
/* 192 */           .getMessage());
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Monitor findByAlias(String paramString) {
/* 201 */     assert Thread.holdsLock(this);
/*     */     
/* 203 */     Monitor monitor = (Monitor)this.aliasCache.get(paramString);
/* 204 */     if (monitor == null) {
/* 205 */       ArrayList arrayList = this.aliasMap.get(paramString);
/* 206 */       if (arrayList != null) {
/* 207 */         for (Iterator<String> iterator = arrayList.iterator(); iterator.hasNext() && monitor == null; ) {
/* 208 */           String str = iterator.next();
/* 209 */           monitor = this.monitors.get(str);
/*     */         } 
/*     */       }
/*     */     } 
/* 213 */     return monitor;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Monitor findByName(String paramString) throws MonitorException {
/* 237 */     Monitor monitor = null;
/*     */     
/* 239 */     synchronized (this) {
/* 240 */       if (this.monitors.isEmpty()) {
/* 241 */         buildMonitorMap(this.monitors);
/* 242 */         buildAliasMap();
/*     */       } 
/*     */ 
/*     */       
/* 246 */       monitor = this.monitors.get(paramString);
/* 247 */       if (monitor == null) {
/*     */         
/* 249 */         getNewMonitors(this.monitors);
/* 250 */         monitor = this.monitors.get(paramString);
/*     */       } 
/* 252 */       if (monitor == null)
/*     */       {
/* 254 */         monitor = findByAlias(paramString);
/*     */       }
/*     */     } 
/* 257 */     return monitor;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<Monitor> findByPattern(String paramString) throws MonitorException, PatternSyntaxException {
/* 280 */     synchronized (this) {
/* 281 */       if (this.monitors.isEmpty()) {
/* 282 */         buildMonitorMap(this.monitors);
/*     */       } else {
/* 284 */         getNewMonitors(this.monitors);
/*     */       } 
/*     */     } 
/*     */     
/* 288 */     Pattern pattern = Pattern.compile(paramString);
/* 289 */     Matcher matcher = pattern.matcher("");
/* 290 */     ArrayList<Monitor> arrayList = new ArrayList();
/*     */     
/* 292 */     Set<Map.Entry<String, Monitor>> set = this.monitors.entrySet();
/*     */     
/* 294 */     for (Map.Entry<String, Monitor> entry : set) {
/*     */       
/* 296 */       String str = (String)entry.getKey();
/* 297 */       Monitor monitor = (Monitor)entry.getValue();
/*     */ 
/*     */       
/* 300 */       matcher.reset(str);
/*     */ 
/*     */       
/* 303 */       if (matcher.lookingAt()) {
/* 304 */         arrayList.add((Monitor)entry.getValue());
/*     */       }
/*     */     } 
/* 307 */     return arrayList;
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
/*     */   public MonitorStatus getMonitorStatus() throws MonitorException {
/* 319 */     synchronized (this) {
/* 320 */       if (this.monitors.isEmpty()) {
/* 321 */         buildMonitorMap(this.monitors);
/*     */       }
/* 323 */       return getMonitorStatus(this.monitors);
/*     */     } 
/*     */   }
/*     */   
/*     */   protected abstract MonitorStatus getMonitorStatus(Map<String, Monitor> paramMap) throws MonitorException;
/*     */   
/*     */   protected abstract void buildMonitorMap(Map<String, Monitor> paramMap) throws MonitorException;
/*     */   
/*     */   protected abstract void getNewMonitors(Map<String, Monitor> paramMap) throws MonitorException;
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\sun\jvmstat\perfdata\monitor\PerfDataBufferImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */