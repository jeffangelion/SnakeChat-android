package tk.jenrus.lamia;

class Message {

    private String nickname;
    //TODO: Добавить передачу бинарных файлов
    private String message ;

    Message(String nickname, String message) {
        this.nickname = nickname;
        this.message = message;
    }

    String getNickname() {
        return nickname;
    }

    String getMessage() {
        return message;
    }
}
