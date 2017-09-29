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
package org.fenixedu.academic.ui.struts.action.commons.student;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.academic.domain.StudentCurricularPlan;
import org.fenixedu.academic.domain.curricularRules.executors.RuleResult;
import org.fenixedu.academic.domain.curricularRules.executors.RuleResultMessage;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.exceptions.EnrollmentDomainException;
import org.fenixedu.academic.domain.studentCurriculum.CurriculumLine;
import org.fenixedu.academic.domain.studentCurriculum.curriculumLine.MoveCurriculumLinesBean;
import org.fenixedu.academic.predicate.IllegalDataAccessException;
import org.fenixedu.academic.service.services.student.curriculumLines.MoveCurriculumLines;
import org.fenixedu.academic.ui.struts.action.base.FenixDispatchAction;
import org.fenixedu.bennu.struts.portal.EntryPoint;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixframework.FenixFramework;

abstract public class AbstractCurriculumLinesLocationManagementDA extends FenixDispatchAction {

    @EntryPoint
    private ActionForward prepare(final ActionMapping mapping, final HttpServletRequest request, final boolean isWithRules) {
        request.setAttribute("studentCurricularPlan", getStudentCurricularPlan(request));
        request.setAttribute("withRules", isWithRules);
        return mapping.findForward("showCurriculum");
    }

    public ActionForward prepareWithoutRules(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        return prepare(mapping, request, false);
    }

    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        return prepare(mapping, request, true);
    }

    public ActionForward chooseNewDestination(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        final List<CurriculumLine> selectedCurriculumLines = getSelectedCurriculumLines(request);

        if (selectedCurriculumLines.isEmpty()) {
            addActionMessage(request, "label.student.moveCurriculumLines.curriculumLines.selection.required");
            return prepare(mapping, request, isWithRules(request));
        }

        final boolean withRules = isWithRules(request);
        final MoveCurriculumLinesBean bean = MoveCurriculumLinesBean.buildFrom(selectedCurriculumLines, withRules);
        bean.setStudentCurricularPlan(getStudentCurricularPlan(request));
        bean.withRules(withRules);
        request.setAttribute("moveCurriculumLinesBean", bean);

        return mapping.findForward("chooseNewLocation");
    }

    protected boolean isWithRules(final HttpServletRequest request) {
        return Boolean.valueOf((String) getFromRequest(request, "withRules")).booleanValue();
    }

    public ActionForward moveCurriculumLines(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        final MoveCurriculumLinesBean moveCurriculumLinesBean = getRenderedObject("move-curriculum-lines-bean");

        if (!RenderUtils.getViewState("move-curriculum-lines-bean-entries").isValid()) {
            request.setAttribute("moveCurriculumLinesBean", moveCurriculumLinesBean);
            return mapping.findForward("chooseNewLocation");
        }

        try {
            MoveCurriculumLines.run(moveCurriculumLinesBean);
        } catch (final EnrollmentDomainException e) {
            addRuleResultMessagesToActionMessages(request, e.getFalseResult());
            request.setAttribute("moveCurriculumLinesBean", moveCurriculumLinesBean);
            return mapping.findForward("chooseNewLocation");

        } catch (final IllegalDataAccessException e) {
            addActionMessage(request, "error.NotAuthorized");
            request.setAttribute("moveCurriculumLinesBean", moveCurriculumLinesBean);
            return mapping.findForward("chooseNewLocation");

        } catch (final DomainException e) {
            addActionMessage(request, e.getMessage(), e.getArgs());
            request.setAttribute("moveCurriculumLinesBean", moveCurriculumLinesBean);
            return mapping.findForward("chooseNewLocation");
        }

        request.setAttribute("studentCurricularPlan", moveCurriculumLinesBean.getStudentCurricularPlan());
        request.setAttribute("withRules", moveCurriculumLinesBean.isWithRules());
        return mapping.findForward("showCurriculum");
    }

    private void addRuleResultMessagesToActionMessages(HttpServletRequest request, RuleResult... falseRuleResults) {
        for (final RuleResult ruleResult : falseRuleResults) {
            for (final RuleResultMessage message : ruleResult.getMessages()) {
                if (message.isToTranslate()) {
                    addActionMessage(request, message.getMessage(), message.getArgs());
                } else {
                    addActionMessageLiteral(request, message.getMessage());
                }
            }
        }
    }

    private List<CurriculumLine> getSelectedCurriculumLines(HttpServletRequest request) {
        final String[] selectedCurriculumLineIds = request.getParameterValues("selectedCurriculumLineIds");
        if (selectedCurriculumLineIds == null) {
            return Collections.emptyList();
        }

        final List<CurriculumLine> result = new ArrayList<CurriculumLine>();
        for (final String curriculumLineIdString : selectedCurriculumLineIds) {
            result.add((CurriculumLine) FenixFramework.getDomainObject(curriculumLineIdString));
        }

        return result;
    }

    protected StudentCurricularPlan getStudentCurricularPlan(HttpServletRequest request) {
        return getDomainObject(request, "scpID");
    }

}
