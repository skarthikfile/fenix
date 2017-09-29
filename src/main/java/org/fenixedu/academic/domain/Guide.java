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
package org.fenixedu.academic.domain;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.beanutils.BeanComparator;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.util.Bundle;
import org.fenixedu.academic.util.State;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.i18n.BundleUtil;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt) Joana Mota (jccm@rnl.ist.utl.pt)
 */

public class Guide extends Guide_Base {

    public static Comparator<Guide> COMPARATOR_BY_VERSION = new Comparator<Guide>() {
        @Override
        public int compare(Guide leftGuide, Guide rightGuide) {
            int comparationResult = leftGuide.getVersion().compareTo(rightGuide.getVersion());
            return (comparationResult == 0) ? leftGuide.getExternalId().compareTo(rightGuide.getExternalId()) : comparationResult;
        }
    };

    public Guide() {
        super();
        setRootDomainObject(Bennu.getInstance());
    }

    public void delete() {
        DomainException.throwWhenDeleteBlocked(getDeletionBlockers());
        setRootDomainObject(null);
        deleteDomainObject();
    }

    @Override
    protected void checkForDeletionBlockers(Collection<String> blockers) {
        super.checkForDeletionBlockers(blockers);
        if (!getGuideEntriesSet().isEmpty() || !getGuideSituationsSet().isEmpty() || (getVersion() == 1)) {
            blockers.add(BundleUtil.getString(Bundle.APPLICATION, "guide.cannot.be.deleted"));
        }
    }

    public final static Comparator<Guide> yearAndNumberComparator = new Comparator<Guide>() {
        @Override
        public int compare(Guide g1, Guide g2) {
            Integer yearComparation = g1.getYear().compareTo(g2.getYear());
            if (yearComparation == 0) {
                return g1.getNumber().compareTo(g2.getNumber());
            }
            return yearComparation;
        }
    };

    public GuideSituation getActiveSituation() {
        if (this.getGuideSituationsSet() != null) {
            Iterator iterator = this.getGuideSituationsSet().iterator();
            while (iterator.hasNext()) {
                GuideSituation guideSituation = (GuideSituation) iterator.next();
                if (guideSituation.getState().getState().equals(State.ACTIVE)) {
                    return guideSituation;
                }
            }
        }
        return null;
    }

    public void updateTotalValue() {

        BigDecimal total = BigDecimal.ZERO;

        for (final GuideEntry guideEntry : getGuideEntriesSet()) {
            total = total.add(guideEntry.getPriceBigDecimal().multiply(BigDecimal.valueOf(guideEntry.getQuantity())));
        }

        total.setScale(2, RoundingMode.HALF_EVEN);

        setTotalBigDecimal(total);

    }

    public GuideEntry getEntry(GraduationType graduationType, DocumentType documentType, String description) {
        for (GuideEntry entry : getGuideEntriesSet()) {
            if (graduationType == null || !graduationType.equals(entry.getGraduationType())) {
                continue;
            }

            if (documentType == null || !documentType.equals(entry.getDocumentType())) {
                continue;
            }

            if (description == null || !description.equals(entry.getDescription())) {
                continue;
            }

            return entry;
        }

        return null;
    }

    public static Integer generateGuideNumber() {
        return Collections.max(Bennu.getInstance().getGuidesSet(), Guide.yearAndNumberComparator).getNumber() + 1;

    }

    public static Guide readByNumberAndYearAndVersion(Integer number, Integer year, Integer version) {
        for (Guide guide : Bennu.getInstance().getGuidesSet()) {
            if (guide.getNumber().equals(number) && guide.getYear().equals(year) && guide.getVersion().equals(version)) {
                return guide;
            }
        }
        return null;
    }

    static public Guide readLastVersionByNumberAndYear(Integer number, Integer year) {
        Set<Guide> result = new HashSet<Guide>();
        for (Guide guide : Bennu.getInstance().getGuidesSet()) {
            if (guide.getYear().equals(year) && guide.getNumber().equals(number)) {
                result.add(guide);
            }
        }

        if (result.isEmpty()) {
            return null;
        }

        return Collections.max(result, Guide.COMPARATOR_BY_VERSION);
    }

    public static List<Guide> readByNumberAndYear(Integer number, Integer year) {
        List<Guide> guides = new ArrayList<Guide>();
        for (Guide guide : Bennu.getInstance().getGuidesSet()) {
            if (guide.getYear().equals(year) && guide.getNumber().equals(number)) {
                guides.add(guide);
            }
        }
        Collections.sort(guides, new BeanComparator("version"));
        return guides;
    }

    public static List<Guide> readByYear(Integer year) {
        List<Guide> guides = new ArrayList<Guide>();
        for (Guide guide : Bennu.getInstance().getGuidesSet()) {
            if (guide.getYear().equals(year)) {
                guides.add(guide);
            }
        }
        return guides;
    }

    public static List<Guide> readByYearAndState(Integer guideYear, GuideState situationOfGuide) {

        List<Guide> result = new ArrayList<Guide>();
        for (Guide guide : Bennu.getInstance().getGuidesSet()) {
            GuideSituation activeSituation = guide.getActiveSituation();
            if (activeSituation != null && activeSituation.getSituation().equals(situationOfGuide)) {
                if (guideYear == null || (guideYear != null && guide.getYear().equals(guideYear))) {
                    result.add(guide);
                }
            }
        }
        return result;

    }

    @Deprecated
    public void setTotal(Double total) {
        if (total != null) {
            setTotalBigDecimal(BigDecimal.valueOf(total));
        } else {
            setTotalBigDecimal(null);
        }
    }

    @Deprecated
    public Double getTotal() {
        return getTotalBigDecimal().doubleValue();
    }

    @Deprecated
    public java.util.Date getCreationDate() {
        org.joda.time.YearMonthDay ymd = getCreationDateYearMonthDay();
        return (ymd == null) ? null : new java.util.Date(ymd.getYear() - 1900, ymd.getMonthOfYear() - 1, ymd.getDayOfMonth());
    }

    @Deprecated
    public void setCreationDate(java.util.Date date) {
        if (date == null) {
            setCreationDateYearMonthDay(null);
        } else {
            setCreationDateYearMonthDay(org.joda.time.YearMonthDay.fromDateFields(date));
        }
    }

    @Deprecated
    public java.util.Date getPaymentDate() {
        org.joda.time.YearMonthDay ymd = getPaymentDateYearMonthDay();
        return (ymd == null) ? null : new java.util.Date(ymd.getYear() - 1900, ymd.getMonthOfYear() - 1, ymd.getDayOfMonth());
    }

    @Deprecated
    public void setPaymentDate(java.util.Date date) {
        if (date == null) {
            setPaymentDateYearMonthDay(null);
        } else {
            setPaymentDateYearMonthDay(org.joda.time.YearMonthDay.fromDateFields(date));
        }
    }

}
