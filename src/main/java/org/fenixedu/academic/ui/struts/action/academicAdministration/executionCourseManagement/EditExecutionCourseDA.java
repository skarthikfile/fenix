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
package org.fenixedu.academic.ui.struts.action.academicAdministration.executionCourseManagement;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.academic.ui.struts.action.academicAdministration.AcademicAdministrationApplication.AcademicAdminExecutionsApp;
import org.fenixedu.academic.ui.struts.action.base.FenixDispatchAction;
import org.fenixedu.bennu.struts.annotations.Forward;
import org.fenixedu.bennu.struts.annotations.Forwards;
import org.fenixedu.bennu.struts.annotations.Mapping;
import org.fenixedu.bennu.struts.portal.EntryPoint;
import org.fenixedu.bennu.struts.portal.StrutsFunctionality;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;

@StrutsFunctionality(app = AcademicAdminExecutionsApp.class, path = "edit-execution-course",
        titleKey = "label.manager.executionCourseManagement.edit.executionCourse",
        accessGroup = "academic(MANAGE_EXECUTION_COURSES)")
@Mapping(path = "/editExecutionCourseChooseExPeriod", module = "academicAdministration")
@Forwards({
        @Forward(name = "editChooseExecutionPeriod",
                path = "/academicAdministration/executionCourseManagement/editChooseExecutionPeriod.jsp"),
        @Forward(name = "editChooseCourseAndYear",
                path = "/academicAdministration/executionCourseManagement/editChooseCourseAndYear.jsp"),
        @Forward(name = "editExecutionCourse",
                path = "/academicAdministration/executionCourseManagement/listExecutionCourseActions.jsp") })
public class EditExecutionCourseDA extends FenixDispatchAction {

    @EntryPoint
    public ActionForward prepareEditExecutionCourse(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        request.setAttribute("sessionBean", new ExecutionCourseBean());

        return mapping.findForward("editChooseExecutionPeriod");
    }

    public ActionForward secondPrepareEditExecutionCourse(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        ExecutionCourseBean bean = getRenderedObject("sessionBeanJSP");

        /*
         * Se chooseNotLinked=checked, entao limpar as dropboxes do curso e ano
         * curricular.
         */
        if (bean.getChooseNotLinked() != null) {
            if (bean.getChooseNotLinked()) {
                bean.setExecutionDegree(null);
                bean.setCurricularYear(null);
            }
        }

        request.setAttribute("sessionBean", bean);

        RenderUtils.invalidateViewState("sessionBeanJSP");

        return mapping.findForward("editChooseCourseAndYear");
    }

    public ActionForward listExecutionCourseActions(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        ExecutionCourseBean bean = getRenderedObject("sessionBeanJSP");
        request.setAttribute("sessionBean", bean);

        return mapping.findForward("editExecutionCourse");
    }
}
