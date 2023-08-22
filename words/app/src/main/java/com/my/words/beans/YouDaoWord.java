package com.my.words.beans;

import java.util.List;

public class YouDaoWord extends BaseResponse {
    private DataDTO data;
    public DataDTO getData() {
        return data;
    }

    public static class DataDTO {
        private List<EntriesDTO> entries;

        public String getQuery() {
            return query;
        }

        private String query;
        private String language;
        private String type;

        public List<EntriesDTO> getEntries() {
            return entries;
        }

        public static class EntriesDTO {
            private String explain;
            private String entry;

            public String getExplain() {
                return explain;
            }

            public String getEntry() {
                return entry;
            }
        }
    }
}
