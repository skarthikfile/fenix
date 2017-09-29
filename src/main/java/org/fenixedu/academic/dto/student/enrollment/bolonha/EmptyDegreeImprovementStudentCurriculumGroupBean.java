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
package org.fenixedu.academic.dto.student.enrollment.bolonha;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.fenixedu.academic.domain.ExecutionSemester;
import org.fenixedu.academic.domain.studentCurriculum.CurriculumGroup;
import org.fenixedu.academic.domain.studentCurriculum.CurriculumModule;
import org.fenixedu.academic.domain.studentCurriculum.NoCourseGroupCurriculumGroup;
import org.fenixedu.academic.domain.studentCurriculum.NoCourseGroupCurriculumGroupType;

public class EmptyDegreeImprovementStudentCurriculumGroupBean extends ImprovementStudentCurriculumGroupBean {

    static private final long serialVersionUID = 1L;

    public EmptyDegreeImprovementStudentCurriculumGroupBean(final CurriculumGroup curriculumGroup,
            final ExecutionSemester executionSemester) {
        super(curriculumGroup, executionSemester);
    }

    @Override
    protected List<StudentCurriculumGroupBean> buildCurriculumGroupsEnroled(CurriculumGroup parentGroup,
            ExecutionSemester executionSemester, int[] curricularYears) {

        final List<StudentCurriculumGroupBean> result = new ArrayList<StudentCurriculumGroupBean>();

        for (final CurriculumGroup curriculumGroup : filterGroups(parentGroup)) {
            result.add(new EmptyDegreeImprovementStudentCurriculumGroupBean(curriculumGroup, executionSemester));
        }

        return result;
    }

    private Set<CurriculumGroup> filterGroups(CurriculumGroup parentGroup) {
        final Set<CurriculumGroup> groups = new TreeSet<CurriculumGroup>(CurriculumModule.COMPARATOR_BY_NAME_AND_ID);

        for (final CurriculumModule curriculumModule : parentGroup.getCurriculumModulesSet()) {

            if (!curriculumModule.isLeaf()) {

                if (curriculumModule.isNoCourseGroupCurriculumGroup()) {
                    final NoCourseGroupCurriculumGroup noCourseGroup = (NoCourseGroupCurriculumGroup) curriculumModule;
                    if (noCourseGroup.getNoCourseGroupCurriculumGroupType() != NoCourseGroupCurriculumGroupType.STANDALONE) {
                        continue;
                    }
                }

                groups.add((CurriculumGroup) curriculumModule);
            }
        }
        return groups;
    }

}
