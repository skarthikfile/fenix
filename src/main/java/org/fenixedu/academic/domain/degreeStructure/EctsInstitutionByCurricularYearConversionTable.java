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
package org.fenixedu.academic.domain.degreeStructure;

import org.fenixedu.academic.domain.CurricularYear;
import org.fenixedu.academic.domain.organizationalStructure.Unit;
import org.fenixedu.academic.domain.time.calendarStructure.AcademicInterval;
import org.fenixedu.bennu.core.domain.Bennu;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.DomainObject;

public class EctsInstitutionByCurricularYearConversionTable extends EctsInstitutionByCurricularYearConversionTable_Base {

    protected EctsInstitutionByCurricularYearConversionTable(Unit school, AcademicInterval year, CycleType cycle,
            CurricularYear curricularYear, EctsComparabilityTable table) {
        super();
        init(year, curricularYear, table);
        setSchool(school);
        setCycle(cycle);
    }

    protected Bennu getRootDomainObject() {
        return getSchool().getRootDomainObject();
    }

    @Override
    public DomainObject getTargetEntity() {
        return getSchool();
    }

    @Atomic
    public static void createConversionTable(Unit ist, AcademicInterval year, CycleType cycleType, CurricularYear curricularYear,
            String[] table) {
        EctsInstitutionByCurricularYearConversionTable conversion =
                EctsTableIndex.readOrCreateByYear(year).getEnrolmentTableBy(ist, curricularYear, cycleType);
        EctsComparabilityTable ectsTable = EctsComparabilityTable.fromStringArray(table);
        if (conversion != null) {
            conversion.delete();
        }
        if (ectsTable != null) {
            new EctsInstitutionByCurricularYearConversionTable(ist, year, cycleType, curricularYear, ectsTable);
        }
    }

    @Override
    public void delete() {
        setSchool(null);
        super.delete();
    }

}
