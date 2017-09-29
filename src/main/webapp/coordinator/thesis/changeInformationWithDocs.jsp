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
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>

<bean:define id="dcpId" name="degreeCurricularPlan" property="externalId"/>
<bean:define id="executionYearId" name="executionYearId"/>
<bean:define id="thesisId" name="thesis" property="externalId"/>

<html:xhtml/>

<jsp:include page="/coordinator/context.jsp" />

<h2><bean:message key="title.coordinator.thesis.edit"/></h2>

<p class="mtop15 mbottom05">
	<strong><bean:message key="title.coordinator.thesis.changeInformation"/></strong>
</p>

<bean:define id="returnMethod" value="<%= request.getParameter("return") != null ? request.getParameter("return") : "viewConfirmed" %>" type="java.lang.String"/>

<fr:edit name="thesis"
         action="<%= String.format("/manageThesis.do?method=editProposalWithDocs&amp;degreeCurricularPlanID=%s&amp;executionYearId=%s&amp;thesisID=%s", dcpId, executionYearId, thesisId) %>"
         schema="thesis.jury.proposal.information.edit">
     <fr:layout name="tabular">
        <fr:property name="classes" value="tstyle5 tdtop thlight thright mtop05"/>
        <fr:property name="columnClasses" value=",,tdclear tderror1"/>
    </fr:layout>
    
    <fr:destination name="cancel" path="<%= String.format("/manageThesis.do?method=%s&amp;degreeCurricularPlanID=%s&amp;executionYearId=%s&amp;thesisID=%s", returnMethod, dcpId, executionYearId, thesisId) %>"/>
 </fr:edit>
