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
<%@ page language="java"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/taglib/enum" prefix="e"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<html:xhtml />

<h2><bean:message key="label.operator.photo.title" bundle="MANAGER_RESOURCES" /></h2>

<p><strong>Fotografias pendentes para aprovação</strong></p>

<logic:empty name="pending">
    <p><em><bean:message key="label.operator.photo.no.pending" bundle="MANAGER_RESOURCES"/>.</em></p>
</logic:empty>

<logic:notEmpty name="pending">
	<fr:edit id="result" name="pending" schema="operator.photo.pending.list"
		action="/pendingPhotos.do?method=accept" layout="tabular-editable">
		<fr:layout name="tabular-editable">
			<fr:property name="classes" value="tstyle1 thlight mtop05 ulnomargin tdcenter" />
		</fr:layout>
	</fr:edit>
</logic:notEmpty>