package model;

public class User {

    private final String username;
    private final String password;
    private final TypeOfUser typeOfUser;
    private int totalAnsweredQuestions;
    private int correctAnsweredQuestions;
    private int currentQuestionInSet;
    private int currentSet;

    public enum TypeOfUser {
        CONTESTANT,
        ADMIN
    }

    public User(String username, String password, String typeOfUser, int correctAnsweredQuestions, int totalAnsweredQuestions, int currentQuestionInSet, int currentSet) {
        this.username = username;
        this.password = password;
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

    public String getPassword() {
        return password;
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

    public void setCurrentSet(int currentSet) {
        this.currentSet = currentSet;
    }

    public void setTotalAnsweredQuestions(int totalAnsweredQuestions) {
        this.totalAnsweredQuestions = totalAnsweredQuestions;
    }

    public void setCorrectAnsweredQuestions(int correctAnsweredQuestions) {
        this.correctAnsweredQuestions = correctAnsweredQuestions;
    }

    public void setCurrentQuestionInSet(int currentQuestionInSet) {
        this.currentQuestionInSet = currentQuestionInSet;
    }

}
