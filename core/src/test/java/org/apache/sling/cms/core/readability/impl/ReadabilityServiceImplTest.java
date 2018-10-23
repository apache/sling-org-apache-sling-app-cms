/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.sling.cms.core.readability.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.annotation.Annotation;
import java.util.stream.Collectors;

import org.apache.sling.cms.readability.Text;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ReadabilityServiceImplTest {

    private static final Logger log = LoggerFactory.getLogger(ReadabilityServiceImplTest.class);
    private ReadabilityServiceImpl readabilityService;
    private String source1;
    private String source2;
    private String source3;

    private String getContents(String name) throws IOException {
        InputStream is = getClass().getClassLoader().getResourceAsStream(name);
        return new BufferedReader(new InputStreamReader(is)).lines().filter(l -> !l.startsWith("#"))
                .collect(Collectors.joining("\n"));
    }

    @Before
    public void init() throws IOException {
        this.readabilityService = new ReadabilityServiceImpl();
        this.readabilityService.activate(new ReadabilityConfig() {

            @Override
            public Class<? extends Annotation> annotationType() {
                return null;
            }

            @Override
            public int complexityMin() {
                return 3;
            }

            @Override
            public String extraVowelExpression() {
                return ".+y$";
            }

            @Override
            public String isWordExpression() {
                return "[a-zA-Z]*";
            }

            @Override
            public String locale() {
                return "en";
            }

            @Override
            public String vowelExpression() {
                return "a|e|i|o|u";
            }

            @Override
            public String[] wordstems() {
                return new String[] { "es", "ed", "ing", "e" };
            }

        });

        source1 = getContents("source1.txt");
        source2 = getContents("source2.txt");
        source3 = getContents("source3.txt");
    }

    private void logAll(Text text) {
        log.debug(text.toString());
        text.getSentences().forEach(s -> {
            log.trace(s.toString());
            s.getWords().forEach(w -> log.trace(w.toString()));
        });
    }

    @Test
    public void testARI() throws IOException {
        log.info("testARI");

        double ari = readabilityService.calculateARI(source1);

        log.debug("ARI {}", ari);

        assertTrue(ari > 16 && ari < 18);
       

        ari = readabilityService.calculateARI(source2);

        log.debug("ARI {}", ari);

        assertTrue(ari > 14 && ari < 15);

        ari = readabilityService.calculateARI(source3);

        log.debug("ARI {}", ari);

        assertTrue(ari > 6 && ari < 7);

        log.info("Tests successful!");
    }

    @Test
    public void testAverageGradeLevel() throws IOException {
        log.info("testAverageGradeLevel");

        double averageGradeLevel = readabilityService.calculateAverageGradeLevel(source1);

        log.debug("Average Grade Level {}", averageGradeLevel);

        assertTrue(averageGradeLevel > 15 && averageGradeLevel < 16);

        averageGradeLevel = readabilityService.calculateARI(source2);

        log.debug("Average Grade Level {}", averageGradeLevel);

        assertTrue(averageGradeLevel > 14 && averageGradeLevel < 15);

        averageGradeLevel = readabilityService.calculateARI(source3);

        log.debug("Average Grade Level {}", averageGradeLevel);

        assertTrue(averageGradeLevel > 6 && averageGradeLevel < 7);

        log.info("Tests successful!");
    }

    @Test
    public void testColemanLiau() throws IOException {
        log.info("testColemanLiau");

        double colemanLiauIndex = readabilityService.calculateColemanLiauIndex(source1);

        log.debug("ColemanLiau {}", colemanLiauIndex);

        assertTrue(colemanLiauIndex > 13 && colemanLiauIndex < 14);

        colemanLiauIndex = readabilityService.calculateColemanLiauIndex(source2);

        log.debug("ColemanLiau {}", colemanLiauIndex);

        assertTrue(colemanLiauIndex > 17 && colemanLiauIndex < 18);

        colemanLiauIndex = readabilityService.calculateColemanLiauIndex(source3);

        log.debug("ColemanLiau {}", colemanLiauIndex);

        assertTrue(colemanLiauIndex > 7 && colemanLiauIndex < 8);

        log.info("Tests successful!");
    }

    @Test
    public void testExtractSentences() throws IOException {
        log.info("testExtractSentences");

        Text text = readabilityService.extractSentences(source1);

        logAll(text);

        assertNotNull(text);
        assertEquals(3, text.getSentences().size());
        assertEquals(86, text.getWordCount());
        assertEquals(11, text.getComplexWordCount());

        text = readabilityService.extractSentences(source2);

        logAll(text);

        assertNotNull(text);
        assertEquals(12, text.getSentences().size());
        assertEquals(195, text.getWordCount());
        assertEquals(50, text.getComplexWordCount());

        text = readabilityService.extractSentences(source3);

        logAll(text);

        assertNotNull(text);
        assertEquals(11, text.getSentences().size());
        assertEquals(157, text.getWordCount());
        assertEquals(7, text.getComplexWordCount());

        log.info("Tests successful!");
    }

    @Test
    public void testFleschKincaidGradeLevel() throws IOException {
        log.info("testFleschKincaidGradeLevel");

        double fleschKincaidGradeLevel = readabilityService.calculateFleschKincaidGradeLevel(source1);

        log.debug("Flesch-Kincaid Grade Level {}", fleschKincaidGradeLevel);

        assertTrue(fleschKincaidGradeLevel > 14 && fleschKincaidGradeLevel < 15);

        fleschKincaidGradeLevel = readabilityService.calculateFleschKincaidGradeLevel(source2);

        log.debug("Flesch-Kincaid Grade Level {}", fleschKincaidGradeLevel);

        assertTrue(fleschKincaidGradeLevel > 12 && fleschKincaidGradeLevel < 13);

        fleschKincaidGradeLevel = readabilityService.calculateFleschKincaidGradeLevel(source3);

        log.debug("Flesch-Kincaid Grade Level {}", fleschKincaidGradeLevel);

        assertTrue(fleschKincaidGradeLevel > 5 && fleschKincaidGradeLevel < 6);

        log.info("Tests successful!");
    }

    @Test
    public void testFleschReadingEase() throws IOException {
        log.info("testFleschReadingEase");

        double fleschReadingEase = readabilityService.calculateFleschReadingEase(source1);

        log.debug("Flesch Reading Ease {}", fleschReadingEase);

        assertTrue(fleschReadingEase > 41 && fleschReadingEase < 42);

        fleschReadingEase = readabilityService.calculateFleschReadingEase(source2);

        log.debug("Flesch Reading Ease {}", fleschReadingEase);

        assertTrue(fleschReadingEase > 31 && fleschReadingEase < 32);

        fleschReadingEase = readabilityService.calculateFleschReadingEase(source3);

        log.debug("Flesch Reading Ease {}", fleschReadingEase);

        assertTrue(fleschReadingEase > 82 && fleschReadingEase < 83);

        log.info("Tests successful!");
    }

    @Test
    public void testGunningFog() throws IOException {
        log.info("testGunningFog");

        double gunningFog = readabilityService.calculateGunningFog(source1);

        log.debug("Gunning Fog {}", gunningFog);

        assertTrue(gunningFog > 15 && gunningFog < 17);

        gunningFog = readabilityService.calculateGunningFog(source2);

        log.debug("Gunning Fog {}", gunningFog);

        assertTrue(gunningFog > 15 && gunningFog < 17);

        gunningFog = readabilityService.calculateGunningFog(source3);

        log.debug("Gunning Fog {}", gunningFog);

        assertTrue(gunningFog > 7 && gunningFog < 9);

        log.info("Tests successful!");
    }

    @Test
    public void testSMOG() throws IOException {
        log.info("testSMOG");

        double smog = readabilityService.calculateSMOG(source1);

        log.debug("SMOG {}", smog);

        assertTrue(smog > 14 && smog < 15);

        smog = readabilityService.calculateSMOG(source2);

        log.debug("SMOG {}", smog);

        assertTrue(smog > 14 && smog < 15);

        smog = readabilityService.calculateSMOG(source3);

        log.debug("SMOG {}", smog);

        assertTrue(smog > 7 && smog < 8);

        log.info("Tests successful!");
    }

}
