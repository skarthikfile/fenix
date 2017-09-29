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
package org.fenixedu.academic.dto;

import org.fenixedu.academic.domain.EnrolmentEvaluation;

/**
 * @author Fernanda Quitério Created on 13/Jul/2004
 * 
 */
public class InfoEnrolmentEvaluationWithResponsibleForGrade extends InfoEnrolmentEvaluation {

    @Override
    public void copyFromDomain(EnrolmentEvaluation enrolmentEvaluation) {
        super.copyFromDomain(enrolmentEvaluation);
        if (enrolmentEvaluation != null) {
            setInfoPersonResponsibleForGrade(InfoPerson.newInfoFromDomain(enrolmentEvaluation.getPersonResponsibleForGrade()));
            if (enrolmentEvaluation.getPerson() != null) {
                setInfoPerson(InfoPerson.newInfoFromDomain(enrolmentEvaluation.getPerson()));
            }
        }
    }

    public static InfoEnrolmentEvaluation newInfoFromDomain(EnrolmentEvaluation enrolmentEvaluation) {
        InfoEnrolmentEvaluationWithResponsibleForGrade infoEnrolmentEvaluationWithInfoPerson = null;
        if (enrolmentEvaluation != null) {
            infoEnrolmentEvaluationWithInfoPerson = new InfoEnrolmentEvaluationWithResponsibleForGrade();
            infoEnrolmentEvaluationWithInfoPerson.copyFromDomain(enrolmentEvaluation);
        }
        return infoEnrolmentEvaluationWithInfoPerson;
    }
}