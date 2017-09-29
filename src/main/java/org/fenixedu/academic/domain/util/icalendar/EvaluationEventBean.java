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
package org.fenixedu.academic.domain.util.icalendar;

import java.util.Set;

import org.fenixedu.academic.domain.ExecutionCourse;
import org.fenixedu.spaces.domain.Space;
import org.joda.time.DateTime;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableSet;

public class EvaluationEventBean extends EventBean {

    Set<ExecutionCourse> course;
    Space assignedRoom;

    public EvaluationEventBean(String title, DateTime begin, DateTime end, boolean allDay, Set<Space> rooms, String url,
            String note, Set<ExecutionCourse> course) {
        this(title, begin, end, allDay, null, rooms, url, note, course);
    }

    public EvaluationEventBean(String title, DateTime begin, DateTime end, boolean allDay, Space assignedRoom, Set<Space> rooms,
            String url, String note, Set<ExecutionCourse> course) {
        super(title, begin, end, allDay, rooms, url, note);
        setCourses(course);
        this.assignedRoom = assignedRoom;
    }

    @Override
    public String getTitle() {
        final ImmutableSet<String> acronyms =
                FluentIterable.from(getCourses()).transform(new Function<ExecutionCourse, String>() {

                    @Override
                    public String apply(ExecutionCourse input) {
                        return input.getSigla();
                    }
                }).toSet();
        return super.getTitle() + " : " + Joiner.on("; ").join(acronyms);
    }

    @Override
    public String getOriginalTitle() {
        return super.getTitle();
    }

    public Set<ExecutionCourse> getCourses() {
        return course;
    }

    public void setCourses(Set<ExecutionCourse> course) {
        this.course = course;
    }

    public Space getAssignedRoom() {
        return assignedRoom;
    }
}
