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
package org.fenixedu.academic.ui.struts.action.candidacy.graduatedPerson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map.Entry;
import java.util.SortedSet;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.academic.domain.Degree;
import org.fenixedu.academic.domain.ExecutionInterval;
import org.fenixedu.academic.domain.candidacyProcess.CandidacyProcess;
import org.fenixedu.academic.domain.candidacyProcess.IndividualCandidacyProcess;
import org.fenixedu.academic.domain.candidacyProcess.graduatedPerson.DegreeCandidacyForGraduatedPersonIndividualProcess;
import org.fenixedu.academic.domain.candidacyProcess.graduatedPerson.DegreeCandidacyForGraduatedPersonProcess;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.period.DegreeCandidacyForGraduatedPersonCandidacyPeriod;
import org.fenixedu.academic.service.services.exceptions.FenixServiceException;
import org.fenixedu.academic.ui.struts.action.academicAdministration.AcademicAdministrationApplication.AcademicAdminCandidaciesApp;
import org.fenixedu.academic.ui.struts.action.candidacy.CandidacyProcessDA;
import org.fenixedu.academic.util.Bundle;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.fenixedu.bennu.struts.annotations.Forward;
import org.fenixedu.bennu.struts.annotations.Forwards;
import org.fenixedu.bennu.struts.annotations.Mapping;
import org.fenixedu.bennu.struts.portal.StrutsFunctionality;
import org.fenixedu.commons.spreadsheet.Spreadsheet;
import org.fenixedu.commons.spreadsheet.Spreadsheet.Row;
import org.fenixedu.commons.spreadsheet.SpreadsheetXLSExporter;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;

@StrutsFunctionality(app = AcademicAdminCandidaciesApp.class, path = "degree-candidacy-for-graduated-person",
        titleKey = "label.candidacy.graduatedPerson",
        accessGroup = "(academic(MANAGE_CANDIDACY_PROCESSES) | academic(MANAGE_INDIVIDUAL_CANDIDACIES))",
        bundle = "ApplicationResources")
@Mapping(path = "/caseHandlingDegreeCandidacyForGraduatedPersonProcess", module = "academicAdministration",
        formBeanClass = DegreeCandidacyForGraduatedPersonProcessDA.DegreeCandidacyForGraduatedPersonProcessForm.class)
@Forwards({ @Forward(name = "intro", path = "/candidacy/graduatedPerson/mainCandidacyProcess.jsp"),
        @Forward(name = "prepare-create-new-process", path = "/candidacy/createCandidacyPeriod.jsp"),
        @Forward(name = "prepare-edit-candidacy-period", path = "/candidacy/editCandidacyPeriod.jsp"),
        @Forward(name = "send-to-coordinator", path = "/candidacy/sendToCoordinator.jsp"),
        @Forward(name = "send-to-scientificCouncil", path = "/candidacy/sendToScientificCouncil.jsp"),
        @Forward(name = "view-candidacy-results", path = "/candidacy/graduatedPerson/viewCandidacyResults.jsp"),
        @Forward(name = "introduce-candidacy-results", path = "/candidacy/graduatedPerson/introduceCandidacyResults.jsp"),
        @Forward(name = "create-registrations", path = "/candidacy/createRegistrations.jsp"),
        @Forward(name = "prepare-select-available-degrees", path = "/candidacy/selectAvailableDegrees.jsp") })
public class DegreeCandidacyForGraduatedPersonProcessDA extends CandidacyProcessDA {

    static public class DegreeCandidacyForGraduatedPersonProcessForm extends CandidacyProcessForm {
        private String selectedProcessId;

        public String getSelectedProcessId() {
            return selectedProcessId;
        }

        public void setSelectedProcessId(String selectedProcessId) {
            this.selectedProcessId = selectedProcessId;
        }
    }

    @Override
    protected Class getProcessType() {
        return DegreeCandidacyForGraduatedPersonProcess.class;
    }

    @Override
    protected Class getChildProcessType() {
        return DegreeCandidacyForGraduatedPersonIndividualProcess.class;
    }

    @Override
    protected Class getCandidacyPeriodType() {
        return DegreeCandidacyForGraduatedPersonCandidacyPeriod.class;
    }

    @Override
    protected DegreeCandidacyForGraduatedPersonProcess getProcess(HttpServletRequest request) {
        return (DegreeCandidacyForGraduatedPersonProcess) super.getProcess(request);
    }

    @Override
    protected DegreeCandidacyForGraduatedPersonProcess getCandidacyProcess(HttpServletRequest request,
            final ExecutionInterval executionInterval) {
        final String selectedProcessId = getStringFromRequest(request, "selectedProcessId");
        if (selectedProcessId != null) {
            List<DegreeCandidacyForGraduatedPersonCandidacyPeriod> candidacyPeriods = getCandidacyPeriods(executionInterval);
            for (final DegreeCandidacyForGraduatedPersonCandidacyPeriod candidacyPeriod : candidacyPeriods) {
                if (candidacyPeriod.getDegreeCandidacyForGraduatedPersonProcess().getExternalId().equals(selectedProcessId)) {
                    return candidacyPeriod.getDegreeCandidacyForGraduatedPersonProcess();
                }
            }
        }
        return null;
    }

    private List<DegreeCandidacyForGraduatedPersonCandidacyPeriod> getCandidacyPeriods(final ExecutionInterval executionInterval) {
        List<DegreeCandidacyForGraduatedPersonCandidacyPeriod> candidacyPeriods =
                (List<DegreeCandidacyForGraduatedPersonCandidacyPeriod>) executionInterval
                        .getCandidacyPeriods(DegreeCandidacyForGraduatedPersonCandidacyPeriod.class);
        return candidacyPeriods;
    }

    @Override
    protected ActionForward introForward(final ActionMapping mapping) {
        return mapping.findForward("intro");
    }

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        setChooseDegreeBean(request);
        return super.execute(mapping, actionForm, request, response);
    }

    @Override
    public ActionForward listProcessAllowedActivities(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        setCandidacyProcessInformation(request, getProcess(request));
        setCandidacyProcessInformation(form, getProcess(request));
        request.setAttribute("candidacyProcesses", getCandidacyProcesses(getProcess(request).getCandidacyExecutionInterval()));
        return introForward(mapping);
    }

    protected void setCandidacyProcessInformation(final ActionForm actionForm, final CandidacyProcess process) {
        final DegreeCandidacyForGraduatedPersonProcessForm form = (DegreeCandidacyForGraduatedPersonProcessForm) actionForm;
        form.setSelectedProcessId(process.getExternalId());
        form.setExecutionIntervalId(process.getCandidacyExecutionInterval().getExternalId());
    }

    private void setChooseDegreeBean(HttpServletRequest request) {
        ChooseDegreeBean chooseDegreeBean = (ChooseDegreeBean) getObjectFromViewState("choose.degree.bean");

        if (chooseDegreeBean == null) {
            chooseDegreeBean = new ChooseDegreeBean();
        }

        request.setAttribute("chooseDegreeBean", chooseDegreeBean);
    }

    private ChooseDegreeBean getChooseDegreeBean(HttpServletRequest request) {
        return (ChooseDegreeBean) request.getAttribute("chooseDegreeBean");
    }

    @Override
    protected void setStartInformation(ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) {
        if (!hasExecutionInterval(request)) {
            final List<ExecutionInterval> executionIntervals =
                    ExecutionInterval.readExecutionIntervalsWithCandidacyPeriod(getCandidacyPeriodType());
            if (executionIntervals.size() == 1) {
                final ExecutionInterval executionInterval = executionIntervals.iterator().next();
                final List<DegreeCandidacyForGraduatedPersonProcess> candidacyProcesses =
                        getCandidacyProcesses(executionInterval);

                if (candidacyProcesses.size() == 1) {
                    final DegreeCandidacyForGraduatedPersonProcess process = candidacyProcesses.iterator().next();
                    setCandidacyProcessInformation(request, process);
                    setCandidacyProcessInformation(actionForm, getProcess(request));
                    request.setAttribute("candidacyProcesses", candidacyProcesses);
                    ChooseDegreeBean chooseDegreeBean = getChooseDegreeBean(request);
                    chooseDegreeBean.setCandidacyProcess(process);
                    return;
                }
            } else {
                request.setAttribute("canCreateProcess", canCreateProcess(getProcessType().getName()));
                request.setAttribute("executionIntervals", executionIntervals);
            }
        } else {
            final ExecutionInterval executionInterval = getExecutionInterval(request);
            final DegreeCandidacyForGraduatedPersonProcess candidacyProcess = getCandidacyProcess(request, executionInterval);

            if (candidacyProcess != null) {
                setCandidacyProcessInformation(request, candidacyProcess);
                setCandidacyProcessInformation(actionForm, getProcess(request));
            } else {
                final List<DegreeCandidacyForGraduatedPersonProcess> candidacyProcesses =
                        getCandidacyProcesses(executionInterval);

                if (candidacyProcesses.size() == 1) {
                    final DegreeCandidacyForGraduatedPersonProcess process = candidacyProcesses.iterator().next();
                    setCandidacyProcessInformation(request, process);
                    setCandidacyProcessInformation(actionForm, getProcess(request));
                    request.setAttribute("candidacyProcesses", candidacyProcesses);
                    ChooseDegreeBean chooseDegreeBean = getChooseDegreeBean(request);
                    chooseDegreeBean.setCandidacyProcess(process);
                    return;
                }

                request.setAttribute("canCreateProcess", canCreateProcess(getProcessType().getName()));
                request.setAttribute("executionIntervals", getExecutionIntervalsWithCandidacyPeriod());
            }
            request.setAttribute("candidacyProcesses", getCandidacyProcesses(executionInterval));
        }
    }

    private List<ExecutionInterval> getExecutionIntervalsWithCandidacyPeriod() {
        return ExecutionInterval.readExecutionIntervalsWithCandidacyPeriod(getCandidacyPeriodType());
    }

    protected List<DegreeCandidacyForGraduatedPersonProcess> getCandidacyProcesses(final ExecutionInterval executionInterval) {
        final List<DegreeCandidacyForGraduatedPersonProcess> result = new ArrayList<DegreeCandidacyForGraduatedPersonProcess>();
        for (final DegreeCandidacyForGraduatedPersonCandidacyPeriod period : getCandidacyPeriods(executionInterval)) {
            result.add(period.getDegreeCandidacyForGraduatedPersonProcess());
        }
        return result;
    }

    public ActionForward prepareExecuteSendToCoordinator(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) {
        return mapping.findForward("send-to-coordinator");
    }

    public ActionForward executeSendToCoordinator(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {
        try {
            executeActivity(getProcess(request), "SendToCoordinator");
        } catch (DomainException e) {
            addActionMessage(request, e.getMessage(), e.getArgs());
            return prepareExecuteSendToCoordinator(mapping, actionForm, request, response);
        }
        return listProcessAllowedActivities(mapping, actionForm, request, response);
    }

    public ActionForward prepareExecuteSendToScientificCouncil(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) {
        return mapping.findForward("send-to-scientificCouncil");
    }

    public ActionForward executeSendToScientificCouncil(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {
        try {
            executeActivity(getProcess(request), "SendToScientificCouncil");
        } catch (final DomainException e) {
            addActionMessage(request, e.getMessage(), e.getArgs());
            return prepareExecuteSendToScientificCouncil(mapping, actionForm, request, response);
        }
        return listProcessAllowedActivities(mapping, actionForm, request, response);
    }

    public ActionForward prepareExecutePrintCandidacies(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws IOException {

        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-disposition", "attachment; filename=" + getReportFilename());

        writeReport(getProcess(request), response.getOutputStream());
        response.getOutputStream().flush();
        response.flushBuffer();
        return null;
    }

    private void writeReport(final DegreeCandidacyForGraduatedPersonProcess process, final ServletOutputStream outputStream)
            throws IOException {
        final List<Spreadsheet> spreadsheets = new ArrayList<Spreadsheet>();
        for (final Entry<Degree, SortedSet<DegreeCandidacyForGraduatedPersonIndividualProcess>> entry : process
                .getValidDegreeCandidaciesForGraduatedPersonsByDegree().entrySet()) {
            spreadsheets.add(buildReport(entry.getKey(), entry.getValue()));
        }
        new SpreadsheetXLSExporter().exportToXLSSheets(outputStream, spreadsheets);
    }

    private Spreadsheet buildReport(final Degree degree,
            final SortedSet<DegreeCandidacyForGraduatedPersonIndividualProcess> candidacyProcesses) {

        final Spreadsheet spreadsheet = new Spreadsheet(degree.getSigla(), getHeader());
        for (final DegreeCandidacyForGraduatedPersonIndividualProcess process : candidacyProcesses) {
            final Row row = spreadsheet.addRow();
            row.setCell(process.getPersonalDetails().getName());
            row.setCell(process.getPrecedentDegreeInformation().getDegreeAndInstitutionName());
            row.setCell(process.getCandidacyAffinity());
            row.setCell(process.getCandidacyDegreeNature());
            row.setCell(process.getPrecedentDegreeInformation().getConclusionGrade());
            row.setCell(process.getCandidacyGrade());
            if (process.isCandidacyAccepted() || process.isCandidacyRejected()) {
                row.setCell(BundleUtil.getString(Bundle.ENUMERATION, process.getCandidacyState().getQualifiedName()));
            } else {
                row.setCell("");
            }
        }

        return spreadsheet;
    }

    private List<Object> getHeader() {
        final List<Object> result = new ArrayList<Object>();
        result.add(BundleUtil.getString(Bundle.APPLICATION, "label.name"));
        result.add(BundleUtil.getString(Bundle.APPLICATION, "label.candidacy.degree.and.school"));
        result.add(BundleUtil.getString(Bundle.APPLICATION, "label.candidacy.affinity"));
        result.add(BundleUtil.getString(Bundle.APPLICATION, "label.candidacy.degreeNature"));
        result.add(BundleUtil.getString(Bundle.APPLICATION, "label.candidacy.mfc"));
        result.add(BundleUtil.getString(Bundle.APPLICATION, "label.candidacy.grade"));
        result.add(BundleUtil.getString(Bundle.APPLICATION, "label.candidacy.result"));
        return result;
    }

    static public class DegreeCandidacyForGraduatedPersonDegreeBean extends CandidacyDegreeBean {
        public DegreeCandidacyForGraduatedPersonDegreeBean(final DegreeCandidacyForGraduatedPersonIndividualProcess process) {
            setPersonalDetails(process.getPersonalDetails());
            setDegree(process.getCandidacySelectedDegree());
            setState(process.getCandidacyState());
            setRegistrationCreated(process.hasRegistrationForCandidacy());
        }
    }

    @Override
    protected List<CandidacyDegreeBean> createCandidacyDegreeBeans(HttpServletRequest request) {
        final DegreeCandidacyForGraduatedPersonProcess process = getProcess(request);
        final List<CandidacyDegreeBean> candidacyDegreeBeans = new ArrayList<CandidacyDegreeBean>();
        for (final DegreeCandidacyForGraduatedPersonIndividualProcess child : process
                .getAcceptedDegreeCandidacyForGraduatedPersonIndividualCandidacies()) {
            candidacyDegreeBeans.add(new DegreeCandidacyForGraduatedPersonDegreeBean(child));
        }
        Collections.sort(candidacyDegreeBeans);
        return candidacyDegreeBeans;
    }

    @Override
    protected Spreadsheet buildIndividualCandidacyReport(final Spreadsheet spreadsheet,
            final IndividualCandidacyProcess individualCandidacyProcess) {
        DegreeCandidacyForGraduatedPersonIndividualProcess degreeCandidacyForGraduatedPersonProcess =
                (DegreeCandidacyForGraduatedPersonIndividualProcess) individualCandidacyProcess;

        final Row row = spreadsheet.addRow();
        row.setCell(degreeCandidacyForGraduatedPersonProcess.getProcessCode());
        row.setCell(degreeCandidacyForGraduatedPersonProcess.getPersonalDetails().getName());
        row.setCell(degreeCandidacyForGraduatedPersonProcess.getPersonalDetails().getIdDocumentType().getLocalizedName());
        row.setCell(degreeCandidacyForGraduatedPersonProcess.getPersonalDetails().getDocumentIdNumber());
        row.setCell(degreeCandidacyForGraduatedPersonProcess.getPersonalDetails().getCountry().getCountryNationality()
                .getContent());
        row.setCell(degreeCandidacyForGraduatedPersonProcess.getPrecedentDegreeInformation().getDegreeAndInstitutionName());
        row.setCell(degreeCandidacyForGraduatedPersonProcess.getPrecedentDegreeInformation().getDegreeDesignation());
        row.setCell(degreeCandidacyForGraduatedPersonProcess.getPrecedentDegreeInformation().getConclusionDate()
                .toString(dateFormat));
        row.setCell(degreeCandidacyForGraduatedPersonProcess.getPrecedentDegreeInformation().getConclusionGrade());
        row.setCell(degreeCandidacyForGraduatedPersonProcess.getCandidacy().getSelectedDegree().getName());
        row.setCell(BundleUtil.getString(Bundle.ENUMERATION, individualCandidacyProcess.getCandidacyState().getQualifiedName()));
        row.setCell(BundleUtil.getString(Bundle.CANDIDATE, degreeCandidacyForGraduatedPersonProcess.getProcessChecked() != null
                && degreeCandidacyForGraduatedPersonProcess.getProcessChecked() ? MESSAGE_YES : MESSAGE_NO));
        return spreadsheet;
    }

    @Override
    protected Predicate<IndividualCandidacyProcess> getChildProcessSelectionPredicate(final CandidacyProcess process,
            HttpServletRequest request) {
        final Degree selectedDegree = getChooseDegreeBean(request).getDegree();
        if (selectedDegree == null) {
            return Predicates.alwaysTrue();
        } else {
            return new Predicate<IndividualCandidacyProcess>() {
                @Override
                public boolean apply(IndividualCandidacyProcess process) {
                    return ((DegreeCandidacyForGraduatedPersonIndividualProcess) process).getCandidacy().getSelectedDegree() == selectedDegree;
                }
            };
        }
    }

}
