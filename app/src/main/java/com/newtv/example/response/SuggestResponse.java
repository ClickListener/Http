package com.newtv.example.response;

import java.io.Serializable;
import java.util.List;

/**
 * @author ZhangXu
 * @date 2020/11/10
 */
public class SuggestResponse implements Serializable {

    public int errorCode;
    public String errorMessage;

    public List<Param> data;

    public int total;


    public static class Param implements Serializable{
        public float grade;
        public String contentId;
        public String contentType;
        public int vipFlag;
        public int drm;
        public int movieLevel;
        public String recentMsg;
        public int realExclusive;
        public String title;
        public String vImage;
        public String hImage;
        public String contentUUID;

        @Override
        public String toString() {
            return "Param{" +
                    "grade=" + grade +
                    ", contentId='" + contentId + '\'' +
                    ", contentType='" + contentType + '\'' +
                    ", vipFlag=" + vipFlag +
                    ", drm=" + drm +
                    ", movieLevel=" + movieLevel +
                    ", recentMsg='" + recentMsg + '\'' +
                    ", realExclusive=" + realExclusive +
                    ", title='" + title + '\'' +
                    ", vImage='" + vImage + '\'' +
                    ", hImage='" + hImage + '\'' +
                    ", contentUUID='" + contentUUID + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "SuggestResponse{" +
                "errorCode=" + errorCode +
                ", errorMessage='" + errorMessage + '\'' +
                ", data=" + data.toString() +
                ", total=" + total +
                '}';
    }
}
