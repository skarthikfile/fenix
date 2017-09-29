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
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@page import="org.fenixedu.academic.ui.renderers.student.curriculum.StudentCurricularPlanRenderer.OrganizationType"%>
<html:xhtml />
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<jsp:include page="/coordinator/context.jsp" />

<h2><bean:message key="label.curricularPlan"
	bundle="APPLICATION_RESOURCES" /> - <bean:write name="registration" property="lastStudentCurricularPlan.degreeCurricularPlan.presentationName"/></h2>

<logic:notEmpty name="registration" property="lastStudentCurricularPlan">
	<div class="infoop2 mbottom15">
		<bean:message  key="label.coordinator.transition.bolonha.message.part1" bundle="APPLICATION_RESOURCES"/>
		<bean:message  key="label.coordinator.transition.bolonha.message.part2" bundle="APPLICATION_RESOURCES"/>
	</div>

	<fr:edit name="registration" property="lastStudentCurricularPlan">
		<fr:layout>
			<fr:property name="organizedBy" value="<%=OrganizationType.GROUPS.name()%>" />
			<fr:property name="detailed" value="true" />
		</fr:layout>
	</fr:edit>
</logic:notEmpty>
<logic:empty name="registration" property="lastStudentCurricularPlan">
	<i><bean:message  key="label.transition.bolonha.noEquivalences" bundle="STUDENT_RESOURCES"/></i>
</logic:empty>
