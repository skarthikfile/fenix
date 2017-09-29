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
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/taglib/enum" prefix="e" %>
<html:xhtml/>

<h2><bean:message key="title.statistics.students" /></h2>

<logic:present name="contextBean">
	<fr:form>
		<fr:edit name="contextBean" id="contextBean" type="org.fenixedu.academic.ui.struts.action.directiveCouncil.StudentStatisticsDA$ContextBean" 
				schema="student.statistics.bean">
			<fr:layout>
				<fr:property name="classes" value="thlight mbottom1"/>
			</fr:layout>
		</fr:edit>
	</fr:form>
</logic:present>

<logic:present name="statisticsBean">
	<logic:equal name="statisticsBean" property="showResult" value="true">
		<br/>
		<fr:view name="statisticsBean" type="org.fenixedu.academic.ui.struts.action.directiveCouncil.StudentStatisticsDA$StatisticsBean" 
				schema="student.statistics.bean.results">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle2 thlight"/>
				<fr:property name="columnClasses" value="smalltxt color888,,acenter,acenter,"/>
			</fr:layout>
		</fr:view>
	</logic:equal>
</logic:present>

