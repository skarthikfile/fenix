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
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
<%@ page import="org.fenixedu.commons.i18n.I18N"%>
<%@ page import="java.util.Locale"%>


<%!
	static String f(String value, Object ... args) {
    	return String.format(value, args);
	}
%>

<html:xhtml/>

<bean:define id="mappingPath" name="mappingPath"/>
<bean:define id="fullPath"><%= request.getContextPath() + "/publico" + mappingPath + ".do" %></bean:define>
<bean:define id="applicationInformationLinkDefault" name="application.information.link.default"/>
<bean:define id="applicationInformationLinkEnglish" name="application.information.link.english"/>

<div class="breadcumbs">
	<%= pt.ist.fenixWebFramework.servlets.filters.contentRewrite.GenericChecksumRewriter.NO_CHECKSUM_PREFIX %><a href="http://gri.ist.utl.pt/en">NMCI</a> &gt;
	<%= pt.ist.fenixWebFramework.servlets.filters.contentRewrite.GenericChecksumRewriter.NO_CHECKSUM_PREFIX %><a href="http://gri.ist.utl.pt/en/ist/">Study at <%=org.fenixedu.academic.domain.organizationalStructure.Unit.getInstitutionAcronym()%></a> &gt;
	<%= pt.ist.fenixWebFramework.servlets.filters.contentRewrite.GenericChecksumRewriter.NO_CHECKSUM_PREFIX %><a href='<%= f("%s/candidacies/erasmus", request.getContextPath()) %>'><bean:message key="title.application.name.mobility" bundle="CANDIDATE_RESOURCES"/></a> &gt;
	<bean:message key="erasmus.title.application.submission" bundle="CANDIDATE_RESOURCES" />
</div>

<h1><bean:write name="application.name"/></h1>
<bean:define id="mobilityProgram" name="individualCandidacyProcessBean" property="mobilityStudentDataBean.selectedMobilityProgram.registrationProtocol.description.content"/>
<bean:define id="onlyAllowedDegreeEnrolment" name="individualCandidacyProcessBean" property="mobilityStudentDataBean.selectedMobilityProgram.registrationProtocol.onlyAllowedDegreeEnrolment"/>
<h1><strong><bean:write name="mobilityProgram"/></strong></h1>

<p class="steps">
	<span><bean:message key="mobility.label.step.one.personal.details" bundle="CANDIDATE_RESOURCES"/></span> >
	<span><bean:message key="mobility.label.step.two.educational.background" bundle="CANDIDATE_RESOURCES" /></span> >
	<span><bean:message key="mobility.label.step.three.mobility.program" bundle="CANDIDATE_RESOURCES" /></span> >
	<span class="actual"><bean:message key="mobility.label.step.four.degree.and.subjects" bundle="CANDIDATE_RESOURCES" /></span> >
	<span><bean:message key="mobility.label.step.five.honour.declaration" bundle="CANDIDATE_RESOURCES" /></span>	 
</p>

<%--
<p class="mtop15"><span><bean:message key="message.fields.required" bundle="CANDIDATE_RESOURCES"/></span></p>
--%>

<html:messages id="message" message="true" bundle="APPLICATION_RESOURCES" property="captcha.error">
	<p><span class="error0"><bean:write name="message"/></span></p>
</html:messages>

<html:messages id="message" message="true" bundle="CANDIDATE_RESOURCES" property="error">
	<p><span class="error0"><bean:write name="message"/></span></p>
</html:messages>

<fr:hasMessages for="CandidacyProcess.personalDataBean" type="conversion">
	<ul class="nobullet list6">
		<fr:messages>
			<li><span class="error0"><fr:message/></span></li>
		</fr:messages>
	</ul>
</fr:hasMessages>

<script src="<%= request.getContextPath() + "/javaScript/jquery/jquery.js" %>" type="text/javascript" ></script>

<fr:form id="thisForm" action='<%= mappingPath + ".do?userAction=createCandidacy" %>'>

	<input type="hidden" id="removeId" name ="removeCourseId"/>
	<input type="hidden" id="skipValidationId" name="skipValidation" value="false"/>
	<input type="hidden" id="methodId" name="method" />
 	
	<fr:edit id="individualCandidacyProcessBean" name="individualCandidacyProcessBean" visible="false" />
	<fr:edit id="degree.course.information.bean" name="degreeCourseInformationBean" visible="false" />

	<h2 class="mtop1">
		<logic:notEqual name="onlyAllowedDegreeEnrolment" value="true">
			<bean:message key="label.eramsus.candidacy.choosen.subjectsAndDegree" bundle="ACADEMIC_OFFICE_RESOURCES" />
		</logic:notEqual>
		<logic:equal name="onlyAllowedDegreeEnrolment" value="true">
			<bean:message key="label.erasmus.candidacy.choose.degree" bundle="ACADEMIC_OFFICE_RESOURCES" />
		</logic:equal>
	</h2>
	
	<bean:define id="universityName" name="individualCandidacyProcessBean" property="mobilityStudentDataBean.selectedUniversity.nameI18n.content" type="String"/>
	<bean:define id="programName" name="individualCandidacyProcessBean" property="mobilityStudentDataBean.selectedMobilityProgram.registrationProtocol.description.content" type="String"/> 
	
	<p class="mbottom05"><bean:message key="message.mobility.available.degrees.for.mobility.agreement" bundle="ACADEMIC_OFFICE_RESOURCES" arg0="<%= programName %>" arg1="<%= universityName %>"/></p>

<style>
table.asd table tr td {
border: none;
padding: 0 !important;
}
</style>

	<fr:view	name="individualCandidacyProcessBean"
				schema="ErasmusCandidacyProcess.view.possible.degrees">
			<fr:layout>
				<fr:property name="classes" value="tstyle1 thlight thright mtop05 asd"/>
		        <fr:property name="columnClasses" value="width12em,,tdclear tderror1"/>
			</fr:layout>
	</fr:view>

	<logic:notEqual name="onlyAllowedDegreeEnrolment" value="true">	
		<div class="mtop3">
			<p>
				<strong><bean:message key="label.eramsus.candidacy.choosen.subjectsAndDegree" bundle="ACADEMIC_OFFICE_RESOURCES" /></strong>
			</p>
		
			<p class="mbottom05"><bean:message key="message.mobility.select.courses.of.associated.degrees" bundle="ACADEMIC_OFFICE_RESOURCES" /></p>
			
			<fr:edit id="degree.course.information.bean" name="degreeCourseInformationBean" schema="PublicErasmusCandidacyProcess.degreeCourseInformationBean">
				<fr:layout name="tabular-editable">
					<fr:property name="classes" value="tstyle5 thlight thright mtop05"/>
			        <fr:property name="columnClasses" value="width12em,,tdclear tderror1"/>
			        <fr:destination name="chooseDegreePostback" path="/candidacies/caseHandlingMobilityApplicationIndividualProcess.do?method=chooseDegree" />
				</fr:layout>
			</fr:edit>
				
			<html:submit onclick="$('#methodId').attr('value', 'addCourse'); $('#skipValidationId').attr('value', 'true'); $('#thisForm').submit(); return true;">+ Add subject</html:submit>
			
			<logic:empty name="individualCandidacyProcessBean" property="sortedSelectedCurricularCourses">
				<p class="mvert15"><em><bean:message key="erasmus.message.empty.courses" bundle="CANDIDATE_RESOURCES" />.</em></p>
			</logic:empty>
			
			<logic:notEmpty name="individualCandidacyProcessBean" property="sortedSelectedCurricularCourses">
			
				<p class="mtop2 mbottom05"><b>Selected subjects</b></p>
			
				<table class="tstyle1 thlight thcenter mtop05">
					<tr>
						<th><bean:message key="label.erasmus.course" bundle="ACADEMIC_OFFICE_RESOURCES"/></th>
						<th><bean:message key="label.erasmus.degree" bundle="ACADEMIC_OFFICE_RESOURCES"/></th>
						<th><bean:message key="label.erasmus.ects" bundle="ACADEMIC_OFFICE_RESOURCES" /></th>
						<th><!-- just in case --></th>
					</tr>
					<logic:iterate id="course" name="individualCandidacyProcessBean" property="sortedSelectedCurricularCourses" indexId="index">
						<bean:define id="curricularCourseId" name="course" property="externalId" />
					<tr>
						<td>
							<fr:view name="course" property="nameI18N"/>
						</td>
						<td>
							<fr:view name="course" property="degree.nameI18N" /> - 
							<fr:view name="course" property="degree.sigla" />
						</td>			
						<td>
							<fr:view name="course" property="ectsCredits" />
						</td>				
						<td>
							<a href="#" onclick="<%= f("$('#methodId').attr('value', 'removeCourse'); $('#skipValidationId').attr('value', 'true'); $('#removeId').attr('value', %s); $('#thisForm').submit()", curricularCourseId) %>"><bean:message key="label.erasmus.remove" bundle="ACADEMIC_OFFICE_RESOURCES" /></a>
						</td>
					</tr>
					</logic:iterate>
				</table>
			</logic:notEmpty>
		
			<p class="mtop15">
				<strong><bean:message key="label.eramsus.candidacy.choosed.degree" bundle="ACADEMIC_OFFICE_RESOURCES"/></strong>
			</p>
		
			<span class="highlight1">
				> <fr:view name="individualCandidacyProcessBean" property="selectedCourseNameForView"/>
			</span>
		</div>	
	</logic:notEqual>

	<logic:equal name="onlyAllowedDegreeEnrolment" value="true">
		<div id="selectDegree" class="mtop3">	
			<fr:edit id="mobility.individual.application" name="mobilityIndividualApplicationProcessBean">
				<fr:schema type="org.fenixedu.academic.domain.candidacyProcess.mobility.MobilityIndividualApplicationProcessBean" bundle="ACADEMIC_OFFICE_RESOURCES" >
					<fr:slot name="degree" key="label.mobility.degree" layout="menu-select-postback">
						<fr:property name="format" value="${presentationName}" />
						<fr:property name="destination" value="chooseDegreePostback"/>
						<fr:property name="providerClass" value="org.fenixedu.academic.ui.struts.action.candidacy.erasmus.DegreesForExecutionYearProviderForMobilityIndividualApplicationProcess" />		
					</fr:slot>
				</fr:schema>
				
				<fr:layout name="tabular-editable">
					<fr:property name="classes" value="tstyle4 thlight thright mtop025"/>
			        <fr:property name="columnClasses" value="width12em,,tdclear tderror1"/>
			        <fr:destination name="chooseDegreePostback" path="/candidacies/caseHandlingMobilityApplicationIndividualProcess.do?method=chooseDegreeForMobility" />
				</fr:layout>
			</fr:edit>
		</div>		
	</logic:equal>
	
	<p class="mtop2">
		<html:submit onclick="this.form.method.value='acceptHonourDeclaration'; return true;"><bean:message key="label.continue" bundle="APPLICATION_RESOURCES" /></html:submit>
		<%--
		<html:cancel onclick="this.form.method.value='listProcesses'; return true;"><bean:message key="label.cancel" bundle="APPLICATION_RESOURCES" /></html:cancel>
		--%>
	</p>

</fr:form>