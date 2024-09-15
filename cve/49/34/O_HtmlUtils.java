/*
 * Copyright (C) Red Gate Software Ltd 2010-2022
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.flywaydb.core.internal.util;

import org.apache.commons.text.StringEscapeUtils;
import org.flywaydb.core.api.configuration.Configuration;
import org.flywaydb.core.api.output.HtmlResult;
import org.flywaydb.core.api.FlywayException;
import org.flywaydb.core.api.output.CompositeResult;

import java.io.FileWriter;

import static org.flywaydb.core.internal.reports.html.HtmlReportGenerator.generateHtml;

public class HtmlUtils {
    public static String toHtmlFile(String filename, CompositeResult<HtmlResult> results, Configuration config) {
        String fileContents = generateHtml(results, config);

        try (FileWriter fileWriter = new FileWriter(filename)) {
            fileWriter.write(fileContents);
        } catch (Exception e) {
            throw new FlywayException("Unable to write HTML to file: " + e.getMessage());
        }
        return filename;
    }

    public static String htmlEncode(String input) {
        return StringEscapeUtils.escapeHtml4(input);
    }
}