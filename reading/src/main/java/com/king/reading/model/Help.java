package com.king.reading.model;

import com.king.reading.model.ExpandNestedList;

import java.util.List;

/**
 * Created by AllynYonge on 22/06/2017.
 */

public class Help extends ExpandNestedList<String> {
    public String question;

    public Help(boolean isExpand, String question, List<String> answers) {
        super(isExpand, answers);
        this.question = question;
    }
}
