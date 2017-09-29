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
package org.fenixedu.academic.domain.phd.individualProcess.activities;

import org.fenixedu.academic.domain.Enrolment;
import org.fenixedu.academic.domain.caseHandling.PreConditionNotValidException;
import org.fenixedu.academic.domain.curriculum.EnrollmentCondition;
import org.fenixedu.academic.domain.phd.ManageEnrolmentsBean;
import org.fenixedu.academic.domain.phd.PhdIndividualProgramProcess;
import org.fenixedu.academic.domain.phd.alert.AlertService;
import org.fenixedu.academic.domain.phd.alert.AlertService.AlertMessage;
import org.fenixedu.bennu.core.domain.User;

public class AcceptEnrolments extends PhdIndividualProgramProcessActivity {

    @Override
    public void activityPreConditions(PhdIndividualProgramProcess process, User userView) {

        if (!process.isCoordinatorForPhdProgram(userView.getPerson())) {
            throw new PreConditionNotValidException();
        }
    }

    @Override
    protected PhdIndividualProgramProcess executeActivity(PhdIndividualProgramProcess process, User userView, Object object) {

        final ManageEnrolmentsBean bean = (ManageEnrolmentsBean) object;

        for (final Enrolment enrolment : bean.getEnrolmentsToValidate()) {
            if (process.getRegistration().hasEnrolments(enrolment)) {
                enrolment.setEnrolmentCondition(EnrollmentCondition.VALIDATED);
            }
        }

        AlertService.alertStudent(process, AlertMessage.create(bean.getMailSubject()).isKey(false),
                AlertMessage.create(buildBody(bean)).isKey(false));

        // TODO: wich group should be used in academic office?
        // AlertService.alertAcademicOffice(process, permissionType,
        // subjectKey, bodyKey)

        return process;
    }

    private String buildBody(ManageEnrolmentsBean bean) {
        final StringBuilder sb = new StringBuilder();
        sb.append(AlertService.getMessageFromResource("label.phd.accepted.enrolments")).append("\n");
        for (final Enrolment enrolment : bean.getEnrolmentsToValidate()) {
            sb.append("- ").append(enrolment.getPresentationName()).append(enrolment.getExecutionPeriod().getQualifiedName())
                    .append("\n");
        }
        return sb.toString();
    }

}