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
/*
 * Created on 2004/08/24
 *
 */
package org.fenixedu.academic.domain;

import java.util.Date;

/**
 * @author Luis Cruz
 */
public class EnrolmentPeriodInClasses extends EnrolmentPeriodInClasses_Base {

    public EnrolmentPeriodInClasses(final DegreeCurricularPlan degreeCurricularPlan, final ExecutionSemester executionSemester,
            final Date startDate, final Date endDate) {
        super();
        init(degreeCurricularPlan, executionSemester, startDate, endDate);
    }

    @Override
    public boolean isForClasses() {
        return true;
    }
}