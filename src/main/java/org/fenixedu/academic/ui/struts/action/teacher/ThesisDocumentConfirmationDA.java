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
package org.fenixedu.academic.ui.struts.action.teacher;

import java.util.Collection;
import java.util.Comparator;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.Teacher;
import org.fenixedu.academic.domain.thesis.Thesis;
import org.fenixedu.academic.domain.thesis.ThesisParticipationType;
import org.fenixedu.academic.predicate.AccessControl;
import org.fenixedu.academic.service.services.exceptions.FenixServiceException;
import org.fenixedu.academic.service.services.thesis.ConfirmThesisDocumentSubmission;
import org.fenixedu.academic.ui.struts.action.commons.AbstractManageThesisDA;
import org.fenixedu.academic.ui.struts.action.coordinator.thesis.ThesisPresentationState;
import org.fenixedu.academic.ui.struts.action.teacher.TeacherApplication.TeacherFinalWorkApp;
import org.fenixedu.bennu.struts.annotations.Forward;
import org.fenixedu.bennu.struts.annotations.Forwards;
import org.fenixedu.bennu.struts.annotations.Mapping;
import org.fenixedu.bennu.struts.portal.EntryPoint;
import org.fenixedu.bennu.struts.portal.StrutsFunctionality;

import pt.ist.fenixframework.FenixFramework;

@StrutsFunctionality(app = TeacherFinalWorkApp.class, path = "document-confirmation",
        titleKey = "link.manage.thesis.document.confirmation")
@Mapping(module = "teacher", path = "/thesisDocumentConfirmation")
@Forwards({ @Forward(name = "viewThesis", path = "/teacher/viewThesis.jsp"),
        @Forward(name = "viewOperationsThesis", path = "/student/thesis/viewOperationsThesis.jsp"),
        @Forward(name = "showThesisList", path = "/teacher/showThesisList.jsp") })
public class ThesisDocumentConfirmationDA extends AbstractManageThesisDA {

    public static class ThesisPresentationWrapper {

        public static final Comparator<ThesisPresentationWrapper> COMPARATOR_BY_STUDENT =
                new Comparator<ThesisPresentationWrapper>() {
                    @Override
                    public int compare(ThesisPresentationWrapper t1, ThesisPresentationWrapper t2) {
                        return Thesis.COMPARATOR_BY_STUDENT.compare(t1.getThesis(), t2.getThesis());
                    }
                };

        protected final Thesis thesis;
        protected final ThesisPresentationState thesisPresentationState;

        public ThesisPresentationWrapper(final Thesis thesis) {
            this.thesis = thesis;
            this.thesisPresentationState = ThesisPresentationState.getThesisPresentationState(thesis);;
        }

        public Thesis getThesis() {
            return thesis;
        }

        public ThesisPresentationState getThesisPresentationState() {
            return thesisPresentationState;
        }

        public boolean isUnexisting() {
            return thesisPresentationState.isUnexisting();
        }

        public boolean isDraft() {
            return thesisPresentationState.isDraft();
        }

        public boolean isSubmitted() {
            return thesisPresentationState.isSubmitted();
        }

        public boolean isRejected() {
            return thesisPresentationState.isRejected();
        }

        public boolean isApproved() {
            return thesisPresentationState.isApproved();
        }

        public boolean isDocumentsSubmitted() {
            return thesisPresentationState.isDocumentsSubmitted();
        }

        public boolean isDocumentsConfirmed() {
            return thesisPresentationState.isDocumentsConfirmed();
        }

        public boolean isConfirmed() {
            return thesisPresentationState.isConfirmed();
        }

        public boolean isEvaluated1st() {
            return thesisPresentationState.isEvaluated1st();
        }

        public boolean isEvaluated() {
            return thesisPresentationState.isEvaluated();
        }

        public boolean isUnknown() {
            return thesisPresentationState.isUnknown();
        }

    }

    public static class ThesisPresentationWrapperSet extends TreeSet<ThesisPresentationWrapper> {
        public ThesisPresentationWrapperSet(final Collection<Thesis> theses) {
            super(ThesisPresentationWrapper.COMPARATOR_BY_STUDENT);
            for (final Thesis thesis : theses) {
                add(new ThesisPresentationWrapper(thesis));
            }
        }

        public ThesisPresentationWrapperSet(final Person person, final ThesisParticipationType thesisParticipationType) {
            this(Thesis.getThesesByParticipationType(person, thesisParticipationType));
        }
    }

    @EntryPoint
    public ActionForward showThesisList(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        final Person person = AccessControl.getPerson();
        if (person != null) {
            final ThesisPresentationWrapperSet orientedTheses =
                    new ThesisPresentationWrapperSet(person, ThesisParticipationType.ORIENTATOR);
            request.setAttribute("orientedTheses", orientedTheses);
            final ThesisPresentationWrapperSet coorientedTheses =
                    new ThesisPresentationWrapperSet(person, ThesisParticipationType.COORIENTATOR);
            request.setAttribute("coorientedTheses", coorientedTheses);
        }

        return mapping.findForward("showThesisList");
    }

    public ActionForward viewThesis(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        final String thesisIdString = request.getParameter("thesisID");

        final Thesis thesis = FenixFramework.getDomainObject(thesisIdString);
        request.setAttribute("thesis", thesis);

        final ThesisPresentationState thesisPresentationState = ThesisPresentationState.getThesisPresentationState(thesis);;
        request.setAttribute("thesisPresentationState", thesisPresentationState);

        return mapping.findForward("viewThesis");
    }

    public ActionForward showConfirmationPage(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        request.setAttribute("showConfirmationPage", Boolean.TRUE);
        return viewThesis(mapping, form, request, response);
    }

    public ActionForward confirmDocuments(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {
        final String thesisIdString = request.getParameter("thesisID");

        final Thesis thesis = FenixFramework.getDomainObject(thesisIdString);
        ConfirmThesisDocumentSubmission.run(thesis);

        request.setAttribute("documentsConfirmed", Boolean.TRUE);
        return viewThesis(mapping, form, request, response);
    }

    protected Teacher getLoggedTeacher() {
        final Person person = AccessControl.getPerson();
        if (person != null) {
            final Teacher teacher = person.getTeacher();
            return teacher;
        }
        return null;
    }

    @Override
    public ActionForward editProposal(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        throw new Error("this.cannot.be.called.here");
    }

}