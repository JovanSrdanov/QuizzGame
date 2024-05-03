package model;

public class User {

    public enum TypeOfUser {
        CONTESTANT,
        ADMIN
    }

    private final String username;
    private final TypeOfUser typeOfUser;
    private final int totalAnsweredQuestions;
    private final int correctAnsweredQuestions;
    private final int currentQuestionInSet;
    private final int currentSet;

    public User(String username, String userType) {
        this.username = username;
        if (userType.equals("admin")) {
            this.typeOfUser = TypeOfUser.ADMIN;
        } else {
            this.typeOfUser = TypeOfUser.CONTESTANT;
        }
        this.currentQuestionInSet = 0;
        this.totalAnsweredQuestions = 0;
        this.correctAnsweredQuestions = 0;
        this.currentSet = 0;

    }

    public User(String username, String typeOfUser, int correctAnsweredQuestions, int totalAnsweredQuestions, int currentQuestionInSet, int currentSet) {
        this.username = username;

        if (typeOfUser.equals("admin")) {
            this.typeOfUser = TypeOfUser.ADMIN;
        } else {
            this.typeOfUser = TypeOfUser.CONTESTANT;
        }
        this.totalAnsweredQuestions = totalAnsweredQuestions;
        this.correctAnsweredQuestions = correctAnsweredQuestions;
        this.currentQuestionInSet = currentQuestionInSet;
        this.currentSet = currentSet;
    }

    public String getUsername() {
        return username;
    }

    public TypeOfUser getTypeOfUser() {
        return typeOfUser;
    }

    public String getTypeOfUserToString() {
        if (this.typeOfUser == TypeOfUser.ADMIN) {
            return "admin";
        }
        return "contestant";
    }

    public int getTotalAnsweredQuestions() {
        return totalAnsweredQuestions;
    }

    public int getCorrectAnsweredQuestions() {
        return correctAnsweredQuestions;
    }

    public int getCurrentQuestionInSet() {
        return currentQuestionInSet;
    }

    public int getCurrentSet() {
        return currentSet;
    }

}
