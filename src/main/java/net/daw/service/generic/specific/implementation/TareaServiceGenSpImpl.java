/*
 * Copyright (C) July 2014 Rafael Aznar
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package net.daw.service.generic.specific.implementation;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.daw.service.generic.implementation.TableServiceGenImpl;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletException;
import net.daw.bean.generic.specific.implementation.TareaBeanGenSpImpl;
import net.daw.dao.generic.specific.implementation.TareaDaoGenSpImpl;
import net.daw.helper.AppConfigurationHelper;
import net.daw.helper.EncodingUtilHelper;
import net.daw.helper.ExceptionBooster;
import net.daw.helper.FilterBeanHelper;

public class TareaServiceGenSpImpl extends TableServiceGenImpl {

     public TareaServiceGenSpImpl(String strObject, String pojo, Connection con) {
        super(strObject, pojo, con);
    }

    public String getContenido(Integer id) throws Exception {
        String data;
        try {
            TareaBeanGenSpImpl oTareaBean = new TareaBeanGenSpImpl();
            oTareaBean.setId(id);
            TareaDaoGenSpImpl oTareaDao = new TareaDaoGenSpImpl(strObjectName, oConnection);
            oTareaBean = oTareaDao.get(oTareaBean, AppConfigurationHelper.getJsonDepth());
            return "{\"data\":\"" + oTareaBean.getDescripcion() + "\"}";
        } catch (Exception e) {
            throw new ServletException("GetDescripcion: View Error: " + e.getMessage());
        }
    }
        
    @Override
    public String set(String jason) throws Exception {
        String resultado = null;
        try {
            oConnection.setAutoCommit(false);
            TareaDaoGenSpImpl oTareaDAO = new TareaDaoGenSpImpl(strObjectName, oConnection);
            TareaBeanGenSpImpl oTarea = new TareaBeanGenSpImpl();
            Gson gson = new GsonBuilder().setDateFormat("dd/MM/yyyy HH:mm:ss").create();
            jason = EncodingUtilHelper.decodeURIComponent(jason);
            oTarea = gson.fromJson(jason, oTarea.getClass());
            oTarea = oTareaDAO.set(oTarea);
            Map<String, String> data = new HashMap<>();
            data.put("status", "200");
            data.put("message", Integer.toString(oTarea.getId()));
            resultado = gson.toJson(data);
            oConnection.commit();
        } catch (Exception ex) {
            oConnection.rollback();
            ExceptionBooster.boost(new Exception(this.getClass().getName() + ":set ERROR: " + ex.getMessage()));
        }
        return resultado;
    }
    
    @Override
    public String get(Integer id) throws Exception {
        String data = null;
        try {
            oConnection.setAutoCommit(false);
            TareaDaoGenSpImpl oTareaDAO = new TareaDaoGenSpImpl(strObjectName, oConnection);
            TareaBeanGenSpImpl oTarea = new TareaBeanGenSpImpl(id);
            oTarea = oTareaDAO.get(oTarea, AppConfigurationHelper.getJsonDepth());
            GsonBuilder gsonBuilder = new GsonBuilder();
            gsonBuilder.setDateFormat("dd/MM/yyyy HH:mm:ss");
            Gson gson = gsonBuilder.create();
            data = gson.toJson(oTarea);
            oConnection.commit();
        } catch (Exception ex) {
            oConnection.rollback();
            ExceptionBooster.boost(new Exception(this.getClass().getName() + ":get ERROR: " + ex.getMessage()));
        }
        return data;
    }

    @Override
    public String getPage(int intRegsPerPag, int intPage, ArrayList<FilterBeanHelper> alFilter, HashMap<String, String> hmOrder) throws Exception {
        String data = null;
        try {
            oConnection.setAutoCommit(false);
            TareaDaoGenSpImpl oTareaDAO = new TareaDaoGenSpImpl(strObjectName, oConnection);
            List<TareaBeanGenSpImpl> oTareas = oTareaDAO.getPage(intRegsPerPag, intPage, alFilter, hmOrder);
            GsonBuilder gsonBuilder = new GsonBuilder();
            gsonBuilder.setDateFormat("dd/MM/yyyy HH:mm:ss");
            Gson gson = gsonBuilder.create();
            data = gson.toJson(oTareas);
            data = "{\"list\":" + data + "}";
            oConnection.commit();
        } catch (Exception ex) {
            oConnection.rollback();
            ExceptionBooster.boost(new Exception(this.getClass().getName() + ":getPage ERROR: " + ex.getMessage()));
        }
        return data;
    }
}
