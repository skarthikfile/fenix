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
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://jakarta.apache.org/taglibs/datetime-1.0" prefix="dt" %>

<h2>Editar <bean:message bundle="MANAGER_RESOURCES" key="label.manager.executionDegreeManagement"/></h2>

<html:messages id="messages" message="true">
	<span class="error"><!-- Error messages go here --><bean:write name="messages" /></span>
</html:messages>

<html:form action="/executionDegreesManagement">
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="deleteExecutionDegrees"/>
	
	<div class='simpleblock4'>
	<fieldset class='lfloat'>
	
	<p><label><strong><bean:message bundle="MANAGER_RESOURCES" key="label.manager.degree.tipoCurso"/></strong>:</label>
		<html:select bundle="HTMLALT_RESOURCES" altKey="select.degreeType" property="degreeType" onchange="this.form.method.value='readDegreeCurricularPlans';this.form.submit();" >
			<html:options collection="degreeTypes" property="value" labelProperty="label" /> 
		</html:select>
	</p>
	
	<logic:notEmpty name="degreeCurricularPlans">
		<p><label><strong><bean:message bundle="MANAGER_RESOURCES" key="label.manager.degreeCurricularPlan"/></strong>:</label>
		<html:select bundle="HTMLALT_RESOURCES" altKey="select.degreeCurricularPlanID" property="degreeCurricularPlanID" onchange="this.form.method.value='readExecutionDegrees';this.form.submit();">
			<html:options collection="degreeCurricularPlans" property="value" labelProperty="label" /> 
		</html:select>
		</p>
	</logic:notEmpty>
	
	</fieldset>
	</div>

<span class="error"><!-- Error messages go here --><html:errors /><br/></span>
<br/>
<logic:notEmpty name="executionDegrees">

		<table cellpadding='0' border='0' class="table table-bordered">
			<tr>
				<td class="listClasses-header"></td>
				<th class='listClasses-header'> <bean:message bundle="MANAGER_RESOURCES" key="label.manager.executionDegree.executionYear"/> </th>
				<th class='listClasses-header'> <bean:message bundle="MANAGER_RESOURCES" key="label.manager.executionDegree.coordinator"/> </th>
				<th class='listClasses-header'> </th>
			</tr>
			<logic:iterate id="executionDegree" name="executionDegrees">
				<tr>
					<td class="listClasses">
						<html:multibox bundle="HTMLALT_RESOURCES" altKey="multibox.internalIds" property="internalIds">
							<bean:write name="executionDegree" property="externalId"/>
						</html:multibox>
					</td>
					<td class='listClasses'>
						<bean:write name="executionDegree" property="executionYear.year" /> 
					</td>
					<td class='listClasses'>
						<logic:notEmpty name="executionDegree"  property="coordinatorsList">
							<logic:iterate id="coordinator" name="executionDegree"  property="coordinatorsList">
								<bean:write name="coordinator" property="person.name" />
								<br/>
							</logic:iterate>
						</logic:notEmpty>
						<logic:empty name="executionDegree"  property="coordinatorsList">
							<bean:message bundle="MANAGER_RESOURCES" key="label.manager.noCoordinatorsList" />
						</logic:empty>
					</td>
					<td class='listClasses'>
						<html:link module="/manager" action="/executionDegreesManagement.do?method=prepareEditExecutionDegree" paramId="executionDegreeID" paramName="executionDegree" paramProperty="externalId">
							<bean:message bundle="MANAGER_RESOURCES" key="link.edit"/>
						</html:link>
						<br/>
						<html:link module="/manager" action="/executionDegreesManagement.do?method=readCoordinators" paramId="executionDegreeID" paramName="executionDegree" paramProperty="externalId">
							<bean:message bundle="MANAGER_RESOURCES" key="label.manager.edit.executionDegree.coordinators" />
						</html:link>
					</td>
				</tr>
			</logic:iterate>
		</table>

		<bean:define id="onclick">
			return confirm('<bean:message bundle="MANAGER_RESOURCES" key="message.confirm.delete.execution.degrees"/>')
		</bean:define>
		<br/>			
		<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton" onclick='<%=onclick.toString() %>'><bean:message bundle="MANAGER_RESOURCES" key="label.manager.delete.selected.executionDegrees"/></html:submit>

	</logic:notEmpty>


</html:form>
