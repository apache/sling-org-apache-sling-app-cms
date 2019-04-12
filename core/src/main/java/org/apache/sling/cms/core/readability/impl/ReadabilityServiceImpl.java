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

import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.sling.cms.readability.ReadabilityService;
import org.apache.sling.cms.readability.Sentence;
import org.apache.sling.cms.readability.Text;
import org.apache.sling.cms.readability.Word;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.metatype.annotations.Designate;

/**
 * Implementation of the ReadabilityService service
 */
@Component(service = ReadabilityService.class, configurationPolicy = ConfigurationPolicy.REQUIRE, immediate = true)
@Designate(ocd = ReadabilityConfig.class, factory = true)
public class ReadabilityServiceImpl implements ReadabilityService {

    private int complexityMin;
    private Pattern extraVowelExpression;
    private Pattern isWordExpression;
    private Locale locale;
    private Pattern vowelExpression;
    private List<String> wordstems;

    @Activate
    public void activate(ReadabilityConfig config) {
        complexityMin = config.complexityMin();
        extraVowelExpression = Pattern.compile(config.extraVowelExpression());
        isWordExpression = Pattern.compile(config.isWordExpression());
        locale = Locale.forLanguageTag(config.locale());
        vowelExpression = Pattern.compile(config.vowelExpression());
        wordstems = Arrays.asList(config.wordstems());
    }

    @Override
    public double calculateARI(String text) {
        return calculateARI(extractSentences(text));
    }

    @Override
    public double calculateARI(Text text) {
        int wordCount = text.getWordCount();
        return 4.71 * text.getCharacterCount() / wordCount + 0.5 * wordCount / text.getSentences().size() - 21.43;
    }

    @Override
    public double calculateAverageGradeLevel(String text) {
        return calculateAverageGradeLevel(extractSentences(text));
    }

    @Override
    public double calculateAverageGradeLevel(Text text) {
        double[] results = new double[] { this.calculateARI(text), this.calculateColemanLiauIndex(text),
                this.calculateFleschKincaidGradeLevel(text), this.calculateGunningFog(text), this.calculateSMOG(text) };

        double sum = 0.0;
        for (double result : results) {
            sum += result;
        }
        return sum / results.length;
    }

    @Override
    public double calculateColemanLiauIndex(String text) {
        return calculateColemanLiauIndex(extractSentences(text));
    }

    @Override
    public double calculateColemanLiauIndex(Text text) {
        int wordCount = text.getWordCount();
        return (5.89 * text.getCharacterCount() / wordCount) - (30 * text.getSentences().size() / (double) wordCount)
                - 15.8;
    }

    @Override
    public double calculateFleschKincaidGradeLevel(String text) {
        return calculateFleschKincaidGradeLevel(extractSentences(text));
    }

    @Override
    public double calculateFleschKincaidGradeLevel(Text text) {
        int wordCount = text.getWordCount();
        return 0.39 * (wordCount / (double) text.getSentences().size())
                + 11.8 * (text.getSyllableCount() / (double) wordCount) - 15.59;
    }

    @Override
    public double calculateFleschReadingEase(String text) {
        return calculateFleschReadingEase(extractSentences(text));
    }

    @Override
    public double calculateFleschReadingEase(Text text) {
        int wordCount = text.getWordCount();
        return 206.835 - 1.015 * wordCount / text.getSentences().size() - 84.6 * text.getSyllableCount() / wordCount;
    }

    @Override
    public double calculateGunningFog(String text) {
        return calculateGunningFog(extractSentences(text));
    }

    @Override
    public double calculateGunningFog(Text text) {
        int wordCount = text.getWordCount();
        return .4 * (((double) wordCount / text.getSentences().size())
                + 100 * ((double) text.getComplexWordCount() / wordCount));
    }

    @Override
    public double calculateSMOG(String text) {
        return calculateSMOG(extractSentences(text));
    }

    @Override
    public double calculateSMOG(Text text) {
        return 1.043 * Math.sqrt(text.getComplexWordCount() * (30.0 / text.getSentences().size())) + 3.1291;
    }

    private int countSylables(String word) {
        int result = 0;
        for (int index = 0; index < word.length(); index++) {
            if (isVowel(word.charAt(index))) {
                result += 1;
            }
        }
        if (extraVowelExpression.matcher(word).matches()) {
            result += 1;
        }
        return result;
    }

    private String deduplicateVowels(String word) {
        for (int index = 0; index < word.length() - 1; index++) {
            if (isVowel(word.charAt(index)) && isVowel(word.charAt(index + 1)) && index + 2 < word.length()) {
                word = word.substring(0, index + 1) + word.substring(index + 2);
            }
        }
        return word;
    }

    @Override
    public Text extractSentences(String input) {
        BreakIterator breakIterator = BreakIterator.getSentenceInstance(locale);
        breakIterator.setText(input);
        int start = breakIterator.first();
        List<String> sentenceStrs = new ArrayList<>();
        for (int end = breakIterator.next(); end != BreakIterator.DONE; start = end, end = breakIterator.next()) {
            sentenceStrs.add(input.substring(start, end));
        }
        return new Text(input,
                sentenceStrs.stream().map(s -> new Sentence(s, extractWords(s))).collect(Collectors.toList()));
    }

    private List<Word> extractWords(String sentence) {
        List<Word> words = new ArrayList<>();
        BreakIterator wordIterator = BreakIterator.getWordInstance(locale);
        wordIterator.setText(sentence);

        int start = wordIterator.first();
        for (int end = wordIterator.next(); end != BreakIterator.DONE; start = end, end = wordIterator.next()) {
            String original = sentence.substring(start, end);
            String wordStr = original.toLowerCase(locale);
            wordStr = deduplicateVowels(wordStr);
            wordStr = stripWordStem(wordStr);
            wordStr = wordStr.trim();
            int sylables = countSylables(wordStr);
            if (!wordStr.isEmpty() && (sylables != 0 || isWordExpression.matcher(wordStr).matches())) {
                if (sylables == 0) {
                    sylables = 1;
                }
                Word word = new Word(original, wordStr, sylables, sylables >= complexityMin);
                words.add(word);
            }
        }
        return words;
    }

    @Override
    public Locale getLocale() {
        return locale;
    }

    private boolean isVowel(char ch) {
        return vowelExpression.matcher(String.valueOf(ch)).matches();
    }

    private String stripWordStem(String wordStr) {
        for (String stem : wordstems) {
            if (wordStr.endsWith(stem)) {
                return wordStr.substring(0, wordStr.length() - stem.length());
            }
        }
        return wordStr;
    }

}
