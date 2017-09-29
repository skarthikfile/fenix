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
package org.fenixedu.academic.servlet;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.apache.commons.lang.StringUtils;
import org.fenixedu.academic.domain.Person;

import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.AcroFields;

public abstract class PdfFiller {
    protected AcroFields form;

    protected String getMail(Person person) {
        if (person.hasInstitutionalEmailAddress()) {
            return person.getInstitutionalEmailAddressValue();
        } else {
            String emailForSendingEmails = person.getEmailForSendingEmails();
            return emailForSendingEmails != null ? emailForSendingEmails : StringUtils.EMPTY;
        }
    }

    public abstract ByteArrayOutputStream getFilledPdf(Person person) throws IOException, DocumentException;

    protected void setField(String fieldName, String fieldContent) throws IOException, DocumentException {
        if (fieldContent != null) {
            form.setField(fieldName, fieldContent);
        }
    }
}
