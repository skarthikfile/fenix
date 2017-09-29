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
import org.fenixedu.academic.domain.phd.PhdProgramDocumentUploadBean;
import org.fenixedu.academic.domain.phd.thesis.PhdThesisProcessStateType;
import org.fenixedu.academic.domain.phd.thesis.meeting.PhdMeeting;
import org.fenixedu.academic.domain.phd.thesis.meeting.PhdMeetingBean;
import org.fenixedu.academic.domain.phd.thesis.meeting.PhdMeetingSchedulingProcess;
import org.fenixedu.academic.domain.phd.thesis.meeting.PhdMeetingSchedulingProcessStateType;
import org.fenixedu.bennu.core.domain.User;

public class SubmitThesisMeetingMinutes extends PhdMeetingSchedulingActivity {

    @Override
    protected void activityPreConditions(PhdMeetingSchedulingProcess process, User userView) {

        if (!process.isAllowedToManageProcess(userView)) {
            throw new PreConditionNotValidException();
        }

        if (!process.hasState(PhdMeetingSchedulingProcessStateType.WITHOUT_THESIS_MEETING_REQUEST)) {
            throw new PreConditionNotValidException();
        }

        if (!process.getThesisProcess().hasState(PhdThesisProcessStateType.WAITING_FOR_THESIS_DISCUSSION_DATE_SCHEDULING)) {
            throw new PreConditionNotValidException();
        }

    }

    @Override
    protected PhdMeetingSchedulingProcess executeActivity(PhdMeetingSchedulingProcess process, User userView, Object object) {
        final PhdMeetingBean bean = (PhdMeetingBean) object;
        final PhdMeeting meeting = bean.getMeeting();

        final PhdProgramDocumentUploadBean document = bean.getDocument();
        if (document.hasAnyInformation()) {
            meeting.addDocument(document, userView.getPerson());
        }

        if (bean.isToNotify()) {
            /*
             * TODO: (check subject and body) AlertService.alertStudent(process,
             * subject, body);
             */
        }

        return process;
    }

}
