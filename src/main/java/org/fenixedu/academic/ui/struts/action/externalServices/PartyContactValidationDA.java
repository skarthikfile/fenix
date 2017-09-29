/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Academic.
 *
 * FenixEdu Academic is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Academic is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.fenixedu.academic.ui.struts.action.externalServices;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.academic.domain.contacts.EmailValidation;
import org.fenixedu.academic.ui.struts.action.base.FenixDispatchAction;
import org.fenixedu.bennu.core.util.CoreConfiguration;
import org.fenixedu.bennu.struts.annotations.Forward;
import org.fenixedu.bennu.struts.annotations.Forwards;
import org.fenixedu.bennu.struts.annotations.Mapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.ist.fenixframework.FenixFramework;

@Mapping(module = "external", path = "/partyContactValidation", scope = "request", parameter = "method")
@Forwards({ @Forward(name = "emailValidation", path = "/publico/emailValidation.jsp"),
        @Forward(name = "phoneValidation", path = "/publico/phoneValidation.jsp") })
public class PartyContactValidationDA extends FenixDispatchAction {

    private static final Logger logger = LoggerFactory.getLogger(PartyContactValidationDA.class);

    public ActionForward validate(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {

        final String validationOID = request.getParameter("validationOID");
        final String token = request.getParameter("token");

        String state = "N/A";
        String tries = "N/A";

        if (!StringUtils.isEmpty(validationOID) && !StringUtils.isEmpty(token)) {
            EmailValidation emailValidation = FenixFramework.getDomainObject(validationOID);
            if (emailValidation != null) {
                emailValidation.processValidation(token);
                state = emailValidation.getState().toString();
                tries = emailValidation.getAvailableTries().toString();
            }
        }
        request.setAttribute("validationOID", validationOID);
        request.setAttribute("token", token);
        request.setAttribute("state", state);
        request.setAttribute("tries", tries);
        return mapping.findForward("emailValidation");
    }

    public ActionForward validatePhone(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        final String code = request.getParameter("code");
        final String lang = request.getParameter("lang");
        String xml = "<Response>";
        final String host = CoreConfiguration.getConfiguration().applicationUrl();
        for (int i = 0; i < 3; i++) {
            xml += String.format("<Play>%s/external/partyContactValidation/%s/start.mp3</Play>", host, lang);
            final String[] split = code.split("");
            for (String digit : split) {
                if (!StringUtils.isEmpty(digit)) {
                    xml += String.format("<Play>%s/external/partyContactValidation/%s/%s.mp3</Play>", host, lang, digit);
                }
            }
            xml += String.format("<Play>%s/external/partyContactValidation/%s/end.mp3</Play>", host, lang);
        }
        xml += "</Response>";
        logger.debug(xml);
        request.setAttribute("xml", xml);
        return mapping.findForward("phoneValidation");
    }
}
