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
package org.fenixedu.academic.dto.phd.student;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.fenixedu.academic.domain.CompetenceCourse;
import org.fenixedu.academic.domain.CurricularCourse;
import org.fenixedu.academic.domain.ExecutionSemester;
import org.fenixedu.academic.domain.degreeStructure.Context;
import org.fenixedu.academic.domain.enrolment.DegreeModuleToEnrol;
import org.fenixedu.academic.domain.enrolment.IDegreeModuleToEvaluate;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.studentCurriculum.CurriculumGroup;
import org.fenixedu.academic.dto.student.enrollment.bolonha.StudentCurriculumGroupBean;

public class PhdStudentCurriculumGroupBean extends StudentCurriculumGroupBean {

    static private final long serialVersionUID = 1L;

    public PhdStudentCurriculumGroupBean(CurriculumGroup curriculumGroup, ExecutionSemester executionSemester,
            int[] curricularYears) {
        super(curriculumGroup, executionSemester, curricularYears);
    }

    @Override
    protected List<IDegreeModuleToEvaluate> buildCurricularCoursesToEnrol(CurriculumGroup group, ExecutionSemester semester) {

        final Collection<CompetenceCourse> collection = getCompetenceCoursesAvailableToEnrol();

        final List<IDegreeModuleToEvaluate> result = new ArrayList<IDegreeModuleToEvaluate>();
        for (final Context context : group.getCurricularCourseContextsToEnrol(semester)) {

            if (canBeUsed(collection, context)) {
                result.add(new DegreeModuleToEnrol(group, context, semester));
            }
        }

        return result;
    }

    private boolean canBeUsed(Collection<CompetenceCourse> collection, Context context) {
        final CurricularCourse course = (CurricularCourse) context.getChildDegreeModule();
        return course.isOptionalCurricularCourse()
                || (course.getCompetenceCourse() != null && collection.contains(course.getCompetenceCourse()));
    }

    private Collection<CompetenceCourse> getCompetenceCoursesAvailableToEnrol() {
        return getRegistration().getPhdIndividualProgramProcess().getCompetenceCoursesAvailableToEnrol();
    }

    @Override
    protected List<IDegreeModuleToEvaluate> buildCurricularCoursesToEnrol(CurriculumGroup group,
            ExecutionSemester executionSemester, int[] curricularYears) {
        throw new DomainException("error.PhdStudentCurriculumGroupBean.unexpected.invocation");
    }

    @Override
    protected StudentCurriculumGroupBean createEnroledCurriculumGroupBean(ExecutionSemester executionSemester,
            int[] curricularYears, CurriculumGroup curriculumGroup) {
        return new PhdStudentCurriculumGroupBean(curriculumGroup, executionSemester, curricularYears);
    }
}
