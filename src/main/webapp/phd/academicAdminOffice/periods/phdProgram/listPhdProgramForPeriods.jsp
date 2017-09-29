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
<%@page import="org.fenixedu.academic.domain.phd.individualProcess.activities.EditPhdParticipant"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/taglib/phd" prefix="phd" %>

<%-- ### Title #### --%>
<h2><bean:message key="title.phdProgram.periods" bundle="PHD_RESOURCES" /></h2>
<%-- ### End of Title ### --%>

<%--  ###  Return Links / Steps Information(for multistep forms)  ### --%>
<p>
	<html:link action="/phdIndividualProgramProcess.do?method=manageProcesses">
		« <bean:message bundle="PHD_RESOURCES" key="label.back"/>
	</html:link>
</p>

<%--  ### Return Links / Steps Information (for multistep forms)  ### --%>

<%--  ### Error Messages  ### --%>
<jsp:include page="/phd/errorsAndMessages.jsp" />
<%--  ### End of Error Messages  ### --%>

<%--  ### Context Information (e.g. Person Information, Registration Information)  ### --%>
 
<p><strong><bean:message  key="title.phd.programs" bundle="PHD_RESOURCES"/></strong></p>

<fr:view name="phdPrograms" >
	<fr:schema bundle="PHD_RESOURCES" type="org.fenixedu.academic.domain.phd.PhdProgram" >
		<fr:slot name="acronym" />
		<fr:slot name="name" />
		<%-- 
		<fr:slot name="mostRecentPeriod.beginDate" layout="null-as-label"/>
		<fr:slot name="mostRecentPeriod.endDate" layout="null-as-label"/>
		--%>
	</fr:schema>
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle2 thlight mtop15 thleft" />

		<fr:link 	label="link.org.fenixedu.academic.domain.phd.PhdProgram.view,PHD_RESOURCES" 
					name="view"
					link="/phdProgram.do?method=viewPhdProgramPeriods&amp;phdProgramId=${externalId}" />
	</fr:layout>
</fr:view>
