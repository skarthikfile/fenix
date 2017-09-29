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
package org.fenixedu.academic.dto.accounting.penaltyExemption;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.fenixedu.academic.domain.accounting.Installment;
import org.fenixedu.academic.domain.accounting.events.gratuity.GratuityEventWithPaymentPlan;

public class CreateInstallmentPenaltyExemptionBean extends CreatePenaltyExemptionBean implements Serializable {

    private List<Installment> installments;

    public CreateInstallmentPenaltyExemptionBean(GratuityEventWithPaymentPlan gratuityEventWithPaymentPlan) {
        super(gratuityEventWithPaymentPlan);
        setGratuityEventWithPaymentPlan(gratuityEventWithPaymentPlan);
        setInstallments(new ArrayList<Installment>());
    }

    public GratuityEventWithPaymentPlan getGratuityEventWithPaymentPlan() {
        return (GratuityEventWithPaymentPlan) getEvent();
    }

    private void setGratuityEventWithPaymentPlan(GratuityEventWithPaymentPlan gratuityEventWithPaymentPlan) {
        setEvent(gratuityEventWithPaymentPlan);
    }

    public List<Installment> getInstallments() {
        final List<Installment> result = new ArrayList<Installment>();
        for (final Installment installment : installments) {
            result.add(installment);
        }

        return result;
    }

    public void setInstallments(List<Installment> installments) {
        final List<Installment> result = new ArrayList<Installment>();
        for (final Installment installment : installments) {
            result.add(installment);
        }

        this.installments = result;
    }

}
