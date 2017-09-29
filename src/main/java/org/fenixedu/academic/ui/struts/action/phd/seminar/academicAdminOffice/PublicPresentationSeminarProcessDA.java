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
package org.fenixedu.academic.ui.struts.action.phd.seminar.academicAdminOffice;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.phd.PhdProcessState;
import org.fenixedu.academic.domain.phd.seminar.PublicPresentationSeminarProcess.AddState;
import org.fenixedu.academic.domain.phd.seminar.PublicPresentationSeminarProcess.EditProcessAttributes;
import org.fenixedu.academic.domain.phd.seminar.PublicPresentationSeminarProcess.RemoveLastState;
import org.fenixedu.academic.domain.phd.seminar.PublicPresentationSeminarProcess.RevertToWaitingComissionForValidation;
import org.fenixedu.academic.domain.phd.seminar.PublicPresentationSeminarProcess.RevertToWaitingForComissionConstitution;
import org.fenixedu.academic.domain.phd.seminar.PublicPresentationSeminarProcessBean;
import org.fenixedu.academic.service.services.caseHandling.ExecuteProcessActivity;
import org.fenixedu.academic.ui.struts.action.phd.PhdProcessStateBean;
import org.fenixedu.academic.ui.struts.action.phd.academicAdminOffice.PhdIndividualProgramProcessDA;
import org.fenixedu.academic.ui.struts.action.phd.seminar.CommonPublicPresentationSeminarDA;
import org.fenixedu.bennu.struts.annotations.Forward;
import org.fenixedu.bennu.struts.annotations.Forwards;
import org.fenixedu.bennu.struts.annotations.Mapping;

@Mapping(path = "/publicPresentationSeminarProcess", module = "academicAdministration",
        functionality = PhdIndividualProgramProcessDA.class)
@Forwards({ @Forward(name = "submitComission", path = "/phd/seminar/academicAdminOffice/submitComission.jsp"),
        @Forward(name = "validateComission", path = "/phd/seminar/academicAdminOffice/validateComission.jsp"),
        @Forward(name = "schedulePresentationDate", path = "/phd/seminar/academicAdminOffice/schedulePresentationDate.jsp"),
        @Forward(name = "uploadReport", path = "/phd/seminar/academicAdminOffice/uploadReport.jsp"),
        @Forward(name = "validateReport", path = "/phd/seminar/academicAdminOffice/validateReport.jsp"),
        @Forward(name = "manageStates", path = "/phd/seminar/academicAdminOffice/manageStates.jsp"),
        @Forward(name = "editProcessAttributes", path = "/phd/seminar/academicAdminOffice/editProcessAttributes.jsp"),
        @Forward(name = "editPhdProcessState", path = "/phd/seminar/academicAdminOffice/editState.jsp") })
public class PublicPresentationSeminarProcessDA extends CommonPublicPresentationSeminarDA {

    public ActionForward revertToWaitingForComissionConstitution(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {

        try {
            ExecuteProcessActivity.run(getProcess(request), RevertToWaitingForComissionConstitution.class,
                    getRenderedObject("submitComissionBean"));

            addSuccessMessage(request, "message.comission.submitted.with.success");

        } catch (DomainException e) {
            addErrorMessage(request, e.getKey(), e.getArgs());
            return mapping.findForward("submitComission");
        }

        return viewIndividualProgramProcess(request, getProcess(request));
    }

    public ActionForward revertToWaitingComissionValidation(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        try {
            ExecuteProcessActivity.run(getProcess(request), RevertToWaitingComissionForValidation.class,
                    getRenderedObject("submitComissionBean"));

            addSuccessMessage(request, "message.comission.submitted.with.success");

        } catch (DomainException e) {
            addErrorMessage(request, e.getKey(), e.getArgs());
            return mapping.findForward("submitComission");
        }

        return viewIndividualProgramProcess(request, getProcess(request));
    }

    public ActionForward manageStates(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        final PublicPresentationSeminarProcessBean bean =
                new PublicPresentationSeminarProcessBean(getProcess(request).getIndividualProgramProcess());

        request.setAttribute("processBean", bean);
        return mapping.findForward("manageStates");
    }

    public ActionForward addState(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        PublicPresentationSeminarProcessBean bean = getRenderedObject("processBean");

        try {
            ExecuteProcessActivity.run(getProcess(request), AddState.class, bean);
        } catch (final DomainException e) {
            addErrorMessage(request, e.getMessage(), e.getArgs());

            return manageStates(mapping, form, request, response);
        }

        return manageStates(mapping, form, request, response);
    }

    public ActionForward addStateInvalid(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        PublicPresentationSeminarProcessBean bean = getRenderedObject("processBean");
        request.setAttribute("processBean", bean);

        return mapping.findForward("manageStates");
    }

    public ActionForward removeLastState(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        try {
            ExecuteProcessActivity.run(getProcess(request), RemoveLastState.class, null);
        } catch (final DomainException e) {
            addErrorMessage(request, e.getMessage(), e.getArgs());
        }
        return manageStates(mapping, form, request, response);
    }

    public ActionForward prepareEditProcessAttributes(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        PublicPresentationSeminarProcessBean bean =
                new PublicPresentationSeminarProcessBean(getProcess(request).getIndividualProgramProcess());
        request.setAttribute("processBean", bean);

        return mapping.findForward("editProcessAttributes");
    }

    public ActionForward editProcessAttributesInvalid(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        request.setAttribute("processBean", getRenderedObject("processBean"));
        return mapping.findForward("editProcessAttributes");
    }

    public ActionForward editProcessAttributes(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        PublicPresentationSeminarProcessBean bean = getRenderedObject("processBean");

        ExecuteProcessActivity.run(getProcess(request), EditProcessAttributes.class, bean);

        return viewIndividualProgramProcess(mapping, form, request, response);
    }

    /* EDIT PHD STATES */

    public ActionForward prepareEditState(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        PhdProcessState state = getDomainObject(request, "stateId");
        PhdProcessStateBean bean = new PhdProcessStateBean(state);

        request.setAttribute("bean", bean);

        return mapping.findForward("editPhdProcessState");
    }

    public ActionForward editState(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        PhdProcessStateBean bean = getRenderedObject("bean");
        bean.getState().editStateDate(bean);

        return manageStates(mapping, form, request, response);
    }

    public ActionForward editStateInvalid(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        PhdProcessStateBean bean = getRenderedObject("bean");
        request.setAttribute("bean", bean);

        return mapping.findForward("editPhdProcessState");
    }

    /* EDIT PHD STATES */

}
