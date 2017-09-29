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
package org.fenixedu.academic.ui.renderers.providers;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.fenixedu.academic.domain.ExecutionCourse;
import org.fenixedu.academic.domain.Professorship;
import org.fenixedu.academic.dto.ShowSummariesBean;
import org.fenixedu.academic.dto.teacher.executionCourse.SummaryTeacherBean;

import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class ListExecutionCourseTeachersToShowSummariesProvider implements DataProvider {

    @Override
    public Object provide(Object source, Object currentValue) {
        ExecutionCourse executionCourse = ((ShowSummariesBean) source).getExecutionCourse();
        List<SummaryTeacherBean> teachers = new ArrayList<SummaryTeacherBean>();
        Set<Professorship> professorships = new TreeSet<Professorship>(Professorship.COMPARATOR_BY_PERSON_NAME);
        if (executionCourse != null) {
            professorships.addAll(executionCourse.getProfessorshipsSet());
            for (Professorship professorship : professorships) {
                teachers.add(teachers.size(), new SummaryTeacherBean(professorship));
            }
            teachers.add(teachers.size(), new SummaryTeacherBean(Boolean.TRUE));
        }
        return teachers;
    }

    @Override
    public Converter getConverter() {
        return null;
    }
}
