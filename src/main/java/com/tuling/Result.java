package com.tuling;

public class Result {

    private Integer return_code;
    private String return_msg;
    private String class_id;
    private String class_name;
    private Test test;

    public Result() {

    }

    public Result(Integer return_code, String return_msg, String class_id, String class_name, Test test) {
        this.return_code = return_code;
        this.return_msg = return_msg;
        this.class_id = class_id;
        this.class_name = class_name;
        this.test = test;
    }

    public Test getTest() {
        return test;
    }

    public void setTest(Test test) {
        this.test = test;
    }

    public Integer getReturn_code() {
        return return_code;
    }

    public void setReturn_code(Integer return_code) {
        this.return_code = return_code;
    }

    public String getReturn_msg() {
        return return_msg;
    }

    public void setReturn_msg(String return_msg) {
        this.return_msg = return_msg;
    }

    public String getClass_id() {
        return class_id;
    }

    public void setClass_id(String class_id) {
        this.class_id = class_id;
    }

    public String getClass_name() {
        return class_name;
    }

    public void setClass_name(String class_name) {
        this.class_name = class_name;
    }

    public static void main(String[] args) {
        Test test = new Test("zhangsan", 10);
        Result result = new Result(0, "sucess", "you class id ", "you class name", test);


//        String t = JSON.toJSONString(result);
        String t = "{\"class_id\":\"you class id \",\"class_name\":\"you class name\",\"return_code\":0,\"return_msg\":\"sucess\",\"test\":{\"age\":10,\"userName\":\"zhangsan\"}}";
        System.out.println(t);
        System.out.println(convertToJsonStr(t));

    }


    public static String convertToJsonStr(String t) {
        char[] cs = t.toCharArray();
        StringBuilder sb = new StringBuilder();
        int n = 0;
        for (char c : cs) {
            if (c == '{') {
                n++;
                sb.append("{").append("\n").append(getNTab(n));
            } else if (c == ',') {
                sb.append(",").append("\n").append(getNTab(n));
            } else if (c == '}') {
                n--;
                sb.append("\n").append(getNTab(n)).append("}");
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    public static String getNTab(int n) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < n; i++) {
            sb.append("\t");
        }
        return sb.toString();
    }
}
