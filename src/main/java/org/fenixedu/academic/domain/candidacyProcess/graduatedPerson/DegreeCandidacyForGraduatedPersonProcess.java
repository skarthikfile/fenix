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
package org.fenixedu.academic.domain.candidacyProcess.graduatedPerson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.fenixedu.academic.domain.AcademicProgram;
import org.fenixedu.academic.domain.Degree;
import org.fenixedu.academic.domain.ExecutionInterval;
import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.accessControl.academicAdministration.AcademicAccessRule;
import org.fenixedu.academic.domain.accessControl.academicAdministration.AcademicOperationType;
import org.fenixedu.academic.domain.candidacyProcess.CandidacyProcess;
import org.fenixedu.academic.domain.candidacyProcess.CandidacyProcessBean;
import org.fenixedu.academic.domain.candidacyProcess.CandidacyProcessSelectDegreesBean;
import org.fenixedu.academic.domain.candidacyProcess.CandidacyProcessState;
import org.fenixedu.academic.domain.candidacyProcess.IndividualCandidacyProcess;
import org.fenixedu.academic.domain.caseHandling.Activity;
import org.fenixedu.academic.domain.caseHandling.PreConditionNotValidException;
import org.fenixedu.academic.domain.caseHandling.StartActivity;
import org.fenixedu.academic.domain.degree.DegreeType;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.period.CandidacyProcessCandidacyPeriod;
import org.fenixedu.academic.domain.period.DegreeCandidacyForGraduatedPersonCandidacyPeriod;
import org.fenixedu.academic.domain.person.RoleType;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.security.Authenticate;
import org.joda.time.DateTime;

import pt.ist.fenixframework.dml.runtime.RelationAdapter;

public class DegreeCandidacyForGraduatedPersonProcess extends DegreeCandidacyForGraduatedPersonProcess_Base {

    static {
        getRelationCandidacyPeriodCandidacyProcess().addListener(
                new RelationAdapter<CandidacyProcess, CandidacyProcessCandidacyPeriod>() {
                    @Override
                    public void beforeAdd(CandidacyProcess candidacyProcess, CandidacyProcessCandidacyPeriod candidacyPeriod) {
                        super.beforeAdd(candidacyProcess, candidacyPeriod);

                        if (candidacyProcess != null && candidacyPeriod != null
                                && candidacyPeriod instanceof DegreeCandidacyForGraduatedPersonCandidacyPeriod) {
                            if (!candidacyPeriod.getCandidacyProcessesSet().isEmpty()) {
                                throw new DomainException(
                                        "error.DegreeCandidacyForGraduatedPersonProcess.candidacy.period.already.has.process");
                            }
                        }
                    }
                });
    }

    static private List<Activity> activities = new ArrayList<Activity>();
    static {
        activities.add(new EditCandidacyPeriod());
        activities.add(new SendToCoordinator());
        activities.add(new SendToScientificCouncil());
        activities.add(new PrintCandidacies());
        activities.add(new ExportCandidacies());
        activities.add(new SelectAvailableDegrees());
    }

    private DegreeCandidacyForGraduatedPersonProcess() {
        super();
    }

    private DegreeCandidacyForGraduatedPersonProcess(final ExecutionYear executionYear, final DateTime start, final DateTime end) {
        this();
        checkParameters(executionYear, start, end);
        setState(CandidacyProcessState.STAND_BY);
        new DegreeCandidacyForGraduatedPersonCandidacyPeriod(this, executionYear, start, end);
    }

    private void checkParameters(final ExecutionInterval executionInterval, final DateTime start, final DateTime end) {
        if (executionInterval == null) {
            throw new DomainException("error.DegreeCandidacyForGraduatedPersonProcess.invalid.executionInterval");
        }

        if (start == null || end == null || start.isAfter(end)) {
            throw new DomainException("error.DegreeCandidacyForGraduatedPersonProcess.invalid.interval");
        }
    }

    private void edit(final DateTime start, final DateTime end) {
        checkParameters(getCandidacyPeriod().getExecutionInterval(), start, end);
        getCandidacyPeriod().edit(start, end);
    }

    @Override
    public boolean canExecuteActivity(User userView) {
        return isAllowedToManageProcess(userView) || RoleType.SCIENTIFIC_COUNCIL.isMember(userView.getPerson().getUser());
    }

    @Override
    public List<Activity> getActivities() {
        return activities;
    }

    public Map<Degree, SortedSet<DegreeCandidacyForGraduatedPersonIndividualProcess>> getValidDegreeCandidaciesForGraduatedPersonsByDegree() {
        final Map<Degree, SortedSet<DegreeCandidacyForGraduatedPersonIndividualProcess>> result =
                new TreeMap<Degree, SortedSet<DegreeCandidacyForGraduatedPersonIndividualProcess>>(
                        Degree.COMPARATOR_BY_NAME_AND_ID);
        for (final IndividualCandidacyProcess child : getChildProcessesSet()) {
            final DegreeCandidacyForGraduatedPersonIndividualProcess process =
                    (DegreeCandidacyForGraduatedPersonIndividualProcess) child;
            if (process.isCandidacyValid() && process.canExecuteActivity(Authenticate.getUser())) {
                addCandidacy(result, process);
            }
        }
        return result;
    }

    private void addCandidacy(final Map<Degree, SortedSet<DegreeCandidacyForGraduatedPersonIndividualProcess>> result,
            final DegreeCandidacyForGraduatedPersonIndividualProcess process) {
        SortedSet<DegreeCandidacyForGraduatedPersonIndividualProcess> values = result.get(process.getCandidacySelectedDegree());
        if (values == null) {
            result.put(process.getCandidacySelectedDegree(), values =
                    new TreeSet<DegreeCandidacyForGraduatedPersonIndividualProcess>(
                            DegreeCandidacyForGraduatedPersonIndividualProcess.COMPARATOR_BY_CANDIDACY_PERSON));
        }
        values.add(process);
    }

    public List<DegreeCandidacyForGraduatedPersonIndividualProcess> getAcceptedDegreeCandidacyForGraduatedPersonIndividualCandidacies() {
        final List<DegreeCandidacyForGraduatedPersonIndividualProcess> result =
                new ArrayList<DegreeCandidacyForGraduatedPersonIndividualProcess>();
        for (final IndividualCandidacyProcess child : getChildProcessesSet()) {
            if (child.isCandidacyValid() && child.isCandidacyAccepted()) {
                result.add((DegreeCandidacyForGraduatedPersonIndividualProcess) child);
            }
        }
        return result;
    }

    public List<DegreeCandidacyForGraduatedPersonIndividualProcess> getValidDegreeCandidaciesForGraduatedPersons(
            final Degree degree) {

        if (degree == null) {
            return Collections.emptyList();
        }

        final List<DegreeCandidacyForGraduatedPersonIndividualProcess> result =
                new ArrayList<DegreeCandidacyForGraduatedPersonIndividualProcess>();
        for (final IndividualCandidacyProcess child : getChildProcessesSet()) {
            final DegreeCandidacyForGraduatedPersonIndividualProcess process =
                    (DegreeCandidacyForGraduatedPersonIndividualProcess) child;
            if (process.isCandidacyValid() && process.hasCandidacyForSelectedDegree(degree)) {
                result.add(process);
            }
        }
        return result;
    }

    // static methods

    private static final Predicate<DegreeType> ALLOWED_DEGREE_TYPES = DegreeType.oneOf(DegreeType::isBolonhaDegree,
            DegreeType::isIntegratedMasterDegree);

    static private boolean isAllowedToManageProcess(User userView) {
        for (AcademicProgram program : AcademicAccessRule.getProgramsAccessibleToFunction(
                AcademicOperationType.MANAGE_CANDIDACY_PROCESSES, userView.getPerson().getUser()).collect(Collectors.toSet())) {
            if (ALLOWED_DEGREE_TYPES.test(program.getDegreeType())) {
                return true;
            }
        }
        return false;
    }

    @StartActivity
    static public class CreateCandidacyPeriod extends Activity<DegreeCandidacyForGraduatedPersonProcess> {

        @Override
        public void checkPreConditions(DegreeCandidacyForGraduatedPersonProcess process, User userView) {
            if (!isAllowedToManageProcess(userView)) {
                throw new PreConditionNotValidException();
            }
        }

        @Override
        protected DegreeCandidacyForGraduatedPersonProcess executeActivity(DegreeCandidacyForGraduatedPersonProcess process,
                User userView, Object object) {
            final CandidacyProcessBean bean = (CandidacyProcessBean) object;
            return new DegreeCandidacyForGraduatedPersonProcess((ExecutionYear) bean.getExecutionInterval(), bean.getStart(),
                    bean.getEnd());
        }
    }

    static private class EditCandidacyPeriod extends Activity<DegreeCandidacyForGraduatedPersonProcess> {

        @Override
        public void checkPreConditions(DegreeCandidacyForGraduatedPersonProcess process, User userView) {
            if (!isAllowedToManageProcess(userView)) {
                throw new PreConditionNotValidException();
            }
        }

        @Override
        protected DegreeCandidacyForGraduatedPersonProcess executeActivity(DegreeCandidacyForGraduatedPersonProcess process,
                User userView, Object object) {
            final CandidacyProcessBean bean = (CandidacyProcessBean) object;
            process.edit(bean.getStart(), bean.getEnd());
            return process;
        }
    }

    static private class SendToCoordinator extends Activity<DegreeCandidacyForGraduatedPersonProcess> {

        @Override
        public void checkPreConditions(DegreeCandidacyForGraduatedPersonProcess process, User userView) {
            if (!isAllowedToManageProcess(userView)) {
                throw new PreConditionNotValidException();
            }

            if (!process.isInStandBy() && !process.isSentToScientificCouncil()) {
                throw new PreConditionNotValidException();
            }

            if (process.getCandidacyPeriod() == null || !process.hasStarted() || process.hasOpenCandidacyPeriod()) {
                throw new PreConditionNotValidException();
            }
        }

        @Override
        protected DegreeCandidacyForGraduatedPersonProcess executeActivity(DegreeCandidacyForGraduatedPersonProcess process,
                User userView, Object object) {
            process.setState(CandidacyProcessState.SENT_TO_COORDINATOR);
            return process;
        }
    }

    static private class SendToScientificCouncil extends Activity<DegreeCandidacyForGraduatedPersonProcess> {

        @Override
        public void checkPreConditions(DegreeCandidacyForGraduatedPersonProcess process, User userView) {
            if (!isAllowedToManageProcess(userView)) {
                throw new PreConditionNotValidException();
            }

            if (!process.isInStandBy() && !process.isSentToCoordinator()) {
                throw new PreConditionNotValidException();
            }

            if (process.getCandidacyPeriod() == null || !process.hasStarted() || process.hasOpenCandidacyPeriod()) {
                throw new PreConditionNotValidException();
            }
        }

        @Override
        protected DegreeCandidacyForGraduatedPersonProcess executeActivity(DegreeCandidacyForGraduatedPersonProcess process,
                User userView, Object object) {
            process.setState(CandidacyProcessState.SENT_TO_SCIENTIFIC_COUNCIL);
            return process;
        }
    }

    static private class PrintCandidacies extends Activity<DegreeCandidacyForGraduatedPersonProcess> {

        @Override
        public void checkPreConditions(DegreeCandidacyForGraduatedPersonProcess process, User userView) {
            if (!isAllowedToManageProcess(userView)) {
                throw new PreConditionNotValidException();
            }
            if (process.isInStandBy()) {
                throw new PreConditionNotValidException();
            }
        }

        @Override
        protected DegreeCandidacyForGraduatedPersonProcess executeActivity(DegreeCandidacyForGraduatedPersonProcess process,
                User userView, Object object) {
            return process; // for now, nothing to be done
        }
    }

    static private class ExportCandidacies extends Activity<DegreeCandidacyForGraduatedPersonProcess> {

        @Override
        public void checkPreConditions(DegreeCandidacyForGraduatedPersonProcess process, User userView) {
            if (!isAllowedToManageProcess(userView)) {
                throw new PreConditionNotValidException();
            }
        }

        @Override
        protected DegreeCandidacyForGraduatedPersonProcess executeActivity(DegreeCandidacyForGraduatedPersonProcess process,
                User userView, Object object) {
            return process; // for now, nothing to be done
        }
    }

    static private class SelectAvailableDegrees extends Activity<DegreeCandidacyForGraduatedPersonProcess> {

        @Override
        public void checkPreConditions(DegreeCandidacyForGraduatedPersonProcess process, User userView) {
            if (!isAllowedToManageProcess(userView)) {
                throw new PreConditionNotValidException();
            }
        }

        @Override
        protected DegreeCandidacyForGraduatedPersonProcess executeActivity(DegreeCandidacyForGraduatedPersonProcess process,
                User userView, Object object) {
            final CandidacyProcessSelectDegreesBean bean = (CandidacyProcessSelectDegreesBean) object;
            final List<Degree> degrees = bean.getDegrees();
            process.getDegreeSet().addAll(degrees);
            process.getDegreeSet().retainAll(degrees);
            return process;
        }

        @Override
        public Boolean isVisibleForCoordinator() {
            return Boolean.FALSE;
        }

        @Override
        public Boolean isVisibleForGriOffice() {
            return Boolean.FALSE;
        }

    }

    public List<Degree> getAvailableDegrees() {
        final Set<Degree> degrees = getDegreeSet();
        return degrees.isEmpty() ? Degree.readAllMatching(DegreeType.oneOf(DegreeType::isBolonhaDegree,
                DegreeType::isIntegratedMasterDegree)) : new ArrayList<Degree>(degrees);
    }

}
