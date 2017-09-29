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
package org.fenixedu.academic.domain.accounting.paymentCodes;

import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.PersonAccount;
import org.fenixedu.academic.domain.accounting.PaymentCode;
import org.fenixedu.academic.domain.accounting.PaymentCodeType;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.student.Registration;
import org.fenixedu.academic.domain.student.Student;
import org.fenixedu.academic.domain.transactions.InsuranceTransaction;
import org.fenixedu.academic.domain.transactions.PaymentType;
import org.fenixedu.academic.util.Money;
import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;

public class MasterDegreeInsurancePaymentCode extends MasterDegreeInsurancePaymentCode_Base {

    protected MasterDegreeInsurancePaymentCode() {
        super();
    }

    private MasterDegreeInsurancePaymentCode(final PaymentCodeType paymentCodeType, final YearMonthDay startDate,
            final YearMonthDay endDate, final Money minAmount, final Money maxAmount, final Student student,
            final ExecutionYear executionYear) {
        this();
        init(paymentCodeType, startDate, endDate, minAmount, maxAmount, student, executionYear);
    }

    private void init(PaymentCodeType paymentCodeType, YearMonthDay startDate, YearMonthDay endDate, Money minAmount,
            Money maxAmount, Student student, ExecutionYear executionYear) {
        super.init(paymentCodeType, startDate, endDate, minAmount, maxAmount, student.getPerson());

        checkParameters(executionYear, student);
        super.setExecutionYear(executionYear);
    }

    private void checkParameters(final ExecutionYear executionYear, final Student student) {
        if (executionYear == null) {
            throw new DomainException(
                    "error.accounting.paymentCodes.MasterDegreeInsurancePaymentCode.executionYear.cannot.be.null");
        }

        if (student == null) {
            throw new DomainException("error.accounting.paymentCodes.MasterDegreeInsurancePaymentCode.student.cannot.be.null");
        }
    }

    public static MasterDegreeInsurancePaymentCode create(final YearMonthDay startDate, final YearMonthDay endDate,
            final Money minAmount, final Money maxAmount, final Student student, final ExecutionYear executionYear) {

        if (PaymentCode.canGenerateNewCode(MasterDegreeInsurancePaymentCode.class,
                PaymentCodeType.PRE_BOLONHA_MASTER_DEGREE_INSURANCE, student.getPerson())) {
            return new MasterDegreeInsurancePaymentCode(PaymentCodeType.PRE_BOLONHA_MASTER_DEGREE_INSURANCE, startDate, endDate,
                    minAmount, maxAmount, student, executionYear);
        }

        throw new DomainException("error.accounting.paymentCodes.MasterDegreeInsurancePaymentCode.could.not.generate.new.code");
    }

    @Override
    public void setExecutionYear(ExecutionYear executionYear) {
        throw new DomainException("error.accounting.paymentCodes.MasterDegreeInsurancePaymentCode.cannot.modify.executionYear");
    }

    @Override
    protected void internalProcess(Person responsiblePerson, Money amount, DateTime whenRegistered, String sibsTransactionId,
            String comments) {
        new InsuranceTransaction(amount.getAmount(), whenRegistered, PaymentType.SIBS, responsiblePerson, getPersonAccount(),
                getExecutionYear(), getRegistration());
    }

    private PersonAccount getPersonAccount() {
        return getPerson().getAssociatedPersonAccount();
    }

    private Registration getRegistration() {
        return getActiveRegistrationByDegreeType(getPerson().getStudent());
    }

    private static Registration getActiveRegistrationByDegreeType(Student student) {
        for (Registration registration : student.getRegistrationsSet()) {
            if (registration.getDegreeType().isPreBolonhaMasterDegree() && registration.isActive()) {
                return registration;
            }
        }
        return null;
    }

    @Override
    public void delete() {
        super.setExecutionYear(null);
        super.delete();
    }
}
