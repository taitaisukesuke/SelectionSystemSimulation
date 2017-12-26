import java.util.Random;

public class Weight {
    private final int value;

    public Weight() {
        int num = new Random().nextInt(2);
        this.value = Setting.WEIGHT_NUMS[num];
    }

    public boolean evaluate(Belief belief) {
        if (this.value == belief.getValue()) {
            return true;
        } else {
            return false;
        }
    }
}
