package com.newtv.example.response;

import java.io.Serializable;

/**
 * @author ZhangXu
 * @date 2020/12/1
 */
public class ActiveResponse implements Serializable {

    public String statusCode;
    public Content response;

    public static class Content implements Serializable {
        public String uuid;

        @Override
        public String toString() {
            return "Content{" +
                    "uuid='" + uuid + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "ActiveResponse{" +
                "statusCode='" + statusCode + '\'' +
                ", response=" + response.toString() +
                '}';
    }
}
