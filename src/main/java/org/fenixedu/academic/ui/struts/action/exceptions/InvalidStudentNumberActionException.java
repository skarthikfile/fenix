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
package org.fenixedu.academic.ui.struts.action.exceptions;

/**
 * 
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 * @author Joana Mota (jccm@rnl.ist.utl.pt)
 */
public class InvalidStudentNumberActionException extends FenixActionException {

    public static String key = "error.exception.invalidStudentNumber";

    public InvalidStudentNumberActionException(Throwable cause) {
        super(key, cause);
    }

    public InvalidStudentNumberActionException(Object value, Throwable cause) {
        super(key, value, cause);
    }

    public InvalidStudentNumberActionException(String key, Throwable cause) {
        super(key, cause);
    }

    public InvalidStudentNumberActionException(Object[] values, Throwable cause) {
        super(key, values, cause);
    }

    /**
     * @return String
     */
    public static String getKey() {
        return key;
    }

    /**
     * Sets the key.
     * 
     * @param key
     *            The key to set
     */
    public static void setKey(String key) {
        InvalidStudentNumberActionException.key = key;
    }

    @Override
    public String toString() {
        String result = "[InvalidStudentNumberActionException\n";
        result += "property" + this.getProperty() + "\n";
        result += "error" + this.getError() + "\n";
        result += "cause" + this.getCause() + "\n";
        result += "]";
        return result;
    }
    // TODO find a way of internationalizing the message passed as argument to
    // the exception error message of the resource bundle
}