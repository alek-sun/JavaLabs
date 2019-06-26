package ru.nsu.fit.fedyaeva.template;

import ru.nsu.fit.fedyaeva.template.processors.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Template {
    private String text;

    Template(String txt) {
        text = txt;
    }

    String fill(Map<String, String> values, Map<String, Boolean> conditions, Map<String, Integer> iterations) throws IncorrectFormException, ArgumentNotFoundException {
        Parser p = new Parser();
        HashMap<String, String> typeStr = new HashMap<>();  //  map <expression, type of expression>
        ArrayList<String> parseRes = p.parse(text, typeStr);
        ArrayList<TemplateProcessor> callList = createTemplate(parseRes, typeStr);  //  processor list
        StringBuilder resultStr = new StringBuilder();
        int i;
        for (i = 0; i < callList.size(); ++i) {
            callList.get(i).fillTemplate(resultStr, values, conditions, iterations);
        }
        return resultStr.toString();
    }

    //Get TemplateProcessor list
    private ArrayList<TemplateProcessor> createTemplate(ArrayList<String> parseRes, HashMap<String, String> typeStr) {
        ArrayList<TemplateProcessor> callList = new ArrayList<>();
        int i;
        for (i = 0; i < parseRes.size(); ++i) {
            String curStr = parseRes.get(i);
            StringBuilder curBuilder = new StringBuilder(curStr);
            switch (typeStr.get(curStr)) {
                case "IF": {
                    String key = getKey(curBuilder, "if");
                    ConditionProcessor valPr = new ConditionProcessor(key, curBuilder.toString());
                    callList.add(valPr);
                    break;
                }
                case "REPEAT": {
                    String key = getKey(curBuilder, "repeat");
                    RepeatProcessor repPr = new RepeatProcessor(key, curBuilder.toString());
                    callList.add(repPr);
                    break;
                }
                case "VALUE": {   //  if not a single '!'
                    ValueProcessor valPr = new ValueProcessor(curStr);
                    callList.add(valPr);
                    break;
                }
                default:
                    StringProcessor strPr = new StringProcessor(curStr);
                    callList.add(strPr);
                    break;
            }
        }
        return callList;
    }

    //Get key and next string
    private String getKey(StringBuilder sb, String whatKey) {
        String s = sb.toString();
        s = s.replaceFirst("!" + whatKey, "");     //  delete special symbols
        s = s.replace("!end" + whatKey + "!", "");
        String[] keyAndVal = s.split("!", 2);
        String key = keyAndVal[0];
        if (key.charAt(0) == ' ') {  //  delete first ' '
            key = key.replaceFirst(" ", "");
        }
        sb = sb.delete(0, sb.length());    // clear start string
        sb.append(keyAndVal[1]);   //  add value
        return key;
    }
}
