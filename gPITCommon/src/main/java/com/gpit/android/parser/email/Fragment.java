package com.gpit.android.parser.email;

/**
 * Created by Pieter Bruegel on 5/31/16.
 */
public class Fragment {

    private String content;
    private boolean isHidden;
    private boolean isSignature;
    private boolean isQuoted;

    public Fragment(String content, boolean isHidden, boolean isSignature, boolean isQuoted) {
        this.content = content;
        this.isHidden = isHidden;
        this.isSignature = isSignature;
        this.isQuoted = isQuoted;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isHidden() {
        return isHidden;
    }

    public void setHidden(boolean hidden) {
        isHidden = hidden;
    }

    public boolean isSignature() {
        return isSignature;
    }

    public void setSignature(boolean signature) {
        isSignature = signature;
    }

    public boolean isQuoted() {
        return isQuoted;
    }

    public void setQuoted(boolean quoted) {
        isQuoted = quoted;
    }

    public boolean isEmpty() {
        return "".equals(this.content.replace("\n", ""));
    }
}
