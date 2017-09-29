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
package org.fenixedu.academic.domain.time.chronologies.durationFields;

import org.fenixedu.academic.domain.time.chronologies.AcademicChronology;
import org.joda.time.Chronology;
import org.joda.time.DurationField;
import org.joda.time.DurationFieldType;

public class AcademicYearsDurationFieldType extends DurationFieldType {

    public static final AcademicYearsDurationFieldType ACADEMIC_YEARS_TYPE;
    static {
        ACADEMIC_YEARS_TYPE = new AcademicYearsDurationFieldType("academicYears");
    }

    protected AcademicYearsDurationFieldType(String name) {
        super(name);
    }

    public static DurationFieldType academicYears() {
        return ACADEMIC_YEARS_TYPE;
    }

    @Override
    public DurationField getField(Chronology chronology) {
        if (chronology instanceof AcademicChronology) {
            return ((AcademicChronology) chronology).academicYears();
        }
        return null;
    }
}
