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

<bean:define id="thesisId" name="thesis" property="externalId"/>
<bean:define id="degreeId" name="degreeId"/>
<bean:define id="executionYearId" name="executionYearId"/>

<html:xhtml/>

<em><bean:message key="scientificCouncil.thesis.process" /></em>
<h2><bean:message key="title.scientificCouncil.thesis.evaluate"/></h2>

<ul>
	<logic:notEmpty name="degreeId"><logic:notEmpty name="executionYearId">
    <li>
		<bean:define id="url">/scientificCouncilManageThesis.do?method=listScientificComission&amp;degreeId=<bean:write name="degreeId"/>&amp;executionYearId=<bean:write name="executionYearId"/></bean:define>
		<html:link page="<%= url %>">
			<bean:message key="link.list.scientific.comission"/>
		</html:link>
    </li>
    </logic:notEmpty></logic:notEmpty>
    <li>
        <html:link page="/scientificCouncilManageThesis.do?method=listThesis">
            <bean:message key="link.scientificCouncil.thesis.list.back"/>
        </html:link>
    </li>
    <logic:equal name="thesis" property="confirmed" value="true">
        <li>
            <html:link page="<%= String.format("/scientificCouncilManageThesis.do?method=confirmApprove&amp;thesisID=%s&amp;degreeID=%s&amp;executionYearID=%s", thesisId, degreeId, executionYearId) %>">
                <bean:message key="title.scientificCouncil.thesis.evaluation.approve"/>
            </html:link>
        </li>
    </logic:equal>
</ul>

<%-- Approve proposal --%>
<logic:present name="confirmApprove">
    <div class="warning0" style="padding: 1em;">
        <strong><bean:message key="label.attention" bundle="APPLICATION_RESOURCES"/>:</strong><br/>
        <bean:message key="label.scientificCouncil.thesis.evaluation.approve.confirm"/>
        <div class="mtop1 forminline">
        <fr:form action="<%= String.format("/scientificCouncilManageThesis.do?method=approveThesis&amp;&amp;thesisID=%s&amp;degreeID=%s&amp;executionYearID=%s", thesisId, degreeId, executionYearId) %>">
                <html:submit>
                    <bean:message key="button.scientificCouncil.thesis.evaluation.approve"/>
                </html:submit>
            </fr:form>
        <fr:form action="<%= String.format("/scientificCouncilManageThesis.do?method=reviewThesis&amp;&amp;thesisID=%s&amp;degreeID=%s&amp;executionYearID=%s", thesisId, degreeId, executionYearId) %>">
                <html:cancel>
                    <bean:message key="button.cancel"/>
                </html:cancel>
            </fr:form>
        </div>
    </div>
</logic:present>

<%-- general process message --%>
<logic:notPresent name="confirmApprove">
	<div class="infoop2">
		<p class="mvert0"><bean:message key="message.scientificCouncil.thesis.confirmed.process"/></p>
	</div>
</logic:notPresent>

<logic:messagesPresent message="true" property="error">
    <html:messages id="message" message="true" property="error">
        <p><span class="error0"><bean:write name="message" filter="false"/></span></p>
    </html:messages>
</logic:messagesPresent>

<%-- Dissertation --%>
<h3 class="mtop15 mbottom05"><bean:message key="title.scientificCouncil.thesis.evaluation.details"/></h3>

<fr:view name="thesis" schema="thesis.jury.proposal.information">
    <fr:layout name="tabular">
    		<fr:property name="classes" value="tstyle2 thlight thright mtop05 mbottom05"/>
    		<fr:property name="columnClasses" value="width12em,,"/>
    </fr:layout>
</fr:view>

<h3 class="mtop15 mbottom05"><bean:message key="label.thesis.abstract"/></h3>

<logic:notEqual name="thesis" property="thesisAbstractInBothLanguages" value="true">
    <bean:message key="label.thesis.abstract.empty"/>
</logic:notEqual>

<logic:equal name="thesis" property="thesisAbstractInBothLanguages" value="true">
    <div style="border: 1px solid #ddd; background: #fafafa; padding: 0.5em; margin-bottom: 1em;">
        <fr:view name="thesis" property="thesisAbstract">
            <fr:layout>
                <fr:property name="language" value="pt"/>
                <fr:property name="showLanguageForced" value="true"/>
            </fr:layout>
        </fr:view>
    </div>
    
    <div style="border: 1px solid #ddd; background: #fafafa; padding: 0.5em; margin-bottom: 1em;">
        <fr:view name="thesis" property="thesisAbstract">
            <fr:layout>
                <fr:property name="language" value="en"/>
                <fr:property name="showLanguageForced" value="true"/>
            </fr:layout>
        </fr:view>
    </div>
</logic:equal>

<h3 class="mtop15 mbottom05"><bean:message key="label.thesis.keywords"/></h3>

<logic:notEqual name="thesis" property="keywordsInBothLanguages" value="true">
    <bean:message key="label.thesis.keywords.empty"/>
</logic:notEqual>

<logic:equal name="thesis" property="keywordsInBothLanguages" value="true">
    <p>
        <fr:view name="thesis" property="keywords">
            <fr:layout>
                <fr:property name="language" value="pt"/>
                <fr:property name="showLanguageForced" value="true"/>
            </fr:layout>
        </fr:view>
    </p>
    
    <p>
        <fr:view name="thesis" property="keywords">
            <fr:layout>
                <fr:property name="language" value="en"/>
                <fr:property name="showLanguageForced" value="true"/>
            </fr:layout>
        </fr:view>
    </p>
</logic:equal>

<h3 class="mtop15 mbottom05"><bean:message key="title.scientificCouncil.thesis.evaluation.extendedAbstract"/></h3>

<logic:empty name="thesis" property="extendedAbstract">
    <bean:message key="label.scientificCouncil.thesis.evaluation.noExtendedAbstract"/>
</logic:empty>

<logic:notEmpty name="thesis" property="extendedAbstract">
    <fr:view name="thesis" property="extendedAbstract" layout="values" schema="coordinator.thesis.file"/>
    (<fr:view name="thesis" property="extendedAbstract.size" layout="fileSize"/>)
</logic:notEmpty>

<h3 class="mtop15 mbottom05"><bean:message key="title.scientificCouncil.thesis.evaluation.dissertation"/></h3>

<logic:empty name="thesis" property="dissertation">
    <bean:message key="label.scientificCouncil.thesis.evaluation.noDissertation"/>
</logic:empty>

<logic:notEmpty name="thesis" property="dissertation">
    <fr:view name="thesis" property="dissertation" layout="values" schema="coordinator.thesis.file"/>
    (<fr:view name="thesis" property="dissertation.size" layout="fileSize"/>)
</logic:notEmpty>

<h3 class="mtop15 mbottom05"><bean:message key="title.scientificCouncil.thesis.evaluation.gradeAndDate"/></h3>

<fr:view name="thesis" schema="coordinator.thesis.revision.view">
    <fr:layout name="tabular">
    		<fr:property name="classes" value="tstyle2 thlight thright mtop05 mbottom05"/>
    		<fr:property name="columnClasses" value="width12em,,"/>
    </fr:layout>
</fr:view>

<%-- Orientation --%>
<h3 class="separator2 mtop2"><bean:message key="title.scientificCouncil.thesis.review.section.orientation"/></h3>

<logic:empty name="thesis" property="orientation">
        <p>
            <em><bean:message key="title.scientificCouncil.thesis.review.orientation.empty"/></em>
        </p>
</logic:empty>

<logic:notEmpty name="thesis" property="orientation">
    <h4 class="mtop2 mbottom05"><bean:message key="title.scientificCouncil.thesis.review.section.orientation.orientator"/></h4>

  <logic:iterate name="thesis" property="orientation" id="advisor">
    <fr:view name="advisor" layout="tabular" schema="thesis.jury.proposal.person">
      <fr:layout name="tabular">
        <fr:property name="classes" value="tstyle2 thlight thright mtop05 mbottom0"/>
        <fr:property name="columnClasses" value="width12em,width35em,"/>
      </fr:layout>
    </fr:view>
    <table class="tstyle2 thlight thright mtop0 mbottom05 tgluetop">
      <tr>
        <th class="width12em"><bean:message key="label.scientificCouncil.thesis.edit.teacher.credits"/>:</th>
        <td class="width35em">
          <logic:empty name="advisor" property="percentageDistribution">-</logic:empty>
          <logic:notEmpty name="advisor" property="percentageDistribution">
            <fr:view name="advisor" property="percentageDistribution"/> %
          </logic:notEmpty>
        </td>
      </tr>
    </table>
  </logic:iterate>
</logic:notEmpty>

<%-- Jury --%>
<h3 class="separator2 mtop2"><bean:message key="title.scientificCouncil.thesis.review.section.jury"/></h3>

<%-- Jury/President --%>
<h4 class="mtop2 mbottom05"><bean:message key="title.scientificCouncil.thesis.review.section.jury.president"/></h4>

<logic:empty name="thesis" property="president">
    <p>
        <bean:message key="title.scientificCouncil.thesis.review.president.empty"/>
    </p>
</logic:empty>

<logic:notEmpty name="thesis" property="president">
    <fr:view name="thesis" property="president" layout="tabular" schema="thesis.jury.proposal.person.loginInfo">
        <fr:layout name="tabular">
        		<fr:property name="classes" value="tstyle2 thlight thright mtop05 mbottom05"/>
        		<fr:property name="columnClasses" value="width12em,width35em,"/>
        </fr:layout>
    </fr:view>
</logic:notEmpty>

<%-- Jury/"Vowels" --%>
<h4 class="mtop2 mbottom05"><bean:message key="title.scientificCouncil.thesis.review.section.vowels"/></h4>

<logic:empty name="thesis" property="vowels">
    <p>
        <bean:message key="title.scientificCouncil.thesis.review.vowels.empty"/>
    </p>
</logic:empty>

<logic:notEmpty name="thesis" property="vowels">
    <logic:iterate id="vowel" name="thesis" property="vowels">
        <fr:view name="vowel" layout="tabular" schema="thesis.jury.proposal.person.loginInfo">
            <fr:layout name="tabular">
            		<fr:property name="classes" value="tstyle2 thlight thright mtop05 mbottom05"/>
            		<fr:property name="columnClasses" value="width12em,width35em,"/>
            </fr:layout>
        </fr:view>
    </logic:iterate>
</logic:notEmpty>
