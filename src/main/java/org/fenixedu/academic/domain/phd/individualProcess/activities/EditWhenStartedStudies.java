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
import org.fenixedu.academic.domain.phd.candidacy.RegistrationFormalizationBean;
import org.fenixedu.bennu.core.domain.User;

public class EditWhenStartedStudies extends PhdIndividualProgramProcessActivity {

    @Override
    protected void activityPreConditions(PhdIndividualProgramProcess process, User userView) {
        if (!process.isAllowedToManageProcess(userView)) {
            throw new PreConditionNotValidException();
        }
    }

    @Override
    protected PhdIndividualProgramProcess executeActivity(PhdIndividualProgramProcess process, User userView, Object object) {

        final RegistrationFormalizationBean bean = (RegistrationFormalizationBean) object;
        Object obj = bean.getWhenStartedStudies();
        String[] args = {};

        if (obj == null) {
            throw new DomainException("error.PhdIndividualProgramProcess.EditWhenStartedStudies.invalid.when.started.studies",
                    args);
        }

        process.setWhenStartedStudies(bean.getWhenStartedStudies());

        if (process.getRegistration() != null) {
            process.getRegistration().editStartDates(bean.getWhenStartedStudies(),
                    process.getCandidacyProcess().getWhenRatified(), bean.getWhenStartedStudies());
        }

        return process;
    }

}