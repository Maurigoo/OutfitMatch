package com.example.outfitmatch;

import com.google.gson.annotations.SerializedName;

public class Clima {
    @SerializedName("location")
    private Location location;

    @SerializedName("current")
    private Current current;

    public Location getLocation() {
        return location;
    }

    public Current getCurrent() {
        return current;
    }

    public static class Location {
        @SerializedName("name")
        private String name;

        @SerializedName("country")
        private String country;

        public String getName() {
            return name;
        }

        public String getCountry() {
            return country;
        }
    }

    public static class Current {
        @SerializedName("temp_c")
        private float temp_c;

        @SerializedName("temp_f")
        private float temp_f;

        @SerializedName("condition")
        private Condition condition;

        public float getTemp_c() {
            return temp_c;
        }

        public float getTemp_f() {
            return temp_f;
        }

        public Condition getCondition() {
            return condition;
        }
    }

    public static class Condition {
        @SerializedName("text")
        private String text;

        public String getText() {
            return text;
        }
    }
}
