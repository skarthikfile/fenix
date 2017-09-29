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
<%@page import="org.fenixedu.academic.domain.phd.candidacy.InstitutionPhdCandidacyPeriod"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/taglib/phd" prefix="phd" %>

<%@page import="org.fenixedu.academic.domain.phd.individualProcess.activities.EditPhdParticipant"%>
<%@page import="org.fenixedu.academic.domain.phd.candidacy.InstitutionPhdCandidacyPeriod" %>

<%-- ### Title #### --%>
<h2><bean:message key="title.phd.candidacy.periods" bundle="PHD_RESOURCES" /></h2>
<%-- ### End of Title ### --%>

<%--  ###  Return Links / Steps Information(for multistep forms)  ### --%>
<html:link action="/phdCandidacyPeriodManagement.do?method=view" paramId="phdCandidacyPeriodId" paramName="phdCandidacyPeriod" paramProperty="externalId">
	<bean:message bundle="PHD_RESOURCES" key="label.back"/>
</html:link>
<%--  ### Return Links / Steps Information (for multistep forms)  ### --%>

<%--  ### Error Messages  ### --%>
<jsp:include page="/phd/errorsAndMessages.jsp" />
<%--  ### End of Error Messages  ### --%>

<%--  ### Context Information (e.g. Person Information, Registration Information)  ### --%>
 
<p><strong><bean:message  key="title.phd.candidacy.period.edit" bundle="PHD_RESOURCES"/></strong></p>

<bean:define id="phdCandidacyPeriodId" name="phdCandidacyPeriod" property="externalId" />

<fr:form action="<%= "/phdCandidacyPeriodManagement.do?method=editPhdCandidacyPeriod&amp;phdCandidacyPeriodId=" + phdCandidacyPeriodId %>">

	<fr:edit id="phdCandidacyPeriodBean" name="phdCandidacyPeriodBean" visible="false" />
	
	<fr:edit id="phdCandidacyPeriodBean.create" name="phdCandidacyPeriodBean">
		<fr:schema bundle="PHD_RESOURCES" type="org.fenixedu.academic.domain.phd.candidacy.PhdCandidacyPeriodBean">
			<fr:slot name="start" required="true" />
			<fr:slot name="end" required="true" />
		</fr:schema>
		
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle5 thlight thright mtop05" />
			<fr:property name="columnClasses" value=",,tdclear tderror1" />
		</fr:layout>
		
		<fr:destination name="invalid" path="<%=  "/phdCandidacyPeriodManagement.do?method=editPhdCandidacyPeriodInvalid&amp;phdCandidacyPeriodId="  + phdCandidacyPeriodId %>"/>
		<fr:destination name="cancel" path="<%= "/phdCandidacyPeriodManagement.do?method=view&amp;phdCandidacyPeriodId=" + phdCandidacyPeriodId %>" />
	</fr:edit>
	
	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" ><bean:message bundle="PHD_RESOURCES" key="label.submit"/></html:submit>
	<html:cancel bundle="HTMLALT_RESOURCES" altKey="cancel.cancel" ><bean:message bundle="PHD_RESOURCES" key="label.cancel"/></html:cancel>
	
</fr:form>

<h3>
	<bean:message key="title.phd.programs" bundle="PHD_RESOURCES" />
</h3>


<logic:equal name="phdCandidacyPeriod" property="class.name" value="<%= InstitutionPhdCandidacyPeriod.class.getName() %>">
	<logic:empty name="phdCandidacyPeriod" property="phdPrograms">
		<em><bean:message key="message.phd.candidacy.period.phd.programs.empty" bundle="PHD_RESOURCES" /></em>
	</logic:empty>
	
	<logic:notEmpty name="phdCandidacyPeriod" property="phdPrograms">
		<fr:view name="phdCandidacyPeriod" property="phdPrograms">
			<fr:schema bundle="PHD_RESOURCES" type="org.fenixedu.academic.domain.phd.PhdProgram" >
				<fr:slot name="name" />
			</fr:schema>
			
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle2 thlight mtop15 thleft" />
				
				<fr:link name="remove"
						label="label.remove,PHD_RESOURCES"
						link="<%= "/phdCandidacyPeriodManagement.do?method=removePhdProgram&amp;phdProgramId=${externalId}&amp;phdCandidacyPeriodId=" + phdCandidacyPeriodId %>" />
			</fr:layout>
		</fr:view>
	</logic:notEmpty>

	<fr:form action="<%= "/phdCandidacyPeriodManagement.do?method=addPhdProgram&amp;phdCandidacyPeriodId=" + phdCandidacyPeriodId %>">
		<fr:edit id="phdCandidacyPeriodBean" name="phdCandidacyPeriodBean" visible="false" />
		
		<fr:edit id="phdCandidacyPeriod.add.phdProgram" name="phdCandidacyPeriodBean" >
			<fr:schema bundle="PHD_RESOURCES" type="org.fenixedu.academic.domain.phd.candidacy.PhdCandidacyPeriodBean">
				<fr:slot name="phdProgramList" layout="option-select" required="true">
					<fr:property name="providerClass" value="org.fenixedu.academic.ui.struts.action.phd.PhdProgramsMultipleProvider" />
					<fr:property name="eachLayout" value="values" />
					<fr:property name="eachSchema" value="PhdProgram.name" />
					<fr:property name="sortBy" value="name" />
					<fr:property name="classes" value="nobullet noindent"/>
				</fr:slot> 
			</fr:schema>
			
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle5 thlight thright mtop05" />
				<fr:property name="columnClasses" value=",,tdclear tderror1" />
			</fr:layout>

			<fr:destination name="invalid" path="<%=  "/phdCandidacyPeriodManagement.do?method=addPhdProgramInvalid&amp;phdCandidacyPeriodId="  + phdCandidacyPeriodId %>"/>
		</fr:edit>

		<html:submit><bean:message bundle="PHD_RESOURCES" key="label.add"/></html:submit>
	</fr:form>
</logic:equal>
