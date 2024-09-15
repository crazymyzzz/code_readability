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

        validatePatternBank();

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
     * Entry point to recursively validate the pattern bank for circular dependencies and malformed URLs
     * via depth-first traversal. This implementation does not include memoization.
     */
    private void validatePatternBank() {
        for (String patternName : patternBank.keySet()) {
            validatePatternBank(patternName, new Stack<>());
        }
    }

    /**
     * Checks whether patterns reference each other in a circular manner and, if so, fail with an exception.
     * Also checks for malformed pattern definitions and fails with an exception.
     *
     * In a pattern, anything between <code>%{</code> and <code>}</code> or <code>:</code> is considered
     * a reference to another named pattern. This method will navigate to all these named patterns and
     * check for a circular reference.
     */
    private void validatePatternBank(String patternName, Stack<String> path) {
        String pattern = patternBank.get(patternName);
        boolean isSelfReference = pattern.contains("%{" + patternName + "}") ||
                                    pattern.contains("%{" + patternName + ":");
        if (isSelfReference) {
            throwExceptionForCircularReference(patternName, pattern);
        } else if (path.contains(patternName)) {
            // current pattern name is already in the path, fetch its predecessor
            String prevPatternName = path.pop();
            String prevPattern = patternBank.get(prevPatternName);
            throwExceptionForCircularReference(prevPatternName, prevPattern, patternName, path);
        }
        path.push(patternName);
        for (int i = pattern.indexOf("%{"); i != -1; i = pattern.indexOf("%{", i + 1)) {
            int begin = i + 2;
            int syntaxEndIndex = pattern.indexOf('}', begin);
            if (syntaxEndIndex == -1) {
                throw new IllegalArgumentException("Malformed pattern [" + patternName + "][" + pattern +"]");
            }
            int semanticNameIndex = pattern.indexOf(':', begin);
            int end = syntaxEndIndex;
            if (semanticNameIndex != -1) {
                end = Math.min(syntaxEndIndex, semanticNameIndex);
            }
            String dependsOnPattern = pattern.substring(begin, end);
            validatePatternBank(dependsOnPattern, path);
        }
        path.pop();
    }

    private static void throwExceptionForCircularReference(String patternName, String pattern) {
        throwExceptionForCircularReference(patternName, pattern, null, null);
    }

    private static void throwExceptionForCircularReference(String patternName, String pattern, String originPatterName,
                                                           Stack<String> path) {
        StringBuilder message = new StringBuilder("circular reference in pattern [");
        message.append(patternName).append("][").append(pattern).append("]");
        if (originPatterName != null) {
            message.append(" back to pattern [").append(originPatterName).append("]");
        }
        if (path != null && path.size() > 1) {
            message.append(" via patterns [").append(String.join("=>", path)).append("]");
        }
        throw new IllegalArgumentException(message.toString());
    }

    

}

