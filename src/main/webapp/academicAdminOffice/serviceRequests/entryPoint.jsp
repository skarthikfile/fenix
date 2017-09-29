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

<h3 class="text-center"><bean:message key="label.academic.service.requests" bundle="ACADEMIC_OFFICE_RESOURCES" /></h3>

<div class="container">
	<div class="row text-center">

		<div class="col-lg-4">
			<html:link styleClass="btn btn-primary" page="/academicServiceRequestsManagement.do?method=search&academicSituationType=NEW"><bean:message key="new.requests" bundle="ACADEMIC_OFFICE_RESOURCES" /></html:link>
		</div>

		<div class="col-lg-4">
			<html:link styleClass="btn btn-info" page="/academicServiceRequestsManagement.do?method=search&academicSituationType=PROCESSING"><bean:message key="processing.requests" bundle="ACADEMIC_OFFICE_RESOURCES" /></html:link>
		</div>

		<div class="col-lg-4">
			<html:link styleClass="btn btn-success" page="/academicServiceRequestsManagement.do?method=search&academicSituationType=CONCLUDED"><bean:message key="concluded.requests" bundle="ACADEMIC_OFFICE_RESOURCES" /></html:link>
		</div>

	</div>
</div>