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
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/taglib/collection-pager" prefix="cp"%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c"%>

<fr:view name="occupationPeriod" property="startYearMonthDay"/>
-
<fr:view name="occupationPeriod" property="endYearMonthDay"/>

<logic:present name="occupationPeriod" property="nextPeriod">
	<br/>
	<bean:define id="occupationPeriod" name="occupationPeriod" property="nextPeriod" toScope="request"/>
	<jsp:include page="viewOccupationPeriod.jsp"/>
</logic:present>
