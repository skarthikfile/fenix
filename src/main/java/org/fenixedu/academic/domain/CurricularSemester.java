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

import org.fenixedu.bennu.core.domain.Bennu;

/**
 * @author dcs-rjao
 * 
 *         19/Mar/2003
 */

public class CurricularSemester extends CurricularSemester_Base implements Comparable<CurricularSemester> {

    public CurricularSemester() {
        super();
        setRootDomainObject(Bennu.getInstance());
    }

    public CurricularSemester(final CurricularYear curricularYear, final Integer semester) {
        this();
        setCurricularYear(curricularYear);
        setSemester(semester);
    }

    @Override
    public int compareTo(final CurricularSemester curricularSemester) {
        return getCurricularYear() == curricularSemester.getCurricularYear() ? getSemester().compareTo(
                curricularSemester.getSemester()) : getCurricularYear().compareTo(curricularSemester.getCurricularYear());
    }

    public static CurricularSemester readBySemesterAndYear(final Integer semester, final Integer year) {
        for (CurricularSemester curricularSemester : Bennu.getInstance().getCurricularSemestersSet()) {
            if (curricularSemester.getSemester().equals(semester)
                    && curricularSemester.getCurricularYear().getYear().equals(year)) {
                return curricularSemester;
            }
        }
        return null;
    }

}
