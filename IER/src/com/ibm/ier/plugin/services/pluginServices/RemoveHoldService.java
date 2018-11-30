/*    */ package com.ibm.ier.plugin.services.pluginServices;
/*    */ 
/*    */ import com.ibm.ier.plugin.nls.IERUIRuntimeException;
/*    */ import com.ibm.ier.plugin.nls.Logger;
/*    */ import com.ibm.ier.plugin.nls.MessageCode;
/*    */ import com.ibm.ier.plugin.services.IERBasePluginService;
/*    */ import com.ibm.ier.plugin.util.IERUtil;
/*    */ import com.ibm.jarm.api.constants.EntityType;
/*    */ import com.ibm.jarm.api.core.FilePlanRepository;
/*    */ import com.ibm.jarm.api.core.Hold;
/*    */ import com.ibm.jarm.api.core.Holdable;
/*    */ import com.ibm.jarm.api.core.RMFactory.Container;
/*    */ import com.ibm.jarm.api.core.RMFactory.Hold;
/*    */ import com.ibm.jarm.api.core.RMFactory.Record;
/*    */ import com.ibm.json.java.JSONArray;
/*    */ import com.ibm.json.java.JSONObject;
/*    */ import java.util.ArrayList;
/*    */ import javax.servlet.http.HttpServletRequest;
/*    */ import javax.servlet.http.HttpServletResponse;
/*    */ 
/*    */ public class RemoveHoldService
/*    */   extends IERBasePluginService
/*    */ {
/*    */   public String getId()
/*    */   {
/* 26 */     return "ierRemoveHoldService";
/*    */   }
/*    */   
/*    */   public void serviceExecute(HttpServletRequest request, HttpServletResponse response) throws Exception
/*    */   {
/* 31 */     Logger.logEntry(this, "serviceExecute", request);
/*    */     try
/*    */     {
/* 34 */       JSONArray holdableArray = JSONArray.parse(request.getParameter("ier_holdables"));
/* 35 */       JSONArray holdArray = JSONArray.parse(request.getParameter("ier_holds"));
/*    */       
/* 37 */       repository = getFilePlanRepository();
/*    */       
/* 39 */       holdList = new ArrayList();
/* 40 */       for (Object o : holdArray) {
/* 41 */         JSONObject holdObject = (JSONObject)o;
/* 42 */         String id = IERUtil.getIdFromDocIdString((String)holdObject.get("id"));
/* 43 */         Hold hold = RMFactory.Hold.fetchInstance(repository, id, null);
/* 44 */         holdList.add(hold);
/*    */       }
/*    */       
/* 47 */       for (Object o : holdableArray) {
/* 48 */         JSONObject holdableObject = (JSONObject)o;
/* 49 */         String id = IERUtil.getIdFromDocIdString((String)holdableObject.get("id"));
/* 50 */         EntityType entityType = EntityType.getInstanceFromInt(Integer.parseInt((String)holdableObject.get("entityType")));
/* 51 */         holdable = null;
/* 52 */         if ((entityType == EntityType.ElectronicRecord) || (entityType == EntityType.EmailRecord) || (entityType == EntityType.PhysicalRecord) || (entityType == EntityType.Record))
/*    */         {
/* 54 */           holdable = RMFactory.Record.fetchInstance(repository, id, null);
/*    */         } else {
/* 56 */           holdable = (Holdable)RMFactory.Container.fetchInstance(repository, entityType, id, null);
/*    */         }
/* 58 */         for (Hold hold : holdList)
/* 59 */           holdable.removeHold(hold);
/*    */       }
/*    */     } catch (Exception e) { FilePlanRepository repository;
/*    */       ArrayList<Hold> holdList;
/*    */       Holdable holdable;
/* 64 */       throw IERUIRuntimeException.createUIRuntimeException(e, MessageCode.E_REMOVEHOLD_UNEXPECTED, new Object[] { e.getLocalizedMessage() });
/*    */     }
/*    */     
/* 67 */     Logger.logExit(this, "serviceExecute", request);
/*    */   }
/*    */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\ier\plugin\services\pluginServices\RemoveHoldService.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */