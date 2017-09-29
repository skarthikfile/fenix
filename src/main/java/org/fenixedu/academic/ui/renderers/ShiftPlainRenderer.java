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
package org.fenixedu.academic.ui.renderers;

import java.util.Set;
import java.util.TreeSet;

import org.fenixedu.academic.domain.Lesson;
import org.fenixedu.academic.domain.Shift;
import org.fenixedu.academic.util.DateFormatUtil;

import pt.ist.fenixWebFramework.renderers.OutputRenderer;
import pt.ist.fenixWebFramework.renderers.components.HtmlComponent;
import pt.ist.fenixWebFramework.renderers.components.HtmlText;
import pt.ist.fenixWebFramework.renderers.layouts.Layout;

public class ShiftPlainRenderer extends OutputRenderer {

    @Override
    protected Layout getLayout(Object object, Class type) {
        return new Layout() {

            @Override
            public HtmlComponent createComponent(Object object, Class type) {
                if (object == null) {
                    return new HtmlText();
                }

                Shift shift = (Shift) object;
                final StringBuilder lessonsLabel = new StringBuilder();
                int index = 0;

                Set<Lesson> shiftLessons = new TreeSet<Lesson>(Lesson.LESSON_COMPARATOR_BY_WEEKDAY_AND_STARTTIME);
                shiftLessons.addAll(shift.getAssociatedLessonsSet());

                for (Lesson lesson : shiftLessons) {
                    index++;
                    lessonsLabel.append(lesson.getDiaSemana().toString()).append(" (");
                    lessonsLabel.append(DateFormatUtil.format("HH:mm", lesson.getInicio().getTime())).append("-");
                    lessonsLabel.append(DateFormatUtil.format("HH:mm", lesson.getFim().getTime())).append(") ");
                    if (lesson.hasSala()) {
                        lessonsLabel.append(lesson.getSala().getName());
                    }
                    if (index < shift.getAssociatedLessonsSet().size()) {
                        lessonsLabel.append(" ; ");
                    } else {
                        lessonsLabel.append(" ");
                    }
                }

                return new HtmlText(lessonsLabel.toString());
            }
        };
    }
}
