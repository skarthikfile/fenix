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
package org.fenixedu.academic.domain.period;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.fenixedu.academic.domain.Country;
import org.fenixedu.academic.domain.Degree;
import org.fenixedu.academic.domain.ExecutionInterval;
import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.candidacyProcess.mobility.MobilityAgreement;
import org.fenixedu.academic.domain.candidacyProcess.mobility.MobilityApplicationProcess;
import org.fenixedu.academic.domain.candidacyProcess.mobility.MobilityEmailTemplate;
import org.fenixedu.academic.domain.candidacyProcess.mobility.MobilityEmailTemplateBean;
import org.fenixedu.academic.domain.candidacyProcess.mobility.MobilityEmailTemplateType;
import org.fenixedu.academic.domain.candidacyProcess.mobility.MobilityProgram;
import org.fenixedu.academic.domain.candidacyProcess.mobility.MobilityQuota;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.organizationalStructure.UniversityUnit;
import org.joda.time.DateTime;

import pt.ist.fenixframework.Atomic;

public class MobilityApplicationPeriod extends MobilityApplicationPeriod_Base {

    public MobilityApplicationPeriod() {
        super();
    }

    public MobilityApplicationPeriod(final MobilityApplicationProcess applicationProcess, final ExecutionYear executionInterval,
            final DateTime start, final DateTime end) {
        this();
        init(applicationProcess, executionInterval, start, end);
    }

    public void delete() {
        if (getMobilityQuotasSet().size() > 0) {
            throw new DomainException("error.mobility.application.period.cant.be.deleted.it.has.defined.quotas");
        }
        if (getCandidacyProcessesSet().size() > 0) {
            throw new DomainException("error.mobility.application.period.cant.be.deleted.it.has.attached.process");
        }
        if (getEmailTemplatesSet().size() > 0) {
            throw new DomainException("error.mobility.application.period.cant.be.deleted.it.has.attached.email.templates");
        }
        if (getErasmusVacancySet().size() > 0) {
            throw new DomainException("error.mobility.application.period.cant.be.deleted.it.has.attached.erasmus.vacancies");
        }
        if (getExecutionInterval() != null) {
            throw new DomainException("error.mobility.application.period.cant.be.deleted.it.has.attached.execution.year");
        }
        setRootDomainObject(null);
        deleteDomainObject();
    }

    private void init(final MobilityApplicationProcess applicationProcess, final ExecutionInterval executionInterval,
            final DateTime start, final DateTime end) {
        checkParameters(applicationProcess);
        checkIfCanCreate(executionInterval, start, end);
        super.init(executionInterval, start, end);
        addCandidacyProcesses(applicationProcess);
    }

    private void checkParameters(final MobilityApplicationProcess applicationProcess) {
        if (applicationProcess == null) {
            throw new DomainException("error.ErasmusCandidacyProcess.invalid.candidacy.process");
        }
    }

    private void checkIfCanCreate(final ExecutionInterval executionInterval, final DateTime start, final DateTime end) {
        for (final MobilityApplicationPeriod mobilityApplicationPeriod : executionInterval.getMobilityApplicationPeriods()) {
            if (mobilityApplicationPeriod.intercept(start, end)) {
                throw new DomainException("error.ErasmusCandidacyPeriod.interception", executionInterval.getName(),
                        start.toString("dd/MM/yyyy HH:mm"), end.toString("dd/MM/yyyy HH:mm"));
            }
        }
    }

    public MobilityApplicationProcess getMobilityApplicationProcess() {
        return (MobilityApplicationProcess) (!getCandidacyProcessesSet().isEmpty() ? getCandidacyProcessesSet().iterator().next() : null);
    }

    @Override
    public ExecutionYear getExecutionInterval() {
        return (ExecutionYear) super.getExecutionInterval();
    }

    @Override
    public String getPresentationName() {
        return getStart().toString("dd/MM/yyyy") + " - " + getEnd().toString("dd/MM/yyyy");
    }

    @Override
    public void edit(final DateTime start, final DateTime end) {
        checkDates(start, end);
        checkIfCanEdit(start, end);
        super.setStart(start);
        super.setEnd(end);
    }

    private void checkIfCanEdit(DateTime start, DateTime end) {
        for (final MobilityApplicationPeriod mobilityApplicationPeriod : getExecutionInterval().getMobilityApplicationPeriods()) {
            if (mobilityApplicationPeriod != this && mobilityApplicationPeriod.intercept(start, end)) {
                throw new DomainException("error.ErasmusCandidacyPeriod.interception", getExecutionInterval().getName(),
                        start.toString("dd/MM/yyyy HH:mm"), end.toString("dd/MM/yyyy HH:mm"));
            }
        }
    }

    public List<Country> getAssociatedCountries() {
        Set<Country> countries = new HashSet<Country>();

        for (MobilityQuota mobilityQuota : this.getMobilityQuotasSet()) {
            countries.add(mobilityQuota.getMobilityAgreement().getUniversityUnit().getCountry());
        }

        return new ArrayList<Country>(countries);
    }

    public List<MobilityQuota> getOpeningsForCountry(Country country) {
        List<MobilityQuota> openingsList = new ArrayList<MobilityQuota>();

        for (MobilityQuota quota : getMobilityQuotasSet()) {
            if (quota.getMobilityAgreement().getUniversityUnit().getCountry() == country) {
                openingsList.add(quota);
            }
        }

        return openingsList;
    }

    public List<UniversityUnit> getUniversityUnitsAssociatedToCountry(Country country) {
        Set<UniversityUnit> universityUnits = new HashSet<UniversityUnit>();

        for (MobilityQuota quota : getOpeningsForCountry(country)) {
            universityUnits.add(quota.getMobilityAgreement().getUniversityUnit());
        }

        return new ArrayList<UniversityUnit>(universityUnits);
    }

    public MobilityQuota getAssociatedOpening(Degree degree, MobilityAgreement agreement) {
        if (degree == null || agreement == null) {
            return null;
        }
        for (MobilityQuota quota : agreement.getMobilityQuotasSet()) {
            if (quota.getDegree() != degree) {
                continue;
            }
            if (quota.getApplicationPeriod() != this) {
                continue;
            }
            return quota;
        }
        return null;
    }

    public List<Degree> getPossibleDegreesAssociatedToUniversity(UniversityUnit university) {
        Set<Degree> degreeSet = new HashSet<Degree>();

        for (MobilityQuota quota : getMobilityQuotasSet()) {
            if (quota.getMobilityAgreement().getUniversityUnit() == university) {
                degreeSet.add(quota.getDegree());
            }
        }

        return new ArrayList<Degree>(degreeSet);
    }

    public List<Degree> getPossibleDegreesAssociatedToAgreement(MobilityAgreement agreement) {
        Set<Degree> degreeSet = new HashSet<Degree>();

        for (MobilityQuota quota : getMobilityQuotasSet()) {
            if (quota.getMobilityAgreement() == agreement) {
                degreeSet.add(quota.getDegree());
            }
        }

        return new ArrayList<Degree>(degreeSet);
    }

    public boolean existsFor(MobilityAgreement agreement, Degree degree) {
        return getAssociatedOpening(degree, agreement) != null;
    }

    public Set<MobilityProgram> getMobilityPrograms() {
        Set<MobilityProgram> programs = new HashSet<MobilityProgram>();

        Collection<MobilityQuota> mobilityQuotas = getMobilityQuotasSet();

        for (MobilityQuota mobilityQuota : mobilityQuotas) {
            programs.add(mobilityQuota.getMobilityAgreement().getMobilityProgram());
        }

        return programs;
    }

    @Atomic
    public void editEmailTemplates(final MobilityEmailTemplateBean bean) {
        final MobilityEmailTemplateType type = bean.getType();
        final String subject = bean.getSubject();
        final String body = bean.getBody();
        final MobilityProgram program = bean.getMobilityProgram();
        /*
         * Don't use getMobilityPrograms() to get the programs affected by this
         * edit. All programs should be affected even those who don't have
         * quotas defines to this period at the time.
         */

        if (!hasEmailTemplateFor(program, type)) {
            MobilityEmailTemplate.create(this, program, type, subject, body);
        } else {
            getEmailTemplateFor(program, type).update(subject, body);
        }

    }

    public List<MobilityQuota> getMobilityQuotasByProgram(final MobilityProgram program) {
        List<MobilityQuota> result = new ArrayList<MobilityQuota>();

        Collection<MobilityQuota> mobilityQuotas = getMobilityQuotasSet();

        for (MobilityQuota mobilityQuota : mobilityQuotas) {
            if (mobilityQuota.isFor(program)) {
                result.add(mobilityQuota);
            }
        }

        return result;
    }

    public MobilityEmailTemplate getEmailTemplateFor(final MobilityProgram program, final MobilityEmailTemplateType type) {
        for (MobilityEmailTemplate template : getEmailTemplatesSet()) {
            if (template.isFor(program, type)) {
                return template;
            }
        }

        return null;
    }

    public boolean hasEmailTemplateFor(final MobilityProgram program, final MobilityEmailTemplateType type) {
        return getEmailTemplateFor(program, type) != null;
    }

    @Deprecated
    public MobilityEmailTemplate getEmailTemplateFor(final MobilityEmailTemplateType type) {
        MobilityProgram mobilityProgram = getMobilityPrograms().iterator().next();

        return getEmailTemplateFor(mobilityProgram, type);
    }

    @Deprecated
    public boolean hasEmailTemplateFor(final MobilityEmailTemplateType type) {
        MobilityProgram mobilityProgram = getMobilityPrograms().iterator().next();
        return hasEmailTemplateFor(mobilityProgram, type);
    }

}
