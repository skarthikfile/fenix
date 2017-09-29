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

import java.util.Comparator;

import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.spaces.domain.Space;

import pt.ist.fenixframework.dml.runtime.RelationAdapter;

public class MeasurementTestRoom extends MeasurementTestRoom_Base {

    static {
        getRelationMeasurementTestShiftMeasurementTestRoom().addListener(
                new RelationAdapter<MeasurementTestShift, MeasurementTestRoom>() {
                    @Override
                    public void beforeAdd(MeasurementTestShift shift, MeasurementTestRoom toAdd) {

                        if (toAdd != null && shift != null) {
                            if (shift.getRoomByName(toAdd.getName()) != null) {
                                throw new DomainException(
                                        "error.org.fenixedu.academic.domain.candidacy.MeasurementTestShift.already.contains.room.with.same.name");
                            }

                        }
                    }

                });
    }

    public static Comparator<MeasurementTestRoom> COMPARATOR_BY_ROOM_ORDER = new Comparator<MeasurementTestRoom>() {
        @Override
        public int compare(MeasurementTestRoom leftMeasurementTestRoom, MeasurementTestRoom rightMeasurementTestRoom) {
            return leftMeasurementTestRoom.getRoomOrder().compareTo(rightMeasurementTestRoom.getRoomOrder());
        }
    };

    protected MeasurementTestRoom() {
        super();
        setRootDomainObject(Bennu.getInstance());
    }

    public MeasurementTestRoom(String name, Integer capacity, MeasurementTestShift shift) {

        this();
        String[] args = {};

        if (shift == null) {
            throw new DomainException("error.org.fenixedu.academic.domain.candidacy.MeasurementTestRoom.shift.cannot.be.null",
                    args);
        }
        String[] args2 = {};
        if (name == null || name.isEmpty()) {
            throw new DomainException("error.org.fenixedu.academic.domain.candidacy.MeasurementTestRoom.name.cannot.be.null",
                    args2);
        }
        String[] args1 = {};
        if (capacity == null) {
            throw new DomainException("error.org.fenixedu.academic.domain.candidacy.MeasurementTestRoom.capacity.cannot.be.null",
                    args1);
        }

        setRoomOrder(shift.getRoomsSet().size() + 1);
        setShift(shift);
        setName(name);
        setCapacity(capacity);

    }

    public boolean isAvailable() {
        return getCapacity().intValue() - getRegistrationsSet().size() > 0;
    }

    public Space getCampus() {
        return getShift().getTest().getCampus();
    }

}
