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

import org.fenixedu.academic.domain.caseHandling.PreConditionNotValidException;
import org.fenixedu.academic.domain.phd.alert.AlertService;
import org.fenixedu.academic.domain.phd.alert.AlertService.AlertMessage;
import org.fenixedu.academic.domain.phd.thesis.PhdThesisProcess;
import org.fenixedu.academic.domain.phd.thesis.PhdThesisProcessBean;
import org.fenixedu.academic.domain.phd.thesis.PhdThesisProcessStateType;
import org.fenixedu.academic.domain.phd.thesis.meeting.PhdMeetingSchedulingProcess;
import org.fenixedu.academic.domain.phd.thesis.meeting.PhdMeetingSchedulingProcessStateType;
import org.fenixedu.bennu.core.domain.User;

public class ScheduleFirstThesisMeetingRequest extends PhdMeetingSchedulingActivity {

    @Override
    protected void activityPreConditions(PhdMeetingSchedulingProcess process, User userView) {

        if (!process.getThesisProcess().getActiveState().equals(PhdThesisProcessStateType.WAITING_FOR_JURY_REPORTER_FEEDBACK)) {
            throw new PreConditionNotValidException();
        }

        if (!process.getActiveState().equals(PhdMeetingSchedulingProcessStateType.WAITING_FIRST_THESIS_MEETING_REQUEST)) {
            throw new PreConditionNotValidException();
        }

        if (!process.isAllowedToManageProcess(userView)) {
            throw new PreConditionNotValidException();
        }
    }

    @Override
    protected PhdMeetingSchedulingProcess executeActivity(PhdMeetingSchedulingProcess process, User userView, Object object) {

        final PhdThesisProcessBean bean = (PhdThesisProcessBean) object;
        final PhdThesisProcess thesisProcess = process.getThesisProcess();

        if (bean.isToNotify()) {

            // Alert president jury element
            AlertService.alertParticipants(thesisProcess.getIndividualProgramProcess(), AlertMessage
                    .create("message.phd.request.schedule.meeting.president.notification.subject"), AlertMessage
                    .create("message.phd.request.schedule.meeting.president.notification.body"), thesisProcess
                    .getPresidentJuryElement().getParticipant());

            AlertService.alertResponsibleCoordinators(thesisProcess.getIndividualProgramProcess(), AlertMessage
                    .create("message.phd.request.schedule.meeting.coordinator.notification.subject"), AlertMessage.create(
                    "message.phd.request.schedule.meeting.coordinator.notification.body", thesisProcess.getPresidentJuryElement()
                            .getNameWithTitle()));

        }

        thesisProcess.createState(PhdThesisProcessStateType.WAITING_FOR_THESIS_MEETING_SCHEDULING, userView.getPerson(),
                bean.getRemarks());
        process.createState(PhdMeetingSchedulingProcessStateType.WAITING_FIRST_THESIS_MEETING_SCHEDULE, userView.getPerson(),
                bean.getRemarks());

        return process;
    }

}
