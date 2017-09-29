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
package org.fenixedu.academic.servlet.taglib;

import java.util.HashMap;
import java.util.List;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.struts.taglib.TagUtils;
import org.fenixedu.academic.domain.phd.PhdIndividualProgramProcess;
import org.fenixedu.academic.domain.phd.email.PhdProgramEmailBean;
import org.fenixedu.academic.util.predicates.AndPredicate;
import org.fenixedu.academic.util.predicates.PredicateContainer;

public class FilterProcessesForEmailTag extends TagSupport {

    static private final long serialVersionUID = 1L;
    static private final HashMap<String, Integer> ScopeIntsMap = new HashMap<String, Integer>() {
        {
            put("page", PageContext.PAGE_SCOPE);
            put("request", PageContext.REQUEST_SCOPE);
            put("session", PageContext.SESSION_SCOPE);
            put("application", PageContext.APPLICATION_SCOPE);
        }
    };

    protected String bean;

    protected String predicateContainer;
    protected String id;
    protected String scope = null;

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    public String getPredicateContainer() {
        return predicateContainer;
    }

    public void setPredicateContainer(String predicateContainer) {
        this.predicateContainer = predicateContainer;
    }

    public String getBean() {
        return bean;
    }

    public void setBean(String bean) {
        this.bean = bean;
    }

    @Override
    public int doEndTag() throws JspException {
        Object predicateContainerObject = TagUtils.getInstance().lookup(pageContext, getPredicateContainer(), getScope());
        if (predicateContainerObject == null) {
            throw new JspException("predicateContainer cannot be null");
        }
        if (!PredicateContainer.class.isAssignableFrom(predicateContainerObject.getClass())) {
            throw new JspException("Specified predicateContainer does not correspond to a "
                    + PredicateContainer.class.getSimpleName());
        }
        Object beanObject = TagUtils.getInstance().lookup(pageContext, getBean(), getScope());
        if (!PhdProgramEmailBean.class.isAssignableFrom(beanObject.getClass())) {
            throw new JspException("Specified bean does not correspond to a " + PhdProgramEmailBean.class.getSimpleName());
        }
        PredicateContainer<PhdIndividualProgramProcess> predicateContainer =
                (PredicateContainer<PhdIndividualProgramProcess>) predicateContainerObject;
        PhdProgramEmailBean bean = (PhdProgramEmailBean) beanObject;
        AndPredicate<PhdIndividualProgramProcess> searchPredicate = new AndPredicate<PhdIndividualProgramProcess>();
        searchPredicate.add(bean.getManagedPhdProgramsPredicate());
        searchPredicate.add(predicateContainer.getPredicate());

        List<PhdIndividualProgramProcess> processList = PhdIndividualProgramProcess.search(searchPredicate);

        int scope = (getScope() == null) ? PageContext.REQUEST_SCOPE : ScopeIntsMap.get(getScope());
        pageContext.setAttribute(id, processList, scope);
        return super.doEndTag();
    }
}
