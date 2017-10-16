package com.gpit.android.parser.email;

/**
 * Created by Pieter Bruegel on 5/31/16.
 */
public class EmailReplyParser {

    public static Email read(String emailText) {
        if (emailText == null) {
            emailText = "";
        }

        EmailParser parser = new EmailParser();
        return parser.parse(emailText);
    }

    public static String parseReply(String emailText) {
        return read(emailText).getVisibleText();
    }
}
