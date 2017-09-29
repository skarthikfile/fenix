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
package org.fenixedu.academic.dto.projectSubmission;

import java.io.InputStream;
import java.io.Serializable;

import org.fenixedu.academic.domain.Attends;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.Project;
import org.fenixedu.academic.domain.StudentGroup;
import org.fenixedu.commons.StringNormalizer;

public class CreateProjectSubmissionBean implements Serializable {

    private StudentGroup studentGroupReference;

    private Attends attendsReference;

    private Project projectReference;

    private Person personReference;

    private transient InputStream inputStream;

    private String filename;

    public StudentGroup getStudentGroup() {
        return this.studentGroupReference;

    }

    public void setStudentGroup(StudentGroup studentGroup) {
        this.studentGroupReference = studentGroup;
    }

    public Attends getAttends() {
        return this.attendsReference;

    }

    public void setAttends(Attends attends) {
        this.attendsReference = attends;

    }

    public Project getProject() {
        return this.projectReference;
    }

    public void setProject(Project project) {
        this.projectReference = project;
    }

    public Person getPerson() {
        return this.personReference;
    }

    public void setPerson(Person person) {
        this.personReference = person;
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    public void setInputStream(InputStream fileInputStream) {
        this.inputStream = fileInputStream;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = StringNormalizer.normalize(filename);
    }

}
