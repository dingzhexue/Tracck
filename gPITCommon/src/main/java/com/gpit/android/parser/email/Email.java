package com.gpit.android.parser.email;

import com.gpit.android.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Pieter Bruegel on 5/31/16.
 */
public class Email {

    private List<Fragment> fragments = new ArrayList<Fragment>();
    private boolean bContainsHidden = false;

    public Email(List<Fragment> fragments, boolean bContainsHidden) {
        this.fragments = fragments;
        this.bContainsHidden = bContainsHidden;
    }

    public List<Fragment> getFragments() {
        return this.fragments;
    }

    public boolean isContainsHidden() {
        return bContainsHidden;
    }

    public String getVisibleText() {
        List<String> visibleFragments = new ArrayList<String>();
        for(Fragment fragment : fragments) {
            if(!fragment.isHidden()) {
                visibleFragments.add(fragment.getContent());
            }
        }

        return StringUtils.stripEnd(StringUtils.join(visibleFragments, "\n"), null);
    }

    public String getHiddenText() {
        List<String> hiddenFragments = new ArrayList<String>();
        for(Fragment fragment : fragments) {
            if(fragment.isHidden()) {
                hiddenFragments.add(fragment.getContent());
            }
        }

        return StringUtils.stripEnd(StringUtils.join(hiddenFragments, "\n"), null);
    }
}
