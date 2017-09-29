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
/*
 * Created on Apr 27, 2005
 *
 */
package org.fenixedu.academic.domain;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.organizationalStructure.Unit;
import org.fenixedu.bennu.core.domain.Bennu;

import pt.ist.fenixframework.Atomic;

/**
 * @author Ricardo Rodrigues
 * 
 */

public class NonAffiliatedTeacher extends NonAffiliatedTeacher_Base {

    public NonAffiliatedTeacher() {
        super();
        setRootDomainObject(Bennu.getInstance());
    }

    public NonAffiliatedTeacher(final String name, final Unit institution) {
        this();
        setName(name);
        setInstitutionUnit(institution);
    }

    public static Set<NonAffiliatedTeacher> findNonAffiliatedTeacherByName(final String name) {
        Pattern pattern = Pattern.compile(name.toLowerCase());
        final Set<NonAffiliatedTeacher> nonAffiliatedTeachers = new HashSet<NonAffiliatedTeacher>();
        for (final NonAffiliatedTeacher nonAffiliatedTeacher : Bennu.getInstance().getNonAffiliatedTeachersSet()) {
            Matcher matcher = pattern.matcher(nonAffiliatedTeacher.getName().toLowerCase());
            if (matcher.find()) {
                nonAffiliatedTeachers.add(nonAffiliatedTeacher);
            }
        }
        return nonAffiliatedTeachers;
    }

    @Atomic
    public static void associateToInstitutionAndExecutionCourse(final String nonAffiliatedTeacherName, final Unit institution,
            final ExecutionCourse executionCourse) {

        NonAffiliatedTeacher nonAffiliatedTeacher = institution.findNonAffiliatedTeacherByName(nonAffiliatedTeacherName);
        if (nonAffiliatedTeacher == null) {
            nonAffiliatedTeacher = new NonAffiliatedTeacher(nonAffiliatedTeacherName, institution);
        }

        if (nonAffiliatedTeacher.getExecutionCoursesSet().contains(executionCourse)) {
            throw new DomainException("error.invalid.executionCourse");
        } else {
            nonAffiliatedTeacher.addExecutionCourses(executionCourse);
        }

    }

    @Atomic
    public void removeExecutionCourse(final ExecutionCourse executionCourse) {
        getExecutionCoursesSet().remove(executionCourse);
    }

    public void delete() {
        setRootDomainObject(null);
        setInstitutionUnit(null);
        getExecutionCoursesSet().clear();

        super.deleteDomainObject();
    }

}
