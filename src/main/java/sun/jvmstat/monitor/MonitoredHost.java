/*     */ package sun.jvmstat.monitor;
/*     */ 
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.net.URISyntaxException;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import sun.jvmstat.monitor.event.HostListener;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class MonitoredHost
/*     */ {
/*  51 */   private static Map<HostIdentifier, MonitoredHost> monitoredHosts = new HashMap<>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final String IMPL_OVERRIDE_PROP_NAME = "sun.jvmstat.monitor.MonitoredHost";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final String IMPL_PKG_PROP_NAME = "sun.jvmstat.monitor.package";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  77 */   private static final String IMPL_PACKAGE = System.getProperty("sun.jvmstat.monitor.package", "sun.jvmstat.perfdata");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final String LOCAL_PROTOCOL_PROP_NAME = "sun.jvmstat.monitor.local";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  89 */   private static final String LOCAL_PROTOCOL = System.getProperty("sun.jvmstat.monitor.local", "local");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final String REMOTE_PROTOCOL_PROP_NAME = "sun.jvmstat.monitor.remote";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 101 */   private static final String REMOTE_PROTOCOL = System.getProperty("sun.jvmstat.monitor.remote", "rmi");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final String MONITORED_HOST_CLASS = "MonitoredHostProvider";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected HostIdentifier hostId;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected int interval;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Exception lastException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static MonitoredHost getMonitoredHost(String paramString) throws MonitorException, URISyntaxException {
/* 142 */     HostIdentifier hostIdentifier = new HostIdentifier(paramString);
/* 143 */     return getMonitoredHost(hostIdentifier);
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
/*     */   public static MonitoredHost getMonitoredHost(VmIdentifier paramVmIdentifier) throws MonitorException {
/* 164 */     HostIdentifier hostIdentifier = new HostIdentifier(paramVmIdentifier);
/* 165 */     return getMonitoredHost(hostIdentifier);
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
/*     */   public static MonitoredHost getMonitoredHost(HostIdentifier paramHostIdentifier) throws MonitorException {
/* 184 */     String str = System.getProperty("sun.jvmstat.monitor.MonitoredHost");
/* 185 */     MonitoredHost monitoredHost = null;
/*     */     
/* 187 */     synchronized (monitoredHosts) {
/* 188 */       monitoredHost = monitoredHosts.get(paramHostIdentifier);
/* 189 */       if (monitoredHost != null) {
/* 190 */         if (monitoredHost.isErrored()) {
/* 191 */           monitoredHosts.remove(paramHostIdentifier);
/*     */         } else {
/* 193 */           return monitoredHost;
/*     */         } 
/*     */       }
/*     */     } 
/*     */     
/* 198 */     paramHostIdentifier = resolveHostId(paramHostIdentifier);
/*     */     
/* 200 */     if (str == null)
/*     */     {
/*     */       
/* 203 */       str = IMPL_PACKAGE + ".monitor.protocol." + paramHostIdentifier.getScheme() + "." + "MonitoredHostProvider";
/*     */     }
/*     */ 
/*     */     
/*     */     try {
/* 208 */       Class<?> clazz = Class.forName(str);
/*     */       
/* 210 */       Constructor<?> constructor = clazz.getConstructor(new Class[] { paramHostIdentifier
/* 211 */             .getClass() });
/*     */ 
/*     */       
/* 214 */       monitoredHost = (MonitoredHost)constructor.newInstance(new Object[] { paramHostIdentifier });
/*     */       
/* 216 */       synchronized (monitoredHosts) {
/* 217 */         monitoredHosts.put(monitoredHost.hostId, monitoredHost);
/*     */       } 
/* 219 */       return monitoredHost;
/* 220 */     } catch (ClassNotFoundException classNotFoundException) {
/*     */       
/* 222 */       throw new IllegalArgumentException("Could not find " + str + ": " + classNotFoundException
/* 223 */           .getMessage(), classNotFoundException);
/* 224 */     } catch (NoSuchMethodException noSuchMethodException) {
/*     */       
/* 226 */       throw new IllegalArgumentException("Expected constructor missing in " + str + ": " + noSuchMethodException
/*     */           
/* 228 */           .getMessage(), noSuchMethodException);
/* 229 */     } catch (IllegalAccessException illegalAccessException) {
/*     */       
/* 231 */       throw new IllegalArgumentException("Unexpected constructor access in " + str + ": " + illegalAccessException
/*     */           
/* 233 */           .getMessage(), illegalAccessException);
/* 234 */     } catch (InstantiationException instantiationException) {
/* 235 */       throw new IllegalArgumentException(str + "is abstract: " + instantiationException
/* 236 */           .getMessage(), instantiationException);
/* 237 */     } catch (InvocationTargetException invocationTargetException) {
/* 238 */       Throwable throwable = invocationTargetException.getCause();
/* 239 */       if (throwable instanceof MonitorException) {
/* 240 */         throw (MonitorException)throwable;
/*     */       }
/* 242 */       throw new RuntimeException("Unexpected exception", invocationTargetException);
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
/*     */   protected static HostIdentifier resolveHostId(HostIdentifier paramHostIdentifier) throws MonitorException {
/* 258 */     String str1 = paramHostIdentifier.getHost();
/* 259 */     String str2 = paramHostIdentifier.getScheme();
/* 260 */     StringBuffer stringBuffer = new StringBuffer();
/*     */     
/* 262 */     assert str1 != null;
/*     */     
/* 264 */     if (str2 == null) {
/* 265 */       if (str1.compareTo("localhost") == 0) {
/* 266 */         str2 = LOCAL_PROTOCOL;
/*     */       } else {
/* 268 */         str2 = REMOTE_PROTOCOL;
/*     */       } 
/*     */     }
/*     */     
/* 272 */     stringBuffer.append(str2).append(":").append(paramHostIdentifier.getSchemeSpecificPart());
/*     */     
/* 274 */     String str3 = paramHostIdentifier.getFragment();
/* 275 */     if (str3 != null) {
/* 276 */       stringBuffer.append("#").append(str3);
/*     */     }
/*     */     
/*     */     try {
/* 280 */       return new HostIdentifier(stringBuffer.toString());
/* 281 */     } catch (URISyntaxException uRISyntaxException) {
/*     */       assert false;
/*     */       
/* 284 */       throw new IllegalArgumentException("Malformed URI created: " + stringBuffer
/* 285 */           .toString());
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public HostIdentifier getHostIdentifier() {
/* 295 */     return this.hostId;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setInterval(int paramInt) {
/* 306 */     this.interval = paramInt;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getInterval() {
/* 315 */     return this.interval;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setLastException(Exception paramException) {
/* 324 */     this.lastException = paramException;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Exception getLastException() {
/* 335 */     return this.lastException;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void clearLastException() {
/* 342 */     this.lastException = null;
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
/*     */   public boolean isErrored() {
/* 355 */     return (this.lastException != null);
/*     */   }
/*     */   
/*     */   public abstract MonitoredVm getMonitoredVm(VmIdentifier paramVmIdentifier) throws MonitorException;
/*     */   
/*     */   public abstract MonitoredVm getMonitoredVm(VmIdentifier paramVmIdentifier, int paramInt) throws MonitorException;
/*     */   
/*     */   public abstract void detach(MonitoredVm paramMonitoredVm) throws MonitorException;
/*     */   
/*     */   public abstract void addHostListener(HostListener paramHostListener) throws MonitorException;
/*     */   
/*     */   public abstract void removeHostListener(HostListener paramHostListener) throws MonitorException;
/*     */   
/*     */   public abstract Set<Integer> activeVms() throws MonitorException;
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\sun\jvmstat\monitor\MonitoredHost.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */