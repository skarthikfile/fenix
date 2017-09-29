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
package org.fenixedu.academic.ui.struts.action.coordinator;

import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.academic.dto.InfoCurricularCourse;
import org.fenixedu.academic.service.services.commons.ReadCurricularCourseByID;
import org.fenixedu.academic.service.services.commons.student.ReadStudentListByCurricularCourse;
import org.fenixedu.academic.service.services.exceptions.ExistingServiceException;
import org.fenixedu.academic.service.services.exceptions.FenixServiceException;
import org.fenixedu.academic.service.services.exceptions.NonExistingServiceException;
import org.fenixedu.academic.service.services.exceptions.NotAuthorizedException;
import org.fenixedu.academic.service.services.masterDegree.administrativeOffice.ReadCurricularCoursesByDegree;
import org.fenixedu.academic.ui.struts.action.base.FenixDispatchAction;
import org.fenixedu.academic.ui.struts.action.exceptions.ExistingActionException;
import org.fenixedu.academic.ui.struts.action.exceptions.FenixActionException;
import org.fenixedu.academic.ui.struts.action.exceptions.NonExistingActionException;
import org.fenixedu.academic.ui.struts.config.FenixErrorExceptionHandler;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.struts.annotations.ExceptionHandling;
import org.fenixedu.bennu.struts.annotations.Exceptions;
import org.fenixedu.bennu.struts.annotations.Forward;
import org.fenixedu.bennu.struts.annotations.Forwards;
import org.fenixedu.bennu.struts.annotations.Mapping;

@Mapping(path = "/listStudentsByCourse", module = "coordinator", functionality = DegreeCoordinatorIndex.class)
@Forwards({ @Forward(name = "ChooseSuccess", path = "/coordinator/student/displayStudentListByCourse_bd.jsp"),
        @Forward(name = "PrepareSuccess", path = "/coordinator/student/displayStudentListByDegree_bd.jsp"),
        @Forward(name = "NoStudents", path = "/coordinator/listStudentsForCoordinator.do?method=getCurricularCourses"),
        @Forward(name = "NotAuthorized", path = "/coordinator/student/notAuthorized_bd.jsp") })
@Exceptions(@ExceptionHandling(key = "resources.Action.exceptions.NonExistingActionException",
        handler = FenixErrorExceptionHandler.class, type = NonExistingActionException.class))
public class ChooseCurricularCourseDispatchActionForCoordinator extends FenixDispatchAction {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        DegreeCoordinatorIndex.setCoordinatorContext(request);
        return super.execute(mapping, actionForm, request, response);
    }

    public ActionForward prepareChooseCurricularCourse(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        String executionYear = getFromRequest("executionYear", request);
        String degree = getFromRequest("degree", request);

        request.setAttribute("jspTitle", getFromRequest("jspTitle", request));
        request.setAttribute("executionYear", executionYear);
        request.setAttribute("degree", degree);

        // Get the Curricular Course List

        User userView = getUserView(request);
        List curricularCourseList = null;
        try {
            curricularCourseList = ReadCurricularCoursesByDegree.run(executionYear, degree);
        } catch (NonExistingServiceException e) {
            ActionErrors errors = new ActionErrors();
            errors.add("nonExisting", new ActionError("message.public.notfound.curricularCourses"));
            saveErrors(request, errors);
            return mapping.getInputForward();

        } catch (ExistingServiceException e) {
            throw new ExistingActionException(e);
        }
        Collections.sort(curricularCourseList, new BeanComparator("name"));
        request.setAttribute("curricularCourses", curricularCourseList);

        return mapping.findForward("PrepareSuccess");
    }

    public ActionForward chooseCurricularCourse(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        request.setAttribute("courseID", getFromRequest("courseID", request));

        // parameters necessary to write in jsp
        request.setAttribute("curricularCourse", getFromRequest("curricularCourse", request));
        request.setAttribute("executionYear", getFromRequest("executionYear", request));
        request.setAttribute("degree", getFromRequest("degree", request));
        request.setAttribute("jspTitle", getFromRequest("jspTitle", request));

        return mapping.findForward("ChooseSuccess");
    }

    public ActionForward chooseCurricularCourseByID(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        if (request.getParameter("degreeCurricularPlanID") != null) {
            String degreeCurricularPlanID = request.getParameter("degreeCurricularPlanID");
            request.setAttribute("degreeCurricularPlanID", degreeCurricularPlanID);
        }
        request.setAttribute("courseID", getFromRequest("courseID", request));

        String courseID = getFromRequest("courseID", request);
        User userView = getUserView(request);

        List studentList = null;
        try {
            studentList = ReadStudentListByCurricularCourse.runReadStudentListByCurricularCourse(userView, courseID, null);
        } catch (NotAuthorizedException e) {
            return mapping.findForward("NotAuthorized");
        } catch (NonExistingServiceException e) {
            ActionErrors errors = new ActionErrors();
            errors.add("nonExisting", new ActionError("error.exception.noStudents"));
            saveErrors(request, errors);
            return mapping.findForward("NoStudents");
        }

        InfoCurricularCourse infoCurricularCourse = null;
        try {

            infoCurricularCourse = ReadCurricularCourseByID.run(courseID);
        } catch (NonExistingServiceException e) {

        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }

        if (infoCurricularCourse != null) {
            request.setAttribute("infoCurricularCourse", infoCurricularCourse);
        }

        request.setAttribute("enrolment_list", studentList);

        String value = request.getParameter("viewPhoto");
        if (value != null && value.equals("true")) {
            request.setAttribute("viewPhoto", Boolean.TRUE);
        } else {
            request.setAttribute("viewPhoto", Boolean.FALSE);
        }

        return mapping.findForward("ChooseSuccess");
    }

    private String getFromRequest(String parameter, HttpServletRequest request) {
        String parameterString = request.getParameter(parameter);
        if (parameterString == null) {
            parameterString = (String) request.getAttribute(parameter);
        }
        return parameterString;
    }

}
