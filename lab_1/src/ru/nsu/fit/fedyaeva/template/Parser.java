package ru.nsu.fit.fedyaeva.template;

import java.util.ArrayList;
import java.util.HashMap;

public class Parser {

    ArrayList<String> parse(String inputStr, HashMap<String, String> typeStr) throws IncorrectFormException {
        if (!isCorrect(inputStr)) { //  check count of % and correctness of name
            throw new IncorrectFormException("Not closed % or incorrect character in name");
        }
        ArrayList<String> splitRes = getStrList(inputStr);
        ArrayList<String> readyForTemp = new ArrayList<>();
        int i, count = 0;
        for (i = 0; i < splitRes.size(); ++i) {
            StringBuilder sb = new StringBuilder(splitRes.get(i));
            if (splitRes.get(i).equals("")) {
                count++;
                continue;
            }
            if (count % 2 == 0) {     //  just some string
                i = uniteStr(sb, i, splitRes);   //  unite strings breaking by %, returns index of last added string
                typeStr.put(sb.toString(), "STRING");
            } else {    //  some expression
                if (splitRes.get(i).contains("!if")) {
                    i = prepareNextStr(i, splitRes, sb, "!endif!");
                    typeStr.put(sb.toString(), "IF");
                } else if (splitRes.get(i).contains("!repeat")) {
                    i = prepareNextStr(i, splitRes, sb, "!endrepeat!");
                    typeStr.put(sb.toString(), "REPEAT");
                } else {
                    typeStr.put(sb.toString(), "VALUE");
                }
            }
            readyForTemp.add(sb.toString());   //  add current string to result
            count++;
        }
        return readyForTemp;
    }

    //Unites strings, breaking by % and changes index to last index of next string
    //Throws exception if "repeat" or "if" isn't closed
    private int prepareNextStr(int i, ArrayList<String> strList, StringBuilder sb, String options) throws IncorrectFormException {
        StringBuilder nextStr = new StringBuilder(strList.get(i + 1));
        i = uniteStr(nextStr, i + 1, strList);
        if (!strList.get(i + 1).equals(options)) {
            throw new IncorrectFormException("Not closed " + options);
        }
        sb.append(nextStr);
        i++;    //  step to %!endoption!%
        sb.append(strList.get(i));
        return i;
    }

    //Unite strings, breaking by %
    private int uniteStr(StringBuilder sb, int i, ArrayList<String> v) {
        while (sb.charAt(sb.length() - 1) == '%') {
            i++;
            sb.append(v.get(i));
        }
        return i;
    }

    //Split string by %, return deleted \% as %
    private ArrayList<String> getStrList(String text) {
        String[] buf = text.split("%");
        int i;
        for (i = 0; i < buf.length; ++i) {     //  replace \% on %
            String curStr = buf[i];
            if (!curStr.isEmpty() && curStr.charAt(curStr.length() - 1) == '\\') {
                StringBuilder b = new StringBuilder(buf[i]);
                b.replace(b.length() - 1, b.length(), "%");
                buf[i] = b.toString();
            }
        }
        ArrayList<String> splitRes = new ArrayList<>(buf.length);
        for (i = 0; i < buf.length; ++i) {
            splitRes.add(i, buf[i]);
        }
        return splitRes;
    }

    private boolean isCorrect(String txt) {
        return countOfSubstr("\\%", txt) / 2 % 2 == countOfSubstr("%", txt) % 2;
    }

    private int countOfSubstr(String countOfWhat, String text) {
        String buf = text.replace(countOfWhat, "");
        return text.length() - buf.length();
    }
}
