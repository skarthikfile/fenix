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
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<h2><bean:message key="link.objectives" /></h2>

<div class="infoop2">
	<bean:message key="label.objectives.explanation" />
</div>

<p>
	<span class="error0"><!-- Error messages go here -->
		<html:errors/>
	</span>
	<span class="warning0"><!-- w3c complient -->
		<html:messages id="info" message="true"/>
	</span>
</p>

<logic:present name="curriculum">
	<bean:define id="curriculum" name="curriculum" type="org.fenixedu.academic.domain.Curriculum"/>
	<bean:define id="curricularCourse" name="curriculum" property="curricularCourse"/>
	<bean:define id="degree" name="curricularCourse" property="degreeCurricularPlan.degree"/>
	<h3>
		<bean:write name="degree" property="presentationName"/>
		<br/>
		<bean:write name="curricularCourse" property="name"/>
	</h3>

	<bean:define id="url" type="java.lang.String">/editObjectives.do?method=editObjectives&amp;executionCourseID=<bean:write name="executionCourse" property="externalId"/></bean:define>
	<bean:define id="curricularCourse" name="curricularCourse" type="org.fenixedu.academic.domain.CurricularCourse"/>

	<bean:define id="curriculumFactoryEditCurriculum" name="curricularCourse" property="curriculumFactoryEditCurriculum" type="org.fenixedu.academic.ui.struts.action.teacher.executionCourse.ExecutionCourseObjectivesDA.CurriculumFactoryEditCurriculum"/>
	<logic:notEqual name="executionCourse" property="executionPeriod.executionYear.state.stateCode" value="C">
		<%
			curriculumFactoryEditCurriculum.setCurriculum(curriculum);
		%>
	</logic:notEqual>

	<fr:edit name="curriculumFactoryEditCurriculum"
			schema="org.fenixedu.academic.domain.Curriculum.Objectives"
			action="<%= url %>"
			>
		<fr:layout name="flow">
			<fr:property name="eachClasses" value="flowblock"/>
		</fr:layout>
	</fr:edit>

</logic:present>
