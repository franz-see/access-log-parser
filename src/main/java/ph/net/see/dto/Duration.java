package ph.net.see.dto;

public enum Duration {
    Hourly("hourly"), Daily("daily");

    private final String value;

    Duration(String value) {
        this.value = value;
    }
}
