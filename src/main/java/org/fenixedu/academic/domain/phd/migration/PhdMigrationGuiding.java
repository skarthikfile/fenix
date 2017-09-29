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
package org.fenixedu.academic.domain.phd.migration;

import java.util.Map;
import java.util.NoSuchElementException;

import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.Teacher;
import org.fenixedu.academic.domain.phd.InternalPhdParticipant;
import org.fenixedu.academic.domain.phd.PhdIndividualProgramProcess;
import org.fenixedu.academic.domain.phd.PhdParticipant;
import org.fenixedu.academic.domain.phd.PhdParticipantBean;
import org.fenixedu.academic.domain.phd.PhdParticipantBean.PhdParticipantSelectType;
import org.fenixedu.academic.domain.phd.PhdParticipantBean.PhdParticipantType;
import org.fenixedu.academic.domain.phd.migration.common.exceptions.IncompleteFieldsException;
import org.fenixedu.academic.domain.phd.migration.common.exceptions.PhdMigrationGuidingNotFoundException;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.domain.User;

public class PhdMigrationGuiding extends PhdMigrationGuiding_Base {
    public static final String IST_INSTITUTION_CODE = "1518";

    protected PhdMigrationGuiding() {
        super();
        setRootDomainObject(Bennu.getInstance());
    }

    protected PhdMigrationGuiding(String data) {
        super();
        setData(data);
    }

    public static class PhdMigrationGuidingBean {

        private transient String data;

        private transient Integer phdStudentNumber;
        private transient String institutionCode;
        private transient String name;
        private transient String teacherId;

        public PhdMigrationGuidingBean(String data) {
            setData(data);
            parse();
        }

        public void parse() {
            try {
                String[] compounds = getData().split("\\t");

                this.phdStudentNumber = Integer.parseInt(compounds[0].trim());
                this.teacherId = compounds[2].trim();
                this.institutionCode = compounds[3].trim();
                this.name = compounds[4].trim();
            } catch (NoSuchElementException e) {
                throw new IncompleteFieldsException();
            }
        }

        public String getData() {
            return data;
        }

        public void setData(String data) {
            this.data = data;
        }

        public Integer getPhdStudentNumber() {
            return phdStudentNumber;
        }

        public void setPhdStudentNumber(Integer phdStudentNumber) {
            this.phdStudentNumber = phdStudentNumber;
        }

        public String getInstitutionCode() {
            return institutionCode;
        }

        public void setInstitutionCode(String institutionCode) {
            this.institutionCode = institutionCode;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getTeacherId() {
            return teacherId;
        }

        public void setTeacherId(String teacherId) {
            this.teacherId = teacherId;
        }

    }

    public PhdMigrationGuidingBean getGuidingBean() {
        return new PhdMigrationGuidingBean(getData());
    }

    public void parseAndSetNumber(Map<String, String> INSTITUTION_MAP) {
        final PhdMigrationGuidingBean guidingBean = getGuidingBean();

        setTeacherId(guidingBean.getTeacherId());
        setInstitution(INSTITUTION_MAP.get(guidingBean.getInstitutionCode()));
    }

    public boolean isExternal() {
        return !getGuidingBean().getInstitutionCode().equals(IST_INSTITUTION_CODE);
    }

    public PhdParticipantBean getPhdParticipantBean(final PhdIndividualProgramProcess individualProcess) {

        if (isExternal()) {
            return getExternalPhdParticipantBean(individualProcess);
        } else {
            return getInternalPhdParticipantBean(individualProcess);
        }

    }

    private PhdParticipantBean getExternalPhdParticipantBean(final PhdIndividualProgramProcess individualProcess) {
        final PhdParticipantBean participantBean = new PhdParticipantBean();
        participantBean.setParticipantType(PhdParticipantType.EXTERNAL);
        participantBean.setParticipantSelectType(PhdParticipantSelectType.NEW);
        participantBean.setIndividualProgramProcess(individualProcess);
        participantBean.setName(getGuidingBean().getName());
        participantBean.setWorkLocation(getInstitution());
        participantBean.setInstitution(getInstitution());

        return participantBean;
    }

    private PhdParticipantBean getInternalPhdParticipantBean(final PhdIndividualProgramProcess individualProcess) {
        final PhdParticipantBean participantBean = new PhdParticipantBean();
        participantBean.setIndividualProgramProcess(individualProcess);
        final Teacher teacher = User.findByUsername(getGuidingBean().getTeacherId()).getPerson().getTeacher();

        if (teacher == null) {
            throw new PhdMigrationGuidingNotFoundException("The guiding is not present in the system as a teacher");
        }

        for (PhdParticipant existingParticipant : individualProcess.getParticipantsSet()) {
            if (!existingParticipant.isInternal()) {
                continue;
            }

            final InternalPhdParticipant existingInternalParticipant = (InternalPhdParticipant) existingParticipant;
            final Person existingInternalPerson = existingInternalParticipant.getPerson();

            if (teacher.getPerson() == existingInternalPerson) {
                // The guider is already associated with the process
                participantBean.setInternalParticipant(teacher.getPerson());
                participantBean.setParticipant(existingParticipant);
                participantBean.setParticipantSelectType(PhdParticipantSelectType.EXISTING);

                return participantBean;
            }
        }

        // The guiding is in the system as teacher, but not yet associated with
        // the process
        participantBean.setParticipantSelectType(PhdParticipantSelectType.NEW);
        participantBean.setInternalParticipant(teacher.getPerson());
        participantBean.setInstitution(getInstitution());
        participantBean.setWorkLocation(getInstitution());
        return participantBean;
    }

}
