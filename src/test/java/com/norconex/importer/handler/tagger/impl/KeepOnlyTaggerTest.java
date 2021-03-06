/* Copyright 2010-2020 Norconex Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.norconex.importer.handler.tagger.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.InputStream;

import org.apache.commons.io.input.NullInputStream;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.norconex.commons.lang.map.Properties;
import com.norconex.commons.lang.text.TextMatcher.Method;
import com.norconex.commons.lang.xml.XML;
import com.norconex.importer.TestUtil;
import com.norconex.importer.handler.ImporterHandlerException;
import com.norconex.importer.parser.ParseState;

public class KeepOnlyTaggerTest {

    @Test
        public void testWriteRead() {
        KeepOnlyTagger tagger = new KeepOnlyTagger();
        tagger.getFieldMatcher().setPattern("field1|field2|field3");
        XML.assertWriteRead(tagger, "handler");
    }

    @Test
    public void testKeepAllFields() throws Exception {

        Properties metadata = new Properties();
        metadata.add("key1", "value1");
        metadata.add("key2", "value2");
        metadata.add("key3", "value3");

        // Should keep all keys
        KeepOnlyTagger tagger = new KeepOnlyTagger();
        tagger.getFieldMatcher()
                .setPattern("(key1|key2|key3)").setMethod(Method.REGEX);
        TestUtil.tag(tagger, "reference", metadata, ParseState.POST);

        assertEquals(3, metadata.size());
    }

    @Test
    public void testKeepSingleField() throws Exception {

        Properties metadata = new Properties();
        metadata.add("key1", "value1");
        metadata.add("key2", "value2");
        metadata.add("key3", "value3");

        // Should only keep key1
        KeepOnlyTagger tagger = new KeepOnlyTagger();
        tagger.getFieldMatcher().setPattern("key1");
        TestUtil.tag(tagger, "reference", metadata, ParseState.POST);

        assertEquals(1, metadata.size());
        assertTrue(metadata.containsKey("key1"));
    }

    @Test
    public void testDeleteAllMetadata() throws Exception {

        Properties metadata = new Properties();
        metadata.add("key1", "value1");
        metadata.add("key2", "value2");
        metadata.add("key3", "value3");

        // Because we are not adding any field to keep, all metadata should be
        // deleted
        KeepOnlyTagger tagger = new KeepOnlyTagger();
        tagger.getFieldMatcher().setPattern("IAmNotThere");
        TestUtil.tag(tagger, "reference", metadata, ParseState.POST);

        assertTrue(metadata.isEmpty());
    }

    @Test
    public void testKeepFieldsRegexViaXMLConfig()
            throws ImporterHandlerException {
        Properties meta = new Properties();
        meta.add("content-type", "blah");
        meta.add("x-access-level", "blah");
        meta.add("X-CONTENT-TYPE-OPTIONS", "blah");
        meta.add("X-FRAME-OPTIONS", "blah");
        meta.add("X-PARSED-BY", "blah");
        meta.add("date", "blah");
        meta.add("X-RATE-LIMIT-LIMIT", "blah");
        meta.add("source", "blah");

        KeepOnlyTagger tagger = new KeepOnlyTagger();

        tagger.loadFromXML(new XML(
                "<tagger><fieldMatcher method=\"regex\">"
              + "[Xx]-.*</fieldMatcher></tagger>"));

        InputStream is = new NullInputStream(0);
        TestUtil.tag(tagger, "blah", is, meta, ParseState.PRE);

        Assertions.assertEquals(5, meta.size(), "Invalid field count");
    }
}
