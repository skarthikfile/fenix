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

public class RatifyFinalThesis extends PhdThesisActivity {

    @Override
    protected void activityPreConditions(PhdThesisProcess process, User userView) {

        if (!process.isAllowedToManageProcess(userView)) {
            throw new PreConditionNotValidException();
        }

        if (process.getActiveState() != PhdThesisProcessStateType.WAITING_FOR_THESIS_RATIFICATION) {
            throw new PreConditionNotValidException();
        }

    }

    @Override
    protected PhdThesisProcess executeActivity(PhdThesisProcess process, User userView, Object object) {

        final PhdThesisProcessBean bean = (PhdThesisProcessBean) object;

        checkParameters(bean);
        LocalDate whenFinalThesisRatified = bean.getWhenFinalThesisRatified();
        process.setWhenFinalThesisRatified(whenFinalThesisRatified);

        for (final PhdProgramDocumentUploadBean document : bean.getDocuments()) {
            if (document.hasAnyInformation()) {
                process.addDocument(document, userView.getPerson());
            }
        }

        process.createState(PhdThesisProcessStateType.WAITING_FOR_FINAL_GRADE, userView.getPerson(), bean.getRemarks());

        return process;
    }

    private void checkParameters(final PhdThesisProcessBean bean) {
        if (bean.getWhenFinalThesisRatified() == null) {
            throw new DomainException("error.RatifyFinalThesis.invalid.final.thesis.ratified.date");
        }
    }

}
