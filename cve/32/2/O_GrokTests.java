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




public class GrokTests extends OpenSearchTestCase {
    

    public void testCircularReference() {
        Exception e = expectThrows(IllegalArgumentException.class, () -> {
            Map<String, String> bank = new HashMap<>();
            bank.put("NAME", "!!!%{NAME}!!!");
            String pattern = "%{NAME}";
            new Grok(bank, pattern, false, logger::warn);
        });
        assertEquals("circular reference in pattern [NAME][!!!%{NAME}!!!]", e.getMessage());

        e = expectThrows(IllegalArgumentException.class, () -> {
            Map<String, String> bank = new HashMap<>();
            bank.put("NAME", "!!!%{NAME:name}!!!");
            String pattern = "%{NAME}";
            new Grok(bank, pattern, false, logger::warn);
        });
        assertEquals("circular reference in pattern [NAME][!!!%{NAME:name}!!!]", e.getMessage());

        e = expectThrows(IllegalArgumentException.class, () -> {
            Map<String, String> bank = new HashMap<>();
            bank.put("NAME", "!!!%{NAME:name:int}!!!");
            String pattern = "%{NAME}";
            new Grok(bank, pattern, false, logger::warn);
        });
        assertEquals("circular reference in pattern [NAME][!!!%{NAME:name:int}!!!]", e.getMessage());

        e = expectThrows(IllegalArgumentException.class, () -> {
            Map<String, String> bank = new TreeMap<>();
            bank.put("NAME1", "!!!%{NAME2}!!!");
            bank.put("NAME2", "!!!%{NAME1}!!!");
            String pattern = "%{NAME1}";
            new Grok(bank, pattern, false, logger::warn);
        });
        assertEquals("circular reference in pattern [NAME2][!!!%{NAME1}!!!] back to pattern [NAME1]", e.getMessage());

        e = expectThrows(IllegalArgumentException.class, () -> {
            Map<String, String> bank = new TreeMap<>();
            bank.put("NAME1", "!!!%{NAME2}!!!");
            bank.put("NAME2", "!!!%{NAME3}!!!");
            bank.put("NAME3", "!!!%{NAME1}!!!");
            String pattern = "%{NAME1}";
            new Grok(bank, pattern, false, logger::warn);
        });
        assertEquals("circular reference in pattern [NAME3][!!!%{NAME1}!!!] back to pattern [NAME1] via patterns [NAME2]",
            e.getMessage());

        e = expectThrows(IllegalArgumentException.class, () -> {
            Map<String, String> bank = new TreeMap<>();
            bank.put("NAME1", "!!!%{NAME2}!!!");
            bank.put("NAME2", "!!!%{NAME3}!!!");
            bank.put("NAME3", "!!!%{NAME4}!!!");
            bank.put("NAME4", "!!!%{NAME5}!!!");
            bank.put("NAME5", "!!!%{NAME1}!!!");
            String pattern = "%{NAME1}";
            new Grok(bank, pattern, false, logger::warn );
        });
        assertEquals("circular reference in pattern [NAME5][!!!%{NAME1}!!!] back to pattern [NAME1] " +
            "via patterns [NAME2=>NAME3=>NAME4]", e.getMessage());
    }

    
}
