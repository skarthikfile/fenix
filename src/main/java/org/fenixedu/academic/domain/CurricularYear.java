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
package org.fenixedu.academic.domain;

import java.util.Collection;
import java.util.Comparator;

import org.fenixedu.bennu.core.domain.Bennu;

/**
 * @author dcs-rjao
 * 
 *         21/Mar/2003
 */

public class CurricularYear extends CurricularYear_Base implements Comparable<CurricularYear> {

    public static Comparator<CurricularYear> CURRICULAR_YEAR_COMPARATORY_BY_YEAR = new Comparator<CurricularYear>() {

        @Override
        public int compare(CurricularYear o1, CurricularYear o2) {
            return o1.getYear().compareTo(o2.getYear());
        }

    };

    public CurricularYear() {
        super();
        setRootDomainObject(Bennu.getInstance());
    }

    public CurricularYear(final Integer year, final int numberOfSemesters) {
        this();
        setYear(year);

        for (int i = 1; i <= numberOfSemesters; i++) {
            new CurricularSemester(this, Integer.valueOf(i));
        }
    }

    @Override
    public int compareTo(final CurricularYear curricularYear) {
        return getYear().compareTo(curricularYear.getYear());
    }

    public CurricularSemester getCurricularSemester(final Integer semester) {
        for (final CurricularSemester curricularSemester : getCurricularSemestersSet()) {
            if (curricularSemester.getSemester().equals(semester)) {
                return curricularSemester;
            }
        }

        return null;
    }

    public static CurricularYear readByYear(Integer year) {
        Collection<CurricularYear> curricularYears = Bennu.getInstance().getCurricularYearsSet();
        for (CurricularYear curricularYear : curricularYears) {
            if (curricularYear.getYear().equals(year)) {
                return curricularYear;
            }
        }
        return null;
    }

}
