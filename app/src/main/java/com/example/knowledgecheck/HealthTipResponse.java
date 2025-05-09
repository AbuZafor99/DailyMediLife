package com.example.knowledgecheck;


public class HealthTipResponse {
    private Slip slip;

    public Slip getSlip() {
        return slip;
    }

    public static class Slip {
        private String advice;

        public String getAdvice() {
            return advice;
        }
    }
}
