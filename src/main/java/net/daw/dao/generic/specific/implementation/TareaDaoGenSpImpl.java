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
package net.daw.dao.generic.specific.implementation;

import net.daw.dao.generic.implementation.TableDaoGenImpl;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import net.daw.bean.generic.specific.implementation.EstadotareaBeanGenSpImpl;
import net.daw.bean.generic.specific.implementation.TareaBeanGenSpImpl;
import net.daw.bean.generic.specific.implementation.TipotareaBeanGenSpImpl;
import net.daw.bean.generic.specific.implementation.UsuarioBeanGenSpImpl;
import net.daw.helper.AppConfigurationHelper;
import net.daw.helper.ExceptionBooster;

public class TareaDaoGenSpImpl extends TableDaoGenImpl<TareaBeanGenSpImpl> {

    private String strTableName = "tarea";
    private Connection oConnection = null;
   public TareaDaoGenSpImpl(String strFuente, Connection pooledConnection) throws Exception {
        super(strFuente, "Tarea", pooledConnection);
        oConnection = pooledConnection;
        
    }

//    public String getDescription(int id) throws Exception {
//        DocumentoBean oDocumentoBean = new DocumentoBean();
//        oDocumentoBean.setId(id);
//        oDocumentoBean = this.get(oDocumentoBean);
//        String description;
//        if (oDocumentoBean.getTitulo().length() > 20) {
//            description = oDocumentoBean.getTitulo().substring(0, 19) + "...";
//        } else {
//            description = oDocumentoBean.getTitulo();
//        }
//        description += " (" + oDocumentoBean.getHits().toString() + " hits)";
//        return description;
//    }
@Override
    public TareaBeanGenSpImpl get(TareaBeanGenSpImpl oTareaBean, Integer expand) throws Exception {
        if (oTareaBean.getId() > 0) {
            try {
                if (!oMysql.existsOne(strTableName, oTareaBean.getId())) {
                    oTareaBean.setId(0);
                } else {
                    expand--;
                    if (expand > 0) {
                        oTareaBean.setDescripcion(oMysql.getOne(strTableName, "descripcion", oTareaBean.getId()));

                        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        String dateInString = oMysql.getOne(strTableName, "fechaentrega", oTareaBean.getId());
                        oTareaBean.setFechaentrega(formatter.parse(dateInString));

                        oTareaBean.setId_usuario(Integer.parseInt(oMysql.getOne(strTableName, "id_usuario", oTareaBean.getId())));
                        oTareaBean.setId_estadotarea(Integer.parseInt(oMysql.getOne(strTableName, "id_estadotarea", oTareaBean.getId())));
                        oTareaBean.setId_tipotarea(Integer.parseInt(oMysql.getOne(strTableName, "id_tipotarea", oTareaBean.getId())));

                        
                        UsuarioBeanGenSpImpl oUsuario = new UsuarioBeanGenSpImpl();
                        oUsuario.setId(Integer.parseInt(oMysql.getOne(strTableName, "id_usuario", oTareaBean.getId())));
                        UsuarioDaoGenSpImpl oUsuarioDAO1 = new UsuarioDaoGenSpImpl("usuario", oConnection);
                        oUsuario = oUsuarioDAO1.get(oUsuario, AppConfigurationHelper.getJsonDepth());
                        oTareaBean.setObj_usuario(oUsuario);
                        
                        TipotareaBeanGenSpImpl oTipotarea = new TipotareaBeanGenSpImpl();
                        oTipotarea.setId(Integer.parseInt(oMysql.getOne(strTableName, "id_tipotarea", oTareaBean.getId())));
                        TipotareaDaoGenSpImpl oTipotareaDAO1 = new TipotareaDaoGenSpImpl("tipotarea", oConnection);
                        oTipotarea = oTipotareaDAO1.get(oTipotarea, AppConfigurationHelper.getJsonDepth());
                        oTareaBean.setObj_tipotarea(oTipotarea);


                        EstadotareaBeanGenSpImpl oEstadotarea = new EstadotareaBeanGenSpImpl();
                        oEstadotarea.setId(Integer.parseInt(oMysql.getOne(strTableName, "id_estadotarea", oTareaBean.getId())));
                        EstadotareaDaoGenSpImpl oEstadotareaDAO2 = new EstadotareaDaoGenSpImpl("estadotarea", oConnection);
                        oEstadotarea = oEstadotareaDAO2.get(oEstadotarea, AppConfigurationHelper.getJsonDepth());
                        oTareaBean.setObj_estadotarea(oEstadotarea);
                    }
                }
            } catch (Exception ex) {
                ExceptionBooster.boost(new Exception(this.getClass().getName() + ":get ERROR: " + ex.getMessage()));
            }
        } else {
            oTareaBean.setId(0);
        }
        return oTareaBean;
    }

}
