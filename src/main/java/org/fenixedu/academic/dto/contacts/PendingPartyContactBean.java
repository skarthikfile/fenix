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
package org.fenixedu.academic.dto.contacts;

import java.util.List;

import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.contacts.EmailAddress;
import org.fenixedu.academic.domain.contacts.MobilePhone;
import org.fenixedu.academic.domain.contacts.Phone;
import org.fenixedu.academic.domain.contacts.PhysicalAddress;

public class PendingPartyContactBean {
    private Person person;

    public PendingPartyContactBean(final Person person) {
        this.person = person;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public PhysicalAddress getDefaultPhysicalAddress() {
        final List<PhysicalAddress> pendingOrValidPhysicalAddresses = getPerson().getPendingOrValidPhysicalAddresses();
        if (!pendingOrValidPhysicalAddresses.isEmpty()) {
            if (pendingOrValidPhysicalAddresses.size() > 0) {
                return pendingOrValidPhysicalAddresses.iterator().next();
            } else {
                for (PhysicalAddress physicalAddress : pendingOrValidPhysicalAddresses) {
                    if (physicalAddress.getPartyContactValidation() != null) {
                        if (physicalAddress.getPartyContactValidation().getToBeDefault() != null) {
                            if (physicalAddress.getPartyContactValidation().getToBeDefault()) {
                                return physicalAddress;
                            }
                        }
                    }
                }
                return null;
            }
        } else {
            return null;
        }
    }

    public Phone getDefaultPhone() {
        Phone defaultPhone = getPerson().getDefaultPhone();
        if (defaultPhone != null) {
            return defaultPhone;
        }
        final List<Phone> pendingPhones = getPerson().getPendingPhones();
        for (Phone phone : pendingPhones) {
            if (Boolean.TRUE.equals(phone.getPartyContactValidation().getToBeDefault())) {
                return phone;
            }
        }
        return null;
    }

    public MobilePhone getDefaultMobilePhone() {
        MobilePhone defaultPhone = getPerson().getDefaultMobilePhone();
        if (defaultPhone != null) {
            return defaultPhone;
        }
        final List<MobilePhone> pendingPhones = getPerson().getPendingMobilePhones();
        for (MobilePhone phone : pendingPhones) {
            if (Boolean.TRUE.equals(phone.getPartyContactValidation().getToBeDefault())) {
                return phone;
            }
        }
        return null;
    }

    public EmailAddress getDefaultEmailAddress() {
        EmailAddress defaultEmailAddress = getPerson().getDefaultEmailAddress();
        if (defaultEmailAddress != null) {
            return defaultEmailAddress;
        }
        final List<EmailAddress> pendingEmailAddresses = getPerson().getPendingEmailAddresses();
        for (EmailAddress emailAddress : pendingEmailAddresses) {
            if (Boolean.TRUE.equals(emailAddress.getPartyContactValidation().getToBeDefault())) {
                return emailAddress;
            }
        }
        return null;
    }

}
