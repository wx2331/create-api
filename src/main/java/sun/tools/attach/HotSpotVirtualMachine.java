/*     */ package sun.tools.attach;
/*     */ 
/*     */ import com.sun.tools.attach.AgentInitializationException;
/*     */ import com.sun.tools.attach.AgentLoadException;
/*     */ import com.sun.tools.attach.VirtualMachine;
/*     */ import com.sun.tools.attach.spi.AttachProvider;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.util.Map;
/*     */ import java.util.Properties;
/*     */ import java.util.stream.Collectors;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class HotSpotVirtualMachine
/*     */   extends VirtualMachine
/*     */ {
/*     */   private static final int JNI_ENOMEM = -4;
/*     */   private static final int ATTACH_ERROR_BADJAR = 100;
/*     */   private static final int ATTACH_ERROR_NOTONCP = 101;
/*     */   private static final int ATTACH_ERROR_STARTFAIL = 102;
/*     */   private static final String MANAGMENT_PREFIX = "com.sun.management.";
/*     */   
/*     */   HotSpotVirtualMachine(AttachProvider paramAttachProvider, String paramString) {
/*  45 */     super(paramAttachProvider, paramString);
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
/*     */   private void loadAgentLibrary(String paramString1, boolean paramBoolean, String paramString2) throws AgentLoadException, AgentInitializationException, IOException {
/*  58 */     InputStream inputStream = execute("load", new Object[] { paramString1, paramBoolean ? "true" : "false", paramString2 });
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/*  63 */       int i = readInt(inputStream);
/*  64 */       if (i != 0) {
/*  65 */         throw new AgentInitializationException("Agent_OnAttach failed", i);
/*     */       }
/*     */     } finally {
/*  68 */       inputStream.close();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void loadAgentLibrary(String paramString1, String paramString2) throws AgentLoadException, AgentInitializationException, IOException {
/*  79 */     loadAgentLibrary(paramString1, false, paramString2);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void loadAgentPath(String paramString1, String paramString2) throws AgentLoadException, AgentInitializationException, IOException {
/*  88 */     loadAgentLibrary(paramString1, true, paramString2);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void loadAgent(String paramString1, String paramString2) throws AgentLoadException, AgentInitializationException, IOException {
/*  98 */     String str = paramString1;
/*  99 */     if (paramString2 != null) {
/* 100 */       str = str + "=" + paramString2;
/*     */     }
/*     */     try {
/* 103 */       loadAgentLibrary("instrument", str);
/* 104 */     } catch (AgentLoadException agentLoadException) {
/* 105 */       throw new InternalError("instrument library is missing in target VM", agentLoadException);
/* 106 */     } catch (AgentInitializationException agentInitializationException) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 112 */       int i = agentInitializationException.returnValue();
/* 113 */       switch (i) {
/*     */         case -4:
/* 115 */           throw new AgentLoadException("Insuffient memory");
/*     */         case 100:
/* 117 */           throw new AgentLoadException("Agent JAR not found or no Agent-Class attribute");
/*     */         case 101:
/* 119 */           throw new AgentLoadException("Unable to add JAR file to system class path");
/*     */         case 102:
/* 121 */           throw new AgentInitializationException("Agent JAR loaded but agent failed to initialize");
/*     */       } 
/* 123 */       throw new AgentLoadException("Failed to load agent - unknown reason: " + i);
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Properties getSystemProperties() throws IOException {
/* 141 */     InputStream inputStream = null;
/* 142 */     Properties properties = new Properties();
/*     */     try {
/* 144 */       inputStream = executeCommand("properties", new Object[0]);
/* 145 */       properties.load(inputStream);
/*     */     } finally {
/* 147 */       if (inputStream != null) inputStream.close(); 
/*     */     } 
/* 149 */     return properties;
/*     */   }
/*     */   
/*     */   public Properties getAgentProperties() throws IOException {
/* 153 */     InputStream inputStream = null;
/* 154 */     Properties properties = new Properties();
/*     */     try {
/* 156 */       inputStream = executeCommand("agentProperties", new Object[0]);
/* 157 */       properties.load(inputStream);
/*     */     } finally {
/* 159 */       if (inputStream != null) inputStream.close(); 
/*     */     } 
/* 161 */     return properties;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static boolean checkedKeyName(Object paramObject) {
/* 167 */     if (!(paramObject instanceof String)) {
/* 168 */       throw new IllegalArgumentException("Invalid option (not a String): " + paramObject);
/*     */     }
/* 170 */     if (!((String)paramObject).startsWith("com.sun.management.")) {
/* 171 */       throw new IllegalArgumentException("Invalid option: " + paramObject);
/*     */     }
/* 173 */     return true;
/*     */   }
/*     */   
/*     */   private static String stripKeyName(Object paramObject) {
/* 177 */     return ((String)paramObject).substring("com.sun.management.".length());
/*     */   }
/*     */ 
/*     */   
/*     */   public void startManagementAgent(Properties paramProperties) throws IOException {
/* 182 */     if (paramProperties == null) {
/* 183 */       throw new NullPointerException("agentProperties cannot be null");
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 190 */     String str = paramProperties.entrySet().stream().filter(paramEntry -> checkedKeyName(paramEntry.getKey())).map(paramEntry -> stripKeyName(paramEntry.getKey()) + "=" + escape(paramEntry.getValue())).collect(Collectors.joining(" "));
/* 191 */     executeJCmd("ManagementAgent.start " + str);
/*     */   }
/*     */   
/*     */   private String escape(Object paramObject) {
/* 195 */     String str = paramObject.toString();
/* 196 */     if (str.contains(" ")) {
/* 197 */       return "'" + str + "'";
/*     */     }
/* 199 */     return str;
/*     */   }
/*     */ 
/*     */   
/*     */   public String startLocalManagementAgent() throws IOException {
/* 204 */     executeJCmd("ManagementAgent.start_local");
/* 205 */     return getAgentProperties().getProperty("com.sun.management.jmxremote.localConnectorAddress");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void localDataDump() throws IOException {
/* 212 */     executeCommand("datadump", new Object[0]).close();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public InputStream remoteDataDump(Object... paramVarArgs) throws IOException {
/* 218 */     return executeCommand("threaddump", paramVarArgs);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public InputStream dumpHeap(Object... paramVarArgs) throws IOException {
/* 224 */     return executeCommand("dumpheap", paramVarArgs);
/*     */   }
/*     */ 
/*     */   
/*     */   public InputStream heapHisto(Object... paramVarArgs) throws IOException {
/* 229 */     return executeCommand("inspectheap", paramVarArgs);
/*     */   }
/*     */ 
/*     */   
/*     */   public InputStream setFlag(String paramString1, String paramString2) throws IOException {
/* 234 */     return executeCommand("setflag", new Object[] { paramString1, paramString2 });
/*     */   }
/*     */ 
/*     */   
/*     */   public InputStream printFlag(String paramString) throws IOException {
/* 239 */     return executeCommand("printflag", new Object[] { paramString });
/*     */   }
/*     */   
/*     */   public InputStream executeJCmd(String paramString) throws IOException {
/* 243 */     return executeCommand("jcmd", new Object[] { paramString });
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
/*     */   private InputStream executeCommand(String paramString, Object... paramVarArgs) throws IOException {
/*     */     try {
/* 261 */       return execute(paramString, paramVarArgs);
/* 262 */     } catch (AgentLoadException agentLoadException) {
/* 263 */       throw new InternalError("Should not get here", agentLoadException);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   int readInt(InputStream paramInputStream) throws IOException {
/*     */     int i, j;
/* 274 */     StringBuilder stringBuilder = new StringBuilder();
/*     */ 
/*     */ 
/*     */     
/* 278 */     byte[] arrayOfByte = new byte[1];
/*     */     do {
/* 280 */       i = paramInputStream.read(arrayOfByte, 0, 1);
/* 281 */       if (i <= 0)
/* 282 */         continue;  j = (char)arrayOfByte[0];
/* 283 */       if (j == 10) {
/*     */         break;
/*     */       }
/* 286 */       stringBuilder.append(j);
/*     */     
/*     */     }
/* 289 */     while (i > 0);
/*     */     
/* 291 */     if (stringBuilder.length() == 0) {
/* 292 */       throw new IOException("Premature EOF");
/*     */     }
/*     */ 
/*     */     
/*     */     try {
/* 297 */       j = Integer.parseInt(stringBuilder.toString());
/* 298 */     } catch (NumberFormatException numberFormatException) {
/* 299 */       throw new IOException("Non-numeric value found - int expected");
/*     */     } 
/* 301 */     return j;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   String readErrorMessage(InputStream paramInputStream) throws IOException {
/* 308 */     byte[] arrayOfByte = new byte[1024];
/*     */     
/* 310 */     StringBuffer stringBuffer = new StringBuffer(); int i;
/* 311 */     while ((i = paramInputStream.read(arrayOfByte)) != -1) {
/* 312 */       stringBuffer.append(new String(arrayOfByte, 0, i, "UTF-8"));
/*     */     }
/* 314 */     return stringBuffer.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 320 */   private static long defaultAttachTimeout = 5000L;
/*     */ 
/*     */ 
/*     */   
/*     */   private volatile long attachTimeout;
/*     */ 
/*     */ 
/*     */   
/*     */   long attachTimeout() {
/* 329 */     if (this.attachTimeout == 0L) {
/* 330 */       synchronized (this) {
/* 331 */         if (this.attachTimeout == 0L) {
/*     */ 
/*     */           
/* 334 */           try { String str = System.getProperty("sun.tools.attach.attachTimeout");
/* 335 */             this.attachTimeout = Long.parseLong(str); }
/* 336 */           catch (SecurityException securityException) {  }
/* 337 */           catch (NumberFormatException numberFormatException) {}
/*     */           
/* 339 */           if (this.attachTimeout <= 0L) {
/* 340 */             this.attachTimeout = defaultAttachTimeout;
/*     */           }
/*     */         } 
/*     */       } 
/*     */     }
/* 345 */     return this.attachTimeout;
/*     */   }
/*     */   
/*     */   abstract InputStream execute(String paramString, Object... paramVarArgs) throws AgentLoadException, IOException;
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\sun\tools\attach\HotSpotVirtualMachine.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */