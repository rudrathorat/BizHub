package com.bd.bizhub.ui.faq;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ExpandableListDataItems {
    public static HashMap<String, List<String>> getData() {
        HashMap<String, List<String>> expandableDetailList = new HashMap<String, List<String>>();

        List<String> whatis = new ArrayList<String>();
        whatis.add("BizHub application is an collaboration hub which takes full \n" +
                "advantage of multiple user collaboration and presents you the ideal workspace \n" +
                "for working together in real time\n");

        List<String> themes = new ArrayList<String>();
        themes.add("There are 2 themes- dark and light. The dark mode is based on marigold with black colour and the light theme is based on blue with white colour. It is suggested as a contrast to night and day mode. You can select whichever theme you prefer for the application and change anytime.\n");

        List<String> edit = new ArrayList<String>();
        edit.add("1. Select Profile tab from bottom navigation menu\n 2. Click on edit icon\n 3. Change to your desired details\n 4. Click Submit\n");

        List<String> view = new ArrayList<String>();
        view.add("The home page consists of 4 cards which are named respectively according to the features. You can select the Workspace to view your projects. Similarly, there are 4 tabs in the bottom navigation menu, you can select one option accordingly.\n");

        List<String> set = new ArrayList<String>();
        set.add("1. Click on the project in which you want to add task \n2. Click on the “+” at the bottom of the page \n3. Set up necessary information such as name, description \n4. Click “OK” to finish\n");

        List<String> delete = new ArrayList<String>();
        delete.add("You just have to swipe left/ right on the Project you want to delete. It’s as easy as pie! \n");

        expandableDetailList.put("What is BizHub", whatis);
        expandableDetailList.put("What are themes?", themes);
        expandableDetailList.put("Where do I see my Projects?", view);
        expandableDetailList.put("How do I add Members", edit);
        expandableDetailList.put("How to set up Tasks?", set);
        expandableDetailList.put("How do I delete a Project?", delete);

        return expandableDetailList;
    }
}

