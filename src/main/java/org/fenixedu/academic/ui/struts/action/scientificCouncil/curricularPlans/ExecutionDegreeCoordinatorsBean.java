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
package org.fenixedu.academic.ui.struts.action.scientificCouncil.curricularPlans;

import java.io.Serializable;
import java.util.Set;

import org.fenixedu.academic.domain.Coordinator;
import org.fenixedu.academic.domain.ExecutionDegree;
import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.Person;

public class ExecutionDegreeCoordinatorsBean implements Serializable {

    private ExecutionYear executionYear;
    private ExecutionDegree executionDegree;
    private Person newCoordinator;
    private String backPath;

    public ExecutionYear getExecutionYear() {
        return executionYear;
    }

    public void setExecutionYear(ExecutionYear executionYear) {
        this.executionYear = executionYear;
    }

    public ExecutionDegree getExecutionDegree() {
        return executionDegree;
    }

    public void setExecutionDegree(ExecutionDegree executionDegree) {
        this.executionDegree = executionDegree;
    }

    public Person getNewCoordinator() {
        return newCoordinator;
    }

    public void setNewCoordinator(Person newCoordinator) {
        this.newCoordinator = newCoordinator;
    }

    public Set<Coordinator> getCoordinators() {
        return executionDegree.getCoordinatorsListSet();
    }

    public ExecutionDegreeCoordinatorsBean(ExecutionDegree executionDegree) {
        this.executionDegree = executionDegree;
    }

    public String getBackPath() {
        return backPath;
    }

    public void setBackPath(String backPath) {
        this.backPath = backPath;
    }

    public String getEscapedBackPath() {
        return new String(this.backPath.replace('&', '§'));
    }

    public void setEscapedBackPath(String escapedBackPath) {
        this.backPath = escapedBackPath.replace('§', '&');
    }

    public ExecutionDegreeCoordinatorsBean() {

    }

}
