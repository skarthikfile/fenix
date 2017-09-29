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
package org.fenixedu.academic.ui.struts.action;

import java.io.Serializable;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.academic.domain.organizationalStructure.Party;
import org.fenixedu.academic.ui.struts.action.base.FenixDispatchAction;
import org.fenixedu.academic.ui.struts.action.manager.ManagerApplications.ManagerStudentsApp;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.struts.annotations.Forward;
import org.fenixedu.bennu.struts.annotations.Forwards;
import org.fenixedu.bennu.struts.annotations.Mapping;
import org.fenixedu.bennu.struts.portal.EntryPoint;
import org.fenixedu.bennu.struts.portal.StrutsFunctionality;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

@StrutsFunctionality(app = ManagerStudentsApp.class, path = "scholarship-provider", titleKey = "label.scolarships.external")
@Mapping(path = "/externalScholarshipProvider", module = "manager")
@Forwards({ @Forward(name = "list", path = "/manager/listExternalScholarshipProvideres.jsp"),
        @Forward(name = "add", path = "/manager/addExternalScholarshipProvideres.jsp") })
public class ExternalScholarshipProviderDA extends FenixDispatchAction {

    public static class ExternalScholarshipBean implements Serializable {
        private Party selected;

        public Party getSelected() {
            return selected;
        }

        public void setSelected(Party selected) {
            this.selected = selected;
        }
    }

    @EntryPoint
    public ActionForward list(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        Collection<Party> externalScholarshipProvider = Bennu.getInstance().getExternalScholarshipProviderSet();
        request.setAttribute("externalScholarshipProviders", externalScholarshipProvider);
        return mapping.findForward("list");
    }

    @Atomic
    public ActionForward add(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        if (request.getMethod() != "POST") {
            request.setAttribute("bean", new ExternalScholarshipBean());
            return mapping.findForward("add");
        } else {
            ExternalScholarshipBean bean = getRenderedObject();
            Collection<Party> externalScholarshipProvider = Bennu.getInstance().getExternalScholarshipProviderSet();
            externalScholarshipProvider.add(bean.getSelected());
            return redirect("/externalScholarshipProvider.do?method=list", request);
        }
    }

    @Atomic
    public ActionForward remove(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        Party party = FenixFramework.getDomainObject(request.getParameter("provider"));
        Collection<Party> externalScholarshipProvider = Bennu.getInstance().getExternalScholarshipProviderSet();
        externalScholarshipProvider.remove(party);
        return redirect("/externalScholarshipProvider.do?method=list", request);
    }
}
