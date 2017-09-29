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
package org.fenixedu.academic.ui.struts.action.publicRelationsOffice;

import org.apache.struts.actions.ForwardAction;
import org.fenixedu.bennu.struts.annotations.Mapping;
import org.fenixedu.bennu.struts.portal.StrutsApplication;

@StrutsApplication(bundle = "ApplicationResources", path = "public-relations", titleKey = "label.publicRelationOffice",
        hint = PublicRelationsApplication.HINT, accessGroup = PublicRelationsApplication.ACCESS_GROUP)
@Mapping(path = "/index", module = "publicRelations", parameter = "/publicRelations/index.jsp")
public class PublicRelationsApplication extends ForwardAction {

    static final String HINT = "Public Relations";
    static final String ACCESS_GROUP = "role(PUBLIC_RELATIONS_OFFICE)";

    @StrutsApplication(bundle = "AlumniResources", path = "alumni", titleKey = "label.alumni.main.title", hint = HINT,
            accessGroup = ACCESS_GROUP)
    public static class PublicRelationsAlumniApp {
    }
}
