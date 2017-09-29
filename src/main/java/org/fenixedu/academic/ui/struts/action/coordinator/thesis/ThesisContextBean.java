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
package org.fenixedu.academic.ui.struts.action.coordinator.thesis;

import java.io.Serializable;
import java.util.SortedSet;

import org.fenixedu.academic.domain.ExecutionYear;

public class ThesisContextBean implements Serializable {

    /**
     * Serial version id.
     */
    private static final long serialVersionUID = 1L;

    private ExecutionYear selected;
    private SortedSet<ExecutionYear> executionYears;
    private ThesisPresentationState presentationState;

    public ThesisContextBean(SortedSet<ExecutionYear> executionYears, ExecutionYear selected) {
        setExecutionYearPossibilities(executionYears);
        setExecutionYear(selected);
    }

    public SortedSet<ExecutionYear> getExecutionYearPossibilities() {
        return this.executionYears;
    }

    public void setExecutionYearPossibilities(SortedSet<ExecutionYear> executionYears) {
        this.executionYears = executionYears;
    }

    public ExecutionYear getExecutionYear() {
        return this.selected;
    }

    public void setExecutionYear(ExecutionYear executionYear) {
        this.selected = executionYear;
    }

    public void setPresentationState(ThesisPresentationState presentationState) {
        this.presentationState = presentationState;
    }

    public ThesisPresentationState getPresentationState() {
        return presentationState;
    }

}
