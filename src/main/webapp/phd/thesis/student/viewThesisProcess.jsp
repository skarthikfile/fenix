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
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>

<logic:notEmpty name="process" property="thesisProcess">

<logic:equal name="process" property="activeState.active" value="true">

	<bean:define id="thesisProcess" name="process" property="thesisProcess" />
	
	<br/>
	<strong><bean:message  key="label.phd.thesisProcess" bundle="PHD_RESOURCES"/></strong>
	<table>
	  <tr>
		    <td>
				<fr:view schema="PhdThesisProcess.view.simple" name="process" property="thesisProcess">
					<fr:layout name="tabular">
						<fr:property name="classes" value="tstyle2 thlight mtop10" />
					</fr:layout>
				</fr:view>
			</td>
		</tr>
		<tr>
			<td>
				<ul class="operations" >
					<li>
						<html:link action="/phdThesisProcess.do?method=manageThesisDocuments" paramId="processId" paramName="process" paramProperty="thesisProcess.externalId">
							<bean:message bundle="PHD_RESOURCES" key="label.phd.manageThesisDocuments"/>
						</html:link>
					</li>
				</ul>
			</td>
	  	</tr>
	 </table>
	
	<br/>

</logic:equal>

</logic:notEmpty>
