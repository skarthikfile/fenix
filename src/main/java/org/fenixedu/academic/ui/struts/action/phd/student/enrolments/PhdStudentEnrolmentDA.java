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
package org.fenixedu.academic.ui.struts.action.phd.student.enrolments;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessages;
import org.fenixedu.academic.domain.EnrolmentPeriodInCurricularCourses;
import org.fenixedu.academic.domain.ExecutionSemester;
import org.fenixedu.academic.domain.StudentCurricularPlan;
import org.fenixedu.academic.domain.student.Registration;
import org.fenixedu.academic.dto.phd.student.PhdStudentEnrolmentBean;
import org.fenixedu.academic.dto.student.enrollment.bolonha.BolonhaStudentEnrollmentBean;
import org.fenixedu.academic.dto.student.enrollment.bolonha.BolonhaStudentOptionalEnrollmentBean;
import org.fenixedu.academic.service.services.exceptions.FenixServiceException;
import org.fenixedu.academic.ui.struts.action.student.enrollment.StudentEnrollmentManagementDA;
import org.fenixedu.academic.ui.struts.action.student.enrollment.bolonha.BolonhaStudentEnrollmentDispatchAction;
import org.fenixedu.bennu.struts.annotations.Forward;
import org.fenixedu.bennu.struts.annotations.Forwards;
import org.fenixedu.bennu.struts.annotations.Mapping;

@Mapping(path = "/phdStudentEnrolment", module = "student", functionality = StudentEnrollmentManagementDA.class)
@Forwards({
        @Forward(name = "showWelcome", path = "/phd/student/enrolments/showWelcome.jsp"),
        @Forward(name = "showDegreeModulesToEnrol", path = "/phd/student/enrolments/showDegreeModulesToEnrol.jsp"),
        @Forward(name = "chooseOptionalCurricularCourseToEnrol",
                path = "/phd/student/enrolments/chooseOptionalCurricularCourseToEnrol.jsp") })
public class PhdStudentEnrolmentDA extends BolonhaStudentEnrollmentDispatchAction {

    private Registration getRegistration(HttpServletRequest request) {
        Registration registration = (Registration) request.getAttribute("registration");
        if (registration != null) {
            return registration;
        }

        registration = getDomainObject(request, "registrationOid");
        if (registration != null) {
            return registration;
        }

        final BolonhaStudentEnrollmentBean bean = getBolonhaStudentEnrollmentBeanFromViewState();
        if (bean != null && bean.getRegistration() != null) {
            return bean.getRegistration();
        }

        final BolonhaStudentOptionalEnrollmentBean optionalBean = getBolonhaStudentOptionalEnrollmentBeanFromViewState();
        if (optionalBean != null && optionalBean.getRegistration() != null) {
            return optionalBean.getRegistration();
        }

        return null;
    }

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        final Registration registration = getRegistration(request);

        if (registration != null && registration.getPhdIndividualProgramProcess() == null) {
            final ActionMessages actionMessages = new ActionMessages();
            request.setAttribute(ACTION_MESSAGES_REQUEST_KEY, actionMessages);
            addActionMessage(request, "label.phd.registration.without.phd.program.process");
            saveMessages(request, actionMessages);

            return mapping.findForward("enrollmentCannotProceed");
        }

        return super.execute(mapping, actionForm, request, response);
    }

    @Override
    protected BolonhaStudentEnrollmentBean createStudentEnrolmentBean(ActionForm form,
            StudentCurricularPlan studentCurricularPlan, ExecutionSemester executionSemester) {
        return new PhdStudentEnrolmentBean(studentCurricularPlan, executionSemester, getCurricularYearForCurricularCourses(),
                getCurricularRuleLevel(form));
    }

    @Override
    public ActionForward showWelcome(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        final Registration registration = (Registration) request.getAttribute("registration");
        final ExecutionSemester semester = ExecutionSemester.readActualExecutionSemester();
        final EnrolmentPeriodInCurricularCourses period =
                registration.getLastDegreeCurricularPlan().getEnrolmentPeriodInCurricularCoursesBy(semester);

        if (period != null) {
            request.setAttribute("enrolmentPeriod", period);
        }

        super.showWelcome(mapping, form, request, response);
        return mapping.findForward("showWelcome");
    }

    private void addCompetenceCoursesAvalailableToEnrol(HttpServletRequest request, StudentCurricularPlan studentCurricularPlan) {
        request.setAttribute("competenceCoursesAvailableToEnrol", studentCurricularPlan.getRegistration()
                .getPhdIndividualProgramProcess().getCompetenceCoursesAvailableToEnrol());
    }

    @Override
    protected ActionForward prepareShowDegreeModulesToEnrol(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response, StudentCurricularPlan studentCurricularPlan, ExecutionSemester executionSemester) {

        addCompetenceCoursesAvalailableToEnrol(request, studentCurricularPlan);
        return super.prepareShowDegreeModulesToEnrol(mapping, form, request, response, studentCurricularPlan, executionSemester);
    }

    @Override
    public ActionForward prepareChooseOptionalCurricularCourseToEnrol(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {

        final BolonhaStudentEnrollmentBean bean = getBolonhaStudentEnrollmentBeanFromViewState();
        addCompetenceCoursesAvalailableToEnrol(request, bean.getStudentCurricularPlan());
        return super.prepareChooseOptionalCurricularCourseToEnrol(mapping, form, request, response);
    }

    @Override
    public ActionForward updateParametersToSearchOptionalCurricularCourses(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) {

        final BolonhaStudentOptionalEnrollmentBean bean = getBolonhaStudentOptionalEnrollmentBeanFromViewState();
        addCompetenceCoursesAvalailableToEnrol(request, bean.getStudentCurricularPlan());
        return super.updateParametersToSearchOptionalCurricularCourses(mapping, actionForm, request, response);
    }

    @Override
    public ActionForward enrolInOptionalCurricularCourse(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {

        final BolonhaStudentOptionalEnrollmentBean bean = getBolonhaStudentOptionalEnrollmentBeanFromViewState();
        addCompetenceCoursesAvalailableToEnrol(request, bean.getStudentCurricularPlan());
        return super.enrolInOptionalCurricularCourse(mapping, form, request, response);
    }

    @Override
    public ActionForward prepareChooseCycleCourseGroupToEnrol(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        throw new RuntimeException("error.PhdStudentEnrolmentBean.unsupported.operation");
    }

    @Override
    public ActionForward enrolInCycleCourseGroup(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {
        throw new RuntimeException("error.PhdStudentEnrolmentBean.unsupported.operation");
    }

    @Override
    protected void enroledWithSuccess(HttpServletRequest request, BolonhaStudentEnrollmentBean bean) {
        if (bean.getStudentCurricularPlan().hasAnyEnrolmentForExecutionPeriod(bean.getExecutionPeriod())) {
            addActionMessage("warning", request, "message.phd.enrolments.waiting.for.approval");
        }
    }
}
