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
<%@page import="org.fenixedu.academic.domain.Person"%>
<%@ page language="java"%>
<%@page import="java.util.TreeSet"%>
<%@page import="org.fenixedu.academic.domain.student.Registration"%>
<%@page import="org.fenixedu.academic.domain.mobility.outbound.OutboundMobilityCandidacySubmission"%>
<%@page import="org.fenixedu.academic.domain.mobility.outbound.OutboundMobilityCandidacy"%>
<%@page import="org.fenixedu.academic.domain.ExecutionDegree"%>
<%@page import="org.fenixedu.academic.domain.mobility.outbound.OutboundMobilityCandidacyPeriod"%>
<%@page import="org.fenixedu.academic.domain.Country"%>
<%@page import="org.fenixedu.academic.domain.organizationalStructure.Unit"%>
<%@page import="org.fenixedu.academic.domain.candidacyProcess.mobility.MobilityAgreement"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
<html:xhtml />

<h2><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.mobility.outbound"/></h2>

<bean:define id="contest" name="contest" type="org.fenixedu.academic.domain.mobility.outbound.OutboundMobilityCandidacyContest"/>
<%
	final MobilityAgreement mobilityAgreement = contest.getMobilityAgreement();
	final Unit unit = mobilityAgreement.getUniversityUnit();
	final Country country = unit.getCountry();
	final OutboundMobilityCandidacyPeriod candidacyPeriod = contest.getOutboundMobilityCandidacyPeriod();
%>
<h2 class="separator2">
	<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.mobility.outbound.contest"/>:
	<%= mobilityAgreement.getMobilityProgram().getRegistrationProtocol().getDescription().getContent() %>
</h2>

<h3>
	<%= unit.getPresentationName() %>
</h3>

<ul>
	<li><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.country"/>: <%= country == null ? "" : country.getName() %></li>
	<li><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.executionYear"/>: <%= candidacyPeriod.getExecutionInterval().getName() %></li>
	<li><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.candidacy.period"/>: <%= candidacyPeriod.getIntervalAsString() %></li>
	<li>
		<bean:message key="label.degrees"/>:
		<% for (final ExecutionDegree executionDegree : contest.getOutboundMobilityCandidacyContestGroup().getSortedExecutionDegrees()) { %>
				<%= executionDegree.getDegree().getSigla() %>
		<% } %>
	</li>
	<li><bean:message key="label.vacancies"/>: <%= contest.getVacancies() == null ? "" : contest.getVacancies().toString() %></li>
	<li>
		<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.candidacy.count"/>:
		<%= contest.getOutboundMobilityCandidacySet().size() == 0 ? "0" : contest.getOutboundMobilityCandidacySet().size() %>
		<div style="margin-top: 10px; margin-left: 15px; width: 1050px;">
			<table class="tstyle1 mtop05">
				<tr>
					<th></th>
					<th><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.username"/></th>
					<th><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.name"/></th>
					<th><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.degree"/></th>
					<th><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.preference.order"/></th>
				</tr>
				<% for (final OutboundMobilityCandidacy candidacy : new TreeSet<OutboundMobilityCandidacy>(contest.getOutboundMobilityCandidacySet())) {
				    	final OutboundMobilityCandidacySubmission submission = candidacy.getOutboundMobilityCandidacySubmission();
				    	final Registration registration = submission.getRegistration();
				    	final Person person = registration.getPerson();
				%>
						<tr>
							<td>
								<div><img src="<%= request.getContextPath() +"/user/photo/" + person.getUsername() %>"  style="padding: 1em 0;" /></div>
							</td>
							<td><%= person.getUsername() %></td>
							<td><%= person.getName() %></td>
							<td><%= registration.getDegree().getSigla() %></td>
							<td><%= candidacy.getPreferenceOrder() %></td>
						</tr>
				<% } %>
			</table>
		</div>
	</li>
</ul>

