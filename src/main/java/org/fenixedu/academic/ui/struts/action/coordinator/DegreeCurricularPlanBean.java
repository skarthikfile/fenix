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
package org.fenixedu.academic.ui.struts.action.coordinator;

import java.io.Serializable;

import org.fenixedu.academic.domain.DegreeCurricularPlan;
import org.fenixedu.academic.domain.DegreeCurricularPlanEquivalencePlan;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;

public class DegreeCurricularPlanBean implements Serializable {

    private final DegreeCurricularPlan degreeCurricularPlan;

    private DegreeCurricularPlan sourceDegreeCurricularPlan = null;

    public DegreeCurricularPlanBean(final DegreeCurricularPlan degreeCurricularPlan) {
        this.degreeCurricularPlan = degreeCurricularPlan;
    }

    public DegreeCurricularPlan getDegreeCurricularPlan() {
        return degreeCurricularPlan;
    }

    public DegreeCurricularPlan getSourceDegreeCurricularPlan() {
        return sourceDegreeCurricularPlan;
    }

    public void setSourceDegreeCurricularPlan(final DegreeCurricularPlan sourceDegreeCurricularPlan) {
        this.sourceDegreeCurricularPlan = sourceDegreeCurricularPlan;
    }

    @Atomic(mode = TxMode.WRITE)
    public DegreeCurricularPlanEquivalencePlan createEquivalencePlan(final DegreeCurricularPlan sourceDegreeCurricularPlan) {
        return new DegreeCurricularPlanEquivalencePlan(degreeCurricularPlan, sourceDegreeCurricularPlan);
    }

}
