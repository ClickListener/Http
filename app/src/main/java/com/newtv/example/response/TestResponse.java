package com.newtv.example.response;

import java.io.Serializable;

/**
 * @author ZhangXu
 * @date 2020/11/18
 */
public class TestResponse implements Serializable {

    public String status;

    public Content content;

    public static class Content {
        public String from;
        public String to;
        public String vendor;
        public String out;
        public String ciba_use;
        public String ciba_out;
        public String err_no;

        @Override
        public String toString() {
            return "Content{" +
                    "from='" + from + '\'' +
                    ", to='" + to + '\'' +
                    ", vendor='" + vendor + '\'' +
                    ", out='" + out + '\'' +
                    ", ciba_use='" + ciba_use + '\'' +
                    ", ciba_out='" + ciba_out + '\'' +
                    ", err_no='" + err_no + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "TestResponse{" +
                "status='" + status + '\'' +
                ", content=" + content.toString() +
                '}';
    }
}
