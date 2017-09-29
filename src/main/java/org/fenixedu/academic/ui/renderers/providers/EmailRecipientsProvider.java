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
package org.fenixedu.academic.ui.renderers.providers;

import java.util.Set;
import java.util.TreeSet;

import org.fenixedu.academic.domain.util.email.EmailBean;
import org.fenixedu.academic.domain.util.email.Recipient;
import org.fenixedu.academic.domain.util.email.Sender;

import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class EmailRecipientsProvider implements DataProvider {

    @Override
    public Object provide(final Object source, final Object currentValue) {
        final EmailBean emailBean = (EmailBean) source;
        final Sender sender = emailBean.getSender();
        final Set<Recipient> recipients = new TreeSet<Recipient>(Recipient.COMPARATOR_BY_NAME);
        recipients.addAll(emailBean.getRecipients());
        if (sender != null) {
            recipients.addAll(sender.getRecipientsSet());
        }
        return recipients;
    }

    @Override
    public Converter getConverter() {
        return null;
    }

}
