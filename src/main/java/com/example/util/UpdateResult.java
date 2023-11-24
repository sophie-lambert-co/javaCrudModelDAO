package com.example.util;


 // Classe pour représenter le résultat d'une mise à jour
    public class UpdateResult {
        private final boolean success;
        private final String message;

        public UpdateResult(boolean success, String message) {
            this.success = success;
            this.message = message;
        }

        public boolean isSuccess() {
            return success;
        }

        public String getMessage() {
            return message;
        }
    }