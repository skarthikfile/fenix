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

import org.fenixedu.academic.domain.StudentCurricularPlan;

/**
 * 
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class InfoStudentCurricularPlanWithFirstTimeEnrolment extends InfoStudentCurricularPlan {

    private Boolean firstTimeEnrolment;

    public InfoStudentCurricularPlanWithFirstTimeEnrolment(StudentCurricularPlan studentCurricularPlan) {
        super(studentCurricularPlan);
    }

    public void copyFromDomain(StudentCurricularPlan studentCurricularPlan) {
        super.copyFromDomain(studentCurricularPlan);
    }

    public static InfoStudentCurricularPlanWithFirstTimeEnrolment newInfoFromDomain(StudentCurricularPlan studentCurricularPlan) {
        return studentCurricularPlan == null ? null : new InfoStudentCurricularPlanWithFirstTimeEnrolment(studentCurricularPlan);
    }

    public Boolean getFirstTimeEnrolment() {
        return firstTimeEnrolment;
    }

    public void setFirstTimeEnrolment(Boolean firstTimeEnrolment) {
        this.firstTimeEnrolment = firstTimeEnrolment;
    }
}