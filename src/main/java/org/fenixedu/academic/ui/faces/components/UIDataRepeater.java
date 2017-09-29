/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Academic.
 *
 * FenixEdu Academic is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Academic is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.fenixedu.academic.ui.faces.components;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import javax.faces.component.UIComponent;
import javax.faces.component.UIData;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;

public class UIDataRepeater extends UIData {

    private static final String COMPONENT_FAMILY = "DataRepeater";

    private String rowIndexVar;

    private String rowCountVar;

    public UIDataRepeater() {
        super();
    }

    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    @Override
    public String getClientId(FacesContext context) {
        String clientId = super.getClientId(context);

        int rowIndex = getRowIndex();

        if (rowIndex == -1) {
            return clientId;
        } else {
            return clientId + "_" + rowIndex;
        }

    }

    @Override
    public void processDecodes(FacesContext context) {
        int first = getFirst();
        int rows = getRows();
        int last;

        if (rows == 0) {
            last = getRowCount();
        } else {
            last = first + rows;
        }

        for (int rowIndex = first; rowIndex < last; rowIndex++) {
            setRowIndex(rowIndex);
            if (isRowAvailable()) {
                for (Iterator it = getChildren().iterator(); it.hasNext();) {
                    UIComponent child = (UIComponent) it.next();
                    // For some reason its necessary to touch Id property,
                    // otherwise the child control will not call getClientId
                    // on
                    // parent (NamingContainer)
                    child.setId(child.getId());
                    if (!child.isRendered()) {
                        continue;
                    }
                    child.processDecodes(context);
                }
            }
        }

        setRowIndex(-1);

        super.processDecodes(context);
    }

    @Override
    public void processUpdates(FacesContext context) {
        int first = getFirst();
        int rows = getRows();
        int last;
        if (rows == 0) {
            last = getRowCount();
        } else {
            last = first + rows;
        }
        for (int rowIndex = first; rowIndex < last; rowIndex++) {
            setRowIndex(rowIndex);
            if (isRowAvailable()) {
                for (Iterator it = getChildren().iterator(); it.hasNext();) {
                    UIComponent child = (UIComponent) it.next();
                    // For some reason its necessary to touch Id property,
                    // otherwise the child control will not call getClientId
                    // on
                    // parent (NamingContainer)
                    child.setId(child.getId());

                    if (!child.isRendered()) {
                        continue;
                    }
                    child.processUpdates(context);
                }
            }
        }

        setRowIndex(-1);

        super.processUpdates(context);
    }

    @Override
    public void processValidators(FacesContext context) {
        int first = getFirst();
        int rows = getRows();
        int last;
        if (rows == 0) {
            last = getRowCount();
        } else {
            last = first + rows;
        }
        for (int rowIndex = first; rowIndex < last; rowIndex++) {
            setRowIndex(rowIndex);
            if (isRowAvailable()) {
                for (Iterator it = getChildren().iterator(); it.hasNext();) {
                    UIComponent child = (UIComponent) it.next();
                    // For some reason its necessary to touch Id property,
                    // otherwise the child control will not call getClientId
                    // on
                    // parent (NamingContainer)
                    child.setId(child.getId());
                    if (!child.isRendered()) {
                        continue;
                    }
                    child.processValidators(context);
                }
            }
        }

        setRowIndex(-1);

        super.processValidators(context);
    }

    @Override
    public void setRowIndex(int rowIndex) {
        super.setRowIndex(rowIndex);
        String rowIndexVar = getRowIndexVar();
        String rowCountVar = getRowCountVar();
        if (rowIndexVar != null || rowCountVar != null) {
            Map requestMap = FacesContext.getCurrentInstance().getExternalContext().getRequestMap();
            if (rowIndex >= 0) {
                // regular row index, update request scope variables
                if (rowIndexVar != null) {
                    requestMap.put(getRowIndexVar(), new Integer(rowIndex));
                }
                if (rowCountVar != null) {
                    requestMap.put(getRowCountVar(), new Integer(getRowCount()));
                }
            } else {
                // rowIndex == -1 means end of loop --> remove request scope
                // variables
                if (rowIndexVar != null) {
                    requestMap.remove(getRowIndexVar());
                }
                if (rowCountVar != null) {
                    requestMap.remove(getRowCountVar());
                }
            }
        }
    }

    public void setRowIndexVar(String rowIndexVar) {
        this.rowIndexVar = rowIndexVar;
    }

    public String getRowIndexVar() {
        if (this.rowIndexVar != null) {
            return this.rowIndexVar;
        }
        ValueBinding vb = getValueBinding("rowIndexVar");
        return vb != null ? (String) vb.getValue(getFacesContext()) : null;
    }

    public void setRowCountVar(String rowCountVar) {
        this.rowCountVar = rowCountVar;
    }

    public String getRowCountVar() {
        if (this.rowCountVar != null) {
            return this.rowCountVar;
        }
        ValueBinding vb = getValueBinding("rowCountVar");
        return vb != null ? (String) vb.getValue(getFacesContext()) : null;
    }

    @Override
    public Object saveState(FacesContext stateToSave) {
        Object state[] = new Object[3];

        state[0] = super.saveState(stateToSave);
        state[1] = getRowCountVar();
        state[2] = getRowIndexVar();

        return state;
    }

    @Override
    public void restoreState(FacesContext context, Object stateToRestore) {
        Object state[] = (Object[]) stateToRestore;

        super.restoreState(context, state[0]);
        setRowCountVar((String) state[1]);
        setRowIndexVar((String) state[2]);
    }

    @Override
    public Object getValue() {
        return super.getValue() instanceof Collection ? new ArrayList((Collection) super.getValue()) : super.getValue();
    }

}
