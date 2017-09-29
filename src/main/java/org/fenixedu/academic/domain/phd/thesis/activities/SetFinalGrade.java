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
package org.fenixedu.academic.domain.phd.thesis.activities;

import org.fenixedu.academic.domain.caseHandling.PreConditionNotValidException;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.phd.PhdProgramDocumentUploadBean;
import org.fenixedu.academic.domain.phd.thesis.PhdThesisProcess;
import org.fenixedu.academic.domain.phd.thesis.PhdThesisProcessBean;
import org.fenixedu.academic.domain.phd.thesis.PhdThesisProcessStateType;
import org.fenixedu.bennu.core.domain.User;
import org.joda.time.LocalDate;

public class SetFinalGrade extends PhdThesisActivity {

    @Override
    protected void activityPreConditions(PhdThesisProcess process, User userView) {

        if (!process.isJuryValidated()) {
            throw new PreConditionNotValidException();
        }

        if (process.getActiveState() != PhdThesisProcessStateType.WAITING_FOR_FINAL_GRADE) {
            throw new PreConditionNotValidException();
        }

        if (!process.isAllowedToManageProcess(userView)) {
            throw new PreConditionNotValidException();
        }
    }

    @Override
    protected PhdThesisProcess executeActivity(PhdThesisProcess process, User userView, Object object) {
        final PhdThesisProcessBean bean = (PhdThesisProcessBean) object;

        for (final PhdProgramDocumentUploadBean each : bean.getDocuments()) {
            if (each.hasAnyInformation()) {
                process.addDocument(each, userView.getPerson());
            }
        }

        checkParameters(bean);

        LocalDate conclusionDate = bean.getConclusionDate();
        process.setConclusionDate(conclusionDate);
        process.setFinalGrade(bean.getFinalGrade());

        if (!process.hasState(PhdThesisProcessStateType.CONCLUDED)) {
            process.createState(PhdThesisProcessStateType.CONCLUDED, userView.getPerson(), bean.getRemarks());
        }

        return process;

    }

    private void checkParameters(final PhdThesisProcessBean bean) {

        if (bean.getFinalGrade() == null) {
            throw new DomainException("error.SetFinalGrade.invalid.grade");
        }

        if (bean.getConclusionDate() == null) {
            throw new DomainException("error.SetFinalGrade.invalid.conclusion.date");
        }
    }

}
