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
package org.fenixedu.academic.domain.candidacyProcess;

import java.io.Serializable;

import org.fenixedu.academic.domain.ExecutionInterval;
import org.joda.time.DateTime;

public class CandidacyProcessBean implements Serializable {

    private ExecutionInterval executionInterval;

    private DateTime start, end;

    private String name;

    protected CandidacyProcessBean() {
    }

    public CandidacyProcessBean(final ExecutionInterval executionInterval) {
        setExecutionInterval(executionInterval);
    }

    public CandidacyProcessBean(final CandidacyProcess process) {
        setExecutionInterval(process.getCandidacyExecutionInterval());
        setStart(process.getCandidacyStart());
        setEnd(process.getCandidacyEnd());
    }

    public ExecutionInterval getExecutionInterval() {
        return this.executionInterval;
    }

    public void setExecutionInterval(ExecutionInterval executionInterval) {
        this.executionInterval = executionInterval;
    }

    public DateTime getStart() {
        return start;
    }

    public void setStart(DateTime start) {
        this.start = start;
    }

    public DateTime getEnd() {
        return end;
    }

    public void setEnd(DateTime end) {
        this.end = end;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
