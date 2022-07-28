/*     */ package sun.jvmstat.perfdata.monitor;
/*     */ 
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.util.List;
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
/*     */ public abstract class AbstractPerfDataBuffer
/*     */ {
/*     */   protected PerfDataBufferImpl impl;
/*     */   
/*     */   public int getLocalVmId() {
/*  59 */     return this.impl.getLocalVmId();
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
/*  71 */     return this.impl.getBytes();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getCapacity() {
/*  80 */     return this.impl.getCapacity();
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
/*     */   public Monitor findByName(String paramString) throws MonitorException {
/* 100 */     return this.impl.findByName(paramString);
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
/*     */   public List<Monitor> findByPattern(String paramString) throws MonitorException {
/* 121 */     return this.impl.findByPattern(paramString);
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
/* 133 */     return this.impl.getMonitorStatus();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ByteBuffer getByteBuffer() {
/* 143 */     return this.impl.getByteBuffer();
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
/*     */   protected void createPerfDataBuffer(ByteBuffer paramByteBuffer, int paramInt) throws MonitorException {
/* 162 */     int i = AbstractPerfDataBufferPrologue.getMajorVersion(paramByteBuffer);
/* 163 */     int j = AbstractPerfDataBufferPrologue.getMinorVersion(paramByteBuffer);
/*     */ 
/*     */     
/* 166 */     String str = "sun.jvmstat.perfdata.monitor.v" + i + "_" + j + ".PerfDataBuffer";
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/* 171 */       Class<?> clazz = Class.forName(str);
/* 172 */       Constructor<?> constructor = clazz.getConstructor(new Class[] {
/* 173 */             Class.forName("java.nio.ByteBuffer"), int.class
/*     */           });
/*     */ 
/*     */       
/* 177 */       this.impl = (PerfDataBufferImpl)constructor.newInstance(new Object[] { paramByteBuffer, new Integer(paramInt) });
/*     */ 
/*     */     
/*     */     }
/* 181 */     catch (ClassNotFoundException classNotFoundException) {
/*     */       
/* 183 */       throw new IllegalArgumentException("Could not find " + str + ": " + classNotFoundException
/* 184 */           .getMessage(), classNotFoundException);
/*     */     }
/* 186 */     catch (NoSuchMethodException noSuchMethodException) {
/*     */       
/* 188 */       throw new IllegalArgumentException("Expected constructor missing in " + str + ": " + noSuchMethodException
/*     */           
/* 190 */           .getMessage(), noSuchMethodException);
/*     */     }
/* 192 */     catch (IllegalAccessException illegalAccessException) {
/*     */       
/* 194 */       throw new IllegalArgumentException("Unexpected constructor access in " + str + ": " + illegalAccessException
/*     */           
/* 196 */           .getMessage(), illegalAccessException);
/*     */     }
/* 198 */     catch (InstantiationException instantiationException) {
/* 199 */       throw new IllegalArgumentException(str + "is abstract: " + instantiationException
/* 200 */           .getMessage(), instantiationException);
/*     */     }
/* 202 */     catch (InvocationTargetException invocationTargetException) {
/* 203 */       Throwable throwable = invocationTargetException.getCause();
/* 204 */       if (throwable instanceof MonitorException) {
/* 205 */         throw (MonitorException)throwable;
/*     */       }
/* 207 */       throw new RuntimeException("Unexpected exception: " + invocationTargetException
/* 208 */           .getMessage(), invocationTargetException);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\sun\jvmstat\perfdata\monitor\AbstractPerfDataBuffer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */