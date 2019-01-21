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
package org.apache.sling.cms.readability;

import java.util.Locale;

/**
 * Service for calculating the readability of text
 */
public interface ReadabilityService {

    /**
     * Calculates Automated Readability Index for text
     * 
     * @param text the text string to analyze
     * @return the Automated Readability Index for text
     */
    double calculateARI(String text);

    /**
     * Calculates Automated Readability Index for text
     * 
     * @param text the analyzed text
     * @return the Automated Readability Index for text
     */
    double calculateARI(Text text);

    /**
     * Calculates the average grade level
     * 
     * @param text the text string to analyze
     * @return the average grade level for text
     */
    double calculateAverageGradeLevel(String text);

    /**
     * Calculates the average grade level
     * 
     * @param text the analyzed text
     * @return the average grade level for text
     */
    double calculateAverageGradeLevel(Text text);

    /**
     * Calculates Coleman-Liau Index for text
     * 
     * @param text the text string to analyze
     * @return the Coleman-Liau Index for text
     */
    double calculateColemanLiauIndex(String text);

    /**
     * Calculates Coleman-Liau Index for text
     * 
     * @param text the text string to analyze
     * @return the Coleman-Liau Index for text
     */
    double calculateColemanLiauIndex(Text text);

    /**
     * Calculates Flesch-Kincaid Readability for text
     * 
     * @param text the text string to analyze
     * @return Returns the Flesch-Kincaid Readability value for the text
     */
    double calculateFleschKincaidGradeLevel(String text);

    /**
     * Calculates Flesch-Kincaid Readability for text
     * 
     * @param text the text string to analyze
     * @return the Flesch-Kincaid Readability value for the text
     */
    double calculateFleschKincaidGradeLevel(Text text);

    /**
     * Calculates Flesch-Kincaid Reading Ease for text
     * 
     * @param text the text string to analyze
     * @return the Flesch-Kincaid Reading Ease value for the text
     */
    double calculateFleschReadingEase(String text);

    /**
     * Calculates Flesch-Kincaid Reading Ease for text
     * 
     * @param text the text string to analyze
     * @return the Flesch-Kincaid Reading Ease value for the text
     */
    double calculateFleschReadingEase(Text text);

    /**
     * Calculates Gunning-Fog Index for text
     * 
     * @param text the text string to analyze
     * @return the Gunning-Fog Index for text
     */
    double calculateGunningFog(String text);

    /**
     * Calculates Gunning-Fog Index for text
     * 
     * @param text the text string to analyze
     * @return the Gunning-Fog Index for text
     */
    double calculateGunningFog(Text text);

    /**
     * Calculates Simple Measure of Gobbledygook Grade for text
     * 
     * @param text the text string to analyze
     * @return the SMOG value for the text
     */
    double calculateSMOG(String text);

    /**
     * Calculates Simple Measure of Gobbledygook Grade for text
     * 
     * @param text the text string to analyze
     * @return the SMOG value for the text
     */
    double calculateSMOG(Text text);

    /**
     * Extracts the sentences from the text including the words and various counts.
     * 
     * @param text the text to analyze
     * @return the analyzied text
     */
    Text extractSentences(String text);

    /**
     * Gets the locale for this configuration.
     * 
     * @return the locale
     */
    Locale getLocale();
}
