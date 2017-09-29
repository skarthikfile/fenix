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
package org.fenixedu.academic.domain.candidacyProcess.degreeTransfer;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.fenixedu.academic.domain.Degree;
import org.fenixedu.academic.domain.candidacyProcess.IndividualCandidacySeriesGradeState;
import org.fenixedu.academic.domain.candidacyProcess.IndividualCandidacyState;

public class DegreeTransferIndividualCandidacyResultBean implements Serializable {

    private DegreeTransferIndividualCandidacyProcess candidacyProcess;
    private BigDecimal affinity;
    private Integer degreeNature;
    private BigDecimal approvedEctsRate;
    private BigDecimal gradeRate;
    private BigDecimal seriesCandidacyGrade;
    private IndividualCandidacyState state;
    private IndividualCandidacySeriesGradeState seriesGradeState;
    private Degree degree;
    private List<Degree> degrees;

    public DegreeTransferIndividualCandidacyResultBean(final DegreeTransferIndividualCandidacyProcess process) {
        setCandidacyProcess(process);
        setAffinity(process.getCandidacyAffinity());
        setDegreeNature(process.getCandidacyDegreeNature());
        setApprovedEctsRate(process.getCandidacyApprovedEctsRate());
        setGradeRate(process.getCandidacyGradeRate());
        setSeriesCandidacyGrade(process.getCandidacySeriesCandidacyGrade());
        if (process.isCandidacyAccepted() || process.isCandidacyRejected()) {
            setState(process.getCandidacyState());
        }
        List<Degree> d = new ArrayList<Degree>();
        d.add(process.getCandidacy().getSelectedDegree());
        setDegrees(d);
    }

    public DegreeTransferIndividualCandidacyResultBean(final DegreeTransferIndividualCandidacyProcess process, Degree degree) {
        setCandidacyProcess(process);
        DegreeTransferIndividualCandidacySeriesGrade seriesGradeForDegree =
                process.getCandidacy().getDegreeTransferIndividualCandidacySeriesGradeForDegree(degree);

        setAffinity(seriesGradeForDegree.getAffinity());
        setDegreeNature(seriesGradeForDegree.getDegreeNature());
        setApprovedEctsRate(seriesGradeForDegree.getApprovedEctsRate());
        setGradeRate(seriesGradeForDegree.getGradeRate());
        setSeriesCandidacyGrade(seriesGradeForDegree.getSeriesCandidacyGrade());

        if (process.isCandidacyAccepted() || process.isCandidacyRejected()) {
            setState(process.getCandidacyState());
        }
        List<Degree> d = new ArrayList<Degree>();
        d.add(degree);
        setDegrees(d);
        setDegree(degree);
        setSeriesGradeState(seriesGradeForDegree.getState());
    }

    public DegreeTransferIndividualCandidacyProcess getCandidacyProcess() {
        return this.candidacyProcess;
    }

    public void setCandidacyProcess(DegreeTransferIndividualCandidacyProcess candidacyProcess) {
        this.candidacyProcess = candidacyProcess;
    }

    public String getStudentNumber() {
        return getCandidacyProcess().getPersonalDetails().hasStudent() ? getCandidacyProcess().getPersonalDetails().getStudent()
                .getNumber().toString() : null;
    }

    public String getCandidacyPersonName() {
        return getCandidacyProcess().getPersonalDetails().getName();
    }

    public BigDecimal getAffinity() {
        return affinity;
    }

    public void setAffinity(BigDecimal affinity) {
        this.affinity = affinity;
    }

    public Integer getDegreeNature() {
        return degreeNature;
    }

    public void setDegreeNature(Integer degreeNature) {
        this.degreeNature = degreeNature;
    }

    public BigDecimal getApprovedEctsRate() {
        return approvedEctsRate;
    }

    public void setApprovedEctsRate(BigDecimal approvedEctsRate) {
        this.approvedEctsRate = approvedEctsRate;
    }

    public BigDecimal getGradeRate() {
        return gradeRate;
    }

    public void setGradeRate(BigDecimal gradeRate) {
        this.gradeRate = gradeRate;
    }

    public BigDecimal getSeriesCandidacyGrade() {
        return seriesCandidacyGrade;
    }

    public void setSeriesCandidacyGrade(BigDecimal seriesCandidacyGrade) {
        this.seriesCandidacyGrade = seriesCandidacyGrade;
    }

    public IndividualCandidacyState getState() {
        return state;
    }

    public void setState(IndividualCandidacyState state) {
        this.state = state;
    }

    public Degree getDegree() {
        return degree;
    }

    public void setDegree(Degree degree) {
        this.degree = degree;
    }

    public List<Degree> getDegrees() {
        return degrees;
    }

    public void setDegrees(List<Degree> degrees) {
        this.degrees = degrees;
    }

    public IndividualCandidacySeriesGradeState getSeriesGradeState() {
        return seriesGradeState;
    }

    public void setSeriesGradeState(IndividualCandidacySeriesGradeState seriesGradeState) {
        this.seriesGradeState = seriesGradeState;
    }
}
