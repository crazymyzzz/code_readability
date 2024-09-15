/*
 * SPDX-License-Identifier: Apache-2.0
 *
 * The OpenSearch Contributors require contributions made to
 * this file be licensed under the Apache-2.0 license or a
 * compatible open source license.
 */

/*
 * Licensed to Elasticsearch under one or more contributor
 * license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright
 * ownership. Elasticsearch licenses this file to you under
 * the Apache License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

/*
 * Modifications Copyright OpenSearch Contributors. See
 * GitHub history for details.
 */


public final class Grok {
    

    private Grok(Map<String, String> patternBank, String grokPattern, boolean namedCaptures, MatcherWatchdog matcherWatchdog,
                 Consumer<String> logCallBack) {
        this.patternBank = patternBank;
        this.namedCaptures = namedCaptures;
        this.matcherWatchdog = matcherWatchdog;

        for (Map.Entry<String, String> entry : patternBank.entrySet()) {
            String name = entry.getKey();
            String pattern = entry.getValue();
            forbidCircularReferences(name, new ArrayList<>(), pattern);
        }

        String expression = toRegex(grokPattern);
        byte[] expressionBytes = expression.getBytes(StandardCharsets.UTF_8);
        this.compiledExpression = new Regex(expressionBytes, 0, expressionBytes.length, Option.DEFAULT, UTF8Encoding.INSTANCE,
            message -> logCallBack.accept(message));

        List<GrokCaptureConfig> captureConfig = new ArrayList<>();
        for (Iterator<NameEntry> entry = compiledExpression.namedBackrefIterator(); entry.hasNext();) {
            captureConfig.add(new GrokCaptureConfig(entry.next()));
        }
        this.captureConfig = unmodifiableList(captureConfig);
    }

    /**
     * Checks whether patterns reference each other in a circular manner and if so fail with an exception
     *
     * In a pattern, anything between <code>%{</code> and <code>}</code> or <code>:</code> is considered
     * a reference to another named pattern. This method will navigate to all these named patterns and
     * check for a circular reference.
     */
    private void forbidCircularReferences(String patternName, List<String> path, String pattern) {
        if (pattern.contains("%{" + patternName + "}") || pattern.contains("%{" + patternName + ":")) {
            String message;
            if (path.isEmpty()) {
                message = "circular reference in pattern [" + patternName + "][" + pattern + "]";
            } else {
                message = "circular reference in pattern [" + path.remove(path.size() - 1) + "][" + pattern +
                    "] back to pattern [" + patternName + "]";
                // add rest of the path:
                if (path.isEmpty() == false) {
                    message += " via patterns [" + String.join("=>", path) + "]";
                }
            }
            throw new IllegalArgumentException(message);
        }

        for (int i = pattern.indexOf("%{"); i != -1; i = pattern.indexOf("%{", i + 1)) {
            int begin = i + 2;
            int brackedIndex = pattern.indexOf('}', begin);
            int columnIndex = pattern.indexOf(':', begin);
            int end;
            if (brackedIndex != -1 && columnIndex == -1) {
                end = brackedIndex;
            } else if (columnIndex != -1 && brackedIndex == -1) {
                end = columnIndex;
            } else if (brackedIndex != -1 && columnIndex != -1) {
                end = Math.min(brackedIndex, columnIndex);
            } else {
                throw new IllegalArgumentException("pattern [" + pattern + "] has circular references to other pattern definitions");
            }
            String otherPatternName = pattern.substring(begin, end);
            path.add(otherPatternName);
            forbidCircularReferences(patternName, path, patternBank.get(otherPatternName));
        }
    }

    
}

