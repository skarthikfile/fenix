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
package org.fenixedu.academic.domain.accessControl;

import org.fenixedu.academic.domain.DegreeCurricularPlan;
import org.fenixedu.academic.domain.ExecutionSemester;
import org.fenixedu.bennu.core.groups.Group;

public class PersistentTeachersWithGradesToSubmitGroup extends PersistentTeachersWithGradesToSubmitGroup_Base {
    protected PersistentTeachersWithGradesToSubmitGroup(ExecutionSemester period, DegreeCurricularPlan degreeCurricularPlan) {
        super();
        init(period, degreeCurricularPlan);
    }

    @Override
    public Group toGroup() {
        return TeachersWithGradesToSubmitGroup.get(getPeriod(), getDegreeCurricularPlan());
    }

    public static PersistentTeachersWithGradesToSubmitGroup getInstance(final ExecutionSemester period,
            final DegreeCurricularPlan degreeCurricularPlan) {
        return singleton(PersistentTeachersWithGradesToSubmitGroup.class, period, degreeCurricularPlan,
                () -> new PersistentTeachersWithGradesToSubmitGroup(period, degreeCurricularPlan));
    }
}
