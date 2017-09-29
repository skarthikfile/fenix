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
<%@ page isELIgnored="true"%>
<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>
<html:xhtml/>

<em><bean:message key="label.alumni" bundle="GEP_RESOURCES"/></em>
<h2><bean:message key="title.alumni.recipients.management" bundle="GEP_RESOURCES"/></h2>
					   
<bean:define id="url"><%= request.getContextPath() %>/messaging/emails.do?method=newEmail</bean:define>
<ul>
	<li><html:link href="<%= url %>"><bean:message key="link.alumni.sendEmail" bundle="GEP_RESOURCES"/></html:link></li>	
	<li><html:link page="/alumni.do?method=prepareAddRecipients"><bean:message key="link.alumni.recipients.add" bundle="GEP_RESOURCES"/></html:link></li>
	<li><html:link page="/alumni.do?method=prepareRemoveRecipients"><bean:message key="link.alumni.recipients.remove" bundle="GEP_RESOURCES"/></html:link></li>
</ul>
