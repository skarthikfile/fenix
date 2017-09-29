<%--

    Copyright © 2002 Instituto Superior Técnico

    This file is part of FenixEdu Academic.

    FenixEdu Academic is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    FenixEdu Academic is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.

--%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
<html:xhtml />
<bean:define id="partyContactClass" scope="request" name="partyContactClass" />

<em><bean:message key="label.academicAdminOffice" bundle="ACADEMIC_OFFICE_RESOURCES" /></em>
<h2><bean:message key="<%= "label.partyContacts.add" +  partyContactClass %>"
    bundle="ACADEMIC_OFFICE_RESOURCES" /></h2>

<fr:form action="/partyContacts.do">
    <html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method"
        value="createPartyContact" />

    <bean:define id="studentID" type="java.lang.String" name="student" property="externalId" />
    <html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="studentID"
        value="<%= studentID.toString() %>" />

    <bean:define id="person" name="student" property="person" />

    <logic:notPresent name="partyContact">
        <fr:create schema="<%= "contacts." + partyContactClass + ".manage" %>"
            type="<%= "org.fenixedu.academic.domain.contacts." + partyContactClass  %>">
            <fr:layout name="tabular-editable">
                <fr:property name="classes" value="tstyle5 thlight thright mtop025" />
                <fr:property name="columnClasses" value=",,tdclear tderror1" />
            </fr:layout>
            <fr:hidden slot="party" name="person" />
        </fr:create>
    </logic:notPresent>

    <logic:present name="partyContact">
        <fr:edit id="edit-contact" name="partyContact"
            schema="<%= "contacts." + partyContactClass + ".manage" %>">
            <fr:layout name="tabular-editable">
                <fr:property name="classes" value="tstyle5 thlight thright mtop025" />
                <fr:property name="columnClasses" value=",,tdclear tderror1" />
            </fr:layout>
            <fr:destination name="postback-set-public"
                path="/partyContacts.do?method=postbackSetPublic&form=create" />
            <fr:destination name="postback-set-elements"
                path="/partyContacts.do?method=postbackSetElements&form=create" />
            <fr:destination name="invalid" path="/partyContacts.do?method=invalid&form=create"/>
        </fr:edit>
    </logic:present>

    <p><html:submit>
        <bean:message key="button.submit" bundle="ACADEMIC_OFFICE_RESOURCES" />
    </html:submit> <html:cancel onclick="this.form.method.value='backToShowInformation';">
        <bean:message key="button.cancel" bundle="ACADEMIC_OFFICE_RESOURCES" />
    </html:cancel></p>
</fr:form>
