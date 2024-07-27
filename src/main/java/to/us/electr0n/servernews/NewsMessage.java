package to.us.electr0n.servernews;

import java.util.UUID;

public class NewsMessage {
  private UUID id;
  private String message;

  public NewsMessage(String msg) {
    message = msg;
    id = UUID.randomUUID();
  }
  public NewsMessage(String msg, UUID uuid) {
    message = msg;
    id = uuid;
  }

  public UUID getID() {
    return id;
  }
  public String getMessage() {
    return message;
  }
  public String toString() {
    return "{id: " + id + ", message: " + message + "}";
  }
}
