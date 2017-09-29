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
<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ page import="org.fenixedu.academic.ui.struts.action.resourceAllocationManager.utils.PresentationConstants" %>

<jsp:include page="contextExecutionDegreeAndCurricularYear.jsp"/>

<logic:present name="<%= PresentationConstants.EXECUTION_COURSE %>" scope="request">
	<bean:define id="executionCourse"
				 name="<%= PresentationConstants.EXECUTION_COURSE %>"
				 toScope="request"
				 scope="request"/>
	<bean:define id="executionCourseOID"
				 type="java.lang.String"
				 name="executionCourse"
				 property="externalId"
				 toScope="request"
				 scope="request"/>
</logic:present>