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
package org.fenixedu.academic.domain.candidacy;

import java.util.HashSet;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.fenixedu.academic.domain.EntryPhase;
import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.student.Registration;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.spaces.domain.Space;

public class MeasurementTest extends MeasurementTest_Base {

    protected MeasurementTest() {
        super();
        setRootDomainObject(Bennu.getInstance());
    }

    public MeasurementTest(EntryPhase entryPhase, ExecutionYear executionYear, Space campus) {
        this();
        String[] args = {};

        if (entryPhase == null) {
            throw new DomainException("error.org.fenixedu.academic.domain.candidacy.MeasurementTest.entryPhase.cannot.be.null",
                    args);
        }
        String[] args1 = {};
        if (executionYear == null) {
            throw new DomainException(
                    "error.org.fenixedu.academic.domain.candidacy.MeasurementTest.executionYear.cannot.be.null", args1);
        }
        String[] args2 = {};
        if (campus == null) {
            throw new DomainException("error.org.fenixedu.academic.domain.candidacy.MeasurementTest.campus.cannot.be.null", args2);
        }

        setEntryPhase(entryPhase);
        setExecutionYear(executionYear);
        setCampus(campus);
    }

    public void assignToRoom(Registration registration) {
        for (final MeasurementTestShift shift : getSortedShifts()) {
            if (shift.getForDegreeCurricularPlanSet().contains(registration.getActiveDegreeCurricularPlan())
                    && shift.hasAvailableRoom()) {
                shift.getAvailableRoom().addRegistrations(registration);

                return;
            }
        }

        throw new DomainException("error.candidacy.MeasurementTest.unable.to.find.empty.room.for.registration");

    }

    private SortedSet<MeasurementTestShift> getSortedShifts() {
        final SortedSet<MeasurementTestShift> result = new TreeSet<MeasurementTestShift>(MeasurementTestShift.COMPARATOR_BY_NAME);

        result.addAll(getShiftsSet());

        return result;
    }

    public Set<Registration> getAssignedRegistrations() {
        final Set<Registration> result = new HashSet<Registration>();

        for (final MeasurementTestShift shift : getSortedShifts()) {
            for (MeasurementTestRoom room : shift.getSortedRooms()) {
                result.addAll(room.getRegistrationsSet());
            }
        }

        return result;
    }

    public static MeasurementTest readBy(EntryPhase entryPhase, ExecutionYear executionYear, Space campus) {
        for (final MeasurementTest test : Bennu.getInstance().getMeasurementTestsSet()) {
            if (test.isFor(entryPhase, executionYear, campus)) {
                return test;
            }
        }

        return null;
    }

    private boolean isFor(EntryPhase entryPhase, ExecutionYear executionYear, Space campus) {
        return getEntryPhase().equals(entryPhase) && getExecutionYear() == executionYear && getCampus() == campus;
    }

    public MeasurementTestShift getShiftByName(String name) {
        for (final MeasurementTestShift each : getShiftsSet()) {
            if (each.getName().equals(name)) {
                return each;
            }
        }

        return null;
    }

}
