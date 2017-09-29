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

import org.fenixedu.academic.domain.caseHandling.PreConditionNotValidException;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.phd.PhdIndividualProgramProcess;
import org.fenixedu.academic.domain.phd.PhdIndividualProgramProcessBean;
import org.fenixedu.academic.domain.phd.PhdIndividualProgramProcessState;
import org.fenixedu.academic.domain.phd.PhdProgramProcessState;
import org.fenixedu.bennu.core.domain.User;

public class ActivatePhdProgramProcessInCandidacyState extends PhdIndividualProgramProcessActivity {

    @Override
    protected void processPreConditions(PhdIndividualProgramProcess process, User userView) {
        // remove restrictions
    }

    @Override
    public void activityPreConditions(PhdIndividualProgramProcess process, User userView) {
        if (!process.isAllowedToManageProcessState(userView)) {
            throw new PreConditionNotValidException();
        }

    }

    @Override
    protected PhdIndividualProgramProcess executeActivity(PhdIndividualProgramProcess process, User userView, Object object) {
        final PhdIndividualProgramProcessBean bean = (PhdIndividualProgramProcessBean) object;

        /*
         * 1 - Check if there's no registration 2 - Check if last active state
         * was candidacy
         */
        if (process.getRegistration() != null) {
            throw new DomainException("error.PhdIndividualProgramProcess.set.candidacy.state.has.registration");
        }

        if (!process.getLastActiveState().getType().equals(PhdIndividualProgramProcessState.CANDIDACY)) {
            throw new DomainException(
                    "error.PhdIndividualProgramProcess.set.candidacy.state.previous.active.state.is.not.candidacy");
        }

        PhdProgramProcessState.createWithGivenStateDate(process, process.getLastActiveState().getType(), userView.getPerson(),
                "", bean.getStateDate().toDateTimeAtStartOfDay());

        return process;
    }
}