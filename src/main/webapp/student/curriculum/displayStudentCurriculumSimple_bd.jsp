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
<%@ page import="org.fenixedu.academic.domain.curriculum.CurricularCourseType" %>
<%@ page import="org.fenixedu.academic.domain.curriculum.EnrollmentState" %>
<%@ page import="org.fenixedu.academic.dto.InfoEnrolment" %>

  <span class="error"><!-- Error messages go here --><html:errors /></span>

  <bean:define id="curriculum" name="<%= PresentationConstants.CURRICULUM %>" scope="request" />
  <bean:size id="enrolmentNumber" name="<%= PresentationConstants.CURRICULUM %>" scope="request" />
  <bean:define id="student" name="<%= PresentationConstants.STUDENT_CURRICULAR_PLAN %>" scope="request" />
  
  
	<table width="98%" border="0" cellpadding="0" cellspacing="0">
		<tr>
			<td bgcolor="#FFFFFF" class="infoselected">
				<bean:message key="label.person.name" />:
				<bean:write name="student" property="infoStudent.infoPerson.nome"/>
				<br/>
				<bean:message key="label.degree.name" />:
				<bean:write name="student" property="infoDegreeCurricularPlan.infoDegree.nome"/>
				<br/>
				<bean:message key="label.number" />:
				<bean:write name="student" property="infoStudent.number"/>
			</td>
		</tr>
	</table>

	<br/>
	<br/>
  
  
  <logic:notEqual name="enrolmentNumber" value="0">
	  <table>
	  	<tr>
		  	<th class="listClasses-header">
		  		<bean:message key="label.executionYear" />
		  	</th >
		  	<th class="listClasses-header">
		  		<bean:message key="label.semester" />
		  	</th >
		  	<th class="listClasses-header">
		  		<bean:message key="label.degree.name" />
		  	</th>
		  	<th class="listClasses-header">
		  		<bean:message key="label.curricular.course.name" />
		  	</th>
		  	<th class="listClasses-header">
		  		<bean:message key="label.finalEvaluation" />
		  	</th>
	  	</tr>
	  
	  	<logic:iterate id="enrolment" name="curriculum">
	  		<tr>
			  <td class="listClasses">
			    <bean:write name="enrolment" property="infoExecutionPeriod.infoExecutionYear.year"/>
			  </td>
			 
			  <td class="listClasses">
			    <bean:write name="enrolment" property="infoExecutionPeriod.semester"/>
			  </td>
			  <td class="listClasses">
			    <bean:write name="enrolment" property="infoCurricularCourse.infoDegreeCurricularPlan.infoDegree.sigla"/>
			  </td>
			  <td class="listClasses" style="text-align:left">
			    <bean:write name="enrolment" property="infoCurricularCourse.name"/>
				<% if ( !((InfoEnrolment) enrolment).getEnrollmentTypeResourceKey().equals(CurricularCourseType.NORMAL_COURSE.name()) ) {%>
					(<bean:message name="enrolment" property="enrollmentTypeResourceKey" bundle="ENUMERATION_RESOURCES"/>)
				<% } %>
			  </td>
			  <td class="listClasses">
				<logic:notEqual name="enrolment" property="enrollmentState" value="<%= EnrollmentState.APROVED.toString() %>">
					<bean:message name="enrolment" property="enrollmentState.name" bundle="ENUMERATION_RESOURCES" />
				</logic:notEqual>
				
				<logic:equal name="enrolment" property="enrollmentState" value="<%= EnrollmentState.APROVED.toString() %>">
					<bean:write name="enrolment" property="infoEnrolmentEvaluation.gradeValue"/>
				</logic:equal>
			  </td>
	  		</tr>
	    </logic:iterate>
	  </table>    	
  </logic:notEqual>
  <logic:equal name="enrolmentNumber" value="0">
		<bean:message key="message.no.enrolments" />
  </logic:equal>
    	
    		
