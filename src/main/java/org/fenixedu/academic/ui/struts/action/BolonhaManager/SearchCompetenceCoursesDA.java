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
package org.fenixedu.academic.ui.struts.action.BolonhaManager;

import java.io.Serializable;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.academic.domain.CompetenceCourse;
import org.fenixedu.academic.ui.struts.action.BolonhaManager.BolonhaManagerApplication.CompetenceCourseManagementApp;
import org.fenixedu.academic.ui.struts.action.base.FenixDispatchAction;
import org.fenixedu.bennu.struts.annotations.Forward;
import org.fenixedu.bennu.struts.annotations.Forwards;
import org.fenixedu.bennu.struts.annotations.Mapping;
import org.fenixedu.bennu.struts.portal.EntryPoint;
import org.fenixedu.bennu.struts.portal.StrutsFunctionality;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;

@StrutsFunctionality(app = CompetenceCourseManagementApp.class, path = "search", titleKey = "navigation.search.competenceCourses")
@Mapping(module = "bolonhaManager", path = "/competenceCourses/searchCompetenceCourses")
@Forwards({ @Forward(name = "searchCompetenceCourses", path = "/bolonhaManager/competenceCourses/searchCompetenceCourses.jsp") })
public class SearchCompetenceCoursesDA extends FenixDispatchAction {

    public static class SearchCompetenceCourseBean implements Serializable {
        private static final long serialVersionUID = 1L;

        private String searchName = "";
        private String searchCode = "";

        public SearchCompetenceCourseBean() {
        }

        public SearchCompetenceCourseBean(String searchName, String searchCode) {
            setSearchName(searchName);
            setSearchCode(searchCode);
        }

        public String getSearchName() {
            return searchName;
        }

        public void setSearchName(String searchName) {
            this.searchName = searchName;
        }

        public void setSearchCode(String searchCode) {
            this.searchCode = searchCode;
        }

        public String getSearchCode() {
            return searchCode;
        }
    }

    @EntryPoint
    public ActionForward search(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        SearchCompetenceCourseBean searchBean = getOrCreateSearchBean(request);
        String searchName = searchBean.getSearchName();
        String searchCode = searchBean.getSearchCode();
        if ((!searchName.isEmpty()) || (!searchCode.isEmpty())) {
            request.setAttribute("searchResults", search(searchName, searchCode));
        }
        request.setAttribute("searchBean", searchBean);
        return mapping.findForward("searchCompetenceCourses");
    }

    public ActionForward sortSearch(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        String searchName = (String) getFromRequest(request, "searchName");
        String searchCode = (String) getFromRequest(request, "searchCode");
        if ((!searchName.isEmpty()) || (!searchCode.isEmpty())) {
            request.setAttribute("searchResults", search(searchName, searchCode));
        }

        request.setAttribute("sortBy", getFromRequest(request, "sortBy"));
        request.setAttribute("searchBean", new SearchCompetenceCourseBean(searchName, searchCode));
        return mapping.findForward("searchCompetenceCourses");
    }

    private SearchCompetenceCourseBean getOrCreateSearchBean(HttpServletRequest request) {
        SearchCompetenceCourseBean searchBean = getRenderedObject("searchBean");
        RenderUtils.invalidateViewState();
        if (searchBean == null) {
            searchBean = new SearchCompetenceCourseBean();
        }
        return searchBean;
    }

    private static Collection<CompetenceCourse> search(String searchName, String searchCode) {
        return CompetenceCourse.searchBolonhaCompetenceCourses(searchName, searchCode);
    }
}