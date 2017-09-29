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
package org.fenixedu.academic.ui.struts.action.phd;

import java.util.ArrayList;

import org.fenixedu.academic.domain.phd.debts.PhdEventExemptionJustificationType;

import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;
import pt.ist.fenixWebFramework.renderers.converters.EnumConverter;

public class PhdEventExemptionJustificationTypeProvider implements DataProvider {

    @Override
    public Converter getConverter() {
        return new EnumConverter();
    }

    @Override
    public Object provide(Object arg0, Object arg1) {
        ArrayList<PhdEventExemptionJustificationType> list = new ArrayList<PhdEventExemptionJustificationType>();
        for (PhdEventExemptionJustificationType type : PhdEventExemptionJustificationType.values()) {
            if (!type.equals(PhdEventExemptionJustificationType.PHD_GRATUITY_FCT_SCHOLARSHIP_EXEMPTION)) {
                list.add(type);
            }
        }
        return list;
    }

}
