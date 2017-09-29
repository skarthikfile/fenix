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
package org.fenixedu.academic.domain.accounting;

import org.fenixedu.academic.domain.accounting.events.AdministrativeOfficeFeeAndInsuranceExemptionJustification;
import org.fenixedu.academic.domain.accounting.events.AdministrativeOfficeFeeAndInsuranceExemptionJustificationByDispatch;
import org.fenixedu.academic.domain.accounting.events.AdministrativeOfficeFeeAndInsuranceExemptionJustificationType;
import org.joda.time.YearMonthDay;

public class AdministrativeOfficeFeeAndInsuranceExemptionJustificationFactory {

    public static AdministrativeOfficeFeeAndInsuranceExemptionJustification create(
            final Exemption administrativeOfficeFeeAndInsuranceExemption,
            final AdministrativeOfficeFeeAndInsuranceExemptionJustificationType justificationType, final String reason,
            final YearMonthDay dispatchDate) {

        switch (justificationType) {
        case MIT_AGREEMENT:
            return new AdministrativeOfficeFeeAndInsuranceExemptionJustification(administrativeOfficeFeeAndInsuranceExemption,
                    justificationType, reason);
        case DIRECTIVE_COUNCIL_AUTHORIZATION:
            return new AdministrativeOfficeFeeAndInsuranceExemptionJustificationByDispatch(
                    administrativeOfficeFeeAndInsuranceExemption, justificationType, reason, dispatchDate);
        default:
            throw new RuntimeException("Unknown justification type");

        }

    }
}
