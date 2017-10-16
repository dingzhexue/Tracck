package com.gpit.android.parser.email;

import android.util.Patterns;

import com.gpit.android.util.ArrayUtils;
import com.gpit.android.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by Pieter Bruegel on 5/31/16.
 */
public class EmailParser {

    private static final Pattern SIG_PATTERN = Pattern.compile("((^Sent from my (\\s*\\w+){1,3}$)|(^-\\w|^\\s?__|^\\s?--|^\u2013|^\u2014))", Pattern.DOTALL);
    private static final Pattern QUOTE_PATTERN = Pattern.compile("(^>+|^-+|^#+|^_+|^=+)", Pattern.DOTALL);
    private static final String[] QUOTE_HEADERS = {
            "^(On\\s(.{1,500})wrote:)",
            "From(\\s?:|\\s)[^\\n]+\\n?([^\\n]+\\n?){0,2}To(\\s?:|\\s)[^\\n]+\\n?([^\\n]+\\n?){0,2}Subject(\\s?:|\\s)[^\\n]+",
            "To(\\s?:|\\s)[^\\n]+\\n?([^\\n]+\\n?){0,2}From(\\s?:|\\s)[^\\n]+\\n?([^\\n]+\\n?){0,2}Subject(\\s?:|\\s)[^\\n]+",
            "(^From(\\s?:|\\s)[^\\n]+\\n?|^Date(\\s?:|\\s)[^\\n]+\\n?|^Subject(\\s?:|\\s)[^\\n]+\\n?|^To(\\s?:|\\s)[^\\n]+\\n?)",
//            "(^_{2}|^-{2})",
            "^Schedule a time (\\s*\\w+)(\\s?:\\s?|\\s)(\\s*\\w+)*$",
            "^Direct(\\s?:|\\s)\\s?\\d{3}(\\s|-|.)\\d{3}(\\s|-|.)\\d{4}(\\s?(x|ext)\\s?\\d{3,5})*$|Mobile(\\s?:|\\s)\\s?\\d{3}(\\s|-|.)\\d{3}(\\s|-|.)\\d{4}(\\s?(x|ext)\\s?\\d{3,5})*$",
            "LinkedIn$|^Twitter$",
            Patterns.DOMAIN_NAME.pattern() + "|" + Patterns.WEB_URL.pattern() + "|^" + Patterns.EMAIL_ADDRESS.pattern() + "$|^" + Patterns.PHONE.pattern() + "$", // domain, url anywhere, email and phone has to be alone in the line to be considered signature
            "^Fax(\\s?:|\\s)[\\d-\\s]+\\s?$|^(\\(?)\\d{3}(\\)?)(\\s|-|.)\\d{3}(\\s|-|.)\\sFax(\\s|\\n)?", // FAX number pattern
            "([^\\s]+(\\.(?i)(jpg|png|gif|bmp))$)", // Image File Name pattern
            "(Content-Type(\\s?:|\\s)[^\\n]+\\n?|Content-Transfer-Encoding(\\s?:|\\s)[^\\n]+\\n?)"
    };

    private static List<Pattern> mCompiledQuoteHeaderPatterns;

    private List<String> mQuoteHeadersRegex = new ArrayList<String>();
    private List<FragmentDTO> fragments = new ArrayList<FragmentDTO>();
    private int maxParagraphLines;
    private int maxNumCharsEachLine;

    /**
     * Initialize EmailParser
     */
    public EmailParser() {
        mCompiledQuoteHeaderPatterns = new ArrayList<Pattern>();
        for (String one : QUOTE_HEADERS) {
            mQuoteHeadersRegex.add(one);
        }
        maxParagraphLines = 6;
        maxNumCharsEachLine = 200;
    }

    /**
     * Splits the given email text into a list of {@link Fragment} and returns the {@link Email} object.
     *
     * @param emailText
     * @return
     */
    public Email parse(String emailText) {
        fragments = new ArrayList<FragmentDTO>();
        compileQuoteHeaderRegexes();

        // Normalize line endings
        emailText = emailText.replace("\r\n", "\n");

        FragmentDTO fragment = null;

        // Split body to multiple lines.
        String[] lines = new StringBuilder("\n" + emailText).toString().split("\n");

        /*
         * Reverse the array
         *
         * Reversing the array makes us to parse from the bottom to the top.
         * This way we can check for quote headers lines above quoted blocks
         */
        ArrayUtils.reverse(lines);

        /*
         * Paragraph for multi-line quote headers.
         * Some clients break up the quote headers into multiple lines.
         */
        List<String> paragraph = new ArrayList<String>();

        // Scans the given email line by line and figures out which fragment it belong to.
        for(String line : lines) {
            // Strip new line at the end of the string
            line = StringUtils.stripEnd(line, "\n").trim();
            // Strip empty spaces at the end of the string
            line = StringUtils.stripEnd(line, null);

            /*
             * If the fragment is not null and we hit the empty line,
             * we get the last line from the fragment and check if the last line is either
             * signature and quote headers.
             * If it is, add fragment to the list of fragments and delete the current fragment.
             * Also, delete the paragraph.
             */
            if(fragment != null && fragment.lines != null && fragment.lines.size() > 0 && line.isEmpty()) {
                String last = fragment.lines.get(fragment.lines.size() - 1);
                if(isSignature(last)) {
                    fragment.isSignature = true;
                } else if (isQuoteHeader(paragraph)) {
                    fragment.isQuoted = true;
                }
                addFragment(fragment);
                fragment = null;
                paragraph.clear();
            }

            // Check if the line is a quoted line.
            boolean isQuoted = isQuote(line);

            /*
             * If fragment is empty or if the line does not matches the current fragment,
             * create new fragment.
             */
            if(fragment == null || !isFragmentLine(fragment, line, isQuoted)) {
                if(fragment != null && fragment.lines.size() > 0) {
                    fragment.isQuoted = isQuoted;
                    addFragment(fragment);
                }

                fragment = new FragmentDTO();
                fragment.isQuoted = isQuoted;
                fragment.lines = new ArrayList<String>();
            }

            // Add line to fragment and paragraph
            if(line != null && !line.isEmpty()) {
                fragment.lines.add(line);
                paragraph.add(line);
            }
        }

        if(fragment != null && fragment.lines.size() > 0) {
            addFragment(fragment);
        }

        return createEmail(fragments);
    }

    /**
     * Returns existing quote headers regular expressions.
     *
     * @return
     */
    public List<String> getQuoteHeadersRegex() {
        return this.mQuoteHeadersRegex;
    }

    /**
     * Sets quote headers reaular expressions.
     *
     * @param newRegex
     */
    public void setQuoteHeadersRegex(List<String> newRegex) {
        this.mQuoteHeadersRegex = newRegex;
    }

    /**
     * Adds quote headers reaular expressions.
     *
     * @param newRegex
     */
    public void addQuoteHeadersRegex(String newRegex) {
        this.mQuoteHeadersRegex.add(newRegex);
    }

    /**
     * Gets max number of lines allowed for each paragraph when checking quote headers.
     *
     * @return
     */
    public int getMaxParagraphLines() {
        return this.maxParagraphLines;
    }

    /**
     * Sets max number of lines allowed for each paragraph when checking quote headers.
     *
     * @param maxParagraphLines
     */
    public void setMaxParagraphLines(int maxParagraphLines) {
        this.maxParagraphLines = maxParagraphLines;
    }

    /**
     * Gets max number of characters allowed for each line when checking quote headers.
     *
     * @return
     */
    public int getMaxNumCharsEachLine() {
        return maxNumCharsEachLine;
    }

    /**
     * Sets max number of characters allowed for each line when checking quote headers.
     *
     * @param maxNumCharsEachLine
     */
    public void setMaxNumCharsEachLine(int maxNumCharsEachLine) {
        this.maxNumCharsEachLine = maxNumCharsEachLine;
    }

    /**
     * Creates {@link Email} object from List of fragments
     *
     * @param fragmentDTOs
     * @return
     */
    protected Email createEmail(List<FragmentDTO> fragmentDTOs) {
        List<Fragment> fragments = new ArrayList<Fragment>();
        Collections.reverse(fragmentDTOs);
        boolean bContainsHidden = false;
        for(FragmentDTO f : fragmentDTOs) {
            Collections.reverse(f.lines);
            String content = new StringBuilder(StringUtils.join(f.lines, "\n")).toString();
            if(f.isHidden) bContainsHidden = true;
            Fragment fragment = new Fragment(content, f.isHidden, f.isSignature, f.isQuoted);
            fragments.add(fragment);
        }

        return new Email(fragments, bContainsHidden);
    }

    /**
     * Compile all the quote headers regular expressions before the parsing.
     */
    private void compileQuoteHeaderRegexes() {
        for(String regex : mQuoteHeadersRegex) {
            mCompiledQuoteHeaderPatterns.add(Pattern.compile(regex, Pattern.MULTILINE | Pattern.DOTALL));
        }
    }

    /**
     * Check if the line is a signature.
     *
     * @param line
     * @return
     */
    private boolean isSignature(String line) {
        boolean bFind = SIG_PATTERN.matcher(line).find();
        return bFind;
    }

    /**
     * Checks if the line is quoted line.
     *
     * @param line
     * @return
     */
    private boolean isQuote(String line) {
        return QUOTE_PATTERN.matcher(line).find();
    }

    /**
     * Checks if lines in the fragment are empty.
     *
     * @param fragmentDTO
     * @return
     */
    private boolean isEmpty(FragmentDTO fragmentDTO) {
        String line = StringUtils.join(fragmentDTO.lines, "");
        if (line == null || line.isEmpty()) {
            return true;
        }

        return false;
    }

    /**
     * If the line matches the current fragment, return true.
     * Note that a common reply header also counts as part of the quoted Fragment.
     * even though it doesn't start with '>'.
     *
     * @param fragmentDTO
     * @param line
     * @param isQuoted
     * @return
     */
    private boolean isFragmentLine(FragmentDTO fragmentDTO, String line, boolean isQuoted) {
        return fragmentDTO.isQuoted == isQuoted || (fragmentDTO.isQuoted && (isQuoteHeader(Arrays.asList(line)) || line.isEmpty()));
    }

    /**
     * Add fragment to fragments list
     *
     * @param fragmentDTO
     */
    private void addFragment(FragmentDTO fragmentDTO) {
        if(fragmentDTO.isQuoted || fragmentDTO.isSignature || isEmpty(fragmentDTO)) {
            fragmentDTO.isHidden = true;
        }

        fragments.add(fragmentDTO);
    }

    /**
     * Checks if the given multiple-lines paragraph has one of the quote headers.
     * Returns false if it doesn't contain any of the quote headers,
     * if paragraph lines are greater than maxParagraphLines, or line has more than maxNumberCharsEachLine characters.
     *
     * @param paragraph
     * @return
     */
    private boolean isQuoteHeader(List<String> paragraph) {
        if(paragraph.size() > maxParagraphLines) {
            return false;
        }

        for (String line : paragraph) {
            if(line.length() > maxNumCharsEachLine) {
                return false;
            }
        }

        Collections.reverse(paragraph);
        String content = new StringBuilder(StringUtils.join(paragraph, "\n")).toString();
        for(Pattern pattern : mCompiledQuoteHeaderPatterns) {
            if(pattern.matcher(content).find()) {
                return true;
            }
        }

        return false;
    }
}
