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
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%><html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>

<%@page import="org.fenixedu.academic.dto.phd.YearMonth"%>

<style>
.unreadSubject { font-weight: bold; background: #fafaea !important; }
.unread { background: #fafaea !important; } 
</style>


<%--  ###  Return Links / Steps Information(for multistep forms)  ### --%>
<html:link action="/phdIndividualProgramProcess.do?method=viewProcess" paramId="processId" paramName="process" paramProperty="externalId">
	« <bean:message bundle="PHD_RESOURCES" key="label.back.to.process"/>
</html:link>
<br/><br/>
<%--  ### Return Links / Steps Information (for multistep forms)  ### --%>

<%--  ### Context Information (e.g. Person Information, Registration Information)  ### --%>
<strong><bean:message  key="label.phd.process" bundle="PHD_RESOURCES"/></strong>
<fr:view schema="PhdIndividualProgramProcess.view.simple" name="process">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle2 thlight mtop15" />
	</fr:layout>
</fr:view>
<%--  ### End Of Context Information  ### --%>

<br/><br/>

<ul>
	<li><html:link action="/phdIndividualProgramProcess.do?method=viewProcessAlertMessages" paramId="processId" paramName="process" paramProperty="externalId">
		<bean:message key="label.phd.recentAlertMessages" bundle="PHD_RESOURCES" />
	</html:link></li>
	
	<li><html:link action="/phdIndividualProgramProcess.do?method=viewUnreadProcessAlertMessages" paramId="processId" paramName="process" paramProperty="externalId">
		<bean:message key="label.phd.unreadAlertMessages" bundle="PHD_RESOURCES" />
		<bean:size id="unreadMessagesSize" name="processAlertMessagesToNotify"/>
		(<%= unreadMessagesSize.toString() %>)
	</html:link></li>
	
	<li><html:link action="/phdIndividualProgramProcess.do?method=viewProcessAlertMessageArchive" paramId="processId" paramName="process" paramProperty="externalId">
		<bean:message key="label.phd.archive" bundle="PHD_RESOURCES" />
	</html:link></li>
</ul>

<br/>

<strong><bean:message key="label.phd.archive" bundle="PHD_RESOURCES"/></strong>


<bean:define id="yearMonthBean" name="yearMonthBean"/>
<bean:define id="chosenYear" name="yearMonthBean" property="yearString" type="String" />
<bean:define id="chosenMonth" value="" type="String"/>
<logic:present name="yearMonthBean" property="month">
	<% chosenMonth = String.valueOf(((YearMonth) yearMonthBean).getMonth().getNumberOfMonth()); %>
</logic:present>
<fr:form>
	<fr:edit id="yearMonthBean" name="yearMonthBean">
		<fr:schema type="org.fenixedu.academic.dto.phd.YearMonth" bundle="PHD_RESOURCES">
			<fr:slot name="yearString" key="label.year" bundle="PHD_RESOURCES" layout="menu-select-postback">
				<fr:property name="nullOptionHidden" value="true" />
				<fr:property name="providerClass" value="org.fenixedu.academic.ui.renderers.providers.CivilYearsProvider" />
			</fr:slot>
			<fr:slot name="month" key="label.month" layout="menu-postback">
				<fr:property name="defaultText" value="-- ${fr:message('resources.PhdResources','label.all')} --" />
			</fr:slot>
		</fr:schema>
		<fr:destination name="postBack" path="/phdIndividualProgramProcess.do?method=viewProcessAlertMessageArchive"/>
		<fr:layout name="matrix">
			<fr:property name="classes" value="tstyle5 thlight thright mtop05 mbottom05 thmiddle" />
			<fr:property name="slot(yearString)" value="yearString"/>
			<fr:property name="row(yearString)" value="0"/>
			<fr:property name="column(yearString)" value="0"/>
			<fr:property name="slot(month)" value="month"/>
			<fr:property name="row(month)" value="0"/>
			<fr:property name="column(month)" value="1"/>
		</fr:layout>
	</fr:edit>
</fr:form>



<logic:notEmpty name="alertMessages">
	<fr:view name="alertMessages">
		<fr:schema type="org.fenixedu.academic.domain.phd.alert.PhdAlertMessage" bundle="PHD_RESOURCES">	
			<fr:slot name="whenCreated" layout="no-time" />
			<fr:slot name="process" layout="link">
				<fr:property name="contextRelative" value="true"/>
				<fr:property name="moduleRelative" value="true"/>
				<fr:property name="linkFormat" value="/phdIndividualProgramProcess.do?method=viewProcess&backMethod=viewAlertMessages&processId=\${externalId}" />
				<fr:property name="format" value="\${processNumber}"/>
			</fr:slot>
			<fr:slot name="subject" layout="link">
				<fr:property name="contextRelative" value="true"/>
				<fr:property name="moduleRelative" value="true"/>
				<fr:property name="useParent" value="true"/>
				<fr:property name="linkFormat" value="<%= "/phdIndividualProgramProcess.do?method=readAlertMessage&global=false&alertMessageId=${externalId}&unread=false&archive=true&year=" + chosenYear + "&month=" + chosenMonth %>" />
			</fr:slot>
			<fr:slot name="readed" />
		</fr:schema>
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle2 thlight mtop15"/>
			<fr:property name="columnClasses" value=",,,acenter"/>
			<fr:property name="conditionalColumnClass(whenCreated)" value="unread"/>
			<fr:property name="useCssIfNot(whenCreated)" value="readed"/>
			<fr:property name="conditionalColumnClass(process)" value="unread"/>
			<fr:property name="useCssIfNot(process)" value="readed"/>
			<fr:property name="conditionalColumnClass(subject)" value="unreadSubject"/>
			<fr:property name="useCssIfNot(subject)" value="readed"/>
			<fr:property name="conditionalColumnClass(readed)" value="unread"/>
			<fr:property name="useCssIfNot(readed)" value="readed"/>
		</fr:layout>
	</fr:view>	
</logic:notEmpty>
<logic:empty name="alertMessages">
	<p><em><bean:message  key="label.phd.noAlertMessages" bundle="PHD_RESOURCES"/>.</em></p>
</logic:empty>
