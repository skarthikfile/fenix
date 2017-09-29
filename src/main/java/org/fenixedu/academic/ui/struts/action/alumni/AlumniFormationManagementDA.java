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
package org.fenixedu.academic.ui.struts.action.alumni;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.academic.domain.EducationArea;
import org.fenixedu.academic.domain.Formation;
import org.fenixedu.academic.domain.Qualification;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.dto.alumni.formation.AlumniFormation;
import org.fenixedu.academic.dto.alumni.formation.AlumniFormationBean;
import org.fenixedu.academic.service.services.alumni.CreateFormation;
import org.fenixedu.academic.service.services.alumni.EditFormation;
import org.fenixedu.academic.service.services.person.qualification.DeleteQualification;
import org.fenixedu.academic.ui.struts.action.alumni.AlumniApplication.AlumniFormationApp;
import org.fenixedu.bennu.struts.annotations.Forward;
import org.fenixedu.bennu.struts.annotations.Forwards;
import org.fenixedu.bennu.struts.annotations.Mapping;
import org.fenixedu.bennu.struts.portal.EntryPoint;
import org.fenixedu.bennu.struts.portal.StrutsFunctionality;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;

@StrutsFunctionality(app = AlumniFormationApp.class, path = "formation", titleKey = "link.graduate.education")
@Mapping(module = "alumni", path = "/formation")
@Forwards({ @Forward(name = "alumniCreateFormation", path = "/alumni/alumniManageFormation.jsp"),
        @Forward(name = "alumniEditFormation", path = "/alumni/alumniManageFormation.jsp"),
        @Forward(name = "viewAlumniQualifications", path = "/alumni/viewAlumniQualifications.jsp") })
public class AlumniFormationManagementDA extends AlumniEntityManagementDA {

    public ActionForward initFormationManagement(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        request.setAttribute("alumniFormationBean", new AlumniFormationBean(getAlumniFromLoggedPerson(request)));
        return mapping.findForward("viewAlumniQualifications");
    }

    @EntryPoint
    public ActionForward innerFormationManagement(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        request.setAttribute("educationFormationList", getAlumniFromLoggedPerson(request).getFormations());
        return mapping.findForward("viewAlumniQualifications");
    }

    public ActionForward initFormationCreation(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        request.setAttribute("alumniFormation", new AlumniFormation());
        request.setAttribute("formationUpdate", "true");
        return mapping.findForward("alumniCreateFormation");
    }

    public ActionForward updateAlumniFormationTypePostback(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        request.setAttribute("alumniFormation", getObjectFromViewState("alumniFormation"));
        AlumniFormation formationInfo = (AlumniFormation) getObjectFromViewState("alumniFormationDegree");
        formationInfo.updateTypeSchema();
        RenderUtils.invalidateViewState("alumniFormationDegree");
        request.setAttribute("alumniFormationDegree", formationInfo);
        request.setAttribute("formationEducationArea", getFromRequest(request, "formationEducationArea"));
        request.setAttribute("formationUpdate", "true");
        return mapping.findForward("alumniCreateFormation");
    }

    public ActionForward updateAlumniFormationInfoPostback(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        request.setAttribute("alumniFormation", getObjectFromViewState("alumniFormation"));
        AlumniFormation formationInfo = (AlumniFormation) getObjectFromViewState("alumniFormationInstitution");
        formationInfo.updateInstitutionSchema();
        RenderUtils.invalidateViewState("alumniFormationInstitution");
        request.setAttribute("alumniFormationInstitution", formationInfo);
        request.setAttribute("formationEducationArea", getFromRequest(request, "formationEducationArea"));
        request.setAttribute("formationUpdate", "true");
        return mapping.findForward("alumniCreateFormation");
    }

    public ActionForward manageAlumniQualification(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        AlumniFormation formationInfo = (AlumniFormation) getObjectFromViewState("alumniFormation");
        formationInfo.setEducationArea(this.<EducationArea> getDomainObject(request, "formationEducationArea"));

        try {
            if (formationInfo.hasAssociatedFormation()) {
                EditFormation.runEditFormation(formationInfo);
            } else {
                CreateFormation.runCreateFormation(getAlumniFromLoggedPerson(request), formationInfo);
            }
        } catch (DomainException e) {
            addActionMessage("error", request, e.getMessage());
            request.setAttribute("alumniFormation", getObjectFromViewState("alumniFormation"));
            request.setAttribute("alumniFormationDegree", getObjectFromViewState("alumniFormationDegree"));
            request.setAttribute("alumniFormationInstitution", getObjectFromViewState("alumniFormationInstitution"));
            request.setAttribute("formationEducationArea", getFromRequest(request, "formationEducationArea"));
            request.setAttribute("formationUpdate", "true");
            return mapping.findForward("alumniCreateFormation");
        }

        return innerFormationManagement(mapping, actionForm, request, response);
    }

    public ActionForward prepareFormationEdit(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        request.setAttribute("formationUpdate", "true");
        return getFormation(mapping, request);
    }

    private ActionForward getFormation(ActionMapping mapping, HttpServletRequest request) {
        final Qualification qualification = getDomainObject(request, "formationId");
        final AlumniFormation formation = AlumniFormation.buildFrom((Formation) qualification);
        request.setAttribute("formationEducationArea", formation.getEducationArea().getExternalId());
        request.setAttribute("alumniFormation", formation);
        return mapping.findForward("alumniEditFormation");
    }

    public ActionForward deleteFormation(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        if (getFromRequest(request, "cancel") == null) {
            try {
                DeleteQualification.runDeleteQualification(getFromRequest(request, "formationId").toString());
            } catch (DomainException e) {
                addActionMessage(request, e.getKey(), e.getArgs());
            }
        }

        return innerFormationManagement(mapping, actionForm, request, response);
    }
}