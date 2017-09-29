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
package org.fenixedu.academic.domain.phd.thesis.meeting.activities;

import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.caseHandling.Activity;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.phd.InternalPhdParticipant;
import org.fenixedu.academic.domain.phd.PhdIndividualProgramProcess;
import org.fenixedu.academic.domain.phd.PhdParticipant;
import org.fenixedu.academic.domain.phd.alert.AlertService.AlertMessage;
import org.fenixedu.academic.domain.phd.log.PhdLog;
import org.fenixedu.academic.domain.phd.thesis.PhdThesisProcess;
import org.fenixedu.academic.domain.phd.thesis.meeting.PhdMeetingSchedulingProcess;
import org.fenixedu.academic.util.phd.PhdProperties;
import org.fenixedu.bennu.core.domain.User;

abstract public class PhdMeetingSchedulingActivity extends Activity<PhdMeetingSchedulingProcess> {

    @Override
    final public void checkPreConditions(final PhdMeetingSchedulingProcess process, final User userView) {
        processPreConditions(process, userView);
        activityPreConditions(process, userView);
    }

    protected void processPreConditions(final PhdMeetingSchedulingProcess process, final User userView) {
    }

    abstract protected void activityPreConditions(final PhdMeetingSchedulingProcess process, final User userView);

    public static String getAccessInformation(PhdIndividualProgramProcess process, PhdParticipant participant,
            String coordinatorMessage, String teacherMessage) {

        if (!participant.isInternal()) {
            return AlertMessage.get("message.phd.external.access", PhdProperties.getPhdExternalAccessLink(),
                    participant.getAccessHashCode(), participant.getPassword());

        } else {
            final Person person = ((InternalPhdParticipant) participant).getPerson();

            if (process.isCoordinatorForPhdProgram(person)) {
                return AlertMessage.get(coordinatorMessage);

            } else if (process.isGuiderOrAssistentGuider(person) || person.getTeacher() != null) {
                return AlertMessage.get(teacherMessage);
            }
        }

        throw new DomainException("error.PhdThesisProcess.unexpected.participant.type");
    }

    @Override
    protected void log(PhdMeetingSchedulingProcess process, User userView, Object object) {
        PhdThesisProcess thesisProcess = process.getThesisProcess();

        PhdLog.logActivity(this, thesisProcess, userView, object);
    }
}