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
package org.fenixedu.academic.service.services.accounting.gratuity;

import static org.fenixedu.academic.predicate.AccessControl.check;

import java.math.BigDecimal;

import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.accounting.events.gratuity.PercentageGratuityExemption;
import org.fenixedu.academic.domain.accounting.events.gratuity.ValueGratuityExemption;
import org.fenixedu.academic.dto.accounting.gratuityExemption.CreateGratuityExemptionBean;
import org.fenixedu.academic.predicate.AcademicPredicates;

import pt.ist.fenixframework.Atomic;

public class CreateGratuityExemption {

    @Atomic
    public static void run(final Person responsible, final CreateGratuityExemptionBean createGratuityExemptionBean) {
        check(AcademicPredicates.MANAGE_STUDENT_PAYMENTS);
        if (createGratuityExemptionBean.isPercentageExemption()) {
            new PercentageGratuityExemption(responsible, createGratuityExemptionBean.getGratuityEvent(),
                    createGratuityExemptionBean.getExemptionJustificationType(), createGratuityExemptionBean.getReason(),
                    createGratuityExemptionBean.getDispatchDate(), createGratuityExemptionBean.getSelectedPercentage().divide(
                            BigDecimal.valueOf(100)));
        } else {
            new ValueGratuityExemption(responsible, createGratuityExemptionBean.getGratuityEvent(),
                    createGratuityExemptionBean.getExemptionJustificationType(), createGratuityExemptionBean.getReason(),
                    createGratuityExemptionBean.getDispatchDate(), createGratuityExemptionBean.getAmount());
        }
    }

}